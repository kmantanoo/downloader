package model.datasource;

import java.io.InputStream;

import config.AppConfig;
import view.window.AppWindow;

public abstract class DataSource {
   protected String source;
   protected AppConfig conf;
   protected AppWindow app;

   public String getSource() {
      return source;
   }

   public void setSource(String source) {
      this.source = source;
   }

   public void setAppConfig(AppConfig config) {
      this.conf = config;
   }

   public void setAppWindow(AppWindow app) {
      this.app = app;
   }

   public abstract InputStream getInputStream() throws Exception;

   public abstract long getSize() throws Exception;

   public abstract void openConnection() throws Exception;

   public abstract void closeConnection() throws Exception;
}
