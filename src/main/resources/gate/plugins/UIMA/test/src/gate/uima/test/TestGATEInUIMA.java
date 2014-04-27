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

import junit.framework.*;

import java.io.File;

import org.apache.uima.UIMAFramework;
import org.apache.uima.util.XMLParser;
import org.apache.uima.util.XMLInputSource;
import org.apache.uima.util.InvalidXMLException;
import org.apache.uima.resource.ResourceSpecifier;
import org.apache.uima.analysis_engine.AnalysisEngine;
import org.apache.uima.cas.Type;
import org.apache.uima.cas.Feature;
import org.apache.uima.cas.FSIndex;
import org.apache.uima.cas.FSIterator;
import org.apache.uima.cas.FeatureStructure;
import org.apache.uima.cas.CAS;

/**
 * Test case for GATE in UIMA (i.e. GATEApplicationAnnotator).
 */
public class TestGATEInUIMA extends TestCase {
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
   * test/conf directory under uima plugin.
   */
  private File testConfDir;

  /**
   * The UIMA XML parser, used to parse TAE descriptors.
   */
  private XMLParser uimaXMLParser;

  /**
   * Set up the fixture.
   */
  protected void setUp() throws Exception {
    super.setUp();
    
    // get paths
    uimaPlugin = new File(System.getProperty("gate.uima.plugin.location"));
    gateHome = new File(System.getProperty("gate.home.location"));

    testConfDir = new File(new File(uimaPlugin, "test"), "conf");

    uimaXMLParser = UIMAFramework.getXMLParser();
  }

  /**
   * Clean up after ourselves.
   */
  protected void tearDown() throws Exception {
    super.tearDown();

  }

  public static Test suite() {
    return new TestSuite(TestGATEInUIMA.class);
  }


  public void testGatePOSTagger() throws Exception {
    // load the TAE containing UIMA tokeniser and GATE POS tagger
    File tokAndPOSTaggerDescriptorFile =
      new File(testConfDir, "TokenizerAndPOSTagger.xml");

    XMLInputSource inputSource =
      new XMLInputSource(tokAndPOSTaggerDescriptorFile);

    ResourceSpecifier tokAndPOSTaggerDescriptor =
      uimaXMLParser.parseResourceSpecifier(inputSource);

    AnalysisEngine tokAndPOSTagger =
      UIMAFramework.produceAnalysisEngine(tokAndPOSTaggerDescriptor);

    // create CAS and populate it with initial text.
    CAS cas = tokAndPOSTagger.newCAS();

    cas.setDocumentText(
        "This is a test document. This is the second sentence.");
    // what POS tags do we expect to get back?
    String[] expectedPOSTags = new String[] {
      "DT",    // This
      "VBZ",   // is
      "DT",    // a
      "NN",    // test
      "NN",    // document
      ".",     // .
      "DT",    // This
      "VBZ",   // is
      "DT",    // the
      "JJ",    // second
      "NN",    // sentence
      "."      // .
    };

    // run the beast
    tokAndPOSTagger.process(cas);

    // check the results have the right POS tags
    Type tokenType = cas.getTypeSystem().getType(
        "org.apache.uima.examples.tokenizer.Token");
    assertNotNull("Token type not found in type system", tokenType);

    Feature posFeature = tokenType.getFeatureByBaseName("POS");
    assertNotNull("Token POS feature not found", posFeature);
    
    FSIndex tokensIndex = cas.getAnnotationIndex(tokenType);
    FSIterator tokensIt = tokensIndex.iterator();
    int tokenNo = 0;
    while(tokensIt.isValid()) {
      // make sure we don't have too many tokens
      assertTrue("Found more tokens than expected",
                 tokenNo < expectedPOSTags.length);
      FeatureStructure token = tokensIt.get();
      String actualPOS = token.getStringValue(posFeature);
      assertEquals("Token has wrong part of speech",
                   expectedPOSTags[tokenNo], actualPOS);
      tokensIt.moveToNext();
      tokenNo++;
    }

    assertEquals("Found fewer tokens than expected",
                 tokenNo, expectedPOSTags.length);
  }
}

