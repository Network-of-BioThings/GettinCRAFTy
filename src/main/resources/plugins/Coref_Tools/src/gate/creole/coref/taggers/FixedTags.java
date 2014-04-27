/*
 *  Const.java
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
 *  $Id: FixedTags.java 15487 2012-02-28 15:35:34Z valyt $
 */
package gate.creole.coref.taggers;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import gate.Annotation;
import gate.creole.coref.CorefBase;
import gate.creole.coref.Tagger;

/**
 *
 */
public class FixedTags extends AbstractTagger {
  
  public FixedTags(String annotationType) {
    this(annotationType, "#");
  }

  public FixedTags(String annotationType, String... values ) {
    super(annotationType);
    HashSet<String> vals = new HashSet<String>(values.length);
    for(String val : values) vals.add(val); 
    this.tags = Collections.unmodifiableSet(vals);
  }

  /* (non-Javadoc)
   * @see gate.creole.coref.Tagger#tag(gate.Annotation[], int, gate.creole.coref.CorefBase)
   */
  @Override
  public Set<String> tag(Annotation[] anaphors, int anaphor, CorefBase owner) {
    return tags;
  }
  
  public final Set<String> tags;
}
