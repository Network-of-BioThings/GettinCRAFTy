/*
 *  TestMaxentWrapper.java
 *
 *  Copyright (c) 1995-2012, The University of Sheffield. See the file
 *  COPYRIGHT.txt in the software or at http://gate.ac.uk/gate/COPYRIGHT.txt
 *
 *  This file is part of GATE (see http://gate.ac.uk/), and is free
 *  software, licenced under the GNU Library General Public License,
 *  Version 2, June 1991 (in the distribution as file licence.html,
 *  and also available at http://gate.ac.uk/gate/licence.html).
 *
 *  Mike Dowman, 1/4/2004
 *
 *  $Id: TestMaxentWrapper.java 7455 2006-06-19 11:18:44 +0000 (Mon, 19 Jun 2006) ian_roberts $
 */

package gate.creole.ml.maxent;

import junit.framework.*;
import gate.*;
import gate.corpora.*;
import java.io.File;
import java.net.*;
import gate.gui.MainFrame;
import gate.util.Files;
import gate.util.GateRuntimeException;

public class TestMaxentWrapper extends TestCase {

  private static final boolean DEBUG=false;

  public TestMaxentWrapper(String name) {
    super(name);
  }

  /**
   * Flag to ensure Gate.init is only called once.
   */
  private static boolean gateInited = false;
  
  private static synchronized void initGate() throws Exception {
    if(!gateInited) {
      File gateHome = new File(System.getProperty("gate.home.location"));
      Gate.setGateHome(gateHome);
      Gate.init();

      File anniePlugin = new File(System.getProperty("annie.plugin"));
      Gate.getCreoleRegister().registerDirectories(anniePlugin.toURI().toURL());
      File mlPlugin = new File(System.getProperty("machinelearning.plugin"));
      Gate.getCreoleRegister().registerDirectories(mlPlugin.toURI().toURL());
      gateInited = true;
    }
  }
  
  /** Fixture set up - init GATE*/
  public void setUp() throws Exception {
    initGate();
  }

  /** Fixture tear down - does nothing */
  public void tearDown() throws Exception {
  } // tearDown

  /** Tests the MAXENT machine learning wrapper, by training it to identify
   * lookup annotations based on the precence of lookup annotations.
   */
  public void testMaxentWrapper() throws Exception {
    // Store the original standard output stream, so we can restore it later.
    java.io.PrintStream normalOutputStream=System.out;

    // Display the gui for debugging purposes.
         if (DEBUG) {
      MainFrame mainFrame = new MainFrame();
      mainFrame.setVisible(true);
    } else {
      // We don't want the output displayed unless we are debugging, so set the
      // standard output stream to a new one that never outputs anything.
      System.setOut(new java.io.PrintStream(
          new java.io.OutputStream() {
        public void write(int b) { }
        public void write(byte[] b, int off, int len) { }
      }));
    }

    //get a document - take it from the gate server.
    // tests/doc0.html is a simple html document.
    Document doc = Factory.newDocument(
      new URL(TestDocument.getTestServerName() + "tests/doc0.html")
    );

    // Get a tokeniser - just use all the default settings.
    LanguageAnalyser tokeniser=
        (LanguageAnalyser) Factory.createResource(
        "gate.creole.tokeniser.DefaultTokeniser");

    // Get a default gazetteer, again just use all the default settings
    LanguageAnalyser gazetteerInst =
        (LanguageAnalyser) Factory.createResource(
        "gate.creole.gazetteer.DefaultGazetteer");

    // Create the Maxent ML Processing resource.
    // First set up the parameters
    FeatureMap maxentParameters = Factory.newFeatureMap();
    maxentParameters.put("configFileURL", Files.getGateResource( 
                                 "/gate.ac.uk/tests/TestMaxentConfigFile.xml"));
    // Then actually make the PR
    LanguageAnalyser maxentPR =
        (LanguageAnalyser)
        Factory.createResource("gate.creole.ml.MachineLearningPR",
                               maxentParameters);

    // runtime stuff - set the document to be used with the gazetteer,the
    // tokeniser and the ML PR to doc, and run each of them in turn.
    tokeniser.setDocument(doc);
    tokeniser.execute();
    gazetteerInst.setDocument(doc);
    gazetteerInst.execute();
    maxentPR.setDocument(doc);
    maxentPR.execute();

    // Now run the trained maxent model.
    maxentPR.setParameterValue("training", Boolean.FALSE);
    maxentPR.execute();

    // Now clean up so we don't get a memory leak.
    Factory.deleteResource(doc);
    Factory.deleteResource(tokeniser);
    Factory.deleteResource(maxentPR);
    Factory.deleteResource(gazetteerInst);

    // Restore the standard output stream.
    System.setOut(normalOutputStream);
  } // TestMaxentWrapper

  /** Test suite routine for the test runner */
  public static Test suite() {
    return new TestSuite(TestMaxentWrapper.class);
  } // suite

  // The main class allows this class to be tested on its own, without the
  // need to call it from another class.
  public static void main(String[] args) {
    try{
      Gate.init();
      TestMaxentWrapper testMax = new TestMaxentWrapper("");
      testMax.setUp();
      testMax.testMaxentWrapper();
      testMax.tearDown();
    } catch(Exception e) {
      e.printStackTrace();
    }
  } // main

} // TestFlexibleGazetteer
