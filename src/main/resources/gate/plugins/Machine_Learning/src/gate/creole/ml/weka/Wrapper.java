/*
 *  Copyright (c) 1995-2012, The University of Sheffield. See the file
 *  COPYRIGHT.txt in the software or at http://gate.ac.uk/gate/COPYRIGHT.txt
 *
 *  This file is part of GATE (see http://gate.ac.uk/), and is free
 *  software, licenced under the GNU Library General Public License,
 *  Version 2, June 1991 (in the distribution as file licence.html,
 *  and also available at http://gate.ac.uk/gate/licence.html).
 *
 *  Valentin Tablan 21/11/2002
 *  
 *  Modified by: Mahesh Joshi
 *  The changes include uncommenting of the SaveDatasetAsArff function
 *  and related UI action.
 *  
 *  Also, the module now supports pure dataset accumulation,
 *  without mandating the presence of a classifier or an output
 *  dataset file.
 *
 *  $Id: Wrapper.java 7030 2005-11-12 14:17:39 +0000 (Sat, 12 Nov 2005) julien_nioche $
 *
 */
package gate.creole.ml.weka;

import java.io.*;
import java.util.*;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import org.jdom.Element;
import weka.classifiers.Classifier;
import weka.classifiers.UpdateableClassifier;
import weka.classifiers.meta.FilteredClassifier;
import weka.core.*;
import weka.filters.Filter;
import gate.ProcessingResource;
import gate.creole.ExecutionException;
import gate.creole.ResourceInstantiationException;
import gate.creole.ml.DatasetDefintion;
import gate.creole.ml.AdvancedMLEngine;
import gate.event.StatusListener;
import gate.gui.ActionsPublisher;
import gate.gui.MainFrame;
import gate.util.*;

/**
 * Wrapper class for the WEKA Machine Learning Engine.
 * 
 * @see <a href="http://www.cs.waikato.ac.nz/ml/weka/">WEKA homepage</a>
 */
