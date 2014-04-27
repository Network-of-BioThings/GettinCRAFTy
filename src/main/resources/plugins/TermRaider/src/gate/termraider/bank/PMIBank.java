/*
 *  Copyright (c) 2008--2012, The University of Sheffield. See the file
 *  COPYRIGHT.txt in the software or at http://gate.ac.uk/gate/COPYRIGHT.txt
 *
 *  This file is part of GATE (see http://gate.ac.uk/), and is free
 *  software, licenced under the GNU Library General Public License,
 *  Version 2, June 1991 (in the distribution as file licence.html,
 *  and also available at http://gate.ac.uk/gate/licence.html).
 *
 *  $Id: PMIBank.java 16296 2012-11-20 12:17:11Z adamfunk $
 */
package gate.termraider.bank;

import gate.creole.metadata.*;
import gate.gui.ActionsPublisher;
import gate.*;
import gate.termraider.util.*;
import java.util.*;



@CreoleResource(name = "PMI Bank",
        icon = "termbank-lr.png",
        comment = "Pointwise Mutual Information from corpora")

public class PMIBank extends AbstractPairbank
    implements ActionsPublisher  {

  private static final long serialVersionUID = 1275893998609017476L;

  /* EXTRA CREOLE PARAMETERS */
  protected String outerAnnotationType;
  protected Set<String> innerAnnotationTypes;
  protected int outerAnnotationWindow;
  protected boolean requireTypeDifference;
  protected boolean allowOverlaps;

  
  private int totalCount, totalPairCount;
  private Set<Set<Integer>> seenCombinations;
  private Set<Integer> seen;
  private Map<Term, Set<String>> termDocuments;
  protected Map<Term, Integer> termCount;

  
  
  protected void addData(Document document) {
    String documentSource = Utilities.sourceOrName(document);
    /** Collocations that have already been processed in this document
     * (each collocation is a pair of IDs for a Token annotation), to avoid counting
     * them again.     */
    seenCombinations = new HashSet<Set<Integer>>();
    
    /** IDs of annotations (Tokens) that have already been processed in this document, to avoid
     * counting them again.     */
    seen = new HashSet<Integer>();
    
    AnnotationSet inputAS = document.getAnnotations(inputASName);
    AnnotationSet innerAnnotations = inputAS.get(innerAnnotationTypes);
    List<Annotation> sentences = gate.Utils.inDocumentOrder(inputAS.get(outerAnnotationType));
    
    if (sentences.isEmpty()) {
      return;
    }
    
    if (sentences.size() <= outerAnnotationWindow) {
      processWindow(sentences, innerAnnotations, document, documentSource);
      return;
    }
    
    List<Annotation> window = new ArrayList<Annotation>();
    while (window.size() < outerAnnotationWindow) {
      window.add(sentences.remove(0));
    }
    
    while (! sentences.isEmpty()) {
      processWindow(window, innerAnnotations, document, documentSource);
      window.remove(0);
      window.add(sentences.remove(0));
    }
    processWindow(window, innerAnnotations, document, documentSource);
  }
  
  
  /** Scan a window of sentences and process each pair of terms in the window.
   * Skip any pair of instances that has already been processed (e.g., both terms are in
   * the same sentence and were processed already when that sentence was added
   * to the end of the window.  
   * @param window
   * @param inners (already restricted by types)
   * @param document
   * @param source
   */
  private void processWindow(List<Annotation> window, AnnotationSet inners, Document document, String source) {
    Long start = window.get(0).getStartNode().getOffset();
    Long end = window.get(window.size() - 1).getEndNode().getOffset();
    List<Annotation> terms = gate.Utils.inDocumentOrder(inners.getContained(start, end));
    for (int i = 0 ; i < (terms.size() - 1) ; i++)  {
      Annotation termI = terms.get(i);
      for (int j = i+1 ; j < terms.size() ; j++)  {
        Annotation termJ = terms.get(j);
        // compatibleTerms checks for overlaps too
        if ( combinationUnseen(termI, termJ) && compatibleTerms(termI, termJ) ) {
          processTerms(termI, termJ, document, source);
        }
      }
    }
  }

  
  /**
   * For a pair of annotations (terms), increment the term count if this instance (annotation)
   * of the term hasn't been counted already.  This method is called only if this
   * pair-instance hasn't been counted already.
   * @param ann0
   * @param ann1
   * @param document
   * @param source
   */
  private void processTerms(Annotation ann0, Annotation ann1, Document document, String source) {
    Term term0 = makeTerm(ann0, document);
    Term term1 = makeTerm(ann1, document);
    UnorderedTermPair pair = new UnorderedTermPair(term0, term1);
    
    if (unseen(ann0)) {
      incrementTermCount(term0, source);
      totalCount++;
    }
    
    if (unseen(ann1)) {
      incrementTermCount(term1, source);
      totalCount++;
    }
    
    incrementPairCount(pair, source);
    totalPairCount++;
  }
  
  
  private void incrementTermCount(Term term, String source) {
    int count;
    Set<String> sources;
    if (termCount.containsKey(term)) {
      count = termCount.get(term);
      sources = termDocuments.get(term);
    }
    else {
      count = 0;
      sources = new HashSet<String>();
    }
    count++;
    sources.add(source);
    termCount.put(term, count);
    termDocuments.put(term, sources);
  }

  
  private void incrementPairCount(UnorderedTermPair pair, String source) {
    int count;
    Set<String> sources;
    if (pairCount.containsKey(pair)) {
      count = pairCount.get(pair);
      sources = documents.get(pair);
    }
    else {
      count = 0;
      sources = new HashSet<String>();
    }
    count++;
    sources.add(source);
    pairCount.put(pair, count);
    documents.put(pair, sources);
  }
  
  
  private boolean combinationUnseen(Annotation a0, Annotation a1) {
    Set<Integer> combo = new HashSet<Integer>();
    combo.add(a0.getId());
    combo.add(a1.getId());
    if (seenCombinations.contains(combo)) {
      return false;
    }
    // implied else
    seenCombinations.add(combo);
    return true;
  }
  
  
  /** Check whether two annotations are compatible as collocations,
   * depending on requireTypeDifference and their types
   * and on allowOverlaps and their spans.
   * @param a0
   * @param a1
   * @return
   */
  
  private boolean compatibleTerms(Annotation a0, Annotation a1) {
    if (this.requireTypeDifference && a0.getType().equals(a1.getType())) {
      return false;
    }
    
    // implied else: types are compatible
    if (this.allowOverlaps) {
      return true;
    }

    // implied else: types are compatible but must check overlaps
    return ! a0.overlaps(a1);
  }
  
  
  
  private boolean unseen(Annotation anno) {
    if (seen.contains(anno.getId())) {
      return false;
    }
    // implied else
    seen.add(anno.getId());
    return true;
  }
  
  
  public int getTotalCount() {
    return this.totalCount;
  }
  
  public int getTotalPairCount() {
    return this.totalPairCount;
  }
  
  public int getNbrDistinctTerms() {
    return this.termCount.size();
  }
  
  public int getNbrDistinctPairs() {
    return this.pairCount.size();
  }
  
  public Set<Term> getTerms() {
    return this.termCount.keySet();
  }
  
  public int getDocumentCount(Term term) {
    if (this.termDocuments.containsKey(term)) {
      return this.termDocuments.get(term).size();
    }
    
    return 0;
  }

  public int getTermCount(Term term) {
    if (this.termCount.containsKey(term)) {
      return this.termCount.get(term);
    }
    // implied else
    return 0;
  }
  


  
  
  protected void resetImplScores() {
    termCount = new HashMap<Term, Integer>();
    termDocuments = new HashMap<Term, Set<String>>();
    totalCount = 0;
    totalPairCount = 0;
  }



  public void calculateScores() {
    double totalCountF = (double) totalCount;
    double totalPairCountF = (double) totalPairCount;
    Map<Term, Double> termProb = new HashMap<Term, Double>();
    for (Term term : termCount.keySet()) {
      double prob = ((double) termCount.get(term)) / totalCountF;
      termProb.put(term, prob);
    }
    
    for (UnorderedTermPair pair : this.pairCount.keySet()) {
      double px = termProb.get(pair.getTerm0());
      double py = termProb.get(pair.getTerm1());
      double pxy = ((double) pairCount.get(pair)) / totalPairCountF;
      
      /*  Notes
       * 
       * PMI not normalized (source?):
       * 
       * pmi(x,y) = log_2 ( P(x,y) / (P(x) * P(y)) )
       * 
       * normalized: -1 = never; 0 = independent; +1 = always coöccurring
       * 
       * npmi(x,y) = pmi(x,y) / (- log(P(x,y)))
       * 
       * npmi(x,y) = log(P(x)P(y)) / log(P(x,y)) - 1 
       * 
       */
      
      double npmi = Utilities.log2(px * py) / Utilities.log2(pxy) - 1;
      this.scores.put(pair, npmi * 100.0);
    }

    if (debugMode) {
      System.out.println("Pairbank: nbr of terms = " + termCount.keySet().size());
      System.out.println("Pairbank: nbr of pairs = " + pairCount.keySet().size());
    }
  }

  
  /***** CREOLE PARAMETERS *****/

  @CreoleParameter(comment = "annotation types to evaluate as terms",
          defaultValue = "Entity")
  public void setInnerAnnotationTypes(Set<String> types) {
    this.innerAnnotationTypes = types;
  }
  
  public Set<String> getInnerAnnotationTypes() {
    return this.innerAnnotationTypes;
  }


  @CreoleParameter(comment = "annotation type for scanning window",
          defaultValue = "Sentence")
  public void setOuterAnnotationType(String type) {
    this.outerAnnotationType = type;
  }
  
  public String getOuterAnnotationType() {
    return this.outerAnnotationType;
  }

  
  @CreoleParameter(comment = "window size in outer annotations",
          defaultValue = "2")
  public void setOuterAnnotationWindow(Integer w) {
    this.outerAnnotationWindow = w;
  }
  
  public Integer getOuterAnnotationWindow() {
    return this.outerAnnotationWindow;
  }

  
  @CreoleParameter(comment = "require each collocation pair to consist of different types",
          defaultValue = "false")
  public void setRequireTypeDifference(Boolean rtd) {
    this.requireTypeDifference = rtd;
  }
  
  public Boolean getRequireTypeDifference() {
    return this.requireTypeDifference;
  }

  
  @CreoleParameter(comment = "allow a collocation pair to consist of overlapping annotations",
          defaultValue = "false")
  public void setAllowOverlapCollocations(Boolean aoc) {
    this.allowOverlaps = aoc;
  }
  
  public Boolean getAllowOverlapCollocations() {
    return this.allowOverlaps;
  }
  
  
  /* override default value from AbstractPairbank   */
  @CreoleParameter(defaultValue = "pmiScore")
  public void setScoreProperty(String name) {
    super.setScoreProperty(name);
  }


}
