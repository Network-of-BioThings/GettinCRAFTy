/*
 *  Copyright (c) 2004, The University of Sheffield.
 *
 *  This file is part of GATE (see http://gate.ac.uk/), and is free
 *  software, licenced under the GNU Library General Public License,
 *  Version 2, June 1991 (in the distribution as file licence.html,
 *  and also available at http://gate.ac.uk/gate/licence.html).
 *
 *  Mike Dowman 08-04-2004
 *
 *  $Id: SVMLightDocument.java 7452 2006-06-15 14:45:17 +0000 (Thu, 15 Jun 2006) ian_roberts $
 *
 */

package gate.creole.ml.svmlight;

import gate.creole.ml.Attribute;
import java.lang.String;

/**
 * An array of these objects is what will be passed to the native methods from
 * GATE. It contains a single instance (a.k.a. document in SVM Light
 * terminology), and the instance is organised in a form that makes it easily
 * readable by the native methods.
 *
 * Native methods are no longer used, but the toString method of this class
 * will convert to SVM_light format so that the document can be saved to disk
 * in a form readable by svm_light.
 *
 * N.B. Weighting factors are now used, so that a new parameter
 * &lt;WEIGHTING&gt; can be specified in the configuration file for each
 * attribute. The attribute's value will be multiplied by this number before
 * being placed in the svm_light format document.
 */
class SVMLightDocument {

  static final boolean DEBUG=false;

  /**
   * The value of the &lt;CLASS/&gt; attribute, also known as the target.
   */
  double classValue;
  /**
   * The number of each feature.
   */
  int[] featureNumbers;
  /**
   * The value of each feature. These values correspond to those in
   * featureNumbers, so the nth value in this array corresponds to the nth in
   * the other.
   */
  double[] featureValues;

  /**
   * Creates a SVMLightDocument from a list of attributes.
   */
  SVMLightDocument(gate.creole.ml.DatasetDefintion datasetDefinition,
                   java.util.Map nominalValue2IntegerHash,
                   java.util.List attributes) throws gate.creole.
      ExecutionException {
    if (DEBUG)
      System.out.println("Starting to make document");

    // First set the class attribute.
    setClassAttribute(datasetDefinition, nominalValue2IntegerHash, attributes);

    // Now create the arrays containing all the other attributes.
    createFeatureValuePairs(datasetDefinition, nominalValue2IntegerHash,
                            attributes);

    if (DEBUG)
      System.out.println("Document made");
  }

  /**
   * Return a string representing the document in SVM Light format (i.e. as a
   * single line of an SVM Light data file).
   */
  public String toString() {
    // The string consists of the class value, followed by all the feature-value
    // pairs in the format feature number:feature value, all separated by
    // spaces, with a new line character on the end.
    StringBuffer svmFormatString = new StringBuffer(""+classValue+" ");

    for (int i=0; i<featureNumbers.length; ++i) {
      svmFormatString.append(""+featureNumbers[i]+":"+featureValues[i]+" ");
    }

    // Using the + operator, not append, makes the string buffer get converted
    // to a String.
    return svmFormatString+"\n";
  }

