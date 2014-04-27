/* Copyright (C) 2004 Univ. of Pennsylvania
    This software is provided under the terms of the Common Public License,
    version 1.0, as published by http://www.opensource.org.  For further
    information, see the file `LICENSE' included with this distribution. */

package edu.upenn.cis.taggers.frontend;
import java.util.*;
import java.io.*;
import edu.upenn.cis.taggers.*;

/**
 * An article is a collection of field names and values, in the order they were received.
 * Methods exist to tag fields and output in MEDLINE and HTML formats.
 * @author Ryan McDonald <a href="mailto:ryantm@cis.upenn.edu">ryantm@cis.upenn.edu</a>
 * @author Kevin Lerman <a href="mailto:klerman@seas.upenn.edu">Kevin Lerman</a>
 * */
public class Article {
  public final static boolean MEDLINE=true;
  public final static boolean HTML=false;  
  private Tagger tagger;
  private ArrayList fields=new ArrayList(), values = new ArrayList();  
  private ArrayList oldFields = new ArrayList(), oldValues = new ArrayList();
  
  public Article(Tagger t){
    tagger = t;
  }
  
  /**
   * Add a field-value pair.  Duplicates allowed.
   * @param f The field
   * @param v The value
   * */
  public void add(String f, String v){
    oldFields.add(f);
    oldValues.add(v);    
    fields.add(f);
    values.add(v);
  }
  
  /**
   * Tag the value associated with that field.
   * Uses find() to get the value, so it only tags the first entry for that field
   * if multiple exist.  This is fine, so long as articles only have one title
   * field and one abstract field.  The old string will be retained for output
   * to MEDLINE files.
   * @param f The field whose value will be tagged
   * */
  public void tag(String f) throws IOException{
    String old = getValue(f);
    if(old!=null){
      values.set(find(f),tagger.tag(old));
    }
  }
  
  /**
   * Returns this article in either MEDLINE or HTML format
   * MEDLINE will use all the original strings, with additional tags
   * (specified by the tagger) for the data.  HTML will use XML and
   * additional tags (usually font colors, as specified by the individual tagger)
   * to show the tagger's work.  Some formatting information
   * is drawn from the tagger object.
   * @param b Article.MEDLINE or Article.HTML depending which output you want
   * @return This article, as either a MEDLINE entry or HTML
   * */
  public String toString(boolean b){
    String toReturn="";        
    if(b==MEDLINE){      
      String toProcess="";
      for(int x=0;x<values.size();x++){
        String thisLine="";
        thisLine=getField(x);
        while(thisLine.length()<4) thisLine+=' ';
        //Get tagging info from title and abstract to append them to the original MEDLINE entry
        if(getField(x).equals("TI") || getField(x).equals("AB")){ 
          toProcess+=getValue(x);          
          thisLine+="- "+getForMedline(getField(x)).trim()+'\n';
        }
        else{
          thisLine+="- "+getValue(x).trim()+'\n';
        }
        toReturn+=thisLine;                
      }
      toReturn+=xmlToMedline(toProcess);
      if(xmlToMedline(toProcess).length()>0) toReturn+='\n';
    }
    else{
      toReturn+="<HR>";
      String[] tags = {"PMID","TI","AB","AU","DP","PT","SO"};
      String[] label = {"PMID: ","","","Author: ","Published: ","Type: ","Reference: "};
      boolean[] bold = {false,true,false,false,false,false,false};
      boolean[] process = {false,true,true,false,false,false,false};
      //Loop through the field-value pairs and tag anybody that should be tagged
      for(int x=0;x<tags.length;x++){
        String val = getValue(tags[x]);
        if(val!=null){
          if(process[x]) val = xmlToHTML(val);
          if(bold[x]) toReturn+="<b>";
          toReturn+=label[x]+val;
          if(bold[x]) toReturn+="</b>";
          toReturn+="<BR>";
        }
      }      
    }
    return toReturn;
  }
  
  /**
   * Return the value associated with a field, or null if there isn't one
   * If it's been tagged or modified in any way, gets the modified one.
   * @param f The field whose value will be retrieved
   * @return The first value associated with that field
   * */
  public String getValue(String f){
    if(find(f)!=-1)
      return (String)(values.get(find(f)));
    else return null;
  }
  
  /**
   * Return the n'th value, or null if there isn't one
   * @param n The number value to return
   * @return The n'th value, or null if there isn't one
   * */
  private String getValue(int n){
    if(n<values.size()) return (String)(values.get(n));
    else return null;
  }
  
