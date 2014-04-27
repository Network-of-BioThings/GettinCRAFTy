/*
 * FingerprintGenerator
 * 
 * Copyright (c) 2011, The University of Sheffield.
 * 
 * This file is part of GATE (see http://gate.ac.uk/), and is free software,
 * licenced under the GNU Library General Public License, Version 3, June 2007
 * (in the distribution as file licence.html, and also available at
 * http://gate.ac.uk/gate/licence.html).
 * 
 * Mark A. Greenwood, 28/05/2011
 */
package org.knallgrau.utils.textcat;

import gate.Annotation;
import gate.AnnotationSet;
import gate.Controller;
import gate.Utils;
import gate.creole.AbstractLanguageAnalyser;
import gate.creole.ControllerAwarePR;
import gate.creole.ExecutionException;
import gate.creole.metadata.CreoleParameter;
import gate.creole.metadata.CreoleResource;
import gate.creole.metadata.Optional;
import gate.creole.metadata.RunTime;
import gate.util.Files;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;

import at.knallgrau.textcat.FingerPrint;

@CreoleResource(name = "TextCat Fingerprint Generator", comment = "Generate language fingerprints for use with the TextCat Language Indentification PR", icon = "fingerprint.png", helpURL="http://gate.ac.uk/userguide/sec:misc-creole:language-identification:fingerprints")
public class FingerprintGenerator extends AbstractLanguageAnalyser implements
                                                                  ControllerAwarePR {

  private URL fingerprintURL;

  private StringBuilder text;

  private File fingerprintFile;

  private String annotationType;

  private String annotationSetName;

  @RunTime
  @CreoleParameter(comment = "The file in which the generated fingerprint should be saved")
  public void setFingerprintURL(URL fingerprintURL) {
    this.fingerprintURL = fingerprintURL;
  }

  public URL getFingerprintURL() {
    return fingerprintURL;
  }
  
  @RunTime
  @Optional
  @CreoleParameter(comment = "The annotation type covering the text to use to build the fingerprint, if unspecifed the whole document will be used")
  public void setAnnotationType(String atype) {
    this.annotationType = atype;
  }

  public String getAnnotationType() {
    return this.annotationType;
  }

  @RunTime
  @Optional
  @CreoleParameter(comment = "The annotation set used for input/output (ignored when using the whole document)")
  public void setAnnotationSetName(String inputASName) {
    this.annotationSetName = inputASName;
  }

  public String getAnnotationSetName() {
    return annotationSetName;
  }

  @Override
  public void execute() {

    if(annotationType == null || annotationType.trim().equals("")) {
      // no annotation specified so use the whole document
      text.append(document.getContent().toString()).append("\n\n\n");
    } else {
      AnnotationSet annotations =
              document.getAnnotations(annotationSetName).get(annotationType);
      for(Annotation annotation : annotations) {
        // add the text from each annotation of the specified type
        text.append(Utils.stringFor(document, annotation)).append("\n\n\n");
      }
    }
  }

  public void controllerExecutionStarted(Controller c)
          throws ExecutionException {
    // check that the URL of the fingerprint we want to generate is a file://

    try {
      fingerprintFile = Files.fileFromURL(fingerprintURL);
    } catch(Exception e) {
      throw new ExecutionException(
              "Location of fingerprint must be a file based URL!", e);
    }

    // create a new place holder for the text we are going to process
    text = new StringBuilder();
  }

  public void controllerExecutionFinished(Controller c)
          throws ExecutionException {
    // save the fingerprint and...

    FingerPrint fp = new FingerPrint();
    fp.create(text.toString());

    try {
      FileOutputStream fos = new FileOutputStream(fingerprintFile);
      fos.write(fp.toString().getBytes("utf8"));
      fos.close();
    } catch(IOException e) {
      throw new ExecutionException("Unable to save fingerprint file", e);
    }

    // ...release any memory we have consumed
    text = null;
    fingerprintFile = null;
  }

  public void controllerExecutionAborted(Controller c, Throwable t)
          throws ExecutionException {
    // release any memory we have used
    fingerprintFile = null;
    text = null;
  }

}
