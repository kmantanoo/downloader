package test.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import controller.DownloadManager;
import controller.Downloader;
import model.CredentialInformation;
import model.datasource.CredentialRequired;
import model.enumeration.DownloadState;
import model.exception.InvalidURLException;

public class TestDownloader {
   private static Downloader downloader;
   private static String basicSource = "http://speedtest.tele2.net/100KB.zip";
   private static Path backingStorePath;
   private static Path testDir;
   
   @Rule
   public ExpectedException exp = ExpectedException.none();
   
   @BeforeClass
   public static void setup() {
      DownloadManager man = new DownloadManager();
      backingStorePath = Paths.get(man.getDestinationFolder());
      testDir = Paths.get(System.getProperty("user.home"), "testdir");
      testDir.toFile().mkdir();
      man.setDestinationFolder(testDir.toString());
   }
   
   @AfterClass
   public static void cleanup() {
      DownloadManager man = new DownloadManager();
      man.setDestinationFolder(backingStorePath.toString());
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
   public void testDownloadWithNullManager() throws NullPointerException, InvalidURLException {
      downloader = new Downloader(0);
      downloader.setDownloadManager(null);
      downloader.setSource(basicSource);
      downloader.execute();
      while(!downloader.isDone());
      assertEquals(DownloadState.ERROR, downloader.getDownloadState());
   }
   
   @Test
   public void testDownloadWithNullSource() throws NullPointerException, InvalidURLException {
      downloader = new Downloader(0);
      downloader.setDownloadManager(new DownloadManager());
      downloader.setSource(null);
      downloader.execute();
      while(!downloader.isDone());
      assertEquals(DownloadState.ERROR, downloader.getDownloadState());
   }
   
   @Test
   public void testDownloadWithInvalidURL() throws NullPointerException, InvalidURLException {
      downloader = new Downloader(0);
      downloader.setDownloadManager(new DownloadManager());
      downloader.setSource("http://test/download//file");
      downloader.execute();
      while(!downloader.isDone());
      assertEquals(DownloadState.ERROR, downloader.getDownloadState());
   }

   @Test
   public void testClone() throws Exception {
      DownloadManager manager = new DownloadManager();
      downloader = new Downloader(2);
      downloader.setDownloadManager(manager);
      downloader.setSource(basicSource);
      Downloader newInstance = downloader.clone();
      assertEquals(2, newInstance.getDownloadSeq());
      assertEquals(manager, newInstance.getDownloadManager());
      assertEquals(basicSource, newInstance.getSource());
   }
   
   @Test
   public void testDownloadHTTP() {
      DownloadManager manager = new DownloadManager();
      downloader = new Downloader(0);
      manager.putNeccessaryStuff(downloader, "http://speedtest.tele2.net/512KB.zip");
      downloader.execute();
      while(!downloader.isDone());
      File downloaded = downloader.getSavedPath().toFile();
      assertEquals(DownloadState.COMPLETE, downloader.getDownloadState());
      assertTrue(downloaded.exists());
      assertEquals(512*1024, downloaded.length());
   }
   
   @Test
   public void testDownloadHTTPWithNotExistUrl() {
      DownloadManager manager = new DownloadManager();
      downloader = new Downloader(0);
      manager.putNeccessaryStuff(downloader, "http://speedtest.tele2.net/5122KB.zip");
      downloader.execute();
      while(!downloader.isDone());
      assertEquals(DownloadState.ERROR, downloader.getDownloadState());
   }
   
   @Test
   public void testDownloadFTP() {
      DownloadManager manager = new DownloadManager();
      downloader = new Downloader(0);
      manager.putNeccessaryStuff(downloader, "ftp://speedtest.tele2.net/512KB.zip");
      downloader.execute();
      while(!downloader.isDone());
      File downloaded = downloader.getSavedPath().toFile();
      assertEquals(DownloadState.COMPLETE, downloader.getDownloadState());
      assertTrue(downloaded.exists());
      assertEquals(512*1024, downloaded.length());
   }
   
   @Test
   public void testDownloadFTPWithNotExistUrl() {
      DownloadManager manager = new DownloadManager();
      downloader = new Downloader(0);
      manager.putNeccessaryStuff(downloader, "ftp://speedtest.tele2.net/5122KB.zip");
      downloader.execute();
      while(!downloader.isDone());
      assertEquals(DownloadState.ERROR, downloader.getDownloadState());
   }
   
   @Test
   public void testDownloadSftp() {
      DownloadManager manager = new DownloadManager();
      CredentialInformation info = new CredentialInformation();
      info.setUsername("demo");
      info.setPassword("password");
      downloader = new Downloader(0);
      
      manager.putNeccessaryStuff(downloader, "sftp://test.rebex.net/readme.txt");
      
      ((CredentialRequired) downloader.getDataSource()).setCredentialInfo(info);
      downloader.execute();
      
      while(!downloader.isDone());
      
      File downloaded = downloader.getSavedPath().toFile();
      
      assertEquals(DownloadState.COMPLETE, downloader.getDownloadState());
      assertTrue(downloaded.exists());
      assertEquals(403, downloaded.length());
   }
   
   @Test
   public void testDownloadSftpWithNotExistUrl() {
      DownloadManager manager = new DownloadManager();
      CredentialInformation info = new CredentialInformation();
      info.setUsername("demo");
      info.setPassword("password");
      downloader = new Downloader(0);
      
      manager.putNeccessaryStuff(downloader, "sftp://test.rebex.net/readmex.txt");
      
      ((CredentialRequired) downloader.getDataSource()).setCredentialInfo(info);
      downloader.execute();
      
      while(!downloader.isDone());
      
      assertEquals(DownloadState.ERROR, downloader.getDownloadState());
   }
   
   @Test
   public void testDownloadSftpWithAuthFail() {
      DownloadManager manager = new DownloadManager();
      CredentialInformation info = new CredentialInformation();
      info.setUsername("demo");
      info.setPassword("passworda");
      downloader = new Downloader(0);
      
      manager.putNeccessaryStuff(downloader, "sftp://test.rebex.net/readme.txt");
      
      ((CredentialRequired) downloader.getDataSource()).setCredentialInfo(info);
      downloader.execute();
      
      while(!downloader.isDone());
      
      assertEquals(DownloadState.ERROR, downloader.getDownloadState());
   }
   
   @Test
   public void testCancelDownload() {
      DownloadManager manager = new DownloadManager();
      Downloader downloader = new Downloader(0);
      manager.putNeccessaryStuff(downloader, "http://speedtest.tele2.net/100MB.zip");
      downloader.execute();
      downloader.cancel(true);
      assertTrue(downloader.isDone());
   }
}
