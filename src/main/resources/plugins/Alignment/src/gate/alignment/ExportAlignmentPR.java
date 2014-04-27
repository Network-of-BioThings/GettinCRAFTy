package gate.alignment;

import gate.Annotation;
import gate.AnnotationSet;
import gate.Document;
import gate.ProcessingResource;
import gate.Resource;
import gate.Utils;
import gate.compound.CompoundDocument;
import gate.creole.AbstractLanguageAnalyser;
import gate.creole.ExecutionException;
import gate.creole.ResourceInstantiationException;
import gate.creole.metadata.CreoleParameter;
import gate.creole.metadata.CreoleResource;
import gate.creole.metadata.Optional;
import gate.creole.metadata.RunTime;
import gate.util.OffsetComparator;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * A PR to export alignment information in an xml file.
 * 
 * @author niraj
 * 
 */
@CreoleResource(comment = "A PR to export alignment information in an xml file.")
public class ExportAlignmentPR extends AbstractLanguageAnalyser implements
                                                               ProcessingResource {

  private static final long serialVersionUID = 4755458725235429653L;

  /**
   * Directory where the resulting files should be stored to.
   */
  private URL outputDirectory;

  /**
   * File object obtained from the outputDirectory
   */
  private File directory;

  /**
   * name of the document feature that has the information about
   * alignments of the alignment units.
   */
  private String unitAlignmentFeatureName;

  /**
   * name of the document feature that has the information about
   * alignments of the parent of the alignment units.
   */
  private String parentOfUnitOfAlignmentFeatureName;

  /**
   * annotation type that has been used as parent of unit of alignment.
   */
  private String parentOfUnitOfAlignment;

  /**
   * annotation type that has been used as unit of alignment.
   */
  private String unitOfAlignment;

  /**
   * id of the source document
   */
  private String sourceDocumentID;

  /**
   * id of the target document
   */
  private String targetDocumentID;

  /**
   * name of the annotation set which has annotations for the unit of
   * alignment and parent of unit of alignment
   */
  private String inputASName;

  /** Initialise this resource, and return it. */
  public Resource init() throws ResourceInstantiationException {
    return this;

  }

  /* this method is called to re-initialise the resource */
  public void reInit() throws ResourceInstantiationException {

    // re-initialisation code
    init();
  }

  /**
   * Internal class to store information about a pair of parent of unit
   * of alignment.
   * 
   * @author niraj
   * 
   */
  class PUPair {
    /**
     * Source annotations
     */
    Set<Annotation> srcAnnots = new HashSet<Annotation>();

    /**
     * Target annotations.
     */
    Set<Annotation> tgtAnnots = new HashSet<Annotation>();
  }

  /**
   * Called when user clicks on the execute button. The main logic of
   * the PR.
   */
  public void execute() throws ExecutionException {

    // check if the right document provided
    if(!(this.document instanceof CompoundDocument)) {
      throw new ExecutionException(this.document.getName()
              + "not instance of CompoundDocument");
    }

    // checking for the output directory. if doesn't exist, it is
    // created
    try {
      directory = new File(outputDirectory.toURI());

      if(!directory.exists()) {
        directory.mkdirs();
      }
      else if(!directory.isDirectory()) {
        throw new ExecutionException(outputDirectory.toString()
                + "does not refer to a directory");
      }
    }
    catch(URISyntaxException e) {
      throw new ExecutionException(e);
    }

    // document is a compound document
    CompoundDocument cd = (CompoundDocument)this.document;

    // writer to create an xml
    BufferedWriter bw = null;

    // name of the file is the name of the document
    try {
      String fileName = this.document.getName();
      File fileToOutput = new File(directory, fileName);

      // if file is already there, create a new one
      if(fileToOutput.exists()) {
        fileToOutput = new File(directory, fileName
                + ("" + Math.random()).substring(2, 5));
      }

      bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(
              fileToOutput), "UTF-8"));
    }
    catch(UnsupportedEncodingException e) {
      throw new ExecutionException(e);
    }
    catch(FileNotFoundException e) {
      throw new ExecutionException(e);
    }

    // obtain the source document
    Document srcDoc = cd.getDocument(sourceDocumentID);
    AnnotationSet srcAS = (inputASName == null || inputASName.trim().length() == 0)
            ? srcDoc.getAnnotations()
            : srcDoc.getAnnotations(inputASName);

    // obtain the target document
    Document tgtDoc = cd.getDocument(targetDocumentID);
    AnnotationSet tgtAS = (inputASName == null || inputASName.trim().length() == 0)
            ? tgtDoc.getAnnotations()
            : tgtDoc.getAnnotations(inputASName);

    // parent of unit of alignment is optional. if user hasn't provided
    // it
    // use the document as the parent instead.
    AnnotationSet parentSrcAS = srcAS;
    AnnotationSet parentTgtAS = tgtAS;
    boolean docAsParent = true;

    if(parentOfUnitOfAlignment != null
            && parentOfUnitOfAlignment.trim().length() != 0) {
      parentSrcAS = srcAS.get(parentOfUnitOfAlignment);
      parentTgtAS = tgtAS.get(parentOfUnitOfAlignment);
      docAsParent = false;
    }

    // this is where we store pairs of parent annotations
    List<PUPair> puPairs = new ArrayList<PUPair>();

    // if parentOfUnitOfAlignmentFeatureName is not provided, we
    // consider annotations in sequence
    if(parentOfUnitOfAlignmentFeatureName == null
            || parentOfUnitOfAlignmentFeatureName.trim().length() == 0) {

      if(!docAsParent) {
        List<Annotation> srcAnnots = new ArrayList<Annotation>(parentSrcAS);
        List<Annotation> tgtAnnots = new ArrayList<Annotation>(parentTgtAS);
        Collections.sort(srcAnnots, new OffsetComparator());
        Collections.sort(tgtAnnots, new OffsetComparator());

        for(int i = 0; i < srcAnnots.size() && i < tgtAnnots.size(); i++) {
          PUPair pair = new PUPair();
          pair.srcAnnots.add(srcAnnots.get(i));
          pair.tgtAnnots.add(tgtAnnots.get(i));
          puPairs.add(pair);
        }
      }
      else {
        // empty one
        puPairs.add(new PUPair());
      }
    }
    else {

      Alignment puAlignment = cd
              .getAlignmentInformation(parentOfUnitOfAlignmentFeatureName);
      Set<Annotation> annots = puAlignment.getAlignedAnnotations();
      annots.retainAll(parentSrcAS);

      for(Annotation srcPU : annots) {

        Set<Annotation> tgtPUs = puAlignment.getAlignedAnnotations(srcPU);
        Set<Annotation> srcPUs = new HashSet<Annotation>();

        for(Annotation tgtPU : tgtPUs) {
          srcPUs.addAll(puAlignment.getAlignedAnnotations(tgtPU));
        }

        PUPair pair = new PUPair();
        pair.srcAnnots.addAll(srcPUs);
        pair.tgtAnnots.addAll(tgtPUs);
        puPairs.add(pair);
      }
    }

    // create a file
    StringBuilder xml = new StringBuilder();
    xml = xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");

    xml = xml.append("<Document>\n");

    // one pair at a time
    for(PUPair puPair : puPairs) {

      xml = xml.append("<PUPair>\n");
      Set<Annotation> srcUAnnots = new HashSet<Annotation>();
      Set<Annotation> tgtUAnnots = new HashSet<Annotation>();

      xml = xml.append("\n<Source>\n");
      if(puPair.srcAnnots.isEmpty()) {
        // first source annotations
        AnnotationSet srcUAnnotsAS = srcAS.get(unitOfAlignment);
        List<Annotation> srcUAnnotsList = new ArrayList<Annotation>(
                srcUAnnotsAS);
        Collections.sort(srcUAnnotsList, new OffsetComparator());
        for(Annotation srcUAnnot : srcUAnnotsList) {
          xml = xml.append("<" + unitOfAlignment + " id=\""
                  + srcUAnnot.getId().intValue() + "\">");
          xml = xml.append(Utils.stringFor(srcDoc, srcUAnnot));
          xml = xml.append("</" + unitOfAlignment + ">");
        }
        srcUAnnots.addAll(srcUAnnotsList);
      }
      else {
        for(Annotation puAnnot : puPair.srcAnnots) {
          xml = xml.append("<" + parentOfUnitOfAlignment + ">");

          // first source annotations
          AnnotationSet srcUAnnotsAS = srcAS.getContained(
                  puAnnot.getStartNode().getOffset(),
                  puAnnot.getEndNode().getOffset()).get(unitOfAlignment);
          List<Annotation> srcUAnnotsList = new ArrayList<Annotation>(
                  srcUAnnotsAS);
          Collections.sort(srcUAnnotsList, new OffsetComparator());
          for(Annotation srcUAnnot : srcUAnnotsList) {
            xml = xml.append("<" + unitOfAlignment + " id=\""
                    + srcUAnnot.getId().intValue() + "\">");
            xml = xml.append(Utils.stringFor(srcDoc, srcUAnnot));
            xml = xml.append("</" + unitOfAlignment + ">");
          }
          srcUAnnots.addAll(srcUAnnotsList);
          xml = xml.append("</" + parentOfUnitOfAlignment + ">");
        }
      }
      xml = xml.append("\n</Source>\n");

      xml = xml.append("\n<Target>\n");
      // then target annots
      if(puPair.tgtAnnots.isEmpty()) {
        AnnotationSet tgtUAnnotsAS = tgtAS.get(unitOfAlignment);
        List<Annotation> tgtUAnnotsList = new ArrayList<Annotation>(
                tgtUAnnotsAS);
        Collections.sort(tgtUAnnotsList, new OffsetComparator());
        for(Annotation tgtUAnnot : tgtUAnnotsList) {
          xml = xml.append("<" + unitOfAlignment + " id=\""
                  + tgtUAnnot.getId().intValue() + "\">");
          xml = xml.append(Utils.stringFor(tgtDoc, tgtUAnnot));
          xml = xml.append("</" + unitOfAlignment + ">");
        }
        tgtUAnnots.addAll(tgtUAnnotsList);
      }
      else {
        for(Annotation puAnnot : puPair.tgtAnnots) {
          xml = xml.append("<" + parentOfUnitOfAlignment + ">");
          // first source annotations
          AnnotationSet tgtUAnnotsAS = tgtAS.getContained(
                  puAnnot.getStartNode().getOffset(),
                  puAnnot.getEndNode().getOffset()).get(unitOfAlignment);
          List<Annotation> tgtUAnnotsList = new ArrayList<Annotation>(
                  tgtUAnnotsAS);
          Collections.sort(tgtUAnnotsList, new OffsetComparator());
          for(Annotation tgtUAnnot : tgtUAnnotsList) {
            xml = xml.append("<" + unitOfAlignment + " id=\""
                    + tgtUAnnot.getId().intValue() + "\">");
            xml = xml.append(Utils.stringFor(tgtDoc, tgtUAnnot));
            xml = xml.append("</" + unitOfAlignment + ">");
          }
          tgtUAnnots.addAll(tgtUAnnotsList);
          xml = xml.append("</" + parentOfUnitOfAlignment + ">");
        }
      }
      xml = xml.append("\n</Target>\n");

      // its time for providing alignment information
      xml = xml.append("\n<Alignment>\n");

      Alignment uAlignment = cd
              .getAlignmentInformation(unitAlignmentFeatureName);

      for(Annotation srcUAnnot : srcUAnnots) {

        Set<Annotation> tgtAlignedAnnots = uAlignment
                .getAlignedAnnotations(srcUAnnot);
        if(tgtAlignedAnnots == null || tgtAlignedAnnots.isEmpty()) {
          continue;
        }

        Set<Annotation> srcAlignedAnnots = new HashSet<Annotation>();
        for(Annotation tgtUAnnot : tgtAlignedAnnots) {
          srcAlignedAnnots.addAll(uAlignment.getAlignedAnnotations(tgtUAnnot));
        }

        srcAlignedAnnots.retainAll(srcUAnnots);
        tgtAlignedAnnots.retainAll(tgtUAnnots);

        for(Annotation srcAA : srcAlignedAnnots) {
          for(Annotation tgtAA : tgtAlignedAnnots) {
            xml = xml.append("<Alignment source=\"" + srcAA.getId().intValue()
                    + "\" target=\"" + tgtAA.getId().intValue() + "\"/>\n");
          }
        }
      }

      xml = xml.append("\n</Alignment>\n");
      xml = xml.append("\n</PUPair>\n");
    }
    xml = xml.append("</Document>");

    try {
      bw.write(xml.toString());
      bw.close();
    }
    catch(IOException ioe) {
      throw new ExecutionException(ioe);
    }
  }

  public URL getOutputDirectory() {
    return outputDirectory;
  }

  @RunTime
  @CreoleParameter
  public void setOutputDirectory(URL outputFile) {
    this.outputDirectory = outputFile;
  }

  public String getParentOfUnitOfAlignment() {
    return parentOfUnitOfAlignment;
  }

  @RunTime
  @Optional
  @CreoleParameter(defaultValue = "Sentence")
  public void setParentOfUnitOfAlignment(String parentOfUnitOfAlignment) {
    this.parentOfUnitOfAlignment = parentOfUnitOfAlignment;
  }

  public String getUnitOfAlignment() {
    return unitOfAlignment;
  }

  @RunTime
  @CreoleParameter(defaultValue = "Token")
  public void setUnitOfAlignment(String unitOfAlignment) {
    this.unitOfAlignment = unitOfAlignment;
  }

  public String getUnitAlignmentFeatureName() {
    return unitAlignmentFeatureName;
  }

  @RunTime
  @CreoleParameter(defaultValue = "word-alignment")
  public void setUnitAlignmentFeatureName(String unitAlignmentFeatureName) {
    this.unitAlignmentFeatureName = unitAlignmentFeatureName;
  }

  public String getParentOfUnitOfAlignmentFeatureName() {
    return parentOfUnitOfAlignmentFeatureName;
  }

  @RunTime
  @Optional
  @CreoleParameter(defaultValue = "sentence-alignment")
  public void setParentOfUnitOfAlignmentFeatureName(
          String parentOfUnitOfAlignmentFeatureName) {
    this.parentOfUnitOfAlignmentFeatureName = parentOfUnitOfAlignmentFeatureName;
  }

  public String getSourceDocumentID() {
    return sourceDocumentID;
  }

  @RunTime
  @CreoleParameter
  public void setSourceDocumentID(String sourceDocumentID) {
    this.sourceDocumentID = sourceDocumentID;
  }

  public String getTargetDocumentID() {
    return targetDocumentID;
  }

  @RunTime
  @CreoleParameter
  public void setTargetDocumentID(String targetDocumentID) {
    this.targetDocumentID = targetDocumentID;
  }

  public String getInputASName() {
    return inputASName;
  }

  @RunTime
  @Optional
  @CreoleParameter(defaultValue = "")
  public void setInputASName(String inputASName) {
    this.inputASName = inputASName;
  }
}