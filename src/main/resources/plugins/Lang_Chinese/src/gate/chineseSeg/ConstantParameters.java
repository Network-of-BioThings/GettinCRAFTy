/*
 *  ConstantParameters.java
 * 
 *  Yaoyong Li 23/04/2009
 *
 *  $Id: ConstantParameters.java 11946 2009-11-11 13:02:35Z nirajaswani $
 */

package gate.chineseSeg;

public class ConstantParameters {
  final static String SEPARATTORLN = ";;";
  final static char SEPARATTOR_BLANK = ' ';
  final static char SEPARATTOR_BLANK_wide = '　';
  final static char REPLACEMENT_BLANK = 'K';
  final static char REPLACEMENT_Letter = 'L';
  final static char REPLACEMENT_Digit = 'N';
  final static char BEGIN_Char = 'B';
  final static char END_Char = 'E';
  final static char NEWLINE_Char = 'R';
  
  final static String LABEL_L = "1";
  final static String LABEL_M = "2";
  final static String LABEL_R = "3";
  final static String LABEL_S = "4";
  
  
  public static final String FILETYPEOFSAVEDFILE = ".save";
  final static String FILENAME_TERMS = "terms.txt";
  final static String FILENAME_resultsDir = "segmented";
  final static String FILENAMEOFLabelList = "labels.txt";
  /** Name of the file storing the feature vectors in sparse format. */
  public static final String FILENAMEOFFeatureVectorData = 
    "featureVectorsData"+ FILETYPEOFSAVEDFILE;;
  /** Name of the tempory file storing the feature vectors in sparse format. */
  public static final String TempFILENAMEofFVData = "featureVectorsDataTemp"
    + FILETYPEOFSAVEDFILE;;
  /** Name of the file storing the learned models */
  public static final String FILENAMEOFModels = "learnedModels"
    + FILETYPEOFSAVEDFILE;
  /** Name of log file. */
  public static final String FILENAMEOFLOGFILE = "logFileForChineseSeg"
    + FILETYPEOFSAVEDFILE;
  
  final static String NONFEATURE = "_NA_";
  
  /** Maximal number of the unique NLP features. */
  public static final int MAXIMUMFEATURES = 6000000; // 5000000;
}
