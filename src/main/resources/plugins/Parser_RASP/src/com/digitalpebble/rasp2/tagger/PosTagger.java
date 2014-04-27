package com.digitalpebble.rasp2.tagger;

import gate.Annotation;
import gate.AnnotationSet;
import gate.Factory;
import gate.FeatureMap;
import gate.Resource;
import gate.creole.AbstractLanguageAnalyser;
import gate.creole.ExecutionException;
import gate.creole.ResourceInstantiationException;
import gate.util.BomStrippingInputStreamReader;
import gate.util.OffsetComparator;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import com.digitalpebble.util.Utilities;

public class PosTagger extends AbstractLanguageAnalyser {

	private Boolean generateMultipleTags = true;

	private String inputASName;

	private String outputASName;

	private String charset = "ISO-8859-1";

	private Boolean debug = false;

	private URL raspHome = null;

	// parameters passed to the native POS tagger
	// no need to modify that
	private final String parametersString = "B1 b C1 N t auxiliary_files/slb.trn d auxiliary_files/seclarge.lex j auxiliary_files/unkstats-seclarge m auxiliary_files/tags.map";

	// built from the value of rasphome
	private String RASPTaggerRoot;

	private String POSexecutable;

	public Resource init() throws ResourceInstantiationException {

		if (getRaspHome() == null) {
			throw new ResourceInstantiationException(new Exception(
					"location of rasp not set"));
		}

		RASPTaggerRoot = getRaspHome().getFile() + File.separator + "tag";
		POSexecutable = com.digitalpebble.util.Utilities.getArch()
				+ File.separator + "label";

		// check that the file exists
		File scriptfile = new File(RASPTaggerRoot, POSexecutable);
		if (scriptfile.exists() == false)
			throw new ResourceInstantiationException(new Exception(
					"POS tagger executable " + scriptfile.getAbsolutePath()
							+ " does not exist"));
		return this;
	}

	public void execute() throws ExecutionException {
		AnnotationSet inputAS = (inputASName == null || inputASName.equals("")) ? document
				.getAnnotations()
				: document.getAnnotations(inputASName);

		AnnotationSet outputAS = (outputASName == null || outputASName
				.equals("")) ? document.getAnnotations() : document
				.getAnnotations(outputASName);

		String encoding = System.getProperty("rasp.tagger.charset");
		// check that it is an authorised charset name
		if (encoding != null && Charset.isSupported(encoding))
			charset = encoding;

		File tempToken = null;

		// create a temp file
		try {
			tempToken = java.io.File.createTempFile("rasp", ".token");
		} catch (IOException e) {
			throw new ExecutionException(e);
		}
		// create a writer for the token file
		BufferedWriter tokenWriter;
		try {
			OutputStream fout = new FileOutputStream(tempToken);
			OutputStreamWriter out = new OutputStreamWriter(fout, charset);
			tokenWriter = new BufferedWriter(out);
		} catch (IOException e) {
			throw new ExecutionException(e);
		}

		// iterate on the sentences
		List sents = new ArrayList(inputAS.get("Sentence"));
		java.util.Collections.sort(sents, new OffsetComparator());
		Iterator sentenceIterator = sents.iterator();

		AnnotationSet tokens = inputAS.get("Token");

		List<Annotation> tokenEntities = new ArrayList<Annotation>(tokens.size());

		try {
			while (sentenceIterator.hasNext()) {
				Annotation sentence = (Annotation) sentenceIterator.next();
				tokenWriter.append("^\n");
				// get the Tokens (or word forms?) located under that sentence

				ArrayList toks = new ArrayList(tokens.getContained(sentence
						.getStartNode().getOffset(), sentence.getEndNode()
						.getOffset()));
				java.util.Collections.sort(toks, new OffsetComparator());
				Iterator<Annotation> iter = toks.iterator();

				while (iter.hasNext()) {
					Annotation token = (Annotation) iter.next();
					String form = (String) token.getFeatures().get("string");
					form = Utilities.detectNonISO1Characters(form);
					tokenWriter.append("<w>").append(form).append("</w>\n");
					tokenEntities.add(token);
				}
			}
			// close the streams
			tokenWriter.close();
		} catch (IOException e) {
			File errorFile = new File(tempToken.getAbsolutePath() + ".error");
			try {
				errorFile.createNewFile();
				Utilities.copyFile(tempToken, errorFile);
			} catch (IOException e2) {
			}
			throw new ExecutionException(e);
		}
		// call the POS tagger on the input file
		// and convert the output file back into Tokens
		callExternalCommand(tempToken, tokenEntities,outputAS);

		if (!debug)
			tempToken.delete();
	}

