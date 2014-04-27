/*
 *  Copyright (c) 2005, The University of Sheffield.
 *
 *  This file is part of the GATE/UIMA integration layer, and is free
 *  software, released under the terms of the GNU Lesser General Public
 *  Licence, version 2.1 (or any later version).  A copy of this licence
 *  is provided in the file LICENCE in the distribution.
 *
 *  UIMA is a product of IBM, details are available from
 *  http://alphaworks.ibm.com/tech/uima
 */
package gate.uima.test;

import gate.*;
import gate.creole.*;
import gate.util.*;

import java.util.*;

import java.io.File;

import junit.framework.*;

/**
 * Test case for UIMA in GATE (i.e. AnalysisEnginePR).
 */
public class TestUIMAInGATE extends TestCase {

  /**
   * Location of gate - passed in as a system property by the test runner.
   */
  private File gateHome;

  /**
   * Location of uima plugin directory - passed in as a system property by the
   * test runner.
   */
  private File uimaPlugin;

  /**
   * examples directory under uima.
   */
  private File examplesDir;
  
  /**
   * conf directory under examples.
   */
  private File confDir;

  /**
   * uima_descriptors dir under conf.
   */
  private File descriptorsDir;

  /**
   * mapping dir under conf.
   */
  private File mappingDir;

  /**
   * The english tokeniser used in the tests.
   */
  private LanguageAnalyser tokeniser;

  /**
   * GATE application used for the tests.
   */
  private SerialAnalyserController app;

  /**
   * Corpus used by the tests.
   */
  private Corpus testCorpus;
  
  /**
   * Flag to ensure Gate.init is called only once, even if multiple instances
   * of the test case are used.
   */
  private static boolean gateInited = false;

  private static synchronized void initGate(File gateHomeDir,
                                            File uimaPluginDir,
                                            File examplesDir)
                                     throws Exception {
    if(!gateInited) {
      Gate.setGateHome(gateHomeDir);
      Gate.init();
      // load ANNIE
      Gate.getCreoleRegister().registerDirectories(new File(Gate.getPluginsHome(), "ANNIE").toURI().toURL());
      // load the uima plugin
      Gate.getCreoleRegister().registerDirectories(uimaPluginDir.toURI().toURL());
      // load the example annotators into the GATE classloader
      Gate.getCreoleRegister().registerDirectories(examplesDir.toURI().toURL());
      gateInited = true;
    }
  }
  
  public TestUIMAInGATE(String s) {
    super(s);
  }

  /**
   * Set up the fixture.
   */
  protected void setUp() throws Exception {
    super.setUp();
    
    // get paths
    uimaPlugin = new File(System.getProperty("gate.uima.plugin.location"));
    gateHome = new File(System.getProperty("gate.home.location"));

    examplesDir = new File(uimaPlugin, "examples");
    confDir = new File(examplesDir, "conf");
    descriptorsDir = new File(confDir, "uima_descriptors");
    mappingDir = new File(confDir, "mapping");
    
    initGate(gateHome, uimaPlugin, examplesDir);

    FeatureMap tokeniserParams = Factory.newFeatureMap();
    tokeniser = (LanguageAnalyser)Factory.createResource(
        "gate.creole.tokeniser.DefaultTokeniser", tokeniserParams);

    FeatureMap sacParams = Factory.newFeatureMap();
    app = (SerialAnalyserController)Factory.createResource(
        "gate.creole.SerialAnalyserController", sacParams);

    app.add(tokeniser);

    testCorpus = Factory.newCorpus("Test corpus");

    app.setCorpus(testCorpus);
  }

  /**
   * Clean up after ourselves.
   */
  protected void tearDown() throws Exception {
    super.tearDown();

    Factory.deleteResource(tokeniser);
    Factory.deleteResource(app);
    Factory.deleteResource(testCorpus);
  }

  public static Test suite() {
    return new TestSuite(TestUIMAInGATE.class);
  }


  ///// Tests /////
  
  /**
   * Test for a local analysis engine that updates a feature value.
   */
  public void testUpdatedOutput() throws Exception {
    File aeDescriptor = new File(descriptorsDir, "TokenHandlerAggregate.xml");
    updatedOutput(aeDescriptor);
  }

