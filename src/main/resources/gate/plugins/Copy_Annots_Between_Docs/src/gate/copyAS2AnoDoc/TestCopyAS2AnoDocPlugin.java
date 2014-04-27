/*
 *  TestCopyAS2AnoDocPlugin.java
 * 
 *  Yaoyong Li 08/10/2007
 *
 *  $Id: TestCopyAS2AnoDocPlugin.java, v 1.0 2009-05-10 11:44:16 +0000 yaoyong $
 */
package gate.copyAS2AnoDoc;

import gate.Annotation;
import gate.AnnotationSet;
import gate.Corpus;
import gate.Document;
import gate.Factory;
import gate.Gate;
import gate.GateConstants;

import gate.util.ExtensionFileFilter;
import gate.util.GateException;

import java.io.File;
import java.net.MalformedURLException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Vector;

import junit.framework.TestCase;

public class TestCopyAS2AnoDocPlugin extends TestCase {
  private static File learningHome;
  /** Use it to do initialisation only once. */
  private static boolean initialized = false;

  /**
   * Construction
   * 
   * @throws GateException
   * @throws MalformedURLException
   */
  public TestCopyAS2AnoDocPlugin(String name) throws GateException,
    MalformedURLException {
    super(name);
    if(!initialized) {
      Gate.init();
      learningHome = new File(new File(Gate.getGateHome(), "plugins"),
        "Copy_Annots_Between_Docs");
      Gate.getCreoleRegister().addDirectory(learningHome.toURI().toURL());
      initialized = true;
    }
  }

  /** Fixture set up */
  public void setUp() {
  } // setUp

  /**
   * Put things back as they should be after running tests.
   */
  public void tearDown() throws Exception {
    super.tearDown();
  } // tearDown

  /** The test for AnnotationMerging. */
  public void testCopyAS2AnoDocPlugin() throws Exception {
    Boolean savedSpaceSetting = Gate.getUserConfig().getBoolean(
      GateConstants.DOCUMENT_ADD_SPACE_ON_UNPACK_FEATURE_NAME);
    Gate.getUserConfig().put(
      GateConstants.DOCUMENT_ADD_SPACE_ON_UNPACK_FEATURE_NAME, Boolean.FALSE);
    //Create a object for merging
    CopyAS2AnoDocMain copyAnnsOne = (CopyAS2AnoDocMain)Factory
      .createResource("gate.copyAS2AnoDoc.CopyAS2AnoDocMain");
    //A corpus
    Corpus corpus = Factory.newCorpus("DataSet");
    ExtensionFileFilter fileFilter = new ExtensionFileFilter();
    fileFilter.addExtension("xml");
    File testData = new File(learningHome, "testData");
    File sourceDir = new File(testData, "source");
    String corpusDirName = new File(testData, "target").getAbsolutePath();
    File[] xmlFiles = new File(corpusDirName).listFiles(fileFilter);
    Arrays.sort(xmlFiles, new Comparator<File>() {
      public int compare(File a, File b) {
        return a.getName().compareTo(b.getName());
      }
    });
    for(File f : xmlFiles) {
      if(!f.isDirectory()) {
        Document doc = Factory.newDocument(f.toURI().toURL(), "UTF-8");
        doc.setName(f.getName());
        corpus.add(doc);
      }
    }

    gate.creole.SerialAnalyserController controller;
    controller = (gate.creole.SerialAnalyserController)Factory
      .createResource("gate.creole.SerialAnalyserController");
    controller.setCorpus(corpus);
    controller.add(copyAnnsOne);
    copyAnnsOne.setInputASName("ann1");
    copyAnnsOne.setOutputASName("ann5");
    copyAnnsOne.setSourceFilesURL(sourceDir.toURI().toURL());
    Vector<String>annTypes = new Vector<String>();
    annTypes.add("Os");
    annTypes.add("sent");
    copyAnnsOne.setAnnotationTypes(annTypes);
    
    controller.execute();
    
    Document doc = (Document)corpus.get(1);
    AnnotationSet anns = doc.getAnnotations("ann5").get("Os");
    int num;
    num = anns.size();
    assertEquals(num, 3);
    anns = doc.getAnnotations("ann5").get("sent");
    num = anns.size();
   
    assertEquals(num, 18);
    doc.removeAnnotationSet("ann5");
    
    doc = (Document)corpus.get(0);
    doc.removeAnnotationSet("ann5");
    
    System.out.println("completed");
    corpus.clear();
    Factory.deleteResource(corpus);
    controller.cleanup();
    Factory.deleteResource(controller);

    // finally {
    Gate.getUserConfig().put(
      GateConstants.DOCUMENT_ADD_SPACE_ON_UNPACK_FEATURE_NAME,
      savedSpaceSetting);
    // }
  }
}
