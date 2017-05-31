package model.table;

import java.awt.Component;

import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import model.enumeration.DownloadState;
import view.panel.DownloadList;

public class DownloadTableButtonRenderer extends JButton implements TableCellRenderer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private DownloadList dlListPanel;

	public DownloadTableButtonRenderer(DownloadList panel) {
		setOpaque(true);
		this.dlListPanel = panel;
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {
		DownloadState dlState = (DownloadState) value;

		setIcon(dlListPanel.getImageIcon(dlState));

		if (DownloadState.COMPLETE == ((DownloadState) value)) {
			setEnabled(false);
			DownloadTableModel model = (DownloadTableModel) table.getModel();
			model.addExceptRow(row);
		} else {
			setEnabled(table.getModel().isCellEditable(row, column));
		}

		return this;
	}

}