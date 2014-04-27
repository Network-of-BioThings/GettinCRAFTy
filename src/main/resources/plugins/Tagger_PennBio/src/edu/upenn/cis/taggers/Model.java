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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.zip.GZIPInputStream;

import edu.umass.cs.mallet.base.fst.CRF4;

/**
 * @author Eric Pancoast
 */
public class Model {
    
    public final static String GZIP_EXTENSION = ".gz";
    public final static String RAW_MODEL_EXTENSION = ".crf";

    public static CRF4 loadAndRetrieveModelFromClassPathResource(String modelPath, Class c) throws LoadModelException {
        InputStream is = null;
        if(modelPath.endsWith(GZIP_EXTENSION)) {
            System.out.println("Creating GZIP InputStream for model from ClassPath resource...");
            try {
                is = new GZIPInputStream(c.getResourceAsStream(modelPath));
            } catch(IOException ioe) {
                ioe.printStackTrace();
                throw new LoadModelException("IOException creating model InputStream from ClassPath resource: ["+modelPath+"]");
            }
            System.out.println("Created GZIP InputStream Successfully.");
        } else if(modelPath.endsWith(RAW_MODEL_EXTENSION)) {
            System.out.println("Creating File InputStream for model from ClassPath resource...");
            is = c.getResourceAsStream(modelPath);
            System.out.println("Created File InputStream Successfully.");
        } else {
            int ext_index = modelPath.lastIndexOf(".");
            throw new LoadModelException("Unknown model extension: "+modelPath.substring((ext_index==-1)?0:ext_index));
        }
        return loadAndRetrieveModel(is);
    }
    
    public static CRF4 loadAndRetrieveModel(String modelLocation) throws LoadModelException {
        InputStream is = null;
        System.out.println("Checking to see if the model file exists...");
        File model = new File(modelLocation);
        if(!model.exists()) {
            throw new LoadModelException("Model file cannot be found: ["+model.getAbsolutePath()+"]");
        }
        if(!model.canRead()) {
            throw new LoadModelException("Model file found but cannot be read: ["+model.getAbsolutePath()+"]");
        }
        System.out.println("Model file exists and is readable.");
        if(model.getName().endsWith(GZIP_EXTENSION)) {
            System.out.println("Creating GZIP InputStream for model...");
            try {
                is = new GZIPInputStream(new FileInputStream(model));
            } catch(IOException ioe) {
                ioe.printStackTrace();
                throw new LoadModelException("IOException creating model InputStream: ["+model.getAbsolutePath()+"]");
            }
            System.out.println("Created GZIP InputStream Successfully.");
        } else if(model.getName().endsWith(RAW_MODEL_EXTENSION)) {
            System.out.println("Creating File InputStream for model...");
            try {
                is = new FileInputStream(model);
            } catch(IOException ioe) {
                ioe.printStackTrace();
                throw new LoadModelException("IOException creating model InputStream: ["+model.getAbsolutePath()+"]");
            }
            System.out.println("Created File InputStream Successfully.");
        } else {
            int ext_index = model.getName().lastIndexOf(".");
            throw new LoadModelException("Unknown model extension: "+model.getName().substring((ext_index==-1)?0:ext_index));
        }
        return loadAndRetrieveModel(is);
    }
    
    public static CRF4 loadAndRetrieveModel(InputStream is) throws LoadModelException {
        ObjectInputStream ois = null;
        CRF4 crf = null;
        System.out.println("Creating ObjectInputStream from InputStream...");
        try {
            ois = new ObjectInputStream(is);
        } catch(IOException ioe) {
            ioe.printStackTrace();
            throw new LoadModelException("IOException making ObjectInputStream from model InputStream.");
        }
        System.out.println("OIS Created Successfully.");
        System.out.println("Reading Java Object from OIS...");
        try { 
            crf = (CRF4)ois.readObject();
        } catch(ClassNotFoundException cnfe) {
            cnfe.printStackTrace();
            throw new LoadModelException("ClassNotFoundException reading model OIS into CRF4");
        } catch(IOException ioe) {
            ioe.printStackTrace();
            throw new LoadModelException("IOException reading model OIS into CRF4");
        }
        System.out.println("CRF4 Model Object read successfully ("+crf+").");
        System.out.println("Stopping CRF InputAlphabet Growth...");
        crf.getInputAlphabet().stopGrowth();
        System.out.println("Model loading completed successfully.");
        return crf;
    }
}
