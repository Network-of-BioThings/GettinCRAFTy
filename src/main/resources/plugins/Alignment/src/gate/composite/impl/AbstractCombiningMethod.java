package gate.composite.impl;

import gate.Annotation;
import gate.AnnotationSet;
import gate.Document;
import gate.Factory;
import gate.FeatureMap;
import gate.Gate;
import gate.Utils;
import gate.composite.CombiningMethod;
import gate.composite.CombiningMethodException;
import gate.composite.CompositeDocument;
import gate.composite.OffsetDetails;
import gate.compound.CompoundDocument;
import gate.corpora.DocumentImpl;
import gate.creole.ResourceInstantiationException;
import gate.util.InvalidOffsetException;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

/**
 * Abstract implementation of the combining method. Classes extending
 * this class must use startDocument() before adding any content (i.e.
 * addContent) and must finalizeDocument() at the end of all additions.
 * 
 * @author niraj
 */
public abstract class AbstractCombiningMethod implements CombiningMethod {

  protected HashMap<String, List<OffsetDetails>> offsetMappings;

  protected StringBuffer documentContent;

  protected String toAdd;

  protected CompoundDocument containerDocument;

  protected List<OffsetDetails> annotations;

  protected List<OffsetDetails> offsets;

  protected Set<String> annotationTypesToCopy;

  private boolean startDocumentCalled = false;

  protected boolean debug = false;

  /**
   * User must call this method to start a composite document
   * 
   * @param containerDocument - instance of compound document that the
   *          new composite is going to become member of.
   * @param annotationTypesToCopy - list of types of annotations to copy
   *          underlying the unit annotation. Supply null to copy all
   *          the annotations. Supply an empty set to copy nothing.
   */
  protected void startDocument(CompoundDocument containerDocument,
          Set<String> annotationTypesToCopy) throws CombiningMethodException {
    if(debug) {
      System.out.println("Start Document called");
    }
    
    offsetMappings = new HashMap<String, List<OffsetDetails>>();
    this.containerDocument = containerDocument;
    this.annotationTypesToCopy = annotationTypesToCopy;
    this.annotations = new ArrayList<OffsetDetails>();
    this.offsets = new ArrayList<OffsetDetails>();
    documentContent = new StringBuffer();
    toAdd = "<?xml version=\"1.0\"?><composite>";
    startDocumentCalled = true;

    if(debug) {
      System.out.println("Exiting Start Document");
    }
    
  }

  protected CompositeDocument finalizeDocument()
          throws CombiningMethodException {
    if(debug) {
      System.out.println("FinalizeDocument called");
    }

    if(!startDocumentCalled)
      throw new CombiningMethodException(
              "CompositeDocument is not initialized - please "
                      + "call the startDocument() method to initialize the "
                      + "composite document");

    XMLOutputFactory outputFactory = XMLOutputFactory.newInstance();
    String encoding = containerDocument.getEncoding();
    if(encoding == null) encoding = "UTF-8";
    StringWriter sw = new StringWriter();
    try {

      XMLStreamWriter xsw = outputFactory.createXMLStreamWriter(sw);
      xsw.writeStartDocument(encoding, "1.0");
      xsw.writeStartElement("", "composite");
      char[] result = documentContent.toString().toCharArray();
      replaceXMLIllegalCharacters(result);
      xsw.writeCharacters(new String(result));
      xsw.writeEndElement();
      xsw.writeEndDocument();
      xsw.close();
    }
    catch(XMLStreamException e2) {
      throw new CombiningMethodException(e2);
    }

    CompositeDocument doc = null;
    try {
      FeatureMap features = Factory.newFeatureMap();
      features.put("collectRepositioningInfo", containerDocument
              .getCollectRepositioningInfo());
      features.put("encoding", encoding);
      features.put("markupAware", new Boolean(true));
      features.put("preserveOriginalContent", containerDocument
              .getPreserveOriginalContent());
      features.put(DocumentImpl.DOCUMENT_STRING_CONTENT_PARAMETER_NAME, sw
              .toString());
      FeatureMap subFeatures = Factory.newFeatureMap();
      Gate.setHiddenAttribute(subFeatures, true);

      doc = (CompositeDocument)Factory.createResource(
              "gate.composite.impl.CompositeDocumentImpl", features,
              subFeatures);
    }
    catch(ResourceInstantiationException e1) {
      throw new CombiningMethodException(e1);
    }

    ((gate.composite.impl.CompositeDocumentImpl)doc).disableListener = true;

    // lets add all annotations now
    for(OffsetDetails od : annotations) {
      // obtain annotation set to add annotations to
      AnnotationSet aSet = od.getAsName() == null
              || od.getAsName().trim().length() == 0
              ? doc.getAnnotations()
              : doc.getAnnotations(od.getAsName());
      String type = od.getOriginalAnnotation().getType();
      gate.FeatureMap f = od.getOriginalAnnotation().getFeatures();
      Integer id;
      try {
        id = aSet.add(new Long(od.getNewStartOffset()), new Long(od
                .getNewEndOffset()), type, f);
        od.setNewAnnotation(aSet.get(id));
      }
      catch(InvalidOffsetException e) {
        System.out.println("Offsets :" + od.getNewStartOffset() + "=>"
                + od.getNewEndOffset());
        throw new CombiningMethodException(e);
      }
    }
    ((gate.composite.impl.CompositeDocumentImpl)doc).disableListener = false;

    doc.setCombiningMethod(this);
    doc.setOffsetMappingInformation(offsetMappings);
    doc.setCombinedDocumentsIds(new HashSet<String>(containerDocument
            .getDocumentIDs()));
    doc.setCompoundDocument(containerDocument);
    if(debug) {
      System.out.println("Exiting FinalizDocument");
    }
    
    return doc;
  }

