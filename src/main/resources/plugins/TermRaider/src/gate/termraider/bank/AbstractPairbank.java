/*
 *  Copyright (c) 2008--2012, The University of Sheffield. See the file
 *  COPYRIGHT.txt in the software or at http://gate.ac.uk/gate/COPYRIGHT.txt
 *
 *  This file is part of GATE (see http://gate.ac.uk/), and is free
 *  software, licenced under the GNU Library General Public License,
 *  Version 2, June 1991 (in the distribution as file licence.html,
 *  and also available at http://gate.ac.uk/gate/licence.html).
 *
 *  $Id: AbstractPairbank.java 16336 2012-11-27 14:01:14Z adamfunk $
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
import gate.termraider.gui.*;
import gate.termraider.util.*;
import javax.swing.Action;



public abstract class AbstractPairbank extends AbstractBank
    implements ActionsPublisher {

  private static final long serialVersionUID = 424942970862740181L;

  // CREOLE init parameters
  protected boolean debugMode;
  protected String inputASName;
  protected String namespaceBase;

  protected transient List<Action> actionsList;
  
  protected Map<UnorderedTermPair, Double> scores;
  protected Map<UnorderedTermPair, Set<String>> documents;
  protected Map<UnorderedTermPair, Integer> pairCount;



  public Resource init() throws ResourceInstantiationException {
    prepare();
    resetScores();
    processCorpora();
    calculateScores();
    return this;
  }
  

  public void cleanup() {
    super.cleanup();
  }

  public Set<UnorderedTermPair> getPairs() {
    return this.pairCount.keySet();
  }
  
  public Map<UnorderedTermPair, Double> getScores() {
    return this.scores;
  }
  
  public Map<UnorderedTermPair, Set<String>> getDocuments() {
    return this.documents;
  }
  
  public int getDocumentCount(UnorderedTermPair pair) {
    if (this.documents.containsKey(pair)) {
      return this.documents.get(pair).size();
    }
    
    return 0;
  }
  
  public int getPairCount(UnorderedTermPair pair) {
    if (this.pairCount.containsKey(pair)) {
      return this.pairCount.get(pair);
    }
    // implied else
    return 0;
  }
  
  
  public Double getMinScore() {
    if (this.scores.isEmpty()) {
      return 0.0;
    }
    // implied else
    return Collections.min(this.scores.values());
  }
  
  public Double getMaxScore() {
    if (this.scores.isEmpty()) {
      return 0.0;
    }
    // implied else
    return Collections.max(this.scores.values());
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
    scanTypesAndLanguages();
  }
  
  
  private void scanTypesAndLanguages() {
    this.types = new TreeSet<String>();
    this.languages = new TreeSet<String>();
    for (UnorderedTermPair pair : this.pairCount.keySet()) {
      this.languages.add(pair.getTerm0().getLanguageCode());
      this.languages.add(pair.getTerm1().getLanguageCode());
      this.types.add(pair.getTerm0().getType());
      this.types.add(pair.getTerm1().getType());
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
  
  
  protected void resetScores() {
    this.documents = new HashMap<UnorderedTermPair, Set<String>>();
    this.scores = new HashMap<UnorderedTermPair, Double>();
    this.pairCount = new HashMap<UnorderedTermPair, Integer>();
    resetImplScores();
  }


  /* BEHOLD THE GUBBINS to distinguish the various (potential) types of Pairbanks*/

  protected abstract void addData(Document document);
  
  protected abstract void calculateScores(); 
  
  protected abstract void resetImplScores();

  
  
  public Double getScore(UnorderedTermPair pair) {
    if (scores.containsKey(pair)) {
      return scores.get(pair);
    }
    
    // error code
    return null;
  }
  
  
  
  
  /* Methods for saving as CSV */
  
  public void saveAsCsv(double threshold, File outputFile) throws GateException {
    PairCsvGenerator generator = new PairCsvGenerator();
    generator.generateAndSaveCsv(this, threshold, outputFile);
  }

  /**
   * Convenience method to save everything in the termbank.
   * @param outputFile
   * @throws GateException
   */
  public void saveAsCsv(File outputFile) throws GateException {
    PairCsvGenerator generator = new PairCsvGenerator();
    generator.generateAndSaveCsv(this, -100.0F, outputFile);
  }
  
  
  public void saveAsRdfAndDeleteOntology(double threshold, File outputFile) throws GateException {
    System.out.println("Saving as RDF not implemented yet for Pairbanks");
  }

  public void saveAsRdfAndDeleteOntology(File outputFile) throws GateException {
    saveAsRdfAndDeleteOntology(0.0, outputFile);
  }

  public void writeRdfAndDeleteOntology(double threshold, OutputStream stream) throws GateException {
    System.out.println("Saving as RDF not implemented yet for Pairbanks");
  }


  
  @Override
  public List<Action> getActions() {
    // lazy instantation because it's transient
    if (this.actionsList == null) {
      createActions();
    }
    
    return this.actionsList;
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
  
  
  @CreoleParameter(comment = "print debugging information during initialization",
          defaultValue = "false")
  public void setDebugMode(Boolean debug) {
    this.debugMode = debug;
  }

  public Boolean getDebugMode() {
    return this.debugMode;
  }

  /* Set this in the application for consistency between termbanks;
   * otherwise it will be randomly generated. */
  @CreoleParameter(comment = "Namespace base (including '#') for ontology generation",
          defaultValue = "")
  public void setNamespaceBase(String nsb) {
    this.namespaceBase = nsb;
  }

  public String getNamespaceBase() {
    return this.namespaceBase;
  }

}



