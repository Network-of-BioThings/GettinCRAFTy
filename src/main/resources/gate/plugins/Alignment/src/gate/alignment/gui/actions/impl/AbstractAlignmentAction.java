package gate.alignment.gui.actions.impl;

import javax.swing.Icon;
import gate.alignment.AlignmentActionInitializationException;
import gate.alignment.gui.AlignmentAction;

/**
 * Abstract class that provides default implementation of some of the
 * methods declared in the AlignmentAction interface.
 * 
 * @author niraj
 * 
 */
public abstract class AbstractAlignmentAction implements AlignmentAction {

  public void init(String[] args) throws AlignmentActionInitializationException {
    // nothing to do
  }

  public void cleanup() {
    // do nothing
  }

  /**
   * true by default
   */
  public boolean invokeForAlignedAnnotation() {
    return true;
  }

  /**
   * true by default
   */
  public boolean invokeForHighlightedUnalignedAnnotation() {
    return true;
  }

  /**
   * true by default
   */
  public boolean invokeForUnhighlightedUnalignedAnnotation() {
    return true;
  }

  /**
   * false by default
   */
  public boolean invokeWithAlignAction() {
    return false;
  }

  /**
   * false by default
   */
  public boolean invokeWithRemoveAction() {
    return false;
  }

  /**
   * no icon (null) by default
   */
  public Icon getIcon() {
    return null;
  }

  /**
   * no icon path (null) by default
   */
  public String getIconPath() {
    return null;
  }

}
