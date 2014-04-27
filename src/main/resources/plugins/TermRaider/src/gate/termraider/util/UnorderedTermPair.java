/*
 *  Copyright (c) 2010--2012, The University of Sheffield. See the file
 *  COPYRIGHT.txt in the software or at http://gate.ac.uk/gate/COPYRIGHT.txt
 *
 *  This file is part of GATE (see http://gate.ac.uk/), and is free
 *  software, licenced under the GNU Library General Public License,
 *  Version 2, June 1991 (in the distribution as file licence.html,
 *  and also available at http://gate.ac.uk/gate/licence.html).
 *
 *  $Id: UnorderedTermPair.java 16296 2012-11-20 12:17:11Z adamfunk $
 */
package gate.termraider.util;

import java.io.Serializable;


public class UnorderedTermPair implements Comparable<UnorderedTermPair>, Serializable  {
  
  private static final long serialVersionUID = -1755379880327093161L;
  
  private Term t0, t1;
  
  public UnorderedTermPair(Term t0, Term t1) {
    if (t0.compareTo(t1) <= 0) {
      this.t0 = t0;
      this.t1 = t1;
    }
    else {
      this.t0 = t1;
      this.t1 = t0;
    }
  }

  public String toString() {
    return t0.toString() + " + " + t1.toString();
  }
  
  public Term getTerm0() {
    return t0;
  }
  
  public Term getTerm1() {
    return t1;
  }
  
  public boolean equals(Object other) {
    return (other instanceof UnorderedTermPair) && 
      (this.compareTo((UnorderedTermPair) other) == 0);
  }
  
  public int hashCode() {
    return this.t0.hashCode() + this.t1.hashCode();
  }

  @Override
  public int compareTo(UnorderedTermPair other) {
    int comp = this.t0.compareTo(other.t0);
    if (comp != 0) {
      return comp;
    }
    
    comp = this.t1.compareTo(other.t1);
    return comp;
  }
  
}
