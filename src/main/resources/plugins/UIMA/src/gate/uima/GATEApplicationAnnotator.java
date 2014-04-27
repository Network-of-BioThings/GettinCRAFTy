/*
 *  Copyright (c) 2005, The University of Sheffield.
 *
 *  This file is part of the GATE/UIMA integration layer, and is free
 *  software, released under the terms of the GNU Lesser General Public
 *  Licence, version 2.1 (or any later version).  A copy of this licence
 *  is provided in the file LICENCE in the distribution.
 *
 *  UIMA is a product of IBM, details are available from
 *  http://alphaworks.ibm.com/tech/uima
 */
package gate.uima;

import gate.uima.mapping.*;

import gate.*;
import gate.util.*;
import gate.util.persistence.*;
import gate.persist.PersistenceException;
import gate.creole.*;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URI;

import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import org.apache.uima.analysis_engine.annotator.*;
import org.apache.uima.analysis_engine.ResultSpecification;
import org.apache.uima.cas.TypeSystem;
import org.apache.uima.cas.Type;
import org.apache.uima.cas.FeatureStructure;
import org.apache.uima.cas.FSIterator;
import org.apache.uima.cas.FSIndexRepository;
import org.apache.uima.cas.CAS;

/**
 * UIMA annotator that encapsulates a GATE processing pipeline.
 */
