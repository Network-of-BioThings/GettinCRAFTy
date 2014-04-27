/* Copyright (C) 2002 Univ. of Massachusetts Amherst, Computer Science Dept.
 This file is part of "MALLET" (MAchine Learning for LanguagE Toolkit).
 http://www.cs.umass.edu/~mccallum/mallet
 This software is provided under the terms of the Common Public License,
 version 1.0, as published by http://www.opensource.org.  For further
 information, see the file `LICENSE' included with this distribution. */


/** 
 @author Ryan McDonald & Yang Jin <a href="mailto:yajin@mail.med.upenn.edu">yajin@mail.med.upenn.edu</a>
 */

//package edu.umass.cs.mallet.users.ryantm.medline;
package edu.upenn.cis.taggers.malignancy;

import java.io.IOException;
import java.net.URL;
import java.util.zip.GZIPInputStream;

import edu.umass.cs.mallet.base.fst.CRF4;
import edu.umass.cs.mallet.base.pipe.SerialPipes;
import edu.umass.cs.mallet.base.types.Instance;
import edu.umass.cs.mallet.base.types.InstanceList;
import edu.upenn.cis.taggers.LoadModelException;
import edu.upenn.cis.taggers.Model;
import edu.upenn.cis.taggers.TagList;
import edu.upenn.cis.taggers.Tagger;
import edu.upenn.cis.tokenizers.BioTokenizer;

public class MalignancyTagger implements Tagger
{
  int numEvaluations = 0;
  static int iterationsBetweenEvals = 16;
  
  private static String CAPS = "[A-Z]";
  private static String LOW = "[a-z]";
  private static String CAPSNUM = "[A-Z0-9]";
  private static String ALPHA = "[A-Za-z]";
  private static String ALPHANUM = "[A-Za-z0-9]";
  private static String PUNT = "[,\\.;:?!()]";
  private static String QUOTE = "[\"`']";
  
  //Moved from Main()
  private MalignancySegmentationEvaluator eval =
    new MalignancySegmentationEvaluator (new String[] {"B-malignancy-type"}, new String[] {"I-malignancy-type"});
  private CRF4 crf;
  private static BioTokenizer tokenizer;    
  
  
  public MalignancyTagger(String model) throws LoadModelException {
    crf = Model.loadAndRetrieveModel(model);
    tokenizer = new BioTokenizer();
  }
  
  public MalignancyTagger(URL modelURL) throws LoadModelException, IOException {
    crf = Model.loadAndRetrieveModel(new GZIPInputStream(modelURL.openStream()));
  }
  
  public TagList tag(String [] toks) throws IOException {
    
    SerialPipes p = (SerialPipes)crf.getInputPipe();    
    InstanceList allData = new InstanceList (p);        
    String inst = "";
    
    for(int i = 0; i < toks.length; i++)
      inst += toks[i] + "\tO\n";    
    Instance i = new Instance(inst,null,null,null,p);
    allData.add(i);   
    return eval.output(crf,allData);
  }
  
  public String tag (String in) throws IOException
  {            
    in = tokenizer.tokenize(in);
    SerialPipes p = (SerialPipes)crf.getInputPipe();    
    InstanceList allData = new InstanceList (p);        
    String inst = "";
    String[] toks = in.split(" ");
    for(int i = 0; i < toks.length; i++)
      inst += toks[i] + "\tO\n";    
    Instance i = new Instance(inst,null,null,null,p);
    allData.add(i);   
    return eval.output(crf,allData).toXML(toks);
  }
  
  
  public String htmlHeader(){
   return "<HTML><BODY>Normal text<BR><font color=BLUE><B>Malignancies</b></font><BR>";
 }
 
 /**
  * See {@link Tagger} for details
  * */
 public String[] xmlTags(){
   String[] t = {"<malignancy-type>"};
   return t;
 }
 
 /**
  * See {@link Tagger} for details
  * */
 public String[] medlineTags(){
   String[] t = {"MTYP"};
   return t;
 }
 
 /**
  * See {@link Tagger} for details
  * */
 public String[] htmlOpenTags(){
   String[] t = {"<B><font color=BLUE>"};
   return t;
 }
 
 /**
  * See {@link Tagger} for details
  * */
 public String[] htmlCloseTags(){
   String[] t = {"</b></font>"};
   return t;
 }
}
