package gate.alignment.gui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import gate.Annotation;
import gate.AnnotationSet;
import gate.Document;
import gate.compound.CompoundDocument;
import gate.util.GateRuntimeException;
import gate.util.InvalidOffsetException;
import gate.util.OffsetComparator;

/**
 * Default implementation of the IteratingMethod interface. Purpose of
 * the IteratingMethod is to allow users to define their own iterating
 * sequence for the alignment editor. For example, it could be a
 * sentence alignment algorithm that decides which sentence in the
 * source language should be paired with which sentence in the target
 * language. Alignment editor takes one pair of annotations (one from
 * the source document and one from the target document) and displays
 * them.
 * 
 * In this implementation, annotations from source and target documents
 * are paired in order of their occurrence in the document. If the
 * unitAnnotationType is set to Sentence for both the source and target
 * documents, Sentence annotations in both the documents are sorted
 * using the gate.util.OffsetComparator. Then first sentence from the
 * sourceDocument is paired with the first sentence in the target
 * document. The same is true for the second, third and for rest of the
 * sentences in both documents.
 * 
 * @author niraj
 */
public class DefaultIteratingMethod implements IteratingMethod {

  /**
   * Offset comparator used for sorting annotations
   */
  private OffsetComparator comparator;

  /**
   * Unit of Alignment in the source document (e.g. Token)
   */
  private String srcTokenAnnotationType;

  /**
   * Unit of alignment in the target document (e.g. Token)
   */
  private String tgtTokenAnnotationType;

  /**
   * ID of the source document)
   */
  private String srcDocumentID;

  /**
   * ID of the target document)
   */
  private String tgtDocumentID;

  /**
   * Internal instance of AASequence that is used for obtaining
   * annotations from the source document one by one.
   */
  private AASequence srcSequence;

  /**
   * Internal instance of AASequence that is used for obtaining
   * annotations from the target document one by one.
   */
  private AASequence tgtSequence;

  /**
   * The document, that is being aligned in the Alignment Editor.
   */
  private CompoundDocument compoundDocument;

  /**
   * Constructor
   */
  public DefaultIteratingMethod() {
    comparator = new OffsetComparator();
  }

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
  public void init(CompoundDocument alignedDocument, String srcDocumentId,
          String tgtDocumentId, String srcInputAS, String tgtInputAS,
          String srcTokenAnnotationType, String tgtTokenAnnotationType,
          String srcUnitAnnotationType, String tgtUnitAnnotationType)
          throws IteratingMethodException {

    this.compoundDocument = alignedDocument;
    this.srcDocumentID = srcDocumentId;
    this.tgtDocumentID = tgtDocumentId;
    this.srcTokenAnnotationType = srcTokenAnnotationType;
    this.tgtTokenAnnotationType = tgtTokenAnnotationType;

    Document doc = compoundDocument.getDocument(srcDocumentId);
    AnnotationSet as = srcInputAS.equals("<null>")
            || srcInputAS.trim().length() == 0 ? doc.getAnnotations() : doc
            .getAnnotations(srcInputAS);
    srcSequence = new AASequence(doc, as, srcUnitAnnotationType);
    doc = compoundDocument.getDocument(tgtDocumentId);
    AnnotationSet as1 = tgtInputAS.equals("<null>")
            || tgtInputAS.trim().length() == 0 ? doc.getAnnotations() : doc
            .getAnnotations(tgtInputAS);
    tgtSequence = new AASequence(doc, as1, tgtUnitAnnotationType);
  }

  /**
   * Retrieves the underlying text for the given annotation in the
   * document with given document id.
   * 
   * @param annot
   * @param documentId
   * @return
   */
  public String getText(Annotation annot, String documentId) {

    try {
      if(documentId.equals(srcDocumentID)) {
        return srcSequence.getText(annot);
      }
      else if(documentId.equals(tgtDocumentID)) {
        return tgtSequence.getText(annot);
      }
    }
    catch(InvalidOffsetException ioe) {
      throw new GateRuntimeException(ioe);
    }

    return null;
  }

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
          String language, String tokenAnnotationType) {
    if(language.equals(srcDocumentID)) {
      return srcSequence.getUnderlyingAnnotations(annot,
              tokenAnnotationType == null
                      ? this.srcTokenAnnotationType
                      : tokenAnnotationType);
    }
    else if(language.equals(tgtDocumentID)) {
      return tgtSequence.getUnderlyingAnnotations(annot,
              tokenAnnotationType == null
                      ? this.tgtTokenAnnotationType
                      : tokenAnnotationType);
    }
    return null;
  }

  /**
   * Cache for currentPair
   */
  private Pair currentPair;

  /**
   * retrieves the next possible pair.
   * 
   * @return
   */
  public Pair next() {
    Pair pair = new Pair(srcDocumentID, srcSequence.next(), tgtDocumentID,
            tgtSequence.next());
    this.currentPair = pair;
    return pair;
  }

  /**
   * retrieves the previous possible pair.
   * 
   * @return
   */
  public Pair previous() {
    Pair pair = new Pair(srcDocumentID, srcSequence.previous(), tgtDocumentID,
            tgtSequence.previous());
    this.currentPair = pair;
    return pair;
  }

  /**
   * returns the current pair.
   * 
   * @return
   */
  public Pair current() {
    return currentPair;
  }

  /**
   * Returns true if there is any next pair available to return.
   * 
   * @return
   */
  public boolean hasNext() {
    return srcSequence.hasNext() && tgtSequence.hasNext();
  }

  /**
   * Return true if there is any previous pair available to return;
   * 
   * @return
   */
  public boolean hasPrevious() {
    return srcSequence.hasPrevious() && tgtSequence.hasPrevious();
  }

  /**
   * Internal class used for maintaining annotation sequences
   * 
   * @author gate
   * 
   */
  class AASequence {
    Document document;
    AnnotationSet set;
    List<Annotation> annotations;

    int counter = -1;

    public AASequence(Document doc, AnnotationSet set, String parentType) {
      this.document = doc;
      this.set = set;
      // collecting all sentences for example
      annotations = new ArrayList<Annotation>(set.get(parentType));
      Collections.sort(annotations, comparator);
    }

    public boolean hasNext() {
      if(counter + 1 < annotations.size()) {
        return true;
      }
      else {
        return false;
      }
    }

    // return next sentence
    public Annotation next() {
      counter++;
      return annotations.get(counter);
    }

    public Annotation previous() {
      counter--;
      return annotations.get(counter);
    }

    public boolean hasPrevious() {
      if(counter - 1 >= 0) {
        return true;
      }
      return false;
    }

    public void reset() {
      counter = -1;
    }

    public AnnotationSet getUnderlyingAnnotations(Annotation parentAnnot,
            String annotationType) {
      return set.getContained(parentAnnot.getStartNode().getOffset(),
              parentAnnot.getEndNode().getOffset()).get(annotationType);
    }

    public String getText(Annotation ann) throws InvalidOffsetException {
      return document.getContent().getContent(ann.getStartNode().getOffset(),
              ann.getEndNode().getOffset()).toString();
    }
  }

}
