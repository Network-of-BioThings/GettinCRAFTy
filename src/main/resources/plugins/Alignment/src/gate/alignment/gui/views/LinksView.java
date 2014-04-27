package gate.alignment.gui.views;

import gate.Annotation;
import gate.Document;
import gate.alignment.Alignment;
import gate.alignment.AlignmentListener;
import gate.alignment.gui.AlignmentAction;
import gate.alignment.gui.AlignmentEditor;
import gate.alignment.gui.AlignmentTask;
import gate.alignment.gui.AlignmentView;
import gate.alignment.gui.PUAPair;
import gate.compound.CompoundDocument;
import gate.util.OffsetComparator;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Stroke;
import java.awt.event.MouseEvent;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.event.MouseInputAdapter;
import javax.swing.table.DefaultTableModel;

/**
 * This class provides an editor for aligning texts in a compound document.
 * This is a links view which shows pairs in horizontal mode and shows links
 * among the highlight
 */
public class LinksView extends JPanel implements AlignmentListener,
                                     AlignmentView {

  private static final long serialVersionUID = -2867467022258265114L;

  /**
   * scrollpane that holds all the sourcePanel, targetPanel and the linesCanvas
   */
  private JScrollPane waScrollPane;

  /**
   * panel with word alignment GUI components
   */
  private JPanel waPanel;

  /**
   * source panel that has labels for each individual alignment unit in the
   * source parent of alignment unit
   */
  private JPanel sourcePanel;

  /**
   * target panel that has labels for each individual alignment unit in the
   * target parent of alignment unit
   */
  private JPanel targetPanel;

  /**
   * canvas used for drawing links between the alignment units
   */
  private MappingsPanel linesCanvas;

  /**
   * Alignment Task object
   */
  private AlignmentTask alignmentTask;

  /**
   * mappings for annotations and their highlights
   */
  private HashMap<Annotation, AnnotationHighlight> sourceHighlights;

  /**
   * mappings for annotations and their highlights
   */
  private HashMap<Annotation, AnnotationHighlight> targetHighlights;

  /**
   * Remembers the selected annotations
   */
  private List<Annotation> sourceLatestAnnotationsSelection;

  /**
   * Remembers the selected annotations
   */
  private List<Annotation> targetLatestAnnotationsSelection;

  /**
   * A color that is being used for current highlighting
   */
  private Color color;

  /**
   * Default font-size
   */
  public static final int TEXT_SIZE = 20;

  /**
   * annotation highlight with the mouse on it
   */
  private AnnotationHighlight currentAnnotationHightlight = null;

  private CompoundDocument compoundDocument;

  private AlignmentView thisInstance = null;
  
  public LinksView(AlignmentTask task) {
    sourceHighlights = new HashMap<Annotation, AnnotationHighlight>();
    targetHighlights = new HashMap<Annotation, AnnotationHighlight>();
    sourceLatestAnnotationsSelection = new ArrayList<Annotation>();
    targetLatestAnnotationsSelection = new ArrayList<Annotation>();
    thisInstance = this;
    initGui();
    setTarget(task);
  }

  /**
   * Initialize the GUI
   */
  private void initGui() {
    setBorder(BorderFactory.createTitledBorder("Links View"));
    waPanel = new JPanel(new BorderLayout());

    sourcePanel = new JPanel();
    sourcePanel.setLayout(new BoxLayout(sourcePanel, BoxLayout.X_AXIS));
    sourcePanel.setBackground(Color.WHITE);

    targetPanel = new JPanel();
    targetPanel.setLayout(new BoxLayout(targetPanel, BoxLayout.X_AXIS));
    targetPanel.setBackground(Color.WHITE);

    linesCanvas = new MappingsPanel();
    linesCanvas.setBackground(Color.WHITE);
    linesCanvas.setLayout(null);
    linesCanvas.setPreferredSize(new Dimension(200, 50));
    linesCanvas.setOpaque(true);

    waPanel.add(sourcePanel, BorderLayout.NORTH);
    waPanel.add(targetPanel, BorderLayout.SOUTH);
    waPanel.add(linesCanvas, BorderLayout.CENTER);
    waScrollPane = new JScrollPane(waPanel);
    waScrollPane.setPreferredSize(new Dimension(800, 200));
    add(waScrollPane, BorderLayout.CENTER);
    color = AlignmentEditor.getColor(null, 0.5f);
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

    if(sourceLatestAnnotationsSelection != null
            && !sourceLatestAnnotationsSelection.isEmpty()) {

      for(Annotation annotation : sourceLatestAnnotationsSelection) {
        AnnotationHighlight ah = sourceHighlights.get(annotation);
        ah.setHighlighted(false, Color.WHITE);
      }
      sourceLatestAnnotationsSelection.clear();

    }

    if(targetLatestAnnotationsSelection != null
            && !targetLatestAnnotationsSelection.isEmpty()) {

      for(Annotation annotation : targetLatestAnnotationsSelection) {
        AnnotationHighlight ah = targetHighlights.get(annotation);
        ah.setHighlighted(false, Color.WHITE);
      }
      targetLatestAnnotationsSelection.clear();
    }

  }

  /**
   * Executes the given action. It uses the pair that is being currently shown
   * to collect the alignment information which is then used as parameters to
   * call the provided action.
   * 
   * @param aa
   */
  public void executeAction(AlignmentAction aa) {

    // obtaining source and target documents
    Document srcDocument =
            compoundDocument.getDocument(alignmentTask.getSrcDocId());
    Document tgtDocument =
            compoundDocument.getDocument(alignmentTask.getTgtDocId());
    Alignment alignment =
            compoundDocument.getAlignmentInformation(alignmentTask
                    .getUaFeatureName());

    // obtaining selected annotations
    Set<Annotation> srcSelectedAnnots =
            new HashSet<Annotation>(sourceLatestAnnotationsSelection);
    Set<Annotation> tgtSelectedAnnots =
            new HashSet<Annotation>(targetLatestAnnotationsSelection);

    if(currentAnnotationHightlight != null) {

      Set<Annotation> alignedAnnots =
              alignment
                      .getAlignedAnnotations(currentAnnotationHightlight.annotation);
      if(alignedAnnots == null) alignedAnnots = new HashSet<Annotation>();
      alignedAnnots.add(currentAnnotationHightlight.annotation);

      for(Annotation annot : alignedAnnots) {
        Document tempDoc = alignment.getDocument(annot);
        if(tempDoc == srcDocument) {
          srcSelectedAnnots.add(annot);
        } else if(tempDoc == tgtDocument) {
          tgtSelectedAnnots.add(annot);
        }
      }
    }

    alignmentTask.getAlignmentActionsManager().executeAction(this, aa,
            srcSelectedAnnots, tgtSelectedAnnots,
            currentAnnotationHightlight.annotation);
  }

  PUAPair currentPair;
  
  /**
   * This method updates the GUI.
   * 
   * @param docIDsAndAnnots
   */
  public void updateGUI(PUAPair pair) {
    this.currentPair = pair;
    
    // before refreshing, we remove all the highlights
    clearLatestAnnotationsSelection();
    sourcePanel.removeAll();
    sourcePanel.updateUI();

    targetPanel.removeAll();
    targetPanel.updateUI();

    linesCanvas.removeAllEdges();
    linesCanvas.updateUI();
    //linesCanvas.repaint();
    Alignment alignment =
            compoundDocument.getAlignmentInformation(alignmentTask
                    .getUaFeatureName());

    // for each underlying unit of alignment, we create a default
    // annotation highlight.
    sourceHighlights = new HashMap<Annotation, AnnotationHighlight>();
    for(Annotation a : pair.getSourceUnitAnnotations()) {
      String text = pair.getText(a, true);
      AnnotationHighlight ah =
              new AnnotationHighlight(text, Color.WHITE, a, true);
      sourceHighlights.put(a, ah);
      sourcePanel.add(ah);
      sourcePanel.add(Box.createRigidArea(new Dimension(5, 0)));
    }
    sourcePanel.revalidate();
    sourcePanel.updateUI();

    targetHighlights = new HashMap<Annotation, AnnotationHighlight>();
    for(Annotation a : pair.getTargetUnitAnnotations()) {
      String text = pair.getText(a, false);
      AnnotationHighlight ah =
              new AnnotationHighlight(text, Color.WHITE, a, false);
      targetHighlights.put(a, ah);
      targetPanel.add(ah);
      targetPanel.add(Box.createRigidArea(new Dimension(5, 0)));
    }
    targetPanel.revalidate();
    targetPanel.updateUI();

    // we keep record of which annotations are already highlighted in
    // order to not highlight them again
    Set<Annotation> highlightedAnnotations = new HashSet<Annotation>();

    List<Annotation> srcAnnotList =
            new ArrayList<Annotation>(sourceHighlights.keySet());
    Collections.sort(srcAnnotList, new OffsetComparator());

    for(int i = 0; i < srcAnnotList.size(); i++) {
      Annotation srcAnnotation = srcAnnotList.get(i);

      // if not aligned
      if(!alignment.isAnnotationAligned(srcAnnotation)) continue;

      // if already highlighted, don't do it again
      if(highlightedAnnotations.contains(srcAnnotation)) continue;

      Set<Annotation> targetAnnots =
              alignment.getAlignedAnnotations(srcAnnotation);
      targetAnnots.retainAll(targetHighlights.keySet());

      Set<Annotation> sourceAnnots = new HashSet<Annotation>();
      sourceAnnots.add(srcAnnotation);
      highlightedAnnotations.add(srcAnnotation);

      for(int j = i + 1; j < srcAnnotList.size(); j++) {

        Annotation sAnnotation = srcAnnotList.get(j);

        // if not aligned
        if(!alignment.isAnnotationAligned(sAnnotation)) continue;

        // if already highlighted, don't do it again
        if(highlightedAnnotations.contains(sAnnotation)) continue;

        Set<Annotation> alignedAnnots1 =
                alignment.getAlignedAnnotations(sAnnotation);
        alignedAnnots1.retainAll(targetHighlights.keySet());

        if(targetAnnots.containsAll(alignedAnnots1)) {
          sourceAnnots.add(sAnnotation);
          highlightedAnnotations.add(sAnnotation);
        }
      }

      Color newColor = AlignmentEditor.getColor(null, 0.5f);
      boolean firstTime = true;

      for(Annotation srcAnnot : sourceAnnots) {
        AnnotationHighlight sAh = sourceHighlights.get(srcAnnot);
        sAh.setHighlighted(true, newColor);

        for(Annotation tgtAnnot : targetAnnots) {

          AnnotationHighlight ah = targetHighlights.get(tgtAnnot);

          if(firstTime) {
            ah.setHighlighted(true, newColor);
          }

          Edge edge = new Edge();
          edge.srcAH = sAh;
          edge.tgtAH = ah;
          linesCanvas.addEdge(edge);
          linesCanvas.repaint();
        }
        firstTime = false;
      }
    }
  }

  /**
   * Internal class - it represents an alignment unit.
   * 
   * @author niraj
   */
  protected class AnnotationHighlight extends JLabel {

    /**
     * indicates if the annotation is highlighted or not
     */
    boolean highlighted = false;

    /**
     * if the current highlight belongs to the source document
     */
    boolean sourceDocument = false;

    /**
     * color of the highlight
     */
    Color colorToUse = Color.WHITE;

    /**
     * annotation it refers to
     */
    Annotation annotation;

    /**
     * constructor
     * 
     * @param text -
     *          the underlying text of the annotation
     * @param color -
     *          color of the highlight
     * @param annot -
     *          annotation the current highlight refers to
     * @param sourceDocument -
     *          if the current annotation belongs to the source document
     */
    public AnnotationHighlight(String text, Color color, Annotation annot,
            boolean sourceDocument) {
      super(text);
      this.setOpaque(true);
      this.annotation = annot;
      this.sourceDocument = sourceDocument;
      this.colorToUse = color;
      setBackground(this.colorToUse);
      this.addMouseListener(new MouseActionListener());
      setFont(new Font(getFont().getName(), Font.PLAIN, TEXT_SIZE));
    }

    /**
     * sets the annotation highlighted/dehighlighted
     * 
     * @param val
     * @param color
     */
    public void setHighlighted(boolean val, Color color) {
      this.highlighted = val;
      this.colorToUse = color;
      this.setBackground(color);
      this.updateUI();
    }

    public boolean isHighlighted() {
      return this.highlighted;
    }

    public void setHighlightColor(Color color) {
      this.colorToUse = color;
      this.setBackground(color);
      this.updateUI();
    }

    public Color getHighlightColor() {
      return this.colorToUse;
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
        AnnotationHighlight ah = (AnnotationHighlight)me.getSource();
        Point pt = me.getPoint();
        currentAnnotationHightlight = ah;
        Alignment alignment =
                compoundDocument.getAlignmentInformation(alignmentTask
                        .getUaFeatureName());

        if(SwingUtilities.isRightMouseButton(me)) {

          if(alignment.isAnnotationAligned(ah.annotation)) {
            // lets clear the latest selection
            clearLatestAnnotationsSelection();
          } else {
            // if user has right clicked and the unit is not already highlighted
            // highlight it and show the right menu
            // reducing one click for the user
            if(!ah.highlighted) {
              if(color == Color.WHITE)
                color = AlignmentEditor.getColor(null, 0.5f);
              ah.setHighlighted(true, color);
              if(ah.isSourceDocument()) {
                if(sourceLatestAnnotationsSelection == null) {
                  sourceLatestAnnotationsSelection = new ArrayList<Annotation>();
                }

                if(!sourceLatestAnnotationsSelection.contains(ah.annotation))
                  sourceLatestAnnotationsSelection.add(ah.annotation);
              } else {
                if(targetLatestAnnotationsSelection == null) {
                  targetLatestAnnotationsSelection = new ArrayList<Annotation>();
                }
                if(!targetLatestAnnotationsSelection.contains(ah.annotation))
                  targetLatestAnnotationsSelection.add(ah.annotation);
              }
            }
          }

          // we should show the option menu here
          JPopupMenu optionsMenu =
                  alignmentTask.getAlignmentActionsManager().prepareOptionsMenu(
                          thisInstance,
                          alignment.isAnnotationAligned(ah.annotation),
                          ah.highlighted);
          optionsMenu.show(ah, (int)pt.getX(), (int)pt.getY());
          optionsMenu.setVisible(true);
          return;
        }

        // was this annotation highlighted?
        // if yes, remove the highlight
        if(ah.highlighted) {

          // we need to check if the ah is aligned
          // if so, we should prompt user to first reset the
          // alignment
          if(alignment.isAnnotationAligned(ah.annotation)) {
            JOptionPane.showMessageDialog(gate.gui.MainFrame.getInstance(),
                    "To remove this annotation from the current"
                            + " aligment, please use the 'Remove Alignment'"
                            + " from the options menu on right click");
            return;
          }

          // the annotation is not aligned but recently highlighted
          // so remove the highlight
          ah.setHighlighted(false, Color.WHITE);

          if(ah.isSourceDocument()) {
            if(sourceLatestAnnotationsSelection == null) {
              sourceLatestAnnotationsSelection = new ArrayList<Annotation>();
            }

            sourceLatestAnnotationsSelection.remove(ah.annotation);
          } else {
            if(targetLatestAnnotationsSelection == null) {
              targetLatestAnnotationsSelection = new ArrayList<Annotation>();
            }

            targetLatestAnnotationsSelection.remove(ah.annotation);
          }
        } else {
          if(color == Color.WHITE)
            color = AlignmentEditor.getColor(null, 0.5f);
          ah.setHighlighted(true, color);
          if(ah.isSourceDocument()) {
            if(sourceLatestAnnotationsSelection == null) {
              sourceLatestAnnotationsSelection = new ArrayList<Annotation>();
            }

            if(!sourceLatestAnnotationsSelection.contains(ah.annotation))
              sourceLatestAnnotationsSelection.add(ah.annotation);
          } else {
            if(targetLatestAnnotationsSelection == null) {
              targetLatestAnnotationsSelection = new ArrayList<Annotation>();
            }
            if(!targetLatestAnnotationsSelection.contains(ah.annotation))
              targetLatestAnnotationsSelection.add(ah.annotation);
          }
        }

      }

      JPopupMenu menu = new JPopupMenu();

      FeaturesModel model = new FeaturesModel();

      JTable featuresTable = new JTable(model);

      Timer timer = new Timer();

      TimerTask task;

      public void mouseEntered(final MouseEvent me) {
        final AnnotationHighlight ah = (AnnotationHighlight)me.getSource();
        model.setAnnotation(ah.annotation);
        task = new TimerTask() {
          public void run() {
            menu.add(featuresTable);
            menu.show(ah, me.getX(), me.getY() + 10);
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

    public boolean isSourceDocument() {
      return sourceDocument;
    }

  }

  /**
   * listens to the annotationsAligned event and updates the GUI accordingly.
   */
  public void annotationsAligned(Annotation srcAnnotation, String srcAS,
          Document srcDocument, Annotation tgtAnnotation, String tgtAS,
          Document tgtDocument) {

    if(srcAnnotation == null || tgtAnnotation == null || srcDocument == null
            || tgtDocument == null) {
      System.err.println("One of the src/tgt annotation/document is null");
      return;
    }

    AnnotationHighlight sAh = sourceHighlights.get(srcAnnotation);
    AnnotationHighlight tAh = targetHighlights.get(tgtAnnotation);

    // may be not related to current highlight
    if(sAh == null || tAh == null) {
      return;
    }
    
    // otherwise, highlight it
    if(sAh.isHighlighted()) {
      tAh.setHighlighted(true, sAh.colorToUse);
    } else if(tAh.isHighlighted()) {
      sAh.setHighlighted(true, tAh.colorToUse);
    } else {
      Color newColor = AlignmentEditor.getColor(null, 0.5f);
      sAh.setHighlighted(true, newColor);
      tAh.setHighlighted(true, newColor);
    }
    
    
    Edge e = new Edge();
    e.srcAH = sAh;
    e.tgtAH = tAh;
    linesCanvas.addEdge(e);
    linesCanvas.updateUI();
    waPanel.updateUI();
  }

  /**
   * listens to the annotationsUnAligned event and updates the GUI accordingly.
   */

  public void annotationsUnaligned(Annotation srcAnnotation, String srcAS,
          Document srcDocument, Annotation tgtAnnotation, String tgtAS,
          Document tgtDocument) {

    if(srcAnnotation == null || tgtAnnotation == null || srcDocument == null
            || tgtDocument == null) {
      System.err.println("One of the src/tgt annotation/document is null");
      return;
    }

    AnnotationHighlight sAh = sourceHighlights.get(srcAnnotation);
    AnnotationHighlight tAh = targetHighlights.get(tgtAnnotation);

    // may be not related to current highlight
    if(sAh == null || tAh == null) {
      return;
    }

    if(linesCanvas.removeEdges(sAh, tAh)) {
      if(!alignmentTask.getAlignment().isAnnotationAligned(srcAnnotation)) {
        sAh.setHighlighted(false, Color.WHITE);
      }
      
      if(!alignmentTask.getAlignment().isAnnotationAligned(tgtAnnotation)) {
        tAh.setHighlighted(false, Color.WHITE);
      }
    }
    
    linesCanvas.updateUI();
    waPanel.updateUI();
  }

  /**
   * simply refreshes the gui
   */
  public void refresh() {
    if(alignmentTask.current() != null) {
      updateGUI(alignmentTask.current());
    }
  }

  /**
   * internal class to represent link between the source and the target
   * alignment unit.
   * 
   * @author gate
   */
  private class Edge {
    AnnotationHighlight srcAH;

    AnnotationHighlight tgtAH;
  }

  /**
   * canvas that shows lines for every link present in the current pair
   * 
   * @author gate
   */
  private class MappingsPanel extends JPanel {

    /**
     * edges to paint
     */
    private Set<Edge> edges = new HashSet<Edge>();

    /**
     * constructor
     */
    public MappingsPanel() {
      // do nothing
      setOpaque(true);
      setBackground(Color.WHITE);
    }

    /**
     * clears the local cache
     */
    public void removeAllEdges() {
      edges.clear();
    }

    /**
     * clears the local cache
     */
    public boolean removeEdges(Edge e) {
      return edges.remove(e);
    }

    /**
     * clears the local cache
     */
    public boolean removeEdges(AnnotationHighlight sAh, AnnotationHighlight tAh) {
      Edge toRemove = null;
      for(Edge e : edges) {
        if(e.srcAH == sAh && e.tgtAH == tAh) {
          toRemove = e;
          break;
        }
      }
      
      if(toRemove != null) {
        return edges.remove(toRemove);
      }
      return false;
    }

    /**
     * adds a new edge to the panel
     * 
     * @param edge
     */
    public void addEdge(Edge edge) {
      if(edge != null) edges.add(edge);
    }

    /**
     * draws edges
     */
    public void paintComponent(Graphics g) {
      super.paintComponent(g);
      Graphics2D g2d = (Graphics2D)g;
      g2d.setBackground(Color.WHITE);
      g2d.clearRect(0, 0, this.getWidth(), this.getHeight());

      for(Edge e : edges) {
        int x =
                (int)(e.srcAH.getBounds().x + (double)((double)e.srcAH
                        .getBounds().width / 2));
        int y = 0;
        int x1 =
                (int)(e.tgtAH.getBounds().x + (double)((double)e.tgtAH
                        .getBounds().width / 2));
        int y1 = this.getBounds().height;
        Line2D line =
                new Line2D.Double(new Point((int)x, (int)y), new Point((int)x1,
                        (int)y1));
        Stroke stroke = new BasicStroke(2.0f);
        g2d.setStroke(stroke);
        Color c = g2d.getColor();
        g2d.setColor(e.srcAH.getBackground());
        g2d.draw(line);
        g2d.setColor(c);
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

}
