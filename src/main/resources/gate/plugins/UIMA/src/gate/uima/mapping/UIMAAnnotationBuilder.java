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
import org.apache.uima.cas.FeatureStructure;
import org.jdom.Element;

/**
 * Class to construct a UIMA Annotation based on a GATE one.
 */
public class UIMAAnnotationBuilder extends UIMAFeatureStructureBuilder {
  /**
   * The GATE annotation type for which we are to create the corresponding UIMA
   * annotations.
   */
  private String gateAnnotationType;

  public String getGateAnnotationType() { return gateAnnotationType; }

  /**
   * The annotation set name in which these GATE annotations are to be found.
   * </code>null</code> corresponds to the default set.
   */
  private String annotationSetName;

  public String getAnnotationSetName() { return annotationSetName; }

  /**
   * Should this builder index the generated annotations, so that changes to
   * their features can be propagated back into GATE?  Default is false.
   */
  private boolean indexed;

  public boolean isIndexed() { return indexed; }

  /**
   * Configure this builder.  Most of the configuration is handed off to the
   * FeatureStructure superclass, this method just extracts the attributes
   * specific to annotations, namely gateType and indexed.
   */
  public void configure(Element elt, TypeSystem typeSystem)
        throws MappingException {
    super.configure(elt, typeSystem);

    gateAnnotationType = elt.getAttributeValue("gateType");
    if(gateAnnotationType == null) {
      throw new MappingException("No \"gateType\" attribute specified for "
          + "annotation");
    }

    annotationSetName = elt.getAttributeValue("annotationSetName");
    // if this is null, we want the default set

    String indexedString = elt.getAttributeValue("indexed");
    indexed = Boolean.valueOf(indexedString).booleanValue();
  }

  protected FeatureStructure createFS(CAS cas, Document doc,
      Annotation currentAnn, Type fsType) {

    // UIMA offsets are int, not long, so possible overflow here - you have
    // been warned...
    int annotStart = currentAnn.getStartNode().getOffset().intValue();
    int annotEnd = currentAnn.getEndNode().getOffset().intValue();

    return cas.createAnnotation(fsType, annotStart, annotEnd);
  }
}