public class Wrapper implements AdvancedMLEngine, ActionsPublisher {
  public Wrapper() {
    actionsList = new ArrayList();
    actionsList.add(new LoadModelAction());
    actionsList.add(new SaveModelAction());
    actionsList.add(null);
    actionsList.add(new LoadDatasetFromArffAction());
    actionsList.add(new SaveDatasetAsArffAction());
  }
  /**
   * No clean up is needed for this wrapper, so this is just added because its
   * in the interface.
   */
  public void cleanUp() {
  }
  public void setOptions(Element optionsElem) {
    this.optionsElement = optionsElem;
  }
  /**
   * Some wrappers allow batch classification, but this one doesn't, so if it's
   * ever called just inform the user about this by throwing an exception.
   * 
   * @param instances
   *          This parameter is not used.
   * @return Nothing is ever returned - an exception is always thrown.
   * @throws ExecutionException
   */
  public List batchClassifyInstances(java.util.List instances) throws ExecutionException {
    throw new ExecutionException("The Weka wrapper does not support " + "batch classification. Remove the "
            + "<BATCH-MODE-CLASSIFICATION/> entry " + "from the XML configuration file and " + "try again.");
  }
  public void addTrainingInstance(List attributeValues) throws ExecutionException {
    Instance instance = buildInstance(attributeValues);
    addTrainingInstance(instance);
  }
  protected void addTrainingInstance(Instance instance) throws ExecutionException {
    if(classifier != null){
      if(classifier instanceof UpdateableClassifier){
        // the classifier can learn on the fly; we need to update it
        try{
          ((UpdateableClassifier)classifier).updateClassifier(instance);
        }catch(Exception e){
          throw new GateRuntimeException("Could not update updateable classifier! Problem was:\n" + e.toString());
        }
      }else{
        // the classifier is not updatebale; we need to mark the dataset as
        // changed
        dataset.add(instance);
        datasetChanged = true;
      }
    }
    if(datasetFile != null){
      // write the new instance to the file
      try{
        FileWriter fw = new FileWriter(datasetFile, true);
        fw.write(instance.toString() + "\n");
        fw.flush();
        fw.close();
      }catch(IOException ioe){
        throw new ExecutionException(ioe);
      }
    }
    // if only accumulating dataset,
    // create a cumulative dataset definition, unless
    // cleared by user
    if(onlyAccumulateDataset == true){
      dataset.add(instance);
    }
  }
  /**
   * Constructs an instance valid for the current dataset from a list of
   * attribute values.
   * 
   * @param attributeValues
   *          the values for the attributes.
   * @return an {@link weka.core.Instance} value.
   */
  protected Instance buildInstance(List attributeValues) throws ExecutionException {
    // sanity check
    if(attributeValues.size() != datasetDefinition.getAttributes().size()){ throw new ExecutionException(
            "The number of attributes provided is wrong for this dataset!"); }
    double[] values = new double[datasetDefinition.getAttributes().size()];
    int index = 0;
    Iterator attrIter = datasetDefinition.getAttributes().iterator();
    Iterator valuesIter = attributeValues.iterator();
    Instance instance = new Instance(attributeValues.size());
    instance.setDataset(dataset);
    while(attrIter.hasNext()){
      gate.creole.ml.Attribute attr = (gate.creole.ml.Attribute)attrIter.next();
      String value = (String)valuesIter.next();
      if(value == null){
        instance.setMissing(index);
      }else{
        if(attr.getFeature() == null){
          // boolean attribute ->the value should already be true/false
          instance.setValue(index, value);
        }else{
          // nominal, numeric or string attribute
          if(attr.getValues() != null){
            // nominal or string
            if(attr.getValues().isEmpty()){
              // string attribute
              instance.setValue(index, value);
            }else{
              // nominal attribute
              if(attr.getValues().contains(value)){
                instance.setValue(index, value);
              }else{
                Out.prln("Warning: invalid value: \"" + value + "\" for attribute " + attr.getName() + " was ignored!");
                instance.setMissing(index);
              }
            }
          }else{
            // numeric attribute
            try{
              double db = Double.parseDouble(value);
              instance.setValue(index, db);
            }catch(Exception e){
              Out.prln("Warning: invalid numeric value: \"" + value + "\" for attribute " + attr.getName()
                      + " was ignored!");
              instance.setMissing(index);
            }
          }
        }
      }
      index++;
    }
    return instance;
  }
  public void setDatasetDefinition(DatasetDefintion definition) {
    this.datasetDefinition = definition;
  }
  public Object classifyInstance(List attributeValues) throws ExecutionException {
    Instance instance = buildInstance(attributeValues);
    // double result;
    try{
      if(classifier instanceof UpdateableClassifier){
        return convertAttributeValue(classifier.classifyInstance(instance));
      }else{
        if(datasetChanged){
          if(sListener != null) sListener.statusChanged("[Re]building model...");
          classifier.buildClassifier(dataset);
          datasetChanged = false;
          if(sListener != null) sListener.statusChanged("");
        }
        if(confidenceThreshold > 0 && dataset.classAttribute().type() == weka.core.Attribute.NOMINAL){
          // confidence set; use probability distribution
          double[] distribution = null;
          try{
            distribution = classifier.distributionForInstance(instance);
          }catch(Exception e){
            // if the classifier cannot return a distribution it will throw
            // a java.lang.Exception
            throw new ExecutionException(e);
          }
          List res = new ArrayList();
          for(int i = 0; i < distribution.length; i++){
            if(distribution[i] >= confidenceThreshold){
              res.add(dataset.classAttribute().value(i));
            }
          }
          return res;
        }else{
          // confidence not set; use simple classification
          return convertAttributeValue(classifier.classifyInstance(instance));
        }
      }
    }catch(Exception e){
      throw new ExecutionException(e);
    }
  }
  protected Object convertAttributeValue(double value) {
    gate.creole.ml.Attribute classAttr = datasetDefinition.getClassAttribute();
    List classValues = classAttr.getValues();
    if(classValues != null && !classValues.isEmpty()){
      // nominal attribute
      return dataset.attribute(datasetDefinition.getClassIndex()).value((int)value);
    }else{
      if(classAttr.getFeature() == null){
        // boolean attribute
        return dataset.attribute(datasetDefinition.getClassIndex()).value((int)value);
      }else{
        // numeric attribute
        return new Double(value);
      }
    }
  }
  /**
   * Initialises the classifier and prepares for running.
   * 
   * @throws GateException
   */
  public void init() throws GateException {
    // onlyAccumulateDataset is false by default
    onlyAccumulateDataset = false;
    // see if we can shout about what we're doing
    sListener = null;
    Map listeners = MainFrame.getListeners();
    if(listeners != null){
      sListener = (StatusListener)listeners.get("gate.event.StatusListener");
    }
    // find the classifier to be used
    if(sListener != null) sListener.statusChanged("Initialising classifier...");
    Element classifierElem = optionsElement.getChild("CLASSIFIER");
    if(classifierElem == null){
      Out.prln("Warning (WEKA ML engine): no classifier selected;" + " dataset collection only!");
      classifier = null;
    }else{
      String classifierClassName = classifierElem.getTextTrim();
      // get the options for the classiffier
      String optionsString = null;
      if(sListener != null) sListener.statusChanged("Setting classifier options...");
      Element classifierOptionsElem = optionsElement.getChild("CLASSIFIER-OPTIONS");
      if(classifierOptionsElem != null){
        optionsString = classifierOptionsElem.getTextTrim();
      }
      // new style overrides old style
      org.jdom.Attribute optionsAttribute = classifierElem.getAttribute("OPTIONS");
      if(optionsAttribute != null){
        optionsString = optionsAttribute.getValue().trim();
      }
      String[] options = parseOptions(optionsString);
      try{
        classifier = Classifier.forName(classifierClassName, options);
      }catch(Exception e){
        throw new GateException(e);
      }
      // if we have any filters apply them to the classifer
      List filterElems = optionsElement.getChildren("FILTER");
      if(filterElems != null && filterElems.size() > 0){
        Iterator elemIter = filterElems.iterator();
        while(elemIter.hasNext()){
          Element filterElem = (Element)elemIter.next();
          String filterClassName = filterElem.getTextTrim();
          String filterOptionsString = "";
          org.jdom.Attribute optionsAttr = filterElem.getAttribute("OPTIONS");
          if(optionsAttr != null){
            filterOptionsString = optionsAttr.getValue().trim();
          }
          // create the new filter
          try{
            Class filterClass = Class.forName(filterClassName);
            if(!Filter.class.isAssignableFrom(filterClass)){ throw new ResourceInstantiationException(filterClassName
                    + " is not a " + Filter.class.getName() + "!"); }
            Filter aFilter = (Filter)filterClass.newInstance();
            // apply the options to the filter
            if(filterOptionsString != null && filterOptionsString.length() > 0){
              if(!(aFilter instanceof OptionHandler)){ throw new ResourceInstantiationException(filterClassName
                      + " cannot handle options!"); }
              options = parseOptions(filterOptionsString);
              ((OptionHandler)aFilter).setOptions(options);
            }
            // apply the filter to the classifier
            FilteredClassifier Fclassifier = new FilteredClassifier();
            Fclassifier.setClassifier(classifier);
            Fclassifier.setFilter(aFilter);
            classifier = Fclassifier;
          }catch(Exception e){
            throw new ResourceInstantiationException(e);
          }
        }
      }
      Element anElement = optionsElement.getChild("CONFIDENCE-THRESHOLD");
      if(anElement != null){
        try{
          confidenceThreshold = Double.parseDouble(anElement.getTextTrim());
        }catch(Exception e){
          throw new GateException("Could not parse confidence threshold value: " + anElement.getTextTrim() + "!");
        }
        // in the new version of WEKA all classifiers might be distribution
        // classifiers - there is no way to distinguish between them
        // if(!(classifier instanceof DistributionClassifier)){
        // throw new GateException(
        // "Cannot use confidence threshold with classifier: " +
        // classifier.getClass().getName() + "!");
        // }
      }
    }
    // find the file to be used for dataset
    Element datafileElem = optionsElement.getChild("DATASET-FILE");
    if(datafileElem != null){
      datasetFile = new File(datafileElem.getTextTrim());
      try{
        Out.prln("Warning (WEKA ML engine): writing dataset as ARFF to " + datasetFile.getCanonicalPath());
      }catch(IOException ioe){
        throw new ResourceInstantiationException(ioe);
      }
    }else{
      if(classifier == null){
        // both classifier and datasetFile are null
        // default to only accumulating dataset.
        onlyAccumulateDataset = true;
        Out.prln("Warning: Neither classifier or dataset file are specified in the "
                + "definition!\nThis only accumulates dataset "
                + "elements internally.\nMake sure to use the SaveDatasetAsArff"
                + "action or function to save the dataset to a file.");
      }
    }
    // initialise the dataset
    if(sListener != null) sListener.statusChanged("Initialising dataset...");
    FastVector attributes = new FastVector();
    weka.core.Attribute classAttribute;
    Iterator attIter = datasetDefinition.getAttributes().iterator();
    while(attIter.hasNext()){
      gate.creole.ml.Attribute aGateAttr = (gate.creole.ml.Attribute)attIter.next();
      weka.core.Attribute aWekaAttribute = null;
      if(aGateAttr.getValues() != null){
        // nominal or String attribute
        if(!aGateAttr.getValues().isEmpty()){
          // nominal attribute
          FastVector attrValues = new FastVector(aGateAttr.getValues().size());
          Iterator valIter = aGateAttr.getValues().iterator();
          while(valIter.hasNext()){
            attrValues.addElement(valIter.next());
          }
          aWekaAttribute = new weka.core.Attribute(aGateAttr.getName(), attrValues);
        }else{
          // VALUES element present but no values defined -> String attribute
          aWekaAttribute = new weka.core.Attribute(aGateAttr.getName(), (FastVector)null);
        }
      }else{
        if(aGateAttr.getFeature() == null){
          // boolean attribute ([lack of] presence of an annotation)
          FastVector attrValues = new FastVector(2);
          attrValues.addElement("true");
          attrValues.addElement("false");
          aWekaAttribute = new weka.core.Attribute(aGateAttr.getName(), attrValues);
        }else{
          // feature is not null but no values provided -> numeric attribute
          aWekaAttribute = new weka.core.Attribute(aGateAttr.getName());
        }
      }
      if(aGateAttr.isClass()) classAttribute = aWekaAttribute;
      attributes.addElement(aWekaAttribute);
    }
    dataset = new Instances("Weka ML Engine Dataset", attributes, 0);
    dataset.setClassIndex(datasetDefinition.getClassIndex());
    // write the head of the datafile
    if(datasetFile != null){
      try{
        FileWriter fw = new FileWriter(datasetFile);
        fw.write(dataset.toString());
        fw.flush();
        fw.close();
      }catch(IOException ioe){
        throw new ResourceInstantiationException(ioe);
      }
    }
    if(classifier != null && classifier instanceof UpdateableClassifier){
      try{
        classifier.buildClassifier(dataset);
      }catch(Exception e){
        throw new ResourceInstantiationException(e);
      }
    }
    if(sListener != null) sListener.statusChanged("");
  }
  protected String[] parseOptions(String optionsString) {
    String[] options = null;
    if(optionsString == null || optionsString.length() == 0){
      options = new String[]{};
    }else{
      List optionsList = new ArrayList();
      StringTokenizer strTok = new StringTokenizer(optionsString, " ", false);
      while(strTok.hasMoreTokens()){
        optionsList.add(strTok.nextToken());
      }
      options = (String[])optionsList.toArray(new String[optionsList.size()]);
    }
    return options;
  }
  /**
   * Loads the state of this engine from previously saved data.
   * 
   * @param is
   */
  public void load(InputStream is) throws IOException {
    if(sListener != null) sListener.statusChanged("Loading model...");
    ObjectInputStream ois = new ObjectInputStream(is);
    try{
      classifier = (Classifier)ois.readObject();
      dataset = (Instances)ois.readObject();
      datasetDefinition = (DatasetDefintion)ois.readObject();
      datasetChanged = ois.readBoolean();
      confidenceThreshold = ois.readDouble();
    }catch(ClassNotFoundException cnfe){
      throw new GateRuntimeException(cnfe.toString());
    }
    ois.close();
    if(sListener != null) sListener.statusChanged("");
  }
  /**
   * Saves the state of the engine for reuse at a later time.
   * 
   * @param os
   */
  public void save(OutputStream os) throws IOException {
    if(sListener != null) sListener.statusChanged("Saving model...");
    ObjectOutputStream oos = new ObjectOutputStream(os);
    oos.writeObject(classifier);
    oos.writeObject(dataset);
    oos.writeObject(datasetDefinition);
    oos.writeBoolean(datasetChanged);
    oos.writeDouble(confidenceThreshold);
    oos.flush();
    oos.close();
    if(sListener != null) sListener.statusChanged("");
  }
  /**
   * Gets the list of actions that can be performed on this resource.
   * 
   * @return a List of Action objects (or null values)
   */
  public List getActions() {
    return actionsList;
  }
  /**
   * Registers the PR using the engine with the engine itself.
   * 
   * @param pr
   *          the processing resource that owns this engine.
   */
  public void setOwnerPR(ProcessingResource pr) {
    this.owner = pr;
  }
  public DatasetDefintion getDatasetDefinition() {
    return datasetDefinition;
  }
  public void saveDatasetAsARFF(FileWriter writer) {
    try{
      writer.write(dataset.toString());
      writer.flush();
    }catch(IOException ioe){
      throw new GateRuntimeException(ioe.getMessage());
    }
  }
  public void loadDatasetFromArff(FileReader reader) throws IOException, ExecutionException, Exception {
    Instances newDataset = new Instances(reader);
    if(!dataset.equalHeaders(newDataset))
      throw new ExecutionException("Loaded dataset incompatible with the one " + " in the definition!");
    Enumeration instEnum = newDataset.enumerateInstances();
    while(instEnum.hasMoreElements()){
      addTrainingInstance((Instance)instEnum.nextElement());
    }
  }
  public boolean supportsBatchMode() {
    return false;
  }

