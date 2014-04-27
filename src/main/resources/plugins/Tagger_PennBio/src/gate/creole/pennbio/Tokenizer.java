/*
 * Tokenizer
 * 
 * Copyright (c) 2011, The University of Sheffield.
 * 
 * This file is part of GATE (see http://gate.ac.uk/), and is free software,
 * licenced under the GNU Library General Public License, Version 3, June 2007
 * (in the distribution as file licence.html, and also available at
 * http://gate.ac.uk/gate/licence.html).
 * 
 * Mark A. Greenwood, 02/06/2011
 */
package gate.creole.pennbio;

import edu.upenn.cis.tokenizers.BioTokenizer;
import gate.AnnotationSet;
import gate.Factory;
import gate.FeatureMap;
import gate.Resource;
import gate.creole.ANNIEConstants;
import gate.creole.AbstractLanguageAnalyser;
import gate.creole.ExecutionException;
import gate.creole.ResourceInstantiationException;
import gate.creole.metadata.CreoleParameter;
import gate.creole.metadata.CreoleResource;
import gate.creole.metadata.Optional;
import gate.creole.metadata.RunTime;

import java.net.URL;

import opennlp.tools.util.Span;

@CreoleResource(name = "Penn BioTokenizer", icon = "tokeniser.png", helpURL = "http://gate.ac.uk/userguide/sec:domain-creole:biomed:pennbio")
public class Tokenizer extends AbstractLanguageAnalyser {

  protected URL tokenizerURL;

  private BioTokenizer tokenizer = null;

  private String annotationSetName;

  public String getAnnotationSetName() {
    return annotationSetName;
  }

  @RunTime
  @Optional
  @CreoleParameter(comment = "the annotation set in which Token annotations will be created")
  public void setAnnotationSetName(String annotationSetName) {
    this.annotationSetName = annotationSetName;
  }

  public URL getTokenizerURL() {
    return tokenizerURL;
  }

  @CreoleParameter(defaultValue = "resources/BioTok.bin.gz")
  public void setTokenizerURL(URL tokenizerURL) {
    this.tokenizerURL = tokenizerURL;
  }

  public Resource init() throws ResourceInstantiationException {
    try {
      tokenizer = new BioTokenizer(tokenizerURL);
    } catch(Exception e) {
      throw new ResourceInstantiationException("Unable to initialize tagger", e);
    }

    return this;
  }

  public void execute() throws ExecutionException {
    AnnotationSet outputAS = document.getAnnotations(annotationSetName);

    String text = document.getContent().toString();

    Span[] tokens = tokenizer.getTokens(text);
    try {
      for(Span token : tokens) {
        FeatureMap features = Factory.newFeatureMap();
        features.put(ANNIEConstants.TOKEN_STRING_FEATURE_NAME,
                text.substring(token.getStart(), token.getEnd()));

        outputAS.add((long)token.getStart(), (long)token.getEnd(),
                ANNIEConstants.TOKEN_ANNOTATION_TYPE, features);
      }
    } catch(Exception e) {
      throw new ExecutionException("error running tokenizer", e);
    }
  }
}
