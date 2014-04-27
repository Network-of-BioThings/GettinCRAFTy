/*
 * AbstractTagger
 * 
 * Copyright (c) 2011, The University of Sheffield.
 * 
 * This file is part of GATE (see http://gate.ac.uk/), and is free software,
 * licenced under the GNU Library General Public License, Version 3, June 2007
 * (in the distribution as file licence.html, and also available at
 * http://gate.ac.uk/gate/licence.html).
 * 
 * Mark A. Greenwood, 02/06/2011
 */
package gate.creole.pennbio;

import edu.upenn.cis.taggers.Tag;
import edu.upenn.cis.taggers.TagList;
import edu.upenn.cis.taggers.Tagger;
import edu.upenn.cis.taggers.gene.GeneTagger;
import edu.upenn.cis.taggers.malignancy.MalignancyTagger;
import edu.upenn.cis.taggers.variation.VariationTagger;
import gate.Annotation;
import gate.AnnotationSet;
import gate.Factory;
import gate.Resource;
import gate.creole.ANNIEConstants;
import gate.creole.AbstractLanguageAnalyser;
import gate.creole.ExecutionException;
import gate.creole.ResourceInstantiationException;
import gate.creole.metadata.CreoleParameter;
import gate.creole.metadata.Optional;
import gate.creole.metadata.RunTime;
import gate.util.OffsetComparator;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public abstract class AbstractTagger extends AbstractLanguageAnalyser {

  protected String type, inputASName, outputASName;

  protected Tagger tagger;

  protected URL modelURL;
  
  public URL getModelURL() {
    return modelURL;
  }
  
  public String getInputASName() {
    return inputASName;
  }
  
  @RunTime
  @Optional
  @CreoleParameter
  public void setInputASName(String inputASName) {
    this.inputASName = inputASName;
  }
  
  public String getOutputASName() {
    return outputASName;
  }
  
  @RunTime
  @Optional
  @CreoleParameter
  public void setOutputASName(String outputASName) {
    this.outputASName = outputASName;
  }
  
  public abstract void setModelURL(URL modelURL);

  public Resource init() throws ResourceInstantiationException {
    if(type == null)
      throw new ResourceInstantiationException("Tagger type must be specified");    

    try {
      if(type.equals("var"))
        tagger = new VariationTagger(modelURL);
      else if(type.equals("gene"))
        tagger = new GeneTagger(modelURL);
      else if(type.equals("mal"))
        tagger = new MalignancyTagger(modelURL);
    } catch(Exception e) {
      throw new ResourceInstantiationException("Unable to load model", e);
    }

    if(tagger == null)
      throw new ResourceInstantiationException("unknown tagger type: " + type);

    return this;
  }

  public void execute() throws ExecutionException {
    
    AnnotationSet outputAS = document.getAnnotations(outputASName);
    
    List<Annotation> tokens = new ArrayList<Annotation>(document.getAnnotations(inputASName).get(ANNIEConstants.TOKEN_ANNOTATION_TYPE));
    Collections.sort(tokens, new OffsetComparator());
    
    String[] strings = new String[tokens.size()];
    
    for (int i = 0 ; i < tokens.size() ; ++i) {
      strings[i] = (String)tokens.get(i).getFeatures().get("string");
    }
    
    try {
      TagList tags  = tagger.tag(strings);
      
      Iterator<Tag> it = tags.iterator();
      while(it.hasNext()) {
        
        Tag tag = it.next();
        
        outputAS.add(tokens.get(tag.getTokenStartIndex()).getStartNode().getOffset(),tokens.get(tag.getTokenEndIndex()).getEndNode().getOffset(),tag.getTagname(),Factory.newFeatureMap());
      }
    } catch(Exception ioe) {
      throw new ExecutionException("Tagger Failed", ioe);
    }
  }
}
