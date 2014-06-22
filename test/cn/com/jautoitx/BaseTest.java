package cn.com.jautoitx;

import java.awt.Frame;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.List;

import javax.swing.JFrame;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TestName;

import com.sun.jna.Native;
import com.sun.jna.Structure;
import com.sun.jna.WString;
import com.sun.jna.platform.win32.Netapi32;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef.HWND;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.win32.W32APIOptions;

public abstract class BaseTest {
	protected static final boolean isZhUserLanguage = "zh"
			.equalsIgnoreCase(System.getProperty("user.language"));

	protected static final String SAVE_AS_DIALOG_TITLE = isZhUserLanguage ? "另存为"
			: "Save As";

	/* Notepad */
	protected static final String NOTEPAD = "notepad.exe";
	protected static final String NOTEPAAD_CLASS_NAME = "Notepad";
	protected static final String NOTEPAAD_CLASS_LIST = "Edit\nmsctls_statusbar32\n";
	protected static final String NOTEPAD_TITLE = isZhUserLanguage ? "无标题 - 记事本"
			: "Untitled - Notepad";
	protected static final String NOTEPAD_TITLE_START = isZhUserLanguage ? "无标题"
			: "Untitled";
	protected static final String NOTEPAD_TITLE_ANY = isZhUserLanguage ? "题 - 记"
			: "titled - Note";
	protected static final String NOTEPAD_TITLE_END = isZhUserLanguage ? "记事本"
			: "Notepad";

	/* HashMyFiles */
	protected static final String HASH_MY_FILES = new File(
			"test/HashMyFiles.exe").getAbsolutePath();
	protected static final String HASH_MY_FILES_PROC_NAME = "HashMyFiles.exe";
	protected static final String HASH_MY_FILES_TITLE = "HashMyFiles";
	protected static final String HASH_MY_FILES_ADD_FILES_TITLE = "Select one or more  filenames to add";

	/* Windows task manager */
	protected static final String TASK_MANAGER = "taskmgr.exe";
	protected static final String TASK_MANAGER_TITLE = isZhUserLanguage ? "Windows 任务管理器"
			: "Windows Task Manager";
	protected static final String STATUS_BAR_TEXT_PROCESSES = isZhUserLanguage ? "进程数:"
			: "Processes:";
	protected static final String STATUS_BAR_TEXT_CPU_USAGE = isZhUserLanguage ? "CPU 使用"
			: "CPU Usage:";
	protected static final String STATUS_BAR_TEXT_COMMIT_CHARGE = isZhUserLanguage ? "提交更改:"
			: "Commit Charge:";
	protected static final String STATUS_BAR_TEXT_PHYSICAL_MEMORY = isZhUserLanguage ? "物理内存:"
			: "Physical Memory:";

	protected static final String MESSAGE_BOX_OK_BUTTON_TEXT = isZhUserLanguage ? "确定"
			: "OK";

	protected static final int NET_SHARE_PERMISSION_ACCESS_NONE = 0;
	protected static final int NET_SHARE_PERMISSION_ACCESS_READ = 1;
	
	@Rule
	public TestName testName = new TestName();

	/* System current time before run every test */
	protected long currentTimeMillis = 0;
	
	@Before
	public void setUp() {
		System.out.println(String.format("--------------------Begin test %s#%s",
				getClass().getName(), testName.getMethodName()));
		
		Integer pid = 0;

		// close notepad
		while ((pid = Process.exists(NOTEPAD)) != null) {
			Process.close(pid);
		}

		// close HashMyFiles
		while ((pid = Process.exists(HASH_MY_FILES_PROC_NAME)) != null) {
			Process.close(pid);
		}

		// close windows task manager
		while ((pid = Process.exists(TASK_MANAGER)) != null) {
			Process.close(pid);
		}

		sleep(200);
		currentTimeMillis = System.currentTimeMillis();
	}
	
	@After
	public void tearDown() {
		System.out.println(String.format(
				"--------------------End   test %s#%s%n",
				getClass().getName(), testName.getMethodName()));
	}

	protected void assertEquals(int expected, Integer actual) {
		Assert.assertNotNull(actual);
		Assert.assertEquals(expected, (int) actual);
	}

	protected void assertEquals(String expected, String actual) {
		Assert.assertEquals(expected, actual);
	}

	protected void assertEmpty(final String text) {
		Assert.assertTrue(StringUtils.isEmpty(text));
	}

	protected void assertBlank(final String text) {
		Assert.assertTrue(StringUtils.isBlank(text));
	}

	protected void assertNotEmpty(final String text) {
		Assert.assertTrue(StringUtils.isNotEmpty(text));
	}

	protected void assertNotBlank(final String text) {
		Assert.assertTrue(StringUtils.isNotBlank(text));
	}

	protected void assertFileContent(final String filePath, final String content) {
		assertNotBlank(filePath);
		try {
			Assert.assertEquals(content,
					FileUtils.readFileToString(new File(filePath)));
		} catch (IOException e) {
			e.printStackTrace();
			Assert.fail("Get content from file " + filePath + " failed.");
		}
	}

