/*
 *  Relation.java
 *
 *  Copyright (c) 1995-2012, The University of Sheffield. See the file
 *  COPYRIGHT.txt in the software or at http://gate.ac.uk/gate/COPYRIGHT.txt
 *
 *  This file is part of GATE (see http://gate.ac.uk/), and is free
 *  software, licenced under the GNU Library General Public License,
 *  Version 2, June 1991 (in the distribution as file licence.html,
 *  and also available at http://gate.ac.uk/gate/licence.html).
 *
 *  Marin Dimitrov, 16/May/2002
 *
 *  $Id: SynsetImpl.java 15334 2012-02-07 13:57:47Z ian_roberts $
 */

package gate.wordnet;

import java.util.*;

import junit.framework.Assert;
import net.didion.jwnl.JWNLException;
import net.didion.jwnl.data.*;
import net.didion.jwnl.dictionary.Dictionary;

import gate.util.GateRuntimeException;


public class SynsetImpl implements Synset {

  private ArrayList wordSenses;
  private ArrayList semRelations;
  private String gloss;
  private int synsetPOS;
  Dictionary wnDictionary;
  private long synsetOffset;

  public SynsetImpl(net.didion.jwnl.data.Synset jwSynset, Dictionary _wnDictionary) throws GateRuntimeException {

    //0.
    Assert.assertNotNull(jwSynset);

    //dictionary
    this.wnDictionary = _wnDictionary;

    //offset
    this.synsetOffset = jwSynset.getOffset();

    //pos
    this.synsetPOS = WNHelper.POS2int(jwSynset.getPOS());

    //gloss
    this.gloss = jwSynset.getGloss();

    //word senses
    net.didion.jwnl.data.Word[] synsetWords = jwSynset.getWords();
    this.wordSenses = new ArrayList(synsetWords.length);

    for (int i= 0; i< synsetWords.length; i++) {

      net.didion.jwnl.data.Word jwWord = synsetWords[i];
      IndexWord jwIndexWord = null;

      try {
        jwIndexWord = this.wnDictionary.lookupIndexWord(jwWord.getPOS(),jwWord.getLemma());
      }
      catch(JWNLException jwe) {
        throw new GateRuntimeException(jwe.getMessage());
      }

      Word gateWord = new WordImpl(jwWord.getLemma(),
                                   jwIndexWord.getSenseCount(),
                                   this.wnDictionary);

      //construct the proper word form
      WordSense gateWordSense = null;

     /* if (this.synsetPOS == WordNet.POS_ADJECTIVE) {

        //Assert.assertTrue(jwWord instanceof net.didion.jwnl.data.Adjective);
        net.didion.jwnl.data.Adjective jwAdjective = (net.didion.jwnl.data.Adjective)jwWord;

        gateWordSense = new AdjectiveImpl(gateWord,
                                          this,
                                          0,
                                          jwWord.getIndex(),
                                          false,
                                          WNHelper.AdjPosition2int(jwAdjective),
                                          this.wnDictionary);
      }

      else*/ 
        if (this.synsetPOS == WordNet.POS_VERB) {

        Assert.assertTrue(jwWord instanceof net.didion.jwnl.data.Verb);
        net.didion.jwnl.data.Verb jwVerb = (net.didion.jwnl.data.Verb)jwWord;

        gateWordSense = new VerbImpl(gateWord,
                                      this,
                                      0,
                                      jwWord.getIndex(),
                                      false,
                                      jwVerb,
                                      this.wnDictionary);
      }

      else {
        gateWordSense = new WordSenseImpl(gateWord,
                                          this,
                                          0,
                                          jwWord.getIndex(),
                                          false,
                                          this.wnDictionary);
      }

      this.wordSenses.add(gateWordSense);
    }

  }


  /** returns the part-of-speech for this synset, see WordNet::POS_XXX constants */
  public int getPOS(){
    return this.synsetPOS;
  }

  /** is this synset a UB - i.e. has no hypernym */
  public boolean isUniqueBeginner() throws WordNetException {
    List parents = getSemanticRelations(Relation.REL_HYPERNYM);
    return parents.isEmpty();
  }

  /** textual description of the synset */
  public String getGloss(){
    return this.gloss;
  }


  /** WordSenses contained in this synset */
  public List getWordSenses(){
    return this.wordSenses;
  }


  /** get specific WordSense according to its order in the synset - most important senses come first  */
  public WordSense getWordSense(int offset){
    return (WordSense)this.wordSenses.get(offset);
  }


  /** get the SemanticRelation-s of this synset */
  public List getSemanticRelations() throws WordNetException{

    if (null == this.semRelations) {
      _loadSemanticRelations();
    }

    return this.semRelations;
  }

  /** get the SemanticRelation-s of specific type (HYPERNYm) for this synset */
  public List getSemanticRelations(int type) throws WordNetException{

    List result = new ArrayList(1);

    if (null == this.semRelations) {
      _loadSemanticRelations();
    }

    Iterator it = this.semRelations.iterator();
    while (it.hasNext()) {
      SemanticRelation sRel = (SemanticRelation)it.next();
      Assert.assertNotNull(sRel);
      if (type == sRel.getType()) {
        result.add(sRel);
      }
    }

    return result;
  }


  private void _loadSemanticRelations() throws WordNetException{

    POS jwPOS = null;
    jwPOS = WNHelper.int2POS(this.synsetPOS);

    try {
      net.didion.jwnl.data.Synset jwSynset = this.wnDictionary.getSynsetAt(jwPOS,this.synsetOffset);
      Assert.assertNotNull(jwSynset);
      Pointer[] jwPointers = jwSynset.getPointers();

      this.semRelations = new ArrayList(jwPointers.length);

      for (int i= 0; i< jwPointers.length; i++) {
        Pointer currPointer = jwPointers[i];

        //skip lexical relations
        if (true == currPointer.isLexical()) {
          continue;
        }
        PointerType currType = currPointer.getType();
        try {
        
//        PointerTarget ptrSource = currPointer.getSource();
        PointerTarget ptrTarget = currPointer.getTarget();
        Assert.assertTrue(ptrTarget instanceof net.didion.jwnl.data.Synset);
        net.didion.jwnl.data.Synset jwTargetSynset = (net.didion.jwnl.data.Synset)ptrTarget;

        Synset gateTargetSynset = new SynsetImpl(jwTargetSynset,this.wnDictionary);
        SemanticRelation currSemRel = new SemanticRelationImpl(WNHelper.PointerType2int(currType),
                                                            this,
                                                            gateTargetSynset);
        //add to list of sem relations for this synset
        this.semRelations.add(currSemRel);
        }
        catch (IllegalArgumentException e){
          //System.err.println("Unknown PointerType:" + currType);
        }
      }
    }
    catch(JWNLException e) {
      throw new WordNetException(e);
    }
  }

  /** offset in index files */
  public long getOffset() {

    return this.synsetOffset;
  }
}