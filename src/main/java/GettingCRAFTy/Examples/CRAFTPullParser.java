/**
 * 
 */
package GettingCRAFTy.Examples;
import java.io.*;
import java.util.*;
/** in xpp.jar */
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

/**
 * @author park
 *
 */
public class CRAFTPullParser {
	private CRAFTReaderEnvironment environment;
	private final String
		FILE_NAME			= "filename",
		MENTION_ID			= "id",
		SPAN				= "span",
		SPANNED_TEXT		= "term",
		ANNOTATOR			= "annotator",
		CLASS_ID			= "classId",
		CLASS_LABEL			= "label";
		
		
	/**
	 * The parse result
	 */
	private Map<String,Object>theMap;
	
	/**
	 * @param env
	 */
	public CRAFTPullParser(CRAFTReaderEnvironment env) {
		environment = env;
	}

	/**
	 * Return the results of a parse
	 * @return
	 */
	public Map<String,Object> getResult() {
		return theMap;
	}
	
	/**
	 * Parse an <code>inFile</code>
	 * @param inFile
	 * @param fileName
	 */
	public void importCRAFTFile(File inFile, String fileName) {
		try {
			System.out.println("Importing "+inFile);
			// grab an inputstream
			FileInputStream fis = new FileInputStream(inFile);
			// parse this puppy
			doParse(fis, fileName);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	/**
	 * Do the parsing legwork
	 * @param fis
	 * @param fileName
	 */
	private void doParse(FileInputStream fis, String fileName) {
		//create the result object for this parse
		theMap = new HashMap<String,Object>();
		theMap.put(FILE_NAME, fileName);
	    try {
	         XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
	         factory.setNamespaceAware(false);
	         XmlPullParser xpp = factory.newPullParser();

	         BufferedReader in = new BufferedReader(new InputStreamReader(fis));
	         xpp.setInput(in);
	         String temp = null;
	         String text = null;
	         String id = null;
	         HashMap<String,String> props;
	         Map<String,Object> theMention = null;
	         int eventType = xpp.getEventType();
	         boolean isStop = false;
	         while (!(isStop || eventType == XmlPullParser.END_DOCUMENT)) {
	        	 Thread.yield();
	            temp = xpp.getName();
	            if(eventType == XmlPullParser.START_DOCUMENT) {
	                System.out.println("Start document");
	            } else if(eventType == XmlPullParser.END_DOCUMENT) {
	                System.out.println("End document");
	            } else if(eventType == XmlPullParser.START_TAG) {
	                System.out.println("Start tag "+temp);
	                props = getAttributes(xpp);
	                if(temp.equalsIgnoreCase("annotation")) {
	                	theMention = new HashMap<String,Object>();
	                } else if(temp.equalsIgnoreCase("mention")) {
	                	id = (String)props.get("id");
	                	theMention.put(MENTION_ID, id);
	                	theMap.put(id, theMention);
	                } else if(temp.equalsIgnoreCase("annotator")) {
	                	id = (String)props.get("id");
	                	theMention.put(ANNOTATOR, id);
	                } else if(temp.equalsIgnoreCase("span")) {
	                	//We support > 1 span for a given mention
	                	List<String>spx = new ArrayList<String>();
	                	spx.add((String)props.get("start"));
	                	spx.add((String)props.get("end"));
	                	List<List<String>> l = (List<List<String>>)theMention.get(SPAN);
	                	if (l == null) {
	                		l = new ArrayList<List<String>>();
	                		theMention.put(SPAN, l);
	                	}
	                	l.add(spx);
	                } else if (temp.equalsIgnoreCase("classMention")) {
	                	id = (String)props.get("id");
	                	theMention = (Map<String,Object>)theMap.get(id);
	                } else if (temp.equalsIgnoreCase("mentionClass")) {
	                	id = (String)props.get("id");
	                	theMention.put(CLASS_ID, id);
	                }
	            } else if(eventType == XmlPullParser.END_TAG) {
	                System.out.println("End tag "+temp+" // "+text);
	                if (temp.equalsIgnoreCase("annotation")) {
	                	theMention = null;
	                } else if(temp.equalsIgnoreCase("spannedText")) {
	                	theMention.put(SPANNED_TEXT, text);
	                } else if (temp.equalsIgnoreCase("classMention")) {
	                	theMention = null;
	                } else if (temp.equalsIgnoreCase("mentionClass")) {
	                	theMention.put(CLASS_LABEL, text);
	                }
	            } else if(eventType == XmlPullParser.TEXT) {
//		                System.out.println("Text "+id+" // "+xpp.getText());
	                text = xpp.getText().trim();
	             } else if(eventType == XmlPullParser.CDSECT) {
	     //         System.out.println("Cdata "+id+" // "+xpp.getText());
	                text = xpp.getText().trim();
	            }
	            eventType = xpp.next();
	      //System.out.println("PARSER NEXT "+id+" // "+result.getCommand()+ " // "+eventType);
	          }
	      } catch (Exception e) {
	    	  	System.out.println("CRAFTPullParser parser failed "+e.getMessage());
	      		e.printStackTrace();
	      } 
	}
	
	/**
     * Return null if no attributes
     */
    HashMap<String,String> getAttributes(XmlPullParser p) {
      HashMap <String,String>result = null;
      int count = p.getAttributeCount();
      if (count > 0) {
        result = new HashMap<String,String>();
        String name = null;
        for (int i = 0; i < count; i++) {
          name = p.getAttributeName(i);
          result.put(name,p.getAttributeValue(i));
        }
      }
      return result;
    }		

}
