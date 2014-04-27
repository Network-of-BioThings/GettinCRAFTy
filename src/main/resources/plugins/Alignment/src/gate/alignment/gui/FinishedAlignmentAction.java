package gate.alignment.gui;

import gate.alignment.AlignmentActionInitializationException;
import gate.alignment.AlignmentException;

/**
 * Implementers of these are called when user says that the alignment is
 * finished.
 * 
 * @author niraj
 */
public interface FinishedAlignmentAction {

  /**
   * This method is called when user says that the alignment is finished.
   * 
   * @param pair
   * @throws AlignmentException
   */
  public void executeFinishedAlignmentAction(PUAPair pair) throws AlignmentException;

  /**
   * This method should be used for initializing any resources required by the
   * execute() method. This method is called whenever it loaded for the first
   * time.
   * 
   * @param args
   * @throws AlignmentActionInitializationException
   */
  public void init(String[] args) throws AlignmentActionInitializationException;

  /**
   * This method should free up the memory by releasing any resources occupied
   * this method. It is called just before the alignment editor is closed.
   */
  public void cleanup();

}
