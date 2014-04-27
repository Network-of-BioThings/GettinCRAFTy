package gate.qa;

import gate.Annotation;
import gate.AnnotationSet;
import gate.Corpus;
import gate.Document;
import gate.Factory;
import gate.FeatureMap;
import gate.Gate;
import gate.ProcessingResource;
import gate.Resource;
import gate.Utils;
import gate.corpora.DocumentImpl;
import gate.creole.AbstractLanguageAnalyser;
import gate.creole.ExecutionException;
import gate.creole.ResourceInstantiationException;
import gate.creole.SerialAnalyserController;
import gate.creole.metadata.CreoleParameter;
import gate.creole.metadata.CreoleResource;
import gate.creole.metadata.Optional;
import gate.creole.metadata.RunTime;
import gate.persist.PersistenceException;
import gate.security.SecurityException;
import gate.util.GateException;
import gate.util.OffsetComparator;

import java.awt.Color;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/**
 * When documents are annotated using Teamware, anonymous annotation
 * sets are created for annotating annotators. This makes it impossible
 * to run QA on such documents as same name annotation sets in different
 * documents may refer to the annoations created by different
 * annotators. This is especially the case when a requirement is to
 * compute IAA between individual annotators. This PR generates a
 * summary of agreements among annotators. It does this by pairing
 * individual annotators. It also compares each individual annotator's
 * annotations with those available in the consensus annotation set in
 * the respective documents.
 *
 * @author niraj
 *
 */
@CreoleResource(name = "QA Summariser for Teamware",
                comment = "The Quality Assurance PR for teamware",
                helpURL = "http://gate.ac.uk/userguide/sec:eval:qaForTW")
