package gate.compound;

/**
 * Event that indicates the removal or addition of a document to the compound
 * document.
 * 
 * @author niraj
 * 
 */
public class CompoundDocumentEvent {

	/**
	 * A source compound document, where a document is added or deleted.
	 */
	private CompoundDocument source;

	/**
	 * ID of the document which is either added to or deleted from the source
	 * compound document.
	 */
	private String documentID;

	/**
	 * Constructor
	 * 
	 * @param source -
	 *            a source compound document, where a document is added or
	 *            deleted.
	 * @param documentID -
	 *            ID of the added or deleted document.
	 */
	public CompoundDocumentEvent(CompoundDocument source, String documentID) {
		this.source = source;
		this.documentID = documentID;
	}

	/**
	 * Gets the source compound document where a document is added to it or
	 * deleted from.
	 * 
	 * @return
	 */
	public CompoundDocument getSource() {
		return source;
	}

	/**
	 * Gets the id of the added or deleted document.
	 * 
	 * @return
	 */
	public String getDocumentID() {
		return documentID;
	}
}
