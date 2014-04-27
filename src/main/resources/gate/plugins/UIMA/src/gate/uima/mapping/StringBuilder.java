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
import org.apache.uima.cas.CAS;
import gate.Document;
import gate.Annotation;
import gate.AnnotationSet;
import org.apache.uima.cas.FeatureStructure;
import org.jdom.Element;

/**
 * An ObjectBuilder that creates a fixed String given by the "value" attribute
 * of its configuration element.
 */
public class StringBuilder implements ObjectBuilder {
  /**
   * The String value to be returned by buildObject.
   */
  private String stringValue;
  
  /**
   * Configure this ObjectBuilder by extracting the string from the "value"
   * attribute.
   */
  public void configure(Element elt, TypeSystem typeSystem) {
    this.stringValue = elt.getAttributeValue("value");
  }

  /**
   * Returns the specified String value.
   */
  public Object buildObject(CAS cas, Document doc, AnnotationSet annSet,
      Annotation currentAnn, FeatureStructure currentFS) {
    return stringValue;
  }
}
