/* Copyright (C) 2002 Univ. of Massachusetts Amherst, Computer Science Dept.
   This file is part of "MALLET" (MAchine Learning for LanguagE Toolkit).
   http://www.cs.umass.edu/~mccallum/mallet
   This software is provided under the terms of the Common Public License,
   version 1.0, as published by http://www.opensource.org.  For further
   information, see the file `LICENSE' included with this distribution. */

package edu.upenn.cis.taggers.variation;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.ObjectInputStream;
import java.util.regex.Pattern;

import edu.umass.cs.mallet.base.fst.CRF4;
import edu.umass.cs.mallet.base.pipe.Pipe;
import edu.umass.cs.mallet.base.pipe.SerialPipes;
import edu.umass.cs.mallet.base.pipe.TokenSequence2FeatureVectorSequence;
import edu.umass.cs.mallet.base.pipe.TokenSequenceLowercase;
import edu.umass.cs.mallet.base.pipe.iterator.LineGroupIterator;
import edu.umass.cs.mallet.base.pipe.tsf.FeaturesInWindow;
import edu.umass.cs.mallet.base.pipe.tsf.RegexMatches;
import edu.umass.cs.mallet.base.pipe.tsf.TokenText;
import edu.umass.cs.mallet.base.pipe.tsf.TokenTextCharNGrams;
import edu.umass.cs.mallet.base.pipe.tsf.TrieLexiconMembership;
import edu.umass.cs.mallet.base.types.Alphabet;
import edu.umass.cs.mallet.base.types.InstanceList;

import edu.upenn.cis.taggers.Model;

public class VariationTrainer
{
    int numEvaluations = 0;
    static int iterationsBetweenEvals = 16;

    private static String CAPS = "[A-Z]";
    private static String LOW = "[a-z]";
    private static String CAPSNUM = "[A-Z0-9]";
    private static String ALPHA = "[A-Za-z]";
    private static String ALPHANUM = "[A-Za-z0-9]";
    private static String PUNT = "[,\\.;:?!()]";
    private static String QUOTE = "[\"`']";
 
