package test.model.datasource;

import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.UnknownHostException;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import model.datasource.HTTPDataSource;
import model.exception.InvalidProtocolException;

public class TestHTTPDataSource {

   @Rule
   public ExpectedException expect = ExpectedException.none();

   private static HTTPDataSource dataSource;

   @BeforeClass
   public static void setup() {
      dataSource = new HTTPDataSource();
   }

   @Test
   public void testOpenConnectionWitnNullSource() throws IOException, InvalidProtocolException {
      expect.expect(NullPointerException.class);
      dataSource.setSource(null);
      dataSource.openConnection();
   }

   @Test
   public void testOpenConnectionWithInvalidURL() throws IOException, InvalidProtocolException {
      expect.expect(MalformedURLException.class);
      dataSource.setSource("www.google.com");
      dataSource.openConnection();
   }
   
   @Test
   public void testOpenConnectionWithInvalidProtocol() throws IOException, InvalidProtocolException {
      expect.expect(InvalidProtocolException.class);
      dataSource.setSource("https://www.google.com");
      dataSource.openConnection();
   }

   @Test
   public void testOpenConnectionWithUnknowHost() throws IOException, InvalidProtocolException {
      expect.expect(UnknownHostException.class);
      dataSource.setSource("http://www.hostnotexist.com/");
      dataSource.openConnection();
   }
   
   @Test
   public void testOpenConnectionWithNotFound() throws IOException, InvalidProtocolException {
      expect.expect(IOException.class);
      String source = "http://www.google.com/testpage";
      dataSource.setSource(source);
      dataSource.openConnection();
   }

   @Test
   public void testGetSize() throws Exception {
      dataSource.setSource("http://speedtest.tele2.net/512KB.zip");
      dataSource.openConnection();
      assertEquals(512*1024, dataSource.getSize());
      dataSource.closeConnection();
      
      dataSource.setSource("http://speedtest.tele2.net/10MB.zip");
      dataSource.openConnection();
      assertEquals(10*1024*1024, dataSource.getSize());
      dataSource.closeConnection();
   }
   
   @Test
   public void testGetInputStream() throws Exception {
      dataSource.setSource("http://speedtest.tele2.net/512KB.zip");
      dataSource.openConnection();
      assertNotNull(dataSource.getInputStream());
      dataSource.closeConnection();
      
      dataSource.setSource("http://speedtest.tele2.net/10MB.zip");
      dataSource.openConnection();
      assertNotNull(dataSource.getInputStream());
      dataSource.closeConnection();
   }
   
   @Test
   public void testCloseConnectionWithoutOpenBefore() throws Exception {
      expect.expect(NullPointerException.class);
      dataSource = new HTTPDataSource();
      dataSource.closeConnection();
   }
   
   @Test
   public void testCloseConnection () throws Exception{
      dataSource = new HTTPDataSource();
      dataSource.setSource("http://speedtest.tele2.net/512KB.zip");
      dataSource.openConnection();
      dataSource.closeConnection();
      
      Field f = HTTPDataSource.class.getDeclaredField("conn");
      f.setAccessible(true);
      HttpURLConnection conn = (HttpURLConnection) f.get(dataSource);
      assertNull(conn);
   }
}