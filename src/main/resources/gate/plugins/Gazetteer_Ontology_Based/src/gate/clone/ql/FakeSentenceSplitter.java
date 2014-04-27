/*
 *  FakeSentenceSplitter.java
 *
 *  Copyright (c) 1995-2012, The University of Sheffield. See the file
 *  COPYRIGHT.txt in the software or at http://gate.ac.uk/gate/COPYRIGHT.txt
 *
 *  This file is part of GATE (see http://gate.ac.uk/), and is free
 *  software, licenced under the GNU Library General Public License,
 *  Version 2, June 1991 (in the distribution as file licence.html,
 *  and also available at http://gate.ac.uk/gate/licence.html).
 */
package gate.clone.ql;

import gate.AnnotationSet;
import gate.Factory;
import gate.Resource;
import gate.creole.AbstractLanguageAnalyser;
import gate.creole.ExecutionException;
import gate.creole.ResourceInstantiationException;
import gate.util.GateRuntimeException;
import gate.util.InvalidOffsetException;

/**
 * This class serves to simulate running of default SentenceSplitter: it will
 * always create one sentence out of any document. It is created so that in
 * cases when processing one sentence always, we don't need to run regular
 * Sentence Splitter, in order to run a POS Tagger.
 * 
 * @author Danica Damljanovic
 * 
 */
public class FakeSentenceSplitter extends AbstractLanguageAnalyser {

  private static final long serialVersionUID = 9202556754479248757L;

  public static final String SPLIT_DOCUMENT_PARAMETER_NAME = "document";

  public static final String SPLIT_INPUT_AS_PARAMETER_NAME = "inputASName";

  public static final String SPLIT_OUTPUT_AS_PARAMETER_NAME = "outputASName";

  public static final String SPLIT_ENCODING_PARAMETER_NAME = "encoding";

  public static final String SPLIT_GAZ_URL_PARAMETER_NAME = "gazetteerListsURL";

  public static final String SPLIT_TRANSD_URL_PARAMETER_NAME = "transducerURL";

  public Resource init() throws ResourceInstantiationException {
    return this;
  }

  public void execute() throws ExecutionException {
    interrupted = false;
    // set the runtime parameters
    if(inputASName != null && inputASName.equals("")) inputASName = null;
    if(outputASName != null && outputASName.equals("")) outputASName = null;

    // get pointers to the annotation sets
    AnnotationSet inputAS =
      (inputASName == null) ? document.getAnnotations() : document
        .getAnnotations(inputASName);

    AnnotationSet outputAS =
      (outputASName == null) ? document.getAnnotations() : document
        .getAnnotations(outputASName);

    // copy the results to the output set if they are different
    if(inputAS != outputAS) {
      outputAS.addAll(inputAS.get(SENTENCE_ANNOTATION_TYPE));
    }

    // create one big sentence if none were found
    AnnotationSet sentences = outputAS.get(SENTENCE_ANNOTATION_TYPE);
    if(sentences == null || sentences.isEmpty()) {
      // create an annotation covering the entire content
      try {
        outputAS.add(new Long(0), document.getContent().size(),
          SENTENCE_ANNOTATION_TYPE, Factory.newFeatureMap());
      }
      catch(InvalidOffsetException ioe) {
        throw new GateRuntimeException(ioe);
      }
    }
  }// execute()

  /**
   * Notifies all the PRs in this controller that they should stop their
   * execution as soon as possible.
   */
  public synchronized void interrupt() {
    interrupted = true;
  }

  private String encoding;

  public void setEncoding(String newEncoding) {
    encoding = newEncoding;
  }

  public String getEncoding() {
    return encoding;
  }

  public void setInputASName(String newInputASName) {
    inputASName = newInputASName;
  }

  public String getInputASName() {
    return inputASName;
  }

  public void setOutputASName(String newOutputASName) {
    outputASName = newOutputASName;
  }

  public String getOutputASName() {
    return outputASName;
  }

  private String inputASName;

  private String outputASName;

}
