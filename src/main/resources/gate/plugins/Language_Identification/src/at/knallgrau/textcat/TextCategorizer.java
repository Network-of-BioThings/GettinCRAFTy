package at.knallgrau.textcat;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;


/**
 * @author Thomas Hammerl
 * 
 * TextCategorizer is able to categorize texts by computing the similarity of
 * the FingerPrint of a text with a collection of the FingerPrints of the
 * categories.
 * 
 */
public class TextCategorizer {
	
  private URL confURL = null;

	private final static int UNKNOWN_LIMIT = 20;

	private Collection<FingerPrint> fingerprints = new ArrayList<FingerPrint>();

	public TextCategorizer(Collection<FingerPrint> fingerprints) {
		this.fingerprints = fingerprints;
	}

	/**
	 * creates a new TextCategorizer with the given configuration file. the
	 * configuration file maps paths to FingerPrint files to categories which
	 * are used to categorize the texts passed to the TextCategorizer.
	 * 
	 * @param confURL
	 *            the URL to the configuration file
	 */
	public TextCategorizer(URL confURL) {
	  this.confURL = confURL;
	  loadCategories();
	}

	/**
	 * clears the categories-collection and fills it with the FingerPrints given
	 * in the configuration file.
	 */
	private void loadCategories() {
		this.fingerprints.clear();
		MyProperties properties = new MyProperties();
		
		try {
			properties.load(confURL.openStream());
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}

		for (Entry<String, String> entry : properties.entrySet()) {
			FingerPrint fp;
			try {
		    URL fpURL = new URL(confURL,entry.getKey());
		    fp = new FingerPrint(fpURL.openStream());

				fp.setCategory(entry.getValue());
				this.fingerprints.add(fp);
			} catch (MalformedURLException mue) {
			  mue.printStackTrace();
			} catch (IOException ioe) {
			  ioe.printStackTrace();
			}
			catch (FingerPrintFileException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * categorizes the text passed to it
	 * 
	 * @param text
	 *            text to be categorized
	 * @return the category name given in the configuration file
	 */
	public String categorize(String text) {
		if (text.length() < UNKNOWN_LIMIT) {
			return "unknown";
		}
		FingerPrint fp = new FingerPrint();
		fp.create(text);
		fp.categorize(fingerprints);

		return fp.getCategory();
	}

	/**
	 * categorizes only a certain amount of characters in the text. recommended
	 * when categorizing large texts in order to increase performance.
	 * 
	 * @param text
	 *            text to be analysed
	 * @param limit
	 *            number of characters to be analysed
	 * @return the category name given in the configuration file
	 */
	public String categorize(String text, int limit) {
		if (text.length() < UNKNOWN_LIMIT) {
			return "unknown";
		}
		if (limit > (text.length() - 1)) {
			return this.categorize(text);
		}
		return this.categorize(text.substring(0, limit));
	}

	/**
	 * categorizes a text but returns a map containing all categories and their
	 * distances to the text.
	 * 
	 * @param text
	 *            text to be categorized
	 * @return HashMap with categories as keys and distances as values
	 */
	public Map<String, Integer> getCategoryDistances(String text) {
		if (this.fingerprints.isEmpty()) {
			loadCategories();
		}
		FingerPrint fp = new FingerPrint();
		fp.create(text);
		fp.categorize(fingerprints);
		return fp.getCategoryDistances();
	}
}
