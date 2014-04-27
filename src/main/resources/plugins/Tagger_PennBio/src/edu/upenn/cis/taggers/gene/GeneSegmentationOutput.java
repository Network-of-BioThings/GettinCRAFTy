/* Copyright (C) 2004 Univ. of Pennsylvania
    This software is provided under the terms of the Common Public License,
    version 1.0, as published by http://www.opensource.org.  For further
    information, see the file `LICENSE' included with this distribution. */

package edu.upenn.cis.taggers.gene;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Vector;

import edu.umass.cs.mallet.base.fst.Transducer;
import edu.umass.cs.mallet.base.types.Instance;
import edu.umass.cs.mallet.base.types.InstanceList;
import edu.umass.cs.mallet.base.types.Sequence;
import edu.upenn.cis.taggers.Tag;
import edu.upenn.cis.taggers.TagList;

/**
 * This class should never be called directly; it exists as an auxiliary to GeneTagger.
 * @author Ryan McDonald <a href="mailto:ryantm@cis.upenn.edu">ryantm@cis.upenn.edu</a>
 * @author Kevin Lerman <a href="mailto:klerman@seas.upenn.edu">Kevin Lerman</a>
 */
public class GeneSegmentationOutput
{
  
    public TagList tag(String passedIn, Transducer crf, InstanceList testingdata) throws IOException {
	return tag(passedIn, crf, testingdata, null);
    }
  
    /**
     * This class's only method.  It tags a properly formatted String according to the CRF model
     * and returns it in XML format
     * @param data The String to tag
     * @param crf The model to use
     * @param instanceData Used to generate the tags
     * @return The XML-tagged String
     * */
    public TagList tag(String data, Transducer crf, InstanceList instanceData, Vector spans) throws IOException {
	//BufferedReader in = new BufferedReader(new StringReader(passedIn));    
	//String toReturn="";
	//String line = in.readLine(); 
	//String sentence ="";
    
	/*do {      
	  if(line!=null && line.length() > 0) {
	  sentence += line + "\n";
	  }
	  else {                
	  }
	  line = in.readLine();
	  } while(line != null);*/
	
	//System.out.println("\nProcessing Data: "+data+"\n\n");
      
	String[] tokens = data.split("\n");
      
	//System.out.println("Tokens from sentence:");
	//for(int x=0; x<tokens.length; x++) { System.out.println("TOKEN["+x+"]: "+tokens[x]); }
	//System.out.println("\n\n");
      
	Instance instance = instanceData.getInstance(instanceData.size()-1);
	Sequence input = (Sequence) instance.getData();
	Sequence predOutput = crf.viterbiPath(input).output();        
	String possibleGeneSegment = "";
	int start = -1;
	int end = -1;
	Tag currentTag = null;
	TagList tagList = new TagList();
	boolean inAGene=false;
	//Convert from BIO to XML
	for(int j=0;j<tokens.length;j++){
	    String[] features = tokens[j].split(" ");
		
	    //System.out.println("Token Features: ");
	    //for(int y=0; y<features.length; y++) {
	    //	System.out.println("FEATURE["+y+"]:"+features[y]);
	    //}
		
	    //grab the information about the token
	    possibleGeneSegment = features[0];
	    if(spans!=null && spans.get(j)!=null) {
		start = ((int[])spans.get(j))[0];
		end = ((int[])spans.get(j))[1];
	    }
      
	    if(predOutput.get(j).equals("B-GENE")){            
		if(inAGene) {
		    //must end the gene
		    //toReturn+=" </gene> ";
		    tagList.addTag(currentTag);
		    currentTag = null;
		}
		//toReturn+=" <gene> "; //either way, must start a new gene
		inAGene=true;
		currentTag = new Tag("gene");
	    }
	    else if(predOutput.get(j).equals("O")){
		if(inAGene){
		    inAGene=false;
		    //toReturn+="</gene> ";
		    tagList.addTag(currentTag);
		    currentTag = null;
		}
	    } 
	    //if it was I we're just continuing the gene
	    //toReturn+=features[0]+" ";
	    //System.out.println("Possible Gene Segment: "+possibleGeneSegment);
	    if(inAGene) {
		currentTag.addSegment(possibleGeneSegment,j);
		currentTag.updateOffset(start, end);
	    }
	}
	if(inAGene){
	    //toReturn+="</gene>";
	    tagList.addTag(currentTag);
	}      
	//return toReturn;
	return tagList;
    }  
}
