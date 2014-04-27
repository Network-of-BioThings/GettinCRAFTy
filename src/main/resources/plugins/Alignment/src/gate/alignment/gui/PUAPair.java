package gate.alignment.gui;

import gate.Annotation;
import gate.AnnotationSet;
import gate.Document;
import gate.Utils;
import gate.util.OffsetComparator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * A pair representing parents of unit of alignment in the source and
 * the target language.
 * 
 * @author niraj
 * 
 */
public class PUAPair {

  /**
   * Alignment task in which these pair is shown
   */
  AlignmentTask alignmentTask;

  /**
   * Source parent of unit of alignment annotations
   */
  Set<Annotation> sourceAnnotations;

  /**
   * Target parent of unit of alignment annotations
   */
  Set<Annotation> targetAnnotations;

  /**
   * indicates whether the alignment has marked finished or not.
   */
  boolean alignmentFinished = false;

  /**
   * Constructor
   * 
   * @param alignmentTask Alignment task in which these pair is shown
   * @param sourceAnnotations source parent of unit of alignment
   *          annotations
   * @param targetAnnotations target parent of unit of alignment
   *          annotations
   */
  public PUAPair(AlignmentTask alignmentTask,
          Set<Annotation> sourceAnnotations, Set<Annotation> targetAnnotations) {
    super();
    this.alignmentTask = alignmentTask;
    this.sourceAnnotations = sourceAnnotations;
    this.targetAnnotations = targetAnnotations;
  }

  /**
   * Returns underlying text annotation
   * 
   * @param annot
   * @param isSrcDocument
   * @return
   */
  public String getText(Annotation annot, boolean isSrcDocument) {
    Document toUse = alignmentTask.getSrcDoc();
    if(!isSrcDocument) {
      toUse = alignmentTask.getTgtDoc();
    }

    return Utils.stringFor(toUse, annot);
  }

  /**
   * Returns unit annotations for the source document
   * 
   * @return
   */
  public List<Annotation> getSourceUnitAnnotations() {
    Set<Annotation> toReturn = new HashSet<Annotation>();

    if(this.sourceAnnotations != null) {
      for(Annotation sAnnotation : sourceAnnotations) {
        AnnotationSet set = alignmentTask.getSrcAS().getContained(
                sAnnotation.getStartNode().getOffset(),
                sAnnotation.getEndNode().getOffset());
        if(set == null || set.isEmpty()) continue;
        toReturn.addAll(set.get(alignmentTask.getUaAnnotType()));
      }
    }
    else {
      toReturn.addAll(alignmentTask.getSrcAS().get(
              alignmentTask.getUaAnnotType()));
    }

    List<Annotation> sortedReturn = new ArrayList<Annotation>(toReturn);
    Collections.sort(sortedReturn, new OffsetComparator());
    return sortedReturn;
  }

  /**
   * Returns unit annotations for the target document
   * 
   * @return
   */
  public List<Annotation> getTargetUnitAnnotations() {
    Set<Annotation> toReturn = new HashSet<Annotation>();

    if(this.targetAnnotations != null) {
      for(Annotation tAnnotation : targetAnnotations) {
        AnnotationSet set = alignmentTask.getTgtAS().getContained(
                tAnnotation.getStartNode().getOffset(),
                tAnnotation.getEndNode().getOffset());
        if(set == null || set.isEmpty()) continue;
        toReturn.addAll(set.get(alignmentTask.getUaAnnotType()));
      }
    }
    else {
      toReturn.addAll(alignmentTask.getTgtAS().get(
              alignmentTask.getUaAnnotType()));
    }

    List<Annotation> sortedReturn = new ArrayList<Annotation>(toReturn);
    Collections.sort(sortedReturn, new OffsetComparator());
    return sortedReturn;
  }

  /**
   * Returns the alignment task
   * 
   * @return
   */
  public AlignmentTask getAlignmentTask() {
    return alignmentTask;
  }

  /**
   * Returns the source annotations in the PUPair
   * 
   * @return
   */
  public Set<Annotation> getSourceAnnotations() {
    return sourceAnnotations;
  }

  /**
   * Returns the target annotations in the PUPair
   * 
   * @return
   */
  public Set<Annotation> getTargetAnnotations() {
    return targetAnnotations;
  }

  /**
   * Indicates if the alignment pair has been marked as finished
   * alignment
   * 
   * @return
   */
  public boolean isAlignmentFinished() {
    return alignmentFinished;
  }

  /**
   * Marks the alignment PUPair as finished
   * 
   * @param alignmentFinished
   */
  public void setAlignmentFinished(boolean alignmentFinished) {
    this.alignmentFinished = alignmentFinished;
  }
}
