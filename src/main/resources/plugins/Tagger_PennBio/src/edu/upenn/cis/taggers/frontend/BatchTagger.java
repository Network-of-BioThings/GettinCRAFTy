/* Copyright (C) 2004 Univ. of Pennsylvania
 This software is provided under the terms of the Common Public License,
 version 1.0, as published by http://www.opensource.org.  For further
 information, see the file `LICENSE' included with this distribution. */

package edu.upenn.cis.taggers.frontend;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import edu.upenn.cis.taggers.LoadModelException;
import edu.upenn.cis.taggers.Tagger;
import edu.upenn.cis.taggers.gene.GeneTagger;
import edu.upenn.cis.taggers.malignancy.MalignancyTagger;
import edu.upenn.cis.taggers.variation.VariationTagger;

/**
 * Facilitates tagging of a batch of MEDLINE articles
 * @author Kevin Lerman <a href="mailto:klerman@seas.upenn.edu">klerman@seas.upenn.edu</a>
 * @author Ryan McDonald <a href="mailto:ryantm@cis.upenn.edu">ryantm@cis.upenn.edu</a>
 * */
public class BatchTagger{
  
  /**
   * Loads the specified tagger with the specified model, and passes the input file through it to be tagged.
   * Outputs MEDLINE and HTML to the output mask.txt and .html.
   * Usage: BatchTagger [tagger] [model] [input] [output mask]
   * */
  public static void main(String[] args) throws IOException, ClassNotFoundException{
    String usageHelp = "Usage: BatchTagger <tagger> <model> <input> <output mask>";
    String taggerHelp = "Taggers supported:\nvar -- Variation Tagger\ngene -- Gene Tagger\nmal -- Malignancy Tagger";
    if(args.length!=4){System.out.println(usageHelp+'\n'+taggerHelp);return;}
    File source = new File(args[2]);
    if(!source.exists() || !source.canRead()) {
        System.err.println("Can't read source information: "+source.getAbsolutePath());
        return;
    }
    if(args[3].indexOf("/") < 0)
	args[3] = "./"+args[3];
    File output = new File(args[3].substring(0,args[3].lastIndexOf("/")));
    if(!output.exists() || !output.canWrite()) {
        System.err.println("Can't write to ouput location: "+args[3]+" ("+output.getAbsolutePath()+")");
        return;
    }
    BufferedReader in = new BufferedReader(new FileReader(source));
    Tagger tagger = null;
    try {
	    if(args[0].toLowerCase().equals("var")) tagger = new VariationTagger(args[1]); 
	    else if(args[0].toLowerCase().equals("gene")) tagger = new GeneTagger(args[1]);
	    else if(args[0].toLowerCase().equals("mal")) tagger = new MalignancyTagger(args[1]);
	    else{System.out.println(usageHelp+'\n'+taggerHelp);return;}
    } catch(LoadModelException lme) {
        lme.printStackTrace();
        System.err.println("Exception Loading Model: "+lme.getMessage());
        return;
    }
    Article art=null; 
    String thisField="";
    String thisValue="";
    String thisLine = in.readLine();
    if(thisLine.length()<4){
      System.out.println("Invalid input file: No blank lines permitted before the first article");
      return;
    }
    BufferedWriter outHTML = new BufferedWriter(new FileWriter(args[3]+".html"));
    outHTML.write(tagger.htmlHeader());
    BufferedWriter outMEDL = new BufferedWriter(new FileWriter(args[3]+".txt"));
    if(!thisLine.substring(0,4).equals("PMID")){
      //These aren't articles, it's just a block of text.
      String myAbstract = "";
      while(thisLine!=null){
        myAbstract+=thisLine;
        thisLine = in.readLine();
      }
      Article a = new Article(tagger);
      a.add("PMID","000000000000");
      a.add("TI","Test Article");
      a.add("AB",myAbstract);
      a.add("AU","Test");
      a.add("DP","Test");
      a.add("PT","Test");
      a.add("SO","Test");
      outputArticle(a,outMEDL,outHTML);
      outMEDL.close();
      outHTML.close();
      return;
    }
    int artNum=0;
    while(thisLine!=null){
      if(thisLine.length()>0){
        if(thisLine.charAt(0)!=' '){ //This is a new field
          if(thisLine.substring(0,4).equals("PMID")){ //This is a new article     
            if(thisValue!=null && thisValue.length()>0){
              art.add(thisField,thisValue);
              thisValue="";
            }
            artNum++;
            if(artNum%25==0) System.out.println(artNum);
            if(art!=null){ 
              outputArticle(art,outMEDL,outHTML);
            }
            art = new Article(tagger);
          }
          //All new fields will run this stuff
          if(thisValue.length()>0){art.add(thisField,thisValue);thisValue="";}            
          thisField=thisLine.substring(0,4).trim();
          thisValue=thisLine.substring(6,thisLine.length()).trim();          
        }
        else{ //This is not a new field
          thisLine=thisLine.trim();
          thisValue+=" "+thisLine;         
        }
      }
      thisLine = in.readLine();
    }    
    //This is stuff to process the last line/article/etc
    if(thisValue.length()>0){art.add(thisField,thisValue);thisValue="";}            
    if(art!=null){ 
      outputArticle(art,outMEDL,outHTML);      
    }
    //End of code to process last thing
  }
  
  /**
   * Tags the right fields and outputs this article in both MEDLINE and HTML format
   * @param art The article to process
   * @param outMEDL The MEDLINE file's BufferedWriter
   * @param outHTML the HTML file's BufferedWriter
   * */
  private static void outputArticle(Article art, BufferedWriter outMEDL, BufferedWriter outHTML) throws IOException{
      art.tag("AB");
      art.tag("TI");
      outMEDL.write(art.toString(Article.MEDLINE)+"\n");
      outHTML.write(art.toString(Article.HTML)+"\n");  
      outMEDL.flush();
      outHTML.flush();
  }
  
}
