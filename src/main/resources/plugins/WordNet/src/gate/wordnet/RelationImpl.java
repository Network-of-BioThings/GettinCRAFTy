/*
 *  Synset.java
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
 *  $Id: RelationImpl.java 15334 2012-02-07 13:57:47Z ian_roberts $
 */

package gate.wordnet;


class RelationImpl implements Relation {

  private int type;

  /** never use directly - instantiate one of the ancestors only */
  protected RelationImpl(int _type) {
    this.type = _type;
  }


  /** returns the type of the relation - one of REL_XXX*/
  public int getType() {
    return this.type;
  }


  /** returns a symbol for the relation, e.g. "@" */
  public String getSymbol() {
    return WNHelper.int2PointerType(this.type).getKey();
  }


  /** returns a label for the relation, e.g. "HYPERNYM" */
  public String getLabel() {
    return WNHelper.int2PointerType(this.type).getLabel();
  }


  /** returns the inverse relation (Hyponym  <-> Hypernym, etc)*/
  public int getInverseType() {

    switch(this.type) {

      case Relation.REL_ANTONYM:
        return Relation.REL_ANTONYM;

      case Relation.REL_HYPONYM:
        return Relation.REL_HYPERNYM;

      case Relation.REL_HYPERNYM:
        return Relation.REL_HYPONYM;

      case Relation.REL_MEMBER_HOLONYM:
        return Relation.REL_MEMBER_MERONYM;

      case Relation.REL_MEMBER_MERONYM:
        return Relation.REL_MEMBER_HOLONYM;

      case Relation.REL_SIMILAR_TO:
        return Relation.REL_SIMILAR_TO;

      case Relation.REL_ATTRIBUTE:
        return Relation.REL_ATTRIBUTE;

      case Relation.REL_VERB_GROUP:
        return Relation.REL_VERB_GROUP;

      default:
        return -1;
    }
  }


  /** checks if the relation is applicab;le to specific POS - see REL_XXX comments */
  public boolean isApplicableTo(int pos) {
    return WNHelper.int2PointerType(this.type).appliesTo(WNHelper.int2POS(pos));
  }

}