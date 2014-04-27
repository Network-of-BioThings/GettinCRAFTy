/*
 *  VerbImpl.java
 *
 *  Copyright (c) 1995-2012, The University of Sheffield. See the file
 *  COPYRIGHT.txt in the software or at http://gate.ac.uk/gate/COPYRIGHT.txt
 *
 *  This file is part of GATE (see http://gate.ac.uk/), and is free
 *  software, licenced under the GNU Library General Public License,
 *  Version 2, June 1991 (in the distribution as file licence.html,
 *  and also available at http://gate.ac.uk/gate/licence.html).
 *
 *  Marin Dimitrov, 20/May/2002
 *
 *  $Id: VerbImpl.java 15334 2012-02-07 13:57:47Z ian_roberts $
 */

package gate.wordnet;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;
import net.didion.jwnl.dictionary.Dictionary;

/** Represents WordNet verb.
 */
public class VerbImpl extends WordSenseImpl
                      implements Verb {

  private ArrayList verbFrames;

  public VerbImpl(Word _word,
                  Synset _synset,
                  int _senseNumber,
                  int _orderInSynset,
                  boolean _isSemcor,
                  net.didion.jwnl.data.Verb _jwVerb,
                  Dictionary _wnDict) {

    super(_word,_synset,_senseNumber,_orderInSynset,_isSemcor, _wnDict);

    Assert.assertNotNull(_jwVerb);

    String[] jwFrames = _jwVerb.getVerbFrames();
    this.verbFrames = new ArrayList(jwFrames.length);

    for (int i= 0; i< jwFrames.length; i++) {
      this.verbFrames.add(new VerbFrameImpl(jwFrames[i]));
    }
  }

  /** returns the verb frames associated with this synset */
  public List getVerbFrames() {
    return this.verbFrames;
  }
}