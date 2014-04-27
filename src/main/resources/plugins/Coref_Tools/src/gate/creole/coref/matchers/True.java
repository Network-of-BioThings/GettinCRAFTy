/*
 *  True.java
 *
 *  Copyright (c) 1995-2010, The University of Sheffield. See the file
 *  COPYRIGHT.txt in the software or at http://gate.ac.uk/gate/COPYRIGHT.txt
 *
 *  This file is part of GATE (see http://gate.ac.uk/), and is free
 *  software, licenced under the GNU Library General Public License,
 *  Version 2, June 1991 (in the distribution as file licence.html,
 *  and also available at http://gate.ac.uk/gate/licence.html).
 *
 *  Valentin Tablan, 16 Feb 2012
 *
 *  $Id: True.java 15487 2012-02-28 15:35:34Z valyt $
 */
package gate.creole.coref.matchers;

import java.util.List;

import gate.Annotation;
import gate.creole.coref.CorefBase;
import gate.creole.coref.Matcher;

/**
 *
 */
public class True extends AbstractMatcher {
  
  public True(String annotationType, String antecedentType) {
    super(annotationType, antecedentType);
    // TODO Auto-generated constructor stub
  }

  /* (non-Javadoc)
   * @see gate.creole.coref.Matcher#matches(java.util.List, int, int, gate.creole.coref.CorefBase)
   */
  @Override
  public boolean matches(Annotation[] anaphors, int antecedent,
                         int anaphor, CorefBase owner) {
    return true;
  }
}