	protected void destroyFrame(Frame frame) {
		if (frame != null) {
			frame.setVisible(false);
			frame.dispose();
		}
	}

	protected void destroyFrame(JFrame frame) {
		if (frame != null) {
			frame.setVisible(false);
			frame.dispose();
		}
	}

	protected void destroyDefaultDisplay(String title) {
		final Display display = Display.getDefault();
		if (display != null) {
			display.syncExec(new Runnable() {
				public void run() {
					Shell[] shells = display.getShells();
					if (shells != null) {
						for (Shell shell : shells) {
							shell.setVisible(false);
							shell.close();
							shell.dispose();
						}
					}
					display.dispose();
				}
			});
			Assert.assertTrue(Win.waitClose(title));
		}
	}

	protected int runNotepad() {
		// run notepad
		Integer pid = Process.run(NOTEPAD);
		Assert.assertNotNull(pid);
		Assert.assertTrue(Win.waitActive(NOTEPAD_TITLE, 3));
		return pid;
	}

	protected void closeNotepad(int pid) {
		if (pid > 0) {
			// close notepad
			Process.close(pid);
			sleep(500);
			Assert.assertFalse(Process.exists(pid));
		}
	}

	protected void sleep(final long millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			e.printStackTrace();
			Assert.fail(String.format("Thread.sleep(%d) failed.", millis));
		}
	}

	protected String getTooltip(final String handle) {
		Assert.assertTrue(StringUtils.isNotBlank(handle));
		HWND hWnd = AutoItX.handleToHwnd(handle);
		// check className
		char[] lpClassName = new char[50];
		int classNameLength = User32.INSTANCE.GetClassName(hWnd, lpClassName,
				lpClassName.length);
		String className = (classNameLength > 0) ? new String(lpClassName, 0,
				classNameLength) : "";
		if (!"tooltips_class32".equals(className)) {
			return "";
		}

		char[] tooltip = new char[User32.INSTANCE.GetWindowTextLength(hWnd) + 1];
		int length = User32.INSTANCE.GetWindowText(hWnd, tooltip,
				tooltip.length);
		return new String(tooltip, 0, length);
	}

	protected String getHostAddress() {
		try {
			return InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			Assert.fail("getHostAddress failed: " + e.getMessage());
			return null;
		}
	}

	/**
	 * Shares a server resource.
	 * 
	 * @param servername
	 * @param netname
	 * @param remark
	 * @param permissions
	 * @param path
	 * @param passwd
	 * @return
	 */
	protected boolean netShareAdd(String servername, String netname,
			String remark, int permissions, String path, String passwd) {
		SHARE_INFO_2 buf = new SHARE_INFO_2();
		buf.shi2_netname = (netname == null) ? null : new WString(netname);
		// STYPE_DISKTREE
		buf.shi2_type = 0;
		buf.shi2_remark = (remark == null) ? null : new WString(remark);
		buf.shi2_permissions = permissions;
		buf.shi2_max_uses = 1;
		buf.shi2_current_uses = 0;
		buf.shi2_path = (path == null) ? null : new WString(path);
		buf.shi2_passwd = (passwd == null) ? null : new WString(passwd);

		IntByReference parm_err = new IntByReference();

		// If the function succeeds, the return value is NERR_Success
		int res = Netapi32Ext.INSTANCE
				.NetShareAdd(servername, 2, buf, parm_err);
		if (res == 0) {
			return true;
		}

		System.err.println("netname = " + netname + ", remark = " + remark
				+ ", path = " + path + ", passwd = " + passwd + ", res = "
				+ res + ", parm_err = " + parm_err.getValue());
		return false;
	}

	/**
	 * Deletes a share name from a server's list of shared resources,
	 * disconnecting all connections to the shared resource.
	 * 
	 * @param servername
	 * @param netname
	 * @return
	 */
	protected boolean netShareDel(String servername, String netname) {
		// If the function succeeds, the return value is NERR_Success
		int res = Netapi32Ext.INSTANCE.NetShareDel(servername, netname, 0);
		if (res == 0) {
			return true;
		}

		System.err.println("res = " + res);
		return false;
	}

	protected static interface Netapi32Ext extends Netapi32 {
		Netapi32Ext INSTANCE = (Netapi32Ext) Native.loadLibrary("Netapi32",
				Netapi32Ext.class, W32APIOptions.DEFAULT_OPTIONS);

		/**
		 * Shares a server resource.
		 * 
		 * @param servername
		 *            Pointer to a string that specifies the DNS or NetBIOS name
		 *            of the remote server on which the function is to execute.
		 *            If this parameter is NULL, the local computer is used.
		 * @param level
		 *            Specifies the information level of the data. This
		 *            parameter can be one of the following values.<br/>
		 *            2:<br/>
		 *            Specifies information about the shared resource, including
		 *            the name of the resource, type and permissions, and number
		 *            of connections. The buf parameter points to a SHARE_INFO_2
		 *            structure.
		 * 
		 *            502:<br/>
		 *            Specifies information about the shared resource, including
		 *            the name of the resource, type and permissions, number of
		 *            connections, and other pertinent information. The buf
		 *            parameter points to a SHARE_INFO_502 structure.
		 * 
		 *            503:<br/>
		 *            Specifies information about the shared resource, including
		 *            the name of the resource, type and permissions, number of
		 *            connections, and other pertinent information. The buf
		 *            parameter points to a SHARE_INFO_503 structure.
		 * @param buf
		 *            Pointer to the buffer that specifies the data. The format
		 *            of this data depends on the value of the level parameter.
		 *            For more information, see <a href=
		 *            "http://msdn.microsoft.com/en-us/library/aa370676(v=vs.85).aspx"
		 *            >Network Management Function Buffers</a>.
		 * @param parm_err
		 *            Pointer to a value that receives the index of the first
		 *            member of the share information structure that causes the
		 *            ERROR_INVALID_PARAMETER error. If this parameter is NULL,
		 *            the index is not returned on error. For more information,
		 *            see the NetShareSetInfo function.
		 * @return If the function succeeds, the return value is NERR_Success.
		 *         If the function fails, the return value can be one of the
		 *         following error codes.
		 */
		int NetShareAdd(String servername, int level, SHARE_INFO_2 buf,
				IntByReference parm_err);

		/**
		 * Deletes a share name from a server's list of shared resources,
		 * disconnecting all connections to the shared resource.
		 * 
		 * @param servername
		 *            Pointer to a string that specifies the DNS or NetBIOS name
		 *            of the remote server on which the function is to execute.
		 *            If this parameter is NULL, the local computer is used.
		 * @param netname
		 *            Pointer to a string that specifies the name of the share
		 *            to delete. This string is Unicode if _WIN32_WINNT or
		 *            FORCE_UNICODE is defined.
		 * @param reversed
		 *            Reserved, must be zero.
		 * @return If the function succeeds, the return value is NERR_Success.
		 *         If the function fails, the return value can be one of the
		 *         following error codes.
		 */
		int NetShareDel(String servername, String netname, int reversed);
	}

	/**
	 * Contains information about the shared resource, including name of the
	 * resource, type and permissions, and the number of current connections.
	 * For more information about controlling access to securable objects, see
	 * Access Control, Privileges, and Securable Objects.
	 * 
	 * @author zhengbo.wang
	 */
	protected static class SHARE_INFO_2 extends Structure {
		/*
		 * Pointer to a Unicode string specifying the share name of a resource.
		 * Calls to the NetShareSetInfo function ignore this member.
		 */
		public WString shi2_netname;

		/*
		 * A combination of values that specify the type of the shared resource.
		 * Calls to the NetShareSetInfo function ignore this member. One of the
		 * following values may be specified. You can isolate these values by
		 * using the STYPE_MASK value.
		 */
		public int shi2_type;

		/*
		 * Pointer to a Unicode string that contains an optional comment about
		 * the shared resource.
		 */
		public WString shi2_remark;

		/*
		 * Specifies a DWORD value that indicates the shared resource's
		 * permissions for servers running with share-level security. A server
		 * running user-level security ignores this member. This member can be
		 * one or more of the following values. Calls to the NetShareSetInfo
		 * function ignore this member. Note that Windows does not support
		 * share-level security.
		 */
		public int shi2_permissions;

		/*
		 * Specifies a DWORD value that indicates the maximum number of
		 * concurrent connections that the shared resource can accommodate. The
		 * number of connections is unlimited if the value specified in this
		 * member is -1.
		 */
		public int shi2_max_uses;

		/*
		 * Specifies a DWORD value that indicates the number of current
		 * connections to the resource. Calls to the NetShareSetInfo function
		 * ignore this member.
		 */
		public int shi2_current_uses;

		/*
		 * Pointer to a Unicode string specifying the local path for the shared
		 * resource. For disks, shi2_path is the path being shared. For print
		 * queues, shi2_path is the name of the print queue being shared. Calls
		 * to the NetShareSetInfo function ignore this member.
		 */
		public WString shi2_path;

		/*
		 * Pointer to a Unicode string that specifies the share's password when
		 * the server is running with share-level security. If the server is
		 * running with user-level security, this member is ignored. The
		 * shi2_passwd member can be no longer than SHPWLEN+1 bytes (including a
		 * terminating null character). Calls to the NetShareSetInfo function
		 * ignore this member. Note that Windows does not support share-level
		 * security.
		 */
		public WString shi2_passwd;

		@SuppressWarnings("rawtypes")
		@Override
		protected List getFieldOrder() {
			return Arrays.asList("shi2_netname", "shi2_type", "shi2_remark",
					"shi2_permissions", "shi2_max_uses", "shi2_current_uses",
					"shi2_path", "shi2_passwd");
		}
	}

	protected static class ObjectHolder {
		public Object value = null;

		public ObjectHolder() {
			// empty
		}

		public ObjectHolder(Object value) {
			this.value = value;
		}
	}
}
