package test.config;

import static org.junit.Assert.*;

import org.junit.Test;

import config.AppConfig;

public class TestAppConfig {
   private AppConfig conf = AppConfig.getInstance();

   @Test
   public void testGetInstance() {
      assertNotNull(AppConfig.getInstance());
      assertTrue(AppConfig.getInstance() instanceof AppConfig);
   }

   @Test
   public void testGetProp() {
      assertEquals("HTTPDataSource", conf.getProp("protocol.http"));
      assertEquals("FTPDataSource", conf.getProp("protocol.ftp"));
      assertEquals("SFTPDataSource", conf.getProp("protocol.sftp"));
      assertEquals("15000", conf.getProp("timeout"));
      assertEquals("model.datasource", conf.getProp("datasource.package"));
   }
   
   @Test
   public void testGetPropWithNull() {
      assertNull(conf.getProp(null));
   }
   
   @Test
   public void testGetPropWithNotExistKey() {
      assertNull(conf.getProp("test"));
   }
}
