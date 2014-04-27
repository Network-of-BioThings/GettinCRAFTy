/*
 *  Copyright (c) 1995-2012, The University of Sheffield. See the file
 *  COPYRIGHT.txt in the software or at http://gate.ac.uk/gate/COPYRIGHT.txt
 *
 *  This file is part of GATE (see http://gate.ac.uk/), and is free
 *  software, licenced under the GNU Library General Public License,
 *  Version 2, June 1991 (in the distribution as file licence.html,
 *  and also available at http://gate.ac.uk/gate/licence.html).
 *
 *  Valentin Tablan 19/11/2002
 *
 *  $Id: MachineLearningPR.java 7307 2006-03-09 13:33:20 +0000 (Thu, 09 Mar 2006) ian_roberts $
 *
 */
package gate.creole.ml;

import java.util.*;

import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import gate.*;
import gate.creole.*;
import gate.gui.ActionsPublisher;
import gate.util.*;

/**
 * This processing resource is used to train a machine learning algorithm with
 * data extracted from a corpus.
 */

public class MachineLearningPR extends AbstractLanguageAnalyser
                       implements gate.gui.ActionsPublisher{

  public MachineLearningPR(){
    actionList = new ArrayList();
    actionList.add(null);
  }

  /**
   * This will make sure that any resources allocated by an ml wrapper get
   * released. This is needed in the case of those wrappers that call
   * native code, as in such cases there is a need to realese dynamically
   * allocated memory.
   */
  public void cleanup() {
    // First call cleanup in the parent, in case any clean up needs to be done
    // there.
    super.cleanup();

    // So long as an ML Engine (wrapper) is associated with the processing
    // resource, call its cleanup method.
    if (engine!=null) {
      engine.cleanUp();
    }
  }

  /** Initialise this resource, and return it. */
  public Resource init() throws ResourceInstantiationException {
    if(configFileURL == null){
      throw new ResourceInstantiationException(
        "No configuration file provided!");
    }

    org.jdom.Document jdomDoc;
    SAXBuilder saxBuilder = new SAXBuilder(false);
    try {
    try{
      jdomDoc = saxBuilder.build(configFileURL);
    }catch(JDOMException jde){
      throw new ResourceInstantiationException(jde);
    }
    } catch (java.io.IOException ex) {
      throw new ResourceInstantiationException(ex);
    }

    //go through the jdom document to extract the data we need
    Element rootElement = jdomDoc.getRootElement();
    if(!rootElement.getName().equals("ML-CONFIG"))
      throw new ResourceInstantiationException(
        "Root element of dataset defintion file is \"" + rootElement.getName() +
        "\" instead of \"ML-CONFIG\"!");

    //create the dataset defintion
    Element datasetElement = rootElement.getChild("DATASET");
    if(datasetElement == null) throw new ResourceInstantiationException(
      "No dataset definition provided in the configuration file!");
    try{
      datasetDefinition = new DatasetDefintion(datasetElement);
    }catch(GateException ge){
      throw new ResourceInstantiationException(ge);
    }

    //create the engine
    Element engineElement = rootElement.getChild("ENGINE");
    if(engineElement == null) throw new ResourceInstantiationException(
      "No engine option provided in the configuration file!");
    Element engineClassElement = engineElement.getChild("WRAPPER");
    if(engineClassElement == null) throw new ResourceInstantiationException(
      "No ML engine class provided!");
    String engineClassName = engineClassElement.getTextTrim();
    try{
      // load MLEngine class from GATE Classloader
      Class engineClass =
        Class.forName(engineClassName, true, Gate.getClassLoader());
      engine = (MLEngine)engineClass.newInstance();
    }catch(ClassNotFoundException cnfe){
      throw new ResourceInstantiationException(
        "ML engine class:" + engineClassName + "not found!");
    }catch(IllegalAccessException iae){
      throw new ResourceInstantiationException(iae);
    }catch(InstantiationException ie){
      throw new ResourceInstantiationException(ie);
    }

    // See if batch classification mode had been set.
    if (engineElement.getChild("BATCH-MODE-CLASSIFICATION") == null) {
      batchModeClassification = false;
    } else {
      // checks wether the engine supports batch mode
      // engines must implement AdvancedMLEngine (extending MLengine)
      // to be asked about this functionality
      if (engine instanceof AdvancedMLEngine){
        batchModeClassification = ((AdvancedMLEngine)engine).supportsBatchMode();
      }
      else batchModeClassification = false;
    }

    engine.setDatasetDefinition(datasetDefinition);
    engine.setOptions(engineElement.getChild("OPTIONS"));
    engine.setOwnerPR(this);
    try{
      engine.init();
    }catch(GateException ge){
      throw new ResourceInstantiationException(ge);
    }

    return this;
  } // init()


  /**
   * Run the resource.
   */
  public void execute() throws ExecutionException {
    interrupted = false;
    //check the input
    if (document == null) {
      throw new ExecutionException(
          "No document provided!"
          );
    }

    if (inputASName == null ||
        inputASName.equals(""))
      annotationSet = document.getAnnotations();
    else
      annotationSet = document.getAnnotations(inputASName);

    if (training.booleanValue()) {
      fireStatusChanged(
          "Collecting training data from " + document.getName() + "...");
    }
    else {
      fireStatusChanged(
          "Applying ML model to " + document.getName() + "...");
    }
    fireProgressChanged(0);
    AnnotationSet anns = annotationSet.
                         get(datasetDefinition.getInstanceType());
    annotations = (anns == null || anns.isEmpty()) ?
                  new ArrayList() : new ArrayList(anns);
    Collections.sort(annotations, new OffsetComparator());
    Iterator annotationIter = annotations.iterator();
    int index = 0;
    int size = annotations.size();

    //create the cache structure
    cache = new Cache();

    if (!batchModeClassification || training.booleanValue()) {
      // This code covers the case when instances are going to be passed to
      // the wrapper one at a time, which is always the case with training,
      // and the case with classification when we are not using batch mode.
      while (annotationIter.hasNext()) {
        Annotation instanceAnn = (Annotation) annotationIter.next();
        List attributeValues = new ArrayList(datasetDefinition.
                                             getAttributes().size());
        //find the values for all attributes
        Iterator attrIter = datasetDefinition.getAttributes().iterator();
        while (attrIter.hasNext()) {
          Attribute attr = (Attribute) attrIter.next();
          if (attr.isClass && !training.booleanValue()) {
            //we're not training so the class will be undefined
            attributeValues.add(null);
          }
          else {
            attributeValues.add(cache.getAttributeValue(index, attr));
          }
        }

        if (training.booleanValue()) {
          engine.addTrainingInstance(attributeValues);
        }
        else {
          Object result = engine.classifyInstance(attributeValues);
          if (result instanceof Collection) {
            Iterator resIter = ( (Collection) result).iterator();
            while (resIter.hasNext())
              updateDocument(resIter.next(), index);
          }
          else {
            updateDocument(result, index);
          }
        }

        cache.shift();
        //every 10 instances fire an event
        if (index % 10 == 0) {
          fireProgressChanged(index * 100 / size);
          if (isInterrupted())
            throw new ExecutionInterruptedException();
        }
        index++;
      }

    }
    else {
      // This code covers the case when all the instances in a document will be 
      // passed to the
      // wrapper as a batch. This is necessary to achieve efficient performance
      // with some wrappers.

      // This list is needed to collect all the test instances.
      List instancesToBeClassified = new ArrayList();

      while (annotationIter.hasNext()) {
        Annotation instanceAnn = (Annotation) annotationIter.next();
        List attributeValues = new ArrayList(datasetDefinition.
                                             getAttributes().size());
        //find the values for all attributes
        Iterator attrIter = datasetDefinition.getAttributes().iterator();
        while (attrIter.hasNext()) {
          Attribute attr = (Attribute) attrIter.next();
          if (attr.isClass) {
            //we're not training so the class will be undefined
            attributeValues.add(null);
          }
          else {
            attributeValues.add(cache.getAttributeValue(index, attr));
          }
        }

        // Instead of classifying the instance, just add it to the list of
        // instances that need classifying.
        instancesToBeClassified.add(attributeValues);

        cache.shift();

        index++;
      }

      // Now all the data is collected in instances to be classified, we can
      // actually get the wrapper to classify them.
      List classificationResults = engine.batchClassifyInstances(
          instancesToBeClassified);

      // Now go through the document and add all the annotations appropriately,
      // given the output of the wrapper.

      // Start with the first instance again.
      index = 0;
      Iterator resultsIterator = classificationResults.iterator();
      while (resultsIterator.hasNext()) {

        Object result = resultsIterator.next();
        if (result instanceof Collection) {
          Iterator resIter = ( (Collection) result).iterator();
          while (resIter.hasNext())
            updateDocument(resIter.next(), index);
        }
        else {
          updateDocument(result, index);
        }

        // Move index on so that it points at the next instance.
        index++;
      }
    }
    annotations = null;
  } // execute()


  protected void updateDocument(Object classificationResult, int instanceIndex){
    //interpret the result according to the attribute semantics
    Attribute classAttr = datasetDefinition.getClassAttribute();
    String type = classAttr.getType();
    String feature = classAttr.getFeature();
    List classValues = classAttr.getValues();
    FeatureMap features = Factory.newFeatureMap();
    boolean shouldCreateAnnotation = true;
    if(classValues != null && !classValues.isEmpty()){
      //nominal attribute -> AnnotationType.feature
      //the result is the value for the feature
      String featureValue = (String)classificationResult;
      features.put(feature, featureValue);
    }else{
      if(feature == null){
        //boolean attribute
        shouldCreateAnnotation = classificationResult.equals("true");
      }else{
        //numeric attribute
        String featureValue = classificationResult.toString();
        features.put(feature, featureValue);
      }
    }

    if(shouldCreateAnnotation){
      //generate the new annotation
      int coveredInstanceIndex = instanceIndex + classAttr.getPosition();
      if(coveredInstanceIndex >= 0 &&
         coveredInstanceIndex < annotations.size()){
        Annotation coveredInstance = (Annotation)annotations.
                                     get(coveredInstanceIndex);
        annotationSet.add(coveredInstance.getStartNode(),
                          coveredInstance.getEndNode(),
                          type, features);
      }
    }
  }


  /**
   * Gets the list of actions that can be performed on this resource.
   * @return a List of Action objects (or null values)
   */
  public List getActions(){
    List result = new ArrayList();
    result.addAll(actionList);
    if(engine instanceof ActionsPublisher){
      result.addAll(((ActionsPublisher)engine).getActions());
    }
    return result;
  }

  protected class Cache{
    public Cache(){
      //find the sizes for the two caches
      int forwardCacheSize = 0;
      int backwardCacheSize = 0;
      Iterator attrIter = datasetDefinition.getAttributes().iterator();
      while(attrIter.hasNext()){
        Attribute anAttribute = (Attribute)attrIter.next();
        if(anAttribute.getPosition() > 0){
          //forward looking
          if(anAttribute.getPosition() > forwardCacheSize){
            forwardCacheSize = anAttribute.getPosition();
          }
        }else if(anAttribute.getPosition() < 0){
          //backward looking
          if(-anAttribute.getPosition() > backwardCacheSize){
            backwardCacheSize = -anAttribute.getPosition();
          }
        }
      }
      //create the caches filled with null values
      forwardCache = new ArrayList(forwardCacheSize);
      for(int i =0; i < forwardCacheSize; i++) forwardCache.add(null);
      backwardCache = new ArrayList(backwardCacheSize);
      for(int i =0; i < backwardCacheSize; i++) backwardCache.add(null);
    }

    /**
     * Finds the value of a specified attribute for a particular instance.
     * @param instanceIndex the index of the current instance in the annotations
     * List.
     * @param attribute the attribute whose value needs to be found
     * @return a String representing the value for the attribute.
     */
    public String getAttributeValue(int instanceIndex, Attribute attribute){
      //sanity check
      int actualPosition = instanceIndex + attribute.getPosition();
      if(actualPosition < 0 || actualPosition >= annotations.size()) return null;

      //check caches first
      if(attribute.getPosition() == 0){
        //current instance
        if(currentAttributes == null) currentAttributes = new HashMap();
        return getValue(attribute, instanceIndex, currentAttributes);
      }else if(attribute.getPosition() > 0){
        //check forward cache
        Map attributesMap = (Map)forwardCache.get(attribute.getPosition() - 1);
        if(attributesMap == null){
          attributesMap = new HashMap();
          forwardCache.set(attribute.getPosition() - 1, attributesMap);
        }
        return getValue(attribute, actualPosition, attributesMap);
      }else if(attribute.getPosition() < 0){
        //check bacward cache
        Map attributesMap = (Map)backwardCache.get(-attribute.getPosition() - 1);
        if(attributesMap == null){
          attributesMap = new HashMap();
          backwardCache.set(-attribute.getPosition() - 1, attributesMap);
        }
        return getValue(attribute, actualPosition, attributesMap);
      }
      //we should never get here
      throw new LuckyException(
        "Attribute position is neither 0, nor negative nor positive!");
    }

    /**
     * Notifies the cache that it should advance its internal structures one
     * step forward.
     */
    public void shift(){
      if(backwardCache.isEmpty()){
        //no backward caching, all attributes have position "0" or more
        //nothing to do
      }else{
        backwardCache.remove(backwardCache.size() - 1);
        backwardCache.add(0, currentAttributes);
      }
      if(forwardCache.isEmpty()){
        //no forward caching, all attributes have position "0" or less
        if(currentAttributes != null) currentAttributes.clear();
      }else{
        currentAttributes = (Map) forwardCache.remove(0);
        forwardCache.add(null);
      }
    }

    /**
     * Finds the value for a particular attribute and returns it.
     * If the value is not present in the cache it will be retrieved from the
     * document and the cache will be updated.
     * @param attribute the attribute whose value is requested.
     * @param cache the Map containing the cache for the appropriate position
     * for the attribute
     * @param instanceIndex the index of the instance annotation which is
     * covered by the sought attribute
     * @return a String value.
     */
    protected String getValue(Attribute attribute,
                              int instanceIndex,
                              Map cache){
      String value = null;
      String annType = attribute.getType();
      String featureName = attribute.getFeature();
      Map typeData = (Map)cache.get(annType);
      if(typeData != null){
        if(featureName == null){
          //we're only interested in the presence of the annotation
          value = (String)typeData.get(null);
        }else{
          value = (String)typeData.get(featureName);
        }
      }else{
        //type data was null -> nothing known about this type of annotations
        //get the insformation; update the cache and return the right value
        Annotation instanceAnnot = (Annotation)annotations.get(instanceIndex);
       
        typeData = new HashMap();
        cache.put(annType, typeData);
        // The annotation retrieved by its index is in a default type
    	// (default : Token). We need to search for overlapping types
    	// only if the Type needed is not the one we already have
    	// (which seems quite reasonable given that most Attributes are
    	// likely to be based on Token informations)
    	
    	if (instanceAnnot.getType().equals(annType)){
    		typeData.putAll(instanceAnnot.getFeatures());
    		typeData.put(null, "true");
    		
    		String stringvalue = (String)typeData.get(featureName);
    		if(featureName == null) return "true";
    		return stringvalue;
    	}
    	
    	// here we search for annotations of another type
    	// first restrict to the needed type
    	// then limit to those covering the current token
    	AnnotationSet typeSubset = annotationSet.get(annType);
    	AnnotationSet coverSubset = null;
    	if (typeSubset!=null) coverSubset =	typeSubset.get(
                annType,
                instanceAnnot.getStartNode().getOffset(),
                instanceAnnot.getEndNode().getOffset());
    	
        if(coverSubset == null || coverSubset.isEmpty()){
          //no such annotations at given location
          typeData.put(null, "false");
          if(featureName == null) value = "false";
          else value = null;
        }else{
          typeData.putAll(((Annotation)coverSubset.iterator().next()).
                          getFeatures());
          typeData.put(null, "true");
          if(featureName == null) value = "true";
          else value = (String)typeData.get(featureName);
        }
      }
      return value;
    }

    /**
     * Stores cached data with attribute values for instances placed
     * <b>after</b> the current instance.
     * For each instance (i.e. for each position in the list) the data is a Map
     * with annotationTypes as keys. For each annotation type the data stored is
     * another Map with feature names as keys and feature values as values.
     * The <tt>null</tt> key is used for a boolean value (stored as one of the
     * &quot;true&quot; or &quot;false&quot; strings) signifying the presence
     * (or lack of presence) of the required type of annotation at the location.
     * forwardCache[2].get("Lookup").get(null) == "false" means that no lookup
     * annotation covers the second instance to the right from the current
     * instance.
     */
    protected List forwardCache;

    /**
     * Stores cached data with attribute values for instances placed
     * <b>before</b> the current instance.
     * For each instance (i.e. for each position in the list) the data is a Map
     * with annotationTypes as keys. For each annotation type the data stored is
     * another Map with feature names as keys and feature values as values.
     * The <tt>null</tt> key is used for a boolean value (stored as one of the
     * &quot;true&quot; or &quot;false&quot; strings) signifying the presence
     * (or lack of presence) of the required type of annotation at the location.
     * backwardCache[2].get("Lookup").get(null) == "false" means that no lookup
     * annotation covers the second instance to the left from the current
     * instance.
     */
    protected List backwardCache;

    /**
     * A Map
     * with annotationTypes as keys. For each annotation type the data stored is
     * another Map with feature names as keys and feature values as values.
     * The <tt>null</tt> key is used for a boolean value (stored as one of the
     * &quot;true&quot; or &quot;false&quot; strings) signifying the presence
     * (or lack of presence) of the required type of annotation at the location.
     * currentAttributes.get(Lookup).get(null) == "false" means that the current
     * instance is not covered by a Lookup annotation.
     * currentAttributes.get(Lookup) == null menas nothing is known about Lookup
     * annotations caovering the current instance.
     */
    protected Map currentAttributes;

  }


  public void setInputASName(String inputASName) {
    this.inputASName = inputASName;
  }
  public String getInputASName() {
    return inputASName;
  }
  public java.net.URL getConfigFileURL() {
    return configFileURL;
  }
  public void setConfigFileURL(java.net.URL configFileURL) {
    this.configFileURL = configFileURL;
  }
  public void setTraining(Boolean training) {
    this.training = training;
  }
  public Boolean getTraining() {
    return training;
  }
  public MLEngine getEngine() {
    return engine;
  }
  public void setEngine(MLEngine engine) {
    this.engine = engine;
  }

  private java.net.URL configFileURL;
  protected DatasetDefintion datasetDefinition;

  protected MLEngine engine;

  protected String inputASName;

  protected AnnotationSet annotationSet;

  protected List annotations;

  protected List actionList;

  protected Cache cache;
  private Boolean training;

  /**
   * This member will be set to true if instances are to be passed to the
   * wrapper in batches, rather than one instance at a time and if the engine
   * supports this functionality.
   */
  protected boolean batchModeClassification;
}
