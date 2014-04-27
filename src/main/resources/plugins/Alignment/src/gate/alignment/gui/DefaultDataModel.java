package gate.alignment.gui;

import javax.swing.table.AbstractTableModel;

/**
 * Implementation of Data Model used for populating tables in the Alignment 
 * Editor.  Actions wishing to publish data to the Alignment Editor needs to
 * implement DataPublisherAction which internally uses instance of this class.
 * One can extend this class and provide a different implementation.
 * @author niraj
 *
 */
public class DefaultDataModel extends AbstractTableModel {
  
  /**
   * Action the data model is for.
   */
  private DataPublisherAction dpa;


  /**
   * Constructor
   * @param dpa
   */
  public DefaultDataModel(DataPublisherAction dpa) {
    this.dpa = dpa;
  }

  /**
   * Number of columns to display
   */
  public int getColumnCount() {

    return dpa.getColumnCount();
  }

  /**
   * Number of rows in the table
   */
  public int getRowCount() {
    return dpa.getRowCount();
  }

  /**
   * Returns a value at the given row and column
   */
  public Object getValueAt(int rowIndex, int columnIndex) {
    return dpa.getValueAt(rowIndex, columnIndex);
  }

  /**
   * Respective class for the given column.
   */
  @Override
  public Class<?> getColumnClass(int columnIndex) {
    return String.class;

  }

  /**
   * Title of the column
   */
  @Override
  public String getColumnName(int column) {
    return dpa.getColumnName(column);
  }

  /**
   * Is the cell editable? By default, data publishing action disables this.
   */
  @Override
  public boolean isCellEditable(int rowIndex, int columnIndex) {
    return false;
  }
}
