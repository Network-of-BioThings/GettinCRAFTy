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
 *  $Id: ContingencyTable.java 12128 2010-01-05 13:47:20Z ggorrell $
 */

package gate.iaaplugin;

/**
 * Get a contingency table (or confusion matrix)and compute the IAA measures
 * such as observed agreement, category specific agreements, Cohen's Kappa, and
 * S&C's Kappa. It also includes a few methods for adding and averaging the
 * measures and printing the results.
 */
public class ContingencyTable {
  /** Number of category. */
  public int numCats;
  /** Number of annotators. */
  public int numJudges;
  /** Number of annotators. */
  // int numAnnotators;
  /* Confusion matrix. */
  public float[][] confusionMatrix;
  /** The observed agreement. */
  public float observedAgreement = 0;
  /** Indicate if the agreement is available or not. */
  public boolean isAgreementAvailable = false;
  /** Cohen's kappa. */
  public float kappaCohen = 0;
  /** Scott's pi or Siegel & Castellan's kappa */
  public float kappaPi = 0;
  /** Assignment matrix for computing the all way kappa. */
  float[][] assignmentMatrix;
  /** Davies and Fleiss's extension of Cohen's kappa. */
  public float kappaDF = 0;
  /** Extension of Scott's pi for more than two judges. */
  public float kappaSC = 0;
  /** Positive and negative specific agreement for each category. */
  public float[][] sAgreements;

  /** Constructor for pairise case. */
  public ContingencyTable(int numC) {
    this.numCats = numC;
    this.confusionMatrix = new float[numC][numC];
    isAgreementAvailable = false;
  }

  /** Constructor with number of annotators. */
  public ContingencyTable(int numC, int numJ) {
    this.numCats = numC;
    this.numJudges = numJ;
    this.assignmentMatrix = new float[numC][numJ];
    isAgreementAvailable = false;
  }

  /** Compute the kappa for two annotators. */
  public void computeKappaPairwise() {
    // Compute the agreement
    if(!isAgreementAvailable) computeObservedAgreement();
    // compute the agreement by chance
    // Get the marginal sum for each annotator
    float[] marginalArrayC = new float[numCats];
    float[] marginalArrayR = new float[numCats];
    float totalSum = 0;
    for(int i = 0; i < numCats; ++i) {
      float sum = 0;
      for(int j = 0; j < numCats; ++j)
        sum += confusionMatrix[i][j];
      marginalArrayC[i] = sum;
      totalSum += sum;
      sum = 0;
      for(int j = 0; j < numCats; ++j)
        sum += confusionMatrix[j][i];
      marginalArrayR[i] = sum;
    }
    // Compute Cohen's p(E)
    float pE = 0;
    if(totalSum > 0) {
      float doubleSum = totalSum * totalSum;
      for(int i = 0; i < numCats; ++i)
        pE += (marginalArrayC[i] * marginalArrayR[i]) / doubleSum;
    }
    // Compute Cohen's Kappa
    if(totalSum > 0)
      kappaCohen = (observedAgreement - pE) / (1 - pE);
    else kappaCohen = 0;
    // Compute S&C's chance agreement
    pE = 0;
    if(totalSum > 0) {
      float doubleSum = 2 * totalSum;
      for(int i = 0; i < numCats; ++i) {
        float p = (marginalArrayC[i] + marginalArrayR[i]) / doubleSum;
        pE += p * p;
      }
    }
    if(totalSum > 0)
      kappaPi = (observedAgreement - pE) / (1 - pE);
    else kappaPi = 0;
    // Compute the specific agreement for each label using marginal sums
    sAgreements = new float[numCats][2];
    for(int i = 0; i < numCats; ++i) {
      if(marginalArrayC[i] + marginalArrayR[i]>0) 
        sAgreements[i][0] = (2 * confusionMatrix[i][i])
          / (marginalArrayC[i] + marginalArrayR[i]);
      else sAgreements[i][0] = 0.0f;
      if(2 * totalSum - marginalArrayC[i] - marginalArrayR[i]>0)
        sAgreements[i][1] = (2 * (totalSum - marginalArrayC[i]
          - marginalArrayR[i] + confusionMatrix[i][i]))
          / (2 * totalSum - marginalArrayC[i] - marginalArrayR[i]);
      else sAgreements[i][1] = 0.0f;
    }
  }

  /** Compute the observed agreement. */
  public void computeObservedAgreement() {
    float sumAgreed = 0;
    float sumTotal = 0;
    for(int i = 0; i < numCats; ++i) {
      sumAgreed += confusionMatrix[i][i];
      for(int j = 0; j < numCats; ++j)
        sumTotal += confusionMatrix[i][j];
    }
    if(sumTotal > 0.0)
      observedAgreement = sumAgreed / sumTotal;
    else observedAgreement = 0;
    isAgreementAvailable = true;
  }

