package gate.alignment.utils;

/**
 * A Utility class to convert simple strings into dictionary regular
 * expression entries
 * 
 * @author niraj
 * 
 */
public class Regex {

  // special chars to escape
  final static char[] specialChars = new char[] {'\\', '[', ']', '(', ')', '{',
      '}', '.', '^', '$', '|', '?', '*', '+'};

  /**
   * This must not be a regular expression but a simple string
   * 
   * @param s
   * @return
   */
  public static String escape(String s) {

    s = s.replaceAll("(\\[\\^ \\]\\+ )", "¬W¬");

    for(int i = 0; i < specialChars.length; i++) {
      s = s.replaceAll("\\" + specialChars[i], "\\\\" + specialChars[i]);
    }

    s = s.replaceAll("(¬W¬)", "([^ ]+ )?");
    return s;
  }
}
