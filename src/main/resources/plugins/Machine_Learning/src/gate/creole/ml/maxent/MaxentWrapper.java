/*
 *  Copyright (c) 2004, The University of Sheffield.
 *
 *  This file is part of GATE (see http://gate.ac.uk/), and is free
 *  software, licenced under the GNU Library General Public License,
 *  Version 2, June 1991 (in the distribution as file licence.html,
 *  and also available at http://gate.ac.uk/gate/licence.html).
 *
 *  Mike Dowman 30-03-2004
 *
 *  $Id: MaxentWrapper.java 6990 2005-10-25 15:03:18 +0000 (Tue, 25 Oct 2005) julien_nioche $
 *
 */

package gate.creole.ml.maxent;

import gate.creole.ml.*;
import gate.util.GateException;
import gate.creole.ExecutionException;
import gate.gui.MainFrame;

import java.util.List;
import java.util.Iterator;

/**
 * Wrapper class for the Maxent machine learning algorithm.
 * @see <a href="http://maxent.sourceforge.net/index.html">Maxent homepage</a>
 */
public class MaxentWrapper
    implements AdvancedMLEngine, gate.gui.ActionsPublisher {

  boolean DEBUG=false;

  /**
   * This constructor sets up action list so that these actions (loading and
   * saving models and data) will be available from a context menu in the
   * gui).
   *
   * There is no option to load or save data sets, as maxent does not support
   * this. If there is a need to save data sets, then this can be done using
   * weka.wrapper instead.
   */
  public MaxentWrapper() {
    actionsList = new java.util.ArrayList();
    actionsList.add(new LoadModelAction());
    actionsList.add(new SaveModelAction());
    actionsList.add(null);
  }

  /**
   * No clean up is needed for this wrapper, so this is just added because its
   * in the interface.
   */
  public void cleanUp() {
  }

  /**
   * Some wrappers allow batch classification, but this one doesn't, so if
   * it's ever called just inform the user about this by throwing an exception.
   *
   * @param instances This parameter is not used.
   * @return Nothing is ever returned - an exception is always thrown.
   * @throws ExecutionException
   */
  public List batchClassifyInstances(java.util.List instances)
      throws ExecutionException {
    throw new ExecutionException("The Maxent wrapper does not support "+
                                 "batch classification. Remove the "+
                                 "<BATCH-MODE-CLASSIFICATION/> entry "+
                                 "from the XML configuration file and "+
                                 "try again.");
  }

  /**
   * Take a representation of the part of the XML configuration file
   * which corresponds to <OPTIONS>, and store it.
   *
   * @throws GateException
   */
  public void setOptions(org.jdom.Element optionsElem) {
    this.optionsElement = optionsElem;
  }

  /**
   * Extract the options from the stored Element, and verifiy that they are
   * all valid. Store them in the class's fields.
   *
   * @throws ResourceInstansitaionException
   */
  private void extractAndCheckOptions() throws gate.creole.
      ResourceInstantiationException {
    setCutoff(optionsElement);
    setConfidenceThreshold(optionsElement);
    setVerbose(optionsElement);
    setIterations(optionsElement);
    setSmoothing(optionsElement);
    setSmoothingObservation(optionsElement);
  }

  /**
   * Set the verbose field appropriately, depending on whether <VERBOSE> is
   * specified in the configuration file.
   */
  private void setVerbose(org.jdom.Element optionsElem) {
    if (optionsElem.getChild("VERBOSE") == null) {
      verbose = false;
    }
    else {
      verbose = true;
    }
  }

  /**
   * Set the smoothing field appropriately, depending on whether <SMOOTHING> is
   * specified in the configuration file.
   */
  private void setSmoothing(org.jdom.Element optionsElem) {
    if (optionsElem.getChild("SMOOTHING") == null) {
      smoothing = false;
    }
    else {
      smoothing = true;
    }
  }

  /**
   * Set the smoothing observation field appropriately, depending on what value
   * is specified for <SMOOTHING-OBSERVATION> in the configuration file.
   */
  private void setSmoothingObservation(org.jdom.Element optionsElem) throws
      gate.creole.ResourceInstantiationException {
    String smoothingObservationString
        = optionsElem.getChildTextTrim("SMOOTHING-OBSERVATION");
    if (smoothingObservationString != null) {
      try {
        smoothingObservation = Double.parseDouble(smoothingObservationString);
      }
      catch (NumberFormatException e) {
        throw new gate.creole.ResourceInstantiationException("Unable to parse " +
            "<SMOOTHING-OBSERVATION> value in maxent configuration file.");
      }
    }
    else {
      smoothingObservation = 0.0;
    }
  }

  /**
   * See if a cutoff is specified in the congif file. If it is set the cutoff
   * field, otherwise set cutoff to its default value.
   */
  private void setConfidenceThreshold(org.jdom.Element optionsElem) throws gate.
      creole.ResourceInstantiationException {
    String confidenceThresholdString
        = optionsElem.getChildTextTrim("CONFIDENCE-THRESHOLD");
    if (confidenceThresholdString != null) {
      try {
        confidenceThreshold = Double.parseDouble(confidenceThresholdString);
      }
      catch (NumberFormatException e) {
        throw new gate.creole.ResourceInstantiationException("Unable to parse " +
            "<CONFIDENCE-THRESHOLD> value in maxent configuration file.");
      }
      if (confidenceThreshold < 0.0 || confidenceThreshold > 1) {
        throw new gate.creole.ResourceInstantiationException(
            "<CONFIDENCE-THRESHOLD> in maxent configuration"
            + " file must be set to a value between 0 and 1."
            + " (It is a probability.)");
      }
    }
    else {
      confidenceThreshold = 0.0;
    }
  }

  /**
   * See if a cutoff is specified in the congif file. If it is set the cutoff
   * field, otherwise set cutoff to its default value.
   */
  private void setCutoff(org.jdom.Element optionsElem) throws gate.creole.
      ResourceInstantiationException {
    String cutoffString = optionsElem.getChildTextTrim("CUT-OFF");
    if (cutoffString != null) {
      try {
        cutoff = Integer.parseInt(cutoffString);
      }
      catch (NumberFormatException e) {
        throw new gate.creole.ResourceInstantiationException(
            "Unable to parse <CUT-OFF> value in maxent " +
            "configuration file. It must be an integer.");
      }
    }
    else {
      cutoff = 0;
    }
  }

  /**
   * See if a value for how many iterations should be performed during training
   * is specified in the congif file. If it is set the iterations field,
   * otherwise set it to its default value, 10.
   */
  private void setIterations(org.jdom.Element optionsElem) throws gate.creole.
      ResourceInstantiationException {
    String iterationsString = optionsElem.getChildTextTrim("ITERATIONS");
    if (iterationsString != null) {
      try {
        iterations = Integer.parseInt(iterationsString);
      }
      catch (NumberFormatException e) {
        throw new gate.creole.ResourceInstantiationException(
            "Unable to parse <ITERATIONS> value in maxent " +
            "configuration file. It must be an integer.");
      }
    }
    else {
      iterations = 0;
    }
  }

  /**
   * This is called to add a new training instance to the data set collected
   * in this wrapper object.
   *
   * @param attributeValues A list of String objects, each of which corresponds
   * to an attribute value. For boolean attributes the values will be true or
   * false.
   */
  public void addTrainingInstance(List attributeValues) {
    markIndicesOnFeatures(attributeValues);
    trainingData.add(attributeValues);
    datasetChanged = true;
  }

  /**
   * Annotate the features (but not the outcome), by prepending the index of
   * their location in the list of attributes, followed by a colon. This is
   * because all features are true or false, but it is important that maxent
   * does not confuse a true in one position with a true in another when, for
   * example, calculating the cutoff.
   *
   * @param attributeValues a list of String objects listing all the
   * feature values and the outcome value for an instance.
   */
  void markIndicesOnFeatures(List attributeValues) {
    for (int i=0; i<attributeValues.size(); ++i) {
      // Skip the outcome (a.k.a. the class).
      if (i != datasetDefinition.getClassIndex())
        attributeValues.set(i, i+":"+(String)attributeValues.get(i));
    }
  }

  /**
   * Set the data set defition for this classifier.
   *
   * @param definition A specification of the types and allowable values of
       * all the attributes, as specified in the <DATASET> part of the configuration
   * file.
   */
  public void setDatasetDefinition(DatasetDefintion definition) {
    this.datasetDefinition = definition;
  }

  /**
   * Tests that the attributes specified in the DatasetDefinition are valid for
   * maxent. That is that all the attributes except for the class attribute are
   * boolean, and that class is boolean or nominal, as that is a requirement of
   * the maxent implementation used.
   */
  private void checkDatasetDefinition() throws gate.creole.
      ResourceInstantiationException {
    // Now go through the dataset definition, and check that each attribute is
    // of the right kind.
    List attributes = datasetDefinition.getAttributes();
    Iterator attributeIterator = attributes.iterator();
    while (attributeIterator.hasNext()) {
      gate.creole.ml.Attribute currentAttribute
          = (gate.creole.ml.Attribute) attributeIterator.next();
      if (currentAttribute.semanticType() != gate.creole.ml.Attribute.BOOLEAN) {
        if (currentAttribute.semanticType() != gate.creole.ml.Attribute.NOMINAL
            || !currentAttribute.isClass()) {
          throw new gate.creole.ResourceInstantiationException(
              "Error in maxent configuration file. All " +
              "attributes except the <CLASS/> attribute " +
              "must be boolean, and the <CLASS/> attribute" +
              " must be boolean or nominal");
        }
      }
    }
  }

  /**
   * This method first sets the static parameters of GIS to reflect those
   * specified in the configuration file, then it trains the model using the
   * data collected up to this point, and stores the model in maxentClassifier.
   */
  private void initialiseAndTrainClassifier() {
    opennlp.maxent.GIS.PRINT_MESSAGES = verbose;
    opennlp.maxent.GIS.SMOOTHING_OBSERVATION = smoothingObservation;

    // Actually create and train the model, and store it for later use.
    if (DEBUG) {
      System.out.println("Number of training instances: "+trainingData.size());
      System.out.println("Class index: "+datasetDefinition.getClassIndex());
      System.out.println("Iterations: "+iterations);
      System.out.println("Cutoff: "+cutoff);
      System.out.println("Confidence threshold: "+confidenceThreshold);
      System.out.println("Verbose: "+verbose);
      System.out.println("Smoothing: "+smoothing);
      System.out.println("Smoothing observation: "+smoothingObservation);

      System.out.println("");
      System.out.println("TRAINING DATA\n");
      System.out.println(trainingData);
    }
    maxentClassifier = opennlp.maxent.GIS.trainModel(
        new GateEventStream(trainingData, datasetDefinition.getClassIndex()),
        iterations, cutoff,smoothing,verbose);
  }

  /**
   * Decide on the outcome for the instance, based on the values of all the
   * maxent features.
   *
   * N.B. Unless this function was previously called, and there has been no new
   * data added since, the model will be trained when it is called. This could
   * result in calls to this function taking a long time to execute.
   *
   * @param attributeValues A list of all the attributes, including the one that
   * corresponds to the maxent outcome (the <CLASS/> attribute). The value of
   * outcome is arbitrary.
   *
   * @return A string value giving the nominal value of the outcome or, if the
   * outcome is boolean, a java String with value "true" or "false"
   *
   * @throws ExecutionException
   */
  public Object classifyInstance(List attributeValues) throws
      ExecutionException {
    // First we need to check whether we need to create a new model.
    // If either we've never made a model, or some new data has been added, then
    // we need to train a new model.
    if (maxentClassifier == null || datasetChanged)
      initialiseAndTrainClassifier();
      // The data now reflects the model, so keep a note of this so we don't
      // have to retrain the model if using the same data.
    datasetChanged=false;

    // We need to mark indices on the features, so that they will be
    // consistent with those on which the model was trained.
    markIndicesOnFeatures(attributeValues);

      // When classifying, we need to remove the outcome from the List of
      // attributes. (N.B. we must do this after marking indices, so that
      // we don't end up with different indices for features after the class.
    attributeValues.remove(datasetDefinition.getClassIndex());

    // Then try to classify stuff.
    if (confidenceThreshold == 0) { // If no confidence threshold has been set
      // then just use simple classification.
      return maxentClassifier.
          getBestOutcome(maxentClassifier.eval(
          (String[])attributeValues.toArray(new String[0])));
    }
    else { // Otherwise, add all outcomes that are over the threshold.
      double[] outcomeProbabilities = maxentClassifier.eval(
          (String[]) attributeValues.toArray(new String[0]));

      List allOutcomesOverThreshold = new java.util.ArrayList();
      for (int i = 0; i < outcomeProbabilities.length; i++) {
        if (outcomeProbabilities[i] >= confidenceThreshold) {
          allOutcomesOverThreshold.add(maxentClassifier.getOutcome(i));
        }
      }
      return allOutcomesOverThreshold;
    }
  } // classifyInstance

  /**
   * Initialises the classifier and prepares for running. Before calling this
   * method, the datasetDefinition and optionsElement fields should have been
   * set using calls to the appropriate methods.
   * @throws GateException If it is not possible to initialise the classifier
   * for any reason.
   */
  public void init() throws GateException {
    //see if we can shout about what we're doing
    sListener = null;
    java.util.Map listeners = gate.gui.MainFrame.getListeners();
    if (listeners != null) {
      sListener = (gate.event.StatusListener)
                  listeners.get("gate.event.StatusListener");
    }

    if (sListener != null) {
      sListener.statusChanged("Setting classifier options...");
    }
    extractAndCheckOptions();

    if (sListener != null) {
      sListener.statusChanged("Checking dataset definition...");
    }
    checkDatasetDefinition();

    // N.B. We don't initialise the classifier here, because maxent classifiers,
    // are both initialised and trained at the same time. Hence initialisation
    // takes place in the method classifyInstance.

    //initialise the dataset
    if (sListener != null) {
      sListener.statusChanged("Initialising dataset...");

    }
    trainingData = new java.util.ArrayList();

    if (sListener != null) {
      sListener.statusChanged("");
    }
  } // init

  /**
   * Loads the state of this engine from previously saved data.
   * @param is An open InputStream from which the model will be loaded.
   */
  public void load(java.io.InputStream is) throws java.io.IOException {
    if (sListener != null) {
      sListener.statusChanged("Loading model...");

    }
    java.io.ObjectInputStream ois = new java.io.ObjectInputStream(is);

    try {
      maxentClassifier = (opennlp.maxent.MaxentModel) ois.readObject();
      trainingData = (java.util.List) ois.readObject();
      datasetDefinition = (DatasetDefintion) ois.readObject();
      datasetChanged = ois.readBoolean();

      cutoff = ois.readInt();
      confidenceThreshold = ois.readDouble();
      iterations = ois.readInt();
      verbose = ois.readBoolean();
      smoothing = ois.readBoolean();
      smoothingObservation = ois.readDouble();
    }
    catch (ClassNotFoundException cnfe) {
      throw new gate.util.GateRuntimeException(cnfe.toString());
    }
    ois.close();

    if (sListener != null) {
      sListener.statusChanged("");
    }
  }

  /**
   * Saves the state of the engine for reuse at a later time.
   * @param os An open output stream to which the model will be saved.
   */
  public void save(java.io.OutputStream os) throws java.io.IOException {
    if (sListener != null) {
      sListener.statusChanged("Saving model...");

    }
    java.io.ObjectOutputStream oos = new java.io.ObjectOutputStream(os);

    oos.writeObject(maxentClassifier);
    oos.writeObject(trainingData);
    oos.writeObject(datasetDefinition);
    oos.writeBoolean(datasetChanged);

    oos.writeInt(cutoff);
    oos.writeDouble(confidenceThreshold);
    oos.writeInt(iterations);
    oos.writeBoolean(verbose);
    oos.writeBoolean(smoothing);
    oos.writeDouble(smoothingObservation);

    oos.flush();
    oos.close();

    if (sListener != null) {
      sListener.statusChanged("");
    }
  }

  /**
   * Gets the list of actions that can be performed on this resource.
   * @return a List of Action objects (or null values)
   */
  public java.util.List getActions() {
    return actionsList;
  }

  /**
   * Registers the PR using the engine with the engine itself.
   * @param pr the processing resource that owns this engine.
   */
  public void setOwnerPR(gate.ProcessingResource pr) {
    this.owner = pr;
  }

  public DatasetDefintion getDatasetDefinition() {
    return datasetDefinition;
  }

  public boolean supportsBatchMode(){
    return false;
  }
  
  /**
   * This allows the model, including its parameters to be saved to a file.
   */
  protected class SaveModelAction
      extends javax.swing.AbstractAction {
    public SaveModelAction() {
      super("Save model");
      putValue(SHORT_DESCRIPTION, "Saves the ML model to a file");
    }

    /**
     * This function will open a file chooser, and then call the save function
     * to actually save the model. (It is not normally called directly by the
     * user, but will be called as the result of the save model menu option
     * being selected.)
     */
    public void actionPerformed(java.awt.event.ActionEvent evt) {
      Runnable runnable = new Runnable() {
        public void run() {
          javax.swing.JFileChooser fileChooser
              = gate.gui.MainFrame.getFileChooser();
          fileChooser.setFileFilter(fileChooser.getAcceptAllFileFilter());
          fileChooser.setFileSelectionMode(javax.swing.JFileChooser.FILES_ONLY);
          fileChooser.setMultiSelectionEnabled(false);
          if (fileChooser.showSaveDialog(null)
              == javax.swing.JFileChooser.APPROVE_OPTION) {
            java.io.File file = fileChooser.getSelectedFile();
            try {
              gate.gui.MainFrame.lockGUI("Saving ML model...");
              save(new java.util.zip.GZIPOutputStream(
                  new java.io.FileOutputStream(
                  file.getCanonicalPath(), false)));
            }
            catch (java.io.IOException ioe) {
              javax.swing.JOptionPane.showMessageDialog(MainFrame.getInstance(),
                  "Error!\n" +
                  ioe.toString(),
                  "GATE", javax.swing.JOptionPane.ERROR_MESSAGE);
              ioe.printStackTrace(gate.util.Err.getPrintWriter());
            }
            finally {
              gate.gui.MainFrame.unlockGUI();
            }
          }
        }
      };
      Thread thread = new Thread(runnable, "ModelSaver(serialisation)");
      thread.setPriority(Thread.MIN_PRIORITY);
      thread.start();
    }
  }

  /**
   * This reloads a file that was previously saved using the SaveModelAction
   * class. A maxent ml processing resource must already exist before this
   * action can be performed.
   */
  protected class LoadModelAction
      extends javax.swing.AbstractAction {
    public LoadModelAction() {
      super("Load model");
      putValue(SHORT_DESCRIPTION, "Loads a ML model from a file");
    }

    /**
     * This function will open a file chooser, and then call the load function
     * to actually load the model. (It is not normally called directly by the
     * user, but will be called as the result of the load model menu option
     * being selected.)
     */
    public void actionPerformed(java.awt.event.ActionEvent evt) {
      Runnable runnable = new Runnable() {
        public void run() {
          javax.swing.JFileChooser fileChooser
              = gate.gui.MainFrame.getFileChooser();
          fileChooser.setFileFilter(fileChooser.getAcceptAllFileFilter());
          fileChooser.setFileSelectionMode(javax.swing.JFileChooser.FILES_ONLY);
          fileChooser.setMultiSelectionEnabled(false);
          if (fileChooser.showOpenDialog(null)
              == javax.swing.JFileChooser.APPROVE_OPTION) {
            java.io.File file = fileChooser.getSelectedFile();
            try {
              gate.gui.MainFrame.lockGUI("Loading model...");
              load(new java.util.zip.GZIPInputStream(
                  new java.io.FileInputStream(file)));
            }
            catch (java.io.IOException ioe) {
              javax.swing.JOptionPane.showMessageDialog(MainFrame.getInstance(),
                  "Error!\n" +
                  ioe.toString(),
                  "GATE", javax.swing.JOptionPane.ERROR_MESSAGE);
              ioe.printStackTrace(gate.util.Err.getPrintWriter());
            }
            finally {
              gate.gui.MainFrame.unlockGUI();
            }
          }
        }
      };
      Thread thread = new Thread(runnable, "ModelLoader(serialisation)");
      thread.setPriority(Thread.MIN_PRIORITY);
      thread.start();
    }
  }

  protected gate.creole.ml.DatasetDefintion datasetDefinition;

  /**
   * The Maxent classifier used by this wrapper
   */
  protected opennlp.maxent.MaxentModel maxentClassifier;

  /**
   * This List stores all the data that has been collected. Each item is a
   * List of Strings, each of which is an attribute. In maxent terms, these
   * are the features and the outcome - the position of the outcome can be found
   * by referring to the the datasetDefition object.
   */
  protected List trainingData;

  /**
   * The JDom element contaning the options fro this wrapper.
   */
  protected org.jdom.Element optionsElement;

  /**
   * Marks whether the dataset was changed since the last time the classifier
   * was built.
   */
  protected boolean datasetChanged = false;

  /*
   *  This list stores the actions that will be available on the context menu
   *  in the GUI.
   */
  protected List actionsList;

  protected gate.ProcessingResource owner;

  protected gate.event.StatusListener sListener;

  /**
   * The following members are set by the <OPTIONS> part of the config file,
   * and control the parameters used for training the model, and for
   * classifying instances. They are initialised with their default values,
   * but may be changed when setOptions is called.
   */
  protected int cutoff = 0;
  protected double confidenceThreshold = 0;
  protected int iterations = 10;
  protected boolean verbose = false;
  protected boolean smoothing = false;
  protected double smoothingObservation = 0.1;

} // MaxentWrapper