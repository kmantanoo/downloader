package view.window;

import java.awt.EventQueue;
import java.nio.file.Path;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;

import controller.DownloadManager;
import model.enumeration.DownloadState;
import view.panel.ControlPanel;
import view.panel.DownloadList;

public class AppWindow extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private ControlPanel controlPanel;
	private DownloadList downloadList;
	private DownloadManager dlManager;
	private static AppWindow window;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
					AppWindow frame = new AppWindow();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public AppWindow() {
		dlManager = new DownloadManager(this);
		
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 639, 466);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		controlPanel = new ControlPanel(this);
		controlPanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		controlPanel.setBounds(10, 11, 613, 96);
		contentPane.add(controlPanel);
		
		downloadList = new DownloadList(this);
		downloadList.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		downloadList.setBounds(10, 118, 613, 308);
		contentPane.add(downloadList);
		
		controlPanel.setDestination(dlManager.getDestinationFolder());
	}
	
	public static AppWindow getInstance() {
		if (window == null) window = new AppWindow();
		return window;
	}

	public void addRow(Object[] objects) {
		downloadList.addRow(objects);
	}

	public void setValueAt(Object obj, int row, int column) {
		downloadList.setValueAt(obj, row, column);
	}
	
	public void downloadFiles(String sources) {
		dlManager.downloadFiles(sources);
	}
	
	public void clearDataTable() {
		downloadList.clearDataTable();
	}
	
	public void  changeDestination(String destination) {
		dlManager.setDestinationFolder(destination);
	}
	
	public String getSourceAtIndex(int index) {
		return dlManager.getSourceAtIndex(index);
	}
	
	public Path getSavedPathAtIndex(int index) {
		return dlManager.getSavedFile(index);
	}
	
	public DownloadState getDownloadState(int downloadSeq) {
		return dlManager.getDownloadState(downloadSeq);
	}
	
	public void cancelDownload(int downloadSeq) {
		dlManager.cancelDownload(downloadSeq);
	}
	
	public void cancelDownload() {
		dlManager.cancelDownload();
	}
	
	public void makeButtonClickableAllRow(boolean clickable){
		downloadList.makeButtonClickableAllRow(clickable);;
	}
	
	public void restartWorker(int downloadSeq) {
		dlManager.restartWorker(downloadSeq);
	}
}
