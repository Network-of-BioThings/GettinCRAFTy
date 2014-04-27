package gate.lingpipe;

import gate.Annotation;
import gate.AnnotationSet;
import gate.FeatureMap;
import gate.ProcessingResource;
import gate.Resource;
import gate.creole.AbstractLanguageAnalyser;
import gate.creole.ExecutionException;
import gate.creole.ResourceInstantiationException;
import gate.util.OffsetComparator;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;
import com.aliasi.classify.ConditionalClassification;
import com.aliasi.hmm.HiddenMarkovModel;
import com.aliasi.hmm.HmmDecoder;
import com.aliasi.tag.ScoredTagging;
import com.aliasi.tag.TagLattice;
import com.aliasi.tag.Tagging;
import com.aliasi.util.Streams;

/**
 * POS tagger based on the LingPipe library.
 * @author gate
 *
 */
public class POSTaggerPR extends AbstractLanguageAnalyser implements
                                                       ProcessingResource {

  private static final long serialVersionUID = -1159974411962638525L;

  
  /** File which cotains model for NE */
  protected URL modelFileUrl;

  /** Model file extracted from the URL */
  protected File modelFile;

  /** Model decoder object */
  protected HmmDecoder decoder;

  /** The name of the annotation set used for input */
  protected String inputASName;

  /**
   * Number of best results to obtain from the model
   */
  protected Integer nBest = 5;

  /**
   * The application mode
   */
  POSApplicationMode applicationMode;

  /**
   * Initializes this resource
   * 
   * @return Resource
   * @throws ResourceInstantiationException
   */
  public Resource init() throws ResourceInstantiationException {
    if(modelFileUrl == null)
      throw new ResourceInstantiationException("No model file provided!");

    try {
      modelFile = new File(modelFileUrl.toURI());
    }
    catch(URISyntaxException e) {
      throw new ResourceInstantiationException(e);
    }

    if(modelFile == null || !modelFile.exists()) {
      throw new ResourceInstantiationException("modelFile:"
              + modelFileUrl.toString() + " does not exists");
    }

    try {
      FileInputStream fileIn = new FileInputStream(modelFile);
      ObjectInputStream objIn = new ObjectInputStream(fileIn);
      HiddenMarkovModel hmm = (HiddenMarkovModel)objIn.readObject();
      Streams.closeQuietly(objIn);
      decoder = new HmmDecoder(hmm);
    }
    catch(IOException ioe) {
      throw new ResourceInstantiationException(ioe);
    }
    catch(ClassNotFoundException e) {
      throw new ResourceInstantiationException(e);
    }
    return this;
  }

  /**
   * Method is executed after the init() method has finished its
   * execution. <BR>
   * 
   * @throws ExecutionException
   */
  public void execute() throws ExecutionException {
    // lets start the progress and initialize the progress counter
    fireProgressChanged(0);

    // If no document provided to process throw an exception
    if(document == null) {
      fireProcessFinished();
      throw new ExecutionException("No document to process!");
    }

    // get the annotationSet name provided by the user, or otherwise use
    // the default method
    AnnotationSet inputAs = (inputASName == null || inputASName.trim().length() == 0)
            ? document.getAnnotations()
            : document.getAnnotations(inputASName);

    if(inputAs.get("Token").isEmpty()) {
      throw new ExecutionException("no Token annotations found");
    }

    List<Annotation> tokenList = new ArrayList<Annotation>(inputAs.get("Token"));
    Collections.sort(tokenList, new OffsetComparator());
    List<String> tokenStrList = new ArrayList<String>();
    for(int i = 0; i < tokenList.size(); i++) {
      Annotation ann = tokenList.get(i);
      String underlying = gate.Utils.stringFor(document, ann);
      tokenStrList.add(underlying);
    }

    if(applicationMode == POSApplicationMode.FIRSTBEST) {
      List<String> tags = firstBest(tokenStrList, decoder);
      for(int m = 0; m < tags.size(); m++) {
        tokenList.get(m).getFeatures().put("category", tags.get(m));
      }
    }
    else if(applicationMode == POSApplicationMode.CONFIDENCE) {
      List<Map<String, Double>> tags = confidence(tokenStrList, decoder);
      for(int m = 0; m < tags.size(); m++) {
        tokenList.get(m).getFeatures().put("category", tags.get(m));
      }
    }
    else {
      // key is the overall score for the tagset
      // value is the tagset for the entire document
      Map<Double, List<String>> tags = nBest(tokenStrList, decoder);
      for(Double score : tags.keySet()) {
        List<String> theTags = tags.get(score);
        for(int m = 0; m < theTags.size(); m++) {
          FeatureMap f = tokenList.get(m).getFeatures();
          Map<String, Set<Double>> scores = (Map<String, Set<Double>>) f.get("category");
          if(scores == null) {
            scores = new HashMap<String, Set<Double>>();
            f.put("category", scores);
          }
          
          Set<Double> vals = scores.get(theTags.get(m));
          if(vals == null) {
            vals = new HashSet<Double>();
            scores.put(theTags.get(m), vals);
          }
          vals.add(score);
        }
      }
    }

    // process finished, acknowledge user about this.
    fireProcessFinished();
  }

  /**
   * Obtains only the first best result.
   * @param tokens
   * @param decoder
   * @return an array of pos tags.
   */
  private List<String> firstBest(List<String> tokens, HmmDecoder decoder) {
    Tagging<String> tagging = decoder.tag(tokens);
    return tagging.tags();
  }

  /**
   * Obtains first five best outputs.
   * @param tokens
   * @param decoder
   * @return
   */
  private Map<Double, List<String>> nBest(List<String> tokens, HmmDecoder decoder) {
    Map<Double, List<String>> toReturn = new HashMap<Double, List<String>>();
    Iterator<ScoredTagging<String>> nBestIt = decoder.tagNBest(tokens, 5);
    for(int n = 0; n < nBest.intValue() && nBestIt.hasNext(); ++n) {
      ScoredTagging<String> tagScores = (ScoredTagging<String>) nBestIt.next();
      double score = tagScores.score();
      List<String> tags = tagScores.tags();
      toReturn.put(new Double(score), tags);
    }
    return toReturn;
  }

  /**
   * For every word, it obtains five pos tags and their confidence
   * @param tokens
   * @param decoder
   * @return
   */
  private List<Map<String, Double>> confidence(List<String> tokens,
          HmmDecoder decoder) {
    List<Map<String, Double>> toReturn = new ArrayList<Map<String, Double>>();
    TagLattice<String> lattice = decoder.tagMarginal(tokens);
    for(int tokenIndex = 0; tokenIndex < tokens.size(); ++tokenIndex) {
      ConditionalClassification tagScores = lattice.tokenClassification(tokenIndex);

      Map<String, Double> map = new HashMap<String, Double>();
      for(int i = 0; i < tagScores.size(); ++i) {
        //double logProb = tagScores.get(i).score();
        //double conditionalProb = java.lang.Math.pow(2.0, logProb);
        double conditionalProb = tagScores.conditionalProbability(i);
        String tag = tagScores.category(i);
        map.put(tag, new Double(conditionalProb));
      }
      toReturn.add(map);
    }
    return toReturn;
  }

  /**
   * Gets the url of the model used for pos tagging
   * @return
   */
  public URL getModelFileUrl() {
    return modelFileUrl;
  }

  /**
   * Sets the url of the model used for pos tagging
   * @param modelFileUrl
   */
  public void setModelFileUrl(URL modelFileUrl) {
    this.modelFileUrl = modelFileUrl;
  }

  /**
   * gets the name of the input annotation set with tokens in it
   * @return
   */
  public String getInputASName() {
    return inputASName;
  }

  /**
   * Sets the name of the input annotation set with tokens in it
   * @param inputAS
   */
  public void setInputASName(String inputASName) {
    this.inputASName = inputASName;
  }

  /**
   * Number of best results to obtain
   * @return
   */
  public int getNBest() {
    return nBest;
  }

  /**
   * Number of best results to obtain
   * @param best
   */
  public void setNBest(int best) {
    nBest = best;
  }

  /**
   * Gets the application mode in which the POS tagger should be run
   * @return
   */
  public POSApplicationMode getApplicationMode() {
    return applicationMode;
  }

  /**
   * Sets the application mode in which the POS tagger should be run
   * @param applicationMode
   */
  public void setApplicationMode(POSApplicationMode applicationMode) {
    this.applicationMode = applicationMode;
  }
}