  protected class SaveDatasetAsArffAction extends javax.swing.AbstractAction {
    public SaveDatasetAsArffAction() {
      super("Save dataset as ARFF");
      putValue(SHORT_DESCRIPTION, "Saves the dataset to a file in ARFF format");
    }
    public void actionPerformed(java.awt.event.ActionEvent evt) {
      Runnable runnable = new Runnable() {
        public void run() {
          JFileChooser fileChooser = MainFrame.getFileChooser();
          fileChooser.setFileFilter(fileChooser.getAcceptAllFileFilter());
          fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
          fileChooser.setMultiSelectionEnabled(false);
          if(fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION){
            File file = fileChooser.getSelectedFile();
            try{
              MainFrame.lockGUI("Saving dataset...");
              FileWriter fw = new FileWriter(file.getCanonicalPath(), false);
              saveDatasetAsARFF(fw);
              fw.close();
            }catch(IOException ioe){
              JOptionPane.showMessageDialog(MainFrame.getInstance(), "Error!\n" + ioe.toString(), "Gate", JOptionPane.ERROR_MESSAGE);
              ioe.printStackTrace(Err.getPrintWriter());
            }finally{
              MainFrame.unlockGUI();
            }
          }
        }
      };
      Thread thread = new Thread(runnable, "DatasetSaver(ARFF)");
      thread.setPriority(Thread.MIN_PRIORITY);
      thread.start();
    }
  }
  protected class LoadDatasetFromArffAction extends javax.swing.AbstractAction {
    public LoadDatasetFromArffAction() {
      super("Load data from ARFF");
      putValue(SHORT_DESCRIPTION, "Loads training data from a file in ARFF format and "
              + "appends it to the current dataset.");
    }
    public void actionPerformed(java.awt.event.ActionEvent evt) {
      Runnable runnable = new Runnable() {
        public void run() {
          JFileChooser fileChooser = MainFrame.getFileChooser();
          fileChooser.setFileFilter(fileChooser.getAcceptAllFileFilter());
          fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
          fileChooser.setMultiSelectionEnabled(false);
          if(fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION){
            File file = fileChooser.getSelectedFile();
            try{
              MainFrame.lockGUI("Loading dataset...");
              FileReader reader = new FileReader(file.getCanonicalPath());
              loadDatasetFromArff(reader);
              reader.close();
            }catch(Exception e){
              JOptionPane.showMessageDialog(MainFrame.getInstance(), "Error!\n" + e.toString(), "GATE", JOptionPane.ERROR_MESSAGE);
              e.printStackTrace(Err.getPrintWriter());
            }finally{
              MainFrame.unlockGUI();
            }
          }
        }
      };
      Thread thread = new Thread(runnable, "DatasetSaver(ARFF)");
      thread.setPriority(Thread.MIN_PRIORITY);
      thread.start();
    }
  }
  protected class SaveModelAction extends javax.swing.AbstractAction {
    public SaveModelAction() {
      super("Save model");
      putValue(SHORT_DESCRIPTION, "Saves the ML model to a file");
    }
    public void actionPerformed(java.awt.event.ActionEvent evt) {
      Runnable runnable = new Runnable() {
        public void run() {
          JFileChooser fileChooser = MainFrame.getFileChooser();
          fileChooser.setFileFilter(fileChooser.getAcceptAllFileFilter());
          fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
          fileChooser.setMultiSelectionEnabled(false);
          if(fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION){
            File file = fileChooser.getSelectedFile();
            try{
              MainFrame.lockGUI("Saving ML model...");
              save(new GZIPOutputStream(new FileOutputStream(file.getCanonicalPath(), false)));
            }catch(IOException ioe){
              JOptionPane.showMessageDialog(MainFrame.getInstance(), "Error!\n" + ioe.toString(), "GATE", JOptionPane.ERROR_MESSAGE);
              ioe.printStackTrace(Err.getPrintWriter());
            }finally{
              MainFrame.unlockGUI();
            }
          }
        }
      };
      Thread thread = new Thread(runnable, "ModelSaver(serialisation)");
      thread.setPriority(Thread.MIN_PRIORITY);
      thread.start();
    }
  }
  protected class LoadModelAction extends javax.swing.AbstractAction {
    public LoadModelAction() {
      super("Load model");
      putValue(SHORT_DESCRIPTION, "Loads a ML model from a file");
    }
    public void actionPerformed(java.awt.event.ActionEvent evt) {
      Runnable runnable = new Runnable() {
        public void run() {
          JFileChooser fileChooser = MainFrame.getFileChooser();
          fileChooser.setFileFilter(fileChooser.getAcceptAllFileFilter());
          fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
          fileChooser.setMultiSelectionEnabled(false);
          if(fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION){
            File file = fileChooser.getSelectedFile();
            try{
              MainFrame.lockGUI("Loading model...");
              load(new GZIPInputStream(new FileInputStream(file)));
            }catch(IOException ioe){
              JOptionPane.showMessageDialog(MainFrame.getInstance(), "Error!\n" + ioe.toString(), "GATE", JOptionPane.ERROR_MESSAGE);
              ioe.printStackTrace(Err.getPrintWriter());
            }finally{
              MainFrame.unlockGUI();
            }
          }
        }
      };
      Thread thread = new Thread(runnable, "ModelLoader(serialisation)");
      thread.setPriority(Thread.MIN_PRIORITY);
      thread.start();
    }
  }

  protected DatasetDefintion datasetDefinition;
  double confidenceThreshold = 0;
  /**
   * The WEKA classifier used by this wrapper
   */
  protected Classifier classifier;
  /**
   * The dataset used for training
   */
  protected Instances dataset;
  /**
   * The JDom element contaning the options fro this wrapper.
   */
  protected Element optionsElement;
  /**
   * Marks whether the dataset was changed since the last time the classifier
   * was built.
   */
  protected boolean datasetChanged = false;
  protected File datasetFile;
  protected List actionsList;
  protected ProcessingResource owner;
  protected StatusListener sListener;
  /**
   * This variable is set when the ML configuration file has neither the
   * classifier nor the output dataset file option. User is responsible for
   * explicitly saving the dataset using the now uncommented SaveDatasetAsArff
   * function / action.
   */
  protected boolean onlyAccumulateDataset;
}