package model;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.SocketException;
import java.util.Observable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.apache.commons.validator.routines.UrlValidator;

import config.AppConfig;
import config.UserSettings;
import model.datasource.DataSource;
import model.enumeration.DownloadState;
import model.exception.InvalidURLException;
import utils.URLUtil;

public class Downloader extends Observable implements Runnable{
	private static int BUFFER_SIZE = 4096;
	private static int FLUSH_THRESHOLD = BUFFER_SIZE * 4;
	private String source;
	private long size;
	private long downloaded;
	private int timeOut;
	private long startTime;
	private AppConfig conf;
	private DownloadState state;
	private DataSource dataSource;
	private UserSettings settings;
	
	public Downloader(String source, UserSettings settings) 
			throws InvalidURLException, NullPointerException {
		UrlValidator validator = new UrlValidator(UrlValidator.ALLOW_ALL_SCHEMES);
		
		if (! validator.isValid(source)) throw new InvalidURLException(source);
		
		if (settings == null) throw new NullPointerException("settings");
		
		this.settings = settings;		
		this.source = source;
		size = -1;
		downloaded = 0;
		instanceDataSource();
	}
	
	private void instanceDataSource() {
		conf = AppConfig.getInstance();
		String className = conf.getProp("protocol." +  URLUtil.getProtocolFromURL(source));
		String classPath = "model.datasource.";
		timeOut = Integer.parseInt(conf.getProp("timeout"));
		try {
				
			dataSource = (DataSource) Class.forName(classPath + className).newInstance();
			
			dataSource.setSource(source);
			dataSource.setAppConfig(conf);
			
			dataSource.openConnection();
			
			size = dataSource.getSize();
			
			state = DownloadState.DOWNLOAD;
		} catch (Exception e) {
			state = DownloadState.ERROR;
			try {
				dataSource.closeConnection();
			} catch (Exception e1) {
				System.err.println(e1.getMessage());
			}
			e.printStackTrace();
		} finally {
			update();
		}
	}
	
	public String getSource() {
		return source;
	}
	
	public long getFileSize() {
		return size;
	}
	
	public long getDownloaded() {
		return downloaded;
	}
	
	public DownloadState getState() {
		return state;
	}
	
	public float getProgress() {
		return ((float) downloaded / size) * 100;
	}
	
	public long getElapsedTime(){
		return System.currentTimeMillis() - startTime;
	}

	@Override
	public void run() {
		if (state != DownloadState.ERROR) {
			String dstDirectory = settings.getDestinationFolder();
			String generatedFileName = URLUtil.genFileNameFromURL(source);
			FileOutputStream fos = null;
			File outputFile = null;
			InputStream in = null;
			ExecutorService executor = Executors.newCachedThreadPool();
			StreamReader streamReader = null;
			Future<Integer> futureTask = null;
			int writerSize = 0;
			try {
				in = dataSource.getInputStream();
				
				streamReader = new StreamReader(BUFFER_SIZE, in);
				
				outputFile = new File(dstDirectory + "/" + generatedFileName);
				if (!outputFile.getParentFile().exists()) outputFile.getParentFile().mkdirs();
				fos = new FileOutputStream(outputFile);
				
				startTime = System.currentTimeMillis();
				long lastTime = startTime;
				
				while (state == DownloadState.DOWNLOAD) {
					futureTask = executor.submit(streamReader);
					int readSize = futureTask.get(timeOut, TimeUnit.MILLISECONDS);
					
					if (readSize == -1) {
						break;
					}
					
					
					fos.write(streamReader.getBuffer(), 0, readSize);
					downloaded += readSize;
					writerSize += readSize;
					
					if (writerSize > FLUSH_THRESHOLD) {
						fos.flush();
						writerSize = 0;
					}
					
					if (System.currentTimeMillis() - lastTime > 1000) {
						lastTime = getElapsedTime();
						update();
					}
				}
				
				state = DownloadState.COMPLETE;
				update();
			} catch (FileNotFoundException e) {
				System.err.println(e.getMessage());
				state = DownloadState.ERROR;
				update();
			} catch (IOException e) {
				e.printStackTrace();
				state = DownloadState.ERROR;
				update();
			} catch (TimeoutException e) {
				System.err.println("Reached time out while reading data from url");
				state = DownloadState.ERROR;
				update();
			}  catch (ExecutionException e) {
				if (e.getCause() instanceof SocketException)
					System.err.println("Connection broken");
				state = DownloadState.ERROR;
				update();
			} catch (Exception e) {
				e.printStackTrace();
				state = DownloadState.ERROR;
				update();
			} finally {
				try {
					executor.shutdown();
					if (fos != null) fos.close();
					if (in != null) in.close();
					dataSource.closeConnection();
				} catch (IOException ex) {
					ex.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	private void update() {
		setChanged();
		notifyObservers();
	}

}
