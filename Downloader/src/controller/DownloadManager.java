package controller;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import config.UserSettings;
import model.NewDownloader;
import model.enumeration.DownloadState;
import model.exception.InvalidURLException;
import view.window.AppWindow;

public class DownloadManager {
	private List<Thread> threads;
	private StringBuilder errorMessage;
	private UserSettings settings;
	private AppWindow app;
	private List<String> sources;
	private List<NewDownloader> workers;
	
	public DownloadManager(AppWindow app) {
		settings = new UserSettings();
		workers = new ArrayList<NewDownloader>();
		this.app = app;
	}
	
	
	public void downloadFiles(String sourceString) {
		sources = getSourcesList(sourceString);
		workers.clear();
		app.makeButtonClickableAllRow(true);
		int downloadSeq = 0;
		for (String source: sources) {
//			download(source);
			newDownload(source, downloadSeq++);
		}
	}

//	private void download(String source) {
//		try {
//			if (threads == null) threads = new LinkedList<Thread>();
//			Downloader dl = new Downloader(source, settings);
//			Progress p = new Progress(dl);
//			Thread t = new Thread(dl);
//			threads.add(t);
//			
//			dl.addObserver(p);
//			
//			t.start();
//		} catch(Exception ex) {
//			if (errorMessage == null) errorMessage = new StringBuilder();
//			errorMessage.append(String.format("%s\n", ex.getMessage()));
//		}
//	}
	
	private void newDownload(String source, int downloadSeq) {
		try {
			NewDownloader worker = new NewDownloader(source, settings, app, downloadSeq);
			app.addRow(new Object[]{0, DownloadState.DOWNLOAD});
			
			workers.add(worker);
			worker.execute();
		} catch (NullPointerException | InvalidURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String getErrorMessage() {
		if (errorMessage != null) return errorMessage.toString();
		return null;
	}
	
	public boolean allDone() {
		if (threads == null) return false;
		for (Thread t : threads) {
			if (t.isAlive()) {
				return false;
			}
		}
		
		return true;
	}

	private List<String> getSourcesList(String sources) {
		sources = sources.replaceAll("\\s", "");
		List<String> output = Arrays.asList(sources.split(","));
		return output;
	}
	
	public String getDestinationFolder() {
		return settings.getDestinationFolder();
	}
	
	public void  setDestinationFolder(String destination) {
		settings.setDestinationFolder(destination);
	}
	
	public String getSourceAtIndex(int index) {
		return sources.get(index);
	}
	
	public Path getSavedFile(int index) {
		return workers.get(index).getSavedPath();
	}
	
	public void cancelDownload() {
		for (NewDownloader worker: workers) {
			worker.cancel(true);
		}
	}
	
	public DownloadState getDownloadState(int downloadSeq) {
		return workers.get(downloadSeq).getDownloadState();
	}
	
	public void cancelDownload(int downloadSeq) {
		workers.get(downloadSeq).cancel(true);
	}
	
	public void restartWorker(int downloadSeq) {
		NewDownloader newInstance = workers.get(downloadSeq).clone();
		workers.set(downloadSeq, newInstance);
		newInstance.execute();
	}
}
