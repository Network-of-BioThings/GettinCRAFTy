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

import org.apache.uima.cas.Feature;

/**
 * Represents a single feature definition (GATE or UIMA) in a mapping.
 */
public class FeatureDefinition {
  /**
   * The name of the feature to generate.
   */
  private String featureName;

  /**
   * The CAS feature object that represents this feature.  This will be null
   * for GATE features.
   */
  private Feature casFeatureObject = null;

  /**
   * The kind of the feature.  This should take one of the *_KIND constant
   * values, and is only applicable for UIMA feature definitions - GATE
   * definitions can be arbitrary Java Objects.
   */
  private int featureKind = STRING_KIND;

  /**
   * An ObjectBuilder that can build the feature's value.
   */
  private ObjectBuilder valueBuilder;

  public static final int STRING_KIND = 0;
  public static final int INT_KIND = 1;
  public static final int FLOAT_KIND = 2;
  public static final int FS_KIND = 3;

  public FeatureDefinition(String featureName, ObjectBuilder valueBuilder) {
    this(featureName, STRING_KIND, valueBuilder, null);
  }

  public FeatureDefinition(String featureName, String featureKindString,
                      ObjectBuilder valueBuilder, Feature featureObject)
                            throws MappingException {
    this(featureName, getKindValue(featureKindString),
        valueBuilder, featureObject);
  }

  public FeatureDefinition(String featureName, int featureKind,
                           ObjectBuilder valueBuilder, Feature featureObject) {
    this.featureName = featureName;
    this.valueBuilder = valueBuilder;
    this.featureKind = featureKind;
    this.casFeatureObject = featureObject;
  }

  public String getFeatureName() { return featureName; }

  public Feature getCASFeatureObject() { return casFeatureObject; }

  public int getFeatureKind() { return featureKind; }

  public ObjectBuilder getValueBuilder() { return valueBuilder; }

  /**
   * Utility method to convert a string representation of a feature kind to the
   * corresponding constant.
   *
   * @throws MappingException if the string does not represent a valid feature
   * kind.
   */
  public static int getKindValue(String kindString) throws MappingException {
    if("string".equals(kindString)) {
      return STRING_KIND;
    }
    else if("int".equals(kindString)) {
      return INT_KIND;
    }
    else if("float".equals(kindString)) {
      return FLOAT_KIND;
    }
    else if("fs".equals(kindString)) {
      return FS_KIND;
    }
    else {
      throw new MappingException("Unrecognised feature kind \""
          + kindString + "\"");
    }
  }
}
