package cn.com.jautoitx;

import java.awt.event.KeyEvent;
import java.io.File;
import java.nio.CharBuffer;

import org.apache.commons.lang3.StringUtils;

import com.sun.jna.Memory;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.platform.win32.GDI32;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.VerRsrc.VS_FIXEDFILEINFO;
import com.sun.jna.platform.win32.Version;
import com.sun.jna.platform.win32.WinDef.HWND;
import com.sun.jna.platform.win32.WinNT.HANDLE;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.PointerByReference;
import com.sun.jna.win32.W32APIOptions;

public class Win32 {
	public static final GDI32Ext gdi32 = GDI32Ext.INSTANCE;
	public static final User32Ext user32 = User32Ext.INSTANCE;
	public static final Version version = Version.INSTANCE;
	public static final int INVALID_CONTROL_ID = -1;

	private static final int WM_GETTEXT = 0x000D;
	private static final int WM_GETTEXTLENGTH = 0x000E;

	private Win32() {
		// Do nothing
	}

	public static String getClassName(final String title, final String control) {
		return getClassName(title, null, control);
	}

	public static String getClassName(final String title, final String text,
			final String control) {
		String className = null;

		if (StringUtils.isNotBlank(control)) {
			HWND hWnd = Control.getHandle_(title, text, control);
			if (hWnd != null) {
				className = getClassName(hWnd);
			}
		}

		return className;
	}

	public static String getClassName(final String handle) {
		return getClassName(AutoItX.handleToHwnd(handle));
	}

	public static String getClassName(final HWND hWnd) {
		String className = null;

		if (hWnd != null) {
			char[] lpClassName = new char[256];
			int classNameLength = user32.GetClassName(hWnd, lpClassName,
					lpClassName.length);
			if (classNameLength >= 0) {
				className = new String(lpClassName, 0, classNameLength);
			}
		}

		return className;
	}

	/**
	 * Retrieves the identifier of the specified control.
	 * 
	 * @param controlHwnd
	 *            A handle to the control.
	 * @return If the function succeeds, the return value is the identifier of
	 *         the control. If the function fails, the return value is -1. An
	 *         invalid value for the hwndCtl parameter, for example, will cause
	 *         the function to fail.
	 */
	public static int getControlId(HWND controlHwnd) {
		int controlId = 0;
		if (controlHwnd != null) {
			controlId = user32.GetDlgCtrlID(controlHwnd);
		}
		if (controlId <= 0) {
			controlId = INVALID_CONTROL_ID;
		}
		return controlId;
	}

	public static String getControlText(HWND hCtrl) {
		String text = null;
		if (isHWnd(hCtrl)) {
			int textLength = user32.SendMessage(hCtrl, WM_GETTEXTLENGTH, 0, 0);
			if (textLength == 0) {
				text = "";
			} else {
				char[] lpText = new char[textLength + 1];
				if (textLength == user32.SendMessage(hCtrl, WM_GETTEXT,
						lpText.length, lpText)) {
					text = new String(lpText, 0, textLength);
				}
			}
		}
		return text;
	}

	/**
	 * Retrieves version information for the specified file.
	 * 
	 * @param filename
	 *            The name of the file to get version information.
	 * @return Return version information for the specified file if success,
	 *         return null if failed.
	 * @see http://msdn.microsoft.com/en-us/library/ms647005(v=vs.85).aspx
	 * @see http://msdn.microsoft.com/en-us/library/ms647003(v=vs.85).aspx
	 * @see http://msdn.microsoft.com/en-us/library/ms647464(v=vs.85).aspx
	 * @see http://stackoverflow.com/questions/6918022/get-version-info-for-exe
	 */
	public static String getFileVersion(String filename) {
		return getFileVersion((filename == null) ? null : new File(filename));
	}

