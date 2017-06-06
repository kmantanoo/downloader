package test.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import model.exception.IllegalFileTypeException;
import utils.FileUtil;

public class TestFileUtil {
   private static File testDir;
   private File f;
   private Pattern p = Pattern.compile("Java([\\d]+)\\.stream");
   
   @BeforeClass
   public static void setup(){
      testDir = Paths.get(System.getProperty("user.home"), "test").toFile();
      testDir.mkdir();
   }
   
   @AfterClass
   public static void cleanup() {
      File[] files = testDir.listFiles();
      if (files != null) {
         for (File f : files) {
            f.delete();
         }
      }
      testDir.delete();
   }
   
   @Rule
   public ExpectedException exp = ExpectedException.none();
   
   @After
   public void removeFile(){
      if (f != null) deleteFileAndDir(f);
   }
   
   public void deleteFileAndDir(File file) {
      if (file.exists()) {
         File parent = file.getParentFile();
         file.delete();
         if (parent != null) {
            deleteFileAndDir(parent);
         }
      }
  }

   @Test
   public void testGenFileName() {
      String generated;
      Matcher m;
      
      try {
         generated = FileUtil.genFileName();
         m = p.matcher(generated);
         if (m.matches()) {
            Long.valueOf(m.group(1));
         } else
            fail();
      } catch(NumberFormatException e) {
         fail();
      }
   }

   @Test
   public void testGetFileOutputStream() throws IOException {
      f = new File("test.txt");
      Object obj = FileUtil.getFileOutputStream(f);
      assertTrue(obj instanceof FileOutputStream);
      ((FileOutputStream) obj).close();
   }
   
   @Test
   public void testGetFileOutputStreamWithNotExistPath() throws IOException {
      f = new File("test/test/test.txt");
      assertFalse(f.getParentFile().exists());
      Object obj = FileUtil.getFileOutputStream(f);
      assertTrue(obj instanceof FileOutputStream);
      assertTrue(f.getParentFile().exists());
      ((FileOutputStream) obj).close();
   }
   
   @Test
   public void testGetFileOutputStreamWithNullFile() throws FileNotFoundException {
      exp.expect(NullPointerException.class);
      FileUtil.getFileOutputStream(null);
   }
   
   @Test
   public void testToRandomFilePath() {
      String dst = "C:\\TEMP";
      Path randomFilePath = FileUtil.toRandomFilePath(dst);
      assertEquals(dst, randomFilePath.toFile().getParentFile().getAbsolutePath());
      Matcher m = p.matcher(randomFilePath.toFile().getName());
      assertTrue(m.matches());
      try {
         Long.valueOf(m.group(1));
      } catch (NumberFormatException ex) {
         fail();
      }
   }
   
   @Test
   public void testRenameFileWithNullPath() throws IOException, IllegalFileTypeException {
      exp.expect(NullPointerException.class);
      FileUtil.renameFile(null, "expectedFileName");
   }
   
   @Test
   public void testRenameFileWithNullFileName() throws IOException, IllegalFileTypeException {
      exp.expect(NullPointerException.class);
      Path file = Paths.get(testDir.getAbsolutePath(), "test.txt");
      FileUtil.renameFile(file.toFile(), null);
   }
   
   @Test
   public void testRenameFileWithInvalidFile() throws IOException, IllegalFileTypeException {
      exp.expect(IllegalFileTypeException.class);
      FileUtil.renameFile(testDir, "test.txt");
   }
   
   @Test
   public void testRenameFileWithInUseFile() throws IOException, IllegalFileTypeException {
      File file = Paths.get(testDir.getAbsolutePath(), "test.txt").toFile();
      FileOutputStream fos = null; 
      try {
         fos = new FileOutputStream(file);
         FileUtil.renameFile(file, "test2.txt");
         fail();
      } catch(Exception e) {
         if (!(e instanceof IOException)) fail();
      } finally {
         fos.close();
         file.delete();
      }
   }
   
   @Test
   public void testRenameFileWithNotExistExpectedFile() throws IOException, IllegalFileTypeException {
      Path savedFile = null;
      Path randomFilePath = FileUtil.toRandomFilePath(testDir.getAbsolutePath());
      new FileOutputStream(randomFilePath.toFile()).close();
      
      assertTrue(randomFilePath.toFile().exists());
      savedFile = FileUtil.renameFile(randomFilePath.toFile(), "test.txt");
      assertFalse(randomFilePath.toFile().exists());
      assertTrue(savedFile.toFile().exists());
      savedFile.toFile().delete();
   }
   
