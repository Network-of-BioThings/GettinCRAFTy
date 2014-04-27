/*
 *  AliasMap.java
 *
 *  Copyright (c) 1995-2012, The University of Sheffield. See the file
 *  COPYRIGHT.txt in the software or at http://gate.ac.uk/gate/COPYRIGHT.txt
 *
 *  This file is part of GATE (see http://gate.ac.uk/), and is free
 *  software, licenced under the GNU Library General Public License,
 *  Version 2, June 1991 (in the distribution as file licence.html,
 *  and also available at http://gate.ac.uk/gate/licence.html).
 *
 *  Valentin Tablan, 9 Mar 2012
 *
 *  $Id: AliasMap.java 15568 2012-03-09 14:15:23Z valyt $
 */
package gate.creole.coref;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

/**
 * A simple data structure for holding aliases. Each alias entry comprises:<ul>
 * <li>a key (String)</li>
 * <li>an alias (String)</li>
 * <li>a confidence score (float, optional)</li>
 * </ul>
 * For example: (ALEX,  ALEXANDER, 0.85).
 * 
 * The data is stored internally as a {@link Map} from key to {@link List} of
 * {@link AliasData}.
 */
public class AliasMap {

  private static final Logger log = Logger.getLogger(AliasMap.class);
  
  private static final Set<AliasData> EMPTY = Collections.emptySet();
  
  /**
   * Data structure holding the information associated with an alias key. 
   */
  public static class AliasData {
    
    private String alias;
    private float score;
    
    /**
     * Create a new instance.
     * @param alias the alias to be associated with the key;
     * @param score the score, as a positive value. By convention, use any 
     * negative value when no score is required.  
     */
    public AliasData(String alias, float score) {
      this.alias = alias;
      this.score = score;
    }

    /**
     * @return the alias
     */
    public final String getAlias() {
      return alias;
    }

    /**
     * Gets the score associated with the key - alias mapping. By convention, a
     * negative value indicates that no score is available.  
     * @return the score
     */
    public final float getScore() {
      return score;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((alias == null) ? 0 : alias.hashCode());
      result = prime * result + Float.floatToIntBits(score);
      return result;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
      if(this == obj) return true;
      if(obj == null) return false;
      if(!(obj instanceof AliasData)) return false;
      AliasData other = (AliasData)obj;
      if(alias == null) {
        if(other.alias != null) return false;
      } else if(!alias.equals(other.alias)) return false;
      if(Float.floatToIntBits(score) != Float.floatToIntBits(other.score))
        return false;
      return true;
    }    
  }
  
  /**
   * The {@link Map} holding the actual data.
   */
  protected Map<String, Set<AliasData>> data;
  
  /**
   * Create a new alias map.
   */
  public AliasMap () {
    data = new HashMap<String, Set<AliasData>>();
  }
  
  
  public AliasMap (Reader inputReader) throws IOException {
    this();
    BufferedReader in = new BufferedReader(inputReader);
    String line = in.readLine();
    int lineNo = 1;
    while(line != null) {
      line = line.trim();
      if(line.startsWith("#") || line.startsWith("//")){
        // ignore
      } else {
        String elems[] = line.split("\\\t");
        if(elems.length >= 2) {
          float score = -1.0f;
          if(elems.length > 2) {
            try {
              score = Float.parseFloat(elems[2]);
            } catch(NumberFormatException e) {
              log.warn("Invalid score value in alias file at line " + 
                  lineNo + " (could not be parsed as a float value):" +
                  elems[2]);
            }
          }
          addAlias(elems[0], elems[1], score);
          // alias relation is symmetric 
          addAlias(elems[1], elems[0], score);
        } else {
          log.warn("Line " + lineNo + " in alias file is invalid " +
          		"(should be 2 or 3 tab-separated values): \"" + line + "\".");
        }
      }
      line = in.readLine();
      lineNo++;
    }
  }
  
  /**
   * Associate a new alias with the given key. 
   * @param key the for the alias
   * @param alias the alias value
   * @param score the score (use a negative value when no score is available).
   */
  public void addAlias(String key, String alias, float score) {
    key = key.toUpperCase();
    Set<AliasData> aliases = data.get(key);
    if(aliases == null) {
      aliases = new HashSet<AliasMap.AliasData>();
      data.put(key, aliases);
    }
    aliases.add(new AliasData(alias, score));
  }
  
  /**
   * Gets all the aliases associated with the given key.
   * @param key the value being looked-up
   * @return all the aliases associated with the given key, or an empty set if
   * there are none.
   */
  public Set<AliasData> getAliases(String key) {
    key = key.toUpperCase();
    Set<AliasData> res = data.get(key);
    return res == null ? EMPTY : res;
  }
  
}