  /** Compute the all way kappa. */
  public void computeAllwayKappa(long ySum, long numInstances,
    long numAgreements, long[] numJudgesCat, boolean isUsingNonlabel) {
    // Compute cohen's kappa using the extended formula.
    float[] pc = new float[numCats];
    for(int j = 0; j < numJudges; ++j) {
      for(int i = 0; i < numCats; ++i)
        pc[i] += assignmentMatrix[i][j];
      float sum = 0;
      for(int i = 0; i < numCats; ++i)
        sum += assignmentMatrix[i][j];
      if(sum > 0) for(int i = 0; i < numCats; ++i)
        assignmentMatrix[i][j] /= sum;
    }
    float sum = 0;
    for(int i = 0; i < numCats; ++i)
      sum += pc[i];
    if(sum > 0) for(int i = 0; i < numCats; ++i)
      pc[i] /= sum;
    float term1, term2;
    term1 = 0;
    for(int i = 0; i < numCats; ++i)
      term1 += pc[i] * (1 - pc[i]);
    term2 = 0;
    for(int i = 0; i < numCats; ++i)
      for(int j = 0; j < numJudges; ++j)
        term2 += (assignmentMatrix[i][j] - pc[i])
          * (assignmentMatrix[i][j] - pc[i]);
    if(numInstances > 0 && numJudges > 1 && (term1 > 0 || term2 > 0))
      kappaDF = 1 - (float)(numInstances * numJudges * numJudges - ySum)
        / (numInstances * (numJudges * (numJudges - 1) * term1 + term2));
    else kappaDF = 0;
    // Compute the observed agreement and the S&C kappa
    if(numInstances == 0 || numJudges == 0) {
      observedAgreement = 0;
      kappaSC = 0;
    }
    else {
      observedAgreement = (float)numAgreements / numInstances;
      // Compute the kappa of S&C
      float pE = 0;
      float dNum = numInstances * numJudges;
      for(int i = 0; i < numCats; ++i) {
        float s = numJudgesCat[i] / dNum;
        float sR = s;
        for(int j = 1; j < numJudges; ++j)
          sR *= s;
        pE += sR;
      }
      kappaSC = (observedAgreement - pE) / (1 - pE);
    }
  }

  /** Print out the results. */
  public String printResultsPairwise() {
    StringBuffer logMessage = new StringBuffer();
    logMessage.append("Observed agreement: " + observedAgreement + ";  ");
    logMessage.append("Cohen's kappa: " + kappaCohen + "; ");
    logMessage.append("Scott's pi: " + kappaPi + "\n");
    return logMessage.toString();
  }

  /** Print out the confusion matrix. */
  public String printConfusionMatrix(String[] labelsArr) {
    StringBuffer logMessage = new StringBuffer();
    // logMessage.append("----------------------------------------------\n");
    logMessage.append("\t\t|");
    int numL = labelsArr.length;
    for(int i = 0; i < numL; ++i) {
      logMessage.append("\t" + labelsArr[i] + "\t|");
    }
    logMessage.append("\t " + IaaCalculation.NONCAT + " \t|\n");
    // logMessage.append("----------------------------------------------\n");
    for(int i = 0; i < numL; ++i) {
      logMessage.append("\t" + labelsArr[i] + "\t|");
      for(int j = 0; j < numL; ++j)
        logMessage.append("\t" + this.confusionMatrix[i][j] + "\t|");
      logMessage.append("\t" + this.confusionMatrix[i][numL] + "\t|\n");
    }
    logMessage.append("\t" + IaaCalculation.NONCAT + "\t|");
    for(int j = 0; j < numL; ++j)
      logMessage.append("\t" + this.confusionMatrix[numL][j] + "\t|");
    logMessage.append("\t" + this.confusionMatrix[numL][numL] + "\t|\n");
    // logMessage.append("----------------------------------------------\n");
    return logMessage.toString();
  }

  /** Print out the results. */
  public String printResultsAllway() {
    StringBuffer logMessage = new StringBuffer();
    logMessage.append("Observed agreement: " + observedAgreement + "; ");
    logMessage.append("Cohen's kappa extended for  " + numJudges
      + " annotators: " + kappaDF + "; ");
    logMessage.append("S&C kappa for " + numJudges + " annotators: " + kappaSC
      + "\n");
    return logMessage.toString();
  }

  /** Add the results. */
  public void add(ContingencyTable another) {
    this.kappaCohen += another.kappaCohen;
    this.kappaDF += another.kappaDF;
    this.kappaPi += another.kappaPi;
    this.kappaSC += another.kappaSC;
    this.observedAgreement += another.observedAgreement;
  }

  /** Compute the macro averaged results. */
  public void macroAveraged(int num) {
    this.kappaCohen /= num;
    this.kappaDF /= num;
    this.kappaPi /= num;
    this.kappaSC /= num;
    this.observedAgreement /= num;
  }
}
