/*
 * BoilerPipe.java
 * 
 * Copyright (c) 2010, The University of Sheffield.
 * 
 * This file is part of GATE (see http://gate.ac.uk/), and is free software,
 * licenced under the GNU Library General Public License, Version 3, June 2007
 * (in the distribution as file licence.html, and also available at
 * http://gate.ac.uk/gate/licence.html).
 * 
 * Mark A. Greenwood, 22/10/2010
 */

package gate.creole.boilerpipe;

import static gate.GateConstants.ORIGINAL_MARKUPS_ANNOT_SET_NAME;
import gate.Annotation;
import gate.AnnotationSet;
import gate.Factory;
import gate.FeatureMap;
import gate.Utils;
import gate.creole.AbstractLanguageAnalyser;
import gate.creole.ExecutionException;
import gate.creole.ExecutionInterruptedException;
import gate.creole.metadata.CreoleParameter;
import gate.creole.metadata.CreoleResource;
import gate.creole.metadata.Optional;
import gate.creole.metadata.RunTime;
import gate.util.InvalidOffsetException;
import gate.util.OffsetComparator;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import de.l3s.boilerpipe.BoilerpipeProcessingException;
import de.l3s.boilerpipe.document.TextBlock;
import de.l3s.boilerpipe.document.TextDocument;

/**
 * A GATE PR which uses the <a
 * href="http://code.google.com/p/boilerpipe/">boilerpipe</a> library to
 * determine which sections of a document are useful content and which are
 * simply boilerplate.
 * 
 * @see <a href="http://gate.ac.uk/userguide/sec:misc-creole:boilerpipe">The GATE
 *      User Guide</a>
 * @author Mark A. Greenwood
 */
@CreoleResource(name = "Boilerpipe Content Detection", icon = "content_detection.png", comment = "Uses boilerpipe to determine which sections of a document are interesting content and which are just boilerplate", helpURL = "http://gate.ac.uk/userguide/sec:misc-creole:boilerpipe")
public class BoilerPipe extends AbstractLanguageAnalyser {

  private static final long serialVersionUID = 5086217897382197476L;

  private transient Logger logger = Logger.getLogger(this.getClass().getName());

  /**
   * A regular expression for finding section breaks
   */
  private static final Pattern BLOCK_SEPARATOR = Pattern
          .compile("(\n\r|\r\n|\n|\r){1,}");

  /**
   * The maximum length of a single line of text before wrapping takes place
   */
  private static final int MAX_LINE_LENGTH = 80;

  /**
   * A counter so we can keep track of which tokens we have looked at all ready.
   * This is for efficiency reasons as we never need to look at old tokens again
   * but removing them from the list would be time consuming
   */
  private int tokenIndex = 0;

  /**
   * A counter so we can keep track of which anchor tags we have looked at all
   * ready. This is for efficiency reasons as we never need to look at old
   * anchors again but removing them from the list would be time consuming
   */
  private int anchorIndex = 0;

  private Extractor extractor = Extractor.DEFAULT;

  @RunTime
  @CreoleParameter(comment = "The type of extractor to use to find the content", defaultValue = "DEFAULT")
  public void setExtractor(Extractor extractor) {
    this.extractor = extractor;
  }

  public Extractor getExtractor() {
    return extractor;
  }

  private Behaviour behaviour = Behaviour.NOT_LISTED;

  @RunTime
  @CreoleParameter(comment = "Determines how the list of mime types is interpretted", defaultValue = "NOT_LISTED")
  public void setAllContent(Behaviour behaviour) {
    this.behaviour = behaviour;
  }

  public Behaviour getAllContent() {
    return behaviour;
  }

  private Set<String> mimeTypes;

  @RunTime
  @CreoleParameter(comment = "A list of mime types that determines which documents are fully processed", defaultValue = "text/html")
  public void setMimeTypes(Set<String> mimeTypes) {
    this.mimeTypes = mimeTypes;
  }

  public Set<String> getMimeTypes() {
    return mimeTypes;
  }

  private String contentAnnotationName;

  @RunTime
  @Optional
  @CreoleParameter(comment = "The name of the annotations to create over real document content", defaultValue = "Content")
  public void setContentAnnotationName(String contentAnnotationName) {
    this.contentAnnotationName = contentAnnotationName;
  }

  public String getContentAnnotationName() {
    return contentAnnotationName;
  }

  private String boilerplateAnnotationName;

  @RunTime
  @Optional
  @CreoleParameter(comment = "The name of the annotations to create over boilerplate sections", defaultValue = "Boilerplate")
  public void setBoilerplateAnnotationName(String boilerplateAnnotationName) {
    this.boilerplateAnnotationName = boilerplateAnnotationName;
  }

