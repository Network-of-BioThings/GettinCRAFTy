package gate.alignment;

import gate.Annotation;
import gate.Document;

/**
 * Object wishing to listen to alignment events should implement this
 * interface and register itself to the appropriate alignment object.
 * 
 * @author niraj
 * 
 */
public interface AlignmentListener {

  /**
   * This method is invoked whenever two annotations are aligned with
   * each other.
   * 
   * @param srcAnnotation
   * @param srcAS - annotation set the source annotation belongs to
   * @param srcDocument - document that the source annotation belongs to
   * @param targetAnnotation
   * @param tgtAS - annotation set the target annotation belongs to
   * @param targetDocument - document that the target annotation belongs
   *          to
   */
  public void annotationsAligned(Annotation srcAnnotation, String srcAS,
          Document srcDocument, Annotation targetAnnotation, String tgtAS,
          Document targetDocument);

  /**
   * This method is invoked whenever two annotations are unaligned with
   * each other.
   * 
   * @param srcAnnotation
   * @param srcAS - annotation set the source annotation belongs to
   * @param srcDocument - document that the source annotation belongs to
   * @param targetAnnotation
   * @param tgtAS - annotation set the target annotation belongs to
   * @param targetDocument - document that the target annotation belongs
   *          to
   */
  public void annotationsUnaligned(Annotation srcAnnotation, String srcAS,
          Document srcDocument, Annotation targetAnnotation, String tgtAS,
          Document targetDocument);
}
