/*
 *  Copyright (c) 1995-2012, The University of Sheffield. See the file
 *  COPYRIGHT.txt in the software or at http://gate.ac.uk/gate/COPYRIGHT.txt
 *
 *  This file is part of GATE (see http://gate.ac.uk/), and is free
 *  software, licenced under the GNU Library General Public License,
 *  Version 2, June 1991 (in the distribution as file licence.html,
 *  and also available at http://gate.ac.uk/gate/licence.html).
 *
 *  Valentin Tablan 21/11/2002
 *
 *  $Id: MLEngine.java 6491 2005-01-11 13:51:38 +0000 (Tue, 11 Jan 2005) ian $
 *
 */
package gate.creole.ml;

import java.util.List;

import org.jdom.Element;

import gate.ProcessingResource;
import gate.creole.ExecutionException;
import gate.util.GateException;

/**
 * This interface is used for wrappers to Machine Learning engines.
 * All classes implementing this interface should have a public constructor
 * that takes no parameters.
 */
public interface MLEngine {

  /**
   * Sets the options from an XML JDom element.
   * @param options the JDom element containing the options from the
   * configuration.
   */
  public void setOptions(Element options);

  /**
   * Adds a new training instance to the dataset.
   * @param attributes the list of attributes describing the instance. The
   * elements in the list are String values that need to be interpreted
   * according to the dataset definition: for nominal attributes the values will
   * used as they are; for numeric attributes the values will be converted to
   * double.
   */
  public void addTrainingInstance(List attributes)throws ExecutionException;

  /**
   * Sets the definition for the dataset used.
   * @param definition
   */
  public void setDatasetDefinition(DatasetDefintion definition);

  /**
   * Classifies a new instance.
   * @param attributes the list of attributes describing the instance. The
   * elements in the list are Object values that need to be interpreted
   * according to the dataset definition. The value for the class element will
   * be arbitrary.
   * @return a String value for nominal and boolean attributes and a Double
   * value for numeric attributes.
   */
  public Object classifyInstance(List attributes)throws ExecutionException;

  /**
   * Like classify instances, but take a list of instances instead of a single
   * instance, and return a list of results (one for each instance) instead of
   * a single result.
   *
   * @param instances A list of lists of attributes describing the instance. The
   * value for all of the class elements will be arbitrary.
   * @return A list of values predicted for the class attribute, which will be
   * Strings when the class in nominal or boolean, and a Double values
   * otherwise.
   * @throws ExecutionException
   */
  public List batchClassifyInstances(List instances) throws ExecutionException;

  /**
   * This method will be called after an engine is created and has its dataset
   * and options set. This allows the ML engine to initialise itself in
   * preparation of being used.
   *
   * @throws GateException
   */
  public void init() throws GateException;

  /**
   * Registers the PR using the engine with the engine itself.
   * @param pr the processing resource that owns this engine.
   */
  public void setOwnerPR(ProcessingResource pr);

  /**
   * Cleans up any resources allocated by the Engine when it is destroyed.
   * (Generally this is most likely to be needed by those wrappers that
   * call native code.)
   */
  public void cleanUp();
}