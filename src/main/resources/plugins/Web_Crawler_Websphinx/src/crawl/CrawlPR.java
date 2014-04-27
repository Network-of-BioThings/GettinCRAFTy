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
 */
package crawl;

import gate.Document;
import gate.Factory;
import gate.ProcessingResource;
import gate.Resource;
import gate.creole.*;
import gate.creole.metadata.*;
import gate.util.*;
import gate.*;
import java.net.URL;
import java.util.*;
import websphinx.*;


@CreoleResource(name = "Crawler PR",
        comment = "GATE implementation of the Websphinx crawling API",
        helpURL = "http://gate.ac.uk/userguide/sec:misc-creole:crawler")
public class CrawlPR 
  extends AbstractLanguageAnalyser 
  implements ProcessingResource {

  private static final long serialVersionUID = 3904269406671650905L;
  @SuppressWarnings("unused")
  private static final String __SVNID = "$Id: CrawlPR.java 15334 2012-02-07 13:57:47Z ian_roberts $";

  private String root = null;
  private int depth = -1;
  private Corpus outputCorpus = null;
  private Boolean dfs;
  private Boolean caseSensitiveKeywords;
  private SphinxWrapper crawler;
  private DomainMode domain = null;
  private Corpus source = null;
  private int maxFetch = -1;
  private int maxKeep  = -1;
  private Boolean convertXmlTypes;
  private String userAgent; // for spoofing
  private int maxPageSize;  // in kB
  
  // ignore keyword requirement if null or empty
  private List<String> keywords = null;

  /** Constructor of the class */
  public CrawlPR() {

  }

  /** Initialise this resource, and return it. */
  public Resource init() throws ResourceInstantiationException {
    return super.init();
  }

  /**
   * Reinitialises the processing resource. After calling this method the
   * resource should be in the state it is after calling init. If the resource
   * depends on external resources (such as rules files) then the resource will
   * re-read those resources. If the data used to create the resource has
   * changed since the resource has been created then the resource will change
   * too after calling reInit().
   */
  public void reInit() throws ResourceInstantiationException {
    init();
  }

  
  /**
   * Override the default behaviour by interrupting the SphinxWrapper itself. 
   */
  public void interrupt() {
    this.interrupted = true;
    if (crawler != null) {
      crawler.interrupt();
    }
    
  }
  
  
  
  /**
   * This method runs the crawler. It assumes that all the needed
   * parameters are set. If they are not, an exception will be fired.
   */
  public void execute() throws ExecutionException {
    this.interrupted = false;
    DownloadParameters downloadParameters = new DownloadParameters();
    downloadParameters = downloadParameters.changeUserAgent(userAgent);
    downloadParameters = downloadParameters.changeMaxPageSize(maxPageSize);
    
    crawler = new SphinxWrapper();
    crawler.clear();
    crawler.setDownloadParameters(downloadParameters);
    crawler.setKeywords(keywords, caseSensitiveKeywords);
    crawler.setConvertXmlTypes(convertXmlTypes);
    crawler.resetCounter();
    
    if(outputCorpus == null) { 
        throw new ExecutionException("Output Corpus cannot be null");
    }

    if ( (root == null) && (source == null) ) {
        throw new ExecutionException("Either root or source must be initialized");
    }
    if(depth < 0) {
        throw new ExecutionException("Limit is not initialized");
    }
    if(dfs == null) {
        throw new ExecutionException("dfs is not initialized");
    }
    if(domain == null) {
      throw new ExecutionException("domain type is not initialized.. Set to either SERVER/SUBTREE/WEB");
    }

    try {
      crawler.setCorpus(outputCorpus);
      crawler.setDepth(depth);
      crawler.setDepthFirst(dfs.booleanValue());
      
      if(domain.equals(DomainMode.SUBTREE)) {
        crawler.setDomain(Crawler.SUBTREE);
      }
      else if(domain.equals(DomainMode.SERVER)) {
        crawler.setDomain(Crawler.SERVER);
      }
      else {
        crawler.setDomain(Crawler.WEB);
      }

      crawler.setMaxPages(maxFetch);
      crawler.setMaxKeep(maxKeep);

      if (root != null && (root.length() > 0)) {
        crawler.addStartLink(root);
      }

      if (source != null) {
        for(int i = 0; i < source.size(); i++) {
          boolean docWasLoaded = source.isDocumentLoaded(i);
          Document doc = (Document) source.get(i);
          URL url = doc.getSourceUrl();
          if (url != null) {
            crawler.addStartLink(url);
          }
          else {
            System.out.println("Skipping source document:" + doc.getName());
          }
          
          if(! docWasLoaded) {
            source.unloadDocument(doc);
            Factory.deleteResource(doc);
          }
        }
      }
      
      crawler.start();
      
      if (this.interrupted) {
        throw new ExecutionInterruptedException();
      }
    
    }
    catch(Exception e) {
      String nl = Strings.getNl();
      Err.prln("  Exception was: " + e + nl + nl);
      e.printStackTrace();
    }
  }

  
  
  /*  CREOLE PARAMETERS  */
  
  @Optional
  @RunTime
  @CreoleParameter(comment = "The starting URL for the crawl")
  public void setRoot(String root) {
    this.root = root;
  }

  public String getRoot() {
    return this.root;
  }

  @RunTime
  @CreoleParameter(comment = "The depth to which the crawl must proceed",
    defaultValue = "3")
  public void setDepth(Integer limit) {
    this.depth = limit.intValue();
  }

  public Integer getDepth() {
    return new Integer(this.depth);
  }

  @RunTime
  @CreoleParameter(comment = "true for depth-first search; false for breadth-first search",
          defaultValue = "true")
  public void setDfs(Boolean dfs) {
    this.dfs = dfs;
  }

  public Boolean getDfs() {
    return this.dfs;
  }
  
  
  @Optional
  @RunTime
  @CreoleParameter(comment = "HTTP User Agent to spoof (leave blank for default)",
          defaultValue = "")
  public void setUserAgent(String ua) {
    this.userAgent = ua;
  }
  
  public String getUserAgent() {
    return this.userAgent;
  }

  @Optional
  @RunTime
  @CreoleParameter(comment = "max page size in kB (0 for no limit)", defaultValue = "100")
  public void setMaxPageSize(Integer mps) {
    this.maxPageSize = mps.intValue();
  }
  
  public Integer getMaxPageSize() {
    return Integer.valueOf(this.maxPageSize);
  }
  

  @RunTime
  @CreoleParameter(comment = "The domain restriction for the crawl",
          defaultValue = "SUBTREE")
  public void setDomain(DomainMode domain) {
    this.domain = domain;
  }

  public DomainMode getDomain() {
    return this.domain;
  }

  @RunTime
  @Optional
  @CreoleParameter(comment = "corpus whose gate.SourceURL document features will be used to seed the crawl")
  public void setSource(Corpus source) {
    this.source = source;
  }

  public Corpus getSource() {
    return this.source;
  }

  @RunTime
  @Optional
  @CreoleParameter(comment = "Stop the crawl after fetching this many pages (-1 to ignore)",
          defaultValue = "-1")
  public void setStopAfter(Integer max) {
    this.maxFetch = max.intValue();
  }

  // stopAfter was maxFetch in AF's first revision
  public Integer getStopAfter() {
    return Integer.valueOf(this.maxFetch);
  }
  
  @RunTime
  @Optional
  @CreoleParameter(comment = "Stop the crawl after saving this many pages (-1 to ignore)",
          defaultValue = "-1")
  public void setMax(Integer max) {
    this.maxKeep = max.intValue();
  }
  
  // max was maxKeep in AF's first revision;
  public Integer getMax() {
    return Integer.valueOf(this.maxKeep);
  }

  @RunTime
  @CreoleParameter(comment = "Store the crawl output here")
  public void setOutputCorpus(Corpus outputCorpus) {
    this.outputCorpus = outputCorpus;
  }

  public Corpus getOutputCorpus() {
    return outputCorpus;
  }
  
  @Optional
  @RunTime
  @CreoleParameter(comment = "Pages that don't match at least one keyword will be dropped; leave empty to keep all pages")
  public void setKeywords(List<String> keywords) {
    this.keywords = keywords;
  }
   
  public List<String> getKeywords() {
    return this.keywords;
  }

  
  @RunTime
  @CreoleParameter(comment = "Are keywords case-sensitive?",
          defaultValue = "true")
  public void setKeywordsCaseSensitive(Boolean kcs) {
    this.caseSensitiveKeywords = kcs;
  }
  
  public Boolean getKeywordsCaseSensitive() {
    return this.caseSensitiveKeywords;
  }
  
  @RunTime
  @CreoleParameter(comment = "Convert other XML mime types to text/xml",
          defaultValue = "true")
  public void setConvertXmlTypes(Boolean convert) {
    this.convertXmlTypes = convert;
  }
  
  public Boolean getConvertXmlTypes() {
    return this.convertXmlTypes;
  }
  
  @HiddenCreoleParameter
  public void setDocument(Document x) {
    // NOTHING
  }

  @HiddenCreoleParameter
  public void setCorpus(Corpus x) {
    // NOTHING
  }

}