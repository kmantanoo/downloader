package utils;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class TimeUtil {
	private static String TIME_STAMP_FORMAT = "yyyyMMddHHmmss";
	private static SimpleDateFormat sdf = new SimpleDateFormat(TIME_STAMP_FORMAT);
	
	public static String getTimeStamp() {
		Timestamp timeStamp = new Timestamp(System.currentTimeMillis());
		return sdf.format(timeStamp);
	}
}
