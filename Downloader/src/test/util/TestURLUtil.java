package test.util;

import static org.junit.Assert.*;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.ArrayList;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import utils.URLUtil;

public class TestURLUtil {
   private static ArrayList<String> links;

   @Rule
   public ExpectedException expEx = ExpectedException.none();

   @BeforeClass
   public static void setup() {
      links = new ArrayList<String>();

      links.add("http://speedtest.tele2.net/a/b/c/d.txt");
      links.add("ftp://localhost/e/f/g/h.txt");
      links.add("sftp://127.0.0.1/dddd.txt");
      links.add("https:////");
      links.add("http:///test/test.txt");
      links.add("ftp://test.domain.com/");
      links.add("");
      links.add(null);
   }

   @Test
   public void testGetHost() throws MalformedURLException, URISyntaxException {
      assertEquals("speedtest.tele2.net", URLUtil.getHost(links.get(0)));
      assertEquals("localhost", URLUtil.getHost(links.get(1)));
      assertEquals("127.0.0.1", URLUtil.getHost(links.get(2)));
      assertNull(URLUtil.getHost(links.get(3)));
      assertNull(URLUtil.getHost(links.get(4)));
      assertEquals("test.domain.com", URLUtil.getHost(links.get(5)));
      assertNull(URLUtil.getHost(links.get(6)));
   }

   @Test
   public void testGetFilePath() throws MalformedURLException, URISyntaxException {
      assertEquals("a/b/c/d.txt", URLUtil.getFilePath(links.get(0)));
      assertEquals("e/f/g/h.txt", URLUtil.getFilePath(links.get(1)));
      assertEquals("dddd.txt", URLUtil.getFilePath(links.get(2)));
      assertNull(URLUtil.getFilePath(links.get(3)));
      assertNull(URLUtil.getFilePath(links.get(4)));
      assertEquals("",URLUtil.getFilePath(links.get(5)));
      assertNull(URLUtil.getFilePath(links.get(6)));
   }

   @Test
   public void testGetProtocol() throws MalformedURLException, URISyntaxException {
      assertEquals("http", URLUtil.getProtocolFromURL(links.get(0)));
      assertEquals("ftp", URLUtil.getProtocolFromURL(links.get(1)));
      assertEquals("sftp", URLUtil.getProtocolFromURL(links.get(2)));
      assertNull(URLUtil.getProtocolFromURL(links.get(3)));
      assertNull(URLUtil.getProtocolFromURL(links.get(4)));
      assertEquals("ftp", URLUtil.getProtocolFromURL(links.get(5)));
      assertNull(URLUtil.getProtocolFromURL(links.get(6)));
   }
   
   @Test
   public void testGetFileName() throws MalformedURLException, URISyntaxException {
      assertEquals("d.txt", URLUtil.getFileName(links.get(0)));
      assertEquals("h.txt", URLUtil.getFileName(links.get(1)));
      assertEquals("dddd.txt", URLUtil.getFileName(links.get(2)));
      assertNull(URLUtil.getFileName(links.get(3)));
      assertNull(URLUtil.getFileName(links.get(4)));
      assertEquals("", URLUtil.getFileName(links.get(5)));
      assertNull(URLUtil.getFileName(links.get(6)));
   }
   
   @Test
   public void testGetFileDirectory() throws MalformedURLException, URISyntaxException {
      assertEquals("a/b/c", URLUtil.getFileDirectory(links.get(0)));
      assertEquals("e/f/g", URLUtil.getFileDirectory(links.get(1)));
      assertEquals("", URLUtil.getFileDirectory(links.get(2)));
      assertNull(URLUtil.getFileDirectory(links.get(3)));
      assertNull(URLUtil.getFileDirectory(links.get(4)));
      assertNull(URLUtil.getFileDirectory(links.get(5)));
      assertNull(URLUtil.getFileDirectory(links.get(6)));
   }

   @Test
   public void testNullInputOnGetHost() throws MalformedURLException, URISyntaxException {
      assertNull(URLUtil.getHost(links.get(7)));
   }

   @Test
   public void testNullInputOnGetFilePath() throws MalformedURLException, URISyntaxException {
      assertNull(URLUtil.getFilePath(links.get(7)));
   }

   @Test
   public void testNullInputOnGetProtocol() throws MalformedURLException, URISyntaxException {
      assertNull(URLUtil.getProtocolFromURL(links.get(7)));
   }
   
   @Test
   public void testNullInputOnGetFileName() throws MalformedURLException, URISyntaxException {
      assertNull(URLUtil.getFileName(links.get(7)));
   }
   
   @Test
   public void testNullInputOnGetFileDirectory() throws MalformedURLException, URISyntaxException {
      assertNull(URLUtil.getFileDirectory(links.get(7)));
   }

   @AfterClass
   public static void destroy() {
      links = null;
   }

}
