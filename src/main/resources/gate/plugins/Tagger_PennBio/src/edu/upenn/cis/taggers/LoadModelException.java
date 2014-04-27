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
 * Created on Apr 7, 2005
 */

package edu.upenn.cis.taggers;

/**
 * @author Eric Pancoast
 */
public class LoadModelException extends Exception {
    public LoadModelException(String model) {
        super("An error occured loading the specified model: ["+model+"]");
    }
}
