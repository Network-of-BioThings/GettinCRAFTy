/*
 *  Or.java
 *
 *  Copyright (c) 1995-2012, The University of Sheffield. See the file
 *  COPYRIGHT.txt in the software or at http://gate.ac.uk/gate/COPYRIGHT.txt
 *
 *  This file is part of GATE (see http://gate.ac.uk/), and is free
 *  software, licenced under the GNU Library General Public License,
 *  Version 2, June 1991 (in the distribution as file licence.html,
 *  and also available at http://gate.ac.uk/gate/licence.html).
 *
 *  Valentin Tablan, 20 Feb 2012
 *
 *  $Id: Or.java 15535 2012-03-06 16:08:48Z valyt $
 */
package gate.creole.coref.matchers;

import java.util.List;

import gate.Annotation;
import gate.creole.ResourceInstantiationException;
import gate.creole.coref.CorefBase;
import gate.creole.coref.Matcher;

/**
 * A compound matcher that succeeds if any of the sub-matchers succeeds.  
 */
public class Or extends CompoundMatcher {
  
  public Or(List<Matcher> subMatchers) throws ResourceInstantiationException {
    super(subMatchers);
  }
  
  public Or(Matcher[] subMatchers) throws ResourceInstantiationException {
    super(subMatchers);
  }

  /* (non-Javadoc)
   * @see gate.creole.coref.Matcher#matches(gate.Annotation[], int, int, gate.creole.coref.CorefBase)
   */
  @Override
  public boolean matches(Annotation[] anaphors, int antecedent, int anaphor,
                         CorefBase owner) {
    for(Matcher matcher : subMatchers) {
      if(matcher.matches(anaphors, antecedent, anaphor, owner)) return true;
    }
    return false;
  }
  


}
