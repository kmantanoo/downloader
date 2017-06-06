package test.controller;

import static org.junit.Assert.*;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.swing.SwingWorker.StateValue;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import config.AppConfig;
import config.UserSettings;
import controller.DownloadManager;
import controller.Downloader;
import model.datasource.DataSource;
import model.datasource.FTPDataSource;
import model.datasource.HTTPDataSource;
import model.datasource.SFTPDataSource;
import model.enumeration.DownloadState;

public class TestDownloadManager {
   private static Path backingStorePath;
   private static Path testDir;
   
   @Rule
   public ExpectedException exp = ExpectedException.none();
   
   @BeforeClass
   public static void setup() {
      DownloadManager manager = new DownloadManager();
      backingStorePath = Paths.get(manager.getDestinationFolder());
      testDir = Paths.get(System.getProperty("user.home"), "testdir");
      manager.setDestinationFolder(testDir.toString());
      testDir.toFile().mkdir();
   }
   
   @AfterClass
   public static void cleanup() {
      DownloadManager manager = new DownloadManager();
      manager.setDestinationFolder(backingStorePath.toString());
      testDir.toFile().delete();
   }
   
   @After
   public void clearTestDir() {
      File[] files = testDir.toFile().listFiles();
      for (File f : files) {
         f.delete();
      }
   }
   
   @Test
   public void testGetDestinationFolder() {
      UserSettings settings = new UserSettings();
      DownloadManager manager = new DownloadManager();
      assertEquals(settings.getDestinationFolder(), manager.getDestinationFolder());
   }

   @Test
   public void testSetDestinationFolder() {
      DownloadManager manager = new DownloadManager();
      
      manager.setDestinationFolder(backingStorePath.toString());
      assertEquals(backingStorePath.toString(), manager.getDestinationFolder());
      manager.setDestinationFolder(testDir.toString());
      assertEquals(testDir.toString(), manager.getDestinationFolder());
   }
   
   @Test
   public void testGetSourceAtIndex() {
      String sources = "http://test/source/one, ftp://test/source/two, sftp://test/source/three";
      DownloadManager manager = new DownloadManager();
      manager.setSources(sources);
      assertEquals("http://test/source/one", manager.getSourceAtIndex(0));
      assertEquals("ftp://test/source/two", manager.getSourceAtIndex(1));
      assertEquals("sftp://test/source/three", manager.getSourceAtIndex(2));
      assertNull(manager.getSourceAtIndex(3));
      assertNull(manager.getSourceAtIndex(-1));
   }
   
   @Test
   public void testGetSavedFile() {
      DownloadManager manager = new DownloadManager();
      manager.setSources("http://speedtest.tele2.net/512KB.zip");
      manager.downloadFiles();
      while(manager.getDownloader(0).getState() != StateValue.DONE);
      Path p = manager.getSavedFile(0);
      assertNotNull(p);
      assertTrue(p.toFile().exists());
   }
   
   @Test
   public void testCancelDownload() {
      DownloadManager manager = new DownloadManager();
      manager.setSources("http://speedtest.tele2.net/5MB.zip");
      manager.downloadFiles();
      manager.cancelDownload(0);
      assertEquals(StateValue.DONE, manager.getDownloader(0).getState());
   }

   @Test
   public void testCancelDownload2() {
      DownloadManager manager = new DownloadManager();
      manager.setSources("http://speedtest.tele2.net/5MB.zip, ftp://speedtest.tele2.net/5MB.zip");
      manager.downloadFiles();
      manager.cancelDownload();
      assertEquals(StateValue.DONE, manager.getDownloader(0).getState());
      assertEquals(StateValue.DONE, manager.getDownloader(1).getState());
   }
   
   @Test
   public void testGetDownloadState() {
      DownloadManager manager = new DownloadManager();
      manager.setSources("http://speedtest.tele2.net/512KB.zip");
      manager.downloadFiles();
      while(manager.getDownloader(0).getState() != StateValue.DONE);
      Path p = manager.getSavedFile(0);
      assertEquals(DownloadState.COMPLETE, manager.getDownloadState(0));
      assertNotNull(p);
      assertTrue(p.toFile().exists());
      assertEquals(512 * 1024, p.toFile().length());
   }

   @Test
   public void testGetTimeOut() {
      DownloadManager manager = new DownloadManager();
      
      assertEquals(Integer.parseInt(AppConfig.getInstance().getProp("timeout")), manager.getTimeOut());
   }
   
   @Test
   public void testRestartWorker() {
      DownloadManager manager = new DownloadManager();
      manager.setSources("http://speedtest.tele2.net/512KB.zip");
      manager.downloadFiles();
      manager.cancelDownload(0);
      manager.restartWorker(0);
      while(manager.getDownloader(0).getState() != StateValue.DONE);
      assertEquals(DownloadState.COMPLETE, manager.getDownloadState(0));
      Path savedPath = manager.getSavedFile(0);
      assertNotNull(savedPath);
      assertTrue(savedPath.toFile().exists());
      assertEquals(512 * 1024, savedPath.toFile().length());
   }
   
   @Test
   public void testInstanceDataSource() {
      DownloadManager manager = new DownloadManager();
      DataSource dataSource = manager.instanceDataSource("http://test/source");
      assertTrue(dataSource instanceof HTTPDataSource);
      
      dataSource = manager.instanceDataSource("ftp://test/source");
      assertTrue(dataSource instanceof FTPDataSource);
      
      dataSource = manager.instanceDataSource("sftp://test/source");
      assertTrue(dataSource instanceof SFTPDataSource);
      
      dataSource = manager.instanceDataSource("https://test/source");
      assertNull(dataSource);
   }
   
   @Test
   public void testPutNeccessaryStuff() {
      DownloadManager manager = new DownloadManager();
      Downloader downloader = new Downloader(0);
      assertNull(downloader.getSource());
      assertNull(downloader.getDataSource());
      assertEquals(0, downloader.getTimeOut());
      
      manager.putNeccessaryStuff(downloader, "http://test/source");
      assertEquals("http://test/source", downloader.getSource());
      assertTrue(downloader.getDataSource() instanceof HTTPDataSource);
      assertEquals(Integer.parseInt(AppConfig.getInstance().getProp("timeout")), downloader.getTimeOut());
   }
   
   @Test
   public void testDownloadFiles() {
      DownloadManager manager = new DownloadManager();
      manager.setSources("http://speedtest.tele2.net/512KB.zip, ftp://speedtest.tele2.net/1MB.zip");
      manager.downloadFiles();
      while(manager.getDownloader(0).getState() != StateValue.DONE);
      Path p = manager.getSavedFile(0);
      assertNotNull(p);
      assertTrue(p.toFile().exists());
      assertEquals(512 * 1024, p.toFile().length());
      
      while(manager.getDownloader(1).getState() != StateValue.DONE);
      p = manager.getSavedFile(1);
      assertNotNull(p);
      assertTrue(p.toFile().exists());
      assertEquals(1024 * 1024, p.toFile().length());
   }
}
