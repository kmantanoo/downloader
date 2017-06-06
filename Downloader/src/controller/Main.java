package controller;

import java.nio.file.Path;

import javax.swing.SwingWorker.StateValue;

import model.enumeration.DownloadState;

public class Main {
   public static void main(String[] args) {
      DownloadManager manager = new DownloadManager();
      manager.setSources("http://speedtest.tele2.net/512KB.zip");
      manager.downloadFiles();
      while(manager.getDownloader(0).getState() != StateValue.DONE);
      
      Path p = manager.getSavedFile(0);
      System.out.println(p);
   }
}
