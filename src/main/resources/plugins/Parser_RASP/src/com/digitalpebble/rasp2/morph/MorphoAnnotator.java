package com.digitalpebble.rasp2.morph;

import gate.Annotation;
import gate.AnnotationSet;
import gate.FeatureMap;
import gate.Resource;
import gate.creole.AbstractLanguageAnalyser;
import gate.creole.ExecutionException;
import gate.creole.ResourceInstantiationException;
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
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.digitalpebble.util.StreamReader;
import com.digitalpebble.util.StreamWriter;

/**
 * Takes a document annotated with Sentences and WordForms gets the
 * morphological information. WordForms must have : - a String feature - a POS
 * feature they will get a lemma and a suffix feature
 * 
 * @author julien
 */
public class MorphoAnnotator extends AbstractLanguageAnalyser {

	private String inputASName;

	private String charset = "ISO-8859-1";

	private boolean debug = false;

	private URL raspHome = null;

	// these two values are set up dynamically from the rasphome parameter
	private String lemmatiserExecutable;

	private String morpherRoot;

	public Resource init() throws ResourceInstantiationException {

		if (getRaspHome() == null) {
			throw new ResourceInstantiationException(new Exception(
					"location of rasp not set"));
		}

		morpherRoot = raspHome.getFile() + File.separator + "morph";
		lemmatiserExecutable = "morpha."
				+ com.digitalpebble.util.Utilities.getArch();

		// check that the file exists
		File scriptfile = new File(morpherRoot, lemmatiserExecutable);
		if (scriptfile.exists() == false)
			throw new ResourceInstantiationException(new Exception("Script "
					+ scriptfile.getAbsolutePath() + " does not exist"));
		return super.init();
	}

	public void execute() throws ExecutionException {
		AnnotationSet inputAS = (inputASName == null || inputASName.equals("")) ? document
				.getAnnotations() : document.getAnnotations(inputASName);

		String encoding = getCharset();
		if (encoding != null && Charset.isSupported(encoding))
			charset = encoding;

		// at this stage we know we need to analyse
		// Tokens containing one or more POS tags
		// the first step consists in generating a text representation
		// of the input similar to content of RASP .forms files
		// (which is what the morpher takes as input and not the .tags)

		File tempForms;
		List wordFormList = null;
		try {
			tempForms = java.io.File.createTempFile("rasp", ".forms");
			tempForms.deleteOnExit();
			wordFormList = this.generateInputForMorpher(inputAS, tempForms);
		} catch (Exception e) {
			throw new ExecutionException(e);
		}
		
		// if no word forms then simply skip that step
		if (wordFormList.size()==0){
			throw new ExecutionException("No annotations of WordForms found");
		}
			// the next step consists in calling the morpho scripts
			// and modify the annotations in the CAS accordingly
			callExternalCommand(tempForms, wordFormList);
		
		// delete the input file if not in debug mode
		if (!debug)
			tempForms.delete();
	}

	// We want to generate things like
	// ^ ^_^:1
	// This This_DD1 &rasp_colon;1
	// is is_VBZ &rasp_colon;1
	// a a_ZZ1 &rasp_colon;4.96223e-05 a_II &rasp_colon;0.000225492 a_AT1
	// &rasp_colon;0.999725
	// test test_NN1 &rasp_colon;0.994738 test_VV0 &rasp_colon;0.00526216
	// @returns a list of Token annotations
	private List<Annotation> generateInputForMorpher(AnnotationSet inputAS,
			File outputFile) throws Exception {

		OutputStream fout = new FileOutputStream(outputFile);
		OutputStreamWriter out = new OutputStreamWriter(fout, charset);
		BufferedWriter writer = new BufferedWriter(out);

		Iterator sentences = inputAS.get("Sentence").iterator();
		AnnotationSet wfs = inputAS.get("WordForm");

		List<Annotation> wordforms = new ArrayList<Annotation>(wfs.size());

		// * We generate things like a a_AT1 &rasp_colon;0.999748 a_ZZ1
		// * &rasp_colon;2.77533e-05 a_II &rasp_colon;0.000223815

		while (sentences.hasNext()) {
			writer.append("^ ^_^:1\n");
			Annotation sentence = (Annotation) sentences.next();
			AnnotationSet wfinsentence = wfs.getContained(sentence
					.getStartNode().getOffset(), sentence.getEndNode()
					.getOffset());
			// sort them
			List<Annotation> sortedWordForms = new ArrayList<Annotation>(
					wfinsentence);
			java.util.Collections.sort(sortedWordForms, new OffsetComparator());
			Iterator<Annotation> iter = sortedWordForms.iterator();

			// create a single entry for word forms located at the same position
			Long previousstart = null;
			Long previousend = null;

			boolean isFirst = true;

			while (iter.hasNext()) {
				Annotation a = iter.next();
				FeatureMap fm = a.getFeatures();
				String form = (String) fm.get("string");
				String pos = (String) fm.get("pos");
				Double prob = (Double) fm.get("probability");

				Long laststartoffset = a.getStartNode().getOffset();
				Long lastendoffset = a.getEndNode().getOffset();

				// do we have a new entity?
				if (laststartoffset != previousstart
						|| lastendoffset != previousend) {
					// finish the line
					if (isFirst == false) {
						writer.newLine();
					}
					isFirst = false;
					// dump the form as found in the text
					String formToken = getDocument().getContent().getContent(
							laststartoffset, lastendoffset).toString();
					writer.append(formToken);
				}
				// add the rest anyway
				writer.append(" ").append(form).append("_");
				writer.append(pos).append(" &rasp_colon;");
				writer.append(Double.toString(prob));
				wordforms.add(a);
				previousstart = laststartoffset;
				previousend = lastendoffset;
			}
			writer.newLine();
		}
		writer.flush();
		writer.close();
		return wordforms;
	}