  public String getBoilerplateAnnotationName() {
    return boilerplateAnnotationName;
  }

  private boolean annotateContent = true;

  @RunTime
  @CreoleParameter(comment = "If true then annotations spanning content will be created", defaultValue = "true")
  public void setAnnotateContent(Boolean annotateContent) {
    this.annotateContent = annotateContent;
  }

  public Boolean getAnnotateContent() {
    return annotateContent;
  }

  private boolean annotateBoilerplate = false;

  @RunTime
  @CreoleParameter(comment = "If true then annotations spanning boilerplate will be created", defaultValue = "false")
  public void setAnnotateBoilerplate(Boolean annotateBoilerplate) {
    this.annotateBoilerplate = annotateBoilerplate;
  }

  public Boolean getAnnotateBoilerplate() {
    return annotateBoilerplate;
  }

  private boolean debug = false;

  @RunTime
  @CreoleParameter(comment = "In debug mode internal variables will be exposed as annotation features", defaultValue = "false")
  public void setDebug(Boolean debug) {
    this.debug = debug;
  }

  public Boolean getDebug() {
    return debug;
  }

  private boolean failOnMissingInputAnnotations = true;

  @RunTime
  @CreoleParameter(comment = "Throw an exception when there are none of the required input annotations", defaultValue = "true")
  public void setFailOnMissingInputAnnotations(Boolean fail) {
    failOnMissingInputAnnotations = fail;
  }

  public Boolean getFailOnMissingInputAnnotations() {
    return failOnMissingInputAnnotations;
  }

  private boolean useHintsFromOriginalMarkups = true;

  @RunTime
  @CreoleParameter(comment = "Use annotations from the Original markups as hints for finding content", defaultValue = "true")
  public void setUseHintsFromOriginalMarkups(Boolean useHints) {
    useHintsFromOriginalMarkups = useHints;
  }

  public Boolean getUseHintsFromOriginalMarkups() {
    return useHintsFromOriginalMarkups;
  }

  private String outputASName;

  @Optional
  @RunTime
  @CreoleParameter(comment = "The name of the output annotation set.")
  public void setOutputASName(String outputASName) {
    this.outputASName = outputASName;
  }

  public String getOutputASName() {
    return outputASName;
  }

  private String inputASName;

  @Optional
  @RunTime
  @CreoleParameter(comment = "The name of the input annotation set.")
  public void setInputASName(String inputASName) {
    this.inputASName = inputASName;
  }

  public String getInputASName() {
    return inputASName;
  }

