package cn.com.jautoitx;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.omg.CORBA.BooleanHolder;
import org.omg.CORBA.IntHolder;

import cn.com.jautoitx.Process.ProcPriority;
import cn.com.jautoitx.Process.RunLogonFlag;
import cn.com.jautoitx.Process.RunShowFlag;
import cn.com.jautoitx.Process.RunWaitResult;
import cn.com.jautoitx.Process.ShutdownCode;

public class ProcessTest extends BaseTest {
	private static final String USER_NAME;
	private static final String USER_PASSWORD;
	private static final String USER_ERROR_PASSWORD = "~AdminiPassword~";

	private static Sigar sigar = null;

	static {
		Properties props = new Properties();
		File propsFile = new File("test/account.conf");
		try {
			props.load(new FileInputStream(propsFile));
		} catch (Exception e) {
			Assert.fail(String.format("Unable to load file %s.",
					propsFile.getAbsolutePath()));
		}
		USER_NAME = props.getProperty("user");
		Assert.assertTrue(StringUtils.isNotBlank(USER_NAME));
		USER_PASSWORD = props.getProperty("password");
		Assert.assertNotNull(USER_ERROR_PASSWORD);
	}

	@BeforeClass
	public static void beforeClass() {
		sigar = new Sigar();
	}

	@Test
	public void close() {
		int pid = runNotepad();

		// close process by name
		Process.close(NOTEPAD_PROC_NAME);
		sleep(500);
		Assert.assertNull(Process.exists(NOTEPAD_PROC_NAME));

		pid = runNotepad();

		// close process by pid
		Process.close(pid);
		sleep(500);
		Assert.assertNull(Process.exists(NOTEPAD_PROC_NAME));
	}

	@Test
	public void exists() {
		Assert.assertNull(Process.exists("notepad.exe"));

		// run notepad
		int pid = Process.run(NOTEPAD_PROC_NAME);
		Assert.assertTrue(pid > 0);
		Assert.assertEquals(pid, Process.exists("notepad.exe").intValue());
		Assert.assertTrue(Process.exists(pid));

		// close notepad
		Process.close(pid);
		sleep(500);
		Assert.assertNull(Process.exists("notepad.exe"));
		Assert.assertFalse(Process.exists(pid));

		Assert.assertNull(Process.exists("System Idle Process"));
		Assert.assertFalse(Process.exists(0));
	}

	@Test
	public void getPriority() {
		// run notepad
		int pid = runNotepad();

		for (ProcPriority priority : ProcPriority.values()) {
			Assert.assertTrue(Process.setPriority(pid, priority));
			Assert.assertEquals(priority, Process.getPriority(pid));
		}

		// close notepad
		closeNotepad(pid);

		Assert.assertNull(Process.getPriority(pid));
	}

