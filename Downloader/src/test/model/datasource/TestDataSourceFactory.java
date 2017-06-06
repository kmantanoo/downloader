package test.model.datasource;

import static org.junit.Assert.*;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import model.datasource.DataSource;
import model.datasource.DataSourceFactory;
import model.datasource.FTPDataSource;
import model.datasource.HTTPDataSource;
import model.datasource.SFTPDataSource;

public class TestDataSourceFactory {
   
   @Rule
   public ExpectedException exp = ExpectedException.none();

   @Test
   public void testGetHTTPDataSource() throws InstantiationException, IllegalAccessException, ClassNotFoundException {
      DataSource httpDataSource = DataSourceFactory.newInstance("model.datasource.HTTPDataSource", "http://test.com");
      assertTrue(httpDataSource instanceof HTTPDataSource);
      assertEquals("http://test.com", httpDataSource.getSource());
   }
   
   @Test
   public void testGetFTPDataSource() throws InstantiationException, IllegalAccessException, ClassNotFoundException {
      DataSource ftpDataSource = DataSourceFactory.newInstance("model.datasource.FTPDataSource", "ftp://test.com");
      assertTrue(ftpDataSource instanceof FTPDataSource);
      assertEquals("ftp://test.com", ftpDataSource.getSource());
   }
   
   @Test 
   public void testGetSftpDataSource() throws InstantiationException, IllegalAccessException, ClassNotFoundException {
      DataSource sftpDataSource = DataSourceFactory.newInstance("model.datasource.SFTPDataSource", "sftp://test.com");
      assertTrue(sftpDataSource instanceof SFTPDataSource);
      assertEquals("sftp://test.com", sftpDataSource.getSource());
   }
   
   @Test
   public void testGetDataSourceWithInvalidClassPath() throws InstantiationException, IllegalAccessException, ClassNotFoundException {
      exp.expect(ClassNotFoundException.class);
      DataSourceFactory.newInstance("model.datasource.HTTPSDataSource", "https://test.com");
   }

   @Test
   public void testGetDataSourceWithInvalidSource() throws InstantiationException, IllegalAccessException, ClassNotFoundException {
      DataSourceFactory.newInstance("model.datasource.FTPDataSource", "http://test.com///");
   }
   
   @Test
   public void testGetDataSourceWithInvalidProtocol() throws InstantiationException, IllegalAccessException, ClassNotFoundException {
      DataSourceFactory.newInstance("model.datasource.FTPDataSource", "http://test.com");
   }
}
