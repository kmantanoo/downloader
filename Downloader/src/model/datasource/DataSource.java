package model.datasource;

import java.io.InputStream;

import config.AppConfig;
import model.CredentialInformation;
import model.Downloader;
import model.exception.InvalidProtocolException;
import utils.URLUtil;

public abstract class DataSource {
   protected String source;
   protected AppConfig conf;
   protected Downloader downloader;

   public String getSource() {
      return source;
   }

   public void setSource(String source) {
      this.source = source;
   }

   public void setAppConfig(AppConfig config) {
      this.conf = config;
   }

   public void setDownloader(Downloader downloader) {
      this.downloader = downloader;
   }
   
   public boolean isValidProtocol(String source, String expectedProtocol) throws InvalidProtocolException {
      if (source == null || expectedProtocol == null ||
          "".equals(source) || "".equals(expectedProtocol))
         throw new InvalidProtocolException();
      
      String sourceProtocol = URLUtil.getProtocolFromURL(source);
      
      if ((sourceProtocol != null) && (expectedProtocol.compareToIgnoreCase(sourceProtocol) == 0)) 
         return true;
      else {
         String msg = "";
         if (sourceProtocol == null) {
            msg = String.format("Protocol not found in \"%s\"", source);
         } else {
            msg = String.format("Protocol mismatch, expect %s but %s is found", expectedProtocol, sourceProtocol);
         }
         throw new InvalidProtocolException(msg);
      }
   }
   
   protected CredentialInformation getCredentialInfo() {
      return downloader.getCredentialInfo(source);
   }

   public abstract InputStream getInputStream() throws Exception;

   public abstract long getSize() throws Exception;

   public abstract void openConnection() throws Exception;

   public abstract void closeConnection() throws Exception;
}
