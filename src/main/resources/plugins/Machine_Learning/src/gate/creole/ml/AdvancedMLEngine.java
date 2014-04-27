/*
 *  Copyright (c) 1995-2012, The University of Sheffield. See the file
 *  COPYRIGHT.txt in the software or at http://gate.ac.uk/gate/COPYRIGHT.txt
 *
 *  This file is part of GATE (see http://gate.ac.uk/), and is free
 *  software, licenced under the GNU Library General Public License,
 *  Version 2, June 1991 (in the distribution as file licence.html,
 *  and also available at http://gate.ac.uk/gate/licence.html).
 *
 *  Julien Nioche 04/03/2005
 *
 *
 */
package gate.creole.ml;

/**
 * This interface is used for wrappers to Machine Learning engines. All classes
 * implementing this interface should have a public constructor that takes no
 * parameters. It extends MLEngine by providing more information about the
 * capacities of the engine.
 */
public interface AdvancedMLEngine extends MLEngine{
  /**
   * Returns true if the engine supports BatchMode, returns false otherwise.
   */
  public boolean supportsBatchMode();
}