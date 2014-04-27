package at.knallgrau.textcat;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Thomas Hammerl A FingerPrint maps so called NGrams to their number of
 *         occurences in the corresponding text. It is able to categorize itself
 *         by comparing its FingerPrint with the FingerPrints of a collection of
 *         categories. See sdair-94-bc.pdf in the doc direcory of the jar-file
 *         for more information.
 * 
 */
public class FingerPrint {

    private Map<String, Integer> ngrams = new HashMap<String, Integer>();

    private static final long serialVersionUID = -2790111752993314113L;

    private final Pattern filePattern = Pattern.compile("-(.*)\\.lm$");

    private final Pattern pattern = Pattern.compile("^_?[^0-9\\?!\\-_/]*_?$");

    private String category = "unknown";

    private Map<String, Integer> categoryDistances = new HashMap<String, Integer>();

    /**
         * Set of NGrams sorted by the number of occurences in the text which
         * was used for creating the FingerPrint.
         * 
         */
    private SortedSet<Entry<String, Integer>> entries = new TreeSet<Entry<String, Integer>>(
	    new NGramEntryComparator());;

    public FingerPrint() {
    }

    /**
         * creates a FingerPrint by reading the FingerPrint-file referenced by
         * the passed path.
         * 
         * @param file
         *                path to the FingerPrint-file
         * @throws FingerPrintFileException
         */
    public FingerPrint(String file) throws FingerPrintFileException {
	this.loadFingerPrintFromFile(file);
    }

    /**
         * creates a FingerPrint by reading it with the passed InputStream
         * 
         * @param is
         *                InputStream for reading the FingerPrint
         * @throws FingerPrintFileException
         */
    public FingerPrint(InputStream is) throws FingerPrintFileException {
	this.loadFingerPrintFromInputStream(is);
    }

    public FingerPrint(InputStream is, String encoding)
	    throws FingerPrintFileException {
	this.loadFingerPrintFromInputStream(is, encoding);
    }

    /**
         * creates a FingerPrint by analysing the content of the given file.
         * 
         * @param file
         *                file to be analysed
         * @throws FileNotFoundException
         *                 thrown when given file does not exist
         */
    public void create(File file) throws FileNotFoundException {
	char[] data = new char[1024];
	String s = "";
	int read;
	FileReader fr = new FileReader(file);
	try {
	    while ((read = fr.read(data)) != -1) {
		s += new String(data, 0, read);
	    }
	    fr.close();
	} catch (IOException ioe) {
	    ioe.printStackTrace();
	    return;
	}
	this.create(s);
    }

    /**
         * fills the FingerPrint with all the NGrams and their numer of
         * occurences in the passed text.
         * 
         * @param text
         *                text to be analysed
         */
    public void create(String text) {
	this.ngrams.clear();
	this.computeNGrams(1, 5, text);
	if (this.ngrams.containsKey("_")) {
	    int blanksScore = this.ngrams.remove("_");
	    this.ngrams.put("_", blanksScore / 2);
	}

	this.entries.clear();
	this.entries.addAll(this.ngrams.entrySet());
    }

    /**
         * adds all NGrams with the passed order occuring in the given text to
         * the FingerPrint. For example:
         * 
         * text = "text" startOrder = 2, maxOrder = 2
         * 
         * so the NGrams added to the FingerPrint are:
         * 
         * "_t", "te", "ex", "xt", "t_"
         * 
         * all with a score (occurence) of 1
         * 
         * @param startOrder
         * @param maxOrder
         * @param text
         */
    private void computeNGrams(int startOrder, int maxOrder, String text) {
	String[] tokens = text.split("\\s");

	for (int order = startOrder; order <= maxOrder; ++order) {

	    for (String token : tokens) {
		token = "_" + token + "_";

		for (int i = 0; i < (token.length() - order + 1); i++) {
		    String ngram = token.substring(i, i + order);

		    Matcher matcher = pattern.matcher(ngram);
		    if (!matcher.find()) {
			continue;
		    } else if (!this.ngrams.containsKey(ngram)) {
			this.ngrams.put(ngram, 1);
		    } else {
			int score = this.ngrams.remove(ngram);
			this.ngrams.put(ngram, ++score);
		    }
		}
	    }
	}
    }

    /**
         * categorizes the FingerPrint by computing the distance to the
         * FingerPrints in the passed Collection. the category of the
         * FingerPrint with the lowest distance is assigned to this FingerPrint.
         * 
         * @param categories
         */
    public Map<String, Integer> categorize(Collection<FingerPrint> categories) {
	int minDistance = Integer.MAX_VALUE;
	for (FingerPrint fp : categories) {
	    int distance = this.computeDistanceTo(fp);
	    this.getCategoryDistances().put(fp.getCategory(), distance);
	    if (distance < minDistance) {
		minDistance = distance;
		this.category = fp.getCategory();
	    }
	}
	return this.getCategoryDistances();
    }

    public Map<String, Integer> getCategoryDistances() {
	return this.categoryDistances;
    }

