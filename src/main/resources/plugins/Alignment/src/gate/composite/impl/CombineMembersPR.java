package gate.composite.impl;

import java.util.HashMap;

import gate.*;
import gate.composite.CombiningMethod;
import gate.composite.CombiningMethodException;
import gate.composite.CompositeDocument;
import gate.compound.CompoundDocument;
import gate.creole.AbstractLanguageAnalyser;
import gate.creole.ExecutionException;
import gate.creole.ResourceInstantiationException;

/**
 * A PR that allows combining members of a compound document. The newly created
 * document is an instance of composite document, which becomes another member
 * of the compound document.
 * 
 * @author niraj
 */
public class CombineMembersPR extends AbstractLanguageAnalyser implements
		ProcessingResource {

	private static final long serialVersionUID = 4755458725235429653L;

	/**
	 * Name of the combining method class that is used for combining the
	 * documents.
	 */
	protected String combiningMethod;

	/**
	 * Other accompanying parameters used for the combining method. It must
	 * follow the following format. param1=value;param2=value;
	 */
	protected String parameters;

	/**
	 * Instance of the combining method
	 */
	protected CombiningMethod combiningMethodInst;

	/** Initialise this resource, and return it. */
	public Resource init() throws ResourceInstantiationException {
		try {
			combiningMethodInst = (CombiningMethod) Class.forName(
					combiningMethod).newInstance();
			return this;
		} catch (Exception e) {
			throw new ResourceInstantiationException(e);
		}
	}

	/* this method is called to reinitialize the resource */
	public void reInit() throws ResourceInstantiationException {
		// reinitialization code
		init();
	}

	public void execute() throws ExecutionException {
		if (document == null) {
			throw new ExecutionException("Document is null!");
		}

		if (!(document instanceof CompoundDocument)) {
			throw new ExecutionException(
					"Since the document is not an instance of CompoundDocument, No changes made for the document :"
							+ document.getName());

		}

		HashMap<String, Object> params = new HashMap<String, Object>();
		String[] prms = parameters.split(";");
		if (prms != null) {
			for (int i = 0; i < prms.length; i++) {
				int index = prms[i].indexOf("=");
				if (index < 0)
					throw new ExecutionException("Invalid parameters!");

				String[] keyValue = new String[2];
				keyValue[0] = prms[i].substring(0, index);
				keyValue[1] = prms[i].substring(index + 1, prms[i].length());
				params.put(keyValue[0], keyValue[1]);
			}
		}

		try {
			Document compositeDocument = combiningMethodInst.combine(
					(CompoundDocument) document, params);
			// we need to delete it first, incase if it is already there
			((CompoundDocument) document)
					.removeDocument(CompositeDocument.COMPOSITE_DOC_NAME);
			((CompoundDocument) document).addDocument(
					CompositeDocument.COMPOSITE_DOC_NAME, compositeDocument);
		} catch (CombiningMethodException ex) {
			throw new ExecutionException(ex);
		}

	}

	/**
	 * Gets the set combining method
	 * 
	 * @return
	 */
	public String getCombiningMethod() {
		return combiningMethod;
	}

	/**
	 * Sets the combining method. It is the name of a class that implements the
	 * CombiningMethod interface.
	 * 
	 * @param combiningMethod
	 */
	public void setCombiningMethod(String combiningMethod) {
		this.combiningMethod = combiningMethod;
	}

	/**
	 * Gets the set parameters.
	 * @return
	 */
	public String getParameters() {
		return parameters;
	}

	/**
	 * Sets the combining method. It must
	 * follow the following format. param1=value;param2=value; 
	 * @param parameters
	 */
	public void setParameters(String parameters) {
		this.parameters = parameters;
	}
} // class AlignedDocumentImpl
