/*
 *  Collate.java
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
 *  $Id: Collate.java 15487 2012-02-28 15:35:34Z valyt $
 */
package gate.creole.coref.taggers;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import gate.Annotation;
import gate.creole.coref.CorefBase;
import gate.creole.coref.Tagger;

/**
 *
 */
public class Collate extends AbstractTagger {
  
  public Collate(String annotationType, List<Tagger> subTaggers) {
    super(annotationType);
    this.subTaggers = subTaggers.toArray(new Tagger[subTaggers.size()]);
  }
  
  public Collate(String annotationType, Tagger[] subTaggers) {
    super(annotationType);
    this.subTaggers = new Tagger[subTaggers.length];
    System.arraycopy(subTaggers, 0, this.subTaggers, 0, subTaggers.length);
  }


  /* (non-Javadoc)
   * @see gate.creole.coref.Tagger#tag(gate.Annotation[], int, gate.creole.coref.CorefBase)
   */
  @Override
  public Set<String> tag(Annotation[] anaphors, int anaphor, CorefBase owner) {
    Set<String> res = new HashSet<String>();
    
    String[][] tags = new String[subTaggers.length][];
    for(int  i = 0; i <  subTaggers.length; i++){
      Set<String> someTags = subTaggers[i].tag(anaphors, anaphor, owner);
      tags[i] = new String[someTags.size()];
      Iterator<String> tagIter = someTags.iterator();
      for(int  j = 0; j < tags[i].length; j++) {
        tags[i][j] = tagIter.next();
      }
    }
    // position pointer into the tags arrays 
    int[] position = new int[tags.length];
    // current tagger
    int posIdx = 0;
    while(posIdx >= 0) {
      if(position[posIdx] < tags[posIdx].length) {
        // use the currently pointed value, and move to next field
        if(posIdx < subTaggers.length -1) {
          posIdx++;
        } else {
          // we're at the last position: produce new output
          StringBuilder tag = new StringBuilder();
          for(int i = 0; i < position.length; i++) {
            tag.append(tags[i][position[i]]);
          }
          res.add(tag.toString());
          // move to next value at this position
          position[posIdx]++;
        }        
      } else {
        // no more values on this field: move back
        position[posIdx] = 0;
        posIdx--;
        if(posIdx >= 0) position[posIdx]++;
      }
    }
    return res;
  }
  
  private final Tagger[] subTaggers;
}
