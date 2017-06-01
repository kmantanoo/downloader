package test.util;

import static org.junit.Assert.*;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Test;

import utils.TimeUtil;

public class TestTimeUtil {

   @Test
   public void testGetTimeStamp() {
      String timeStamp = TimeUtil.getTimeStamp();
      Date d = new Date(System.currentTimeMillis());
      SimpleDateFormat sdf = TimeUtil.numericDateTimeFormat;
      assertEquals(sdf.format(d), timeStamp);
   }

   @Test
   public void testGetTimeStampWithDate() {
      String timeStamp = TimeUtil.getTimeStampWithDate();
      Date d = new Date(System.currentTimeMillis());
      SimpleDateFormat sdf = TimeUtil.alphaNumericDateTimeFormat;
      assertEquals(sdf.format(d), timeStamp);
   }
}
