/*
 *  WordSense.java
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
 *  $Id: WNHelper.java 15334 2012-02-07 13:57:47Z ian_roberts $
 */

package gate.wordnet;

import net.didion.jwnl.data.POS;
import net.didion.jwnl.data.PointerType;


final class WNHelper {

  private WNHelper() {
  }

  /** converts GATE pos to JWNL pos */
  public static POS int2POS(int pos) {

    POS result = null;

    switch(pos) {

      case WordNet.POS_ADJECTIVE:
        result = POS.ADJECTIVE;
        break;

      case WordNet.POS_ADVERB:
        result = POS.ADVERB;
        break;

      case WordNet.POS_NOUN:
        result = POS.NOUN;
        break;

      case WordNet.POS_VERB:
        result = POS.VERB;
        break;

      default:
        throw new IllegalArgumentException();
    }

    return result;
  }

  /** converts JWNL pos to GATE pos */
  public static int POS2int(POS pos) {

    int result;

    //pos
    if (pos.equals(POS.ADJECTIVE)) {
      result = WordNet.POS_ADJECTIVE;
    }
    else if (pos.equals(POS.ADVERB)) {
      result = WordNet.POS_ADVERB;
    }
    else if (pos.equals(POS.NOUN)) {
      result = WordNet.POS_NOUN;
    }
    else if (pos.equals(POS.VERB)) {
      result = WordNet.POS_VERB;
    }
    else {
      throw new IllegalArgumentException();
    }

    return result;
  }


  /** converts JWNL pointer type to GATE pointer type */
  public static int PointerType2int(PointerType pt) {

    //0.
    if (null == pt) {
      throw new IllegalArgumentException();
    }

    if (pt.equals(PointerType.ANTONYM)) {
      return Relation.REL_ANTONYM;
    }
    else if (pt.equals(PointerType.ATTRIBUTE)) {
      return Relation.REL_ATTRIBUTE;
    }
    else if (pt.equals(PointerType.CAUSE)) {
      return Relation.REL_CAUSE;
    }
    else if (pt.equals(PointerType.DERIVED)) {
      return Relation.REL_DERIVED_FROM_ADJECTIVE;
    }
    else if (pt.equals(PointerType.ENTAILMENT)) {
      return Relation.REL_ENTAILMENT;
    }
    else if (pt.equals(PointerType.HYPERNYM)) {
      return Relation.REL_HYPERNYM;
    }
    else if (pt.equals(PointerType.HYPONYM)) {
      return Relation.REL_HYPONYM;
    }
    else if (pt.equals(PointerType.MEMBER_HOLONYM)) {
      return Relation.REL_MEMBER_HOLONYM;
    }
    else if (pt.equals(PointerType.MEMBER_MERONYM)) {
      return Relation.REL_MEMBER_MERONYM;
    }
    else if (pt.equals(PointerType.PARTICIPLE_OF)) {
      return Relation.REL_PARTICIPLE_OF_VERB;
    }
    else if (pt.equals(PointerType.PART_HOLONYM)) {
      return Relation.REL_PART_HOLONYM;
    }
    else if (pt.equals(PointerType.PART_MERONYM)) {
      return Relation.REL_PART_MERONYM;
    }
    else if (pt.equals(PointerType.SIMILAR_TO)) {
      return Relation.REL_SIMILAR_TO;
    }
    else if (pt.equals(PointerType.SEE_ALSO)) {
      return Relation.REL_SEE_ALSO;
    }
    else if (pt.equals(PointerType.SUBSTANCE_MERONYM)) {
      return Relation.REL_SUBSTANCE_MERONYM;
    }
    else if (pt.equals(PointerType.SUBSTANCE_HOLONYM)) {
      return Relation.REL_SUBSTANCE_HOLONYM;
    }
    else if (pt.equals(PointerType.VERB_GROUP)) {
      return Relation.REL_VERB_GROUP;
    }
    else{
        throw new IllegalArgumentException();
    }

  }