  /**
   * Convert the attributes, other than the class attribute, into arrays, one
   * storing feature numbers, and another feature values. Store these arrays
   * within the object's members.
   *
   * @param datasetDefinition An object specifying the dataset, which is derived
   * from the configuration file.
   * @param attributes All the attributes in list form, as received from the
   * gate Machine learning processing resource.
   */
  private void createFeatureValuePairs(
      gate.creole.ml.DatasetDefintion datasetDefinition,
      java.util.Map nominalValue2IntegerHash,
      java.util.List attributes) throws gate.creole.ExecutionException {
    java.util.List featureNumbersList = new java.util.ArrayList();
    java.util.List featureValuesList = new java.util.ArrayList();
    // SVM wants the attributes (a.k.a. features) numbering from 1.
    int svmLightFeatureNumber = 1;
    // In datasetDefinition, the attributes are numbered from 0.
    int attributesIndex = 0;
    while (attributesIndex < attributes.size()) {
      // Skip the class attribute.
      if (attributesIndex != datasetDefinition.getClassIndex()) {
        // Nominal attributes are treated differently to boolean and numeric
        // ones, becasue each nominal attribute can map to many svm features.
        // This is because we have one feature per possible value, and this is
        // one if the feature is present, else zero.
        Attribute currentAttribute =
            (Attribute) datasetDefinition.getAttributes().get(attributesIndex);
        if (currentAttribute.semanticType() == Attribute.NOMINAL) {
          // Find out the index of the attribute, in terms of which of the
          // possible values it has taken.
          int nominalNumber =
              nominalValue2Integer(datasetDefinition, nominalValue2IntegerHash,
                                   attributesIndex,
                                   (String) attributes.get(attributesIndex));

          // The SVM light features will be numbered, with one for each of the
          // possible values of the nominal. But as we only actually add those
          // which are non-zero, we can just skip over those before the one
          // corresponding to the actual value of the attribute.
          svmLightFeatureNumber+=nominalNumber-1;

          // So long as the attribute has been recognised, add it. Don't do
          // anything for unrecognised or missing attributes.
          if (nominalNumber<=((Attribute)datasetDefinition.getAttributes()
               .get(attributesIndex)).getValues().size()) {
            featureNumbersList.add(new java.lang.Integer(svmLightFeatureNumber));
            // The feature value will always be 1, to indicate that the nominal
            // has the value corresponding to this feature, except that this
            // value will be weighted if a weighting is specified in the
            // configuration file.
            featureValuesList.add(
                new java.lang.Double(1.0 * currentAttribute.getWeighting()));
            // Now move on svmLightFeatureNumber so that it corresponds to the
            // first feature after the end of those representing this nominal.
          }
          svmLightFeatureNumber+=
              ((Attribute)datasetDefinition.getAttributes()
               .get(attributesIndex)).getValues().size()-nominalNumber+1;
        }
        else { // The following code is for boolean and numeric attributes.
          // Only add attributes if their value is not zero.
          double attributeValue = string2AttributeValue(datasetDefinition,
              nominalValue2IntegerHash,
              (String) attributes.get(attributesIndex), attributesIndex);
          if (attributeValue != 0.0) {
            featureNumbersList.add(new java.lang.Integer(svmLightFeatureNumber));
            featureValuesList.add(new java.lang.Double(attributeValue));
          }
          ++svmLightFeatureNumber;
        }
      }
      ++attributesIndex;
    }

    // N.B. This is not really efficient - it would be better to find some
    // library class that supports lists for primitive types, and allows them
    // to be converted to arrays efficiently.
    featureNumbers = intList2Array(featureNumbersList);
    featureValues = doubleList2Array(featureValuesList);
  }

  /**
   * Take a list containing Integer classes, and convert it to an array of
   * integers.
   */
  private int[] intList2Array(java.util.List integerList) {
    int[] integerArray = new int[integerList.size()];
    for (int index = 0; index < integerList.size(); ++index) {
      integerArray[index] = ( (Integer) integerList.get(index)).intValue();
    }
    return integerArray;
  }

  /**
   * Take a list containing Double classes, and convert it to an array of
   * doubles.
   */
  private double[] doubleList2Array(java.util.List doubleList) {
    double[] doubleArray = new double[doubleList.size()];
    for (int index = 0; index < doubleList.size(); ++index) {
      doubleArray[index] = ( (Double) doubleList.get(index)).doubleValue();
    }
    return doubleArray;
  }

