package controller;

import java.awt.EventQueue;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.UIManager;

import config.AppConfig;
import config.UserSettings;
import model.CredentialInformation;
import model.datasource.CredentialRequired;
import model.datasource.DataSource;
import model.datasource.DataSourceFactory;
import model.enumeration.DownloadState;
import utils.URLUtil;
import view.window.AppWindow;

public class DownloadManager {
   private UserSettings settings;
   private AppConfig conf;
   private AppWindow app;
   private List<Downloader> workers;
   private String sources;

   public DownloadManager() {
      settings = new UserSettings();
      conf = AppConfig.getInstance();
      workers = new ArrayList<Downloader>();
   }

   public void setAppWindow(AppWindow app) {
      this.app = app;
   }

   public void downloadFiles() {
      workers.clear();
      if (app != null) app.makeButtonClickableAllRow(true);
      int downloadSeq = 0;
      for (String source : getSourcesList(sources)) {
         newDownload(source, downloadSeq++);
      }
   }

   private void newDownload(String source, int downloadSeq) {
      // try {
      // } catch (NullPointerException | InvalidURLException e) {
      // try {
      // String filePath = FileUtil.logToFile(getDestinationFolder(),
      // "download_error.log", e);
      // String errorMsg = String.format("%s\n%s %s", e.getMessage(), "See full
      // log at", filePath);
      // JOptionPane.showMessageDialog(app, errorMsg, "Error",
      // JOptionPane.ERROR_MESSAGE);
      // } catch (IOException e1) {
      // e1.printStackTrace();
      // }
      // }
      Downloader worker = new Downloader(downloadSeq);
      putNeccessaryStuff(worker, source);
      if (app != null) app.addRow(new Object[] { 0, DownloadState.DOWNLOAD});

      workers.add(worker);
      worker.execute();
   }
   
   public void putNeccessaryStuff(Downloader worker, String source) {
      worker.setSource(source);
      worker.setDownloadManager(this);
      worker.setDataSource(instanceDataSource(source));
      worker.setTimeOut(getTimeOut());
   }

   public DataSource instanceDataSource(String source) {
      DataSource dataSource = null;
      try {
         String dataSourceClass = conf.getProp("protocol." + URLUtil.getProtocolFromURL(source));
         String dataSourcePackage = conf.getProp("datasource.package");

         dataSource = DataSourceFactory.newInstance(String.format("%s.%s", dataSourcePackage, dataSourceClass), source);
         if (dataSource.isRequireCredential()) {
            CredentialInformation credInfo = app.getCredentialInfo(source);
            ((CredentialRequired) dataSource).setCredentialInfo(credInfo);
         }
      } catch (Exception e) {
         
      }
      return dataSource;
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
   
   public void setSources(String sources) {
      this.sources = sources;
   }

   public String getSourceAtIndex(int index) {
      return (index < 0 || index >= (getSourcesList(sources).size())) ? 
            null : getSourcesList(sources).get(index);
   }

   public Path getSavedFile(int index) {
      return workers.get(index).getSavedPath();
   }

   public AppWindow getAppWindow() {
      return app;
   }

   public void cancelDownload() {
      for (int i = 0; i < workers.size(); i++) {
         cancelDownload(i);
      }
   }

   public void cancelDownload(int downloadSeq) {
      workers.get(downloadSeq).cancel(true);
   }

   public DownloadState getDownloadState(int downloadSeq) {
      return workers.get(downloadSeq).getDownloadState();
   }
   
   public Downloader getDownloader(int index) {
      return workers.get(index);
   }

   public int getTimeOut() {
      return Integer.parseInt(conf.getProp("timeout"));
   }

   public void restartWorker(int downloadSeq) {
      Downloader newInstance = workers.get(downloadSeq).clone();
      workers.set(downloadSeq, newInstance);
      newInstance.execute();
   }

   /**
    * Launch the application.
    */
   public static void main(String[] args) {
      EventQueue.invokeLater(new Runnable() {
         public void run() {
            try {
               UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
               DownloadManager dlManager = new DownloadManager();
               AppWindow appWindow = new AppWindow();

               dlManager.setAppWindow(appWindow);
               appWindow.setDownloadManager(dlManager);
               appWindow.setVisible(true);
            } catch (Exception e) {
               e.printStackTrace();
            }
         }
      });
   }
}
