/* Copyright (C) 2004 Univ. of Pennsylvania
    This software is provided under the terms of the Common Public License,
    version 1.0, as published by http://www.opensource.org.  For further
    information, see the file `LICENSE' included with this distribution. */

package edu.upenn.cis.taggers.gene;

import java.io.IOException;
import java.io.StringReader;
import java.net.URL;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;

import edu.umass.cs.mallet.base.fst.CRF4;
import edu.umass.cs.mallet.base.pipe.SerialPipes;
import edu.umass.cs.mallet.base.pipe.iterator.LineGroupIterator;
import edu.umass.cs.mallet.base.types.InstanceList;
import edu.upenn.cis.taggers.LoadModelException;
import edu.upenn.cis.taggers.Model;
import edu.upenn.cis.taggers.TagList;
import edu.upenn.cis.taggers.Tagger;
import edu.upenn.cis.tokenizers.BioTokenizer;

/**
 * Tags genes within a body of text
 * @author Ryan McDonald <a href="mailto:ryantm@cis.upenn.edu">ryantm@cis.upenn.edu</a> 
 * @author Kevin Lerman <a href="mailto:klerman@seas.upenn.edu">klerman@seas.upenn.edu</a>
 */
public class GeneTagger implements Tagger
{
 int numEvaluations = 0;
 static int iterationsBetweenEvals = 16;
 TagList tl;

 private static String CAPS = "[A-Z]";
 private static String LOW = "[a-z]";
 private static String CAPSNUM = "[A-Z0-9]";
 private static String ALPHA = "[A-Za-z]";
 private static String ALPHANUM = "[A-Za-z0-9]";
 private static String PUNT = "[,\\.;:?!]";
 private static String QUOTE = "[\"`']";
 private static String SEQ = "[atgcu]+";
 private static String BADSUFFIX = ".*ole|.*ane|.*ate|.*ide|.*ine|.*ite|.*ol|.*ose|.*cooh|.*ar|.*ic|.*al|.*ive|.*ly|.*yl|.*ing|.*ry|.*ian|.*ent|.*ward|.*fold|.*ene|.*ory|.*ized|.*ible|.*ize|.*izes|.*ed|.*tion|.*ity|.*ure|.*ence";
 private static String GOODSUFFIX = ".*gene|.*like|.*ase|homeo.*";
 
 private CRF4 crf = null;
 private InstanceList instanceData = null;
 private SerialPipes p = null;
 private BioTokenizer tokenizer = null;
 
  /**
  * Construct a new gene tagger -- read in the model and whatnot
  * @param in The CRF4 model to use
  * */
 public GeneTagger(String model) throws LoadModelException {
   tokenizer = new BioTokenizer();
   System.out.println("Initializing Gene Tagger...");
   tl = new TagList();
   crf = Model.loadAndRetrieveModel(model);       
   System.out.println("Gene Tagger Initialized.");
 }
 
 public GeneTagger(URL modelURL) throws LoadModelException, IOException {
   System.out.println("Initializing Gene Tagger...");
   tl = new TagList();
   crf = Model.loadAndRetrieveModel(new GZIPInputStream(modelURL.openStream()));       
   System.out.println("Gene Tagger Initialized.");
 }
 
 public TagList tag(String [] tokens) throws IOException {
   p = (SerialPipes)crf.getInputPipe();
   String toPass="";
   for(int x=0;x<tokens.length;x++){
       String[] w = tokens[x].split("\n");
       for(int y = 0;y<w.length;y++){
           if(w[y].length()>0)
               toPass+=w[y]+"\tO\n";
           else
               toPass+=w[y]+'\n';
       }
   } 
   
   instanceData = new InstanceList (p);
   instanceData.add (new LineGroupIterator (new StringReader (toPass), Pattern.compile("^$"), true));
   GeneSegmentationOutput gso = new GeneSegmentationOutput();
   
   //Process string to find genes
   tl = gso.tag(toPass,crf,instanceData);
   
   return tl;   
 }
 
 /**
  * Returns XML-tagged data
  * @param in The String to tag
  * @return The tagged String
  * */
 //TODO: THIS RETURNS A STRING -  really we have a TagList! 
 public String tag (String in) throws IOException
 {
     p = (SerialPipes)crf.getInputPipe();  
     in = tokenizer.tokenize(in);
     //Program will crash if we ever have two adjacent spaces, so let's prevent that
     in = in.replaceAll("\\s+"," ");
     
     //Work data into a format usable by the model
     String[] tokens = in.split(" ");
     
     
     String toPass="";
     for(int x=0;x<tokens.length;x++){
         String[] w = tokens[x].split("\n");
         for(int y = 0;y<w.length;y++){
             if(w[y].length()>0)
                 toPass+=w[y]+"\tO\n";
             else
                 toPass+=w[y]+'\n';
         }
     } 
     
     instanceData = new InstanceList (p);
     instanceData.add (new LineGroupIterator (new StringReader (toPass), Pattern.compile("^$"), true));
     GeneSegmentationOutput gso = new GeneSegmentationOutput();
     
     //Process string to find genes
     tl = gso.tag(toPass,crf,instanceData);
     
     return tl.toXML(tokens);   
 }
 
 public TagList getTagList() {
     return tl;
 }
 
 public String htmlHeader(){
   return "<HTML><BODY>Normal text<BR><font color=RED>Genes</font><BR>";
 }
 
 /**
  * See {@link Tagger} for details
  * */
 public String[] xmlTags(){
   String[] t = {"<gene>"};
   return t;
 }
 
 /**
  * See {@link Tagger} for details
  * */
 public String[] medlineTags(){
   String[] t = {"GENE"};
   return t;
 }
 
 /**
  * See {@link Tagger} for details
  * */
 public String[] htmlOpenTags(){
   String[] t = {"<B><font color=RED>"};
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
