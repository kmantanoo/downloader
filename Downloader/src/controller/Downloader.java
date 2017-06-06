package controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import javax.swing.SwingWorker;

import org.apache.commons.validator.routines.UrlValidator;

import model.StreamReader;
import model.datasource.DataSource;
import model.enumeration.DownloadState;
import model.exception.InvalidURLException;
import utils.FileUtil;
import utils.URLUtil;

public class Downloader extends SwingWorker<Long, Integer> {
   private static int BUFFER_SIZE = 4096;
   private static int FLUSH_THRESHOLD = BUFFER_SIZE * 4;
   private static UrlValidator validator = new UrlValidator(
         UrlValidator.ALLOW_ALL_SCHEMES | UrlValidator.ALLOW_LOCAL_URLS);
   public static final int PUBLISH_CANCEL = -1;
   public static final int PUBLISH_ERROR = -2;
   private int timeOut;
   private int downloadSeq;
   private long size;
   private long downloaded;
   private long startTime;
   private String source;
   private DownloadState downloadState;
   private DataSource dataSource;
   private Path savedPath;
   private DownloadManager manager;

   public Downloader(int downloadSeq) {
      this.downloadSeq = downloadSeq;
      this.size = -1;
      this.downloaded = 0;
      downloadState = DownloadState.INTIAL;
   }

   @Override
   protected Long doInBackground() throws Exception {
      long result;
      FileOutputStream fos = null;
      File outputFile = null;
      ExecutorService executor = Executors.newCachedThreadPool();

      InputStream in = null;
      Path pathToFile = null;
      
      try {
         if (!validator.isValid(source))
            throw new InvalidURLException(source);
         if (manager == null)
            throw new NullPointerException("manager");
      } catch (Exception e) {
         downloadState = DownloadState.ERROR;
         return -1L;
      }

      try {
         pathToFile = FileUtil.toRandomFilePath(manager.getDestinationFolder());
         outputFile = pathToFile.toFile();
         fos = FileUtil.getFileOutputStream(outputFile);

         dataSource.openConnection();
         size = dataSource.getSize();
         downloadState = DownloadState.DOWNLOAD;
         
         in = dataSource.getInputStream();
         
         startTime = System.currentTimeMillis();
         
         transferDataStream(in, fos, executor);
         
         downloadState = DownloadState.COMPLETE;
         publish(downloadProgress());
         result = downloaded;
      } catch (Exception e) {
         if (e instanceof InterruptedException) {
            downloadState = DownloadState.CANCEL;
            publish(PUBLISH_CANCEL);
            result = 0;
         } else {
            FileUtil.logToFile(manager.getDestinationFolder(), "log_error.log", e);
            downloadState = DownloadState.ERROR;
            publish(PUBLISH_ERROR);
            result = -1;
         }
         cancel(true);
      } finally {
         try {
            executor.shutdown();
            if (fos != null)
               fos.close();
            if (in != null)
               in.close();

            if (outputFile != null) {
               if (downloadState != DownloadState.COMPLETE) {
                  outputFile.delete();
               } else {
                  savedPath = FileUtil.renameFile(outputFile, URLUtil.getFileName(source));
                  publish(downloadProgress());
                  result = downloaded;
               }
            }

            dataSource.closeConnection();

         } catch (Exception e) {
            
         }
      }
      return result;
   }

   @Override
   public void process(List<Integer> ints) {
      try {
         manager.getAppWindow().setValueAt(ints.get(ints.size() - 1), downloadSeq, 0);
         manager.getAppWindow().setValueAt(downloadState, downloadSeq, 1);
      } catch (NullPointerException ex) {
         System.err.println("manager.getAppWindow() returned null");
      }
   }

   private void transferDataStream(InputStream in, OutputStream fos, ExecutorService executor) throws Exception {

      Future<Integer> futureTask = null;
      StreamReader streamReader = new StreamReader(BUFFER_SIZE, in);

      int writerSize = 0;
      long lastTime = startTime;

      while (downloadState == DownloadState.DOWNLOAD && !isCancelled()) {
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
   }

   private int downloadProgress() {
      return (int) (downloaded * 100 / size);
   }

   private long getElapsedTime() {
      return System.currentTimeMillis() - startTime;
   }

   public DownloadState getDownloadState() {
      return downloadState;
   }

   public Path getSavedPath() {
      return savedPath;
   }

   public int getDownloadSeq() {
      return downloadSeq;
   }

   public String getSource() {
      return source;
   }

   public void setSource(String source) {
      this.source = source;
   }

   public DownloadManager getDownloadManager() {
      return manager;
   }

   public void setDownloadManager(DownloadManager manager) {
      this.manager = manager;
   }
   
   public DataSource getDataSource() {
      return dataSource;
   }
   
   public void setDataSource(DataSource dataSource) {
      this.dataSource = dataSource;
   }
   
   public int getTimeOut() {
      return timeOut;
   }
   
   public void setTimeOut(int timeOut) {
      this.timeOut = timeOut;
   }

   public Downloader clone() {
      Downloader newInstance = new Downloader(downloadSeq);
      manager.putNeccessaryStuff(newInstance, source);
      return newInstance;
   }
}
