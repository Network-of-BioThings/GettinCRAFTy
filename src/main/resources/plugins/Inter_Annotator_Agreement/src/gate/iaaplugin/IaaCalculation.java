/**
 *
 *  Copyright (c) 1995-2012, The University of Sheffield. See the file
 *  COPYRIGHT.txt in the software or at http://gate.ac.uk/gate/COPYRIGHT.txt
 *
 *  This file is part of GATE (see http://gate.ac.uk/), and is free
 *  software, licenced under the GNU Library General Public License,
 *  Version 2, June 1991 (in the distribution as file licence.html,
 *  and also available at http://gate.ac.uk/gate/licence.html).
 *
 *  $Id: IaaCalculation.java 12006 2009-12-01 17:24:28Z thomas_heitz $
 */

package gate.iaaplugin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import gate.Annotation;
import gate.AnnotationSet;
import gate.Factory;
import gate.FeatureMap;
import gate.util.AnnotationDiffer;

/**
 * 
 * Compute the IAA(inter-annotator agreements), including the observed
 * agreement, category specific agreeement, several variants of Kappa such as
 * Cohen's Kappa, and the F-measures between the annotators (see the GATE user
 * Guide for detailed explanations about those measures).
 * 
 */
public class IaaCalculation {
  /** Non-category string. */
  public static String NONCAT = "Non-cat";
  /** Number of annotators. */
  public int numAnnotators;
  /** Number of documents. */
  public int numDocs;
  /** Number of labels. */
  public int numLabels;
  /** Name of label feature. */
  public String nameClassFeat;
  /** Array of labels. */
  public String[] labelsArr;
  /** Name of annotation type. */
  public String nameAnnType;
  /** Using label or not. */
  public boolean isUsingLabel = false;
  /**
   * Array of annotation sets, first dimension is for document, and second
   * dimension is for annotator.
   */
  public AnnotationSet[][] annsArrArr;
  /** The overall contingency table. */
  public ContingencyTable contingencyOverall;
  /** The contingency table for each pair of annotator. */
  public ContingencyTable[] contingencyTables=null;
  /** The overall F-measure. */
  public FMeasure fMeasureOverall;
  /** Fmeaures for each pair of annotator and each label. */
  public FMeasure[][] fMeasuresPairwiseLabel=null;
  /** Fmeaures for each pair of annotator and over all labels. */
  public FMeasure[] fMeasuresPairwise=null;
  /** The verbosity level for print the measures. */
  int verbosity = 2;
  /** The name of annotators. */
  public String [] annotatorNames = null;

  /** Constractor by giving the annotation sets and a list of labels. */
  public IaaCalculation(String nameAnnT, String nameF, String[] labels,
    AnnotationSet[][] annsA2, int verbsy) {
    this.nameAnnType = nameAnnT;
    this.nameClassFeat = nameF;
    this.labelsArr = labels;
    isUsingLabel = true;
    this.annsArrArr = annsA2;
    this.numLabels = labels.length;
    this.numDocs = annsA2.length; // the number of documents
    this.numAnnotators = annsA2[0].length; // the number of annotators
    this.verbosity = verbsy;
    if(this.numAnnotators < 2) {
      if(this.verbosity>0) System.out
        .println("Warning: The IAA calculation needs at least two annotation sets. ");
    }
    checkIsAnnsMissing();
  }

  /** Constructor by giving the annotation sets and the name of annotation type. */
  public IaaCalculation(String nameAnnT, AnnotationSet[][] annsA2, int verbsy) {
    this.nameAnnType = nameAnnT;
    this.numLabels = 1;
    this.labelsArr = new String[this.numLabels];
    this.labelsArr[0] = "Anns";
    isUsingLabel = false;
    this.annsArrArr = annsA2;
    this.numDocs = annsA2.length; // the number of documents
    this.numAnnotators = annsA2[0].length; // the number of annotators
    this.verbosity = verbsy;
    if(this.numAnnotators < 2) {
      if(verbosity>0) System.out
        .println("Warning: the IAA calculation needs at least two annotation sets. ");
    }
    checkIsAnnsMissing();
  }

