/*
 *  Copyright (c) 2004, The University of Sheffield.
 *
 *  This file is part of GATE (see http://gate.ac.uk/), and is free
 *  software, licenced under the GNU Library General Public License,
 *  Version 2, June 1991 (in the distribution as file licence.html,
 *  and also available at http://gate.ac.uk/gate/licence.html).
 *
 *  Mike Dowman 30-03-2004
 *
 *  $Id: GateEventStream.java 5616 2004-04-28 11:25:29 +0000 (Wed, 28 Apr 2004) valyt $
 *
 */

package gate.creole.ml.maxent;

/**
 * This class is used by MaxentWrapper. When created, it is passed a data
 * structure containg all the training data for the classifier. It can then
 * provide this data to the maxent model itself, as needed.
 */
public class GateEventStream implements opennlp.maxent.EventStream {

  boolean DEBUG=false;

  final java.util.List trainingData;
  final int indexOfOutcome;

  int index=0;

  /**
   * This constructor stores all the training data in the object when the object
   * is created.
   *
   * @param newTrainingData A List of Lists of String objects. Each String is
   * a maxent feature or outcome.
   * @param newIndexOfOutcome This is the index of the String objects that are
   * the outcomes.
   */
  GateEventStream(java.util.List newTrainingData, int newIndexOfOutcome) {
    trainingData=newTrainingData;
    indexOfOutcome=newIndexOfOutcome;
  }

  /**
   * Extract the next instance from those stored in this object, and advance
   * the objects internal index to point at the next instance.
   *
   * An exception will be thrown if this method is called when there are no
   * more instances to extract.
   *
   * @return The next instance.
   */
  public opennlp.maxent.Event nextEvent() {
    ++index;
    return instance2Event((java.util.List)trainingData.get(index-1));
  }

  /**
   * See whether there are any more instances to be extracted from this object.
   *
   * @return true if there are more instances, false otherwise.
   */
  public boolean hasNext() {
    return index<trainingData.size();
  }

  /**
   * Convert an instance into an Event object, taking note of the position of
   * the outcome (class attribute) stored in this object.
   *
   * @param instance The instance in the form of a list of String objects.
   * @return A maxent Event object containing the outcome (class attribute) and
   * the features (other attributes).
   */
  private opennlp.maxent.Event instance2Event(java.util.List instance) {
    // Store the outcome separately - and make sure that if it's null then
    // it gets converted to the String "null".
    java.lang.String outcome=""+(java.lang.String)instance.get(indexOfOutcome);

    // Then make a new list which doesn't contain the outcome.
    java.util.List features=
        new java.util.ArrayList(instance.subList(0, indexOfOutcome));
    features.addAll(instance.subList(indexOfOutcome+1, instance.size()));

    if (DEBUG) {
      System.out.println("New event: outcome="+outcome);
      System.out.println("features="+instance);
    }

    // Now make the Event and return it.
    return new opennlp.maxent.Event(outcome,
                                    (String[])features.toArray(new String[0]));
  }
}

