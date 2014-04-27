/*
 *  Copyright (c) 2008--2012, The University of Sheffield. See the file
 *  COPYRIGHT.txt in the software or at http://gate.ac.uk/gate/COPYRIGHT.txt
 *
 *  This file is part of GATE (see http://gate.ac.uk/), and is free
 *  software, licenced under the GNU Library General Public License,
 *  Version 2, June 1991 (in the distribution as file licence.html,
 *  and also available at http://gate.ac.uk/gate/licence.html).
 *
 *  $Id: PMIDebugger.java 16296 2012-11-20 12:17:11Z adamfunk $
 */
package gate.termraider.gui;

import java.awt.BorderLayout;
import java.util.*;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;
import gate.termraider.bank.*;
import gate.termraider.util.*;


public class PMIDebugger extends JPanel {

  private static final long serialVersionUID = 4515459904186726963L;
  
  private JSplitPane splitPane;
  private JTable termTable, pairTable;
  private TableModel termTableModel, pairTableModel;
  private JScrollPane termPane, pairPane;
  
  
  public PMIDebugger(AbstractBank bank) {
    this.setLayout(new BorderLayout());
    if (bank instanceof PMIBank) {
      this.populate((PMIBank) bank);
    }
  }

  
  private void populate(PMIBank bank) {
    JTextField countField = new JTextField("Term count = " + bank.getTotalCount()
            + "; Distinct terms = " + bank.getNbrDistinctTerms()
            + "; Pair count = " + bank.getTotalPairCount()
            + "; Distinct pairs = " + bank.getNbrDistinctPairs()  );
    this.add(countField, BorderLayout.NORTH);
    splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
    termTableModel = new PDTermModel(bank);
    pairTableModel = new PDPairModel(bank);
    termTable = new JTable(termTableModel);
    pairTable = new JTable(pairTableModel);
    termTable.setAutoCreateRowSorter(true);
    pairTable.setAutoCreateRowSorter(true);
    termPane = new JScrollPane(termTable, 
            JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, 
            JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    pairPane = new JScrollPane(pairTable, 
            JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, 
            JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    splitPane.setLeftComponent(termPane);
    splitPane.setRightComponent(pairPane);
    this.add(splitPane, BorderLayout.CENTER);
    this.validate();
    this.repaint();
  }

}


class PDPairModel extends AbstractTableModel {
  private static final long serialVersionUID = 4553799329984692710L;

  private List<UnorderedTermPair> pairs;
  private Map<UnorderedTermPair, Double> scores; 
  private PMIBank bank;

  public PDPairModel(PMIBank bank) {
    this.pairs = new ArrayList<UnorderedTermPair>(bank.getPairs());
    this.scores = bank.getScores();
    this.bank = bank;
  }
  
  public int getColumnCount() {
    return 5;
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
    if (col == 2) {
      return bank.getPairCount(pair);
    }
    if (col == 3) {
      return bank.getDocumentCount(pair);
    }
    // implied else
    return this.scores.get(pair);
  }
  
  public Class<?> getColumnClass(int col) {
    if (col < 2) {
      return String.class;
    }
    // implied else
    if (col < 4) {
      return Integer.class;
    }
    // implied else
    return Double.class;
  }
  
  public String getColumnName(int col) {
    if (col < 2) {
      return "Term";
    }
    // implied else
    if (col == 2) {
      return "Frequency";
    }
    if (col == 3) {
      return "DocFrequency";
    }
    // implied else
    return "Score";
  }

}


class PDTermModel extends AbstractTableModel {
  private static final long serialVersionUID = 2673610596047342256L;

  private List<Term> terms;
  private PMIBank bank;

  public PDTermModel(PMIBank bank) {
    this.terms = new ArrayList<Term>(bank.getTerms());
    this.bank = bank;
  }
  
  public int getColumnCount() {
    return 3;
  }

  public int getRowCount() {
    return this.terms.size();
  }

  public Object getValueAt(int row, int col) {
    Term term = this.terms.get(row);
    if (col == 0) {
      return term.toString();
    }
    // implied else
    if (col == 1) {
      return this.bank.getTermCount(term);
    }
    // implied else
    return this.bank.getDocumentCount(term);
  }
  
  public Class<?> getColumnClass(int col) {
    if (col == 0) {
      return String.class;
    }
    // implied else
    return Integer.class;
  }
  
  public String getColumnName(int col) {
    if (col == 0) {
      return "Term";
    }
    if (col == 1) {
      return "Frequency";
    }
    return "DocFrequency";
  }

}
