/*
 *  Copyright (c) 2012, The University of Sheffield. See the file
 *  COPYRIGHT.txt in the software or at http://gate.ac.uk/gate/COPYRIGHT.txt
 *
 *  This file is part of GATE (see http://gate.ac.uk/), and is free
 *  software, licenced under the GNU Library General Public License,
 *  Version 2, June 1991 (in the distribution as file licence.html,
 *  and also available at http://gate.ac.uk/gate/licence.html).
 *
 *  $Id: TermScoreCopier.java 16320 2012-11-23 13:56:00Z adamfunk $
 */
package gate.termraider.apply;

import gate.*;
import gate.creole.AbstractLanguageAnalyser;
import gate.creole.ExecutionException;
import gate.creole.ExecutionInterruptedException;
import gate.creole.ResourceInstantiationException;
import gate.creole.metadata.CreoleParameter;
import gate.creole.metadata.CreoleResource;
import gate.creole.metadata.RunTime;
import gate.termraider.bank.AbstractTermbank;
import gate.termraider.util.*;

import java.text.NumberFormat;
import java.util.*;


@CreoleResource(name = "Termbank Score Copier",
    icon = "termbank-lr.png",
    comment = "Copy scores from Termbanks back to their source annotations")
public class TermScoreCopier extends AbstractLanguageAnalyser
  implements  ProcessingResource {

  private static final long serialVersionUID = -8766478645866790985L;
  
  
  /* CREOLE PARAMETERS */
  private AbstractTermbank termbank;
  private String annotationSetName;
  
  
  public Resource init() throws ResourceInstantiationException {
    return super.init();
  }
  
  
  public void reInit() throws ResourceInstantiationException {
    this.init();
  }
  
  
  public void execute() throws ExecutionException {
    interrupted = false;
    long startTime = System.currentTimeMillis();
    if(document == null) { throw new ExecutionException(
        "No document to process!"); }
    fireStatusChanged("Running " + this.getName() + " on " + document.getName());
    fireProgressChanged(0);

    if (this.termbank == null) {
      throw new ExecutionException("Termbank must be set!");
    }
    
    Set<String> annotationTypes = termbank.getInputAnnotationTypes();
    String termFeature = termbank.getInputAnnotationFeature();
    String languageFeature = termbank.getLanguageFeature();
    String scoreFeature = termbank.getScoreProperty();
    String rawScoreFeature = scoreFeature + ".raw";
    
    AnnotationSet candidates = document.getAnnotations(annotationSetName).get(annotationTypes);
    checkInterruption();
    
    for (Annotation candidate : candidates) {
      Term term = new Term(candidate, document, languageFeature, termFeature);
      FeatureMap fm = candidate.getFeatures();
      Double score = termbank.getScore(term);
      if (score != null) {
        fm.put(scoreFeature, score);
      }
      
      Double rawScore = termbank.getRawScore(term);
      if (rawScore != null) {
        fm.put(rawScoreFeature, rawScore);
      }
      checkInterruption();
    } // end for candidates loop
    
    fireProcessFinished();
    fireStatusChanged("Finished " + this.getName() + " on "
        + document.getName() + " in "
        + NumberFormat.getInstance().format(
            (double)(System.currentTimeMillis() - startTime) / 1000)
        + " seconds!");
  } // end execute()
  
  
  private void checkInterruption() throws ExecutionInterruptedException {
    if(isInterrupted()) { throw new ExecutionInterruptedException(
        "Execution of " + this.getName() + " has been abruptly interrupted!"); }
  }

  
  /* CREOLE METHODS */
  
  @RunTime
  @CreoleParameter(comment = "Termbank (source of annotation type and scores)")
  public void setTermbank(AbstractTermbank termbank) {
    this.termbank = termbank;
  }
  
  public AbstractTermbank getTermbank() {
    return this.termbank;
  }
  
  @RunTime
  @CreoleParameter(comment = "AnnotationSet name",
          defaultValue = "")
  public void setAnnotationSetName(String name) {
    this.annotationSetName = name;
  }
  
  public String getAnnotationSetName() {
    return this.annotationSetName;
  }
  
}
