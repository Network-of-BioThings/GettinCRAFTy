package gate.composite.impl;

import gate.Annotation;
import gate.AnnotationSet;
import gate.Corpus;
import gate.CorpusController;
import gate.Document;
import gate.Factory;
import gate.FeatureMap;
import gate.Gate;
import gate.LanguageAnalyser;
import gate.ProcessingResource;
import gate.Resource;
import gate.composite.CombiningMethod;
import gate.composite.CombiningMethodException;
import gate.composite.CompositeDocument;
import gate.compound.CompoundDocument;
import gate.compound.impl.CompoundDocumentImpl;
import gate.creole.AbstractLanguageAnalyser;
import gate.creole.ExecutionException;
import gate.creole.ResourceInstantiationException;
import gate.util.GateRuntimeException;
import gate.util.OffsetComparator;
import gate.util.Out;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * As the name suggests, the PR is useful processing segments of the text. Given
 * a analyser, annotation type and a document, this PR creates a composite
 * documents for every annotation with type as specified by the <annotation
 * type>. Since the composite documents are linked with their original
 * documents, when the PR processing the composite document, the composite
 * document takes care of transferring relevant annotations back to the original
 * document. This is a good way of processing just a segment of a document.
 * 
 * @author niraj
 */
public class SegmentProcessingPR extends AbstractLanguageAnalyser implements
                                                                 ProcessingResource {
  /**
   * Controller that should be used to process segments.
   */
  private LanguageAnalyser analyser;

  /**
   * annotation type that the segment is annotated with.
   */
  private String segmentAnnotationType;

  /**
   * Only the annotation that has the specified feature should be considered for
   * annotating.
   */
  private String segmentAnnotationFeatureName;

  /**
   * Only the annotation that has the feature specified as
   * segmentAnnotationFeatureName with the value of
   * segmentAnnotaitonFeatureValue then only the annotation is considered for
   * annotating.
   */
  private String segmentAnnotationFeatureValue;

  /**
   * Annotation set that contains the segment annotation and the annotations to
   * be copied to the composite document.
   */
  private String inputASName;

  /**
   * Used internally - this is the document that will be used for holding the
   * original document and the composite documents.
   */
  private CompoundDocument compoundDoc;

  /**
   * Method used for creating a new composite document.
   */
  protected CombiningMethod combiningMethodInst;

  private boolean debug = false;

  /** Initialise this resource, and return it. */
  public Resource init() throws ResourceInstantiationException {
    // a combining method that creates a composite document with the
    // annotation as identified by the annotation id
    combiningMethodInst = new CombineFromAnnotID();
    compoundDoc = new CompoundDocumentImpl();
    // initializing an empty compound document
    compoundDoc.init();
    return this;
  }

  /* this method is called to reinitialize the resource */
  public void reInit() throws ResourceInstantiationException {
    // reinitialization code
    init();
  }

  /**
   * Should be called to execute this PR on a document.
   */
  public void execute() throws ExecutionException {
    // if no document provided
    if(document == null) { throw new ExecutionException("Document is null!"); }
    // annotation set to use
    AnnotationSet set =
        inputASName == null || inputASName.trim().length() == 0 ? document
            .getAnnotations() : document.getAnnotations(inputASName);
    AnnotationSet segmentSet = set.get(segmentAnnotationType);
    if(set.isEmpty()) {
      Out.prln("Could not find annotations of type: " + segmentAnnotationType
          + " in the document: " + document.getName());
      return;
    }
    String originalDocument = document.getName();
    if(document instanceof CompoundDocument) {
      if(debug) {
        System.out
            .println("Document is a compound document and using the memeber \""
                + document.getName() + "\" for processing");
      }
      compoundDoc.addDocument(document.getName(),
          ((CompoundDocument)document).getCurrentDocument());
    } else {
      if(debug) {
        System.out.println("Document is a normal GATE document with name \""
            + document.getName() + "\"");
      }
      // add the current document as a member of the compound document
      compoundDoc.addDocument(document.getName(), document);
    }
    Corpus tempCorpus = null;
    Corpus oldCorpus = analyser.getCorpus();
    Document oldDoc = analyser.getDocument();

    try {
      Map<String, Object> map = new HashMap<String, Object>();
      map.put(CombineFromAnnotID.INPUT_AS_NAME_FEATURE_NAME, inputASName);
      map.put(CombineFromAnnotID.DOCUMENT_ID_FEATURE_NAME, document.getName());
      FeatureMap hideMap = Factory.newFeatureMap();
      Gate.setHiddenAttribute(hideMap, true);
      tempCorpus =
          (Corpus)Factory.createResource("gate.corpora.CorpusImpl",
              Factory.newFeatureMap(), hideMap, "compoundDocCorpus");
      tempCorpus.add(compoundDoc);
      analyser.setDocument(compoundDoc);
      analyser.setCorpus(tempCorpus);
      List<Annotation> segmentList = new ArrayList<Annotation>(segmentSet);
      Collections.sort(segmentList, new OffsetComparator());
      for(Annotation annotation : segmentList) {
        if(debug) {
          System.out.println("Processing annotation" + annotation.getType()
              + "=>" + annotation.getId());
        }
        // only consider the annotation if it has a specific feature and a value
        if(segmentAnnotationFeatureName != null
            && segmentAnnotationFeatureName.length() != 0
            && segmentAnnotationFeatureValue != null
            && segmentAnnotationFeatureValue.length() != 0) {
          Object value =
              annotation.getFeatures().get(segmentAnnotationFeatureName);
          if(value == null || !value.equals(segmentAnnotationFeatureValue)) {
            continue;
          }
        }
        String nameForCompositeDoc = "Composite" + Gate.genSym();
        map.put(CombineFromAnnotID.ANNOTATION_ID_FEATURE_NAME,
            annotation.getId());
        CompositeDocument compositeDoc = null;
        try {
          if(debug) {
            System.out.println("Creating temp composite document:"
                + nameForCompositeDoc);
          }
          compositeDoc = combiningMethodInst.combine(compoundDoc, map);
          compositeDoc.setName(nameForCompositeDoc);
          compoundDoc.addDocument(nameForCompositeDoc, compositeDoc);
          // change focus to composite document
          compoundDoc.setCurrentDocument(nameForCompositeDoc);
          // now run the application on the composite document
          analyser.execute();
        } catch(CombiningMethodException e) {
          throw new ExecutionException(e);
        } finally {
          // finally get rid of the composite document
          compoundDoc.removeDocument(nameForCompositeDoc);
          if(compositeDoc != null) {
            gate.Factory.deleteResource(compositeDoc);
          }
        }
      }
    } catch(ResourceInstantiationException e) {
      throw new ExecutionException(e);
    } finally {
      // make sure you are resetting the reference
      analyser.setCorpus(oldCorpus);
      analyser.setDocument(oldDoc);

      compoundDoc.removeDocument(originalDocument);
      if(tempCorpus != null) {
        // clear the corpus before deleting it
        tempCorpus.clear();
        gate.Factory.deleteResource(tempCorpus);
      }
    }
  }

  /**
   * Gets the set analyser. The analyser is used for processing the segmented
   * document.
   * 
   * @return
   */
  public LanguageAnalyser getAnalyser() {
    return analyser;
  }

  /**
   * Sets the analyser. The analyser is used for processing the segmented
   * document.
   * 
   * @param analyser
   */
  public void setAnalyser(LanguageAnalyser controller) {
    this.analyser = controller;
  }

  /**
   * Sets the analyser. The analyser is used for processing the segmented
   * document.
   * 
   * @param analyser
   */
  @Deprecated
  public void setController(CorpusController controller) {
    if(!(controller instanceof LanguageAnalyser)) { throw new GateRuntimeException(
        "controller must be of type LanguageAnalyser"); }
    setAnalyser((LanguageAnalyser)controller);
  }

  /**
   * Annotation type that has been used for segmenting the document. The PR uses
   * annotations of this type to create new composite documents and process them
   * individually.
   * 
   * @return
   */
  public String getSegmentAnnotationType() {
    return segmentAnnotationType;
  }

  /**
   * Annotation type that has been used for segmenting the document. The PR uses
   * annotations of this type to create new composite documents and process them
   * individually.
   * 
   * @param unitAnnotationType
   */
  public void setSegmentAnnotationType(String segmentAnnotationType) {
    this.segmentAnnotationType = segmentAnnotationType;
  }

  /**
   * Annotation set to use for obtaining segment annotations and the annotations
   * to copy into the composite document.
   * 
   * @return
   */
  public String getInputASName() {
    return inputASName;
  }

  /**
   * Annotation set to use for obtaining segment annotations and the annotations
   * to copy into the composite document.
   * 
   * @param inputASName
   */
  public void setInputASName(String inputAS) {
    this.inputASName = inputAS;
  }

  public String getSegmentAnnotationFeatureName() {
    return segmentAnnotationFeatureName;
  }

  public void setSegmentAnnotationFeatureName(
      String segmentAnnotationFeatureName) {
    this.segmentAnnotationFeatureName = segmentAnnotationFeatureName;
  }

  public String getSegmentAnnotationFeatureValue() {
    return segmentAnnotationFeatureValue;
  }

  public void setSegmentAnnotationFeatureValue(
      String segmentAnnotationFeatureValue) {
    this.segmentAnnotationFeatureValue = segmentAnnotationFeatureValue;
  }

  /**
   * must clean up the original compound document.
   */
  public void cleanup() {
    super.cleanup();
    Factory.deleteResource(compoundDoc);
  }
} // class SegmentProcessingPR
