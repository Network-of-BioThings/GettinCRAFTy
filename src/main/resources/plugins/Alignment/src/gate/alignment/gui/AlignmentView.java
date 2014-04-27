package gate.alignment.gui;

public interface AlignmentView {

  public void executeAction(AlignmentAction action);
  public void updateGUI(PUAPair pair);
  public void setTarget(AlignmentTask alignmentTask);
  public void clearLatestAnnotationsSelection();
}
