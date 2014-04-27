/*
 *  CrawlPR.java
 *
 *  Copyright (c) 1995-2012, The University of Sheffield. See the file
 *  COPYRIGHT.txt in the software or at http://gate.ac.uk/gate/COPYRIGHT.txt
 *
 *  This file is part of GATE (see http://gate.ac.uk/), and is free
 *  software, licenced under the GNU Library General Public License,
 *  Version 2, June 1991 (in the distribution as file licence.html,
 *  and also available at http://gate.ac.uk/gate/licence.html).
 *  
 */
package crawl;

import websphinx.*;
import gate.creole.*;
import gate.persist.PersistenceException;
import gate.security.SecurityException;
import gate.corpora.*;
import gate.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.net.*;
import javax.mail.internet.ContentType;
import javax.mail.internet.ParseException;
import org.apache.commons.lang.StringUtils;


public class SphinxWrapper extends Crawler{

  private static final long serialVersionUID = -6524027714398026402L;
  @SuppressWarnings("unused")
  private static final String __SVNID = "$Id: SphinxWrapper.java 15334 2012-02-07 13:57:47Z ian_roberts $";

  private Corpus corpus = null;
  private static int maxFetch = -1;
  private static int maxKeep  = -1;
  private static AtomicInteger countFetched, countKept;
  private static boolean ignoreKeywords;
  private static boolean caseSensitiveKeywords;
  private static boolean convertXmlTypes;
  private static List<String> keywords;

  
  protected void setKeywords(List<String> newKeywords, boolean caseSensitive) {
    keywords = newKeywords;
    ignoreKeywords = (keywords == null) || keywords.isEmpty();
    caseSensitiveKeywords = caseSensitive;
  }
  
  
  protected void setConvertXmlTypes(boolean convert) {
    convertXmlTypes = convert;
  }
  
  
  @SuppressWarnings("unchecked")
  public void visit(Page p) {
    if ( ( (maxFetch != -1) && (countFetched.get() >= maxFetch) ) ||
         ( (maxKeep != -1) && (countKept.get() >= maxKeep) ) )    {
      syncIfNecessary();
      super.stop();
      return;
    }

    int currentFetched = countFetched.incrementAndGet();
    String urlString = p.toURL();
    int depth = p.getDepth();
    Document doc = makeDocument(p);
    p.discardContent();
    
    /* For the keyword-matching, we tried p.toText() but it doesn't
     * parse JavaScript as well as GATE's HTML parser.       */

    if (doc == null)  {// failed to produce a valid gate.Document
      System.out.println(countKept.toString() + " / " + currentFetched + 
              " [" + depth + "] Drop: " + urlString);
    }

    else if (ignoreKeywords || containsAnyKeyword(doc, keywords, caseSensitiveKeywords)) {    
      // produced a valid gate.Document
      // keyword match succeeded
      corpus.add(doc);
      int currentCount = countKept.incrementAndGet();

      if (corpus.getLRPersistenceId() != null) {
        corpus.unloadDocument(doc);
        Factory.deleteResource(doc);
      }
      System.out.println(currentCount + " / " + currentFetched + 
              " [" + depth + "] Keep: " + urlString);
    }
    
    else {  // keyword match failed
      System.out.println(countKept.toString() + " / " + currentFetched + 
              " [" + depth + "] Drop: " + urlString);
      Factory.deleteResource(doc);
    }
  }
  

  public boolean shouldVisit(Link l) {
    return super.shouldVisit(l);
  }

  protected void setDepth(int depth) {
    super.setMaxDepth(depth);
  }

  protected void setMaxPages(int max) {
    maxFetch = max;
  }
  
  protected void setMaxKeep(int max) {
    maxKeep = max;
  }

  protected int getMaxPages() {
    return maxFetch;
  }
  
  protected int getMaxKeep() {
    return maxKeep;
  }


  protected void addStartLink(String root) {
    try {
      URL url = new URL(root);
      Link link = new Link(url);
      System.out.println("Adding seed URL  " + url.toString());
      super.addRoot(link);
    }
    catch (MalformedURLException me) {
      System.err.println("Malformed url "+root);
      me.printStackTrace();
    }
  }

  protected void addStartLink(URL url) {
    Link link = new Link(url);
    System.out.println("Adding seed URL  " + url.toString());
    super.addRoot(link);
  }
  

  public void setCorpus(Corpus corpus) {
    this.corpus = corpus;
  }


