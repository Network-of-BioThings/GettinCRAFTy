package gate.compound.impl;

import gate.ProcessingResource;
import gate.Resource;
import gate.compound.CompoundDocument;
import gate.creole.AbstractLanguageAnalyser;
import gate.creole.ExecutionException;
import gate.creole.ResourceInstantiationException;

/**
 * This PR allows switching the focus of the compound document to its various
 * member documents. The currently set document is used for all operations. For
 * example a compound document has two members, hi and en. If users uses this PR
 * to set its focus to hi, all the operations executed on the compound document
 * will be performed on the document with id hi.
 * 
 * @author niraj
 */
public class SwitchMemberPR extends AbstractLanguageAnalyser implements
		ProcessingResource {

	private static final long serialVersionUID = -7350534955434439647L;

	private String documentID;

	/*
	 * this method gets called whenever an object of this class is created
	 * either from GATE GUI or if initiated using Factory.createResource()
	 * method.
	 */
	public Resource init() throws ResourceInstantiationException {
		// here initialize all required variables, and may
		// be throw an exception if the value for any of the
		// mandatory parameters is not provided
		return this;
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

		if (document instanceof CompoundDocument)
			((CompoundDocument) document).setCurrentDocument(documentID);
		else
			System.err
					.println("Since the document is not an instance of CompoundDocument, No changes made for the document :"
							+ document.getName());
	}

	public String getDocumentID() {
		return documentID;
	}

	public void setDocumentID(String language) {
		this.documentID = language;
	}
}