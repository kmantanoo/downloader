package test.util;

import static org.junit.Assert.*;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Test;

import utils.TimeUtil;

public class TestTimeUtil {

	@Test
	public void test() {
		String timeStamp = TimeUtil.getTimeStamp();
		Date d = new Date(System.currentTimeMillis());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		assertEquals(sdf.format(d), timeStamp);
	}

}
