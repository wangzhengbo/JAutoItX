package cn.com.jautoitx;

import java.io.File;

import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Test;

import cn.com.jautoitx.DriveMap.DriveMapAddError;
import cn.com.jautoitx.DriveMap.DriveMapAddFlag;

public class DriveMapTest extends BaseTest {
	@Test
	public void add() {
		long currentTimeMillis = System.currentTimeMillis();
		File dir = new File("test/tmp" + currentTimeMillis);
		String netname = "add" + currentTimeMillis;
		String path = dir.getAbsolutePath();
		boolean netShareAdded = false;

		try {
			Assert.assertTrue(dir.mkdir());

			// add net share with read permission
			Assert.assertTrue(netShareAdd(null, netname, "add Test",
					NET_SHARE_PERMISSION_ACCESS_READ, path, null));
			netShareAdded = true;

			// add drive map
			String drive = DriveMap.add(
					String.format("\\\\%s\\%s", getHostAddress(), netname),
					DriveMapAddFlag.DEFAULT);
			Assert.assertTrue(StringUtils.isNotBlank(drive));
			Assert.assertTrue(drive.endsWith(":"));

			String drive2 = DriveMap.add(
					String.format("\\\\%s\\%s", getHostAddress(), netname),
					DriveMapAddFlag.DEFAULT);
			Assert.assertTrue(StringUtils.isNotBlank(drive2));
			Assert.assertTrue(drive2.endsWith(":"));
			Assert.assertNotEquals(drive, drive2);

			// delete drive map
			Assert.assertTrue(DriveMap.del(drive));
			Assert.assertTrue(DriveMap.del(drive2));

			// add drive map
			String drive3 = DriveMap.add(drive,
					String.format("\\\\%s\\%s", getHostAddress(), netname),
					DriveMapAddFlag.DEFAULT);
			Assert.assertEquals(drive3, drive);

			// The device is already assigned
			Assert.assertNull(DriveMap.add(drive,
					String.format("\\\\%s\\%s", getHostAddress(), netname),
					DriveMapAddFlag.DEFAULT));
			Assert.assertEquals(DriveMapAddError.DEVICE_ALREADY_ASSIGNED,
					DriveMap.getAddError());

			// delete drive map
			Assert.assertTrue(DriveMap.del(drive3));

			// Undefined / Other error
			Assert.assertNull(DriveMap.add(
					String.format("\\\\xxxx.com\\%s", netname),
					DriveMapAddFlag.DEFAULT));
			Assert.assertEquals(DriveMapAddError.UNDEFINED,
					DriveMap.getAddError());

			// Invalid device name
			Assert.assertNull(DriveMap.add("HelloDevice",
					String.format("\\\\%s\\%s", getHostAddress(), netname),
					DriveMapAddFlag.DEFAULT));
			Assert.assertEquals(DriveMapAddError.INVALID_DEVICE_NAME,
					DriveMap.getAddError());

			// delete net share
			Assert.assertTrue(netShareDel(null, netname));
			netShareAdded = false;

			// add net share with none permission and password 'test'
			Assert.assertTrue(netShareAdd(null, netname, "add Test",
					NET_SHARE_PERMISSION_ACCESS_NONE, path, "test"));
			netShareAdded = true;

			// delete net share
			Assert.assertTrue(netShareDel(null, netname));
			netShareAdded = false;
		} finally {
			if (dir.exists()) {
				if (netShareAdded) {
					// delete net share
					Assert.assertTrue(netShareDel(null, netname));
				}

				Assert.assertTrue(dir.delete());
			}
		}
	}

