/*
 * Copyright (c) 2009-2012, The University of Sheffield.
 * 
 * This file is part of GATE (see http://gate.ac.uk/), and is free software,
 * Licensed under the GNU Library General Public License, Version 3, June 2007
 * (in the distribution as file licence.html, and also available at
 * http://gate.ac.uk/gate/licence.html).
 */
package gate.zemanta;

import gate.Annotation;
import gate.AnnotationSet;
import gate.Factory;
import gate.FeatureMap;
import gate.Resource;
import gate.Utils;
import gate.creole.ExecutionException;
import gate.creole.ResourceInstantiationException;
import gate.creole.metadata.CreoleParameter;
import gate.creole.metadata.CreoleResource;
import gate.creole.metadata.Optional;
import gate.creole.metadata.RunTime;
import gate.event.ProgressListener;
import gate.util.InvalidOffsetException;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.zemanta.api.Zemanta;
import com.zemanta.api.ZemantaResult;
import com.zemanta.api.suggest.Markup.Link;
import com.zemanta.api.suggest.Markup.Target;

/**
 * The PR uses zemanta online service to annotate documents.
 * 
 * @author niraj
 */
@CreoleResource(name = "Zemanta Service PR", comment = "Runs a zemanta annotation service on a GATE document")
public class ZemantaServicePR extends gate.creole.AbstractLanguageAnalyser
  implements ProgressListener {
  private static final long serialVersionUID = -8443994795704361590L;

  /**
   * developer key. One has to obtain this from Zemanta by creating an account
   * online
   */
  private String apiKey;

  /**
   * Name of the annotation set where new annotations should be created.
   */
  private String outputASName;

  /**
   * The PR requires Sentence annotations as input. This parameter tells PR
   * where it can find the Sentence annotations
   */
  private String inputASName;

  /**
   * Number of sentences to be sent to Zemanta to process in one batch Default
   * is 10 sentences.
   */
  private Integer numberOfSentencesInBatch = 10;

  /**
   * This is the number of sentences that are used as sentences in context both
   * on left and right side and not annotated when sent as part of a batch
   * unless there's no more sentence to be considered as part of the context.
   */
  private Integer numberOfSentencesInContext = 2;

  /**
   * Service URL
   */
  private final String SERVICE_URL =
    "http://api.zemanta.com/services/rest/0.0/";

  /**
   * Zemanta service
   */
  private Zemanta zemanta = null;

  /** Initialise this resource, and return it. */
  public Resource init() throws ResourceInstantiationException {
    if(getApiKey() == null || getApiKey().isEmpty()) { throw new ResourceInstantiationException(
      "Invalid API key. Please visit developer.zemanta.com for more information"); }
    // intiate the service
    zemanta = new Zemanta(getApiKey(), SERVICE_URL);
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
    progressChanged(0);
    // if no document provided
    if(document == null) { throw new ExecutionException("Document is null!"); }
    // obtain the content
    String documentContent = document.getContent().toString();
    if(documentContent.trim().length() == 0) return;
    // annotation set to use
    AnnotationSet set =
      outputASName == null || outputASName.trim().length() == 0 ? document
        .getAnnotations() : document.getAnnotations(outputASName);
    AnnotationSet inputAS =
      inputASName == null || inputASName.trim().length() == 0 ? document
        .getAnnotations() : document.getAnnotations(inputASName);
    // start time
    long startTime = System.currentTimeMillis();
    // all sentences
    List<Annotation> allSents = Utils.inDocumentOrder(inputAS.get("Sentence"));
    // if no sentence annotations report that
    if(allSents.size() == 0) { throw new ExecutionException(
      "Atleast one sentence must be provided"); }
    // Tokens
    List<Annotation> allTokens = Utils.inDocumentOrder(inputAS.get("Token"));
    // if no sentence annotations report that
    if(allTokens.size() == 0) { throw new ExecutionException(
      "Document must have Tokens"); }
    for(int i = 0; i < allSents.size(); i += numberOfSentencesInBatch) {
      if(interrupted) return;
      int endIndex = i + numberOfSentencesInBatch - 1;
      if(endIndex >= allSents.size()) endIndex = allSents.size() - 1;
      // we add numberOfSentencesInContext in left and right context if
      // they are available
      int contextStartIndex = i - numberOfSentencesInBatch;
      if(contextStartIndex < 0) contextStartIndex = 0;
      int contextEndIndex = endIndex + numberOfSentencesInContext;
      if(contextEndIndex >= allSents.size()) {
        contextEndIndex = allSents.size() - 1;
      }
      // obtain the string to be annotated
      String sentString =
        Utils.stringFor(document, Utils.start(allSents.get(contextStartIndex)),
          Utils.end(allSents.get(contextEndIndex)));
      // the actual content
      String contentString =
        Utils.stringFor(document, Utils.start(allSents.get(i)),
          Utils.end(allSents.get(endIndex)));
      // progress
      progressChanged((int)(i * 90 / (double)allSents.size()));
      fireStatusChanged("Posted a part of document for processing");
      // now process the text
      // post the content to a service and obtain output
      // what we get back is the mathcing text which uri in them
      List<Result> result = postRequest(sentString.toString());
      fireStatusChanged("Copying annotations on the document");
      for(Result r : result) {
        int index = -1;
        // annotate all occurrences of the matching Text
        while((index = contentString.indexOf(r.matchingText, index + 1)) >= 0) {
          int stOffset = Utils.start(allSents.get(i)).intValue() + index;
          int enOffset = stOffset + r.matchingText.length();
          FeatureMap map = Factory.newFeatureMap();
          map.put("inst", r.uri);
          map.put("type", r.type);
          map.put("confidence", r.confidence);
          try {
            set.add((long)stOffset, (long)enOffset, "Mention", map);
          } catch(InvalidOffsetException e) {
            throw new ExecutionException(e);
          }
        }
      }
    }
    fireStatusChanged("deleting Mention annotations not in sync with Tokens");
    // get rid of annotations that donot sync with token boundaries
    AnnotationSet mentionSet = set.get("Mention");
    Set<Annotation> toDelete = new HashSet<Annotation>();
    for(Annotation aMention : mentionSet) {
      long start = Utils.start(aMention);
      long end = Utils.end(aMention);
      // obtain the contained token annotations
      AnnotationSet tokenSet = inputAS.get("Token").getContained(start, end);
      if(tokenSet.isEmpty()) {
        toDelete.add(aMention);
        continue;
      }
      // sort the tokens
      // start and end boundaries must be in sync
      List<Annotation> orderedTokens = Utils.inDocumentOrder(tokenSet);
      if(start != Utils.start(orderedTokens.get(0)).longValue() ||
        end != Utils.end(orderedTokens.get(orderedTokens.size() - 1))
          .longValue()) {
        toDelete.add(aMention);
        continue;
      }
    }
    // delete the mentions not in sync with tokens boundaries
    for(Annotation toDelAnnot : toDelete) {
      set.remove(toDelAnnot);
    }
    // progress
    progressChanged(100);
    // let everyone who is interested know that we have now finished
    fireStatusChanged(document.getName() +
      " tagged with ZemantaServicePR in " +
      NumberFormat.getInstance().format(
        (double)(System.currentTimeMillis() - startTime) / 1000) + " seconds!");
  }

  /**
   * Using URLConnection to connect to zemanta
   * 
   * @param text
   * @return
   * @throws ExecutionException
   */
  private List<Result> postRequest(String text) throws ExecutionException {
    List<Result> toReturn = new ArrayList<Result>();
    HashMap<String, String> parameters = new HashMap<String, String>();
    parameters.put("method", "zemanta.suggest");
    parameters.put("api_key", getApiKey());
    parameters.put("text", text);
    parameters.put("format", "xml");
    // Example 2: suggest markup
    ZemantaResult zemMarkup = zemanta.suggestMarkup(text);
    if(!zemMarkup.isError) {
      for(Link l : zemMarkup.markup.links) {
        Result r = new Result();
        r.matchingText = l.anchor;
        for(Target t : l.targets) {
          if(t.url.indexOf("wikipedia") > 0) {
            r.uri =
              "http://dbpedia.org/resource/" +
                t.url.substring(t.url.lastIndexOf('/') + 1);
            r.type = l.entityType;
            r.confidence = l.confidence;
            break;
          }
        }
        if(r.uri == null) continue;
        toReturn.add(r);
      }
    }
    return toReturn;
  }

  public String getOutputASName() {
    return outputASName;
  }

  @RunTime
  @CreoleParameter
  @Optional
  public void setOutputASName(String outputASName) {
    this.outputASName = outputASName;
  }

  /**
   * The PR requires Sentence annotations as input. This parameter tells PR
   * where it can find the Sentence annotations
   */
  public String getInputASName() {
    return inputASName;
  }

  /**
   * The PR requires Sentence annotations as input. This parameter tells PR
   * where it can find the Sentence annotations
   */
  @CreoleParameter
  @Optional
  @RunTime
  public void setInputASName(String inputASName) {
    this.inputASName = inputASName;
  }

  /**
   * developer key. One has to obtain this from Zemanta by creating an account
   * online
   */
  public String getApiKey() {
    return apiKey;
  }

  /**
   * developer key. One has to obtain this from Zemanta by creating an account
   * online
   */
  @CreoleParameter(comment = "developer key. One has to obtain this from Zemanta by creating an account online")
  public void setApiKey(String apiKey) {
    this.apiKey = apiKey;
  }

  public Integer getNumberOfSentencesInBatch() {
    return numberOfSentencesInBatch;
  }

  @CreoleParameter(comment = "Number of sentences to be sent to Zemanta to process in one batch")
  @RunTime
  public void setNumberOfSentencesInBatch(Integer numberOfSentencesInBatch) {
    this.numberOfSentencesInBatch = numberOfSentencesInBatch;
  }

  public Integer getNumberOfSentencesInContext() {
    return numberOfSentencesInContext;
  }

  @CreoleParameter(comment = "This is the number of sentences that are used as sentences in context both on left and right side and not annotated when sent as part of a less there are no more sentences to be considered as part of the context")
  @RunTime
  public void setNumberOfSentencesInContext(Integer numberOfSentencesInContext) {
    this.numberOfSentencesInContext = numberOfSentencesInContext;
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

class Result {
  String matchingText;

  String uri;

  float confidence;

  String type;
}