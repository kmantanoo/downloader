package model.table;

import java.util.HashSet;

import javax.swing.table.DefaultTableModel;

public class DownloadTableModel extends DefaultTableModel {

   /**
    * 
    */
   private static final long serialVersionUID = 1L;
   private static String[] columnNames = { "Progress", "" };
   private HashSet<Integer> exceptRows;
   private boolean exceptAll;

   public DownloadTableModel(Object[][] data) {
      super(data, columnNames);
      exceptAll = false;
      exceptRows = new HashSet<>();
   }

   public static Object[] getColumnNames() {
      return columnNames;
   }

   public void exceptAll(boolean isExceptAll) {
      exceptAll = isExceptAll;
      exceptRows.clear();
   }

   public void addExceptRow(int row) {
      if (!exceptRows.contains(row))
         exceptRows.add(row);
   }

   public void clearExceptRows() {
      exceptRows.clear();
   }

   public void removeExceptRow(int row) {
      exceptRows.remove(row);
   }

   @Override
   public Class<?> getColumnClass(int column) {
      return getValueAt(0, column).getClass();
   }

   @Override
   public boolean isCellEditable(int row, int col) {
      return exceptAll ? false : col == 1 && (!exceptRows.contains(row));
   }

}
