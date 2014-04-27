/* Copyright (C) 2002 Univ. of Massachusetts Amherst, Computer Science Dept.
   This file is part of "MALLET" (MAchine Learning for LanguagE Toolkit).
   http://www.cs.umass.edu/~mccallum/mallet
   This software is provided under the terms of the Common Public License,
   version 1.0, as published by http://www.opensource.org.  For further
   information, see the file `LICENSE' included with this distribution. */


/**
  Evaluate segmentation f1 for several different tags (marked in OIB format).
  For example, tags might be B-PERSON I-PERSON O B-LOCATION I-LOCATION O...

   @author Ryan McDonald
 */

//package edu.umass.cs.mallet.users.ryantm.medline;
package edu.upenn.cis.taggers.gene;
import edu.umass.cs.mallet.base.fst.MultiSegmentationEvaluator;

public class GeneSegmentationEvaluator extends MultiSegmentationEvaluator
{

 public GeneSegmentationEvaluator (Object[] segmentStartTags, Object[] segmentContinueTags)
 {
  super(segmentStartTags,segmentContinueTags,false);

  //setViterbiVariables(false,false,null,10000,10000,"UTF-8");
 }
 
}
