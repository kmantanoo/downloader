package controller;

import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileSystemView;

import model.table.DownloadTableModel;

public class Main {
	public static void main(String args[]) {
		mainTest();
		
//		fileChooserTest();
	}

	public static void mainTest() {
//		String sources = "http://speedtest.tele2.net/1MB.zip";
		// String sources = "ftp://speedtest.tele2.net/20MB.zip ,
		// http://speedtest.tele2.net/5MB.zip,"
		// + " sftp://localhost/Desktop/10MB.zip";
		// String sources = "sftp://localhost/Desktop/10MB.zip";
		
//		DownloadTableModel model = new DownloadTableModel(null);
		
//		DownloadManager dl = new DownloadManager();
//		dl.downloadFiles(sources);
	}

	public static void fileChooserTest() {
		JFileChooser fc = new JFileChooser(new File(System.getProperty("user.home")));
		fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		fc.setDialogTitle("Choose destination directory");
		fc.setApproveButtonText("Choose");

		if (fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
			System.out.println(fc.getSelectedFile().getAbsolutePath());
		}
	}
}
