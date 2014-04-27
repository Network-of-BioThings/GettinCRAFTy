/*
 *  Copyright (c) 2008--2012, The University of Sheffield. See the file
 *  COPYRIGHT.txt in the software or at http://gate.ac.uk/gate/COPYRIGHT.txt
 *
 *  This file is part of GATE (see http://gate.ac.uk/), and is free
 *  software, licenced under the GNU Library General Public License,
 *  Version 2, June 1991 (in the distribution as file licence.html,
 *  and also available at http://gate.ac.uk/gate/licence.html).
 *
 *  $Id: CsvFileSelectionActionListener.java 16296 2012-11-20 12:17:11Z adamfunk $
 */
package gate.termraider.gui;

import gate.util.GateException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import gate.termraider.util.*;
import java.io.*;


public class CsvFileSelectionActionListener implements ActionListener {
  
  public enum Mode {LOAD, SAVE};

  private JFileChooser chooser;
  private AbstractBank termbank;
  private SliderPanel sliderPanel;
  private JDialog dialog;
  private Mode mode;
  
  public CsvFileSelectionActionListener(JFileChooser chooser, AbstractBank termbank, SliderPanel sliderPanel, JDialog dialog, Mode mode) {
    this.chooser = chooser;
    this.termbank = termbank;
    this.sliderPanel = sliderPanel;
    this.dialog = dialog;
    this.mode = mode;
  }
  
  @Override
  public void actionPerformed(ActionEvent event) {
    if (event.getActionCommand().equals(JFileChooser.APPROVE_SELECTION)) {
      File file = Utilities.addExtensionIfNotExtended(chooser.getSelectedFile(),
              Utilities.EXTENSION_CSV);
      try {
        if (mode == Mode.SAVE) {
          termbank.saveAsCsv(sliderPanel.getValues(), file);
        }
        else { // must be LOAD
          System.err.println("LOAD mode is no longer supported.");
        }
      }
      catch(GateException e) {
        e.printStackTrace();
      }
      finally {
        dialog.dispose();
      }
    }
    else if (event.getActionCommand().equals(JFileChooser.CANCEL_SELECTION)) {
      dialog.dispose();
    }
  }
}
