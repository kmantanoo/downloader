  package model.datasource;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

import model.CredentialInformation;
import model.exception.InvalidProtocolException;
import utils.URLUtil;

public class FTPDataSource extends DataSource implements CredentialRequired{
   private FTPClient client;
   private FTPFile file;
   private CredentialInformation credInfo;
   private static String DEFAULT_USER = "anonymous";
   private static String DEFAULT_PASSWORD = "test@test.com";
   private static int LOGIN_SUCCESS = 230;
   
   public FTPDataSource() {
      isConnectionOpen = false;
   }

   @Override
   public InputStream getInputStream() throws IOException, URISyntaxException {
      if (! isConnectionOpen) throw new IllegalStateException("Connection was not established yet.");
      return client.retrieveFileStream(URLUtil.getFilePath(source));
   }

   @Override
   public long getSize() {
      if (! isConnectionOpen) throw new IllegalStateException("Connection was not established yet.");
      return file.getSize();
   }

   @Override
   public void openConnection() throws IOException, InvalidProtocolException, URISyntaxException {
      if (isConnectionOpen) throw new IllegalStateException("Connection was established.");
      
      if (client == null)
         client = new FTPClient();
      
      if (source == null)
         throw new NullPointerException("source");
      
      isValidProtocol(source, "ftp");
      client.connect(URLUtil.getHost(source));
      client.login(DEFAULT_USER, DEFAULT_PASSWORD);
      if (client.getReplyCode() != LOGIN_SUCCESS) {
         if (credInfo == null) throw new NullPointerException("credential info");
         client.login(credInfo.getUsername(), credInfo.getPassword());
      }

      try {
         String filePath = URLUtil.getFilePath(source);
         file = client.listFiles(filePath)[0];
      } catch (IndexOutOfBoundsException e) {
         throw new IOException(String.format("%s:Not Found", source));
      }
      isConnectionOpen = true;
   }

   @Override
   public void closeConnection() throws Exception {
      if (! isConnectionOpen) throw new IllegalStateException("Connection was not established yet.");
      if (client != null)
         client.disconnect();
         isConnectionOpen = false;
   }
   
   @Override
   public boolean isRequireCredential() {
      return true;
   }
   
   @Override
   public void setCredentialInfo(CredentialInformation credInfo) {
      this.credInfo = credInfo;
   }
}
