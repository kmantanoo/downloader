package utils;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.regex.Pattern;

import org.apache.commons.validator.routines.UrlValidator;

public class URLUtil {
   private static final UrlValidator vldt = new UrlValidator(
         UrlValidator.ALLOW_ALL_SCHEMES | UrlValidator.ALLOW_LOCAL_URLS | UrlValidator.NO_FRAGMENTS);
   private static String SIMPLE_URL_PATTERN = "([a-z]+)://(.*)";
   private static Pattern COMPILED = Pattern.compile(SIMPLE_URL_PATTERN);

   public static boolean validURL(String url) throws MalformedURLException {
      try {
         return vldt.isValid(url);
      } catch (Exception e) {
         throw new MalformedURLException(url);
      }
   }

   public static String getProtocolFromURL(String url) throws MalformedURLException, URISyntaxException {

//      String processed = preprocessURL(url);
//      Matcher m = COMPILED.matcher(processed);
//
//      if (m.matches()) {
//         return m.group(1);
//      }
//
//      return null;
      if(validURL(url)){
         URI uri = new URI(url);
         return uri.getScheme();
      }
      return null;
   }

   public static String getFilePath(String url) throws MalformedURLException, URISyntaxException {

//      String processed = preprocessURL(url);
//      Matcher m = COMPILED.matcher(processed);
//      if (m.matches()) {
//
//         String woProtocol = m.group(2);
//         Pattern p = Pattern.compile("(?:[\\w\\.]+)/(.+)");
//         m = p.matcher(woProtocol);
//
//         if (m.matches()) {
//
//            String filePath = m.group(1);
//            return filePath;
//
//         }
//
//      }
//
//      return null;
      if(validURL(url)){
         URI uri = new URI(url);
         return uri.getPath().replaceFirst("/", "");
      }
      return null;
   }

   public static String getHost(String url) throws MalformedURLException, URISyntaxException {

//      String processed = preprocessURL(url);
//      Matcher m = COMPILED.matcher(processed);
//      if (m.matches()) {
//
//         String woProtocol = m.group(2);
//         Pattern p = Pattern.compile("([\\w\\.]+)/.*");
//         m = p.matcher(woProtocol);
//
//         if (m.matches()) {
//
//            String domainName = m.group(1);
//            return domainName;
//
//         }
//
//      }
//
//      return null;
      if(validURL(url)) {
         URI uri = new URI(url);
         return uri.getHost();
      }
      return null;
   }

   public static String getFileName(String url) throws MalformedURLException, URISyntaxException {

//      String processed = preprocessURL(url);
//      Matcher m = COMPILED.matcher(processed);
//      if (m.matches()) {
//
//         String woProtocol = m.group(2);
//         Pattern p = Pattern.compile("([\\w\\.]+)+(?:\\/\\w+)*\\/(.+)");
//         m = p.matcher(woProtocol);
//
//         if (m.matches()) {
//
//            String fileName = m.group(2);
//            return fileName;
//
//         }
//
//      }
//
//      return null;
      if (validURL(url)) {
         String filePath = getFilePath(url);
         int lastSlashIndex = filePath.lastIndexOf("/");
         return filePath.substring(lastSlashIndex+1);
      }
      return null;
   }

   public static String getFileDirectory(String url) throws MalformedURLException, URISyntaxException {

//      String processed = preprocessURL(url);
//      Matcher m = COMPILED.matcher(processed);
//      if (m.matches()) {
//
//         String woProtocol = m.group(2);
//         Pattern p = Pattern.compile("(?:[\\w\\.]+)((?:/\\w+)*)/(?:[^/]+)");
//         m = p.matcher(woProtocol);
//
//         if (m.matches()) {
//
//            String fileDirectory = m.group(1);
//            return "".equals(fileDirectory) ? "/" : fileDirectory;
//
//         }
//
//      }
//      return null;
      if (validURL(url)) {
         String filePath = getFilePath(url);
         if (filePath.contains("/")) {
            int lastSlashIndex = filePath.lastIndexOf("/");
            if (lastSlashIndex > -1) return filePath.substring(0, lastSlashIndex);
         } else {
            return "".equals(filePath) ? null : "";
         }
      }
      return null;
   }
}
