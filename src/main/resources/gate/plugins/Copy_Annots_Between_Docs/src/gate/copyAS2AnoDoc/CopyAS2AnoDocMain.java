/*
 *  CopyAS2AnoDocMain.java
 * 
 *  Yaoyong Li 08/10/2007
 *
 *  $Id: CopyAS2AnoDocMain.java, v 1.0 2009-05-10 11:44:16 +0000 yaoyong $
 */
package gate.copyAS2AnoDoc;

import gate.Annotation;
import gate.AnnotationSet;
import gate.Document;
import gate.Factory;
import gate.ProcessingResource;
import gate.creole.AbstractLanguageAnalyser;
import gate.creole.ExecutionException;
import gate.creole.ResourceInstantiationException;
import gate.util.ExtensionFileFilter;
import gate.util.Files;
import gate.util.InvalidOffsetException;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

public class CopyAS2AnoDocMain extends AbstractLanguageAnalyser implements
ProcessingResource {

  URL sourceFilesURL = null;
  private String inputASName;
  private String outputASName;
  private List annotationTypes;
  

  /** Initialise this resource, and return it. */
  public gate.Resource init() throws ResourceInstantiationException {
    return this;
  } // init()

  /**
   * Run the resource.
   * 
   * @throws ExecutionException
   */
  File[] xmlFiles = null;
  boolean [] fileNotUsedYet = null;
  public void execute() throws ExecutionException {
    
    //  now we need to see if the corpus is provided
    if(corpus == null)
      throw new ExecutionException("Provided corpus is null!");

    if(corpus.size() == 0)
      throw new ExecutionException("No Document found in corpus!");
    
    int positionDoc = corpus.indexOf(document);

    // first document in the corpus
    
    if(positionDoc == 0) {
      System.out.println("\n\n------------ new session starts ------------\n");
      System.out.println("Copy the Annotation Set "+inputASName +" from the files in"
        +sourceFilesURL.getPath() + " to the files in the corpus as AS "+ outputASName);
      
      //collect all the file names in the source dir
      ExtensionFileFilter fileFilter = null;
      xmlFiles = Files.fileFromURL(this.sourceFilesURL)
        .listFiles(fileFilter);
      Arrays.sort(xmlFiles, new Comparator<File>() {
        public int compare(File a, File b) {
          return a.getName().compareTo(b.getName());
        }
      });
      fileNotUsedYet = new boolean[xmlFiles.length];
      for(int i=0; i<fileNotUsedYet.length; ++i)
        fileNotUsedYet[i] = true;
    }
    
    //for current document in the corpus, find the corresponding document in the source dir
    int filePos = findCorresFile(document.getName(), xmlFiles, fileNotUsedYet);
    if(filePos<0) {
      System.out.println("Cannot find a corresponding file in the source dir for"
      + " the current document "+document.getName());
      return;
    } else {
      fileNotUsedYet[filePos] = false;
    }
    
    //load the corresponding file to GATE
    Document docCorres;
    try {
      docCorres = Factory.newDocument(xmlFiles[filePos].toURI().toURL(), "UTF-8");
      AnnotationSet sourceAS = null;
      if(inputASName == null || inputASName.length()==0)
        sourceAS = docCorres.getAnnotations();
      else 
        sourceAS = docCorres.getAnnotations(inputASName);
      AnnotationSet asToCopy = null;
      if (annotationTypes != null && annotationTypes.size() > 0) {
        //String [] annTypes = annotationTypes.split(";");
        asToCopy = sourceAS.get(new HashSet(annotationTypes));
        //asToCopy = sourceAS.get();
      } else {
        // transfer everything
        asToCopy = sourceAS.get();
      }
      System.out.println("Copying from "+xmlFiles[filePos].getName() +" to "+ 
        document.getName());
      
      //get the target annotation set
      AnnotationSet targetAS = null;
      if(outputASName == null || outputASName.length()==0)
        targetAS = document.getAnnotations();
      else
        targetAS = document.getAnnotations(outputASName);
      
      //copy the annotations from source file to target file
      for(Object obj:asToCopy) {
        Annotation oneAnn = (Annotation)obj;
        targetAS.add(oneAnn.getStartNode().getOffset(), oneAnn.getEndNode().getOffset(),
          oneAnn.getType(), oneAnn.getFeatures());
      }
      
      Factory.deleteResource(docCorres);

    }
    catch(ResourceInstantiationException e) {
      e.printStackTrace();
    }
    catch(MalformedURLException e) {
      e.printStackTrace();
    }
    catch(InvalidOffsetException e) {
      e.printStackTrace();
    }
   
    
  }
  
  private int findCorresFile(String fileName, File [] xmlFiles, boolean [] notUsedYet) {
    int fpos =-1;
    int sameLenMax = 0;
    char [] fnChars = fileName.toCharArray();
    for(int i=0; i<xmlFiles.length; ++i) {
      //System.out.println(i+", "+xmlFiles[i].getName()+", notusedYet="+notUsedYet[i]+".");
      
      if(!notUsedYet[i]) continue;
      char [] fnXmlFChars = xmlFiles[i].getName().toCharArray();
      int lenStr = fnChars.length;
      if(lenStr>fnXmlFChars.length)
        lenStr = fnXmlFChars.length;
      int sameLen = lenStr;
      for(int j=0; j<lenStr; ++j)
        if(fnChars[j] != fnXmlFChars[j]) {
          sameLen = j;
          break;
        }
      if(sameLenMax <sameLen) {
        fpos = i;
        sameLenMax = sameLen;
      }
    }
    //System.out.println("source file="+xmlFiles[fpos].getName()+", target="+fileName);
    
    return fpos;
  }

  public void setInputASName(String iasn) {
    this.inputASName = iasn;
  }

  public String getInputASName() {
    return this.inputASName;
  }

  public void setOutputASName(String iasn) {
    this.outputASName = iasn;
  }

  public String getOutputASName() {
    return this.outputASName;
  }

  public void setSourceFilesURL(URL modelU) {
    this.sourceFilesURL = modelU;
  }

  public URL getSourceFilesURL() {
    return this.sourceFilesURL;
  }
  
  public List getAnnotationTypes() {
    return this.annotationTypes;
  }

  public void setAnnotationTypes(List newTypes) {
    annotationTypes = newTypes;
  }

}
 