  /** converts GATE pointer type to JWNL pointer type */
  public static PointerType int2PointerType(int type) {

    switch(type) {

      case Relation.REL_ANTONYM:
        return PointerType.ANTONYM;

      case Relation.REL_ATTRIBUTE:
        return PointerType.ATTRIBUTE;

      case Relation.REL_CAUSE:
        return PointerType.CAUSE;

      case Relation.REL_DERIVED_FROM_ADJECTIVE:
        return PointerType.DERIVED;

      case Relation.REL_ENTAILMENT:
        return PointerType.ENTAILMENT;

      case Relation.REL_HYPERNYM:
        return PointerType.HYPERNYM;

      case Relation.REL_HYPONYM:
        return PointerType.HYPONYM;

      case Relation.REL_MEMBER_HOLONYM:
        return PointerType.MEMBER_HOLONYM;

      case Relation.REL_MEMBER_MERONYM:
        return PointerType.MEMBER_MERONYM;

      case Relation.REL_PARTICIPLE_OF_VERB:
        return PointerType.PARTICIPLE_OF;

      case Relation.REL_PART_HOLONYM:
        return PointerType.PART_HOLONYM;

      case Relation.REL_PART_MERONYM:
        return PointerType.PART_MERONYM;

      case Relation.REL_PERTAINYM:
        return null;

      case Relation.REL_SEE_ALSO:
        return PointerType.SEE_ALSO;

      case Relation.REL_SIMILAR_TO:
        return PointerType.SIMILAR_TO;

      case Relation.REL_SUBSTANCE_HOLONYM:
        return PointerType.SUBSTANCE_HOLONYM;

      case Relation.REL_SUBSTANCE_MERONYM:
        return PointerType.SUBSTANCE_MERONYM;

      case Relation.REL_VERB_GROUP:
        return PointerType.VERB_GROUP;

      default:
        throw new IllegalArgumentException();
    }
  }

  /** checks if relation is semantic one */
  public static boolean isValidSemanticPointer(int _type) {
    return _type == Relation.REL_ATTRIBUTE ||
          _type == Relation.REL_CAUSE ||
          //_type == Relation.REL_DERIVED_FROM_ADJECTIVE ||
          _type == Relation.REL_ENTAILMENT ||
          _type == Relation.REL_HYPERNYM ||
          _type == Relation.REL_HYPONYM ||
          _type == Relation.REL_MEMBER_HOLONYM ||
          _type == Relation.REL_MEMBER_MERONYM ||
          _type == Relation.REL_PART_HOLONYM ||
          _type == Relation.REL_PART_MERONYM ||
          _type == Relation.REL_SIMILAR_TO ||
          _type == Relation.REL_SUBSTANCE_HOLONYM ||
          _type == Relation.REL_SUBSTANCE_MERONYM ||
          _type == Relation.REL_SEE_ALSO ||
          _type == Relation.REL_VERB_GROUP;
  }

  /** checks if relation is lexical one */
  public static boolean isValidLexicalPointer(int _type) {
        return _type == Relation.REL_ANTONYM ||
               _type == Relation.REL_PERTAINYM ||
               _type == Relation.REL_PARTICIPLE_OF_VERB ||
               _type == Relation.REL_SEE_ALSO ||
               _type == Relation.REL_DERIVED_FROM_ADJECTIVE;
  }


  /** converts JWNL adjective position to GATE  adjective position*/
  public static int AdjPosition2int(net.didion.jwnl.data.Adjective adj) {

    int result;

    if (adj.getAdjectivePosition().equals(net.didion.jwnl.data.Adjective.ATTRIBUTIVE)) {
      result = Adjective.ADJ_POS_ATTRIBUTIVE;
    }
    else if (adj.getAdjectivePosition().equals(net.didion.jwnl.data.Adjective.IMMEDIATE_POSTNOMINAL)) {
      result = Adjective.ADJ_POS_IMMEDIATE_POSTNOMINAL;
    }
    else if (adj.getAdjectivePosition().equals(net.didion.jwnl.data.Adjective.NONE)) {
      result = Adjective.ADJ_POS_NONE;
    }
    else if (adj.getAdjectivePosition().equals(net.didion.jwnl.data.Adjective.PREDICATIVE)) {
      result = Adjective.ADJ_POS_PREDICATIVE;
    }
    else {
      throw new IllegalArgumentException();
    }

    return result;
  }
}