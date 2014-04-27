package gate.compound.gui;

import gate.Document;
import gate.Factory;
import gate.Gate;
import gate.Main;
import gate.Resource;
import gate.alignment.gui.AlignmentEditor;
import gate.compound.CompoundDocument;
import gate.compound.CompoundDocumentEvent;
import gate.compound.CompoundDocumentListener;
import gate.compound.impl.AbstractCompoundDocument;
import gate.corpora.DocumentImpl;
import gate.creole.AbstractVisualResource;
import gate.creole.ResourceInstantiationException;
import gate.event.ProgressListener;
import gate.gui.ActionsPublisher;
import gate.gui.Handle;
import gate.gui.MainFrame;
import gate.gui.NameBearerHandle;
import gate.util.GateException;
import gate.util.GateRuntimeException;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import javax.swing.AbstractAction;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;

/**
 * This is an extension of the GATE Document viewer/editor. This class
 * provides the implementation for CompoundDocument Editor. Compound
 * document is a set of multiple documents. this class simply wraps all
 * document editors for all member documents of the compound document under
 * a single component.
 */

public class CompoundDocumentEditor extends AbstractVisualResource
                                                                  implements
                                                                  ActionsPublisher,
                                                                  ProgressListener,
                                                                  CompoundDocumentListener {

  private static final long serialVersionUID = -7623216613025540025L;

  /**
   * Tabbed pane for showing document members.
   */
  private JTabbedPane tabbedPane;

  /**
   * document id to document editor map
   */
  private HashMap<String, Handle> documentsMap;
  
  /**
   * Set of alignment editors created
   */
  private Set<AlignmentEditor> alignmentEditors;
  
  /**
   * Toolbar containing various actions
   */
  protected JToolBar toolbar;

  /**
   * The document view is just an empty shell. This method publishes the
   * actions from the contained views.
   */
  public List getActions() {
    List actions = new ArrayList();
    return actions;
  }

  /*
   * (non-Javadoc)
   * 
   * @see gate.Resource#init()
   */
  public Resource init() throws ResourceInstantiationException {
    alignmentEditors = new HashSet<AlignmentEditor>();
    documentsMap = new HashMap<String, Handle>();
    tabbedPane = new JTabbedPane();
    toolbar = new JToolBar();
    toolbar.add(new NewDocumentAction());
    toolbar.add(new RemoveDocumentsAction());
    toolbar.addSeparator();
    toolbar.add(new SaveAllDocuments());
    toolbar.add(new SaveAsASingleXML());
    toolbar.addSeparator();
    toolbar.add(new SwitchDocument());
    toolbar.addSeparator();
    toolbar.add(new ShowAlignmentEditorAction());

    this.setLayout(new java.awt.BorderLayout());
    this.add(tabbedPane, java.awt.BorderLayout.CENTER);
    this.add(toolbar, BorderLayout.NORTH);
    return this;
  }

  /*
   * (non-Javadoc)
   * 
   * @see gate.VisualResource#setTarget(java.lang.Object)
   */
  public void setTarget(Object target) {
    this.document = (Document)target;
  }

  /**
   * Used by the main GUI to tell this VR what handle created it. The
   * VRs can use this information e.g. to add items to the popup for the
   * resource.
   */
  public void setHandle(Handle handle) {
    super.setHandle(handle);
    Map documents = ((CompoundDocument)this.document).getDocuments();
    ((CompoundDocument)this.document).addCompoundDocumentListener(this);

    Iterator iter = documents.values().iterator();
    try {
      while(iter.hasNext()) {
        Document doc = (Document)iter.next();
        NameBearerHandle nbHandle = new NameBearerHandle(doc, Main
                .getMainFrame());
        JComponent largeView = nbHandle.getLargeView();
        if(largeView != null) {
          tabbedPane.addTab(nbHandle.getTitle(), nbHandle.getIcon(), largeView,
                  nbHandle.getTooltipText());
          documentsMap.put(doc.getName(), nbHandle);

        }
      }
    }
    catch(Exception e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Allows adding an existing GATE document as a member to the compound document 
   * @author niraj
   *
   */
  class NewDocumentAction extends AbstractAction {
    public NewDocumentAction() {
      super("Add");
      putValue(SHORT_DESCRIPTION,
              "Add new document(s) to this compound document");
    }

    public void actionPerformed(ActionEvent e) {
      try {
        // get all the documents loaded in the system
        java.util.List loadedDocuments = Gate.getCreoleRegister()
                .getAllInstances("gate.Document");
        if(loadedDocuments == null || loadedDocuments.isEmpty()) {
          JOptionPane.showMessageDialog(CompoundDocumentEditor.this,
                  "There are no documents available in the system.\n"
                          + "Please load some and try again.", "GATE",
                  JOptionPane.ERROR_MESSAGE);
          return;
        }

        Vector docNames = new Vector();
        for(int i = 0; i < loadedDocuments.size(); i++) {
          Document doc = (Document)loadedDocuments.get(i);
          if(doc instanceof CompoundDocument) {
            loadedDocuments.remove(i);
            i--;
            continue;
          }
          docNames.add(doc.getName());
        }

        JList docList = new JList(docNames);
        JOptionPane dialog = new JOptionPane(new JScrollPane(docList),
                JOptionPane.QUESTION_MESSAGE, JOptionPane.OK_CANCEL_OPTION);
        dialog.createDialog(CompoundDocumentEditor.this,
                "Add document(s) to compound document").setVisible(true);

        if(((Integer)dialog.getValue()).intValue() == JOptionPane.OK_OPTION) {
          int[] selection = docList.getSelectedIndices();
          for(int i = 0; i < selection.length; i++) {
            Document doc = (Document)loadedDocuments.get(selection[i]);
            ((CompoundDocument)document).addDocument(doc.getName(), doc);
          }
        }
      }
      catch(GateException ge) {
        // gate.Document is not registered in creole.xml....what is!?
        throw new GateRuntimeException(
                "gate.Document is not registered in the creole register!\n"
                        + "Something must be terribly wrong...take a vacation!");
      }
    }
  }

  /**
   * Creating new instance of alignment editor.
   * @author niraj
   *
   */
  class ShowAlignmentEditorAction extends AbstractAction {
    public ShowAlignmentEditorAction() {
      super("Alignment Editor");
      putValue(SHORT_DESCRIPTION, "Brings up new Alignment editor");
    }

    public void actionPerformed(ActionEvent e) {
      AlignmentEditor editor = new AlignmentEditor(((CompoundDocument)document));
      alignmentEditors.add(editor);
    }
  }

  /**
   * Action to allow deletion of a member from the compound document
   * @author niraj
   *
   */
  class RemoveDocumentsAction extends AbstractAction {
    public RemoveDocumentsAction() {
      super("Remove");
      putValue(SHORT_DESCRIPTION,
              "Removes selected document(s) from this corpus");
    }

    public void actionPerformed(ActionEvent e) {
      if(tabbedPane.getSelectedIndex() >= 0) {
        String docName = tabbedPane.getTitleAt(tabbedPane.getSelectedIndex());
        ((CompoundDocument)document).removeDocument(docName);
      }
    }
  }

  /**
   * All documents are saved in individual xml files.
   * @author niraj
   *
   */
  class SaveAllDocuments extends AbstractAction {

    private static final long serialVersionUID = -1377052643002026640L;

    public SaveAllDocuments() {
      super("Save");
      putValue(SHORT_DESCRIPTION,
              "Saves all member documents in individual XML files");
    }

    public void actionPerformed(ActionEvent ae) {
      CompoundDocument cd = (CompoundDocument)document;
      JFileChooser fileChooser = new JFileChooser();
      fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
      try {
        fileChooser.showSaveDialog(Main.getMainFrame());
        File dir = null;
        if((dir = fileChooser.getSelectedFile()) == null) {
          return;
        }

        List<String> docIDs = cd.getDocumentIDs();
        for(int i = 0; i < docIDs.size(); i++) {
          Document doc = cd.getDocument(docIDs.get(i));
          File file = null;
          if(doc.getName().equals("Composite")) {
            file = new File(dir.getAbsolutePath() + "/Composite.xml");
          }
          else {
            file = new File(doc.getSourceUrl().getFile());
            file = new File(dir.getAbsolutePath() + "/" + file.getName());
          }

          BufferedWriter bw = new BufferedWriter(
                  new OutputStreamWriter(new FileOutputStream(file),
                          ((DocumentImpl)doc).getEncoding()));
          bw.write(doc.toXml());
          bw.flush();
          bw.close();
        }
      }
      catch(Exception e) {
        e.printStackTrace();
      }
    }
  }

  /**
   * Action to save all the member documents in a single xml file.
   * @author niraj
   *
   */
  public class SaveAsASingleXML extends AbstractAction {

    private static final long serialVersionUID = -1377052643002026640L;

    public SaveAsASingleXML() {
      super("Save As XML");
      putValue(SHORT_DESCRIPTION, "Saves all documents in a single XML file");

    }

    public void actionPerformed(ActionEvent ae) {
      CompoundDocument cd = (CompoundDocument)document;

      JFileChooser fileChooser = MainFrame.getFileChooser();
      fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
      try {
        fileChooser.showSaveDialog(Main.getMainFrame());
        File fileToSaveIn = null;
        if((fileToSaveIn = fileChooser.getSelectedFile()) == null) {
          return;
        }

        String xml = AbstractCompoundDocument.toXmlAsASingleDocument(cd);
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(fileToSaveIn), cd.getEncoding()));
        bw.write(xml);
        bw.flush();
        bw.close();
      }
      catch(Exception e) {
        e.printStackTrace();
      }
    }
  }

  /**
   * Action that allows users to switch the focus to one of the member documents
   * @author niraj
   *
   */
  class SwitchDocument extends AbstractAction {

    private static final long serialVersionUID = -1377052643002026640L;

    /**
     * Constructor
     */
    public SwitchDocument() {
      super("Switch");
      putValue(SHORT_DESCRIPTION,
              "Allows setting focus of the compound document to one of"
                      + " its member documents");

    }

    public void actionPerformed(ActionEvent ae) {
      CompoundDocument cd = (CompoundDocument)document;
      List<String> docIDs = cd.getDocumentIDs();
      JComboBox box = new JComboBox(docIDs.toArray());
      Object[] options = {"OK", "CANCEL"};
      int reply = JOptionPane.showOptionDialog(MainFrame.getInstance(), box,
              "Select the document ID to switch to...",
              JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null,
              options, options[0]);
      if(reply == JOptionPane.OK_OPTION) {
        String documentID = (String)box.getSelectedItem();
        ((CompoundDocument)document).setCurrentDocument(documentID);
      }
    }
  } //SwitchDocument

  protected Document document;

  public void processFinished() {
    ((CompoundDocument)this.document).setCurrentDocument(null);
  }

  public void progressChanged(int prgress) {

  }

  public void documentAdded(CompoundDocumentEvent event) {
    try {
      Document doc = event.getSource().getDocument(event.getDocumentID());
      final NameBearerHandle nbHandle = new NameBearerHandle(doc, Main
              .getMainFrame());
      final JComponent largeView = nbHandle.getLargeView();
      if(largeView != null) {
        SwingUtilities.invokeLater(new Runnable() {
          public void run() {
            tabbedPane.addTab(nbHandle.getTitle(), nbHandle.getIcon(),
                    largeView, nbHandle.getTooltipText());
          }
        });
        documentsMap.put(doc.getName(), nbHandle);
      }

      tabbedPane.updateUI();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  public void documentRemoved(CompoundDocumentEvent event) {
    Handle handle = (Handle)documentsMap.remove(event.getDocumentID());
    if(handle != null) {
      tabbedPane.remove(handle.getLargeView());
      tabbedPane.updateUI();
      handle.cleanup();
      Document doc = event.getSource().getDocument(event.getDocumentID());
      if(Gate.getHiddenAttribute(doc.getFeatures())) {
        Factory.deleteResource(event.getSource().getDocument(
                event.getDocumentID()));
      }
    }
  }

  public void cleanup() {
    super.cleanup();
    
    // close all open alignment editors
    for(AlignmentEditor editor : alignmentEditors) {
      // dispose will clean up the editor internally
      editor.dispose();
    }
    /*
     * close all documents as well
     */
    for(Handle h : documentsMap.values()) {
      tabbedPane.remove(h.getLargeView());
      h.cleanup();
    }
    
  }
}
