package view;

import java.time.LocalTime;
import java.util.Observable;
import java.util.Observer;

import model.Downloader;
import model.enumeration.DownloadState;
import utils.URLUtil;

public class Progress implements Observer{
	private Downloader downloader;
	private Window frame;
	
	public Progress(Downloader downloader) {
		this.downloader = downloader;
		frame = new Window();
	}

	@Override
	public void update(Observable o, Object arg) {
		LocalTime lt = LocalTime.ofSecondOfDay(downloader.getElapsedTime() / 1000);
		System.out.println(String.format("%s:%s:%s:Progress:%.2f%%"
				, lt.toString()
				, URLUtil.getProtocolFromURL(downloader.getSource())
				, downloader.getState().toString()
				, downloader.getProgress()));
		frame.update((int) downloader.getProgress() * 100);
		if (downloader.getState() == DownloadState.COMPLETE) frame.dispose();
	}

}
