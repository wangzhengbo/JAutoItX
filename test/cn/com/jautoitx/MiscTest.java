package cn.com.jautoitx;

import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Test;

public class MiscTest extends BaseTest {
	@Test
	public void isAdmin() {
		String userHome = System.getProperty("user.home");
		assertNotBlank(userHome);
		if (StringUtils.endsWithIgnoreCase(userHome, "\\Administrator")) {
			Assert.assertTrue(Misc.isAdmin());
		} else {
			Assert.assertFalse(Misc.isAdmin());
		}
	}

	@Test
	public void sleep() {
		for (int i = 1; i <= 3; i++) {
			long startTime = System.currentTimeMillis();
			Misc.sleep(i * 1000);
			long endTime = System.currentTimeMillis();
			Assert.assertTrue("Sleeped time is " + (endTime - startTime)
					+ " milliseconds.", (endTime - startTime) >= i * 1000);
		}
	}
}
