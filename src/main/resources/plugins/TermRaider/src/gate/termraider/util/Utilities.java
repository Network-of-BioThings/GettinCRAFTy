/*
 *  Copyright (c) 2008--2012, The University of Sheffield. See the file
 *  COPYRIGHT.txt in the software or at http://gate.ac.uk/gate/COPYRIGHT.txt
 *
 *  This file is part of GATE (see http://gate.ac.uk/), and is free
 *  software, licenced under the GNU Library General Public License,
 *  Version 2, June 1991 (in the distribution as file licence.html,
 *  and also available at http://gate.ac.uk/gate/licence.html).
 *
 *  $Id: Utilities.java 16320 2012-11-23 13:56:00Z adamfunk $
 */
package gate.termraider.util;

import gate.*;
import gate.creole.ANNIEConstants;
import java.io.*;
import java.net.*;
import java.util.*;
import gate.termraider.bank.*;


public class Utilities implements ANNIEConstants {

  public static final String EXTENSION_CSV = "csv";
  public static final String EXTENSION_RDF = "rdf";

  private static double log10of2;
  
  static {
    log10of2 = Math.log10(2.0);
  }


  public static double meanDoubleList(List<Double> list) {
    if (list.isEmpty()) {
      return 0.0;
    }
    // implied else
    double total = 0.0;
    for (Double item : list) {
      total += item;
    }
    return total / ((double) list.size());
  }
  
  
  public static double normalizeScore(double score) {
    double norm = 1.0 - 1.0 / (1.0 + Math.log10(1.0 + score));
    return (double) (100.0F * norm);
  }

  

  public static Double convertToDouble(Object x) {
    if (x instanceof Number) {
      return ((Number) x).doubleValue();
    }
    
    return Double.parseDouble(x.toString()) ;
  }

  
  /**
   * Suitable for embedding in URIs.
   */
  public static String veryCleanString(String input) {
    String clean = input.trim();
    return clean.replaceAll("[^\\p{Alnum}\\p{Lu}\\p{Ll}]+", "_");
  }

  
  
  public static String generateID(String prefix, String suffix) {
    return prefix + java.util.UUID.randomUUID().toString() + suffix;
  }

  
  public static URL getUrlInJar(AbstractTermbank termbank, String filename) {
    ClassLoader cl = termbank.getClass().getClassLoader();
    return cl.getResource(filename);
  }
  
  public static List<String> keysAsStrings(FeatureMap fm) {
    List<String> result = new ArrayList<String>();
    if (fm != null) {
      Set<?> keys = fm.keySet();
      for (Object key : keys) {
        result.add(key.toString());
      }
    }
    return result;
  }


  public static List<String> valuesAsStrings(FeatureMap fm) {
    List<String> result = new ArrayList<String>();
    if (fm != null) {
      for (Object key : fm.keySet()) {
        result.add(fm.get(key).toString());
      }
    }
    return result;
  }
  
  
  public static void setCanonicalFromLemma(Annotation token, Document doc, String lemmaFeatureName) {
    String canonical = getCanonicalFromLemma(token, doc, lemmaFeatureName);
    token.getFeatures().put("canonical", canonical);
  }

  
  public static String getCanonicalFromLemma(Annotation token, Document doc, String lemmaFeatureName) {
    FeatureMap fm = token.getFeatures();
    String canonical = "";
    if (fm.containsKey(lemmaFeatureName)) {
      canonical = fm.get(lemmaFeatureName).toString().toLowerCase();
    }

    if (canonical.equals("") || canonical.equals("<unknown>")) {
      if (fm.containsKey(TOKEN_STRING_FEATURE_NAME)) {
        canonical = fm.get(TOKEN_STRING_FEATURE_NAME).toString().toLowerCase();
      }
      else {
        canonical = gate.Utils.stringFor(doc, token).toLowerCase();
      }
    }
    
    return canonical;
  }


  public static void setCanonicalFromString(Annotation token, Document doc) {
    String canonical = getCanonicalFromString(token, doc);
    token.getFeatures().put("canonical", canonical);
  }

  
  public static String getCanonicalFromString(Annotation token, Document doc) {
    FeatureMap fm = token.getFeatures();
    String canonical = "";
    if (fm.containsKey(TOKEN_STRING_FEATURE_NAME)) {
      canonical = fm.get(TOKEN_STRING_FEATURE_NAME).toString().toLowerCase();
    }
    else {
      canonical = gate.Utils.stringFor(doc, token).toLowerCase();
    }
    
    return canonical;
  }

  
  public static String sourceOrName(Document document) {
    URL url = document.getSourceUrl();
    if (url == null) {
      return document.getName();
    }
    
    //implied else
    return url.toString();
  }
  
  
  
  
  
  public static File addExtensionIfNotExtended(File file, String extension) {
    String name = file.getName();
    if (name.contains(".")) {
      return file;
    }

    // implied else: add extension
    File parentDir = file.getParentFile();
    if (extension.startsWith(".")) {
      name = name + extension;
    }
    else {
      name = name + "." + extension;
    }

    return new File(parentDir, name);
  }

  
  public static String integerToString(Integer i) {
    if (i == null) {
      return "<null>";
    }
    // implied else
    return Integer.toString(i);
  }
  
  
  public static double log2(double input) {
    /*  log_a x = log_b x * log_a b
     * 
     *  log_b x = log_a x / log_a b
     */
    return Math.log10(input) / log10of2;
  }


}
