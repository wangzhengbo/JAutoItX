package cn.com.jautoitx;

import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.CharBuffer;
import java.util.logging.Logger;

import org.apache.commons.lang3.StringUtils;

import cn.com.jautoitx.Opt.CoordMode;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Platform;
import com.sun.jna.Pointer;
import com.sun.jna.WString;
import com.sun.jna.platform.win32.WinDef.HWND;
import com.sun.jna.platform.win32.WinDef.POINT;
import com.sun.jna.platform.win32.WinDef.RECT;

/**
 * 
 * 
 * @author zhengbo.wang
 */
public class AutoItX {
	/* AutoItX's version */
	private static String AUTOITX_VERSION = null;

	/* Hides the window and activates another window. */
	public static final int SW_HIDE = 0;

	/* Maximizes the specified window. */
	public static final int SW_MAXIMIZE = 3;

	/*
	 * Minimizes the specified window and activates the next top-level window in
	 * the Z order.
	 */
	public static final int SW_MINIMIZE = 6;

	/*
	 * Activates and displays the window. If the window is minimized or
	 * maximized, the system restores it to its original size and position. An
	 * application should specify this flag when restoring a minimized window.
	 */
	public static final int SW_RESTORE = 9;

	/*
	 * Activates the window and displays it in its current size and position.
	 */
	public static final int SW_SHOW = 5;

	/*
	 * Sets the show state based on the SW_ value specified by the program that
	 * started the application.
	 */
	public static final int SW_SHOWDEFAULT = 10;

	/* Activates the window and displays it as a maximized window. */
	public static final int SW_SHOWMAXIMIZED = 3;

	/* Activates the window and displays it as a minimized window. */
	public static final int SW_SHOWMINIMIZED = 2;

	/*
	 * Displays the window as a minimized window. This value is similar to
	 * SW_SHOWMINIMIZED, except the window is not activated.
	 */
	public static final int SW_SHOWMINNOACTIVE = 7;

	/*
	 * Displays the window in its current size and position. This value is
	 * similar to SW_SHOW, except the window is not activated.
	 */
	public static final int SW_SHOWNA = 8;

	/*
	 * Displays a window in its most recent size and position. This value is
	 * similar to SW_SHOWNORMAL, except the window is not actived.
	 */
	public static final int SW_SHOWNOACTIVATE = 4;

	/*
	 * Activates and displays a window. If the window is minimized or maximized,
	 * the system restores it to its original size and position. An application
	 * should specify this flag when displaying the window for the first time.
	 */
	public static final int SW_SHOWNORMAL = 1;

	protected static int INT_BUF_SIZE = String.valueOf(Integer.MIN_VALUE)
			.length();
	protected static int BOOLEAN_BUF_SIZE = 8;
	protected static int HANDLE_BUF_SIZE = 20;

	/* AutoItX library name */
	private static final String DLL_LIB_NAME = "AutoItX3"
			+ (Platform.is64Bit() ? "_x64" : "");

	/* AutoItX library path */
	private static final String DLL_LIB_RESOURCE_PATH = "/cn/com/jautoitx/lib/";

	protected static final Logger logger = Logger.getAnonymousLogger();
	protected static AutoItXLibrary autoItX;

	protected static final int SUCCESS_RETURN_VALUE = 1;
	protected static final int FAILED_RETURN_VALUE = 0;
	protected static final int TRUE = 1;

	static {
		// Initialize AutoIt
		initAutoItX();
	}

	protected AutoItX() {
		// Do nothing
	}

	/**
	 * Initialize AutoItX.
	 */
	private static void initAutoItX() {
		// Initialize AutoItX
		try {
			autoItX = loadNativeLibrary();

			logger.fine("AutoItX initialized.");
		} catch (Throwable e) {
			logger.warning("Unable to initialize "
					+ AutoItX.class.getSimpleName());
		}
	}

