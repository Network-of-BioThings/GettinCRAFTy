/* Copyright (C) 2002 Univ. of Massachusetts Amherst, Computer Science Dept.
   This file is part of "MALLET" (MAchine Learning for LanguagE Toolkit).
   http://www.cs.umass.edu/~mccallum/mallet
   This software is provided under the terms of the Common Public License,
   version 1.0, as published by http://www.opensource.org.  For further
   information, see the file `LICENSE' included with this distribution. */




/** 
    @author Ryan McDonald <a href="mailto:ryantm@cis.upenn.edu">ryantm@cis.upenn.edu</a>
*/

package edu.upenn.cis.taggers.gene;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Random;
import java.util.regex.Pattern;

import edu.umass.cs.mallet.base.fst.CRF4;
import edu.umass.cs.mallet.base.pipe.Pipe;
import edu.umass.cs.mallet.base.pipe.SerialPipes;
import edu.umass.cs.mallet.base.pipe.TokenSequence2FeatureVectorSequence;
import edu.umass.cs.mallet.base.pipe.TokenSequenceLowercase;
import edu.umass.cs.mallet.base.pipe.iterator.LineGroupIterator;
import edu.umass.cs.mallet.base.pipe.tsf.FeaturesInWindow;
import edu.umass.cs.mallet.base.pipe.tsf.OffsetConjunctions;
import edu.umass.cs.mallet.base.pipe.tsf.RegexMatches;
import edu.umass.cs.mallet.base.pipe.tsf.TokenText;
import edu.umass.cs.mallet.base.pipe.tsf.TokenTextCharNGrams;
import edu.umass.cs.mallet.base.pipe.tsf.TokenTextCharPrefix;
import edu.umass.cs.mallet.base.pipe.tsf.TokenTextCharSuffix;
import edu.umass.cs.mallet.base.pipe.tsf.TrieLexiconMembership;
import edu.umass.cs.mallet.base.types.Alphabet;
import edu.umass.cs.mallet.base.types.Instance;
import edu.umass.cs.mallet.base.types.InstanceList;
import edu.umass.cs.mallet.base.types.Sequence;

import edu.upenn.cis.taggers.Model;

public class GeneTrainer
{
    int numEvaluations = 0;
    static int iterationsBetweenEvals = 16;

    private static String CAPS = "[A-Z]";
    private static String LOW = "[a-z]";
    private static String CAPSNUM = "[A-Z0-9]";
    private static String ALPHA = "[A-Za-z]";
    private static String ALPHANUM = "[A-Za-z0-9]";
    private static String PUNT = "[,\\.;:?!]";
    private static String QUOTE = "[\"`']";
    private static String SEQ = "[atgcu]+";
    private static String BADSUFFIX = ".*ole|.*ane|.*ate|.*ide|.*ine|.*ite|.*ol|.*ose|.*cooh|.*ar|.*ic|.*al|.*ive|.*ly|.*yl|.*ing|.*ry|.*ian|.*ent|.*ward|.*fold|.*ene|.*ory|.*ized|.*ible|.*ize|.*izes|.*ed|.*tion|.*ity|.*ure|.*ence";
    private static String GOODSUFFIX = ".*gene|.*like|.*ase|homeo.*";
	
