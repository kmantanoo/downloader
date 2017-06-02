package model.datasource;

import java.io.FileNotFoundException;
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

public class SFTPDataSource extends DataSource {
   private Session session;
   private ChannelSftp ch;
   private String filePath;
   private static int DEFAULT_SFTP_PORT = 22;

   @Override
   public InputStream getInputStream() throws Exception {
      return ch.get(URLUtil.getFilePath(source));
   }

   @Override
   public long getSize() throws Exception {
      try {
         if (filePath == null)
            filePath = URLUtil.getFilePath(source);

         @SuppressWarnings("rawtypes")
         Vector files = ch.ls(filePath);
         if (files.size() > 1)
            throw new Exception(String.format("Found more than 1 file for %s", filePath));
         return ((LsEntry) files.get(0)).getAttrs().getSize();
      } catch (SftpException ex) {
         throw new FileNotFoundException(String.format("No such file or directory => %s", filePath));
      }
   }

   @Override
   public void openConnection() throws Exception {
      String host;
      Properties prop = new Properties();

      host = URLUtil.getHost(source);
      CredentialInformation credInfo = getCredentialInfo();

      JSch sch = new JSch();
      try {
         prop.put("StrictHostKeyChecking", "no");

         session = sch.getSession(credInfo.getUsername(), host, DEFAULT_SFTP_PORT);
         session.setPassword(credInfo.getPassword());
         session.setConfig(prop);
         session.connect();

         ch = (ChannelSftp) session.openChannel("sftp");
         ch.connect();

      } catch (JSchException e) {
         session.disconnect();
         if (e.getCause() != null) {
            throw (Exception) e.getCause();
         } else
            throw e;
      }
   }

   @Override
   public void closeConnection() throws Exception {
      if (ch != null)
         ch.disconnect();
      if (session != null)
         session.disconnect();
   }

//   @Override
//   public void setCredentialInfo(CredentialInformation info) {
//      this.username = info.getUsername();
//      this.password = info.getPassword();
//   }
}
