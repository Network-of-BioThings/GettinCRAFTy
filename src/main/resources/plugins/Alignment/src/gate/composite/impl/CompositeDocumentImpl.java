package gate.composite.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import gate.Annotation;
import gate.AnnotationSet;
import gate.Document;
import gate.Factory;
import gate.FeatureMap;
import gate.Resource;
import gate.composite.CombiningMethod;
import gate.composite.CompositeDocument;
import gate.composite.OffsetDetails;
import gate.compound.CompoundDocument;
import gate.corpora.DocumentImpl;
import gate.creole.ResourceInstantiationException;
import gate.event.AnnotationEvent;
import gate.event.AnnotationListener;
import gate.event.AnnotationSetEvent;
import gate.event.AnnotationSetListener;
import gate.event.DocumentEvent;
import gate.event.DocumentListener;
import gate.event.FeatureMapListener;
import gate.util.GateRuntimeException;
import gate.util.InvalidOffsetException;

/**
 * Implementation of the Composite Document.
 * 
 * @author niraj
 */
public class CompositeDocumentImpl extends DocumentImpl implements
                                                       CompositeDocument,
                                                       AnnotationSetListener,
                                                       DocumentListener,
                                                       AnnotationListener,
                                                       FeatureMapListener {

  private static final long serialVersionUID = -1379936764549428131L;

  /**
   * CombiningMethod that was used for creating this document.
   */
  protected CombiningMethod combiningMethod;

  /**
   * A Map that contains offet mappings. This is used for copying and removing
   * annotations to the respective documents.
   */
  protected HashMap<String, List<OffsetDetails>> offsetMappings;

  /**
   * Set of ids of combined documents.
   */
  protected Set<String> combinedDocumentIds;

  /**
   * The compound document which the current composite document belongs to.
   */
  protected CompoundDocument compoundDocument;

  /**
   * When a new annotation is added to the composite document, this parameter
   * tells whether the annotation copying and deletion to their respective
   * documents should be carried out or not.
   */
  protected boolean disableListener = false;

  // init method
  public Resource init() throws ResourceInstantiationException {
    super.init();
    this.getAnnotations().addAnnotationSetListener(this);
    Map<String, AnnotationSet> namedAnnotationSets = this.getNamedAnnotationSets();
    if(namedAnnotationSets != null) {
      Set<String> annotNames = namedAnnotationSets.keySet();
      if(annotNames != null && !annotNames.isEmpty()) {
        Iterator<String> iter = annotNames.iterator();
        while(iter.hasNext()) {
          String asName = (String)iter.next();
          this.getAnnotations(asName).addAnnotationSetListener(this);
        }
      }
    }
    this.addDocumentListener(this);
    this.getFeatures().addFeatureMapListener(this);
    return this;
  }

  /**
   * Gets the combing method used for creating the composite document.
   */
  public CombiningMethod getCombiningMethod() {
    return combiningMethod;
  }

  /**
   * Sets the combining method to be used for creating the composite document.
   */
  public void setCombiningMethod(CombiningMethod combiningMethod) {
    this.combiningMethod = combiningMethod;
  }

  /**
   * Annotation added event
   */
  public void annotationAdded(AnnotationSetEvent ase) {

    if(!disableListener && ase.getSourceDocument() == this) {
      AnnotationSet as = (AnnotationSet)ase.getSource();
      Annotation annot = ase.getAnnotation();
      annot.addAnnotationListener(this);

      FeatureMap features = Factory.newFeatureMap();
      features.putAll(annot.getFeatures());

      boolean defaultAS = as.getName() == null;
      for(String docID : combinedDocumentIds) {
        Document aDoc = compoundDocument.getDocument(docID);
        long stOffset =
          getOffsetInSrcDocument(docID, annot.getStartNode().getOffset()
            .longValue());
        if(stOffset == -1) continue;
        long enOffset =
          getOffsetInSrcDocument(docID, annot.getEndNode().getOffset()
            .longValue());
        if(enOffset == -1) continue;
        Annotation originalAnnot = null;
        try {

          if(defaultAS) {
            Integer id =
              aDoc.getAnnotations().add(new Long(stOffset), new Long(enOffset),
                annot.getType(), features);
            originalAnnot = aDoc.getAnnotations().get(id);
          }
          else {
            Integer id =
              aDoc.getAnnotations(as.getName()).add(new Long(stOffset),
                new Long(enOffset), annot.getType(), features);
            originalAnnot = aDoc.getAnnotations(as.getName()).get(id);
          }
        }
        catch(InvalidOffsetException ioe) {
          System.out.println(aDoc.getName() + "=" + stOffset + "=" + enOffset);
          throw new GateRuntimeException(ioe);
        }

        OffsetDetails od = new OffsetDetails();
        od.setOldStartOffset(stOffset);
        od.setOldEndOffset(enOffset);
        od.setNewStartOffset(annot.getStartNode().getOffset().longValue());
        od.setNewEndOffset(annot.getEndNode().getOffset().longValue());
        od.setOriginalAnnotation(originalAnnot);
        od.setNewAnnotation(annot);
        addNewOffsetDetails(docID, od);
        break;
      }
    }
  }

  /**
   * Annotation remove event
   */
  public void annotationRemoved(AnnotationSetEvent ase) {
    if(!disableListener && ase.getSourceDocument() == this) {
      AnnotationSet as = (AnnotationSet)ase.getSource();
      Annotation annot = ase.getAnnotation();
      FeatureMap features = Factory.newFeatureMap();
      features.putAll(annot.getFeatures());
      
      

      boolean defaultAS = as.getName() == null;
      for(String docID : combinedDocumentIds) {
        Document aDoc = compoundDocument.getDocument(docID);

        // find out the details which refer to the deleted annotation
        OffsetDetails od = getOffsetDetails(docID, as.getName(), annot);
        if(od == null) continue;

        if(defaultAS) {
          aDoc.getAnnotations().remove(od.getOriginalAnnotation());
        }
        else {
          aDoc.getAnnotations(as.getName()).remove(od.getOriginalAnnotation());
        }
        removeOffsetDetails(docID, od);
        break;
      }
    }
  }

  /**
   * This method returns the respective offset in the source document.
   */
  public long getOffsetInSrcDocument(String srcDocumentID, long offset) {
    List<OffsetDetails> list = offsetMappings.get(srcDocumentID);
    if(list == null) return -1;

    for(int i = 0; i < list.size(); i++) {
      OffsetDetails od = list.get(i);
      if(offset >= od.getNewStartOffset() && offset <= od.getNewEndOffset()) {
        // so = 10 eo = 15
        // offset = 15
        // difference = offset - so = 5
        // oso = 0 oeo = 5 newoffset = oso + diff = 0 + 5 = 5
        long difference = offset - od.getNewStartOffset();
        return od.getOldStartOffset() + difference;
      }
    }
    return -1;
  }

  /**
   * This method returns the respective offset in the source document.
   */
  public OffsetDetails getOffsetDetails(String srcDocumentID, String asName,
    Annotation newAnnot) {
    List<OffsetDetails> list = offsetMappings.get(srcDocumentID);
    if(list == null) return null;

    for(int i = 0; i < list.size(); i++) {
      OffsetDetails od = list.get(i);
      if(od.getNewAnnotation() != null
        && od.getNewAnnotation().equals(newAnnot)) return od;
    }
    return null;
  }

  /**
   * This method returns the respective offset in the source document.
   */
  public OffsetDetails getOffsetDetails(String srcDocumentID, Integer id) {
    List<OffsetDetails> list = offsetMappings.get(srcDocumentID);
    if(list == null) return null;

    for(int i = 0; i < list.size(); i++) {
      OffsetDetails od = list.get(i);
      if(od.getNewAnnotation() != null
        && od.getNewAnnotation().getId().equals(id)) return od;
    }
    return null;
  }

  /**
   * This method returns the respective offset in the source document.
   */
  public void addNewOffsetDetails(String srcDocumentID, OffsetDetails od) {
    List<OffsetDetails> list = offsetMappings.get(srcDocumentID);
    if(list == null) {
      list = new ArrayList<OffsetDetails>();
      offsetMappings.put(srcDocumentID, list);
    }

    list.add(od);
  }

  /**
   * This method returns the respective offset in the source document.
   */
  public void removeOffsetDetails(String srcDocumentID, OffsetDetails od) {
    List<OffsetDetails> list = offsetMappings.get(srcDocumentID);
    if(list != null) list.remove(od);
  }

  /**
   * sets the offset mapping information
   */
  public void setOffsetMappingInformation(
    HashMap<String, List<OffsetDetails>> offsetMappings) {
    this.offsetMappings = offsetMappings;
  }

  /**
   * return the IDs of combined documents
   * 
   * @return
   */
  public Set<String> getCombinedDocumentsIds() {
    return this.combinedDocumentIds;
  }

  /**
   * Sets the combined document IDs
   * 
   * @param combinedDocumentIds
   */
  public void setCombinedDocumentsIds(Set<String> combinedDocumentIds) {
    this.combinedDocumentIds = combinedDocumentIds;
  }

  /**
   * This method returns the compoundDocument whose member this composite
   * document is.
   * 
   * @return
   */
  public CompoundDocument getCompoundDocument() {
    return this.compoundDocument;
  }

  /**
   * Sets the compound document.
   * 
   * @param compoundDocument
   */
  public void setCompoundDocument(CompoundDocument compoundDocument) {
    this.compoundDocument = compoundDocument;
  }

  public void annotationSetAdded(DocumentEvent de) {
    Document doc = (Document)de.getSource();
    if(this == doc) {
      doc.getAnnotations(de.getAnnotationSetName()).addAnnotationSetListener(
        this);
    }
  }

  public void annotationSetRemoved(DocumentEvent de) {
    Document doc = (Document)de.getSource();
    if(this == doc) {
      doc.getAnnotations(de.getAnnotationSetName())
        .removeAnnotationSetListener(this);
    }
  }

  public void contentEdited(DocumentEvent de) {
    // do nothing
  }

  public void annotationUpdated(AnnotationEvent e) {
    if(e.getType() == AnnotationEvent.FEATURES_UPDATED) {
      if(!disableListener) {
        Annotation annot = (Annotation)e.getSource();
        // lets find out which annotation set it belongs to
        AnnotationSet as = null;
        if(getAnnotations().contains(annot)) {
          as = getAnnotations();
        }
        else {
          Map ass = getNamedAnnotationSets();
          if(ass == null) return;
          Iterator namesIter = getNamedAnnotationSets().keySet().iterator();
          while(namesIter.hasNext()) {
            String name = (String)namesIter.next();
            as = (AnnotationSet)getNamedAnnotationSets().get(name);
            if(as.contains(annot)) {
              break;
            }
            else as = null;
          }
        }

        if(as == null) return;
        for(String docID : combinedDocumentIds) {
          OffsetDetails od = getOffsetDetails(docID, as.getName(), annot);
          if(od == null) continue;
          Annotation toUse = od.getOriginalAnnotation();
          toUse.setFeatures(annot.getFeatures());
        }
      }
    }
  }

  public void featureMapUpdated() {
    Map<Object, List<List<Integer>>> matches =
      (Map<Object, List<List<Integer>>>)this.getFeatures().get("MatchesAnnots");
    if(matches == null) return;
    for(List<List<Integer>> topList : matches.values()) {
      for(List<Integer> list : topList) {
        Map<String, List<Integer>> newList =
          new HashMap<String, List<Integer>>();
        for(Integer id : list) {
          for(String docID : combinedDocumentIds) {
            // find out the details which refer to the deleted
            // annotation
            OffsetDetails od = getOffsetDetails(docID, id);
            if(od == null) continue;

            // bingo found it
            List<Integer> subMatches = newList.get(docID);
            if(subMatches == null) {
              subMatches = new ArrayList<Integer>();
              newList.put(docID, subMatches);
            }
            subMatches.add(od.getOriginalAnnotation().getId());
          }
        }
        for(String docID : newList.keySet()) {
          Document aDoc = compoundDocument.getDocument(docID);
          Map<Object, List<List<Integer>>> docMatches =
            (Map<Object, List<List<Integer>>>)aDoc.getFeatures().get(
              "MatchesAnnots");
          if(docMatches == null) {
            docMatches = new HashMap<Object, List<List<Integer>>>();
            aDoc.getFeatures().put("MatchesAnnots", docMatches);
          }

          List<List<Integer>> listOfList = docMatches.get(null);
          if(listOfList == null) {
            listOfList = new ArrayList<List<Integer>>();
            docMatches.put(null, listOfList);
          }
          listOfList.add(newList.get(docID));
        }
      }
    }
  }
}