  /**
   * The same test as testUpdatedOutput, but using the analysis engine as a
   * Vinci service.
   */
  public void testVinciUpdatedOutput() throws Exception {
    File aeDescriptor = new File(descriptorsDir, "RemoteTokenHandler.xml");
    updatedOutput(aeDescriptor);
  }

  
  /**
   * Run "updated" test against the given analysis engine.
   */
  private void updatedOutput(File aeDescriptor) throws Exception {
    File gateMapping = new File(mappingDir, "TokenHandlerGateMapping.xml");

    FeatureMap aeprParams = Factory.newFeatureMap();
    aeprParams.put("analysisEngineDescriptor", aeDescriptor.toURI().toURL());
    aeprParams.put("mappingDescriptor", gateMapping.toURI().toURL());

    LanguageAnalyser aepr = (LanguageAnalyser)Factory.createResource(
        "gate.uima.AnalysisEnginePR", aeprParams);

    app.add(aepr);

    try {
      // create test document where the first token has one lower case letter,
      // the second has two, etc.
      Document testDoc = Factory.newDocument("ONe Two THree four");
      try {
        testCorpus.add(testDoc);
        try {
          app.execute();

          // Check the results
          AnnotationSet annots = testDoc.getAnnotations();
          assertNotNull("test document has no annotations!", annots);
          AnnotationSet tokens = annots.get(ANNIEConstants.TOKEN_ANNOTATION_TYPE);
          assertNotNull("test document has no Token annotations!", tokens);
          List tokensList = new ArrayList(tokens);
          // sort in document order
          Collections.sort(tokensList, new OffsetComparator());
          assertEquals(getLowercaseCount((Annotation)tokensList.get(0)), 1);
          assertEquals(getLowercaseCount((Annotation)tokensList.get(1)), 2);
          assertEquals(getLowercaseCount((Annotation)tokensList.get(2)), 3);
          assertEquals(getLowercaseCount((Annotation)tokensList.get(3)), 4);
        }
        finally {
          testCorpus.remove(testDoc);
        }
      }
      finally {
        Factory.deleteResource(testDoc);
      }
    }
    finally {
      app.remove(aepr);
    }
  }

  private static int getLowercaseCount(Annotation ann) throws Exception {
    FeatureMap features = ann.getFeatures();
    Integer lowerCount = (Integer)features.get("numLower");
    return lowerCount.intValue();
  }

  /**
   * A test for an analysis engine that removes some of the input annotations.
   */
  public void testRemovedOutput() throws Exception {
    File aeDescriptor = new File(descriptorsDir, "RemoveEvenLengthTokens.xml");
    File gateMapping = new File(mappingDir, "RemoveEvenGateMapping.xml");

    FeatureMap aeprParams = Factory.newFeatureMap();
    aeprParams.put("analysisEngineDescriptor", aeDescriptor.toURI().toURL());
    aeprParams.put("mappingDescriptor", gateMapping.toURI().toURL());

    LanguageAnalyser aepr = (LanguageAnalyser)Factory.createResource(
        "gate.uima.AnalysisEnginePR", aeprParams);

    app.add(aepr);

    try {
      Document testDoc = Factory.newDocument("1 22 333 4444 666666 55555");
      try {
        testCorpus.add(testDoc);
        try {
          app.execute();

          // Check the results
          AnnotationSet annots = testDoc.getAnnotations();
          assertNotNull("test document has no annotations!", annots);
          AnnotationSet tokens = annots.get(ANNIEConstants.TOKEN_ANNOTATION_TYPE);
          assertNotNull("test document has no Token annotations!", tokens);
          List tokensList = new ArrayList(tokens);
          // sort in document order
          Collections.sort(tokensList, new OffsetComparator());
          assertEquals("Document should have three tokens", tokensList.size(), 3);

          assertEquals("First token should be 1",
              getTokenString((Annotation)tokensList.get(0)), "1");
          assertEquals("Second token should be 333",
              getTokenString((Annotation)tokensList.get(1)), "333");
          assertEquals("Third token should be 55555",
              getTokenString((Annotation)tokensList.get(2)), "55555");
        }
        finally {
          testCorpus.remove(testDoc);
        }
      }
      finally {
        Factory.deleteResource(testDoc);
      }
    }
    finally {
      app.remove(aepr);
    }
  }

  private static String getTokenString(Annotation ann) throws Exception {
    FeatureMap features = ann.getFeatures();
    return (String)features.get("string");
  }

}
