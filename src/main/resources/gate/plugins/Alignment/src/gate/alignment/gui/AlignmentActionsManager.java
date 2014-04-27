package gate.alignment.gui;

import gate.Annotation;
import gate.Document;
import gate.Gate;
import gate.alignment.Alignment;
import gate.alignment.AlignmentActionInitializationException;
import gate.alignment.AlignmentException;
import gate.alignment.gui.actions.impl.AlignAction;
import gate.alignment.gui.actions.impl.RemoveAlignmentAction;
import gate.alignment.gui.actions.impl.ResetAction;
import gate.compound.CompoundDocument;
import gate.compound.impl.CompoundDocumentImpl;
import gate.creole.ResourceData;
import gate.swing.XJTable;
import gate.util.GateRuntimeException;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;

/**
 * Alignment actions manager that allows managing actions that should be
 * taken on various occasions.
 * @author niraj
 *
 */
public class AlignmentActionsManager extends JPanel {

  /**
   * a list of alignment actions available to the user
   */
  private List<AlignmentAction> allActions;

  /**
   * mappings for menu item and associated alignment action
   */
  protected Map<JMenuItem, AlignmentAction> actions;

  /**
   * default align action - i.e. what happens when user clicks on the align
   * button
   */
  private AlignAction alignAction = null;

  /**
   * mappings for alignment actions and its respective check box - which if
   * checked, indicates that the respective alignment action should be executed.
   */
  private HashMap<AlignmentAction, PropertyActionCB> actionsCBMap = null;

  /**
   * default unalign action - i.e. what happens when user clicks on the unalign
   * button
   */
  private RemoveAlignmentAction removeAlignmentAction = null;

  /**
   * list of actions that should be executed before a pair is displayed on the
   * screen.
   */
  private List<PreDisplayAction> preDisplayActions = null;

  /**
   * list of actions that should be executed after a user has indicated that the
   * alignment for the given pair is finished.
   */
  private List<FinishedAlignmentAction> finishedAlignmentActions = null;

  /**
   * DataPublishersActions - actions have data to publish
   */
  private List<DataPublisherAction> dataPublisherActions = null;

  /**
   * mappings for menu items and their captions
   */
  private Map<String, JMenuItem> actionsMenuItemByCaption;

  /**
   * properties panel that shows various options available to user when aligning
   */
  private JPanel propertiesPanel;

  /**
   * holds tabbed panes
   */
  private JTabbedPane tableTabbedPane;

  AlignmentTask alignmentTask;

  CompoundDocument document;

  Document srcDocument;

  Document tgtDocument;

  String srcASName;

  String tgtASName;

  Alignment alignment;

  public AlignmentActionsManager(AlignmentTask alignmentTask,
          String actionsFilePath) {
    propertiesPanel = new JPanel();
    propertiesPanel.setLayout(new BoxLayout(propertiesPanel, BoxLayout.Y_AXIS));
    JScrollPane propertiesPane = new JScrollPane(propertiesPanel);
    propertiesPanel.add(new JLabel("Options"));
    propertiesPanel.add(Box.createGlue());

    setLayout(new BorderLayout());
    JPanel centralPanel = new JPanel(new GridLayout(1, 2));
    centralPanel.add(propertiesPane);
    tableTabbedPane = new JTabbedPane();
    JScrollPane tableTabbedScroller = new JScrollPane(tableTabbedPane);
    centralPanel.add(tableTabbedScroller);
    add(centralPanel, BorderLayout.CENTER);

    this.setPreferredSize(new Dimension(400, 200));

    actions = new HashMap<JMenuItem, AlignmentAction>();
    actionsMenuItemByCaption = new HashMap<String, JMenuItem>();
    allActions = new ArrayList<AlignmentAction>();
    preDisplayActions = new ArrayList<PreDisplayAction>();
    finishedAlignmentActions = new ArrayList<FinishedAlignmentAction>();
    dataPublisherActions = new ArrayList<DataPublisherAction>();
    actionsCBMap = new HashMap<AlignmentAction, PropertyActionCB>();

    setBorder(BorderFactory.createTitledBorder("Actions"));
    readAction(new ResetAction());
    alignAction = new AlignAction();
    readAction(alignAction);
    removeAlignmentAction = new RemoveAlignmentAction();
    readAction(removeAlignmentAction);

    // read the provided actions file
    if(actionsFilePath != null) {
      readActions(new File(actionsFilePath));
    } // read

    // default actions conf file
    ResourceData myResourceData =
            (ResourceData)Gate.getCreoleRegister().get(
                    CompoundDocumentImpl.class.getName());
    URL creoleXml = myResourceData.getXmlFileUrl();
    URL alignmentHomeURL = null;

    // read actions file
    File actionsConfFile = null;
    try {
      alignmentHomeURL = new URL(creoleXml, ".");

      // loading the default actions config file.
      actionsConfFile =
              new File(
                      new File(new File(alignmentHomeURL.toURI()), "resources"),
                      "actions.conf");
      readActions(actionsConfFile);
    } catch(MalformedURLException mue) {
      throw new GateRuntimeException(mue);
    } catch(URISyntaxException use) {
      throw new GateRuntimeException(use);
    }

    setTarget(alignmentTask);
  } // initActions

