/*
 * MediaWikiDocumentFormat.java
 *
 * Copyright (c) 2012, The University of Sheffield. See the file COPYRIGHT.txt
 * in the software or at http://gate.ac.uk/gate/COPYRIGHT.txt
 *
 * This file is part of GATE (see http://gate.ac.uk/), and is free software,
 * licenced under the GNU Library General Public License, Version 2, June 1991
 * (in the distribution as file licence.html, and also available at
 * http://gate.ac.uk/gate/licence.html).
 *
 * Mark A. Greenwood, 31/10/2012
 */

package gate.corpora;

import java.net.URL;
import java.util.Map;

import org.apache.commons.lang.StringEscapeUtils;

import gate.Document;
import gate.Resource;
import gate.creole.ResourceInstantiationException;
import gate.creole.metadata.AutoInstance;
import gate.creole.metadata.CreoleResource;
import gate.util.DocumentFormatException;
import info.bliki.wiki.model.WikiModel;

/**
 * A document format for parsing MediaWiki markup. The format converts
 * MediaWiki markup into HTML which is then passed to the standard HTML
 * document format for final parsing into a GATE document. The format is
 * activated by specifying the text/x-mediawiki MIME type.
 * 
 * @author Mark A. Greenwood
 */
@CreoleResource(name = "MediaWiki Document Format", isPrivate = true,
    autoinstances = {@AutoInstance(hidden = true)})
public class MediaWikiDocumentFormat extends NekoHtmlDocumentFormat {

  /**
   * so that we don't end up with a document littered with unparsed "magic words" we
   * we need a custom model that we can use to filter them out
   */
  private final WikiModel model = new WikiModel("${image}", "${title}") {
    @Override
    public String getRawWikiContent(String namespace, String articleName,
      Map<String, String> templateParameters) {
        String rawContent = super.getRawWikiContent(namespace, articleName, templateParameters);

        if (rawContent == null){
          //if we return 'null' then the magic variables end up in the doc with
          //full markup which isn't really what we want, so we just return the
          //empty string instead to remove them entirely from the document
          return "";
        }
        else {
          return rawContent;
        }
      }
  };
  
  @Override
  public Boolean supportsRepositioning() {
    return false;
  }

  @Override
  public Resource init() throws ResourceInstantiationException {
    
    // create the MIME type object
    MimeType mime = new MimeType("text","x-mediawiki");
    
    // Register the class handler for this mime type
    mimeString2ClassHandlerMap.put(mime.getType()+ "/" + mime.getSubtype(), this);
    
    // Register the mime type with mine string
    mimeString2mimeTypeMap.put(mime.getType() + "/" + mime.getSubtype(), mime);
    
    // Set the mimeType for this language resource
    setMimeType(mime);
    
    return this;
  }

  @Override
  public void unpackMarkup(final Document doc) throws DocumentFormatException {
        
    try {
      
      // get the model ready for parsing
      model.setUp();    

      //unescape any HTML entities so that the MediaWiki parser can process
      //them properly otherwise they get ignored and passed straight through
      String unescaped = StringEscapeUtils.unescapeHtml(doc.getContent().toString());
      
      // convert the mediawiki markup to HTML
      String htmlText = model.render(unescaped);
      
      // use the HTML to update the document content
      doc.setContent(new DocumentContentImpl(htmlText));
      
      // we need to nullify the source URL otherwise the HTML doc format we
      // are about to use will re-parse the original document which will
      // undo everything we have just done    
      URL url = doc.getSourceUrl();
      doc.setSourceUrl(null);
      
      // now let the HTML unpacker also do its job
      super.unpackMarkup(doc);
      
      // now we can put the URL back as it won't mess anything up
      doc.setSourceUrl(url);
    }
    finally {
      // signal that, at least for now, we have finished with the model
      model.tearDown();
    }
  }
 
}
