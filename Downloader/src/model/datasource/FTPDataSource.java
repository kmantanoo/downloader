package model.datasource;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.AccessDeniedException;

import javax.swing.JDialog;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

import utils.URLUtil;
import view.window.AppWindow;
import view.window.UserPasswordDialog;

public class FTPDataSource extends DataSource implements CredentialRequired {
	private FTPClient client;
	private FTPFile file;
	private static String DEFAULT_USER = "anonymous";
	private static String DEFAULT_PASSWORD = "test@test.com";
	private static int LOGIN_SUCCESS = 230;
	private String username;
	private String password;
	private AppWindow app;

	@Override
	public InputStream getInputStream() throws IOException {
		return client.retrieveFileStream(URLUtil.getFilePath(source));
	}

	@Override
	public long getSize(){
		return file.getSize();
	}

	@Override
	public void openConnection() throws IOException {
		
		if (client == null) client = new FTPClient();
		client.connect(URLUtil.getHost(source));
		client.login(DEFAULT_USER, DEFAULT_PASSWORD);
		if (client.getReplyCode() != LOGIN_SUCCESS) {
			getCredentialInfos();
			client.login(username, password);
		}
		
		try {
			file = client.listFiles(URLUtil.getFilePath(source))[0];
		} catch (IndexOutOfBoundsException e) {
			throw new AccessDeniedException(URLUtil.getFilePath(source));
		}
	}
	
	private void getCredentialInfos() {
		String title = String.format("Username/password for %s", source);
		UserPasswordDialog userPasswordDialog = new UserPasswordDialog(app, title, this);
		userPasswordDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		userPasswordDialog.setVisible(true);
	}
	
	@Override
	public void closeConnection() throws Exception {
		if (client != null) client.disconnect();
	}
	
	@Override
	public void setCredentialInfo(String username, String password) {
		this.username = username;
		this.password = password;
	}
}
