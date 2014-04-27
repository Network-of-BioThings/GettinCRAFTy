package gate.alignment.gui;

import gate.Annotation;
import gate.AnnotationSet;
import gate.compound.CompoundDocument;

import java.io.Serializable;

/**
 * This interface declares a method that needs to be implemented by the
 * implementors of this interface. The purpose of this interface is to
 * allow users to define their own iterating sequence for the alignment
 * editor. For example, it could be a sentence alignment algorithm.
 * 
 * @author niraj
 */
public interface IteratingMethod extends Serializable {

  /**
   * This method, given necessary parameters, initialises the internal
   * resources.
   * 
   * @param compoundDocument - source document for which alignment is
   *          taking place
   * @param srcDocumentId - id of the source document
   * @param tgtDocumentId - id of the target document
   * @param srcInputAS - annotation set in the source document which to
   *          take annotations from
   * @param tgtInputAS - annotation set in the target document which to
   *          take annotations from
   * @param srcTokenAnnotationType - e.g. Token for word alignment
   * @param tgtTokenAnnotationType - e.g. Token for word alignment
   * @param srcUnitAnnotationType - e.g. Sentence for sentence alignment
   * @param tgtUnitAnnotationType - e.g. Sentence for sentence alignment
   * @throws IteratingMethodException - could throw exception if
   *           something is wrong with the parameters or with the source
   *           and target documents.
   */
  public void init(CompoundDocument compoundDocument, String srcDocumentId,
          String tgtDocumentId, String srcInputAS, String tgtInputAS,
          String srcTokenAnnotationType, String tgtTokenAnnotationType,
          String srcUnitAnnotationType, String tgtUnitAnnotationType)
          throws IteratingMethodException;

  /**
   * Retrieves the underlying text for the given annotation in the
   * document with given document id.
   * 
   * @param annot
   * @param documentId
   * @return
   */
  public String getText(Annotation annot, String documentId);

  /**
   * Similar to getContained method of the gate.AnnotationSet.
   * 
   * @param annot - this method uses annot.getStartNode().getOffset()
   *          and annot.getEndNode().getOffset() to decide boundaries.
   * @param documentId - id of the document to be used in the compound
   *          document.
   * @param annotationType - type of the annotations to be retrieved
   * @return gate.AnnotationSet with annotations of type annotationType
   */
  public AnnotationSet getUnderlyingAnnotations(Annotation annot,
          String documentId, String annotationType);

  /**
   * retrieves the next possible pair. 
   * @return
   */
  public Pair next();

  /**
   * retrieves the previous pair.
   * @return
   */
  public Pair previous();

  /**
   * Returns the last retrieved pair.
   * @return
   */
  public Pair current();

  /**
   * Returns true if there is any next pair available to return.
   * @return
   */
  public boolean hasNext();

  /**
   * Return true if there is any previous pair available to return;
   * @return
   */
  public boolean hasPrevious();

}
