package gate.lingpipe;

import gate.AnnotationSet;
import gate.FeatureMap;
import gate.ProcessingResource;
import gate.Resource;
import gate.creole.AbstractLanguageAnalyser;
import gate.creole.ExecutionException;
import gate.creole.ResourceInstantiationException;
import gate.util.InvalidOffsetException;

import java.util.Iterator;
import java.util.Set;

import com.aliasi.chunk.Chunk;
import com.aliasi.chunk.Chunking;
import com.aliasi.sentences.MedlineSentenceModel;
import com.aliasi.sentences.SentenceChunker;
import com.aliasi.sentences.SentenceModel;
import com.aliasi.tokenizer.IndoEuropeanTokenizerFactory;
import com.aliasi.tokenizer.TokenizerFactory;

/**
 * The Sentence splitter takes a document and find the sentence within.
 * @author Ekaterina Mihaylova
*/
public class SentenceSplitterPR extends AbstractLanguageAnalyser implements
		ProcessingResource {

  /**
   * Instance of the tokeniser
   */
	static final TokenizerFactory TOKENIZER_FACTORY = IndoEuropeanTokenizerFactory.INSTANCE;
	
	/**
	 * Sentence model
	 */
	static final SentenceModel SENTENCE_MODEL = new MedlineSentenceModel();
	
	/**
	 * Sentence chunker
	 */
	static final SentenceChunker SENTENCE_CHUNKER = new SentenceChunker(
			TOKENIZER_FACTORY, SENTENCE_MODEL);

	/**
	 * Name of the annotation set
	 */
	private String outputASName;

	/**
	 * Gets name of the output annotation set where the Sentence annotations are stored
	 * @return
	 */
	public String getOutputASName() {
		return outputASName;
	}

	/**
	 * Sets name of the output annotation set where the Sentence annotations are stored
	 * @param outputAS
	 */
	public void setOutputASName(String outputAS) {
		this.outputASName = outputAS;
	}

	/** Initialise this resource, and return it. */
	public Resource init() throws ResourceInstantiationException {
		return super.init();
	}

	/**
	 * Reinitialises the processing resource. After calling this method the
	 * resource should be in the state it is after calling init. If the resource
	 * depends on external resources (such as rules files) then the resource
	 * will re-read those resources. If the data used to create the resource has
	 * changed since the resource has been created then the resource will change
	 * too after calling reInit().
	 */
	public void reInit() throws ResourceInstantiationException {
		init();
	}

	/**
	 * This method runs the coreferencer. It assumes that all the needed
	 * parameters are set. If they are not, an exception will be fired.
	 */
	public void execute() throws ExecutionException {

		if (document == null) {
			throw new ExecutionException("The document can't be null");
		}

		AnnotationSet set = null;

		if (outputASName == null || outputASName.trim().length() == 0){
			set = document.getAnnotations();
		}else{
			set = document.getAnnotations(outputASName);
		}

		fireProgressChanged(0);

		String text = document.getContent().toString();

		Chunking chunking = SENTENCE_CHUNKER.chunk(text.toCharArray(), 0, text
				.length());
		Set sentences = chunking.chunkSet();
		if (sentences.size() < 1) {
			System.out.println("No sentence chunks found.");
			return;
		}

		FeatureMap map = gate.Factory.newFeatureMap();
		int i=1;
		for (Iterator it = sentences.iterator(); it.hasNext();i++) {
			Chunk sentence = (Chunk) it.next();
			int start = sentence.start();
			int end = sentence.end();
			try {
				set.add(new Long(start), new Long(end), "Sentence", map);
			} catch (InvalidOffsetException e) {
				throw new ExecutionException(e);
			}
			fireProgressChanged(100*i/sentences.size());
		}

		fireProcessFinished();
	}

}