/*
 *  TestChineseSegMain.java
 *
 *  Yaoyong Li 23/04/2009
 *
 *  $Id: TestChineseSegMain.java 13646 2011-04-08 11:17:49Z ian_roberts $
 */

package gate.chineseSeg;

import gate.Corpus;
import gate.Document;
import gate.Factory;
import gate.FeatureMap;
import gate.Gate;
import gate.GateConstants;
import gate.util.BomStrippingInputStreamReader;
import gate.util.ExtensionFileFilter;
import gate.util.GateException;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Map;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class TestChineseSegMain extends TestCase {
  /** Use it to do initialisation only once. */
  private static boolean initialized = false;
  /** Learning home for reading the data and configuration file. */
  private static File bdmPluginHome;

  /** Constructor, setting the home directory. */
  public TestChineseSegMain(String arg0) throws GateException,
    MalformedURLException {
    super(arg0);
    if(!initialized) {
      Gate.init();

      bdmPluginHome = new File(new File(Gate.getGateHome(), "plugins"),
        "Lang_Chinese");
      Gate.getCreoleRegister().addDirectory(bdmPluginHome.toURI().toURL());
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
    return new TestSuite(TestChineseSegMain.class);
  } // suite

  /** The test the IAA. */
  public void testChineseSegMain() throws Exception {

    Boolean savedSpaceSetting = Gate.getUserConfig().getBoolean(
            GateConstants.DOCUMENT_ADD_SPACE_ON_UNPACK_FEATURE_NAME);
    Gate.getUserConfig().put(
            GateConstants.DOCUMENT_ADD_SPACE_ON_UNPACK_FEATURE_NAME,
            Boolean.FALSE);
    try {

      String corpusDirName;
      corpusDirName = new File(bdmPluginHome, "test").getAbsolutePath();

      ChineseSegMain chineseSeg = null;

      FeatureMap parameters = Factory.newFeatureMap();

      chineseSeg = (ChineseSegMain)Factory.createResource(
        "gate.chineseSeg.ChineseSegMain", parameters);

      File trainData = new File(corpusDirName, "trainingData-utf8");
      File modelDir = new File(corpusDirName, "model-utf8");
      File testData = new File(corpusDirName, "testData-utf8");

      chineseSeg.setModelURL(modelDir.toURI().toURL());
      chineseSeg.setTextFilesURL(trainData.toURI().toURL());
      chineseSeg.setLearningMode(RunMode.LEARNING);

      chineseSeg.setTextCode("UTF-8");
      chineseSeg.setLearningAlg("PAUM");

      /** The controller include the segmenter as one PR. */
      gate.creole.SerialController
      controller = (gate.creole.SerialController)Factory
      .createResource("gate.creole.SerialController");
      //controller.setCorpus(data);
      controller.add(chineseSeg);

      //System.out.println("starting executing...");
      //learning ...
      emptySavedFiles(modelDir); //remove the model files
      controller.execute();

      //Application
      chineseSeg.setTextFilesURL(testData.toURI().toURL());
      chineseSeg.setLearningMode(RunMode.SEGMENTING);

      controller.execute();

      //emptySavedFiles(modelDir); //remove the model files

      BufferedReader inSegText = null;
      inSegText = new BomStrippingInputStreamReader(
          new FileInputStream(new File(new File(testData, ConstantParameters.FILENAME_resultsDir),
            "doc-utf8.txt.seg.txt")), "UTF-8");

      for(int i=0; i<14; ++i)
        inSegText.readLine();

      String lastLine = inSegText.readLine();

      inSegText.close();

      char[] chs  = new char[17];

      for(int i=0; i<17; ++i) {
        chs[i] = lastLine.charAt(i);
        //System.out.println("i="+i+", ch="+chs[i]+"*");
      }

      //System.out.println("1="+nPwF[0]+", 2="+nPwF[1]+", 3="+nPwF[2]+", 4="+nPwF[3]+".");
      assertEquals("Wrong value for correct: ", ConstantParameters.SEPARATTOR_BLANK, chs[2]);
      assertEquals("Wrong value for correct: ", '9', chs[4]);
      assertEquals("Wrong value for correct: ", ConstantParameters.SEPARATTOR_BLANK, chs[8]);
      assertEquals("Wrong value for correct: ", ConstantParameters.SEPARATTOR_BLANK, chs[14]);

    }
    finally {
      Gate.getUserConfig().put(
              GateConstants.DOCUMENT_ADD_SPACE_ON_UNPACK_FEATURE_NAME,
              savedSpaceSetting);
    }

  }

  private void emptySavedFiles(File savedFilesDir) {
    (new File(savedFilesDir, ConstantParameters.FILENAME_TERMS)).delete();
    (new File(savedFilesDir, ConstantParameters.FILENAMEOFLabelList)).delete();
    (new File(savedFilesDir, ConstantParameters.FILENAMEOFLOGFILE)).delete();
    (new File(savedFilesDir, ConstantParameters.FILENAMEOFFeatureVectorData)).delete();
  }
}
