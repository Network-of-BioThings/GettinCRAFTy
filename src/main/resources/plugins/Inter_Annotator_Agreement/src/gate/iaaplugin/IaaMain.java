/*
 * IaaMain.java
 * 
 * Yaoyong Li 15/03/2008
 * 
 * $Id: IaaMain.java, v 1.0 2008-03-15 12:58:16 +0000 yaoyong $
 */
package gate.iaaplugin;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import gate.Annotation;
import gate.AnnotationSet;
import gate.Factory;
import gate.FeatureMap;
import gate.ProcessingResource;
import gate.creole.AbstractLanguageAnalyser;
import gate.creole.ExecutionException;
import gate.creole.ResourceInstantiationException;
import gate.util.AnnotationDiffer;

// import gate.util.FMeasure;
// import gate.util.IaaCalculation;
/**
 * Compute the inter-annotator agreements (IAAs). Currently only f-measures are
 * computed as IAA. But other measures, such as Cohen's Kappa, can be computed
 * easily if needed.
 */
public class IaaMain extends AbstractLanguageAnalyser implements
                                                     ProcessingResource {
  /** Annotation sets for merging in one document. */
  private String annSetsForIaa;

  /** Specifying the annotation types and features for merging. */
  private String annTypesAndFeats;

  /** Specify the verbosity level for IAA results outputs. */
  private String verbosity;

  private int verbo;

  /**
   * Specify the problem is a classification or not. For classification problem,
   * compute and output the kappa measures as IAA. Otherwise, compute and output
   * the F-measures.
   */
  private MeasureType measureType;

  private MeasureType fMeasure;

  private MeasureType agreementAndKappa;

  /** The overall Cohen's kappa value over all pairs and types. */
  public float[] overallTypesPairs = null;

  /** The overall Cohen's kappa value for each type. */
  private float[][] kappaOverall = null;

  /** The contingency table for each pair of annotator and each type. */
  private float[][][] kappaPairwise = null;

  /**
   * Number of types of kappa, now it's 3: Obeserved agreement, Cohen's kappa,
   * and Scott's pi.
   */
  private final int numTypesKappa = 3;

  private final String[] namesKappa =
          {"Observed agreement", "Cohen's kappa", "Scott's pi"};

  /** The overall F-measure for each type. */
  private FMeasure[] fMeasureOverall;

  /** Fmeaures for each pair of annotator and each label. */
  private HashMap<String, FMeasure> fMeasuresPairwiseLabel = null;

  /** Using the labels or not for one type for IAA computation */
  private boolean isUsingLabel;

  /**
   * number of documents not being counted because they don't have some
   * annotation set required.
   */
  private int numDocNotCounted = 0;

  /** Fmeaures for each pair of annotator and over all labels for each type. */
  private FMeasure[][] fMeasuresPairwise = null;

  /** The overall F-measure for all types. */
  public FMeasure fMeasureOverallTypes;

  /** Average the results from each documents. */
  /** All the types and features from all documents in corpus. */
  HashMap<String, String> allTypeFeats = null;

  /** Annotation type and the feature names. */
  HashMap<String, String> annsTypes = new HashMap<String, String>();

  /** The URL storing the BDM scores computed by bdmComputation plugin. */
  URL bdmScoreFile = null;

  /** the map from a pair of concepts to their bdm score. */
  HashMap<String, Float> conceptNames2BDM = null;

  /** Whether or not using the BDM. */
  boolean isUsingBDM;

  /** The overall F-measure for each type for BDM. */
  private FMeasure[] fMeasureOverallBDM;

  /** Fmeaures for each pair of annotator and each label for BDM. */
  private HashMap<String, FMeasure> fMeasuresPairwiseLabelBDM = null;

  /**
   * Fmeaures for each pair of annotator and over all labels for each type for
   * BDM.
   */
  private FMeasure[][] fMeasuresPairwiseBDM = null;

  /** The overall F-measure for all types for BDM. */
  public FMeasure fMeasureOverallTypesBDM;

  /** Initialise this resource, and return it. */
  public gate.Resource init() throws ResourceInstantiationException {
    allTypeFeats = new HashMap<String, String>();
    this.agreementAndKappa = MeasureType.AGREEMENTANDKAPPA;
    this.fMeasure = MeasureType.FMEASURE;
    return this;
  } // init()

  /**
   * Run the resource.
   * 
   * @throws ExecutionException
   */
  public void execute() throws ExecutionException {
    int positionDoc = corpus.indexOf(document);
    if(positionDoc == 0) {
      allTypeFeats.clear();
      isUsingLabel = false;
      verbo = Integer.parseInt(verbosity);
      numDocNotCounted = 0;
      if(verbo > 0)
        System.out
                .println("\n\n------------------------------------------------\n");
      annsTypes.clear();
      String[] annTs =
              this.annTypesAndFeats.split(ConstantParameters.TERMSeparator);
      for(int i = 0; i < annTs.length; ++i) {
        annTs[i] = annTs[i].trim();
        if(annTs[i].contains(ConstantParameters.TypeFeatSeparator)) {
          String ty =
                  annTs[i].substring(0, annTs[i]
                          .indexOf(ConstantParameters.TypeFeatSeparator));
          String tf =
                  annTs[i].substring(annTs[i]
                          .indexOf(ConstantParameters.TypeFeatSeparator)
                          + ConstantParameters.TypeFeatSeparator.length());
          annsTypes.put(ty.trim(), tf.trim());
        } else {
          annsTypes.put(annTs[i], "");
        }
      }
      // initialise the variables for bdm score.
      if(bdmScoreFile != null && bdmScoreFile.toString() != "") {
        isUsingBDM = true;
        conceptNames2BDM = new HashMap<String, Float>();
        System.out.println("The BDM file used is "
                + bdmScoreFile.getPath().toString());
        read(bdmScoreFile, conceptNames2BDM); // read the bdm scores
        // from the file into
        // memory
      } else {
        isUsingBDM = false;
      }
    }
    // For each document, at first assume all the annotation sets specified
    // available
    boolean isAvailabelAllAnnSets = true;
    // Get the annotation sets for computing the IAA
    // Get all the existing annotation sets from the current document
    Set annsExisting = document.getAnnotationSetNames();
    String[] annsArray = null;
    if(annSetsForIaa == null || annSetsForIaa.trim().length() == 0) {
      // if there is no annotation specified, compare all the annotation
      // sets in the document.
      // count how many annotation sets the document has, but not use
      // the default annotation set which has a empty string as its name.
      int num = 0;
      for(Object obj : annsExisting) {
        if(obj != null && obj.toString().trim().length() > 0) ++num;
      }
      annsArray = new String[num];
      num = 0;
      List<String> annsE = new Vector<String>(annsExisting);
      Collections.sort(annsE);
      for(Object obj : annsE) {
        if(obj != null && obj.toString().trim().length() > 0)
          annsArray[num++] = obj.toString();
      }
    } else { // if specify some annotation sets already
      annSetsForIaa = annSetsForIaa.trim();
      annsArray = annSetsForIaa.split(ConstantParameters.TERMSeparator);
    }
    int numAnns = annsArray.length;
    if(verbo > 1 && positionDoc == 0) System.out.println("Annotation sets:");
    for(int i = 0; i < numAnns; ++i) {
      annsArray[i] = annsArray[i].trim();
      if(verbo > 1 && positionDoc == 0)
        System.out.println("*" + annsArray[i] + "*");
      // Check if each annotation set for merging exist in the current
      // document
      if(!annsExisting.contains(annsArray[i])) isAvailabelAllAnnSets = false;
    }
    // Collect the annotation types from annotation sets for iaa computation
    // Get the map from annotation type to feature, if specified in the
    // setting.
    if(this.annTypesAndFeats == null
            || this.annTypesAndFeats.trim().length() == 0) {
      // If not specify the annotation type and features, use
      // all the types but no feature.
      for(int i = 0; i < numAnns; ++i) {
        Set types = document.getAnnotations(annsArray[i]).getAllTypes();
        for(Object obj : types)
          if(!annsTypes.containsKey(obj)) annsTypes.put(obj.toString(), "");
      }
    }
    // Get all the type names
    Vector<String> typeNames = new Vector<String>(annsTypes.keySet());
    // Get the avaraged f-measures for all documents in the corpus
    // initialise the averaged measures
    if(positionDoc == 0) {
      int num1 = annsArray.length * (annsArray.length - 1) / 2;
      int numTypes = annsTypes.keySet().size();
      fMeasuresPairwise = new FMeasure[numTypes][num1];
      fMeasureOverall = new FMeasure[numTypes];
      for(int j = 0; j < numTypes; ++j) {
        for(int i = 0; i < num1; ++i)
          fMeasuresPairwise[j][i] = new FMeasure();
        fMeasureOverall[j] = new FMeasure();
      }
      fMeasureOverallTypes = new FMeasure();
      fMeasuresPairwiseLabel = new HashMap<String, FMeasure>();
      // Initialise the kappa measure: they should has zeros as
      // initialisation values.
      kappaOverall = new float[numTypesKappa][numTypes];
      kappaPairwise = new float[numTypesKappa][numTypes][num1];
      if(this.isUsingBDM) {
        // Initialise the F-measures for the BDM
        fMeasuresPairwiseBDM = new FMeasure[numTypes][num1];
        fMeasureOverallBDM = new FMeasure[numTypes];
        for(int j = 0; j < numTypes; ++j) {
          for(int i = 0; i < num1; ++i)
            fMeasuresPairwiseBDM[j][i] = new FMeasure();
          fMeasureOverallBDM[j] = new FMeasure();
        }
        fMeasureOverallTypesBDM = new FMeasure();
        fMeasuresPairwiseLabelBDM = new HashMap<String, FMeasure>();
      }
    }
    if(!isAvailabelAllAnnSets) {
      ++numDocNotCounted;
      System.out.println("\nThe document " + document.getName()
              + " doesn't have all the annotation sets required!");
    } else {
      // Put the types and features into the map for all documents
      for(String t : annsTypes.keySet()) {
        allTypeFeats.put(t, annsTypes.get(t));
      }
      // Compute the IAA for each annotation type and feature
      // Get all the annotation sets
      AnnotationSet[] annSAll = new AnnotationSet[annsArray.length];
      for(int i = 0; i < annSAll.length; ++i)
        annSAll[i] = document.getAnnotations(annsArray[i]);
      if(verbo > 0)
        System.out.println("\nFor the document: " + document.getName());
      // Sort the types names
      Collections.sort(typeNames);
      for(int iIndex = 0; iIndex < typeNames.size(); ++iIndex) { // for
        // each
        // annotation
        // type
        String typeN = typeNames.get(iIndex);
        if(verbo > 0)
          System.out.println("For the annotation type *" + typeN + "*");
        IaaCalculation iaaC = null;
        AnnotationSet[][] annSs = new AnnotationSet[1][annsArray.length];
        for(int j = 0; j < annSs[0].length; ++j) {
          annSs[0][j] = annSAll[j].get(typeN);
          // annSs[0][j].setName(annsArray[j]);
        }
        String[] labels = null;
        if(annsTypes.get(typeN) != null && annsTypes.get(typeN) != "") {
          String nameF = annsTypes.get(typeN);
          ArrayList<String> labelList =
                  IaaCalculation.collectLabels(annSs, nameF);
          Collections.sort(labelList);
          labels = new String[labelList.size()];
          for(int j = 0; j < labelList.size(); ++j)
            labels[j] = labelList.get(j);
          iaaC = new IaaCalculation(typeN, nameF, labels, annSs, verbo);
          isUsingLabel = true;
          if(verbo > 1)
            System.out.println("Annotation feature=*" + nameF + "*");
        } else {
          iaaC = new IaaCalculation(typeN, annSs, verbo);
        }
        // transfer Annotators' names to iaaC
        iaaC.setAnnotatorNames(annsArray);
        // Compute the F-measure
        if(this.measureType.equals(this.fMeasure)) {
          computeFmeasures(iIndex, iaaC, typeN, labels, annsArray);
          // compute the F-measure for BDM
          if(this.isUsingBDM) {
            computeFmeasuresBDM(iIndex, iaaC, typeN, labels, annsArray);
          }
        }
        // Compute the cohen's Kappa
        if(this.measureType.equals(this.agreementAndKappa))
          computeKappa(iIndex, iaaC, annsArray);
      }
    }
    // Print out the overall results
    if(positionDoc == corpus.size() - 1) {
      if(this.measureType.equals(this.fMeasure)) {
        printOverallResultsFmeasure(typeNames, annsArray);
        if(this.isUsingBDM) {
          printOverallResultsFmeasureBDM(typeNames, annsArray);
        }
      }
      // print the kappa
      if(this.measureType.equals(this.agreementAndKappa))
        printOverallResultsKappa(typeNames, annsArray);
    }
  }

  private void computeKappa(int i, IaaCalculation iaaC, String[] annsArray) {
    iaaC.pairwiseIaaKappa();
    if(verbo > 1) iaaC.printResultsPairwiseIaa();
    // get the kappa values from this document and add them to overall.
    this.kappaOverall[0][i] += iaaC.contingencyOverall.observedAgreement;
    this.kappaOverall[1][i] += iaaC.contingencyOverall.kappaCohen;
    this.kappaOverall[2][i] += iaaC.contingencyOverall.kappaPi;
    int num111 = annsArray.length * (annsArray.length - 1) / 2;
    for(int i11 = 0; i11 < num111; ++i11) {
      this.kappaPairwise[0][i][i11] +=
              iaaC.contingencyTables[i11].observedAgreement;
      this.kappaPairwise[1][i][i11] += iaaC.contingencyTables[i11].kappaCohen;
      this.kappaPairwise[2][i][i11] += iaaC.contingencyTables[i11].kappaPi;
    }
  }

  private void computeFmeasures(int iIndex, IaaCalculation iaaC, String typeN,
          String[] labels, String[] annsArray) {
    iaaC.pairwiseIaaFmeasure();
    if(verbo > 0)
      System.out.println("For the annotation type *" + typeN + "*");
    if(verbo > 0) iaaC.printResultsPairwiseFmeasures();
    // sum the fmeasure of all documents
    fMeasureOverall[iIndex].add(iaaC.fMeasureOverall);
    for(int j = 0; j < fMeasuresPairwise[0].length; ++j)
      fMeasuresPairwise[iIndex][j].add(iaaC.fMeasuresPairwise[j]);
    // add the fmeasure for each sub-type label
    if(annsTypes.get(typeN) != null && annsTypes.get(typeN) != "") {
      for(int i1 = 0; i1 < labels.length; ++i1) {
        int num11 = 0;
        for(int i11 = 0; i11 < annsArray.length; ++i11)
          for(int j11 = i11 + 1; j11 < annsArray.length; ++j11) {
            String key = typeN.concat("->" + labels[i1]);
            key = "(" + annsArray[i11] + "," + annsArray[j11] + "):" + key;
            if(!fMeasuresPairwiseLabel.containsKey(key))
              fMeasuresPairwiseLabel.put(key, new FMeasure());
            fMeasuresPairwiseLabel.get(key).add(
                    iaaC.fMeasuresPairwiseLabel[num11][i1]);
            ++num11;
          }
      }
    }
  }

  private void printOverallResultsKappa(Vector<String> typeNames,
          String[] annsArray) {
    int numDoc = corpus.size();
    numDoc -= numDocNotCounted;
    if(numDoc < 1) ++numDoc;
    if(verbo > 0)
      System.out.println("\nMacro averaged over " + numDoc + " documents:");
    if(verbo > 1)
      System.out.println("\nFor each pair of annotators and each type:");
    // if(verbo>0) System.out.println("for each type:" );
    int numTypes = annsTypes.keySet().size();
    overallTypesPairs = new float[this.numTypesKappa];
    for(int i = 0; i < numTypes; ++i) {
      String typeN = typeNames.get(i);
      if(verbo > 0) System.out.println("Annotation type *" + typeN + "*");
      for(int ii = 0; ii < this.numTypesKappa; ++ii) {
        this.kappaOverall[ii][i] /= numDoc;
      }
      for(int j = 0; j < this.kappaPairwise[0][0].length; ++j)
        for(int ii = 0; ii < this.numTypesKappa; ++ii) {
          this.kappaPairwise[ii][i][j] /= numDoc;
        }
      if(verbo > 0) System.out.println("For each pair of annotators");
      int num11 = 0;
      for(int i1 = 0; i1 < annsArray.length; ++i1)
        for(int j = i1 + 1; j < annsArray.length; ++j) {
          if(verbo > 0) {
            String resS = new String("");
            for(int ii = 0; ii < this.numTypesKappa; ++ii) {
              resS +=
                      this.namesKappa[ii] + ": "
                              + this.kappaPairwise[ii][i][num11] + ";  ";
            }
            System.out.println("For pair (" + annsArray[i1] + ","
                    + annsArray[j] + "): " + resS);
          }
          ++num11;
        }
      if(verbo > 0) {
        String resS = new String("");
        for(int ii = 0; ii < this.numTypesKappa; ++ii) {
          resS += this.namesKappa[ii] + ": " + this.kappaOverall[ii][i] + ";  ";
        }
        System.out.println("Overall pairs: " + resS);
      }
      for(int ii = 0; ii < this.numTypesKappa; ++ii) {
        overallTypesPairs[ii] += this.kappaOverall[ii][i];
      }
    }
    if(numTypes > 0) for(int ii = 0; ii < this.numTypesKappa; ++ii) {
      overallTypesPairs[ii] /= numTypes;
    }
    if(verbo > 0) {
      String resS = new String("");
      for(int ii = 0; ii < this.numTypesKappa; ++ii) {
        resS += this.namesKappa[ii] + ": " + overallTypesPairs[ii] + ";  ";
      }
      System.out.println("Overall pairs and types: " + resS);
    }
  }

  private void printOverallResultsFmeasure(Vector<String> typeNames,
          String[] annsArray) {
    ArrayList<String> keyList = new ArrayList(fMeasuresPairwiseLabel.keySet());
    Collections.sort(keyList);
    int numDoc = corpus.size();
    numDoc -= numDocNotCounted;
    if(numDoc < 1) ++numDoc;
    int numTypes = annsTypes.keySet().size();
    // Code presenting the macro average. Printouts are commented out due to
    // dispute over
    // how to handle documents that are missing an annotation type
    // altogether.
    // if(verbo>0)
    // System.out.println("\nMacro averaged over "+numDoc+" documents:");
    // if(verbo>0)
    // System.out.println("\nFor each pair of annotators, each type and each label:");
    // if(verbo>0) System.out.println("for each type:");
    for(int i = 0; i < numTypes; ++i) {
      String typeN = typeNames.get(i);
      // if(verbo>0) System.out.println("Annotation type *"+ typeN+"*");
      fMeasureOverall[i].macroAverage(numDoc);
      for(int j = 0; j < fMeasuresPairwise[0].length; ++j)
        fMeasuresPairwise[i][j].macroAverage(numDoc);
      // if(verbo>0) System.out.println("For each pair of annotators");
      /*
       * int num11=0; for(int i1=0; i1<annsArray.length; ++i1) for(int j=i1+1;
       * j<annsArray.length; ++j) { if(verbo>0) System.out.println(
       * "For pair ("+annsArray[i1]+","+annsArray[j]+"): "+
       * fMeasuresPairwise[i][num11].printResults()); ++num11; }
       */
      if(verbo > 1) {
        isUsingLabel = false;
        if(annsTypes.get(typeN) != null && annsTypes.get(typeN) != "")
          isUsingLabel = true;
        if(isUsingLabel) {
          for(int i1 = 0; i1 < keyList.size(); ++i1) {
            String key = keyList.get(i1);
            if(key.contains("):" + typeN + "->")) {
              fMeasuresPairwiseLabel.get(key).macroAverage(numDoc);
              // String pairAnns =
              // key.substring(0,key.indexOf("):")+1);
              // String typeAnn =
              // key.substring(key.indexOf("):")+2,
              // key.indexOf("->"));
              // String labelAnn =
              // key.substring(key.indexOf("->")+2);
              // System.out.println("pairAnns="+pairAnns+", type="+typeAnn+", label="+labelAnn+": "
              // +fMeasuresPairwiseLabel.get(key).printResults());
            }
          }
        }
      }
      // if(verbo>0)
      // System.out.println("Overall pairs: "+fMeasureOverall[i].printResults());
      fMeasureOverallTypes.add(fMeasureOverall[i]);
    }
    fMeasureOverallTypes.macroAverage(numTypes);
    // if(verbo>0) System.out.println("Overall pairs and types: "+
    // fMeasureOverallTypes.printResults());
    // Code presenting the micro average.
    if(verbo > 0)
      System.out.println("\nMicro averaged over " + numDoc + " documents:");
    if(verbo > 0) System.out.println("For each pair of annotators");
    for(int i = 0; i < numTypes; ++i) {
      String typeN = typeNames.get(i);
      if(verbo > 0) System.out.println("Annotation type *" + typeN + "*");
      int num11 = 0;
      for(int i1 = 0; i1 < annsArray.length; ++i1)
        for(int j = i1 + 1; j < annsArray.length; ++j) {
          fMeasuresPairwise[i][num11].computeFmeasure();
          fMeasuresPairwise[i][num11].computeFmeasureLenient();
          if(verbo > 0)
            System.out.println("For pair (" + annsArray[i1] + ","
                    + annsArray[j] + "): "
                    + fMeasuresPairwise[i][num11].printResults());
          ++num11;
        }
      fMeasureOverall[i].computeFmeasure();
      fMeasureOverall[i].computeFmeasureLenient();
      if(verbo > 0)
        System.out.println("Overall pairs: "
                + fMeasureOverall[i].printResults());
      if(verbo > 1) {
        isUsingLabel = false;
        if(annsTypes.get(typeN) != null && annsTypes.get(typeN) != "")
          isUsingLabel = true;
        if(isUsingLabel) {
          System.out
                  .println("\nFor each pair of annotators, each type and each label:");
          for(int i1 = 0; i1 < keyList.size(); ++i1) {
            String key = keyList.get(i1);
            if(key.contains("):" + typeN + "->")) {
              fMeasuresPairwiseLabel.get(key).computeFmeasure();
              fMeasuresPairwiseLabel.get(key).computeFmeasureLenient();
              String pairAnns = key.substring(0, key.indexOf("):") + 1);
              String typeAnn =
                      key.substring(key.indexOf("):") + 2, key.indexOf("->"));
              String labelAnn = key.substring(key.indexOf("->") + 2);
              System.out.println("pairAnns=" + pairAnns + ", type=" + typeAnn
                      + ", label=" + labelAnn + ": "
                      + fMeasuresPairwiseLabel.get(key).printResults());
            }
          }
        }
      }
    }
    fMeasureOverallTypes.computeFmeasure();
    fMeasureOverallTypes.computeFmeasureLenient();
    if(verbo > 0)
      System.out.println("Overall pairs and types: "
              + fMeasureOverallTypes.printResults());
  }

  private void printOverallResultsFmeasureBDM(Vector<String> typeNames,
          String[] annsArray) {
    ArrayList<String> keyList =
            new ArrayList(fMeasuresPairwiseLabelBDM.keySet());
    Collections.sort(keyList);
    int numDoc = corpus.size();
    numDoc -= numDocNotCounted;
    if(numDoc < 1) ++numDoc;
    int numTypes = annsTypes.keySet().size();
    if(verbo > 0)
      System.out
              .println("\n********  The F-measure based on the BDM scores specified in the following:");
    if(verbo > 0)
      System.out.println("\nMacro averaged over " + numDoc + " documents:");
    if(verbo > 0)
      System.out
              .println("\nFor each pair of annotators, each type and each label:");
    // if(verbo>0) System.out.println("for each type:");
    for(int i = 0; i < numTypes; ++i) {
      String typeN = typeNames.get(i);
      if(verbo > 0) System.out.println("Annotation type *" + typeN + "*");
      fMeasureOverallBDM[i].macroAverage(numDoc);
      for(int j = 0; j < fMeasuresPairwiseBDM[0].length; ++j)
        fMeasuresPairwiseBDM[i][j].macroAverage(numDoc);
      if(verbo > 0) System.out.println("For each pair of annotators");
      int num11 = 0;
      for(int i1 = 0; i1 < annsArray.length; ++i1)
        for(int j = i1 + 1; j < annsArray.length; ++j) {
          if(verbo > 0)
            System.out.println("For pair (" + annsArray[i1] + ","
                    + annsArray[j] + "): "
                    + fMeasuresPairwiseBDM[i][num11].printResults());
          ++num11;
        }
      if(verbo > 1) {
        isUsingLabel = false;
        if(annsTypes.get(typeN) != null && annsTypes.get(typeN) != "")
          isUsingLabel = true;
        /*
         * if(isUsingLabel) {
         * 
         * for(int i1=0; i1<keyList.size(); ++i1) { String key =
         * keyList.get(i1); if(key.contains("):"+typeN+"->")) {
         * fMeasuresPairwiseLabelBDM.get(key).macroAverage(numDoc); String
         * pairAnns = key.substring(0,key.indexOf("):")+1); String typeAnn =
         * key.substring(key.indexOf("):")+2, key.indexOf("->")); String
         * labelAnn = key.substring(key.indexOf("->")+2);
         * System.out.println("pairAnns="
         * +pairAnns+", type="+typeAnn+", label="+labelAnn+": "
         * +fMeasuresPairwiseLabelBDM.get(key).printResults()); } } }
         */
      }
      if(verbo > 0)
        System.out.println("Overall pairs: "
                + fMeasureOverallBDM[i].printResults());
      fMeasureOverallTypesBDM.add(fMeasureOverallBDM[i]);
    }
    fMeasureOverallTypesBDM.macroAverage(numTypes);
    if(verbo > 0)
      System.out.println("Overall pairs and types: "
              + fMeasureOverallTypesBDM.printResults());
    if(verbo > 0) System.out.println("\n ********");
  }

  /** Read the BDM scores from the file to the Hashmap */
  void read(URL bdmFile, HashMap<String, Float> conceptNames2BDM) {
    try {
      BufferedReader bdmResultsReader =
              new BufferedReader(new InputStreamReader(new FileInputStream(
                      new File(bdmFile.toURI())), "UTF-8"));
      bdmResultsReader.readLine(); // read the first line as the header
      // line.
      String oneLine = bdmResultsReader.readLine();
      while(oneLine != null) {
        String[] terms = oneLine.split(", ");
        // int leftB = terms[0].indexOf("Key=");
        if(terms.length > 3) {
          String oneCon = terms[0].substring(4);
          String anoCon = terms[1].substring(9);
          String bdmS = terms[2].substring(4);
          conceptNames2BDM.put(oneCon + ", " + anoCon, new Float(bdmS));
          // System.out.println("("+oneCon+","+anoCon+"), bdm="+bdmS);
        } else {
          this.isUsingBDM = false;
          if(bdmFile != null)
            System.out.println("The file " + bdmFile.toString()
                    + " is not a BDM results file!");
          else System.out.println("There is no BDM results file specified!");
          break;
        }
        oneLine = bdmResultsReader.readLine();
      }
      bdmResultsReader.close();
      return;
    } catch(UnsupportedEncodingException e) {
      this.isUsingBDM = false;
      System.out
              .println("There is something wrong with the BDM file. The BDM score cannot be used!");
      e.printStackTrace();
    } catch(FileNotFoundException e) {
      this.isUsingBDM = false;
      System.out
              .println("There is something wrong with the BDM file. The BDM score cannot be used!");
      e.printStackTrace();
    } catch(URISyntaxException e) {
      this.isUsingBDM = false;
      System.out
              .println("There is something wrong with the BDM file. The BDM score cannot be used!");
      e.printStackTrace();
    } catch(IOException e) {
      this.isUsingBDM = false;
      System.out
              .println("There is something wrong with the BDM file. The BDM score cannot be used!");
      e.printStackTrace();
    }
  }

  /**
   * Compute the F-measures based on BDM by computing the pairwise fmeasure for
   * annotators.
   */
  public void computeFmeasuresBDM(int iIndex, IaaCalculation iaaC,
          String typeN, String[] labels, String[] annsArray) {
    // Create one fmeasure object for each pair of annotators and each label
    int num1 = iaaC.numAnnotators * (iaaC.numAnnotators - 1) / 2;
    FMeasure[][] fMeasures = new FMeasure[num1][iaaC.numLabels];
    for(int i = 0; i < num1; ++i) {
      // for(int j = 0; j < iaaC.numLabels; ++j) {
      int j = 0;
      fMeasures[i][j] = new FMeasure();
    }
    // Count the F-measure numbers for each case
    for(int iDoc = 0; iDoc < iaaC.numDocs; ++iDoc) {
      int num11 = 0;
      for(int iAnr1 = 0; iAnr1 < iaaC.numAnnotators; ++iAnr1)
        for(int iAnr2 = iAnr1 + 1; iAnr2 < iaaC.numAnnotators; ++iAnr2) {
          countFmeasureNumberBDM(iaaC, iaaC.annsArrArr[iDoc][iAnr1],
                  iaaC.annsArrArr[iDoc][iAnr2], fMeasures, num11);
          ++num11;
        }
    }
    // Compute the precision, recall and F1
    for(int i = 0; i < num1; ++i) {
      // for(int j = 0; j < iaaC.numLabels; ++j) {
      int j = 0;
      fMeasures[i][j].computeFmeasure();
      fMeasures[i][j].computeFmeasureLenient();
    }
    // Compute the averaged result over the pairs of annotators
    FMeasure fMAve = new FMeasure();
    FMeasure[] fMPair = new FMeasure[num1];
    if(isUsingLabel) {
      // Create one fmeasure for each pair of annotators for all labels
      for(int i = 0; i < num1; ++i) {
        fMPair[i] = new FMeasure();
        // for(int j = 0; j < iaaC.numLabels; ++j)
        int j = 0;
        fMPair[i].add(fMeasures[i][j]);
        // fMPair[i].macroAverage(numLabels);
        fMPair[i].computeFmeasure();
        fMPair[i].computeFmeasureLenient();
        fMAve.add(fMPair[i]);
      }
    } else {
      for(int i = 0; i < num1; ++i) {
        fMPair[i] = fMeasures[i][0];
        fMAve.add(fMeasures[i][0]);
      }
    }
    fMAve.macroAverage(num1);
    // this.fMeasureOverall = fMAve;
    // this.fMeasuresPairwiseLabel = fMeasures;
    // this.fMeasuresPairwise = fMPair;
    if(verbo > 0)
      System.out.println("For the annotation type *" + typeN + "*");
    if(verbo > 0) {
      printResultsPairwiseFmeasuresDBM(iaaC, fMAve, fMeasures, fMPair);
    }
    // sum the fmeasure of all documents
    fMeasureOverallBDM[iIndex].add(fMAve); // (iaaC.fMeasureOverall);
    for(int j = 0; j < fMeasuresPairwiseBDM[0].length; ++j) {
      fMeasuresPairwiseBDM[iIndex][j].add(fMPair[j]); // iaaC.fMeasuresPairwise[j]);
    }
    // add the fmeasure for each sub-type label
    if(annsTypes.get(typeN) != null && annsTypes.get(typeN) != "") {
      // for(int i1=0; i1<labels.length; ++i1) {
      int i1 = 0;
      int num11 = 0;
      for(int i11 = 0; i11 < annsArray.length; ++i11)
        for(int j11 = i11 + 1; j11 < annsArray.length; ++j11) {
          String key = typeN.concat("->" + labels[i1]);
          key = "(" + annsArray[i11] + "," + annsArray[j11] + "):" + key;
          if(!fMeasuresPairwiseLabelBDM.containsKey(key))
            fMeasuresPairwiseLabelBDM.put(key, new FMeasure());
          fMeasuresPairwiseLabelBDM.get(key).add(fMeasures[num11][i1]); // iaaC.fMeasuresPairwiseLabel[num11][i1]);
          ++num11;
        }
    }
  }

  void countFmeasureNumberBDM(IaaCalculation iaaC, AnnotationSet annsOriginal,
          AnnotationSet annsTest, FMeasure[][] fMeasures, int num11) {
    if(iaaC.isUsingLabel) {
      // HashSet<String> signSet = new HashSet<String>();
      // signSet.add(iaaC.nameClassFeat);
      // System.out.println("nameClassFeat=*"+nameClassFeat+"*");
      // Create an annotationDiffer()
      // AnnotationDiffer annDiff = new AnnotationDiffer();
      // annDiff.setSignificantFeaturesSet(signSet);
      float[] fNumbers = new float[5]; // the numbers for correct, partial
      // correct, missing and
      // spurious,
      // and the last number is n, the number of matches between key and
      // response.
      // for(int iLabel = 0; iLabel < iaaC.numLabels; ++iLabel) {
      // Get key and response annotation sets by ann type and feature
      // FeatureMap featMap = Factory.newFeatureMap();
      // featMap.put(iaaC.nameClassFeat, iaaC.labelsArr[iLabel]);
      int iLabel = 0;
      if(annsOriginal != null && annsTest != null) {
        AnnotationSet keyAnns = annsOriginal.get(iaaC.nameAnnType);
        AnnotationSet responseAnns = annsTest.get(iaaC.nameAnnType);
        // System.out.println("*** iLabel="+iLabel+", name="+iaaC.labelsArr[iLabel]+", keyS="+keyAnns.size()
        // +", resS="+responseAnns.size());
        // Apply the AnnotationDiffer()
        // annDiff.calculateDiff(keyAnns, responseAnns);
        computeFNumbersBDM(keyAnns, responseAnns, iaaC.nameClassFeat, fNumbers);
        // System.out.println("label="+labelsArr[iLabel]+", correct="+annDiff.getCorrectMatches()+"*");
        // Add the number
        fMeasures[num11][iLabel].correct += fNumbers[0]; // annDiff.getCorrectMatches();
        fMeasures[num11][iLabel].partialCor += fNumbers[1]; // annDiff
        // .getPartiallyCorrectMatches();
        fMeasures[num11][iLabel].missing += fNumbers[2]; // annDiff.getMissing();
        fMeasures[num11][iLabel].spurious += fNumbers[3]; // annDiff.getSpurious();
        fMeasures[num11][iLabel].BDMn += fNumbers[4]; // adding the
        // BDM's n
      } else if(annsOriginal == null && annsTest != null) {
        AnnotationSet responseAnns = annsTest.get(iaaC.nameAnnType);
        // Add the number
        if(responseAnns != null)
          fMeasures[num11][iLabel].spurious += responseAnns.size();
      } else if(annsOriginal != null && annsTest == null) {
        AnnotationSet keyAnns = annsOriginal.get(iaaC.nameAnnType);
        // Add the number
        if(keyAnns != null) fMeasures[num11][iLabel].missing += keyAnns.size();
      }
    } else { // because there is no label, there is no need of using the
      // BDM.
      HashSet<String> signSet = new HashSet<String>();
      AnnotationDiffer annDiff = new AnnotationDiffer();
      annDiff.setSignificantFeaturesSet(signSet);
      int iLabel = 0;
      if(annsOriginal != null && annsTest != null) {
        // Get key and response annotation sets by ann type and feature
        AnnotationSet keyAnns = annsOriginal.get(iaaC.nameAnnType);
        AnnotationSet responseAnns = annsTest.get(iaaC.nameAnnType);
        // Apply the AnnotationDiffer()
        annDiff.calculateDiff(keyAnns, responseAnns);
        // Add the number
        fMeasures[num11][iLabel].correct += annDiff.getCorrectMatches();
        fMeasures[num11][iLabel].partialCor +=
                annDiff.getPartiallyCorrectMatches();
        fMeasures[num11][iLabel].missing += annDiff.getMissing();
        fMeasures[num11][iLabel].spurious += annDiff.getSpurious();
      } else if(annsOriginal == null && annsTest != null) {
        AnnotationSet responseAnns = annsTest.get(iaaC.nameAnnType);
        // Add the number
        if(responseAnns != null)
          fMeasures[num11][iLabel].spurious += responseAnns.size();
      } else if(annsOriginal != null && annsTest == null) {
        AnnotationSet keyAnns = annsOriginal.get(iaaC.nameAnnType);
        // Add the number
        if(keyAnns != null) fMeasures[num11][iLabel].missing += keyAnns.size();
      }
    }
    // }
  }

  void computeFNumbersBDM(AnnotationSet keyAnns, AnnotationSet responseAnns,
          String nameClassFeat, float[] fNumbers) {
    Set<Annotation> matchKey = new HashSet<Annotation>();
    Set<Annotation> matchRes = new HashSet<Annotation>();
    for(int i = 0; i < fNumbers.length; ++i)
      fNumbers[i] = 0.0f;
    for(Annotation annK : keyAnns) {
      String labelKey = annK.getFeatures().get(nameClassFeat).toString();
      for(Annotation annR : responseAnns) {
        if(matchRes.contains(annR)) continue;
        String labelRes = annR.getFeatures().get(nameClassFeat).toString();
        if(annK.coextensive(annR)) { // exact matching
          float bdm = 0;
          String onePair = labelKey + ", " + labelRes;
          String anoPair = labelRes + ", " + labelKey;
          // System.out.println("!!!the two concepts *"+labelKey+"* and *"+labelRes+"*");
          if(this.conceptNames2BDM.containsKey(onePair))
            bdm = this.conceptNames2BDM.get(onePair).floatValue();
          else if(this.conceptNames2BDM.containsKey(anoPair))
            bdm = this.conceptNames2BDM.get(anoPair).floatValue();
          else System.out.println("No BDM entry for the two concepts *"
                  + labelKey + "* and *" + labelRes + "*");
          if(bdm < 0) bdm = 0; // just in case that some bdm happens to be
          // negative.
          // System.out.println("bbbbbb ("+labelKey+","+labelRes+"), bdmS="+
          // bdm);
          fNumbers[0] += bdm; // exact match
          if(bdm > 0.0f) {
            matchRes.add(annR);
            matchKey.add(annK);
            ++fNumbers[4];
          }
        } else if(annK.overlaps(annR)) { // partial matching
          float bdm = 0;
          String onePair = labelKey + ", " + labelRes;
          String anoPair = labelRes + ", " + labelKey;
          if(this.conceptNames2BDM.containsKey(onePair))
            bdm = this.conceptNames2BDM.get(onePair).floatValue();
          else if(this.conceptNames2BDM.containsKey(anoPair))
            bdm = this.conceptNames2BDM.get(anoPair).floatValue();
          else System.out.println("No BDM entry for the two concepts *"
                  + labelKey + "* and *" + labelRes + "*");
          fNumbers[1] += bdm; // partial match
          if(bdm > 0) {
            matchRes.add(annR);
            matchKey.add(annK);
            ++fNumbers[4];
          }
        }
      }// end of the loop for response ann.
    }// end of the loop for the key ann.
    fNumbers[2] = keyAnns.size() - matchKey.size(); // for Missing
    fNumbers[3] = responseAnns.size() - matchRes.size(); // for Spurious
  }

  void printResultsPairwiseFmeasuresDBM(IaaCalculation iaaC,
          FMeasure fMeasureOverall, FMeasure[][] fMeasuresPairwiseLabel,
          FMeasure[] fMeasuresPairwise) {
    // Print out the FMeasures for pairwise comparison
    int num1 = iaaC.numAnnotators * (iaaC.numAnnotators - 1) / 2;
    System.out
            .println("\n ********  The F-measures based on the BDM scores specified in the following: ");
    System.out.println("F-measures averaged over " + num1
            + " pairs of annotators.");
    System.out.println(fMeasureOverall.printResults());
    System.out.println("For each pair of annotators:");
    int num11 = 0;
    for(int i = 0; i < iaaC.numAnnotators; ++i)
      for(int j = i + 1; j < iaaC.numAnnotators; ++j) {
        System.out.println("(" + i + "," + j + "): "
                + fMeasuresPairwise[num11].printResults());
        ++num11;
      }
    /*
     * if(isUsingLabel) { if(verbo >= 2) { System.out
     * .println("For each pair of annotators, and for each label:"); num11 = 0;
     * for(int i = 0; i < iaaC.numAnnotators; ++i) for(int j = i + 1; j <
     * iaaC.numAnnotators; ++j) { for(int iL = 0; iL < iaaC.numLabels; ++iL)
     * System.out.println("(" + i + "," + j + "), label= " + iaaC.labelsArr[iL]
     * + ": " + fMeasuresPairwiseLabel[num11][iL].printResults()); ++num11; } }
     * }
     */
    System.out.println("\n ********\n");
  }

  public void setAnnSetsForIaa(String annSetSeq) {
    this.annSetsForIaa = annSetSeq;
  }

  public String getAnnSetsForIaa() {
    return this.annSetsForIaa;
  }

  public void setAnnTypesAndFeats(String annTypeSeq) {
    this.annTypesAndFeats = annTypeSeq;
  }

  public String getAnnTypesAndFeats() {
    return this.annTypesAndFeats;
  }

  public void setVerbosity(String v) {
    this.verbosity = v;
  }

  public String getVerbosity() {
    return this.verbosity;
  }

  public void setMeasureType(MeasureType v) {
    this.measureType = v;
  }

  public MeasureType getMeasureType() {
    return this.measureType;
  }

  public void setBdmScoreFile(URL bf) {
    this.bdmScoreFile = bf;
  }

  public URL getBdmScoreFile() {
    return this.bdmScoreFile;
  }
}
