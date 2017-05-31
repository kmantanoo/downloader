package model.datasource;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.ConnectException;
import java.util.Properties;
import java.util.Vector;

import javax.swing.JDialog;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.ChannelSftp.LsEntry;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

import utils.URLUtil;
import view.window.AppWindow;
import view.window.UserPasswordDialog;

public class SFTPDataSource extends DataSource implements CredentialRequired{
	private Session session;
	private ChannelSftp ch;
	private String filePath;
	private static int DEFAULT_SFTP_PORT = 22;
	private String username;
	private String password;
	private AppWindow app;

	@Override
	public InputStream getInputStream() throws Exception {
		return ch.get(URLUtil.getFilePath(source));
	}

	@Override
	public long getSize() throws Exception {
		try{
			if(filePath == null) filePath = URLUtil.getFilePath(source);
			
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
	public void openConnection() throws Exception{
		String host;
		Properties prop = new Properties();
		
		host = URLUtil.getHost(source);
		getCredentialInfos();

		JSch sch = new JSch();
		try {
			prop.put("StrictHostKeyChecking", "no");
			
			session = sch.getSession(username, host, DEFAULT_SFTP_PORT);
			session.setPassword(password);
			session.setConfig(prop);
			session.connect();
			
			if (! session.isConnected()) throw new ConnectException(String.format("Cannot connect to source %s", source));
			
			ch = (ChannelSftp) session.openChannel("sftp");
			ch.connect();
			
		} catch (JSchException e) {
			session.disconnect();
			if (e.getCause() != null) {
				throw (Exception) e.getCause();
			} else 
				throw e;
		} catch (ConnectException e) {
			throw e;
		}
	}
	
	private void getCredentialInfos() {
		String title = String.format("Username/password for %s", source);
		UserPasswordDialog userPasswordDialog = new UserPasswordDialog(app, title, this);
		userPasswordDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		userPasswordDialog.setVisible(true);
		
		while(userPasswordDialog.isShowing());
	}
	
	@Override
	public void closeConnection() throws Exception {
		if (ch != null) ch.disconnect();
		if (session != null) session.disconnect();
	}
	
	@Override
	public void setCredentialInfo(String username, String password) {
		this.username = username;
		this.password = password;
	}
}
