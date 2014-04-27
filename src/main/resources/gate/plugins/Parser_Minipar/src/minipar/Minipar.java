/*
 *  Minipar.java
 *
 *  Copyright (c) 1995-2012, The University of Sheffield. See the file
 *  COPYRIGHT.txt in the software or at http://gate.ac.uk/gate/COPYRIGHT.txt
 *
 *  This file is part of GATE (see http://gate.ac.uk/), and is free
 *  software, licenced under the GNU Library General Public License,
 *  Version 2, June 1991 (in the distribution as file licence.html,
 *  and also available at http://gate.ac.uk/gate/licence.html).
 *
 *  Niraj Aswani
 *
 *  $Id: Minipar.java 15334 2012-02-07 13:57:47Z ian_roberts $
 */

package minipar;

import java.io.*;
import java.util.*;
import java.net.*;

import org.apache.log4j.Logger;

import gate.*;
import gate.creole.*;
import gate.util.*;
import gate.event.*;
import gate.gui.MainFrame;

/**
 * This class is the implementation of the resource Minipar
 */
public class Minipar extends AbstractLanguageAnalyser implements
		ProcessingResource {

  /**
   * Shared Log4J logger.
   */
  private static final Logger log = Logger.getLogger(Minipar.class);
  
	/**
	 * Name of the temporary file, which is populated with the text of GATE
	 * document in order to process it with Minipar
	 */
	public static final String GATETEXTFILE = "GATESentences";

  /**
   * The Minipar executable limits the length of a sentence to 1024 characters.
   * If a sentence is longer than the maximal length it is not sent to MINIPAR7
   */
  public static final long maxSentenceLength = 1024;

	/**
	 * URL of the minipar Data Directory that contains lexicons etc.
	 */
	private URL miniparDataDir;

	/**
	 * URL of the minipar Binary file, it can be either exe or linux,
	 * distributed along with Minipar in GATE.
	 */
	private URL miniparBinary;
	
	/**
	 * Minipar creates tokens. This variable provides the type name for it.
	 */
	private String annotationTypeName;

	/**
	 * Name of the inputAnnotationSet
	 */
	private String annotationInputSetName;

	/**
	 * Name of the outputAnnotationSet
	 */
	private String annotationOutputSetName;

	/**
	 * Get the MiniparDataDir value.
	 */
	public URL getMiniparDataDir() {
		return miniparDataDir;
	}

	/**
	 * Get the AnnotationTypeName, new annotations are created with this name
	 */
	public String getAnnotationTypeName() {
		return this.annotationTypeName;
	}

	/**
	 * Set the AnnotationTypeName, new annotations are created with this name
	 */
	public void setAnnotationTypeName(String aTypeName) {
		this.annotationTypeName = aTypeName;
	}

	/**
	 * Get the AnnotationInputSetName, source of the annotations to be taken
	 * from and to work on
	 */
	public String getAnnotationInputSetName() {
		return this.annotationInputSetName;
	}

	/**
	 * Get the AnnotationOutputSetName, annotations to be created under this
	 * annotationSet
	 */
	public String getAnnotationOutputSetName() {
		return this.annotationOutputSetName;
	}

	/**
	 * Get the AnnotationInputSetName, source of the annotations to be taken
	 * from and to work on
	 */
	public void setAnnotationInputSetName(String inputSet) {
		this.annotationInputSetName = inputSet;
	}

	/**
	 * Get the AnnotationOutputSetName, annotations to be created under this
	 * annotationSet
	 */
	public void setAnnotationOutputSetName(String outputSet) {
		this.annotationOutputSetName = outputSet;
	}

	/**
	 * Set the MiniparDataDirectory.. This is the directory that Minipar uses to
	 * collect the data for its internal processing. Default location is
	 * minipar_home/data
	 */
	public void setMiniparDataDir(URL newMiniparDataDir) {
		this.miniparDataDir = newMiniparDataDir;
	}

	/**
	 * This is the url of MiniparBinary It should be somewhere located on the
	 * drive where the user has execution rights
	 */
	public URL getMiniparBinary() {
		return miniparBinary;
	}

	/**
	 * This is the url of MiniparBinary It should be somewhere located on the
	 * drive where the user has execution rights
	 */
	public void setMiniparBinary(URL newMiniparBinary) {
		this.miniparBinary = newMiniparBinary;
	}

	/**
	 * Init method is called when the resource is instantiated in GATE. This
	 * checks for the supported operating systems and the mandotary init-time
	 * parameters.
	 */
	public Resource init() throws ResourceInstantiationException {
		// we need to check the operating system
		// And to detect the underlying Operating system
		String osName = System.getProperty("os.name").toLowerCase();
		// Detecting Linux and Windows
		if (osName.toLowerCase().indexOf("linux") == -1
				&& osName.toLowerCase().indexOf("windows") == -1) {
			throw new ResourceInstantiationException(
					"This PR can only be instantiated on Windows/Linux Machine");
		}
		return super.init();
	}

	/**
	 * Minipar Binary file takes a file as an argument, which has one sentence
	 * written on one line. It takes one sentence at a time and parses them one
	 * by one.
	 *
	 * @return The list containing annotations of type *Sentence*
	 * @throws ExecutionException
	 */
	private ArrayList saveGateSentences(File gateTextFile) throws ExecutionException {

		AnnotationSet allAnnotations;

		// get sentences from document
		allAnnotations = (annotationInputSetName == null || annotationInputSetName
				.equals("")) ? document.getAnnotations() : document
				.getAnnotations(annotationInputSetName);

		if (allAnnotations == null || allAnnotations.size() == 0) {
			throw new ExecutionException(
					"Document doesn't have sentence annotations. "
							+ "please run tokenizer, sentence splitter "
							+ "and then Minipar");
		}

		// Get all "sentences"
		List sentences = new ArrayList((Set) allAnnotations
				.get(SENTENCE_ANNOTATION_TYPE));

		// create an ArrayList to hold all sentences
		ArrayList allSentences = new ArrayList(sentences);

		// sort all sentences by start offset
		Collections.sort(allSentences, new gate.util.OffsetComparator());

    // only the sentences shorter than maxSentenceLength are sent to Minipar
    ArrayList sentencesSent =  new ArrayList(allSentences.size());

		// save sentence strings to file for Minipar
		ListIterator sentenceIterator = allSentences.listIterator();
		try {
			String sentenceString = null;
			FileWriter fw = new FileWriter(gateTextFile);
			PrintWriter pw = new PrintWriter(fw, true);
			while (sentenceIterator.hasNext()) {
				Annotation sentence = (Annotation) sentenceIterator.next();
				sentenceString = null;
        Long startOffset = sentence.getStartNode().getOffset();
        Long endOffset = sentence.getEndNode().getOffset();
        // check that the length is inferior to the limit
        long length = endOffset.longValue()-startOffset.longValue();
        if (length>=maxSentenceLength) continue;
				try {
					sentenceString = getDocument().getContent().getContent(
                  startOffset,endOffset).toString();
				} catch (InvalidOffsetException ioe) {
					throw new GateRuntimeException(
							"Invalid offset of the annotation");
				}
				sentenceString = sentenceString.replaceAll("\r", " ");
				sentenceString = sentenceString.replaceAll("\n", " ");
				pw.println(sentenceString);
        sentencesSent.add(sentence);
			}
			fw.close();
		} catch (java.io.IOException except) {
			throw new ExecutionException(except);
		}

		return sentencesSent;
	}

	/**
	 * Core of the wrapper. GATETEXTFILE is processed with Minipar. Minipar
	 * given a text file, binary executable and the location of data directory,
	 * returns a parse, which is then mapped over the GATE document.
	 *
	 * @param allSentences -
	 *            GATE sentence annotations
	 * @throws ExecutionException
	 */
	private void runMinipar(ArrayList allSentences, File gateTextFile) throws ExecutionException {

		// this should be the miniparBinary + "-p " + getMiniparDataDir +
		// GATETEXTFILE
		File binary = Files.fileFromURL(getMiniparBinary());
		File dataFile = Files.fileFromURL(getMiniparDataDir());
		//String cmdline = binary.getAbsolutePath() + " -p "
		//		+ dataFile.getAbsolutePath() + " -file "
		//		+ gateTextFile.getAbsolutePath();
		String[] cmdline = new String[5];
		cmdline[0] = binary.getAbsolutePath();
		cmdline[1] = "-p";
		cmdline[2] = dataFile.getAbsolutePath();
		cmdline[3] = "-file";
		cmdline[4] = gateTextFile.getAbsolutePath();
		// run minipar and save output
		try {
			String line;
			Process p = Runtime.getRuntime().exec(cmdline);
			BufferedReader input = new BomStrippingInputStreamReader(p.getInputStream());
			final BufferedReader err = new BomStrippingInputStreamReader(p.getErrorStream());
			new Thread(new Runnable(){
			  public void run() {
			    try {
            String line = null;
            while((line = err.readLine()) != null) {
              log.info(line);
            }
          } catch(IOException e) {
            e.printStackTrace();
          }
			  }
			}).start();
			
			// this has ArrayList as its each element
			// this element consists of all annotations for that particular
			// sentence
			ArrayList sentenceTokens = new ArrayList();

			// this will have an annotation for each line begining with a number
			ArrayList tokens = new ArrayList();
			outer: while ((line = input.readLine()) != null) {
				WordToken wt = new WordToken();
				// so here whatever we get in line
				// is of our interest only if it begins with any number
				// each line is deliminated with a tab sign
				String[] output = line.split("\t");
				if (output.length < 5)
					continue;
				for (int i = 0; i < output.length; i++) {
					// we ignore case 2 and 3 and 6 and after.. because we don't
					// want
					// that information
					switch (i) {
					case 0:
						// this is a word number
						try {
							int number = Integer.parseInt(output[i].trim());
							// yes this is correct line
							// we need to check if the line number is 1
							// it may be the begining of new sentence
							if (number == 1 && tokens.size() > 0) {
								// we need to add tokens to the sentenceTokens
								sentenceTokens.add(tokens);
								tokens = new ArrayList();
							}
						} catch (NumberFormatException infe) {
							// if we are here, there is something wrong with
							// number
							// ignore this line and continue with next line
							continue outer;
						}
						break;
					case 1:
						// this is the actual word (Token.string)
						wt.word = output[i];
						break;
					case 4:
						// this should be the number and if it is not
						// then we leave it and do not add any head
						try {
							int head = Integer.parseInt(output[i].trim());
							// yes this is the correct head number
							wt.headNumber = head;
						} catch (NumberFormatException nfe) {
							// if we are here, there is something wrong with
							// number
							// ignore this and make headNumber -1 letter on to
							// remember that we don't want headnumber to be
							// inserted as a
							// feature
							wt.headNumber = -1;
						}
						break;
					case 5:
						// this is the relation between head and the current
						// node
						wt.relationWithHead = output[i];
						break;
					default:
						break;
					}
				}

				// here we have parsed the one line and thus now we should add
				// it to the
				// tokens for letter use
				tokens.add(wt);
			}
			if (tokens.size() > 0) {
				sentenceTokens.add(tokens);
			}
			input.close();

			// ok so here we have all the information we need from the minipar
			// in
			// local variables
			// ok so first we would create annotation for each word Token

			AnnotationSet annotSet = (annotationOutputSetName == null || annotationOutputSetName
					.equals("")) ? document.getAnnotations() : document
					.getAnnotations(annotationOutputSetName);

			// size of the sentenceTokens and the allSentences would be always
			// same
			for (int i = 0; i < sentenceTokens.size(); i++) {
				tokens = (ArrayList) sentenceTokens.get(i);

				// we need this to generate the offsets
				Annotation sentence = (Annotation) allSentences.get(i);
				String sentenceString = document.getContent().getContent(
						sentence.getStartNode().getOffset(),
						sentence.getEndNode().getOffset()).toString();

				sentenceString = sentenceString.replaceAll("\r", " ");
				sentenceString = sentenceString.replaceAll("\n", " ");

				// this will hold the position from where it should start
				// searching for
				// the token text
				int startOffset = sentence.getStartNode().getOffset()
						.intValue();

				int index = -1;
				for (int j = 0; j < tokens.size(); j++) {
					// each item here is a separate word token
					WordToken wt = (WordToken) tokens.get(j);
					// ok so find out the offsets
					int stOffset = sentenceString.toLowerCase().indexOf(
							wt.word.toLowerCase(), index)
							+ startOffset;
					int enOffset = stOffset + wt.word.length();
					FeatureMap map = Factory.newFeatureMap();
					map.put("word", wt.word);
					Integer id = annotSet.add(new Long(stOffset), new Long(
							enOffset), annotationTypeName, map);
					wt.annotation = annotSet.get(id);
					index = enOffset - startOffset;
				}
			}

			// now we need to create the children nodes
			for (int i = 0; i < sentenceTokens.size(); i++) {
				tokens = (ArrayList) sentenceTokens.get(i);

				for (int j = 0; j < tokens.size(); j++) {
					WordToken wt = (WordToken) tokens.get(j);
					// read the head node
					// find out the respective word token for that head node
					// and add the current node as its child
					if (wt.headNumber > 0) {
						WordToken headToken = (WordToken) tokens
								.get(wt.headNumber - 1);
						headToken.children.add(wt.annotation.getId());
					}
				}
			}

			// and finally we need to add features to the annotations
			// now we need to create the children nodes
			for (int i = 0; i < sentenceTokens.size(); i++) {
				tokens = (ArrayList) sentenceTokens.get(i);

				for (int j = 0; j < tokens.size(); j++) {
					// for every wordtoken,
					// we look for its head
					// and create annotations
					// the annotation will have the type of relation string
					// and as a features
					// it will have one head ID and one child ID
					// head ID is the id of head
					// and child ID is the id of wt
					WordToken wt = (WordToken) tokens.get(j);
					FeatureMap map = Factory.newFeatureMap();
					if (wt.headNumber > 0) {
						Annotation headAnn = ((WordToken) tokens
								.get(wt.headNumber - 1)).annotation;
						map.put("head_id", headAnn.getId());
						map.put("child_id", wt.annotation.getId());
						map.put("head_word", ((WordToken) tokens
								.get(wt.headNumber - 1)).word);
						map.put("child_word", wt.word);
						// so create the new annotation
						int stOffset1 = headAnn.getStartNode().getOffset()
								.intValue();
						int stOffset2 = wt.annotation.getStartNode()
								.getOffset().intValue();
						int enOffset1 = headAnn.getEndNode().getOffset()
								.intValue();
						int enOffset2 = wt.annotation.getEndNode().getOffset()
								.intValue();
						stOffset1 = stOffset1 < stOffset2 ? stOffset1
								: stOffset2;
						enOffset1 = enOffset1 > enOffset2 ? enOffset1
								: enOffset2;
						annotSet.add(new Long(stOffset1), new Long(enOffset1),
								wt.relationWithHead, map);
					}
				}
			}

			// and finally make the sentenceTokens and tokens to null
			tokens = null;
			sentenceTokens = null;
		} catch (Exception exception) {
			exception.printStackTrace();
			throw new ExecutionException(exception);
		}
	} // end of runMinipar()

	/**
	 * Execute method is called whenever user clicks on the "RUN" button in GATE
	 * GUI. It should be called after the instantiation of the resource. This
	 * method first checks for all mandatory runtime parameters, if not provided
	 * prompts user by throwing an executionexception. For optional parameters
	 * it considers the default values. this method initially given a document,
	 * converts the document text into a text file. This text file is then sent
	 * to runMinipar function in order to parse it with Minipar.
	 */
	public void execute() throws ExecutionException {
		if (document == null)
			throw new ExecutionException("No document to process!");
		if (getMiniparBinary() == null)
			throw new ExecutionException(
					"Please provide the URL for Minipar Binary");
		if (getMiniparDataDir() == null)
			throw new ExecutionException(
					"Minipar requires the location of its data directory "
							+ "(By default it is %Minipar_Home%/data");

    // generate a temporary file for the current document
    File gateTempFile = null;
    try {
    gateTempFile =  File.createTempFile( GATETEXTFILE, ".txt");
    gateTempFile.deleteOnExit();
    }
    catch ( java.io.IOException e){
      throw new ExecutionException("Impossible to generate temp file!");
    }
		// obtain GATE sentence annotations as a list
		ArrayList allSentences = saveGateSentences(gateTempFile);
		// finally runMinipar
		runMinipar(allSentences,gateTempFile);
	}

	/**
	 * WorkToken subclass is used as an internal data structure to hold
	 * temporary information returned by the minipar parser. This information is
	 * then mapped over the GATE annotations.
	 */
	private class WordToken {
		/**
		 * Word is the string, that represents the token in minipar
		 */
		String word;

		/**
		 * Each token in minipar is given a number. This contains the number of
		 * the headToken
		 */
		int headNumber;

		/**
		 * This stores the relation of head word with its children
		 */
		String relationWithHead;

		/**
		 * Contains other instances of WordTokens which have been identified as
		 * children of the headword
		 */
		ArrayList children = new ArrayList();

		/**
		 * GATE annotation that represents the WordToken in GATE
		 */
		gate.Annotation annotation;
	}

}
