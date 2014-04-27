/*
 * Units is a program for unit conversion originally written in C by Adrian
 * Mariano (adrian@cam.cornell.edu.). Copyright (C) 1996, 1997, 1999, 2000,
 * 2001, 2002, 2003, 2004, 2005, 2006 by Free Software Foundation, Inc.
 * 
 * Java version Copyright (C) 2003, 2004, 2005, 2006, 2007 by Roman R
 * Redziejowski (roman.redz@tele2.se).
 * 
 * GATE version is Copyright (c) 2009-2011, The University of Sheffield.
 * 
 * This file is part of GATE (see http://gate.ac.uk/), and is free software,
 * Licensed under the GNU Library General Public License, Version 3, June 2007
 * (in the distribution as file licence.html, and also available at
 * http://gate.ac.uk/gate/licence.html).
 */
package gate.creole.measurements;

/**
 * A function (built-in, computed, or tabular).
 */
abstract class Function extends Entity {
  Function(String nam, MeasurementsParser gnuUnits) {
    super(nam, gnuUnits);
  }

  /**
   * Apply the function to Value 'v' (with result in 'v').
   */
  abstract void applyTo(Measurement v) throws Parser.Exception;

  /**
   * Apply inverse of the function to Value 'v' (with result in 'v').
   */
  abstract void applyInverseTo(Measurement v) throws Parser.Exception;
}