	@Test
	public void run() throws SigarException, IOException {
		Assert.assertFalse(Win.exists(NOTEPAD_TITLE));

		// run notepad in default mode
		int pid = Process.run(NOTEPAD_PROC_NAME);
		Assert.assertTrue(pid > 0);
		Assert.assertTrue(Win.wait(NOTEPAD_TITLE, 3));
		Assert.assertTrue(Win.exists(NOTEPAD_TITLE));
		Assert.assertTrue(Win.visible(NOTEPAD_TITLE));
		Assert.assertFalse(Win.minimized(NOTEPAD_TITLE));
		Assert.assertFalse(Win.maximized(NOTEPAD_TITLE));
		// close notepad
		Win.close(NOTEPAD_TITLE);
		Assert.assertFalse(Win.exists(NOTEPAD_TITLE));

		// run notepad in hide mode
		pid = Process.run(NOTEPAD_PROC_NAME, RunShowFlag.HIDE);
		Assert.assertTrue(pid > 0);
		Assert.assertTrue(Win.wait(NOTEPAD_TITLE, 3));
		Assert.assertTrue(Win.exists(NOTEPAD_TITLE));
		Assert.assertFalse(Win.visible(NOTEPAD_TITLE));
		Assert.assertFalse(Win.minimized(NOTEPAD_TITLE));
		Assert.assertFalse(Win.maximized(NOTEPAD_TITLE));
		// close notepad
		Win.close(NOTEPAD_TITLE);
		Assert.assertFalse(Win.exists(NOTEPAD_TITLE));

		// run notepad in minimize mode
		pid = Process.run(NOTEPAD_PROC_NAME, RunShowFlag.MINIMIZE);
		Assert.assertTrue(pid > 0);
		Assert.assertTrue(Win.wait(NOTEPAD_TITLE, 3));
		Assert.assertTrue(Win.exists(NOTEPAD_TITLE));
		Assert.assertTrue(Win.visible(NOTEPAD_TITLE));
		Assert.assertTrue(Win.minimized(NOTEPAD_TITLE));
		Assert.assertFalse(Win.maximized(NOTEPAD_TITLE));
		// close notepad
		Win.close(NOTEPAD_TITLE);
		Assert.assertFalse(Win.exists(NOTEPAD_TITLE));

		// run notepad in maximize mode
		pid = Process.run(NOTEPAD_PROC_NAME, "C:/", RunShowFlag.MAXIMIZE);
		Assert.assertTrue(pid > 0);
		Assert.assertTrue(Win.wait(NOTEPAD_TITLE, 3));
		Assert.assertTrue(Win.exists(NOTEPAD_TITLE));
		Assert.assertTrue(Win.visible(NOTEPAD_TITLE));
		Assert.assertFalse(Win.minimized(NOTEPAD_TITLE));
		Assert.assertTrue(Win.maximized(NOTEPAD_TITLE));
		Assert.assertEquals("C:", sigar.getProcExe(pid).getCwd());
		// close notepad
		Win.close(NOTEPAD_TITLE);
		Assert.assertFalse(Win.exists(NOTEPAD_TITLE));

		// run notepad in normal mode
		final String workingDir = (new File(".")).getCanonicalPath();
		pid = Process.run(NOTEPAD_PROC_NAME, workingDir, RunShowFlag.NORMAL);
		Assert.assertTrue(pid > 0);
		Assert.assertTrue(Win.wait(NOTEPAD_TITLE, 3));
		Assert.assertTrue(Win.exists(NOTEPAD_TITLE));
		Assert.assertTrue(Win.visible(NOTEPAD_TITLE));
		Assert.assertFalse(Win.minimized(NOTEPAD_TITLE));
		Assert.assertFalse(Win.maximized(NOTEPAD_TITLE));
		Assert.assertEquals(workingDir, sigar.getProcExe(pid).getCwd());
		// close notepad
		Win.close(NOTEPAD_TITLE);
		Assert.assertFalse(Win.exists(NOTEPAD_TITLE));

		// run notepad in normal mode
		Assert.assertNull(Process.run(NOTEPAD_PROC_NAME + "xxx", RunShowFlag.NORMAL));
	}

