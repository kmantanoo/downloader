package model;

import java.io.InputStream;
import java.util.concurrent.Callable;

public class StreamReader implements Callable<Integer> {
   private InputStream inputStream;
   private byte[] buff;

   public StreamReader(int bufferSize, InputStream inputStream) {
      this.buff = new byte[bufferSize];
      this.inputStream = inputStream;
   }

   public byte[] getBuffer() {
      return buff;
   }

   @Override
   public Integer call() throws Exception {
      return this.inputStream.read(buff);
   }
}
