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

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import edu.umass.cs.mallet.base.pipe.Pipe;
import edu.umass.cs.mallet.base.types.Instance;
import edu.umass.cs.mallet.base.types.Token;
import edu.umass.cs.mallet.base.types.TokenSequence;
import edu.upenn.cis.taggers.Constants;

public class InBracket extends Pipe implements Serializable {
    String name;
    boolean ignoreCase;

    public InBracket(String name, boolean ignoreCase) {
        this.name = name;
        this.ignoreCase = ignoreCase;
    }

    public Instance pipe(Instance carrier) {
        TokenSequence ts = (TokenSequence) carrier.getData();
        int depth = 0;
        for (int i = 0; i < ts.size(); i++) {
            Token t = ts.getToken(i);
            String s = t.getText();
            s = ignoreCase ? s.toLowerCase() : s;
            if (s.equals("(")) {
                depth++;
                t.setFeatureValue(name, 1.0);
            } else if (s.equals(")")) {
                depth--;
                t.setFeatureValue(name, 1.0);
            } else if (depth > 0)
                t.setFeatureValue(name, 1.0);

        }
        return carrier;
    }

    // Serialization

    private static final long serialVersionUID = Constants.SVUID_GENE_IN_BRACKET;
    private static final int CURRENT_SERIAL_VERSION = 0;

    private void writeObject(ObjectOutputStream out) throws IOException {
        out.writeInt(CURRENT_SERIAL_VERSION);
        out.writeObject(name);
        out.writeBoolean(ignoreCase);
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        int version = in.readInt();
        this.name = (String) in.readObject();
        this.ignoreCase = in.readBoolean();
    }

}