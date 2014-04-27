/*
 * GENIASentenceSplitter
 * 
 * Copyright (c) 2011, The University of Sheffield.
 * 
 * This file is part of GATE (see http://gate.ac.uk/), and is free software,
 * licenced under the GNU Library General Public License, Version 3, June 2007
 * (in the distribution as file licence.html, and also available at
 * http://gate.ac.uk/gate/licence.html).
 * 
 * Mark A. Greenwood, 01/06/2011
 */
package gate.creole.genia.splitter;

import gate.AnnotationSet;
import gate.Factory;
import gate.creole.AbstractLanguageAnalyser;
import gate.creole.ExecutionException;
import gate.creole.metadata.CreoleParameter;
import gate.creole.metadata.CreoleResource;
import gate.creole.metadata.Optional;
import gate.creole.metadata.RunTime;
import gate.util.Files;
import gate.util.ProcessManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.net.URL;

@CreoleResource(name = "GENIA Sentence Splitter", icon = "sentence-splitter.png", helpURL = "http://gate.ac.uk/userguide/sec:domain-creole:biomed:genia")
public class GENIASentenceSplitter extends AbstractLanguageAnalyser {

  private boolean debug = false;

  private String annotationSetName;

  private URL splitterBinary;

  private ProcessManager manager = new ProcessManager();

  public Boolean getDebug() {
    return debug;
  }

  @RunTime
  @CreoleParameter(defaultValue = "false", comment = "if true then debugging info is reported")
  public void setDebug(Boolean debug) {
    this.debug = debug;
  }

  public URL getSplitterBinary() {
    return splitterBinary;
  }

  @RunTime
  @CreoleParameter(comment = "the URL of the GENIA sentence splitter binary")
  public void setSplitterBinary(URL splitterBinary) {
    this.splitterBinary = splitterBinary;
  }

  public String getAnnotationSetName() {
    return annotationSetName;
  }

  @RunTime
  @Optional
  @CreoleParameter(comment = "the annotation set in which Sentence annotations will be created")
  public void setAnnotationSetName(String annotationSetName) {
    this.annotationSetName = annotationSetName;
  }

  public void execute() throws ExecutionException {

    // get the sentence splitter file from the URL provided
    File splitter = Files.fileFromURL(splitterBinary);

    // get the document content and replace non-breaking spaces with spaces
    // TODO replace new-lines with spaces so we don't get a sentence per line
    String docContent =
            document.getContent().toString().replace((char)160, ' ');

    try {
      // create temporary files to use with the external sentence splitter
      File tmpIn = File.createTempFile("GENIA", ".txt");
      File tmpOut = File.createTempFile("GENIA", ".txt");

      // store the document content in the input file
      FileOutputStream fos = new FileOutputStream(tmpIn);
      fos.write(docContent.getBytes("utf8"));
      fos.close();

      // setup the command line to run the sentence splitter
      String[] args =
              new String[]{splitter.getAbsolutePath(), tmpIn.getAbsolutePath(),
                  tmpOut.getAbsolutePath()};

      // run the sentence splitter over the docuement
      manager.runProcess(args, splitter.getParentFile(), (debug
              ? System.out
              : null), (debug ? System.err : null));

      // get the annotation set we are going to store results in
      AnnotationSet annotationSet = document.getAnnotations(annotationSetName);

      // we haven't found any sentence yet so start looking for the next one
      // from the beginning of the document
      int end = 0;

      // read in the output from the sentence splitter one line at a time
      BufferedReader in = new BufferedReader(new FileReader(tmpOut));
      String sentence = in.readLine();
      while(sentence != null) {

        // trim the sentence so we don't annotate extranious white space,
        // this isn't python code after all :)
        sentence = sentence.trim();

        //find the start of the sentence
        //TODO throw a sensible exception if the sentence can't be found?
        int start = docContent.indexOf(sentence, end);

        //work out where the sentence ends
        end = start + sentence.length();

        if(end > start) {
          //the sentence has a length so annotate it 
          annotationSet.add((long)start, (long)end, "Sentence",
                  Factory.newFeatureMap());
        }

        //get the next line from the output from the tagger
        sentence = in.readLine();
      }

      //delete the temp files
      if(!debug && !tmpIn.delete()) tmpIn.deleteOnExit();
      if(!debug && !tmpOut.delete()) tmpOut.deleteOnExit();

    } catch(Exception ioe) {
      throw new ExecutionException("An error occured running the splitter", ioe);
    }
  }
}
