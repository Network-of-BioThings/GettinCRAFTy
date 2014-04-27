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
package gate.creole.coref.taggers;

import gate.Annotation;
import gate.creole.ResourceInstantiationException;
import gate.creole.coref.AliasMap;
import gate.creole.coref.AliasMap.AliasData;
import gate.creole.coref.CorefBase;
import gate.creole.coref.Utils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;


/**
 * A tagger using aliases (such as nicknames).
 */
public class Alias extends AbstractTagger {

  protected String aliasFile;
  
  protected String encoding = "UTF=8";
  
  transient protected AliasMap aliasMap;
  
  /**
   * @param annotationType
   */
  public Alias(String annotationType, String aliasFile) {
    super(annotationType);
    this.aliasFile = aliasFile;
  }

  /* (non-Javadoc)
   * @see gate.creole.coref.taggers.AbstractTagger#init(gate.creole.coref.CorefBase)
   */
  @Override
  public void init(CorefBase owner) throws ResourceInstantiationException {
    if(aliasFile == null || aliasFile.length() == 0) {
      throw new ResourceInstantiationException("No value for the " +
      		"\"aliasFile\" parameter was provided!");
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

  /**
   * @return the aliasMap
   */
  public AliasMap getAliasMap() {
    return aliasMap;
  }

  /* (non-Javadoc)
   * @see gate.creole.coref.Tagger#tag(gate.Annotation[], int, gate.creole.coref.CorefBase)
   */
  @Override
  public Set<String> tag(Annotation[] anaphors, int anaphor, CorefBase owner) {
    Set<String> tags = new HashSet<String>();
    String text = gate.Utils.cleanStringFor(owner.getDocument(), 
        anaphors[anaphor]).trim();
    String[] keys = Utils.isMwe(text) ? Utils.partsForMwe(text) : new String[]{text};
    for(String key : keys) {
      for(AliasData aliasData : aliasMap.getAliases(key)) {
        tags.add(aliasData.getAlias().toUpperCase());
      }
    }
    if(tags.size() > 0) {
      // we've had some aliases: also add the plain text as uppercase
      tags.add(text.toUpperCase());
    }
    return tags;
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
