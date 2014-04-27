/*
 *  OpenCalais.java
 *
 *  Copyright (c) 2009, The University of Sheffield.
 *
 *  This file is part of GATE (see http://gate.ac.uk/), and is free
 *  software, licenced under the GNU Library General Public License,
 *  Version 2, June 1991 (in the distribution as file licence.html,
 *  and also available at http://gate.ac.uk/gate/licence.html).
 *
 */



package gate.opencalais;

import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.net.URISyntaxException;
import java.io.UnsupportedEncodingException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.util.Iterator;

import gate.Annotation;
import gate.AnnotationSet;
import gate.Document;
import gate.Factory;
import gate.FeatureMap;
import gate.Gate;
import gate.LanguageAnalyser;
import gate.ProcessingResource;
import gate.Resource;
import gate.creole.ResourceInstantiationException;
import gate.creole.AbstractLanguageAnalyser;
import gate.creole.ExecutionException;
import gate.util.InvalidOffsetException;
import gate.corpora.DocumentContentImpl;

import org.codehaus.jettison.json.*;
import org.codehaus.jettison.mapped.*;

/**
 * <p>A semantic annotator using OpenCalais. OpenCalais is called
 * using its REST interface, via a http POST.</p>
 *
 * <p>This GATE Processing resource was written by participants at
 * FIG09 - the GATE Summer School 2009. A small portion of the code
 * was inspired by James Leigh, "Extracting meaning from text with
 * OpenCalias R3" at http://www.devx.com/semantic/Article/39550/1954
 * </p>
 */
