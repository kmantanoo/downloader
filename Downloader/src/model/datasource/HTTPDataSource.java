package model.datasource;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import model.exception.InvalidProtocolException;

public class HTTPDataSource extends DataSource {
   private HttpURLConnection conn;
   private URL url;

   @Override
   public InputStream getInputStream() throws IOException {
      return conn.getInputStream();
   }

   @Override
   public long getSize() {
      return conn.getContentLengthLong();
   }

   @Override
   public void openConnection() throws IOException, InvalidProtocolException {
      if (source == null)
         throw new NullPointerException(source);
      
      url = new URL(source);
      isValidProtocol(source, "http");
      conn = (HttpURLConnection) url.openConnection();
      conn.setRequestProperty("User-Agent",
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36");

      if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
         throw new IOException(String.format("%s:%s", source, conn.getResponseMessage()));
      }
   }

   @Override
   public void closeConnection() throws Exception {
      if (conn == null) throw new NullPointerException("conn");
      conn.disconnect();
      conn = null;
   }
}
