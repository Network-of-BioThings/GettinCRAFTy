/* Copyright (C) 2002 Univ. of Massachusetts Amherst, Computer Science Dept.
 This file is part of "MALLET" (MAchine Learning for LanguagE Toolkit).
 http://www.cs.umass.edu/~mccallum/mallet
 This software is provided under the terms of the Common Public License,
 version 1.0, as published by http://www.opensource.org.  For further
 information, see the file `LICENSE' included with this distribution. */

/**
 @author Ryan McDonald <a href="mailto:ryantm@cis.upenn.edu">ryantm@cis.upenn.edu</a>
 */

package edu.upenn.cis.taggers.variation;

import java.io.Serializable;

import edu.umass.cs.mallet.base.pipe.Pipe;
import edu.umass.cs.mallet.base.types.Instance;
import edu.umass.cs.mallet.base.types.LabelAlphabet;
import edu.umass.cs.mallet.base.types.LabelSequence;
import edu.umass.cs.mallet.base.types.Token;
import edu.umass.cs.mallet.base.types.TokenSequence;

public class VariationSentence2TokenSequence extends Pipe implements Serializable {

    private static final long serialVersionUID = 25275L;

    public VariationSentence2TokenSequence() {
        super(null, LabelAlphabet.class);
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

    public Instance pipe(Instance carrier) {
        String sentenceLines = (String) carrier.getData();
        String[] tokens = sentenceLines.split("\n");
        TokenSequence data = new TokenSequence(tokens.length);
        LabelSequence target = new LabelSequence((LabelAlphabet) getTargetAlphabet(), tokens.length);

        String prevLabel = "NOLABEL";
        String word, label, bit;
        String[] words = new String[tokens.length];
        for (int i = 0; i < tokens.length; i++) {
            if (tokens[i].length() != 0) {
                String[] features = tokens[i].split("\t");
                if (features.length != 2)
                    throw new IllegalStateException("Line \"" + sentenceLines + " " + tokens[i] + "\" doesn't have 2 elements");
                word = features[0]; // .toLowerCase();
                label = features[1];
            } else {
                word = "-<S>-";
                label = "O";
            }

            words[i] = word;

            if (label.startsWith("I-state-altered"))
                label = "I-state-altered";
            else if (label.startsWith("B-state-altered"))
                label = "B-state-altered";
            else if (label.startsWith("I-state-original"))
                label = "I-state-original";
            else if (label.startsWith("B-state-original"))
                label = "B-state-original";
            else if (label.startsWith("I-type"))
                label = "I-type";
            else if (label.startsWith("B-type"))
                label = "B-type";
            else if (label.startsWith("I-location"))
                label = "I-location";
            else if (label.startsWith("B-location"))
                label = "B-location";
            else
                label = "O";

            Token token = new Token(word);

            // Append
            data.add(token);
            target.add(label);
        }

        carrier.setData(data);
        carrier.setTarget(target);
        carrier.setName(words);
        return carrier;
    }

}