package test.model;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Arrays;

import org.junit.BeforeClass;
import org.junit.Test;

import model.StreamReader;

public class TestStreamReader {
   private static StringBuilder sb = new StringBuilder();
   
   @BeforeClass
   public static void setup() {
      sb.append("Test Stream Reader\n");
      sb.append("Second Line\n");
      sb.append("Third Line");
   }
   
   @Test
   public void testGetBuffer() throws Exception {
      int bufferSize = 8;
      InputStream inputStream = new ByteArrayInputStream(sb.toString().getBytes());
      StreamReader streamReader = new StreamReader(bufferSize, inputStream);
      assertArrayEquals(new byte[bufferSize], streamReader.getBuffer());
      
      assertEquals(bufferSize, streamReader.call().intValue());
      assertArrayEquals("Test Str".getBytes(), streamReader.getBuffer());
   }
   
   @Test
   public void testCall() throws Exception {
      int bufferSize = 16;
      InputStream inputStream = new ByteArrayInputStream(sb.toString().getBytes());
      StreamReader streamReader = new StreamReader(bufferSize, inputStream);
      
      assertArrayEquals(new byte[bufferSize], streamReader.getBuffer());
      
      assertEquals(bufferSize, streamReader.call().intValue());
      assertArrayEquals("Test Stream Read".getBytes(), streamReader.getBuffer());
      assertEquals(bufferSize, streamReader.call().intValue());
      assertArrayEquals("er\nSecond Line\nT".getBytes(), streamReader.getBuffer());
      assertEquals(9, streamReader.call().intValue());
      assertArrayEquals("hird Line".getBytes(), Arrays.copyOf(streamReader.getBuffer(), 9));
      assertEquals(-1, streamReader.call().intValue());
   }

}
