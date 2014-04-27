/*
 *  Copyright (c) 2008--2012, The University of Sheffield. See the file
 *  COPYRIGHT.txt in the software or at http://gate.ac.uk/gate/COPYRIGHT.txt
 *
 *  This file is part of GATE (see http://gate.ac.uk/), and is free
 *  software, licenced under the GNU Library General Public License,
 *  Version 2, June 1991 (in the distribution as file licence.html,
 *  and also available at http://gate.ac.uk/gate/licence.html).
 *
 *  $Id: SliderPanel.java 16296 2012-11-20 12:17:11Z adamfunk $
 */
package gate.termraider.gui;

import java.awt.FlowLayout;
import java.util.Hashtable;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import gate.termraider.util.*;


public class SliderPanel extends JPanel {
  
  private static final long serialVersionUID = -6864836321387988786L;
  
  private AbstractBank scoredbank;
  private JSlider slider;
  
  
  public SliderPanel(AbstractBank scoredbank, String verb, boolean startLeft,
          TermbankViewer viewer) {
    this.scoredbank = scoredbank;
    this.setLayout(new FlowLayout());
    
    JLabel typeLabel = new JLabel("Score cut-off");
    double min = Math.floor(this.scoredbank.getMinScore());
    double max = Math.ceil(this.scoredbank.getMaxScore());
    
    if (max - min < 1.0) {
      max = max + 1.0;
      min = min - 1.0;
    }
    else if (max - min < 2.0) {
      min = min - 1.0;
    }
    
    int imin = (int) min;
    int imax = (int) max - 1;
    
    int middle = (int) Math.floor((min + max) / 2);
    slider = new JSlider(imin, imax);
    slider.setToolTipText("minimum value to " + verb);
    slider.setPaintTicks(false);
    
    Hashtable<Integer, JLabel> labelTable = new Hashtable<Integer, JLabel>();
    labelTable.put(imin, intLabel(imin));
    labelTable.put(middle, intLabel(middle));
    labelTable.put(imax, intLabel(imax));
    slider.setLabelTable(labelTable);
    slider.setPaintLabels(true);
    
    Box labelBox = Box.createHorizontalBox();
    JLabel valueLabel = new JLabel(Integer.toString(middle));
    valueLabel.setBorder(new EtchedBorder(EtchedBorder.RAISED));
    labelBox.add(Box.createHorizontalGlue());
    labelBox.add(typeLabel);
    labelBox.add(Box.createHorizontalGlue());
    labelBox.add(valueLabel);
    labelBox.add(Box.createHorizontalGlue());
    Box typeSlideBox = Box.createVerticalBox();
    typeSlideBox.add(labelBox);
    typeSlideBox.add(slider);
    this.add(typeSlideBox);
    SliderChangeListener listener = new SliderChangeListener(slider, valueLabel, viewer);
    slider.addChangeListener(listener);
    if (startLeft) {
      slider.setValue(slider.getMinimum());
    }
  }

  
  public void reformat() {
    this.validate();
    this.repaint();
  }
  
  
  public Double getValues() {
    return Double.valueOf(slider.getValue());
  }
  
  public static JLabel intLabel(int i) {
    return new JLabel(Integer.toString(i));
  }
  
}

class SliderChangeListener implements ChangeListener {
  private JSlider slider;
  private JLabel valueLabel;
  private TermbankViewer viewer;

  /**
   * Set viewer to null for the "save" GUIs, so the slider doesn't 
   * change the tree; set viewer "correctly" for the main GUI, 
   * so it does update the tree. 
   */
  public SliderChangeListener(JSlider slider, JLabel valueLabel, TermbankViewer viewer) {
    this.slider = slider;
    this.valueLabel = valueLabel;
    this.viewer = viewer;
  }

  public void stateChanged(ChangeEvent e) {
    int value = slider.getValue();
    this.valueLabel.setText(Integer.toString(value));
    if (this.viewer != null) {
      this.viewer.regenerateTree();
    }
  }
}