	@Test
	public void runAs() throws SigarException, IOException {
		final String domain = InetAddress.getLocalHost().getHostName();

		Assert.assertFalse(Win.exists(NOTEPAD_TITLE));

		Assert.assertNull(Process.runAs(USER_NAME, domain, USER_ERROR_PASSWORD,
				(Integer) null, NOTEPAD_PROC_NAME));

		// run notepad in default mode
		int pid = Process.runAs(USER_NAME, domain, USER_PASSWORD,
				(Integer) null, NOTEPAD_PROC_NAME);
		Assert.assertTrue(pid > 0);
		Assert.assertTrue(Win.wait(NOTEPAD_TITLE, 3));
		Assert.assertTrue(Win.exists(NOTEPAD_TITLE));
		Assert.assertTrue(Win.visible(NOTEPAD_TITLE));
		Assert.assertFalse(Win.minimized(NOTEPAD_TITLE));
		Assert.assertFalse(Win.maximized(NOTEPAD_TITLE));
		// close notepad
		Win.close(NOTEPAD_TITLE);
		Assert.assertFalse(Win.exists(NOTEPAD_TITLE));

		// run notepad in hide mode
		pid = Process.runAs(USER_NAME, domain, USER_PASSWORD,
				(RunLogonFlag) null, NOTEPAD_PROC_NAME, RunShowFlag.HIDE);
		Assert.assertTrue(pid > 0);
		Assert.assertTrue(Win.wait(NOTEPAD_TITLE, 3));
		Assert.assertTrue(Win.exists(NOTEPAD_TITLE));
		Assert.assertFalse(Win.visible(NOTEPAD_TITLE));
		Assert.assertFalse(Win.minimized(NOTEPAD_TITLE));
		Assert.assertFalse(Win.maximized(NOTEPAD_TITLE));
		// close notepad
		Win.close(NOTEPAD_TITLE);
		Assert.assertFalse(Win.exists(NOTEPAD_TITLE));

		// run notepad in minimize mode
		pid = Process.runAs(USER_NAME, domain, USER_PASSWORD,
				(RunLogonFlag) null, NOTEPAD_PROC_NAME, RunShowFlag.MINIMIZE);
		Assert.assertTrue(pid > 0);
		Assert.assertTrue(Win.wait(NOTEPAD_TITLE, 3));
		Assert.assertTrue(Win.exists(NOTEPAD_TITLE));
		Assert.assertTrue(Win.visible(NOTEPAD_TITLE));
		Assert.assertTrue(Win.minimized(NOTEPAD_TITLE));
		Assert.assertFalse(Win.maximized(NOTEPAD_TITLE));
		// close notepad
		Win.close(NOTEPAD_TITLE);
		Assert.assertFalse(Win.exists(NOTEPAD_TITLE));

		// run notepad in maximize mode
		pid = Process.runAs(USER_NAME, domain, USER_PASSWORD,
				(RunLogonFlag) null, NOTEPAD_PROC_NAME, "C:/", RunShowFlag.MAXIMIZE);
		Assert.assertTrue(pid > 0);
		Assert.assertTrue(Win.wait(NOTEPAD_TITLE, 3));
		Assert.assertTrue(Win.exists(NOTEPAD_TITLE));
		Assert.assertTrue(Win.visible(NOTEPAD_TITLE));
		Assert.assertFalse(Win.minimized(NOTEPAD_TITLE));
		Assert.assertTrue(Win.maximized(NOTEPAD_TITLE));
		Assert.assertEquals("C:", sigar.getProcExe(pid).getCwd());
		// close notepad
		Win.close(NOTEPAD_TITLE);
		Assert.assertFalse(Win.exists(NOTEPAD_TITLE));

		// run notepad in normal mode
		final String workingDir = (new File(".")).getCanonicalPath();
		pid = Process.runAs(USER_NAME, domain, USER_PASSWORD,
				(RunLogonFlag) null, NOTEPAD_PROC_NAME, workingDir, RunShowFlag.NORMAL);
		Assert.assertTrue(pid > 0);
		Assert.assertTrue(Win.wait(NOTEPAD_TITLE, 3));
		Assert.assertTrue(Win.exists(NOTEPAD_TITLE));
		Assert.assertTrue(Win.visible(NOTEPAD_TITLE));
		Assert.assertFalse(Win.minimized(NOTEPAD_TITLE));
		Assert.assertFalse(Win.maximized(NOTEPAD_TITLE));
		Assert.assertEquals(workingDir, sigar.getProcExe(pid).getCwd());
		// close notepad
		Win.close(NOTEPAD_TITLE);
		Assert.assertFalse(Win.exists(NOTEPAD_TITLE));

		// run notepad in normal mode
		Assert.assertNull(Process.runAs(USER_NAME, domain, USER_PASSWORD,
				(RunLogonFlag) null, NOTEPAD_PROC_NAME + "xxx", RunShowFlag.NORMAL));
	}

