/*
 *  WordImpl.java
 *
 *  Copyright (c) 1995-2012, The University of Sheffield. See the file
 *  COPYRIGHT.txt in the software or at http://gate.ac.uk/gate/COPYRIGHT.txt
 *
 *  This file is part of GATE (see http://gate.ac.uk/), and is free
 *  software, licenced under the GNU Library General Public License,
 *  Version 2, June 1991 (in the distribution as file licence.html,
 *  and also available at http://gate.ac.uk/gate/licence.html).
 *
 *  Marin Dimitrov, 17/May/2002
 *
 *  $Id: WordImpl.java 15334 2012-02-07 13:57:47Z ian_roberts $
 */

package gate.wordnet;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;
import net.didion.jwnl.JWNLException;
import net.didion.jwnl.data.IndexWord;
import net.didion.jwnl.data.IndexWordSet;
import net.didion.jwnl.dictionary.Dictionary;

public class WordImpl implements Word {

  private String lemma;
  private int senseCount;
  private ArrayList wordSenses;
  private Dictionary wnDictionary;

  public WordImpl(String _lemma, int _senseCount, Dictionary _wnDictionary) {

    //0.
    Assert.assertNotNull(_lemma);
    Assert.assertNotNull(_wnDictionary);
    Assert.assertTrue(_senseCount > 0);

    this.lemma = _lemma;
    this.senseCount = _senseCount;
    this.wnDictionary = _wnDictionary;
  }


  /** returns the senses of this word */
  public List getWordSenses() throws WordNetException{

    //do we have the list already?
    if (null == this.wordSenses) {
      _loadWordSenses();
    }

    return this.wordSenses;
  }

  private void _loadWordSenses() throws WordNetException {

//    Dictionary dict = this.wnMain.getJWNLDictionary();

    try {
      IndexWordSet iwSet = this.wnDictionary.lookupAllIndexWords(this.lemma);
      IndexWord[] arrIndexWords = iwSet.getIndexWordArray();

      for (int i=0; i< arrIndexWords.length; i++) {
        IndexWord iWord = arrIndexWords[i];
        net.didion.jwnl.data.Synset[] synsets = iWord.getSenses();
        for (int j=0; j< synsets.length; j++) {
          net.didion.jwnl.data.Synset currSynset = synsets[j];
        }
      }

//      this.
    }
    catch(JWNLException jwne) {
      throw new WordNetException(jwne);
    }

  }


  /** returns the lemma of this word */
  public String getLemma(){
    return this.lemma;
  }


  /** returns the number of senses of this word (not necessarily loading them from storage) */
  public int getSenseCount(){
    return this.senseCount;
  }
}