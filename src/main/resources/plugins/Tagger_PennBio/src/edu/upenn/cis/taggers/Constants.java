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
 * Created on May 2, 2005
 */

package edu.upenn.cis.taggers;

/**
 * @author Eric Pancoast
 */
public class Constants {
    
    //Gene Tagger Serial Version UIDs
    public static final long SVUID_GENE_CONTAINS_LOW_FREQ_TRIGRAM = 91L;
    public static final long SVUID_GENE_BIO_CREATIVE_SENTENCE_2_TOKEN_SEQUENCE = 92L;
    public static final long SVUID_GENE_REGEX_TRIELEXICON_MEMBERSHIP = 93L;
    public static final long SVUID_GENE_IN_BRACKET = 94L;
    
    //Variation Tagger Serial Version UIDs
    public static final long SVUID_VARIATION_REGEX_TRIELEXICON_MEMBERSHIP = 71L;
    public static final long SVUID_VARIATION_SENTENCE_2_TOKEN_SEQUENCE = 72L;
}
