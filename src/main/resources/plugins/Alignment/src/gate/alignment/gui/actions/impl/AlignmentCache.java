package gate.alignment.gui.actions.impl;

import gate.Annotation;
import gate.AnnotationSet;
import gate.Document;
import gate.alignment.Alignment;
import gate.alignment.AlignmentActionInitializationException;
import gate.alignment.AlignmentException;
import gate.alignment.gui.AlignmentTask;
import gate.alignment.gui.FinishedAlignmentAction;
import gate.alignment.gui.PUAPair;
import gate.alignment.gui.PreDisplayAction;
import gate.alignment.utils.Regex;
import gate.util.BomStrippingInputStreamReader;
import gate.util.GateRuntimeException;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Alignment Cache is used for automatically aligning units of alignment
 * that were automatically aligned.
 *
 * @author niraj
 *
 */
public class AlignmentCache implements PreDisplayAction,
                           FinishedAlignmentAction {

  /**
   * Dictionary is loaded in memory
   */
  private Map<String, SortedSet<String>> dictionary = null;

  /**
   * What feature to use to store in the dictionary.  User can provide his/her
   * own feature
   */
  private String featureToUse = "string";

  /**
   * Writer we use to write to the dictionary file.
   */
  private BufferedWriter bw = null;

  /**
   * Is the init called?
   */
  boolean initCalled = false;

  // name of the output file where to store the alignment cache
  private String outputFileName;

  /**
   * the init method.
   * Arguments: 
   * args[0] = name of the file where the alignment should be stored.
   * args[1] = Optional. feature that should be used for locating alignment.
   */
  public void init(String[] args) throws AlignmentActionInitializationException {
    if(initCalled) return;
    initCalled = true;

    if(args.length > 1) {
      featureToUse = args[1];
    }

    outputFileName = args[0];
    if(dictionary == null) {
      if(args != null && args.length > 0) {
        try {
          dictionary = new HashMap<String, SortedSet<String>>();

          if(new File(args[0]).exists()) {
            BufferedReader br = new BomStrippingInputStreamReader(
                    new FileInputStream(new File(args[0])), "UTF-8");

            String line = br.readLine();
            while(line != null) {
              if(line.trim().length() == 0 || line.trim().startsWith("#")) {
                line = br.readLine();
                continue;
              }

              String[] words = line.split("\t");
              SortedSet<String> tgtEntries = dictionary.get(words[0].trim());
              if(tgtEntries == null) {
                tgtEntries = new TreeSet<String>(new Comparator<String>() {
                  public int compare(String s1, String s2) {
                    String[] s1Array = s1.split("[ ]+");
                    String[] s2Array = s2.split("[ ]+");
                    int diff = s2Array.length - s1Array.length;
                    if(diff == 0) return s2.compareTo(s1);
                    else return diff;
                  }
                });
                dictionary.put(words[0], tgtEntries);
              }

              tgtEntries.add(words[1]);
              line = br.readLine();
            }
            br.close();
          }
        }
        catch(IOException ioe) {
          throw new AlignmentActionInitializationException(ioe);
        }
      }
    }
  }

  public void executePreDisplayAction(PUAPair pair) throws AlignmentException {

    AlignmentTask task = pair.getAlignmentTask();
    Alignment alignment = task.getAlignment();

    List<Annotation> srcTokens = null;
    List<Annotation> tgtTokens = null;

    List<Annotation> srcSet = pair.getSourceUnitAnnotations();
    List<Annotation> tgtSet = pair.getTargetUnitAnnotations();

    srcTokens = new ArrayList<Annotation>(srcSet);
    Collections.sort(srcTokens, new gate.util.OffsetComparator());
    tgtTokens = new ArrayList<Annotation>(tgtSet);
    Collections.sort(tgtTokens, new gate.util.OffsetComparator());

    HashMap<String, Annotation> sstOffsets = new HashMap<String, Annotation>();
    HashMap<String, Annotation> senOffsets = new HashMap<String, Annotation>();
    HashMap<String, Annotation> tstOffsets = new HashMap<String, Annotation>();
    HashMap<String, Annotation> tenOffsets = new HashMap<String, Annotation>();

    String srcString = "";
    for(int i = 0; i < srcTokens.size(); i++) {
      Annotation annot = srcTokens.get(i);
      srcString += (i == 0 ? "" : " ");
      sstOffsets.put(srcString.length() + "", annot);
      srcString += getText(annot, task.getSrcDoc());
      senOffsets.put(srcString.length() + "", annot);
    }

    String tgtString = "";
    for(int i = 0; i < tgtTokens.size(); i++) {
      Annotation annot = tgtTokens.get(i);
      tgtString += (i == 0 ? "" : " ");
      tstOffsets.put(tgtString.length() + "", annot);
      tgtString += getText(annot, task.getTgtDoc());
      tenOffsets.put(tgtString.length() + "", annot);
    }

    outerfor: for(String s : dictionary.keySet()) {
      Pattern p = Pattern.compile(Regex.escape(s), Pattern.UNICODE_CASE);
      Matcher m = p.matcher(srcString);
      int startIndex = 0;
      int tStartIndex = 0;
      outer: while(m.find(startIndex)) {
        int start = m.start(0);
        int end = start + m.group(0).length();

        startIndex = end;
        // find the firstToken using start as a startOffset
        // find the lastToken using end as a endOffset
        Annotation aFirstToken = sstOffsets.get("" + start);
        if(aFirstToken == null) continue;

        Annotation aLastToken = senOffsets.get("" + end);
        if(aLastToken == null) continue;

        Set<String> tStrings = dictionary.get(s);
        if(tStrings == null || tStrings.isEmpty()) {
          System.out.println("Invalid entry in alignment cache:" + s);
          continue outerfor;
        }

        for(String t : tStrings) {
          Pattern tPat = Pattern.compile(Regex.escape(t), Pattern.UNICODE_CASE);
          Matcher tMat = tPat.matcher(tgtString);

          whileLoop: while(tMat.find(tStartIndex)) {
            int tStart = tMat.start(0);
            int tEnd = tStart + tMat.group(0).length();
            tStartIndex = tEnd;

            Annotation tFirstToken = tstOffsets.get("" + tStart);
            Annotation tLastToken = tenOffsets.get("" + tEnd);
            if(tFirstToken == null || tLastToken == null) {
              tStartIndex = tStart + 1;
              continue;
            }

            int sst = srcTokens.indexOf(aFirstToken);
            int sen = srcTokens.indexOf(aLastToken);

            int tst = tgtTokens.indexOf(tFirstToken);
            int ten = tgtTokens.indexOf(tLastToken);

            for(int k = sst; k <= sen; k++) {
              Annotation sAnnot = srcTokens.get(k);
              if(alignment.isAnnotationAligned(sAnnot)) continue whileLoop;
            }

            for(int k = tst; k <= ten; k++) {
              Annotation tAnnot = tgtTokens.get(k);
              if(alignment.isAnnotationAligned(tAnnot)) continue whileLoop;
            }

            for(int k = sst; k <= sen; k++) {
              Annotation sAnnot = srcTokens.get(k);
              for(int kk = tst; kk <= ten; kk++) {
                Annotation tAnnot = tgtTokens.get(kk);
                if(!alignment.areTheyAligned(sAnnot, tAnnot)) {
                  if(!alignment.isAnnotationAligned(sAnnot)) {
                    sAnnot.getFeatures().put(
                            Alignment.ALIGNMENT_METHOD_FEATURE_NAME,
                            "phraseAlignAction");
                  }

                  if(!alignment.isAnnotationAligned(tAnnot)) {
                    tAnnot.getFeatures().put(
                            Alignment.ALIGNMENT_METHOD_FEATURE_NAME,
                            "phraseAlignAction");
                  }

                  alignment.align(sAnnot, task.getSrcASName(),
                          task.getSrcDoc(), tAnnot, task.getTgtASName(), task
                                  .getTgtDoc());

                }
              }
            }
            continue outer;
          }
          tStartIndex = 0;
        }
      }
    }
  }

  private String getText(Annotation annot, Document document) {

    if(annot.getFeatures().containsKey(featureToUse)) {
      return annot.getFeatures().get(featureToUse).toString();
    }

    return document.getContent().toString().substring(
            annot.getStartNode().getOffset().intValue(),
            annot.getEndNode().getOffset().intValue()).toLowerCase();
  }

  public String getText(Set<Annotation> annotations, Document document,
          AnnotationSet set) {
    if(annotations == null || annotations.isEmpty()) return null;

    List<Annotation> annotsList = new ArrayList<Annotation>(annotations);
    Collections.sort(annotsList, new gate.util.OffsetComparator());
    String toReturn = "";
    List<Annotation> allAnnots = null;
    int indexOfAnnot = 0;

    for(int i = 0; i < annotsList.size(); i++) {
      Annotation annot = annotsList.get(i);
      if(i == 0) {
        allAnnots = new ArrayList<Annotation>(set.get(annot.getType()));
        Collections.sort(allAnnots, new gate.util.OffsetComparator());
        indexOfAnnot = allAnnots.indexOf(annot);
        if(annot.getFeatures().get(featureToUse) == null) {
          toReturn = document.getContent().toString().substring(
                  annot.getStartNode().getOffset().intValue(),
                  annot.getEndNode().getOffset().intValue());
        }
        else {
          toReturn = (String)annot.getFeatures().get(featureToUse);
        }
      }

      if(i != 0) {
        int tempIndex = allAnnots.indexOf(annot);
        if(indexOfAnnot + 1 != tempIndex) {
          toReturn += " [^ ]+ ";
        }
        else {
          toReturn += " ";
        }
        if(annot.getFeatures().get(featureToUse) == null) {
          toReturn += document.getContent().toString().substring(
                  annot.getStartNode().getOffset().intValue(),
                  annot.getEndNode().getOffset().intValue());
        }
        else {
          toReturn += (String)annot.getFeatures().get(featureToUse);
        }
        indexOfAnnot = tempIndex;
      }
    }
    return toReturn;
  }

  public void cleanup() {
    if(bw == null) {
      try {
        bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(
                new File(outputFileName), false), "UTF-8"));
      }
      catch(UnsupportedEncodingException e) {
        throw new GateRuntimeException(e);
      }
      catch(FileNotFoundException e) {
        throw new GateRuntimeException(e);
      }
    }

    try {
      for(String s : dictionary.keySet()) {
        for(String t : dictionary.get(s)) {
          bw.write(s + "\t" + t);
          bw.newLine();
          bw.flush();
        }
      }
    }
    catch(IOException ioe) {
      throw new GateRuntimeException(ioe);
    }
    finally {
      if(bw != null) {
        try {
          bw.close();
        }
        catch(IOException e) {
          throw new GateRuntimeException(e);
        }
      }
    }
  }

  public String getToolTip() {
    return "Alignment Cache";
  }

  public void executeFinishedAlignmentAction(PUAPair pair)
          throws AlignmentException {

    // task
    AlignmentTask task = pair.getAlignmentTask();

    AnnotationSet sas = task.getSrcAS();
    AnnotationSet tas = task.getTgtAS();

    Alignment alignment = task.getAlignment();

    Set<Annotation> consideredAnnots = new HashSet<Annotation>();
    for(Annotation srcAnnot : pair.getSourceUnitAnnotations()) {
      if(consideredAnnots.contains(srcAnnot)) {
        continue;
      }

      Set<Annotation> srcAlignedAnnots = new HashSet<Annotation>();
      srcAlignedAnnots.add(srcAnnot);
      Set<Annotation> tgtAlignedAnnots = alignment
              .getAlignedAnnotations(srcAnnot);
      if(tgtAlignedAnnots == null || tgtAlignedAnnots.isEmpty()) {
        continue;
      }

      for(Annotation tgtAnnot : tgtAlignedAnnots) {
        consideredAnnots.add(tgtAnnot);
        srcAlignedAnnots.addAll(alignment.getAlignedAnnotations(tgtAnnot));
      }

      String sourceText = getText(srcAlignedAnnots, task.getSrcDoc(), sas);
      String targetText = getText(tgtAlignedAnnots, task.getTgtDoc(), tas);

      if(dictionary.containsKey(sourceText)) {
        if(dictionary.get(sourceText).contains(targetText)) {
          return;
        }
      }

      SortedSet<String> sourceTexts = dictionary.get(sourceText);
      if(sourceTexts == null) {
        sourceTexts = new TreeSet<String>(new Comparator<String>() {
          public int compare(String s1, String s2) {
            String[] s1Array = s1.split("[ ]+");
            String[] s2Array = s2.split("[ ]+");
            return s2Array.length - s1Array.length;
          }
        });
        dictionary.put(sourceText, sourceTexts);
      }
      sourceTexts.add(targetText);
    }
  }
}