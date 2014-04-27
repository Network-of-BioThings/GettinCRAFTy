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
import org.apache.uima.cas.Feature;
import org.apache.uima.cas.CASRuntimeException;
import org.apache.uima.cas.CAS;
import gate.Document;
import gate.Annotation;
import gate.AnnotationSet;
import org.apache.uima.cas.FeatureStructure;
import org.jdom.Element;

/**
 * An ObjectBuilder that creates an Object by extracting the value of a
 * particular feature from the current UIMA feature structure.
 */
public class UIMAFSFeatureValueBuilder implements ObjectBuilder {
  /**
   * The feature whose value is to be extracted.
   */
  private Feature feature;

  /**
   * The kind of feature that it is - this controls how the value is extracted.
   * The value is one of the FeatureDefinition.*_KIND constants.
   */
  private int kind;
  
  /**
   * Configure this ObjectBuilder by extracting the feature name and kind.  If
   * no such attributes exist, an exception is thrown.
   */
  public void configure(Element elt, TypeSystem typeSystem)
                      throws MappingException {
    String featureName = elt.getAttributeValue("name");
    if(featureName == null) {
      throw new MappingException("gateAnnotFeatureValue element must have "
          + "name attribute");
    }

    feature = typeSystem.getFeatureByFullName(featureName);
    if(feature == null) {
      throw new MappingException("Feature " + featureName + " not found in "
          + "type system");
    }
    // we don't know at this stage whether the feature object is appropriate to
    // the type of FeatureStructure that will be passed in at buildObject time.

    String kindString = elt.getAttributeValue("kind");
    if(kindString == null) {
      // assume string if not otherwise specified
      kindString = "string";
    }

    kind = FeatureDefinition.getKindValue(kindString);
    // throws MappingException if not a valid kind.
  }

  /**
   * Returns the value of the specified feature of the document.  If the
   * document has no feature by that name, <code>null</code> is returned.
   */
  public Object buildObject(CAS cas, Document doc, AnnotationSet annSet,
      Annotation currentAnn, FeatureStructure currentFS)
                     throws MappingException {
    Object returnValue = null;
    try {
      switch(kind) {
        case FeatureDefinition.STRING_KIND:
          returnValue = currentFS.getStringValue(feature);
          break;

        case FeatureDefinition.INT_KIND:
          returnValue = new Integer(currentFS.getIntValue(feature));
          break;

        case FeatureDefinition.FLOAT_KIND:
          returnValue = new Float(currentFS.getFloatValue(feature));
          break;

        case FeatureDefinition.FS_KIND:
          // it's your own fault if this doesn't do what you expected -
          // FeatureStructure objects will NOT be valid once the CAS has been
          // reset, so GATE code will have to extract the relevant data from
          // the FS downstream in the pipeline, before we move on to the next
          // document.  You have been warned...
          returnValue = currentFS.getFeatureValue(feature);
          break;

        default:
          throw new MappingException("Unrecognised feature kind - this "
              + "exception should have been trapped at configure time, "
              + "so something is seriously wrong.");
      }
    }
    catch(CASRuntimeException cre) {
      throw new MappingException("Feature " + feature.getName() + " not valid "
          + "for feature structure of type " + currentFS.getType().getName()
          + ", or not of the expected kind");
    }

    return returnValue;
  }
}
