package GettingCRAFTy.Examples;

import CRAFTReaderEnvironment;

import java.util.*;
import java.io.*;
import java.net.*;

import gate.*;
import gate.creole.*;
import gate.util.*;
import gate.util.persistence.PersistenceManager;
import gate.corpora.RepositioningInfo;

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

      // Here we want to add things to the pipeline
      new CRAFTReaderEnvironment();
      pipeline.add(tokeniser);
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
      System.out.println("Found annotations of the following types: " +
			 doc.getAnnotations());
  }
}
