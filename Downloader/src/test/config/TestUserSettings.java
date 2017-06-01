package test.config;

import static org.junit.Assert.*;

import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import config.UserSettings;

public class TestUserSettings {
   private static String tempDir;
   private static UserSettings pref;
   
   @BeforeClass
   public static void setup() throws BackingStoreException {
      pref = new UserSettings();
      tempDir = pref.getDestinationFolder();
      pref.getPreferences().clear();
   }
   
   @AfterClass
   public static void endtest() {
      pref.setDestinationFolder(tempDir);
   }

   @Test
   public void testGetDefaultDestination() throws BackingStoreException {
      pref.getPreferences().clear();
      String homeDownloads = System.getProperty("user.home") + "\\Downloads";
      assertEquals(homeDownloads, pref.getDestinationFolder());
   }
   
   @Test
   public void testGetLastDestination() throws BackingStoreException {
      pref.getPreferences().clear();
      String dummyDestination = "C:/test";
      pref.setDestinationFolder(dummyDestination);
      assertEquals(dummyDestination, pref.getDestinationFolder());
   }
   
   @Test
   public void testGetPreferences() {
      assertTrue(pref.getPreferences() instanceof Preferences);
   }

}