	@Test
	public void runAsWait() throws SigarException, IOException {
		final String domain = InetAddress.getLocalHost().getHostName();

		Assert.assertFalse(Win.exists(NOTEPAD_TITLE));

		// run notepad in default mode
		final IntHolder exitCodeHolder = new IntHolder(Integer.MIN_VALUE);
		Thread thread = new Thread(new Runnable() {
			public void run() {
				exitCodeHolder.value = Process.runAsWait(USER_NAME, domain,
						USER_PASSWORD, NOTEPAD_PROC_NAME).getExitCode();
			}
		});
		thread.start();
		Assert.assertTrue(Win.wait(NOTEPAD_TITLE, 3));
		Assert.assertTrue(Win.exists(NOTEPAD_TITLE));
		Assert.assertTrue(Win.visible(NOTEPAD_TITLE));
		Assert.assertFalse(Win.minimized(NOTEPAD_TITLE));
		Assert.assertFalse(Win.maximized(NOTEPAD_TITLE));
		Assert.assertTrue(thread.isAlive());
		// close notepad
		Win.close(NOTEPAD_TITLE);
		Assert.assertFalse(Win.exists(NOTEPAD_TITLE));
		sleep(500);
		Assert.assertFalse(thread.isAlive());
		Assert.assertEquals(0, exitCodeHolder.value);

		// run notepad in hide mode
		exitCodeHolder.value = Integer.MIN_VALUE;
		thread = new Thread(new Runnable() {
			public void run() {
				exitCodeHolder.value = Process.runAsWait(USER_NAME, domain,
						USER_PASSWORD, null, NOTEPAD_PROC_NAME, RunShowFlag.HIDE)
						.getExitCode();
			}
		});
		thread.start();
		Assert.assertTrue(Win.wait(NOTEPAD_TITLE, 3));
		Assert.assertTrue(Win.exists(NOTEPAD_TITLE));
		Assert.assertFalse(Win.visible(NOTEPAD_TITLE));
		Assert.assertFalse(Win.minimized(NOTEPAD_TITLE));
		Assert.assertFalse(Win.maximized(NOTEPAD_TITLE));
		Assert.assertTrue(thread.isAlive());
		// close notepad
		Win.close(NOTEPAD_TITLE);
		Assert.assertFalse(Win.exists(NOTEPAD_TITLE));
		sleep(500);
		Assert.assertFalse(thread.isAlive());
		Assert.assertEquals(0, exitCodeHolder.value);

		// run notepad in minimize mode
		exitCodeHolder.value = Integer.MIN_VALUE;
		thread = new Thread(new Runnable() {
			public void run() {
				exitCodeHolder.value = Process.runAsWait(USER_NAME, domain,
						USER_PASSWORD, null, NOTEPAD_PROC_NAME, RunShowFlag.MINIMIZE)
						.getExitCode();
			}
		});
		thread.start();
		Assert.assertTrue(Win.wait(NOTEPAD_TITLE, 3));
		Assert.assertTrue(Win.exists(NOTEPAD_TITLE));
		Assert.assertTrue(Win.visible(NOTEPAD_TITLE));
		Assert.assertTrue(Win.minimized(NOTEPAD_TITLE));
		Assert.assertFalse(Win.maximized(NOTEPAD_TITLE));
		Assert.assertTrue(thread.isAlive());
		// close notepad
		Win.close(NOTEPAD_TITLE);
		Assert.assertFalse(Win.exists(NOTEPAD_TITLE));
		sleep(500);
		Assert.assertFalse(thread.isAlive());
		Assert.assertEquals(0, exitCodeHolder.value);

		// run notepad in maximize mode
		exitCodeHolder.value = Integer.MIN_VALUE;
		thread = new Thread(new Runnable() {
			public void run() {
				exitCodeHolder.value = Process.runAsWait(USER_NAME, domain,
						USER_PASSWORD, null, NOTEPAD_PROC_NAME, "C:/",
						RunShowFlag.MAXIMIZE).getExitCode();
			}
		});
		thread.start();
		Assert.assertTrue(Win.wait(NOTEPAD_TITLE, 3));
		Assert.assertTrue(Win.exists(NOTEPAD_TITLE));
		Assert.assertTrue(Win.visible(NOTEPAD_TITLE));
		Assert.assertFalse(Win.minimized(NOTEPAD_TITLE));
		Assert.assertTrue(Win.maximized(NOTEPAD_TITLE));
		Assert.assertTrue(thread.isAlive());
		int pid = Win.getProcess(NOTEPAD_TITLE);
		Assert.assertTrue(pid > 0);
		Assert.assertEquals("C:", sigar.getProcExe(pid).getCwd());
		// close notepad
		Win.close(NOTEPAD_TITLE);
		Assert.assertFalse(Win.exists(NOTEPAD_TITLE));
		sleep(500);
		Assert.assertFalse(thread.isAlive());
		Assert.assertEquals(0, exitCodeHolder.value);

		// run notepad in normal mode
		final String workingDir = (new File(".")).getCanonicalPath();
		exitCodeHolder.value = Integer.MIN_VALUE;
		final BooleanHolder errorHolder = new BooleanHolder(true);
		thread = new Thread(new Runnable() {
			public void run() {
				RunWaitResult result = Process.runAsWait(USER_NAME, domain,
						USER_PASSWORD, null, NOTEPAD_PROC_NAME, workingDir,
						RunShowFlag.NORMAL);
				exitCodeHolder.value = result.getExitCode();
				errorHolder.value = result.hasError();
			}
		});
		thread.start();
		sleep(2000);
		Assert.assertTrue(Win.wait(NOTEPAD_TITLE, 3));
		Assert.assertTrue(Win.exists(NOTEPAD_TITLE));
		Assert.assertTrue(Win.visible(NOTEPAD_TITLE));
		Assert.assertFalse(Win.minimized(NOTEPAD_TITLE));
		Assert.assertFalse(Win.maximized(NOTEPAD_TITLE));
		Assert.assertTrue(thread.isAlive());
		pid = Win.getProcess(NOTEPAD_TITLE);
		Assert.assertTrue(pid > 0);
		Assert.assertEquals(workingDir, sigar.getProcExe(pid).getCwd());
		// close notepad
		Win.close(NOTEPAD_TITLE);
		Assert.assertFalse(Win.exists(NOTEPAD_TITLE));
		sleep(2000);
		Assert.assertFalse(thread.isAlive());
		Assert.assertEquals(0, exitCodeHolder.value);
		Assert.assertFalse(errorHolder.value);

		// run notepad in normal mode
		exitCodeHolder.value = Integer.MIN_VALUE;
		errorHolder.value = false;
		thread = new Thread(new Runnable() {
			public void run() {
				RunWaitResult result = Process.runAsWait(USER_NAME, domain,
						USER_PASSWORD, null, NOTEPAD_PROC_NAME + "xxx",
						RunShowFlag.NORMAL);
				exitCodeHolder.value = result.getExitCode();
				errorHolder.value = result.hasError();
			}
		});
		thread.start();
		sleep(2000);
		Assert.assertFalse(thread.isAlive());
		Assert.assertTrue(errorHolder.value);
	}

