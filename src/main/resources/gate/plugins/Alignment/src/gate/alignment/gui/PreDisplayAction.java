package gate.alignment.gui;

import gate.alignment.AlignmentActionInitializationException;
import gate.alignment.AlignmentException;

/**
 * Implementers of these are called just before the pair is displayed.
 * 
 * @author niraj
 */
public interface PreDisplayAction {

  /**
   * This method is called before the pair is displayed to the user.
   * 
   * @param pair
   * @throws AlignmentException
   */
  public void executePreDisplayAction(PUAPair pair) throws AlignmentException;

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
