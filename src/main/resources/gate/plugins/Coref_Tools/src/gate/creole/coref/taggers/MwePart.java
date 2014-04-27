/*
 *  SingleWord.java
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
 *  $Id: MwePart.java 15535 2012-03-06 16:08:48Z valyt $
 */
package gate.creole.coref.taggers;

import gate.Annotation;
import gate.Utils;
import gate.creole.coref.CorefBase;

import java.util.HashSet;
import java.util.Set;

import static gate.creole.coref.Utils.MWE_UPPER_INITIAL_PATTERN;
/**
 * Tagger that emits constituent words of a multi-word name as tags (e.g. 
 * &quot;Lehman Brothers&quot -> {&quot;Lehman&quot;, &quot;Brothers&quot;).
 */
public class MwePart extends AbstractTagger {
  
  /**
   * @param annotationType
   */
  public MwePart(String annotationType) {
    super(annotationType);
  }

  /* (non-Javadoc)
   * @see gate.creole.coref.Tagger#tag(gate.Annotation[], int, gate.creole.coref.CorefBase)
   */
  @Override
  public Set<String> tag(Annotation[] anaphors, int anaphor, CorefBase owner) {
    String input = Utils.cleanStringFor(owner.getDocument(), anaphors[anaphor]);
    Set<String> res = new HashSet<String>();
    // check it's in the right format (e.g. "McDonnel Douglas")
    if(MWE_UPPER_INITIAL_PATTERN.matcher(input).matches()) {
      for(String word : gate.creole.coref.Utils.partsForMwe(input)) {
        res.add(word);
      }
    }
    return res;
  }
}