  /**
   * Extract the class attribute from the attributes list, and set the member
   * variable appropriately.
   *
   * @param datasetDefinition An object describing the dataset as specified in
   * the configuration file.
   * @param attributes The list of all the attribute values, in the order in
   * which they appear in the configuratoin file.
   */
  private void setClassAttribute(
      gate.creole.ml.DatasetDefintion datasetDefinition,
      java.util.Map nominalValue2IntegerHash,
      java.util.List attributes) throws gate.creole.ExecutionException {
    Attribute classAttribute = (Attribute) datasetDefinition.getClassAttribute();
    String stringClassValue =
        (String) attributes.get(datasetDefinition.getClassIndex());

    // It's OK if their is no class value, so long as we are doing
    // classification not training.
    if (stringClassValue==null)
      classValue = 0;
    else
      classValue = string2AttributeValue(datasetDefinition,
                                         nominalValue2IntegerHash,
                                         stringClassValue,
                                         datasetDefinition.getClassIndex());
  }

  /**
   * Take an attribute value string, in the form that it's received from gate,
   * and convert it into the form required by svm light. Note that nominal
   * attributes are treated differently if they are class attributes, as opposed
   * to normal ones.
   *
   * Also each attribute value will always be multiplied by its weighting, as
   * specified in the configuration file.
   */
  private double string2AttributeValue(
      gate.creole.ml.DatasetDefintion datasetDefinition,
      java.util.Map nominalValue2IntegerHash,
      String stringClassValue, int attributeIndex) throws gate.creole.
      ExecutionException {
    Attribute attributeObject =
        (Attribute) datasetDefinition.getAttributes().get(attributeIndex);

    if (attributeObject.semanticType() == Attribute.BOOLEAN) {
      if (stringClassValue.equals("true")) {
        return 1.0 * attributeObject.getWeighting();
      }
      else {
        return -1.0 * attributeObject.getWeighting();
      }
    }
    else if (attributeObject.semanticType() == Attribute.NOMINAL) {
      int attributeValueNumber = nominalValue2Integer(datasetDefinition,
          nominalValue2IntegerHash,
          datasetDefinition.getClassIndex(), stringClassValue);
      // N.B. If a nomianl attribute is the class attribute, it is treated
      // differently to other nominal attributes. It must be set to -1, +1 or
      // 0. (0 indicates that transduction is to be used.)
      if (attributeObject.isClass()) {
        if (attributeValueNumber == 1.0) {
          return 1.0 * attributeObject.getWeighting(); // Indicate positive example.
        }
        else if (attributeValueNumber == 2.0) {
          return -1.0 * attributeObject.getWeighting(); // Indicate negative example.
        }
        else {
          return 0.0; // Indicated unclassified example - or possibly an example
                      // for which the class attribute is missing.
        }
      }
      else {
        // We no longer use this code to map nominal attributes other than the
        // class attribute, so if we get here it is an error in the code.
      }
    }

    // In this case the attributes must be NUMERIC
    try {
      return Double.parseDouble(stringClassValue) * attributeObject.getWeighting();
    } catch (Exception ex) {
      // If a numeric value is missing, or is not a valid number just give it
      // a zero value.
      return 0;
    }
  }

  /**
   * Change a nominal feature value into an integer. Features are numbered from
   * one in the order in which they are declared in the configuration file.
   *
   * This code uses the nominalValue2IntegerHashMap, that is created when the
   * wrapper is initialised, so quickly map from an attribute number and feature
   * value in the format passed from gate into an integer.
   *
   * @param datasetDefintition
   * @param indexOfAttribute
   * @param value
   * @return
   */
  private int nominalValue2Integer(
      gate.creole.ml.DatasetDefintion datasetDefinition,
      java.util.Map nominalValue2IntegerHash,
      int indexOfAttribute, String value)
      throws gate.creole.ExecutionException {

    if (nominalValue2IntegerHash.containsKey(""+indexOfAttribute+":"+value)) {
      return ((Integer)nominalValue2IntegerHash.
              get(""+indexOfAttribute+":"+value)).intValue();
    } else
      // If we get here, then we will have an unrecognised value, or there will
      // be no value (possibly because we are looking before the beginning or
      // after the end of the document. We mark such cases with a distinct
      // value, with a value one greater than that of the last real value.
      return ((Attribute)datasetDefinition.getAttributes()
              .get(indexOfAttribute)).getValues().size()+1;
  }

}
