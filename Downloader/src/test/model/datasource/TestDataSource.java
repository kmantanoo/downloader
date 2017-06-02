package test.model.datasource;

import static org.junit.Assert.*;

import java.io.InputStream;
import java.lang.reflect.Field;

import org.junit.BeforeClass;
import org.junit.Test;

import config.AppConfig;
import controller.DownloadManager;
import model.Downloader;
import model.datasource.DataSource;
import model.exception.InvalidProtocolException;

public class TestDataSource {
   private static DataSource dataSource;
   
   @BeforeClass
   public static void setup() {
      dataSource = new DataSource() {
         
         @Override
         public void openConnection() throws Exception {
            // TODO Auto-generated method stub
            
         }
         
         @Override
         public long getSize() throws Exception {
            // TODO Auto-generated method stub
            return 0;
         }
         
         @Override
         public InputStream getInputStream() throws Exception {
            // TODO Auto-generated method stub
            return null;
         }
         
         @Override
         public void closeConnection() throws Exception {
            // TODO Auto-generated method stub
            
         }
      };
   }

   @Test
   public void testGetSetSource() {
      dataSource.setSource(null);
      assertEquals(null, dataSource.getSource());
      
      dataSource.setSource("source");
      assertEquals("source", dataSource.getSource());
      
      String source = "http://some.url.com/file/to/path";
      dataSource.setSource(source);
      assertEquals(source, dataSource.getSource());
      
      dataSource.setSource(null);
      assertEquals(null, dataSource.getSource());
   }
   
   @Test
   public void testSetAppConfig() throws Exception {
      Field conf = DataSource.class.getDeclaredField("conf");
      conf.setAccessible(true);
      
      dataSource.setAppConfig(AppConfig.getInstance());
      AppConfig fromField = (AppConfig) conf.get(dataSource);
      assertEquals(AppConfig.getInstance(), fromField);
   }

   @Test
   public void testSetDownloader() throws Exception {
      Field downloader = DataSource.class.getDeclaredField("downloader");
      downloader.setAccessible(true);
      
      Downloader dl = new Downloader(new DownloadManager(), "http://test.com", 0);
      dataSource.setDownloader(dl);
      Downloader fromField = (Downloader) downloader.get(dataSource);
      assertEquals(dl, fromField);
   }
   
   @Test
   public void testProtocolIsValidProtocol() throws Exception {
      assertTrue(dataSource.isValidProtocol("http://www.google.com", "HTTP"));
      assertTrue(dataSource.isValidProtocol("ftp://speedtest.tele2.net/512KB.zip", "Ftp"));
      assertTrue(dataSource.isValidProtocol("sftp://localhost", "SfTP"));
   }
   
   @Test(expected=InvalidProtocolException.class)
   public void testProtocolIsNotValidProtocol() throws Exception {
      assertTrue(dataSource.isValidProtocol("https://www.google.com", "http"));
   }
   
   @Test(expected=InvalidProtocolException.class)
   public void testProtocolIsNotValidProtocol2() throws Exception {
      assertTrue(dataSource.isValidProtocol("ftps://www.google.com", "ftp"));
   }
   
   @Test(expected=InvalidProtocolException.class)
   public void testProtocolIsNotValidProtocol3() throws Exception {
      assertTrue(dataSource.isValidProtocol("http://www.google.com", "sftp"));
   }
   
   @Test(expected=InvalidProtocolException.class)
   public void testIsValidProtocolWithNull() throws InvalidProtocolException {
      assertTrue(dataSource.isValidProtocol(null, "http"));
   }
   
   @Test(expected=InvalidProtocolException.class)
   public void testIsValidProtocolWithNull2() throws InvalidProtocolException {
      assertTrue(dataSource.isValidProtocol("http://www.google.com", null));
   }
   
   @Test(expected=InvalidProtocolException.class)
   public void testIsValidProtocolWithEmpty() throws InvalidProtocolException {
      assertTrue(dataSource.isValidProtocol("", "http"));
   }
   
   @Test(expected=InvalidProtocolException.class)
   public void testIsValidProtocolWithEmpty2() throws InvalidProtocolException {
      assertTrue(dataSource.isValidProtocol("http://www.google.com", ""));
   }
   
   @Test(expected=InvalidProtocolException.class)
   public void testIsValidProtocolWithNotContainsProtocol() throws InvalidProtocolException {
      assertTrue(dataSource.isValidProtocol("www.google.com", "http"));
   }
}
