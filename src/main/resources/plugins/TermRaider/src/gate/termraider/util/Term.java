/*
 *  Copyright (c) 2010--2012, The University of Sheffield. See the file
 *  COPYRIGHT.txt in the software or at http://gate.ac.uk/gate/COPYRIGHT.txt
 *
 *  This file is part of GATE (see http://gate.ac.uk/), and is free
 *  software, licenced under the GNU Library General Public License,
 *  Version 2, June 1991 (in the distribution as file licence.html,
 *  and also available at http://gate.ac.uk/gate/licence.html).
 *
 *  $Id: Term.java 16336 2012-11-27 14:01:14Z adamfunk $
 */
package gate.termraider.util;

import gate.Annotation;
import gate.Document;
import gate.FeatureMap;

import java.io.Serializable;


public class Term  implements Comparable<Term>, Serializable {
  
  private static final long serialVersionUID = -4849144989013687570L;
  
  private String termString, languageCode, type;
  private int hashCode;
  private String toString;
  

  public Term(String termString, String languageCode, String type) {
    this.termString = termString;
    this.languageCode = languageCode;
    this.type = type;
    this.setup();
  }

  
  public  Term(Annotation annotation, Document document, String languageFeature,
      String stringFeature) {
    this.type = annotation.getType();
    this.termString = Term.getFeatureOrString(document, annotation, stringFeature);
    this.languageCode = Term.getLanguage(annotation, languageFeature);
    this.setup();
  }


  private void setup() {
    if (languageCode == null) {
      languageCode = "";
    }

    hashCode = termString.hashCode() + languageCode.hashCode() + type.hashCode();

    if (languageCode.isEmpty()) {
      toString = termString + " (" + type + ")";
    }
    else {
      toString = termString + " (" + languageCode + "," + type + ")";
    }
  }
  

  public String toString() {
    return toString;
  }
  
  public String getTermString() {
    return this.termString;
  }
  
  public String getLanguageCode() {
    return this.languageCode;
  }
  
  public String getType() {
    return this.type;
  }
  
  
  public boolean equals(Object other) {
    return (other instanceof Term) && 
      this.termString.equals(((Term) other).termString) &&
      this.languageCode.equals(((Term) other).languageCode) &&
      this.type.equals(((Term) other).type);
  }
  
  public int hashCode() {
    return hashCode;
  }
  
  
  /**
   * This is used for alphabetical sorting.  The Term instance
   * does not know what its score is.
   */
  public int compareTo(Term other)  {
    int comp = this.getTermString().compareTo(other.getTermString());
    if (comp != 0) {
      return comp;
    }

    comp = this.getLanguageCode().compareTo(other.getLanguageCode());
    if (comp != 0) {
      return comp;
    }
    
    comp = this.getType().compareTo(other.getType());
    return comp;
  }
  
  
  
  public static String getLanguage(Annotation annotation, String languageFeature) {
    String language = "";
    if (annotation.getFeatures().containsKey(languageFeature)) {
      language = annotation.getFeatures().get(languageFeature).toString();
    }
    return language;
  }

  
  public static String getFeatureOrString(Document document, Annotation annotation, String key) {
    FeatureMap fm = annotation.getFeatures();
    if (fm.containsKey(key)) {
      return fm.get(key).toString();
    }
    // implied else
    return gate.Utils.cleanStringFor(document, annotation);
  }
  
}