  /* yes: application/rss+xml.xml
   * no:  image/svg+xml.xml
   */
  private static String convertMimeType(String originalType) {
    String result = originalType;
    if (originalType.endsWith("xml")
            && (originalType.startsWith("application") || originalType.startsWith("application") )
    ) {
      result = "text/xml";
    }
    return result;
  }
  
  
  public void start() {
    super.run();
  }
  
  protected void resetCounter() {
    countFetched = new AtomicInteger(0);
    countKept    = new AtomicInteger(0);
  }
  
  protected void interrupt()  {
    super.stop();
    syncIfNecessary();
  }

  private void syncIfNecessary() {
    if (corpus.getLRPersistenceId() != null) {
      try {
        corpus.sync();
      }
      catch(PersistenceException e) {
        e.printStackTrace();
      }
      catch(SecurityException e) {
        e.printStackTrace();
      }
    }
  }

  
  private static boolean containsAnyKeyword(Document document, List<String> keywords, boolean caseSensitive) {
    return containsAnyKeyword(document.getContent().toString(), keywords, caseSensitive);
  }
  

  private static boolean containsAnyKeyword(String content, List<String> keywords, boolean caseSensitive) {
    if ( (keywords == null) || keywords.isEmpty()) {
      return true;
    }
    
    // implied else: test the keywords
    if (caseSensitive) {
      for (String kw : keywords) {
        if (StringUtils.contains(content, kw)) {
          return true;  
        }
      }
    }
    
    else { // case-insensitive
      for (String kw : keywords) {
        if (StringUtils.containsIgnoreCase(content, kw)) {
          return true;
        }
      }
    }
    
    return false;
  }


 
  private static Document makeDocument(Page page) {
    String url = page.toURL();
    String content = page.getContent();
    FeatureMap params = Factory.newFeatureMap();
    
    // This is more efficient than creating the gate.Document from the URL
    // (by downloading it again).
    params.put(Document.DOCUMENT_STRING_CONTENT_PARAMETER_NAME, content);
    Document doc = null;

    String docName = shortenUrl(url).replaceAll("[^\\p{ASCII}]", "_") + "_" + Gate.genSym();

    /* Take advantage of the MIME type from the server when
     * constructing the GATE document.      */
    String contentTypeStr = page.getContentType();
    String originalMimeType = null;

    if (contentTypeStr != null) {
      try {
        ContentType contentType = new ContentType(contentTypeStr);
        String mimeType = contentType.getBaseType();
        String encoding = contentType.getParameter("charset");

        if (mimeType != null) {
          if (convertXmlTypes) {
            originalMimeType = mimeType;
            mimeType = convertMimeType(mimeType);
            if (! originalMimeType.equals(mimeType)) {
              System.out.println("   convert " + originalMimeType + " -> " + mimeType);
            }
          }
          params.put(Document.DOCUMENT_MIME_TYPE_PARAMETER_NAME, mimeType);
        }

        if (encoding != null) {
          params.put(Document.DOCUMENT_ENCODING_PARAMETER_NAME, encoding);

        }
      } catch(ParseException e) {
        e.printStackTrace();
      }
    }


    try {
      doc = (Document) Factory.createResource(
              DocumentImpl.class.getName(), params, null, docName);
      FeatureMap docFeatures = doc.getFeatures();

      Integer originalLength = page.getLength();
      docFeatures.put("originalLength", originalLength);
      
      /* Use the Last-Modified HTTP header if available.  */
      long lastModified = page.getLastModified();
      Date date;
      if (lastModified > 0L) {
        date = new Date(lastModified);
      }
      else {
        date = new Date();
      }
      docFeatures.put("Date", date);
      
      if (originalMimeType != null) {
        docFeatures.put("originalMimeType", originalMimeType);
      }
      
      doc.setSourceUrl(page.getURL());
      docFeatures.put("gate.SourceURL", url);
    }
    catch (ResourceInstantiationException e) {
      System.err.println("WARNING: could not intantiate document " + docName);
      e.printStackTrace();
    }

    return doc;
  }

  
  private static String shortenUrl(String url) {
    String result = url.replaceAll("//+", "/");
    int s0 = StringUtils.lastIndexOf(url, '/');
    int s1 = StringUtils.lastIndexOf(url, '/', s0 -1 );
    if (s1 > 0) {
      result = url.substring(s1 + 1);
    }
    return result;
  }
  
  
}