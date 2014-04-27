/*
 * AbnerRunMode.java
 * 
 * Mark A. Greenwood
 */
package gate.abner;

import java.util.EnumSet;

import abner.Tagger;

/**
 * An Enum used to control which model ABNER uses when processing documents
 * 
 * @author Mark A. Greenwood
 */
public enum AbnerRunMode {
  BIOCREATIVE(Tagger.BIOCREATIVE), NLPBA(Tagger.NLPBA);

  private final int id;

  private AbnerRunMode(int id) {
    this.id = id;
  }

  public int getValue() {
    return id;
  }

  public static AbnerRunMode get(int id) {
    for(AbnerRunMode t : EnumSet.allOf(AbnerRunMode.class)) {
      if(t.id == id) return t;
    }

    throw new IllegalArgumentException("'" + id + "' is not a run mode");
  }
}
