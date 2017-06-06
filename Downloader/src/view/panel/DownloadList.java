package view.panel;

import java.nio.file.Path;

import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

import model.enumeration.DownloadState;
import model.table.DownloadTableButtonEditor;
import model.table.DownloadTableButtonRenderer;
import model.table.DownloadTableModel;
import model.table.DownloadTableProgressRenderer;
import view.window.AppWindow;

public class DownloadList extends JPanel {
   /**
    * 
    */
   private static final long serialVersionUID = 1L;
   private JTable table;
   private JScrollPane scrollPane;
   private DownloadTableModel model;
   private AppWindow app;

   /**
    * Create the panel.
    */
   public DownloadList(AppWindow app) throws NullPointerException {
      if (app == null)
         throw new NullPointerException("app");

      setLayout(null);

      scrollPane = new JScrollPane();
      scrollPane.setBounds(0, 0, 613, 308);
      add(scrollPane);

      model = new DownloadTableModel(null);

      table = new JTable(model);
      table.setShowGrid(false);
      table.setShowVerticalLines(false);
      table.getColumnModel().setColumnSelectionAllowed(false);
      table.getTableHeader().setResizingAllowed(false);
      table.getTableHeader().setReorderingAllowed(false);
      table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
      table.setRowSelectionAllowed(false);
      table.getColumnModel().getColumn(0).setPreferredWidth(500);
      table.getColumnModel().getColumn(1).setPreferredWidth(111);
      table.setRowHeight(40);

      scrollPane.setViewportView(table);
      this.app = app;
   }

   public void addRow(Object[] objects) {

      model.addRow(objects);
      TableColumn colProgress = table.getColumnModel().getColumn(0);
      TableColumn colButton = table.getColumnModel().getColumn(1);

      colProgress.setCellRenderer(new DownloadTableProgressRenderer(this));
      colButton.setCellRenderer(new DownloadTableButtonRenderer(this));
      colButton.setCellEditor(new DownloadTableButtonEditor(new JCheckBox(), this));
   }

   public void setTableModel(TableModel model) {
      table.setModel(model);
   }

   public void setValueAt(Object obj, int row, int column) {
      model.setValueAt(obj, row, column);
   }

   public void clearDataTable() {
      model.setRowCount(0);
   }

   public String getSourceAtIndex(int index) {
      return app.getSourceAtIndex(index);
   }

   public Path getSavedPathAtIndex(int index) {
      return app.getSavedPathAtIndex(index);
   }

   public void cancelDownload(int downloadSeq) {
      app.cancelDownload(downloadSeq);
   }

   public void disableButtonAtRow(int row) {
      DownloadTableModel model = (DownloadTableModel) table.getModel();
      model.addExceptRow(row);
   }

   public void makeButtonClickableAllRow(boolean clickable) {
      DownloadTableModel model = (DownloadTableModel) table.getModel();
      model.exceptAll(!clickable);
   }

   public ImageIcon getImageIcon(DownloadState state) {
      final String clearIco = "icon/ic_clear.png";
      final String redoIco = "icon/ic_redo.png";

      String resourcePath = state == DownloadState.ERROR ? redoIco : clearIco;
      ImageIcon ico = new ImageIcon(getClass().getClassLoader().getResource(resourcePath));

      return ico;
   }

   public void restartWorker(int downloadSeq) {
      app.restartWorker(downloadSeq);
   }

}
