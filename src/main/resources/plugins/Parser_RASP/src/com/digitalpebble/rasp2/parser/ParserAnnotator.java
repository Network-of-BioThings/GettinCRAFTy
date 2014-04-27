package com.digitalpebble.rasp2.parser;

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
import java.io.StringReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import com.digitalpebble.util.StreamReader;
import com.digitalpebble.util.StreamWriter;
import com.digitalpebble.util.Utilities;

/**
 * Wrapper for the RASP Parser, which generates dependencies between Tokens but
 * also a syntactic analysis. This annotator requires a preliminary analysis
 * with the POS tagger and morphological analyser
 */
public class ParserAnnotator extends AbstractLanguageAnalyser {

	// static String[] outputparamvalues = new String[] { "-oa", "-ot", "-og",
	// 		"-otg", "-ogio", "-ogw", "-otgio" };

	static String[] outputparamvalues = new String[] {"-og","-ogio", "-ogw" };
	
	// parameters specified via the GUI

	// one of the values specified above
	private String outputFormat;

	private Integer parseNum;

	private Integer time;

	private Boolean subcategorisation;

	private Boolean phrasalVerbs;

	private String inputASName;

	private String outputASName;

	private String charset = "ISO-8859-1";

	private boolean debug = false;

	private URL raspHome = null;

	// built from the value of rasphome
	private String parserScript;

	DocumentBuilder builder;

	/**
	 * Send each sentence to the parser instead of the entire document Can be
	 * slower but is also safer and avoids memory issues. -1 indicates that the
	 * entire document has to be sent in one batch, 1 or more indicates that n
	 * sentences have to be sent TODO add an option to modify that
	 */
	private Integer sentenceBatch = new Integer(-1);

	public Resource init() throws ResourceInstantiationException {

		if (getRaspHome() == null) {
			throw new ResourceInstantiationException(new Exception(
					"location of rasp not set"));
		}

		parserScript = getRaspHome().getFile() + File.separator + "scripts"
				+ File.separator + "rasp_parse.sh";

		// check that the file exists
		File scriptfile = new File(parserScript);
		if (scriptfile.exists() == false)
			throw new ResourceInstantiationException(new Exception("Script "
					+ scriptfile.getAbsolutePath() + " does not exist"));

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		try {
			builder = factory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			throw new ResourceInstantiationException(e);
		}

		return super.init();
	}

	public void execute() throws ExecutionException {
		AnnotationSet inputAS = (inputASName == null || inputASName.equals("")) ? document
				.getAnnotations() : document.getAnnotations(inputASName);

		AnnotationSet outputAS = (outputASName == null || outputASName.equals("")) ? document
				.getAnnotations() : document.getAnnotations(outputASName);

		boolean noWordForms = inputAS.get("WordForm").isEmpty();		
				
		if (noWordForms){
			System.err.println("RASP Parser needs annotations of type WordForm - skipping document");
		}
		
		String encoding = getCharset();
		if (encoding != null && Charset.isSupported(encoding))
			charset = encoding;

		// check that the output format is in the list
		boolean inList = java.util.Arrays.asList(outputparamvalues).contains(
				getOutputFormat());
		if (inList == false) {
			// unknown format -> ignored
			throw new ExecutionException("Value of output param not allowed ["
					+ getOutputFormat() + "]");
		}
		// do we call the parser for each sentence
		// or the whole document
		if (sentenceBatch == 1) {
			processBySentence(inputAS,outputAS);
		} else
			processAll(inputAS,outputAS);
	}

	public void processAll(AnnotationSet input, AnnotationSet output) throws ExecutionException {
		File tempForms;
		try {
			tempForms = java.io.File.createTempFile("rasp", ".data");
			this.generateInputForParser(input, tempForms, -1);
		} catch (Exception e) {
			throw new ExecutionException(e);
		}
		// the next step consists in calling the morpho scripts
		// and modify the annotations in the CAS accordingly
		ParserXMLoutputAnalyser parser = new ParserXMLoutputAnalyser(input,output);
		callExternalCommand(parser, tempForms, false);

		if (!debug)
			tempForms.delete();
	}

