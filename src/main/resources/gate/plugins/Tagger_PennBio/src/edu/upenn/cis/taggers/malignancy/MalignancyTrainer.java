/* Copyright (C) 2002 Univ. of Massachusetts Amherst, Computer Science Dept.
   This file is part of "MALLET" (MAchine Learning for LanguagE Toolkit).
   http://www.cs.umass.edu/~mccallum/mallet
   This software is provided under the terms of the Common Public License,
   version 1.0, as published by http://www.opensource.org.  For further
   information, see the file `LICENSE' included with this distribution. */




/** 
    @author Ryan McDonald & Yang Jin <a href="mailto:yajin@mail.med.upenn.edu">yajin@mail.med.upenn.edu</a>
*/

package edu.upenn.cis.taggers.malignancy;

import edu.umass.cs.mallet.base.types.*;
import edu.umass.cs.mallet.base.fst.*;
import edu.umass.cs.mallet.base.minimize.*;
import edu.umass.cs.mallet.base.minimize.tests.*;
import edu.umass.cs.mallet.base.maximize.*;
import edu.umass.cs.mallet.base.maximize.tests.*;
import edu.umass.cs.mallet.base.pipe.*;
import edu.umass.cs.mallet.base.pipe.iterator.*;
import edu.umass.cs.mallet.base.pipe.tsf.*;
import junit.framework.*;
import java.util.Iterator;
import java.util.Random;
import java.util.regex.*;
import java.io.*;

import edu.upenn.cis.taggers.Model;

public class MalignancyTrainer
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
		new MaligSentence2TokenSequence (),
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
		new RegexMatches ("CAPLETTER", Pattern.compile ("[A-Z]")),
		new RegexMatches ("PUNC", Pattern.compile (PUNT)),
		new RegexMatches ("QUOTE", Pattern.compile (QUOTE)),
		new TokenSequenceLowercase(),
		new RegexMatches ("ISTYPE", Pattern.compile (".*oma")),
		//new LocationBiGram(),
				
		new TokenText ("WORD="),
		new TokenTextCharNGrams ("CHARNGRAM=", new int[] {2,3,4}),
				
		// List membership
		new TrieLexiconMembership("ISTYPE",new File("data/training/malignancy/lists/MALIGNANCY.TYPES"),true),
				
		new FeaturesInWindow("WINDOW=",-1,1,Pattern.compile("IS.*|WORD=.*|[A-Z]+"),true),
				
		//new PrintTokenSequenceFeatures(),
		new TokenSequence2FeatureVectorSequence (true, true)
	    });
			
	    InstanceList allData = new InstanceList (p);
	    allData.add (new LineGroupIterator (new FileReader (new File (args[1])), Pattern.compile("^$"), true));
	    System.out.println ("Number of predicates in training data: "+p.getDataAlphabet().size());

	    InstanceList testData = null;
	    if(!args[2].equals("null")) {
		testData = new InstanceList (p);
		testData.add (new LineGroupIterator (new FileReader (new File (args[2])), Pattern.compile("^$"), true));
	    }
			
	    Alphabet data = p.getDataAlphabet();			
			
	    // Print out all the target names		
	    Alphabet targets = p.getTargetAlphabet();
			
	    // Print out some feature information
	    System.out.println ("Number of features = "+p.getDataAlphabet().size());
			
	    MalignancySegmentationEvaluator eval =
		new MalignancySegmentationEvaluator (new String[] {"B-malignancy-type"}, new String[] {"I-malignancy-type"});
			
	    CRF4 crf = new CRF4(p,null);
	    crf.addFullyConnectedStatesForLabels();
			
	    crf.setGaussianPriorVariance (5.0);
			
	    crf.train (allData, null, testData, eval, 200);

	    crf.write(new File(args[3]));
	}
	// test the model on a new data set.
	else if(args[0].equals("test")) {
	    MalignancySegmentationEvaluator eval =
		new MalignancySegmentationEvaluator (new String[] {"B-malignancy-type"}, new String[] {"I-malignancy-type"});
			
	    CRF4 crf = Model.loadAndRetrieveModel(args[2]);
	    crf.getInputAlphabet().stopGrowth();

	    SerialPipes p = (SerialPipes)crf.getInputPipe();
   
	    InstanceList allData = new InstanceList (p);
	    System.out.println(p.getDataAlphabet().size());
	    allData.add (new LineGroupIterator (new FileReader (new File (args[1])), Pattern.compile("^$"), true));
	    
	    crf.evaluate(eval,allData);
			
	}
	else {
	    System.out.println("Bad arguments.");
	    System.exit(0);
	}
		
    }
	
}
