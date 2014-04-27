/*
 *  Copyright (c) 2008--2012, The University of Sheffield. See the file
 *  COPYRIGHT.txt in the software or at http://gate.ac.uk/gate/COPYRIGHT.txt
 *
 *  This file is part of GATE (see http://gate.ac.uk/), and is free
 *  software, licenced under the GNU Library General Public License,
 *  Version 2, June 1991 (in the distribution as file licence.html,
 *  and also available at http://gate.ac.uk/gate/licence.html).
 *
 *  $Id: PairbankViewer.java 16296 2012-11-20 12:17:11Z adamfunk $
 */
package gate.termraider.gui;

import java.awt.BorderLayout;
import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import gate.Resource;
import gate.creole.*;
import gate.creole.metadata.*;
import gate.event.ProgressListener;
import java.util.*;
import gate.termraider.bank.*;
import gate.termraider.util.*;


@CreoleResource(name = "Pairbank Viewer",
        comment = "viewer for the TermRaider Pairbank",
        guiType = GuiType.LARGE,
        mainViewer = true,
        resourceDisplayed = "gate.termraider.bank.AbstractPairbank")
public class PairbankViewer 
  extends AbstractVisualResource 
  implements ANNIEConstants, ProgressListener {

  private static final long serialVersionUID = -8174187050175560886L;

  private AbstractPairbank pairbank;
  private JTabbedPane tabbedPane;
  private JTable scoreTable;
  private JScrollPane scoreTablePane;
  private PairTableModel scoreTableModel;
  
  
  @Override
  public Resource init() {
    initGuiComponents();
    return this;
  }



  private void initGuiComponents() {
    setLayout(new BorderLayout());
    tabbedPane = new JTabbedPane();
  }
  
  private void generate() {
    JPanel tableTab = new JPanel(new BorderLayout());
    tabbedPane.addTab("Scores", tableTab);
    
    scoreTableModel = new PairTableModel(pairbank);
    scoreTable = new JTable(scoreTableModel);
    scoreTable.setAutoCreateRowSorter(true);
    scoreTablePane = new JScrollPane(scoreTable, 
            JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, 
            JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    tableTab.add(scoreTablePane, BorderLayout.CENTER);
    
    if (pairbank instanceof PMIBank) {
      JPanel debugger = new PMIDebugger(pairbank);
      tabbedPane.addTab("Details", debugger);
    }
    
    this.add(tabbedPane, BorderLayout.CENTER);
    tabbedPane.validate();
    tabbedPane.repaint();
  }
  
  

  public void processFinished() {
    setTarget(pairbank);
  }

  public void progressChanged(int i) {
    // nothing?
  }

  public void setTarget(Object target) {
    if(target == null || ! (target instanceof AbstractPairbank)) {
      throw new IllegalArgumentException("This Viewer cannot show a "
              + (target == null ? "null" : target.getClass().toString()));
    }
    
    pairbank = (AbstractPairbank) target;
    this.generate();
  }
  
}


class PairTableModel extends AbstractTableModel {
  
  private static final long serialVersionUID = 8824583674024239907L;
  
  private List<UnorderedTermPair> pairs;
  private Map<UnorderedTermPair, Double> scores; 

  public PairTableModel(AbstractPairbank pairbank) {
    this.pairs = new ArrayList<UnorderedTermPair>(pairbank.getPairs());
    this.scores = pairbank.getScores();
  }
  
  public int getColumnCount() {
    return 3;
  }

  public int getRowCount() {
    return this.pairs.size();
  }

  public Object getValueAt(int row, int col) {
    UnorderedTermPair pair = this.pairs.get(row);
    if (col == 0) {
      return pair.getTerm0().toString();
    }
    // implied else
    if (col == 1) {
      return pair.getTerm1().toString();
    }
    // implied else
    return this.scores.get(pair);
  }
  
  public Class<?> getColumnClass(int col) {
    if (col < 2) {
      return String.class;
    }
    // implied else
    return Integer.class;
  }
  
  public String getColumnName(int col) {
    if (col < 2) {
      return "Term";
    }
    // implied else
    return "Score";
  }

}
