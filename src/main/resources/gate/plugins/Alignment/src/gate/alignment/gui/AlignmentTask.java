package gate.alignment.gui;

import gate.Annotation;
import gate.AnnotationSet;
import gate.Document;
import gate.alignment.Alignment;
import gate.compound.CompoundDocument;
import gate.util.GateRuntimeException;
import gate.util.OffsetComparator;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;

/**
 * Alignment Task class, used for retrieving PUAPairs
 * 
 * @author gate
 */
public class AlignmentTask {

  /**
   * List of pairs of PUAs
   */
  List<PUAPair> puaList;

  /**
   * Compound document
   */
  CompoundDocument compoundDocument;

  Alignment alignment;

  Document srcDoc;

  Document tgtDoc;

  AnnotationSet srcAS;

  AnnotationSet tgtAS;

  String srcDocId;

  String tgtDocId;

  String srcASName;

  String tgtASName;

  String uaAnnotType;

  String puaAnnotType;

  String puaFeatureName;

  String uaFeatureName;

  String name;

  String actionsFilePath;

  String alignmentView;

  int counter = -1;

  AlignmentActionsManager alignmentActionsManager = null;

  /**
   * @param compoundDocument
   */
  public AlignmentTask(CompoundDocument compoundDocument) {
    this.compoundDocument = compoundDocument;
  }// AlignmentTask

  public void initialize(String name, String srcDocId, String tgtDocId,
          String srcASName, String tgtASName, String uaAnnotType,
          String puaAnnotType, String puaFeatureName, String uaFeatureName,
          String alignmentView, String actionsFilePath) {
    this.srcDocId = srcDocId;
    this.tgtDocId = tgtDocId;
    this.srcASName = srcASName;
    this.tgtASName = tgtASName;
    this.name = name;
    this.alignmentView = alignmentView;
    this.actionsFilePath = actionsFilePath;

    srcDoc = compoundDocument.getDocument(srcDocId);
    tgtDoc = compoundDocument.getDocument(tgtDocId);
    srcAS = srcASName.equals(AlignmentEditor.DEFAULT_AS_NAME) ? srcDoc
            .getAnnotations() : srcDoc.getAnnotations(srcASName);
    tgtAS = tgtASName.equals(AlignmentEditor.DEFAULT_AS_NAME) ? tgtDoc
            .getAnnotations() : tgtDoc.getAnnotations(tgtASName);
    this.puaFeatureName = puaFeatureName;
    this.uaFeatureName = uaFeatureName;
    this.uaAnnotType = uaAnnotType;
    this.puaAnnotType = puaAnnotType;
    
    Alignment puaAlignment = this.compoundDocument
            .getAlignmentInformation(puaFeatureName);
    alignment = this.compoundDocument.getAlignmentInformation(uaFeatureName);
    puaList = new ArrayList<PUAPair>();

    // only if parentOfUnitOfAlignment is provided
    if(puaAnnotType != null && puaAnnotType.trim().length() > 0) {
      Set<Annotation> srcVisitedAnnots = new HashSet<Annotation>();

      // sort annotations - this will return pairs in the sorted order
      List<Annotation> srcAnnotsList = new ArrayList<Annotation>(srcAS
              .get(puaAnnotType));
      Collections.sort(srcAnnotsList, new OffsetComparator());

      // find out all linked annotations and create pauListItem
      for(Annotation srcAnnot : srcAnnotsList) {
        if(srcVisitedAnnots.contains(srcAnnot)) continue;

        if(puaAlignment.isAnnotationAligned(srcAnnot)) {
          Set<Annotation> srcAnnots = new HashSet<Annotation>();
          Set<Annotation> tgtAnnots = puaAlignment
                  .getAlignedAnnotations(srcAnnot);
          for(Annotation tgtAnnot : tgtAnnots) {
            srcAnnots.addAll(puaAlignment.getAlignedAnnotations(tgtAnnot));
          }

          srcAnnots.retainAll(srcAS);
          tgtAnnots.retainAll(tgtAS);

          srcVisitedAnnots.addAll(srcAnnots);
          puaList.add(new PUAPair(this, srcAnnots, tgtAnnots));
        }
      }
    }
    else {
      // entire document is a single parent of unit
      puaList.add(new PUAPair(this, null, null));
    }

    alignmentActionsManager = new AlignmentActionsManager(this, actionsFilePath);
  }

  public String toString() {
    StringBuffer buffer = new StringBuffer();
    buffer.append("Task Name:"+getName());
    buffer.append("\n");
    buffer.append("Source Doc : "+getSrcDocId());
    buffer.append("\n");
    buffer.append("Target Doc : "+getTgtDocId());
    buffer.append("\n");
    buffer.append("Source AS Name : "+getSrcASName());
    buffer.append("\n");
    buffer.append("Target AS Name : "+getTgtASName());
    buffer.append("\n");
    buffer.append("PUA Type : "+getPuaAnnotType());
    buffer.append("\n");
    buffer.append("UA Type : "+getUaAnnotType());
    buffer.append("\n");
    buffer.append("PUA Alignment Feature : "+getPuaFeatureName());
    buffer.append("\n");
    buffer.append("UA Alignment Feature : "+getUaFeatureName());
    buffer.append("\n");
    buffer.append("Alignment view: "+getAlignmentView());
    return buffer.toString();
  }
  public String getName() {
    return this.name;
  }

