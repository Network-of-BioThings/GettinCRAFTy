/*
 * Behaviour.java
 * 
 * Copyright (c) 2011, The University of Sheffield.
 * 
 * This file is part of GATE (see http://gate.ac.uk/), and is free software,
 * licenced under the GNU Library General Public License, Version 3, June 2007
 * (in the distribution as file licence.html, and also available at
 * http://gate.ac.uk/gate/licence.html).
 * 
 * Mark A. Greenwood, 07/02/2010
 */

package gate.creole.boilerpipe;

/**
 * An enum to describe how to interpret the set of mime types being processed by
 * the processing resource.
 * 
 * @author Mark A. Greenwood
 */
public enum Behaviour {

  /**
   * If the mime type of the document being processed is in the list then it's
   * entire body will be tagged as content.
   */
  LISTED("If Mime Type Is Listed"),

  /**
   * If the mime type of the document being processed is not in the list then
   * it's entire body will be tagged as content.
   */
  NOT_LISTED("If Mime Type Is NOT Listed");

  /**
   * The human readable description that will get displayed in the GATE gui
   */
  private final String description;

  Behaviour(String description) {
    this.description = description;
  }

  /**
   * Returns the description of the enum value
   * 
   * @return the description of the enum value
   */
  @Override
  public String toString() {
    return description;
  }
}
