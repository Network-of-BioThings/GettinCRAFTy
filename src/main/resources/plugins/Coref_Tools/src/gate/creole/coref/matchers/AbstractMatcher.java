/*
 *  AbstractMatcher.java
 *
 *  Copyright (c) 1995-2012, The University of Sheffield. See the file
 *  COPYRIGHT.txt in the software or at http://gate.ac.uk/gate/COPYRIGHT.txt
 *
 *  This file is part of GATE (see http://gate.ac.uk/), and is free
 *  software, licenced under the GNU Library General Public License,
 *  Version 2, June 1991 (in the distribution as file licence.html,
 *  and also available at http://gate.ac.uk/gate/licence.html).
 *
 *  Valentin Tablan, 28 Feb 2012
 *
 *  $Id: AbstractMatcher.java 15568 2012-03-09 14:15:23Z valyt $
 */
package gate.creole.coref.matchers;

import gate.Annotation;
import gate.creole.ResourceInstantiationException;
import gate.creole.coref.CorefBase;
import gate.creole.coref.Matcher;

/**
 *
 */
public abstract class AbstractMatcher implements Matcher {
  
  protected String annotationType;
  
  protected String antecedentType;
  
  
  
  protected AbstractMatcher(String annotationType, String antecedentType) {
    this.annotationType = annotationType;
    this.antecedentType = antecedentType;
  }

  /* (non-Javadoc)
   * @see gate.creole.coref.Matcher#getAnnotationType()
   */
  @Override
  public String getAnnotationType() {
    return annotationType;
  }

  /* (non-Javadoc)
   * @see gate.creole.coref.Matcher#getAntecedentType()
   */
  @Override
  public String getAntecedentType() {
    return antecedentType;
  }

  /* (non-Javadoc)
   * @see gate.creole.coref.Matcher#init(gate.creole.coref.CorefBase)
   */
  @Override
  public void init(CorefBase owner) throws ResourceInstantiationException {}
  
  
}
