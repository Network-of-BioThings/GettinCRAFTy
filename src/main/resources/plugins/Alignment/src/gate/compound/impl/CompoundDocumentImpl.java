package gate.compound.impl;

import gate.Document;
import gate.Factory;
import gate.FeatureMap;
import gate.Gate;
import gate.Resource;
import gate.alignment.Alignment;
import gate.alignment.gui.AlignmentFactory;
import gate.compound.CompoundDocumentEvent;
import gate.compound.CompoundDocumentListener;
import gate.creole.ResourceInstantiationException;
import gate.util.Files;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * Implemention of the CompoundDocument. Compound Document is a set of
 * one or more documents. It provides a more convenient way to group
 * documents and interpret them as a single document. It has a
 * capability to switch the focus among the different memebers of it.
 * 
 * @author niraj
 */
public class CompoundDocumentImpl extends AbstractCompoundDocument {

  private static final long serialVersionUID = 8114328411647768889L;

  private transient ArrayList<CompoundDocumentListener> listeners;

  /**
   * Constructor
   */
  public CompoundDocumentImpl() {
    listeners = new ArrayList<CompoundDocumentListener>();
    this.features = Factory.newFeatureMap();
    this.documents = new HashMap<String, Document>();
    this.documentIDs = new ArrayList<String>();
  }

  /** Initialise this resource, and return it. */
  public Resource init() throws ResourceInstantiationException {
    // set up the source URL and create the content

    if(markupAware == null) {
      throw new ResourceInstantiationException(
              "The markupAware cannot be null.");
    }

    if(collectRepositioningInfo == null) {
      throw new ResourceInstantiationException(
              "The collectRepositioningInfo is null!");
    }

    if(preserveOriginalContent == null) {
      throw new ResourceInstantiationException(
              "The preserveOriginalContent is null!");
    }

    if(sourceUrl != null) {

      if(documentIDs == null || documentIDs.isEmpty()) {
        throw new ResourceInstantiationException(
                "You must provide atleast one document id");
      }

      // source URL can be a file
      File file = Files.fileFromURL(sourceUrl);
      if(file.isDirectory()) {
        throw new ResourceInstantiationException(
                "You must select one of the files!");
      }

      // instancetiate all documents
      createDocuments(file);
    }
    else {
      documents = new HashMap<String, Document>();
      documentIDs = new ArrayList<String>();
    }

    currentDocument = null;
    return this;
  } // init()

  /**
   * Given a file name it should try to identify other language pairs
   * 
   * @param file
   * @throws ResourceInstantiationException
   */
  protected void createDocuments(File file)
          throws ResourceInstantiationException {
    // if it is a file
    // it should follow the name conventions
    // which is X.language.extension
    String name = file.getName();
    int index = name.lastIndexOf('.');
    String extension = "";
    if(index == -1) {
      // there is no extension
    }
    else {
      extension = name.substring(index, name.length());
      name = name.substring(0, index);
    }

    // so the name contains X.language
    index = name.lastIndexOf('.');
    if(index == -1) {
      // no id provided
      throw new ResourceInstantiationException(
              "You must select one of the file that is named as X.language.ext");
    }

    String documentID = name.substring(index + 1, name.length());
    if(documentID.trim().length() == 0) {
      throw new ResourceInstantiationException(
              "You must select one of the file that is named as X.language.ext");
    }
    else {
      name = name.substring(0, index);
    }

    // else search for the language in the documentIDs
    index = documentIDs.indexOf(documentID);
    if(index == -1) {
      throw new ResourceInstantiationException(
              "Document ID of the selected file does not exist in the provided documentIDs");
    }

    try {
      documents = new HashMap<String, Document>();
      for(int i = 0; i < documentIDs.size(); i++) {
        // apart from the index, we need to search for all other files
        // create file Name
        documentID = (String)documentIDs.get(i);
        String fileNameToSearch = file.getParentFile().getAbsolutePath() + "/"
                + name + "." + documentID + extension;
        File newFile = new File(fileNameToSearch);
        if(!newFile.exists()) {
          System.err.println("File " + fileNameToSearch + " does not exist!");
          documentIDs.remove(i);
          i--;
          continue;
        }
        FeatureMap features = Factory.newFeatureMap();
        features.put("collectRepositioningInfo", collectRepositioningInfo);
        features.put("encoding", encoding);
        features.put("markupAware", new Boolean(true));
        features.put("preserveOriginalContent", preserveOriginalContent);
        features.put("sourceUrl", newFile.toURI().toURL());
        FeatureMap subFeatures = Factory.newFeatureMap();
        Gate.setHiddenAttribute(subFeatures, true);
        Document doc = (Document)Factory.createResource(
                "gate.corpora.DocumentImpl", features, subFeatures);
        doc.setName(documentID);
        documents.put(documentID, doc);
      }

      if(documents.isEmpty())
        throw new ResourceInstantiationException("No documents found");

    }
    catch(Exception e) {
      throw new ResourceInstantiationException(e);
    }
  }

