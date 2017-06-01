package utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class URLUtil {
   private static String SIMPLE_URL_PATTERN = "([a-z]+)://(.*)";
   private static Pattern COMPILED = Pattern.compile(SIMPLE_URL_PATTERN);

   public static String getProtocolFromURL(String url) throws NullPointerException {

      if (url == null)
         throw new NullPointerException("url");

      Matcher m = COMPILED.matcher(url);

      if (m.matches()) {
         return m.group(1);
      }

      return null;

   }

   public static String getFilePath(String url) throws NullPointerException {

      if (url == null)
         throw new NullPointerException("url");
      Matcher m = COMPILED.matcher(url);
      if (m.matches()) {

         String woProtocol = m.group(2);
         Pattern p = Pattern.compile("(?:[\\w\\.]+)/(.+)");
         m = p.matcher(woProtocol);

         if (m.matches()) {

            String filePath = m.group(1);
            return filePath;

         }

      }

      return null;
   }

   public static String getHost(String url) throws NullPointerException {

      if (url == null)
         throw new NullPointerException("url");

      Matcher m = COMPILED.matcher(url);
      if (m.matches()) {

         String woProtocol = m.group(2);
         Pattern p = Pattern.compile("([\\w\\.]+)/.*");
         m = p.matcher(woProtocol);

         if (m.matches()) {

            String domainName = m.group(1);
            return domainName;

         }

      }

      return null;
   }

   public static String getFileName(String url) throws NullPointerException {
      if (url == null)
         throw new NullPointerException("url");

      Matcher m = COMPILED.matcher(url);
      if (m.matches()) {

         String woProtocol = m.group(2);
         Pattern p = Pattern.compile("([\\w\\.]+)+(?:\\/\\w+)*\\/(.+)");
         m = p.matcher(woProtocol);

         if (m.matches()) {

            String fileName = m.group(2);
            return fileName;

         }

      }

      return null;
   }
}
