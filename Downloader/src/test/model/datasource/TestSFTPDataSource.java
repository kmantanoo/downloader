package test.model.datasource;

import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.UnknownHostException;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.Session;

import model.CredentialInformation;
import model.datasource.SFTPDataSource;
import model.exception.InvalidProtocolException;

public class TestSFTPDataSource {
   @Rule
   public ExpectedException expect = ExpectedException.none();
   
   private static SFTPDataSource dataSource;
   private static CredentialInformation info;
   
   @BeforeClass
   public static void setup() {
      dataSource = new SFTPDataSource();
      info = new CredentialInformation();
      info.setUsername("demo");
      info.setPassword("password");
   }

   @After
   public void closeConn() {
      try {
         dataSource.closeConnection();
      } catch (Exception e) {
      }
   }

   @Test
   public void testOpenConnectionWitnNullSource() throws Exception {
      expect.expect(NullPointerException.class);
      dataSource.setSource(null);
      dataSource.openConnection();
   }

   @Test
   public void testOpenConnectionWithInvalidURL() throws Exception {
      expect.expect(InvalidProtocolException.class);
      dataSource = new SFTPDataSource();
      dataSource.setCredentialInfo(info);
      dataSource.setSource("www.google.com");
      dataSource.openConnection();
   }

   @Test
   public void testOpenConnectionWithUnknowHost() throws Exception {
      expect.expect(UnknownHostException.class);
      dataSource = new SFTPDataSource();
      dataSource.setCredentialInfo(info);
      dataSource.setSource("sftp://hostnotexist.com/");
      dataSource.openConnection();
   }
   
   @Test
   public void testOpenConnectionWithNotFound() throws Exception {
      expect.expect(IOException.class);
      dataSource = new SFTPDataSource();
      dataSource.setCredentialInfo(info);
      dataSource.setSource("sftp://test.rebex.net/readme2.txt");
      dataSource.openConnection();
   }

   @Test
   public void testGetSize() throws Exception {
      dataSource = new SFTPDataSource();
      dataSource.setCredentialInfo(info);
      dataSource.setSource("sftp://test.rebex.net/readme.txt");
      dataSource.openConnection();
      assertEquals(403, dataSource.getSize());
      dataSource.closeConnection();
   }
   
   @Test
   public void testGetInputStream() throws Exception {
      dataSource = new SFTPDataSource();
      dataSource.setCredentialInfo(info);
      dataSource.setSource("sftp://test.rebex.net/readme.txt");
      dataSource.openConnection();
      assertNotNull(dataSource.getInputStream());
      dataSource.closeConnection();
   }
   
   @Test
   public void testCloseConnection () throws Exception{
      dataSource = new SFTPDataSource();
      dataSource.setCredentialInfo(info);
      dataSource.setSource("sftp://test.rebex.net/readme.txt");
      dataSource.openConnection();
      dataSource.closeConnection();
      
      Field session = SFTPDataSource.class.getDeclaredField("session");
      Field channel = SFTPDataSource.class.getDeclaredField("ch");
      session.setAccessible(true);
      channel.setAccessible(true);
      Session sessionObj = (Session) session.get(dataSource);
      ChannelSftp channelObj = (ChannelSftp) channel.get(dataSource);
      assertFalse(sessionObj.isConnected());
      assertFalse(channelObj.isConnected());
   }

   @Test
   public void testValidStateOfUse() throws Exception {
      dataSource = new SFTPDataSource();
      dataSource.setCredentialInfo(info);
      dataSource.setSource("sftp://test.rebex.net/readme.txt");
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
      dataSource = new SFTPDataSource();
      dataSource.setSource("sftp://test.rebex.net/readme.txt");
      dataSource.closeConnection();
   }
   
   @Test
   public void testInvalidStateOfUse2() throws Exception {
      expect.expect(IllegalStateException.class);
      dataSource = new SFTPDataSource();
      dataSource.setSource("sftp://test.rebex.net/readme.txt");
      dataSource.getSize();
   }
   
   @Test
   public void testInvalidStateOfUse3() throws Exception {
      expect.expect(IllegalStateException.class);
      dataSource = new SFTPDataSource();
      dataSource.setSource("sftp://test.rebex.net/readme.txt");
      dataSource.getInputStream();
   }
   
   @Test
   public void testInvalidStateOfUse4() throws Exception {
      expect.expect(IllegalStateException.class);
      dataSource = new SFTPDataSource();
      dataSource.setSource("sftp://test.rebex.net/readme.txt");
      dataSource.setCredentialInfo(info);
      dataSource.openConnection();
      dataSource.openConnection();
   }
}