public class OpenCalais extends AbstractLanguageAnalyser implements
                                                    ProcessingResource,
                                                    Serializable {

  /**
   * The OpenCalais configuraiton xml
   * 
   */
  private String paramsXMLString;


  /**
   * The AnnotationSet to which output will be added
   * 
   */
  private String outputASName;


  public void setOutputASName(String oasn) {
    this.outputASName = oasn;
  }

  public String getOutputASName() {
    return this.outputASName;
  }


  /**
   * The URL of the OpenCalais REST service
   * 
   */
  private URL openCalaisURL;

  public void setOpenCalaisURL(URL newValue) {
    openCalaisURL = newValue;
  }

  public URL getOpenCalaisURL() {
    return openCalaisURL;
  }




  /**
   * The license key for OpenCalais
   * 
   */
  private String licenseID;

  public void setLicenseID(String newValue) {
    licenseID = newValue;
  }

  public String getLicenseID() {
    return licenseID;
  }




  /**
   * The calculateRelevanceScore: Indicates whether the extracted metadata will include relevance score for each unique entity
   * 
   */
  private Boolean calculateRelevanceScore;

  public void setCalculateRelevanceScore(Boolean newValue) {
    calculateRelevanceScore = newValue;
  }

  public Boolean getCalculateRelevanceScore() {
    return calculateRelevanceScore;
  }


  /**
   * The enableMetadataType: Indicates whether output will include Generic Relation extractions (RDF) and/or SocialTags 
   *
   */
  private gate.opencalais.MetadataType enableMetadataType;

  public void setEnableMetadataType(gate.opencalais.MetadataType newValue) {
    enableMetadataType = newValue;
  }

  public gate.opencalais.MetadataType getEnableMetadataType() {
    return enableMetadataType;
  }


  /**
   * The docRDFaccessible: Indicates whether entire XML/RDF document is saved in the Calais Linked Data repository
   * 
   */
  private Boolean docRDFaccessible;

  public void setDocRDFaccessible(Boolean newValue) {
    docRDFaccessible = newValue;
  }

  public Boolean getDocRDFaccessible() {
    return docRDFaccessible;
  }


  /**
   * The allowDistribution: Indicates whether the extracted metadata can be distributed
   * 
   */
  private Boolean allowDistribution;

  public void setAllowDistribution(Boolean newValue) {
    allowDistribution = newValue;
  }

  public Boolean getAllowDistribution() {
    return allowDistribution;
  }


  /**
   * The allowSearch: Indicates whether future searches can be performed on the extracted metadata
   * 
   */
  private Boolean allowSearch;

  public void setAllowSearch(Boolean newValue) {
    allowSearch = newValue;
  }

  public Boolean getAllowSearch() {
    return allowSearch;
  }


  /**
   * The externalID: User-generated ID for the submission
   * 
   */
  private String externalID;

  public void setExternalID(String newValue) {
    externalID = newValue;
  }

  public String getExternalID() {
    return externalID;
  }


  /**
   * The submitter: Identifier for the content submitter
   * 
   */
  private String submitter;

  public void setSubmitter(String newValue) {
    submitter = newValue;
  }

  public String getSubmitter() {
    return submitter;
  }





  /**
   * Create the semantic annotator
   */
  public Resource init() throws ResourceInstantiationException {
    // sanity check parameters
    if(openCalaisURL == null) { throw new ResourceInstantiationException(
            "OpenCalais URL must be specified"); }

    if(licenseID == null || licenseID.equals("")) { throw new ResourceInstantiationException(
            "OpenCalais license ID must be specified"); }

    // Construct the params xml
    paramsXMLString  = "<c:params xmlns:c=\"http://s.opencalais.com/1/pred/\" xmlns:rdf=\"http://www.w3.org/1999/02/22-rdf-syntax-ns#\"> "
                       + "<c:processingDirectives c:contentType=\"text/txt\" c:outputFormat=\"application/json\"> "
                       + "</c:processingDirectives> ";
    // include parameters that have at least default values
    paramsXMLString += "<c:userDirectives " 
                       + "c:calculateRelevanceScore=\"" + calculateRelevanceScore
                       + "\" c:docRDFaccessible=\"" + docRDFaccessible   
                       + "\" c:allowDistribution=\"" + allowDistribution
                       + "\" c:allowSearch=\"" + allowSearch;
    // for all other parameters, check if they have been set, before including
    if (enableMetadataType != null) {
      if (enableMetadataType.equals("GenericRelations") || enableMetadataType.equals("SocialTags"))
        paramsXMLString += "\" c:enableMetadataType=\"" + enableMetadataType;
      if (enableMetadataType.equals("both"))
        paramsXMLString += "\" c:enableMetadataType=\"GenericRelations,SocialTags\"";
    }

    if (externalID != null)
      paramsXMLString += "\" c:externalID=\"" + externalID;
    
    if (submitter != null)
      paramsXMLString += "\" c:submitter=\"" + submitter;
    
    paramsXMLString += "\"> </c:userDirectives>"
                       + " <c:externalMetadata>"
                       + " </c:externalMetadata>"
                       + " </c:params>";

    return this;
  }

  public void execute() throws ExecutionException {
    Document doc = getDocument();

    //  Get the output annotation set
    AnnotationSet outputAS = null;
    if(outputASName == null || outputASName.equals("")) {
	outputAS = doc.getAnnotations();
    } else {
        outputAS = doc.getAnnotations(outputASName);
    }

    // Get the text out of the document
    String docText = ((DocumentContentImpl) doc.getContent()).toString();

    // Post it to OpenCalais
    
    BufferedReader results = null;
    try {
       results = new BufferedReader(post(docText));
    }catch (IOException ioe) {
      throw new ExecutionException(
              "Problem talking to OpenCalais service: " + openCalaisURL);
    }

    
    String firstline = "";
    try {
      firstline = results.readLine();
    } catch(Exception e){
      e.printStackTrace();
    }
    
    //If OpenCalais returns an error, it will be an invalid JSONObject
    //text, and will not begin with a { at character 1. In that case,
    //just print it, don't process it.
    if(firstline.charAt(0)!='{'){
      System.out.println(doc.getName() + ", OpenCalais returns: " + firstline);
    } else {
        
      
      // Parse the output into GATE annotations
      //Start by putting the entire output into one String
      String jsonstr = firstline;
      try{
        String nextLine = results.readLine();
        while(nextLine != null) {
          //System.out.println(nextLine);
      	  jsonstr=jsonstr + nextLine;
          nextLine = results.readLine();
        }
      } catch (IOException ioe) {
        throw new ExecutionException(
                "Problem reading output from OpenCalais");
      }
      
      //System.out.println(jsonstr+"\n\n");
      
      try {
  	    JSONObject obj = new JSONObject(jsonstr);
  	  
  	    //The doc has a bunch of top level sections. Doc etc., later entities.
  	    Iterator docit=obj.keys();
  	    while(docit.hasNext()){
  	    	String key=(String)docit.next();
  	    	//System.out.println("Top: "+key);
  	    	JSONObject thisSubObj=(JSONObject)obj.get(key);
  	    	Iterator subdocit=thisSubObj.keys();
  	    	FeatureMap fm=Factory.newFeatureMap();
  	    	//int isanent=0;
  	    	while(subdocit.hasNext()){
  		    	String key2=(String)subdocit.next();
  		    	//System.out.println("Second level: "+key2);
  		    			    		
  		    	if(key2.equals("instances")){
  		    		//If there are instances we can create annotations
  		    		JSONArray inst=(JSONArray)thisSubObj.get(key2);
  	    			String offset=null;
  	    			String length=null;
  		    		for(int k=0;k<inst.length();k++){
  		    			JSONObject instobj=(JSONObject)inst.get(k);
  		    			Iterator instit=instobj.keys();
  			    		while(instit.hasNext()){
  			    			String itkey=(String)instit.next();
  			    			//System.out.println(itkey);
  			    			if(itkey.equals("offset")){
  			    				offset=instobj.getString("offset");
  			    			} else if(itkey.equals("length")){
  			    				length=instobj.getString("length");
  			    			}
  			    		}
  			    		//Make a new copy of fm to add to the annotation.
  		    			FeatureMap fmforthis=Factory.newFeatureMap();
  			    		if(fm!=null){
  			    			fmforthis.putAll(fm);
  			    		} else {
  			    			System.out.println("Null feature map.");
  			    		}
  		    			Long start=Long.valueOf(offset);
  		    			Long len=Long.valueOf(length);
  		    			Long end=start+len;
  		    			outputAS.add(start, end, "OpenCalais", fmforthis);
  		    			offset=null;
  		    			length=null;
  		    		}
  		    	} else {
  		    		if(fm!=null){
  			    		fm.put(key2, thisSubObj.getString(key2));
  			    	} else {
  			    		System.out.println("Null feature map.");
  			    	}
  		    	}
  	    	}
      	}
  	    
      } catch (Exception e){
      	System.out.println(e);
      }
    }
  }
  

  /**
   * Post a request to the OpenCalais REST interface.
   */
  private Reader post(String text) throws IOException, UnsupportedEncodingException {
    StringBuilder sb = new StringBuilder(text.length() + 1024);
    sb.append("licenseID=").append(encode(licenseID));
    sb.append("&content=").append(encode(text));
    sb.append("&paramsXML=").append(encode(paramsXMLString));
    URLConnection connection = openCalaisURL.openConnection();
    connection.addRequestProperty("Content-Type",
      "application/x-www-form-urlencoded");
    connection.addRequestProperty("Content-Length", Integer.toString(sb.length()));
    connection.setDoOutput(true);
    OutputStream out = connection.getOutputStream();
    OutputStreamWriter writer = new OutputStreamWriter(out);
    writer.write(sb.toString());
    writer.flush();
    return new InputStreamReader(connection.getInputStream());
  }


  /** URL encode a string */
  private String encode(String s) throws UnsupportedEncodingException {
    return URLEncoder.encode(s, "UTF-8");
  }

}
