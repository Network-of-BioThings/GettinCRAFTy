package gate.compound.impl;

import gate.Annotation;
import gate.AnnotationSet;
import gate.Document;
import gate.util.InvalidOffsetException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * AnnotationStream is a helper class that helps in combining multiple documents
 * into a single composite document. It takes various parameters and provides an
 * iterator over annotations of the document.
 * 
 * @author niraj
 */
public class AnnotationStream implements Serializable {

	private static final long serialVersionUID = 3761967168283554616L;

	private ArrayList<Annotation> list;

	private int counter = 0;

	private Document doc;

	private String language;

	private String annotationSet;

	/**
	 * Constructor
	 * 
	 * @param doc -
	 *            document for which the annotation stream to be created
	 * @param annotationSet -
	 *            inputAnnotationSetName from which the annotation stream takes
	 *            its input
	 * @param unitAnnotationType -
	 *            type of annotation to be retrieved from the provided
	 *            annotation set
	 * @param language -
	 *            what is the language of the document
	 * @param comparator -
	 *            annotations are sorted and returned in the specified order.
	 */
	public AnnotationStream(Document doc, String annotationSet,
			String unitAnnotationType, String language, Comparator comparator) {
		this.doc = doc;
		this.language = language;
		this.annotationSet = annotationSet;

		AnnotationSet set = annotationSet == null
				|| annotationSet.trim().length() == 0 ? doc.getAnnotations()
				: doc.getAnnotations(annotationSet);
		set = set.get(unitAnnotationType);
		if (set == null) {
			list = null;
		} else {
			list = new ArrayList<Annotation>(set);
			Collections.sort(list, comparator);
			counter = 0;
		}
	}

	public String getLanguage() {
		return this.language;
	}

	/**
	 * Gets the next annotation in the stream.
	 * 
	 * @return
	 */
	public Annotation next() {
		if (list == null)
			return null;
		if (counter < list.size()) {
			counter++;
			Annotation ann = list.get(counter - 1);
			return ann;
		}
		return null;
	}

	/**
	 * Gets the underlying text of the annotation.
	 * 
	 * @param ann
	 * @return
	 * @throws InvalidOffsetException
	 */
	public String getText(Annotation ann) throws InvalidOffsetException {
		return doc.getContent().getContent(ann.getStartNode().getOffset(),
				ann.getEndNode().getOffset()).toString();
	}

	/**
	 * Gets all the contained annotations within the boundaries of the given
	 * annotation.
	 * 
	 * @param ann
	 * @return
	 */
	public AnnotationSet getUnderlyingAnnotations(Annotation ann) {
		AnnotationSet set = annotationSet == null
				|| annotationSet.trim().length() == 0 ? doc.getAnnotations()
				: doc.getAnnotations(annotationSet);
		return set.getContained(ann.getStartNode().getOffset(), ann
				.getEndNode().getOffset());
	}
}
