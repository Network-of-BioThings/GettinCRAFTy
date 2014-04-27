package com.digitalpebble.rasp2.parser;

import gate.Annotation;
import gate.AnnotationSet;
import gate.Factory;
import gate.FeatureMap;
import gate.util.OffsetComparator;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class ParserXMLoutputAnalyser {
	
	private AnnotationSet outputAS;
	
	Iterator sentenceIterator;

	AnnotationSet wordformAS;

	ParserXMLoutputAnalyser(AnnotationSet in, AnnotationSet out) {
		this.outputAS = out;
		List sents = new ArrayList(in.get("Sentence"));
		java.util.Collections.sort(sents, new OffsetComparator());
		sentenceIterator = sents.iterator();
		wordformAS = in.get("WordForm");
	}

	// we have obtained a RASP XML output which we will parse
	public void parseRASPOutput(Document rasp) throws Exception {
		NodeList nl = rasp.getElementsByTagName("sentence");
		for (int i = 0; i < nl.getLength(); i++) {
			Element sentenceElement = (Element) nl.item(i);
			Annotation  sentence = (Annotation) sentenceIterator.next();
			parseRASPSentence(sentenceElement, sentence);
		}
	}

	/***************************************************************************
	 * Parse a specific sentence - this is used when the sentences are sent to
	 * the parser one by one
	 **************************************************************************/
	public void parseRASPOutputSingleSentence(String input,
			DocumentBuilder builder) throws Exception {
		// we get the next sentence in any case - even if this one crashes we want 
		// to be able to process the next one
		Annotation  sentence = (Annotation) sentenceIterator.next();
		Document rasp = builder.parse(new InputSource(new StringReader(input)));
		NodeList nl = rasp.getElementsByTagName("sentence");
		Element sentenceElement = (Element) nl.item(0);
		parseRASPSentence(sentenceElement, sentence);
	}

	private void parseRASPSentence(Element sentence, Annotation sentenceAnn)
			throws Exception {
		// matches the lemmas returned by the parser with
		// original annotations in cas
		HashMap<String, Annotation> mappingIntegerAnnotation = new HashMap<String, Annotation>();
		HashMap<String, Annotation> tempmappingUIMAidAnnotation = new HashMap<String, Annotation>();
		// for this sentence => get all the wordForms
		// and put them in the map mappingIntegerAnnotation
		Iterator allwf = wordformAS.getContained(sentenceAnn.getStartNode().getOffset(), sentenceAnn.getEndNode().getOffset()).iterator();
		while (allwf.hasNext()) {
			Annotation wordForm = (Annotation) allwf.next();
				tempmappingUIMAidAnnotation.put(Integer.toString(wordForm
						.getId()), wordForm);
		}
		NodeList nl = sentence.getElementsByTagName("lemma-list");
		if (nl.getLength() > 0) {
			// get all the lemma elements
			NodeList lemmalist = sentence.getElementsByTagName("lemma");
			for (int lemmanum = 0; lemmalist != null
					&& lemmanum < lemmalist.getLength(); lemmanum++) {
				Element lemma = (Element) lemmalist.item(lemmanum);
				// the the value of the attributes num and wtag
				String num = lemma.getAttribute("num");
				String wtag = lemma.getAttribute("wtag");
				wtag = wtag.substring(7);
				int pos = wtag.indexOf("\"");
				wtag = wtag.substring(0, pos);
				// get the corresponding annotation from the cas
				Annotation wf = tempmappingUIMAidAnnotation.get(wtag);
				if (wf == null)
					throw new Exception("No annotation found for "
							+ lemma.toString());
				mappingIntegerAnnotation.put(num, wf);
			}
		}
		// now that we have the mappings between the numbers returned by RASP
		// and the original annotations we can analyse the information about the
		// dependencies
		NodeList grl = sentence.getElementsByTagName("gr-list");
		if (grl.getLength() > 0) {
			// get all the gr elements elements
			NodeList grlist = sentence.getElementsByTagName("gr");
			for (int grnum = 0; grlist != null && grnum < grlist.getLength(); grnum++) {
				Element gr = (Element) grlist.item(grnum);
				String grtype = gr.getAttribute("type");
				String grsubtype = gr.getAttribute("subtype");
				// TODO fix problem in RASP instead of patching output
				if (grsubtype.startsWith("<w id="))
					grsubtype = "";
				String head = gr.getAttribute("head");
				String dep = gr.getAttribute("dep");
				Annotation wfhead = mappingIntegerAnnotation.get(head);
				Annotation wfdep = mappingIntegerAnnotation.get(dep);
				if (wfdep == null && wfhead == null)
					throw new Exception("No head and no dep for this relation");
				// some relations such as 'passive' don't have a dep
				if (wfdep == null) {
					FeatureMap fm = Factory.newFeatureMap();
					fm.put("head", wfhead);
					fm.put("type", grtype);
					fm.put("subtype", grsubtype);
					outputAS.add(wfhead.getStartNode().getOffset(), wfhead.getEndNode().getOffset(), "Dependency", fm);
					continue;
				}
				// other relations such as 'ta' don't have a head
				if (wfhead == null) {
					FeatureMap fm = Factory.newFeatureMap();
					fm.put("dep", wfdep);
					fm.put("type", grtype);
					fm.put("subtype", grsubtype);
					outputAS.add(wfdep.getStartNode().getOffset(), wfdep.getEndNode().getOffset(), "Dependency", fm);
					continue;
				}
				// create a new annotation of type Dependency
				// and add it to the cas
				Long startDependency = wfhead.getStartNode().getOffset();
				Long endDependency = wfdep.getEndNode().getOffset();
				if (wfdep.getStartNode().getOffset() < startDependency) {
					startDependency = wfdep.getStartNode().getOffset();
					endDependency = wfhead.getEndNode().getOffset();
				}
				FeatureMap fm = Factory.newFeatureMap();
				fm.put("dep", wfdep);
				fm.put("head", wfhead);
				fm.put("type", grtype);
				fm.put("subtype", grsubtype);
				outputAS.add(startDependency, endDependency, "Dependency", fm);
			}
		}
		
	}

	
}
