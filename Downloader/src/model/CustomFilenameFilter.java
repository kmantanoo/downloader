package model;

import java.io.File;
import java.io.FilenameFilter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FilenameUtils;

public class CustomFilenameFilter implements FilenameFilter {
   private Pattern p;

   public CustomFilenameFilter(String expectedFileName) {
      if (expectedFileName == null) throw new NullPointerException("expectedFileName");
      String regex = Pattern.quote(FilenameUtils.removeExtension(expectedFileName)) + "(\\(\\d+\\))?\\."
            + FilenameUtils.getExtension(expectedFileName);
      p = Pattern.compile(regex);

   }

   @Override
   public boolean accept(File dir, String name) {
      Matcher m = p.matcher(name);
      if (m.matches()) {
         return true;
      }
      return false;
   }
}