	/**
	 * Unpacking and loading the library into the Java Virtual Machine.
	 */
	protected static AutoItXLibrary loadNativeLibrary() {
		try {
			// Get what the system "thinks" the library name should be.
			String libNativeName = System.mapLibraryName(DLL_LIB_NAME);

			// Slice up the library name.
			int i = libNativeName.lastIndexOf('.');
			String libNativePrefix = libNativeName.substring(0, i) + '_';
			String libNativeSuffix = libNativeName.substring(i);

			// This may return null in some circumstances.
			InputStream libInputStream = AutoItX.class
					.getResourceAsStream(DLL_LIB_RESOURCE_PATH.toLowerCase()
							+ libNativeName);
			if (libInputStream == null) {
				throw new IOException("Unable to locate the native library.");
			}

			// Create the temp file for this instance of the library.
			File libFile = File
					.createTempFile(libNativePrefix, libNativeSuffix);
			libFile.deleteOnExit();

			// Copy AutoItX.dll or AutoItX_x64.dll library to the temp file.
			copyInputStreamToFile(libInputStream, libFile);
			closeQuietly(libInputStream);
			if (AUTOITX_VERSION == null) {
				AUTOITX_VERSION = Win32.getFileVersion(libFile);
			}

			System.load(libFile.getPath());

			Native.setProtected(true);
			return (AutoItXLibrary) Native.loadLibrary(libFile.getName(),
					AutoItXLibrary.class);
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	/**
	 * Get AutoItX's version.
	 * 
	 * @return Returns AutoItX's version.
	 */
	public static String version() {
		return AUTOITX_VERSION;
	}

	/**
	 * Get error property for the last AutoItX method if it will set error code.
	 * 
	 * @return Returns error property.
	 */
	public static int error() {
		return autoItX.AU3_error();
	}

	/**
	 * Check last AutoItX method execute status.
	 * 
	 * @return Returns true if last AutoItX method has error, otherwise return
	 *         false.
	 */
	public static boolean hasError() {
		return error() != 0;
	}

	/**
	 * Clears a displaying tooltip
	 */
	public static void tooltip() {
		tooltip(null);
	}

	/**
	 * Creates a tooltip anywhere on the screen.
	 * 
	 * If the x and y coordinates are omitted the, tip is placed near the mouse
	 * cursor. If the coords would cause the tooltip to run off screen, it is
	 * repositioned to visible. Tooltip appears until it is cleared, until
	 * script terminates, or sometimes until it is clicked upon. You may use a
	 * linefeed character to create multi-line tooltips.
	 * 
	 * @param text
	 *            The text of the tooltip. (An empty string clears a displaying
	 *            tooltip).
	 */
	public static void tooltip(final String text) {
		if (StringUtils.isEmpty(text)) {
			// clear tooltip
			tooltip(text, null, null);
		} else {
			// If the x and y coordinates are omitted the, tip is placed near
			// the mouse cursor

			// Fix AutoItX's bug
			CoordMode newCoodMode = CoordMode.ABSOLUTE_SCREEN_COORDINATES;
			CoordMode oldCoodMode = Opt.setMouseCoordMode(newCoodMode);

			int mousePosX = Mouse.getPosX();
			int mousePosY = Mouse.getPosY();

			// restore MouseCoordMode
			if (!newCoodMode.equals(oldCoodMode)) {
				Opt.setMouseCoordMode(oldCoodMode);
			}

			tooltip(text, mousePosX, mousePosY);
		}
	}

	/**
	 * Creates a tooltip anywhere on the screen.
	 * 
	 * If the x and y coordinates are omitted, the tip is placed near the mouse
	 * cursor. If the coords would cause the tooltip to run off screen, it is
	 * repositioned to visible. Tooltip appears until it is cleared, until
	 * script terminates, or sometimes until it is clicked upon. You may use a
	 * linefeed character to create multi-line tooltips.
	 * 
	 * @param text
	 *            The text of the tooltip. (An empty string clears a displaying
	 *            tooltip).
	 * @param x
	 *            The x position of the tooltip.
	 * @param y
	 *            The y position of the tooltip.
	 */
	public static void tooltip(final String text, Integer x, Integer y) {
		if (StringUtils.isEmpty(text)) {
			autoItX.AU3_ToolTip(stringToWString(""), null, null);
		} else {
			// Fix AutoItX's bug
			if ((x != null) && (x < 0)) {
				x = 0;
			}
			if ((y != null) && (y < 0)) {
				y = 0;
			}
			autoItX.AU3_ToolTip(stringToWString(defaultString(text)), x, y);
		}
	}

	protected static String defaultString(final String string) {
		return StringUtils.defaultString(string);
	}

	protected static WString stringToWString(final String string) {
		return (string == null ? null : new WString(string));
	}

	protected static String buildTitle(final HWND hWnd) {
		return TitleBuilder.byHandle(hWnd);
	}

	protected static String buildControlId(final HWND hCtrl) {
		return ControlIdBuilder.byHandle(hCtrl);
	}

	protected static void closeQuietly(final Closeable closeable) {
		if (closeable != null) {
			try {
				closeable.close();
			} catch (IOException e) {
				// Ignore exception
			}
		}
	}

	protected static void copyInputStreamToFile(final InputStream input,
			final File file) throws IOException {
		OutputStream output = null;
		try {
			output = new FileOutputStream(file);
			int n = 0;
			byte[] buffer = new byte[4 * 1024];
			while ((n = input.read(buffer)) != -1) {
				output.write(buffer, 0, n);
			}
		} finally {
			closeQuietly(output);
		}
	}

	public static HWND handleToHwnd(String handle) {
		HWND hWnd = null;
		try {
			if (handle != null) {
				if (handle.startsWith("0x")) {
					handle = handle.substring(2);
				}
				hWnd = new HWND(Pointer.createConstant(Long.parseLong(handle,
						16)));
			}
		} catch (Exception e) {
			// Ignore exception
		}
		return hWnd;
	}

	public static String hwndToHandle(final HWND hWnd) {
		return (hWnd == null) ? null : ("0x" + StringUtils.leftPad(Long
				.toHexString(Pointer.nativeValue(hWnd.getPointer()))
				.toUpperCase(), 8, '0'));
	}

	protected static interface AutoItXLibrary extends Library {
		/**
		 * Initialize AutoItX.
		 */
		public void AU3_Init();

		/**
		 * Get error property for the last AutoItX method if it will set error
		 * property.
		 * 
		 * @return Returns error property.
		 */
		public int AU3_error();

		/**
		 * Retrieves text from the clipboard.
		 * 
		 * Sets oAutoIt.error to 1 if clipboard is empty or contains a non-text
		 * entry.
		 * 
		 * @param clip
		 * @param bufSize
		 */
		public void AU3_ClipGet(final CharBuffer clip, final int bufSize);

		/**
		 * Writes text to the clipboard.
		 * 
		 * Any existing clipboard contents are overwritten.
		 * 
		 * @param clip
		 *            The text to write to the clipboard.
		 */
		public void AU3_ClipPut(final WString clip);

		/**
		 * Sends a mouse click command to a given control.
		 * 
		 * Some controls will resist clicking unless they are the active window.
		 * Use the WinActive() function to force the control's window to the top
		 * before using ControlClick(). Using 2 for the number of clicks will
		 * send a double-click message to the control - this can even be used to
		 * launch programs from an explorer control!
		 * 
		 * @param title
		 *            The title of the window to access.
		 * @param text
		 *            The text of the window to access.
		 * @param control
		 *            The control to interact with.
		 * @param button
		 *            The button to click, "left", "right" or "middle". Default
		 *            is the left button.
		 * @param numClicks
		 *            The number of times to click the mouse. Default is 1.
		 * @param x
		 *            The x position to click within the control. Default is
		 *            center.
		 * @param y
		 *            The y position to click within the control. Default is
		 *            center.
		 * @return Returns 1 if success, returns 0 if failed.
		 */
		public int AU3_ControlClick(final WString title, final WString text,
				final WString control, final WString button,
				final int numClicks, final Integer x, final Integer y);

		/**
		 * Sends a command to a control.
		 * 
		 * Certain commands that work on normal Combo and ListBoxes do not work
		 * on "ComboLBox" controls. When using a control name in the Control
		 * functions, you need to add a number to the end of the name to
		 * indicate which control. For example, if there two controls listed
		 * called "MDIClient", you would refer to these as "MDIClient1" and
		 * "MDIClient2". Use AU3_Spy.exe to obtain a control's number.
		 * 
		 * When using text instead of ClassName# in "Control" commands, be sure
		 * to use the entire text of the control. Partial text will fail.
		 * 
		 * @param title
		 *            The title of the window to access.
		 * @param text
		 *            The text of the window to access.
		 * @param control
		 *            The control to interact with.
		 * @param command
		 *            The command to send to the control.
		 * @param extra
		 *            Additional parameter required by some commands; use "" if
		 *            parameter is not required.
		 * @param result
		 * @param bufSize
		 */
		public void AU3_ControlCommand(final WString title, final WString text,
				final WString control, final WString command,
				final WString extra, final CharBuffer result, int bufSize);

		/**
		 * Sends a command to a ListView32 control.
		 * 
		 * @param title
		 *            The title of the window to access.
		 * @param text
		 *            The text of the window to access.
		 * @param control
		 *            The control to interact with.
		 * @param command
		 *            The command to send to the control.
		 * @param extra1
		 *            Additional parameter required by some commands; use "" if
		 *            parameter is not required.
		 * @param extra2
		 *            Additional parameter required by some commands; use "" if
		 *            parameter is not required.
		 * @param result
		 * @param bufSize
		 */
		public void AU3_ControlListView(final WString title,
				final WString text, final WString control,
				final WString command, final WString extra1,
				final WString extra2, final CharBuffer result, final int bufSize);

		/**
		 * Disables or "grays-out" a control.
		 * 
		 * When using a control name in the Control functions, you need to add a
		 * number to the end of the name to indicate which control. For example,
		 * if there two controls listed called "MDIClient", you would refer to
		 * these as "MDIClient1" and "MDIClient2".
		 * 
		 * @param title
		 *            The title of the window to access.
		 * @param text
		 *            The text of the window to access.
		 * @param control
		 *            The control to interact with.
		 * @return Returns 1 if success, returns 0 if failed.
		 */
		public int AU3_ControlDisable(final WString title, final WString text,
				final WString control);

		/**
		 * Enables a "grayed-out" control.
		 * 
		 * Use with caution.<br>
		 * When using a control name in the Control functions, you need to add a
		 * number to the end of the name to indicate which control. For example,
		 * if there two controls listed called "MDIClient", you would refer to
		 * these as "MDIClient1" and "MDIClient2".
		 * 
		 * @param title
		 *            The title of the window to access.
		 * @param text
		 *            The text of the window to access.
		 * @param control
		 *            The control to interact with.
		 * @return Returns 1 if success, returns 0 if failed.
		 */
		public int AU3_ControlEnable(final WString title, final WString text,
				final WString control);

		/**
		 * Sets input focus to a given control on a window.
		 * 
		 * When using a control name in the Control functions, you need to add a
		 * number to the end of the name to indicate which control. For example,
		 * if there two controls listed called "MDIClient", you would refer to
		 * these as "MDIClient1" and "MDIClient2".
		 * 
		 * @param title
		 *            The title of the window to access.
		 * @param text
		 *            The text of the window to access.
		 * @param control
		 *            The control to interact with.
		 * @return Returns 1 if success, returns 0 if failed.
		 */
		public int AU3_ControlFocus(final WString title, final WString text,
				final WString control);

		/**
		 * Returns the ControlRef# of the control that has keyboard focus within
		 * a specified window.
		 * 
		 * @param title
		 *            Title of window to check.
		 * @param text
		 *            Text from window to check.
		 * @param controlWithFocus
		 * @param bufSize
		 */
		public void AU3_ControlGetFocus(final WString title,
				final WString text, final CharBuffer controlWithFocus,
				final int bufSize);

		/**
		 * Retrieves the internal handle of a control.
		 * 
		 * @param title
		 *            The title of the window to read.
		 * @param text
		 *            The text of the window to read.
		 * @param control
		 *            The control to interact with.
		 * @param retText
		 * @param bufSize
		 */
		public void AU3_ControlGetHandleAsText(final WString title,
				final WString text, final WString control,
				final CharBuffer retText, final int bufSize);

		/**
		 * Retrieves the position and size of a control relative to it's window.
		 * 
		 * When using a control name in the Control functions, you need to add a
		 * number to the end of the name to indicate which control. For example,
		 * if there two controls listed called "MDIClient", you would refer to
		 * these as "MDIClient1" and "MDIClient2".
		 * 
		 * @param title
		 *            The title of the window to access.
		 * @param text
		 *            The text of the window to access.
		 * @param control
		 *            The control to interact with.
		 * @param rect
		 */
		public void AU3_ControlGetPos(final WString title, final WString text,
				final WString control, final RECT rect);

		/**
		 * Retrieves text from a control.
		 * 
		 * When using a control name in the Control functions, you need to add a
		 * number to the end of the name to indicate which control. For example,
		 * if there two controls listed called "MDIClient", you would refer to
		 * these as "MDIClient1" and "MDIClient2".
		 * 
		 * @param title
		 *            The title of the window to access.
		 * @param text
		 *            The text of the window to access.
		 * @param control
		 *            The control to interact with.
		 * @param controlText
		 * @param bufSize
		 */
		public void AU3_ControlGetText(final WString title, final WString text,
				final WString control, final CharBuffer controlText,
				final int bufSize);

		/**
		 * Hides a control.
		 * 
		 * When using a control name in the Control functions, you need to add a
		 * number to the end of the name to indicate which control. For example,
		 * if there two controls listed called "MDIClient", you would refer to
		 * these as "MDIClient1" and "MDIClient2".
		 * 
		 * @param title
		 *            The title of the window to access.
		 * @param text
		 *            The text of the window to access.
		 * @param control
		 *            The control to interact with.
		 * @return Returns 1 if success, returns 0 if window/control is not
		 *         found.
		 */
		public int AU3_ControlHide(final WString title, final WString text,
				final WString control);

		/**
		 * Moves a control within a window.
		 * 
		 * When using a control name in the Control functions, you need to add a
		 * number to the end of the name to indicate which control. For example,
		 * if there two controls listed called "MDIClient", you would refer to
		 * these as "MDIClient1" and "MDIClient2".
		 * 
		 * @param title
		 *            The title of the window to move.
		 * @param text
		 *            The text of the window to move.
		 * @param control
		 *            The control to interact with.
		 * @param x
		 *            X coordinate to move to.
		 * @param y
		 *            Y coordinate to move to.
		 * @param width
		 *            New width of the window.
		 * @param height
		 *            New height of the window.
		 * @return Returns 1 if success, returns 0 if window/control is not
		 *         found.
		 */
		public int AU3_ControlMove(final WString title, final WString text,
				final WString control, final int x, final int y,
				final Integer width, final Integer height);

		/**
		 * Sends a string of characters to a control.
		 * 
		 * ControlSend can be quite useful to send capital letters without
		 * messing up the state of "Shift."
		 * 
		 * When using a control name in the Control functions, you need to add a
		 * number to the end of the name to indicate which control. For example,
		 * if there two controls listed called "MDIClient", you would refer to
		 * these as "MDIClient1" and "MDIClient2". Note, this function cannot
		 * send all the characters that the usual Send function can (notably ALT
		 * keys) but it can send most of them--even to non-active or hidden
		 * windows!
		 * 
		 * @param title
		 *            The title of the window to access.
		 * @param text
		 *            The text of the window to access.
		 * @param control
		 *            The control to interact with.
		 * @param sendText
		 *            String of characters to send to the control.
		 * @param mode
		 *            Changes how "keys" is processed: flag = 0 (default), Text
		 *            contains special characters like + to indicate SHIFT and
		 *            {LEFT} to indicate left arrow. flag = 1, keys are sent
		 *            raw.
		 * @return Returns 1 if success, returns 0 if window/control is not
		 *         found.
		 */
		public int AU3_ControlSend(final WString title, final WString text,
				final WString control, final WString sendText,
				final Integer mode);

		/**
		 * Sets text of a control.
		 * 
		 * When using a control name in the Control functions, you need to add a
		 * number to the end of the name to indicate which control. For example,
		 * if there two controls listed called "MDIClient", you would refer to
		 * these as "MDIClient1" and "MDIClient2".
		 * 
		 * @param title
		 *            The title of the window to access.
		 * @param text
		 *            The text of the window to access.
		 * @param control
		 *            The control to interact with.
		 * @param controlText
		 *            The new text to be set into the control.
		 * @return Returns 1 if success, returns 0 if window/control is not
		 *         found.
		 */
		public int AU3_ControlSetText(final WString title, final WString text,
				final WString control, final WString controlText);

		/**
		 * Shows a control that was hidden.
		 * 
		 * When using a control name in the Control functions, you need to add a
		 * number to the end of the name to indicate which control. For example,
		 * if there two controls listed called "MDIClient", you would refer to
		 * these as "MDIClient1" and "MDIClient2".
		 * 
		 * @param title
		 *            The title of the window to access.
		 * @param text
		 *            The text of the window to access.
		 * @param control
		 *            The control to interact with.
		 * @return Returns 1 is success, returns 0 if window/control is not
		 *         found.
		 */
		public int AU3_ControlShow(final WString title, final WString text,
				final WString control);

		/**
		 * Sends a command to a TreeView32 control.
		 * 
		 * Depends on command as table below shows. In case of an error (such as
		 * an invalid command or window/control could not be found) then @error
		 * is set to 1.<br>
		 * <br>
		 * <br>
		 * <table border="1" width="100%" cellspacing="0" cellpadding="3" bordercolor="#C0C0C0">
		 * <tr>
		 * <td width="40%"><b>Command, Option1, Option2</b></td>
		 * <td width="60%"><b>Operation</b></td>
		 * </tr>
		 * <tr>
		 * <td>"Check", "item"</td>
		 * <td>Checks an item (if the item supports it).</td>
		 * </tr>
		 * <tr>
		 * <td>"Collapse", "item"</td>
		 * <td>Collapses an item to hide its children.</td>
		 * </tr>
		 * <tr>
		 * <td>"Exists", "item"</td>
		 * <td>Returns 1 if an item exists, otherwise 0.</td>
		 * </tr>
		 * <tr>
		 * <td>"Expand", "item"</td>
		 * <td>Expands an item to show its children.</td>
		 * </tr>
		 * <tr>
		 * <td>"GetItemCount", "item"</td>
		 * <td>Returns the number of children for a selected item.</td>
		 * </tr>
		 * <tr>
		 * <td>"GetSelected" [, UseIndex]</td>
		 * <td>Returns the item reference of the current selection using the
		 * text reference of the item (or index reference if UseIndex is set to
		 * 1).</td>
		 * </tr>
		 * <tr>
		 * <td>"GetText", "item"</td>
		 * <td>Returns the text of an item.</td>
		 * </tr>
		 * <tr>
		 * <td>"IsChecked"</td>
		 * <td>Returns the state of an item. 1:checked, 0:unchecked, -1:not a
		 * checkbox.</td>
		 * </tr>
		 * <tr>
		 * <td>"Select", "item"</td>
		 * <td>Selects an item.</td>
		 * </tr>
		 * <tr>
		 * <td>"Uncheck", "item"</td>
		 * <td>Unchecks an item (if the item supports it).</td>
		 * </tr>
		 * </table>
		 * 
		 * @param title
		 *            The title of the window to access.
		 * @param text
		 *            The text of the window to access.
		 * @param control
		 *            The control to interact with.
		 * @param command
		 *            The command to send to the control.
		 * @param extra1
		 *            Additional parameter required by some commands; use "" if
		 *            parameter is not required.
		 * @param extra2
		 *            Additional parameter required by some commands; use "" if
		 *            parameter is not required.
		 * @param result
		 * @param bufSize
		 */
		public void AU3_ControlTreeView(final WString title,
				final WString text, final WString control,
				final WString command, final WString extra1,
				final WString extra2, final CharBuffer result, final int bufSize);

		/**
		 * Maps a network drive.
		 * 
		 * When the function fails (returns 0) oAutoIt.error contains extended
		 * information:
		 * 
		 * 1 = Undefined / Other error
		 * 
		 * 2 = Access to the remote share was denied
		 * 
		 * 3 = The device is already assigned
		 * 
		 * 4 = Invalid device name
		 * 
		 * 5 = Invalid remote share
		 * 
		 * 6 = Invalid password
		 * 
		 * Note: When using "*" for the device parameter the drive letter
		 * selected will be returned instead of 1 or 0, e.g. "U:". If there was
		 * an error using "*" then a blank string "" will be returned.
		 * 
		 * @param device
		 *            The device to map, for example "O:" or "LPT1:". If you
		 *            pass a blank string for this parameter a connection is
		 *            made but not mapped to a specific drive. If you specify
		 *            "*" an unused drive letter will be automatically selected.
		 * @param share
		 *            The remote share to connect to in the form
		 *            "\\server\share".
		 * @param flags
		 *            A combination of the following:
		 * 
		 *            0 = default
		 * 
		 *            1 = Persistant mapping
		 * 
		 *            8 = Show authentication dialog if required
		 * @param user
		 *            The username to use to connect. In the form "username" or
		 *            "domain\Username".
		 * @param password
		 *            The password to use to connect.
		 * @param result
		 * @param bufSize
		 */
		public void AU3_DriveMapAdd(final WString device, final WString share,
				final int flags, final WString user, final WString password,
				final CharBuffer result, final int bufSize);

		/**
		 * Disconnects a network drive.
		 * 
		 * If a connection has no drive letter mapped you may use the connection
		 * name to disconnect, e.g. \\server\share
		 * 
		 * @param device
		 *            The device to disconnect, e.g. "O:" or "LPT1:".
		 * @return Return 1 if success, return 0 if the disconnection was
		 *         unsuccessful.
		 */
		public int AU3_DriveMapDel(final WString device);

		/**
		 * Retreives the details of a mapped drive.
		 * 
		 * @param device
		 *            The device (drive or printer) letter to query. Eg. "O:" or
		 *            "LPT1:"
		 * @param mapping
		 * @param bufSize
		 */
		public void AU3_DriveMapGet(final WString device,
				final CharBuffer mapping, final int bufSize);

		/**
		 * Checks if the current user has administrator privileges.
		 * 
		 * @return Return 1 if the current user has administrator privileges,
		 *         return 0 if user lacks admin privileges.
		 */
		public int AU3_IsAdmin();

		/**
		 * Perform a mouse click operation.
		 * 
		 * If the button is an empty string, the left button will be clicked.
		 * 
		 * If the button is not in the list, then oAutoIt.error will be set to
		 * 1.
		 * 
		 * If the user has swapped the left and right mouse buttons in the
		 * control panel, then the behaviour of the buttons is different. "Left"
		 * and "right" always click those buttons, whether the buttons are
		 * swapped or not. The "primary" or "main" button will be the main
		 * click, whether or not the buttons are swapped. The "secondary" or
		 * "menu" buttons will usually bring up the context menu, whether the
		 * buttons are swapped or not.
		 * 
		 * <table width="100%" border="1">
		 * <tr>
		 * <td><b>Button</b></td>
		 * <td><b>Normal</b></td>
		 * <td><b>Swapped</b></td>
		 * </tr>
		 * <tr>
		 * <td>""</td>
		 * <td>Left</td>
		 * <td>Left</td>
		 * </tr>
		 * <tr>
		 * <td>"left"</td>
		 * <td>Left</td>
		 * <td>Left</td>
		 * </tr>
		 * <tr>
		 * <td>"middle"</td>
		 * <td>Middle</td>
		 * <td>Middle</td>
		 * </tr>
		 * <tr>
		 * <td>"right"</td>
		 * <td>Right</td>
		 * <td>Right</td>
		 * </tr>
		 * <tr>
		 * <td>"primary"</td>
		 * <td>Left</td>
		 * <td>Right</td>
		 * </tr>
		 * <tr>
		 * <td>"main"</td>
		 * <td>Left</td>
		 * <td>Right</td>
		 * </tr>
		 * <tr>
		 * <td>"secondary"</td>
		 * <td>Right</td>
		 * <td>Left</td>
		 * </tr>
		 * <tr>
		 * <td>"menu"</td>
		 * <td>Right</td>
		 * <td>Left</td>
		 * </tr>
		 * </table>
		 * 
		 * @param button
		 *            The button to click: "left", "right", "middle", "main",
		 *            "menu", "primary", "secondary".
		 * @param x
		 *            The x coordinate to move the mouse to. If no x and y
		 *            coords are given, the current position is used.
		 * @param y
		 *            The y coordinate to move the mouse to. If no x and y
		 *            coords are given, the current position is used.
		 * @param clicks
		 *            The number of times to click the mouse. Default is 1.
		 * @param speed
		 *            the speed to move the mouse in the range 1 (fastest) to
		 *            100 (slowest). A speed of 0 will move the mouse instantly.
		 *            Default speed is 10.
		 * @return
		 */
		public int AU3_MouseClick(final WString button, final Integer x,
				final Integer y, final Integer clicks, final Integer speed);

		/**
		 * Perform a mouse click and drag operation.
		 * 
		 * If the button is an empty string, the left button will be clicked.
		 * 
		 * If the button is not in the list, then oAutoIt.error will be set to
		 * 1.
		 * 
		 * If the user has swapped the left and right mouse buttons in the
		 * control panel, then the behaviour of the buttons is different. "Left"
		 * and "right" always click those buttons, whether the buttons are
		 * swapped or not. The "primary" or "main" button will be the main
		 * click, whether or not the buttons are swapped. The "secondary" or
		 * "menu" buttons will usually bring up the context menu, whether the
		 * buttons are swapped or not. See the table in MouseClick for more
		 * explaination
		 * 
		 * @param button
		 *            The button to click: "left", "right", "middle", "main",
		 *            "menu", "primary", "secondary".
		 * @param x1
		 *            The x coord to start the drag operation from.
		 * @param y1
		 *            The y coord to start the drag operation from.
		 * @param x2
		 *            The x coord to start the drag operation to.
		 * @param y2
		 *            The y coord to start the drag operation to.
		 * @param speed
		 *            the speed to move the mouse in the range 1 (fastest) to
		 *            100 (slowest). A speed of 0 will move the mouse instantly.
		 *            Default speed is 10.
		 * @return
		 */
		public int AU3_MouseClickDrag(final WString button, final int x1,
				final int y1, final int x2, final int y2, final Integer speed);

		/**
		 * Perform a mouse down event at the current mouse position.
		 * 
		 * Use responsibly: For every MouseDown there should eventually be a
		 * corresponding MouseUp event.
		 * 
		 * @param button
		 *            The button to click: "left", "right", "middle", "main",
		 *            "menu", "primary", "secondary".
		 */
		public void AU3_MouseDown(final WString button);

		/**
		 * Returns a cursor ID Number of the current Mouse Cursor.
		 * 
		 * @return Returns a cursor ID Number:<br>
		 *         0 = UNKNOWN (this includes pointing and grabbing hand icons)<br>
		 *         1 = APPSTARTING<br>
		 *         2 = ARROW<br>
		 *         3 = CROSS<br>
		 *         4 = HELP<br>
		 *         5 = IBEAM<br>
		 *         6 = ICON<br>
		 *         7 = NO<br>
		 *         8 = SIZE<br>
		 *         9 = SIZEALL<br>
		 *         10 = SIZENESW<br>
		 *         11 = SIZENS<br>
		 *         12 = SIZENWSE<br>
		 *         13 = SIZEWE<br>
		 *         14 = UPARROW<br>
		 *         15 = WAIT
		 */
		public int AU3_MouseGetCursor();

		/**
		 * Retrieves the current X position of the mouse cursor.
		 * 
		 * See MouseCoordMode for relative/absolute position settings. If
		 * relative positioning, numbers may be negative.
		 * 
		 * @param point
		 */
		public void AU3_MouseGetPos(POINT point);

		/**
		 * Moves the mouse pointer.
		 * 
		 * @param x
		 *            The screen x coordinate to move the mouse to.
		 * @param y
		 *            The screen y coordinate to move the mouse to.
		 * @param speed
		 *            the speed to move the mouse in the range 1 (fastest) to
		 *            100 (slowest). A speed of 0 will move the mouse instantly.
		 *            Default speed is 10.
		 * @return User mouse movement is hindered during a non-instantaneous
		 *         MouseMove operation. If MouseCoordMode is relative
		 *         positioning, numbers may be negative.
		 */
		public int AU3_MouseMove(final int x, final int y, final Integer speed);

		/**
		 * Perform a mouse up event at the current mouse position.
		 * 
		 * Use responsibly: For every MouseDown there should eventually be a
		 * corresponding MouseUp event.
		 * 
		 * @param button
		 *            The button to click: "left", "right", "middle", "main",
		 *            "menu", "primary", "secondary".
		 */
		public void AU3_MouseUp(final WString button);

		/**
		 * Moves the mouse wheel up or down. NT/2000/XP ONLY.
		 * 
		 * If the direction is not recognized, oAutoIt.error is set to 1.
		 * 
		 * This function only works on NT, 2000, XP and later operating systems.
		 * 
		 * @param direction
		 *            "up" or "down"
		 * @param clicks
		 *            The number of times to move the wheel. Default is 1.
		 */
		public void AU3_MouseWheel(final WString direction, final Integer clicks);

		/**
		 * Changes the operation of various AutoIt functions/parameters.
		 * 
		 * AutoIt will halt with an error message if the requested option is
		 * unknown. Options are as follows:<br>
		 * <table border="1" width="100%" cellspacing="0" cellpadding="3" bordercolor="#C0C0C0">
		 * <tr>
		 * <td width="15%"><b>Option</b></td>
		 * <td width="85%"><b>Param</b></td>
		 * </tr>
		 * <tr>
		 * <td>CaretCoordMode</td>
		 * <td><a name="CaretCoordMode"></a>Sets the way coords are used in the
		 * caret functions, either absolute coords or coords relative to the
		 * current active window:<br>
		 * 0 = relative coords to the active window<br>
		 * 1 = absolute screen coordinates (default)<br>
		 * 2 = relative coords to the client area of the active window</td>
		 * </tr>
		 * <tr>
		 * <td>MouseClickDelay</td>
		 * <td><a name="MouseClickDelay"></a>Alters the length of the brief
		 * pause in between mouse clicks.<br>
		 * Time in milliseconds to pause (default=10).</td>
		 * </tr>
		 * <tr>
		 * <td>MouseClickDownDelay</td>
		 * <td><a name="MouseClickDownDelay"></a>Alters the length a click is
		 * held down before release.<br>
		 * Time in milliseconds to pause (default=10).</td>
		 * </tr>
		 * <tr>
		 * <td>MouseClickDragDelay</td>
		 * <td><a name="MouseClickDragDelay"></a>Alters the length of the brief
		 * pause at the start and end of a mouse drag operation.<br>
		 * Time in milliseconds to pause (default=250).</td>
		 * </tr>
		 * <tr>
		 * <td>MouseCoordMode</td>
		 * <td><a name="MouseCoordMode"></a>Sets the way coords are used in the
		 * mouse functions, either absolute coords or coords relative to the
		 * current active window:<br>
		 * 0 = relative coords to the active window<br>
		 * 1 = absolute screen coordinates (default)<br>
		 * 2 = relative coords to the client area of the active window</td>
		 * </tr>
		 * <tr>
		 * <td>PixelCoordMode</td>
		 * <td><a name="PixelCoordMode"></a>Sets the way coords are used in the
		 * pixel functions, either absolute coords or coords relative to the
		 * current active window:<br>
		 * 0 = relative coords to the active window<br>
		 * 1 = absolute screen coordinates (default)<br>
		 * 2 = relative coords to the client area of the active window</td>
		 * </tr>
		 * <tr>
		 * <td>SendAttachMode</td>
		 * <td><a name="SendAttachMode"></a>Specifies if AutoIt attaches input
		 * threads when using then Send() function. When not attaching (default
		 * mode=0) detecting the state of capslock/scrolllock and numlock can be
		 * unreliable under NT4. However, when you specify attach mode=1 the
		 * Send("{... down/up}") syntax will not work and there may be problems
		 * with sending keys to "hung" windows. ControlSend() ALWAYS attaches
		 * and is not affected by this mode.<br>
		 * 0 = don't attach (default)<br>
		 * 1 = attach</td>
		 * </tr>
		 * <tr>
		 * <td>SendCapslockMode</td>
		 * <td><a name="SendCapslockMode"></a>Specifies if AutoIt should store
		 * the state of capslock before a Send function and restore it
		 * afterwards.<br>
		 * 0 = don't store/restore<br>
		 * 1 = store and restore (default)</td>
		 * </tr>
		 * <tr>
		 * <td>SendKeyDelay</td>
		 * <td><a name="SendKeyDelay"></a>Alters the the length of the brief
		 * pause in between sent keystrokes.<br>
		 * Time in milliseconds to pause (default=5). Sometimes a value of 0
		 * does not work; use 1 instead.</td>
		 * </tr>
		 * <tr>
		 * <td>SendKeyDownDelay</td>
		 * <td><a name="SendKeyDownDelay"></a>Alters the length of time a key is
		 * held down before released during a keystroke. For applications that
		 * take a while to register keypresses (and many games) you may need to
		 * raise this value from the default.<br>
		 * Time in milliseconds to pause (default=1).</td>
		 * </tr>
		 * <tr>
		 * <td>WinDetectHiddenText</td>
		 * <td><a name="WinDetectHiddenText"></a>Specifies if hidden window text
		 * can be "seen" by the window matching functions.<br>
		 * 0 = Do not detect hidden text (default)<br>
		 * 1 = Detect hidden text</td>
		 * </tr>
		 * <tr>
		 * <td>WinSearchChildren</td>
		 * <td><a name="WinSearchChildren"></a>Allows the window search routines
		 * to search child windows as well as top-level windows.<br>
		 * 0 = Only search top-level windows (default)<br>
		 * 1 = Search top-level and child windows</td>
		 * </tr>
		 * <tr>
		 * <td>WinTextMatchMode</td>
		 * <td><a name="WinTextMatchMode"></a>Alters the method that is used to
		 * match window text during search operations.<br>
		 * 1 = Complete / Slow mode (default)<br>
		 * 2 = Quick mode<br>
		 * In quick mode AutoIt can usually only "see" dialog text, button text
		 * and the captions of some controls. In the default mode much more text
		 * can be seen (for instance the contents of the Notepad window).<br>
		 * If you are having performance problems when performing many window
		 * searches then changing to the "quick" mode may help.</td>
		 * </tr>
		 * <tr>
		 * <td>WinTitleMatchMode</td>
		 * <td><a name="WinTitleMatchMode"></a>Alters the method that is used to
		 * match window titles during search operations.<br>
		 * 1 = Match the title from the start (default)<br>
		 * 2 = Match any substring in the title<br>
		 * 3 = Exact title match<br>
		 * 4 = Advanced mode, see <a
		 * href="../../intro/windowsadvanced.htm">Window Titles & Text
		 * (Advanced)</a></td>
		 * </tr>
		 * <tr>
		 * <td>WinWaitDelay</td>
		 * <td><a name="WinWaitDelay"></a>Alters how long a script should
		 * briefly pause after a successful window-related operation.<br>
		 * Time in milliseconds to pause (default=250).</td>
		 * </tr>
		 * </table>
		 * 
		 * @param option
		 *            The option to change.
		 * @param value
		 *            The parameter (varies by option).
		 * @return Returns the value of the previous setting.
		 */
		public int AU3_Opt(final WString option, final int value);

		/**
		 * Generates a checksum for a region of pixels.
		 * 
		 * Performing a checksum of a region is very time consuming, so use the
		 * smallest region you are able to reduce CPU load. On some machines a
		 * checksum of the whole screen could take many seconds!
		 * 
		 * A checksum only allows you to see if "something" has changed in a
		 * region - it does not tell you exactly what has changed.
		 * 
		 * When using a step value greater than 1 you must bear in mind that the
		 * checksumming becomes less reliable for small changes as not every
		 * pixel is checked.
		 * 
		 * @param rect
		 *            position and size of rectangle
		 * @param step
		 *            Instead of checksumming each pixel use a value larger than
		 *            1 to skip pixels (for speed). E.g. A value of 2 will only
		 *            check every other pixel. Default is 1.
		 * @return Returns the checksum value of the region.
		 */
		public int AU3_PixelChecksum(final RECT rect, final Integer step);

		/**
		 * Returns a pixel color according to x,y pixel coordinates.
		 * 
		 * @param x
		 *            x coordinate of pixel.
		 * @param y
		 *            y coordinate of pixel.
		 * @return Return decimal value of pixel's color if success, return -1
		 *         if invalid coordinates.
		 */
		public int AU3_PixelGetColor(int x, int y);

		/**
		 * Searches a rectangle of pixels for the pixel color provided.
		 * 
		 * @param rect
		 *            position and size of rectangle.
		 * @param color
		 *            Colour value of pixel to find (in decimal or hex).
		 * @param shadeVariation
		 *            A number between 0 and 255 to indicate the allowed number
		 *            of shades of variation of the red, green, and blue
		 *            components of the colour. Default is 0 (exact match).
		 * @param step
		 *            Instead of searching each pixel use a value larger than 1
		 *            to skip pixels (for speed). E.g. A value of 2 will only
		 *            check every other pixel. Default is 1.
		 * @param point
		 *            Return the pixel's coordinates if success, sets
		 *            oAutoIt.error to 1 if color is not found.
		 */
		public void AU3_PixelSearch(RECT rect, int color,
				Integer shadeVariation, Integer step, POINT point);

		/**
		 * Terminates a named process.
		 * 
		 * Process names are executables without the full path, e.g.,
		 * "notepad.exe" or "winword.exe" If multiple processes have the same
		 * name, the one with the highest PID is terminated--regardless of how
		 * recently the process was spawned. PID is the unique number which
		 * identifies a Process. A PID can be obtained through the ProcessExists
		 * or Run commands. In order to work under Windows NT 4.0, ProcessClose
		 * requires the file PSAPI.DLL (included in the AutoIt installation
		 * directory). The process is polled approximately every 250
		 * milliseconds.
		 * 
		 * @param process
		 *            The title or PID of the process to terminate.
		 * @return Returns 1 regardless of success/failure.
		 */
		public int AU3_ProcessClose(final WString process);

		/**
		 * Checks to see if a specified process exists.
		 * 
		 * Process names are executables without the full path, e.g.,
		 * "notepad.exe" or "winword.exe". PID is the unique number which
		 * identifies a Process. In order to work under Windows NT 4.0,
		 * ProcessExists requires the file PSAPI.DLL (included in the AutoIt
		 * installation directory). The process is polled approximately every
		 * 250 milliseconds.
		 * 
		 * @param process
		 *            The name or PID of the process to check.
		 * @return Returns the PID of the process if success, Returns 0 if
		 *         process does not exist.
		 */
		public int AU3_ProcessExists(final WString process);

		/**
		 * Changes the priority of a process.
		 * 
		 * Above Normal and Below Normal priority classes are not supported on
		 * Windows 95/98/ME. If you try to use them on those platforms, the
		 * function will fail and oAutoIt.error will be set to 2.
		 * 
		 * @param process
		 *            The name or PID of the process to check.
		 * @param priority
		 *            A flag which determines what priority to set
		 * 
		 *            0 - Idle/Low
		 * 
		 *            1 - Below Normal (Not supported on Windows 95/98/ME)
		 * 
		 *            2 - Normal
		 * 
		 *            3 - Above Normal (Not supported on Windows 95/98/ME)
		 * 
		 *            4 - High
		 * 
		 *            5 - Realtime (Use with caution, may make the system
		 *            unstable)
		 * @return Return 1 if success, return 0 and sets oAutoIt.error to 1.
		 *         May set oAutoIt.error to 2 if attempting to use an
		 *         unsupported priority class.
		 */
		public int AU3_ProcessSetPriority(final WString process,
				final int priority);

		/**
		 * Pauses script execution until a given process exists.
		 * 
		 * Process names are executables without the full path, e.g.,
		 * "notepad.exe" or "winword.exe" In order to work under Windows NT 4.0,
		 * ProcessWait requires the file PSAPI.DLL (included in the AutoIt
		 * installation directory). The process is polled approximately every
		 * 250 milliseconds.
		 * 
		 * This function is the only process function not to accept a PID.
		 * Because PIDs are allocated randomly, waiting for a particular PID to
		 * exist doesn't make sense.
		 * 
		 * @param process
		 *            The name of the process to check.
		 * @param timeout
		 *            Specifies how long to wait (default is to wait
		 *            indefinitely).
		 * @return Return 1 if success, return 0 if the wait timed out.
		 */
		public int AU3_ProcessWait(final WString process, final Integer timeout);

		/**
		 * Pauses script execution until a given process does not exist.
		 * 
		 * Process names are executables without the full path, e.g.,
		 * "notepad.exe" or "winword.exe" PID is the unique number which
		 * identifies a Process. A PID can be obtained through the ProcessExists
		 * or Run commands. In order to work under Windows NT 4.0,
		 * ProcessWaitClose requires the file PSAPI.DLL (included in the AutoIt
		 * installation directory). The process is polled approximately every
		 * 250 milliseconds.
		 * 
		 * @param process
		 *            The name or PID of the process to check.
		 * @param timeout
		 *            Specifies how long to wait (default is to wait
		 *            indefinitely).
		 * @return Return 1 if success, return 0 if the wait timed out.
		 */
		public int AU3_ProcessWaitClose(final WString process,
				final Integer timeout);

		/**
		 * Deletes a key from the registry.
		 * 
		 * A registry key must start with "HKEY_LOCAL_MACHINE" ("HKLM") or
		 * "HKEY_USERS" ("HKU") or "HKEY_CURRENT_USER" ("HKCU") or
		 * "HKEY_CLASSES_ROOT" ("HKCR") or "HKEY_CURRENT_CONFIG" ("HKCC").
		 * 
		 * Deleting from the registry is potentially dangerous--please exercise
		 * caution!
		 * 
		 * It is possible to access remote registries by using a keyname in the
		 * form "\\computername\keyname". To use this feature you must have the
		 * correct access rights on NT/2000/XP/2003, or if you are using a 9x
		 * based OS the remote PC must have the remote regsitry service
		 * installed first (See Microsoft Knowledge Base Article - 141460).
		 * 
		 * @param keyName
		 *            The registry key to delete.
		 * @return Return 1 if success, return 0 if the key dose not exist,
		 *         return 2 if error deleting key.
		 */
		public int AU3_RegDeleteKey(final WString keyName);

		/**
		 * Deletes a value from the registry.
		 * 
		 * A registry key must start with "HKEY_LOCAL_MACHINE" ("HKLM") or
		 * "HKEY_USERS" ("HKU") or "HKEY_CURRENT_USER" ("HKCU") or
		 * "HKEY_CLASSES_ROOT" ("HKCR") or "HKEY_CURRENT_CONFIG" ("HKCC").
		 * 
		 * To access the (Default) value use "" (a blank string) for the
		 * valuename.
		 * 
		 * Deleting from the registry is potentially dangerous--please exercise
		 * caution!
		 * 
		 * It is possible to access remote registries by using a keyname in the
		 * form "\\computername\keyname". To use this feature you must have the
		 * correct access rights on NT/2000/XP/2003, or if you are using a 9x
		 * based OS the remote PC must have the remote regsitry service
		 * installed first (See Microsoft Knowledge Base Article - 141460).
		 * 
		 * @param keyName
		 *            The registry key to write to.
		 * @param valueName
		 *            The value name to delete.
		 * @return Return 1 if success, return 0 if the key/value does not
		 *         exist, return 2 if error deleting key/value.
		 */
		public int AU3_RegDeleteVal(final WString keyName,
				final WString valueName);

		/**
		 * Reads the name of a subkey according to it's instance.
		 * 
		 * A registry key must start with "HKEY_LOCAL_MACHINE" ("HKLM") or
		 * "HKEY_USERS" ("HKU") or "HKEY_CURRENT_USER" ("HKCU") or
		 * "HKEY_CLASSES_ROOT" ("HKCR") or "HKEY_CURRENT_CONFIG" ("HKCC").
		 * 
		 * @param keyName
		 *            The registry key to read.
		 * @param instance
		 *            The 1-based key instance to retrieve.
		 * @param result
		 * @param bufSize
		 */
		public void AU3_RegEnumKey(final WString keyName, final int instance,
				final CharBuffer result, final int bufSize);

		/**
		 * Reads the name of a value according to it's instance.
		 * 
		 * A registry key must start with "HKEY_LOCAL_MACHINE" ("HKLM") or
		 * "HKEY_USERS" ("HKU") or "HKEY_CURRENT_USER" ("HKCU") or
		 * "HKEY_CLASSES_ROOT" ("HKCR") or "HKEY_CURRENT_CONFIG" ("HKCC").
		 * 
		 * @param keyName
		 *            The registry key to read.
		 * @param instance
		 *            The 1-based value instance to retrieve.
		 * @param result
		 * @param bufSize
		 */
		public void AU3_RegEnumVal(final WString keyName, final int instance,
				final CharBuffer result, final int bufSize);

		/**
		 * Reads a value from the registry.
		 * 
		 * A registry key must start with "HKEY_LOCAL_MACHINE" ("HKLM") or
		 * "HKEY_USERS" ("HKU") or "HKEY_CURRENT_USER" ("HKCU") or
		 * "HKEY_CLASSES_ROOT" ("HKCR") or "HKEY_CURRENT_CONFIG" ("HKCC").
		 * 
		 * AutoIt supports registry keys of type REG_BINARY, REG_SZ,
		 * REG_MULTI_SZ, REG_EXPAND_SZ, and REG_DWORD.
		 * 
		 * To access the (Default) value use "" (a blank string) for the
		 * valuename.
		 * 
		 * When reading a REG_BINARY key the result is a string of hex
		 * characters, e.g. the REG_BINARY value of 01,a9,ff,77 will be read as
		 * the string "01A9FF77".
		 * 
		 * When reading a REG_MULTI_SZ key the multiple entries are seperated by
		 * a linefeed character.
		 * 
		 * It is possible to access remote registries by using a keyname in the
		 * form "\\computername\keyname". To use this feature you must have the
		 * correct access rights on NT/2000/XP/2003, or if you are using a 9x
		 * based OS the remote PC must have the remote regsitry service
		 * installed first (See Microsoft Knowledge Base Article - 141460).
		 * 
		 * @param keyName
		 *            The registry key to read.
		 * @param valueName
		 *            The value to read.
		 * @param result
		 * @param bufSize
		 */
		public void AU3_RegRead(final WString keyName, final WString valueName,
				final CharBuffer result, final int bufSize);

		/**
		 * Creates a key or value in the registry.
		 * 
		 * A registry key must start with "HKEY_LOCAL_MACHINE" ("HKLM") or
		 * "HKEY_USERS" ("HKU") or "HKEY_CURRENT_USER" ("HKCU") or
		 * "HKEY_CLASSES_ROOT" ("HKCR") or "HKEY_CURRENT_CONFIG" ("HKCC").
		 * 
		 * AutoIt supports registry keys of type REG_BINARY, REG_SZ,
		 * REG_MULTI_SZ, REG_EXPAND_SZ, and REG_DWORD.
		 * 
		 * To access the (Default) value use "" (a blank string) for the
		 * valuename.
		 * 
		 * When writing a REG_BINARY key use a string of hex characters, e.g.
		 * the REG_BINARY value of 01,a9,ff,77 can be written by using the
		 * string "01A9FF77".
		 * 
		 * When writing a REG_MULTI_SZ key you must separate each value with
		 * 
		 * @LF. The value must NOT end with @LF and no "blank" entries are
		 *      allowed (see example).
		 * 
		 *      It is possible to access remote registries by using a keyname in
		 *      the form "\\computername\keyname". To use this feature you must
		 *      have the correct access rights on NT/2000/XP/2003, or if you are
		 *      using a 9x based OS the remote PC must have the remote regsitry
		 *      service installed first (See Microsoft Knowledge Base Article -
		 *      141460).
		 * 
		 * @param keyName
		 *            The registry key to write to. If no other parameters are
		 *            specified this key will simply be created.
		 * @param valueName
		 *            The valuename to write to.
		 * @param type
		 *            Type of key to write: "REG_SZ", "REG_MULTI_SZ",
		 *            "REG_EXPAND_SZ", "REG_DWORD", or "REG_BINARY".
		 * @param value
		 *            The value to write.
		 * @return Return 1 if success, return 0 if error writing registry key
		 *         or value.
		 */
		public int AU3_RegWrite(final WString keyName, final WString valueName,
				final WString type, final WString value);

		/**
		 * Runs an external program.
		 * 
		 * After running the requested program the script continues. To pause
		 * execution of the script until the spawned program has finished use
		 * the RunWait function instead.
		 * 
		 * The error property is set to 1 as an indication of failure.
		 * 
		 * @param fileName
		 *            The name of the executable (EXE, BAT, COM, or PIF) to run.
		 * @param workingDir
		 *            The working directory.
		 * @param showFlag
		 *            The "show" flag of the executed program:
		 * 
		 *            SW_HIDE = Hidden window
		 * 
		 *            SW_MINIMIZE = Minimized window
		 * 
		 *            SW_MAXIMIZE = Maximized window
		 * @return Return the PID of the process that was launched, the error
		 *         property is set to 1 as an indication of failure.
		 */
		public int AU3_Run(final WString fileName, final WString workingDir,
				final Integer showFlag);

		/**
		 * Runs an external program.
		 * 
		 * After running the requested program the script continues. To pause
		 * execution of the script until the spawned program has finished use
		 * the RunWait function instead.
		 * 
		 * The error property is set to 1 as an indication of failure.
		 * 
		 * @param user
		 *            The user name to use.
		 * @param domain
		 *            The domain name to use.
		 * @param password
		 *            The password to use.
		 * @param logonFlag
		 *            0 = do not load the user profile,<br/>
		 *            1 = (default) load the user profile,<br/>
		 *            2 = use for net credentials only
		 * @param fileName
		 *            The name of the executable (EXE, BAT, COM, or PIF) to run.
		 * @param workingDir
		 *            Optional: The working directory.
		 * @param showFlag
		 *            Optional: The "show" flag of the executed program:<br/>
		 *            SW_HIDE = Hidden window<br/>
		 *            SW_MINIMIZE = Minimized window<br/>
		 *            SW_MAXIMIZE = Maximized window
		 * @return Returns the PID of the process that was launched.
		 */
		public int AU3_RunAs(final WString user, final WString domain,
				final WString password, final int logonFlag,
				final WString fileName, final WString workingDir,
				final int showFlag);

		/**
		 * Runs an external program and pauses script execution until the
		 * program finishes.
		 * 
		 * After running the requested program the script pauses until the
		 * program terminates. To run a program and then immediately continue
		 * script execution use the Run function instead.
		 * 
		 * Some programs will appear to return immediately even though they are
		 * still running; these programs spawn another process - you may be able
		 * to use the ProcessWaitClose function to handle these cases.
		 * 
		 * The error property is set to 1 as an indication of failure.
		 * 
		 * @param user
		 *            The user name to use.
		 * @param domain
		 *            The domain name to use.
		 * @param password
		 *            The password to use.
		 * @param logonFlag
		 *            0 = do not load the user profile,<br/>
		 *            1 = (default) load the user profile,<br/>
		 *            2 = use for net credentials only
		 * @param program
		 *            The name of the executable (EXE, BAT, COM, or PIF) to run.
		 * @param dir
		 *            Optional: The working directory.
		 * @param showFlag
		 *            Optional: The "show" flag of the executed program:<br/>
		 *            SW_HIDE = Hidden window<br/>
		 *            SW_MINIMIZE = Minimized window<br/>
		 *            SW_MAXIMIZE = Maximized window
		 * @return Returns the exit code of the program that was run.
		 */
		public int AU3_RunAsWait(final WString user, final WString domain,
				final WString password, final int logonFlag,
				final WString program, final WString dir, final int showFlag);

		/**
		 * Runs an external program and pauses script execution until the
		 * program finishes.
		 * 
		 * After running the requested program the script pauses until the
		 * program terminates. To run a program and then immediately continue
		 * script execution use the Run function instead.
		 * 
		 * Some programs will appear to return immediately even though they are
		 * still running; these programs spawn another process - you may be able
		 * to use the ProcessWaitClose function to handle these cases.
		 * 
		 * The error property is set to 1 as an indication of failure.
		 * 
		 * @param fileName
		 *            The name of the executable (EXE, BAT, COM, or PIF) to run.
		 * @param workingDir
		 *            The working directory.
		 * @param showFlag
		 *            The "show" flag of the executed program:
		 * 
		 *            SW_HIDE = Hidden window
		 * 
		 *            SW_MINIMIZE = Minimized window
		 * 
		 *            SW_MAXIMIZE = Maximized window
		 * @return Return the exit code of the program that was run, the error
		 *         property is set to 1 as an indication of failure.
		 */
		public int AU3_RunWait(final WString fileName,
				final WString workingDir, final Integer showFlag);

		/**
		 * Sends simulated keystrokes to the active window.
		 * 
		 * See the Appendix for some tips.
		 * 
		 * The "Send" command syntax is similar to that of ScriptIt and the
		 * Visual Basic "SendKeys" command. Characters are sent as written with
		 * the exception of the following characters:
		 * 
		 * '!' This tells AutoIt to send an ALT keystroke, therefore
		 * Send("This is text!a") would send the keys "This is text" and then
		 * press "ALT+a".
		 * 
		 * N.B. Some programs are very choosy about capital letters and ALT
		 * keys, i.e. "!A" is different to "!a". The first says ALT+SHIFT+A, the
		 * second is ALT+a. If in doubt, use lowercase!
		 * 
		 * '+' This tells AutoIt to send a SHIFT keystroke, therefore
		 * Send("Hell+o") would send the text "HellO". Send("!+a") would send
		 * "ALT+SHIFT+a".
		 * 
		 * '^' This tells AutoIt to send a CONTROL keystroke, therefore
		 * Send("^!a") would send "CTRL+ALT+a".
		 * 
		 * N.B. Some programs are very choosy about capital letters and CTRL
		 * keys, i.e. "^A" is different to "^a". The first says CTRL+SHIFT+A,
		 * the second is CTRL+a. If in doubt, use lowercase!
		 * 
		 * '#' The hash now sends a Windows keystroke; therefore, Send("#r")
		 * would send Win+r which launches the Run dialog box.
		 * 
		 * You can set SendCapslockMode to make CAPS LOCK disabled at the start
		 * of a Send operation and restored upon completion. However, if a user
		 * is holding down the Shift key when a Send function begins, text may
		 * be sent in uppercase. One workaround is to
		 * Send("{SHIFTDOWN}{SHIFTUP}") before the other Send operations.
		 * 
		 * Certain special keys can be sent and should be enclosed in braces:
		 * 
		 * N.B. Windows does not allow the simulation of the "CTRL-ALT-DEL"
		 * combination!
		 * 
		 * To send the ASCII value A (same as pressing ALT+065 on the numeric
		 * keypad) Send("{ASC 65}")
		 * 
		 * Single keys can also be repeated, e.g. Send("{DEL 4}") ;Presses the
		 * DEL key 4 times Send("{S 30}") ;Sends 30 'S' characters Send("+{TAB
		 * 4}) ;Presses SHIFT+TAB 4 times
		 * 
		 * To hold a key down (generally only useful for games) Send("{a down}")
		 * ;Holds the A key down Send("{a up}") ;Releases the A key
		 * 
		 * To set the state of the capslock, numlock and scrolllock keys
		 * Send("{NumLock on}") ;Turns the NumLock key on Send("{CapsLock off}")
		 * ;Turns the CapsLock key off Send("{ScrollLock toggle}") ;Toggles the
		 * state of ScrollLock
		 * 
		 * If you with to use a variable for the count, try $n = 4 Send("+{TAB "
		 * & $n & "}")
		 * 
		 * If you wish to send the ASCII value A four times, then try $x =
		 * Chr(65) Send("{" & $x & " 4}")
		 * 
		 * Most laptop computer keyboards have a special Fn key. This key cannot
		 * be simulated.
		 * 
		 * Note, by setting the flag parameter to 1 the above "special"
		 * processing will be disabled. This is useful when you want to send
		 * some text copied from a variable and you want the text sent exactly
		 * as written.
		 * 
		 * For example, open Folder Options (in the control panel) and try the
		 * following:
		 * 
		 * Use Alt-key combos to access menu items. Also, open Notepad and try
		 * the following: Send("!f") Send Alt+f, the access key for Notepad's
		 * file menu. Try other letters!
		 * 
		 * See Windows' Help--press Win+F1--for a complete list of keyboard
		 * shortcuts if you don't know the importance of Alt+F4, PrintScreen,
		 * Ctrl+C, and so on.
		 * 
		 * @param text
		 *            The sequence of keys to send.
		 * @param flag
		 *            Changes how "keys" is processed: flag = 0 (default), Text
		 *            contains special characters like + and ! to indicate SHIFT
		 *            and ALT key presses. flag = 1, keys are sent raw.
		 */
		public void AU3_Send(final WString text, final Integer flag);

		/**
		 * Shuts down the system.
		 * 
		 * The shutdown code is a combination of the following values: 0 =
		 * Logoff 1 = Shutdown 2 = Reboot 4 = Force 8 = Power down
		 * 
		 * Add the required values together. To shutdown and power down, for
		 * example, the code would be 9 (shutdown + power down = 1 + 8 = 9).
		 * 
		 * Standby or hibernate can be achieved with third-party software such
		 * as http://grc.com/wizmo/wizmo.htm
		 * 
		 * @param code
		 *            A combination of shutdown codes.
		 * @return Return 1 if success, return 0 if failed.
		 */
		public int AU3_Shutdown(final int code);

		/**
		 * Pause script execution.
		 * 
		 * Maximum sleep time is 2147483647 milliseconds (24 days).
		 * 
		 * @param milliSeconds
		 *            Amount of time to pause (in milliseconds).
		 */
		public void AU3_Sleep(final int milliSeconds);

		/**
		 * Retrieves the text from a standard status bar control.
		 * 
		 * @param title
		 *            The title of the window to check.
		 * @param text
		 *            The text of the window to check.
		 * @param part
		 *            The "part" number of the status bar to read - the default
		 *            is 1. 1 is the first possible part and usually the one
		 *            that contains the useful messages like "Ready"
		 *            "Loading...", etc.
		 * @param statusText
		 * @param bufSize
		 */
		public void AU3_StatusbarGetText(final WString title,
				final WString text, final Integer part,
				final CharBuffer statusText, final int bufSize);

		/**
		 * Creates a tooltip anywhere on the screen.
		 * 
		 * If the x and y coordinates are omitted, the tip is placed near the
		 * mouse cursor. If the coords would cause the tooltip to run off
		 * screen, it is repositioned to visible. Tooltip appears until it is
		 * cleared, until script terminates, or sometimes until it is clicked
		 * upon. You may use a linefeed character to create multi-line tooltips.
		 * 
		 * @param text
		 *            The text of the tooltip. (An empty string clears a
		 *            displaying tooltip).
		 * @param x
		 *            The x position of the tooltip.
		 * @param y
		 *            The y position of the tooltip.
		 */
		public void AU3_ToolTip(final WString text, final Integer x,
				final Integer y);

		/**
		 * Activates (gives focus to) a window.
		 * 
		 * You can use the WinActive function to check if WinActivate succeeded.
		 * If multiple windows match the criteria, the window that was most
		 * recently active is the one activated. WinActivate works on minimized
		 * windows. However, a window that is "Always On Top" could still cover
		 * up a window you Activated.
		 * 
		 * @param title
		 *            The title of the window to activate.
		 * @param text
		 *            The text of the window to activate.
		 */
		public void AU3_WinActivate(final WString title, final WString text);

		/**
		 * Checks to see if a specified window exists and is currently active.
		 * 
		 * @param title
		 *            The title of the window to check.
		 * @param text
		 *            The text of the window to check.
		 * @return Returns 1 if window is active, otherwise returns 0.
		 */
		public int AU3_WinActive(final WString title, final WString text);

		/**
		 * Closes a window.
		 * 
		 * This function sends a close message to a window, the result depends
		 * on the window (it may ask to save data, etc.). To force a window to
		 * close, use the WinKill function. If multiple windows match the
		 * criteria, the window that was most recently active is closed.
		 * 
		 * @param title
		 *            The title of the window to close.
		 * @param text
		 *            The text of the window to close.
		 * @return Returns 1 if success, returns 0 if window is not found.
		 */
		public int AU3_WinClose(final WString title, final WString text);

		/**
		 * Checks to see if a specified window exists.
		 * 
		 * @param title
		 *            The title of the window to check.
		 * @param text
		 *            The text of the window to check.
		 * @return Returns 1 if the window exists, otherwise returns 0. WinExist
		 *         will return 1 even if a window is hidden.
		 */
		public int AU3_WinExists(final WString title, final WString text);

		/**
		 * Returns the coordinates of the caret in the foreground window.
		 * 
		 * WinGetCaretPos might not return accurate values for Multiple Document
		 * Interface (MDI) applications if absolute CaretCoordMode is used. See
		 * example for a workaround. Note: Some applications report static
		 * coordinates regardless of caret position!
		 * 
		 * @param lpPoint
		 */
		public void AU3_WinGetCaretPos(POINT lpPoint);

		/**
		 * Retrieves the classes from a window.
		 * 
		 * Class names are linefeed separated. WinGetClassList works on both
		 * minimized and hidden windows. Up to 64KB of text can be retrieved. If
		 * multiple windows match the criteria, the classes are read from the
		 * most recently active window.
		 * 
		 * @param title
		 *            The title of the window to read.
		 * @param text
		 *            The text of the window to read.
		 * @param retText
		 * @param bufSize
		 */
		public void AU3_WinGetClassList(final WString title,
				final WString text, final CharBuffer retText, int bufSize);

		/**
		 * Retrieves the size of a given window's client area.
		 * 
		 * If the window is minimized, the returned width and height values are
		 * both zero. However, WinGetClientSize works correctly on
		 * (non-minimized) hidden windows. If the window title "Program Manager"
		 * is used, the function will return the size of the desktop.
		 * WinGetClientSize("") matches the active window. If multiple windows
		 * match the criteria, the most recently active window is used.
		 * 
		 * @param title
		 *            The title of the window to read.
		 * @param text
		 *            The text of the window to read.
		 * @param rect
		 */
		public void AU3_WinGetClientSize(final WString title,
				final WString text, RECT rect);

		/**
		 * Retrieves the internal handle of a window.
		 * 
		 * This function is for use with the advanced WinTitleMatchMode options
		 * that allow you to use classnames and handles to specify windows
		 * rather than "title" and "text". Once you have obtained the handle you
		 * can access the required window even if its title changes.
		 * 
		 * @param title
		 *            The title of the window to read.
		 * @param text
		 *            The text of the window to read.
		 * @return Returns the internal handle of a window if success, returns
		 *         null if no window matches the criteria.
		 */
		public HWND AU3_WinGetHandle(final WString title, final WString text);

		/**
		 * Retrieves the position and size of a given window.
		 * 
		 * WinGetPos returns negative numbers such as -32000 for minimized
		 * windows, but works fine with (non-minimized) hidden windows. If the
		 * window title "Program Manager" is used, the function will return the
		 * size of the desktop. If multiple windows match the criteria, the most
		 * recently active window is used.
		 * 
		 * @param title
		 *            The title of the window to read.
		 * @param text
		 *            The text of the window to read.
		 * @param rect
		 */
		public void AU3_WinGetPos(final WString title, final WString text,
				RECT rect);

		/**
		 * Retrieves the Process ID (PID) associated with a window.
		 * 
		 * @param title
		 *            The title of the window to read.
		 * @param text
		 *            The text of the window to read.
		 * @return Returns the Process ID (PID) associated with the window if
		 *         success, returns -1 if no window matches the criteria.
		 */
		public int AU3_WinGetProcess(final WString title, final WString text);

		/**
		 * Retrieves the state of a given window.
		 * 
		 * @param title
		 *            The title of the window to read.
		 * @param text
		 *            The text of the window to read.
		 * @return If success, returns a value indicating the state of the
		 *         window. Multiple values are added together so use BitAND() to
		 *         examine the part you are interested in: 1 = Window
		 *         exists</br> 2 = Window is visible</br> 4 = Windows is
		 *         enabled</br> 8 = Window is active</br> 16 = Window is
		 *         minimized</br> 32 = Windows is maximized</br> Returns 0 and
		 *         sets oAutoIt.error to 1 if the window is not found.
		 */
		public int AU3_WinGetState(final WString title, final WString text);

		/**
		 * Retrieves the text from a window.
		 * 
		 * Up to 64KB of window text can be retrieved. WinGetText works on
		 * minimized windows, but only works on hidden windows if you've set
		 * AutoItSetOption("WinDetectHiddenText", 1) If multiple windows match
		 * the criteria for WinGetText, the information for the most recently
		 * active match is returned. Use WinGetText("") to get the active
		 * window's text.
		 * 
		 * @param title
		 *            The title of the window to read.
		 * @param text
		 *            The text of the window to read.
		 * @param retText
		 * @param bufSize
		 */
		public void AU3_WinGetText(final WString title, final WString text,
				final CharBuffer retText, final int bufSize);

		/**
		 * Retrieves the full title from a window.
		 * 
		 * WinGetTitle("") returns the active window's title. WinGetTitle works
		 * on both minimized and hidden windows. If multiple windows match the
		 * criteria, the most recently active window is used.
		 * 
		 * @param title
		 *            The title of the window to read.
		 * @param text
		 *            The text of the window to read.
		 * @param retText
		 * @param bufSize
		 */
		public void AU3_WinGetTitle(final WString title, final WString text,
				final CharBuffer retText, final int bufSize);

		/**
		 * Forces a window to close.
		 * 
		 * The difference between this function and WinClose is that WinKill
		 * will forcibly terminate the window if it doesn't close quickly
		 * enough. Consequently, a user might not have time to respond to
		 * dialogs prompting the user to save data. Although WinKill can work on
		 * both minimized and hidden windows, some windows (notably explorer
		 * windows) can only be terminated using WinClose.
		 * 
		 * @param title
		 *            The title of the window to close.
		 * @param text
		 *            The text of the window to close.
		 * @return Returns 1 if success, returns 0 if window is not found.
		 */
		public int AU3_WinKill(final WString title, final WString text);

		/**
		 * Invokes a menu item of a window.
		 * 
		 * You should note that underlined menu items actually contain a &
		 * character to indicate the underlining. Thus, the menu item
		 * <bold><u>File</u></bold> would actually require the text "&File", and
		 * <bold>Con<u>v</u>ert</bold> would require "Con&vert" You can access
		 * menu items up to six levels deep; and the window can be inactive,
		 * minimized, and/or even hidden.
		 * 
		 * WinMenuSelectItem will only work on standard menus. Unfortunately,
		 * many menus in use today are actually custom written or toolbars
		 * "pretending" to be menus. This is true for most Microsoft
		 * applications.
		 * 
		 * @param title
		 *            The title of the window to read.
		 * @param text
		 *            The text of the window to read.
		 * @param item1
		 *            Text of Menu Item
		 * @param item2
		 *            Text of SubMenu item
		 * @param item3
		 *            Text of SubMenu item
		 * @param item4
		 *            Text of SubMenu item
		 * @param item5
		 *            Text of SubMenu item
		 * @param item6
		 *            Text of SubMenu item
		 * @param item7
		 *            Text of SubMenu item
		 * @param item8
		 *            Text of SubMenu item
		 * @return Returns 1 if success, return 0 if the menu could not be
		 *         found.
		 */
		public int AU3_WinMenuSelectItem(final WString title,
				final WString text, final WString item1, final WString item2,
				final WString item3, final WString item4, final WString item5,
				final WString item6, final WString item7, final WString item8);

		/**
		 * Minimizes all windows.
		 */
		public void AU3_WinMinimizeAll();

		/**
		 * Undoes a previous WinMinimizeAll function.
		 * 
		 * Send("#+m") is a possible alternative.
		 */
		public void AU3_WinMinimizeAllUndo();

		/**
		 * Moves and/or resizes a window.
		 * 
		 * WinMove has no effect on minimized windows, but WinMove works on
		 * hidden windows. If very width and height are small (or negative), the
		 * window will go no smaller than 112 x 27 pixels. If width and height
		 * are large, the window will go no larger than approximately
		 * [12+@DesktopWidth] x [12+@DesktopHeight] pixels. Negative values are
		 * allowed for the x and y coordinates. In fact, you can move a window
		 * off screen; and if the window's program is one that remembers its
		 * last window position, the window will appear in the corner (but fully
		 * on-screen) the next time you launch the program. If multiple windows
		 * match the criteria, the most recently active window is used.
		 * 
		 * @param title
		 *            The title of the window to move/resize.
		 * @param text
		 *            The text of the window to move/resize.
		 * @param x
		 *            X coordinate to move to.
		 * @param y
		 *            Y coordinate to move to.
		 * @param width
		 *            New width of the window.
		 * @param height
		 *            New height of the window.
		 * @return Returns 1 if success, returns 0 if window is not found.
		 */
		public int AU3_WinMove(final WString title, final WString text,
				final int x, final int y, final Integer width,
				final Integer height);

		/**
		 * Change a window's "Always On Top" attribute.
		 * 
		 * @param title
		 *            The title of the window to affect.
		 * @param text
		 *            The text of the window to affect.
		 * @param flag
		 *            Determines whether the window should have the "TOPMOST"
		 *            flag set. 1=set on top flag, 0 = remove on top flag
		 * @return Returns 1 if success, returns 0 if window is not found.
		 */
		public int AU3_WinSetOnTop(final WString title, final WString text,
				final int flag);

		/**
		 * Shows, hides, minimizes, maximizes, or restores a window.
		 * 
		 * WinSetState is a replacement for the badly named WinShow function.
		 * WinShow is accepted as an alias but this may be withdrawn in the
		 * future.
		 * 
		 * If multiple windows match the criteria, the most recently active
		 * window is used. SW_MINIMIZE and SW_MAXIMIZE even work on modal dialog
		 * windows.
		 * 
		 * @param title
		 *            The title of the window to show.
		 * @param text
		 *            The text of the window to show.
		 * @param flags
		 *            The "show" flag of the executed program:
		 * 
		 *            SW_HIDE = Hide window
		 * 
		 *            SW_SHOW = Shows a previously hidden window
		 * 
		 *            SW_MINIMIZE = Minimize window
		 * 
		 *            SW_MAXIMIZE = Maximize window
		 * 
		 *            SW_RESTORE = Undoes a window minimization or maximization
		 * @return Returns 1 if success, returns 0 if window is not found.
		 */
		public int AU3_WinSetState(final WString title, final WString text,
				final int flags);

		/**
		 * Changes the title of a window.
		 * 
		 * If multiple windows match the criteria the title of most recently
		 * active window is changed.
		 * 
		 * @param title
		 *            The title of the window to change.
		 * @param text
		 *            The text of the window to change.
		 * @param newTitle
		 *            The new title to give to the window.
		 * @return Returns 1 if success, returns 0 if window is not found.
		 */
		public int AU3_WinSetTitle(final WString title, final WString text,
				final WString newTitle);

		/**
		 * Sets the transparency of a window.
		 * 
		 * Requires Windows 2000/XP or later. Screen color must be greater or
		 * equal to 16-bit.
		 * 
		 * @param title
		 *            The title of the window to change.
		 * @param text
		 *            The text of the window to change.
		 * @param transparency
		 *            A number in the range 0 - 255. The larger the number, the
		 *            more transparent the window will become.
		 * @return Returns non-zero if success, returns 0 if failure,
		 *         oAutoIt.error will be set to 1 if the function isn't
		 *         supported on an OS.
		 */
		public int AU3_WinSetTrans(final WString title, final WString text,
				final int transparency);

		/**
		 * Pauses execution of the script until the requested window exists.
		 * 
		 * The script polls for window match every 250 milliseconds or so.
		 * 
		 * @param title
		 *            The title of the window to check.
		 * @param text
		 *            The text of the window to check.
		 * @param timeout
		 *            Timeout in seconds
		 * @return Returns 1 if success, returns 0 if timeout occurred.
		 */
		public int AU3_WinWait(final WString title, final WString text,
				final Integer timeout);

		/**
		 * Pauses execution of the script until the requested window is active.
		 * 
		 * AutoIt polls for a window match every 250 milliseconds or so.
		 * 
		 * @param title
		 *            The title of the window to check.
		 * @return Returns 1 if success, returns 0 if timeout occurred.
		 */
		public int AU3_WinWaitActive(final WString title);

		/**
		 * Pauses execution of the script until the requested window is active.
		 * 
		 * AutoIt polls for a window match every 250 milliseconds or so.
		 * 
		 * @param title
		 *            The title of the window to check.
		 * @param text
		 *            The text of the window to check.
		 * @return Returns 1 if success, returns 0 if timeout occurred.
		 */
		public int AU3_WinWaitActive(final WString title, final WString text);

		/**
		 * Pauses execution of the script until the requested window is active.
		 * 
		 * AutoIt polls for a window match every 250 milliseconds or so.
		 * 
		 * @param title
		 *            The title of the window to check.
		 * @param text
		 *            The text of the window to check.
		 * @param timeout
		 *            Timeout in seconds
		 * @return Returns 1 if success, returns 0 if timeout occurred.
		 */
		public int AU3_WinWaitActive(final WString title, final WString text,
				final Integer timeout);

		/**
		 * Pauses execution of the script until the requested window does not
		 * exist.
		 * 
		 * If the window already doesn't exist when this function is called it
		 * will return 0 immediately. The window is polled every 250
		 * milliseconds or so.
		 * 
		 * @param title
		 *            The title of the window to check.
		 * @return Returns 1 if success, returns 0 if timeout occurred.
		 */
		public int AU3_WinWaitClose(final WString title);

		/**
		 * Pauses execution of the script until the requested window does not
		 * exist.
		 * 
		 * If the window already doesn't exist when this function is called it
		 * will return 0 immediately. The window is polled every 250
		 * milliseconds or so.
		 * 
		 * @param title
		 *            The title of the window to check.
		 * @param text
		 *            The text of the window to check.
		 * @return Returns 1 if success, returns 0 if timeout occurred.
		 */
		public int AU3_WinWaitClose(final WString title, final WString text);

		/**
		 * Pauses execution of the script until the requested window does not
		 * exist.
		 * 
		 * If the window already doesn't exist when this function is called it
		 * will return 0 immediately. The window is polled every 250
		 * milliseconds or so.
		 * 
		 * @param title
		 *            The title of the window to check.
		 * @param text
		 *            The text of the window to check.
		 * @param timeout
		 *            Timeout in seconds
		 * @return Returns 1 if success, returns 0 if timeout occurred.
		 */
		public int AU3_WinWaitClose(final WString title, final WString text,
				final Integer timeout);

		/**
		 * Pauses execution of the script until the requested window is not
		 * active.
		 * 
		 * If the window already not active when this function is called it will
		 * return 0 immediately. The script polls for a window match every 250
		 * milliseconds or so.
		 * 
		 * @param title
		 *            The title of the window to check.
		 * @return Returns 0 if the timeout occurred, otherwise returns 1.
		 */
		public int AU3_WinWaitNotActive(final WString title);

		/**
		 * Pauses execution of the script until the requested window is not
		 * active.
		 * 
		 * If the window already not active when this function is called it will
		 * return 0 immediately. The script polls for a window match every 250
		 * milliseconds or so.
		 * 
		 * @param title
		 *            The title of the window to check.
		 * @param text
		 *            The text of the window to check.
		 * @return Returns 0 if the timeout occurred, otherwise returns 1.
		 */
		public int AU3_WinWaitNotActive(final WString title, final WString text);

		/**
		 * Pauses execution of the script until the requested window is not
		 * active.
		 * 
		 * If the window already not active when this function is called it will
		 * return 0 immediately. The script polls for a window match every 250
		 * milliseconds or so.
		 * 
		 * @param title
		 *            The title of the window to check.
		 * @param text
		 *            The text of the window to check.
		 * @param timeout
		 *            Timeout in seconds
		 * @return Returns 0 if the timeout occurred, otherwise returns 1.
		 */
		public int AU3_WinWaitNotActive(final WString title,
				final WString text, final Integer timeout);

		/**
		 * Checks to see if a specified window exists.
		 * 
		 * @param hWnd
		 *            The handle the window to check.
		 * @return Returns 1 if the window exists, otherwise returns 0. WinExist
		 *         will return 1 even if a window is hidden.
		 */
		public int AU3_WinExistsByHandle(final HWND hWnd);

		/**
		 * Retrieves the full title from a window.
		 * 
		 * WinGetTitle("") returns the active window's title. WinGetTitle works
		 * on both minimized and hidden windows. If multiple windows match the
		 * criteria, the most recently active window is used.
		 * 
		 * @param hWnd
		 *            The handle of the window to read.
		 * @param retText
		 * @param bufSize
		 */
		public void AU3_WinGetTitleByHandle(final HWND hWnd,
				final CharBuffer retText, final int bufSize);
	}
}
