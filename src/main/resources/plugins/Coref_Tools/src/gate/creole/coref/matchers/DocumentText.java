/*
 *  IdentityMatcher.java
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
 *  $Id: DocumentText.java 15532 2012-03-06 14:34:56Z valyt $
 */
package gate.creole.coref.matchers;

import gate.Annotation;
import gate.Utils;
import gate.creole.coref.CorefBase;
import gate.creole.coref.Matcher;

/**
 * A {@link Matcher} that checks whether the document text for two annotations 
 * is identical.
 */
public class DocumentText extends  AbstractMatcher {
  
  public DocumentText(String annotationType, String antecedentType) {
    super(annotationType, antecedentType);
  }

  /* (non-Javadoc)
   * @see gate.creole.coref.Matcher#matches(gate.Annotation[], int, int, gate.creole.coref.CorefBase)
   */
  @Override
  public boolean matches(Annotation[] anaphors, int antecedent, int anaphor,
                         CorefBase owner) {
    String one = Utils.cleanStringFor(owner.getDocument(), 
      anaphors[anaphor]);
    String other = Utils.cleanStringFor(owner.getDocument(), 
      anaphors[antecedent]);
    return one.equals(other);
  }
  
}