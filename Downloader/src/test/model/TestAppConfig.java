package test.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Constructor;
import java.util.Properties;

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

	@Test
	public void testSaveToFile() {
		String keyToChange, oldValue, newValue;
		AppConfig newConfig = null;
		Constructor<?> cons = AppConfig.class.getDeclaredConstructors()[0];
		Properties props = AppConfig.getInstance().getProperties();
		
		cons.setAccessible(true);

		keyToChange = "location.destination";
		oldValue = props.getProperty(keyToChange);
		newValue = "D:/";
		props.replace(keyToChange, oldValue, newValue);
		
		if (AppConfig.getInstance().saveToFile()){

			try {
				newConfig = (AppConfig) cons.newInstance();
				assertNotNull(newConfig);
				assertTrue(newConfig.hashCode() != AppConfig.getInstance().hashCode());
				
				props = newConfig.getProperties();
				assertEquals(newValue, props.getProperty(keyToChange));
				
				props.replace(keyToChange, newValue, oldValue);
				newConfig.saveToFile();
			} catch (Exception e) { 
				e.printStackTrace();
			}
		}
		
		
	}
}
