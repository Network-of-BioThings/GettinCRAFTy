/*
 *  Copyright (c) 2008--2012, The University of Sheffield. See the file
 *  COPYRIGHT.txt in the software or at http://gate.ac.uk/gate/COPYRIGHT.txt
 *
 *  This file is part of GATE (see http://gate.ac.uk/), and is free
 *  software, licenced under the GNU Library General Public License,
 *  Version 2, June 1991 (in the distribution as file licence.html,
 *  and also available at http://gate.ac.uk/gate/licence.html).
 *
 *  $Id: ActionSaveCsv.java 16296 2012-11-20 12:17:11Z adamfunk $
 */
package gate.termraider.gui;

import gate.gui.MainFrame;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import javax.swing.*;
import gate.termraider.gui.CsvFileSelectionActionListener.Mode;
import gate.termraider.util.*;
import javax.swing.filechooser.FileNameExtensionFilter;


/**
 * Action class for saving RDF-XML from the GATE GUI.
 */
public class ActionSaveCsv
    extends AbstractAction {
    
  private static final long serialVersionUID = 8086944391438384470L;

  private AbstractBank termbank;

  public ActionSaveCsv(String label, AbstractBank termbank) {
    super(label);
    this.termbank = termbank;
  }

  public void actionPerformed(ActionEvent ae) {
    JDialog saveDialog = new JDialog(MainFrame.getInstance(), "Save Termbank as CSV", true);
    MainFrame.getGuiRoots().add(saveDialog);
    saveDialog.setLayout(new BorderLayout());
    SliderPanel sliderPanel = new SliderPanel(termbank, "save", true, null);
    saveDialog.add(sliderPanel, BorderLayout.CENTER);

    JPanel chooserPanel = new JPanel();
    chooserPanel.setLayout(new BoxLayout(chooserPanel, BoxLayout.Y_AXIS));
    chooserPanel.add(new JSeparator());
    
    JFileChooser chooser = new JFileChooser();
    FileNameExtensionFilter filter = new FileNameExtensionFilter("CSV files", Utilities.EXTENSION_CSV);
    chooser.setFileFilter(filter);
    chooser.setApproveButtonText("Save");
    chooser.addActionListener(new CsvFileSelectionActionListener(chooser, termbank, sliderPanel, saveDialog, Mode.SAVE));
    chooserPanel.add(chooser);
    saveDialog.add(chooserPanel, BorderLayout.SOUTH);
    saveDialog.pack();
    saveDialog.setLocationRelativeTo(saveDialog.getOwner());
    saveDialog.setVisible(true);
  }
}


