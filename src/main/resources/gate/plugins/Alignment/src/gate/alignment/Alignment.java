package gate.alignment;

import gate.Annotation;
import gate.Document;
import gate.compound.CompoundDocument;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * This class stores all the alignment information about a document. It
 * provides various methods to know which annotation is aligned with
 * which annotations and what is the source document of each annotation.
 * 
 * @author niraj
 */
public class Alignment implements Serializable {

  private static final long serialVersionUID = 3977299936398488370L;

  public static int counter = 0;

  /**
   * a map that stores information about annotation alignment. As a key,
   * a source annotation and as value, a set of aligned annotations to
   * the source annotation are stored.
   */
  protected Map<Annotation, Set<Annotation>> alignmentMatrix;

  /**
   * For each annotation we store the information about its annotation
   * set. This is used for letting the user know which annotation set
   * the given annotation belongs to.
   */
  protected Map<Annotation, String> annotation2Document;

  /**
   * which annotation belongs to what annotation set
   */
  protected Map<Annotation, String> annotation2AS;

  /**
   * all the alignment listeners that wish to listen to vairous
   * alignment events
   */
  protected transient List<AlignmentListener> listeners = new ArrayList<AlignmentListener>();

  /**
   * the document this alignment object belongs to.
   */
  protected transient CompoundDocument compoundDocument;

  /**
   * A feature that PRs can use to specify which method was used to
   * align that particular annotation.
   */
  public static final String ALIGNMENT_METHOD_FEATURE_NAME = "align-method";

  /**
   * Constructor
   */
  public Alignment(CompoundDocument compoundDocument) {
    this.compoundDocument = compoundDocument;
    alignmentMatrix = new HashMap<Annotation, Set<Annotation>>();
    annotation2Document = new HashMap<Annotation, String>();
    annotation2AS = new HashMap<Annotation, String>();
    counter++;
  }

  /**
   * Sets the source document, this alignment object belongs to.
   * 
   * @param cd
   */
  public void setSourceDocument(CompoundDocument cd) {
    this.compoundDocument = cd;
  }

  /**
   * Returns if two annotations are aligned with each other.
   * 
   * @param srcAnnotation
   * @param targetAnnotation
   * @return
   */
  public boolean areTheyAligned(Annotation srcAnnotation,
          Annotation targetAnnotation) {
    Set<Annotation> alignedTo = alignmentMatrix.get(srcAnnotation);
    if(alignedTo == null || alignedTo.isEmpty())
      return false;
    else return alignedTo.contains(targetAnnotation);
  }

  /**
   * Aligns the given source annotation with the given target
   * annotation.
   * 
   * @param srcAnnotation
   * @param srcDocument
   * @param targetAnnotation
   * @param targetDocument
   */
  public void align(Annotation srcAnnotation, String srcAS,
          Document srcDocument, Annotation targetAnnotation, String tgtAS,
          Document targetDocument) {

    if(srcAnnotation == null || targetAnnotation == null) return;
    if(areTheyAligned(srcAnnotation, targetAnnotation)) return;

    Set<Annotation> alignedToT = alignmentMatrix.get(srcAnnotation);
    if(alignedToT == null) {
      alignedToT = new HashSet<Annotation>();
      alignmentMatrix.put(srcAnnotation, alignedToT);
    }
    Set<Annotation> alignedToS = alignmentMatrix.get(targetAnnotation);
    if(alignedToS == null) {
      alignedToS = new HashSet<Annotation>();
      alignmentMatrix.put(targetAnnotation, alignedToS);
    }

    alignedToT.add(targetAnnotation);
    annotation2Document.put(srcAnnotation, srcDocument.getName());
    annotation2AS.put(srcAnnotation, srcAS);

    alignedToS.add(srcAnnotation);
    annotation2Document.put(targetAnnotation, targetDocument.getName());
    annotation2AS.put(targetAnnotation, tgtAS);

    fireAnnotationsAligned(srcAnnotation, srcAS, srcDocument, targetAnnotation,
            tgtAS, targetDocument);
  }

