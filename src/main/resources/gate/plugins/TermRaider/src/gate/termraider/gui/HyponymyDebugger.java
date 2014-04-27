/*
 *  Copyright (c) 2008--2012, The University of Sheffield. See the file
 *  COPYRIGHT.txt in the software or at http://gate.ac.uk/gate/COPYRIGHT.txt
 *
 *  This file is part of GATE (see http://gate.ac.uk/), and is free
 *  software, licenced under the GNU Library General Public License,
 *  Version 2, June 1991 (in the distribution as file licence.html,
 *  and also available at http://gate.ac.uk/gate/licence.html).
 *
 *  $Id: HyponymyDebugger.java 16296 2012-11-20 12:17:11Z adamfunk $
 */
package gate.termraider.gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.table.*;
import org.apache.commons.lang.StringUtils;
import gate.creole.*;
import java.util.*;
import gate.termraider.bank.*;
import gate.termraider.util.*;


public class HyponymyDebugger 
  extends JPanel 
  implements ANNIEConstants {

  private static final long serialVersionUID = -3587317001405213679L;
  
  private JScrollPane scrollPane;
  private HyponymyTermbank termbank;
  private JTable table;
  private JButton goButton;
  private JPanel controlPanel, placeholder;


  
  public HyponymyDebugger(HyponymyTermbank termbank) {
    this.termbank = termbank;
    setLayout(new BorderLayout());
    makeControlPanel();
    this.add(controlPanel, BorderLayout.NORTH);
    placeholder = new JPanel();
    this.add(placeholder, BorderLayout.CENTER);
  }
  
  
  private void makeControlPanel() {
    goButton = new JButton("generate debugging table");
    goButton.setToolTipText("This may take some time!");
    goButton.addActionListener(new HDGoButtonActionListener(this));

    controlPanel = new JPanel();
    controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.X_AXIS));
    controlPanel.add(Box.createHorizontalGlue());
    controlPanel.add(goButton);
    controlPanel.add(Box.createHorizontalGlue());
  }
  
  
  protected void generateTable() {
    JTextField tempField = new JTextField("generating...");
    placeholder.add(tempField);
    
    TableModel tableModel = new HDTableModel(termbank); 
    table = new JTable(tableModel);
    table.setDefaultRenderer(String.class, new MultilineCellRenderer());
    table.setAutoCreateRowSorter(true);
    scrollPane = new JScrollPane(table, 
            JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, 
            JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    this.remove(placeholder);
    this.add(scrollPane, BorderLayout.CENTER);
  }
  
}


class HDTableModel extends AbstractTableModel {
  private static final long serialVersionUID = -1124137938074923640L;
  private String[] columnNames = {"term", "raw score", "docs", "docs", "hyponyms", "hyponyms", "heads"};
  private Map<Term, Set<String>> termDocuments, termHyponyms, termHeads;
  private List<Term> terms;
  private HyponymyTermbank termbank;
  
  public HDTableModel(HyponymyTermbank termbank) {
    this.termbank = termbank;
    this.termDocuments = termbank.getTermDocuments();
    this.termHeads = termbank.getTermHeads();
    this.termHyponyms = termbank.getTermHyponyms();
    terms = new ArrayList<Term>(termDocuments.keySet());
    Collections.sort(terms, new TermComparator());
  }
  
  
  public Class<?> getColumnClass(int i) {
    return String.class;
  }

  @Override
  public int getColumnCount() {
    return columnNames.length;
  }

  @Override
  public int getRowCount() {
    return termDocuments.size();
  }
  
  public String getColumnName(int c) {
    return columnNames[c];
  }
  
  public boolean isCellEditable(int r, int c) {
    return false;
  }

  @Override
  public Object getValueAt(int row, int column) {
    Term term = terms.get(row);
    String result = "";
    switch(column) {
      case 0: 
        result = term.toString();
        break;
      case 1:
        result = Double.toString(termbank.getRawScore(term));
        break;
      case 2: 
        result = Integer.toString(termDocuments.get(term).size());
        break;
      case 3:
        result = StringUtils.join(termDocuments.get(term), '\n');
        break;
      case 4:
        result = Integer.toString(termHyponyms.get(term).size());
        break;
      case 5:
        result = StringUtils.join(termHyponyms.get(term), '\n');
        break;
      case 6:
        result = StringUtils.join(termHeads.get(term), '\n');
        break;
    }
    return result;
  }
  
}

class HDGoButtonActionListener implements ActionListener {
  private HyponymyDebugger viewer;
  
  public HDGoButtonActionListener(HyponymyDebugger viewer) {
    this.viewer = viewer;
  }
  
  @Override
  public void actionPerformed(ActionEvent arg0) {
    viewer.generateTable();
  }
}