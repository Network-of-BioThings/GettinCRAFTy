/*
 * LanguageIdentifier
 * 
 * Copyright (c) 1995-2011, The University of Sheffield.
 * 
 * This file is part of GATE (see http://gate.ac.uk/), and is free software,
 * licenced under the GNU Library General Public License, Version 3, June 2007
 * (in the distribution as file licence.html, and also available at
 * http://gate.ac.uk/gate/licence.html).
 * 
 * $Id: LanguageIdentifier.java 15876 2012-06-06 14:44:42Z markagreenwood $
 */
package org.knallgrau.utils.textcat;

import gate.Annotation;
import gate.AnnotationSet;
import gate.Utils;
import gate.creole.ExecutionException;
import gate.creole.ResourceInstantiationException;
import gate.creole.metadata.CreoleParameter;
import gate.creole.metadata.CreoleResource;
import gate.creole.metadata.Optional;
import gate.creole.metadata.RunTime;
import gate.util.Files;

import java.net.URL;

import at.knallgrau.textcat.TextCategorizer;

@CreoleResource(name = "TextCat Language Identification", comment = "Recognizes the document language using TextCat", icon = "paw-print.png", helpURL = "http://gate.ac.uk/userguide/sec:misc-creole:language-identification")
public class LanguageIdentifier extends gate.creole.AbstractLanguageAnalyser {

  private static final long serialVersionUID = 5831213212185693826L;

  private TextCategorizer guesser;

  private String languageFeatureName;

  private String annotationType;

  private String annotationSetName;

  private URL configURL;

  public LanguageIdentifier init() throws ResourceInstantiationException {
    try {
        guesser =
                new TextCategorizer(configURL);
    } catch(Exception e) {
      throw new ResourceInstantiationException(
              "unable to load TextCat config file", e);
    }

    return this;
  }

  /**
   * Based on the document content, recognizes the language and adds a document
   * feature.
   */
  public void execute() throws ExecutionException {
    if(document == null || document.getFeatures() == null) return;

    if(annotationType == null || annotationType.trim().equals("")) {
      /*
       * Default situation: classify the whole document and save the result as a
       * document feature.
       */
      String text = document.getContent().toString();
      String category = guesser.categorize(text);
      document.getFeatures().put(languageFeatureName, category);
    }

    else {
      /*
       * New option: classify the text underlying each annotation (specified by
       * AS and type) and save the result as an annotation feature.
       */
      AnnotationSet annotations =
              document.getAnnotations(annotationSetName).get(annotationType);
      for(Annotation annotation : annotations) {
        String text = Utils.stringFor(document, annotation);
        String category = guesser.categorize(text);
        annotation.getFeatures().put(languageFeatureName, category);
      }
    }

  }

  public void reInit() throws ResourceInstantiationException {
    init();
  }

  @RunTime
  @Optional
  @CreoleParameter(comment = "name of document or annotation features for the language identified", defaultValue = "lang")
  public void setLanguageFeatureName(String languageFeatureName) {
    this.languageFeatureName = languageFeatureName;
  }

  public String getLanguageFeatureName() {
    return languageFeatureName;
  }

  @RunTime
  @Optional
  @CreoleParameter(comment = "type of annotations to classify; leave blank for whole-document classification")
  public void setAnnotationType(String atype) {
    this.annotationType = atype;
  }

  public String getAnnotationType() {
    return this.annotationType;
  }

  @RunTime
  @Optional
  @CreoleParameter(comment = " annotation set used for input/output (ignored for whole-document classification)")
  public void setAnnotationSetName(String inputASName) {
    this.annotationSetName = inputASName;
  }

  public String getAnnotationSetName() {
    return annotationSetName;
  }

  @CreoleParameter(defaultValue = "resources/default-names.conf")
  public void setConfigURL(URL configURL) {
    this.configURL = configURL;
  }

  public URL getConfigURL() {
    return configURL;
  }
}