	@Test
	public void del() {
		long currentTimeMillis = System.currentTimeMillis();
		File dir = new File("test/tmp" + currentTimeMillis);
		String netname = "driveMapDel" + currentTimeMillis;
		String path = dir.getAbsolutePath();
		boolean netShareAdded = false;

		try {
			Assert.assertTrue(dir.mkdir());

			// add net share with read permission
			Assert.assertTrue(netShareAdd(null, netname, "driveMapDel Test",
					NET_SHARE_PERMISSION_ACCESS_READ, path, null));
			netShareAdded = true;

			// add drive map
			String drive = DriveMap.add(
					String.format("\\\\%s\\%s", getHostAddress(), netname),
					DriveMapAddFlag.DEFAULT);
			Assert.assertTrue(StringUtils.isNotBlank(drive));
			Assert.assertTrue(drive.endsWith(":"));

			String drive2 = DriveMap.add(
					String.format("\\\\%s\\%s", getHostAddress(), netname),
					DriveMapAddFlag.DEFAULT);
			Assert.assertTrue(StringUtils.isNotBlank(drive2));
			Assert.assertTrue(drive2.endsWith(":"));
			Assert.assertNotEquals(drive, drive2);

			String drive3 = DriveMap.add(null,
					String.format("\\\\%s\\%s", getHostAddress(), netname),
					DriveMapAddFlag.DEFAULT);
			Assert.assertEquals("", drive3);
			Assert.assertNull(DriveMap.getAddError());

			// delete drive map
			Assert.assertTrue(DriveMap.del(drive));
			Assert.assertTrue(DriveMap.del(drive2));

			// If a connection has no drive letter mapped you may use the
			// connection name to disconnect, e.g. \\server\share
			Assert.assertTrue(DriveMap.del(String.format("\\\\%s\\%s",
					getHostAddress(), netname)));

			// delete drive map
			Assert.assertFalse(DriveMap.del(drive));
			Assert.assertFalse(DriveMap.del(drive2));
			Assert.assertFalse(DriveMap.del(String.format("\\\\%s\\%s",
					getHostAddress(), netname)));

			// add drive map
			String drive4 = DriveMap.add(null,
					String.format("\\\\%s\\%s", getHostAddress(), netname),
					DriveMapAddFlag.DEFAULT);
			Assert.assertEquals("", drive4);
			Assert.assertNull(DriveMap.getAddError());

			String drive5 = DriveMap.add(null,
					String.format("\\\\%s\\%s", getHostAddress(), netname),
					DriveMapAddFlag.DEFAULT);
			Assert.assertEquals("", drive5);
			Assert.assertNull(DriveMap.getAddError());

			// If a connection has no drive letter mapped you may use the
			// connection name to disconnect, e.g. \\server\share
			Assert.assertTrue(DriveMap.del(String.format("\\\\%s\\%s",
					getHostAddress(), netname)));
			Assert.assertFalse(DriveMap.del(String.format("\\\\%s\\%s",
					getHostAddress(), netname)));
		} finally {
			if (dir.exists()) {
				if (netShareAdded) {
					// delete net share
					Assert.assertTrue(netShareDel(null, netname));
				}

				Assert.assertTrue(dir.delete());
			}
		}
	}

	@Test
	public void get() {
		long currentTimeMillis = System.currentTimeMillis();
		File dir = new File("test/tmp" + currentTimeMillis);
		String netname = "driveMapGet" + currentTimeMillis;
		String path = dir.getAbsolutePath();
		boolean netShareAdded = false;

		try {
			Assert.assertTrue(dir.mkdir());

			// add net share with read permission
			Assert.assertTrue(netShareAdd(null, netname, "driveMapGet Test",
					NET_SHARE_PERMISSION_ACCESS_READ, path, null));
			netShareAdded = true;

			// add drive map
			String drive = DriveMap.add(
					String.format("\\\\%s\\%s", getHostAddress(), netname),
					DriveMapAddFlag.DEFAULT);
			Assert.assertTrue(StringUtils.isNotBlank(drive));
			Assert.assertTrue(drive.endsWith(":"));

			String drive2 = DriveMap.add(
					String.format("\\\\%s\\%s", getHostAddress(), netname),
					DriveMapAddFlag.DEFAULT);
			Assert.assertTrue(StringUtils.isNotBlank(drive2));
			Assert.assertTrue(drive2.endsWith(":"));
			Assert.assertNotEquals(drive, drive2);

			// get drive map
			Assert.assertEquals(
					String.format("\\\\%s\\%s", getHostAddress(), netname),
					DriveMap.get(drive));
			Assert.assertEquals(
					String.format("\\\\%s\\%s", getHostAddress(), netname),
					DriveMap.get(drive2));

			// delete drive map
			Assert.assertTrue(DriveMap.del(drive));

			// get drive map
			Assert.assertNull(DriveMap.get(drive));
			Assert.assertTrue(AutoItX.hasError());
			Assert.assertEquals(
					String.format("\\\\%s\\%s", getHostAddress(), netname),
					DriveMap.get(drive2));

			// delete drive map
			Assert.assertTrue(DriveMap.del(drive2));
			Assert.assertNull(DriveMap.get(drive));
			Assert.assertTrue(AutoItX.hasError());
			Assert.assertNull(DriveMap.get(drive2));
			Assert.assertTrue(AutoItX.hasError());
		} finally {
			if (dir.exists()) {
				if (netShareAdded) {
					// delete net share
					Assert.assertTrue(netShareDel(null, netname));
				}

				Assert.assertTrue(dir.delete());
			}
		}
	}
}
