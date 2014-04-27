/*
 *  Utils.java
 *
 *  Copyright (c) 1995-2012, The University of Sheffield. See the file
 *  COPYRIGHT.txt in the software or at http://gate.ac.uk/gate/COPYRIGHT.txt
 *
 *  This file is part of GATE (see http://gate.ac.uk/), and is free
 *  software, licenced under the GNU Library General Public License,
 *  Version 2, June 1991 (in the distribution as file licence.html,
 *  and also available at http://gate.ac.uk/gate/licence.html).
 *
 *  Valentin Tablan, 6 Mar 2012
 *
 *  $Id: Utils.java 15577 2012-03-12 11:16:38Z valyt $
 */
package gate.creole.coref;

import java.io.File;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Utilities used in multiple places inside the gate.creole.coref package.
 */
public class Utils {

  /**
   * A RegEx pattern used to identify multi-word expressions containing only
   * words that start with a capital letter.
   */
  public static final Pattern MWE_UPPER_INITIAL_PATTERN = Pattern.compile(
      // (Upper-starting word
      "(?:\\p{javaUpperCase}(?:\\p{javaLowerCase}|\\p{javaUpperCase})+" +
      // followed by space), one or more times
      "\\p{javaWhitespace}+)+" +
      // ending with another upper-starting word.
      "\\p{javaUpperCase}(?:\\p{javaLowerCase}|\\p{javaUpperCase})+");
  
  /**
   * {@link Pattern} used to identify abbreviations based on initials (e.g. 
   * &quot;BT&quot; or &quot;B.T.&quot).
   */
  public static final Pattern INITIALS_PATTERN = Pattern.compile(
      "\\p{javaUpperCase}+|(?:\\p{javaUpperCase}\\.)+");
  
  /**
   * A word that starts with an upper case letter. This is a sub-pattern of 
   * {@link #MWE_UPPER_INITIAL_PATTERN}.
   */
  public static final Pattern UPPER_INITIAL_PATTERN = Pattern.compile(
    "\\p{javaUpperCase}(?:\\p{javaLowerCase}|\\p{javaUpperCase})+");
  
  
  /**
   * Given an input string matched by {@link #MWE_UPPER_INITIAL_PATTERN}, this 
   * method returns the initials for constituent words of the multi-word 
   * expression.
   * @param input
   * @return
   */
  public static final char[] initialsForMwe(String input) {
    // split on whitespace
    String[] words = input.split("\\p{javaWhitespace}+");
    char[] res = new char[words.length];
    for(int i = 0; i < words.length; i++) {
      res[i] = words[i].charAt(0);
    }
    return res;
  }
  
  /**
   * Given a multi-word expression, this method splits it at whitespace and 
   * returns its constituent parts. 
   * @param input the String containing the multi-word expression.
   * @return the array of parts, in the same order as they occurred in the input
   * string.
   */
  public static final String[] partsForMwe(String input) {
    return input.split("\\p{javaWhitespace}+");
  }
  
  /**
   * Checks whether the input string is a multi-word reference as recognised by
   * {@link #MWE_UPPER_INITIAL_PATTERN}.
   * @param text
   * @return
   */
  public static final boolean isMwe(String text) {
    return MWE_UPPER_INITIAL_PATTERN.matcher(text).matches();
  }
}
