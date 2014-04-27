/*
 *  WriteLegacyCorefPR.java
 *
 *  Copyright (c) 1995-2012, The University of Sheffield. See the file
 *  COPYRIGHT.txt in the software or at http://gate.ac.uk/gate/COPYRIGHT.txt
 *
 *  This file is part of GATE (see http://gate.ac.uk/), and is free
 *  software, licenced under the GNU Library General Public License,
 *  Version 2, June 1991 (in the distribution as file licence.html,
 *  and also available at http://gate.ac.uk/gate/licence.html).
 *
 *  Valentin Tablan, 7 Mar 2012
 *
 *  $Id: LegacyCorefDataWriter.java 15542 2012-03-07 14:50:36Z valyt $
 */
package gate.creole.coref;

import gate.Annotation;
import gate.AnnotationSet;
import gate.creole.AbstractLanguageAnalyser;
import gate.creole.ExecutionException;
import gate.creole.metadata.CreoleParameter;
import gate.creole.metadata.CreoleResource;
import gate.creole.metadata.RunTime;
import gate.relations.Relation;
import gate.relations.RelationSet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.log4j.Logger;

/**
 * A simple PR that converts co-reference data from the new 
 * {@link Relation}s-based model to the old format (&quot;matches&quot; 
 * annotation and document features).
 */
@CreoleResource(name = "Legacy Coref Data Writer", 
  comment = "A simple PR that converts co-reference data from the " + 
  "Relations-based model to the legacy format (based on 'matches' " +
  "annotation and document features).")
public class LegacyCorefDataWriter extends AbstractLanguageAnalyser {

  private static final long serialVersionUID = -302072745365477755L;

  private static final Logger log = Logger.getLogger(LegacyCorefDataWriter.class);
  
  protected String annotationSetName;
  
  /**
   * @return the annotationSetName
   */
  public String getAnnotationSetName() {
    return annotationSetName;
  }

  /**
   * @param annotationSetName the annotationSetName to set
   */
  @CreoleParameter(comment = "The name of the annotation set for which the " +
   "data conversion should be performed. Leave blank for the default " +
   "annotation set", defaultValue = "")
  @RunTime
  public void setAnnotationSetName(String annotationSetName) {
    this.annotationSetName = annotationSetName;
  }


  /* (non-Javadoc)
   * @see gate.creole.AbstractProcessingResource#execute()
   */
  @Override
  public void execute() throws ExecutionException {
    fireStatusChanged("Generating legacy co-reference data for " + 
        document.getName());
    fireProgressChanged(1);
    
    AnnotationSet inputAnnSet = 
        (annotationSetName == null || annotationSetName.trim().length() == 0) ?
        document.getAnnotations() : document.getAnnotations(annotationSetName); 
    // get the relations set
    RelationSet relSet = RelationSet.getRelations(inputAnnSet);
    
    // now create the annotation and document features from the relations
    Map<String, List<List<Integer>>> docCorefMap;
    if(document.getFeatures().containsKey(DOCUMENT_COREF_FEATURE_NAME)){
      Object oldMap = document.getFeatures().get(DOCUMENT_COREF_FEATURE_NAME);
      if(Map.class.isAssignableFrom(oldMap.getClass())) {
        docCorefMap = (Map)oldMap;
      } else {
        log.warn("Old value for \"" + DOCUMENT_COREF_FEATURE_NAME +
          "\" document feature was invalid and has been deleted");
        docCorefMap = new HashMap<String, List<List<Integer>>>();
        document.getFeatures().put(DOCUMENT_COREF_FEATURE_NAME, docCorefMap);
      }
    } else {
      docCorefMap = new HashMap<String, List<List<Integer>>>();
      document.getFeatures().put(DOCUMENT_COREF_FEATURE_NAME, docCorefMap);
    }
    List<List<Integer>> corefChains = new ArrayList<List<Integer>>();
    docCorefMap.put(inputAnnSet.getName(), corefChains);
    
    Map<Integer, SortedSet<Integer>> ann2chain = 
        new HashMap<Integer, SortedSet<Integer>>();
    for(Relation aRelation : relSet.getRelations(Relation.COREF)){
      SortedSet<Integer> chain = ann2chain.get(aRelation.getMembers()[0]);
      if(chain == null) {
        chain = ann2chain.get(aRelation.getMembers()[1]);
        if(chain == null) {
          // neither anns were known
          chain = new TreeSet<Integer>();
          chain.add(aRelation.getMembers()[0]);
          ann2chain.put(aRelation.getMembers()[0], chain);
          chain.add(aRelation.getMembers()[1]);
          ann2chain.put(aRelation.getMembers()[1], chain);
        } else {
          // member 1 already known
          chain.add(aRelation.getMembers()[0]);
          ann2chain.put(aRelation.getMembers()[0], chain);
        }
      } else {
        // member 0 already known
        chain.add(aRelation.getMembers()[1]);
        ann2chain.put(aRelation.getMembers()[1], chain);
      }
    }
    
    Set<SortedSet<Integer>> chains = new HashSet<SortedSet<Integer>>(
        ann2chain.values());
    for(SortedSet<Integer> aChain : chains) {
      if(aChain.size() > 1) {
        List<Integer> annIds = new ArrayList<Integer>();
        for(int annId : aChain) {
          annIds.add(annId);
          Annotation ann = inputAnnSet.get(annId);
          if(ann != null) {
            ann.getFeatures().put(ANNOTATION_COREF_FEATURE_NAME, annIds);  
          } else {
            log.warn("Relation refers non-existant annotation ID " + annId + ".");
          }
          
        }
        corefChains.add(annIds);
      }
    }
    fireStatusChanged("");
    fireProgressChanged(100);
    fireProcessFinished();
  }
}
