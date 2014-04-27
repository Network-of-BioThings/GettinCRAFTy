/*
 *  TransitiveAnd.java
 *
 *  Copyright (c) 1995-2012, The University of Sheffield. See the file
 *  COPYRIGHT.txt in the software or at http://gate.ac.uk/gate/COPYRIGHT.txt
 *
 *  This file is part of GATE (see http://gate.ac.uk/), and is free
 *  software, licenced under the GNU Library General Public License,
 *  Version 2, June 1991 (in the distribution as file licence.html,
 *  and also available at http://gate.ac.uk/gate/licence.html).
 *
 *  Valentin Tablan, 5 Mar 2012
 *
 *  $Id: TransitiveAnd.java 15535 2012-03-06 16:08:48Z valyt $
 */
package gate.creole.coref.matchers;

import gate.Annotation;
import gate.creole.ResourceInstantiationException;
import gate.creole.coref.CorefBase;
import gate.creole.coref.Matcher;
import gate.relations.Relation;
import gate.relations.RelationsUtils;

/**
 * A meta-{@link Matcher} that modifies the behaviour of an existing 
 * {@link Matcher} - the delegate (provided at construction time). The 
 * TransitiveAnd matches when the provided anaphor matches (according to the
 * delegate) with the antecendent and all the other annotations which the 
 * antecedent co-refers to.    
 */
public class TransitiveAnd extends CompoundMatcher {
  
  public TransitiveAnd(Matcher delegate) throws ResourceInstantiationException {
    super(new Matcher[]{delegate});
  }

  /* (non-Javadoc)
   * @see gate.creole.coref.Matcher#matches(gate.Annotation[], int, int, gate.creole.coref.CorefBase)
   */
  @Override
  public boolean matches(Annotation[] anaphors, int antecedent, int anaphor,
                         CorefBase owner) {
    for(int annotationId : RelationsUtils.transitiveClosure(
        owner.getRelationSet(), Relation.COREF, anaphors[antecedent].getId())) {
      // find the antecendent Id
      int antecedentId = 0;
      while(antecedentId < anaphors.length){
        if(anaphors[antecedentId].getId() == annotationId)
          break;
        else
          antecedentId++;
      }
      if(antecedentId < anaphors.length &&
         // check if applicable
         subMatchers[0].getAnnotationType().equals(anaphors[anaphor].getType()) &&
         subMatchers[0].getAntecedentType().equals(anaphors[antecedentId].getType()) &&
         (!subMatchers[0].matches(anaphors, antecedentId, anaphor, owner))) {
        return false;
      }
    }
    return true;
  }
}
