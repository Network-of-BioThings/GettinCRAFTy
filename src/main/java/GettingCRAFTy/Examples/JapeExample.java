package GettingCRAFTy.Examples;

import java.util.*;
import java.io.*;
import java.net.*;

import gate.*;
import gate.creole.*;
import gate.util.*;
import gate.util.persistence.PersistenceManager;
import gate.corpora.RepositioningInfo;
import gate.creole.ontology.*;

/**
 * This class illustrates how to use ANNIE as a sausage machine
 * in another application - put ingredients in one end (URLs pointing
 * to documents) and get sausages (e.g. Named Entities) out the
 * other end.
 * <P><B>NOTE:</B><BR>
 * For simplicity's sake, we don't do any exception handling.
 */

public class JapeExample  {


  public static void main(String args[]) throws GateException, IOException {

      // initialise GATE
      //Gate.setGateHome(new File("C:\\Program Files\\GATE_Developer_7.1"));
      Gate.setGateHome(new File("src/main/resources/gate"));
      Gate.setPluginsHome(new File("src/main/resources/gate/plugins"));
      Gate.init();

      // load ANNIE plugin - you must do this before you can create tokeniser
      // or JAPE transducer resources.
      Gate.getCreoleRegister()
	  .registerDirectories(new File(Gate.getPluginsHome(), "ANNIE").toURI().toURL());

      // Build the pipeline
      SerialAnalyserController pipeline =
	  (SerialAnalyserController)Factory
	  .createResource("gate.creole.SerialAnalyserController");
      LanguageAnalyser tokeniser = (LanguageAnalyser)Factory
	  .createResource("gate.creole.tokeniser.DefaultTokeniser");

      String japeFilePath = "src/main/resources/gate/jape/CellExample.jape";

      LanguageAnalyser jape = (LanguageAnalyser)Factory
      	  .createResource("gate.creole.Transducer", 
      			  gate.Utils.featureMap("grammarURL", 
						new File(japeFilePath).toURI().toURL(),
      						"encoding", "UTF-8")); 

      // // Find the directory for the Ontology plugin 
      // 2File pluginHome = 
      // 3 new File(new File(Gate.getGateHome(), "plugins"), "Ontology"); 
      // 4// Load the plugin from that directory 
      // 5Gate.getCreoleRegister().registerDirectories(pluginHome.toURI().toURL());


      // step 1: initialize GATE 
      //if(!Gate.isInitialized()) { Gate.init(); } 
      
      // step 2: load the Ontology plugin that contains the implementation 
      File ontoHome = new File(Gate.getPluginsHome(),"Ontology"); 
      Gate.getCreoleRegister().addDirectory(ontoHome.toURL()); 
      
      System.out.println("GATE HOME:\n");
      System.out.println(Gate.getGateHome());

      // step 3: set the parameters 
      FeatureMap fm = Factory.newFeatureMap(); 
      fm.put("rdfXmlURL", "../../../../../../src/main/resources/owl/CL.owl");
      //fm.put("rdfXmlURL", "../owl/CL.owl");
      //this is crap - but it seems to be relative to the plug-in not the gate home
      fm.put("dataDirectoryURL", "../../../../../../src/main/resources/gate/");
 

     //fm.put("baseURI", theBaseURI); 
      //fm.put("mappingsURL", urlOfTheMappingsFile); 
      // .. any other parameters 
 
      // step 4: finally create an instance of ontology 
      Ontology ontology = (Ontology) 
	  Factory.createResource("gate.creole.ontology.impl.sesame.OWLIMOntology", 
				 fm); 
 
      // //PRINT ALL THE CLASSES
      // // retrieving a list of top classes 
      // Set<OClass> topClasses = ontology.getOClasses(true); 
      //  // for all top classes, printing their direct sub classes and print 
      // // their URI or blank node ID in turtle format. 
      // for(OClass c : topClasses) { 
      // 	  Set<OClass> dcs = c.getSubClasses(OConstants.DIRECT_CLOSURE); 
      // 	  for(OClass sClass : dcs) { 
      // 	      System.out.println(sClass.getONodeID().toTurtle()); 
      // 	      System.out.println(sClass.getLabels());
      // 	  } 
      // } 


      // FeatureMap params = Factory.newFeatureMap();  
      // params.put("listsUrl", listsDefLocation);  
      // LanguageAnalyser mainGazetteer = (LanguageAnalyser)Factory
      // 	  .createResource("gate.creole.gazetteer.DefaultGazetteer", params);

      // Then create any number of SharedDefaultGazetteer instances, 
      // 	  passing this regular gazetteer as a parameter:
	  
      // 	  FeatureMap params = Factory.newFeatureMap();  
      // params.put("bootstrapGazetteer", mainGazetteer);  
      // LanguageAnalyser sharedGazetteer = (LanguageAnalyser)Factory
      // 	  .createResource("gate.creole.gazetteer.SharedDefaultGazetteer", params);

      // testing

      pipeline.add(tokeniser);
      //pipeline.add(ontology);
      pipeline.add(jape);

      // create document and corpus
      Corpus corpus = Factory.newCorpus(null);
      Document doc = Factory.newDocument("This is test. And this is a mention of cell.");
      corpus.add(doc);
      pipeline.setCorpus(corpus);

      // run it
      pipeline.execute();

      // extract results
      System.out.println("Found annotations of the following types: " +
			 doc.getAnnotations().getAllTypes());
      System.out.println("Number of annotations: " +
			 doc.getAnnotations().size());


    System.exit(0);
  }

}