  @Override
  public void execute() throws ExecutionException {

    // assume we haven't been interrupted yet
    interrupted = false;

    // fire some progress notifications
    long startTime = System.currentTimeMillis();
    fireStatusChanged("Performing content detection in " + document.getName());
    fireProgressChanged(0);

    // if there is no document to process then stop right now
    if(document == null)
      throw new ExecutionException("No document to process!");

    // if neither type of annotation are required then quit
    if(!annotateContent && !annotateBoilerplate) return;

    // if we are supposed to be annotating content then check that the
    // annotation name has been set
    if(annotateContent
            && (contentAnnotationName == null || contentAnnotationName.trim()
                    .equals("")))
      throw new ExecutionException(
              "You must set the name of the content annotations!");

    // if we are supposed to be annotating boilerplate then check that the
    // annotation name has been set
    if(annotateBoilerplate
            && (boilerplateAnnotationName == null || boilerplateAnnotationName
                    .trim().equals("")))
      throw new ExecutionException(
              "You must set the name of the boilerplate annotations!");

    // make sure an extractor has been specified
    if(extractor == null)
      throw new ExecutionException("An extractor must be specified!");

    try {

      // is the mime type of the document one specified in the mime type list
      boolean listed =
              (mimeTypes == null ? false : mimeTypes.contains(document
                      .getFeatures().get("MimeType")));

      // find the annotation set we are supposed to be adding things to
      AnnotationSet outputAS = document.getAnnotations(outputASName);

      if((listed && behaviour.equals(Behaviour.LISTED))
              || (!listed && behaviour.equals(Behaviour.NOT_LISTED))) {

        // if the PR has been configured in such a way that the mime type of the
        // document means that we should assume it's entire contents is content
        // and we are annotating content then add a single content annotation
        annotateDocument(outputAS);

      } else {
        // we actually have to process the document so lets start doing some
        // work!

        // assume we start from the beginning of the document...
        int startIndex = 0;

        // and from the first token...
        tokenIndex = 0;

        // and from the first anchor...
        anchorIndex = 0;

        // and that the ID of the first text section will be 0
        int offsetBlocks = 0;

        // get the textual content of the document as we will be using this to
        // determine the blocks of text we need to classify
        String docContent = document.getContent().toString();

        if(useHintsFromOriginalMarkups) {
          try {
            // see if there is a "body" annotation in the original markups
            Annotation body =
                    document.getAnnotations(ORIGINAL_MARKUPS_ANNOT_SET_NAME)
                            .get("body").iterator().next();

            // and if there is then we only want to consider text inside from
            // where it starts
            startIndex = body.getStartNode().getOffset().intValue();
          } catch(Exception e) {
            // if we get an exception here just assume there is no body and
            // continue on regardless
          }
        }

        // get all the tokens from the input annotation set
        List<Annotation> tokens = new ArrayList<Annotation>();
        tokens.addAll(document.getAnnotations(inputASName).get(
                TOKEN_ANNOTATION_TYPE));

        if(tokens.size() == 0) {
          // if there are no tokens then either fail or print a warning
          if(failOnMissingInputAnnotations) {
            throw new ExecutionException(
                    "Either "
                            + document.getName()
                            + " does not have any contents or \n you need to run the tokenizer first");
          } else {
            Utils.logOnce(
                    logger,
                    Level.INFO,
                    "Content Detection: either a document does not have any text or you need to run the tokenizer first - see debug log for details.");
            logger.debug("No input annotations in document "
                    + document.getName());
            return;
          }
        }

        // sort the tokens to ensure they are in the same order as in the
        // document
        Collections.sort(tokens, new OffsetComparator());

        // get all the anchors (<a></a>) tags from the original markups set
        List<Annotation> anchors = new ArrayList<Annotation>();

        if(useHintsFromOriginalMarkups) {
          anchors.addAll(document.getAnnotations(
                  ORIGINAL_MARKUPS_ANNOT_SET_NAME).get("a"));

          // sort the anchors so they appear in the same order as in the
          // document
          Collections.sort(anchors, new OffsetComparator());
        }

        // for ease of use we are going to build two lists both holding
        // information about the set of text blocks. One will get processed,
        // which invovles merging and deleting blocks, the other will remain
        // untouched. The information on relevance will then be mapped back to
        // the untouched list to ensure that all the text can be annotated as
        // content or boilerplate properly when we have finished
        List<PositionedTextBlock> origBlocks =
                new ArrayList<PositionedTextBlock>();
        List<TextBlock> blocks = new ArrayList<TextBlock>();

        // let's start by assuming that the name of the document is it's title
        String title = document.getName();

        if(useHintsFromOriginalMarkups) {
          try {
            // if there is an actual "title" element in the original markups
            // then
            // use the text it spans as the title instead
            Annotation t =
                    document.getAnnotations(ORIGINAL_MARKUPS_ANNOT_SET_NAME)
                            .get("title").iterator().next();
            title = Utils.stringFor(document, t);
          } catch(Exception e) {
            // if we get an exception here just assume there is no title and
            // just
            // continue to use the document name
          }
        }

        // get a matcher over the doc content so we can find the different
        // blocks
        Matcher m = BLOCK_SEPARATOR.matcher(docContent);

        while(m.find()) {

          // if we have been asked to stop then do so
          if(isInterrupted()) { throw new ExecutionInterruptedException(
                  "The execution of the \""
                          + getName()
                          + "\" Boilerpipe Content Detection has been abruptly interrupted!"); }

          // for each separator we find...

          // try and create block that spans everything from the last block to
          // this separator
          PositionedTextBlock tb =
                  createTextBlock(docContent, tokens, anchors, startIndex,
                          m.start(), offsetBlocks);

          if(tb != null) {
            // if we created a block then...

            // increment the ID ready for next time
            ++offsetBlocks;

            // remember the end of the separator for use as the start of the
            // next block
            startIndex = m.end();

            // store the block in both lists that we are building
            blocks.add(tb);
            origBlocks.add((PositionedTextBlock)tb.clone());
          }

          // assume that half the time is processing the tokens
          fireProgressChanged((tokenIndex / tokens.size()) * 50);
        }

        // try and create a block from the last separator to the end of the
        // document
        PositionedTextBlock tb =
                createTextBlock(docContent, tokens, anchors, startIndex,
                        docContent.length(), offsetBlocks);
        if(tb != null) {
          // if we created a block then store it
          blocks.add(tb);
          origBlocks.add((PositionedTextBlock)tb.clone());
        }

        // create a document object that we can pass to the boilerpipe library
        TextDocument td = new TextDocument(title, blocks);

        if(extractor.getInstance().process(td)) {
          // if boilerpipe successfully processed the document then...

          // may back from the merged blocks to the original list
          for(TextBlock block : blocks) {
            // if we have been asked to stop then do so
            if(isInterrupted()) { throw new ExecutionInterruptedException(
                    "The execution of the \""
                            + getName()
                            + "\" Boilerpipe Content Detection has been abruptly interrupted!"); }

            for(int i = block.getOffsetBlocksStart(); i <= block
                    .getOffsetBlocksEnd(); ++i) {

              origBlocks.get(i).setIsContent(block.isContent());
            }
          }

          // now go through the original list and merge successive blocks of the
          // same type
          PositionedTextBlock previous = null;
          for(PositionedTextBlock block : origBlocks) {
            // if we have been asked to stop then do so
            if(isInterrupted()) { throw new ExecutionInterruptedException(
                    "The execution of the \""
                            + getName()
                            + "\" Boilerpipe Content Detection has been abruptly interrupted!"); }

            // if this block is 75% of the document title then assume it's
            // content no matter what boilerpipe says
            block.setIsContent(block.isContent()
                    || title.indexOf(block.getText()) != -1
                    && ((float)block.getText().length() / (float)title.length()) > 0.75);

            if(previous == null) {
              // if we are on the first block just store it and move on
              previous = block;
            } else if(previous.isContent() == block.isContent()) {
              // if this block is of the same type as the last then merge the
              // two blocks
              previous.mergeNext(block);
            } else {
              // we have just changed block types so annotate the last block
              addAnnotation(previous, outputAS);

              // and now store the new one and move on
              previous = block;
            }

            // assume adding annotations is 50% of the work
            fireProgressChanged(50 + ((block.getOffsetBlocksStart() / origBlocks
                    .size()) * 50));
          }

          // if there is still an unprocessed block then annotate it
          if(previous != null) {
            addAnnotation(previous, outputAS);
          }
        } else {
          // boilerpipe usually only returns false if te document is really
          // short and it doesn't try and process, at which point we should
          // probably assume the whole document is content
          annotateDocument(outputAS);
        }
      }
    } catch(InvalidOffsetException ioe) {
      // we should never see this exception so if we do convert it to an
      // execution exception and make it someone else's problem!
      throw new ExecutionException(ioe);
    } catch(BoilerpipeProcessingException bpe) {
      // I've no idea why this might happen so just make it someone else's
      // problem if it does!
      throw new ExecutionException(bpe);
    } finally {
      // let anyone who cares know that we have now finished
      fireProcessFinished();
      fireStatusChanged("Content detected in \""
              + document.getName()
              + "\" in "
              + NumberFormat.getInstance().format(
                      (double)(System.currentTimeMillis() - startTime) / 1000)
              + " seconds!");
    }
  }

