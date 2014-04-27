/*
 * MutationFinder
 * 
 * Copyright (c) 2011, The University of Sheffield.
 * 
 * This file is part of GATE (see http://gate.ac.uk/), and is free software,
 * licenced under the GNU Library General Public License, Version 3, June 2007
 * (in the distribution as file licence.html, and also available at
 * http://gate.ac.uk/gate/licence.html).
 * 
 * Mark A. Greenwood, 01/08/2011
 */

package gate.creole.mutationfinder;

import edu.uchsc.ccp.nlp.ei.mutation.Mutation;
import edu.uchsc.ccp.nlp.ei.mutation.MutationFinder;
import edu.uchsc.ccp.nlp.ei.mutation.PointMutation;
import gate.AnnotationSet;
import gate.Factory;
import gate.FeatureMap;
import gate.Resource;
import gate.creole.AbstractLanguageAnalyser;
import gate.creole.ExecutionException;
import gate.creole.metadata.CreoleParameter;
import gate.creole.metadata.CreoleResource;
import gate.creole.metadata.Optional;
import gate.creole.metadata.RunTime;
import gate.util.Files;

import java.net.URL;
import java.util.Map;
import java.util.Set;

@CreoleResource(name = "MutationFinder", comment = "GATE MutationFinder Wrapper", icon = "DNA.png", helpURL = "http://gate.ac.uk/userguide/sec:domain-creole:biomed:mutationfinder")
public class MutationFinderPR extends AbstractLanguageAnalyser {

  private MutationFinder finder = null;

  private URL regexpURL = null;

  private String annotationSetName;

  @CreoleParameter(defaultValue = "resources/regex.txt")
  public void setRegexURL(URL regexpURL) {
    this.regexpURL = regexpURL;
  }

  public URL getRegexURL() {
    return regexpURL;
  }

  @RunTime
  @Optional
  @CreoleParameter
  public void setAnnotationSetName(String annotationSetName) {
    this.annotationSetName = annotationSetName;
  }

  public String getAnnotationSetName() {
    return annotationSetName;
  }

  @Override
  public Resource init() {

    // create a new instance of MutationFinder using the supplied file
    finder = new MutationFinder(Files.fileFromURL(regexpURL));

    return this;
  }

  @Override
  public void execute() throws ExecutionException {
    String content = document.getContent().toString();
    AnnotationSet outputAS = document.getAnnotations(annotationSetName);

    try {
      Map<Mutation, Set<int[]>> mutations = finder.extractMutations(content);

      for(Map.Entry<Mutation, Set<int[]>> entry : mutations.entrySet()) {
        Mutation mutation = entry.getKey();
        Set<int[]> offsets = entry.getValue();

        for(int[] span : offsets) {
          FeatureMap params = Factory.newFeatureMap();

          if(mutation instanceof PointMutation) {
            PointMutation pm = (PointMutation)mutation;

            params.put("type", "point");
            params.put("position", pm.getPosition());
            params.put("wildType", pm.getWtResidue());
            params.put("mutant", pm.getMutResidue());
            params.put("wNm", pm.toString());
          }

          outputAS.add((long)span[0], (long)span[1], "Mutation", params);
        }
      }
    } catch(Exception e) {
      throw new ExecutionException(e);
    }
  }
}
