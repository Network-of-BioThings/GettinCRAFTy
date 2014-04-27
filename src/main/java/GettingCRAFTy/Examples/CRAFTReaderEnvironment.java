/**
 * 
 */
package org.topicquests.craft;
import java.io.*;
import java.util.*;
/**
 * @author park
 *
 */
public class CRAFTReaderEnvironment {
	private CRAFTPullParser parser;
	/**
	 * NOTE: DATA_DIR is hardwired here; it could
	 * be passed in as a parameter or system property
	 */
	private String DATA_DIR = "data";
	/**
	 * 
	 */
	public CRAFTReaderEnvironment() {
		parser = new CRAFTPullParser(this);
		System.out.println("Hello World");
		testParser();
	}

	/**
	 * Simple method to boot the system on startup
	 */
	void testParser() {
		File dir = new File(DATA_DIR);
		importDirectory(dir);
	}
	
	/**
	 * <p>Recursive walk through directories of XML files</p>
	 * <p>A reasonable directory structure is precisely that of
	 * the <em>knowtator-xml</em> directory in the Craft download.</p>
	 * @param dir
	 */
	void importDirectory(File dir) {
		System.out.println("IMPORTING DIRECTORY "+dir.getName());
		File [] files = dir.listFiles();
		if (files != null) {
			int len = files.length;
			System.out.println("NUMFILES "+len);
			File f;
			Map<String,Object>result;
			String fname;
			for (int i=0;i<len;i++) {
				f = files[i];
				if (f.isDirectory())
					importDirectory(f);
				else {
					fname = f.getName();
					//sanity check
					if (fname.endsWith("xml")) {
						parser.importCRAFTFile(f,fname);
						System.out.println("PARSED "+fname);
						/////////////////////
						//This is where we take the results
						// of a parse and do something with it.
						result = parser.getResult();
						System.out.println(result);
						processParse(result);
					}
				}
			}
		}
	}
	
	/**
	 * Shell method to map a <code>parseResult</code> to some
	 * other form for Gate and ship it or save it
	 * @param parseResult
	 */
	void processParse(Map<String,Object> parseResult) {
		//TODO
	}
}
