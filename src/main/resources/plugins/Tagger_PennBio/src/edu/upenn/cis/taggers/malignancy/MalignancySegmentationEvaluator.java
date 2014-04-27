/* Copyright (C) 2002 Univ. of Massachusetts Amherst, Computer Science Dept.
 This file is part of "MALLET" (MAchine Learning for LanguagE Toolkit).
 http://www.cs.umass.edu/~mccallum/mallet
 This software is provided under the terms of the Common Public License,
 version 1.0, as published by http://www.opensource.org.  For further
 information, see the file `LICENSE' included with this distribution. */


/**
 * 
 @author Ryan McDonald
 */

//package edu.umass.cs.mallet.users.ryantm.medline;
package edu.upenn.cis.taggers.malignancy;

import java.io.IOException;

import edu.umass.cs.mallet.base.fst.MultiSegmentationEvaluator;
import edu.umass.cs.mallet.base.fst.Transducer;
import edu.umass.cs.mallet.base.types.Instance;
import edu.umass.cs.mallet.base.types.InstanceList;
import edu.umass.cs.mallet.base.types.Sequence;
import edu.upenn.cis.taggers.Tag;
import edu.upenn.cis.taggers.TagList;

public class MalignancySegmentationEvaluator extends MultiSegmentationEvaluator
{
  String[] segmentStartTags;
  String[] segmentContinueTags;
  
  public MalignancySegmentationEvaluator (String[] segmentStartTags, String[] segmentContinueTags)
  {
    super(segmentStartTags,segmentContinueTags,false);
    this.segmentStartTags = segmentStartTags;
    this.segmentContinueTags = segmentContinueTags;
        
  }

  public TagList output (Transducer crf, InstanceList eval) throws IOException {
    return output(crf,eval,null);
  }
  
  public TagList output (Transducer crf, InstanceList eval, java.util.Vector spans) throws IOException
  {   
    //String toReturn="";
    //for (int i = 0; i < eval.size(); i++) {
      Instance instance = eval.getInstance(eval.size()-1);
      Sequence input = (Sequence) instance.getData();
      Sequence predOutput = crf.viterbiPath(input).output();
      String[] tokens = (String[])instance.getName();
      
      Tag currentTag = null;
      TagList tagList = new TagList();
      for(int j = 0; j < tokens.length; j++) {
          boolean foundTag = false;
          for(int s = 0; s < segmentStartTags.length; s++) {
              if(((String)predOutput.get(j)).equals(segmentStartTags[s])) {
                  foundTag = true;
                  currentTag = new Tag(segmentStartTags[s].substring(2,segmentStartTags[s].length()));
                  currentTag.addSegment(tokens[j], j);
                  if(spans!=null) {
            currentTag.updateOffset(((int[])spans.get(j))[0],((int[])spans.get(j))[1]);
                  }
          //toReturn+="<"+segmentStartTags[s].substring(2,segmentStartTags[s].length())+">" + tokens[j];
                  for(int j1 = j+1; j1 < tokens.length && ((String)predOutput.get(j1)).equals(segmentContinueTags[s]); j1++) {
                      currentTag.addSegment(tokens[j1], j1);
                      if(spans!=null) {
              currentTag.updateOffset(((int[])spans.get(j1))[0],((int[])spans.get(j1))[1]);
            }
                      //toReturn+=" " + tokens[j1];
                      j=j1;
                  }
                  tagList.addTag(currentTag);
                  currentTag = null;
                  //toReturn+="</"+segmentStartTags[s].substring(2,segmentStartTags[s].length())+">";
                  break;
              }
          }
          //if(!foundTag)
          //    toReturn+=tokens[j];
          
          //if(j < tokens.length-1)
          //    toReturn+=" ";
      }
      //toReturn+="\n";
    //}   
    return tagList;//toReturn;
  }
  
  /*public String output (Transducer crf, InstanceList eval) throws IOException
  {
    StringBuffer toReturn = new StringBuffer();
    for (int i = 0; i < eval.size(); i++) {
      Instance instance = eval.getInstance(i);
      Sequence input = (Sequence) instance.getData();
      Sequence predOutput = crf.viterbiPath(input).output();
      String[] words = (String[])instance.getName();
      String[] types = new String[words.length];      
      for (int j = 0; j < predOutput.size(); j++) {
        types[j] = (String)predOutput.get(j);
        
      }
      
      for(int j = 0; j < words.length; j++) {
        boolean f = false;
        for(int s = 0; s < segmentStartTags.length; s++) {
          if(types[j].equals(segmentStartTags[s])) {
            f = true;
            toReturn.append("<"+segmentStartTags[s].substring(2,segmentStartTags[s].length())+">" + words[j]);
            for(int j1 = j+1; j1 < words.length && types[j1].equals(segmentContinueTags[s]); j1++) {
              toReturn.append(" " + words[j1]);
              j=j1;
            }
            toReturn.append("</"+segmentStartTags[s].substring(2,segmentStartTags[s].length())+">");
            break;
          }
        }
        if(!f)
          toReturn.append(words[j]);
        
        if(j < words.length-1)
          toReturn.append(" ");
      }
      toReturn.append("\n");
    }
    return toReturn.toString();
  }*/
  
}
