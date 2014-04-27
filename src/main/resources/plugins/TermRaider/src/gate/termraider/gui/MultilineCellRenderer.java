/*
 *  Copyright (c) 2008--2012, The University of Sheffield. See the file
 *  COPYRIGHT.txt in the software or at http://gate.ac.uk/gate/COPYRIGHT.txt
 *
 *  This file is part of GATE (see http://gate.ac.uk/), and is free
 *  software, licenced under the GNU Library General Public License,
 *  Version 2, June 1991 (in the distribution as file licence.html,
 *  and also available at http://gate.ac.uk/gate/licence.html).
 *
 *  $Id: MultilineCellRenderer.java 16336 2012-11-27 14:01:14Z adamfunk $
 *  
 *  Note: this class is mostly borrowed from Botunge:
 *  http://blog.botunge.dk/post/2009/10/09/JTable-multiline-cell-renderer.aspx
 *  
 */
package gate.termraider.gui;

import java.awt.Component;
import java.awt.Dimension;
import java.util.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.TableCellRenderer;

public class MultilineCellRenderer 
  extends JTextArea 
  implements TableCellRenderer {

  private static final long serialVersionUID = -6873413648365356985L;
  
  private List<List<Integer>> rowColHeight = new ArrayList<List<Integer>>();
  
  public MultilineCellRenderer() {
    setLineWrap(true);
    setWrapStyleWord(true);
    setOpaque(true);
  }
 
  public Component getTableCellRendererComponent(
      JTable table, Object value, boolean isSelected, boolean hasFocus,
      int row, int column) {
    if (isSelected) {
      setForeground(table.getSelectionForeground());
      setBackground(table.getSelectionBackground());
    } else {
      setForeground(table.getForeground());
      setBackground(table.getBackground());
    }
    setFont(table.getFont());
    if (hasFocus) {
      setBorder(UIManager.getBorder("Table.focusCellHighlightBorder"));
      if (table.isCellEditable(row, column)) {
        setForeground(UIManager.getColor("Table.focusCellForeground"));
        setBackground(UIManager.getColor("Table.focusCellBackground"));
      }
    } else {
      setBorder(new EmptyBorder(1, 2, 1, 2));
    }
    if (value != null) {
      setText(value.toString());
    } else {
      setText("");
    }
    adjustRowHeight(table, row, column);
    return this;
  }
 
  /**
   * Calculate the new preferred height for a given row, and sets the height on the table.
   */
  private void adjustRowHeight(JTable table, int row, int column) {
    // The trick to get this to work properly is to set the width of the column to the
    // textarea. The reason for this is that getPreferredSize(), without a width tries
    // to place all the text in one line. By setting the size with the with of the column,
    // getPreferredSize() returns the proper height which the row should have in
    // order to make room for the text.
    int cWidth = table.getTableHeader().getColumnModel().getColumn(column).getWidth();
    setSize(new Dimension(cWidth, 1000));
    int prefH = getPreferredSize().height;
    while (rowColHeight.size() <= row) {
      rowColHeight.add(new ArrayList<Integer>(column));
    }
    List<Integer> colHeights = rowColHeight.get(row);
    while (colHeights.size() <= column) {
      colHeights.add(0);
    }
    colHeights.set(column, prefH);
    int maxH = prefH;
    for (Integer colHeight : colHeights) {
      if (colHeight > maxH) {
        maxH = colHeight;
      }
    }
    if (table.getRowHeight(row) != maxH) {
      table.setRowHeight(row, maxH);
    }
  }
}
