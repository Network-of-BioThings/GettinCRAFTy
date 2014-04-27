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
 *
 *  $Id: DatasetDefintion.java 6974 2005-10-18 11:52:16 +0000 (Tue, 18 Oct 2005) nirajaswani $
 *
 */
package gate.creole.ml;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jdom.Element;

import gate.util.GateException;

/**
 * Stores data describing a dataset.
 */

public class DatasetDefintion implements Serializable{

  public DatasetDefintion(Element domElement) throws GateException{
    if(!domElement.getName().equals("DATASET")) throw new GateException(
    "Dataset defintion element is \"" + domElement.getName() +
    "\" instead of \"DATASET\"!");

    //find instance the type
    Element anElement = domElement.getChild("INSTANCE-TYPE");
    if(anElement != null) instanceType = anElement.getTextTrim();
    else throw new GateException(
      "Required element \"INSTANCE-TYPE\" not present!");

    //find the attributes
    int attrIndex = 0;
    attributes = new ArrayList();
    Iterator childrenIter = domElement.getChildren("ATTRIBUTE").iterator();
    while(childrenIter.hasNext()){
      Element child = (Element)childrenIter.next();
      Attribute attribute = new Attribute(child);
      if(attribute.isClass()){
        if(classAttribute != null) throw new GateException(
          "Attribute \""+ attribute.getName() +
          "\" marked as class attribute but the class is already known to be\""+
          classAttribute.getName() + "\"!");
        classAttribute = attribute;
        classIndex = attrIndex;
      }
      attributes.add(attribute);
      attrIndex ++;
    }
	
	// parsing ATTRIBUTELIST to support range
	List attributeList = domElement.getChildren("ATTRIBUTELIST");
	if(attributeList != null) {
		Iterator childrenSerieIter = attributeList.iterator();
		while(childrenSerieIter.hasNext()){
			Element child = (Element)childrenSerieIter.next();
			List attributelist = Attribute.parseSeries(child);
			attributes.addAll(attributelist);
			attrIndex += attributelist.size();
		}
	}
	
    if(classAttribute == null) throw new GateException(
      "No class attribute defined!");
  }

  public DatasetDefintion(){
    attributes = new ArrayList();
    classAttribute = null;
    classIndex = -1;
    instanceType = null;
  }


  public String toString(){
    StringBuffer res = new StringBuffer();
    res.append("Instance type: " + instanceType + "\n");
    Iterator attrIter = attributes.iterator();
    while(attrIter.hasNext()){
      res.append("Attribute:" + attrIter.next().toString() + "\n");
    }
    return res.toString();
  }


  public java.util.List getAttributes() {
    return attributes;
  }

  public Attribute getClassAttribute(){
    return classAttribute;
  }
  public String getInstanceType() {
    return instanceType;
  }

  public int getClassIndex() {
    return classIndex;
  }
  public void setClassAttribute(Attribute classAttribute) {
    this.classAttribute = classAttribute;
  }
  public void setClassIndex(int classIndex) {
    this.classIndex = classIndex;
  }
  public void setInstanceType(String instanceType) {
    this.instanceType = instanceType;
  }

  protected java.util.List attributes;
  protected Attribute classAttribute = null;
  protected String instanceType;

  protected int classIndex;
}