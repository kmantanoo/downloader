package test.model.exception;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.BeforeClass;
import org.junit.Test;

import model.exception.InvalidURLException;

public class TestInvalidURLException {
   private static ArrayList<InvalidURLException> exs;

   @BeforeClass
   public static void setup() {
      exs = new ArrayList<>();

      exs.add(new InvalidURLException("http://speedtest.tele2.net/a/b/c/d.txt"));
      exs.add(new InvalidURLException("ftp://localhost/e/f/g/h.txt"));
      exs.add(new InvalidURLException("sftp://127.0.0.1/aa/bb/cc/dddd.txt"));
      exs.add(new InvalidURLException(""));
      exs.add(new InvalidURLException("http:///test/test.txt"));
      exs.add(new InvalidURLException("ftp://test.domain.com/"));
      exs.add(new InvalidURLException());
   }

   @Test
   public void test() {
      assertEquals("Invalid URL:\"http://speedtest.tele2.net/a/b/c/d.txt\"", exs.get(0).getMessage());
      assertEquals("Invalid URL:\"ftp://localhost/e/f/g/h.txt\"", exs.get(1).getMessage());
      assertEquals("Invalid URL:\"sftp://127.0.0.1/aa/bb/cc/dddd.txt\"", exs.get(2).getMessage());
      assertEquals("Invalid URL:\"\"", exs.get(3).getMessage());
      assertEquals("Invalid URL:\"http:///test/test.txt\"", exs.get(4).getMessage());
      assertEquals("Invalid URL:\"ftp://test.domain.com/\"", exs.get(5).getMessage());
      assertEquals(null, exs.get(6).getMessage());
   }

}
