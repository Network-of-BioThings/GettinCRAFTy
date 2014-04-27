/*
 *  DocumentText.java
 *
 *  Copyright (c) 1995-2012, The University of Sheffield. See the file
 *  COPYRIGHT.txt in the software or at http://gate.ac.uk/gate/COPYRIGHT.txt
 *
 *  This file is part of GATE (see http://gate.ac.uk/), and is free
 *  software, licenced under the GNU Library General Public License,
 *  Version 2, June 1991 (in the distribution as file licence.html,
 *  and also available at http://gate.ac.uk/gate/licence.html).
 *
 *  Valentin Tablan, 21 Feb 2012
 *
 *  $Id: DocumentText.java 15487 2012-02-28 15:35:34Z valyt $
 */
package gate.creole.coref.taggers;

import java.util.Collections;
import java.util.Set;

import gate.Annotation;
import gate.Utils;
import gate.creole.coref.CorefBase;
import gate.creole.coref.Tagger;

/**
 *
 */
public class DocumentText extends AbstractTagger {
  
  public DocumentText(String annotationType) {
    super(annotationType);
  }

  /* (non-Javadoc)
   * @see gate.creole.coref.Tagger#tag(gate.Annotation[], int, gate.creole.coref.CorefBase)
   */
  @Override
  public Set<String> tag(Annotation[] anaphors, int anaphor, CorefBase owner) {
    return Collections.singleton(Utils.cleanStringFor(owner.getDocument(), 
        anaphors[anaphor]));
  }
}
