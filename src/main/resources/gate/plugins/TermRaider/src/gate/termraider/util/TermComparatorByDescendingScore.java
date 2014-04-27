/*
 *  Copyright (c) 2010--2012, The University of Sheffield. See the file
 *  COPYRIGHT.txt in the software or at http://gate.ac.uk/gate/COPYRIGHT.txt
 *
 *  This file is part of GATE (see http://gate.ac.uk/), and is free
 *  software, licenced under the GNU Library General Public License,
 *  Version 2, June 1991 (in the distribution as file licence.html,
 *  and also available at http://gate.ac.uk/gate/licence.html).
 *
 *  $Id: TermComparatorByDescendingScore.java 16296 2012-11-20 12:17:11Z adamfunk $
 */
package gate.termraider.util;

import java.util.*;

/**
 * Comparator used to sort terms in descending order by score.
 */
public class TermComparatorByDescendingScore implements Comparator<Term> {

  private Map<Term, ? extends Number> termScores;
  
  public TermComparatorByDescendingScore(Map<Term, ? extends Number> termScores) {
    this.termScores = termScores;
  }
  
  public int compare(Term t0, Term t1) {
    double diff = termScores.get(t0).doubleValue() - termScores.get(t1).doubleValue();
    
    if (diff > 0) {
      return -1;
    }
    
    // implied else
    if (diff < 0) {
      return 1;
    }
    
    return 0;
  }
  
}

