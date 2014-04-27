package gate.alignment.gui.actions.impl;

import gate.Annotation;
import gate.alignment.Alignment;
import gate.alignment.AlignmentException;
import gate.alignment.gui.AlignmentTask;
import gate.alignment.gui.AlignmentView;

import java.util.Set;

/**
 * It uses the highlighted annotations from the editor and aligns them with one
 * other.
 * 
 * @author niraj
 */
public class AlignAction extends AbstractAlignmentAction {

  /**
   * non-javadoc
   * 
   * @see AlignmentAction.execute(...)
   */
  public void executeAlignmentAction(AlignmentView alignmentView, AlignmentTask task,
          Set<Annotation> srcAlignedAnnotations,
          Set<Annotation> tgtAlignedAnnotations, Annotation clickedAnnotation)
          throws AlignmentException {

    Alignment alignment = task.getAlignment();

    // so first of all clear the latestSelection
    alignmentView.clearLatestAnnotationsSelection();

    if(srcAlignedAnnotations == null || srcAlignedAnnotations.isEmpty())
      return;
    if(tgtAlignedAnnotations == null || tgtAlignedAnnotations.isEmpty())
      return;
    for(Annotation srcAnnotation : srcAlignedAnnotations) {
      for(Annotation tgtAnnotation : tgtAlignedAnnotations) {
        if(!alignment.areTheyAligned(srcAnnotation, tgtAnnotation)) {

          if(!alignment.isAnnotationAligned(srcAnnotation)) {
            srcAnnotation.getFeatures().put(
                    Alignment.ALIGNMENT_METHOD_FEATURE_NAME, "manual");
          }

          if(!alignment.isAnnotationAligned(tgtAnnotation)) {
            tgtAnnotation.getFeatures().put(
                    Alignment.ALIGNMENT_METHOD_FEATURE_NAME, "manual");
          }

          alignment.align(srcAnnotation, task.getSrcASName(), task.getSrcDoc(),
                  tgtAnnotation, task.getTgtASName(), task.getTgtDoc());

        }
      }
    }
  }

  /**
   * @return "Align"
   */
  public String getCaption() {
    return "Align";
  }

  /**
   * @return false
   */
  public boolean invokeForAlignedAnnotation() {
    return false;
  }

  /**
   * @return false
   */
  public boolean invokeForUnhighlightedUnalignedAnnotation() {
    return false;
  }

  /**
   * Description of the class
   */
  public String getToolTip() {
    return "Aligns the selected source and target annotations";
  }
}
