/*
 *
 *  Copyright (c) 1995-2012, The University of Sheffield. See the file
 *  COPYRIGHT.txt in the software or at http://gate.ac.uk/gate/COPYRIGHT.txt
 *
 *  This file is part of GATE (see http://gate.ac.uk/), and is free
 *  software, licenced under the GNU Library General Public License,
 *  Version 2, June 1991 (in the distribution as file licence.html,
 *  and also available at http://gate.ac.uk/gate/licence.html).
 *
 *  $Id: TestIaa.java 12006 2009-12-01 17:24:28Z thomas_heitz $
 */

package gate.iaaplugin.test;

import java.util.ArrayList;
import java.util.HashMap;
import junit.framework.*;

import gate.Annotation;
import gate.AnnotationSet;
import gate.Corpus;
import gate.Document;
import gate.Factory;
import gate.FeatureMap;
import gate.Gate;
import gate.GateConstants;
import gate.iaaplugin.IaaCalculation;
import gate.util.AnnotationMerging;
import gate.util.InvalidOffsetException;

public class TestIaa extends TestCase {
  /** The id of test case. */
  int caseN;

  /** Construction */
  public TestIaa(String name) {
    super(name);
  }

  /** Fixture set up */
  public void setUp() {
  } // setUp

  /**
   * Put things back as they should be after running tests.
   */
  public void tearDown() throws Exception {
  } // tearDown

  /** Test suite routine for the test runner */
  public static Test suite() {
    return new TestSuite(TestIaa.class);
  } // suite

  private Document loadDocument(String path, String name) throws Exception {
    Document doc = Factory.newDocument(Gate.getUrl(path), "UTF-8");
    doc.setName(name);
    return doc;
  }

  /** The test the IAA. */
  public void testIaa() throws Exception {

    Boolean savedSpaceSetting = Gate.getUserConfig().getBoolean(
            GateConstants.DOCUMENT_ADD_SPACE_ON_UNPACK_FEATURE_NAME);
    Gate.getUserConfig().put(
            GateConstants.DOCUMENT_ADD_SPACE_ON_UNPACK_FEATURE_NAME,
            Boolean.FALSE);
    try {
      
      //Gate.setGateHome(new File("C:\\svn\\gate"));
      //Gate.setUserConfigFile(new File("C:\\svn\\gate.xml"));
      //Gate.init();
      //ExtensionFileFilter fileFilter = new ExtensionFileFilter();
      //fileFilter.addExtension("xml");

      // Load the documents into a corpus
      Corpus data = Factory.newCorpus("data");

      boolean isUsingLabel = true;
      // Put the annotated document into a matrix for IAA
      String nameAnnSet;
      String nameAnnType;
      String nameAnnFeat;
     
      data.add(loadDocument("tests/iaa/beijing-opera.xml", "beijing-opera.xml"));
      //Document doc = Factory.newDocument(new File("C:\\svn\\gate\\src\\gate\\resources\\gate.ac.uk\\tests\\iaa\\beijing-opera.xml").toURI().toURL(), "UTF-8");
      //data.add(doc);
      // Test with feature
      nameAnnSet = "ann1;ann2;ann3";
      caseN = 1;
      nameAnnType = "sent";
      nameAnnFeat = "Op";
      isUsingLabel = true;
      testWithfeat(nameAnnSet, nameAnnType, nameAnnFeat,
              data, isUsingLabel);

      caseN = 2;
      nameAnnType = "Os";
      nameAnnFeat = null;
      isUsingLabel = false;
      testWithfeat(nameAnnSet, nameAnnType, nameAnnFeat,
              data, isUsingLabel);
    }
    finally {
      Gate.getUserConfig().put(
              GateConstants.DOCUMENT_ADD_SPACE_ON_UNPACK_FEATURE_NAME,
              savedSpaceSetting);
    }

  }