	/**
	 * Retrieves version information for the specified file.
	 * 
	 * @param file
	 *            The file to get version information.
	 * @return Return version information for the specified file if success,
	 *         return null if failed.
	 * @see http://msdn.microsoft.com/en-us/library/ms647005(v=vs.85).aspx
	 * @see http://msdn.microsoft.com/en-us/library/ms647003(v=vs.85).aspx
	 * @see http://msdn.microsoft.com/en-us/library/ms647464(v=vs.85).aspx
	 * @see http://stackoverflow.com/questions/6918022/get-version-info-for-exe
	 */
	public static String getFileVersion(File file) {
		String fileVersion = null;
		if ((file != null) && file.exists()) {
			String filePath = file.getAbsolutePath();

			// A pointer to a variable that the function sets to zero
			IntByReference dwHandle = new IntByReference(0);

			// Determines whether the operating system can retrieve version
			// information for a specified file. If version information is
			// available, GetFileVersionInfoSize returns the size, in bytes, of
			// that information. If the function fails, the return value is
			// zero.
			int versionLength = version.GetFileVersionInfoSize(filePath,
					dwHandle);
			if (versionLength > 0) {
				// Pointer to a buffer that receives the file-version
				// information
				Pointer lpData = new Memory(versionLength);

				// Retrieves version information for the specified file
				if (version.GetFileVersionInfo(filePath, 0, versionLength,
						lpData)) {
					PointerByReference lplpBuffer = new PointerByReference();

					if (version.VerQueryValue(lpData, "\\", lplpBuffer,
							new IntByReference())) {
						VS_FIXEDFILEINFO lplpBufStructure = new VS_FIXEDFILEINFO(
								lplpBuffer.getValue());
						lplpBufStructure.read();

						int v1 = (lplpBufStructure.dwFileVersionMS).intValue() >> 16;
						int v2 = (lplpBufStructure.dwFileVersionMS).intValue() & 0xffff;
						int v3 = (lplpBufStructure.dwFileVersionLS).intValue() >> 16;
						int v4 = (lplpBufStructure.dwFileVersionLS).intValue() & 0xffff;
						fileVersion = String.format("%d.%d.%d.%d", v1, v2, v3,
								v4);
					}
				}
			}
		}
		return fileVersion;
	}

	public static String getWindowText(HWND hWnd) {
		String text = null;
		if (isHWnd(hWnd)) {
			int textLength = user32.GetWindowTextLength(hWnd);
			if (textLength == 0) {
				text = "";
			} else {
				char[] lpText = new char[textLength + 1];
				user32.GetWindowText(hWnd, lpText, lpText.length);
				text = new String(lpText, 0, textLength);
			}
		}
		return text;
	}

	/**
	 * Check whether the capslock is on or not.
	 * 
	 * @return Return true if the capslock is on, otherwise return false.
	 */
	public static boolean isCapslockOn() {
		return (user32.GetKeyState(KeyEvent.VK_CAPS_LOCK) & 0xffff) != 0;
	}

	public static boolean isComboBox(final String title, final String control) {
		return isComboBox(title, null, control);
	}

	public static boolean isComboBox(final String title, final String text,
			final String control) {
		return "ComboBox".equals(getClassName(title, text, control));
	}

	public static boolean isComboBox(final HWND hWnd) {
		return "ComboBox".equals(getClassName(hWnd));
	}

	public static boolean isClassName(final HWND hWnd, String className) {
		return ((className != null) && className
				.equalsIgnoreCase(getClassName(hWnd)));
	}

	/**
	 * Checks if the handle is a valid window handle.
	 * 
	 * @param hWnd
	 *            Handle to the window.
	 * @return Returns true if the handle is a valid window handle, otherwise
	 *         returns false.
	 */
	public static boolean isHWnd(HWND hWnd) {
		return (hWnd != null) && Win.exists(TitleBuilder.byHandle(hWnd));
	}

	public static boolean isListBox(final String title, final String control) {
		return isListBox(title, null, control);
	}

	public static boolean isListBox(final String title, final String text,
			final String control) {
		return "ListBox".equals(getClassName(title, text, control));
	}

	public static boolean isListBox(final HWND hWnd) {
		return "ListBox".equals(getClassName(hWnd));
	}

	/**
	 * Set the status of the capslock.
	 * 
	 * @param on
	 *            Turn on capslock if true, otherwise turn off capslock.
	 */
	public static void setCapslockState(boolean on) {
		if (Win32.isCapslockOn() ^ on) {
			boolean restore = Opt.setSendCapslockMode(false);
			Keyboard.send("{CAPSLOCK}");
			if (restore) {
				Opt.setSendCapslockMode(restore);
			}
		}
	}

