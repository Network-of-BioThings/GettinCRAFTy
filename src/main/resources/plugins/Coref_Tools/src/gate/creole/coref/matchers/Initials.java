/*
 *  Initials.java
 *
 *  Copyright (c) 1995-2012, The University of Sheffield. See the file
 *  COPYRIGHT.txt in the software or at http://gate.ac.uk/gate/COPYRIGHT.txt
 *
 *  This file is part of GATE (see http://gate.ac.uk/), and is free
 *  software, licenced under the GNU Library General Public License,
 *  Version 2, June 1991 (in the distribution as file licence.html,
 *  and also available at http://gate.ac.uk/gate/licence.html).
 *
 *  Valentin Tablan, 6 Mar 2012
 *
 *  $Id: Initials.java 15535 2012-03-06 16:08:48Z valyt $
 */
package gate.creole.coref.matchers;


import gate.Annotation;
import gate.Utils;
import gate.creole.coref.CorefBase;
import gate.creole.coref.Matcher;

import static gate.creole.coref.Utils.MWE_UPPER_INITIAL_PATTERN;
import static gate.creole.coref.Utils.INITIALS_PATTERN;

/**
 * A {@link Matcher} that checks whether the text of one annotation is the 
 * initials of another (e.g. MD <-> McDonnel Douglas).
 */
public class Initials extends AbstractMatcher {

  /**
   * @param annotationType
   * @param antecedentType
   */
  public Initials(String annotationType, String antecedentType) {
    super(annotationType, antecedentType);
  }

  /* (non-Javadoc)
   * @see gate.creole.coref.Matcher#matches(gate.Annotation[], int, int, gate.creole.coref.CorefBase)
   */
  @Override
  public boolean matches(Annotation[] anaphors, int antecedent, int anaphor,
                         CorefBase owner) {
    String text1 = Utils.cleanStringFor(owner.getDocument(), 
        anaphors[anaphor]);
    String text2 = Utils.cleanStringFor(owner.getDocument(),
        anaphors[antecedent]);
    String full = null;
    String abbrev = null;
    if(INITIALS_PATTERN.matcher(text1).matches()) {
      if(MWE_UPPER_INITIAL_PATTERN.matcher(text2).matches()) {
        full = text2;
        abbrev = text1;
      }
    } else if(INITIALS_PATTERN.matcher(text2).matches()) {
      if(MWE_UPPER_INITIAL_PATTERN.matcher(text1).matches()) {
        full = text1;
        abbrev = text2;
      }
    }
    if(full != null && abbrev != null) {
      char[] initials = gate.creole.coref.Utils.initialsForMwe(full);
      int pos = 0;
      for(int iniPos = 0; iniPos < initials.length; iniPos++) {
        if(pos >= abbrev.length() || 
           initials[iniPos] != abbrev.charAt(pos)){
          return false;
        } else {
          pos++;
        }
        // skip optional '.'
        if(pos < abbrev.length() && abbrev.charAt(pos) == '.') pos++; 
      }
      if(pos != abbrev.length()) return false;
      return true;
    }
    return false;
  }
}
