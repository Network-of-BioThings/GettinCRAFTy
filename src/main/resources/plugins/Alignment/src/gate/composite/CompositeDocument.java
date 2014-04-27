package gate.composite;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

import gate.Document;
import gate.compound.CompoundDocument;

/**
 * Composite document is a result of combining one or more documents altogether.
 * 
 * @author niraj
 * 
 */
public interface CompositeDocument extends Document {

	/**
	 * Global name for the composite document
	 */
	public static final String COMPOSITE_DOC_NAME = "Composite";

	/**
	 * Returns the combining Method used for creating the composite document.
	 * @return
	 */
	public CombiningMethod getCombiningMethod();

	/**
	 * Sets the combining method used for creating the composite document.
	 * @param combiningMethod
	 */
	public void setCombiningMethod(CombiningMethod combiningMethod);
	
	/**
	 * This method returns the original offset in its source document
	 * @param srcDocumentID
	 * @param offset
	 * @return -1 if the provided offset has no linking with the sourceDocument
	 */
	public long getOffsetInSrcDocument(String srcDocumentID, long offset);

	
	public void setOffsetMappingInformation(HashMap<String, List<OffsetDetails>> offsetMappings);
	
	/**
	 * return the IDs of combined documents
	 * @return
	 */
	public Set<String> getCombinedDocumentsIds();
	
	/**
	 * Sets the combined documents IDs
	 * @param combinedDocumentsIds
	 */
	public void setCombinedDocumentsIds(Set<String> combinedDocumentsIds);

	/**
	 * This method returns the compoundDocument whose member this composite document is.
	 * @return
	 */
	public CompoundDocument getCompoundDocument();
	
	public void setCompoundDocument(CompoundDocument compoundDocument);
	
}
