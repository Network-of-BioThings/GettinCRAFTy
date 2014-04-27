/* Copyright (C) 2004 Univ. of Pennsylvania
    This software is provided under the terms of the Common Public License,
    version 1.0, as published by http://www.opensource.org.  For further
    information, see the file `LICENSE' included with this distribution. */

package edu.upenn.cis.taggers.variation;

import java.io.IOException;
import java.io.ObjectInputStream;
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

/**
 * Tags variations (mutations and such) within a body of text
 * @author Kevin Lerman <a href="mailto:klerman@seas.upenn.edu">klerman@seas.upenn.edu</a>
 * @author Ryan McDonald <a href="mailto:ryantm@cis.upenn.edu">ryantm@cis.upenn.edu</a>
 */
public class VariationTagger implements Tagger
{
  int numEvaluations = 0;
  static int iterationsBetweenEvals = 16;
  TagList tl;
  
  private static String CAPS = "[A-Z]";
  private static String LOW = "[a-z]";
  private static String CAPSNUM = "[A-Z0-9]";
  private static String ALPHA = "[A-Za-z]";
  private static String ALPHANUM = "[A-Za-z0-9]";
  private static String PUNT = "[,\\.;:?!()]";
  private static String QUOTE = "[\"`']";
  
  private VariationSegmentationEvaluator eval;
  private ObjectInputStream ois;
  private CRF4 crf;
  private SerialPipes p;
  private boolean init=false;
  private static BioTokenizer tokenizer;    
  
  /**
   * Construct a variationTagger
   * @param model Filename of the model to use
   * */
  public VariationTagger(String model) throws LoadModelException {
	   System.out.println("Initializing Variation Tagger...");
	   eval = new VariationSegmentationEvaluator (new String[] {"B-type", "B-location", "B-state-original","B-state-altered"}, new String[] {"I-type", "I-location", "I-state-original","I-state-altered"});
	   tl = new TagList();
	   crf = Model.loadAndRetrieveModel(model);
	   p = (SerialPipes)crf.getInputPipe();
	   tokenizer = new BioTokenizer();
	   init=true;
	   System.out.println("Variation Tagger Initialized.");
  }
  
  public VariationTagger(URL modelURL) throws LoadModelException, IOException {
    System.out.println("Initializing Variation Tagger...");
    eval = new VariationSegmentationEvaluator (new String[] {"B-type", "B-location", "B-state-original","B-state-altered"}, new String[] {"I-type", "I-location", "I-state-original","I-state-altered"});
    tl = new TagList();
    crf = Model.loadAndRetrieveModel(new GZIPInputStream(modelURL.openStream()));
    p = (SerialPipes)crf.getInputPipe();  
    init=true;
    System.out.println("Variation Tagger Initialized.");    
  }
  
  public TagList tag(String [] toks) throws IOException {
    if(!init)
      throw new UnsupportedOperationException("This tagger has not yet been set up with a model.");
    
    InstanceList allData = new InstanceList (p);
    String inst = "";
    
    for(int i = 0; i < toks.length; i++)
      inst += toks[i] + "\tO\n";
    Instance i = new Instance(inst,null,null,null,p);
    allData.add(i);
    tl = eval.output(crf,allData);
    return tl;
  }
  
  public String tag(String line) throws IOException{
    if(!init)
      throw new UnsupportedOperationException("This tagger has not yet been set up with a model.");
    line = tokenizer.tokenize(line);
    InstanceList allData = new InstanceList (p);
    String inst = "";
    String[] toks = line.split(" ");
    for(int i = 0; i < toks.length; i++)
      inst += toks[i] + "\tO\n";
    Instance i = new Instance(inst,null,null,null,p);
    allData.add(i);
    tl = eval.output(crf,allData);
    return tl.toXML(toks);
  }
  
  public TagList getTagList() {
      return tl;
  }
  
  public String htmlHeader(){
    return "<HTML><BODY><b><h3>Legend:<br><font color=red>Type</font> - - - - <font color=blue>Location</font> - - - - <font color=\"00CC00\">Original State</font> - - - - <font color=\"AA00AA\">Altered State</font></h3></b>";
  }
  
  public String[] xmlTags(){
    String[] t = {"<type>", "<location>", "<state-original>", "<state-altered>"};
    return t;
  }
  
  public String[] medlineTags(){
    String[] t = {"VTYP","VLOC","VORG","VALT"};
    return t;
  }
  
  public String[] htmlOpenTags(){
    String[] t = {" <B><font color=RED>", " <B><font color=BLUE>", " <B><font color=\"00CC00\">", " <B><font color=\"AA00AA\">"};
    return t;
  }
  
  public String[] htmlCloseTags(){
    String[] t = {"</font></b>","</font></b>","</font></b>","</font></b>"};
    return t;
  }
}