   @Test
   public void testRenameFileWithExistOnlyOneExpectedFile() throws IOException, IllegalFileTypeException {
      Path existFile = Paths.get(testDir.getAbsolutePath(), "exist.txt");
      new FileOutputStream(existFile.toFile()).close();
      
      Path savedFile = null;
      Path randomFilePath = FileUtil.toRandomFilePath(testDir.getAbsolutePath());
      new FileOutputStream(randomFilePath.toFile()).close();
      
      assertTrue(randomFilePath.toFile().exists());
      savedFile = FileUtil.renameFile(randomFilePath.toFile(), existFile.toFile().getName());
      assertFalse(randomFilePath.toFile().exists());
      assertTrue(savedFile.toFile().exists());
      assertEquals("exist(1).txt", savedFile.toFile().getName());
      savedFile.toFile().delete();
      existFile.toFile().delete();
   }
   
   @Test
   public void testRenameFileWithExistOnlyOneExpectedFile2() throws IOException, IllegalFileTypeException {
      Path existFile = Paths.get(testDir.getAbsolutePath(), "exist(4).txt");
      new FileOutputStream(existFile.toFile()).close();
      
      Path savedFile = null;
      Path randomFilePath = FileUtil.toRandomFilePath(testDir.getAbsolutePath());
      new FileOutputStream(randomFilePath.toFile()).close();
      
      assertTrue(randomFilePath.toFile().exists());
      savedFile = FileUtil.renameFile(randomFilePath.toFile(), existFile.toFile().getName());
      assertFalse(randomFilePath.toFile().exists());
      assertTrue(savedFile.toFile().exists());
      assertEquals("exist(4)(1).txt", savedFile.toFile().getName());
      savedFile.toFile().delete();
      existFile.toFile().delete();
   }
   
   @Test
   public void testRenameFileWithExistSequenceExpectedFile() throws IOException, IllegalFileTypeException {
      List<Path> existFilePaths = new ArrayList<Path>();
      Path existFile = null;
      String existFileBaseName = "exist.txt";
      existFile = Paths.get(testDir.getAbsolutePath(), existFileBaseName);
      existFilePaths.add(existFile);
      new FileOutputStream(existFile.toFile()).close();
      
      for (int i = 1; i <= 4; i++) {
         existFile = Paths.get(testDir.getAbsolutePath(), String.format("exist(%d).txt", i));
         existFilePaths.add(existFile);
         new FileOutputStream(existFile.toFile()).close();
      }
      
      Path savedFile = null;
      Path randomFilePath = FileUtil.toRandomFilePath(testDir.getAbsolutePath());
      new FileOutputStream(randomFilePath.toFile()).close();
      
      assertTrue(randomFilePath.toFile().exists());
      savedFile = FileUtil.renameFile(randomFilePath.toFile(), existFileBaseName);
      assertFalse(randomFilePath.toFile().exists());
      assertTrue(savedFile.toFile().exists());
      assertEquals("exist(5).txt", savedFile.toFile().getName());
      savedFile.toFile().delete();
      for (Path p : existFilePaths) {
         p.toFile().delete();
      }
   }
   
   @Test
   public void testLogToFile() throws IOException {
      String logFileName = "log_error.log";
      Exception dummy = new Exception("test");
      FileUtil.logToFile(testDir.getAbsolutePath(), logFileName, dummy);
      File logFile = Paths.get(testDir.getAbsolutePath(), logFileName).toFile();
      
      assertTrue(logFile.exists());
      assertTrue(logFile.length() > 0);
      logFile.delete();
   }
   
   @Test
   public void testLogToFileWithNullDestination() throws IOException {
      exp.expect(NullPointerException.class);
      FileUtil.logToFile(null, "log_error.log", new Exception());
   }
   
   @Test
   public void testLogToFileWithNullFileName() throws IOException {
      exp.expect(NullPointerException.class);
      FileUtil.logToFile(testDir.getAbsolutePath(), null, new Exception());
   }
   
   @Test
   public void testLogToFileWithNullCause() throws IOException {
      exp.expect(NullPointerException.class);
      FileUtil.logToFile(testDir.getAbsolutePath(), "log_error.log", null);
   }
}
