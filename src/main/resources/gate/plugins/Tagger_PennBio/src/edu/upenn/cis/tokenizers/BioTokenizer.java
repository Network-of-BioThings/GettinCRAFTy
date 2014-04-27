package edu.upenn.cis.tokenizers;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.zip.GZIPInputStream;

import opennlp.maxent.MaxentModel;
import opennlp.maxent.io.BinaryGISModelReader;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.util.Span;

/**
 * A class to handle tokenization of biological text
 * @author Ryan McDonald <a href="mailto:ryantm@cis.upenn.edu">ryantm@cis.upenn.edu</a>
 * @author Kevin Lerman <a href="mailto:klerman@seas.upenn.edu">Kevin Lerman</a>
 * */
public class BioTokenizer {
  private TokenizerME tokenizer = null;
  public BioTokenizer() {   
    tokenizer = new TokenizerME(getModel("data/BioTok.bin.gz"));
  }
  
  public BioTokenizer(URL data) throws IOException {
    tokenizer = new TokenizerME(getModel(data));
  }
  
  /**
   * Returns a tokenized version of the passed String
   * @param in The String to tokenize
   * @return The tokenized String
   * */
  public String tokenize(String in){
    String toReturn="";
    String[] toks = tokenizer.tokenize(in);
    for(int x=0;x<toks.length;x++){
      toReturn+=toks[x]+' ';
    }
    return toReturn.substring(0,toReturn.length()-1);
  }
  
  public Span[] getTokens(String in) {
    return tokenizer.tokenizePos(in);
  }
  
  
  private static MaxentModel getModel(URL name) {
    try {
      return new BinaryGISModelReader(
                                      new DataInputStream(
                                                          new GZIPInputStream(name.openStream())))
        .getModel();
    } catch (IOException E) {
      E.printStackTrace();
      return null;
    }
  }
  
  private static MaxentModel getModel(String name) {
    try {
      return new BinaryGISModelReader(
                                      new DataInputStream(
                                                          new GZIPInputStream(new FileInputStream(name))))
        .getModel();
    } catch (IOException E) {
      E.printStackTrace();
      return null;
    }
  }
  
}
