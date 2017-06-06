package test.model.datasource;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URISyntaxException;
import java.net.UnknownHostException;

import org.apache.commons.net.ftp.FTPClient;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import model.datasource.FTPDataSource;
import model.exception.InvalidProtocolException;

public class TestFTPDataSource {

   @Rule
   public ExpectedException expect = ExpectedException.none();

   private static FTPDataSource dataSource;

   @BeforeClass
   public static void setup() {
      dataSource = new FTPDataSource();
   }
   
   @After
   public void closeConn() {
      try {
         dataSource.closeConnection();
      } catch (Exception e) {
      }
   }

   @Test
   public void testOpenConnectionWitnNullSource() throws IOException, InvalidProtocolException, URISyntaxException {
      expect.expect(NullPointerException.class);
      dataSource.setSource(null);
      dataSource.openConnection();
   }

   @Test
   public void testOpenConnectionWithInvalidURL() throws IOException, InvalidProtocolException, URISyntaxException {
      expect.expect(InvalidProtocolException.class);
      dataSource.setSource("www.google.com");
      dataSource.openConnection();
   }

   @Test
   public void testOpenConnectionWithUnknowHost() throws IOException, InvalidProtocolException, URISyntaxException {
      expect.expect(UnknownHostException.class);
      dataSource.setSource("ftp://hostnotexist.com/");
      dataSource.openConnection();
   }
   
   @Test
   public void testOpenConnectionWithNotFound() throws IOException, InvalidProtocolException, URISyntaxException {
      expect.expect(IOException.class);
      String source = "ftp://speedtest.tele2.net/11MB.zip";
      dataSource.setSource(source);
      dataSource.openConnection();
   }

   @Test
   public void testGetSize() throws Exception {
      dataSource.setSource("ftp://speedtest.tele2.net/512KB.zip");
      dataSource.openConnection();
      assertEquals(512*1024, dataSource.getSize());
      dataSource.closeConnection();
      
      dataSource.setSource("ftp://speedtest.tele2.net/10MB.zip");
      dataSource.openConnection();
      assertEquals(10*1024*1024, dataSource.getSize());
      dataSource.closeConnection();
   }
   
   @Test
   public void testGetInputStream() throws Exception {
      dataSource.setSource("ftp://speedtest.tele2.net/512KB.zip");
      dataSource.openConnection();
      assertNotNull(dataSource.getInputStream());
      dataSource.closeConnection();
      
      dataSource.setSource("ftp://speedtest.tele2.net/10MB.zip");
      dataSource.openConnection();
      assertNotNull(dataSource.getInputStream());
      dataSource.closeConnection();
   }
   
   @Test
   public void testCloseConnection () throws Exception{
      dataSource = new FTPDataSource();
      dataSource.setSource("ftp://speedtest.tele2.net/512KB.zip");
      dataSource.openConnection();
      dataSource.closeConnection();
      
      Field f = FTPDataSource.class.getDeclaredField("client");
      f.setAccessible(true);
      FTPClient client = (FTPClient) f.get(dataSource);
      assertFalse(client.isConnected());
   }

   @Test
   public void testValidStateOfUse() throws Exception {
      dataSource = new FTPDataSource();
      dataSource.setSource("ftp://speedtest.tele2.net/");
      dataSource.openConnection();
      dataSource.getSize();
      dataSource.getInputStream();
      dataSource.closeConnection();
      
      dataSource.openConnection();
      dataSource.getInputStream();
      dataSource.getSize();
      dataSource.closeConnection();
   }
   
   @Test
   public void testInvalidStateOfUse() throws Exception {
      expect.expect(IllegalStateException.class);
      dataSource = new FTPDataSource();
      dataSource.setSource("ftp://speedtest.tele2.net/");
      dataSource.closeConnection();
   }
   
   @Test
   public void testInvalidStateOfUse2() throws Exception {
      expect.expect(IllegalStateException.class);
      dataSource = new FTPDataSource();
      dataSource.setSource("ftp://speedtest.tele2.net/");
      dataSource.getSize();
   }
   
   @Test
   public void testInvalidStateOfUse3() throws Exception {
      expect.expect(IllegalStateException.class);
      dataSource = new FTPDataSource();
      dataSource.setSource("ftp://speedtest.tele2.net/");
      dataSource.getInputStream();
   }
   
   @Test
   public void testInvalidStateOfUse4() throws Exception {
      expect.expect(IllegalStateException.class);
      dataSource = new FTPDataSource();
      dataSource.setSource("ftp://speedtest.tele2.net/");
      dataSource.openConnection();
      dataSource.openConnection();
   }
}
