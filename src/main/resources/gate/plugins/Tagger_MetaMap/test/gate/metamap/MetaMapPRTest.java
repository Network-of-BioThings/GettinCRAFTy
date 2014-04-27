/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gate.metamap;

import gate.*;
import gate.creole.*;
import gate.creole.metadata.*;
import gate.util.*;

import gate.Annotation;
import gate.AnnotationSet;
import gate.Document;
import gate.Factory;
import gate.FeatureMap;

import gov.nih.nlm.nls.metamap.Ev;
import gov.nih.nlm.nls.metamap.Negation;
import gov.nih.nlm.nls.metamap.PCM;
import gov.nih.nlm.nls.metamap.Result;
import java.util.ArrayList;
import java.util.List;
import junit.framework.TestCase;

/**
 *
 * @author philipgooch
 */
public class MetaMapPRTest extends TestCase  {
    
    public MetaMapPRTest(String testName) {
        super(testName);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Test of init method, of class MetaMapPR.
     */
    public void testInit() throws Exception {
        System.out.println("init");
        MetaMapPR instance = new MetaMapPR();
        instance.setOutputMode(OutputMode.AllMappings);
        instance.setTaggerMode(TaggerMode.CoReference);
        instance.setOutputASType("MetaMap");
        
        Resource result = instance.init();
        assertNotNull("Instance should not be null", result);
        assertEquals(instance.getOutputMode(), OutputMode.AllMappings);
    }


    /**
     * Test of normalizeString method, of class MetaMapPR.
     */
    public void testNormalizeString() throws ClassNotFoundException {
        System.out.println("normalizeString");
        String str = "Ångström";
        MetaMapPR instance = new MetaMapPR();
        String expResult = "Angstrom";
        String result = instance.normalizeString(str);
        assertEquals(expResult, result);
        
    }

    /**
     * Test of filterNonAscii method, of class MetaMapPR.
     */
    public void testFilterNonAscii() throws Exception {
        System.out.println("filterNonAscii");
        String text = "Ångström";
        MetaMapPR instance = new MetaMapPR();
        String expResult = "?ngstr?m";
        String result = instance.filterNonAscii(text);
        assertEquals(expResult, result);
        
    }

   

}
