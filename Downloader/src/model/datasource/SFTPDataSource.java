package model.datasource;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Vector;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.ChannelSftp.LsEntry;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

import model.CredentialInformation;
import utils.URLUtil;

public class SFTPDataSource extends DataSource implements CredentialRequired {
   private Session session;
   private ChannelSftp ch;
//   private String filePath;
   private static int DEFAULT_SFTP_PORT = 22;
   private CredentialInformation credInfo;

   public SFTPDataSource() {
      isConnectionOpen = false;
   }

   @Override
   public InputStream getInputStream() throws Exception {
      if (!isConnectionOpen)
         throw new IllegalStateException("Connection was not established yet.");
      return ch.get(URLUtil.getFilePath(source));
   }

   @Override
   public long getSize() throws Exception {
      if (!isConnectionOpen)
         throw new IllegalStateException("Connection was not established yet.");
      try {

         @SuppressWarnings("unchecked")
         Vector<LsEntry> files = ch.ls(URLUtil.getFilePath(source));
         if (files.size() > 1)
            throw new IOException(String.format("%s:Ambiguous file", source));
         return files.get(0).getAttrs().getSize();
      } catch (SftpException ex) {
         if (ex.id == ChannelSftp.SSH_FX_NO_SUCH_FILE) {
            throw new IOException(String.format("%s:Not Found", source));
         }
         throw ex;
      }
   }

   @Override
   public void openConnection() throws Exception {
      if (isConnectionOpen)
         throw new IllegalStateException("Connection was established.");
      if (credInfo == null)
         throw new NullPointerException("credential info");
      if (source == null)
         throw new NullPointerException("source");
      
      isValidProtocol(source, "sftp");
      JSch sch = new JSch();
      try {
         Properties prop = new Properties();
         prop.put("StrictHostKeyChecking", "no");

         String host = URLUtil.getHost(source);
         session = sch.getSession(credInfo.getUsername(), host, DEFAULT_SFTP_PORT);
         session.setPassword(credInfo.getPassword());
         session.setConfig(prop);
         session.connect();

         ch = (ChannelSftp) session.openChannel("sftp");
         ch.connect();
         ch.ls(URLUtil.getFilePath(source));

      } catch (JSchException e) {
         isConnectionOpen = false;
         session.disconnect();
         if (e.getCause() != null) {
            throw (Exception) e.getCause();
         } else
            throw e;
      } catch (SftpException sftp) {
         if (sftp.id == ChannelSftp.SSH_FX_NO_SUCH_FILE) {
            throw new IOException(String.format("%s:Not Found", source));
         }
      }
      isConnectionOpen = true;
   }

   @Override
   public void closeConnection() throws Exception {
      if (!isConnectionOpen)
         throw new IllegalStateException("Connection was not established yet.");
      if (ch != null)
         ch.disconnect();
      if (session != null)
         session.disconnect();
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
