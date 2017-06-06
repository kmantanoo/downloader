package model.table;

import java.awt.Component;
import java.nio.file.Path;

import javax.swing.BorderFactory;
import javax.swing.JProgressBar;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import controller.Downloader;
import model.enumeration.DownloadState;
import view.panel.DownloadList;

public class DownloadTableProgressRenderer extends DefaultTableCellRenderer {
   /**
    * 
    */
   private static final long serialVersionUID = 1L;
   private final JProgressBar downloadProgress = new JProgressBar(0, 100);
   private DownloadList dlListPanel;

   public DownloadTableProgressRenderer(DownloadList panel) {
      super();
      setOpaque(true);
      this.dlListPanel = panel;
      downloadProgress.setBorder(BorderFactory.createEmptyBorder());
      downloadProgress.setStringPainted(true);
   }

   @Override
   public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
         int row, int column) {
      Integer i = (Integer) value;
      Path savedPath = dlListPanel.getSavedPathAtIndex(row);
      String file = null;
      if (savedPath != null)
         file = savedPath.toFile().getName();
      else
         file = dlListPanel.getSourceAtIndex(row);
      String text = null;
      String state = DownloadState.COMPLETE.toString();
      if (i == Downloader.PUBLISH_ERROR) {
         state = DownloadState.ERROR.toString();
      } else if (i == Downloader.PUBLISH_CANCEL) {
         state = DownloadState.CANCEL.toString();
      } else if (i < 100) {
         state = DownloadState.DOWNLOAD.toString();
         text = String.format("%s %d %%", file, i);
         downloadProgress.setValue(i);
         downloadProgress.setString(text);
         return downloadProgress;
      }

      text = String.format("%s:%s", state.toLowerCase(), file);
      super.getTableCellRendererComponent(table, text, isSelected, hasFocus, row, column);
      return this;
   }
}