	public static interface GDI32Ext extends GDI32 {
		GDI32Ext INSTANCE = (GDI32Ext) Native.loadLibrary("gdi32",
				GDI32Ext.class, W32APIOptions.DEFAULT_OPTIONS);

		/**
		 * The GetStockObject function retrieves a handle to one of the stock
		 * pens, brushes, fonts, or palettes.
		 * 
		 * @param fnObject
		 *            The type of stock object. This parameter can be one of the
		 *            following values.
		 * @return If the function succeeds, the return value is a handle to the
		 *         requested logical object. If the function fails, the return
		 *         value is null.
		 * @see <a
		 *      href="http://msdn.microsoft.com/en-us/library/windows/desktop/dd144925(v=vs.85).aspx">GetStockObject
		 *      function (Windows)</a>
		 */
		public HANDLE GetStockObject(int fnObject);
	}

	public static interface User32Ext extends User32 {
		User32Ext INSTANCE = (User32Ext) Native.loadLibrary("user32",
				User32Ext.class, W32APIOptions.DEFAULT_OPTIONS);

		/**
		 * Enables or disables mouse and keyboard input to the specified window
		 * or control. When input is disabled, the window does not receive input
		 * such as mouse clicks and key presses. When input is enabled, the
		 * window receives all input.
		 * 
		 * @param hWnd
		 *            A handle to the window to be enabled or disabled.
		 * @param enable
		 *            Indicates whether to enable or disable the window. If this
		 *            parameter is true, the window is enabled. If the parameter
		 *            is false, the window is disabled.
		 * @return If the window was previously disabled, the return value is
		 *         true. If the window was not previously disabled, the return
		 *         value is false.
		 */
		boolean EnableWindow(HWND hWnd, boolean enable);

		/**
		 * Retrieves the identifier of the specified control.
		 * 
		 * GetDlgCtrlID accepts child window handles as well as handles of
		 * controls in dialog boxes. An application sets the identifier for a
		 * child window when it creates the window by assigning the identifier
		 * value to the hmenu parameter when calling the CreateWindow or
		 * CreateWindowEx function. Although GetDlgCtrlID may return a value if
		 * hwndCtl is a handle to a top-level window, top-level windows cannot
		 * have identifiers and such a return value is never valid.
		 * 
		 * @param hwndCtl
		 *            A handle to the control.
		 * @return If the function succeeds, the return value is the identifier
		 *         of the control. If the function fails, the return value is
		 *         zero. An invalid value for the hwndCtl parameter, for
		 *         example, will cause the function to fail. To get extended
		 *         error information, call GetLastError.
		 * @see <a href=
		 *      "http://msdn.microsoft.com/en-us/library/windows/desktop/ms645478(v=vs.85).aspx"
		 *      >GetDlgCtrlID function (Windows)</a>
		 */
		int GetDlgCtrlID(HWND hwndCtl);

		public HWND GetFocus();

		/**
		 * Retrieves the status of the specified virtual key. The status
		 * specifies whether the key is up, down, or toggled (on,
		 * off-alternating each time the key is pressed).
		 * 
		 * @param key
		 *            virtual key. If the desired virtual key is a letter or
		 *            digit (A through Z, a through z, or 0 through 9), nVirtKey
		 *            must be set to the ASCII value of that character. For
		 *            other keys, it must be a virtual-key code. If a
		 *            non-English keyboard layout is used, virtual keys with
		 *            values in the range ASCII A through Z and 0 through 9 are
		 *            used to specify most of the character keys. For example,
		 *            for the German keyboard layout, the virtual key of value
		 *            ASCII O (0x4F) refers to the "o" key, whereas VK_OEM_1
		 *            refers to the "o with umlaut" key.
		 * @return The return value specifies the status of the specified
		 *         virtual key, as follows:<br/>
		 * 
		 *         If the high-order bit is 1, the key is down; otherwise, it is
		 *         up.<br/>
		 * 
		 *         If the low-order bit is 1, the key is toggled. A key, such as
		 *         the CAPS LOCK key, is toggled if it is turned on. The key is
		 *         off and untoggled if the low-order bit is 0. A toggle key's
		 *         indicator light (if any) on the keyboard will be on when the
		 *         key is toggled, and off when the key is untoggled.
		 * @see <a href=
		 *      "http://msdn.microsoft.com/en-us/library/windows/desktop/ms646301(v=vs.85).aspx"
		 *      >GetKeyState function (Windows)</a>
		 */
		public short GetKeyState(int key);

