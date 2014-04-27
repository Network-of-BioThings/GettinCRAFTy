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
package gate.uima.mapping;

import org.apache.uima.cas.TypeSystem;
import org.apache.uima.cas.Type;
import org.apache.uima.cas.Feature;
import org.apache.uima.cas.CAS;
import gate.Document;
import gate.Annotation;
import gate.AnnotationSet;
import org.apache.uima.cas.FeatureStructure;
import org.apache.uima.cas.CASRuntimeException;
import org.jdom.Element;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * An ObjectBuilder that creates a UIMA FeatureStructure.
 */
public class UIMAFeatureStructureBuilder implements ObjectBuilder {
  /**
   * The UIMA Type of the feature structure to generate.
   */
  protected Type fsType;

  /**
   * Feature definitions for this feature structure.
   */
  protected List featureDefs;
  
  /**
   * Configure this ObjectBuilder by extracting the FeatureStructure type and
   * feature definitions from the XML element.
   */
  public void configure(Element elt, TypeSystem typeSystem)
        throws MappingException {
    String fsTypeName = elt.getAttributeValue("type");
    if(fsTypeName == null) {
      throw new MappingException("No \"type\" attribute specified for "
          + "featureStructure");
    }

    fsType = typeSystem.getType(fsTypeName);
    if(fsType == null) {
      throw new MappingException("Type " + fsTypeName
          + " not found in UIMA type system");
    }

    // build the list of feature definitions for this FS
    List featureElements = elt.getChildren("feature");
    featureDefs = new ArrayList(featureElements.size());

    Iterator featureElementsIt = featureElements.iterator();
    while(featureElementsIt.hasNext()) {
      Element featureElt = (Element)featureElementsIt.next();
      String featureName = featureElt.getAttributeValue("name");
      if(featureName == null) {
        throw new MappingException("feature element must have \"name\" "
            + "attribute specified");
      }

      Feature featureObject = fsType.getFeatureByBaseName(featureName);
      if(featureObject == null) {
        throw new MappingException("feature named \"" + featureName + "\" in "
            + "type \"" + fsTypeName + "\" does not exist in type system");
      }

      String featureKind = featureElt.getAttributeValue("kind");
      if(featureKind == null) {
        throw new MappingException("feature element must have \"kind\" "
            + "attribute specified");
      }

      List children = featureElt.getChildren();
      if(children.isEmpty()) {
        throw new MappingException("feature element must have a child element "
            + "specifying its value");
      }
      Element valueElement = (Element)children.get(0);

      // create the object builder that gives this feature's value
      ObjectBuilder valueBuilder = ObjectManager.createBuilder(valueElement,
                                                               typeSystem);

      featureDefs.add(new FeatureDefinition(featureName, featureKind,
                                            valueBuilder, featureObject));
    }
  }

  /**
   * Constructs and returns the FeatureStructure.
   */
  public Object buildObject(CAS cas, Document doc, AnnotationSet annSet,
      Annotation currentAnn, FeatureStructure currentFS)
          throws MappingException {
    FeatureStructure newFS = createFS(cas, doc, currentAnn, fsType);

    populateFeatures(newFS, cas, doc, annSet, currentAnn, currentFS);

    return newFS;
  }

  /**
   * Create the feature structure object.  This method may be overridden by
   * subclasses that create more specific types of FS (e.g. annotations).
   */
  protected FeatureStructure createFS(CAS cas, Document doc,
      Annotation currentAnn, Type fsType) {
    return cas.createFS(fsType);
  }


  /**
   * Uses the set of feature definitions to populate the features of the given
   * feature structure.
   */
  public void populateFeatures(FeatureStructure newFS, CAS cas,
            Document doc, AnnotationSet annSet, Annotation currentAnn,
            FeatureStructure currentFS) throws MappingException {
    Iterator featuresIt = featureDefs.iterator();
    while(featuresIt.hasNext()) {
      FeatureDefinition def = (FeatureDefinition)featuresIt.next();
      
      // build the feature value
      ObjectBuilder valueBuilder = def.getValueBuilder();
      Object featureValue = valueBuilder.buildObject(cas, doc, annSet,
                                               currentAnn, currentFS);

      // based on the feature kind, add it to the FS appropriately
      switch(def.getFeatureKind()) {
        // String kind - call toString on the value object and use that
        case FeatureDefinition.STRING_KIND:
          newFS.setStringValue(def.getCASFeatureObject(),
                               String.valueOf(featureValue));
          break;

        // integer kind - if the value object is a Number, call intValue,
        // otherwise try and parse the toString as an integer.  If this fails,
        // give up.
        case FeatureDefinition.INT_KIND:
          if(featureValue instanceof Number) {
            newFS.setIntValue(def.getCASFeatureObject(),
                              ((Number)featureValue).intValue());
          }
          else {
            try {
              newFS.setIntValue(def.getCASFeatureObject(),
                                Integer.parseInt(String.valueOf(featureValue)));
            }
            catch(NumberFormatException nfe) {
              throw new MappingException("Couldn't convert feature value \""
                  + featureValue + "\" to integer", nfe);
            }
          }
          break;

        // float kind - if the value object is a Number, call floatValue,
        // otherwise try and parse the toString as a float.  If this fails,
        // give up.
        case FeatureDefinition.FLOAT_KIND:
          if(featureValue instanceof Number) {
            newFS.setFloatValue(def.getCASFeatureObject(),
                              ((Number)featureValue).floatValue());
          }
          else {
            try {
              newFS.setFloatValue(def.getCASFeatureObject(),
                                Float.parseFloat(String.valueOf(featureValue)));
            }
            catch(NumberFormatException nfe) {
              throw new MappingException("Couldn't convert feature value \""
                  + featureValue + "\" to float", nfe);
            }
          }
          break;

        // feature structure kind - cast the value to a feature structure and
        // hope...
        case FeatureDefinition.FS_KIND:
          if(featureValue instanceof FeatureStructure) {
            newFS.setFeatureValue(def.getCASFeatureObject(),
                                  (FeatureStructure)featureValue);
          }
          else {
            throw new MappingException("Value for feature \"" 
                + def.getCASFeatureObject().getName() + "\" should be "
                + "a FeatureStructure");
          }
          break;

        default:
          throw new MappingException(
              "Unrecognised kind for feature definition");
      }
    }
  }
}
