/*
 *  Copyright (c) 2009--2010 University of Sheffield
 * 
 *  This file is part of GATE (see http://gate.ac.uk/), and is free
 *  software, licenced under the GNU Library General Public License,
 *  Version 2, June 1991 (in the distribution as file licence.html,
 *  and also available at http://gate.ac.uk/gate/licence.html).
 *
 */
package gate.lingpipe;

import gate.*;
import gate.creole.AbstractLanguageAnalyser;
import gate.creole.ExecutionException;
import gate.creole.ResourceInstantiationException;
import gate.util.GateRuntimeException;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import com.aliasi.classify.Classification;
import com.aliasi.classify.LMClassifier;
import com.aliasi.util.AbstractExternalizable;
import gate.creole.metadata.*;

/**
 * A Processing resource to identify language of the document based on
 * LingPipe language identifier classifier. Please download appropriate
 * models from the LingPipe website. see
 * http://alias-i.com/lingpipe/web/models.html
 * 
 * The default model supplied with GATE distribution is based on the
 * Leipzig corpora collection that consists of the data in the following
 * languages: Catalan (cat), Danish (dk), English (en), Estonian (ee),
 * Finnish (fi), French (fr), German (de), Italian (it), Japanese (jp),
 * Korean (kr), Norwegian (no), Sorbian (sorb), Swedish (se), and
 * Turkish (tr).
 * 
 * Should you want to train models on other languages or different
 * dataset, please refer to the URL: *
 * http://alias-i.com/lingpipe/demos/tutorial/langid/read-me.html.
 * 
 * @author niraj
 * 
 */
@CreoleResource(name = "LingPipe Language Identifier PR",
        helpURL = "http://gate.ac.uk/userguide/sec:misc-creole:lingpipe:langid",
        comment = "GATE PR for language identification using LingPipe")
public class LanguageIdentifierPR 
  extends AbstractLanguageAnalyser 
  implements ProcessingResource {

  private static final long serialVersionUID = -4141432815604763890L;
  @SuppressWarnings("unused")
  private static final String __SVNID = "$Id: LanguageIdentifierPR.java 13437 2011-02-11 17:25:45Z adamfunk $";

  
  /** File which contains model for NE */
  protected URL modelFileUrl;

  /** Model file extracted from the URL */
  protected File modelFile;

  /** classifier object */
  protected LMClassifier classifier;

  /** document feature name */
  protected String languageIdFeatureName;
  private String annotationSetName;
  private String annotationType;

  /**
   * Initializes this resource
   * 
   * @return Resource
   * @throws ResourceInstantiationException
   */
  public Resource init() throws ResourceInstantiationException {
    if(modelFileUrl == null)
      throw new ResourceInstantiationException("No model file provided!");

    try {
      modelFile = new File(modelFileUrl.toURI());
    }
    catch(URISyntaxException e) {
      throw new ResourceInstantiationException(e);
    }

    if(modelFile == null || !modelFile.exists()) {
      throw new ResourceInstantiationException("modelFile:"
              + modelFileUrl.toString() + " does not exists");
    }

    try {
      classifier = (LMClassifier) AbstractExternalizable.readObject(modelFile);
    }
    catch(IOException e) {
      throw new ResourceInstantiationException(e);
    }
    catch(ClassNotFoundException e) {
      throw new ResourceInstantiationException(e);
    }

    return this;
  }

  /**
   * Method is executed after the init() method has finished its
   * execution. <BR>
   * 
   * @throws ExecutionException
   */
  public void execute() throws ExecutionException {
    // lets start the progress and initialize the progress counter
    fireProgressChanged(0);

    // If no document provided to process throw an exception
    if(document == null) {
      fireProcessFinished();
      throw new GateRuntimeException("No document to process!");
    }

    // langugage ID feature Name
    if(languageIdFeatureName == null
            || languageIdFeatureName.trim().length() == 0)
      languageIdFeatureName = "lang";

    /* Default behaviour: classify the text of the whole document and 
     * store the result as a document feature.     */
    if ( (annotationType == null) || (annotationType.length() == 0) ) {
      String docText = document.getContent().toString();
      Classification classification = classifier.classify(docText);
      document.getFeatures().put(languageIdFeatureName, classification.bestCategory());
    }
    
    /* Optional behaviour: classify the text underlying each annotation 
     * and store each results as an annotation feature.     */
    else {
      AnnotationSet annotations = document.getAnnotations(annotationSetName).get(annotationType);
      
      for (Annotation annotation : annotations) {
        String text = Utils.stringFor(document, annotation);
        Classification classification = classifier.classify(text);
        annotation.getFeatures().put(languageIdFeatureName, classification.bestCategory());
      }
    }

    // process finished, acknowledge user about this.
    fireProcessFinished();
  }

  
  
  /*  CREOLE PARAMETERS  */
  
  /**
   * Required init parameter.
   */
  @CreoleParameter (comment = "Model file to use for Language Identification",
          defaultValue = "resources/models/langid-leipzig.classifier")
  public void setModelFileUrl(URL modelFileUrl) {
    this.modelFileUrl = modelFileUrl;
  }

  public URL getModelFileUrl() {
    return modelFileUrl;
  }

  
  @Optional
  @RunTime
  @CreoleParameter(comment = "name of document or annotation features for the language identified",
          defaultValue = "lang")
  public void setLanguageIdFeatureName(String languageIdFeatureName) {
    this.languageIdFeatureName = languageIdFeatureName;
  }
  
  public String getLanguageIdFeatureName() {
    return languageIdFeatureName;
  }

  
  @Optional
  @RunTime
  @CreoleParameter(comment = "annotation set used for input/output (ignored for whole-document classification)",
            defaultValue = "")
  public void setAnnotationSetName(String name) {
    this.annotationSetName = name;
  }
  
  public String getAnnotationSetName() {
    return this.annotationSetName;
  }
  
  @Optional
  @RunTime
  @CreoleParameter(comment = "type of annotations to classify; leave blank for whole-document classification", 
          defaultValue = "")
  public void setAnnotationType(String type) {
    this.annotationType = type;
  }
  
  public String getAnnotationType() {
    return this.annotationType;
  }
  
}
