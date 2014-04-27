package gate.alignment.gui;

import java.io.Serializable;

/**
 * Exception that could occur while iterating over pairs for alignment.
 * 
 * @author niraj
 */
public class IteratingMethodException extends Exception implements Serializable {

	private static final long serialVersionUID = 1147261517821203797L;

	/**
	 * constructor
	 * @param message
	 */
	public IteratingMethodException(String message) {
		super(message);
	}

	/**
	 * constructor
	 */
	public IteratingMethodException() {
		super();
	}

	/**
	 * constructor
	 * @param e
	 */
	public IteratingMethodException(Exception e) {
		super(e);
	}
}