public class GATEApplicationAnnotator extends Annotator_ImplBase
                                      implements TextAnnotator {
  private static final boolean DEBUG = false;

  /**
   * Version ID for CVS.
   */
  private static final String __CVSID = "$Id";

  /**
   * Message digest used for our exceptions.
   */
  public static final String MESSAGE_DIGEST =
    "gate.uima.GATEApplicationAnnotator_Messages";

  public static final String GATE_APP_RESOURCE_NAME = "GateApplication";
  public static final String MAPPING_DESCRIPTOR_RESOURCE_NAME =
                                                    "MappingDescriptor";

  public static final String CONFIG_DIR_PROPERTY = "uima.gate.configdir";
  public static final String SITE_CONFIG_PROPERTY = "uima.gate.siteconfig";
  public static final String USER_CONFIG_PROPERTY = "uima.gate.userconfig";

  private static synchronized void initGate()
                  throws AnnotatorInitializationException {
    if(!Gate.isInitialised()) {
      try {
        File gateConfigDir = null;
        String gateConfigDirPath = System.getProperty(CONFIG_DIR_PROPERTY);
        if(gateConfigDirPath == null) {
          try {
            // attempt to locate gate-config directory at the same level as
            // uima-gate.jar or the classes directory we are running from, if
            // appropriate.
            URL thisClass = GATEApplicationAnnotator.class.getResource(
                "GATEApplicationAnnotator.class");
            if("jar".equals(thisClass.getProtocol())) {
              // running from uima-gate.jar
              String thisClassStr = thisClass.getPath();
              URI jarFileURI =
                new URI(thisClassStr.substring(0, thisClassStr.indexOf('!')));
              File baseDir = new File(jarFileURI).getParentFile();
              gateConfigDir = new File(baseDir, "gate-config");
            }
            else if("file".equals(thisClass.getProtocol())) {
              // classes directory, so thisClass is
              // .../classes/gate/uima/GATEApplicationAnnotator.class
              URI classFileURI = new URI(thisClass.toExternalForm());
              File baseDir =
                new File(classFileURI) // .../classes/gate/uima/file.class
                .getParentFile()       // .../classes/gate/uima
                .getParentFile()       // .../classes/gate
                .getParentFile()       // .../classes
                .getParentFile();      // ...
              gateConfigDir = new File(baseDir, "gate-config");

            }
          }
          catch(Throwable t) {
            throw new AnnotatorInitializationException(MESSAGE_DIGEST,
                "gate_init_exception", new Object[0], t);
          }
        }
        else {
          gateConfigDir = new File(gateConfigDirPath);
        }

        if(!gateConfigDir.exists() || !gateConfigDir.isDirectory()) {
          throw new AnnotatorInitializationException(MESSAGE_DIGEST,
              "config_dir_not_found", new Object[0]);
        }
        
        String siteConfigPath = System.getProperty(SITE_CONFIG_PROPERTY);
        File siteConfigFile = null;
        if(siteConfigPath == null) {
          siteConfigFile = new File(gateConfigDir, "site-gate.xml");
        }
        else {
          siteConfigFile = new File(siteConfigPath);
        }

        String userConfigPath = System.getProperty(USER_CONFIG_PROPERTY);
        File userConfigFile = null;
        if(userConfigPath == null) {
          userConfigFile = new File(gateConfigDir, "user-gate.xml");
        }
        else {
          userConfigFile = new File(userConfigPath);
        }
        
        Gate.setGateHome(gateConfigDir);
        Gate.setPluginsHome(gateConfigDir);
        Gate.setSiteConfigFile(siteConfigFile);
        Gate.setUserConfigFile(userConfigFile);
        Gate.init();
      }
      catch(GateException gx) {
        throw new AnnotatorInitializationException(
            MESSAGE_DIGEST, "gate_init_exception", new Object[0], gx);
      }
    }
  }

  ///// Private variables /////
  
  private URL gateAppURL;
  private URL mappingDescriptorURL;

  private CorpusController gateApplication;
  private Corpus gateCorpus;

  private Map uimaGateIndex;
  
  /**
   * A Map taking annotation set names to Lists of ObjectBuilders defining the
   * input mappings of UIMA annotations to GATE annotations.  The map may
   * contain <code>null</code> as a key, denoting the mappings to the default
   * (unnamed) annotation set.
   */
  private Map inputMappings;

  /**
   * A list of ObjectBuilders defining the new annotations created by GATE that
   * should be mapped back into UIMA.
   */
  private List outputsAdded;

  /**
   * A list of ObjectBuilders defining the annotations whose features have been
   * updated by GATE, and for which changes should be propagated back into
   * UIMA.
   */
  private List outputsUpdated;

  /**
   * A list of ObjectBuilders giving the annotations that have been removed by
   * GATE and for which the corresponding annotations should be removed in
   * UIMA.
   */
  private List outputsRemoved;

  /**
   * Initialise this annotator, by extracting parameter values from the
   * context, and initialising GATE, if necessary.
   */
  public void initialize(AnnotatorContext aContext)
                 throws AnnotatorConfigurationException,
                        AnnotatorInitializationException {
    super.initialize(aContext);
    // make sure GATE is initialized
    initGate();

    try {
      gateAppURL = getContext().getResourceURL(GATE_APP_RESOURCE_NAME);
      mappingDescriptorURL =
        getContext().getResourceURL(MAPPING_DESCRIPTOR_RESOURCE_NAME);
    }
    catch(AnnotatorContextException ace) {
      throw new AnnotatorInitializationException(ace);
    }

    try {
      gateApplication = (CorpusController)
        PersistenceManager.loadObjectFromUrl(gateAppURL);
    }
    catch(PersistenceException px) {
      throw new AnnotatorInitializationException(MESSAGE_DIGEST,
          "error_loading_gate_app", new Object[] {gateAppURL}, px);
    }
    catch(ResourceInstantiationException rix) {
      throw new AnnotatorInitializationException(MESSAGE_DIGEST,
          "error_loading_gate_app", new Object[] {gateAppURL}, rix);
    }
    catch(IOException iox) {
      throw new AnnotatorInitializationException(MESSAGE_DIGEST,
          "error_loading_gate_app", new Object[] {gateAppURL}, iox);
    }

    try {
      gateCorpus = Factory.newCorpus("UIMA corpus");
    }
    catch(ResourceInstantiationException rix) {
      throw new AnnotatorInitializationException(MESSAGE_DIGEST,
          "error_creating_corpus", new Object[0], rix);
    }

    gateApplication.setCorpus(gateCorpus);
    uimaGateIndex = new HashMap();
  }

  /**
   * Free the GATE objects created by this annotator.
   */
  public void destroy() {
    if(gateCorpus != null) {
      Factory.deleteResource(gateCorpus);
      gateCorpus = null;
    }

    if(gateApplication != null) {
      Factory.deleteResource(gateApplication);
      gateApplication = null;
    }
  }
  
  /**
   * Initialize the annotator with a new type system.  This is where we parse
   * the mapping descriptor, as the processing of the descriptor depends on the
   * type system.
   */
  public void typeSystemInit(TypeSystem typeSystem)
                 throws AnnotatorConfigurationException,
                        AnnotatorInitializationException {
    super.typeSystemInit(typeSystem);

    // parse the mapping file somehow
    SAXBuilder builder = new SAXBuilder();
    builder.setErrorHandler(new ErrorHandler() {
      public void warning(SAXParseException ex) {
        // do nothing on warnings
      }

      // treat all errors as fatal
      public void error(SAXParseException ex) throws SAXException {
        throw ex;
      }

      public void fatalError(SAXParseException ex) throws SAXException {
        throw ex;
      }
    });

    org.jdom.Document configDoc = null;

    try {
      configDoc = builder.build(mappingDescriptorURL);
    }
    catch(JDOMException jde) {
      throw new AnnotatorInitializationException(MESSAGE_DIGEST,
          "mapping_descriptor_parse_error", new Object[0], jde);
    }
    catch(IOException ioe) {
      throw new AnnotatorInitializationException(MESSAGE_DIGEST,
          "mapping_descriptor_io_error", new Object[0], ioe);
    }

    processMappingDescriptor(configDoc, typeSystem);
  }

  ///// Processing mapping descriptor /////
  
  /**
   * Process the mapping descriptor to build the mapping between GATE and UIMA
   * annotation models.
   */
  private void processMappingDescriptor(org.jdom.Document doc,
                                        TypeSystem typeSystem)
               throws AnnotatorInitializationException {
    Element topElement = doc.getRootElement();
    // process input section
    Element inputsElement = topElement.getChild("inputs");
    inputMappings = new HashMap();

    if(inputsElement != null) {
      List inputElements = inputsElement.getChildren();
      Iterator inputMappingsIt = inputElements.iterator();
      while(inputMappingsIt.hasNext()) {
        Element mapping = (Element)inputMappingsIt.next();

        try {
          ObjectBuilder inputBuilder =
            ObjectManager.createBuilder(mapping, typeSystem);

          if(!(inputBuilder instanceof GateAnnotationBuilder)) {
            throw new AnnotatorInitializationException(MESSAGE_DIGEST,
                "input_must_be_gab", new Object[0]);
          }
          
          String annotationSetName =
            mapping.getAttributeValue("annotationSetName");
          // annotation set name may be null, this is not a problem
          addInputMapping(annotationSetName, inputBuilder);
        }
        catch(MappingException mx) {
          throw new AnnotatorInitializationException(MESSAGE_DIGEST,
              "error_creating_mapping", new String[] {"input"}, mx);
        }
      }
    }

    // process outputs
    outputsAdded = new ArrayList();
    outputsUpdated = new ArrayList();
    outputsRemoved = new ArrayList();

    Element outputsElement = topElement.getChild("outputs");
    if(outputsElement != null) {
      String[] elements = new String[] {"added", "updated", "removed"};
      List[] lists = new List[] {outputsAdded, outputsUpdated, outputsRemoved};
      for(int i = 0; i < elements.length; i++) {
        Element elt = outputsElement.getChild(elements[i]);
        if(elt != null) {
          List outputElements = elt.getChildren();
          Iterator outputMappingsIt = outputElements.iterator();
          while(outputMappingsIt.hasNext()) {
            Element mapping = (Element)outputMappingsIt.next();
            
            try {
              ObjectBuilder outputBuilder =
                ObjectManager.createBuilder(mapping, typeSystem);

              if(!(outputBuilder instanceof UIMAFeatureStructureBuilder)) {
                throw new AnnotatorInitializationException(MESSAGE_DIGEST,
                    "output_must_be_fsbuilder", new Object[0]);
              }
              
              lists[i].add(outputBuilder);
            }
            catch(MappingException mx) {
              throw new AnnotatorInitializationException(MESSAGE_DIGEST,
                    "error_creating_mapping", new String[] {"output"}, mx);
            }
          }
        }
      }
    }
  }

  /**
   * Add an input mapping (ObjectBuilder) to the mapping list for the given
   * annotation set name.
   */
  private void addInputMapping(String annSetName, ObjectBuilder builder) {
    List inputsForSet = (List)inputMappings.get(annSetName);
    if(inputsForSet == null) {
      inputsForSet = new ArrayList();
      inputMappings.put(annSetName, inputsForSet);
    }
    inputsForSet.add(builder);
  }

 
  public void process(CAS cas, ResultSpecification resultSpec)
                  throws AnnotatorProcessException {
    String docText = cas.getDocumentText();
    gate.Document gateDoc = null;
    try {
      // load the document text without unpacking any markup
      FeatureMap docParams = Factory.newFeatureMap();
      docParams.put(Document.DOCUMENT_STRING_CONTENT_PARAMETER_NAME, docText);
      docParams.put(
          Document.DOCUMENT_MARKUP_AWARE_PARAMETER_NAME, Boolean.FALSE);
      gateDoc = (Document)Factory.createResource("gate.corpora.DocumentImpl",
          docParams);
    }
    catch(ResourceInstantiationException rix) {
      throw new AnnotatorProcessException(MESSAGE_DIGEST,
          "error_creating_gate_doc", new Object[0], rix);
    }

    try {
      mapInputAnnotations(cas, gateDoc);

      gateCorpus.add(gateDoc);
      
      gateApplication.execute();

      mapOutputs(cas, gateDoc);
    }
    catch(ExecutionException ex) {
      throw new AnnotatorProcessException(MESSAGE_DIGEST,
          "error_executing_app", new Object[0], ex);
    }
    catch(MappingException mx) {
      throw new AnnotatorProcessException(MESSAGE_DIGEST,
          "error_mapping_annots", new Object[0], mx);
    }
    finally {
      // clear indexes and free GATE document
      uimaGateIndex.clear();
      gateCorpus.remove(gateDoc);
      Factory.deleteResource(gateDoc);
    }
  }

  private void mapInputAnnotations(CAS cas, gate.Document gateDoc)
                          throws MappingException, AnnotatorProcessException {
    // nothing to do if there are no input mappings
    if(inputMappings == null || inputMappings.isEmpty()) {
      return;
    }

    // input mappings is a map from annotation set name to list of mappings
    Iterator mappingSetsIt = inputMappings.entrySet().iterator();
    while(mappingSetsIt.hasNext()) {
      Map.Entry mappingSet = (Map.Entry)mappingSetsIt.next();

      // get the right annotation set for this set of mappings
      AnnotationSet annSet = null;
      if(mappingSet.getKey() == null) {
        annSet = gateDoc.getAnnotations();
      }
      else {
        annSet = gateDoc.getAnnotations((String)mappingSet.getKey());
      }

      List mappings = (List)mappingSet.getValue();
      Iterator mappingsIt = mappings.iterator();
      while(mappingsIt.hasNext()) {
        GateAnnotationBuilder gab = (GateAnnotationBuilder)mappingsIt.next();

        Type uimaType = gab.getUimaType();
        String gateType = gab.getGateType();
        FSIterator annotsToMap = cas.getAnnotationIndex(uimaType).iterator();
        while(annotsToMap.hasNext()) {
          FeatureStructure uimaAnnot = (FeatureStructure)annotsToMap.next();

          // create the annotation in the given annotation set
          Integer id = (Integer)
            gab.buildObject(cas, gateDoc, annSet, null, uimaAnnot);
          // add to index
          addToUimaGateIndex(uimaAnnot, (String)mappingSet.getKey(),
                             gateType, id);
        }
      }
    }
  }

  private void mapOutputs(CAS cas, gate.Document gateDoc)
                          throws MappingException, AnnotatorProcessException {
    FSIndexRepository uimaIndexes = cas.getIndexRepository();
    // added
    if(!outputsAdded.isEmpty()) {
      Iterator outputsAddedIt = outputsAdded.iterator();
      while(outputsAddedIt.hasNext()) {
        UIMAFeatureStructureBuilder fsBuilder =
          (UIMAFeatureStructureBuilder)outputsAddedIt.next();

        if(fsBuilder instanceof UIMAAnnotationBuilder) {
          // iterate over all the GATE annotations of the appropriate type in
          // the right set and add corresponding UIMA annotations to the CAS
          UIMAAnnotationBuilder annotBuilder =
            (UIMAAnnotationBuilder)fsBuilder;
          String annotationSetName = annotBuilder.getAnnotationSetName();
          String gateAnnotType = annotBuilder.getGateAnnotationType();
          AnnotationSet annSet = null;
          if(annotationSetName == null) {
            annSet = gateDoc.getAnnotations();
          }
          else {
            annSet = gateDoc.getAnnotations(annotationSetName);
          }

          AnnotationSet annotsOfType = annSet.get(gateAnnotType);
          if(annotsOfType != null) {
            Iterator annotsIt = annotsOfType.iterator();
            while(annotsIt.hasNext()) {
              gate.Annotation ann = (gate.Annotation)annotsIt.next();
              FeatureStructure uimaAnn = (FeatureStructure)
                annotBuilder.buildObject(cas, gateDoc, annSet, ann, null);
              uimaIndexes.addFS(uimaAnn);
            }
          }
        }
        else {
          // non-Annotation FS, so just build one object and add it
          FeatureStructure fs = (FeatureStructure)
            fsBuilder.buildObject(cas, gateDoc, null, null, null);
          uimaIndexes.addFS(fs);
        }
      }
    }
    
    // updated
    if(!outputsUpdated.isEmpty()) {
      Iterator outputsUpdatedIt = outputsUpdated.iterator();
      while(outputsUpdatedIt.hasNext()) {
        UIMAFeatureStructureBuilder fsBuilder =
          (UIMAFeatureStructureBuilder)outputsUpdatedIt.next();

        if(fsBuilder instanceof UIMAAnnotationBuilder) {
          // iterate over all the annotations of the right type in the right
          // set, find their corresponding UIMA annotation and update its
          // features.
          UIMAAnnotationBuilder annotBuilder =
            (UIMAAnnotationBuilder)fsBuilder;
          String annotationSetName = annotBuilder.getAnnotationSetName();
          String gateAnnotType = annotBuilder.getGateAnnotationType();
          AnnotationSet annSet = null;
          if(annotationSetName == null) {
            annSet = gateDoc.getAnnotations();
          }
          else {
            annSet = gateDoc.getAnnotations(annotationSetName);
          }

          AnnotationSet annotsOfType = annSet.get(gateAnnotType);
          if(annotsOfType != null) {
            Iterator annotsIt = annotsOfType.iterator();
            while(annotsIt.hasNext()) {
              gate.Annotation ann = (gate.Annotation)annotsIt.next();
              FeatureStructure uimaAnn = (FeatureStructure)
                getFSForGATEAnnot(annotationSetName, ann.getId());
              if(uimaAnn != null) {
                // remove from indexes during update, in case we change the
                // value of any features which are keys in an index.
                uimaIndexes.removeFS(uimaAnn);
                annotBuilder.populateFeatures(uimaAnn, cas, gateDoc,
                    annSet, ann, null);
                uimaIndexes.addFS(uimaAnn);
              }
              else {
                if(DEBUG) {
                  System.err.println("Tried to update a FS for annotation "
                      + ann + " but no FS found in index");
                }
              }
            }
          }
        }
        else {
          throw new AnnotatorProcessException(MESSAGE_DIGEST,
              "only_annotations_updated", new Object[0]);
        }
      }
    }

    // removed
    if(!outputsRemoved.isEmpty()) {
      Iterator outputsRemovedIt = outputsRemoved.iterator();
      while(outputsRemovedIt.hasNext()) {
        UIMAFeatureStructureBuilder fsBuilder =
          (UIMAFeatureStructureBuilder)outputsRemovedIt.next();

        if(fsBuilder instanceof UIMAAnnotationBuilder) {
          // iterate over all the annotations of the appropriate type in the
          // right annotation set and check which ones are in the index.  Any
          // index entries of the correct type that are left over (i.e. whose
          // annotation is not found in the annotation set) must have been
          // removed by GATE, so remove their corresponding entries in the CAS
          UIMAAnnotationBuilder annotBuilder =
            (UIMAAnnotationBuilder)fsBuilder;
          String annotationSetName = annotBuilder.getAnnotationSetName();
          String gateAnnotType = annotBuilder.getGateAnnotationType();
          AnnotationSet annSet = null;
          if(annotationSetName == null) {
            annSet = gateDoc.getAnnotations();
          }
          else {
            annSet = gateDoc.getAnnotations(annotationSetName);
          }

          Map indexForAnnSet = getIndexForAnnotationSet(annotationSetName);
          if(indexForAnnSet != null) {
            Iterator indexIt = indexForAnnSet.entrySet().iterator();
            while(indexIt.hasNext()) {
              Map.Entry indexEntry = (Map.Entry)indexIt.next();
              Integer id = (Integer)indexEntry.getKey();
              TypeAndFS tfs = (TypeAndFS)indexEntry.getValue();
              if(gateAnnotType.equals(tfs.type)) {
                Annotation ann = annSet.get(id);
                if(ann == null) {
                  // must have been deleted
                  uimaIndexes.removeFS(tfs.fs);
                }
              }
            }
          }
        }
        else {
          throw new AnnotatorProcessException(MESSAGE_DIGEST,
              "only_annotations_removed", new Object[0]);
        }
      }
    }
  }

  ///// GATE/UIMA annotation indexes /////

  private void addToUimaGateIndex(FeatureStructure uimaAnnot,
                                  String annotationSetName, String annType,
                                  Integer gateID) {
    Map mapForAS = (Map)uimaGateIndex.get(annotationSetName);
    if(mapForAS == null) {
      mapForAS = new HashMap();
      uimaGateIndex.put(annotationSetName, mapForAS);
    }
    mapForAS.put(gateID, new TypeAndFS(annType, uimaAnnot));
  }

  private FeatureStructure getFSForGATEAnnot(String asName, Integer id) {
    TypeAndFS tfs = getTypeAndFSForGATEAnnot(asName, id);
    return (tfs == null) ? null : tfs.fs;
  }

  private String getTypeForGATEAnnot(String asName, Integer id) {
    TypeAndFS tfs = getTypeAndFSForGATEAnnot(asName, id);
    return (tfs == null) ? null : tfs.type;
  }

  private TypeAndFS getTypeAndFSForGATEAnnot(String asName, Integer id) {
    Map mapForAS = (Map)uimaGateIndex.get(asName);
    if(mapForAS == null) {
      return null;
    }
    else {
      return (TypeAndFS)mapForAS.get(id);
    }
  }

  private Map getIndexForAnnotationSet(String asName) {
    return (Map)uimaGateIndex.get(asName);
  }

  /**
   * Ordered pair of an annotation type and a feature structure.
   */
  private static class TypeAndFS {
    String type;
    FeatureStructure fs;

    TypeAndFS(String type, FeatureStructure fs) {
      this.type = type;
      this.fs = fs;
    }
  }
}
