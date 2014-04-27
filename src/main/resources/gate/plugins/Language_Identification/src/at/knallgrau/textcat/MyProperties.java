package at.knallgrau.textcat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.Hashtable;

/**
 * @author Thomas Hammerl
 * 
 * Propertyclass extending java.util.Hashtable which reads textcat configuration
 * files and textcat fingerprint files.
 */
class MyProperties extends Hashtable<String, String> implements Serializable {

    private static final long serialVersionUID = 3256446889091347768L;

    /**
         * reads properties and stores them in the Hashtable
         * 
         * @param is
         *                InputStream reading properties
         */
    public void load(InputStream is) {
	String line;
	String[] prop = new String[2];
	int i;
	InputStreamReader isr = new InputStreamReader(is);
	BufferedReader br = new BufferedReader(isr);
	try {
	    while ((line = br.readLine()) != null) {
		i = 0;
		String[] tokens = line.split("\\s");
		for (String token : tokens) {
		    if (!token.equals("")) {
			prop[i] = token;
			i++;
			if (i == 2) {
			    break;
			}
		    }
		}
		this.put(prop[0], prop[1]);
	    }
	    br.close();
	    isr.close();
	} catch (IOException ioe) {
	    ioe.printStackTrace();
	}
    }

}