  /**
   * Adds an annotation to the document to represent the supplied text block
   * 
   * @param block
   *          the block that should become an annotation
   * @param annotationSet
   *          the annotation set to add the annotation to
   * @throws InvalidOffsetException
   *           if the block falls outside of the document
   */
  private void addAnnotation(PositionedTextBlock block,
          AnnotationSet annotationSet) throws InvalidOffsetException {

    // only do something if the PR is configured to annotate this type of block
    if((annotateContent && block.isContent())
            || (annotateBoilerplate && !block.isContent())) {

      // creae a new feature map to hold any features
      FeatureMap params = Factory.newFeatureMap();

      if(debug) {
        // if we are in debug mode dump everything we know about this block into
        // the feature map
        params.put("content", block.isContent());
        params.put("start", block.getOffsetBlocksStart());
        params.put("end", block.getOffsetBlocksEnd());
        params.put("nwiwl", block.getNumWordsInWrappedLines());
        params.put("nwl", block.getNumWrappedLines());
        params.put("ld", block.getLinkDensity());
      }

      // now actually create and add the annotation to the annotation set
      annotationSet.add((long)block.getStartOffset(), (long)block
              .getEndOffset(), block.isContent()
              ? contentAnnotationName
              : boilerplateAnnotationName, params);
    }
  }

  private void annotateDocument(AnnotationSet annotationSet)
          throws InvalidOffsetException {
    if(annotateContent) {
      FeatureMap params = Factory.newFeatureMap();

      if(debug) params.put("content", Boolean.TRUE);

      long start = 0;
      long end = document.getContent().size();

      if(useHintsFromOriginalMarkups) {
        try {
          // see if there is a "body" annotation in the original markups
          AnnotationSet body =
                  document.getAnnotations(ORIGINAL_MARKUPS_ANNOT_SET_NAME).get(
                          "body");

          if(body.size() > 0) {
            // use the body annotation rather than the whole content
            start = body.firstNode().getOffset();
            end = body.lastNode().getOffset();
          }
        } catch(Exception e) {
          // if we get an exception here just assume there is no body and
          // continue on regardless
        }
      }

      annotationSet.add(start, end, contentAnnotationName, params);
    }
  }

