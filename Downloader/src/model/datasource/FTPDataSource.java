  package model.datasource;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.AccessDeniedException;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

import model.CredentialInformation;
import model.exception.InvalidProtocolException;
import utils.URLUtil;

public class FTPDataSource extends DataSource {
   private FTPClient client;
   private FTPFile file;
   private static String DEFAULT_USER = "anonymous";
   private static String DEFAULT_PASSWORD = "test@test.com";
   private static int LOGIN_SUCCESS = 230;

   @Override
   public InputStream getInputStream() throws IOException {
      return client.retrieveFileStream(URLUtil.getFilePath(source));
   }

   @Override
   public long getSize() {
      return file.getSize();
   }

   @Override
   public void openConnection() throws IOException, InvalidProtocolException {

      if (client == null)
         client = new FTPClient();
      
      if (source == null)
         throw new NullPointerException("source");
      
      isValidProtocol(source, "ftp");
      client.connect(URLUtil.getHost(source));
      client.login(DEFAULT_USER, DEFAULT_PASSWORD);
      if (client.getReplyCode() != LOGIN_SUCCESS) {
         CredentialInformation credInfo = getCredentialInfo();
         client.login(credInfo.getUsername(), credInfo.getPassword());
      }

      try {
         String filePath = URLUtil.getFilePath(source);
         file = client.listFiles(filePath)[0];
      } catch (IndexOutOfBoundsException e) {
         throw new AccessDeniedException(URLUtil.getFilePath(source));
      }
   }

   @Override
   public void closeConnection() throws Exception {
      if (client != null)
         client.disconnect();
   }

//   @Override
//   public void setCredentialInfo(String username, String password) {
//      this.username = username;
//      this.password = password;
//   }
}