  /**
   * This method reads the given action and decides whether it should be added
   * to the properties panel or not. It also adds it to the appropriate local
   * data structure in order to invoke them when appropriate.
   * 
   * @param action
   */
  private void readAction(AlignmentAction action) {

    // indicates if this action should be added to the menu
    boolean addToMenu = true;

    if(action.invokeWithAlignAction()) {
      allActions.add(action);
      addToMenu = false;
    }

    if(action.invokeWithRemoveAction()) {
      if(!allActions.contains(action)) {
        allActions.add(action);
      }
      addToMenu = false;
    }

    String caption = action.getCaption();
    Icon icon = action.getIcon();

    if(addToMenu) {

      final JMenuItem menuItem;
      if(icon != null) {
        menuItem = new JMenuItem(caption, icon);
        JMenuItem actionItem =
                actionsMenuItemByCaption.get(action.getIconPath());
        if(actionItem != null) {
          actions.remove(actionItem);
          actionsMenuItemByCaption.remove(action.getIconPath());
        }
        actionsMenuItemByCaption.put(action.getIconPath(), menuItem);
      } else {
        menuItem = new JMenuItem(caption);
        JMenuItem actionItem = actionsMenuItemByCaption.get(caption);
        if(actionItem != null) {
          actions.remove(actionItem);
          actionsMenuItemByCaption.remove(caption);
        }
        actionsMenuItemByCaption.put(caption, menuItem);
      }

      menuItem.setToolTipText(action.getToolTip());
      actions.put(menuItem, action);
    }
  }

  class CustomJMenuItem extends JMenuItem {

    AlignmentView alignmentView;

    JMenuItem item;

    public CustomJMenuItem(AlignmentView alignmentView, JMenuItem item,
            String caption) {
      super(caption);
      this.alignmentView = alignmentView;
      this.item = item;
      initListener();
    }

    public CustomJMenuItem(AlignmentView alignmentView, JMenuItem item,
            String caption, Icon icon) {
      super(caption, icon);
      this.alignmentView = alignmentView;
      this.item = item;
      initListener();
    }

