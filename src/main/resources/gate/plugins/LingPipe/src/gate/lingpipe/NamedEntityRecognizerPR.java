package gate.lingpipe;

import gate.AnnotationSet;
import gate.FeatureMap;
import gate.ProcessingResource;
import gate.Resource;
import gate.creole.AbstractLanguageAnalyser;
import gate.creole.ExecutionException;
import gate.creole.ResourceInstantiationException;
import gate.util.GateRuntimeException;
import gate.util.InvalidOffsetException;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

import com.aliasi.chunk.Chunk;
import com.aliasi.chunk.Chunker;
import com.aliasi.chunk.Chunking;
import com.aliasi.util.AbstractExternalizable;

/**
 * This PR is used for recognizing named entities such as location,
 * organizaiton etc. It uses the LingPipe models to achieve that.
 * 
 * @author niraj
 * 
 */
public class NamedEntityRecognizerPR extends AbstractLanguageAnalyser implements
                                                                     ProcessingResource {

  /** File which cotains model for NE */
  protected URL modelFileUrl;

  /** Model file extracted from the URL */
  protected File modelFile;

  /** The name of the annotation set used for input */
  protected String outputASName;

  /** Chunker object */
  protected Chunker chunker;

  /**
   * Initializes this resource
   * 
   * @return Resource
   * @throws ResourceInstantiationException
   */
  public Resource init() throws ResourceInstantiationException {
    if(modelFileUrl == null)
      throw new ResourceInstantiationException("No model file provided!");

    try {
      modelFile = new File(modelFileUrl.toURI());
    }
    catch(URISyntaxException e) {
      throw new ResourceInstantiationException(e);
    }

    if(modelFile == null || !modelFile.exists()) {
      throw new ResourceInstantiationException("modelFile:"
              + modelFileUrl.toString() + " does not exists");
    }

    try {
      chunker = (Chunker)AbstractExternalizable.readObject(modelFile);
    }
    catch(IOException e) {
      throw new ResourceInstantiationException(e);
    }
    catch(ClassNotFoundException e) {
      throw new ResourceInstantiationException(e);
    }

    return this;
  }

  /**
   * Method is executed after the init() method has finished its
   * execution. <BR>
   * 
   * @throws ExecutionException
   */
  public void execute() throws ExecutionException {
    // lets start the progress and initialize the progress counter
    fireProgressChanged(0);

    // If no document provided to process throw an exception
    if(document == null) {
      fireProcessFinished();
      throw new GateRuntimeException("No document to process!");
    }

    // get the annotationSet name provided by the user, or otherwise use
    // the
    // default method
    AnnotationSet outputAs = (outputASName == null || outputASName.trim()
            .length() == 0) ? document.getAnnotations() : document
            .getAnnotations(outputASName);

    try {
      String docText = document.getContent().toString();
      Chunking chunking = chunker.chunk(docText);
      for(Chunk c : chunking.chunkSet()) {
        FeatureMap fm = gate.Factory.newFeatureMap();
        outputAs.add(new Long(c.start()), new Long(c.end()), c.type(), fm);
      }

    }
    catch(InvalidOffsetException e) {
      throw new ExecutionException(e);
    }

    // process finished, acknowledge user about this.
    fireProcessFinished();
  }

  /**
   * Returns the name of the AnnotationSet that has been provided to
   * create the AnnotationSet
   */
  public String getOutputASName() {
    return outputASName;
  }

  /**
   * Sets the AnnonationSet name, that is used to create the
   * AnnotationSet
   * 
   * @param annotationSetName
   */
  public void setOutputASName(String outputAS) {
    this.outputASName = outputAS;
  }

  /**
   * gets the url of the model used for recognizing named entiries in
   * the document.
   * 
   * @return
   */
  public URL getModelFileUrl() {
    return modelFileUrl;
  }

  /**
   * sets the url of the model used for recognizing named entiries in
   * the document.
   * 
   * @param modelFileUrl
   */
  public void setModelFileUrl(URL modelFileUrl) {
    this.modelFileUrl = modelFileUrl;
  }

}
