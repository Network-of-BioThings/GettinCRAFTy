package gate.composite.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import gate.Annotation;
import gate.Document;
import gate.composite.CombiningMethodException;
import gate.composite.CompositeDocument;
import gate.compound.CompoundDocument;
import gate.compound.impl.AnnotationStream;
import gate.util.OffsetComparator;

/**
 * Default Implementation of the Combining Method. This method requires
 * three parameters.
 * <p>
 * unitAnnotationType
 * <p>
 * inputASName
 * <p>
 * copyUnderlyingAnnotations.
 * <p>
 * unit Annotation type is the unit of combining two texts. E.g. if it
 * is set to "Sentence", one "Sentence" annotation from each document
 * member is considered and put together in the composite document.
 * <p>
 * inputASName tells the method from which annotation set the
 * annotations should be used from.
 * <p>
 * if set to true the copyUnderlyingAnnotations, all the underlying
 * annotations are copied to the composite document.
 * 
 * @author niraj
 */
public class DefaultCombiningMethod extends AbstractCombiningMethod {

  private static final long serialVersionUID = 4050197561715800118L;

  /**
   * The parameters must contain two parameters "unitAnnotationType" and
   * "inputASName" and "copyUnderlyingAnnotations" The parameter names
   * are Case Sensitive. Example:
   * <p>
   * map.put("unitAnnotationType","Sentence");
   * <p>
   * map.put("inputASName","Key");
   * <p>
   * map.put("copyUnderlyingAnnotations","true");
   */
  public CompositeDocument combine(CompoundDocument compoundDocument,
          Map parameters) throws CombiningMethodException {
    try {

      // parameters
      String unitAnnotationType = (String)parameters.get("unitAnnotationType");
      if(unitAnnotationType == null || unitAnnotationType.trim().length() == 0)
        throw new CombiningMethodException("unitAnnotationType cannot be null");

      String inputASName = (String)parameters.get("inputASName");
      String copy = (String)parameters.get("copyUnderlyingAnnotations");
      boolean copyUnderlyingAnnotations = copy == null
              ? false
              : Boolean.parseBoolean((String)parameters
                      .get("copyUnderlyingAnnotations"));
      Set<String> annotationTypesToCopy = null;
      if(!copyUnderlyingAnnotations)
        annotationTypesToCopy = new HashSet<String>();

      
      // initialize startDocument
      startDocument(compoundDocument, annotationTypesToCopy);

      // obtain a list of documentIDs
      List<String> documentIDs = compoundDocument.getDocumentIDs();
      int total = 0;
      for(int i = 0; i < documentIDs.size(); i++) {
        String documentID = documentIDs.get(i);
        Document doc = compoundDocument.getDocument(documentID);
        if(doc instanceof CompositeDocument) continue;
        total++;
      }

      AnnotationStream streams[] = new AnnotationStream[total];
      for(int i = 0, j = 0; i < documentIDs.size() && j < total; i++, j++) {
        String documentID = documentIDs.get(i);
        Document doc = compoundDocument.getDocument(documentID);
        if(doc instanceof CompositeDocument) {
          j--;
          continue;
        }
        streams[j] = new AnnotationStream(doc, inputASName, unitAnnotationType,
                documentID, new OffsetComparator());
      }

      boolean breaked = false;
      while(true) {
        for(int i = 0; i < streams.length; i++) {
          Annotation annot = streams[i].next();
          if(annot == null) {
            breaked = true;
            break;
          }

          String docID = streams[i].getLanguage();
          Document doc = compoundDocument.getDocument(docID);
          // adding it to the composite document
          addContent(doc, annot);
        }
        if(breaked) break;
      }
      
      // finalize document
      return finalizeDocument();
    }
    catch(Exception e) {
      throw new CombiningMethodException(e);
    }
  }
}