    public static void main (String[] args) throws FileNotFoundException, Exception
    {

	CRF4 crf = null;
	InstanceList trainingData, testingData = null;
		
	if(args[0].equals("train")) {

	    Pipe p = new SerialPipes (new Pipe[] {
		new GeneSentence2TokenSequence (),
			
		// Pattern matching features on the words
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
		new RegexMatches ("STARTDASH", Pattern.compile ("-.*")),
		new RegexMatches ("ENDDASH", Pattern.compile (".*-")),
		new RegexMatches ("FORWARDSLASH", Pattern.compile ("/")),				
		new RegexMatches ("ISBRACKET", Pattern.compile ("[()]")),				
			
		new TokenSequenceLowercase(),

		new RegexMatches ("SEQUENCE",Pattern.compile(SEQ)),
			
		// Make the word a feature
		new TokenText ("WORD="),
		new TokenTextCharSuffix("SUFFIX2=",2),
		new TokenTextCharSuffix("SUFFIX3=",3),
		new TokenTextCharSuffix("SUFFIX4=",4),
		new TokenTextCharPrefix("PREFIX2=",2),
		new TokenTextCharPrefix("PREFIX3=",3),
		new TokenTextCharPrefix("PREFIX4=",4),
				
		new InBracket("INBRACKET",true),

		// FeatureInWindow features
		new FeaturesInWindow("WINDOW=",-1,1,Pattern.compile("WORD=.*|SUFFIX.*|PREFIX.*|[A-Z]+"),true),
				
		new TokenTextCharNGrams ("CHARNGRAM=", new int[] {2,3,4}),

		// List membership criteria - from hugo and NCBI
		// - gene lists
		new TrieLexiconMembership("GENELIST1",new File("data/training/gene/lists/hugo.lst.norm"),true),
		new TrieLexiconMembership("GENELIST2",new File("data/training/gene/lists/Gene.Lexicon.norm"),true),

		//AbGene Lists
		// - gene lists
		new TrieLexiconMembership("GENELIST3",new File("data/training/gene/abgene_lists/singlegenes.lst.norm"),true),
		new TrieLexiconMembership("GENELIST4",new File("data/training/gene/abgene_lists/multigenes.lst.norm"),true),
		new TrieLexiconMembership("GENELIST5",new File("data/training/gene/abgene_lists/othergenes.lst.norm"),true),
		new TrieLexiconMembership("GENETERMS",new File("data/training/gene/abgene_lists/geneterms.lst.norm"),true),

		// - False positive filter lists
		new TrieLexiconMembership("NOTGENE1",new File("data/training/gene/abgene_lists/genbio.lst.norm"),true),
		new TrieLexiconMembership("NOTGENE2",new File("data/training/gene/abgene_lists/aminoacids.lst.norm"),true),
		new TrieLexiconMembership("NOTGENE3",new File("data/training/gene/abgene_lists/restenzymes.lst.norm"),true),
		new TrieLexiconMembership("NOTGENE4",new File("data/training/gene/abgene_lists/celllines.lst.norm"),true),
		new TrieLexiconMembership("NOTGENE5",new File("data/training/gene/abgene_lists/organismsNCBI.lst.norm"),true),
		new TrieLexiconMembership("NOTGENE6",new File("data/training/gene/abgene_lists/nonbio.lst.norm"),true),
		new TrieLexiconMembership("NOTGENE7",new File("data/training/gene/abgene_lists/stopwords.lst.norm"),true),
		new TrieLexiconMembership("NOTGENE8",new File("data/training/gene/abgene_lists/units.lst.norm"),true),
		new TrieLexiconMembership("NOTGENE9",new File("data/training/gene/lists/common.words"),true),
				
		// - context lists
		new TrieLexiconMembership(new File("data/training/gene/abgene_lists/contextbefore.lst.norm"),true),
		new TrieLexiconMembership(new File("data/training/gene/abgene_lists/contextafter.lst.norm"),true),
			
		// - low freq tri grams
		new ContainsLowFreqTriGram("lowfreqtri.norm",new File("data/training/gene/abgene_lists/lowfreqtri.lst.norm"),true),
		new TrieLexiconMembership("PETELIST",new File("data/training/gene/lists/PeteList.norm"),true),
		new ContainsLowFreqTriGram("PETETRI",new File("data/training/gene/lists/PeteList.tri"),true),
			
		// List FeatureInWin
		new FeaturesInWindow("WINDOW=",-1,1,Pattern.compile("GENELIST.*"),true),
		new FeaturesInWindow("WINDOW=",-1,1,Pattern.compile("GENETERMS"),true),
		new FeaturesInWindow("WINDOW=",-1,1,Pattern.compile("NOTGENE.*"),true),
		new FeaturesInWindow("WINDOW=",-1,0,Pattern.compile("contextbefore.lst.norm"),true),
		new FeaturesInWindow("WINDOW=",0,1,Pattern.compile("contextafter.lst.norm"),true),
		new FeaturesInWindow("WINDOW=",-1,1,Pattern.compile("lowfreqtri.norm"),true),

		new OffsetConjunctions(true,Pattern.compile("WORD=.*"),new int[][]{{-1},{1},{-1,0}}),
				
		//new PrintTokenSequenceFeatures(),
		new TokenSequence2FeatureVectorSequence (true, true)
	    });

	    trainingData = new InstanceList (p);
	    trainingData.add (new LineGroupIterator
			      (new FileReader (new File (args[1])), Pattern.compile("^$"), true));
	    System.out.println ("Number of predicates in training data: "+p.getDataAlphabet().size());
			
	    Alphabet data = p.getDataAlphabet();
	    Alphabet targets = p.getTargetAlphabet();
	    data.stopGrowth();
	    //targets.stopGrowth();

	    testingData = null;
	    if (!args[2].equals("null")) {
		testingData = new InstanceList (p);
		testingData.add (new LineGroupIterator
				 (new FileReader (new File (args[2])), Pattern.compile("^$"), true));
	    }
	    else
		testingData = null;

	    crf = null;
	    GeneSegmentationEvaluator eval =
		new GeneSegmentationEvaluator (new String[] {"B-GENE"},
					       new String[] {"I-GENE"});

	    crf = new CRF4 (p, null);
	    crf.addOrderNStates(trainingData, new int[] {0,1,2},null,"O",Pattern.compile("O,I-GENE"),null,true);
	    crf.setGaussianPriorVariance (1.0);
	    for (int i = 0; i < crf.numStates(); i++)
		crf.getState(i).setInitialCost (Double.POSITIVE_INFINITY);
	    crf.getState("O,O").setInitialCost (0.0);
			
	    System.out.println("Training on "+trainingData.size()+" training instances ...");
	    if(testingData != null)
		System.out.println("Testing on "+trainingData.size()+" testing instances ...");
	    
	    crf.train (trainingData, null, testingData, eval, 100);
	    //crf.trainWithFeatureInduction(trainingData,null,testingData,eval,310,10,30,700,0.5,false,null);
	    
	    System.out.print("Saving model ... ");
	    crf.write(new File(args[3]));
	    System.out.println("done.");
	    
	}
	else if(args[0].equals("test")) {
	    GeneSegmentationEvaluator eval =
		new GeneSegmentationEvaluator (new String[] {"B-GENE"},
					       new String[] {"I-GENE"});
	    
	    //ObjectInputStream ois;
	    //ois = new ObjectInputStream(new FileInputStream(args[2]));
	    
	    crf = Model.loadAndRetrieveModel(args[2]);//(CRF4)ois.readObject();
	    crf.getInputAlphabet().stopGrowth();
	    
	    SerialPipes p = (SerialPipes)crf.getInputPipe();
	    
	    testingData = new InstanceList (p);
	    testingData.add (new LineGroupIterator (new FileReader (new File (args[1])), Pattern.compile("^$"), true));
	
	    crf.evaluate(eval,testingData);
	    
	}
    }

    public static CRF4 loadModel() throws IOException, ClassNotFoundException {
	return loadModel("model.crf");
    }
	
    public static CRF4 loadModel(String name) throws IOException, ClassNotFoundException {
	ObjectInputStream ois = new ObjectInputStream(new FileInputStream(name));
	return (CRF4)ois.readObject();
    }	
}
