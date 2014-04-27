/*
 *  Copyright (c) 2008--2012, The University of Sheffield. See the file
 *  COPYRIGHT.txt in the software or at http://gate.ac.uk/gate/COPYRIGHT.txt
 *
 *  This file is part of GATE (see http://gate.ac.uk/), and is free
 *  software, licenced under the GNU Library General Public License,
 *  Version 2, June 1991 (in the distribution as file licence.html,
 *  and also available at http://gate.ac.uk/gate/licence.html).
 *
 *  $Id: PairCsvGenerator.java 16296 2012-11-20 12:17:11Z adamfunk $
 */
package gate.termraider.output;

import gate.util.GateException;
import java.io.*;
import java.util.*;
import org.apache.commons.lang.StringEscapeUtils;
import gate.termraider.bank.*;
import gate.termraider.util.*;


public class PairCsvGenerator {
  
  private AbstractPairbank pairbank;
  private boolean debugMode;
  private String scorePropertyName;
  
  public void generateAndSaveCsv(AbstractPairbank pairbank, 
          double threshold, File outputFile) throws GateException {
    this.pairbank = pairbank;
    this.debugMode = pairbank.getDebugMode();
    this.scorePropertyName = pairbank.getScoreProperty();
    PrintWriter writer = initializeWriter(outputFile);
    generateCsv(writer, threshold);
    writer.flush();
    writer.close();
    if (debugMode) {
      System.out.println("Pairbank: saved CSV in " + outputFile.getAbsolutePath());
    }

  }
  
  
  private void generateCsv(PrintWriter writer, double threshold) {
    Map<UnorderedTermPair, Double> scores = pairbank.getScores();
    List<UnorderedTermPair> pairs = new ArrayList<UnorderedTermPair>(scores.keySet());
    Collections.sort(pairs, new TermPairComparatorByDescendingScore(scores));
    addComment("threshold = " + threshold);
    addComment("Unfiltered nbr of pairs = " + pairs.size());
    int written = 0;
    writeHeader(writer);    
    for (UnorderedTermPair pair: pairs) {
      double score = scores.get(pair);
      if (score < threshold) {
        break;
      }
      
      writeContent(writer, pair.getTerm0(), pair.getTerm1(), score, pairbank.getDocumentCount(pair),
              pairbank.getPairCount(pair));
    }
    
    addComment("Filtered nbr of pairs = " + written);
  }
  
  
  private void addComment(String commentStr) {
    if (debugMode) {
      System.err.println(commentStr);
    }
  }
  
  
  private PrintWriter initializeWriter(File outputFile) throws GateException {
    try {
      return new PrintWriter(outputFile);
    } 
    catch(FileNotFoundException e) {
      throw new GateException(e);
    }
  }
  
  
  
  private void writeContent(PrintWriter writer, Term t0, Term t1, Double score, Integer documents, Integer frequency) {
    StringBuilder sb = new StringBuilder();
    sb.append(StringEscapeUtils.escapeCsv(t0.getTermString()));
    sb.append(',');
    sb.append(StringEscapeUtils.escapeCsv(t0.getLanguageCode()));
    sb.append(',');
    sb.append(StringEscapeUtils.escapeCsv(t0.getType()));
    sb.append(',');
    sb.append(StringEscapeUtils.escapeCsv(t1.getTermString()));
    sb.append(',');
    sb.append(StringEscapeUtils.escapeCsv(t1.getLanguageCode()));
    sb.append(',');
    sb.append(StringEscapeUtils.escapeCsv(t1.getType()));
    sb.append(',');
    sb.append(StringEscapeUtils.escapeCsv(score.toString()));
    sb.append(',');
    sb.append(StringEscapeUtils.escapeCsv(documents.toString()));
    sb.append(',');
    sb.append(StringEscapeUtils.escapeCsv(frequency.toString()));
    writer.println(sb.toString());
  }
  
  
  private void writeHeader(PrintWriter writer) {
    StringBuilder sb = new StringBuilder();
    sb.append(StringEscapeUtils.escapeCsv("Term"));
    sb.append(',');
    sb.append(StringEscapeUtils.escapeCsv("Lang"));
    sb.append(',');
    sb.append(StringEscapeUtils.escapeCsv("Type"));
    sb.append(',');
    sb.append(StringEscapeUtils.escapeCsv("Term"));
    sb.append(',');
    sb.append(StringEscapeUtils.escapeCsv("Lang"));
    sb.append(',');
    sb.append(StringEscapeUtils.escapeCsv("Type"));
    sb.append(',');
    sb.append(StringEscapeUtils.escapeCsv(scorePropertyName));
    sb.append(',');
    sb.append(StringEscapeUtils.escapeCsv("DocFrequency"));
    sb.append(',');
    sb.append(StringEscapeUtils.escapeCsv("Frequency"));
    writer.println(sb.toString());
  }
  
}