  /**
   * Returns the Ids of combined documents
   * 
   * @return
   */
  public Set<String> getCombinedDocumentsIds() {
    return offsetMappings.keySet();
  }

  /**
   * This method returns the new offset for where the content was added
   * 
   * @param srcDocument
   * @param inputAS
   * @param unitAnnotation
   * @param copyUnderlyingAnnotations
   * @return
   */
  protected long[] addContent(Document srcDocument, Annotation unitAnnotation)
          throws CombiningMethodException {
    if(debug) {
      System.out.println("AddContent called");
    }

    if(!startDocumentCalled)
      throw new CombiningMethodException(
              "CompositeDocument is not initialized - please "
                      + "call the startDocument() method to initialize the "
                      + "composite document");

    String documentID = srcDocument.getName();
    offsets = offsetMappings.get(documentID);
    if(offsets == null) {
      offsets = new ArrayList<OffsetDetails>();
      offsetMappings.put(documentID, offsets);
    }

    OffsetDetails offset = new OffsetDetails();
    offset.setOldStartOffset(unitAnnotation.getStartNode().getOffset()
            .longValue());
    offset.setOldEndOffset(unitAnnotation.getEndNode().getOffset().longValue());
    offset.setNewStartOffset(documentContent.length());
    documentContent.append(gate.Utils.contentFor(srcDocument, unitAnnotation));
    offset.setNewEndOffset(documentContent.length());
    offset.setOriginalAnnotation(unitAnnotation);
    offsets.add(offset);
    annotations.add(offset);

    if(debug) {
      System.out.println("Unit annotation:" + unitAnnotation.getType() + "=>"
              + offset.getOldStartOffset() + "=>" + offset.getOldEndOffset()
              + "=>" + offset.getNewStartOffset() + "=>"
              + offset.getNewEndOffset());
    }

    OffsetDetails unitAnnotDetails = new OffsetDetails();
    unitAnnotDetails.setOldStartOffset(offset.getOldStartOffset());
    unitAnnotDetails.setOldEndOffset(offset.getOldEndOffset());
    unitAnnotDetails.setNewStartOffset(offset.getNewStartOffset());
    unitAnnotDetails.setNewEndOffset(offset.getNewEndOffset());
    offsets.add(unitAnnotDetails);

    if(annotationTypesToCopy == null || !annotationTypesToCopy.isEmpty()) {

      if(debug) {
        System.out
                .println("copying annotations from the default Annotation set");
      }
      // copy annotations under the default annotation set
      copyAnnotations(srcDocument.getAnnotations(), unitAnnotation, offset);

      // copy annotations from all the named annotation set
      Map<String, AnnotationSet> annotationSets = srcDocument
              .getNamedAnnotationSets();
      if(annotationSets != null) {
        for(String asName : annotationSets.keySet()) {
          if(debug) {
            System.out.println("copying annotations from the :" + asName
                    + " Annotation set");
          }
          copyAnnotations(srcDocument.getAnnotations(asName), unitAnnotation,
                  offset);
        }
      }
    }
    documentContent.append("\n");
    if(debug) {
      System.out.println("Exiting AddContent");
    }

    return new long[] {unitAnnotDetails.getNewStartOffset(),
        unitAnnotDetails.getNewEndOffset()};
  }