	@Test
	public void runWait() throws SigarException, IOException {
		Assert.assertFalse(Win.exists(NOTEPAD_TITLE));

		// run notepad in default mode
		final IntHolder exitCodeHolder = new IntHolder(Integer.MIN_VALUE);
		Thread thread = new Thread(new Runnable() {
			public void run() {
				exitCodeHolder.value = Process.runWait(NOTEPAD_PROC_NAME).getExitCode();
			}
		});
		thread.start();
		Assert.assertTrue(Win.wait(NOTEPAD_TITLE, 3));
		Assert.assertTrue(Win.exists(NOTEPAD_TITLE));
		Assert.assertTrue(Win.visible(NOTEPAD_TITLE));
		Assert.assertFalse(Win.minimized(NOTEPAD_TITLE));
		Assert.assertFalse(Win.maximized(NOTEPAD_TITLE));
		Assert.assertTrue(thread.isAlive());
		// close notepad
		Win.close(NOTEPAD_TITLE);
		Assert.assertFalse(Win.exists(NOTEPAD_TITLE));
		sleep(500);
		Assert.assertFalse(thread.isAlive());
		Assert.assertEquals(0, exitCodeHolder.value);

		// run notepad in hide mode
		exitCodeHolder.value = Integer.MIN_VALUE;
		thread = new Thread(new Runnable() {
			public void run() {
				exitCodeHolder.value = Process.runWait(NOTEPAD_PROC_NAME,
						RunShowFlag.HIDE).getExitCode();
			}
		});
		thread.start();
		Assert.assertTrue(Win.wait(NOTEPAD_TITLE, 3));
		Assert.assertTrue(Win.exists(NOTEPAD_TITLE));
		Assert.assertFalse(Win.visible(NOTEPAD_TITLE));
		Assert.assertFalse(Win.minimized(NOTEPAD_TITLE));
		Assert.assertFalse(Win.maximized(NOTEPAD_TITLE));
		Assert.assertTrue(thread.isAlive());
		// close notepad
		Win.close(NOTEPAD_TITLE);
		Assert.assertFalse(Win.exists(NOTEPAD_TITLE));
		sleep(500);
		Assert.assertFalse(thread.isAlive());
		Assert.assertEquals(0, exitCodeHolder.value);

		// run notepad in minimize mode
		exitCodeHolder.value = Integer.MIN_VALUE;
		thread = new Thread(new Runnable() {
			public void run() {
				exitCodeHolder.value = Process.runWait(NOTEPAD_PROC_NAME,
						RunShowFlag.MINIMIZE).getExitCode();
			}
		});
		thread.start();
		Assert.assertTrue(Win.wait(NOTEPAD_TITLE, 3));
		Assert.assertTrue(Win.exists(NOTEPAD_TITLE));
		Assert.assertTrue(Win.visible(NOTEPAD_TITLE));
		Assert.assertTrue(Win.minimized(NOTEPAD_TITLE));
		Assert.assertFalse(Win.maximized(NOTEPAD_TITLE));
		Assert.assertTrue(thread.isAlive());
		// close notepad
		Win.close(NOTEPAD_TITLE);
		Assert.assertFalse(Win.exists(NOTEPAD_TITLE));
		sleep(500);
		Assert.assertFalse(thread.isAlive());
		Assert.assertEquals(0, exitCodeHolder.value);

		// run notepad in maximize mode
		exitCodeHolder.value = Integer.MIN_VALUE;
		thread = new Thread(new Runnable() {
			public void run() {
				exitCodeHolder.value = Process.runWait(NOTEPAD_PROC_NAME, "C:/",
						RunShowFlag.MAXIMIZE).getExitCode();
			}
		});
		thread.start();
		Assert.assertTrue(Win.wait(NOTEPAD_TITLE, 3));
		Assert.assertTrue(Win.exists(NOTEPAD_TITLE));
		Assert.assertTrue(Win.visible(NOTEPAD_TITLE));
		Assert.assertFalse(Win.minimized(NOTEPAD_TITLE));
		Assert.assertTrue(Win.maximized(NOTEPAD_TITLE));
		Assert.assertTrue(thread.isAlive());
		int pid = Win.getProcess(NOTEPAD_TITLE);
		Assert.assertTrue(pid > 0);
		Assert.assertEquals("C:", sigar.getProcExe(pid).getCwd());
		// close notepad
		Win.close(NOTEPAD_TITLE);
		Assert.assertFalse(Win.exists(NOTEPAD_TITLE));
		sleep(500);
		Assert.assertFalse(thread.isAlive());
		Assert.assertEquals(0, exitCodeHolder.value);

		// run notepad in normal mode
		final String workingDir = (new File(".")).getCanonicalPath();
		exitCodeHolder.value = Integer.MIN_VALUE;
		final BooleanHolder errorHolder = new BooleanHolder(true);
		thread = new Thread(new Runnable() {
			public void run() {
				RunWaitResult result = Process.runWait(NOTEPAD_PROC_NAME, workingDir,
						RunShowFlag.NORMAL);
				exitCodeHolder.value = result.getExitCode();
				errorHolder.value = result.hasError();
			}
		});
		thread.start();
		sleep(2000);
		Assert.assertTrue(Win.wait(NOTEPAD_TITLE, 3));
		Assert.assertTrue(Win.exists(NOTEPAD_TITLE));
		Assert.assertTrue(Win.visible(NOTEPAD_TITLE));
		Assert.assertFalse(Win.minimized(NOTEPAD_TITLE));
		Assert.assertFalse(Win.maximized(NOTEPAD_TITLE));
		Assert.assertTrue(thread.isAlive());
		pid = Win.getProcess(NOTEPAD_TITLE);
		Assert.assertTrue(pid > 0);
		Assert.assertEquals(workingDir, sigar.getProcExe(pid).getCwd());
		// close notepad
		Win.close(NOTEPAD_TITLE);
		Assert.assertFalse(Win.exists(NOTEPAD_TITLE));
		sleep(2000);
		Assert.assertFalse(thread.isAlive());
		Assert.assertEquals(0, exitCodeHolder.value);
		Assert.assertFalse(errorHolder.value);

		// run notepad in normal mode
		exitCodeHolder.value = Integer.MIN_VALUE;
		errorHolder.value = false;
		thread = new Thread(new Runnable() {
			public void run() {
				RunWaitResult result = Process.runWait(NOTEPAD_PROC_NAME + "xxx",
						RunShowFlag.NORMAL);
				exitCodeHolder.value = result.getExitCode();
				errorHolder.value = result.hasError();
			}
		});
		thread.start();
		sleep(2000);
		Assert.assertFalse(thread.isAlive());
		Assert.assertTrue(errorHolder.value);
	}

