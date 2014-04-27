/*
 *  Copyright (c) 2008--2012, The University of Sheffield. See the file
 *  COPYRIGHT.txt in the software or at http://gate.ac.uk/gate/COPYRIGHT.txt
 *
 *  This file is part of GATE (see http://gate.ac.uk/), and is free
 *  software, licenced under the GNU Library General Public License,
 *  Version 2, June 1991 (in the distribution as file licence.html,
 *  and also available at http://gate.ac.uk/gate/licence.html).
 *
 *  $Id: AbstractTermbank.java 16337 2012-11-27 14:42:40Z adamfunk $
 */
package gate.termraider.bank;

import gate.creole.*;
import gate.creole.metadata.*;
import gate.gui.ActionsPublisher;
import gate.util.*;
import gate.*;
import java.io.*;
import java.util.*;

import gate.termraider.output.*;
import gate.termraider.util.*;
import gate.termraider.gui.*;
import javax.swing.Action;



public abstract class AbstractTermbank extends AbstractBank 
    implements ActionsPublisher  {
  private static final long serialVersionUID = -2809051430169834059L;
  
  // CREOLE init parameters
  protected boolean debugMode;
  protected String inputASName;
  protected Set<String> inputAnnotationTypes;

  // transient to allow serialization
  protected transient List<Action> actionsList;
  
  protected Map<Term, Set<String>>  termDocuments;
  protected Map<Term, Double>       termScores;
  protected Map<Term, Double>       rawTermScores;
  protected List<Term> termsByDescendingScore, termsByDescendingFrequency,
    termsByDescendingDocFrequency;
  protected Map<Term, Integer>      termFrequencies, docFrequencies;

  public static final String freqProperty = "frequency";


  public Resource init() throws ResourceInstantiationException {
    prepare();
    resetScores();
    processCorpora();
    scanTypesLanguagesDocFreq();
    calculateScores();
    return this;
  }
  

  public void cleanup() {
    super.cleanup();
  }
  
  
  public List<Term> getTermsByDescendingScore() {
    return this.termsByDescendingScore;
  }
  
  public List<Term> getTermsByDescendingFrequency() {
    return this.termsByDescendingFrequency;
  }
  
  public List<Term> getTermsByDescendingDocFrequency() {
    return this.termsByDescendingDocFrequency;
  }
  
  public Map<Term, Double> getTermScores() {
    return this.termScores;
  }
  
  public Map<Term, Set<String>> getTermDocuments() {
    return this.termDocuments;
  }
  
  
  public Set<String> getDocumentsForTerm(Term term) {
    if (this.termDocuments.containsKey(term)) {
      return this.termDocuments.get(term);
    }
    
    // implied else: empty set
    return new HashSet<String>();
  }
  
  
  public Map<Term, Integer> getTermFrequencies() {
    return this.termFrequencies;
  }
  
  public Map<Term, Integer> getDocFrequencies() {
    return this.docFrequencies;
  }
  
  public String getFreqProperty() {
    return freqProperty;
  }

  public Double getMinScore() {
    if (this.termScores.isEmpty()) {
      return 1.0;
    }
    // implied else
    return Collections.min(this.termScores.values());
  }
  
  public Double getMaxScore() {
    if (this.termScores.isEmpty()) {
      return 1.0;
    }
    // implied else
    return Collections.max(this.termScores.values());
  }
  
  
  protected void prepare() throws ResourceInstantiationException {
    if ( (corpora == null) || (corpora.size() == 0) ) {
      throw new ResourceInstantiationException("No corpora given");
    }
  }
  
  protected void createActions() {
    actionsList = new ArrayList<Action>();
    actionsList.add(new ActionSaveCsv("Save as CSV...", this));
  }
  
  
  protected void processCorpora() {
    for (Corpus corpus : corpora) {
      processCorpus(corpus);
      if (debugMode) {
        System.out.println("Termbank: added corpus " + corpus.getName() + " with " + corpus.size() + " documents");
      }
    }
  }
  
  
  protected void processCorpus(Corpus corpus) {
    for (int i=0 ; i < corpus.size() ; i++) {
      boolean wasLoaded = corpus.isDocumentLoaded(i);
      Document document = (Document) corpus.get(i);
      
      addData(document);

      // datastore safety
      if (! wasLoaded) {
        corpus.unloadDocument(document);
        Factory.deleteResource(document);
      }
    }
  }
  
  
  private void scanTypesLanguagesDocFreq() {
    this.types = new TreeSet<String>();
    this.languages = new TreeSet<String>();
    for (Term term : this.termFrequencies.keySet()) {
      this.languages.add(term.getLanguageCode());
      this.types.add(term.getType());
      this.docFrequencies.put(term, termDocuments.get(term).size());
    }
  }
  

  /* BEHOLD THE GUBBINS to distinguish the various types of Termbanks*/

  /**
   * This method needs to call incrementTermFreq(...)!
   */
  protected abstract void addData(Document document);
  
  protected abstract void calculateScores(); 
  
  protected abstract void resetScores();
  

  
  
  protected int incrementTermFreq(Term term, int increment) {
    return incrementMap(termFrequencies, term, increment);
  }
  
  
  protected int incrementMap(Map<Term, Integer> map, Term key, int increment) {
    int count = 0;
    if (map.containsKey(key)) {
      count = map.get(key).intValue();
    }
    count += increment;
    map.put(key, Integer.valueOf(count));
    return count;
  }
  
  
  public Double getScore(Term term) {
    if (termScores.containsKey(term)) {
      return termScores.get(term).doubleValue();
    }
    
    // error code
    return null;
  }

  
  public Double getRawScore(Term term) {
    if (rawTermScores.containsKey(term)) {
      return rawTermScores.get(term).doubleValue();
    }
    
    // error code
    return null;
  }

  
  
  /* Methods for saving as CSV */
  
  public void saveAsCsv(double threshold, File outputFile) throws GateException {
    CsvGenerator generator = new CsvGenerator();
    generator.generateAndSaveCsv(this, threshold, outputFile);
  }

  /**
   * Convenience method to save everything in the termbank.
   * @param outputFile
   * @throws GateException
   */
  public void saveAsCsv(File outputFile) throws GateException {
    double threshold = this.getMinScore();
    CsvGenerator generator = new CsvGenerator();
    generator.generateAndSaveCsv(this, threshold, outputFile);
  }
  
  
  @Override
  public List<Action> getActions() {
    // lazy instantiation because actionsList is transient
    if (actionsList == null) {
      createActions();
    }
    
    return this.actionsList;
  }

  
  public int getTermFrequency(Term term) {
    if (termFrequencies.containsKey(term)) {
      return termFrequencies.get(term);
    }
    // implied else
    return 0;
  }
  
  
  /***** CREOLE PARAMETERS *****/

  @CreoleParameter(comment = "input AS name",
          defaultValue = "")
  public void setInputASName(String name) {
    this.inputASName = name;
  }
  public String getInputASName() {
    return this.inputASName;
  }
  
  
  @CreoleParameter(comment = "input annotation types",
          defaultValue = "SingleWord;MultiWord")
  public void setInputAnnotationTypes(Set<String> names) {
    this.inputAnnotationTypes = names;
  }
  
  public Set<String> getInputAnnotationTypes() {
    return this.inputAnnotationTypes;
  }
  
  
  @CreoleParameter(comment = "print debugging information during initialization",
          defaultValue = "false")
  public void setDebugMode(Boolean debug) {
    this.debugMode = debug;
  }

  public Boolean getDebugMode() {
    return this.debugMode;
  }
  

}
