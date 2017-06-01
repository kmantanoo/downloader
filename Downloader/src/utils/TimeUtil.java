package utils;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class TimeUtil {
   public static final String TIME_STAMP_FORMAT = "yyyyMMddHHmmss";
   public static final String TIME_STAMP_DATE_FORMAT = "EEE, d MMM yyyy HH:mm:ss";
   public static final SimpleDateFormat numericDateTimeFormat = new SimpleDateFormat(TIME_STAMP_FORMAT);
   public static final SimpleDateFormat alphaNumericDateTimeFormat = new SimpleDateFormat(TIME_STAMP_DATE_FORMAT);

   public static String getTimeStamp() {
      Timestamp timeStamp = getTimeStampMillis();
      return numericDateTimeFormat.format(timeStamp);
   }

   public static String getTimeStampWithDate() {
      Timestamp timeStamp = getTimeStampMillis();
      return alphaNumericDateTimeFormat.format(timeStamp);
   }

   public static Timestamp getTimeStampMillis() {
      return new Timestamp(System.currentTimeMillis());
   }
}