	@Test
	public void setPriority() {
		// run notepad
		int pid = runNotepad();

		for (ProcPriority priority : ProcPriority.values()) {
			Assert.assertTrue(Process.setPriority(pid, priority));
			Assert.assertEquals(priority, Process.getPriority(pid));
		}

		// close notepad
		closeNotepad(pid);

		Assert.assertFalse(Process.setPriority(pid, ProcPriority.NORMAL));
	}

	@Ignore
	@Test
	public void shutdown() {
		Assert.assertTrue(Process.shutdown(ShutdownCode.LOGOFF));
	}

	@Test
	public void testWait() {
		Thread thread = new Thread(new Runnable() {
			public void run() {
				Process.wait(NOTEPAD_PROC_NAME);
			}
		});
		thread.start();
		sleep(2000);
		Assert.assertTrue(thread.isAlive());

		// run notepad
		int pid = runNotepad();
		Assert.assertFalse(thread.isAlive());

		// close notepad
		Process.close(pid);
		sleep(500);
		Assert.assertFalse(Process.exists(pid));

		final IntHolder timeHolder = new IntHolder();
		thread = new Thread(new Runnable() {
			public void run() {
				long start = System.currentTimeMillis();
				Process.wait(NOTEPAD_PROC_NAME, 2);
				long end = System.currentTimeMillis();
				timeHolder.value = (int) (end - start);
			}
		});
		thread.start();
		sleep(3000);
		Assert.assertFalse(thread.isAlive());
		Assert.assertTrue("Assert timeHolder.value >= 2000, but actual is "
				+ timeHolder.value + ".", timeHolder.value >= 2000);
		Assert.assertTrue("Assert timeHolder.value <= 3000, but actual is "
				+ timeHolder.value + ".", timeHolder.value <= 3000);
	}

