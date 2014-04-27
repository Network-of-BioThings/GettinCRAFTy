package gate.alignment.gui.actions.impl;

import gate.Annotation;
import gate.alignment.AlignmentException;
import gate.alignment.gui.AlignmentTask;
import gate.alignment.gui.AlignmentView;

import java.util.Set;

/**
 * It uses the highlighted annotations and dehighlights them.
 * 
 * @author niraj
 * 
 */
public class ResetAction extends AbstractAlignmentAction {
  /**
   * non-javadoc
   * 
   * @see AlignmentAction.execute(...)
   */
  public void executeAlignmentAction(AlignmentView alignmentView, AlignmentTask task,
          Set<Annotation> srcAlignedAnnotations, Set<Annotation> tgtAlignedAnnotations,
          Annotation clickedAnnotation) throws AlignmentException {
    
    alignmentView.clearLatestAnnotationsSelection();

  }

  /**
   * @return "Reset Selection"
   */
  public String getCaption() {
    return "Reset Selection";
  }

  /**
   * @return false
   */
  public boolean invokeForAlignedAnnotation() {
    return false;
  }

  /**
   * return false
   */
  public boolean invokeForUnhighlightedUnalignedAnnotation() {
    return false;
  }

  /**
   * @return Description of the class
   */
  public String getToolTip() {
    return "Dehighlight selected annotations";
  }

}
