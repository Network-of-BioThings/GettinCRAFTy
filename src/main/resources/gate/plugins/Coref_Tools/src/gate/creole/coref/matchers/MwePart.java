/*
 * MwePart.java
 * 
 * Copyright (c) 1995-2012, The University of Sheffield. See the file
 * COPYRIGHT.txt in the software or at http://gate.ac.uk/gate/COPYRIGHT.txt
 * 
 * This file is part of GATE (see http://gate.ac.uk/), and is free software,
 * licenced under the GNU Library General Public License, Version 2, June 1991
 * (in the distribution as file licence.html, and also available at
 * http://gate.ac.uk/gate/licence.html).
 * 
 * Valentin Tablan, 6 Mar 2012
 * 
 * $Id: MwePart.java 15535 2012-03-06 16:08:48Z valyt $
 */
package gate.creole.coref.matchers;

import static gate.creole.coref.Utils.UPPER_INITIAL_PATTERN;
import static gate.creole.coref.Utils.MWE_UPPER_INITIAL_PATTERN;
import gate.Annotation;
import gate.Utils;
import gate.creole.coref.CorefBase;

/**
 * Matcher that checks whether the text of one annotation is one constituent
 * part of the text for the other annotation (which must be a multi-word
 * expression).
 */
public class MwePart extends AbstractMatcher {
  /**
   * @param annotationType
   * @param antecedentType
   */
  public MwePart(String annotationType, String antecedentType) {
    super(annotationType, antecedentType);
  }

  /*
   * (non-Javadoc)
   * 
   * @see gate.creole.coref.Matcher#matches(gate.Annotation[], int, int,
   * gate.creole.coref.CorefBase)
   */
  @Override
  public boolean matches(Annotation[] anaphors, int antecedent, int anaphor,
                         CorefBase owner) {
    String text1 = Utils.cleanStringFor(owner.getDocument(), anaphors[anaphor]);
    String text2 = Utils.cleanStringFor(owner.getDocument(), 
        anaphors[antecedent]);
    String whole = null;
    String part = null;
    if(UPPER_INITIAL_PATTERN.matcher(text1).matches()) {
      if(MWE_UPPER_INITIAL_PATTERN.matcher(text2).matches()) {
        whole = text2;
        part = text1;
      }
    } else if(UPPER_INITIAL_PATTERN.matcher(text2).matches()) {
      if(MWE_UPPER_INITIAL_PATTERN.matcher(text1).matches()) {
        whole = text1;
        part = text2;
      }
    }
    if(whole != null && part != null) {
      String[] parts = gate.creole.coref.Utils.partsForMwe(whole);
      for(String aPart : parts) {
        if(aPart.equals(part)) return true;
      }
    }
    return false;
  }
}
