/*
 *  Alias.java
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
 *  $Id: Alias.java 15587 2012-03-13 14:16:59Z valyt $
 */
package gate.creole.coref.matchers;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import gate.Annotation;
import gate.creole.ResourceInstantiationException;
import gate.creole.coref.AliasMap;
import gate.creole.coref.AliasMap.AliasData;
import gate.creole.coref.CorefBase;
import gate.creole.coref.Utils;

/**
 * A matcher using aliases (e.g. nicknames, such as Alex for Alexander).
 */
public class Alias extends AbstractMatcher {

  protected gate.creole.coref.taggers.Alias aliasTagger;
  
  protected String aliasFile;
  
  protected String encoding = "UTF-8";
  
  protected transient AliasMap aliasMap;
  
  
  /**
   * @param annotationType
   * @param antecedentType
   */
  public Alias(String annotationType, String antecedentType, 
      String aliasFile) {
    super(annotationType, antecedentType);
    this.aliasFile = aliasFile;
  }

  
  /**
   * @param annotationType
   * @param antecedentType
   */
  public Alias(String annotationType, String antecedentType, 
      gate.creole.coref.taggers.Alias aliasTagger) {
    super(annotationType, antecedentType);
    this.aliasTagger = aliasTagger;
  }
  
  /* (non-Javadoc)
   * @see gate.creole.coref.taggers.AbstractTagger#init(gate.creole.coref.CorefBase)
   */
  @Override
  public void init(CorefBase owner) throws ResourceInstantiationException {
    if(aliasTagger != null) {
      // use shared data
      this.aliasMap = aliasTagger.getAliasMap();
    } else {
      if(aliasFile == null || aliasFile.length() == 0) {
        throw new ResourceInstantiationException("No shared AliasTagger " +
        		"instance was provided, and no aliasFile was used either.");
      }
      try {
        URL configFileUrl = owner.getConfigFileUrl();
        URL aliasFileUrl = new URL(configFileUrl, aliasFile);
        // parse the aliases
        aliasMap = new AliasMap(new InputStreamReader(
            aliasFileUrl.openStream(), encoding));
      } catch(IOException e) {
        throw new ResourceInstantiationException(e);
      }      
    }
  }
  
  /* (non-Javadoc)
   * @see gate.creole.coref.Matcher#matches(gate.Annotation[], int, int, gate.creole.coref.CorefBase)
   */
  @Override
  public boolean matches(Annotation[] anaphors, int antecedent, int anaphor,
      CorefBase owner) {
    String one = gate.Utils.cleanStringFor(owner.getDocument(), 
        anaphors[anaphor]).trim().toUpperCase();
    String other = gate.Utils.cleanStringFor(owner.getDocument(), 
        anaphors[antecedent]).trim().toUpperCase();
    
    Map<String, AliasData> someAliases = new HashMap<String, AliasMap.AliasData>();
    String[] someKeys = Utils.isMwe(one) ? Utils.partsForMwe(one) : new String[]{one};
    for(String key : someKeys) {
      for(AliasData aData : aliasMap.getAliases(key)){
        someAliases.put(aData.getAlias().toUpperCase(), aData);
      }
    }
    
    Map<String, AliasData> otherAliases = new HashMap<String, AliasMap.AliasData>();
    String[] otherKeys = Utils.isMwe(other) ? Utils.partsForMwe(other) : new String[]{other};
    for(String key : otherKeys) {
      for(AliasData aData : aliasMap.getAliases(key)){
        otherAliases.put(aData.getAlias().toUpperCase(), aData);
      }
    }
    
    AliasData match = null;
    for(String key : otherKeys) {
      match = someAliases.get(key);
      if(match != null) break;
    }
    if(match == null) {
      for(String key : someKeys) {
        match = someAliases.get(key);
        if(match != null) break;
      } 
    }
    if(match == null) {
      // try alias to alias
      Set<String> sharedAliases = new HashSet<String>(someAliases.keySet());
      sharedAliases.retainAll(otherAliases.keySet());
      if(!sharedAliases.isEmpty()) {
        match = someAliases.get(sharedAliases.iterator().next());
      }
    }
    // TODO: do something about the score
    return match != null;
  }

  /**
   * @return the encoding
   */
  public String getEncoding() {
    return encoding;
  }

  /**
   * @param encoding the encoding to set
   */
  public void setEncoding(String encoding) {
    this.encoding = encoding;
  }
}
