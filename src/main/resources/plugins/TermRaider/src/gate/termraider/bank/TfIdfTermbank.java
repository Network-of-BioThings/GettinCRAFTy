/*
 *  Copyright (c) 2008-2012, The University of Sheffield. See the file
 *  COPYRIGHT.txt in the software or at http://gate.ac.uk/gate/COPYRIGHT.txt
 *
 *  This file is part of GATE (see http://gate.ac.uk/), and is free
 *  software, licenced under the GNU Library General Public License,
 *  Version 2, June 1991 (in the distribution as file licence.html,
 *  and also available at http://gate.ac.uk/gate/licence.html).
 *
 *  $Id: TfIdfTermbank.java 16296 2012-11-20 12:17:11Z adamfunk $
 */
package gate.termraider.bank;

import gate.creole.metadata.*;
import gate.gui.ActionsPublisher;
import gate.*;
import gate.termraider.bank.modes.IdfCalculation;
import gate.termraider.bank.modes.TfCalculation;
import gate.termraider.util.*;
import java.util.*;



@CreoleResource(name = "TfIdfTermbank",
        icon = "termbank-lr.png",
        comment = "TermRaider Termbank derived from vectors in document features")

public class TfIdfTermbank extends AbstractTermbank
    implements ActionsPublisher  {

  private static final long serialVersionUID = -8275690376233755995L;
  
  /* EXTRA CREOLE PARAMETERS */
  private TfCalculation tfCalculation;
  private IdfCalculation idfCalculation;
  
  /* EXTRA DATA */
  private int documentCount;
  
  
  protected void addData(Document document) {
    documentCount++;
    String documentSource = Utilities.sourceOrName(document);
    AnnotationSet candidates = document.getAnnotations(inputASName).get(inputAnnotationTypes);

    for (Annotation candidate : candidates) {
      Term term = makeTerm(candidate, document);
      incrementTermFreq(term, 1);
      
      if (termDocuments.containsKey(term)) {
        termDocuments.get(term).add(documentSource);
      }
      else {
        Set<String> docNames = new HashSet<String>();
        docNames.add(documentSource);
        termDocuments.put(term, docNames);
      }
    }
  }

  
  protected void calculateScores() {
    for (Term term : termFrequencies.keySet()) {
      int tf = termFrequencies.get(term);
      int df = termDocuments.get(term).size();
      double score = TfCalculation.calculate(tfCalculation, tf) * IdfCalculation.calculate(idfCalculation, df, documentCount);
      rawTermScores.put(term, Double.valueOf(score));
      termScores.put(term, Utilities.normalizeScore(score));
    }
    
    termsByDescendingScore = new ArrayList<Term>(termScores.keySet());
    Collections.sort(termsByDescendingScore, new TermComparatorByDescendingScore(termScores));
    
    if (debugMode) {
      System.out.println("Termbank: nbr of terms = " + termsByDescendingScore.size());
    }
  }
  
  
  protected void resetScores() {
    termDocuments    = new HashMap<Term, Set<String>>();
    termScores       = new HashMap<Term, Double>();
    rawTermScores    = new HashMap<Term, Double>();
    termsByDescendingScore      = new ArrayList<Term>();
    termsByDescendingFrequency = new ArrayList<Term>();
    termsByDescendingDocFrequency = new ArrayList<Term>();
    termFrequencies = new HashMap<Term, Integer>();
    docFrequencies = new HashMap<Term, Integer>();
    documentCount = 0;
  }


  
  /***** CREOLE PARAMETERS *****/

  @CreoleParameter(comment = "term frequency calculation",
          defaultValue = "Logarithmic")
  public void setTfCalculation(TfCalculation mode) {
    this.tfCalculation = mode;
  }
  
  public TfCalculation getTfCalculation() {
    return this.tfCalculation;
  }
          

          
  @CreoleParameter(comment = "inverted document frequency calculation",
          defaultValue = "Logarithmic")
  public void setIdfCalculation(IdfCalculation mode) {
    this.idfCalculation = mode;
  }
  
  public IdfCalculation getIdfCalculation() {
    return this.idfCalculation;
  }

  
  /* override default value from AbstractTermbank   */
  @CreoleParameter(defaultValue = "tfIdf")
  public void setScoreProperty(String name) {
    super.setScoreProperty(name);
  }

}
