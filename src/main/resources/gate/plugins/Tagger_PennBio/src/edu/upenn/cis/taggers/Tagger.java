/* Copyright (C) 2004 Univ. of Pennsylvania
    This software is provided under the terms of the Common Public License,
    version 1.0, as published by http://www.opensource.org.  For further
    information, see the file `LICENSE' included with this distribution. */

package edu.upenn.cis.taggers;
import java.io.*;

/**
 * All taggers intended to be run on MEDLINE data should implement this
 * interface.  It is expected that they will have constructors that take one
 * argument -- a string with the filename of a model they will read in using
 * ObjectInputStreams.  The tag() method may throw an IOException; all other
 * methods must have their errors handled internally.
 * @author Ryan McDonald <a href="mailto:ryantm@cis.upenn.edu">ryantm@cis.upenn.edu</a>
 * @author Kevin Lerman <a href="mailto:klerman@seas.upenn.edu">klerman@seas.upenn.edu</a>
 * */
public interface Tagger {
  
  /**
   * Returns the input String with xml tagging inline.
   * The model file used for this should have already been loaded in
   * the Tagger's constructor.
   * @param in The String to be tagged and returned
   * @return The String tagged with XML data inline
   * */
  public String tag(String in) throws IOException;
  
  public TagList tag(String[] tokens) throws IOException;
  
  /**
   * Returns the header that should be written to an HTML output file.
   * This should include, at bare minimum, HTML and BODY tags, but may
   * have other information as well, such as a legend for the document.
   * @return The HTML header
   * */
  public String htmlHeader();
  
  /**
   * Returns a string array of the xml tags this tagger may insert.
   * Should be the complete opening tag, with brackets.
   * @return An array of XML tags, complete with brackets
   * */
  public String[] xmlTags();
  
  /**
   * Returns a string array of the MEDLINE tags that should be written
   * to the MEDLINE file.  Should correspond to the XML tags -- that is,
   * the data tagged by xmlTags()[i] will be written out to MEDLINE tag
   * medlineTags()[i].  Should be no longer than 4 characters, but may
   * be shorter.
   * @return An array of MEDLINE tags
   * */
  public String[] medlineTags();
  
  /**
   * Returns a string array of the HTML tags that should precede each 
   * tagged entity.  These should correspond in order to the xmlTags array.
   * @return An array of HTML tags to be written before tagged data
   * */
  public String[] htmlOpenTags();
  
  /**
   * Returns a string array of the HTML tags that should follow each 
   * tagged entity.  These should correspond in order to the xmlTags array.
   * @return An array of HTML tags to be written after tagged data
   * */
  public String[] htmlCloseTags();
}