	private void callExternalCommand(File tempToken, List tokenList, AnnotationSet outputAS)
			throws ExecutionException {
		File POSroot = new File(this.RASPTaggerRoot);
		File POSexec = new File(POSroot, POSexecutable);
		File tempPOS;
		try {
			tempPOS = java.io.File.createTempFile("rasp", ".pos");
			tempPOS.deleteOnExit();
		} catch (IOException err) {
			throw new ExecutionException(err);
		}
		// build the command line
		String[] parameters = this.parametersString.split(" ");
		// need to add the executable + the source file + format + output file
		String[] cmdline = new String[parameters.length + 4];
		System.arraycopy(parameters, 0, cmdline, 2, parameters.length);
		cmdline[0] = POSexec.getAbsolutePath();
		cmdline[1] = tempToken.getAbsolutePath();
		String format = "O36";
		if (this.generateMultipleTags)
			format = "O60";
		cmdline[cmdline.length - 2] = format;
		cmdline[cmdline.length - 1] = "o" + tempPOS.getAbsolutePath();

		Iterator iter = tokenList.iterator();
		Process p = null;
		// run tagger
		try {
			p = Runtime.getRuntime().exec(cmdline, null, POSroot);
			p.waitFor();
		} catch (Exception err) {
			throw new ExecutionException(err);
		} finally {
			p.destroy();
		}

		// read from the POS temporary file
		try {
			BufferedReader input = new BomStrippingInputStreamReader(new FileInputStream(
					tempPOS), charset);
			String line = null;
			while ((line = input.readLine()) != null) {
				// ignore sentence markers
				if (line.startsWith("^ ^"))
					continue;
				if (iter.hasNext() == false) {
					// out of synch
					throw new ExecutionException(
							"Impossible to synchronise tokens with output of POS tagger");
				}
				Annotation currentToken = (Annotation) iter.next();
				String currentTokenString = (String) currentToken.getFeatures().get("string");
				// if we generate multiple tags we get things such as
				// <w>in fact</w> JJ:0.949046[*+] NN1:0.0509544
				// isolate the form
				int sepform = line.indexOf("</w>");
				line = line.substring(sepform + 4);
				String[] splits = line.split(" ");

				int wfNum = 0;

				for (int i = 0; i < splits.length; i++) {
					if (splits[i].length() == 0)
						continue;
					if (splits[i].endsWith("[*+]")) {
						// found the best form
						splits[i] = splits[i].substring(0,
								splits[i].length() - 4);
					}
					int sep = splits[i].lastIndexOf(":");
					// case where we get just a single tag
					// <w>Virus</w> NN1 or even <w>:</w> :
					String pos = null;
					String score = null;
					if (this.generateMultipleTags) {
						pos = splits[i].substring(0, sep);
						score = splits[i].substring(sep + 1);
					} else {
						pos = splits[i];
						score = "1.0";
					}
					// create a new instance of Form
					// add it to the annotationset with the same position as the original
					// Token

					FeatureMap features = Factory.newFeatureMap();
					features.put("string", currentTokenString);
					features.put("probability", Double.parseDouble(score));
					features.put("pos", pos);

					outputAS.add(currentToken.getStartNode().getOffset(), currentToken.getEndNode().getOffset(), "WordForm", features);
					wfNum++;
				}

			}
			// more tokens available?
			if (iter.hasNext()) {
				// out of synch
				throw new ExecutionException(
						"Impossible to synchronise tokens with output of POS tagger");
			}
		} catch (Exception err) {
			System.out.println("Problem when reading from POS file");

			throw new ExecutionException(err);
		} finally {
			if (!debug)
				tempPOS.delete();
		}
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

	public URL getRaspHome() {
		return raspHome;
	}

	public void setRaspHome(URL raspHome) {
		this.raspHome = raspHome;
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

	public Boolean getGenerateMultipleTags() {
		return generateMultipleTags;
	}

	public void setGenerateMultipleTags(Boolean generateMultipleTags) {
		this.generateMultipleTags = generateMultipleTags;
	}

}

class WFComparator implements Comparator {

	// compare two WordForms on the basis of their probabilities
	// otherwise use their tag
	public int compare(Annotation arg0, Annotation arg1) {
		if (arg0.getType().equals("WordForm")==false) throw new RuntimeException("WFComparator should have annotations of type WordForm - found "+arg0.getType());
		if (arg1.getType().equals("WordForm")==false) throw new RuntimeException("WFComparator should have annotations of type WordForm - found "+arg1.getType());

		FeatureMap fm1 = arg0.getFeatures();
		FeatureMap fm2 = arg1.getFeatures();

		Double pro1 = (Double) fm1.get("probability");
		Double pro2 = (Double) fm2.get("probability");

		String pos1 = (String) fm1.get("pos");
		String pos2 = (String) fm2.get("pos");

		double diff = pro1 - pro2;
		if (diff < 0)
			return -1;
		else if (diff > 0)
			return 1;
		return pos1.compareTo(pos2);
	}

	public int compare(Object arg0, Object arg1) {
		return compare((Annotation) arg0, (Annotation) arg1);
	}

}
