package gate.composite;

import java.io.Serializable;
import gate.Annotation;


/**
 * OffsetDetails is a utility class that is used by composite document to keep
 * the mapping of annotations in their original documents to the ones copied in
 * the composite document.
 * 
 * @author niraj
 */
public class OffsetDetails implements Serializable {
	private static final long serialVersionUID = 3256446923450888500L;

	protected long oldStartOffset;

	protected long oldEndOffset;

	protected long newStartOffset;

	protected long newEndOffset;

	// optional
	protected Annotation originalAnnotation;

	// optional
	protected Annotation newAnnotation;
	
	// name of the annotation set annotations belong to
	protected String asName;
	
	/**
	 * Name of the annotation set
	 * @return
	 */
	public String getAsName() {
    return asName;
  }

	/**
	 * Name of the annotation set
	 * @param asName
	 */
  public void setAsName(String asName) {
    this.asName = asName == null ? null : asName.intern();
  }

  /**
	 * Gets the new end offset
	 * @return
	 */
	public long getNewEndOffset() {
		return newEndOffset;
	}

	/**
	 * Sets the new end offset
	 * @param newEndOffset
	 */
	public void setNewEndOffset(long newEndOffset) {
		this.newEndOffset = newEndOffset;
	}

	/**
	 * Gets the new start offset
	 * @return
	 */
	public long getNewStartOffset() {
		return newStartOffset;
	}

	/**
	 * Sets the new start offset
	 * @param newStartOffset
	 */
	public void setNewStartOffset(long newStartOffset) {
		this.newStartOffset = newStartOffset;
	}

	/**
	 * Gets the old end offset
	 * @return
	 */
	public long getOldEndOffset() {
		return oldEndOffset;
	}

	/**
	 * Sets the old end offset
	 * @param oldEndOffset
	 */
	public void setOldEndOffset(long oldEndOffset) {
		this.oldEndOffset = oldEndOffset;
	}

	/**
	 * Gets the old start offset
	 * @return
	 */
	public long getOldStartOffset() {
		return oldStartOffset;
	}

	/**
	 * Sets the old start offset
	 * @param oldStartOffset
	 */
	public void setOldStartOffset(long oldStartOffset) {
		this.oldStartOffset = oldStartOffset;
	}

  public Annotation getOriginalAnnotation() {
    return originalAnnotation;
  }

  public void setOriginalAnnotation(Annotation originalAnnotation) {
    this.originalAnnotation = originalAnnotation;
  }

  public Annotation getNewAnnotation() {
    return newAnnotation;
  }

  public void setNewAnnotation(Annotation newAnnotation) {
    this.newAnnotation = newAnnotation;
  }
}