    public void initListener() {
      addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent ae) {
          alignmentView.executeAction(actions.get(item));
        }
      });
    }
  }

  /**
   * Prepares option menu
   * 
   * @param aligned
   * @param highlighted
   * @return
   */
  public JPopupMenu prepareOptionsMenu(final AlignmentView alignmentView,
          boolean aligned, boolean highlighted) {
    JPopupMenu optionsMenu = new JPopupMenu();
    optionsMenu.setOpaque(true);
    optionsMenu.add(new JLabel("Options"));
    optionsMenu.addSeparator();

    for(final JMenuItem item : actions.keySet()) {

      CustomJMenuItem cmi = null;
      if(item.getIcon() != null) {
        cmi =
                new CustomJMenuItem(alignmentView, item, item.getText(), item
                        .getIcon());
      } else {
        cmi = new CustomJMenuItem(alignmentView, item, item.getText());
      }

      AlignmentAction aa = actions.get(item);
      if(aligned) {
        if(aa.invokeForAlignedAnnotation()) {
          optionsMenu.add(cmi);
        }
      } else if(highlighted) {
        if(aa.invokeForHighlightedUnalignedAnnotation()) {
          optionsMenu.add(cmi);
        }
      } else {
        if(aa.invokeForUnhighlightedUnalignedAnnotation()) {
          optionsMenu.add(cmi);
        }
      }
    }
    return optionsMenu;
  }

  public void setTarget(AlignmentTask alignmentTask) {
    this.alignmentTask = alignmentTask;
    this.document = alignmentTask.getCompoundDocument();
    srcDocument = document.getDocument(alignmentTask.getSrcDocId());
    tgtDocument = document.getDocument(alignmentTask.getTgtDocId());
    srcASName = alignmentTask.getSrcASName();
    tgtASName = alignmentTask.getTgtASName();
    alignment =
            document.getAlignmentInformation(alignmentTask.getUaFeatureName());
  }

  /**
   * if user says that the pair is completely aligned, this method is invoked,
   * which retrieves a list of FinishedAlignmentActions and calls them one by
   * one.
   */
  public void executeFinishedAlignmentActions(PUAPair pair) {

    for(FinishedAlignmentAction faa : finishedAlignmentActions) {
      try {
        faa.executeFinishedAlignmentAction(pair);
      } catch(AlignmentException ae) {
        throw new GateRuntimeException(ae);
      }
    }
  }

  /**
   * Execute Pre-display actions
   * 
   * @param pair
   */
  public void executePreDisplayActions(PUAPair pair) {
    // only if the pair is not marked as finished alignment
    if(pair.isAlignmentFinished()) return;
    
    // before showing, lets execute preDisplayActions
    for(PreDisplayAction pda : preDisplayActions) {
      try {
        pda.executePreDisplayAction(pair);
      } catch(AlignmentException ae) {
        throw new GateRuntimeException(ae);
      }
    }
  }

  /**
   * Executes the given action. It uses the pair that is being currently shown
   * to collect the alignment information which is then used as parameters to
   * call the provided action.
   * 
   * @param aa
   */
  public void executeAction(AlignmentView alignmentView, AlignmentAction aa,
          Set<Annotation> srcSelectedAnnots, Set<Annotation> tgtSelectedAnnots,
          Annotation clickedAnnotation) {

    try {
      aa.executeAlignmentAction(alignmentView, alignmentTask,
              srcSelectedAnnots, tgtSelectedAnnots, clickedAnnotation);
      
      // set the alignment finished to false
      alignmentTask.current().setAlignmentFinished(false);

      if(aa == alignAction) {
        for(AlignmentAction a : allActions) {
          if(a.invokeWithAlignAction()) {
            JCheckBox cb = actionsCBMap.get(a);
            if((cb != null && cb.isSelected()) || cb == null)
              a.executeAlignmentAction(alignmentView, alignmentTask,
                      srcSelectedAnnots, tgtSelectedAnnots, clickedAnnotation);

          }

        }
      } else if(aa == removeAlignmentAction) {
        for(AlignmentAction a : allActions) {
          if(a.invokeWithRemoveAction()) {
            JCheckBox cb = actionsCBMap.get(a);
            if((cb != null && cb.isSelected()) || cb == null)

              a.executeAlignmentAction(alignmentView, alignmentTask,
                      srcSelectedAnnots, tgtSelectedAnnots, clickedAnnotation);
          }
        }
      }
    } catch(AlignmentException ae) {
      throw new GateRuntimeException(ae);
    }
  }

  /**
   * Reads different actions from the actions configuration file.
   * 
   * @param actionsConfFile
   */
  private void readActions(File actionsConfFile) {

    if(actionsConfFile != null && actionsConfFile.exists()) {
      try {
        BufferedReader br = new BufferedReader(new FileReader(actionsConfFile));
        String line = br.readLine();
        String cName = "";
        while(line != null) {
          // each line will have a class name
          // System.out.println(line);

          try {
            if(line.trim().startsWith("#") || line.trim().length() == 0) {
              continue;
            }

            int index = line.indexOf(",");
            cName = index < 0 ? line.trim() : line.substring(0, index);
            line = index < 0 ? "" : line.substring(index + 1);

            Class actionClass =
                    Class.forName(cName, true, Gate.getClassLoader());

            Object action = actionClass.newInstance();
            String[] args = line.split("[,]");

            // replace args with relative paths
            String relPath = actionsConfFile.getParentFile().getAbsolutePath();
            if(!relPath.endsWith("/")) {
              relPath += "/";
            }

            for(int i = 0; i < args.length; i++) {
              args[i] = args[i].replaceAll("(\\$relpath\\$)", relPath);
              // System.out.println(args[i]);
            } // for

            if(action instanceof AlignmentAction) {
              loadAlignmentAction((AlignmentAction)action, args);
            }

            if(action instanceof PreDisplayAction) {
              loadPreDisplayAction((PreDisplayAction)action, args);
            }

            if(action instanceof FinishedAlignmentAction) {
              loadFinishedAlignmentAction((FinishedAlignmentAction)action, args);
            }

            if(action instanceof DataPublisherAction) {
              loadDataPublisherAction((DataPublisherAction)action, args);
            }

          } catch(ClassNotFoundException cnfe) {
            System.err.println("class " + cName + " not found!");
            continue;
          } catch(IllegalAccessException ilae) {
            System.err.println("class " + cName
                    + " threw the illegal access exception!");
            continue;
          } catch(InstantiationException ie) {
            System.err.println("class " + cName + " could not be instantiated");
            continue;
          } finally {
            line = br.readLine();
          }
        }
      } catch(IOException ioe) {
        throw new GateRuntimeException(ioe);
      }
    }
  }

  private void loadAlignmentAction(AlignmentAction aa, String[] args) {
    try {
      aa.init(args);
    } catch(AlignmentActionInitializationException aaie) {
      throw new GateRuntimeException(aaie);
    }

    readAction(aa);
    if(aa.invokeWithAlignAction() || aa.invokeWithRemoveAction()) {
      String title = aa.getCaption();
      if(title == null || title.trim().length() == 0) return;
      PropertyActionCB pab = new PropertyActionCB(title, false, aa);
      pab.setToolTipText(aa.getToolTip());
      actionsCBMap.put(aa, pab);
      int count = propertiesPanel.getComponentCount();
      propertiesPanel.add(pab, count - 1);
      propertiesPanel.validate();
      propertiesPanel.updateUI();
    }
  }

  /**
   * load a finished alignment action
   * 
   * @param faa
   * @param args
   */
  private void loadFinishedAlignmentAction(FinishedAlignmentAction faa,
          String[] args) {
    try {
      faa.init(args);
      finishedAlignmentActions.add(faa);
    } catch(AlignmentActionInitializationException aaie) {
      throw new GateRuntimeException(aaie);
    }
  }

  /**
   * load a data publishers action
   * 
   * @param faa
   * @param args
   */
  private void loadDataPublisherAction(final DataPublisherAction dpa,
          String[] args) {
    try {
      dpa.init(args);
      dataPublisherActions.add(dpa);
      SwingUtilities.invokeLater(new Runnable() {
        public void run() {
          DefaultDataModel ddm = new DefaultDataModel(dpa);
          tableTabbedPane.add(dpa.getTableTitle(), new XJTable(ddm));
          dpa.setDataModel(ddm);
        }
      });
    } catch(AlignmentActionInitializationException aaie) {
      throw new GateRuntimeException(aaie);
    }
  }

  /**
   * loads a pre-display action.
   * 
   * @param pda
   * @param args
   */
  private void loadPreDisplayAction(PreDisplayAction pda, String[] args) {
    try {
      pda.init(args);
      preDisplayActions.add(pda);
    } catch(AlignmentActionInitializationException aaie) {
      throw new GateRuntimeException(aaie);
    }
  }

  /**
   * If selected, the appropriate AlignmentAction is obtained from the local
   * data-structure and invoked.
   * 
   * @author gate
   */
  private class PropertyActionCB extends JCheckBox {
    /**
     * Which action to call if the check box is selected
     */
    AlignmentAction aa;

    /**
     * GUI component, that allows selecting, deselecting the action
     */
    JCheckBox thisInstance;

    /**
     * caption
     */
    String key;

    /**
     * Constructor
     * 
     * @param propKey -
     *          caption
     * @param value -
     *          selected or deselected
     * @param action -
     *          to be called if selected
     */
    public PropertyActionCB(String propKey, boolean value,
            AlignmentAction action) {
      super(propKey);
      setSelected(value);
      this.aa = action;
      thisInstance = this;
      key = propKey;
    }
  }

  public void cleanup() {
    Set<Object> visited = new HashSet<Object>();
    for(AlignmentAction a : allActions) {
      // System.out.println(a.getClass().getName());
      a.cleanup();
      visited.add(a);
    }

    for(PreDisplayAction a : preDisplayActions) {
      if(!visited.contains(a)) {
        // System.out.println(a.getClass().getName());
        a.cleanup();
        visited.add(a);
      }
    }

    for(FinishedAlignmentAction a : finishedAlignmentActions) {
      if(!visited.contains(a)) {
        // System.out.println(a.getClass().getName());
        a.cleanup();
        visited.add(a);
      }
    }

    for(DataPublisherAction a : dataPublisherActions) {
      if(!visited.contains(a)) {
        // System.out.println(a.getClass().getName());
        a.cleanup();
        visited.add(a);
      }
    }

  }

}
