package model.table;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JTable;

import model.enumeration.DownloadState;
import view.panel.DownloadList;

public class DownloadTableButtonEditor extends DefaultCellEditor {

   /**
    * 
    */
   private static final long serialVersionUID = 1L;
   private JButton button;
   private boolean clicked;
   private int row;
   private DownloadState state;
   private DownloadList dlListPanel;

   public DownloadTableButtonEditor(JCheckBox checkBox, DownloadList panel) {
      super(checkBox);
      this.dlListPanel = panel;
      button = new JButton();
      button.setOpaque(true);
      button.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            fireEditingStopped();
         }
      });
   }

   @Override
   public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
      this.row = row;

      state = (DownloadState) value;
      button.setIcon(dlListPanel.getImageIcon(state));
      button.setActionCommand(state == DownloadState.ERROR ? "redo" : "clear");
      clicked = true;
      return button;
   }

   public Object getCellEditorValue() {
      if (clicked) {
         switch (button.getActionCommand()) {
         case "clear":
            doClear();
            break;
         case "redo":
            doRedo();
            break;
         }
      }
      clicked = false;
      return state;
   }

   public boolean stopCellEditing() {
      clicked = false;
      return super.stopCellEditing();
   }

   protected void fireEditingStopped() {
      super.fireEditingStopped();
   }

   private void doClear() {
      dlListPanel.cancelDownload(row);
      dlListPanel.disableButtonAtRow(row);
   }

   private void doRedo() {
      dlListPanel.restartWorker(row);
   }
}