  /**
   * Aligns the given source annotation with the given target
   * annotation.
   * 
   * @param srcAnnotation
   * @param srcDocument
   * @param targetAnnotation
   * @param targetDocument
   */
  public void unalign(Annotation srcAnnotation, String srcAS,
          Document srcDocument, Annotation targetAnnotation, String tgtAS,
          Document targetDocument) {

    if(srcAnnotation == null || targetAnnotation == null) return;
    if(!areTheyAligned(srcAnnotation, targetAnnotation)) return;

    Set<Annotation> alignedToT = alignmentMatrix.get(srcAnnotation);
    Set<Annotation> alignedToS = alignmentMatrix.get(targetAnnotation);

    if(alignedToT != null) {
      alignedToT.remove(targetAnnotation);
      if(alignedToT.isEmpty()) {
        alignmentMatrix.remove(srcAnnotation);
        annotation2Document.remove(srcAnnotation);
        annotation2AS.remove(srcAnnotation);
      }
      else {
        alignmentMatrix.put(srcAnnotation, alignedToT);
      }
    }

    if(alignedToS != null) {
      alignedToS.remove(srcAnnotation);
      if(alignedToS.isEmpty()) {
        alignmentMatrix.remove(targetAnnotation);
        annotation2Document.remove(targetAnnotation);
        annotation2AS.remove(targetAnnotation);
      }
      else {
        alignmentMatrix.put(targetAnnotation, alignedToS);
      }
    }
    fireAnnotationsUnAligned(srcAnnotation, srcAS, srcDocument,
            targetAnnotation, tgtAS, targetDocument);
  }

  /**
   * Returns a set of aligned annotations.
   * 
   * @return
   */
  public Set<Annotation> getAlignedAnnotations() {
    Set<Annotation> annots = alignmentMatrix.keySet();
    if(annots == null)
      return new HashSet<Annotation>();
    else {
      return new HashSet<Annotation>(annots);
    }
  }

  /**
   * This method tells which document the given annotation belongs to.
   * 
   * @param annotation
   * @return
   */
  public Document getDocument(Annotation annotation) {
    return compoundDocument.getDocument(annotation2Document.get(annotation));
  }

  public String getAnnotationSetName(Annotation annotation) {
    return annotation2AS.get(annotation);
  }

  /**
   * Given the annotation, this method returns a set of the aligned
   * annotations to that annotation.
   * 
   * @param srcAnnotation
   * @return
   */
  public Set<Annotation> getAlignedAnnotations(Annotation srcAnnotation) {
    Set<Annotation> annots = alignmentMatrix.get(srcAnnotation);
    if(annots != null)
      return new HashSet<Annotation>(annots);
    else return new HashSet<Annotation>();
  }

  /**
   * This method tells whether the given annotation is aligned or not.
   * 
   * @param srcAnnotation
   * @return
   */
  public boolean isAnnotationAligned(Annotation srcAnnotation) {
    Set<Annotation> alignedTo = alignmentMatrix.get(srcAnnotation);
    if(alignedTo == null || alignedTo.isEmpty())
      return false;
    else {
      return !alignedTo.isEmpty();
    }
  }

  /**
   * adds a new member who wants to listens to alignment events
   * 
   * @param listener
   */
  public void addAlignmentListener(AlignmentListener listener) {
    if(this.listeners == null) {
      this.listeners = new ArrayList<AlignmentListener>();
    }
    if(listener != null) this.listeners.add(listener);
  }

  /**
   * removes the given listener from the list of listeners who want to
   * listens to alignment events
   * 
   * @param listener
   */
  public void removeAlignmentListener(AlignmentListener listener) {
    if(this.listeners == null) {
      this.listeners = new ArrayList<AlignmentListener>();
    }

    if(listener != null) this.listeners.remove(listener);
  }

  /**
   * calls the annotationsAligned(...) method on each of the registered
   * listeners
   * 
   * @param srcAnnotation
   * @param srcAS
   * @param srcDocument
   * @param targetAnnotation
   * @param tgtAS
   * @param targetDocument
   */
  protected void fireAnnotationsAligned(Annotation srcAnnotation, String srcAS,
          Document srcDocument, Annotation targetAnnotation, String tgtAS,
          Document targetDocument) {
    if(listeners == null) {
      listeners = new ArrayList<AlignmentListener>();
    }
    for(AlignmentListener aListener : listeners) {
      aListener.annotationsAligned(srcAnnotation, srcAS, srcDocument,
              targetAnnotation, tgtAS, targetDocument);
    }
  }

  /**
   * calls the annotationsUnaligned(...) method on each of the resitered
   * listeners
   * 
   * @param srcAnnotation
   * @param srcAS
   * @param srcDocument
   * @param targetAnnotation
   * @param tgtAS
   * @param targetDocument
   */
  protected void fireAnnotationsUnAligned(Annotation srcAnnotation,
          String srcAS, Document srcDocument, Annotation targetAnnotation,
          String tgtAS, Document targetDocument) {
    if(listeners == null) {
      listeners = new ArrayList<AlignmentListener>();
    }
    for(AlignmentListener aListener : listeners) {
      aListener.annotationsUnaligned(srcAnnotation, srcAS, srcDocument,
              targetAnnotation, tgtAS, targetDocument);
    }
  }

  /**
   * Returns a list of registered listeners
   * 
   * @return
   */
  public List<AlignmentListener> getAlignmentListeners() {
    if(listeners == null) {
      listeners = new ArrayList<AlignmentListener>();
    }
    return new ArrayList<AlignmentListener>(this.listeners);
  }
}
