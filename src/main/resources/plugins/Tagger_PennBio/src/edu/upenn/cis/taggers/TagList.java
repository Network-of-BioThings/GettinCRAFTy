/* ***** BEGIN LICENSE BLOCK *****
 * Version: MPL 1.1
 *
 * The contents of this file are subject to the Mozilla Public License Version
 * 1.1 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 * for the specific language governing rights and limitations under the
 * License.
 *
 * The Original Code is the BioTagger : Intellegent Biomedical Tagging System.
 *
 * The Initial Developer of the Original Code is Ryan T. McDonald.
 * Portions created by the Initial Developer are Copyright (C) 2005
 * the Initial Developer. All Rights Reserved.
 *
 * Contributor(s):
 *    Ryan T. MacDonald <ryantm@cis.upenn.edu> (Original Author)
 *    Kevin Lerman      <klerman@seas.upenn.edu>
 *    Eric D. Pancoast  <edp23@linc.cis.upenn.edu> 
 *
 * ***** END LICENSE BLOCK ***** */

/*
 * Created on Jan 11, 2005
 */

package edu.upenn.cis.taggers;

import java.util.ArrayList;
import java.util.Iterator;


public class TagList {
      ArrayList tags;
      
      public TagList() {
          tags = new ArrayList();
      }
      
      public Iterator iterator() {
          return tags.iterator();
      }
      
      public void addTag(Tag tag) {
          if(tag instanceof Tag) {
              tags.add(tag);
          } else {
              //throw exception
          }
      }
      public Tag getTag(int index) {
          return (Tag)tags.get(index);
      }
      
      public String toXML(String[] tokens) {
          //Convert tag data to original XML
          StringBuffer xml = new StringBuffer();
          Tag t = null;
          int ti = 0;
          boolean tagComplete = false;
          for(int i=0; i<tokens.length; i++){
              if(t == null || tagComplete) {
                  t = (ti<tags.size())?getTag(ti++):null;
                  tagComplete = false;
              }
              String pre="",post="";
              if(t!=null && t.getTokenStartIndex()==i) { pre = "<"+t.getTagname()+">"; }
              if(t!=null && t.getTokenEndIndex()==i) { post = "</"+t.getTagname()+">"; tagComplete=true; }
              xml.append(pre+tokens[i]+post+((i<tokens.length-1)?" ":""));
          }
          return xml.toString();
      }
  }