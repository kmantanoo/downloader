package model.datasource;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

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
   public void openConnection() throws IOException, MalformedURLException {
      url = new URL(source);
      if (url == null)
         throw new MalformedURLException(source);
      conn = (HttpURLConnection) url.openConnection();
      conn.setRequestProperty("User-Agent",
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36");

      InputStream stream = conn.getErrorStream();
      if (stream != null) {
         throw new IOException(String.format("Error occured when try to connect to %s", source));
      }
   }

   @Override
   public void closeConnection() throws Exception {
      if (conn != null)
         conn.disconnect();
   }
}
