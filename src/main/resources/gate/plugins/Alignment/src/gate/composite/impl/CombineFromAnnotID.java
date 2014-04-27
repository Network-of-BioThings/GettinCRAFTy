package gate.composite.impl;

import gate.Annotation;
import gate.AnnotationSet;
import gate.Document;
import gate.composite.CombiningMethodException;
import gate.composite.CompositeDocument;
import gate.compound.CompoundDocument;

import java.util.Map;

/**
 * User can specify which of the annotation should be copied to the
 * Compsite document.
 * 
 * @author niraj
 */
public class CombineFromAnnotID extends AbstractCombiningMethod {

  private static final long serialVersionUID = 4050197561715800118L;

  public static final String ANNOTATION_ID_FEATURE_NAME = "annotationID";

  public static final String INPUT_AS_NAME_FEATURE_NAME = "inputASName";

  public static final String DOCUMENT_ID_FEATURE_NAME = "documentID";

  /**
   * The parameters must contain four parameters as illustrated below:
   * <p>
   * map.put(ANNOTATION_ID_FEATURE_NAME,annotation.getId());
   * <p>
   * map.put(INPUT_AS_NAME_FEATURE_NAME,"Key");
   * <p>
   * map.put(DOCUMENT_ID_FEATURE_NAME,document.getName());
   * <p>
   * Provide null value to copy all the annotations. Provide empty set
   * to copy none - otherwise the listed annotation types will be
   * copied.
   */
  @SuppressWarnings("unchecked")
  public CompositeDocument combine(CompoundDocument compoundDocument,
          Map<String, Object> parameters) throws CombiningMethodException {

    // params
    Integer annotationID = (Integer)parameters.get(ANNOTATION_ID_FEATURE_NAME);
    String inputASName = (String)parameters.get(INPUT_AS_NAME_FEATURE_NAME);
    String documentID = (String)parameters.get(DOCUMENT_ID_FEATURE_NAME);
    

    if(debug) {
      System.out.println("Combine method called");
      System.out.println("\tannotationID" + annotationID);
      System.out.println("\tinputASName" + inputASName);
      System.out.println("\tdocumentID" + documentID);
    }

    // start a document
    startDocument(compoundDocument, annotationTypesToCopy);

    Document adoc = compoundDocument.getDocument(documentID);
    if(debug) {
      System.out.println("obtaining :" + adoc.getName()
              + " from the compound document");
    }
    
    AnnotationSet inputAS = inputASName == null
            || inputASName.trim().length() == 0 ? adoc.getAnnotations() : adoc
            .getAnnotations(inputASName);
    Annotation annot = inputAS.get(annotationID);
    if(annot == null)
      throw new CombiningMethodException("annotation with id :" + annotationID
              + " could not be found");
    // add content
    addContent(adoc, annot);

    // finalize document
    return finalizeDocument();
  }
}
