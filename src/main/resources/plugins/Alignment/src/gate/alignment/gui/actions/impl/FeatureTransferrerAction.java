package gate.alignment.gui.actions.impl;

import gate.Annotation;
import gate.alignment.Alignment;
import gate.alignment.AlignmentActionInitializationException;
import gate.alignment.AlignmentException;
import gate.alignment.gui.AlignmentTask;
import gate.alignment.gui.FinishedAlignmentAction;
import gate.alignment.gui.PUAPair;

import java.util.Set;

/**
 * This actions is useful for transferring features from source
 * annotations to the respective aligned target annotations.
 * 
 * User need to provide names of features to transfer. If none provided,
 * all features are transferred.
 * 
 * @author niraj
 * 
 */
public class FeatureTransferrerAction implements FinishedAlignmentAction {

  /**
   * What feature to use to store in the dictionary. User can provide
   * his/her own feature
   */
  private String[] featureToUse = new String[0];

  /**
   * Is the init called?
   */
  boolean initCalled = false;

  /**
   * the init method. Arguments: names of features to transfer. If none
   * provided, all features are transferred.
   */
  public void init(String[] args) throws AlignmentActionInitializationException {
    if(initCalled) return;
    initCalled = true;

    if(args.length == 0) {
      throw new AlignmentActionInitializationException(
              "provide at least one feature name to transfer");
    }

    featureToUse = new String[args.length];
    for(int i = 0; i < args.length; i++) {
      featureToUse[i] = args[i];
    }
  }

  public void cleanup() {
  }

  public String getToolTip() {
    return "Feature Transferrer";
  }

  public void executeFinishedAlignmentAction(PUAPair pair)
          throws AlignmentException {

    // task
    AlignmentTask task = pair.getAlignmentTask();

    Alignment alignment = task.getAlignment();

    for(Annotation srcAnnot : pair.getSourceUnitAnnotations()) {
      Set<Annotation> tgtAlignedAnnots = alignment
              .getAlignedAnnotations(srcAnnot);
      if(tgtAlignedAnnots == null || tgtAlignedAnnots.isEmpty()) {
        continue;
      }

      for(String feature : featureToUse) {
        Object value = srcAnnot.getFeatures().get(feature);
        if(value == null) continue;
        
        for(Annotation tgtAnnot : tgtAlignedAnnots) {
          Object tcat = tgtAnnot.getFeatures().get(feature);
          if(tcat == null) {
            tcat = value;
          }
          else {
            tcat = tcat.toString() + "," + value.toString();
          }

          tgtAnnot.getFeatures().put(feature, tcat);
        }
      }
    }
  }
}
