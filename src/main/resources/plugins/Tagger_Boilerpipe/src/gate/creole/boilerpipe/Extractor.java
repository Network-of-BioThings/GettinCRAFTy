/*
 * Extractor.java
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

import de.l3s.boilerpipe.BoilerpipeExtractor;
import de.l3s.boilerpipe.extractors.ArticleExtractor;
import de.l3s.boilerpipe.extractors.CanolaExtractor;
import de.l3s.boilerpipe.extractors.DefaultExtractor;
import de.l3s.boilerpipe.extractors.LargestContentExtractor;
import de.l3s.boilerpipe.extractors.NumWordsRulesExtractor;

/**
 * An enum describing the different content extraction algorithms that are
 * available. Not all the extractors from boilerpipe are available due to issues
 * with aligning the output with GATE documents.
 * 
 * @author Mark A. Greenwood
 */
public enum Extractor {

  ARTICLE("Article", ArticleExtractor.INSTANCE),

  /**
   * Trained on the <a
   * href="https://krdwrd.org/trac/attachment/wiki/Corpora/Canola/CANOLA.pdf"
   * >CANOLA</a> corpus.
   */
  CANOLA("Trained on CANOLA", CanolaExtractor.INSTANCE),

  DEFAULT("Default", DefaultExtractor.INSTANCE),

  LARGEST("Largest Content", LargestContentExtractor.INSTANCE),

  WORDS("Number of Words", NumWordsRulesExtractor.INSTANCE);

  /**
   * The human readable description that will get displayed in the GATE gui
   */
  private final String description;

  /**
   * The actual extractor that can be used to find content
   */
  private final BoilerpipeExtractor extractor;

  Extractor(String description, BoilerpipeExtractor extractor) {
    this.description = description;
    this.extractor = extractor;
  }

  /**
   * Get the underlying boilerpipe extractor so you can actually do some work.
   * 
   * @return the underlying extractor
   */
  public BoilerpipeExtractor getInstance() {
    return extractor;
  }

  /**
   * Returns the description of the enum value
   * 
   * @return the description of the enum value
   */
  @Override
  public String toString() {
    return description;
  }
}