  public String getSrcDocId() {
    return srcDocId;
  }

  public String getTgtDocId() {
    return tgtDocId;
  }

  public String getSrcASName() {
    return srcASName;
  }

  public String getTgtASName() {
    return tgtASName;
  }

  public String getPuaAnnotType() {
    return puaAnnotType;
  }

  public String getPuaFeatureName() {
    return puaFeatureName;
  }

  public String getUaFeatureName() {
    return uaFeatureName;
  }

  public String getUaAnnotType() {
    return uaAnnotType;
  }

  public String getAlignmentView() {
    return this.alignmentView;
  }

  public static void toXML(AlignmentTask task, String filePath) {
    Element root = new Element("AlignmentTask");
    Element e = new Element("Name");
    e.setText(task.getName());
    root.addContent(e);

    e = new Element("SrcDocId");
    e.setText(task.getSrcDocId());
    root.addContent(e);

    e = new Element("TgtDocId");
    e.setText(task.getTgtDocId());
    root.addContent(e);

    e = new Element("SrcASName");
    e.setText(task.getSrcASName());
    root.addContent(e);

    e = new Element("TgtASName");
    e.setText(task.getTgtASName());
    root.addContent(e);

    e = new Element("PuaAnnotType");
    e.setText(task.getPuaAnnotType());
    root.addContent(e);

    e = new Element("PuaFeatureName");
    e.setText(task.getPuaFeatureName());
    root.addContent(e);

    e = new Element("UaFeatureName");
    e.setText(task.getUaFeatureName());
    root.addContent(e);

    e = new Element("UaAnnotType");
    e.setText(task.getUaAnnotType());
    root.addContent(e);

    e = new Element("AlignmentView");
    e.setText(task.getAlignmentView());
    root.addContent(e);

    if(task.getActionsFilePath() != null) {
      e = new Element("ActionFilePath");
      e.setText(task.getActionsFilePath());
      root.addContent(e);
    }

    org.jdom.Document doc = new org.jdom.Document(root);

    try {
      BufferedWriter bw = new BufferedWriter(new FileWriter(filePath));
      XMLOutputter serializer = new XMLOutputter();
      serializer.output(doc, bw);
      bw.close();
    }
    catch(IOException e1) {
      throw new GateRuntimeException(e1);
    }
  }// toXML

  public static AlignmentTask fromXML(CompoundDocument document, String filePath) {
    SAXBuilder parser = new SAXBuilder();

    try {
      org.jdom.Document doc = parser.build(new File(filePath));

      String name = doc.getRootElement().getChildText("Name");
      String tgtDocId = doc.getRootElement().getChildText("TgtDocId");
      String srcDocId = doc.getRootElement().getChildText("SrcDocId");
      String srcASName = doc.getRootElement().getChildText("SrcASName");
      String tgtASName = doc.getRootElement().getChildText("TgtASName");
      String puaAnnotType = doc.getRootElement().getChildText("PuaAnnotType");
      String puaFeatureName = doc.getRootElement().getChildText(
              "PuaFeatureName");
      String uaFeatureName = doc.getRootElement().getChildText("UaFeatureName");

      String uaAnnotType = doc.getRootElement().getChildText("UaAnnotType");
      String alignmentView = doc.getRootElement().getChildText("AlignmentView");
      String actionsFile = doc.getRootElement().getChildText("ActionFilePath");
      AlignmentTask at = new AlignmentTask(document);
      at.initialize(name, srcDocId, tgtDocId, srcASName, tgtASName,
              uaAnnotType, puaAnnotType, puaFeatureName, uaFeatureName,
              alignmentView, actionsFile);
      return at;
    }
    catch(JDOMException e) {
      throw new GateRuntimeException(e);
    }
    catch(IOException e) {
      throw new GateRuntimeException(e);
    }
  } // fromXML

  public CompoundDocument getCompoundDocument() {
    return compoundDocument;
  }

  /**
   * Returns the next possible pair
   * 
   * @return
   */
  public PUAPair next() {
    counter++;
    return current();
  }

  /**
   * Returns the previous pair
   * 
   * @return
   */
  public PUAPair previous() {
    counter--;
    return current();
  }

  /**
   * Returns the current pair
   * 
   * @return
   */
  public PUAPair current() {
    if(puaList == null || counter < 0 || counter >= puaList.size())
      return null;
    return puaList.get(counter);
  }

  /**
   * Returns true if there is any next pair available
   * 
   * @return
   */
  public boolean hasNext() {
    return counter + 1 >= 0 && counter + 1 < puaList.size();
  }

  /**
   * Returns true if there is any previous pair available
   * 
   * @return
   */
  public boolean hasPrevious() {
    return counter - 1 >= 0 && counter - 1 < puaList.size();
  }

  public Alignment getAlignment() {
    return alignment;
  }

  public Document getSrcDoc() {
    return srcDoc;
  }

  public Document getTgtDoc() {
    return tgtDoc;
  }

  public AnnotationSet getSrcAS() {
    return srcAS;
  }

  public AnnotationSet getTgtAS() {
    return tgtAS;
  }

  public String getActionsFilePath() {
    return actionsFilePath;
  }

  public AlignmentActionsManager getAlignmentActionsManager() {
    return alignmentActionsManager;
  }
}
