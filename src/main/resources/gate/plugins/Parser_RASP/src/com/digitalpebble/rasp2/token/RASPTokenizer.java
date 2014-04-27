package com.digitalpebble.rasp2.token;

import gate.Annotation;
import gate.AnnotationSet;
import gate.Factory;
import gate.FeatureMap;
import gate.Gate;
import gate.Resource;
import gate.creole.AbstractLanguageAnalyser;
import gate.creole.ExecutionException;
import gate.creole.ResourceData;
import gate.creole.ResourceInstantiationException;
import gate.util.Files;
import gate.util.InvalidOffsetException;
import gate.util.OffsetComparator;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.digitalpebble.util.StreamReader;
import com.digitalpebble.util.StreamWriter;

/*******************************************************************************
 * Simple tokenizer which is partially compatible with the default GATE one.
 * Generates Tokens which have a single feature 'string'. Should be faster than
 * the default GATE component.
 ******************************************************************************/
public class RASPTokenizer extends AbstractLanguageAnalyser {

	private String inputASName;

	private String outputASName;

	private String charset = "ISO-8859-1";

	private boolean debug = false;

	private String tokenExecutable;

	public Resource init() throws ResourceInstantiationException {
		// check that the tokenizer is in the resource
		// directory
	    ResourceData thisRD =
	      (ResourceData)Gate.getCreoleRegister().get(this.getClass().getName());
	    URL myCreoleXML = thisRD.getXmlFileUrl();
	    if(!"file".equals(myCreoleXML.getProtocol())) {
	      throw new ResourceInstantiationException(
	          "Tokenizer plugin must be loaded from a file: URL");
	    }
	    File myCreoleXMLFile = Files.fileFromURL(myCreoleXML);
	    
		tokenExecutable = myCreoleXMLFile.getParent()+ File.separator + "resources" + File.separator + "tokenise"
				+ File.separator + "token."
				+ com.digitalpebble.util.Utilities.getArch();

		// check that the file exists
		File scriptfile = new File(tokenExecutable);
		if (scriptfile.exists() == false)
			throw new ResourceInstantiationException(new Exception(
					"Executable " + scriptfile.getAbsolutePath()
							+ " does not exist"));
		return super.init();
	}

	public void execute() throws ExecutionException {
		AnnotationSet inputAS = (inputASName == null || inputASName.equals("")) ? document
				.getAnnotations()
				: document.getAnnotations(inputASName);

		AnnotationSet outputAS = (outputASName == null || outputASName
				.equals("")) ? document.getAnnotations() : document
				.getAnnotations(outputASName);

		if (getCharset() != null && Charset.isSupported(getCharset()))
			charset = getCharset();

		File exec = new File(tokenExecutable);
		String[] cmdline = new String[] { exec.toString() };

		// run the lemmer and convert output into annotations
		Process process = null;
		Thread sw = null;
		Thread srt = null;

		File tempSentences;
		try {
			tempSentences = java.io.File.createTempFile("rasp", ".sent");
		} catch (IOException e) {
			throw new ExecutionException(e);
		}

		try {
			// generate an input file for the Tokeniser
			OutputStream fout = new FileOutputStream(tempSentences);
			OutputStreamWriter out = new OutputStreamWriter(fout, charset);
			BufferedWriter writer = new BufferedWriter(out);

			List sentences = new ArrayList(inputAS.get("Sentence"));
			Collections.sort(sentences, new OffsetComparator());
			Iterator iter = sentences.iterator();

			// no sentences?
			// skip the processing
			if (iter.hasNext() == false) {
				System.err.println("RASP Tokenizer needs annotations of type Sentence as input");
				return;
			}

			else
				while (iter.hasNext()) {
					writer.append("^ ");
					Annotation sentence = (Annotation) iter.next();
					String sentenceT = getDocument().getContent().getContent(
							sentence.getStartNode().getOffset(),
							sentence.getEndNode().getOffset()).toString();
					sentenceT = sentenceT.replaceAll("\n", " ");
					writer.append(sentenceT).append("\n");
				}
			writer.close();

			process = Runtime.getRuntime().exec(cmdline);
			// pass the content of the file to the buffer of the process
			// in a different Thread
			FileInputStream tempin = new FileInputStream(tempSentences);
			sw = new Thread(new StreamWriter(tempin, process.getOutputStream()));
			StreamReader sr = new StreamReader(process.getInputStream(),
					charset);
			sw.start();
			// read the information returned by the application
			// in a different Thread
			srt = new Thread(sr);
			srt.start();
			// wait for the process to finish
			process.waitFor();

			if (debug)
				dumpToFile(sr.getBuffer());

			// need to wait for the reader to get everything else
			sw.join();
			srt.join();

			// get the buffer from the StreamReader
			processTokens(outputAS, sr.getBuffer().toString());

			if (!debug)
				tempSentences.delete();

		} catch (Exception err) {
			System.out.println("Problem when calling the executable");
			throw new ExecutionException(err);
		} finally {
			// destroy the process
			process.destroy();
			try {
				sw.join();
				srt.join();
			} catch (InterruptedException e) {
			}
		}

	}

	// each line corresponds to a sentence
	// each token is separated by a space character
	private void processTokens(AnnotationSet out, String buffer)
			throws ExecutionException {
		String originalText = getDocument().getContent().toString();

		String[] lines = buffer.split("\n");
		int lastPos = 0;
		// read output from tokeniser
		for (int i = 0; i < lines.length; i++) {
			String[] splits = lines[i].split(" ");
			// skip the sentence marker
			for (int s = 1; s < splits.length; s++) {
				String target = splits[s];
				// skip empty strings				
				if (target.length()==0) continue;
				int start = originalText.indexOf(target, lastPos);
				int end = start + target.length();
				lastPos = end;
				if (start == -1) {
					throw new ExecutionException(new Exception(
							"Can't match token " + target));
				}
				// create new annotation
				FeatureMap features = Factory.newFeatureMap();
				features.put("string", originalText.substring(start, end));
				features.put("length", originalText.length());
				try {
					out.add(new Long(start), new Long(end), "Token", features);
				} catch (InvalidOffsetException e) {
					throw new ExecutionException(e);
				}
			}
		}
	}

	private final void dumpToFile(StringBuffer buffer) {
		File tempForms;
		try {
			tempForms = java.io.File.createTempFile("rasp", ".tokenoutput");

			BufferedWriter writer = new BufferedWriter(
					new FileWriter(tempForms));
			writer.write(buffer.toString());
			writer.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String getCharset() {
		return charset;
	}

	public void setCharset(String charset) {
		this.charset = charset;
	}

	public Boolean getDebug() {
		return debug;
	}

	public void setDebug(Boolean debug) {
		this.debug = debug;
	}

	public String getInputASName() {
		return inputASName;
	}

	public void setInputASName(String inputASName) {
		this.inputASName = inputASName;
	}

	public String getOutputASName() {
		return outputASName;
	}

	public void setOutputASName(String outputASName) {
		this.outputASName = outputASName;
	}

}