  /** The actual method for testing. */
  public void testWithfeat(String nameAnnSets,
          String nameAnnType, String nameAnnFeat, Corpus data,
          boolean isUsingLabel) {

    //get the annotation sets
    String [] annSetsN = nameAnnSets.split(";");
    int numJudges = annSetsN.length;
    int numDocs = data.size();
    AnnotationSet[][] annArr2 = new AnnotationSet[numDocs][numJudges];
   
    for(int i = 0; i < numDocs; ++i) {
      Document doc = (Document)data.get(i);
      for(int j=0; j<numJudges; ++j) {
        // Get the annotation
        annArr2[i][j] = doc.getAnnotations(annSetsN[j]).get(nameAnnType);
      }
    }
    // Get the Iaa computation
    // First collect labels for the feature
    IaaCalculation iaa = null;
    int verbo = 2;
    if(isUsingLabel) {
      ArrayList<String> labelsSet;
      labelsSet = IaaCalculation.collectLabels(annArr2, nameAnnFeat);
      String[] labelsArr = new String[labelsSet.size()]; // (String
      // [])labelsSet.toArray();
      for(int i = 0; i < labelsSet.size(); ++i) {
        labelsArr[i] = labelsSet.get(i);
      }
      iaa = new IaaCalculation(nameAnnType, nameAnnFeat, labelsArr, annArr2, verbo);
    } else 
      iaa = new IaaCalculation(nameAnnType, annArr2, verbo);

    iaa.pairwiseIaaFmeasure();
    int[] nPwF = new int[4];
    nPwF[0] = (int)Math.ceil((double)iaa.fMeasureOverall.correct);
    nPwF[1] = (int)Math.ceil((double)iaa.fMeasureOverall.partialCor);
    nPwF[2] = (int)Math.ceil((double)iaa.fMeasureOverall.spurious);
    nPwF[3] = (int)Math.ceil((double)iaa.fMeasureOverall.missing);

    boolean isSuitable = true;
    for(int i = 0; i < annArr2.length; ++i)
      if(!IaaCalculation.isSameInstancesForAnnotators(annArr2[i], 0)) {
        isSuitable = false;
        break;
      }

    // Get a reference annotation set by merging all
    AnnotationSet[] refAnnsArr = new AnnotationSet[annArr2.length];
    boolean[] isMerged = new boolean[annArr2.length];
    for(int i = 0; i < annArr2.length; ++i)
      isMerged[i] = false;
    for(int iJ = 0; iJ < annArr2.length; ++iJ) {
      Document docC = (Document)data.get(iJ);
      HashMap<Annotation, String> mergeInfor = new HashMap<Annotation, String>();
      AnnotationMerging.mergeAnnotation(annArr2[iJ], nameAnnFeat, mergeInfor, 2, isSuitable);
      AnnotationSet annsDoc = docC.getAnnotations("mergedAnns");
      for(Annotation ann : mergeInfor.keySet()) {
        // FeatureMap featM = ann.getFeatures();
        FeatureMap featM = Factory.newFeatureMap();
        featM.put(nameAnnFeat, ann.getFeatures().get(nameAnnFeat));
        featM.put("annotators", mergeInfor.get(ann));
        try {
          annsDoc.add(ann.getStartNode().getOffset(), ann.getEndNode()
                  .getOffset(), nameAnnType, featM);
        }
        catch(InvalidOffsetException e) {
          e.printStackTrace();
        }
      }
      refAnnsArr[iJ] = annsDoc.get(nameAnnType);
    }
    iaa.allwayIaaFmeasure(refAnnsArr);
    //  remove the reference annotations created
    for(int iJ = 0; iJ < numDocs; ++iJ) {
      Document docC = (Document)data.get(iJ);
      docC.removeAnnotationSet("mergedAnns");
    }
    
    int[] nAwF = new int[4];
    nAwF[0] = (int)Math.ceil((double)iaa.fMeasureOverall.correct);
    nAwF[1] = (int)Math.ceil((double)iaa.fMeasureOverall.partialCor);
    nAwF[2] = (int)Math.ceil((double)iaa.fMeasureOverall.spurious);
    nAwF[3] = (int)Math.ceil((double)iaa.fMeasureOverall.missing);

    // Compute the kappa
    iaa.pairwiseIaaKappa();
    
    int[] nPwKa = new int[3];
    nPwKa[0] = (int)Math
            .ceil((double)iaa.contingencyOverall.observedAgreement * 100);
    nPwKa[1] = (int)Math.ceil((double)iaa.contingencyOverall.kappaCohen * 100);
    nPwKa[2] = (int)Math.ceil((double)iaa.contingencyOverall.kappaPi * 100);

    iaa.allwayIaaKappa();
   
    int[] nAwKa = new int[3];
    nAwKa[0] = (int)Math
            .ceil((double)iaa.contingencyOverall.observedAgreement * 100);
    nAwKa[1] = (int)Math.ceil((double)iaa.contingencyOverall.kappaDF * 100);
    nAwKa[2] = (int)Math.ceil((double)iaa.contingencyOverall.kappaSC * 100);

    checkNumbers(nPwF, nAwF, nPwKa, nAwKa, isSuitable);
  }

  /** Check the numbers. */
  private void checkNumbers(int[] nPwF, int[] nAwF, int[] nPwKa, int[] nAwKa,
          boolean isSuitable) {
    switch(caseN) {
      case 1:
        assertEquals(nPwF[0], 7);
        assertEquals(nPwF[1], 0);
        assertEquals(nPwF[2], 2);
        assertEquals(nPwF[3], 2);
        assertEquals(nAwF[0], 8);
        assertEquals(nAwF[1], 0);
        assertEquals(nAwF[2], 1);
        assertEquals(nAwF[3], 1);
        assertEquals(nPwKa[0], 90);
        assertEquals(nPwKa[1], 83);
        assertEquals(nPwKa[2], 82);
        assertEquals(nAwKa[0], 85);
        assertEquals(nAwKa[1], 83);
        assertEquals(nAwKa[2], 81);
        assertEquals(isSuitable, true);
        break;
      case 2:
        assertEquals(nPwF[0], 2);
        assertEquals(nPwF[1], 1);
        assertEquals(nPwF[2], 1);
        assertEquals(nPwF[3], 2);
        assertEquals(nAwF[0], 2);
        assertEquals(nAwF[1], 0);
        assertEquals(nAwF[2], 2);
        assertEquals(nAwF[3], 0);
        assertEquals(nPwKa[0], 53);
        assertEquals(nPwKa[1], -12);
        assertEquals(nPwKa[2], -32);
        assertEquals(nAwKa[0], 61);
        assertEquals(nAwKa[1], 0);
        assertEquals(nAwKa[2], 19);
        assertEquals(isSuitable, false);
        break;
      default:
        System.out.println("The test case " + caseN + " is not defined yet.");
    }
  }

}