    /**
         * computes and returns the distance of this FingerPrint to the
         * FingerPrint passed to the method.
         * 
         * @param category
         *                the FingerPrint to be compared to this one
         * @return the distance of the passed FingerPrint to this FingerPrint
         */
    private int computeDistanceTo(FingerPrint category) {
	int distance = 0;
	int count = 0;
	for (Entry<String, Integer> entry : this.entries) {
	    String ngram = entry.getKey();
	    count++;
	    if (count > 400) {
		break;
	    }
	    if (!category.containsNgram(ngram)) {
		distance += category.numNgrams();
	    } else {
		distance += Math.abs(this.getPosition(ngram)
			- category.getPosition(ngram));
	    }
	}
	return distance;
    }

    public boolean containsNgram(String ngram) {
	return this.ngrams.containsKey(ngram);
    }

    public int numNgrams() {
	return this.ngrams.size();
    }

    /**
         * reads a FingerPrint from the passed InputStream
         * 
         * @param is
         *                InputStream to be read
         * @throws FingerPrintFileException
         */
    private void loadFingerPrintFromInputStream(InputStream is)
	    throws FingerPrintFileException {
	this.loadFingerPrintFromInputStream(is, "UTF-8");
    }

    private void loadFingerPrintFromInputStream(InputStream is, String encoding)
	    throws FingerPrintFileException {
	this.entries.clear();
	MyProperties properties = new MyProperties();
	try {
	    String line;
	    InputStreamReader isr = new InputStreamReader(is, encoding);
	    BufferedReader reader = new BufferedReader(isr);
	    while ((line = reader.readLine()) != null) {
		if (!line.equals("")) {
		    String[] property = line.split("\\s+");
		    if (property.length >= 2) {
			properties.put(property[0], property[1]);
		    }
		}
	    }
	    /* properties.load(is); */
	    for (Entry<String, String> entry : properties.entrySet()) {
		this.ngrams.put(entry.getKey(), Integer.parseInt(entry
			.getValue()));
	    }
	    entries.addAll(this.ngrams.entrySet());
	} catch (UnsupportedEncodingException e) {
	    throw new FingerPrintFileException(e);
	} catch (IOException e) {
	    throw new FingerPrintFileException(e);
	}
    }

    /**
         * reads a FingerPrint from the file referenced by the passed path
         * 
         * @param file
         *                FingerPrint file to be read
         * @throws FingerPrintFileException
         */
    private void loadFingerPrintFromFile(String file)
	    throws FingerPrintFileException {
	File fpFile = new File(file);
	if (!fpFile.isDirectory()) {
	    try {
		String encoding = null;
		File f = new File(file);
		Matcher matcher = filePattern.matcher(f.getName());
		if (matcher.matches()) {
		    encoding = matcher.group(1);
		}

		FileInputStream fis = new FileInputStream(file.toString());

		if (encoding != null) {
		    this.loadFingerPrintFromInputStream(fis, encoding);
		} else {
		    this.loadFingerPrintFromInputStream(fis);
		}
	    } catch (FileNotFoundException e) {
		throw new FingerPrintFileException(e);
	    }
	}
    }

    /**
         * gets the position of the NGram passed to method in the FingerPrint.
         * the NGrams are in descending order according to the number of
         * occurences in the text which was used creating the FingerPrint.
         * 
         * @param key
         *                the NGram
         * @return the position of the NGram in the FingerPrint
         */
    public int getPosition(String key) {
	int pos = 1;

	int value = this.entries.first().getValue();
	for (Entry<String, Integer> entry : this.entries) {
	    if (value != entry.getValue()) {
		value = entry.getValue();
		pos++;
	    }
	    if (entry.getKey().equals(key)) {
		return pos;
	    }
	}
	return -1;
    }

    /**
         * saves the fingerprint to a file named <categoryname>.lm in the
         * execution path.
         */
    public void save() {
	File file = new File(this.getCategory() + "-utf8.lm");
	try {
	    if (file.createNewFile()) {
		FileOutputStream fos = new FileOutputStream(file);
		fos.write(this.toString().getBytes("utf8"));
		fos.close();
	    }
	} catch (FileNotFoundException fnfe) {
	    fnfe.printStackTrace();
	} catch (IOException ioe) {
	    ioe.printStackTrace();
	}
    }

    /**
         * returns the category of the FingerPrint or "unknown" if the
         * FingerPrint wasn't categorized yet.
         * 
         * @return the category of the FingerPrint
         */
    public String getCategory() {
	return this.category;
    }

    /**
         * returns the FingerPrint as a String in the FingerPrint file-format
         */
    public String toString() {
	String s = "";
	for (Entry<String, Integer> entry : entries) {
	    s += entry.getKey() + "\t" + entry.getValue() + "\n";
	}
	return s;
    }

    /**
         * sets the category of the FingerPrint
         * 
         * @param category
         *                the category
         */
    protected void setCategory(String category) {
	this.category = category;
    }

}
