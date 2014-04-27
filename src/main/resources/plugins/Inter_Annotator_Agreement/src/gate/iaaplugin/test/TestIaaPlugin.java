/*
 *  TestIaaPlugin.java
 * 
 *  Yaoyong Li 15/07/2008
 *
 *  $Id: TestIaaPlugin.java, v 1.0 2008-07-15 12:58:16 +0000 yaoyong $
 */

package gate.iaaplugin.test;

import java.io.File;
import java.net.MalformedURLException;
import java.util.Arrays;
import java.util.Comparator;

import gate.Corpus;
import gate.Document;
import gate.Factory;
import gate.FeatureMap;
import gate.Gate;
import gate.GateConstants;
import gate.util.ExtensionFileFilter;
import gate.util.GateException;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import gate.iaaplugin.IaaMain;
import gate.iaaplugin.MeasureType;
/**
 * Test the IAA computation by using the test
 * methods and small dataset.
 */
public class TestIaaPlugin extends TestCase {
  /** Use it to do initialisation only once. */
  private static boolean initialized = false;
  /** Learning home for reading the data and configuration file. */
  private static File iaaPluginHome;
  
  /** Constructor, setting the home directory. */
  public TestIaaPlugin(String arg0) throws GateException,
    MalformedURLException {
    super(arg0);
    if(!initialized) {
      Gate.init();
      iaaPluginHome = new File(new File(Gate.getGateHome(), "plugins"),
        "Inter_Annotator_Agreement");
      Gate.getCreoleRegister().addDirectory(iaaPluginHome.toURI().toURL());
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
  } // tearDown

  /** Test suite routine for the test runner */
  public static Test suite() {
    return new TestSuite(TestIaaPlugin.class);
  } // suite
  
  /** The test the IAA. */
  public void testIaaPlugin() throws Exception {

    Boolean savedSpaceSetting = Gate.getUserConfig().getBoolean(
            GateConstants.DOCUMENT_ADD_SPACE_ON_UNPACK_FEATURE_NAME);
    Gate.getUserConfig().put(
            GateConstants.DOCUMENT_ADD_SPACE_ON_UNPACK_FEATURE_NAME,
            Boolean.FALSE);
    try {
      
      //Gate.setGateHome(new File("C:\\svn\\gate"));
      //Gate.setUserConfigFile(new File("C:\\svn\\gate.xml"));
      //Gate.init();
      //ExtensionFileFilter fileFilter = new ExtensionFileFilter();
      //fileFilter.addExtension("xml");
      //iaaPluginHome = new File(new File(Gate.getGateHome(), "plugins"),
      //"iaaPlugin");
      //Gate.getCreoleRegister().addDirectory(iaaPluginHome.toURI().toURL());

      // Load the documents into a corpus
      Corpus data = Factory.newCorpus("data");
      ExtensionFileFilter fileFilter = new ExtensionFileFilter();
      fileFilter.addExtension("xml");
      data.populate(new File(iaaPluginHome, "test").toURI().toURL(), fileFilter, "UTF-8", false);
      
      /*String corpusDirName = new File(iaaPluginHome, "test")
      .getAbsolutePath();
      
      
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
          data.add(doc);
        }
      }*/
      
      //String testDir = "plugins/iaaPlugin/test/";
      
      //System.out.println("testDir00=*"+(new File(testDir,"beijing-opera.xml")).getAbsolutePath().toString()+"*");
      /*Document doc = Factory.newDocument(new File("C:\\svn\\gate\\plugins\\iaaPlugin\\test\\beijing-opera.xml").toURI().toURL(), "UTF-8");
      Document doc1 = Factory.newDocument(new File("C:\\svn\\gate\\plugins\\iaaPlugin\\test\\beijing-opera-2.xml").toURI().toURL(), "UTF-8");
      data.add(doc);
      data.add(doc1);*/
      
      IaaMain iaaM;

      FeatureMap parameters = Factory.newFeatureMap();
      
      iaaM = (IaaMain)Factory.createResource(
        "gate.iaaplugin.IaaMain", parameters);
     
      iaaM.setAnnSetsForIaa("ann1;ann2;ann3");
      iaaM.setAnnTypesAndFeats("Os;sent->Op");
      iaaM.setVerbosity("0");
      /** The controller include the ML Api as one PR. */
      gate.creole.SerialAnalyserController
      controller = (gate.creole.SerialAnalyserController)Factory
      .createResource("gate.creole.SerialAnalyserController");
      controller.setCorpus(data);
      controller.add(iaaM);
      
      controller.execute();
      
      int[] nPwF = new int[4];
      nPwF[0] = (int)Math.ceil((double)iaaM.fMeasureOverallTypes.correct*100);
      nPwF[1] = (int)Math.ceil((double)iaaM.fMeasureOverallTypes.partialCor*100);
      nPwF[2] = (int)Math.ceil((double)iaaM.fMeasureOverallTypes.spurious*100);
      nPwF[3] = (int)Math.ceil((double)iaaM.fMeasureOverallTypes.missing*100);
      
      assertEquals("Wrong value for correct: ", nPwF[0], 425);
      assertEquals("Wrong value for correct: ", nPwF[1], 50);
      assertEquals("Wrong value for correct: ", nPwF[2], 167);
      assertEquals("Wrong value for correct: ", nPwF[3], 234);
      
      //test f-measure on named entity recognition
      iaaM.setAnnTypesAndFeats("Os");
      iaaM.setVerbosity("0");
      
      controller.execute();
      
      nPwF[0] = (int)Math.ceil((double)iaaM.fMeasureOverallTypes.correct*100);
      nPwF[1] = (int)Math.ceil((double)iaaM.fMeasureOverallTypes.partialCor*100);
      nPwF[2] = (int)Math.ceil((double)iaaM.fMeasureOverallTypes.spurious*100);
      nPwF[3] = (int)Math.ceil((double)iaaM.fMeasureOverallTypes.missing*100);
      
      assertEquals("Wrong value for correct: ", nPwF[0], 167);
      assertEquals("Wrong value for correct: ", nPwF[1], 100);
      assertEquals("Wrong value for correct: ", nPwF[2], 117);
      assertEquals("Wrong value for correct: ", nPwF[3], 150);
      
      //Test kappa on sentence classification
      iaaM.setAnnTypesAndFeats("sent->Op");
      iaaM.setVerbosity("0");
      MeasureType pt = MeasureType.AGREEMENTANDKAPPA;
      iaaM.setMeasureType(pt);
      
      controller.execute();
      
      nPwF[0] = (int)Math.ceil((double)iaaM.overallTypesPairs[0]*1000);
      nPwF[1] = (int)Math.ceil((double)iaaM.overallTypesPairs[1]*1000);
      nPwF[2] = (int)Math.ceil((double)iaaM.overallTypesPairs[2]*1000);
      
      assertEquals("Wrong value for correct: ", nPwF[0], 847);
      assertEquals("Wrong value for correct: ", nPwF[1], 761);
      assertEquals("Wrong value for correct: ", nPwF[2], 759);
      
    }
    finally {
      Gate.getUserConfig().put(
              GateConstants.DOCUMENT_ADD_SPACE_ON_UNPACK_FEATURE_NAME,
              savedSpaceSetting);
    }

  }
  
  /** The test the IAA using BDM. */
  public void testIaaBDM() throws Exception {

    Boolean savedSpaceSetting = Gate.getUserConfig().getBoolean(
            GateConstants.DOCUMENT_ADD_SPACE_ON_UNPACK_FEATURE_NAME);
    Gate.getUserConfig().put(
            GateConstants.DOCUMENT_ADD_SPACE_ON_UNPACK_FEATURE_NAME,
            Boolean.FALSE);
    try {
      
      // Load the documents into a corpus
      Corpus data = Factory.newCorpus("data");
      ExtensionFileFilter fileFilter = new ExtensionFileFilter();
      fileFilter.addExtension("xml");
      File testDir = new File(new File(iaaPluginHome, "test"), "ontoData");
      data.populate(testDir.toURI().toURL(), fileFilter, "UTF-8", false);
      
      //String testDir = "plugins/iaaPlugin/test/";
      
      //System.out.println("testDir00=*"+(new File(testDir,"beijing-opera.xml")).getAbsolutePath().toString()+"*");
      /*Document doc = Factory.newDocument(new File("C:\\svn\\gate\\plugins\\iaaPlugin\\test\\beijing-opera.xml").toURI().toURL(), "UTF-8");
      Document doc1 = Factory.newDocument(new File("C:\\svn\\gate\\plugins\\iaaPlugin\\test\\beijing-opera-2.xml").toURI().toURL(), "UTF-8");
      data.add(doc);
      data.add(doc1);*/
      
      IaaMain iaaM;

      FeatureMap parameters = Factory.newFeatureMap();
      
      iaaM = (IaaMain)Factory.createResource(
        "gate.iaaplugin.IaaMain", parameters);
     
      iaaM.setAnnSetsForIaa("Key;Response");
      iaaM.setAnnTypesAndFeats("Mention->class");
      iaaM.setVerbosity("0");
      iaaM.setBdmScoreFile(new File(testDir, "protonU-bdm.txt").toURI().toURL());
      /** The controller include the ML Api as one PR. */
      gate.creole.SerialAnalyserController
      controller = (gate.creole.SerialAnalyserController)Factory
      .createResource("gate.creole.SerialAnalyserController");
      controller.setCorpus(data);
      controller.add(iaaM);
      
      controller.execute();
      
      int[] nPwF = new int[4];
      nPwF[0] = (int)Math.ceil((double)iaaM.fMeasureOverallTypesBDM.correct*100);
      nPwF[1] = (int)Math.ceil((double)iaaM.fMeasureOverallTypesBDM.partialCor*100);
      nPwF[2] = (int)Math.ceil((double)iaaM.fMeasureOverallTypesBDM.spurious*100);
      nPwF[3] = (int)Math.ceil((double)iaaM.fMeasureOverallTypesBDM.missing*100);
      
      assertEquals("Wrong value for correct: ", nPwF[0], 764);
      assertEquals("Wrong value for correct: ", nPwF[1], 0);
      assertEquals("Wrong value for correct: ", nPwF[2], 1050);
      assertEquals("Wrong value for correct: ", nPwF[3], 5600);
      
    }
    finally {
      Gate.getUserConfig().put(
              GateConstants.DOCUMENT_ADD_SPACE_ON_UNPACK_FEATURE_NAME,
              savedSpaceSetting);
    }

  }

}