  /**
   * Creates a PositionedTextBlock for boilerpipe from a section of text and the
   * annotations that overlap with it.
   * 
   * @param docContent
   *          the String content of the document
   * @param tokens
   *          all the Token annotations in the document (sorted into order)
   * @param anchors
   *          all the a annotations from the original markups set (sorted into
   *          order)
   * @param start
   *          the start offset of this block
   * @param end
   *          the end offset of this block
   * @param offset
   *          of block offset
   * @return a newly created PositionedTextBlock or null if we didn't create one
   */
  private PositionedTextBlock createTextBlock(String docContent,
          List<Annotation> tokens, List<Annotation> anchors, int start,
          int end, int offset) {

    // This is basically a re-write of
    // de.l3s.boilerpipe.sax.boilerpipeHTMLContentHandler
    // using the information already available in the document through GATE so
    // that a) we don't have to re-parse the documents b) can support formats
    // other than HTML and c) can map the blocks directly back to the GATE
    // document so that we can add annotations in the correct place. I've even
    // reimplemented what I think are bugs in the original (tokens that trigger
    // a wrapped line appear to be counted twice)

    // if we haven't got to the start yet then stop!
    if(end - start <= 0) return null;

    // don't count the first space
    int currentLineLength = -1;

    // the number of words in the block
    int numWords = 0;

    // the number of words that fall within a link
    int numLinkedWords = 0;

    // the number of lines in the block (80 chars per line)
    int numWrappedLines = 0;

    // number of tokens (i.e. words + punctuation etc.)
    int numTokens = 0;

    // number of words on the current line
    int numWordsCurrentLine = 0;

    while(tokenIndex < tokens.size()) {
      // while there are still tokens to process

      // get the current token
      Annotation token = tokens.get(tokenIndex);

      // if we have moved onto a token after the block then stop looking
      if(token.getStartNode().getOffset() > end) break;

      // update the token counter ready for next time around the loop
      ++tokenIndex;

      if(token.getEndNode().getOffset() < start) {
        // if we are before the beginning of the block then skip on to the next
        // token

        continue;
      }

      // add one to the number of tokens in this block
      numTokens++;

      // get the type of the token
      String tokenKind =
              (String)token.getFeatures().get(TOKEN_KIND_FEATURE_NAME);

      if("word".equals(tokenKind)) {
        // if this token is a word then...

        // increase the number of words in the block
        numWords++;

        // add one to the number of words in the current line
        numWordsCurrentLine++;

        // get the length of the token
        int tokenLength = Utils.length(token);

        // add the length of the token to the length of the line
        currentLineLength += tokenLength + 1;
        if(currentLineLength > MAX_LINE_LENGTH) {
          // if the line is now longer than the max...

          // add one to the number of lines in the block
          numWrappedLines++;

          // set the length of the new line to the length of the token
          currentLineLength = tokenLength;

          // and there is now just one word on the current line
          numWordsCurrentLine = 1;
        }

        // skip over anchors before the token starts
        Annotation a = null;
        while(anchorIndex < anchors.size()) {
          a = anchors.get(anchorIndex);
          if(a.getEndNode().getOffset() > token.getStartNode().getOffset())
            break;
          ++anchorIndex;
        }

        // check to see if the token overlaps with the anchor we have found
        if(a != null
                && a.getStartNode().getOffset() <= token.getEndNode()
                        .getOffset()) {
          // if we are in an anchor then add one more to the list of linked
          // words
          numLinkedWords++;
        }
      }
    }

    // if there are no tokens under this block then don't create a block
    if(numTokens == 0) { return null; }

    // work out how many words are in the wrapped lines
    int numWordsInWrappedLines;
    if(numWrappedLines == 0) {
      numWordsInWrappedLines = numWords;
      numWrappedLines = 1;
    } else {
      numWordsInWrappedLines = numWords - numWordsCurrentLine;
    }

    // now create and return the block ready for it to be passed to the
    // boilerpipe library
    return new PositionedTextBlock(docContent.substring(start, end).trim(),
            new BitSet(), numWords, numLinkedWords, numWordsInWrappedLines,
            numWrappedLines, offset, start, end);
  }
}
