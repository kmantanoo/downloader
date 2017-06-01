package test.util;

import static org.junit.Assert.assertEquals;

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
      links.add("sftp://127.0.0.1/aa/bb/cc/dddd.txt");
      links.add("https:////");
      links.add("http:///test/test.txt");
      links.add("ftp://test.domain.com/");
      links.add("");
      links.add(null);
   }

   @Test
   public void testGetHost() {
      assertEquals("speedtest.tele2.net", URLUtil.getHost(links.get(0)));
      assertEquals("localhost", URLUtil.getHost(links.get(1)));
      assertEquals("127.0.0.1", URLUtil.getHost(links.get(2)));
      assertEquals(null, URLUtil.getHost(links.get(3)));
      assertEquals(null, URLUtil.getHost(links.get(4)));
      assertEquals("test.domain.com", URLUtil.getHost(links.get(5)));
      assertEquals(null, URLUtil.getHost(links.get(6)));
   }

   @Test
   public void testGetFilePath() {
      assertEquals("a/b/c/d.txt", URLUtil.getFilePath(links.get(0)));
      assertEquals("e/f/g/h.txt", URLUtil.getFilePath(links.get(1)));
      assertEquals("aa/bb/cc/dddd.txt", URLUtil.getFilePath(links.get(2)));
      assertEquals(null, URLUtil.getFilePath(links.get(3)));
      assertEquals(null, URLUtil.getFilePath(links.get(4)));
      assertEquals(null, URLUtil.getFilePath(links.get(5)));
      assertEquals(null, URLUtil.getFilePath(links.get(6)));
   }

   @Test
   public void testGetProtocol() {
      assertEquals("http", URLUtil.getProtocolFromURL(links.get(0)));
      assertEquals("ftp", URLUtil.getProtocolFromURL(links.get(1)));
      assertEquals("sftp", URLUtil.getProtocolFromURL(links.get(2)));
      assertEquals("https", URLUtil.getProtocolFromURL(links.get(3)));
      assertEquals("http", URLUtil.getProtocolFromURL(links.get(4)));
      assertEquals("ftp", URLUtil.getProtocolFromURL(links.get(5)));
      assertEquals(null, URLUtil.getProtocolFromURL(links.get(6)));
   }
   
   @Test
   public void testGetFileName() {
      assertEquals("d.txt", URLUtil.getFileName(links.get(0)));
      assertEquals("h.txt", URLUtil.getFileName(links.get(1)));
      assertEquals("dddd.txt", URLUtil.getFileName(links.get(2)));
      assertEquals(null, URLUtil.getFileName(links.get(3)));
      assertEquals(null, URLUtil.getFileName(links.get(4)));
      assertEquals(null, URLUtil.getFileName(links.get(5)));
      assertEquals(null, URLUtil.getFileName(links.get(6)));
   }

   @Test
   public void testNullInputOnGetHost() {
      expEx.expect(NullPointerException.class);
      URLUtil.getHost(links.get(7));
   }

   @Test
   public void testNullInputOnGetFilePath() {
      expEx.expect(NullPointerException.class);
      URLUtil.getFilePath(links.get(7));
   }

   @Test
   public void testNullInputOnGetProtocol() {
      expEx.expect(NullPointerException.class);
      URLUtil.getProtocolFromURL(links.get(7));
   }
   
   @Test
   public void testNullInputOnGetFileName() {
      expEx.expect(NullPointerException.class);
      URLUtil.getFileName(links.get(7));
   }

   @AfterClass
   public static void destroy() {
      links = null;
   }

}
