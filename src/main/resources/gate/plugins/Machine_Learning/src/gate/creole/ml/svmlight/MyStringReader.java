/*
 *  Copyright (c) 2004, The University of Sheffield.
 *
 *  This file is part of GATE (see http://gate.ac.uk/), and is free
 *  software, licenced under the GNU Library General Public License,
 *  Version 2, June 1991 (in the distribution as file licence.html,
 *  and also available at http://gate.ac.uk/gate/licence.html).
 *
 *  Mike Dowman 08-04-2004
 *
 *  $Id: MyStringReader.java 7452 2006-06-15 14:45:17 +0000 (Thu, 15 Jun 2006) ian_roberts $
 *
 */

package gate.creole.ml.svmlight;

class MyStringReader {
  static final boolean DEBUG=false;

  String inputFile;
  int indexInInputFile;

  /**
   * Initialises the string reader by reading a whole file and putting it in a
   * string for easy access. Initialises the index to point at the beginning of
   * the string.
   *
   * @param reader The file reader to read the file from.
   * @throws IOException if there's an error reading the file.
   */
  public MyStringReader(java.io.FileReader reader) throws java.io.IOException {
    StringBuffer wholeFile=new StringBuffer();
    while (reader.ready()) {
      wholeFile.append((char)reader.read());
    }
    inputFile=wholeFile.toString();
    indexInInputFile=0;

    if (DEBUG) {
      System.out.println("A MyStringReader has been initialised with the "+
                         "following contents:\n"+inputFile);
    }
  }

  /**
   * Skip over any lines which are just comments.
   */
  public void skipLeadingComments() {
    // Keep skipping lines till we find one on which the first white space
    // character is not the comment symbol (#).
    while (true) {
      skipWhiteSpace();
      if (indexInInputFile<inputFile.length()
          && inputFile.charAt(indexInInputFile) == '#')
        skipToStartOfNextLine();
      else
        return;
    }
  }

  /**
   * Skip over any white space (spaces or tabs).
   */
  public void skipWhiteSpace() {
    while (indexInInputFile<inputFile.length()
           && (inputFile.charAt(indexInInputFile)==' '
           || inputFile.charAt(indexInInputFile)=='\t')) {
      ++indexInInputFile;
    }
  }

  /**
   * Skip over any white space, including new lines.
   */
  public void skipBlankLinesAndWhiteSpace() {
    while (indexInInputFile<inputFile.length()
           && (inputFile.charAt(indexInInputFile)==' '
               || inputFile.charAt(indexInInputFile)=='\t'
               || inputFile.charAt(indexInInputFile)=='\n')) {
      ++indexInInputFile;
    }
  }

  /**
   * Move on to the first character after the end of the line (or possibly after
   * the end of the file).
   */
  public void skipToStartOfNextLine() {
    int indexOfStartOfNextLine=inputFile.indexOf('\n', indexInInputFile)+1;
    // If there are no more lines in the file, just point to the first character
    // after the end of the file.
    if (indexOfStartOfNextLine==-1)
      indexInInputFile=inputFile.length();
    else
      indexInInputFile=indexOfStartOfNextLine;
  }

  /**
   * See if we've reached the end of the file.
   * @return true if we've got to the end of the file, false otherwise.
   */
  public boolean endOfFileReached() {
    return indexInInputFile==inputFile.length();
  }

  /**
   * Read a string from the file, starting at the first non-white space
   * character at or after the current position, and ending at the last
   * non-white space character after that.
   */
  public String readItem() {
    skipWhiteSpace();
    // First make sure that there is an item to read.
    if (indexInInputFile==inputFile.length()
        || inputFile.charAt(indexInInputFile)=='\n')
      throw new gate.util.GateRuntimeException("Error when reading from file"+
        " at character index "+indexInInputFile+". \nEnd of line or end of file "+
        "reached unexpectedly because the file has an invalid format.");

    int indexOfStartOfItem=indexInInputFile;
    skipToWhiteSpaceOrEndOfLineOrEndOfFile();
    return inputFile.substring(indexOfStartOfItem, indexInInputFile);
  }

  /**
   * Move on the current position to the end of the line or the file, or the
   * first white space character encountered.
   */
  private void skipToWhiteSpaceOrEndOfLineOrEndOfFile() {
    while (indexInInputFile<inputFile.length()
           && inputFile.charAt(indexInInputFile)!=' '
           && inputFile.charAt(indexInInputFile)!='\t'
           && inputFile.charAt(indexInInputFile)!='\n')
      ++indexInInputFile;
  }

  /**
   * Try to read a feature value pair.
   *
   * @return The feature value pair or null if no feature value pair was read
   * @throws GateRuntimeException if there's something other than a valid
   * feature-value pair or just white space at the current position in the line.
   */
  public FeatureValuePair readFeatureValuePair()
      throws gate.util.GateRuntimeException {
    skipWhiteSpace();
    // If there's nothing more to be read on the current line just return
    // signalling that.
    if (endOfFileReached() || inputFile.charAt(indexInInputFile) == '\n')
      return null;

    // Now we're committed to reading a feature-value pair, so if we can't it's
    // an error.
    FeatureValuePair featureValuePair = new FeatureValuePair();
    // Read the feature number, which starts at the current location, and
    // finishes just before the colon.
    int indexOfStartOfFeatureNumber = indexInInputFile;
    skipToColon();
    featureValuePair.featureNumber = Integer.parseInt(
        inputFile.substring(indexOfStartOfFeatureNumber, indexInInputFile));
    // Move on to the first character after the colon.
    ++indexInInputFile;
    if (inputFile.charAt(indexInInputFile) == ' '
        || inputFile.charAt(indexInInputFile) == '\t'
        || inputFile.charAt(indexInInputFile) == '\n')
      throw new gate.util.GateRuntimeException(
          "Error when reading from file at"
          + " character index " + indexInInputFile +
          " due to the file having an \n" +
          "invalid format. There must always be a \nfeature"
          + " value immediately after a colon in a feature value pair.");

    // Now read the feature value.
    int indexOfStartOfFeatureValue = indexInInputFile;
    skipToWhiteSpaceOrEndOfLineOrEndOfFile();
    featureValuePair.featureValue = Double.parseDouble(
        inputFile.substring(indexOfStartOfFeatureValue, indexInInputFile));

    return featureValuePair;
  }

  /**
   * Move on the current position from the start of a feature value pair to the
   * colon in a feature value pair.
   */
  private void skipToColon() {
    while (inputFile.charAt(indexInInputFile) != ':') {
      // If we find white space, or the end of the file or line before we find
      // a colon, there must be an error in the format of the input file.
      if (inputFile.charAt(indexInInputFile) == ' '
          || inputFile.charAt(indexInInputFile) == '\t'
          || inputFile.charAt(indexInInputFile) == '\n'
          || indexInInputFile == inputFile.length() - 1)
        throw new gate.util.GateRuntimeException(
            "Error when reading from file at character index " +
            indexInInputFile +
            " due \nto file having an invalid format. A colon (:) was not found " +
            "in a feature-value pair.");

      // So long as we've not found white space, just move on the index.
      ++indexInInputFile;
    }
  }

}
