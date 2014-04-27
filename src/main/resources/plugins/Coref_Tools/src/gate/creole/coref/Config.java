/*
 *  Config.java
 *
 *  Copyright (c) 1995-2012, The University of Sheffield. See the file
 *  COPYRIGHT.txt in the software or at http://gate.ac.uk/gate/COPYRIGHT.txt
 *
 *  This file is part of GATE (see http://gate.ac.uk/), and is free
 *  software, licenced under the GNU Library General Public License,
 *  Version 2, June 1991 (in the distribution as file licence.html,
 *  and also available at http://gate.ac.uk/gate/licence.html).
 *
 *  Valentin Tablan, 20 Feb 2012
 *
 *  $Id: Config.java 15487 2012-02-28 15:35:34Z valyt $
 */
package gate.creole.coref;

import java.util.List;

/**
 * Class storing the configuration information for a coreferencer. Used to 
 * serialise the config to and from XML.
 */
public class Config {
  
  private List<Tagger> taggers;
  
  private List<Matcher> matchers;

  /**
   * @return the taggers
   */
  public List<Tagger> getTaggers() {
    return taggers;
  }

  /**
   * @param taggers the taggers to set
   */
  public void setTaggers(List<Tagger> taggers) {
    this.taggers = taggers;
  }

  /**
   * @return the matchers
   */
  public List<Matcher> getMatchers() {
    return matchers;
  }

  /**
   * @param matchers the matchers to set
   */
  public void setMatchers(List<Matcher> matchers) {
    this.matchers = matchers;
  }
  
}
