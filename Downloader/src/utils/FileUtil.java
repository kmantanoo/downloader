package utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.Random;

import org.apache.commons.io.FilenameUtils;

import model.CustomFilenameFilter;
import model.exception.IllegalFileTypeException;

public class FileUtil {
   public static String genFileName() {
      long randomNumber = new Random().nextLong();
      return "Java" + Long.toString(Math.abs(randomNumber)) + ".stream";
   }

   public static Path renameFile(File genFile, String newName) throws IOException, IllegalFileTypeException {
      if (genFile.isDirectory())
         throw new IllegalFileTypeException(String.format("Expect file but %s is a directory", genFile.getName()));

      Path dirPath = genFile.getParentFile().toPath();
      Path newPath = null;
      File directory = new File(dirPath.toUri());
      CustomFilenameFilter cfFilter = new CustomFilenameFilter(newName);
      String sequence = "";

      File[] filtered = directory.listFiles(cfFilter);
      String[] sorted = new String[filtered.length];
      int index = 0;
      for (File f : filtered) {
         sorted[index++] = FilenameUtils.removeExtension(f.getName());
      }
      if (sorted.length > 0) {
         int fileSequence = 0;
         if (sorted.length > 1) {
            Arrays.sort(sorted);
            String latestFileName = sorted[sorted.length - 1];
            fileSequence = Integer
                  .parseInt(latestFileName.substring(latestFileName.indexOf("(") + 1, latestFileName.indexOf(")"))) + 1;
         } else {
            fileSequence = 1;
         }
         sequence = String.format("(%d)", fileSequence);
      }

      newPath = Paths.get(dirPath.toString(), String.format("%s%s.%s", FilenameUtils.removeExtension(newName), sequence,
            FilenameUtils.getExtension(newName)));
      return Files.move(genFile.toPath(), newPath, StandardCopyOption.ATOMIC_MOVE);
   }

   public static Path toRandomFilePath(String destination) {
      String generatedFileName = genFileName();
      return Paths.get(destination, generatedFileName);
   }

   public static FileOutputStream getFileOutputStream(File outputFile) throws FileNotFoundException {
      if (outputFile == null)
         throw new NullPointerException("outputFile");

      File parent = outputFile.getParentFile();
      if (parent == null)
         return new FileOutputStream(outputFile);
      if (!outputFile.getParentFile().exists()) {
         outputFile.getParentFile().mkdirs();
      }
      return new FileOutputStream(outputFile);
   }

   public static String logToFile(String destinationFolder, String fileName, Throwable e) throws IOException {
      
      if (destinationFolder == null) throw new NullPointerException("Destination");
      if (fileName == null) throw new NullPointerException("File name");
      if (e == null) throw new NullPointerException("Causes");
      
      Path logFilePath = Paths.get(destinationFolder, fileName);
      FileOutputStream fos = new FileOutputStream(logFilePath.toString(), true);
      PrintStream ps = new PrintStream(fos);

      ps.println(TimeUtil.getTimeStampWithDate() + "\n");
      e.printStackTrace(ps);
      ps.println("\n");
      fos.close();

      ps.close();

      return logFilePath.toString();
   }
}
