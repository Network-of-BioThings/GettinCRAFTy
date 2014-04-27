package gate.alignment;

import java.io.Serializable;

/**
 * Exception that could occur while aligning annotations with one other
 * 
 * @author niraj
 * 
 */
public class AlignmentException extends Exception implements Serializable {

  private static final long serialVersionUID = 1147261517821203797L;

  public AlignmentException(String message, Throwable t) {
    super(message, t);
  }

  public AlignmentException(String message) {
    super(message);
  }

  public AlignmentException() {
    super();
  }

  public AlignmentException(Exception e) {
    super(e);
  }
}
