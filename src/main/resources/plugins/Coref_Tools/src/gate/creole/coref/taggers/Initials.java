/*
 * Initials.java
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
 * $Id: Initials.java 15532 2012-03-06 14:34:56Z valyt $
 */
package gate.creole.coref.taggers;

import gate.Annotation;
import gate.Utils;
import gate.creole.coref.CorefBase;
import gate.creole.coref.Tagger;

import java.util.HashSet;
import java.util.Set;

/**
 * A {@link Tagger} that uses the initials of a multi-word capitalised text as a
 * tag (International Business Machines -> {IBM, I.B.M.}).
 */
public class Initials extends AbstractTagger {
  
  /**
   * @param annotationType
   */
  public Initials(String annotationType) {
    super(annotationType);
  }

  /*
   * (non-Javadoc)
   * 
   * @see gate.creole.coref.Tagger#tag(gate.Annotation[], int,
   * gate.creole.coref.CorefBase)
   */
  @Override
  public Set<String> tag(Annotation[] anaphors, int anaphor, CorefBase owner) {
    String input = Utils.cleanStringFor(owner.getDocument(), anaphors[anaphor]);
    Set<String> res = new HashSet<String>();
    // check it's in the right format (e.g. "McDonnel Douglas")
    if(gate.creole.coref.Utils.MWE_UPPER_INITIAL_PATTERN.matcher(input).matches()) {
      char[] initialChars = gate.creole.coref.Utils.initialsForMwe(input);
      StringBuilder initials = new StringBuilder();
      StringBuilder initialsDotted = new StringBuilder();
      for(int i = 0; i < initialChars.length; i++) {
        initials.append(initialChars[i]);
        initialsDotted.append(initialChars[i]);
        initialsDotted.append('.');
      }
      res.add(initials.toString());
      res.add(initialsDotted.toString());
    }
    return res;
  }
}
