package view.panel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import view.window.AppWindow;

public class ControlPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextField txtUrls;
	private JTextField txtDestination;
	private AppWindow app;

	/**
	 * Create the panel.
	 */
	public ControlPanel(AppWindow app) throws NullPointerException {
		if (app == null) throw new NullPointerException("app");
		
		this.app = app;
		setLayout(null);
		
		JLabel lblNewLabel = new JLabel("URLs:");
		lblNewLabel.setBounds(10, 11, 46, 14);
		add(lblNewLabel);
		
		txtUrls = new JTextField();
		txtUrls.setBounds(41, 8, 560, 20);
		add(txtUrls);
		txtUrls.setColumns(10);
		
		JLabel lblNewLabel_1 = new JLabel("Destination:");
		lblNewLabel_1.setBounds(10, 39, 63, 14);
		add(lblNewLabel_1);
		
		txtDestination = new JTextField();
		txtDestination.setEditable(false);
		txtDestination.setBounds(72, 36, 427, 20);
		add(txtDestination);
		txtDestination.setColumns(10);
		
		JButton btnBrowse = new JButton("Browse");
		btnBrowse.setBounds(512, 35, 89, 23);
		btnBrowse.addActionListener(new BrowseListener(this));
		add(btnBrowse);
		
		
		JButton btnDownload = new JButton("Download");
		btnDownload.addActionListener(new DownloadListener());
		btnDownload.setBounds(10, 64, 89, 23);
		add(btnDownload);
		
		
		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new CancelListener());
		btnCancel.setBounds(109, 64, 89, 23);
		add(btnCancel);

	}
	
	public String getURLs() {
		return txtUrls.getText();
	}
	
	public String getDestination() {
		return txtDestination.getText();
	}
	
	public void setDestination(String destination) {
		txtDestination.setText(destination);
	}
	
	private class BrowseListener implements ActionListener {
		private ControlPanel panel;
		
		public BrowseListener(ControlPanel controlPanel) {
			this.panel = controlPanel;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			JFileChooser fc = new JFileChooser(txtDestination.getText());
			fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			
			if (fc.showDialog(panel, "Choose") == JFileChooser.APPROVE_OPTION) {
				String destination = fc.getSelectedFile().getAbsolutePath();
				panel.setDestination(destination);
				app.changeDestination(destination);
			}
		}
		
	}
	
	private class DownloadListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			app.clearDataTable();
			String sources = getURLs();
			app.downloadFiles(sources);
		}
		
	}
	
	private class CancelListener implements ActionListener {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			app.makeButtonClickableAllRow(false);
			app.cancelDownload();
		}
	}
}
