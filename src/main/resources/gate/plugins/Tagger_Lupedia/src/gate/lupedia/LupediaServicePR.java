/*
 * Copyright (c) 2009-2012, The University of Sheffield.
 * 
 * This file is part of GATE (see http://gate.ac.uk/), and is free software,
 * Licensed under the GNU Library General Public License, Version 3, June 2007
 * (in the distribution as file licence.html, and also available at
 * http://gate.ac.uk/gate/licence.html).
 */
package gate.lupedia;

import gate.AnnotationSet;
import gate.Factory;
import gate.FeatureMap;
import gate.Resource;
import gate.creole.ExecutionException;
import gate.creole.ResourceInstantiationException;
import gate.creole.metadata.CreoleParameter;
import gate.creole.metadata.CreoleResource;
import gate.creole.metadata.Optional;
import gate.creole.metadata.RunTime;
import gate.event.ProgressListener;
import gate.util.InvalidOffsetException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

/**
 * The PR uses lupedia annotation service provided by ontotext to annotate
 * documents.
 * 
 * @author niraj
 */
@CreoleResource(name = "Lupedia Service PR", comment = "Runs a lupedia annotation service on a GATE document")
public class LupediaServicePR extends gate.creole.AbstractLanguageAnalyser
  implements ProgressListener {
  Map<String, String> datasetsMap;

  /**
   * 
   */
  private static final long serialVersionUID = -8443994795704361590L;

  /**
   * Language in which the text is provided.
   */
  private Language lang;

  /**
   * Weight threshold, matches with weights below the threshold will be filtered
   * out. Default is set to 0.70.
   */
  private Double threshold = 0.70D;

  /**
   * Datasets to match to. Leave this empty for all the datasets.
   */
  private List<String> datasets;

  /**
   * Skip matches shorter than 3 symbols
   */
  private Boolean skipShortWords = true;

  /**
   * Skip matches that are stop-words
   */
  private Boolean skipStopWords = true;

  /**
   * Keep only first and longest match at given point
   */
  private Boolean keepFirstAndLongestMatch;

  /**
   * Keep only matches with highest weight
   */
  private Boolean keepHighest;

  /**
   * Keep only the most specific class
   */
  private Boolean keepSpecific;

  /**
   * Case sensitive matching
   */
  private Boolean caseSensitive;

  /**
   * Single greedy match only
   */
  private Boolean singleGreedyMatch;

  /**
   * Name of the annotation set where new annotations should be created.
   */
  private String outputASName;

  /**
   * Service URL
   */
  private final String LUPEDIA_SERVICE_URL =
    "http://lupedia.ontotext.com/lookup/text2json";

  /** Initialise this resource, and return it. */
  public Resource init() throws ResourceInstantiationException {
    datasetsMap = new HashMap<String, String>();
    datasetsMap.put("person", "dbpedia-owl:Person");
    datasetsMap.put("event", "dbpedia-owl:Event");
    datasetsMap.put("place", "dbpedia-owl:Place");
    datasetsMap.put("organisation", "dbpedia-owl:Organisation");
    datasetsMap.put("work", "dbpedia-owl:Work");
    return this;
  }

  /* this method is called to reinitialize the resource */
  public void reInit() throws ResourceInstantiationException {
    // reinitialization code
    init();
  }

  /**
   * Should be called to execute this PR on a document.
   */
  public void execute() throws ExecutionException {
    fireStatusChanged("Checking runtime parameters");
    // start time
    long startTime = System.currentTimeMillis();
    // if no document provided
    if(document == null) { throw new ExecutionException("Document is null!"); }
    // obtain the content
    String documentContent = document.getContent().toString();
    if(documentContent.trim().length() == 0) return;
    // annotation set to use
    AnnotationSet set =
      outputASName == null || outputASName.trim().length() == 0 ? document
        .getAnnotations() : document.getAnnotations(outputASName);
    // progress
    progressChanged(20);
    fireStatusChanged("Posted document for processing");
    // post the content to a service and obtain output
    String jsonOutput = postRequest(documentContent);
    progressChanged(80);
    fireStatusChanged("Received annotations -- now projecting them back to the document");
    // convert the output into json array object
    try {
      JSONArray jsonArray = (JSONArray)JSONSerializer.toJSON(jsonOutput);
      // iterate over each annotation and translate it into an annotation
      for(int i = 0; i < jsonArray.size(); i++) {
        if(interrupted) return;
        JSONObject json = (JSONObject)jsonArray.get(i);
        long startOffset = json.getLong("startOffset");
        long endOffset = json.getLong("endOffset");
        String instUri = json.getString("instanceUri");
        String instClass = json.getString("instanceClass");
        String predicateUri = json.getString("predicateUri");
        double weight = json.getDouble("weight");
        // featre amp
        FeatureMap map = Factory.newFeatureMap();
        map.put("inst", instUri);
        map.put("class", instClass);
        map.put("pred", predicateUri);
        map.put("weight", weight);
        // add annotation
        try {
          set.add(startOffset, endOffset,
            instClass.substring(instClass.lastIndexOf("/") + 1), map);
        } catch(InvalidOffsetException e) {
          throw new ExecutionException(e);
        }
      }
    } catch(JSONException exception) {
      throw new ExecutionException("Invalid response received\n" + jsonOutput);
    }
    // progress
    progressChanged(100);
    // let everyone who is interested know that we have now finished
    fireStatusChanged(document.getName() +
      " tagged with LupediaServicePR in " +
      NumberFormat.getInstance().format(
        (double)(System.currentTimeMillis() - startTime) / 1000) + " seconds!");
  }

  /**
   * Using URLConnection to connect to lupedia
   * 
   * @param text
   * @return
   * @throws ExecutionException
   */
  private String postRequest(String text) throws ExecutionException {
    // to store response
    StringBuffer resp = new StringBuffer();
    // Construct data
    String data =
      encode("lang", lang.toString()) + "&" +
        encode("threshold", "" + threshold) + "&" + datasets() +
        encode("skip_sh3", "" + skipShortWords) + "&" +
        encode("skip_stp", "" + skipStopWords) + "&" +
        encode("skip_ldata", "false") + "&" +
        encode("keep_fnl", "" + keepFirstAndLongestMatch) + "&" +
        encode("keep_highest", "" + keepHighest) + "&" +
        encode("keep_specific", "" + keepSpecific) + "&" +
        encode("single_match", "" + singleGreedyMatch) + "&" +
        encode("case_sensitive", "" + caseSensitive) + "&" +
        encode("lookupText", text);
    // url stream writers
    OutputStreamWriter wr = null;
    // to read response
    BufferedReader rd = null;
    try {
      // Send data
      URL url = new URL(LUPEDIA_SERVICE_URL);
      URLConnection conn = url.openConnection();
      conn.setDoOutput(true);
      wr = new OutputStreamWriter(conn.getOutputStream());
      // post the data to url
      wr.write(data);
      wr.flush();
      // Get the response
      rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
      // read it
      String line;
      while((line = rd.readLine()) != null) {
        resp.append(line + "\n");
      }
    } catch(Exception e) {
      throw new ExecutionException(e);
    } finally {
      // close the streams
      if(wr != null) {
        try {
          wr.close();
        } catch(IOException e) {
          throw new ExecutionException(e);
        }
      }
      if(rd != null) {
        try {
          rd.close();
        } catch(IOException e) {
          throw new ExecutionException(e);
        }
      }
    }
    return resp.toString();
  }

  /**
   * Encode data parameter and value as UTF strings
   * 
   * @param key
   * @param value
   * @return
   * @throws ExecutionException
   */
  private String encode(String key, String value) throws ExecutionException {
    try {
      return URLEncoder.encode(key, "UTF-8") + "=" +
        URLEncoder.encode(value, "UTF-8");
    } catch(UnsupportedEncodingException e) {
      throw new ExecutionException(e);
    }
  }

  /**
   * Producing datasets strings
   * 
   * @return
   * @throws ExecutionException
   */
  private String datasets() throws ExecutionException {
    StringBuffer sb = new StringBuffer();
    for(String ds : datasets) {
      String dsUri = datasetsMap.get(ds.toLowerCase());
      if(dsUri == null) continue;
      sb.append(encode("dataset", dsUri)).append("&");
    }
    return sb.toString();
  }

  public Language getLang() {
    return lang;
  }

  @RunTime
  @CreoleParameter(defaultValue = "en", comment = "Language in which the text is provided.")
  public void setLang(Language lang) {
    this.lang = lang;
  }

  public Double getThreshold() {
    return threshold;
  }

  @RunTime
  @CreoleParameter(defaultValue = "0.70", comment = "Weight threshold, matches with weights below the threshold will be filtered out. Default is set to 0.70.")
  public void setThreshold(Double threshold) {
    this.threshold = threshold;
  }

  public List<String> getDatasets() {
    return datasets;
  }

  @RunTime
  @CreoleParameter(defaultValue = "Person;Event;Place;Organisation;Work", comment = "Datasets to match to. Leave this empty for all the datasets.")
  public void setDatasets(List<String> datasets) {
    this.datasets = datasets;
  }

  public Boolean getSkipShortWords() {
    return skipShortWords;
  }

  @RunTime
  @CreoleParameter(defaultValue = "true", comment = "Skip matches shorter than 3 symbols")
  public void setSkipShortWords(Boolean skipShortWords) {
    this.skipShortWords = skipShortWords;
  }

  public Boolean getSkipStopWords() {
    return skipStopWords;
  }

  @RunTime
  @CreoleParameter(defaultValue = "true", comment = "Skip matches that are stop-words")
  public void setSkipStopWords(Boolean skipStopWords) {
    this.skipStopWords = skipStopWords;
  }

  public Boolean getKeepFirstAndLongestMatch() {
    return keepFirstAndLongestMatch;
  }

  @RunTime
  @CreoleParameter(defaultValue = "true", comment = "Keep only first and longest match at given point")
  public void setKeepFirstAndLongestMatch(Boolean keepFirstAndLongestMatch) {
    this.keepFirstAndLongestMatch = keepFirstAndLongestMatch;
  }

  public Boolean getKeepHighest() {
    return keepHighest;
  }

  @RunTime
  @CreoleParameter(defaultValue = "true", comment = "Keep only matches with highest weight")
  public void setKeepHighest(Boolean keepHighest) {
    this.keepHighest = keepHighest;
  }

  public Boolean getKeepSpecific() {
    return keepSpecific;
  }

  @RunTime
  @CreoleParameter(defaultValue = "true", comment = "Keep only the most specific class")
  public void setKeepSpecific(Boolean keepSpecific) {
    this.keepSpecific = keepSpecific;
  }

  public Boolean getCaseSensitive() {
    return caseSensitive;
  }

  @RunTime
  @CreoleParameter(defaultValue = "true", comment = "Case sensitive matching")
  public void setCaseSensitive(Boolean caseSensitive) {
    this.caseSensitive = caseSensitive;
  }

  public Boolean getSingleGreedyMatch() {
    return singleGreedyMatch;
  }

  @RunTime
  @CreoleParameter(defaultValue = "false", comment = "Single greedy match only")
  public void setSingleGreedyMatch(Boolean singleGreedyMatch) {
    this.singleGreedyMatch = singleGreedyMatch;
  }

  public String getOutputASName() {
    return outputASName;
  }

  @RunTime
  @CreoleParameter(comment = "Name of the annotation set where new annotations should be created.")
  @Optional
  public void setOutputASName(String outputASName) {
    this.outputASName = outputASName;
  }

  @Override
  public void progressChanged(int i) {
    fireProgressChanged(i);
  }

  @Override
  public void processFinished() {
    fireProcessFinished();
  }
} // class
