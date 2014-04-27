/*
 *  Copyright (c) 2010--2012, The University of Sheffield. See the file
 *  COPYRIGHT.txt in the software or at http://gate.ac.uk/gate/COPYRIGHT.txt
 *
 *  This file is part of GATE (see http://gate.ac.uk/), and is free
 *  software, licenced under the GNU Library General Public License,
 *  Version 2, June 1991 (in the distribution as file licence.html,
 *  and also available at http://gate.ac.uk/gate/licence.html).
 *
 *  $Id: AbstractBank.java 16334 2012-11-27 11:13:41Z adamfunk $
 */

package gate.termraider.util;

import gate.*;
import gate.creole.AbstractLanguageResource;
import gate.creole.metadata.CreoleParameter;
import gate.util.GateException;
import java.io.File;
import org.apache.commons.lang.StringUtils;
import java.util.*;


/**
 * A thing that has a score name, can be saved as CSV, and 
 * can be used to generate a SliderPanel (which needs 
 * min & max scores).
 */
public abstract class AbstractBank extends AbstractLanguageResource {
  private static final long serialVersionUID = -9168657973312733783L;

  protected Set<String> languages, types;
  
  public abstract Double getMinScore();
  
  public abstract Double getMaxScore();
  
  public abstract void saveAsCsv(double threshold, File file)
    throws GateException;

  public abstract void saveAsCsv(File file)
    throws GateException;
  
  public Set<String> getLanguages() {
    return this.languages;
  }
  
  public Set<String> getTypes() {
    return this.types;
  }

  
  public String shortScoreDescription() {
    String result = "";
    if (this.scoreProperty != null) {
      result = this.scoreProperty;
      if (result.endsWith("Score")) {
        result = StringUtils.substring(result, 0, -5);
      }
    }
    return result;
  }
  
  
  
  public Term makeTerm(Annotation annotation, Document document) {
    return new Term(annotation, document, 
            this.languageFeature, this.inputAnnotationFeature);
  }

  
  /* CREOLE */
  
  protected String scoreProperty;
  protected String languageFeature;
  protected String inputAnnotationFeature;
  protected Set<Corpus> corpora;




  /* Default value is overridden in the implementations   */
  @CreoleParameter(comment = "name of ontology score property",
          defaultValue = "score")
  public void setScoreProperty(String name) {
    this.scoreProperty = name;
  }

  public String getScoreProperty() {
    return this.scoreProperty;
  }
  
  
  @CreoleParameter(comment = "language feature on term candidates",
          defaultValue = "lang")
  public void setLanguageFeature(String name) {
    this.languageFeature = name;
  }
  public String getLanguageFeature() {
    return this.languageFeature;
  }
  
  
  @CreoleParameter(comment = "input annotation feature",
          defaultValue = "canonical")
  public void setInputAnnotationFeature(String name) {
    this.inputAnnotationFeature = name;
  }
  public String getInputAnnotationFeature() {
    return this.inputAnnotationFeature;
  }
  
  @CreoleParameter(comment = "Processed corpora to analyse for pairs of terms")
  public void setCorpora(Set<Corpus> corpora) {
    this.corpora = corpora;
  }

  public Set<Corpus> getCorpora() {
    return this.corpora;
  }
  
}
