package test.model.datasource;

import static org.junit.Assert.assertTrue;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URISyntaxException;

import org.junit.BeforeClass;
import org.junit.Test;

import model.datasource.DataSource;
import model.exception.InvalidProtocolException;

public class TestDataSource {
   private static DataSource dataSource;
   
   @BeforeClass
   public static void setup() {
      dataSource = new DataSource(){

         @Override
         public InputStream getInputStream() throws Exception {
            return null;
         }

         @Override
         public long getSize() throws Exception {
            return 0;
         }

         @Override
         public void openConnection() throws Exception {
            
         }

         @Override
         public void closeConnection() throws Exception {
         }

         @Override
         public boolean isRequireCredential() {
            return false;
         }
         
      };
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
   public void testIsValidProtocolWithNull() throws InvalidProtocolException, MalformedURLException, URISyntaxException {
      assertTrue(dataSource.isValidProtocol(null, "http"));
   }
   
   @Test(expected=InvalidProtocolException.class)
   public void testIsValidProtocolWithNull2() throws InvalidProtocolException, MalformedURLException, URISyntaxException {
      assertTrue(dataSource.isValidProtocol("http://www.google.com", null));
   }
   
   @Test(expected=InvalidProtocolException.class)
   public void testIsValidProtocolWithEmpty() throws InvalidProtocolException, MalformedURLException, URISyntaxException {
      assertTrue(dataSource.isValidProtocol("", "http"));
   }
   
   @Test(expected=InvalidProtocolException.class)
   public void testIsValidProtocolWithEmpty2() throws InvalidProtocolException, MalformedURLException, URISyntaxException {
      assertTrue(dataSource.isValidProtocol("http://www.google.com", ""));
   }
   
   @Test(expected=InvalidProtocolException.class)
   public void testIsValidProtocolWithNotContainsProtocol() throws InvalidProtocolException, MalformedURLException, URISyntaxException {
      assertTrue(dataSource.isValidProtocol("www.google.com", "http"));
   }
}
