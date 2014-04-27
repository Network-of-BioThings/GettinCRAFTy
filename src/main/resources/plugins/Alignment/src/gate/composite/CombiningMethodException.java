package gate.composite;

import java.io.Serializable;

/**
 * Exception that occurs while combining members of a compound document.
 * 
 * @author niraj
 */
public class CombiningMethodException extends Exception implements Serializable {

	private static final long serialVersionUID = 1147261517821203797L;

	public CombiningMethodException(String message) {
		super(message);
	}

	public CombiningMethodException() {
		super();
	}

	public CombiningMethodException(Exception e) {
		super(e);
	}
}