	public void processBySentence(AnnotationSet input, AnnotationSet output)
			throws ExecutionException {
		ParserXMLoutputAnalyser parser = new ParserXMLoutputAnalyser(input,output);

		List sents = new ArrayList(input.get("Sentence"));
		java.util.Collections.sort(sents, new OffsetComparator());
		Iterator sentenceIterator = sents.iterator();
		int sentNum = 0;
		while (sentenceIterator.hasNext()) {
			sentNum++;
			sentenceIterator.next();
			File tempForms;
			try {
				tempForms = java.io.File.createTempFile("rasp", ".data_"
						+ sentNum);
				this.generateInputForParser(input, tempForms, sentNum);
			} catch (Exception e) {
				throw new ExecutionException(e);
			}
			// the next step consists in calling the morpho scripts
			// and modify the annotations in the CAS accordingly
			callExternalCommand(parser, tempForms, true);

			if (!debug)
				tempForms.delete();
		}
	}

	// We want to generate things like
	// ^ ^_^:1
	// This This_DD1 &rasp_colon;1
	// is is_VBZ &rasp_colon;1
	// a a_ZZ1 &rasp_colon;4.96223e-05 a_II &rasp_colon;0.000225492 a_AT1
	// &rasp_colon;0.999725
	// test test_NN1 &rasp_colon;0.994738 test_VV0 &rasp_colon;0.00526216
	// @returns a list of Token annotations
	// @param sentenceNumber = number of the sentence to generate; -1 to
	// generate them all
	private List<Annotation> generateInputForParser(AnnotationSet inputAS,
			File outputFile, int sentenceNumber) throws IOException,
			InvalidOffsetException {
		OutputStream fout = new FileOutputStream(outputFile);
		OutputStreamWriter out = new OutputStreamWriter(fout, charset);
		BufferedWriter writer = new BufferedWriter(out);

		// for each sentence dump the information about the WordForms for each
		// token
		List sents = new ArrayList(inputAS.get("Sentence"));
		java.util.Collections.sort(sents, new OffsetComparator());
		Iterator sentenceIterator = sents.iterator();

		AnnotationSet wfs = inputAS.get("WordForm");

		List<Annotation> rewordforms = new ArrayList<Annotation>(wfs.size());

		// AnnotationIndex wordformAnnotationIndex =
		// cas.getAnnotationIndex(WordForm.type);
		// FSIterator wordFormIterator = wordformAnnotationIndex.iterator();
		int sentNum = 0;
		while (sentenceIterator.hasNext()) {
			sentNum++;
			Annotation sentence = (Annotation) sentenceIterator.next();
			// check that this is the one we want
			if (sentenceNumber != -1) {
				if (sentNum < sentenceNumber) {
					continue;
				}
				if (sentNum > sentenceNumber) {
					break;
				}
			}
			writer.append("^ ^_^:1\n");

			// get the Tokens (or word forms?) located under that sentence

			ArrayList wordForms = new ArrayList(wfs.getContained(sentence
					.getStartNode().getOffset(), sentence.getEndNode()
					.getOffset()));
			java.util.Collections.sort(wordForms, new OffsetComparator());
			Iterator<Annotation> iter = wordForms.iterator();

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
				String lemma = (String) fm.get("lemma");
				String suffix = (String) fm.get("suffix");

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
				writer.append(" ");
				writer.append("<w id=\"" + a.getId()).append("\">");
				writer.append(lemma);
				writer.append(suffix).append("_").append(pos);
				writer.append("</w>");
				writer.append(":" + Double.toString(prob));

				rewordforms.add(a);
				previousstart = laststartoffset;
				previousend = lastendoffset;
			}
			writer.newLine();
		}
		writer.flush();
		writer.close();
		return rewordforms;
	}

	private void callExternalCommand(ParserXMLoutputAnalyser parser,
			File inputFile, boolean perSentence) {
		File RASPexec = new File(this.parserScript);
		
		List params = new ArrayList();
		if (time!=null){
			params.add( "-t");
			params.add(this.time.toString());
		}
		
		if (this.parseNum!=null){
			params.add( "-n");
			params.add(this.parseNum.toString());
		}
		
		params.add(outputFormat);
		
		// build the command line
		String[] parameters = new String[params.size()];
		parameters = (String[]) params.toArray(parameters);

		int lengthParams = parameters.length;

		// subcategorisation
		if (subcategorisation.booleanValue() == true)
			lengthParams++;

		// no phrasal verbs?
		if (phrasalVerbs.booleanValue() == false)
			lengthParams++;

		// need to add the executable + specify XML format
		String[] cmdline = new String[lengthParams + 2];
		System.arraycopy(parameters, 0, cmdline, 1, parameters.length);
		cmdline[0] = RASPexec.getAbsolutePath();

		if (subcategorisation.booleanValue() == true)
			cmdline[parameters.length + 1] = "-s";

		if (phrasalVerbs.booleanValue() == false)
			cmdline[parameters.length + 2] = "-x";

		cmdline[cmdline.length - 1] = "-y";

		// run the lemmer and convert output into annotations
		Process process = null;
		Thread sw = null;
		Thread srt = null;
		try {
			process = Runtime.getRuntime().exec(cmdline);
			// pass the content of the file to the buffer of the process
			// in a different Thread
			FileInputStream tempin = new FileInputStream(inputFile);
			sw = new Thread(new StreamWriter(tempin, process.getOutputStream()));

			StreamReader sr = new StreamReader(process.getInputStream(),
					this.charset);
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
			String xmloutput = sr.getBuffer().toString();

			// updates the cas with the information contained in the XML
			if (!perSentence) {
				Document domDoc = this.builder.parse(new InputSource(
						new StringReader(xmloutput)));
				parser.parseRASPOutput(domDoc);
			} else {
				parser.parseRASPOutputSingleSentence(xmloutput, builder);
			}
		} catch (Exception err) {
			// System.out.println("Problem when calling the executable");
			// throw new AnalysisEngineProcessException(err);
			// Just log the exception message and continue with the annotation
			// of the documents
			String message = "Exception thrown in ParserAnnotator: "
					+ err.getMessage();
			System.err.println(message);
			// copy the input file under a different name so that
			// we can trace the problem
			File errorFile = new File(inputFile.getAbsolutePath() + ".error");
			try {
				errorFile.createNewFile();
				Utilities.copyFile(inputFile, errorFile);
			} catch (IOException e) {
			}

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

	private final void dumpToFile(StringBuffer buffer) {
		File tempForms;
		try {
			tempForms = java.io.File.createTempFile("rasp", ".parse");

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

	public String getInputASName() {
		return inputASName;
	}

	public void setInputASName(String inputASName) {
		this.inputASName = inputASName;
	}

	public String getOutputFormat() {
		return outputFormat;
	}

	public void setOutputFormat(String outputFormat) {
		this.outputFormat = outputFormat;
	}

	public Integer getParseNum() {
		return parseNum;
	}

	public void setParseNum(Integer parseNum) {
		this.parseNum = parseNum;
	}

	public Boolean getPhrasalVerbs() {
		return phrasalVerbs;
	}

	public void setPhrasalVerbs(Boolean phrasalVerbs) {
		this.phrasalVerbs = phrasalVerbs;
	}

	public URL getRaspHome() {
		return raspHome;
	}

	public void setRaspHome(URL raspHome) {
		this.raspHome = raspHome;
	}

	public Boolean getSubcategorisation() {
		return subcategorisation;
	}

	public void setSubcategorisation(Boolean subcategorisation) {
		this.subcategorisation = subcategorisation;
	}

	public Integer getTime() {
		return time;
	}

	public void setTime(Integer time) {
		this.time = time;
	}

	public void setDebug(Boolean debug) {
		this.debug = debug;
	}

	public Boolean getDebug() {
		return debug;
	}

	public String getOutputASName() {
		return outputASName;
	}

	public void setOutputASName(String outputASName) {
		this.outputASName = outputASName;
	}

}
