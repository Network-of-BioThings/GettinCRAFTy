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


public class Tag {
      //Name of the tag
      String tagname;
      //Contains all of the words or parts of the tag
      ArrayList segments;
      //Where the tag begins in the original text
      int startOffset;
      //Where the tag ends in the original text
      int endOffset;
      //What number token the tag starts at in the 
      //current chunk being processed
      int tokenStartIndex;
      //What number token the tag ends at in the 
      //current chunk being processed
      int tokenEndIndex;
      
      public Tag(String tagname) {
          this.tagname = tagname;
          segments = null;
          startOffset = -1;
          endOffset = -1;
          tokenStartIndex = -1;
          tokenEndIndex = -1;
      }
      
      public String getTagname() {
          return tagname;
      }
      
      //Adds a string segment to the tag
      public void addSegment(String segment, int tokenIndex) {
          if(tokenStartIndex == -1) { this.tokenStartIndex = tokenIndex; }
          tokenEndIndex = tokenIndex;
          if(segments==null) {
              segments = new ArrayList();
          }
          segments.add(segment);
      }
      
      public void updateOffset(int start, int end) {
          if(startOffset == -1) { startOffset = start; }
          endOffset = end;
      }
      
      public int getStartOffset() {
          return startOffset;
      }
      public int getEndOffset() {
          return endOffset;
      }
      
      public int getTokenStartIndex() {
          return tokenStartIndex;
      }
      public int getTokenEndIndex() {
          return tokenEndIndex;
      }
      
      public String toString() {
          String tag = "";
          for(int i=0; i<segments.size(); i++) {
              tag+=((i>0)?",":"")+"["+(String)segments.get(i)+"]";
          }
          return tag;
      }
  }