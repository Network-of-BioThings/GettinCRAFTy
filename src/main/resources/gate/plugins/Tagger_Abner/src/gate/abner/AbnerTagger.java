/*
 * AbnerTagger.java Wrapper over Abner:
 * http://pages.cs.wisc.edu/~bsettles/abner/
 * 
 * georgi.georgiev@ontotext.com
 */

package gate.abner;

import gate.AnnotationSet;
import gate.Factory;
import gate.FeatureMap;
import gate.creole.AbstractLanguageAnalyser;
import gate.creole.ExecutionException;
import gate.creole.metadata.CreoleParameter;
import gate.creole.metadata.CreoleResource;
import gate.creole.metadata.Optional;
import gate.creole.metadata.RunTime;
import gate.util.InvalidOffsetException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

import org.apache.log4j.Logger;

import abner.Tagger;

/**
 * ABNER wrapper for GATE. This tagger is based on CRF statistical modeling and
 * is taken off the shelf. It also uses its own build in tokenizer and sentence
 * split. One can use without any other nlp components. The tagger has two
 * models: 1. Trained on GENIA 2. Trained on NLPBA
 * 
 * @author <A HREF="mailto:georgiev@ontotext.com>Georgi Georgiev</A>
 * @author Mark A. Greenwood
 */
@CreoleResource(name = "ABNER Tagger", comment = "GATE wrapper over ABNER", helpURL = "http://gate.ac.uk/userguide/sec:parsers:abner", icon = "DNA.png")
public class AbnerTagger extends AbstractLanguageAnalyser {
  public static final long serialVersionUID = 1L;

  private static final Logger logger = Logger.getLogger(AbnerTagger.class);

  private static final char[] compartibleChars = "[,.;:?!-+]{}\"`'()"
          .toCharArray();

  private static final Map<String, String> friendlyNames =
          new HashMap<String, String>();

  static {
    friendlyNames.put("PROTEIN", "Protein");
    friendlyNames.put("CELL_LINE", "CellLine");
    friendlyNames.put("CELL_TYPE", "CellType");
    friendlyNames.put("DNA", "DNA");
    friendlyNames.put("RNA", "RNA");
  }

  private AbnerRunMode abnerMode = null;

  private String outputASName = null;

  private Long gateDocumentIndex = null;

  private Tagger abnerTagger;

  private String annotationName = null;

  @RunTime
  @CreoleParameter(defaultValue = "BIOCREATIVE", comment = "This option allows tow different models to be used for tagging. Namely: NLPBA and BIOCREATIVE. NLPBA entity types are Gene, DNA, RNA, Cell Lines, Cell cultures. BIOCREATIVE entity type is Gene.")
  public void setAbnerMode(AbnerRunMode a) {
    abnerMode = a;
  }

  public AbnerRunMode getAbnerMode() {
    return abnerMode;
  }

  @RunTime
  @Optional
  @CreoleParameter(comment = "The annotation set in which new annotations will be created")
  public void setOutputASName(String a) {
    outputASName = a;
  }

  public String getOutputASName() {
    return outputASName;
  }

  @RunTime
  @Optional
  @CreoleParameter(defaultValue = "Tagger", comment = "The name of the annotation to create, if blank (or null) the type of entity is used as the annotation name")
  public void setAnnotationName(String annotationName) {
    this.annotationName = annotationName;
  }

  public String getAnnotationName() {
    return annotationName;
  }

