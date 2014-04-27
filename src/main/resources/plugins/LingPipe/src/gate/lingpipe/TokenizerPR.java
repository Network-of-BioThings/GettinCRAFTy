package gate.lingpipe;

import gate.ProcessingResource;
import gate.Resource;
import gate.creole.*;
import gate.util.*;
import gate.*;
import java.util.*;

import com.aliasi.tokenizer.IndoEuropeanTokenizerFactory;
import com.aliasi.tokenizer.Tokenizer;

/**
 * LingPipe Tokenizer PR.
 * 
 * @author Ekaterina Stambolieva
 * 
 */
public class TokenizerPR extends AbstractLanguageAnalyser implements
                                                         ProcessingResource {
  /**
   * Name of the output annotation set
   */
  private String outputASName;

  /**
   * tokeniser factory used for tokenizing text
   */
  private com.aliasi.tokenizer.TokenizerFactory tf;

  /** Initialise this resource, and return it. */
  public Resource init() throws ResourceInstantiationException {

    // construct tokenizer
    tf = IndoEuropeanTokenizerFactory.INSTANCE;
    return this;
  }

  public void reInit() throws ResourceInstantiationException {
    init();
  }

  /**
   * execute method. Makes LingPipe API calls to tokenize the document.
   * It uses the document's string and passes it over to the LingPipe to
   * tokenize. It also generates space tokens as well.
   */
  public void execute() throws ExecutionException {

    if(document == null) {
      throw new ExecutionException("There is no loaded document");
    }

    super.fireProgressChanged(0);

    long startOffset = 0, endOffset = 0;
    AnnotationSet as = null;
    if(outputASName == null || outputASName.trim().length() == 0)
      as = document.getAnnotations();
    else as = document.getAnnotations(outputASName);

    String docContent = document.getContent().toString();

    List<String> tokenList = new ArrayList<String>();
    List<String> whiteList = new ArrayList<String>();
    Tokenizer tokenizer = tf.tokenizer(docContent.toCharArray(), 0, docContent
            .length());
    tokenizer.tokenize(tokenList, whiteList);

    for(int i = 0; i < whiteList.size(); i++) {
      try {

        startOffset = endOffset;
        endOffset = startOffset + whiteList.get(i).length();
        if((endOffset - startOffset) != 0) {
          FeatureMap fmSpaces = Factory.newFeatureMap();
          fmSpaces.put("length", "" + (endOffset - startOffset));
          as.add(new Long(startOffset), new Long(endOffset), "SpaceToken",
                  fmSpaces);
        }

        if(i < tokenList.size()) {
          startOffset = endOffset;
          endOffset = startOffset + tokenList.get(i).length();
          FeatureMap fmTokens = Factory.newFeatureMap();
          fmTokens.put("length", "" + (endOffset - startOffset));
          as.add(new Long(startOffset), new Long(endOffset), "Token", fmTokens);
        }
      }
      catch(InvalidOffsetException e) {
        throw new ExecutionException(e);
      }
    }
  }

  /**
   * gets name of the output annotation set
   * @return
   */
  public String getOutputASName() {
    return outputASName;
  }

  /**
   * sets name of the output annotaiton set
   * @param outputAS
   */
  public void setOutputASName(String outputAS) {
    this.outputASName = outputAS;
  }
}
