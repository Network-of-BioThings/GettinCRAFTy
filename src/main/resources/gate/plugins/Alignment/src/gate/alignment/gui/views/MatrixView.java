package gate.alignment.gui.views;

import gate.Annotation;
import gate.Document;
import gate.alignment.AlignmentListener;
import gate.alignment.gui.AlignmentAction;
import gate.alignment.gui.AlignmentTask;
import gate.alignment.gui.AlignmentView;
import gate.alignment.gui.PUAPair;
import gate.compound.CompoundDocument;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.MouseInputAdapter;
import javax.swing.table.DefaultTableModel;

/**
 * This class provides an editor for aligning texts in a compound document.
 */
public class MatrixView extends JPanel implements AlignmentListener,
                                      AlignmentView {

  private static final long serialVersionUID = -2867467022258265114L;

  /**
   * scrollpane that holds the jtable
   */
  private JScrollPane waScrollPane;

  /**
   * Actual matrix to show data. First column and first row are used for labels.
   */
  private JTable matrix;

  /**
   * Alignment Task object
   */
  private AlignmentTask alignmentTask;

  /**
   * Default font-size
   */
  public static final int TEXT_SIZE = 20;

  private CompoundDocument compoundDocument;

  private AlignmentView thisInstance = null;

  // represents source annotations as shown in rows
  Map<Integer, Annotation> srcAnnotations;

  // represents target annotations as shown in columns
  Map<Integer, Annotation> tgtAnnotations;

  Cell currentCell;

  PUAPair currentPair;

  public MatrixView(AlignmentTask task) {
    srcAnnotations = new HashMap<Integer, Annotation>();
    tgtAnnotations = new HashMap<Integer, Annotation>();
    thisInstance = this;
    setTarget(task);
    initGui();
  }

  /**
   * Initialize the GUI
   */
  private void initGui() {
    setBorder(BorderFactory.createTitledBorder("Matrix View"));
    matrix = new JTable();
    matrix.setColumnSelectionAllowed(false);
    matrix.setTableHeader(null);
    matrix.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
    waScrollPane = new JScrollPane(matrix);
    waScrollPane.setColumnHeaderView(null);
    waScrollPane.setPreferredSize(new Dimension(800, 400));
    add(waScrollPane, BorderLayout.CENTER);
    MouseActionListener mal = new MouseActionListener();
    matrix.addMouseListener(mal);
    matrix.addMouseMotionListener(mal);
  }

  public void setTarget(AlignmentTask alignmentTask) {
    this.alignmentTask = alignmentTask;
    this.compoundDocument = alignmentTask.getCompoundDocument();
    compoundDocument.getAlignmentInformation(
            this.alignmentTask.getUaFeatureName()).addAlignmentListener(this);
  }

  /**
   * This method clears up the latest annotation selection
   */
  public void clearLatestAnnotationsSelection() {
    // do nothing - not relevant while using matrix view
  }

  /**
   * Executes the given action. It uses the pair that is being currently shown
   * to collect the alignment information which is then used as parameters to
   * call the provided action.
   * 
   * @param aa
   */
  public void executeAction(AlignmentAction aa) {

    // obtaining selected annotations
    Set<Annotation> srcSelectedAnnots = new HashSet<Annotation>();
    Set<Annotation> tgtSelectedAnnots = new HashSet<Annotation>();

    tgtSelectedAnnots.add(tgtAnnotations.get(currentCell.column));
    srcSelectedAnnots.add(srcAnnotations.get(currentCell.row));

    alignmentTask.getAlignmentActionsManager().executeAction(this, aa,
            srcSelectedAnnots, tgtSelectedAnnots,
            srcAnnotations.get(currentCell.row));
  }

  /**
   * This method updates the GUI.
   * 
   * @param docIDsAndAnnots
   */
  public void updateGUI(PUAPair pair) {
    this.currentPair = pair;
    // before refreshing, we remove all the highlights
    clearLatestAnnotationsSelection();
    srcAnnotations.clear();
    tgtAnnotations.clear();
    int counter = 1;
    for(Annotation a : pair.getSourceUnitAnnotations()) {
      srcAnnotations.put(counter, a);
      counter++;
    }

    counter = 1;
    for(Annotation a : pair.getTargetUnitAnnotations()) {
      tgtAnnotations.put(counter, a);
      counter++;
    }

    matrix.setModel(new AlignmentTableModel(srcAnnotations, tgtAnnotations));
    matrix.updateUI();
  }

  /**
   * listens to the annotationsAligned event and updates the GUI accordingly.
   */
  public void annotationsAligned(Annotation srcAnnotation, String srcAS,
          Document srcDocument, Annotation tgtAnnotation, String tgtAS,
          Document tgtDocument) {
    matrix.updateUI();
  }

  /**
   * listens to the annotationsUnAligned event and updates the GUI accordingly.
   */

  public void annotationsUnaligned(Annotation srcAnnotation, String srcAS,
          Document srcDocument, Annotation tgtAnnotation, String tgtAS,
          Document tgtDocument) {

    matrix.updateUI();
  }

  /**
   * Implements various mouse events. E.g. what should happen when someone
   * clicks on an unhighlighted annotation etc.
   * 
   * @author niraj
   */
  protected class MouseActionListener extends MouseInputAdapter {

    public void mouseClicked(MouseEvent me) {
      mouseExited(me);
      Point p = me.getPoint();
      int row = matrix.rowAtPoint(p);
      int column = matrix.columnAtPoint(p);
      if(row == 0 || column == 0) return;
      Annotation srcAnnot = srcAnnotations.get(row);
      Annotation tgtAnnot = tgtAnnotations.get(column);
      currentCell = new Cell(column, row);
      boolean alignedAnnots =
              alignmentTask.getAlignment().areTheyAligned(srcAnnot, tgtAnnot);

      // we should show the option menu here
      JPopupMenu optionsMenu =
              alignmentTask.getAlignmentActionsManager().prepareOptionsMenu(
                      thisInstance, alignedAnnots, true);
      optionsMenu.show(matrix, (int)p.getX(), (int)p.getY());
      optionsMenu.setVisible(true);
      return;
    }

    JPopupMenu menu = new JPopupMenu();

    FeaturesModel model = new FeaturesModel();

    JTable featuresTable = new JTable(model);

    Timer timer = new Timer();

    TimerTask task;

    public void mouseEntered(final MouseEvent me) {
      mouseExited(me);
      Point p = me.getPoint();
      int row = matrix.rowAtPoint(p);
      int column = matrix.columnAtPoint(p);
      currentCell = new Cell(column, row);

      Annotation a = null;
      if(row == 0) {
        a = tgtAnnotations.get(column);
      } else if(column == 0) {
        a = srcAnnotations.get(row);
      } else {
        return;
      }

      model.setAnnotation(a);
      task = new TimerTask() {
        public void run() {
          menu.add(featuresTable);
          menu.show(matrix, me.getX(), me.getY() + 10);
          menu.revalidate();
          menu.updateUI();
        }
      };
      timer.schedule(task, 2000);
    }

    public void mouseExited(MouseEvent me) {
      if(task != null) {
        task.cancel();
      }
      if(menu != null && menu.isVisible()) {
        menu.setVisible(false);
      }
    }
  }

  /**
   * represents annotation features. This is used for displaying features of the
   * annotation currently being focused by the mouse pointer.
   * 
   * @author gate
   */
  public class FeaturesModel extends DefaultTableModel {

    // annotation whoes features need to be displayed
    Annotation toShow;

    /**
     * keys
     */
    ArrayList<String> features;

    /**
     * values
     */
    ArrayList<String> values;

    /**
     * constructor
     */
    public FeaturesModel() {
      super(new String[]{"Feature", "Value"}, 0);
    }

    /**
     * sets the annotation whoes features need to be shown
     * 
     * @param annot
     */
    public void setAnnotation(Annotation annot) {
      features = new ArrayList<String>();
      values = new ArrayList<String>();
      for(Object key : annot.getFeatures().keySet()) {
        features.add(key.toString());
        values.add(annot.getFeatures().get(key).toString());
      }
      super.fireTableDataChanged();
    }

    public Class getColumnClass(int column) {
      return String.class;
    }

    public int getRowCount() {
      return values == null ? 0 : values.size();
    }

    public int getColumnCount() {
      return 2;
    }

    public String getColumnName(int column) {
      switch(column){
        case 0:
          return "Feature";
        default:
          return "Value";
      }
    }

    public Object getValueAt(int row, int column) {
      switch(column){
        case 0:
          return features.get(row);
        default:
          return values.get(row);
      }
    }

  }

  /**
   * @author gate
   */
  public class AlignmentTableModel extends DefaultTableModel {

    Map<Integer, Annotation> srcAnns = new HashMap<Integer, Annotation>();
    Map<Integer, Annotation> tgtAnns = new HashMap<Integer, Annotation>();

    public AlignmentTableModel(Map<Integer, Annotation> srcAnnots,
            Map<Integer, Annotation> tgtAnnots) {
      this.srcAnns = srcAnnots;
      this.tgtAnns = tgtAnnots;
    }

    
    public int getRowCount() {
      return srcAnns == null ? 0 : srcAnns.size() + 1;
    }

    public int getColumnCount() {
      return tgtAnns == null ? 0 : tgtAnns.size() + 1;
    }

    public Object getValueAt(int row, int col) {
      if((row == 0 && col == 0) || (currentPair == null)) { return ""; }

      if(row == 0) { return currentPair.getText(tgtAnns.get(col), false); }

      if(col == 0) { return currentPair.getText(srcAnns.get(row), true); }

      Annotation srcAnnot = srcAnns.get(row);
      Annotation tgtAnnot = tgtAnns.get(col);
      if(alignmentTask.getAlignment().areTheyAligned(srcAnnot,  tgtAnnot)) {
        return "*";
      } else {
        return "";
      }
    }
  }

  class Cell {
    // column number
    int column;

    // row number
    int row;

    public Cell(int column, int row) {
      super();
      this.column = column;
      this.row = row;
    }
  } // Cell

}