public class QAForTeamwarePR extends AbstractLanguageAnalyser implements
                                                             ProcessingResource {

  /**
   * types of annotations to use
   */
  private List<String> annotationTypes;

  /**
   * features to use
   */
  private List<String> featureNames;

  /**
   * which measure
   */
  private Measure measure;

  /**
   * folder where the output files need to be stored
   */
  private URL outputFolderUrl;

  /**
   * number formatter
   */
  protected NumberFormat f = NumberFormat.getInstance(Locale.ENGLISH);

  /**
   * Quality Assurance PR used internally
   */
  private QualityAssurancePR assurancePR;

  /**
   * used for keeping record of documents annotated by individual
   * annotators
   */
  private Map<String, Set<Object>> annotatorToDocuments;

  /**
   * create combinations of annotators who have done annotations on same
   * documents
   */
  private Map<String, Set<Object>> annotatorsPairToDocuments;

  /**
   * Map used for storing score results
   */
  private Map<String, Map<String, Result>> results = null;

  /**
   * Controller with QA PR as part of it.
   */
  private SerialAnalyserController controller;

  /**
   * name of the consensus annotation set
   */
  public static final String CONSENSUS_AS_NAME = "consensus";

  /**
   * Indicates if the consensus annotation set exists in documents
   */
  private boolean consensusExists = false;

  /** Initialise this resource, and return it. */
  public Resource init() throws ResourceInstantiationException {
    f.setMaximumFractionDigits(2); // format used for all decimal values
    f.setMinimumFractionDigits(2);

    // using QualityAssurancePR internally to calculate QA stats
    // but hiding this PR just in case
    FeatureMap hideParams = Factory.newFeatureMap();
    Gate.setHiddenAttribute(hideParams, true);
    assurancePR = (QualityAssurancePR)Factory.createResource(
            "gate.qa.QualityAssurancePR", Factory.newFeatureMap(), hideParams);

    // we use controller to execute assurance PR
    controller = (SerialAnalyserController)Factory.createResource(
            SerialAnalyserController.class.getName(), Factory.newFeatureMap(),
            hideParams);
    controller.add(assurancePR);
    return this;
  } // init()

  /**
   * The execute method
   */
  public void execute() throws ExecutionException {

    // the corpus cannot be null or empty
    if(corpus == null || corpus.size() == 0) {
      throw new ExecutionException("Corpus cannot be null or empty");
    }

    // similarly user must provide annotation types that they want to
    // compare
    if(annotationTypes == null || annotationTypes.isEmpty())
      throw new ExecutionException(
              "Please provide at least one annotation type to compare");

    // also a measure to use for computation
    if(measure == null) {
      throw new ExecutionException("No measure selected");
    }

    // check if we are processing the last document in the corpus
    Document firstDocument = (Document)corpus.get(0);
    if(firstDocument == document) {
      annotatorToDocuments = new HashMap<String, Set<Object>>();
      annotatorsPairToDocuments = new HashMap<String, Set<Object>>();
      results = new HashMap<String, Map<String, Result>>();
      consensusExists = false;
    }

    // checking if consensus annotation set exists
    consensusExists = (consensusExists || document.getNamedAnnotationSets()
            .containsKey(CONSENSUS_AS_NAME));

    // annotators found in this document
    Set<String> annotators = new HashSet<String>();
    for(Object featureName : document.getFeatures().keySet()) {
      if(featureName instanceof String) {
        String fName = (String)featureName;
        if(fName.startsWith("safe.asname.")) {
          String annotatorName = (String)document.getFeatures().get(fName);
          annotators.add(annotatorName);
        }
      }
    }

    // if no annotators found print a warning
    if(annotators.isEmpty()) {
      System.err.println("No annotators found for the document "
              + document.getName() + "\n"
              + "Please make sure the document is annotated using Teamware!");
    }

    // if documents are loaded from datastore, we store only the
    // persistence Ids
    // or the document object itself
    Object persistenceId = getDocument().getLRPersistenceId();
    Object toStore = document;
    if(persistenceId != null) {
      toStore = persistenceId;
    }

    // which annotator annotated what documents
    for(String annotatorName : annotators) {
      Set<Object> docs = annotatorToDocuments.get(annotatorName);
      if(docs == null) {
        docs = new HashSet<Object>();
        annotatorToDocuments.put(annotatorName, docs);
      }
      docs.add(toStore);
    }

    // given annotator names, we need to find out possible pairings
    List<String> sortedAnnNames = new ArrayList<String>(annotators);
    Collections.sort(sortedAnnNames);
    for(int i = 0; i < sortedAnnNames.size() - 1; i++) {
      String annName1 = sortedAnnNames.get(i);
      for(int j = i + 1; j < sortedAnnNames.size(); j++) {
        String annName2 = sortedAnnNames.get(j);
        String key = annName1 + ";" + annName2;
        Set<Object> docs = annotatorsPairToDocuments.get(key);
        if(docs == null) {
          docs = new HashSet<Object>();
          annotatorsPairToDocuments.put(key, docs);
        }
        docs.add(toStore);
      }
    }

    // check if we are processing the last document in the corpus
    Document lastDocument = (Document)corpus.get(corpus.size() - 1);
    if(lastDocument != document) {
      return;
    }

    // if documents are being loaded from a datastore, it should be
    // deleted
    // after it has been used
    boolean deleteDocs = false;

    // first iteration
    // this is for obtaining IAA between each individual annotator and
    // concensus
    // annotation sets
    for(String annName : annotatorToDocuments.keySet()) {

      // documents annotated by the current annotator
      Set<Object> docs = annotatorToDocuments.get(annName);

      Corpus corpus;
      try {
        corpus = Factory.newCorpus("qaCorpus");
      }
      catch(ResourceInstantiationException e1) {
        throw new ExecutionException(e1);
      }

      // one doc at a time
      for(Object aDoc : docs) {

        // load from ds if not in memory already
        Document gateDoc = null;
        if(!(aDoc instanceof Document)) {
          try {
            gateDoc = (Document)getCorpus().getDataStore().getLr(
                    DocumentImpl.class.getName(), aDoc);
            deleteDocs = true;
          }
          catch(PersistenceException e) {
            throw new ExecutionException(e);
          }
          catch(SecurityException e) {
            throw new ExecutionException(e);
          }
        }
        else {
          gateDoc = (Document)aDoc;
        }

        // creating temporary annotation set with annotator's name
        // across all documents in the corpus and copying annotations
        // produced
        // by him in that document into his annotationset
        createAnnSet(gateDoc, getSetName(gateDoc, annName), annName);

        // add document to the corpus
        // doc will remain in memory until all computations are done
        corpus.add(gateDoc);
      }

      // calculating IAA stats between annotator's annotations and
      // consensus annotation set
      if(consensusExists) {
        calculateIAA(corpus, annName, CONSENSUS_AS_NAME, annName);
      }

      // deleting newly created annotation set and unloading documents
      // if necessary
      for(int k = corpus.size() - 1; k >= 0; k--) {
        Document aDoc = (Document)corpus.get(k);
        if(deleteDocs) {
          Factory.deleteResource(aDoc);
          continue;
        }
        aDoc.removeAnnotationSet(annName);
      }

      // delete the corpus as well
      Factory.deleteResource(corpus);
    }

    // preparing for second iteration
    deleteDocs = false;

    // second iteration
    // this is for obtaining IAA between individual annotators
    for(String annotatorsPair : annotatorsPairToDocuments.keySet()) {

      // documents annotated by this pair of annotators
      Set<Object> docs = annotatorsPairToDocuments.get(annotatorsPair);
      Corpus corpus;
      try {
        corpus = Factory.newCorpus("qaCorpus");
      }
      catch(ResourceInstantiationException e1) {
        throw new ExecutionException(e1);
      }

      // annotators in this pair
      String annotator1 = annotatorsPair.substring(0, annotatorsPair
              .indexOf(';'));
      String annotator2 = annotatorsPair
              .substring(annotatorsPair.indexOf(';') + 1);

      // one doc at a time
      for(Object aDoc : docs) {

        // load the doc from DS if needed
        Document gateDoc = null;
        if(!(aDoc instanceof Document)) {
          try {
            gateDoc = (Document)getCorpus().getDataStore().getLr(
                    DocumentImpl.class.getName(), aDoc);
            deleteDocs = true;
          }
          catch(PersistenceException e) {
            throw new ExecutionException(e);
          }
          catch(SecurityException e) {
            throw new ExecutionException(e);
          }
        }
        else {
          gateDoc = (Document)aDoc;
        }

        // creating sets for individual annotators and copying
        // annotations from
        // their anonymous annotation sets into their own annotation set
        createAnnSet(gateDoc, getSetName(gateDoc, annotator1), annotator1);
        createAnnSet(gateDoc, getSetName(gateDoc, annotator2), annotator2);

        // adding doc to the corpus
        corpus.add(gateDoc);
      }

      // calculate IAA
      calculateIAA(corpus, annotator1, annotator2, annotator1 + "-"
              + annotator2);

      // deleting temporarily created annotation sets and deleting docs
      // from
      // memory if docs were loaded from the DS
      for(int k = corpus.size() - 1; k >= 0; k--) {
        Document aDoc = (Document)corpus.get(k);

        if(deleteDocs) {
          Factory.deleteResource(aDoc);
          continue;
        }

        aDoc.removeAnnotationSet(annotator1);
        aDoc.removeAnnotationSet(annotator2);
      }

      // delete the corpus
      Factory.deleteResource(corpus);
    }

    // generating summary
    // only accept the files with document-stats at the end
    File[] resultFiles = new File(outputFolderUrl.getFile())
            .listFiles(new FileFilter() {
              public boolean accept(File pathname) {
                return pathname.getAbsolutePath().endsWith(
                        "-document-stats.html");
              }
            });

    // authors found in the corpus
    List<String> columnAuthorNames = new ArrayList<String>();
    double consensusMacro = 0.0D;
    double consensusMicro = 0.0D;
    double annotatorMacro = 0.0D;
    double annotatorMicro = 0.0D;

    // one file at a time
    for(File file : resultFiles) {

      // finding author names
      String fileName = file.getName().substring(0,
              file.getName().indexOf("-document-stats.html"));
      int index = fileName.indexOf('-');
      String author1 = null;
      String author2 = null;

      // if only one author found, it means this file is for author vs
      // consensus
      // IAA
      if(index < 0) {
        author1 = fileName;
        author2 = CONSENSUS_AS_NAME;
      }
      else {
        author1 = fileName.substring(0, index);
        author2 = fileName.substring(index + 1);
      }

      if(!columnAuthorNames.contains(author2)) {
        columnAuthorNames.add(author2);
      }

      if(!columnAuthorNames.contains(author1)) {
        columnAuthorNames.add(author1);
      }

      // loading the document-stats file as GATE document
      // and utilizing original markups to collect the needed
      // information
      // create a gate document
      Document aDoc = null;
      try {
        aDoc = Factory.newDocument(file.toURI().toURL());
      }
      catch(ResourceInstantiationException e) {
        throw new ExecutionException(e);
      }
      catch(MalformedURLException e) {
        throw new ExecutionException(e);
      }

      // we're interested in macro and micro averages figures only
      // these are the last two rows in the document-stats
      AnnotationSet omSet = aDoc.getAnnotations("Original markups");
      List<Annotation> rows = new ArrayList<Annotation>(omSet.get("tr"));
      Collections.sort(rows, new OffsetComparator());

      // temporary result object
      Result r = new Result();
      r.documentFileName = file.getName();

      // consider last row for micro summary
      Annotation row = rows.get(rows.size() - 1);
      List<Annotation> cols = new ArrayList<Annotation>(omSet.getContained(
              Utils.start(row), Utils.end(row)).get("td"));
      Collections.sort(cols, new OffsetComparator());

      // only interested in the last column
      r.micro = Double.parseDouble(Utils.stringFor(aDoc, cols
              .get(cols.size() - 1)));

      // consider second last row for macro summary
      row = rows.get(rows.size() - 2);
      cols = new ArrayList<Annotation>(omSet.getContained(Utils.start(row),
              Utils.end(row)).get("td"));
      Collections.sort(cols, new OffsetComparator());

      // only interested in the last column
      r.macro = Double.parseDouble(Utils.stringFor(aDoc, cols
              .get(cols.size() - 1)));

      // delete the document from GATe
      Factory.deleteResource(aDoc);

      // making two entries
      // i.e. author1-author2 and author2-author1
      Map<String, Result> authorResults = results.get(author1);
      if(authorResults == null) {
        authorResults = new HashMap<String, Result>();
        results.put(author1, authorResults);
      }

      authorResults.put(author2, r);

      authorResults = results.get(author2);
      if(authorResults == null) {
        authorResults = new HashMap<String, Result>();
        results.put(author2, authorResults);
      }

      authorResults.put(author1, r);
    }

    // collected all the results
    Collections.sort(columnAuthorNames);

    // generating html file contents
    StringBuffer buffer = new StringBuffer();
    buffer.append("<html>\n<title>Summary of IAA Results</title>\n<body>\n");
    buffer.append("<h1>Summary of IAA Results</h1>");
    buffer.append("<b>AnnotationTypes:</b> ");
    for(String aType : annotationTypes) {
      buffer.append(aType + ";");
    }
    buffer.append("<br>");
    buffer.append("<b>Features:</b> ");
    if(featureNames != null && !featureNames.isEmpty()) {
      for(String aFeature : featureNames) {
        buffer.append(aFeature + ";");
      }
    }
    buffer.append("<br>");
    buffer.append("<b>Measure:</b> " + measure.toString() + "<br>");
    buffer.append("<table border=\"1\">\n");

    // first row
    buffer.append("\t<tr>\n");
    buffer.append("\t\t<td><b>Author Names</b></td>\n");
    for(String author2 : columnAuthorNames) {
      buffer.append("\t\t<td colspan=\"2\"><b>" + author2 + "</b></td>\n");
    }
    buffer.append("\t\t<td colspan=\"2\"><b> Averages </b></td>\n");
    buffer.append("\t</tr>\n");

    // second row
    buffer.append("\t<tr>\n");
    buffer.append("\t\t<td>&nbsp;</td>\n");

    // additional columns for averages
    for(int i = 0; i <= columnAuthorNames.size(); i++) {
      buffer.append("\t\t<td><b>Macro</b></td>\n");
      buffer.append("\t\t<td><b>Micro</b></td>\n");
    }
    buffer.append("\t</tr>\n");

    // color coding with transperancy
    // 1.0 = dark green
    // ... as we proceed we lighten the green color
    // 0.5 = white
    // ... as we proceed we brighten the red color
    // 0.0 = red

    List<String> authorNamesList = new ArrayList<String>(results.keySet());
    Collections.sort(authorNamesList);

    // all rows onwards
    // producing matrix
    for(String author1 : authorNamesList) {
      double annMacro = 0.0D;
      double annMicro = 0.0D;
      int docs = 0;

      buffer.append("\t<tr>\n");
      buffer.append("\t\t<td><b>" + author1 + "</b></td>\n");

      Map<String, Result> resultsForAuthor1 = results.get(author1);

      // columns
      for(String author2 : columnAuthorNames) {
        Result r = resultsForAuthor1.get(author2);
        if(r == null) {
          buffer.append("\t\t<td colspan=\"2\">&nbsp;</td>\n");
        }
        else {

          // append macro micro figures to the table
          appendMacroMicroFigures(buffer, r.macro, r.micro);

          if(author1.equals(CONSENSUS_AS_NAME)) {
            consensusMacro += r.macro;
            consensusMicro += r.micro;
          }
          else {
            annMacro += r.macro;
            annMicro += r.micro;
            docs++;
          }
        }
      }

      // adding averages to the last 2 columns
      if(author1.equals(CONSENSUS_AS_NAME)) {
        double caMacroAvg = (double)(consensusMacro / (double)(columnAuthorNames
                .size() - 1));
        double caMicroAvg = (double)(consensusMicro / (double)(columnAuthorNames
                .size() - 1));

        appendMacroMicroFigures(buffer, caMacroAvg, caMicroAvg);
      }
      else {
        double aaMacroAvg = (double)(annMacro / (double)(docs));
        double aaMicroAvg = (double)(annMicro / (double)(docs));

        annotatorMacro += aaMacroAvg;
        annotatorMicro += aaMicroAvg;

        appendMacroMicroFigures(buffer, aaMacroAvg, aaMicroAvg);
      }

      buffer.append("\t</tr>\n");

      buffer.append("\t<tr>\n");
      buffer.append("\t\t<td>&nbsp;</td>\n");

      for(String author2 : columnAuthorNames) {
        Result r = resultsForAuthor1.get(author2);
        if(r == null) {
          buffer.append("\t\t<td colspan=\"2\">&nbsp;</td>\n");
        }
        else {
          buffer.append("\t\t<td colspan=\"2\"><a target=\"_blank\" href=\""
                  + r.documentFileName + "\">document</a></td>\n");
        }
      }

      buffer.append("\t\t<td colspan=\"2\">&nbsp;</td>\n");
    }

    buffer.append("</table>\n");

    buffer.append("<hr>");

    if(consensusExists) {
      buffer.append("<br><b>Avg. "
              + CONSENSUS_AS_NAME
              + " macro avg:</b> "
              + (f.format((double)consensusMacro
                      / (columnAuthorNames.size() - 1))));
      buffer.append("<br><b>Avg. "
              + CONSENSUS_AS_NAME
              + " micro avg:</b> "
              + (f.format((double)consensusMicro
                      / (columnAuthorNames.size() - 1))));
    }

    // if consensus annotation set exists, make sure the number used in
    // demoninator does not include consensus as one annotator
    int totalAuthors = consensusExists ?
            columnAuthorNames.size() - 1 : columnAuthorNames.size();

    buffer.append("<br><b>Avg. IAA macro avg:</b> "
            + (f
                    .format((double)annotatorMacro
                            / totalAuthors)));
    buffer.append("<br><b>Avg. IAA micro avg:</b> "
            + (f
                    .format((double)annotatorMicro
                            / totalAuthors)));
    buffer.append("</body>\n</html>");

    BufferedWriter bw = null;
    try {
      File indexFile = new File(outputFolderUrl.getFile(), "index.html");
      bw = new BufferedWriter(new FileWriter(indexFile));
      bw.write(buffer.toString());
    }
    catch(IOException ioe) {
      throw new ExecutionException(ioe);
    }
    finally {
      if(bw != null) {
        try {
          bw.close();
        }
        catch(IOException e) {
          throw new ExecutionException(e);
        }
      }
    }

  }

  /**
   * A method that adds two columns to the buffer - one for the macro
   * figure and the other one for the micro figure. It also adds
   * relevant style tag to give a proper color to each cell depending on
   * the value of macro and micro figures. See documentation of the
   * getStyleTag(double) for more information on how cell backgrounds
   * are color coded.
   *
   * @param buffer
   * @param macro
   * @param micro
   */
  private void appendMacroMicroFigures(StringBuffer buffer, double macro,
          double micro) {
    String macCol = getStyleTag(macro);
    String micCol = getStyleTag(micro);

    buffer.append("\t\t<td " + macCol + " >" + f.format(macro) + "</td>\n");
    buffer.append("\t\t<td " + micCol + " >" + f.format(micro) + "</td>\n");

  }

  /**
   * Gets the style tag based on the score.
   *
   * The color green is used for a cell background to indicate full
   * agreement (i.e. 1.0). The background color becomes lighter as the
   * agreement reduces towards 0.5. At 0.5 agreement, the background
   * color of a cell is fully white. From 0.5 downwards, the color red
   * is used and as the agreement reduces further, the color becomes
   * darker with dark red at 0.0 agreement.
   *
   * @param score
   * @return
   */
  private String getStyleTag(double score) {

    // two colors
    Color gc = Color.GREEN;
    Color rc = Color.RED;

    Color c = null;

    // if score is above .50, use the green color, otherwise the red one
    if(score > 0.50) {
      c = new Color(gc.getRed(), gc.getGreen(), gc.getBlue(),
              ((int)(score * 100) * 2) - 100);
    }
    else {
      c = new Color(rc.getRed(), rc.getGreen(), rc.getBlue(),
              100 - ((int)(score * 100) * 2));
    }

    return "style=\"background-color: rgba(" + c.getRed() + "," + c.getGreen()
            + "," + c.getBlue() + "," + ((double)c.getAlpha() / 100) + ")\"";
  }

  /**
   * Given the annotator name, the method finds out the annotation set
   * which contains annotations for that annotator
   *
   * @param doc
   * @param annotatorName
   * @return
   */
  private String getSetName(Document doc, String annotatorName) {
    String inputAS = null;
    for(Object key : doc.getFeatures().keySet()) {
      Object val = doc.getFeatures().get(key);
      if(val.equals(annotatorName)) {
        inputAS = key.toString().substring(12);
        break;
      }
    }
    return inputAS;
  }

  /**
   * Creating a temporary annotation set
   *
   * @param doc
   * @param inputAS
   * @param outputAS
   */
  private void createAnnSet(Document doc, String inputAS, String outputAS) {

    AnnotationSet inAS = inputAS == null || inputAS.trim().length() == 0 ? doc
            .getAnnotations() : doc.getAnnotations(inputAS);
    inAS = inAS.get(new HashSet<String>(annotationTypes));
    AnnotationSet outAS = doc.getAnnotations(outputAS);
    for(Annotation a : inAS) {
      outAS.add(a.getStartNode(), a.getEndNode(), a.getType(), a.getFeatures());
    }
  }

  /**
   * A method that calculates IAA between the given annotation sets
   *
   * @param corpus
   * @param keyAS
   * @param responseAS
   * @param filePrefix
   * @throws ExecutionException
   */
  private void calculateIAA(Corpus corpus, String keyAS, String responseAS,
          String filePrefix) throws ExecutionException {
    try {
      assurancePR.reInit();
    }
    catch(ResourceInstantiationException rie) {
      throw new ExecutionException(rie);
    }

    // lets set the params on qualityAssurancePR
    assurancePR.setAnnotationTypes(annotationTypes);
    assurancePR.setFeatureNames(featureNames == null
            ? new ArrayList<String>()
            : featureNames);
    assurancePR.setMeasure(measure);
    assurancePR.setOutputFolderUrl(outputFolderUrl);

    // the remaining two params will be set later
    assurancePR.setKeyASName(keyAS);
    assurancePR.setResponseASName(responseAS);

    controller.setCorpus(corpus);
    controller.execute();

    // QA PR produces two stats file, one for corpus and one for
    // documents
    // for QAForTeamware, we don't need corpus one
    File corpusFile = new File(outputFolderUrl.getFile(), "corpus-stats.html");
    corpusFile.delete();

    File documentFile = new File(outputFolderUrl.getFile(),
            "document-stats.html");
    File documentOutFile = new File(outputFolderUrl.getFile(), filePrefix
            + "-document-stats.html");
    documentFile.renameTo(documentOutFile);
  }

  /**
   * Method called when a PR is unloaded.
   */
  public void cleanup() {
    super.cleanup();
    Factory.deleteResource(assurancePR);
    Factory.deleteResource(controller);
  }

  /**
   * Annotation types for which the stats should be calculated
   *
   * @return
   */
  public List<String> getAnnotationTypes() {
    return annotationTypes;
  }

  /**
   * Annotation types for which the stats should be calculated
   *
   * @param annotationTypes
   */
  @RunTime
  @CreoleParameter
  public void setAnnotationTypes(List<String> annotationTypes) {
    this.annotationTypes = annotationTypes;
  }

  /**
   * Features names for which the stats should be calculated
   *
   * @return
   */
  public List<String> getFeatureNames() {
    return featureNames;
  }

  /**
   * Features names for which the stats should be calculated
   *
   * @param featureNames
   */
  @RunTime
  @Optional
  @CreoleParameter
  public void setFeatureNames(List<String> featureNames) {
    this.featureNames = featureNames;
  }

  /**
   * Measure to use for stats calculation
   *
   * @return
   */
  public Measure getMeasure() {
    return measure;
  }

  /**
   * Measure to use for stats calculation
   *
   * @param measure
   */
  @RunTime
  @CreoleParameter
  public void setMeasure(Measure measure) {
    this.measure = measure;
  }

  /**
   * URL of the folder to store output files into
   *
   * @return
   */
  public URL getOutputFolderUrl() {
    return outputFolderUrl;
  }

  /**
   * URL of the folder to store output files into
   *
   * @param outputFolderUrl
   */
  @RunTime
  @CreoleParameter(suffixes = "html")
  public void setOutputFolderUrl(URL outputFolderUrl) {
    this.outputFolderUrl = outputFolderUrl;
  }

  /**
   * Storing individual results for each pair of annotators
   *
   * @author niraj
   */
  class Result {
    // macro average
    double macro = 0.0D;

    // micro average
    double micro = 0.0D;

    // name of the file to link to
    String documentFileName;
  }
}
