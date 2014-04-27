package gate.bdmComp;

import gate.Corpus;
import gate.Factory;
import gate.FeatureMap;
import gate.Gate;
import gate.GateConstants;
import gate.creole.ontology.Ontology;
import gate.util.GateException;

import java.io.File;
import java.net.MalformedURLException;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class TestBDMCompPlugin extends TestCase {
  /** Use it to do initialisation only once. */
  private static boolean initialized = false;
  /** Learning home for reading the data and configuration file. */
  private static File bdmPluginHome;
  
  /** Constructor, setting the home directory. */
  public TestBDMCompPlugin(String arg0) throws GateException,
    MalformedURLException {
    super(arg0);
    if(!initialized) {
      Gate.init();
      File owlimPluginHome = new File(new File(Gate.getGateHome(), "plugins"), "Ontology");
      Gate.getCreoleRegister().addDirectory(owlimPluginHome.toURI().toURL());
      bdmPluginHome = new File(new File(Gate.getGateHome(), "plugins"),
        "Ontology_BDM_Computation");
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
    return new TestSuite(TestBDMCompPlugin.class);
  } // suite
  
  /** The test the IAA. */
  public void testBDMCompPlugin() throws Exception {

    Boolean savedSpaceSetting = Gate.getUserConfig().getBoolean(
            GateConstants.DOCUMENT_ADD_SPACE_ON_UNPACK_FEATURE_NAME);
    Gate.getUserConfig().put(
            GateConstants.DOCUMENT_ADD_SPACE_ON_UNPACK_FEATURE_NAME,
            Boolean.FALSE);
    try {
      // Load the documents into a corpus
      Corpus data = Factory.newCorpus("data");
      
      String corpusDirName; 
      corpusDirName = new File(bdmPluginHome, "test/ontology").getAbsolutePath();
      
      BDMCompMain bdmM;

      FeatureMap parameters = Factory.newFeatureMap();
      
      bdmM = (BDMCompMain)Factory.createResource(
        "gate.bdmComp.BDMCompMain", parameters);
      
      File testOnto = new File(corpusDirName, "protont.owl");
      File bdmFile = new File(corpusDirName, "protont-bdm.txt");
     
      FeatureMap fm = Factory.newFeatureMap(); 
      fm.put("rdfXmlURL", testOnto.toURI().toURL());
            
      Ontology ontology = (Ontology)Factory.createResource("gate.creole.ontology.impl.sesame.OWLIMOntology", fm);
             
      bdmM.setOntology(ontology); //("ann1;ann2;ann3");
      bdmM.setOutputBDMFile(bdmFile.toURI().toURL());
      //bdmM.setAnnTypesAndFeats("Os;sent->Op");
      //bdmM.setVerbosity("0");
      /** The controller include the ML Api as one PR. */
      gate.creole.SerialController
      controller = (gate.creole.SerialController)Factory
      .createResource("gate.creole.SerialController");
      //controller.setCorpus(data);
      controller.add(bdmM);
      
      System.out.println("starting executing...");
      
      controller.execute();
      
      /** four BDM scores for testing purpose. */
      float [] bdmTS = new float[4];
      
      for(BDMOne oneb: bdmM.bdmScores) {
        String key = oneb.con11.getName();
        String res = oneb.con22.getName();
        if((key.equals("ContactInformation") && res.equals("Topic")) ||
          (res.equals("ContactInformation") && key.equals("Topic")))
          bdmTS[0] = oneb.bdmScore;
        if((key.equals("InformationResource") && res.equals("GeneralTerm")) ||
          (res.equals("InformationResource") && key.equals("GeneralTerm")))
          bdmTS[1] = oneb.bdmScore;
        if((key.equals("Recognized") && res.equals("Entity")) ||
          (res.equals("Recognized") && key.equals("Entity")))
          bdmTS[2] = oneb.bdmScore;
        if((key.equals("Role") && res.equals("Event")) ||
          (res.equals("Role") && key.equals("Event")))
          bdmTS[3] = oneb.bdmScore;
      }
      
      int[] nPwF = new int[4];
      nPwF[0] = (int)Math.ceil((double)bdmTS[0]*1000);
      nPwF[1] = (int)Math.ceil((double)bdmTS[1]*1000);
      nPwF[2] = (int)Math.ceil((double)bdmTS[2]*1000);
      nPwF[3] = (int)Math.ceil((double)bdmTS[3]*1000);
      //System.out.println("1="+nPwF[0]+", 2="+nPwF[1]+", 3="+nPwF[2]+", 4="+nPwF[3]+".");
      assertEquals("Wrong value for correct: ", 488, nPwF[0]);
      assertEquals("Wrong value for correct: ", 0, nPwF[1]);
      assertEquals("Wrong value for correct: ", 0, nPwF[2]);
      assertEquals("Wrong value for correct: ", 290, nPwF[3]);
    }
    finally {
      Gate.getUserConfig().put(
              GateConstants.DOCUMENT_ADD_SPACE_ON_UNPACK_FEATURE_NAME,
              savedSpaceSetting);
    }

  }

}