  /**
   * Return the n'th field, or null if there isn't one
   * @param n The number field to return
   * @return The n'th field, or null if there isn't one
   * */
  private String getField(int f){
    if(f<fields.size()) return (String)(fields.get(f));
    else return null;
  }
  
  /**
   * Find the (first) index of a field, or return -1 if it's not there
   * @param f The field
   * @return The first index of that field, or -1 if there isn't one
   * */
  private int find(String f){
    for(int x=0;x<fields.size();x++)
      if(((String)(fields.get(x))).equals(f)) return x;
    return -1;
  }
  
  /**
   * Search the input string for XML tags, and return them in MEDLINE format
   * @param in The string to search
   * @return A String consisting of the MEDLINE tags generated by the Tagger
   * */
  private String xmlToMedline(String in){    
    String[] open = tagger.xmlTags();
    String[] close = tagger.xmlTags();    
    String[] tags = tagger.medlineTags();
    for(int x=0;x<close.length;x++){
      close[x]="</"+open[x].substring(1,open[x].length());
      while (tags[x].length()<4) 
        tags[x]+=' ';
      tags[x]=tags[x]+"- ";
    }
    ArrayList[] list = {new ArrayList(), new ArrayList(), new ArrayList(), new ArrayList()};
    int found=0;
    //Do a search-and-destroy sequence until we run out of things to destroy
    do{
      found=0;
      for(int x=0;x<open.length;x++){
        //Look for the open and close tags, then extract the stuff between them.  Destroy the set, then increment found
        int start = in.indexOf(open[x]);
        int end = in.indexOf(close[x]);
        if(start!=-1 && end!=-1){
          found++;
          String value = (in.substring(start+open[x].length(),end)).trim().toLowerCase();
          boolean canI=true;
          for(int y=0;y<list[x].size() && canI;y++){
            if(((String)(list[x].get(y))).equals(value)) canI=false;
          }
          if(canI) list[x].add(value);
          found++;          
          if(end+close[x].length()<in.length())
            in = in.substring(0,start)+in.substring(end+close[x].length(),in.length());
          else
            in = in.substring(0,start);
        }
      }
    } while(found>0);
    String toReturn="";
    for(int x=0;x<list.length;x++){
      for(int n=0;n<list[x].size();n++){
        toReturn+=tags[x]+((String)(list[x].get(n)))+'\n';
      }
    }
    if(toReturn.length()>0)
      return toReturn.substring(0,toReturn.length()-1);
    else return "";
  }
  
  /**
   * Insert <font> tags as specified by the tagger object into the input string
   * wherever XML tags are found.  Return the new string.
   * @param in The input string
   * @return The input string, with HTML tags inserted as dictated by the Tagger
   * */
  private String xmlToHTML(String in){
    String[] open = tagger.xmlTags();
    String[] close = tagger.xmlTags();
    String[] openFont = tagger.htmlOpenTags();
    String[] closeFont = tagger.htmlCloseTags();
    for(int x=0;x<close.length;x++){
      close[x]="</"+open[x].substring(1,open[x].length());    
      closeFont[x]=closeFont[x]+' ';
    }
    String toReturn="";
    boolean found=false;
    do{
      found=false;
      int o = in.length()+1;
      int t = -1;      
      for(int x=0;x<open.length;x++){
        if(in.indexOf(open[x])!=-1 && in.indexOf(open[x])<o){
          o = in.indexOf(open[x]);
          t = x;
          found=true;
        }
      }
      if(found){
        //Now o is where the tag opens, and t is the type of tag we're looking for
        int c = in.indexOf(close[t]);
        String value = (in.substring(o+open[t].length(),c));
        if(o!=0) toReturn+=in.substring(0,o);
        toReturn+=open[t]+openFont[t]+value+closeFont[t]+close[t];
        if(c+close[t].length()<in.length())
          in = in.substring(c+close[t].length(),in.length());
        else
          in = "";
      }
    }while(found);
    //If there's anything leftover, transfer it.
    return toReturn+in;      
  }
  
  /**
   * Return the value associated with a field.  If the value's since been modified,
   * this will return the old version.  Returns null if not found.
   * @param f The field to search for.
   * @return The unmodified value associated with that field / null if there isn't one
   * */
  private String getForMedline(String f){
    for(int x=0;x<oldFields.size();x++){      
      if(((String)(oldFields.get(x))).equals(f)){return (String)(oldValues.get(x));}
    }    
    return null;
  }  
}