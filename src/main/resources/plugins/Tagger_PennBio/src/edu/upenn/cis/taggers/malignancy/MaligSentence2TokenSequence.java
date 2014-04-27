/* Copyright (C) 2002 Univ. of Massachusetts Amherst, Computer Science Dept.
   This file is part of "MALLET" (MAchine Learning for LanguagE Toolkit).
   http://www.cs.umass.edu/~mccallum/mallet
   This software is provided under the terms of the Common Public License,
   version 1.0, as published by http://www.opensource.org.  For further
   information, see the file `LICENSE' included with this distribution. */


/**
   @author Ryan McDonald <a href="mailto:ryantm@cis.upenn.edu">ryantm@cis.upenn.edu</a>
 */

/*
	An error?  CoNLLTrue MalletTrue MalletPred
	O O O
	I-MISC B-MISC B-MISC
	B-MISC B-MISC I-MISC
	I-MISC B-MISC I-MISC
	O O O
	O O O
	O O O
*/

package edu.upenn.cis.taggers.malignancy;

import edu.umass.cs.mallet.base.pipe.Pipe;
import edu.umass.cs.mallet.base.types.Instance;
import edu.umass.cs.mallet.base.types.LabelAlphabet;
import edu.umass.cs.mallet.base.types.LabelSequence;
import edu.umass.cs.mallet.base.types.Token;
import edu.umass.cs.mallet.base.types.TokenSequence;

public class MaligSentence2TokenSequence extends Pipe
{

	boolean saveSource = false;
	boolean doSpelling = false;
	boolean doDigitCollapses = false;
	boolean doDowncasing = false;
	
	public MaligSentence2TokenSequence ()
	{
		super (null, LabelAlphabet.class);
	}

	public MaligSentence2TokenSequence (boolean extraFeatures)
	{
		super (null, LabelAlphabet.class);
		if (!extraFeatures) {
			doDigitCollapses = doSpelling = false;
			doDowncasing = true;
		}
	}
	
	/* Lines look like this:

	allele O
	- O
	specific O
	for O
	activating O
	mutations B-type
	at O
	the O
	12th B-location
	, O
	13th B-location
	, O
	and O
	codons B-location
	of O
	three O
	
	*/

	public Instance pipe (Instance carrier)
	{
		String sentenceLines = (String) carrier.getData();
		String[] tokens = sentenceLines.split ("\n");
		TokenSequence data = new TokenSequence (tokens.length);
		LabelSequence target = new LabelSequence ((LabelAlphabet)getTargetAlphabet(), tokens.length);
		StringBuffer source = saveSource ? new StringBuffer() : null;

		String prevLabel = "NOLABEL";
		String word, label;
		String[] words = new String[tokens.length];
		for (int i = 0; i < tokens.length; i++) {
			if (tokens[i].length() != 0) {
				String[] features = tokens[i].split ("\t");
				if (features.length != 2)
					throw new IllegalStateException ("Line \""+sentenceLines + " " + tokens[i] +"\" doesn't have 2 elements");
				word = features[0]; // .toLowerCase();
				label = features[1];
			} else {
				word = "-<S>-";
				label = "O";
			}

			words[i] = word;
			
			if(label.startsWith("I-malignancy-type"))
				label = "I-malignancy-type";
			else if(label.startsWith("B-malignancy-type"))
				label = "B-malignancy-type";
			else
				label = "O";
			
			// Transformations
			if (doDigitCollapses) {
				if (word.matches ("19\\d\\d"))
					word = "<YEAR>";
				else if (word.matches ("19\\d\\ds"))
					word = "<YEARDECADE>";
				else if (word.matches ("19\\d\\d-\\d+"))
					word = "<YEARSPAN>";
				else if (word.matches ("\\d+\\\\/\\d"))
					word = "<FRACTION>";
				else if (word.matches ("\\d[\\d,\\.]*"))
					word = "<DIGITS>";
				else if (word.matches ("19\\d\\d-\\d\\d-\\d--d"))
					word = "<DATELINEDATE>";
				else if (word.matches ("19\\d\\d-\\d\\d-\\d\\d"))
					word = "<DATELINEDATE>";
				else if (word.matches (".*-led"))
					word = "<LED>";
				else if (word.matches (".*-sponsored"))
					word = "<LED>";
			}

			if (doDowncasing)
				word = word.toLowerCase();
			Token token = new Token (word);
			
			// Append
			data.add (token);
			//target.add (bigramLabel);
			target.add (label);
			//System.out.print (label + ' ');
			if (saveSource) {
				source.append (word); source.append (" ");
				//source.append (bigramLabel); source.append ("\n");
				source.append (label); source.append ("\n");
			}

		}
		//System.out.println ("");
		carrier.setData(data);
		carrier.setTarget(target);
		carrier.setName(words);
		if (saveSource)
			carrier.setSource(source);
		return carrier;
	}
}
