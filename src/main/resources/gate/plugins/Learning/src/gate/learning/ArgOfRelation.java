/*
 *  ArgOfRelation.java
 * 
 *  Yaoyong Li 22/03/2007
 *
 *  $Id: ArgOfRelation.java, v 1.0 2007-03-22 12:58:16 +0000 yaoyong $
 */
package gate.learning;

/**
 * Store the identification information and the related features for the
 * annotation corresponding to one argument of a relation
 */
public class ArgOfRelation {
  /** Annotation type one argument of relation refers to. */
  String type;
  /** Annotation feature one argument of relation refers to. */
  String feat;
  /** Attribute features related to the argument. */
  java.util.List attributes;
  /** Ngram features related to the argument. */
  java.util.List ngrams;
  /** Object for arrays and variables for fast computation purpose. */
  ArraysDataSetDefinition arrs;
  /** The maximal total position of the features in this argument. */
  int maxTotalPosition;
}