		public HWND GetParent(HWND hWnd);

		/**
		 * Displays a modal dialog box that contains a system icon, a set of
		 * buttons, and a brief application-specific message, such as status or
		 * error information. The message box returns an integer value that
		 * indicates which button the user clicked.
		 * 
		 * @param hWnd
		 *            A handle to the owner window of the message box to be
		 *            created. If this parameter is NULL, the message box has no
		 *            owner window.
		 * @param text
		 *            The message to be displayed. If the string consists of
		 *            more than one line, you can separate the lines using a
		 *            carriage return and/or linefeed character between each
		 *            line.
		 * @param caption
		 *            The dialog box title. If this parameter is NULL, the
		 *            default title is Error.
		 * @param type
		 *            The contents and behavior of the dialog box. This
		 *            parameter can be a combination of flags from the following
		 *            groups of flags.
		 * @return If a message box has a Cancel button, the function returns
		 *         the IDCANCEL value if either the ESC key is pressed or the
		 *         Cancel button is selected. If the message box has no Cancel
		 *         button, pressing ESC has no effect. If the function fails,
		 *         the return value is zero. To get extended error information,
		 *         call GetLastError. If the function succeeds, the return value
		 *         is one of the following menu-item values.
		 * @see <a
		 *      href="http://msdn.microsoft.com/en-us/library/windows/desktop/ms645505(v=vs.85).aspx">MessageBox
		 *      function (Windows)</a>
		 */
		int MessageBox(HWND hWnd, String text, String caption, int type);

		/**
		 * Sends the specified message to a window or windows. The SendMessage
		 * function calls the window procedure for the specified window and does
		 * not return until the window procedure has processed the message.
		 * 
		 * @param hWnd
		 *            A handle to the window whose window procedure will receive
		 *            the message. If this parameter is HWND_BROADCAST
		 *            ((HWND)0xffff), the message is sent to all top-level
		 *            windows in the system, including disabled or invisible
		 *            unowned windows, overlapped windows, and pop-up windows;
		 *            but the message is not sent to child windows.
		 * 
		 *            Message sending is subject to UIPI. The thread of a
		 *            process can send messages only to message queues of
		 *            threads in processes of lesser or equal integrity level.
		 * @param msg
		 *            The message to be sent.
		 * @param wParam
		 *            Additional message-specific information.
		 * @param lParam
		 *            Additional message-specific information.
		 * @return The return value specifies the result of the message
		 *         processing; it depends on the message sent.
		 * @see <a href=
		 *      "http://msdn.microsoft.com/en-us/library/windows/desktop/ms644950(v=vs.85).aspx"
		 *      >SendMessage function (Windows)</a>
		 */
		int SendMessage(HWND hWnd, int msg, int wParam, int lParam);

		int SendMessage(HWND hWnd, int msg, int wParam, boolean lParam);

		int SendMessage(HWND hWnd, int msg, int wParam, char[] lParam);

		HANDLE SendMessage(HWND hWnd, int msg, int wParam, HANDLE lParam);

		int SendMessage(HWND hWnd, int msg, HANDLE wParam, boolean lParam);

		int SendMessage(HWND hWnd, int msg, boolean wParam);

		/**
		 * Sends the specified message to a window or windows. The SendMessage
		 * function calls the window procedure for the specified window and does
		 * not return until the window procedure has processed the message.
		 * 
		 * @param hWnd
		 *            A handle to the window whose window procedure will receive
		 *            the message. If this parameter is HWND_BROADCAST
		 *            ((HWND)0xffff), the message is sent to all top-level
		 *            windows in the system, including disabled or invisible
		 *            unowned windows, overlapped windows, and pop-up windows;
		 *            but the message is not sent to child windows.
		 * 
		 *            Message sending is subject to UIPI. The thread of a
		 *            process can send messages only to message queues of
		 *            threads in processes of lesser or equal integrity level.
		 * @param msg
		 *            The message to be sent.
		 * @param wParam
		 *            Additional message-specific information.
		 * @param lParam
		 *            Additional message-specific information.
		 * @return The return value specifies the result of the message
		 *         processing; it depends on the message sent.
		 * @see <a href=
		 *      "http://msdn.microsoft.com/en-us/library/windows/desktop/ms644950(v=vs.85).aspx"
		 *      >SendMessage function (Windows)</a>
		 */
		HWND SendMessage(HWND hWnd, int msg, WPARAM wParam, LPARAM lParam);

