package gate.alignment.gui;

import gate.Annotation;
import gate.AnnotationSet;
import gate.compound.CompoundDocument;

/**
 * This class provides various methods that help in alignment process.
 * 
 * @author niraj
 */
public class AlignmentFactory {

  /**
   * Alignments are stored as a document feature by default under this feature name 
   */
  public static final String ALIGNMENT_FEATURE_NAME = "alignment";

  /**
   * The document that is being processed for alignment
   */
  protected CompoundDocument compoundDocument;

  /**
   * An instance of IteratingMethod that decides iterating order
   */
  protected IteratingMethod iteratingMethod;

  /**
   * ID of the source document
   */
  private String srcDocumentID;

  /**
   * ID of the target document
   */
  private String tgtDocumentID;

  /**
   * AlignmentFactory makes alignment easier
   * 
   * @param compoundDocument -> document where we want to achieve
   *          alignment
   * @param inputAS -> name of the inputAnnotationSet
   * @param tokenAnnotationType -> the level at what we want to achieve
   *          alignment (e.g. Token or may be some other annotation
   *          type)
   * @param unitAnnotationType -> AlignedParentAnnotationType (e.g. if
   *          sentences are already aligned)
   * @throws Exception
   */
  @SuppressWarnings("unchecked")
  public AlignmentFactory(CompoundDocument alignedDocument,
          String srcDocumentId, String tgtDocumentId, String srcInputAS,
          String tgtInputAS, String srcTokenAnnotationType,
          String tgtTokenAnnotationType, String srcUnitAnnotationType,
          String tgtUnitAnnotationType, String iteratingMethodClassName)
          throws Exception {

    this.compoundDocument = alignedDocument;
    this.srcDocumentID = srcDocumentId;
    this.tgtDocumentID = tgtDocumentId;

    iteratingMethod = (IteratingMethod)Class.forName(iteratingMethodClassName)
            .newInstance();
    iteratingMethod.init(alignedDocument, srcDocumentId, tgtDocumentId,
            srcInputAS, tgtInputAS, srcTokenAnnotationType,
            tgtTokenAnnotationType, srcUnitAnnotationType,
            tgtUnitAnnotationType);
  }

  /**
   * Gets the text for the given annotation
   * @param annot
   * @param documentID - id of the document that the annot belongs to
   * @return
   */
  public String getText(Annotation annot, String documentID) {
    return iteratingMethod.getText(annot, documentID);
  }

  /**
   * Gets the underlying annotations with the given type under the given annotation
   * @param annot
   * @param documentID - id of the document that the annot belongs to.
   * @param tokenAnnotationType
   * @return
   */
  public AnnotationSet getUnderlyingAnnotations(Annotation annot,
          String language, String tokenAnnotationType) {
    return iteratingMethod.getUnderlyingAnnotations(annot, language, tokenAnnotationType);
  }

  /**
   * Returns the next possible pair
   * @return
   */
  public Pair next() {
    return iteratingMethod.next();
  }

  /**
   * Returns the previous pair
   * @return
   */
  public Pair previous() {
    return iteratingMethod.previous();
  }

  /**
   * Returns the current pair
   * @return
   */
  public Pair current() {
    return iteratingMethod.current();
  }

  /**
   * Returns true if there is any next pair available 
   * @return
   */
  public boolean hasNext() {
    return iteratingMethod.hasNext();
  }

  /**
   * Returns true if there is any previous pair available
   * @return
   */
  public boolean hasPrevious() {
    return iteratingMethod.hasPrevious();
  }

  /**
   * ID of the source document
   * @return
   */
  public String getSrcDocumentID() {
    return srcDocumentID;
  }

  /**
   * ID of the target document
   * @return
   */
  public String getTgtDocumentID() {
    return tgtDocumentID;
  }
}