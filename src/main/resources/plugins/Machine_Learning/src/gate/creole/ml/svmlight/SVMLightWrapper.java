/*
 *  Copyright (c) 2004, The University of Sheffield.
 *
 *  This file is part of GATE (see http://gate.ac.uk/), and is free
 *  software, licenced under the GNU Library General Public License,
 *  Version 2, June 1991 (in the distribution as file licence.html,
 *  and also available at http://gate.ac.uk/gate/licence.html).
 *
 *  Mike Dowman 08-04-2004
 *
 *  $Id: SVMLightWrapper.java 7452 2006-06-15 14:45:17 +0000 (Thu, 15 Jun 2006) ian_roberts $
 *
 */

package gate.creole.ml.svmlight;

import gate.creole.ml.*;
import gate.util.GateException;
import gate.creole.ExecutionException;
import gate.gui.MainFrame;

import java.util.List;

import java.io.File;
import java.io.IOException;

/**
 * Wrapper class for the SVM Light support vector machine learning algorithm.
 * The executable files, SVM_Learn and SVM_Classify must be placed on your path
 * in order for this wrapper to work.
 * {@ see http://svmlight.joachims.org/}
 */
public class SVMLightWrapper
    implements AdvancedMLEngine, gate.gui.ActionsPublisher {

  static boolean DEBUG = false;

  /**
   * This constructor sets up action list so that these actions (loading and
   * saving models and data) will be available from a context menu in the
   * gui).
   */
  public SVMLightWrapper() {
    actionsList = new java.util.ArrayList();
    actionsList.add(new LoadDatasetAction());
    actionsList.add(new SaveDatasetAction());
    actionsList.add(new LoadModelAction());
    actionsList.add(new SaveModelAction());
    actionsList.add(null);
  }

  /**
   * Delete all the temporary files when the processing resource is closed.
   */
  public void cleanUp() {
    try {
      trainingDataFile.delete();
      testDataFile.delete();
      modelFile.delete();
      resultsFile.delete();
    } catch (java.lang.SecurityException ex) {
      // If an exception is thrown just do nothing.
    }
  }

  /**
   * Take a representation of the part of the XML configuration file
   * which corresponds to &lt;OPTIONS&gt;, and store it.
   *
   * @throws GateException
   */
  public void setOptions(org.jdom.Element optionsElem) {
    this.optionsElement = optionsElem;
  }

  /**
   * See if any &lt;CLASSIFIER-OPTIONS&gt; are specified in the congif file. If
   * such an element exists, then extract the string of options and store it.
   * Otherwise set classifierOptions to the empty string.
   *
   * This is the only configuration file option for SVM Light.
   */
   private void extractAndCheckOptions() {
     if (optionsElement == null) { 
       classifierOptions = "";
       return;
     }
     classifierOptions = optionsElement.getChildTextTrim("CLASSIFIER-OPTIONS");
     if (classifierOptions == null)
     	classifierOptions = "";
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
    trainingData.add(attributeValues);
    datasetChanged = true;
  }

  /**
   * Set the data set defition for this classifier.
   *
   * @param definition A specification of the types and allowable values of
   * all the attributes, as specified in the &lt;DATASET&gt; part of the
   * configuration file.
   */
  public void setDatasetDefinition(DatasetDefintion definition) {
    this.datasetDefinition = definition;
  }

  /**
   * Tests that the attributes specified in the DatasetDefinition are valid for
   * SVM Light. That is that the class attribute is boolean, numeric, or is a
   * two or three value nominal. (In the case of a three value nominal, the
   * first value will be taken to be positive (1), the second negative (-1)
   * and the third indicates that no value is known for this instance, and
   * that transduction should be used instead (0). A two value nominal has
   * only the first of these two possibilities.
   */
  private void checkDatasetDefinition() throws gate.creole.
      ResourceInstantiationException {
    // Just find the class attribute, and check that it's the right kind.
    Attribute classAttribute =
        (Attribute) datasetDefinition.getClassAttribute();
    if (classAttribute.semanticType() == Attribute.NOMINAL) {
      if (classAttribute.getValues().size() != 2 &&
          classAttribute.getValues().size() != 3) {
        throw new gate.creole.ResourceInstantiationException(
            "Error in SVM Light configuration file. The <CLASS> attribute " +
            "must be boolean, numeric or a two or three valued nominal.");
      }
    }
  }

  /**
   * Decide on the outcome for the instance, based on the values of all the
   * features.
   *
   * N.B. Unless this function was previously called, and there has been no new
   * data added since, the model will be trained when it is called. This could
   * result in calls to this function taking a long time to execute.
   *
   * @param attributeValues A list of all the attributes, including the
   * &lt;CLASS/&gt; attribute. The value of the &lt;CLASS/&gt; attribute is,
   * however, arbitrary.
   *
   * @return A string value giving the nominal value of the class or, if the
   * outcome is boolean, a java String with value "true" or "false", or if the
   * 'class' is numeric, the estimated numeric value for class.
   *
   * @throws ExecutionException
   */
  public Object classifyInstance(List attributeValues) throws
      ExecutionException {

    try {
      // If no training examples have been added yet.
      if (trainingData.size() == 0)
        throw new ExecutionException("An attempt has been made to use an SVM " +
                                     "Light model to classify data before the " +
                                     "model was been trained. At least one " +
                                     "training example must be provided.");

      // First we need to check whether we need to create a new model.
      // If either we've never made a model, or some new data has been added, then
      // we need to train a new model.
      if (!modelTrained || datasetChanged) {
        initialiseAndTrainClassifier();
      }
      // The data now reflects the model, so keep a note of this so we don't
      // have to retrain the model if using the same data.
      datasetChanged = false;

      // Before we classify, we need to save the test data to the disk.
      saveTestInstanceToDisk(attributeValues);

      // Then try to classify stuff. There is no option to use a confidence
      // threshold, so we will just get a simple prediction for the class.
      // This function call returns a string or double.
      // First convert the attribute values to an SVM Light document (the value
      // given to the class attribute is arbitrary.
      java.lang.Process svmLightProcess;
      try {
        svmLightProcess =
            (Runtime.getRuntime()).exec(new String[] {"svm_classify",
                                        "-v", "0",
                                        testDataFile.getPath(),
                                        modelFile.getPath(),
                                        resultsFile.getPath()});

        // We need to read the standard output and error streams, otherwise
        // the process won't run.
        java.io.BufferedReader stdOutput = new java.io.BufferedReader(new
            java.io.InputStreamReader(svmLightProcess.getInputStream()));
        java.io.BufferedReader stdError = new java.io.BufferedReader(new
            java.io.InputStreamReader(svmLightProcess.getErrorStream()));

        // Display any standard output or error. (The verbosity can be set to
        // zero which normally stops there being any standard output.)
        String string;
        while ( (string = stdOutput.readLine()) != null)
          System.out.println(string);

        while ( (string = stdError.readLine()) != null)
          System.out.println(string);

      // Then wait until the process has completely finished.
      svmLightProcess.waitFor();
      }  catch (Exception ex) {
        modelTrained = false;
        ex.printStackTrace();
        throw new gate.util.GateRuntimeException("Exception occured when an " +
                                                 "attempt was made to run " +
                                                 "svm_classify.\n");
      }

      // Check that the processes exit code is normal.
      if (svmLightProcess.exitValue() != 0) {
        modelTrained = false;
        throw new RuntimeException("svm_classify did not exit normally when " +
                                   "called as an external command.");
      }

      double classificationResult;
      // Extract the class value and assign it to classificatin result.
      try {
        classificationResult = extractResultFromResultsFile();
      }
      catch (Exception ex) {
        throw new gate.util.GateRuntimeException(
            "Error when reading the result of " +
            "classification from the results file.");
      }

      return classificationResult2GateFormat(classificationResult);
    } catch (java.io.IOException ex) {
      throw new ExecutionException(ex);
    }
  } // classifyInstance

  /**
   * Decide on the outcomes for all the instances, based on the values of all
   * the features for each of the instances in a document.
   *
   * N.B . Unless this function was previously called, and there has been no new
   * data added since, the model will be trained when it is called. This could
   * result in calls to this function taking a long time to execute.
   *
   * @param attributeValues A list of lists of all the attributes, (one list
   * per instance) including the &lt;CLASS/&gt;attribute.  The value of the
   * &lt;CLASS/&gt;attribute is, however, arbitrary.
   *
   * @return A list of string values giving the nominal value of the class or,
   * if the outcome is boolean, a java String with value "true" or "false", or
   * if the 'class' is numeric, the estimated numeric value for class.
   *
   * @throws ExecutionException
   */
  public List batchClassifyInstances(List instances) throws ExecutionException {
    try {
      // If no training examples have been added yet.
      if (trainingData.size() == 0)
        throw new ExecutionException("An attempt has been made to use an SVM " +
                                     "Light model to classify data before the " +
                                     "model was been trained. At least one " +
                                     "training example must be provided.");

      // First we need to check whether we need to create a new model.
      // If either we've never made a model, or some new data has been added, then
      // we need to train a new model.
      if (!modelTrained || datasetChanged) {
        initialiseAndTrainClassifier();
      }
      // The data now reflects the model, so keep a note of this so we don't
      // have to retrain the model if using the same data.
      datasetChanged = false;

      // Before we classify, we need to save all the test data to the disk.
      saveAllTestInstancesToDisk(instances);

      // Then try to classify stuff. There is no option to use a confidence
      // threshold, so we will just get a simple prediction for the class.
      // This function call returns a string or double.
      // First convert the attribute values to an SVM Light document (the value
      // given to the class attribute is arbitrary.
      java.lang.Process svmLightProcess;
      try {
        svmLightProcess =
            (Runtime.getRuntime()).exec(new String[] {"svm_classify",
                                        "-v", "0",
                                        testDataFile.getPath(),
                                        modelFile.getPath(),
                                        resultsFile.getPath()});

        // We need to read the standard output and error streams, otherwise
        // the process won't run.
        java.io.BufferedReader stdOutput = new java.io.BufferedReader(new
            java.io.InputStreamReader(svmLightProcess.getInputStream()));
        java.io.BufferedReader stdError = new java.io.BufferedReader(new
            java.io.InputStreamReader(svmLightProcess.getErrorStream()));

        // Display any standard output or error. (The verbosity can be set to
        // zero which normally stops there being any standard output.)
        String string;
        while ( (string = stdOutput.readLine()) != null)
          System.out.println(string);

        while ( (string = stdError.readLine()) != null)
          System.out.println(string);

          // Then wait until the process has completely finished.
        svmLightProcess.waitFor();
      }
      catch (Exception ex) {
        modelTrained = false;
        ex.printStackTrace();
        throw new gate.util.GateRuntimeException("Exception occured when an " +
                                                 "attempt was made to run " +
                                                 "svm_classify.\n");
      }

      // Check that the processes exit code is normal.
      if (svmLightProcess.exitValue() != 0) {
        modelTrained = false;
        throw new RuntimeException("svm_classify did not exit normally when " +
                                   "called as an external command.");
      }

       // Get all the results out of the results file, convert them to GATE
       // format, put them in a list, and return the list.
       java.util.List Results = extractAllResultsFromResultsFile();
       return Results;
    }
    catch (java.io.IOException ex) {
      throw new ExecutionException(ex);
    }
  } // batchClassifyInstances

  private List extractAllResultsFromResultsFile() {
    try {
      // First open the file.
      java.io.FileReader reader
          = new java.io.FileReader(resultsFile.getCanonicalPath());
      MyStringReader inputFile = new MyStringReader(reader);
      reader.close();

      // Then extract all the results, converting them to gate format as we go.
      List classificationResults = new java.util.ArrayList();
      while (inputFile.endOfFileReached() == false) {
        String result = inputFile.readItem();
        inputFile.skipToStartOfNextLine();
        classificationResults.add(classificationResult2GateFormat
                                  (Double.parseDouble(result)));
      }

      return classificationResults;
    }
    catch (Exception ex) {
      ex.printStackTrace();
      throw new gate.util.GateRuntimeException(
          "Error when reading the results of a batch " +
          "classification from the results file.");
    }
  } // extractAllResultsFromResultsFile

  /**
   * Just take an instance, in the form of a list of values (as passed from
   * the machine learning PR), and save it to disk in SVM Light format, so that
   * it can be read by svm_classify
   *
   * @param attributeValues The training instance in the form received from
   * the machine learning PR.
   */
  private void saveTestInstanceToDisk(java.util.List attributeValues) throws
      java.io.IOException, gate.creole.ExecutionException {
    java.io.FileWriter fileWriter
       = new java.io.FileWriter(testDataFile.getCanonicalPath(), false);

   fileWriter.write(new SVMLightDocument(datasetDefinition,
                                         nominalValue2IntegerHash,
                                         attributeValues
                                         ).toString());
   fileWriter.close();
  }

  /**
   * Take a list of instances, in the form of a list of lists of values, in
   * the form passed from the machine learning PR, and save it to disk in SVM
   * Light format, so that it can be read by svm_classify.
   *
   * @param instances The list of training instances.
   * @throws java.io.IOException
   * @throws gate.creole.ExecutionException
   */
  private void saveAllTestInstancesToDisk(List instances)
      throws java.io.IOException, gate.creole.ExecutionException {
    java.io.FileWriter fileWriter
       = new java.io.FileWriter(testDataFile.getCanonicalPath(), false);

   saveDataset(fileWriter, instances);

   fileWriter.close();
  }

  /**
   * Just open the file, and read the number it contains on its first line,
   * converting it to a double.
   *
   * @return The value on the first line of the results file.
   */
  private double extractResultFromResultsFile() throws java.io.IOException {
    java.io.FileReader reader
        = new java.io.FileReader(resultsFile.getCanonicalPath());
    MyStringReader inputFile=new MyStringReader(reader);
    String classValue=inputFile.readItem();
    reader.close();
    return Double.parseDouble(classValue);
  }

  /**
   * This function converts back from classification results to the format in
   * used for attributes by GATE. It is the converse of the conversion code
   * found in SVMLightDocument.
   */
  private Object classificationResult2GateFormat(
      double classificationResult) {
    gate.creole.ml.Attribute classAttribute
        = datasetDefinition.getClassAttribute();
    // Numeric attributes need no conversion, except that they need to be
    // objects to be returned.
    if (classAttribute.semanticType() == gate.creole.ml.Attribute.NUMERIC) {
      return new Double(classificationResult);
      // Boolean attributes need to be converted to strings, depending on the
      // sign of the returned double.
    }
    else if
        (classAttribute.semanticType() == gate.creole.ml.Attribute.BOOLEAN) {
      if (classificationResult < 0) {
        return new String("false");
      }
      else {
        return new String("true");
      }
    }
    else { // Otherwise it must be a Nominal attribute.
      // As it is a class attribute, it will be a two or three value nominal,
      // but only the first two values (true and false) can be returned as a
      // result of classification.
      if (classificationResult < 0) {
        // If result is negative, get the second nominal value, and return it.
        return (String) classAttribute.getValues().get(1);
      }
      else {
        // Otherwise must be a positive result, so return the first nominal
        // value.
        return (String) classAttribute.getValues().get(0);
      }
    }
  }

  /**
   * Use svm_learn to create a new svm model, based on all the data currently
   * stored in the wrapper.
   *
   * @throws gate.creole.ExecutionException
   */
  public void initialiseAndTrainClassifier() throws gate.creole.
      ExecutionException, java.io.IOException {

    if (DEBUG)
      System.out.println("Entering initialise and train classifier");

    // First save the dataset to disk, so svm_learn can access it.
    java.io.FileWriter fileWriter
        = new java.io.FileWriter(trainingDataFile.getCanonicalPath(), false);
    saveDataset(fileWriter, trainingData);
    fileWriter.close();

    if (DEBUG)
      System.out.println("Training dataset saved to disk");

    java.lang.Process svmLightProcess;
    try {
      svmLightProcess =
          (Runtime.getRuntime()).exec(
          optionsString2OptionsList(
          getSVMLightClassificationOrRegressionOption()
          + classifierOptions));

      if (DEBUG)
        System.out.println("SVM_LEARN process started");

      // We need to read the standard output and error streams, otherwise
      // the process won't run.
      java.io.BufferedReader stdOutput = new java.io.BufferedReader(new
          java.io.InputStreamReader(svmLightProcess.getInputStream()));
      java.io.BufferedReader stdError = new java.io.BufferedReader(new
          java.io.InputStreamReader(svmLightProcess.getErrorStream()));

      if (DEBUG)
        System.out.println("output streams created");

      // Display any standard output or error. (The verbosity can be set to
      // zero which normally stops there being any standard output.)
      String string;
      while ( (string = stdOutput.readLine()) != null) {
        if (DEBUG)
          System.out.println("Printing output");
        System.out.println(string);
      }
      while ( (string = stdError.readLine()) != null) {
        if (DEBUG)
          System.out.println("Printing error");
        System.out.println(string);
      }

      // Wait until the process has completely finished.
      svmLightProcess.waitFor();

      if(DEBUG)
        System.out.println("SVM LEARN PROCESS FINISHED");
    }
    catch (Exception ex) {
      modelTrained = false;
      ex.printStackTrace();
      throw new gate.util.GateRuntimeException("Exception occured when an " +
                                               "attempt was made to run " +
                                               "svm_learn.\n");
    }

    // Check that the processes exit code is normal.
    if (svmLightProcess.exitValue() != 0) {
      modelTrained = false;
      throw new RuntimeException("svm_learn did not exit normally when " +
                                 "called as an external command.");
    }

    modelTrained = true;
    if (DEBUG)
      System.out.println("Leaving initialise and train classifier");
  }

  /**
   * Takes options for SVM_Learn in string form, and converts them to the form
   * required by java for calling system functions, adding on the required
   * filenames for data and model to the end of the list of options.
   *
   * @param options The list of options in the form of a string, separated by
   * tabs or spaces.
   * @return A list with one options per entry, in the same order as they
   * appeared in the input string, followed by the name of the data file, and
   * finally the name of the model file. (N.B. options here means each item
   * separated by white space - in reality an svm light option is two of these
   * options.)
   */
   java.lang.String[] optionsString2OptionsList(String optionsString) {
     
     String[] optionsArray1 = optionsString.split("\\s");
     // Make an array with enough space for all the options plus the two
     // filenames, plus the name of the command.
     java.lang.String[] optionsArray= new java.lang.String[optionsArray1.length+3];

     optionsArray[0] = "svm_learn";
     for (int i=0; i < optionsArray1.length; ++i) {
       optionsArray[i+1] = optionsArray1[i];
     }
     optionsArray[optionsArray.length-2] = trainingDataFile.getPath();
     optionsArray[optionsArray.length-1] = modelFile.getPath();

     if (DEBUG) {
       System.out.println("Options array contents are:");
       for (int i=0; i<optionsArray.length; ++i) {
         System.out.println(optionsArray[i]);
       }
     }

     return optionsArray;
   }

  /**
   * Get the SVM Light command line option specifying whether we are doing
   * regression or classification.
   * @return The String "-z r " if the class attribute is numeric, otherwise
   * the String "-z c ".
   */
  String getSVMLightClassificationOrRegressionOption() {
    if (datasetDefinition.getClassAttribute().semanticType()
        == Attribute.NUMERIC) {
      return "-z r ";
    }
    else {
      return "-z c ";
    }
  }

  /**
   * Convert the training data to a form which can be passed to the native
   * code function, and which is closer to that required by SVMLight itself.
   * N.B. SVM Light calls documents instances.
   *
   * @return An array of instances (a.k.a. documents), containing all the
   * training data.
   */
  private SVMLightDocument[] convertTrainingDataToArrayOfDocuments() throws
      gate.creole.ExecutionException {
    // Create an array with one element for each training instance.
    SVMLightDocument[] documents = new SVMLightDocument[trainingData.size()];
    int indexInArray = 0;
    java.util.Iterator trainingDataIterator = trainingData.iterator();
    while (trainingDataIterator.hasNext()) {
      documents[indexInArray] =
          new SVMLightDocument(datasetDefinition, nominalValue2IntegerHash,
                               (java.util.List) trainingDataIterator.next());
      ++indexInArray;
    }
    return documents;
  }

  /**
   * Initialises the classifier and prepares for running. Before calling this
   * method, the datasetDefinition and optionsElement fields should have been
   * set using calls to the appropriate methods.
   *
   * It also creates temporary files needed for passing data to and from
   * SVMLight.
   *
   * @throws GateException If it is not possible to initialise the classifier
   * for any reason.
   */
  public void init() throws GateException {
    if (DEBUG) {
      System.out.println("Entering SVMLightWrapper.init()");
    }
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

    // N.B. We don't initialise the classifier here, because svmLight classifiers,
    // are both initialised and trained at the same time. Hence initialisation
    // takes place in the method classifyInstance.

    //initialise the dataset
    if (sListener != null) {
      sListener.statusChanged("Initialising dataset...");
    }
    trainingData = new java.util.ArrayList();

    createNominalValue2IntegerHash();

    // Create the working directory in which temporary files can be stored.
    if (sListener != null) {
      sListener.statusChanged("Creating temporary files...");
    }

    try {
      // Now create file objects for each of the four files we need.
      // Java will put these files in the default temporary files directory.
      trainingDataFile = java.io.File.createTempFile("train", null);
      testDataFile = java.io.File.createTempFile("test", null);
      modelFile = java.io.File.createTempFile("model", null);
      resultsFile = java.io.File.createTempFile("results", null);

      // We want all of these files to be deleted when the java virtual machine
      // exits. They will also be deleted if the processing resource is deleted
      // by factory.
      trainingDataFile.deleteOnExit();
      testDataFile.deleteOnExit();
      modelFile.deleteOnExit();
      resultsFile.deleteOnExit();
    } catch (java.io.IOException exception) {
      throw new gate.creole.ResourceInstantiationException(
          "Unable to create temporary files needed for SVMLightWrapper.");
    }

    if (sListener != null) {
      sListener.statusChanged("");
    }
  } // init

  /**
   * Create a hash so that nominal values can quickly be mapped
   * to their feature numbers as used by SVM Light.
   */
  private void createNominalValue2IntegerHash() {
    nominalValue2IntegerHash = new java.util.HashMap();

    java.util.Iterator attributeIterator
        = datasetDefinition.getAttributes().iterator();
    // number the attribtues from 1.
    int attributeNumber = 0;
    while (attributeIterator.hasNext()) {
      // Get the list of feature values for this attribute.
      Attribute currentAttribute = (Attribute) attributeIterator.next();

      // Only do the indexing for nominal attributes.
      if (currentAttribute.semanticType() == Attribute.NOMINAL) {
        java.util.List features = currentAttribute.getValues();

        java.util.Iterator featureIterator = features.iterator();
        int featureValueNumber = 1; // Number the feature values from 1.
        while (featureIterator.hasNext()) {

          // Add keys of the form attributeNumber:attributeFeatureValue pointing
          // to the index of the particular feature value.
          nominalValue2IntegerHash.put("" + attributeNumber + ":" +
                                       featureIterator.next()
                                       , new Integer(featureValueNumber));
          ++featureValueNumber;
        }
      }

      ++attributeNumber;
    }

  } // createNominalValue2IntegerHash

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

  /**
   * This class adds the option to the context menu in the GUI that allows the
   * user to load a dataset which is in SVM Light's own format from a file.
   */
  protected class LoadDatasetAction extends javax.swing.AbstractAction {
    public LoadDatasetAction() {
      super("Load data from SVM Light format file");
      putValue(SHORT_DESCRIPTION,
               "Loads training data from a file in SVM Light format and " +
               "appends it to the current dataset.");
    }

    /**
     * This is the funtion called when the user selects the menu option
     * load dataset.
     *
     * @param evt
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
              gate.gui.MainFrame.lockGUI("Loading dataset...");
              java.io.FileReader reader
                  = new java.io.FileReader(file.getCanonicalPath());
              loadDataset(reader);
              reader.close();
            }
            catch (Exception e) {
              javax.swing.JOptionPane.showMessageDialog(MainFrame.getInstance(),
                  "Error!\n" + e.toString(), "Gate",
                  javax.swing.JOptionPane.ERROR_MESSAGE);
              e.printStackTrace(gate.util.Err.getPrintWriter());
            }
            finally {
              gate.gui.MainFrame.unlockGUI();
            }
          }
        }
      };

      Thread thread = new Thread(runnable, "DatasetSaver(SVM Light format)");
      thread.setPriority(Thread.MIN_PRIORITY);
      thread.start();
    }
  }

  protected class SaveDatasetAction extends javax.swing.AbstractAction {
    public SaveDatasetAction() {
      super("Save dataset in SVM Light format");
      putValue(SHORT_DESCRIPTION,
               "Saves the dataset to a file in SVM Light format");
    }

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
              gate.gui.MainFrame.lockGUI("Saving dataset...");
              java.io.FileWriter fw
                  = new java.io.FileWriter(file.getCanonicalPath(), false);
              saveDataset(fw, trainingData);
              fw.close();
            }
            catch (java.io.IOException ioe) {
              javax.swing.JOptionPane.showMessageDialog(MainFrame.getInstance(),
                  "Error!\n" + ioe.toString(),
                  "Gate", javax.swing.JOptionPane.ERROR_MESSAGE);
              ioe.printStackTrace(gate.util.Err.getPrintWriter());
            }
            finally {
              gate.gui.MainFrame.unlockGUI();
            }
          }
        }
      };

      Thread thread = new Thread(runnable, "DatasetSaver(SVM Light format)");
      thread.setPriority(Thread.MIN_PRIORITY);
      thread.start();
    }
  }

  /**
   * Write the data set to a file in SVM Light format.
   *
   * @param writer An open file writer to which the data is to be written.
   * @param dataSet The data set to be saved, in the form of a list of
   * attributes in the form passed from the ML PR.
   */
  public void saveDataset(java.io.FileWriter writer, java.util.List dataSet) {
    try {
      java.util.Iterator trainingDataIterator = dataSet.iterator();
      while (trainingDataIterator.hasNext()) {
        // Create a new SVM Light document for each instance, and convert it to
        // a String.
        writer.write(new SVMLightDocument(datasetDefinition,
                                          nominalValue2IntegerHash,
                                   (java.util.List)
                                   trainingDataIterator.next()).toString());
      }

      writer.flush();
    }
    catch (java.io.IOException ioe) {
      throw new gate.util.GateRuntimeException(ioe.getMessage());
    }
    // Just change the kind of exception to the correct one.
    catch (gate.creole.ExecutionException ex) {
      throw new gate.util.GateRuntimeException(ex);
    }
  }

  /**
   * Reads training data in SVM Light format from a file and adds it to the
   * collection of training examples.
   *
   * @param reader A file reader from which to read the data.
   */
  public void loadDataset(java.io.FileReader reader)
      throws gate.util.GateRuntimeException, java.io.IOException {
    MyStringReader inputFile=new MyStringReader(reader);

    inputFile.skipLeadingComments();
    while (inputFile.endOfFileReached()==false) {
      readAndAddInstance(inputFile);
    }
  }

  private void readAndAddInstance(MyStringReader inputFile)
      throws gate.util.GateRuntimeException {
    // First check if there's no more data to read, and if so just return.
    inputFile.skipBlankLinesAndWhiteSpace();
    if (inputFile.endOfFileReached())
      return;

    String classValue=inputFile.readItem();
    List featureNumbers=new java.util.ArrayList();
    List featureValues=new java.util.ArrayList();

    while (true) {
      // Read in a feature value pair, but if there aren't any more exit the
      // loop.
      FeatureValuePair featureValuePair=
          inputFile.readFeatureValuePair();
      if (featureValuePair==null)
        break;

      featureNumbers.add(new Integer(featureValuePair.featureNumber));
      featureValues.add(new Double(featureValuePair.featureValue));
    }

    // Convert the data we've read into the form required, and add it to the
    // training data.
    if (DEBUG) {
      System.out.println("Adding an instance with class value="+classValue
                         +" and feature numbers:"+featureNumbers
                         +" and feature values: "+featureValues);
      System.out.println("There are "+featureNumbers.size()+" feature numbers"+
                         " and "+featureValues.size()+" feature values.");
    }
    addInstance(classValue, featureNumbers, featureValues);
  }

  /**
   * Take data read in from an SVM Light format file, and convert it into data
   * in the format passed from GATE. Then add it as a new training instance
   * to the training data.
   *
   * @param classValue The value of the class attribute in SVM Light format.
   * @param featureNumbers A list of feature numbers, as in SVM Light format
   * files.
   * @param featureValues A list of feature values, corresponding to the feature
   * numbers list, as they appear in an SVM Light format file.
   */
  private void addInstance(String classValue, List featureNumbers, List featureValues) {
    java.util.List attributeValues = new java.util.ArrayList();

    // We might get to the end of the list, and we don't want to get an error,
    // when we try to access an item that doesn't exist, so add a fake entry to
    // the end of the list with a feature number so high that we will never
    // get past it.
    featureNumbers.add(new Integer(Integer.MAX_VALUE));
    // The value that the double is set to is completely arbitrary, so long as
    // it's not zero.
    featureValues.add(new Double(1.0));

    int indexInFeatureNumbersList=0;
    // Each GATE attribute might map to many SVM Light attributes, so we need to
    // keep track of what SVM Light attribute we're up to separately. SVM Light
    // attributes are numbered from 1.
    int firstIndexOfSVMLightAttributeForCurrentGATEAttribute=1;
    for (int attributeIndex=0;
           attributeIndex<datasetDefinition.getAttributes().size();
           ++attributeIndex) {
      // First make sure that we're at the right place in the list of SVM Light
      // features by skipping over any features with value zero (that is the
      // default value, so we don't need to have it specified), and over any
      // features that have (or should have) already been used, that is ones
      // that specify values for GATE attributes that we've already processed.
      while (((Integer)featureNumbers.get(indexInFeatureNumbersList)).intValue()
             <firstIndexOfSVMLightAttributeForCurrentGATEAttribute
             || ((Double)featureValues
                 .get(indexInFeatureNumbersList)).doubleValue()==0.0)
        ++indexInFeatureNumbersList;

      // If we've got to the class attribute, add it in.
      if (attributeIndex==datasetDefinition.getClassIndex())
        attributeValues.add(
            getGATEClassAttributeValue(attributeIndex, classValue));
      else { // If we've got to a regular attribute, add that in.
       // If the feature is in the list, move on the index in the list of
       // regular attributes.
        attributeValues.add(getGATEAttributeValue(attributeIndex,
          ((Double)featureValues.get(indexInFeatureNumbersList)).doubleValue(),
          firstIndexOfSVMLightAttributeForCurrentGATEAttribute,
          ((Integer)featureNumbers.get(indexInFeatureNumbersList)).intValue()));
        firstIndexOfSVMLightAttributeForCurrentGATEAttribute+=
            numberOfSVMLightAttributesUsedForGATEAttribute(attributeIndex);
      }
    }

    // Finally actually add the list of attribute values we've just made to the
    // training data.
    addTrainingInstance(attributeValues);
  }

  /**
   * Find the value for a GATE class attribute, based on the information read in
   * from an SVM Light data file.
   *
   * Note that we must unweight any attributes that have been weighted here. It
   * is possible to tell the unweighted value of boolean and nominal attributes
   * just by checking their signs, which is what is done here.
   *
   * @param gateAttributeIndex The index of the attribute in datasetDefinition
   * @param classValue The value read in for the class value from a file in SVM
   * Light format.
   * @return The value of the class attribute in the format used by GATE.
   */
  private String getGATEClassAttributeValue(
      int gateAttributeIndex, String classValue) {
    Attribute classAttribute=datasetDefinition.getClassAttribute();
    // Class attributes can be either boolean, or two or three valued nominals.
    if (classAttribute.semanticType()==Attribute.BOOLEAN) {
      if (Double.parseDouble(classValue) > 0.0)
        return "true";
      else if (Double.parseDouble(classValue) < 0.0/classAttribute.getWeighting())
        return "false";
      else throw new gate.util.GateRuntimeException("Value of class attribute "+
            "in a file in SVM Light format is zero, but this is "+
            "not allowed because the gate attribute used \nto represent this value"+
            " is boolean. The value of the attribute \nis \""+classValue+"\".");
    }
    // Next deal with two value nominals.
    else if (classAttribute.semanticType()==Attribute.NOMINAL) {
      if (classAttribute.getValues().size()==2) {
        if (Double.parseDouble(classValue) > 0.0)
          return (String)classAttribute.getValues().get(0);
        else if (Double.parseDouble(classValue) < 0.0)
          return (String)classAttribute.getValues().get(1);
        else throw new gate.util.GateRuntimeException("Value of class attribute "+
              "in a file in SVM Light format \nis zero, but this is "+
              "not allowed because the gate attribute \nused to represent this value"+
              " is a two value nominal attribute. \nThe value of the attribute is"
              +" \""+classValue+"\".");
      }

      // Finally deal with three value nominals, which allow a third value,
      // signifying that the class value is unknown and that transduction should
      // be used.
      else if (classAttribute.getValues().size()==3) {
        if (Double.parseDouble(classValue) > 0.0/classAttribute.getWeighting())
          return (String)classAttribute.getValues().get(0);
        else if (Double.parseDouble(classValue) < 0.0/classAttribute.getWeighting())
          return (String)classAttribute.getValues().get(1);
        else if (Double.parseDouble(classValue)==0)
          return (String)classAttribute.getValues().get(2);
      }
    }
    // If we haven't returned by this point, it must be because the class value
    // is of the wrong type.
    throw new gate.util.GateRuntimeException("The class value specified in "+
                                             " configuration file is not "+
                                             " boolean or a \ntwo or three "+
                                             "valued nominal, as it is "+
                                             "required to be.");
  }

  /**
   * Find the value for a GATE attribute, based on the information read in from
   * an SVM Light data file. This method unweights any values that were weighted
   * when the file was created. For boolean and nominal attributes this means
   * that it just looks at the sign of the attribute, as that is sufficient
   * for determining the unweighted value of the attribute, which will always
   * be +1, -1 or 0.
   *
   * @param gateAttributeIndex The index of the attribute in datasetDefinition
   * @param svmFeatureValue The value of the feature in the SVM Light data file
   * @param firstIndexOfSVMLightAttributeForCurrentGATEAttribute The number of
   * the first SVM Light parameter that encodes values for the GATE attribute.
   * @param svmFeatureNumber The number of the SVM feature that might correspond
   * to the the current GATE attribtue. (The function will check to see if it
   * does correspond, if not it is ignored.)
   * @return The value of the attribute in the format used by GATE
   */
  private String getGATEAttributeValue(int gateAttributeIndex,
                     double svmFeatureValue,
                     int firstIndexOfSVMLightAttributeForCurrentGATEAttribute,
                     int svmFeatureNumber) {
    Attribute gateAttribute=
        (Attribute)datasetDefinition.getAttributes().get(gateAttributeIndex);
    // Numeric attributes are straightforward. Each one maps to a single SVM
    // Light attribute, and their values are unchanged. If they're missing it
    // just indicates that their value is zero.
    if (gateAttribute.semanticType()==Attribute.NUMERIC) {
      if (firstIndexOfSVMLightAttributeForCurrentGATEAttribute
          ==svmFeatureNumber)
        return ""+(svmFeatureValue/gateAttribute.getWeighting());
      else
        return "0";
    }
    // Boolean attributes also map to just one SVM Light attribute. We just need
    // to change their values from +1 or -1 to true or false.
    else if (gateAttribute.semanticType()==Attribute.BOOLEAN) {
      if (firstIndexOfSVMLightAttributeForCurrentGATEAttribute
          ==svmFeatureNumber) {
        if (svmFeatureValue > 0.0)
          return "true";
        if (svmFeatureValue < 0.0)
          return "false";
        // Those are the only allowable values for a boolean attribute, so if we
        // have got one there's an error in the input data set.
        throw new gate.util.GateRuntimeException(
            "Error when loading an SVM Light"
            + " format file. The feature-valu"
            + "e pair " + svmFeatureNumber + ":" +
            svmFeatureValue + " \nshould be boolean"
            + ", but it's value is zero.");
      }
      throw new gate.util.GateRuntimeException(
          "Error when loading an SVM Light format file. A boolean value is not "
          +"\nspecified, \nand this is not allowed because boolean values must take"
          +" \neither +1 or -1 as their \nvalues, and so the default value of 0 can"
          +" \nnot be assigned to them.");
    }
    // Otherwise we must have a nominal attribute.
    else {
      // If the current svm light attribute speficies a value for the current
      // gate attribute.
      if (svmFeatureNumber >= firstIndexOfSVMLightAttributeForCurrentGATEAttribute
          && svmFeatureNumber <
          firstIndexOfSVMLightAttributeForCurrentGATEAttribute+
          numberOfSVMLightAttributesUsedForGATEAttribute(gateAttributeIndex)) {

        // The only valid values for a nominal svm Light feature are 0 or 1
        // (or their weighted equivalents),
        // but as we've already skipped any attributes that have the value 0,
        // the feature value must be 1, or its weighted equivalent.

        // The value of the nominal attribute will correspond to which slot it
        // fills, i.e. which svm light feature is set to 1.
        return (String)gateAttribute.getValues().get(
            svmFeatureNumber-firstIndexOfSVMLightAttributeForCurrentGATEAttribute);
      }
      // If the attribute doesn't specify a value for the gate attribute (in
      // which case is should be a value for a later attribute) then the value
      // of the current attribute must be unspecified.
      else {
        return "";
      }
    }
  }

  /**
   * Return the number of SVM light attributes that are used to represent a
   * GATE attribute.
   * @param gateAttributeIndex The index in the dataset definition of the GATE
   * attribute
   * @return The number of SVM light attributes used to represent the GATE
   * attribute.
   */
  private int numberOfSVMLightAttributesUsedForGATEAttribute(
      int gateAttributeIndex) {
    Attribute gateAttribute=
        (Attribute)datasetDefinition.getAttributes().get(gateAttributeIndex);
    // Each boolean or numeric gateAttribute just maps to a single SVM Light
    // attribute.
    if (gateAttribute.semanticType()==Attribute.BOOLEAN
        || gateAttribute.semanticType()==Attribute.NUMERIC)
      return 1;

    // If we get here, we must have a nominal attribute, for which each possible
    // value is represented by a different SVM Light attribute.
    return gateAttribute.getValues().size();
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
              saveModel(file);
            }
            catch (java.io.IOException ioe) {
              javax.swing.JOptionPane.showMessageDialog(MainFrame.getInstance(),
                  "Error!\n" +
                  ioe.toString(),
                  "Gate", javax.swing.JOptionPane.ERROR_MESSAGE);
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
   * class.
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
              loadModel(file);
            } catch (java.io.IOException ioe) {
              javax.swing.JOptionPane.showMessageDialog(MainFrame.getInstance(),
                  "Error!\n" +
                  ioe.toString(),
                  "Gate", javax.swing.JOptionPane.ERROR_MESSAGE);
              ioe.printStackTrace(gate.util.Err.getPrintWriter());
            } finally {
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

  /**
   * This copies a model file saved on disk to the temp file used by the
   * wrapper to store models. This effectively 'loads' the model.
   *
   * @param filename The name of the file to copy.
   */
  private void copyModelToTempFile(java.lang.String filename)
      throws java.io.IOException {
    java.io.File source = new java.io.File(filename);
    copyFile(source, modelFile);
  }

  /**
   * This copies a model file from the temp file used by the wrapper to store
   * models. This effectively 'saves' the model.
   *
   * @param filename The name of the destination file for the copy.
   */
  private void copyModelFromTempFile(java.lang.String filename)
      throws java.io.IOException {
    java.io.File destination = new java.io.File(filename);
    copyFile(modelFile, destination);
  }

  /**
   * Copy a file on disk from one location to another. N.B. This is implemented
   * by loading and then saving the file, as otherwise a system dependent
   * external command would have to be used.
   *
   * @param source The file to copy.
   * @param destination The location of the file to be copied.
   */
  private void copyFile(java.io.File source, java.io.File destination) throws
      java.io.IOException {
    if (!destination.exists())
      destination.createNewFile();

    java.io.BufferedInputStream inputFromFile
        = new java.io.BufferedInputStream(new
                                          java.io.FileInputStream(source));
    java.io.BufferedOutputStream outputToFile
        = new java.io.BufferedOutputStream(new
                                           java.io.FileOutputStream(destination));

    int i = inputFromFile.read();
    while (i != -1) {
      outputToFile.write(i);
      i = inputFromFile.read();
    }
    outputToFile.close();
    inputFromFile.close();
  }

  /**
   * Loads the state of this engine from previously saved data.
   * @param An open InputStream from which the model will be loaded.
   */
  public void load(java.io.InputStream is) throws java.io.IOException {
    if (sListener != null) {
      sListener.statusChanged("Loading java part of model...");

    }
    java.io.ObjectInputStream ois = new java.io.ObjectInputStream(is);

    try {
      // svmLightModel = ois.readInt();
      trainingData = (java.util.List) ois.readObject();
      datasetDefinition = (DatasetDefintion) ois.readObject();
      datasetChanged = ois.readBoolean();
      modelTrained = ois.readBoolean();
      classifierOptions = (String) ois.readObject();
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
   * Saves the state of the engine for reuse at a later time. optionsElement is
   * not saved so as to make this code consistent with wekaWrapper.
   * @param An open output stream to which the model will be saved.
   */
  public void save(java.io.OutputStream os) throws java.io.IOException {
    if (sListener != null) {
      sListener.statusChanged("Saving java part of model...");

    }
    java.io.ObjectOutputStream oos = new java.io.ObjectOutputStream(os);

    //oos.writeInt(svmLightModel);
    oos.writeObject(trainingData);
    oos.writeObject(datasetDefinition);
    oos.writeBoolean(datasetChanged);
    oos.writeBoolean(modelTrained);
    oos.writeObject(classifierOptions);

    oos.flush();
    oos.close();

    if (sListener != null) {
      sListener.statusChanged("");
    }
  }

  /**
   * Load a previously saved state of the engine.  If the saved state includes
   * an up-to-date trained model, this is also reloaded.
   *
   * @param file the file from which the state is to be loaded.  If the state
   * indicates that a trained model should be loaded, this should be in
   * <i>file</i>.NativePart.
   */
  public void loadModel(File file) throws IOException {
    load(new java.util.zip.GZIPInputStream(
        new java.io.FileInputStream(file)));

    // If an up to date svm_model was saved when the model was saved,
    // load that back in too, from a separate file,
    // with the same name but with .NativePart appended. As we want
    // to end up with the model in a file, we don't need to actually
    // load it, instead we just copy the file to the modelFile.
    if (datasetChanged==false && modelTrained) {
      copyModelToTempFile(file.getAbsolutePath() + ".NativePart");
    }
  }

  /**
   * Saves the state of the engine for reuse at a later time. optionsElement is
   * not saved so as to make this code consistent with wekaWrapper.  If an
   * up-to-date trained model exists, it will be saved in
   * <i>file</i>.NativePart.
   */
  public void saveModel(File file) throws IOException {
    save(new java.util.zip.GZIPOutputStream(
        new java.io.FileOutputStream(
        file.getCanonicalPath(), false)));

    // If we've got an up to date model trained, then save that too,
    // in a file with the same name but with .NativePart appended.
    // N.B. As models are always stored on the disk anyway, this
    // really just involves copying a file to the location given
    // by the user.
    if (datasetChanged==false && modelTrained)
      copyModelFromTempFile(file.getCanonicalPath()+".NativePart");
  }


  /**
   * Has the dataset changed since the model was last trained?
   */
  public boolean isDatasetChanged() {
    return datasetChanged;
  }

  /**
   * Is there a trained model available (whether or not it is up to date)?
   */
  public boolean isModelTrained() {
    return modelTrained;
  }
  
  public boolean supportsBatchMode(){
    return true;
  }
  
  protected java.util.HashMap nominalValue2IntegerHash;

  protected gate.creole.ml.DatasetDefintion datasetDefinition;

  /**
   * This List stores all the data that has been collected. Each item is a
   * List of objects, each of which is an attribute (and one of which is the
   * class attribute).
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

  /**
   * Marks whether in the present state a trained model exists (whether or not
   * it is up to date)
   */
  protected boolean modelTrained = false;

  /**
   * These file objects store the path names to the files that will be used
   * to store the model, data and results while they are passed to and from
   * svm light.
   */
  protected java.io.File trainingDataFile;
  protected java.io.File testDataFile;
  protected java.io.File modelFile;
  protected java.io.File resultsFile;

  /*
   *  This list stores the actions that will be available on the context menu
   *  in the GUI.
   */
  protected List actionsList;

  protected gate.ProcessingResource owner;

  protected gate.event.StatusListener sListener;

  /**
   * The following parameter is set by the &lt;CLASSIFIER-OPTIONS&gt; element
   * of the config file, with the &lt;OPTIONS&gt; element. It specifies the
   * options in the same format as is required when they are specified on the
   * command line.
   */
  java.lang.String classifierOptions;

} // SVMLightWrapper