  /**
   * Adds a new document member to the compound document.
   */
  public void addDocument(String documentID, Document document) {
    if(document.getName() == null) {
      document.setName(documentID);
    }

    if(documents.containsKey(documentID)) {
      System.err.println("Document with name \"" + documentID
              + "\" already exists. Document has not been added");
      return;
    }
    documents.put(documentID, document);
    documentIDs.add(documentID);
    fireDocumentAdded(documentID);
  }

  /**
   * Removes the provided document member from the compound document.
   * Please note that the event is fired first and document is removed
   * after that.
   */
  public void removeDocument(String documentID) {
    if(documentIDs.contains(documentID)) {
      fireDocumentRemoved(documentID);
      documentIDs.remove(documentID);
      documents.remove(documentID);
    }
  }

  /**
   * Objects wishing to listen to document addition and removal events
   * should implement the CompoundDocumentListener interface register
   * themselves to the compound document.
   */
  public void addCompoundDocumentListener(CompoundDocumentListener listener) {
    if(this.listeners == null)
      this.listeners = new ArrayList<CompoundDocumentListener>();
    this.listeners.add(listener);
  }

  /**
   * Objects no longer wishing listen to document addition and removal
   * events should call this method to get unregistered.
   */
  public void removeCompoundDocumentListener(CompoundDocumentListener listener) {
    this.listeners.remove(listener);
  }

  /**
   * Fires the new document addition event.
   * 
   * @param documentID
   */
  public void fireDocumentAdded(String documentID) {
    CompoundDocumentEvent cde = new CompoundDocumentEvent(this, documentID);
    for(int i = 0; i < listeners.size(); i++) {
      CompoundDocumentListener cdl = (CompoundDocumentListener)listeners.get(i);
      cdl.documentAdded(cde);
    }
  }

  /**
   * Fires the document removal event.
   * 
   * @param documentID
   */
  public void fireDocumentRemoved(String documentID) {
    CompoundDocumentEvent cde = new CompoundDocumentEvent(this, documentID);
    for(int i = 0; i < listeners.size(); i++) {
      CompoundDocumentListener cdl = (CompoundDocumentListener)listeners.get(i);
      cdl.documentRemoved(cde);
    }
  }

  /**
   * The alignment object contains information about the alignment of
   * text.
   * 
   * @param alignmentFeatureName
   * @return
   */
  public Alignment getAlignmentInformation(String alignmentFeatureName) {
    if(alignmentFeatureName == null) {
      alignmentFeatureName = AlignmentFactory.ALIGNMENT_FEATURE_NAME;
    }
    if(this.features.get(alignmentFeatureName) == null) {
      this.features.put(alignmentFeatureName, new Alignment(this));
    }

    return (Alignment)this.features.get(alignmentFeatureName);
  }

  /**
   * This method returns all available alignment feature names
   * 
   * @return
   */
  public Set<String> getAllAlignmentFeatureNames() {
    Set<String> toReturn = new HashSet<String>();
    if(this.features.keySet() != null) {
      for(Object key : this.features.keySet()) {
        if(this.features.get(key) instanceof Alignment) {
          toReturn.add(key.toString());
        }
      }
    }
    return toReturn;
  }

  /**
   * Alignment object with the provided feature name is deleted
   * 
   * @param alignmentFeatureName
   */
  public void removeAlignmentInformation(String alignmentFeatureName) {
    if(this.features.keySet() != null) {
      this.features.remove(alignmentFeatureName);
    }
  }

} // class CompoundDocumentImpl
