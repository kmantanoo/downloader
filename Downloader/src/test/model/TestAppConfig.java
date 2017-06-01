package test.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import config.AppConfig;

public class TestAppConfig {

   @Test
   public void testGetInstance() {
      assertNotNull(AppConfig.getInstance());
      assertTrue(AppConfig.getInstance() instanceof AppConfig);
   }

   @Test
   public void testGetProp() {
      AppConfig conf = AppConfig.getInstance();

      assertEquals("HTTPDataSource", conf.getProp("protocol.http"));
      assertEquals("FTPDataSource", conf.getProp("protocol.ftp"));
      assertEquals("SFTPDataSource", conf.getProp("protocol.sftp"));
      assertEquals("user", conf.getProp("protocol.sftp.user"));
      assertEquals("password", conf.getProp("protocol.sftp.password"));
      assertEquals("C:/", conf.getProp("location.destination"));
      assertEquals("15000", conf.getProp("timeout"));
      assertEquals(null, conf.getProp("test"));
   }
}
