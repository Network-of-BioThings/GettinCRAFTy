/* Copyright (C) 2002 Univ. of Massachusetts Amherst, Computer Science Dept.
 This file is part of "MALLET" (MAchine Learning for LanguagE Toolkit).
 http://www.cs.umass.edu/~mccallum/mallet
 This software is provided under the terms of the Common Public License,
 version 1.0, as published by http://www.opensource.org.  For further
 information, see the file `LICENSE' included with this distribution. */

/**
 * Tests membership of the token text in the provided list of regular expressions. The lexicon words are provided in a file, one space-separated phrase per
 * line.
 * 
 * @author Ryan McDonald <a href="mailto:ryantm@cis.upenn.edu">ryantm@cis.upenn.edu </a>
 */

package edu.upenn.cis.taggers.variation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Reader;
import java.io.Serializable;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

import edu.umass.cs.mallet.base.pipe.Pipe;
import edu.umass.cs.mallet.base.types.Instance;
import edu.umass.cs.mallet.base.types.Token;
import edu.umass.cs.mallet.base.types.TokenSequence;
import edu.upenn.cis.taggers.Constants;

public class VariationRegexTrieLexiconMembership extends Pipe implements Serializable {
    // Perhaps give it your own tokenizer?
    String name; // perhaps make this an array of names
    boolean ignoreCase;
    java.util.Vector lexicon;
    boolean indvMatch = true;

    public VariationRegexTrieLexiconMembership(String name, Reader lexiconReader, boolean ignoreCase) {
        this.name = name;
        this.lexicon = new java.util.Vector();
        LineNumberReader reader = new LineNumberReader(lexiconReader);
        String line;
        while (true) {
            try {
                line = reader.readLine();
            } catch (IOException e) {
                throw new IllegalStateException();
            }
            if (line == null) {
                break;
            } else {
                StringTokenizer st = new StringTokenizer(line, " ");
                int numToks = st.countTokens();
                if (numToks > 0) {
                    lexicon.add(new Pattern[numToks]);
                    Pattern[] pats = (Pattern[]) lexicon.elementAt(lexicon.size() - 1);
                    for (int i = 0; i < numToks; i++) {
                        pats[i] = Pattern.compile(ignoreCase ? st.nextToken().toLowerCase() : st.nextToken());
                    }
                }
            }
        }
        if (lexicon.size() == 0)
            throw new IllegalArgumentException("Empty lexicon");
    }

    public VariationRegexTrieLexiconMembership(String name, File lexiconFile, boolean ignoreCase) throws FileNotFoundException {
        this(name, new BufferedReader(new FileReader(lexiconFile)), ignoreCase);
    }

    public VariationRegexTrieLexiconMembership(File lexiconFile, boolean ignoreCase) throws FileNotFoundException {
        this(lexiconFile.getName(), lexiconFile, ignoreCase);
    }

    public VariationRegexTrieLexiconMembership(File lexiconFile) throws FileNotFoundException {
        this(lexiconFile.getName(), lexiconFile, true);
    }

    public Instance pipe(Instance carrier) {
        TokenSequence ts = (TokenSequence) carrier.getData();
        for (int i = 0; i < ts.size(); i++) {

            for (int j = 0; j < lexicon.size(); j++) {
                Pattern[] pats = (Pattern[]) lexicon.elementAt(j);
                boolean matched = true;

                for (int k = 0; k < pats.length && i + k < ts.size(); k++) {
                    Token tok = ts.getToken(i + k);
                    String t = tok.getText().intern();
                    if (!(pats[k].matcher(ignoreCase ? t.toLowerCase() : t)).matches()) {
                        matched = false;
                        break;
                    }
                }

                if (matched) {
                    for (int k = 0; k < pats.length && i + k < ts.size(); k++)
                        ts.getToken(i + k).setFeatureValue(name + (indvMatch ? "" + j : ""), 1.0);
                    i = i + pats.length - 1;
                    break;
                }

            }

        }
        return carrier;
    }

    // Serialization

    private static final long serialVersionUID = Constants.SVUID_VARIATION_REGEX_TRIELEXICON_MEMBERSHIP;
    private static final int CURRENT_SERIAL_VERSION = 0;

    private void writeObject(ObjectOutputStream out) throws IOException {
        out.writeInt(CURRENT_SERIAL_VERSION);
        out.writeObject(name);
        out.writeObject(lexicon);
        out.writeBoolean(ignoreCase);
        out.writeBoolean(indvMatch);
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        int version = in.readInt();
        this.name = (String) in.readObject();
        this.lexicon = (java.util.Vector) in.readObject();
        this.ignoreCase = in.readBoolean();
        this.indvMatch = in.readBoolean();
    }
}