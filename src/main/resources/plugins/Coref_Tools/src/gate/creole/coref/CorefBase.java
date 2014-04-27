/*
 *  OrthoRef.java
 *
 *  Copyright (c) 1995-2012, The University of Sheffield. See the file
 *  COPYRIGHT.txt in the software or at http://gate.ac.uk/gate/COPYRIGHT.txt
 *
 *  This file is part of GATE (see http://gate.ac.uk/), and is free
 *  software, licenced under the GNU Library General Public License,
 *  Version 2, June 1991 (in the distribution as file licence.html,
 *  and also available at http://gate.ac.uk/gate/licence.html).
 *
 *  Valentin Tablan 18 Jan 2012
 *  
 *  $Id: CorefBase.java 16094 2012-09-26 10:11:33Z ian_roberts $
 */
package gate.creole.coref;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.log4j.Logger;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.core.ReferenceByIdMarshallingStrategy;
import com.thoughtworks.xstream.io.xml.StaxDriver;

import gate.Annotation;
import gate.AnnotationSet;
import gate.Gate;
import gate.Resource;
import gate.Utils;
import gate.creole.AbstractLanguageAnalyser;
import gate.creole.ExecutionException;
import gate.creole.ResourceInstantiationException;
import gate.creole.coref.matchers.AbstractMatcher;
import gate.creole.coref.matchers.CompoundMatcher;
import gate.creole.coref.taggers.AbstractTagger;
import gate.creole.coref.taggers.Collate;
import gate.creole.coref.taggers.FixedTags;
import gate.creole.metadata.CreoleParameter;
import gate.creole.metadata.CreoleResource;
import gate.creole.metadata.RunTime;
import gate.relations.Relation;
import gate.relations.RelationSet;

@CreoleResource
/**
 * Base class for coreferencers. This class has no internal state, so it safe to
 * use in a multi-threaded environment. The same may not be true about 
 * subclasses: check their own documentation first! 
 */
public abstract class CorefBase extends AbstractLanguageAnalyser {
  
  /**
   * A very simple implementation of a immutable String pair to be used as 
   * hashmap keys.
   */
  protected static class StringPair {
    private String[] elements;

    private final int hashcode;
    
    public StringPair(String... elements) {
      this.elements = elements;
      this.hashcode = Arrays.hashCode(this.elements);
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
      return hashcode;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
      if(this == obj) return true;
      if(obj == null) return false;
      if(this.hashCode() != obj.hashCode()) return false;
      if(!(obj instanceof StringPair)) return false;
      StringPair other = (StringPair)obj;
      return Arrays.equals(this.elements, other.elements);
    }
  }
  
  /**
   * 
   */
  private static final long serialVersionUID = 4534496318441250057L;


  private String annotationSetName;
  
  private Integer maxLookBehind;
  
  private Config config;
  
  private URL configFileUrl;
  
  
  protected static final Logger log = Logger.getLogger(CorefBase.class);

  public String getAnnotationSetName() {
    return annotationSetName;
  }

  @CreoleParameter(comment="The name of the annotation set to be processed", 
      defaultValue="")
  @RunTime
  public void setAnnotationSetName(String annotationSetName) {
    this.annotationSetName = annotationSetName;
  }

  
  /**
   * @return the maxLookBehind
   */
  public Integer getMaxLookBehind() {
    return maxLookBehind;
  }

  /**
   * Set the maximum look behind: the maximum allowed distance between an 
   * anaphor and its antecedent (implemented as the maximum number of anaphors 
   * allowed to skip over).  
   * 
   * @param maxLookBehind the maxLookBehind to set
   */
  @CreoleParameter(comment = "The maximum number of anaphor the system is " +
  		"allowed to skip over when looking for an antecedent. Use a negative " +
  		"value for infinite look-behind (not recommended).",  
  		defaultValue = "10")
  @RunTime
  public void setMaxLookBehind(Integer maxLookBehind) {
    this.maxLookBehind = maxLookBehind;
  }

  /**
   * @return the configFileUrl
   */
  public URL getConfigFileUrl() {
    return configFileUrl;
  }

  /**
   * @param configFileUrl the configFileUrl to set
   */
  @CreoleParameter(comment = "The URL for the configuration file.", 
      suffixes = ".coref.xml" )
  public void setConfigFileUrl(URL configFileUrl) {
    this.configFileUrl = configFileUrl;
  }
  
  
  protected static XStream getXstream(){
    XStream xstream = new XStream(new StaxDriver());
    
    xstream.setMarshallingStrategy(new ReferenceByIdMarshallingStrategy());
    
    xstream.setClassLoader(Gate.getClassLoader());
    xstream.alias("coref.Config", Config.class);
    xstream.aliasPackage("default", "gate.creole.coref");
    xstream.useAttributeFor(AbstractTagger.class, "annotationType");
    xstream.useAttributeFor(AbstractMatcher.class, "annotationType");
    xstream.useAttributeFor(AbstractMatcher.class, "antecedentType");
    
    xstream.useAttributeFor(gate.creole.coref.taggers.Alias.class, "aliasFile");
    xstream.useAttributeFor(gate.creole.coref.taggers.Alias.class, "encoding");
    
    xstream.useAttributeFor(gate.creole.coref.matchers.Alias.class, "aliasFile");
    xstream.useAttributeFor(gate.creole.coref.matchers.Alias.class, "encoding");
    
    xstream.addImplicitCollection(Collate.class, "subTaggers");
    xstream.addImplicitCollection(CompoundMatcher.class, "subMatchers");
    xstream.addImplicitCollection(FixedTags.class, "tags");
    return xstream;
  }

