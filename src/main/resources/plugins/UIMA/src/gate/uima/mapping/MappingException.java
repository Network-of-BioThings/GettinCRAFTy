/*
 *  Copyright (c) 2005, The University of Sheffield.
 *
 *  This file is part of the GATE/UIMA integration layer, and is free
 *  software, released under the terms of the GNU Lesser General Public
 *  Licence, version 2.1 (or any later version).  A copy of this licence
 *  is provided in the file LICENCE in the distribution.
 *
 *  UIMA is a product of IBM, details are available from
 *  http://alphaworks.ibm.com/tech/uima
 */
package gate.uima.mapping;

/**
 * Signals an error of some kind that has occurred during either constructing
 * the mapping between GATE and UIMA annotation models, or while trying to
 * apply that mapping to a particular document.
 */
public class MappingException extends Exception {
  public MappingException() {
    super();
  }

  public MappingException(String message) {
    super(message);
  }

  public MappingException(Throwable cause) {
    super(cause);
  }

  public MappingException(String message, Throwable cause) {
    super(message, cause);
  }
}