	private void callExternalCommand(File tempToken, List tokenList)
			throws ExecutionException {
		File lemmRoot = new File(this.morpherRoot);
		File lemmer = new File(lemmRoot, this.lemmatiserExecutable);
		// parameters : -actf /usr/local/bin/RASP/verbstem.list
		String[] cmdline = new String[3];
		cmdline[0] = lemmer.getAbsolutePath();
		cmdline[1] = "-actf";
		cmdline[2] = "verbstem.list";

		// run the lemmer and convert output into annotations
		Process process = null;
		Thread sw = null;
		Thread srt = null;

		try {
			process = Runtime.getRuntime().exec(cmdline, null, lemmRoot);
			// pass the content of the file to the buffer of the process
			// in a different Thread
			FileInputStream tempin = new FileInputStream(tempToken);
			sw = new Thread(new StreamWriter(tempin, process.getOutputStream()));
			StreamReader sr = new StreamReader(process.getInputStream());
			sw.start();
			// read the information returned by the application
			// in a different Thread
			srt = new Thread(sr);
			srt.start();
			// wait for the process to finish
			process.waitFor();

			// need to wait for the reader to get everything else
			sw.join();
			srt.join();

			// dump the content of the buffer into a file (for debug)
			if (debug)
				dumpToFile(sr.getBuffer());

			// get the buffer from the StreamReader
			processTokens(sr.getBuffer().toString(), tokenList);

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

	private void processTokens(String buffer, List wordForms)
			throws ExecutionException, InvalidOffsetException {
		String[] lines = buffer.split("\n");

		Iterator<Annotation> iter = wordForms.iterator();

		Pattern splitpattern = Pattern
				.compile(" (.+?)_(.+?) &rasp_colon;(.+?)");

		String line = null;
		// read output from lemmer
		for (int i = 0; i < lines.length; i++) {
			line = lines[i];
			// skip sentence separators
			if (line.startsWith("^ ^_^"))
				continue;
			if (iter.hasNext() == false) {
				// out of sync
				throw new ExecutionException(
						"Impossible to synchronise tokens with output of POS tagger");
			}

			// extract information about lemmas and suffix
			// e.g. a a_AT1 &rasp_colon;0.997075 a_ZZ1 &rasp_colon;0.0022923
			// a_II &rasp_colon;0.00063225
			// and store it in the WordForms

			// detect patterns in the line
			Matcher match = splitpattern.matcher(line);
			while (match.find()) {
				// we want match 1
				String lemma = match.group(1);
				String suffix = "";
				// separate the lemma from the suffix
				int suffix_offset = lemma.lastIndexOf("+");
				if (suffix_offset > 0) {
					suffix = lemma.substring(suffix_offset);
					lemma = lemma.substring(0, suffix_offset);
				}
				Annotation wf = iter.next();
				wf.getFeatures().put("lemma", lemma);
				wf.getFeatures().put("suffix", suffix);
			}
		}
		// more tokens available?
		if (iter.hasNext()) {
			// out of synch
			throw new ExecutionException(
					"Impossible to synchronise tokens with output of POS tagger");
		}

	}

	private final void dumpToFile(StringBuffer buffer) {
		File tempForms;
		try {
			tempForms = java.io.File.createTempFile("rasp", ".lemmas");

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
		return new Boolean(debug);
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

	public URL getRaspHome() {
		return raspHome;
	}

	public void setRaspHome(URL raspHome) {
		this.raspHome = raspHome;
	}

}