  @Override
  public Resource init() throws ResourceInstantiationException {
    XStream xstream = getXstream();
    if(configFileUrl == null) throw new ResourceInstantiationException(
      "No value provided for required \"configFileUrl\" parameter.");
    config = (Config) xstream.fromXML(configFileUrl);
    for(Tagger aTagger : config.getTaggers()) aTagger.init(this);
    for(Matcher aMatcher : config.getMatchers()) aMatcher.init(this);
    return super.init();
  }

  /**
   * Gets the {@link RelationSet} used to store the data produced by this 
   * co-referencer.
   * @return the {@link RelationSet} used during the {@link #execute()} call.
   */
  public RelationSet getRelationSet() {
    return RelationSet.getRelations(
        (annotationSetName == null || annotationSetName.trim().length() == 0) ?
        document.getAnnotations() : document.getAnnotations(annotationSetName));
  }
  
  @Override
  public void execute() throws ExecutionException {
    interrupted = false;
    fireStatusChanged("Running co-reference on " + document.getName());
    fireProgressChanged(1);
    // prepare
    Set<String> anaphorAnnotationTypes = new HashSet<String>();
    Map<String, Set<Tagger>> taggersByType = new HashMap<String, Set<Tagger>>();
    for(Tagger aTagger : config.getTaggers()) {
      String annType = aTagger.getAnnotationType();
      anaphorAnnotationTypes.add(annType);
      Set<Tagger> taggers = taggersByType.get(annType);
      if(taggers == null) {
        taggers = new HashSet<Tagger>();
        taggersByType.put(annType, taggers);
      }
      taggers.add(aTagger);
    }
    Map<StringPair, Set<Matcher>> matchersByType = new HashMap<StringPair, Set<Matcher>>();
    for(Matcher aMatcher : config.getMatchers()) {
      StringPair key = new StringPair(aMatcher.getAnnotationType(), 
          aMatcher.getAntecedentType()); 
      Set<Matcher> matchers = matchersByType.get(key);
      if(matchers == null) {
        matchers = new HashSet<Matcher>();
        matchersByType.put(key, matchers);
      }
      matchers.add(aMatcher);
    }
    
    //collect all candidate anaphors
    AnnotationSet inputAnnSet = 
        (annotationSetName == null || annotationSetName.trim().length() == 0) ?
        document.getAnnotations() : document.getAnnotations(annotationSetName); 
    List<Annotation> anaphorsLst = new ArrayList<Annotation>(
        inputAnnSet.get(anaphorAnnotationTypes));
    Collections.sort(anaphorsLst, Utils.OFFSET_COMPARATOR);
    Annotation[] anaphors = new Annotation[anaphorsLst.size()];
    anaphors = anaphorsLst.toArray(anaphors);
    
    // get the relations set
    RelationSet relSet = getRelationSet();
    
    // A 'bucket' is a sorted set of annotation indexes that have the same tag. 
    // The set is sorted in reverse order, so that the annotations with higher
    // index (i.e later position in the document) are first.
    Map<String, SortedSet<Integer>> tag2bucket = 
        new HashMap<String, SortedSet<Integer>>(anaphors.length * 3);
    for(int annId = 0; annId < anaphors.length; annId++) {
      // get all tags for the current anaphor
      Set<String> tags = new HashSet<String>();
      for(Tagger tagger : taggersByType.get(anaphors[annId].getType())){
        tags.addAll(tagger.tag(anaphors, annId, this));
      }
      // the IDs for all the antecendents that match 
      SortedSet<Integer> matchingAntecedents = new TreeSet<Integer>(
          Collections.reverseOrder());
      for(String tag : tags) {
        SortedSet<Integer> aBucket = tag2bucket.get(tag);
        if(aBucket != null) {
          // perform matching
          antecedents:for(int antecedentId : aBucket) {
            // buckets are sorted in inverse ID order, 
            // so the closest antecedent is tried first
            if(maxLookBehind > 0 && 
               annId - antecedentId > maxLookBehind) break antecedents;
            Set<Matcher> matchers = matchersByType.get(new StringPair(
                anaphors[annId].getType(), anaphors[antecedentId].getType()));
            if(matchers != null) {
              for(Matcher matcher : matchers) {
                if(matcher.matches(anaphors, antecedentId, annId, this)) {
                  // add the potential match
                  matchingAntecedents.add(antecedentId);
                  // and stop looking for more matches in this bucket
                  break antecedents;
                }
              } 
            }
          }            
        } else {
          // no bucket for this tag: create a new one
          aBucket = new TreeSet<Integer>(Collections.reverseOrder());
          tag2bucket.put(tag, aBucket);
        }
        // add the new annotation to the bucket
        aBucket.add(annId);
      }
      if(matchingAntecedents.size() > 0) {
        int antecedentId = matchingAntecedents.first();
        // add the relation
        relSet.addRelation(Relation.COREF, anaphors[antecedentId].getId(), 
            anaphors[annId].getId());
      }
      if(annId % 20 == 0) {
        // every 20 annotations we report progress
        fireProgressChanged(100 * annId / anaphors.length);
        // and check for interruptions
        if(isInterrupted()) {
          throw new ExecutionException("Execution of this was interrupted.");
        }
      }
    }
    fireStatusChanged("");
    fireProgressChanged(100);
    fireProcessFinished();
  }
}
