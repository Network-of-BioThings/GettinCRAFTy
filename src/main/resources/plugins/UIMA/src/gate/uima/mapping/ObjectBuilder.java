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
 * Basic interface for an object that creates other objects based on
 * information from an XML configuration file.
 */
public interface ObjectBuilder {
  /**
   * Configure this ObjectBuilder from the given XML element.  The UIMA type
   * system is also provided to allow the builder to acquire references to any
   * UIMA Type and Feature objects it will need later.
   */
  public void configure(Element elt, TypeSystem typeSystem)
    throws MappingException;

  /**
   * Build an object.  The current UIMA and GATE document/annotation set and
   * annotation objects are provided, as the precise objects to be created may
   * depend on the current context.
   */
  public Object buildObject(CAS cas, Document doc, AnnotationSet annSet,
    Annotation currentAnn, FeatureStructure currentFS) throws MappingException;
}
