package config;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class AppConfig {
   private static AppConfig instance;
   private static String APP_CONFIG_FILE_NAME = "config.properties";
   private static Properties properties;

   private AppConfig() {

      try {
         properties = new Properties();

         InputStream inStream = getClass().getClassLoader().getResourceAsStream(APP_CONFIG_FILE_NAME);
         if (inStream == null)
            throw new FileNotFoundException(APP_CONFIG_FILE_NAME);

         properties.load(inStream);
         inStream.close();
      } catch (FileNotFoundException ex) {
         System.err.println(String.format("File \"%s\" not found!", APP_CONFIG_FILE_NAME));
      } catch (IOException e) {
         System.err.println("Cannot load properties from file!");
      }
   }

   public static AppConfig getInstance() {
      if (instance == null)
         instance = new AppConfig();
      return instance;
   }

   public String getProp(String key) {
      return properties.getProperty(key);
   }

   public Properties getProperties() {
      return properties;
   }

}
