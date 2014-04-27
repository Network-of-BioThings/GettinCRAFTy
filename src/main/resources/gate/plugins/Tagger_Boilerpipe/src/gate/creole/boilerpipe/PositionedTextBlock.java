/*
 * PositionedTextBlock.java
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

import java.util.BitSet;

import de.l3s.boilerpipe.document.TextBlock;

/**
 * An instance of TextBlock that stores positional information allowing GATE
 * annotations to be created to cover the same text spans.
 * 
 * @author Mark A. Greenwood
 */
public class PositionedTextBlock extends TextBlock implements Cloneable {

  private int start, end, numWordsInWrappedLines, numWrappedLines;

  public PositionedTextBlock(String text, BitSet containedTextElements,
          int numWords, int numWordsInAnchorText, int numWordsInWrappedLines,
          int numWrappedLines, int offsetBlocks, int start, int end) {
    super(text, containedTextElements, numWords, numWordsInAnchorText,
            numWordsInWrappedLines, numWrappedLines, offsetBlocks);

    this.start = start;
    this.end = end;
    this.numWordsInWrappedLines = numWordsInWrappedLines;
    this.numWrappedLines = numWrappedLines;
  }

  public int getNumWordsInWrappedLines() {
    return numWordsInWrappedLines;
  }

  public int getNumWrappedLines() {
    return numWrappedLines;
  }

  @Override
  public void mergeNext(final TextBlock other) {
    super.mergeNext(other);

    if(other instanceof PositionedTextBlock) {
      PositionedTextBlock ptb = (PositionedTextBlock)other;
      numWordsInWrappedLines += ptb.numWordsInWrappedLines;
      numWrappedLines += ptb.numWrappedLines;

      start = Math.min(start, ptb.start);
      end = Math.max(end, ptb.end);
    } else {
      throw new RuntimeException(
              "You can't merge different types of TextBlock!");
    }
  }

  @Override
  public String toString() {
    return start + "-->" + end + ": " + super.toString();
  }

  @Override
  public Object clone() {
    return super.clone();
  }

  /**
   * Returns the character offset within the document that this block starts at.
   * 
   * @return the character offset within the document that this block starts at.
   */
  public int getStartOffset() {
    return start;
  }

  /**
   * Returns the character offset within the document that this block ends at.
   * 
   * @return the character offset within the document that this block ends at.
   */
  public int getEndOffset() {
    return end;
  }
}