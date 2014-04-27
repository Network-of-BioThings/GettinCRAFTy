/**
 *  Copyright (c) 1995-2012, The University of Sheffield. See the file
 *  COPYRIGHT.txt in the software or at http://gate.ac.uk/gate/COPYRIGHT.txt
 *
 *  This file is part of GATE (see http://gate.ac.uk/), and is free
 *  software, licenced under the GNU Library General Public License,
 *  Version 2, June 1991 (in the distribution as file licence.html,
 *  and also available at http://gate.ac.uk/gate/licence.html).
 *
 *  Yaoyong Li 15/03/2009
 *
 *  $Id$
 */

package gate.bdmComp;

import gate.creole.ontology.OClass;

public class BDMOne {
  OClass con11;
  OClass con22;
  OClass msca;
  public float bdmScore = 0;
  int cp = 0;
  int dpk = 0;
  int dpr =0;
  float n0;
  float n1;
  float n2;
  float bran;
  /**
   * Constructor Create a BDMone object for two concepts.
   */
  public BDMOne(OClass oneC, OClass twoC) {
    con11 = oneC;
    con22 = twoC;
    msca = null;
    bdmScore = -1.0f;
    cp = 0;
    dpk = 0;
    dpr = 0;
    n0 = 0;
    n1 = 0;
    n2 = 0;
    bran = 0;
    
  }
  public void setValues(float b, int c, int k, int r, float m0, float m1, float m2, float br) {
    bdmScore = b;
    cp = c;
    dpk = k;
    dpr = r;
    n0 = m0;
    n1 = m1;
    n2 = m2;
    bran = br;
  }
  public void setMsca(OClass con) {
    msca = con;
  }
  public String printResult() {
    String text = "";
    text += "key="+con11.getONodeID().toString()+", ";
    text += "response="+con22.getONodeID().toString()+", ";
    text += "bdm="+bdmScore +", "; 
    //text += "("+con11.getName()+","+con22.getName()+"), ";
    if(msca != null) text += "msca="+msca.getONodeID().toString()+", ";
    else text +=" no MSCA! ";
    text += "cp="+cp+", dpk="+dpk+", dpr="+dpr+", ";
    text += "n0="+n0+", n1="+n1+", n2="+n2+", bran="+bran;
    //System.out.println(text);
    return text;
  }
}