  /**
   * This is the method that calls ABNER Tagger.getSegments methods annotations
   * are returned as string chunks with attached meta tags in vector of 2D
   * arrays containing respectively 1. string chunks in first array 2.
   * corresponding meta tags in second array one element of the vector contains
   * one sentence in terms of ABNER (JLex) tokenization.
   */
  public void execute() throws ExecutionException {
    gateDocumentIndex = 0L;

    if(abnerTagger == null
            || !(abnerTagger.getMode() == getAbnerMode().getValue())) {
      try {
        abnerTagger = new Tagger(getAbnerMode().getValue());
      } catch(Exception e) {
        throw new ExecutionException("Can not instantiate ABNER", e);
      }
    }

    try {
      AnnotationSet annotations = document.getAnnotations(outputASName);

      @SuppressWarnings("unchecked")
      Vector<String[][]> tagged =
              abnerTagger.getSegments(removeNonASCII(document.getContent()
                      .toString()));

      for(Iterator<String[][]> iter = tagged.iterator(); iter.hasNext();) {
        // each element contain sentence
        String[][] element = iter.next();

        for(int token = 0; token < element[1].length; token++) {

          String phrase = element[0][token];
          String annotation = element[1][token];

          int offset[] = findLength(phrase);

          long startIndex = gateDocumentIndex;

          String friendlyName = friendlyNames.get(annotation);

          if(friendlyName != null) {
            FeatureMap fm = Factory.newFeatureMap();
            fm.put("source", "abner");

            String type =
                    (annotationName == null || annotationName.trim().equals("")
                            ? friendlyName
                            : annotationName);

            if(type.equals(annotationName)) fm.put("type", friendlyName);

            annotations.add((startIndex - offset[1]) + offset[0],
                    gateDocumentIndex, type, fm);
          }
        }
      }// end iterate over sentences

    } catch(Exception e) {
      e.printStackTrace();
    }

  }

  private static boolean isABNERCompartible(char ch) {
    boolean bool = false;
    for(char element : compartibleChars) {
      if(element == ch) bool = true;
    }
    return bool;
  }

  private static String removeNonASCII(String document) {

    char[] charDoc = null;
    charDoc = document.toCharArray();
    char[] returnDoc = document.toCharArray();

    for(int i = 0; i < charDoc.length; i++) {
      if(isABNERCompartible(charDoc[i])) {
        returnDoc[i] = charDoc[i];
      } else if(Character.getNumericValue(charDoc[i]) > 255
              || Character.getNumericValue(charDoc[i]) < 0) {
        returnDoc[i] = ' ';
      } else {
        returnDoc[i] = charDoc[i];
      }
    }

    StringBuffer a = new StringBuffer();

    for(char c : returnDoc) {
      a.append(c);
    }

    return a.toString();
  }

  private static boolean isASCII(char ch) {
    if(isABNERCompartible(ch)) {
      return true;
    } else if(Character.getNumericValue(ch) > 255
            || Character.getNumericValue(ch) < 0) {
      return false;
    } else {
      return true;
    }

  }

  private int[] findLength(String phrase) {
    int length = 0;
    int endIndex = 0;

    long phraseSize = (long)(200 + phrase.length());
    endIndex =
            (int)((document.getContent().size() - gateDocumentIndex) < phraseSize
                    ? phrase.length()
                            + ((document.getContent().size() - gateDocumentIndex) - (long)phrase
                                    .length())
                    : phrase.length() + 200);
    //

    char[] origSentence = new char[10];
    char[] parsePhrase = new char[10];

    try {
      origSentence =
              document.getContent()
                      .getContent(gateDocumentIndex,
                              gateDocumentIndex + endIndex).toString()
                      .toCharArray();

      parsePhrase = phrase.toCharArray();

    } catch(InvalidOffsetException e) {
      logger.error("The Gate document offset are not correct: " + e + "\n");
    }
    // start to compare
    int gateIndex = 0;
    int parseIndex = 0;
    /* most probably true */
    boolean isStartWhite = false;
    int start = 0;
    if(Character.isWhitespace(origSentence[0])) isStartWhite = true;

    try {
      // to see indexes
      while(parseIndex < parsePhrase.length) {
        if(origSentence[gateIndex] != parsePhrase[parseIndex]) {
          if(Character.isWhitespace(origSentence[gateIndex])) {
            length++;
            gateIndex++;
            if(isStartWhite) start++;
          } else if(!isASCII(origSentence[gateIndex])) {
            length++;
            gateIndex++;
          } else if(parsePhrase[parseIndex] == ' ') {
            parseIndex++;
            // do nothing it was parsed
          } else {
            throw new IOException("The parsed element is not interval but: "
                    + parsePhrase[parseIndex] + "\n");
          }

        } else {
          if(isStartWhite) isStartWhite = false;
          length++;
          gateIndex++;
          parseIndex++;
        }
      }

      gateDocumentIndex += gateIndex;
    } catch(IOException e) {
      logger.error("Error: " + e.toString());
    }
    int result[] = {start, length};

    return result;
  }
}
