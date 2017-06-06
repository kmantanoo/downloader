package model.datasource;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;

import model.exception.InvalidProtocolException;

public class HTTPDataSource extends DataSource {
   private HttpURLConnection conn;
   private URL url;
   
   public HTTPDataSource() {
      isConnectionOpen = false;
   }

   @Override
   public InputStream getInputStream() throws IOException {
      if (! isConnectionOpen) throw new IllegalStateException("Connection was not established yet.");
      return conn.getInputStream();
   }

   @Override
   public long getSize() {
      if (! isConnectionOpen) throw new IllegalStateException("Connection was not established yet.");
      return conn.getContentLengthLong();
   }

   @Override
   public void openConnection() throws IOException, InvalidProtocolException, URISyntaxException {
      if (isConnectionOpen) throw new IllegalStateException("Connection was established.");
      
      if (source == null)
         throw new NullPointerException("source");
      
      url = new URL(source);
      isValidProtocol(source, "http");
      conn = (HttpURLConnection) url.openConnection();
      conn.setRequestProperty("User-Agent",
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36");

      if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
         throw new IOException(String.format("%s:%s", source, conn.getResponseMessage()));
      }
      isConnectionOpen = true;
   }

   @Override
   public void closeConnection() throws Exception {
      if (! isConnectionOpen) throw new IllegalStateException("Connection was not established yet.");
      if (conn == null) throw new NullPointerException("conn");
      conn.disconnect();
      conn = null;
      isConnectionOpen = false;
   }
   
   @Override
   public boolean isRequireCredential() {
      return false;
   }
}
