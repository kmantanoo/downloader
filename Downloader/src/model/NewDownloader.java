package model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.net.SocketException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.SwingWorker;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.validator.routines.UrlValidator;

import config.AppConfig;
import config.UserSettings;
import model.datasource.DataSource;
import model.enumeration.DownloadState;
import model.exception.InvalidURLException;
import utils.URLUtil;
import view.window.AppWindow;

public class NewDownloader extends SwingWorker<Long, Integer>{
	private static int BUFFER_SIZE = 4096;
	private static int FLUSH_THRESHOLD = BUFFER_SIZE * 4;
	public static final int PUBLISH_CANCEL = -1;
	public static final int PUBLISH_ERROR = -2;
	private String source;
	private long size;
	private long downloaded;
	private int timeOut;
	private int downloadSeq;
	private long startTime;
	private AppConfig conf;
	private DownloadState state;
	private DataSource dataSource;
	private UserSettings settings;
	private AppWindow app;
	private Path savedPath;
	
	public NewDownloader(String source, UserSettings settings, AppWindow app,int downloadSeq) 
			throws InvalidURLException, NullPointerException {
		UrlValidator validator = new UrlValidator(UrlValidator.ALLOW_ALL_SCHEMES | UrlValidator.ALLOW_LOCAL_URLS);
		
		if (! validator.isValid(source)) throw new InvalidURLException(source);
		
		if (settings == null) throw new NullPointerException("settings");
		
		if (app == null) throw new NullPointerException("app");
		
		this.settings = settings;
		this.source = source;
		this.app = app;
		this.downloadSeq = downloadSeq;
		this.size = -1;
		this.downloaded = 0;
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
			dataSource.setAppWindow(app);
			
			dataSource.openConnection();
			size = dataSource.getSize();
			state = DownloadState.DOWNLOAD;
		} catch (Exception e) {
			state = DownloadState.ERROR;
			publish(PUBLISH_ERROR);
			try {
				dataSource.closeConnection();
			} catch (Exception e1) {
				System.err.println(e1.getMessage());
			}
			e.printStackTrace();
		}
	}

	@Override
	protected Long doInBackground() throws Exception {
		long result;
		
		instanceDataSource();
		
		if (state != DownloadState.ERROR) {
			int writerSize = 0;
			ExecutorService executor = Executors.newCachedThreadPool();
			String dstDirectory = settings.getDestinationFolder();
//			String generatedFileName = URLUtil.genFileNameFromURL(source);
			String generatedFileName = genFileName();
			Path pathToFile = FileSystems.getDefault().getPath(dstDirectory, generatedFileName);
			
			FileOutputStream fos = null;
			File outputFile = null;
			InputStream in = null;
			StreamReader streamReader = null;
			Future<Integer> futureTask = null;
			
			try {
				in = dataSource.getInputStream();
				
				streamReader = new StreamReader(BUFFER_SIZE, in);
				
				outputFile = new File(pathToFile.toUri());
				if (!outputFile.getParentFile().exists()) outputFile.getParentFile().mkdirs();
				fos = new FileOutputStream(outputFile);
				
				startTime = System.currentTimeMillis();
				long lastTime = startTime;
				
				while (state == DownloadState.DOWNLOAD && !isCancelled()) {
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
						publish(downloadProgress());
					}
				}
				
				state = DownloadState.COMPLETE;
				publish(downloadProgress());
				result = downloaded;
			} catch (FileNotFoundException e) {
				
				System.err.println(e.getMessage());
				state = DownloadState.ERROR;
				publish(PUBLISH_ERROR);
				cancel(true);
				result = -1;
				
			} catch (IOException e) {
				
				e.printStackTrace();
				state = DownloadState.ERROR;
				publish(PUBLISH_ERROR);
				cancel(true);
				result = -1;
				
			} catch (TimeoutException e) {
				
				System.err.println("Reached time out while reading data from url");
				state = DownloadState.ERROR;
				publish(PUBLISH_ERROR);
				cancel(true);
				result = -1;
				
			}  catch (ExecutionException e) {
				
				if (e.getCause() instanceof SocketException)
					System.err.println("Connection broken");
				state = DownloadState.ERROR;
				publish(PUBLISH_ERROR);
				cancel(true);
				result = -1;
				
			} catch (InterruptedException itr) {
				state = DownloadState.CANCEL;
				publish(PUBLISH_CANCEL);
				cancel(true);
				result = 0;
			} catch (Exception e) {	
				e.printStackTrace();
				state = DownloadState.ERROR;
				publish(PUBLISH_ERROR);
				cancel(true);
				result = -1;
				
			} finally {
				try {
					executor.shutdown();
					if (fos != null) fos.close();
					if (in != null) in.close();
					dataSource.closeConnection();
					
					if (outputFile != null){
						if(state != DownloadState.COMPLETE) {
							outputFile.delete();
						} else {
							savedPath = renameFile(pathToFile, URLUtil.getFileName(source));
							publish(downloadProgress());
							result = downloaded;
						}
					} 
				} catch (IOException ex) {
					ex.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			return result;
		}
		return new Long(-1);
	}
	
	@Override
	public void process(List<Integer> ints) {
		app.setValueAt(ints.get(ints.size() - 1), downloadSeq, 0);
		app.setValueAt(state, downloadSeq, 1);
	}
	
	private Path renameFile(Path genFile, String newName) throws IOException {
		Path dirPath = genFile.getParent();
		Path newPath = null;
		File directory = new File(dirPath.toUri());
		CustomFilenameFilter cfFilter = new CustomFilenameFilter(newName);
		String sequence = "";
		
		File[] filtered = directory.listFiles(cfFilter);
		String[] sorted = new String[filtered.length];
		int index = 0;
		for (File f: filtered) {
			sorted[index++] = FilenameUtils.removeExtension(f.getName());
		}
		if (sorted.length > 0) {
			int fileSequence = 0;
			if (sorted.length > 1) {
				Arrays.sort(sorted);
				String latestFileName = sorted[sorted.length - 1];
				fileSequence = Integer.parseInt(latestFileName.substring(
						latestFileName.indexOf("(")+1, latestFileName.indexOf(")"))) + 1;
			} else {
				fileSequence = 1;
			}
			sequence = String.format("(%d)", fileSequence);
		}
		
		newPath = FileSystems.getDefault().getPath(dirPath.toString(), String.format("%s%s.%s"
				, FilenameUtils.removeExtension(newName)
				, sequence
				, FilenameUtils.getExtension(newName)));
		return Files.move(genFile, newPath, StandardCopyOption.ATOMIC_MOVE);
		
	}
	
	private String genFileName() {
		long randomNumber = new Random().nextLong();
		return "Java"+Long.toString(Math.abs(randomNumber))+".stream";
	}
		
	public int downloadProgress() {
		return (int) (downloaded * 100 / size);
	}
	
	public long getElapsedTime(){
		return System.currentTimeMillis() - startTime;
	}
	
	public DownloadState getDownloadState() {
		return state;
	}
	
	public Path getSavedPath() {
		return savedPath;
	}
	
	public NewDownloader clone() {
		NewDownloader newInstance = null;
		try {
			newInstance = new NewDownloader(source, settings, app, downloadSeq);
		} catch (NullPointerException | InvalidURLException e) {
			e.printStackTrace();
		}
		return newInstance;
	}
	
	class CustomFilenameFilter implements FilenameFilter {
		private Pattern p;
		
		public CustomFilenameFilter(String expectFileName) {
			String regex = FilenameUtils.removeExtension(expectFileName)
							+ "(\\(\\d+\\))?\\." 
							+ FilenameUtils.getExtension(expectFileName);
			p = Pattern.compile(regex);
			
		}

		@Override
		public boolean accept(File dir, String name) {
			Matcher m = p.matcher(name);
			if (m.matches()) {
				return true;
			}
			return false;
		}
	}

}
