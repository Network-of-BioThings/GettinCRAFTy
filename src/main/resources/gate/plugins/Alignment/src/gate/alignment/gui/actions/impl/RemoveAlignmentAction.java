package gate.alignment.gui.actions.impl;

import gate.Annotation;
import gate.alignment.Alignment;
import gate.alignment.AlignmentException;
import gate.alignment.gui.AlignmentTask;
import gate.alignment.gui.AlignmentView;

import java.util.Set;

/**
 * It uses the highlighted annotations from the editor and unaligns them with
 * one other.
 * 
 * @author niraj
 */
public class RemoveAlignmentAction extends AbstractAlignmentAction {

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

    // alignment object
    if(srcAlignedAnnotations == null || srcAlignedAnnotations.isEmpty())
      return;
    if(tgtAlignedAnnotations == null || tgtAlignedAnnotations.isEmpty())
      return;
    for(Annotation srcAnnotation : srcAlignedAnnotations) {
      for(Annotation tgtAnnotation : tgtAlignedAnnotations) {
        if(alignment.areTheyAligned(srcAnnotation, tgtAnnotation)) {
          alignment.unalign(srcAnnotation, task.getSrcASName(), task
                  .getSrcDoc(), tgtAnnotation, task.getTgtASName(), task
                  .getTgtDoc());

          if(alignment.getAlignedAnnotations(srcAnnotation).size() == 0) {
            srcAnnotation.getFeatures().remove(
                    Alignment.ALIGNMENT_METHOD_FEATURE_NAME);
          } 

          if(alignment.getAlignedAnnotations(tgtAnnotation).size() == 0) {
            tgtAnnotation.getFeatures().remove(
                    Alignment.ALIGNMENT_METHOD_FEATURE_NAME);
          }
        }
      }
    }
    alignmentView.clearLatestAnnotationsSelection();
  }

  /**
   * @return "Remove Alignment"
   */
  public String getCaption() {
    return "Remove Alignment";
  }

  /**
   * @return false
   */
  public boolean invokeForHighlightedUnalignedAnnotation() {
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
    return "Removes the alignment for selected annotations";
  }

}
