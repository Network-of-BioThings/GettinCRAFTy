package gate.compound;

/**
 * Programs wishing to listen to the events of document being added and removed
 * to the compound document should implement this listener.
 * 
 * @author niraj
 * 
 */
public interface CompoundDocumentListener {
	/**
	 * This method is called whenever a document is added to the compound document.
	 * @param event
	 */
	public void documentAdded(CompoundDocumentEvent event);

	/**
	 * This method is called whenever a document is removed from the compound document.
	 * @param event
	 */
	public void documentRemoved(CompoundDocumentEvent event);
}
