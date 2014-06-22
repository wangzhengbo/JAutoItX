package cn.com.jautoitx;

import org.junit.Assert;
import org.junit.Test;

public class Win32Test extends BaseTest {

	@Test
	public void getFileVersion() {
		Assert.assertNull(Win32.getFileVersion("lib"));
		Assert.assertNull(Win32.getFileVersion("test/iniDelete.ini"));

		Assert.assertEquals("3.3.10.2", Win32
				.getFileVersion("src/cn/com/jautoitx/lib/AutoItX3_x64.dll"));
		Assert.assertEquals("3.3.10.2",
				Win32.getFileVersion("src/cn/com/jautoitx/lib/AutoItX3.dll"));
	}
}
