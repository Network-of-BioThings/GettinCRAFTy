package gate.alignment;

import java.io.Serializable;

/**
 * Exception that could occur while initializing AlilgnmentAction
 * 
 * @author niraj
 * 
 */
public class AlignmentActionInitializationException extends Exception implements
                                                                     Serializable {

  private static final long serialVersionUID = 1147261517821203797L;

  public AlignmentActionInitializationException(String message, Throwable t) {
    super(message, t);
  }

  public AlignmentActionInitializationException(String message) {
    super(message);
  }

  public AlignmentActionInitializationException() {
    super();
  }

  public AlignmentActionInitializationException(Exception e) {
    super(e);
  }
}
