/*
 *  Copyright (c) 2010--2012, The University of Sheffield. See the file
 *  COPYRIGHT.txt in the software or at http://gate.ac.uk/gate/COPYRIGHT.txt
 *
 *  This file is part of GATE (see http://gate.ac.uk/), and is free
 *  software, licenced under the GNU Library General Public License,
 *  Version 2, June 1991 (in the distribution as file licence.html,
 *  and also available at http://gate.ac.uk/gate/licence.html).
 *
 *  $Id: TermPairComparatorByDescendingScore.java 16296 2012-11-20 12:17:11Z adamfunk $
 */
package gate.termraider.util;

import java.util.*;

/**
 * Comparator used to sort UnorderedTermPair instance in descending order by score.
 */
public class TermPairComparatorByDescendingScore implements Comparator<UnorderedTermPair> {

  private Map<UnorderedTermPair, Double> scores;
  
  public TermPairComparatorByDescendingScore(Map<UnorderedTermPair, Double> scores) {
    this.scores = scores;
  }
  
  public int compare(UnorderedTermPair p0, UnorderedTermPair p1) {
    double diff = scores.get(p0) - scores.get(p1);
    
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

