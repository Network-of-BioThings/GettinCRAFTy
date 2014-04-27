/*
 *  Copyright (c) 2010--2012, The University of Sheffield. See the file
 *  COPYRIGHT.txt in the software or at http://gate.ac.uk/gate/COPYRIGHT.txt
 *
 *  This file is part of GATE (see http://gate.ac.uk/), and is free
 *  software, licenced under the GNU Library General Public License,
 *  Version 2, June 1991 (in the distribution as file licence.html,
 *  and also available at http://gate.ac.uk/gate/licence.html).
 *
 *  $Id: TermComparator.java 16296 2012-11-20 12:17:11Z adamfunk $
 */
package gate.termraider.util;

import java.util.*;

/**
 * Comparator used to sort Term data structures.
 */
public class TermComparator implements Comparator<Term> {

  public int compare(Term t0, Term t1) {
    return t0.compareTo(t1);
  }
  
}