    public static void main (String[] args) throws FileNotFoundException, Exception
    {

	// train a new model
	if(args[0].equals("train")) {
  
	    Pipe p = new SerialPipes (new Pipe[] {
		new VariationSentence2TokenSequence (),
		new RegexMatches ("INITCAP", Pattern.compile (CAPS+".*")),
		new RegexMatches ("CAPITALIZED", Pattern.compile (CAPS+LOW+"*")),
		new RegexMatches ("ALLCAPS", Pattern.compile (CAPS+"+")),
		new RegexMatches ("MIXEDCAPS", Pattern.compile ("[A-Z][a-z]+[A-Z][A-Za-z]*")),
		new RegexMatches ("CONTAINSDIGITS", Pattern.compile (".*[0-9].*")),
		new RegexMatches ("SINGLEDIGITS", Pattern.compile ("[0-9]")),
		new RegexMatches ("DOUBLEDIGITS", Pattern.compile ("[0-9][0-9]")),
		new RegexMatches ("ALLDIGITS", Pattern.compile ("[0-9]+")),
		new RegexMatches ("NUMERICAL", Pattern.compile ("[-0-9]+[\\.,]+[0-9\\.,]+")),
		new RegexMatches ("ALPHNUMERIC", Pattern.compile ("[A-Za-z0-9]+")),
		new RegexMatches ("ROMAN", Pattern.compile ("[ivxdlcm]+|[IVXDLCM]+")),
		new RegexMatches ("MULTIDOTS", Pattern.compile ("\\.\\.+")),
		new RegexMatches ("ENDSINDOT", Pattern.compile ("[^\\.]+.*\\.")),
		new RegexMatches ("CONTAINSDASH", Pattern.compile (ALPHANUM+"+-"+ALPHANUM+"*")),
		new RegexMatches ("ACRO", Pattern.compile ("[A-Z][A-Z\\.]*\\.[A-Z\\.]*")),
		new RegexMatches ("LONELYINITIAL", Pattern.compile (CAPS+"\\.")),
		new RegexMatches ("SINGLECHAR", Pattern.compile (ALPHA)),
		new RegexMatches("CAPLETTER", Pattern.compile ("[A-Z]")),
		new RegexMatches ("PUNC", Pattern.compile (PUNT)),
		new RegexMatches ("QUOTE", Pattern.compile (QUOTE)),
		new TokenSequenceLowercase(),
		new RegexMatches ("ISTYPE1", Pattern.compile (".*ploidy|.*somy")),
    
		new TokenText ("WORD="),

		new TokenTextCharNGrams ("CHARNGRAM=", new int[] {2,3,4}),
    
		// List membership
		new TrieLexiconMembership("ISTYPE",new File("data/training/variation/lists/VARIATION.TYPES"),true),
		new VariationRegexTrieLexiconMembership("ISLOC",new File("data/training/variation/lists/LOCATION.PATTERNS"),true),
		new VariationRegexTrieLexiconMembership("ISSTATE",new File("data/training/variation/lists/STATE.PATTERNS"),true),
    
		new FeaturesInWindow("WINDOW=",-1,1,Pattern.compile("IS.*|WORD=.*|[A-Z]+"),true),
    
		new TokenSequence2FeatureVectorSequence (true, true)
	    });
   
	    InstanceList allData = new InstanceList (p);
	    allData.add (new LineGroupIterator
			 (new FileReader (new File (args[1])), Pattern.compile("^$"), true));
	    System.out.println ("Number of predicates in training data: "+p.getDataAlphabet().size());
   
	    Alphabet data = p.getDataAlphabet();   
	    data.stopGrowth();
	    // Print out all the target names  
	    Alphabet targets = p.getTargetAlphabet();

	    InstanceList testData = null;
	    if(!args[2].equals("null")) {
		testData = new InstanceList (p);
		testData.add (new LineGroupIterator (new FileReader
						     (new File (args[2])), Pattern.compile("^$"), true));
	    }

	    // Print out some feature information
	    System.out.println ("Number of features = "+p.getDataAlphabet().size());

	    VariationSegmentationEvaluator eval =
		new VariationSegmentationEvaluator (new String[] {"B-type", "B-location",
								  "B-state-original","B-state-altered"},
						    new String[] {"I-type", "I-location",
								  "I-state-original","I-state-altered"});
   
	    CRF4 crf = new CRF4(p,null);
   
	    crf.addFullyConnectedStatesForLabels();
	    crf.setGaussianPriorVariance (5.0);
   
	    crf.train (allData, null, testData, eval, 200);

	    crf.write(new File(args[3]));
	}
	else if(args[0].equals("test")) {
	    VariationSegmentationEvaluator eval =
		new VariationSegmentationEvaluator (new String[] {"B-type", "B-location",
								  "B-state-original","B-state-altered"},
						    new String[] {"I-type", "I-location",
								  "I-state-original","I-state-altered"});
   
	    //ObjectInputStream ois;
	    //ois = new ObjectInputStream(new FileInputStream(args[2]));
   
	    CRF4 crf = Model.loadAndRetrieveModel(args[2]);//(CRF4)ois.readObject();
	    crf.getInputAlphabet().stopGrowth();

	    SerialPipes p = (SerialPipes)crf.getInputPipe();
   
	    InstanceList allData = new InstanceList (p);
	    System.out.println(p.getDataAlphabet().size());
	    allData.add (new LineGroupIterator (new FileReader (new File (args[1])), Pattern.compile("^$"), true));
	    
	    crf.evaluate(eval,allData);
	    //eval.test(crf,allData,"Testing",null);
   
	}
	else {
	    System.out.println("Bad arguments.");
	    System.exit(0);
	}
  
    }
 
}
