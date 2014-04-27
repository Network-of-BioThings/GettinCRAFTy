/* Copyright (C) 2002 Univ. of Massachusetts Amherst, Computer Science Dept.
 This file is part of "MALLET" (MAchine Learning for LanguagE Toolkit).
 http://www.cs.umass.edu/~mccallum/mallet
 This software is provided under the terms of the Common Public License,
 version 1.0, as published by http://www.opensource.org.  For further
 information, see the file `LICENSE' included with this distribution. */

/**
 * Tests membership of the token text in the provided list of words. The lexicon words are provided in a file, one word per line.
 * 
 * @author Ryan McDonald <a href="mailto:ryantm@cis.upenn.edu">ryantm@cis.upenn.edu </a>
 */

package edu.upenn.cis.taggers.gene;

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

import edu.umass.cs.mallet.base.pipe.Pipe;
import edu.umass.cs.mallet.base.types.Instance;
import edu.umass.cs.mallet.base.types.Token;
import edu.umass.cs.mallet.base.types.TokenSequence;
import edu.upenn.cis.taggers.Constants;
import gnu.trove.THashSet;

public class ContainsLowFreqTriGram extends Pipe implements Serializable {
    String name;
    THashSet lexicon;
    boolean ignoreCase;

    public ContainsLowFreqTriGram(String name, Reader lexiconReader, boolean ignoreCase) {
        this.name = name;
        this.lexicon = new THashSet();
        this.ignoreCase = ignoreCase;
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
                lexicon.add(ignoreCase ? line.toLowerCase().intern() : line.intern());
            }
        }
        if (lexicon.size() == 0)
            throw new IllegalArgumentException("Empty lexicon");
    }

    public ContainsLowFreqTriGram(String name, File lexiconFile, boolean ignoreCase) throws FileNotFoundException {
        this(name, new BufferedReader(new FileReader(lexiconFile)), ignoreCase);
    }

    public ContainsLowFreqTriGram(File lexiconFile, boolean ignoreCase) throws FileNotFoundException {
        this(lexiconFile.getName(), lexiconFile, ignoreCase);
    }

    public ContainsLowFreqTriGram(File lexiconFile) throws FileNotFoundException {
        this(lexiconFile.getName(), lexiconFile, true);
    }

    public Instance pipe(Instance carrier) {
        TokenSequence ts = (TokenSequence) carrier.getData();
        for (int i = 0; i < ts.size(); i++) {
            Token t = ts.getToken(i);
            String s = t.getText();
            String w = ignoreCase ? s.toLowerCase() : s;

            // if word contains low freq tri gram
            if (w.length() >= 3) {
                for (int j = 3; j <= w.length(); j++) {
                    if (lexicon.contains(w.substring(j - 3, j))) {
                        t.setFeatureValue(name, 1.0);
                        break;
                    }
                }
            }

        }
        return carrier;
    }

    // Serialization

    private static final long serialVersionUID = Constants.SVUID_GENE_CONTAINS_LOW_FREQ_TRIGRAM;
    private static final int CURRENT_SERIAL_VERSION = 0;

    private void writeObject(ObjectOutputStream out) throws IOException {
        out.writeInt(CURRENT_SERIAL_VERSION);
        out.writeObject(name);
        out.writeObject(lexicon);
        out.writeBoolean(ignoreCase);
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        int version = in.readInt();
        this.name = (String) in.readObject();
        this.lexicon = (THashSet) in.readObject();
        this.ignoreCase = in.readBoolean();
    }

}