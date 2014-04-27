/*
 * NormaGene
 * 
 * Copyright (c) 2011, The University of Sheffield.
 * 
 * This file is part of GATE (see http://gate.ac.uk/), and is free software,
 * licenced under the GNU Library General Public License, Version 3, June 2007
 * (in the distribution as file licence.html, and also available at
 * http://gate.ac.uk/gate/licence.html).
 * 
 * Mark A. Greenwood, 05/08/2011
 */

package gate.creole.normagene;

import gate.AnnotationSet;
import gate.Factory;
import gate.Resource;
import gate.creole.AbstractLanguageAnalyser;
import gate.creole.ExecutionException;
import gate.creole.metadata.CreoleParameter;
import gate.creole.metadata.CreoleResource;
import gate.creole.metadata.Optional;
import gate.creole.metadata.RunTime;
import genes.models.ws.GeneTools;
import genes.models.ws.GeneToolsService;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@CreoleResource(name = "NormaGene Tagger", icon = "DNA.png")
public class NormaGene extends AbstractLanguageAnalyser {

  private static Pattern GENE_PATTERN = Pattern
          .compile("<gene start='([0-9]+)' end='([0-9]+)'>");

  private GeneTools port;

  private Double threshold;

  private String annotationSetName;

  public Resource init() {
    GeneToolsService service = new genes.models.ws.GeneToolsService();
    port = service.getGeneToolsPort();
    return this;
  }

  @RunTime
  @CreoleParameter(defaultValue = "0.6")
  public void setThreshold(Double threshold) {
    this.threshold = threshold;
  }

  public Double getThreshold() {
    return threshold;
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

  public void execute() throws ExecutionException {
    AnnotationSet outputAS = document.getAnnotations(annotationSetName);

    try {
      String result =
              port.getGenesTagger(document.getContent().toString(), threshold);

      Matcher m = GENE_PATTERN.matcher(result);
      while(m.find()) {
        outputAS.add(Long.valueOf(m.group(1)), Long.valueOf(m.group(2)),
                "Gene", Factory.newFeatureMap());
      }

      // The following will run the normalizer but there doesn't seem to be
      // anyway of matching the output back to the tagged genes
      /*
       * MyMap map = port.getBioCreativeResults(document.getContent().toString(), false, threshold);
       * 
       * for (A a : map.getA()) {
       *   System.out.println(a.getKey()+"==>"+a.getValue());
       * }
       */

    } catch(Exception ex) {
      throw new ExecutionException(ex);
    }

  }
}
