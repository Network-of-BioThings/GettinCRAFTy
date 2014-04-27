/*
 *  Copyright (c) 2012, The University of Sheffield. See the file
 *  COPYRIGHT.txt in the software or at http://gate.ac.uk/gate/COPYRIGHT.txt
 *
 *  This file is part of GATE (see http://gate.ac.uk/), and is free
 *  software, licenced under the GNU Library General Public License,
 *  Version 2, June 1991 (in the distribution as file licence.html,
 *  and also available at http://gate.ac.uk/gate/licence.html).
 *
 *  $Id: TfCalculation.java 16296 2012-11-20 12:17:11Z adamfunk $
 */
package gate.termraider.bank.modes;

public enum TfCalculation {
  Natural,
  Logarithmic;
  
  
  public static double calculate(TfCalculation mode, int rawTF) {
    double tf = (double) rawTF;
    
    if (mode == Logarithmic) {
      return 1.0 + IdfCalculation.logarithm(tf);
    }
    
    // must be Natural
    return tf;
  }
}