  private void copyAnnotations(AnnotationSet inputAS,
          Annotation unitAnnotation, OffsetDetails boundaries) {
    if(debug) {
      System.out.println("CopyAnnotations called");
    }

    if(debug) {
      System.out.println("Obtaning annotations between :"
              + Utils.start(unitAnnotation) + " and "
              + Utils.end(unitAnnotation));
    }
    AnnotationSet tempSet = inputAS.getContained(Utils.start(unitAnnotation),
            Utils.end(unitAnnotation));
    if(annotationTypesToCopy != null && !annotationTypesToCopy.isEmpty()) {
      tempSet = tempSet.get(annotationTypesToCopy);
    }

    Iterator<Annotation> iter = tempSet.iterator();
    while(iter.hasNext()) {

      Annotation anAnnot = iter.next();
      if(anAnnot == unitAnnotation) continue;
      Long start = Utils.start(anAnnot);
      Long end = Utils.end(anAnnot);
      if(start < boundaries.getOldStartOffset() || start > boundaries.getOldEndOffset())
        continue;
      
      if(end < boundaries.getOldStartOffset() || end > boundaries.getOldEndOffset())
        continue;
      
      OffsetDetails anOffset = new OffsetDetails();
      anOffset.setOldStartOffset(start);
      anOffset.setOldEndOffset(end);

      long stDiff = anOffset.getOldStartOffset()
              - boundaries.getOldStartOffset();
      long len = anOffset.getOldEndOffset() - anOffset.getOldStartOffset();

      anOffset.setNewStartOffset(boundaries.getNewStartOffset() + stDiff);
      anOffset.setNewEndOffset(anOffset.getNewStartOffset() + len);
      anOffset.setOriginalAnnotation(anAnnot);

      if(debug) {
        System.out.println("\tCopied" + anAnnot.getType() + "="
                + anOffset.getOldStartOffset() + "="
                + anOffset.getOldEndOffset() + "="
                + anOffset.getNewStartOffset() + "="
                + anOffset.getNewEndOffset());
      }

      // this will be interned - making it easier to store and less
      // expensive
      anOffset.setAsName(inputAS.getName());
      offsets.add(anOffset);
      annotations.add(anOffset);
    }
    if(debug) {
      System.out.println("Exiting copy contents");
    }
    
  }

  static void replaceXMLIllegalCharacters(char[] buf) {
    for(int i = 0; i < buf.length; i++) {
      if(buf[i] <= 0x0008 || buf[i] == 0x000B || buf[i] == 0x000C
              || (buf[i] >= 0x000E && buf[i] <= 0x001F)) {
        buf[i] = ' ';
        continue;
      }

      // buf[i) is a high surrogate...
      if(buf[i] >= 0xD800 && buf[i] <= 0xDBFF) {
        // if we're not at the end of the buffer we can look ahead
        if(i < buf.length - 1) {
          // followed by a low surrogate is OK
          if(buf[i + 1] >= 0xDC00 && buf[i + 1] <= 0xDFFF) {
            continue;
          }
        }
        buf[i] = ' ';
        continue;
      }

      // buf[i) is a low surrogate...
      if(buf[i] >= 0xDC00 && buf[i] <= 0xDFFF) {
        // if we're not at the start of the buffer we can look behind
        if(i > 0) {
          // preceded by a high surrogate is OK
          if(buf[i - 1] >= 0xD800 && buf[i - 1] <= 0xDBFF) {
            continue;
          }
        }

        buf[i] = ' ';
        continue;
      }

      // buf[i) is a BOM character
      if(buf[i] == 0xFFFE || buf[i] == 0xFFFF) {
        buf[i] = ' ';
        continue;
      }
    }
  }
}