  /** Check if some annotation set for some document is missed. */
  public void checkIsAnnsMissing() {
    boolean isMissing = false;
    for(int i = 0; i < numDocs; ++i)
      for(int j = 0; j < numAnnotators; ++j)
        if(annsArrArr[i][j] == null) {
          if(verbosity>0) System.out.println("Warning: The annotation set of the "
            + "Annotator " + j + " on the document " + i + " is missed!");
          isMissing = true;
        }
    if(isMissing){
      if(verbosity>0) System.out.println("There should be " + numAnnotators
        + " Annotator(s) and " + numDocs + " document(s).");
    }
    else {
      if(verbosity>0) System.out.println("Compute the IAA for " + numAnnotators + " Annotator(s) and on "
      + numDocs + " document(s).");
    }
  }

  /** Compute the pairwise kappa for annotators. */
  public void pairwiseIaaKappa() {
    // Create one contingency object for each pair of annotators and all labels
    int num1 = numAnnotators * (numAnnotators - 1) / 2; // Number of pairs of
    // annotators
    contingencyTables = new ContingencyTable[num1];
    for(int i = 0; i < num1; ++i) {
      contingencyTables[i] = new ContingencyTable(numLabels + 1); // Count the
      // non-label
    }
    // Count the numbers for contingency table
    for(int iDoc = 0; iDoc < numDocs; ++iDoc) {
      int num11 = 0;
      for(int iAnr1 = 0; iAnr1 < numAnnotators; ++iAnr1)
        for(int iAnr2 = iAnr1 + 1; iAnr2 < numAnnotators; ++iAnr2) {
          countContingencyTableNumber(annsArrArr[iDoc][iAnr1],
            annsArrArr[iDoc][iAnr2], contingencyTables, num11);
          ++num11;
        }
    }
    // Compute the Cohen's kappa, observed agreements, and S&C's kappa
    for(int i = 0; i < num1; ++i)
      contingencyTables[i].computeKappaPairwise();
    // Compute the overall results
    ContingencyTable conOverall = new ContingencyTable(numLabels + 1);
    for(int i = 0; i < num1; ++i)
      conOverall.add(contingencyTables[i]);
    conOverall.macroAveraged(num1);
    
    contingencyOverall = conOverall;
  }
  /** Print out the results for pairwise Kappas. */
  public void printResultsPairwiseIaa() {
    // Print out the results
    if(verbosity >= 1) {
      int num1 = numAnnotators * (numAnnotators - 1) / 2; // Number of pairs of
      // annotators
      System.out.println("Overall results macro-averaged over " + num1
        + " pairs:");
      System.out.println(contingencyOverall.printResultsPairwise());
      System.out.println("Results for each pair of annotators:");
      int num11 = 0;
      for(int i = 0; i < numAnnotators; ++i)
        for(int j = i + 1; j < numAnnotators; ++j) {
          System.out.println("(" + this.annotatorNames[i] + "," + 
            this.annotatorNames[j] + "): "
            + contingencyTables[num11].printResultsPairwise());
          System.out.println("Confusion Matrix:");
          System.out.println(contingencyTables[num11]
            .printConfusionMatrix(this.labelsArr));
          if(verbosity >= 2) {
            System.out.println("Specific agreement for each label:");
            for(int jj = 0; jj < numLabels; ++jj)
              System.out.println("label=" + labelsArr[jj]
                + ": positive specific agreement = "
                + contingencyTables[num11].sAgreements[jj][0]
                + "; negative specific agreement = "
                + contingencyTables[num11].sAgreements[jj][1]);
            System.out.println("label=" + IaaCalculation.NONCAT
              + ": positive specific agreement = "
              + contingencyTables[num11].sAgreements[numLabels][0]
              + "; negative specific agreement = "
              + contingencyTables[num11].sAgreements[numLabels][1]);
          }
          ++num11;
        }
    }
  }
  /**
   * Compute the allway kappa, i.e. for more than two annotator, with extended
   * formula.
   */
  public void allwayIaaKappa() {
    // Create the contigency object for all the annotators and labels.
    ContingencyTable contingencyT = new ContingencyTable(numLabels + 1,
      numAnnotators);
    // Get the statistics for all way kappa formula
    // First create a map of label to its dimension in label array
    HashMap<String, Integer> id2Label = new HashMap<String, Integer>();
    for(int i = 0; i < numLabels; ++i)
      id2Label.put(labelsArr[i], new Integer(i));
    // The statistical quantity in the upper part of the formula
    long ySum = 0; // One term in the extended kappa formula.
    long numInstances = 0; // Total number of instances
    long numAgreements = 0; // Total number of instances agreed by all
    // annotators
    long[] numJudgementsCat = new long[numLabels + 1];
    boolean isUsingNonlabel = false;
    for(int iDoc = 0; iDoc < numDocs; ++iDoc) {// for each document
      // For each instance in one document
      if(annsArrArr[iDoc][0] != null) {
        for(Annotation ann0 : annsArrArr[iDoc][0]) {
          ++numInstances;
          int[] yc = new int[numLabels + 1];
          // First check the first annotation set
          Object obj0 = ann0.getFeatures().get(nameClassFeat);
          if(obj0 != null) {
            String label0 = obj0.toString();
            if(id2Label.containsKey(label0)) {
              ++yc[id2Label.get(label0).intValue()];
              ++(contingencyT.assignmentMatrix[id2Label.get(label0).intValue()][0]);
            }
            else {
              ++yc[numLabels];
              ++(contingencyT.assignmentMatrix[numLabels][0]);
            }
          }
          else {
            ++yc[numLabels];
            ++(contingencyT.assignmentMatrix[numLabels][0]);
          }
          // Then check other annotation sets
          Long sOffset = ann0.getStartNode().getOffset();
          Long eOffset = ann0.getEndNode().getOffset();
          for(int iJud = 1; iJud < numAnnotators; ++iJud) {
            // ?? how to determine if two annotation is the same
            if(annsArrArr[iDoc][iJud] != null) {
              AnnotationSet anns = annsArrArr[iDoc][iJud].get(sOffset, eOffset);
              if(!anns.isEmpty()) {
                for(Annotation ann2 : anns) {
                  Object obj3 = ann2.getFeatures().get(nameClassFeat);
                  if(obj3 != null) {
                    String label3 = obj3.toString();
                    if(id2Label.containsKey(label3)) {
                      ++yc[id2Label.get(label3).intValue()];
                      ++(contingencyT.assignmentMatrix[id2Label.get(label3)
                        .intValue()][iJud]);
                    }
                    else {
                      ++yc[numLabels];
                      ++(contingencyT.assignmentMatrix[numLabels][iJud]);
                    }
                  }
                  else {
                    ++yc[numLabels];
                    ++(contingencyT.assignmentMatrix[numLabels][iJud]);
                  }
                  break;
                }
              }// else System.out
              // .println("Warning: cannot compute kappa because the instances
              // are not the same for every annotators.");
            }
          }// end of the loop for judges iJud
          // Check if the current instance has been agreed by all annotators
          for(int iL = 0; iL < numLabels + 1; ++iL)
            if(yc[iL] == numAnnotators) {
              ++numAgreements;
              break;
            }
          // Finally compute the statistics
          for(int iL = 0; iL < numLabels + 1; ++iL) {
            ySum += yc[iL] * yc[iL];
            numJudgementsCat[iL] += yc[iL];
          }
          if(yc[numLabels] > 0 && !isUsingNonlabel) isUsingNonlabel = true;
        }
      }
    } // end of loop for document
    // Compute the all way kappa
    contingencyT.computeAllwayKappa(ySum, numInstances, numAgreements,
      numJudgementsCat, isUsingNonlabel);
    contingencyOverall = contingencyT;
  }
  /** Print out the results for allway Kappa. */
  public void printAllwayIaa() {
    // Print out the results
    if(verbosity >= 1) {
      System.out.println("Overall results (allWay) over " + numAnnotators
        + " annotators:");
      System.out.println(contingencyOverall.printResultsAllway());
    }
  }
  /** Compute the pairwise fmeasure for annotators. */
  public void pairwiseIaaFmeasure() {
    // Create one fmeasure object for each pair of annotators and each label
    int num1 = numAnnotators * (numAnnotators - 1) / 2;
    FMeasure[][] fMeasures = new FMeasure[num1][numLabels];
    for(int i = 0; i < num1; ++i)
      for(int j = 0; j < numLabels; ++j) {
        fMeasures[i][j] = new FMeasure();
      }
    // Count the F-measure numbers for each case
    for(int iDoc = 0; iDoc < numDocs; ++iDoc) {
      int num11 = 0;
      for(int iAnr1 = 0; iAnr1 < numAnnotators; ++iAnr1)
        for(int iAnr2 = iAnr1 + 1; iAnr2 < numAnnotators; ++iAnr2) {
          countFmeasureNumber(annsArrArr[iDoc][iAnr1], annsArrArr[iDoc][iAnr2],
            fMeasures, num11);
          ++num11;
        }
    }
    // Compute the precision, recall and F1
    for(int i = 0; i < num1; ++i)
      for(int j = 0; j < numLabels; ++j) {
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
        for(int j = 0; j < numLabels; ++j)
          fMPair[i].add(fMeasures[i][j]);
        // fMPair[i].macroAverage(numLabels);
        fMPair[i].computeFmeasure();
        fMPair[i].computeFmeasureLenient();
        fMAve.add(fMPair[i]);
      }
    }
    else {
      for(int i = 0; i < num1; ++i) {
        fMPair[i] = fMeasures[i][0];
        fMAve.add(fMeasures[i][0]);
      }
    }
    fMAve.macroAverage(num1);
    this.fMeasureOverall = fMAve;
    this.fMeasuresPairwiseLabel = fMeasures;
    this.fMeasuresPairwise = fMPair;
  }
  /** Print out the results for pairwise F-measures. */
  public void printResultsPairwiseFmeasures() {
    //  Print out the FMeasures for pairwise comparison
    if(verbosity >= 1) {
      int num1 = numAnnotators * (numAnnotators - 1) / 2;
      System.out.println("F-measures averaged over " + num1
        + " pairs of annotators.");
      System.out.println(this.fMeasureOverall.printResults());
      System.out.println("For each pair of annotators:");
      int num11 = 0;
      for(int i = 0; i < numAnnotators; ++i)
        for(int j = i + 1; j < numAnnotators; ++j) {
          System.out.println("(" + this.annotatorNames[i] + "," + 
            this.annotatorNames[j] + "): "
            + this.fMeasuresPairwise[num11].printResults());
          ++num11;
        }
      if(isUsingLabel) {
        if(verbosity >= 2) {
          System.out
            .println("For each pair of annotators, and for each label:");
          num11 = 0;
          for(int i = 0; i < numAnnotators; ++i)
            for(int j = i + 1; j < numAnnotators; ++j) {
              for(int iL = 0; iL < numLabels; ++iL)
                System.out.println("(" + this.annotatorNames[i] + "," + 
                  this.annotatorNames[j] + "), label= "
                  + labelsArr[iL] + ": " + this.fMeasuresPairwiseLabel[num11][iL].printResults());
              ++num11;
            }
        }
      }
    }
  }

  /** Compute the fmeasure for annotators against one reference annotations. */
  public void allwayIaaFmeasure(AnnotationSet[] refAnns) {
    // Create one fmeasure object for each annotator and each label
    int num1 = numAnnotators;
    FMeasure[][] fMeasures = new FMeasure[num1][numLabels];
    for(int i = 0; i < num1; ++i)
      for(int j = 0; j < numLabels; ++j) {
        fMeasures[i][j] = new FMeasure();
      }
    // Count the F-measure numbers for each case
    for(int iDoc = 0; iDoc < numDocs; ++iDoc) {
      int num11 = 0;
      for(int iAnr1 = 0; iAnr1 < numAnnotators; ++iAnr1) {
        countFmeasureNumber(refAnns[iDoc], annsArrArr[iDoc][iAnr1], fMeasures,
          num11);
        ++num11;
      }
    }
    // Compute the precision, recall and F1
    for(int i = 0; i < num1; ++i)
      for(int j = 0; j < numLabels; ++j) {
        fMeasures[i][j].computeFmeasure();
        fMeasures[i][j].computeFmeasureLenient();
      }
    // Compute the averaged result over the pairs of annotators
    FMeasure fMAve = new FMeasure();
    FMeasure[] fMPair = new FMeasure[num1];
    if(isUsingLabel) {
      // Create one fmeasure for each pair of annotators
      for(int i = 0; i < num1; ++i) {
        fMPair[i] = new FMeasure();
        for(int j = 0; j < numLabels; ++j)
          fMPair[i].add(fMeasures[i][j]);
        // fMPair[i].macroAverage(numLabels);
        fMPair[i].computeFmeasure();
        fMPair[i].computeFmeasureLenient();
        fMAve.add(fMPair[i]);
      }
    }
    else {
      for(int i = 0; i < num1; ++i) {
        fMAve.add(fMeasures[i][0]);
        fMPair[i] = fMeasures[i][0];
      }
    }
    fMAve.macroAverage(num1);
    this.fMeasureOverall = fMAve;
    this.fMeasuresPairwise = fMPair;
    this.fMeasuresPairwiseLabel = fMeasures;
  }
  /** Print out the results for allway F-measures. */
  public void printResultsAllwayFmeasures() {
    //  Print out the FMeasures for pairwise comparison
    if(verbosity >= 1) {
      int num1 = numAnnotators;
      System.out
        .println("F-measures against the reference annotations provided:");
      System.out.println("Macro averaged over " + num1 + " pairs:");
      System.out.println(this.fMeasureOverall.printResults());
      System.out.println("For each annotator:");
      for(int i = 0; i < numAnnotators; ++i)
        System.out.println("Annotator: " + this.annotatorNames[i] + ": " + this.fMeasuresPairwise[i].printResults());
      if(isUsingLabel) {
        if(verbosity >= 2) {
          System.out
            .println("For each pair of annotators, and for each label:");
          for(int i = 0; i < numAnnotators; ++i) {
            for(int iL = 0; iL < numLabels; ++iL)
              System.out.println("Annotator: " + this.annotatorNames[i] + ", label= " + labelsArr[iL]
                + ": " + this.fMeasuresPairwiseLabel[i][iL].printResults());
          }
        }
      }
    }
  }

  /** Count the f-measure numbers given two annotation set. */
  public void countFmeasureNumber(AnnotationSet annsOriginal,
    AnnotationSet annsTest, FMeasure[][] fMeasures, int num11) {
    // if(annsOriginal != null && annsTest != null) {
    if(isUsingLabel) {
      HashSet<String> signSet = new HashSet<String>();
      signSet.add(nameClassFeat);
      
      //System.out.println("nameClassFeat=*"+nameClassFeat+"*");
      
      // Create an annotationDiffer()
      AnnotationDiffer annDiff = new AnnotationDiffer();
      annDiff.setSignificantFeaturesSet(signSet);
      for(int iLabel = 0; iLabel < numLabels; ++iLabel) {
        // Get key and response annotation sets by ann type and feature
        FeatureMap featMap = Factory.newFeatureMap();
        featMap.put(nameClassFeat, labelsArr[iLabel]);
        if(annsOriginal != null && annsTest != null) {
          AnnotationSet keyAnns = annsOriginal.get(nameAnnType, featMap);
          AnnotationSet responseAnns = annsTest.get(nameAnnType, featMap);
          // Apply the AnnotationDiffer()
          annDiff.calculateDiff(keyAnns, responseAnns);
          
          //System.out.println("label="+labelsArr[iLabel]+", correct="+annDiff.getCorrectMatches()+"*");
          
          // Add the number
          fMeasures[num11][iLabel].correct += annDiff.getCorrectMatches();
          fMeasures[num11][iLabel].partialCor += annDiff
            .getPartiallyCorrectMatches();
          fMeasures[num11][iLabel].missing += annDiff.getMissing();
          fMeasures[num11][iLabel].spurious += annDiff.getSpurious();
        }
        else if(annsOriginal == null && annsTest != null) {
          AnnotationSet responseAnns = annsTest.get(nameAnnType, featMap);
          // Add the number
          if(responseAnns != null)
            fMeasures[num11][iLabel].spurious += responseAnns.size();
        }
        else if(annsOriginal != null && annsTest == null) {
          AnnotationSet keyAnns = annsOriginal.get(nameAnnType, featMap);
          // Add the number
          if(keyAnns != null)
            fMeasures[num11][iLabel].missing += keyAnns.size();
        }
      }
    }
    else {
      HashSet<String> signSet = new HashSet<String>();
      AnnotationDiffer annDiff = new AnnotationDiffer();
      annDiff.setSignificantFeaturesSet(signSet);
      int iLabel = 0;
      if(annsOriginal != null && annsTest != null) {
        // Get key and response annotation sets by ann type and feature
        AnnotationSet keyAnns = annsOriginal.get(nameAnnType);
        AnnotationSet responseAnns = annsTest.get(nameAnnType);
        // Apply the AnnotationDiffer()
        annDiff.calculateDiff(keyAnns, responseAnns);
        // Add the number
        fMeasures[num11][iLabel].correct += annDiff.getCorrectMatches();
        fMeasures[num11][iLabel].partialCor += annDiff
          .getPartiallyCorrectMatches();
        fMeasures[num11][iLabel].missing += annDiff.getMissing();
        fMeasures[num11][iLabel].spurious += annDiff.getSpurious();
      }
      else if(annsOriginal == null && annsTest != null) {
        AnnotationSet responseAnns = annsTest.get(nameAnnType);
        // Add the number
        if(responseAnns != null)
          fMeasures[num11][iLabel].spurious += responseAnns.size();
      }
      else if(annsOriginal != null && annsTest == null) {
        AnnotationSet keyAnns = annsOriginal.get(nameAnnType);
        // Add the number
        if(keyAnns != null)
          fMeasures[num11][iLabel].missing += keyAnns.size();
      }
    }
    // }
  }

  /** Count the contingency numbers given two annotation set. */
  public void countContingencyTableNumber(AnnotationSet annsOriginal,
    AnnotationSet annsTest, ContingencyTable[] contingencyTableNumbers,
    int num11) {
    // if(annsOriginal != null && annsTest != null) {
    HashSet<String> signSet = new HashSet<String>();
    // signSet.add(nameClassFeat);
    if(isUsingLabel) {
      // For each pair of categories
      for(int iLabel = 0; iLabel < numLabels; ++iLabel) {
        for(int iL2 = 0; iL2 < numLabels; ++iL2) {
          // Get annotation set containing one label
          if(annsOriginal != null && annsTest != null) {
            FeatureMap featMap = Factory.newFeatureMap();
            featMap.put(nameClassFeat, labelsArr[iLabel]);
            AnnotationSet keyAnns = annsOriginal.get(nameAnnType, featMap);
            // Get annotation set containing another label
            featMap.clear();
            featMap.put(nameClassFeat, labelsArr[iL2]);
            AnnotationSet responseAnns = annsTest.get(nameAnnType, featMap);
            // Apply the AnnotationDiffer(), not sure if span is harmful for
            // some
            // application
            AnnotationDiffer annDiff = new AnnotationDiffer();
            // An empty signSet, just count the span
            annDiff.setSignificantFeaturesSet(signSet);
            annDiff.calculateDiff(keyAnns, responseAnns);
            // Add the number
            contingencyTableNumbers[num11].confusionMatrix[iLabel][iL2] += annDiff
              .getCorrectMatches();
            // For the un-matched annotations
            // if(iLabel==iL2) {
            // contingencyTableNumbers[num11].confusionMatrix[numLabels][iL2] +=
            // annDiff.getSpurious();
            // contingencyTableNumbers[num11].confusionMatrix[iLabel][numLabels]
            // +=
            // annDiff.getMissing();
            // }
          }
        }
      }
      // For the pair of the non-category and one category
      HashSet<String> id2Label = new HashSet<String>();
      for(int i = 0; i < numLabels; ++i)
        id2Label.add(labelsArr[i]);
      // Extract those annotation without label and with label but without a
      // value in label list as non-label set.
      HashSet<Annotation> keyAnnsNonlabel = new HashSet<Annotation>();
      HashSet<Annotation> testAnnsNonlabel = new HashSet<Annotation>();
      if(annsOriginal != null && annsTest != null) {
        for(Annotation ann : annsOriginal) {
          if(!ann.getFeatures().containsKey(nameClassFeat)
            || !id2Label.contains(ann.getFeatures().get(nameClassFeat)))
            keyAnnsNonlabel.add(ann);
        }
        for(Annotation ann : annsTest) {
          if(!ann.getFeatures().containsKey(nameClassFeat)
            || !id2Label.contains(ann.getFeatures().get(nameClassFeat)))
            testAnnsNonlabel.add(ann);
        }
        for(int iLabel = 0; iLabel < numLabels; ++iLabel) {
          FeatureMap featMap = Factory.newFeatureMap();
          featMap.put(nameClassFeat, labelsArr[iLabel]);
          // for the key set with label
          AnnotationSet keyAnns = annsOriginal.get(nameAnnType, featMap);
          AnnotationDiffer annDiff = new AnnotationDiffer();
          // An empty signSet, just count the span
          annDiff.setSignificantFeaturesSet(signSet);
          annDiff.calculateDiff(keyAnns, testAnnsNonlabel);
          contingencyTableNumbers[num11].confusionMatrix[iLabel][numLabels] += annDiff
            .getCorrectMatches();
          // For the test set with label
          AnnotationSet testAnns = annsTest.get(nameAnnType, featMap);
          AnnotationDiffer annDiff1 = new AnnotationDiffer();
          // An empty signSet, just count the span
          annDiff1.setSignificantFeaturesSet(signSet);
          annDiff1.calculateDiff(testAnns, keyAnnsNonlabel);
          contingencyTableNumbers[num11].confusionMatrix[numLabels][iLabel] += annDiff1
            .getCorrectMatches();
        }
        // For two non-category
        AnnotationDiffer annDiff = new AnnotationDiffer();
        // An empty signSet, just count the span
        annDiff.setSignificantFeaturesSet(signSet);
        annDiff.calculateDiff(keyAnnsNonlabel, testAnnsNonlabel);
        contingencyTableNumbers[num11].confusionMatrix[numLabels][numLabels] += annDiff
          .getCorrectMatches();
      }
      else if(annsOriginal == null && annsTest != null) {
        for(Annotation ann : annsTest) {
          if(!ann.getFeatures().containsKey(nameClassFeat)
            || !id2Label.contains(ann.getFeatures().get(nameClassFeat)))
            testAnnsNonlabel.add(ann);
        }
        for(int iLabel = 0; iLabel < numLabels; ++iLabel) {
          FeatureMap featMap = Factory.newFeatureMap();
          featMap.put(nameClassFeat, labelsArr[iLabel]);
          // For the test set with label
          AnnotationSet testAnns = annsTest.get(nameAnnType, featMap);
          if(testAnns != null)
            contingencyTableNumbers[num11].confusionMatrix[numLabels][iLabel] += testAnns
              .size();
        }
        // For two non-category
        if(testAnnsNonlabel !=null)
          contingencyTableNumbers[num11].confusionMatrix[numLabels][numLabels] += testAnnsNonlabel
            .size();
      }
      else if(annsOriginal != null && annsTest == null) {
        for(Annotation ann : annsOriginal) {
          if(!ann.getFeatures().containsKey(nameClassFeat)
            || !id2Label.contains(ann.getFeatures().get(nameClassFeat)))
            keyAnnsNonlabel.add(ann);
        }
        for(int iLabel = 0; iLabel < numLabels; ++iLabel) {
          FeatureMap featMap = Factory.newFeatureMap();
          featMap.put(nameClassFeat, labelsArr[iLabel]);
          // For the test set with label
          AnnotationSet keyAnns = annsOriginal.get(nameAnnType, featMap);
          if(keyAnns != null)
            contingencyTableNumbers[num11].confusionMatrix[iLabel][numLabels] += keyAnns
              .size();
        }
        // For two non-category
        if(keyAnnsNonlabel != null)
        contingencyTableNumbers[num11].confusionMatrix[numLabels][numLabels] += keyAnnsNonlabel
          .size();
      }
    }
    else { // if not use labels
      int numL = 0;
      // For each pair of categories
      if(annsOriginal != null && annsTest != null) {
        AnnotationSet keyAnns = annsOriginal.get(nameAnnType);
        // Get annotation set containing another label
        AnnotationSet responseAnns = annsTest.get(nameAnnType);
        // Apply the AnnotationDiffer(), not sure if span is harmful for some
        // application
        AnnotationDiffer annDiff = new AnnotationDiffer();
        // An empty signSet, just count the span
        annDiff.setSignificantFeaturesSet(signSet);
        annDiff.calculateDiff(keyAnns, responseAnns);
        // Add the number
        contingencyTableNumbers[num11].confusionMatrix[numL][numL] += annDiff
          .getCorrectMatches();
        // For the un-matched annotations
        contingencyTableNumbers[num11].confusionMatrix[numLabels][numL] += annDiff
          .getSpurious();
        contingencyTableNumbers[num11].confusionMatrix[numL][numLabels] += annDiff
          .getMissing();
      }
      else if(annsOriginal == null && annsTest != null) {
        // Get annotation set containing another label
        AnnotationSet responseAnns = annsTest.get(nameAnnType);
        // For the un-matched annotations
        if(responseAnns != null)
          contingencyTableNumbers[num11].confusionMatrix[numLabels][numL] += responseAnns
            .size();
      }
      else if(annsOriginal != null && annsTest == null) {
        // Get annotation set containing another label
        AnnotationSet keyAnns = annsOriginal.get(nameAnnType);
        // For the un-matched annotations
        if(keyAnns != null)
          contingencyTableNumbers[num11].confusionMatrix[numL][numLabels] += keyAnns
            .size();
      }
    }
  }

  /**
   * Check if the annotation task is suitable for computing kappa by checking if
   * the annotation sets contain the same annotations.
   */
  public static boolean isSameInstancesForAnnotators(AnnotationSet[] annsA, int vsy) {
    int numAnnotators = annsA.length;
    if(annsA[0] == null) return false;
    for(Annotation ann : annsA[0]) {
      for(int iJud = 1; iJud < numAnnotators; ++iJud) {
        if(annsA[iJud] == null) return false;
        boolean isContained = false;
        for(Annotation ann1 : annsA[iJud]) {
          // If the ann is not the same
          if(ann.coextensive(ann1)) {
            isContained = true;
            break;
          }
        }
        if(!isContained) {
          if(vsy>0)
          System.out.println("The " + iJud + " annotator cause different");
          return false;
        }
      }// end of the loop for annotators
    }// end of loop for each annotation in one document
    // If the annotated instances are the same for every annotators.
    return true;
  }

  /** Collect the labels into a list and sort it alphabetically. */
  public static ArrayList<String> collectLabels(AnnotationSet[][] annsA2,
    String nameF) {
    ArrayList<String> labelsSet = new ArrayList<String>();
    int numD = annsA2.length;
    int numJ = annsA2[0].length;
    for(int iDoc = 0; iDoc < numD; ++iDoc)
      for(int iJud = 0; iJud < numJ; ++iJud) {
        if(annsA2[iDoc][iJud] != null)
          for(Annotation ann : annsA2[iDoc][iJud]) {
            if(ann.getFeatures().containsKey(nameF)) {
              String label = ann.getFeatures().get(nameF).toString();
              if(label != null && !labelsSet.contains(label))
                labelsSet.add(label);
            }
          }// end of the loop for annotation
      }// end of the loop for annotators
    // If the annotated instances are the same for every annotators.
    Collections.sort(labelsSet);
    return labelsSet;
  }
  
  public void setAnnotatorNames(String [] names) {
    this.annotatorNames = names;
  }
}