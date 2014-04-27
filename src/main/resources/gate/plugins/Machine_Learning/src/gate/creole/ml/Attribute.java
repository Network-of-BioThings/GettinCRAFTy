/*
 *  Copyright (c) 1995-2012, The University of Sheffield. See the file
 *  COPYRIGHT.txt in the software or at http://gate.ac.uk/gate/COPYRIGHT.txt
 *
 *  This file is part of GATE (see http://gate.ac.uk/), and is free
 *  software, licenced under the GNU Library General Public License,
 *  Version 2, June 1991 (in the distribution as file licence.html,
 *  and also available at http://gate.ac.uk/gate/licence.html).
 *
 *  Valentin Tablan 19/11/2002
 *  semantic type added by Mike Dowman 31-03-2004
 *  Weightings added by Mike Dowman 24-5-2004
 *
 *  $Id: Attribute.java 6974 2005-10-18 11:52:16 +0000 (Tue, 18 Oct 2005) nirajaswani $
 *
 */
package gate.creole.ml;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

import org.jdom.Element;

import gate.util.GateException;

/**
 * Describes an attribute associated to a ML instance.
 */

public class Attribute implements Serializable{

  public Attribute(Element jdomElement) throws GateException {
    //find the name
    Element anElement = jdomElement.getChild("NAME");
    if(anElement == null) throw new GateException(
      "Required element \"NAME\" not present in attribute:\n" +
      jdomElement.toString() + "!");
    else name = anElement.getTextTrim();

    //find the type
    anElement = jdomElement.getChild("TYPE");
    if(anElement == null) throw new GateException(
      "Required element \"TYPE\" not present in attribute:\n" +
      jdomElement.toString() + "!");
    else type = anElement.getTextTrim();

    //find the feature if present
    anElement = jdomElement.getChild("FEATURE");
    if(anElement != null)feature = anElement.getTextTrim();

    //find the position if present
    anElement = jdomElement.getChild("POSITION");
    if(anElement == null) position = 0;
    else position = Integer.parseInt(anElement.getTextTrim());

    // find the weighting if present
    anElement = jdomElement.getChild("WEIGHTING");
    if (anElement == null) weighting = 1.0;
    else weighting = Double.parseDouble(anElement.getTextTrim());

    //find the class if present
    isClass = jdomElement.getChild("CLASS") != null;

    //find the allowed values if present
    anElement = jdomElement.getChild("VALUES");
    if(anElement == null) values = null;
    else{
      values = new ArrayList();
      Iterator valuesIter = anElement.getChildren("VALUE").iterator();
      while(valuesIter.hasNext()){
        values.add(((Element)valuesIter.next()).getTextTrim());
      }
    }
  }

  public Attribute(){
    name = null;
    type =null;
    feature = null;
    isClass = false;
    position = 0;
    values = null;
    weighting = 1.0;
  }

  public String toString(){
    StringBuffer res = new StringBuffer();
    res.append("Name: " + name + "\n");
    res.append("Type: " + type + "\n");
    res.append("Feature: " + feature + "\n");
    res.append("Weighting: "+ weighting + "\n");
    Iterator valIter = values.iterator();
    while(valIter.hasNext()){
      res.append("  Value:" + valIter.next().toString() + "\n");
    }
    return res.toString();
  }

  /**  
   * This method is a clone of gate.creole.mi.Attribute.parseSerie method with minor 
   * changes to make it compatible with ML API. It basically given an attribute element
   * first locates all required variable and creates multiple attributes for the given RANGE.
   */
  public static java.util.List parseSeries(Element jdomElement) throws GateException {
      //find the name
      Element anElement = jdomElement.getChild("NAME");
      if(anElement == null) throw new GateException(
        "Required element \"NAME\" not present in attribute:\n" +
        jdomElement.toString() + "!");
      
      String name = anElement.getTextTrim();

	  //find the type
      anElement = jdomElement.getChild("TYPE");
      if(anElement == null) throw new GateException(
        "Required element \"TYPE\" not present in attribute:\n" +
        jdomElement.toString() + "!");
      
      String type = anElement.getTextTrim();

	  String feature = null;
      
      //find the feature if present
      anElement = jdomElement.getChild("FEATURE");
      if(anElement != null)feature = anElement.getTextTrim();

      int minpos = 0;
      int maxpos = 0;
      
      //find the range of this element (e.g. from - to)
      anElement = jdomElement.getChild("RANGE");
      try {
      minpos = Integer.parseInt(anElement.getAttributeValue("from").trim());
      maxpos = Integer.parseInt(anElement.getAttributeValue("to").trim());
      } catch (Exception e){
        throw new GateException(
                "Range element is uncorrect:\n" +
                jdomElement.toString() + "!");
      }
      
      double weighting = 1.0;
      
      // find the weighting if present
      anElement = jdomElement.getChild("WEIGHTING");
      if (anElement != null) weighting = Double.parseDouble(anElement.getTextTrim());

      //find the class if present
      boolean isClass = jdomElement.getChild("CLASS") != null;
      if (isClass){
        throw new GateException(
                "Cannot define the class in a serie:\n" +
                jdomElement.toString() + "!");
      }

	
	  java.util.List values = null;
      //find the allowed values if present
	  anElement = jdomElement.getChild("VALUES");
	  if(anElement == null) values = null;
	  else{
	    values = new ArrayList();
	    Iterator valuesIter = anElement.getChildren("VALUE").iterator();
	    while(valuesIter.hasNext()){
	      values.add(((Element)valuesIter.next()).getTextTrim());
	    }
	  }
	  
      // Create a list of Attributes
      ArrayList attributes = new ArrayList();  
      for (int position =minpos; position<maxpos+1;position++ ){
        Attribute attribute = new Attribute();
        attribute.setClass(false);
        attribute.setFeature(feature);
        attribute.setName(name+"_"+position);
        attribute.setPosition(position);
        attribute.setType(type);
        attribute.setWeighting(weighting);
		attribute.setValues(values);
        attributes.add(attribute);
      }
      return attributes;
  }
  
  
  public boolean isClass(){
    return isClass;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getType() {
    return type;
  }

  public void setFeature(String feature) {
    this.feature = feature;
  }

  public String getFeature() {
    return feature;
  }

  public void setWeighting(double weighting) {
    this.weighting = weighting;
  }

  public double getWeighting() {
    return weighting;
  }

  public java.util.List getValues() {
    return values;
  }

  public int getPosition() {
    return position;
  }

  public void setClass(boolean isClass) {
    this.isClass = isClass;
  }

  public void setValues(java.util.List values) {
    this.values = values;
  }

  public void setPosition(int position) {
    this.position = position;
  }

  /**
   * This method reports whether the attribute is nominal, numeric or boolean.
   *
   * @return Attribute.NOMINAL, Attribute.NUMERIC or Attribute.BOOLEAN
   */
  public int semanticType() {
    // Only nominal attributes specify values, and only numeric and nominal
    // attributes specify feature, so this code is sufficient to distinguish
    // the three kinds of attribute.
    if (feature==null)
      return BOOLEAN;
    if (values==null)
      return NUMERIC;
    return NOMINAL;
  }

  // These constants are used only for returning values from semanticType
  public static final int NOMINAL=1;
  public static final int NUMERIC=2;
  public static final int BOOLEAN=3;

  boolean isClass = false;
  private String name;
  private String type;
  private String feature;
  private java.util.List values;
  private int position;
  // The SVMLightWrapper allows weighting for attributes to be specified in
  // the configuration file, and those weightings are stored in this member.
  // Weightings are (at time of writing) ignored by the Weka and Maxent
  // wrappers.
  private double weighting;
}