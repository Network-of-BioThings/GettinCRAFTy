package gate.alignment.gui;

import gate.Annotation;

/**
 * It represents a pair of two annotations, one from the source document and one from the target document. For example, a pair of two sentences. 
 * @author niraj
 *
 */
public class Pair {
  /**
   * Id of the source document
   */
  private String sourceDocumentId;

  /**
   * ID of the target document
   */
  private String targetDocumentId;

  /**
   * annotation that belongs to the source document
   */
  private Annotation srcAnnotation;

  /**
   * annotation that belongs to the target document
   */
  private Annotation tgtAnnotation;

  /**
   * Constructor
   * @param sourceDocumentId
   * @param srcAnnotation
   * @param targetDocumentId
   * @param tgtAnnotation
   */
  public Pair(String sourceDocumentId, Annotation srcAnnotation,
          String targetDocumentId, Annotation tgtAnnotation) {
    super();
    this.sourceDocumentId = sourceDocumentId;
    this.targetDocumentId = targetDocumentId;
    this.srcAnnotation = srcAnnotation;
    this.tgtAnnotation = tgtAnnotation;
  }

  /**
   * gets the id of the source document
   * @return
   */
  public String getSourceDocumentId() {
    return sourceDocumentId;
  }

  /**
   * gets the id of the target document
   * @return
   */
  public String getTargetDocumentId() {
    return targetDocumentId;
  }

  /**
   * gets the annotation that belongs to the source document
   * @return
   */
  public Annotation getSrcAnnotation() {
    return srcAnnotation;
  }

  /**
   * gets the annotation that belongs to the target document
   * @return
   */
  public Annotation getTgtAnnotation() {
    return tgtAnnotation;
  }
}