		/**
		 * Sends the specified message to a window or windows. The SendMessage
		 * function calls the window procedure for the specified window and does
		 * not return until the window procedure has processed the message.
		 * 
		 * @param hWnd
		 *            A handle to the window whose window procedure will receive
		 *            the message. If this parameter is HWND_BROADCAST
		 *            ((HWND)0xffff), the message is sent to all top-level
		 *            windows in the system, including disabled or invisible
		 *            unowned windows, overlapped windows, and pop-up windows;
		 *            but the message is not sent to child windows.
		 * 
		 *            Message sending is subject to UIPI. The thread of a
		 *            process can send messages only to message queues of
		 *            threads in processes of lesser or equal integrity level.
		 * @param msg
		 *            The message to be sent.
		 * @param wParam
		 *            Additional message-specific information.
		 * @param lParam
		 *            Additional message-specific information.
		 * @return The return value specifies the result of the message
		 *         processing; it depends on the message sent.
		 * @see <a href=
		 *      "http://msdn.microsoft.com/en-us/library/windows/desktop/ms644950(v=vs.85).aspx"
		 *      >SendMessage function (Windows)</a>
		 */
		HWND SendMessage(HWND hWnd, int msg, WPARAM wParam, HWND lParam);

		/**
		 * Sends the specified message to a window or windows. The SendMessage
		 * function calls the window procedure for the specified window and does
		 * not return until the window procedure has processed the message.
		 * 
		 * @param hWnd
		 *            A handle to the window whose window procedure will receive
		 *            the message. If this parameter is HWND_BROADCAST
		 *            ((HWND)0xffff), the message is sent to all top-level
		 *            windows in the system, including disabled or invisible
		 *            unowned windows, overlapped windows, and pop-up windows;
		 *            but the message is not sent to child windows.
		 * 
		 *            Message sending is subject to UIPI. The thread of a
		 *            process can send messages only to message queues of
		 *            threads in processes of lesser or equal integrity level.
		 * @param msg
		 *            The message to be sent.
		 * @param wParam
		 *            Additional message-specific information.
		 * @param lParam
		 *            Additional message-specific information.
		 * @return The return value specifies the result of the message
		 *         processing; it depends on the message sent.
		 * @see <a href=
		 *      "http://msdn.microsoft.com/en-us/library/windows/desktop/ms644950(v=vs.85).aspx"
		 *      >SendMessage function (Windows)</a>
		 */
		int SendMessage(HWND hWnd, int msg, WPARAM wParam, Structure lParam);

		/**
		 * Sends the specified message to a window or windows. The SendMessage
		 * function calls the window procedure for the specified window and does
		 * not return until the window procedure has processed the message.
		 * 
		 * @param hWnd
		 *            A handle to the window whose window procedure will receive
		 *            the message. If this parameter is HWND_BROADCAST
		 *            ((HWND)0xffff), the message is sent to all top-level
		 *            windows in the system, including disabled or invisible
		 *            unowned windows, overlapped windows, and pop-up windows;
		 *            but the message is not sent to child windows.
		 * 
		 *            Message sending is subject to UIPI. The thread of a
		 *            process can send messages only to message queues of
		 *            threads in processes of lesser or equal integrity level.
		 * @param msg
		 *            The message to be sent.
		 * @param wParam
		 *            Additional message-specific information.
		 * @param buffer
		 *            Additional message-specific information.
		 * @return The return value specifies the result of the message
		 *         processing; it depends on the message sent.
		 * @see <a href=
		 *      "http://msdn.microsoft.com/en-us/library/windows/desktop/ms644950(v=vs.85).aspx"
		 *      >SendMessage function (Windows)</a>
		 */
		int SendMessage(HWND hWnd, int msg, WPARAM wParam, CharBuffer buffer);

		boolean SetWindowText(HWND hWnd, String text);

		boolean ScreenToClient(HWND hWnd, POINT point);
	}
}
