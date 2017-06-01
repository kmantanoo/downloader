package controller;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JOptionPane;

import config.UserSettings;
import model.Downloader;
import model.enumeration.DownloadState;
import model.exception.InvalidURLException;
import utils.TimeUtil;
import view.window.AppWindow;

public class DownloadManager {
   // private List<Thread> threads;
   // private StringBuilder errorMessage;
   private UserSettings settings;
   private AppWindow app;
   private List<String> sources;
   private List<Downloader> workers;

   public DownloadManager(AppWindow app) {
      settings = new UserSettings();
      workers = new ArrayList<Downloader>();
      this.app = app;
   }

   public void downloadFiles(String sourceString) {
      sources = getSourcesList(sourceString);
      workers.clear();
      app.makeButtonClickableAllRow(true);
      int downloadSeq = 0;
      for (String source : sources) {
         newDownload(source, downloadSeq++);
      }
   }

   private void newDownload(String source, int downloadSeq) {
      try {
         Downloader worker = new Downloader(source, settings, app, downloadSeq);
         app.addRow(new Object[] { 0, DownloadState.DOWNLOAD });

         workers.add(worker);
         worker.execute();
      } catch (NullPointerException | InvalidURLException e) {
         try {
            String filePath = logToFile(e);
            String errorMsg = String.format("%s\n%s %s", e.getMessage(), "See full log at", filePath);
            JOptionPane.showMessageDialog(app, errorMsg, "Error", JOptionPane.ERROR_MESSAGE);
         } catch (IOException e1) {
            e1.printStackTrace();
         }
      }
   }

   private String logToFile(Throwable e) throws IOException {
      String destination = settings.getDestinationFolder();
      Path logFilePath = FileSystems.getDefault().getPath(destination, "downloader_error.log");
      FileOutputStream fos = new FileOutputStream(logFilePath.toString(), true);
      PrintStream ps = new PrintStream(fos);

      ps.println();
      ps.println(TimeUtil.getTimeStampWithDate() + "\n");
      e.printStackTrace(ps);
      ps.println();
      fos.close();

      ps.close();

      return logFilePath.toString();
   }

   private List<String> getSourcesList(String sources) {
      sources = sources.replaceAll("\\s", "");
      List<String> output = Arrays.asList(sources.split(","));
      return output;
   }

   public String getDestinationFolder() {
      return settings.getDestinationFolder();
   }

   public void setDestinationFolder(String destination) {
      settings.setDestinationFolder(destination);
   }

   public String getSourceAtIndex(int index) {
      return sources.get(index);
   }

   public Path getSavedFile(int index) {
      return workers.get(index).getSavedPath();
   }

   public void cancelDownload() {
      for (Downloader worker : workers) {
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
      Downloader newInstance = workers.get(downloadSeq).clone();
      workers.set(downloadSeq, newInstance);
      newInstance.execute();
   }
}