	@Test
	public void waitClose() {
		// run notepad
		int pid = Process.run(NOTEPAD_PROC_NAME);
		Assert.assertTrue(pid > 0);
		sleep(500);
		Assert.assertTrue(Win.active(NOTEPAD_TITLE));

		final int notepadPid = pid;
		Thread thread = new Thread(new Runnable() {
			public void run() {
				Process.waitClose(notepadPid);
			}
		});
		thread.start();
		sleep(2000);
		Assert.assertTrue(thread.isAlive());
		Process.close(pid);
		sleep(500);
		Assert.assertFalse(thread.isAlive());

		// run notepad
		pid = Process.run(NOTEPAD_PROC_NAME);
		Assert.assertTrue(pid > 0);
		sleep(500);
		Assert.assertTrue(Win.active(NOTEPAD_TITLE));

		thread = new Thread(new Runnable() {
			public void run() {
				Process.waitClose(NOTEPAD_PROC_NAME);
			}
		});
		thread.start();
		sleep(2000);
		Assert.assertTrue(thread.isAlive());
		Process.close(pid);
		sleep(500);
		Assert.assertFalse(thread.isAlive());

		// run notepad
		pid = Process.run(NOTEPAD_PROC_NAME);
		Assert.assertTrue(pid > 0);
		sleep(500);
		Assert.assertTrue(Win.active(NOTEPAD_TITLE));
		long start = System.currentTimeMillis();
		Process.waitClose(pid, 2);
		long end = System.currentTimeMillis();
		Assert.assertTrue((end - start) >= 2000);
		Assert.assertTrue((end - start) <= 3000);
		Assert.assertTrue(Process.exists(pid));

		// close notepad
		Process.close(pid);
		sleep(500);
		Assert.assertFalse(Process.exists(pid));

		start = System.currentTimeMillis();
		Process.waitClose(pid, 2);
		end = System.currentTimeMillis();
		Assert.assertTrue((end - start) >= 0);
		Assert.assertTrue((end - start) <= 500);
	}
}
