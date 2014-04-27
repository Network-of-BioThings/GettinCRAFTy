/*
 *  Copyright (c) 2010--2012, The University of Sheffield. See the file
 *  COPYRIGHT.txt in the software or at http://gate.ac.uk/gate/COPYRIGHT.txt
 *
 *  This file is part of GATE (see http://gate.ac.uk/), and is free
 *  software, licenced under the GNU Library General Public License,
 *  Version 2, June 1991 (in the distribution as file licence.html,
 *  and also available at http://gate.ac.uk/gate/licence.html).
 *
 *  $Id: CsvGenerator.java 16296 2012-11-20 12:17:11Z adamfunk $
 */
package gate.termraider.output;

import gate.util.GateException;
import java.io.*;
import java.util.*;
import org.apache.commons.lang.*;
import gate.termraider.bank.*;
import gate.termraider.util.*;

public class CsvGenerator {
  
  private AbstractTermbank termbank;
  private boolean debugMode;
  private String scorePropertyName;
  
  public void generateAndSaveCsv(AbstractTermbank termbank, 
          double threshold, File outputFile) throws GateException {
    this.termbank = termbank;
    this.debugMode = termbank.getDebugMode();
    this.scorePropertyName = termbank.getScoreProperty();
    PrintWriter writer = initializeWriter(outputFile);
    generateCsv(writer, threshold);
    writer.flush();
    writer.close();
    if (debugMode) {
      System.out.println("Termbank: saved CSV in " + outputFile.getAbsolutePath());
    }

  }
  
  
  
  private void generateCsv(PrintWriter writer, double threshold) {
    Map<Term, Double> termScores = termbank.getTermScores();
    Map<Term, Set<String>> termDocuments = termbank.getTermDocuments();
    Map<Term, Integer> termFrequencies = null;
    termFrequencies = termbank.getTermFrequencies();
    addComment("threshold = " + threshold);
    List<Term> sortedTerms = termbank.getTermsByDescendingScore();
    
    addComment("Unfiltered nbr of terms = " + sortedTerms.size());
    int written = 0;
    writeHeader(writer);
    
    for (Term term : sortedTerms) {
      Double score = termScores.get(term);
      if (score >= threshold) {
        Set<String> documents = termDocuments.get(term);
        Integer frequency = termFrequencies.get(term);
        writeContent(writer, term, score, documents, frequency);
        written++;
      }
      else {  // the rest must be lower
        break;
      }
    }
    addComment("Filtered nbr of terms = " + written);
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
  
  
  
  private void writeContent(PrintWriter writer, Term term, Double score, Set<String> documents, Integer frequency) {
    StringBuilder sb = new StringBuilder();
    sb.append(StringEscapeUtils.escapeCsv(term.getTermString()));
    sb.append(',');
    sb.append(StringEscapeUtils.escapeCsv(term.getLanguageCode()));
    sb.append(',');
    sb.append(StringEscapeUtils.escapeCsv(term.getType()));
    sb.append(',');
    sb.append(StringEscapeUtils.escapeCsv(this.scorePropertyName));
    sb.append(',');
    sb.append(StringEscapeUtils.escapeCsv(score.toString()));
    sb.append(',');
    sb.append(StringEscapeUtils.escapeCsv(Integer.toString(documents.size())));
    sb.append(',').append(StringEscapeUtils.escapeCsv(frequency.toString()));
    writer.println(sb.toString());
  }
  
  private void writeHeader(PrintWriter writer) {
    StringBuilder sb = new StringBuilder();
    sb.append(StringEscapeUtils.escapeCsv("Term"));
    sb.append(',').append(StringEscapeUtils.escapeCsv("Lang"));
    sb.append(',').append(StringEscapeUtils.escapeCsv("Type"));
    sb.append(',').append(StringEscapeUtils.escapeCsv("ScoreType"));
    sb.append(',').append(StringEscapeUtils.escapeCsv("Score"));
    sb.append(',').append(StringEscapeUtils.escapeCsv("Document_Count"));
    sb.append(',').append(StringEscapeUtils.escapeCsv("Term_Frequency"));
    writer.println(sb.toString());
  }
  
  
}
