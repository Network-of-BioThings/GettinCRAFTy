package gate.alignment.gui;

import gate.Annotation;
import gate.AnnotationSet;
import gate.Document;
import gate.alignment.Alignment;
import gate.alignment.gui.views.LinksView;
import gate.alignment.gui.views.MatrixView;
import gate.alignment.gui.views.ParallelTextView;
import gate.compound.CompoundDocument;
import gate.event.DocumentEvent;
import gate.event.DocumentListener;
import gate.event.FeatureMapListener;
import gate.gui.MainFrame;
import gate.swing.ColorGenerator;
import gate.util.GateRuntimeException;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.DefaultHighlighter.DefaultHighlightPainter;

/**
 * This class provides an editor for aligning texts in a compound document.
 */
public class AlignmentEditor extends JFrame implements FeatureMapListener,
                                           DocumentListener {

  /**
   * initialize the editor
   */
  AlignmentEditor editor = null;

  /**
   * Menus for creating new alignment tasks, setting up active learning setup,
   * specifying user preferences etc.
   */
  JMenuBar menubar;

  /**
   * Currently loaded tasks. Use can select from them.
   */
  JMenu alignmentTasksMenu;

  /**
   * Toolbar to hold alignment viewer controls
   */
  JPanel controls;

  /**
   * The document on which it would work
   */
  private CompoundDocument document;

  /**
   * Shows available alignment features alignment features
   */
  private JComboBox alignmentFeatures;

  /**
   * annotation sets of the source document
   */
  private JComboBox srcAnnotationSets;

  /**
   * annotation sets of the target document
   */
  private JComboBox tgtAnnotationSets;

  /**
   * source annotation set
   */
  private AnnotationSet srcAS;

  /**
   * target annotation set
   */
  private AnnotationSet tgtAS;

  /**
   * Current alignment object
   */
  private Alignment alignment;

  /**
   * Flag to notice user if the window needs reloading
   */
  private boolean reload = false;

  /**
   * Refresh highlights button
   */
  private JButton refreshHighlights;

  /**
   * Source document
   */
  private Document srcDoc;

  /**
   * Target document
   */
  private Document tgtDoc;

  /**
   * Editor used for displaying source document
   */
  JTextArea sourceEditor;

  /**
   * Constant representing default annotation set name
   */
  public static final String DEFAULT_AS_NAME = "<null>";

  /**
   * Editor used for displaying target document
   */
  JTextArea targetEditor;

  /**
   * Timer used for determine exactly when to select a highlight
   */
  protected Timer mouseMovementTimer;

  /**
   * Time before the highlight is selected
   */
  private static final int MOUSE_MOVEMENT_TIMER_DELAY = 300;

  /**
   * Action that should be taken when the mouse has stopped moving and is on one
   * of the highlights
   */
  protected MouseStoppedMovingAction mouseStoppedMovingAction;

  /**
   * Highlight used for highlighting aligned annotations.
   */
  final DefaultHighlighter.DefaultHighlightPainter HIGHLIGHT =
      new DefaultHighlighter.DefaultHighlightPainter(
          getColor(Color.GREEN, 0.1f));

  /**
   * Highlight used for highlighting selected aligned annotations.
   */
  final DefaultHighlighter.DefaultHighlightPainter SELECTED_HIGHLIGHT =
      new DefaultHighlighter.DefaultHighlightPainter(getColor(Color.RED, 0.5f));

  /**
   * Highlight used for highlighting selected aligned annotations.
   */
  final DefaultHighlighter.DefaultHighlightPainter PROCESSING_HIGHLIGHT =
      new DefaultHighlighter.DefaultHighlightPainter(getColor(Color.BLUE, 0.5f));

  /**
   * Color generator used for generatig color
   */
  private static ColorGenerator colorGenerator = new ColorGenerator();

  /**
   * To store currently selected highlight objects
   */
  private Set<Object> srcSelectedHighlights = new HashSet<Object>();

  /**
   * To store currently selected highlight objects
   */
  private Set<Object> tgtSelectedHighlights = new HashSet<Object>();

  /**
   * Stores alignment tasks objects
   */
  private Map<String, AlignmentTask> alignmentTasks =
      new HashMap<String, AlignmentTask>();

  /**
   * The current alignment task that is being shown on the screen
   */
  private AlignmentTask currentAT = null;

  /**
   * every alignment task has a view associated with it. This map is used for
   * storing the reference
   */
  private HashMap<AlignmentTask, AlignmentTaskView> alignmentTaskViews =
      new HashMap<AlignmentTask, AlignmentTaskView>();

  /**
   * Alignment task panel
   */
  private JPanel alignmentTaskPanel = null;

  /**
   * Constructor
   * 
   * @param document
   */
  public AlignmentEditor(CompoundDocument document) {
    // setting the title to be alignment editor
    super("Alignment Editor");

    // using the same icon as used for annotation diff
    setIconImage(((ImageIcon)MainFrame.getIcon("annotation-diff")).getImage());

    // when the main GATE gui closes - so should this one
    MainFrame.getGuiRoots().add(this);
    this.editor = this;
    this.document = document;

    /**
     * When the window is closing, the dispose method should be closed
     */
    addWindowListener(new WindowAdapter() {
      @Override
      public void windowClosing(WindowEvent we) {
        super.windowClosing(we);
        dispose();
      }
    });

    setBounds(0, 0, 800, 600);
    setLayout(new BorderLayout());
    setVisible(true);

    // ask for the source and target documents
    Document[] documents = askForDocuments();
    if(documents == null) {
      dispose();
      return;
    }
    srcDoc = documents[0];
    tgtDoc = documents[1];

    // based on the source and target documents - initialises the GUI
    initGui();

    // we need to listen to the annotation set update
    srcDoc.addDocumentListener(this);
    tgtDoc.addDocumentListener(this);

    this.document.getFeatures().addFeatureMapListener(this);
  }

  /**
   * Disposes the window and cleans up the resource
   */
  @Override
  public void dispose() {
    cleanup();
    super.dispose();
  }

  /** Clean up method that is called when the window is closed */
  public void cleanup() {

    // delete document listeners and feature update listener
    if(this.srcDoc != null) this.srcDoc.removeDocumentListener(this);

    if(this.tgtDoc != null) this.tgtDoc.removeDocumentListener(this);

    this.document.getFeatures().removeFeatureMapListener(this);
  }

  /**
   * Introduces transparency to the given color. If c is null, a new random
   * color is generated using the color generater class.
   * 
   * @param c
   * @return
   */
  public static Color getColor(Color c, float alphaValue) {
    float components[] = null;
    if(c == null)
      components = colorGenerator.getNextColor().getComponents(null);
    else components = c.getComponents(null);

    Color colour =
        new Color(components[0], components[1], components[2], alphaValue);
    int rgb = colour.getRGB();
    int alpha = colour.getAlpha();
    int rgba = rgb | (alpha << 24);
    colour = new Color(rgba, true);
    return colour;
  }

  /**
   * Initializes this GUI
   */
  private void initGui() {

    // menu for various things
    menubar = new JMenuBar();
    this.setJMenuBar(menubar);
    this.alignmentTasksMenu = new JMenu("Switch Tasks");
    JMenu taskMenu = new JMenu("Alignment Tasks");
    menubar.add(taskMenu);
    taskMenu.add(new NewAlignmentTaskAction());
    taskMenu.add(this.alignmentTasksMenu);
    taskMenu.add(new LoadAlignmentTaskAction());
    taskMenu.add(new SaveAlignmentTaskAction());

    // toolbar for viewer
    controls = new JPanel(new FlowLayout(FlowLayout.LEFT));
    add(controls, BorderLayout.NORTH);

    // full screen mode
    this.setExtendedState(this.getExtendedState() | JFrame.MAXIMIZED_BOTH);

    // preferred size
    this.setPreferredSize(new Dimension(800, 600));

    // editors for source and target documents
    sourceEditor = new JTextArea();
    targetEditor = new JTextArea();

    // we need scrollpanes to show editors
    JScrollPane sourcePane = new JScrollPane(sourceEditor);
    sourcePane.setBorder(BorderFactory.createTitledBorder(srcDoc.getName()));
    JScrollPane targetPane = new JScrollPane(targetEditor);
    targetPane.setBorder(BorderFactory.createTitledBorder(tgtDoc.getName()));

    JPanel contentPanel = new JPanel();
    contentPanel.setLayout(new GridLayout(1, 2));
    contentPanel.add(sourcePane);
    contentPanel.add(targetPane);
    add(contentPanel, BorderLayout.CENTER);

    // set the contents

    Font f = new Font(sourceEditor.getFont().getFontName(), Font.BOLD, 15);
    sourceEditor.setFont(f);
    targetEditor.setFont(f);
    sourceEditor.setAutoscrolls(false);
    sourceEditor.setLineWrap(true);
    sourceEditor.setWrapStyleWord(true);
    targetEditor.setAutoscrolls(false);
    targetEditor.setLineWrap(true);
    targetEditor.setWrapStyleWord(true);

    sourceEditor.setEditable(false);
    targetEditor.setEditable(false);

    sourceEditor.setText(srcDoc.getContent().toString());
    targetEditor.setText(tgtDoc.getContent().toString());

    /**
     * Obtain all the alignment feature names
     */
    List<String> alignmentFeatureNames =
        new ArrayList<String>(document.getAllAlignmentFeatureNames());
    this.alignmentFeatures = new JComboBox(alignmentFeatureNames.toArray());

    controls.add(new JLabel("Alignment Features:"));
    controls.add(this.alignmentFeatures);

    controls.add(new JLabel(" Source InputAS:"));
    this.srcAnnotationSets = new JComboBox(getASNames(srcDoc).toArray());
    controls.add(this.srcAnnotationSets);

    controls.add(new JLabel(" Target InputAS:"));
    this.tgtAnnotationSets = new JComboBox(getASNames(tgtDoc).toArray());
    controls.add(this.tgtAnnotationSets);

    refreshHighlights = new JButton("Go");
    controls.add(refreshHighlights);

    mouseStoppedMovingAction = new MouseStoppedMovingAction();
    mouseMovementTimer =
        new javax.swing.Timer(MOUSE_MOVEMENT_TIMER_DELAY,
            mouseStoppedMovingAction);
    mouseMovementTimer.setRepeats(false);
    alignmentTaskPanel = new JPanel();

    add(alignmentTaskPanel, BorderLayout.SOUTH);
    initListeners();
    pack();
  }

  /**
   * Highlighting parent annotations.
   * 
   * @param pair
   */
  private void highlightParentAnnotations(PUAPair pair) {
    try {

      // removing previously highlighted highlight
      for(Object o : srcSelectedHighlights) {
        sourceEditor.getHighlighter().removeHighlight(o);
      }

      // removing previously highlighted highlight
      for(Object o : tgtSelectedHighlights) {
        targetEditor.getHighlighter().removeHighlight(o);
      }

      // add new highlights
      if(pair.sourceAnnotations != null) {
        for(Annotation a : pair.sourceAnnotations) {
          Object o =
              sourceEditor.getHighlighter().addHighlight(
                  a.getStartNode().getOffset().intValue(),
                  a.getEndNode().getOffset().intValue(), PROCESSING_HIGHLIGHT);
          srcSelectedHighlights.add(o);
        }
      }

      // add new highlights
      if(pair.targetAnnotations != null) {
        for(Annotation a : pair.targetAnnotations) {
          Object o =
              targetEditor.getHighlighter().addHighlight(
                  a.getStartNode().getOffset().intValue(),
                  a.getEndNode().getOffset().intValue(), PROCESSING_HIGHLIGHT);
          tgtSelectedHighlights.add(o);
        }
      }
    } catch(BadLocationException e) {
      throw new GateRuntimeException(e);
    }
  }

  /**
   * Action to show the next pair
   * 
   * @author niraj
   */
  class NextPairAction extends AbstractAction {

    // Alignment task
    AlignmentTask task;

    // Alignment task view
    AlignmentTaskView view;

    /**
     * Constructor
     * 
     * @param view
     */
    public NextPairAction(AlignmentTaskView view) {
      super("Next");
      this.task = view.alignmentTask;
      this.view = view;
    } // constructor

    public void actionPerformed(ActionEvent e) {

      if(task.current() != null) {

        // ask this only if the pair hasn't been marked as finished
        if(!task.current().isAlignmentFinished()) {
          int answer =
              JOptionPane.showConfirmDialog(editor,
                  "Is alignment complete for this pair?");
          if(answer == JOptionPane.YES_OPTION) {
            task.getAlignmentActionsManager().executeFinishedAlignmentActions(
                task.current());
            task.current().setAlignmentFinished(true);
          } else if(answer == JOptionPane.CANCEL_OPTION) { return; }
        }
      }

      if(task.hasNext()) {
        PUAPair pair = task.next();
        task.getAlignmentActionsManager().executePreDisplayActions(pair);
        highlightParentAnnotations(pair);
        view.updateView(pair);
      } else {
        JOptionPane.showMessageDialog(editor, "Reached End of the Document");
      }
    }
  }

  /**
   * Shows the previous pair
   * 
   * @author niraj
   */
  class PreviousPairAction extends AbstractAction {

    AlignmentTask task;

    AlignmentTaskView view;

    public PreviousPairAction(AlignmentTaskView view) {
      super("Previous");
      this.task = view.alignmentTask;
      this.view = view;
    } // constructor

    public void actionPerformed(ActionEvent e) {

      // ask this only if the pair hasn't been marked as finished
      if(!task.current().isAlignmentFinished()) {
        int answer =
            JOptionPane.showConfirmDialog(editor,
                "Is alignment complete for this pair?");
        if(answer == JOptionPane.YES_OPTION) {
          task.getAlignmentActionsManager().executeFinishedAlignmentActions(
              task.current());
          task.current().setAlignmentFinished(true);
        } else if(answer == JOptionPane.CANCEL_OPTION) { return; }
      }

      if(task.hasPrevious()) {
        PUAPair pair = task.previous();
        highlightParentAnnotations(pair);
        view.updateView(pair);
      } else {
        JOptionPane.showMessageDialog(editor, "Reached Start of the Document");
      }
    }
  }

  /**
   * Create a new alignment task
   * 
   * @author niraj
   */
  class NewAlignmentTaskAction extends AbstractAction {

    public NewAlignmentTaskAction() {
      super("New Task");
    } // constructor

    public void actionPerformed(ActionEvent e) {
      AlignmentTask alignmentTask = createAlignmentTask();
      if(alignmentTask == null) return;
      alignmentTasks.put(alignmentTask.getName(), alignmentTask);
      alignmentTasksMenu.add(new SwitchAlignmentTaskAction(alignmentTask
          .getName()));
      currentAT = alignmentTask;

      // updateGUI
      alignmentTaskPanel.removeAll();
      AlignmentTaskView view = new AlignmentTaskView(currentAT);
      alignmentTaskPanel.add(view, BorderLayout.CENTER);
      alignmentTaskViews.put(currentAT, view);
      editor.validate();
    }
  }

  class SwitchAlignmentTaskAction extends AbstractAction {
    /**
     * Alignment task name
     */
    String taskName;

    public SwitchAlignmentTaskAction(String caption) {
      super(caption);
      this.taskName = caption;
    } // constructor

    public void actionPerformed(ActionEvent e) {

      currentAT = alignmentTasks.get(taskName);
      AlignmentTaskView view = alignmentTaskViews.get(currentAT);

      // updateGUI
      alignmentTaskPanel.removeAll();
      alignmentTaskPanel.add(view, BorderLayout.CENTER);
      editor.validate();
    }

  }

  class HideTaskAction extends AbstractAction {

    AlignmentTaskView view;

    public HideTaskAction(AlignmentTaskView view) {
      super("Hide");
      this.view = view;
    } // constructor

    public void actionPerformed(ActionEvent e) {

      // updateGUI
      alignmentTaskPanel.removeAll();
      editor.validate();
    }

  }

  class CloseTaskAction extends AbstractAction {

    AlignmentTaskView view;

    public CloseTaskAction(AlignmentTaskView view) {
      super("Close");
      this.view = view;
    } // constructor

    public void actionPerformed(ActionEvent e) {
      if(currentAT != null) {
        alignmentTasks.remove(currentAT.getName());

        for(int i = 0; i < alignmentTasksMenu.getItemCount(); i++) {
          JMenuItem mi = alignmentTasksMenu.getItem(i);
          SwitchAlignmentTaskAction sata =
              (SwitchAlignmentTaskAction)mi.getAction();
          if(sata.taskName.equals(currentAT.getName())) {
            alignmentTasksMenu.remove(mi);
            alignmentTasksMenu.updateUI();
            break;
          }
        }

        alignmentTaskViews.remove(currentAT);
        currentAT.getAlignmentActionsManager().cleanup();
        currentAT = null;

        // updateGUI
        alignmentTaskPanel.removeAll();
        editor.validate();
      }
    }

  }

  class LoadAlignmentTaskAction extends AbstractAction {

    public LoadAlignmentTaskAction() {
      super("Open Task");
    } // constructor

    public void actionPerformed(ActionEvent e) {
      JFileChooser chooser = MainFrame.getFileChooser();
      chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

      int result = chooser.showOpenDialog(editor);
      if(result == JFileChooser.APPROVE_OPTION) {
        File selected = chooser.getSelectedFile();
        currentAT = AlignmentTask.fromXML(document, selected.getAbsolutePath());
        alignmentTasks.put(currentAT.getName(), currentAT);
        alignmentTasksMenu.add(new SwitchAlignmentTaskAction(currentAT
            .getName()));

        // updateGUI
        alignmentTaskPanel.removeAll();
        AlignmentTaskView view = new AlignmentTaskView(currentAT);
        alignmentTaskPanel.add(view, BorderLayout.CENTER);
        alignmentTaskViews.put(currentAT, view);
        editor.validate();

      }
    }
  }

  class SaveAlignmentTaskAction extends AbstractAction {

    public SaveAlignmentTaskAction() {
      super("Save Task");
    } // constructor

    public void actionPerformed(ActionEvent e) {
      JFileChooser chooser = MainFrame.getFileChooser();
      chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

      int result = chooser.showSaveDialog(editor);
      if(result == JFileChooser.APPROVE_OPTION) {
        File selected = chooser.getSelectedFile();
        AlignmentTask.toXML(currentAT, selected.getAbsolutePath());
      }
    }
  }

  /**
   * Obtains annotation set names from the document String
   * 
   * @param document
   * @return
   */
  private List<String> getASNames(Document document) {
    Set<String> setNames = document.getAnnotationSetNames();
    List<String> setNamesList = null;
    if(setNames == null)
      setNamesList = new ArrayList<String>();
    else setNamesList = new ArrayList<String>(setNames);
    setNamesList.add(0, DEFAULT_AS_NAME);
    return setNamesList;
  }

  private void initListeners() {
    // add focus listener to the current window
    this.addFocusListener(new FocusListener() {

      public void focusGained(FocusEvent e) {
        if(reload) {
          JOptionPane
              .showMessageDialog(editor,
                  "Alignment information has changed - the document will be refreshed!");
          reload = false;

          // execute the refreshHighlight action
          refreshHighlightsAction();
        }
      }

      public void focusLost(FocusEvent e) {
        // TODO Auto-generated method stub
        // nothing
      }
    });

    // add action listener to the refresh highlights button
    this.refreshHighlights.addActionListener(new ActionListener() {

      // refresh highlights
      public void actionPerformed(ActionEvent e) {
        refreshHighlightsAction();
      }

    });

    SelectHighlightListener sh = new SelectHighlightListener();
    this.sourceEditor.addMouseMotionListener(sh);
    this.targetEditor.addMouseMotionListener(sh);

  }

  private void refreshHighlightsAction() {
    int selectedIndex = srcAnnotationSets.getSelectedIndex();
    if(selectedIndex == -1) return;
    selectedIndex = tgtAnnotationSets.getSelectedIndex();
    if(selectedIndex == -1) return;

    boolean isDefault = srcAnnotationSets.getSelectedIndex() == 0;
    srcAS =
        isDefault ? srcDoc.getAnnotations() : srcDoc
            .getAnnotations((String)srcAnnotationSets.getSelectedItem());

    isDefault = tgtAnnotationSets.getSelectedIndex() == 0;
    tgtAS =
        isDefault ? tgtDoc.getAnnotations() : tgtDoc
            .getAnnotations((String)tgtAnnotationSets.getSelectedItem());

    // obtain the alignment object
    alignment =
        document.getAlignmentInformation((String)alignmentFeatures
            .getSelectedItem());

    refreshHighlights();
  }

  /**
   * Checks the selected alignment information
   */
  private void addHighlights() {
    // obtain all aligned annotations
    int index = this.alignmentFeatures.getSelectedIndex();
    if(index == 0) return;

    addHighlights(sourceEditor, srcAS);
    addHighlights(targetEditor, tgtAS);
  }

  /**
   * Adds highlights to the document - indicating which annotations are aligned
   * 
   * @param alignment
   * @param editorPane
   * @param inputAS
   */
  private void addHighlights(JTextArea editorPane, AnnotationSet inputAS) {
    for(Annotation a : inputAS) {
      if(alignment.isAnnotationAligned(a)) {
        addHighlight(a, editorPane, HIGHLIGHT);
      }
    }
  }

  /**
   * Adds the highlight to annotation
   * 
   * @param a
   * @param editorPane
   * @param painter
   */
  private Object addHighlight(Annotation a, JTextArea editorPane,
      DefaultHighlightPainter painter) {
    try {
      return editorPane.getHighlighter().addHighlight(
          a.getStartNode().getOffset().intValue(),
          a.getEndNode().getOffset().intValue(), painter);
    } catch(BadLocationException e) {
      throw new GateRuntimeException(e);
    }
  }

  /**
   * Remove all existing highlights and refresh
   */
  private void refreshHighlights() {
    sourceEditor.getHighlighter().removeAllHighlights();
    targetEditor.getHighlighter().removeAllHighlights();
    addHighlights();
  }

  class SelectHighlightListener implements MouseMotionListener, MouseListener {

    Set<Object> srcToDel = new HashSet<Object>();

    Set<Object> tgtToDel = new HashSet<Object>();

    Map<Annotation, Object> selectedAnnots = new HashMap<Annotation, Object>();

    public void mouseDragged(MouseEvent e) {
      // do not create annotations while dragging
      mouseMovementTimer.stop();
    }

    public void mouseMoved(MouseEvent e) {
      // this triggers select annotation leading to edit annotation or
      // new
      // annotation actions
      // ignore movement if CTRL pressed or dragging
      int modEx = e.getModifiersEx();
      if((modEx & MouseEvent.CTRL_DOWN_MASK) != 0) {
        mouseMovementTimer.stop();
        return;
      }
      if((modEx & MouseEvent.BUTTON1_DOWN_MASK) != 0) {
        mouseMovementTimer.stop();
        return;
      }

      JTextArea textPane = (JTextArea)e.getSource();

      // check the text location is real
      int textLocation = textPane.viewToModel(e.getPoint());
      try {
        Rectangle viewLocation = textPane.modelToView(textLocation);
        // expand the rectangle a bit
        int error = 10;
        viewLocation =
            new Rectangle(viewLocation.x - error, viewLocation.y - error,
                viewLocation.width + 2 * error, viewLocation.height + 2 * error);
        if(viewLocation.contains(e.getPoint())) {
          mouseStoppedMovingAction.setTextLocation(textLocation);
          mouseStoppedMovingAction.setEditorPane(textPane);
        } else {
          mouseStoppedMovingAction.setTextLocation(-1);
        }
      }

      catch(BadLocationException e1) {
        // don't do anything
      } finally {
        mouseMovementTimer.restart();
      }
    }

    public void mouseExited(MouseEvent e) {
      mouseMovementTimer.stop();
    }

    public void mouseClicked(MouseEvent e) {
      // do nothing

    }

    public void mouseEntered(MouseEvent e) {
      // do nothing

    }

    public void mousePressed(MouseEvent e) {
      // do nothing

    }

    public void mouseReleased(MouseEvent e) {
      // do nothing

    }
  }

  private final String LINKS_VIEW = "Links";

  private final String MATRIX_VIEW = "Matrix";

  private final String PARALLEL_VIEW = "Parallel";

  /**
   * types of views we support
   */
  private final String[] VIEWS = new String[]{LINKS_VIEW, MATRIX_VIEW,
      PARALLEL_VIEW};

  private AlignmentTask createAlignmentTask() {
    JPanel mainPanel = new JPanel(new GridBagLayout());
    // two combo-boxes
    JTextField taskName =
        new JTextField("AlignmentTask"
            + ("" + Math.random() * 5000).substring(0, 4), 25);
    JTextField unitOfAlignment = new JTextField("Token", 25);
    JTextField parentOfUnitOfAlignment = new JTextField("Sentence", 25);

    List<String> alignmentFeatureNames = new ArrayList<String>();
    alignmentFeatureNames.addAll(document.getAllAlignmentFeatureNames());
    JComboBox dataSources = new JComboBox(alignmentFeatureNames.toArray());
    dataSources.setEditable(true);

    JComboBox storeAlignmentIn = new JComboBox(alignmentFeatureNames.toArray());
    storeAlignmentIn.setEditable(true);

    JComboBox viewsToSelectFrom = new JComboBox(VIEWS);
    viewsToSelectFrom.setEditable(false);

    final JTextField actionsFile = new JTextField("", 25);
    JButton actionsFileChooser = new JButton(MainFrame.getIcon("open-file"));

    // fourth row row
    GridBagConstraints constraints = new GridBagConstraints();
    constraints.gridx = GridBagConstraints.RELATIVE;
    constraints.gridy = 1;
    constraints.gridwidth = 1;
    constraints.anchor = GridBagConstraints.WEST;
    constraints.fill = GridBagConstraints.NONE;
    constraints.insets = new Insets(0, 0, 0, 5);
    mainPanel.add(new JLabel("Task Name:"), constraints);

    constraints = new GridBagConstraints();
    constraints.gridx = GridBagConstraints.RELATIVE;
    constraints.gridy = 1;
    constraints.gridwidth = 1;
    constraints.anchor = GridBagConstraints.NORTHWEST;
    mainPanel.add(taskName, constraints);

    // fourth row row
    constraints = new GridBagConstraints();
    constraints.gridx = GridBagConstraints.RELATIVE;
    constraints.gridy = 2;
    constraints.gridwidth = 1;
    constraints.anchor = GridBagConstraints.WEST;
    constraints.fill = GridBagConstraints.NONE;
    constraints.insets = new Insets(0, 0, 0, 5);
    mainPanel.add(new JLabel("Unit Annotation Type:"), constraints);

    constraints = new GridBagConstraints();
    constraints.gridx = GridBagConstraints.RELATIVE;
    constraints.gridy = 2;
    constraints.gridwidth = 1;
    constraints.anchor = GridBagConstraints.NORTHWEST;
    mainPanel.add(unitOfAlignment, constraints);

    // fifth row
    constraints = new GridBagConstraints();
    constraints.gridx = GridBagConstraints.RELATIVE;
    constraints.gridy = 3;
    constraints.gridwidth = 1;
    constraints.anchor = GridBagConstraints.WEST;
    constraints.fill = GridBagConstraints.NONE;
    constraints.insets = new Insets(0, 0, 0, 5);
    mainPanel.add(new JLabel("Parent of Unit of Alignment:"), constraints);

    constraints = new GridBagConstraints();
    constraints.gridx = GridBagConstraints.RELATIVE;
    constraints.gridy = 3;
    constraints.gridwidth = 1;
    constraints.anchor = GridBagConstraints.NORTHWEST;
    mainPanel.add(parentOfUnitOfAlignment, constraints);

    // sixth row
    constraints = new GridBagConstraints();
    constraints.gridx = GridBagConstraints.RELATIVE;
    constraints.gridy = 4;
    constraints.gridwidth = 1;
    constraints.anchor = GridBagConstraints.WEST;
    constraints.fill = GridBagConstraints.NONE;
    constraints.insets = new Insets(0, 0, 0, 5);
    mainPanel.add(new JLabel("Data Source:"), constraints);

    constraints = new GridBagConstraints();
    constraints.gridx = GridBagConstraints.RELATIVE;
    constraints.gridy = 4;
    constraints.gridwidth = 1;
    constraints.anchor = GridBagConstraints.NORTHWEST;
    mainPanel.add(dataSources, constraints);

    // seventh row
    constraints = new GridBagConstraints();
    constraints.gridx = GridBagConstraints.RELATIVE;
    constraints.gridy = 5;
    constraints.gridwidth = 1;
    constraints.anchor = GridBagConstraints.WEST;
    constraints.fill = GridBagConstraints.NONE;
    constraints.insets = new Insets(0, 0, 0, 5);
    mainPanel.add(new JLabel("Alignment Feature:"), constraints);

    constraints = new GridBagConstraints();
    constraints.gridx = GridBagConstraints.RELATIVE;
    constraints.gridy = 5;
    constraints.gridwidth = 1;
    constraints.anchor = GridBagConstraints.NORTHWEST;
    mainPanel.add(storeAlignmentIn, constraints);

    // seventh row
    constraints = new GridBagConstraints();
    constraints.gridx = GridBagConstraints.RELATIVE;
    constraints.gridy = 6;
    constraints.gridwidth = 1;
    constraints.anchor = GridBagConstraints.WEST;
    constraints.fill = GridBagConstraints.NONE;
    constraints.insets = new Insets(0, 0, 0, 5);
    mainPanel.add(new JLabel("Alignment View:"), constraints);

    constraints = new GridBagConstraints();
    constraints.gridx = GridBagConstraints.RELATIVE;
    constraints.gridy = 6;
    constraints.gridwidth = 1;
    constraints.anchor = GridBagConstraints.NORTHWEST;
    mainPanel.add(viewsToSelectFrom, constraints);

    // seventh row
    constraints = new GridBagConstraints();
    constraints.gridx = GridBagConstraints.RELATIVE;
    constraints.gridy = 7;
    constraints.gridwidth = 1;
    constraints.anchor = GridBagConstraints.WEST;
    constraints.fill = GridBagConstraints.NONE;
    constraints.insets = new Insets(0, 0, 0, 5);
    mainPanel.add(new JLabel("Actions Acitons File:"), constraints);

    constraints = new GridBagConstraints();
    constraints.gridx = GridBagConstraints.RELATIVE;
    constraints.gridy = 7;
    constraints.gridwidth = 1;
    constraints.anchor = GridBagConstraints.NORTHWEST;
    mainPanel.add(actionsFile, constraints);

    constraints = new GridBagConstraints();
    constraints.gridx = GridBagConstraints.RELATIVE;
    constraints.gridy = 7;
    constraints.gridwidth = 1;
    constraints.anchor = GridBagConstraints.NORTHWEST;
    mainPanel.add(actionsFileChooser, constraints);
    actionsFileChooser.setBorderPainted(false);
    actionsFileChooser.setContentAreaFilled(false);

    actionsFileChooser.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent ae) {
        // first we need to ask for a new empty directory
        JFileChooser fileChooser = MainFrame.getFileChooser();
        fileChooser.setDialogTitle("Please select actions configuration file");
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int result = fileChooser.showOpenDialog(editor);
        if(result == JFileChooser.APPROVE_OPTION) {
          try {
            actionsFile.setText(fileChooser.getSelectedFile().toURI().toURL()
                .toExternalForm());
          } catch(Exception e) {
            actionsFile.setText("");
          }
        }

      }
    });

    int returnValue =
        JOptionPane.showOptionDialog(editor.getContentPane(), mainPanel,
            "New Alignment Task", JOptionPane.PLAIN_MESSAGE,
            JOptionPane.OK_CANCEL_OPTION, MainFrame.getIcon("annotation-diff"),
            new String[]{"OK", "Cancel"}, "OK");
    if(returnValue == JOptionPane.OK_OPTION) {
      AlignmentTask alignmentTask = new AlignmentTask(document);

      File actionsConfFile = null;
      if(actionsFile.getText().trim().length() > 0) {
        try {
          actionsConfFile =
              new File(new URL(actionsFile.getText().trim()).toURI());
        } catch(MalformedURLException e) {
          e.printStackTrace();
          actionsConfFile = null;
        } catch(URISyntaxException e) {
          e.printStackTrace();
          actionsConfFile = null;
        }
        if(!actionsConfFile.exists()) actionsConfFile = null;
      }
      // MainFrame.lockGUI("Initializing alignment task : "+
      // taskName.getText()+"... ");
      try {
        alignmentTask.initialize(taskName.getText(), srcDoc.getName(),
            tgtDoc.getName(), (String)srcAnnotationSets.getSelectedItem(),
            (String)tgtAnnotationSets.getSelectedItem(),
            unitOfAlignment.getText(), parentOfUnitOfAlignment.getText(),
            (String)dataSources.getSelectedItem(),
            (String)storeAlignmentIn.getSelectedItem(),
            (String)viewsToSelectFrom.getSelectedItem(),
            actionsConfFile == null ? null : actionsConfFile.getAbsolutePath());
      } finally {
        // MainFrame.unlockGUI();
      }
      return alignmentTask;
    } else {
      return null;
    }
  }

  /**
   * This method invokes a dialog that asks for source and target documents
   * 
   * @return
   */
  private Document[] askForDocuments() {
    JPanel mainPanel = new JPanel(new GridBagLayout());
    // two combo-boxes
    JComboBox sourceDocs = new JComboBox(document.getDocumentIDs().toArray());
    JComboBox targetDocs = new JComboBox(document.getDocumentIDs().toArray());

    GridBagConstraints constraints = new GridBagConstraints();
    constraints.gridx = GridBagConstraints.RELATIVE;
    constraints.gridy = 0;
    constraints.gridwidth = 1;
    constraints.anchor = GridBagConstraints.WEST;
    constraints.fill = GridBagConstraints.NONE;
    constraints.insets = new Insets(0, 0, 0, 5);
    mainPanel.add(new JLabel("Source Document:"), constraints);

    constraints = new GridBagConstraints();
    constraints.gridx = GridBagConstraints.RELATIVE;
    constraints.gridy = 0;
    constraints.gridwidth = 1;
    constraints.fill = GridBagConstraints.HORIZONTAL;
    constraints.insets = new Insets(0, 0, 0, 10);
    mainPanel.add(sourceDocs, constraints);

    // second row
    constraints = new GridBagConstraints();
    constraints.gridx = GridBagConstraints.RELATIVE;
    constraints.gridy = 1;
    constraints.gridwidth = 1;
    constraints.anchor = GridBagConstraints.WEST;
    constraints.fill = GridBagConstraints.NONE;
    constraints.insets = new Insets(0, 0, 0, 5);
    mainPanel.add(new JLabel("Target Document:"), constraints);

    constraints = new GridBagConstraints();
    constraints.gridx = GridBagConstraints.RELATIVE;
    constraints.gridy = 1;
    constraints.gridwidth = 1;
    constraints.fill = GridBagConstraints.HORIZONTAL;
    constraints.insets = new Insets(0, 0, 0, 10);
    mainPanel.add(targetDocs, constraints);

    int returnValue =
        JOptionPane.showOptionDialog(editor.getContentPane(), mainPanel,
            "Select Source and Target Documents", JOptionPane.PLAIN_MESSAGE,
            JOptionPane.OK_CANCEL_OPTION, MainFrame.getIcon("annotation-diff"),
            new String[]{"OK", "Cancel"}, "OK");
    if(returnValue == JOptionPane.OK_OPTION) {
      Document srcDoc =
          document.getDocument((String)sourceDocs.getSelectedItem());
      Document tgtDoc =
          document.getDocument((String)targetDocs.getSelectedItem());
      return new Document[]{srcDoc, tgtDoc};
    } else {
      return null;
    }
  }

  class HighlightAlignedAnnotation extends AbstractAction {

    Annotation a;

    Alignment alignment;

    JTextArea container;

    public HighlightAlignedAnnotation(Annotation a, Alignment alignment,
        JTextArea container) {
      super(a.getType());
      this.a = a;
      this.alignment = alignment;
      this.container = container;
    }

    public void actionPerformed(ActionEvent e) {
      // lets delete all the highlights
      for(Object a : srcSelectedHighlights) {
        sourceEditor.getHighlighter().removeHighlight(a);
      }
      for(Object a : tgtSelectedHighlights) {
        targetEditor.getHighlighter().removeHighlight(a);
      }

      // find out the new ones to add
      Set<Annotation> tgtAlignedAnnots = alignment.getAlignedAnnotations(a);
      Set<Annotation> srcAlignedAnnots = new HashSet<Annotation>();

      for(Annotation ta : tgtAlignedAnnots) {
        srcAlignedAnnots.addAll(alignment.getAlignedAnnotations(ta));
      }

      if(container == targetEditor) {
        Set<Annotation> tmp = srcAlignedAnnots;
        srcAlignedAnnots = tgtAlignedAnnots;
        tgtAlignedAnnots = tmp;
      }

      srcAlignedAnnots.retainAll(srcAS);
      tgtAlignedAnnots.retainAll(tgtAS);

      for(Annotation a : srcAlignedAnnots) {
        srcSelectedHighlights.add(addHighlight(a, sourceEditor,
            SELECTED_HIGHLIGHT));
      }

      for(Annotation a : tgtAlignedAnnots) {
        tgtSelectedHighlights.add(addHighlight(a, targetEditor,
            SELECTED_HIGHLIGHT));
      }
    }
  }

  protected class MouseStoppedMovingAction extends AbstractAction {
    public void actionPerformed(ActionEvent evt) {
      if(textLocation == -1) return;
      if(srcAS == null) return;
      JPopupMenu popup = new JPopupMenu();

      // check for annotations at location
      JTextArea editorToUse =
          editorPane == sourceEditor ? sourceEditor : targetEditor;
      AnnotationSet setToUse = editorPane == sourceEditor ? srcAS : tgtAS;
      Document docToUse = editorPane == sourceEditor ? srcDoc : tgtDoc;

      HighlightAlignedAnnotation haa = null;
      for(Annotation a : setToUse.get(Math.max(0l, textLocation - 1),
          Math.min(docToUse.getContent().size() - 1, textLocation + 1))) {

        if(alignment.isAnnotationAligned(a)) {
          haa = new HighlightAlignedAnnotation(a, alignment, editorPane);
          popup.add(haa);
        }
      }

      Rectangle rect;
      try {
        if(popup.getComponentCount() > 1) {
          rect = editorToUse.modelToView(textLocation);
          popup.show(editorToUse, rect.x + 10, rect.y);
        } else if(popup.getComponentCount() == 1) {
          haa.actionPerformed(null);
        }
      } catch(BadLocationException e) {
        e.printStackTrace();
      }
    }

    public void setTextLocation(int textLocation) {
      this.textLocation = textLocation;
    }

    public void setEditorPane(JTextArea editorPane) {
      this.editorPane = editorPane;
    }

    int textLocation;

    JTextArea editorPane;
  }

  class AlignmentTaskView extends JPanel {
    JToolBar viewsToolbar;

    AlignmentView alignmentView;

    JPanel thisInstance = null;

    JButton closeTask = null;

    JButton hideTask = null;

    AlignmentTask alignmentTask;

    public AlignmentTaskView(AlignmentTask alignmentTask) {
      this.alignmentTask = alignmentTask;
      setLayout(new BorderLayout());
      setBorder(BorderFactory.createTitledBorder(alignmentTask.getName()));
      viewsToolbar = new JToolBar();
      thisInstance = this;

      if(alignmentTask.getAlignmentView().equals(LINKS_VIEW)) {
        alignmentView = new LinksView(alignmentTask);
      } else if(alignmentTask.getAlignmentView().equals(MATRIX_VIEW)) {
        alignmentView = new MatrixView(alignmentTask);
      } else if(alignmentTask.getAlignmentView().endsWith(PARALLEL_VIEW)) {
        alignmentView = new ParallelTextView(alignmentTask);
      } else {
        throw new GateRuntimeException("Invalid alignment view :"
            + alignmentTask.getAlignmentView());
      }

      thisInstance.add((JPanel)alignmentView, BorderLayout.CENTER);
      viewsToolbar.add(new JLabel("Selected View:"
          + alignmentTask.getAlignmentView()));
      viewsToolbar.addSeparator();

      viewsToolbar.add(new JLabel("Pair:"));
      viewsToolbar.add(new PreviousPairAction(this));
      NextPairAction npa = new NextPairAction(this);
      viewsToolbar.add(npa);

      viewsToolbar.add(new HideTaskAction(this));
      viewsToolbar.add(new CloseTaskAction(this));

      add(viewsToolbar, BorderLayout.NORTH);
      if(alignmentTask.getActionsFilePath() != null) {
        add(alignmentTask.getAlignmentActionsManager(), BorderLayout.EAST);
      }

      npa.actionPerformed(null);
    }

    public void updateView(PUAPair pair) {
      alignmentView.updateGUI(pair);
    }
  }

  /**
   * When the features are updated
   */
  public void featureMapUpdated() {
    String selectedFeature = null;
    if(this.alignmentFeatures != null) {
      selectedFeature = (String)this.alignmentFeatures.getSelectedItem();
    }

    List<String> alignmentFeatureNames =
        new ArrayList<String>(document.getAllAlignmentFeatureNames());
    final boolean reselect =
        selectedFeature != null ? alignmentFeatureNames
            .contains(selectedFeature) : false;
    DefaultComboBoxModel model =
        new DefaultComboBoxModel(alignmentFeatureNames.toArray());
    if(this.alignmentFeatures == null) {
      this.alignmentFeatures = new JComboBox(model);
    } else {
      this.alignmentFeatures.setModel(model);
    }

    final String featureToReselect = selectedFeature;
    // update ui
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        alignmentFeatures.invalidate();
        alignmentFeatures.updateUI();
        if(reselect) {
          alignmentFeatures.setSelectedItem(featureToReselect);
          refreshHighlightsAction();
        } else {
          // delete any highlights
          sourceEditor.getHighlighter().removeAllHighlights();
          targetEditor.getHighlighter().removeAllHighlights();
        }
      }
    });
  }

  /**
   * update the src or target annotation sets when there's any new annotation
   * set added
   */
  public void annotationSetAdded(DocumentEvent e) {
    Document doc = (Document)e.getSource();
    JComboBox comboBox = null;
    if(srcDoc == doc) {
      comboBox = this.srcAnnotationSets;
    } else if(tgtDoc == doc) {
      comboBox = this.tgtAnnotationSets;
    } else {
      return;
    }

    String selectedASName = null;
    if(comboBox != null) {
      selectedASName = (String)comboBox.getSelectedItem();
    }

    String addedASName = e.getAnnotationSetName();
    if(addedASName == null || addedASName.trim().length() == 0) {
      addedASName = DEFAULT_AS_NAME;
    }

    DefaultComboBoxModel model = (DefaultComboBoxModel)comboBox.getModel();
    model.addElement(addedASName);

    final String ASToReselect = selectedASName;
    final JComboBox finalComboBox = comboBox;

    // update ui
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        finalComboBox.invalidate();
        finalComboBox.updateUI();
        if(ASToReselect != null) {
          finalComboBox.setSelectedItem(ASToReselect);
          refreshHighlightsAction();
        } else {
          // delete any highlights
          sourceEditor.getHighlighter().removeAllHighlights();
          targetEditor.getHighlighter().removeAllHighlights();
        }
      }
    });
  }

  /**
   * update the src or target annotation set combobox when an annotation set is
   * deleted.
   */
  public void annotationSetRemoved(DocumentEvent e) {
    Document doc = (Document)e.getSource();
    JComboBox comboBox = null;
    if(srcDoc == doc) {
      comboBox = this.srcAnnotationSets;
    } else if(tgtDoc == doc) {
      comboBox = this.tgtAnnotationSets;
    } else {
      return;
    }

    String selectedASName = null;
    if(comboBox != null) {
      selectedASName = (String)comboBox.getSelectedItem();
    }

    String deletedASName = e.getAnnotationSetName();
    if(deletedASName == null || deletedASName.trim().length() == 0) {
      deletedASName = DEFAULT_AS_NAME;
    }

    DefaultComboBoxModel model = (DefaultComboBoxModel)comboBox.getModel();
    model.removeElement(deletedASName);

    final String ASToReselect =
        selectedASName.equals(deletedASName) ? null : deletedASName;
    final JComboBox finalComboBox = comboBox;

    // update ui
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        finalComboBox.invalidate();
        finalComboBox.updateUI();
        if(ASToReselect != null) {
          finalComboBox.setSelectedItem(ASToReselect);
          refreshHighlightsAction();
        } else {
          // delete any highlights
          sourceEditor.getHighlighter().removeAllHighlights();
          targetEditor.getHighlighter().removeAllHighlights();
        }
      }
    });
  }

  public void contentEdited(DocumentEvent e) {
    // we don't do anything here
  }
}
