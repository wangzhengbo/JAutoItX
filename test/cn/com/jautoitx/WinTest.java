package cn.com.jautoitx;

import java.awt.Button;
import java.awt.Frame;
import java.awt.Toolkit;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import cn.com.jautoitx.Win.WinState;

import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef.HWND;
import com.sun.jna.platform.win32.WinUser;

public class WinTest extends BaseTest {
	@Test
	public void activate() {
		Assert.assertFalse(Win.activate((HWND) null));

		// run notepad
		int pid = runNotepad();

		HWND hWnd = Win.getHandle_(NOTEPAD_TITLE);
		Assert.assertNotNull(hWnd);

		// minimize notepad
		Assert.assertTrue(Win.minimize(NOTEPAD_TITLE));
		Assert.assertFalse(Win.active(NOTEPAD_TITLE));

		// restore notepad
		Assert.assertTrue(Win.restore(NOTEPAD_TITLE));
		Assert.assertFalse(Win.active(NOTEPAD_TITLE));

		// activate notepad
		Assert.assertTrue(Win.activate(NOTEPAD_TITLE));
		Assert.assertTrue(Win.active(NOTEPAD_TITLE));

		// minimize notepad
		Assert.assertTrue(Win.minimize(NOTEPAD_TITLE));
		Assert.assertFalse(Win.active(NOTEPAD_TITLE));

		// activate notepad
		Assert.assertTrue(Win.activate(hWnd));
		Assert.assertTrue(Win.active(NOTEPAD_TITLE));

		// close notepad
		closeNotepad(pid);

		Assert.assertFalse(Win.activate(NOTEPAD_TITLE));
		Assert.assertFalse(Win.activate(hWnd));
	}

	@Test
	public void active() {
		Assert.assertFalse(Win.active(NOTEPAD_TITLE));
		Assert.assertFalse(Win.active((HWND) null));

		// run notepad
		int pid = runNotepad();

		HWND hWnd = Win.getHandle_(NOTEPAD_TITLE);
		Assert.assertNotNull(hWnd);

		// maximize notepad
		Assert.assertTrue(Win.maximize(NOTEPAD_TITLE));
		Assert.assertTrue(Win.active(NOTEPAD_TITLE));
		Assert.assertTrue(Win.active(hWnd));

		// restore notepad
		Assert.assertTrue(Win.restore(NOTEPAD_TITLE));
		Assert.assertTrue(Win.active(NOTEPAD_TITLE));
		Assert.assertTrue(Win.active(hWnd));

		// minimize notepad
		Assert.assertTrue(Win.minimize(NOTEPAD_TITLE));
		Assert.assertFalse(Win.active(NOTEPAD_TITLE));
		Assert.assertFalse(Win.active(hWnd));

		// restore notepad
		Assert.assertTrue(Win.restore(NOTEPAD_TITLE));
		Assert.assertFalse(Win.active(NOTEPAD_TITLE));
		Assert.assertFalse(Win.active(hWnd));

		// activate notepad
		Win.activate(NOTEPAD_TITLE);
		Assert.assertTrue(Win.active(NOTEPAD_TITLE));
		Assert.assertTrue(Win.active(hWnd));

		// hide notepad
		Assert.assertTrue(Win.hide(NOTEPAD_TITLE));
		Assert.assertTrue(Win.active(NOTEPAD_TITLE));
		Assert.assertTrue(Win.active(hWnd));

		// close notepad
		closeNotepad(pid);

		Assert.assertFalse(Win.active(NOTEPAD_TITLE));
		Assert.assertFalse(Win.active(hWnd));
	}

	@Test
	public void close() {
		Assert.assertFalse(Win.close(NOTEPAD_TITLE));
		Assert.assertFalse(Win.close((HWND) null));

		// run notepad
		runNotepad();

		// close notepad
		Assert.assertTrue(Win.close(NOTEPAD_TITLE));
		Assert.assertFalse(Win.exists(NOTEPAD_TITLE));

		// run notepad
		runNotepad();

		// close notepad
		HWND hWnd = Win.getHandle_(NOTEPAD_TITLE);
		Assert.assertNotNull(hWnd);
		Assert.assertTrue(Win.close(hWnd));
		Assert.assertFalse(Win.exists(NOTEPAD_TITLE));

		// run notepad
		runNotepad();

		Keyboard.send("1");

		// close notepad
		Assert.assertTrue(Win.close(NOTEPAD_TITLE));
		Assert.assertTrue(Win.exists(NOTEPAD_TITLE));

		// kill notepad
		Assert.assertTrue(Win.kill(NOTEPAD_TITLE));
		Assert.assertFalse(Win.exists(NOTEPAD_TITLE));

		// run notepad
		runNotepad();

		Keyboard.send("1");
		hWnd = Win.getHandle_(NOTEPAD_TITLE);
		Assert.assertNotNull(hWnd);

		// close notepad
		Assert.assertTrue(Win.close(hWnd));
		Assert.assertTrue(Win.exists(NOTEPAD_TITLE));

		// kill notepad
		Assert.assertTrue(Win.kill(NOTEPAD_TITLE));
		Assert.assertFalse(Win.exists(NOTEPAD_TITLE));

		Assert.assertFalse(Win.close(NOTEPAD_TITLE));
		Assert.assertFalse(Win.close(hWnd));
	}

	@Test
	public void exists() {
		Assert.assertFalse(Win.exists(NOTEPAD_TITLE));
		Assert.assertFalse(Win.exists((HWND) null));

		// run notepad
		int pid = runNotepad();
		HWND hWnd = Win.getHandle_(NOTEPAD_TITLE);
		Assert.assertNotNull(hWnd);
		Assert.assertTrue(Win.exists(NOTEPAD_TITLE));
		Assert.assertTrue(Win.exists(hWnd));

		// hide notepad
		Assert.assertTrue(Win.hide(NOTEPAD_TITLE));
		Assert.assertTrue(Win.exists(NOTEPAD_TITLE));
		Assert.assertTrue(Win.exists(hWnd));

		// show notepad
		Assert.assertTrue(Win.show(NOTEPAD_TITLE));
		Assert.assertTrue(Win.exists(NOTEPAD_TITLE));
		Assert.assertTrue(Win.exists(hWnd));

		// minimize notepad
		Assert.assertTrue(Win.minimize(NOTEPAD_TITLE));
		Assert.assertTrue(Win.exists(NOTEPAD_TITLE));
		Assert.assertTrue(Win.exists(hWnd));

		// close notepad
		closeNotepad(pid);

		Assert.assertFalse(Win.exists(NOTEPAD_TITLE));
		Assert.assertFalse(Win.exists(hWnd));
	}

	@Test
	public void getActiveTitle() {
		Assert.assertNotEquals(NOTEPAD_TITLE, Win.getActiveTitle());

		// run notepad
		int pid = runNotepad();
		Assert.assertEquals(NOTEPAD_TITLE, Win.getActiveTitle());

		// close notepad
		closeNotepad(pid);
		Assert.assertNotEquals(NOTEPAD_TITLE, Win.getActiveTitle());
	}

	@Test
	public void getCaretPosX() {
		// run notepad
		int pid = runNotepad();

		int caretPosX1 = Win.getCaretPosX();
		Keyboard.send("1");
		int caretPosX2 = Win.getCaretPosX();
		Keyboard.send("2");
		int caretPosX3 = Win.getCaretPosX();
		Keyboard.send("{BACKSPACE}");
		int caretPosX4 = Win.getCaretPosX();
		Keyboard.send("{BACKSPACE}");
		int caretPosX5 = Win.getCaretPosX();
		Keyboard.send("{ENTER}");
		int caretPosX6 = Win.getCaretPosX();
		Keyboard.send("{BACKSPACE}");
		int caretPosX7 = Win.getCaretPosX();
		Assert.assertTrue(caretPosX2 > caretPosX1);
		Assert.assertTrue(caretPosX3 > caretPosX2);
		assertEquals(caretPosX3 - caretPosX2, caretPosX2 - caretPosX1);
		assertEquals(caretPosX4, caretPosX2);
		assertEquals(caretPosX5, caretPosX1);
		assertEquals(caretPosX6, caretPosX1);
		assertEquals(caretPosX7, caretPosX1);

		// close notepad
		closeNotepad(pid);
	}

	@Test
	public void getCaretPosY() {
		// run notepad
		int pid = runNotepad();

		int caretPosY1 = Win.getCaretPosY();
		Keyboard.send("1");
		int caretPosY2 = Win.getCaretPosY();
		Keyboard.send("2");
		int caretPosY3 = Win.getCaretPosY();
		Keyboard.send("{ENTER}");
		int caretPosY4 = Win.getCaretPosY();
		Keyboard.send("{ENTER}");
		int caretPosY5 = Win.getCaretPosY();
		Keyboard.send("{BACKSPACE}");
		int caretPosY6 = Win.getCaretPosY();
		Keyboard.send("{BACKSPACE}");
		int caretPosY7 = Win.getCaretPosY();
		assertEquals(caretPosY1, caretPosY2);
		assertEquals(caretPosY1, caretPosY3);
		Assert.assertTrue(caretPosY4 > caretPosY3);
		Assert.assertTrue(caretPosY5 > caretPosY4);
		assertEquals(caretPosY5 - caretPosY4, caretPosY4 - caretPosY3);
		assertEquals(caretPosY6, caretPosY4);
		assertEquals(caretPosY7, caretPosY3);
		Keyboard.send("{BACKSPACE}");
		assertEquals(caretPosY1, Win.getCaretPosY());
		Keyboard.send("{BACKSPACE}");
		assertEquals(caretPosY1, Win.getCaretPosY());

		// close notepad
		closeNotepad(pid);
	}

	@Test
	public void getClassList() {
		Assert.assertNull(Win.getClassList(NOTEPAD_TITLE));
		Assert.assertNull(Win.getClassList((HWND) null));

		// run notepad
		int pid = runNotepad();

		HWND hWnd = Win.getHandle_(NOTEPAD_TITLE);
		Assert.assertNotNull(hWnd);

		assertEquals(NOTEPAAD_CLASS_LIST, Win.getClassList(NOTEPAD_TITLE));
		assertEquals(NOTEPAAD_CLASS_LIST, Win.getClassList(hWnd));

		// hide notepad
		Assert.assertTrue(Win.hide(NOTEPAD_TITLE));
		assertEquals(NOTEPAAD_CLASS_LIST, Win.getClassList(NOTEPAD_TITLE));
		assertEquals(NOTEPAAD_CLASS_LIST, Win.getClassList(hWnd));

		// show notepad
		Assert.assertTrue(Win.show(NOTEPAD_TITLE));
		assertEquals(NOTEPAAD_CLASS_LIST, Win.getClassList(NOTEPAD_TITLE));
		assertEquals(NOTEPAAD_CLASS_LIST, Win.getClassList(hWnd));

		// minimize notepad
		Assert.assertTrue(Win.minimize(NOTEPAD_TITLE));
		assertEquals(NOTEPAAD_CLASS_LIST, Win.getClassList(NOTEPAD_TITLE));
		assertEquals(NOTEPAAD_CLASS_LIST, Win.getClassList(hWnd));

		// close notepad
		closeNotepad(pid);

		Assert.assertNull(Win.getClassList(NOTEPAD_TITLE));
		Assert.assertNull(Win.getClassList(hWnd));
	}

	@Test
	public void getClassList_() {
		Assert.assertNull(Win.getClassList_(NOTEPAD_TITLE));
		Assert.assertNull(Win.getClassList_((HWND) null));

		// run notepad
		int pid = runNotepad();

		HWND hWnd = Win.getHandle_(NOTEPAD_TITLE);
		Assert.assertNotNull(hWnd);

		List<String> classList = Win.getClassList_(NOTEPAD_TITLE);
		assertEquals(2, classList.size());
		assertEquals("Edit", classList.get(0));
		assertEquals("msctls_statusbar32", classList.get(1));

		classList = Win.getClassList_(hWnd);
		assertEquals(2, classList.size());
		assertEquals("Edit", classList.get(0));
		assertEquals("msctls_statusbar32", classList.get(1));

		// hide notepad
		Assert.assertTrue(Win.hide(NOTEPAD_TITLE));
		classList = Win.getClassList_(NOTEPAD_TITLE);
		assertEquals(2, classList.size());
		assertEquals("Edit", classList.get(0));
		assertEquals("msctls_statusbar32", classList.get(1));

		classList = Win.getClassList_(hWnd);
		assertEquals(2, classList.size());
		assertEquals("Edit", classList.get(0));
		assertEquals("msctls_statusbar32", classList.get(1));

		// show notepad
		Assert.assertTrue(Win.show(NOTEPAD_TITLE));
		classList = Win.getClassList_(NOTEPAD_TITLE);
		assertEquals(2, classList.size());
		assertEquals("Edit", classList.get(0));
		assertEquals("msctls_statusbar32", classList.get(1));

		classList = Win.getClassList_(hWnd);
		assertEquals(2, classList.size());
		assertEquals("Edit", classList.get(0));
		assertEquals("msctls_statusbar32", classList.get(1));

		// minimize notepad
		Assert.assertTrue(Win.minimize(NOTEPAD_TITLE));
		classList = Win.getClassList_(NOTEPAD_TITLE);
		assertEquals(2, classList.size());
		assertEquals("Edit", classList.get(0));
		assertEquals("msctls_statusbar32", classList.get(1));

		classList = Win.getClassList_(hWnd);
		assertEquals(2, classList.size());
		assertEquals("Edit", classList.get(0));
		assertEquals("msctls_statusbar32", classList.get(1));

		// close notepad
		closeNotepad(pid);

		Assert.assertNull(Win.getClassList(NOTEPAD_TITLE));
		Assert.assertNull(Win.getClassList_(hWnd));
	}

	@Test
	public void getClassName() {
		Assert.assertNull(Win.getClassName(NOTEPAD_TITLE));
		Assert.assertNull(Win.getClassName((HWND) null));

		// run notepad
		int pid = runNotepad();

		HWND hWnd = Win.getHandle_(NOTEPAD_TITLE);
		Assert.assertNotNull(hWnd);

		Assert.assertEquals(NOTEPAAD_CLASS_NAME,
				Win.getClassName(NOTEPAD_TITLE));
		Assert.assertEquals(NOTEPAAD_CLASS_NAME, Win.getClassName(hWnd));

		// close notepad
		closeNotepad(pid);

		Assert.assertNull(Win.getClassName(NOTEPAD_TITLE));
		Assert.assertNull(Win.getClassName(hWnd));
	}

	@Test
	public void getClientHeight() {
		// run notepad
		int pid = runNotepad();

		int height = Win.getHeight(NOTEPAD_TITLE);
		Assert.assertTrue(height > 1);
		int clientHeight = Win.getClientHeight(NOTEPAD_TITLE);
		Assert.assertTrue(clientHeight > 1);
		Assert.assertTrue(clientHeight < height);

		// change notepad's height
		Win.move(NOTEPAD_TITLE, Win.getPosX(NOTEPAD_TITLE),
				Win.getPosY(NOTEPAD_TITLE), Win.getWidth(NOTEPAD_TITLE), 200);
		assertEquals(200 + clientHeight - height,
				Win.getClientHeight(NOTEPAD_TITLE));

		Win.move(NOTEPAD_TITLE, Win.getPosX(NOTEPAD_TITLE),
				Win.getPosY(NOTEPAD_TITLE), Win.getWidth(NOTEPAD_TITLE), 150);
		assertEquals(150 + clientHeight - height,
				Win.getClientHeight(NOTEPAD_TITLE));

		// restore notepad's height
		Assert.assertTrue(Win.move(NOTEPAD_TITLE, Win.getPosX(NOTEPAD_TITLE),
				Win.getPosY(NOTEPAD_TITLE), Win.getWidth(NOTEPAD_TITLE), height));
		assertEquals(clientHeight, Win.getClientHeight(NOTEPAD_TITLE));

		// hide notepad
		Assert.assertTrue(Win.hide(NOTEPAD_TITLE));
		assertEquals(clientHeight, Win.getClientHeight(NOTEPAD_TITLE));

		// show notepad
		Assert.assertTrue(Win.show(NOTEPAD_TITLE));
		assertEquals(clientHeight, Win.getClientHeight(NOTEPAD_TITLE));

		// minimize notepad
		Assert.assertTrue(Win.minimize(NOTEPAD_TITLE));
		Assert.assertNull(Win.getClientHeight(NOTEPAD_TITLE));

		// restore notepad
		Assert.assertTrue(Win.restore(NOTEPAD_TITLE));
		assertEquals(clientHeight, Win.getClientHeight(NOTEPAD_TITLE));

		// close notepad
		closeNotepad(pid);

		Assert.assertNull(Win.getClientHeight(NOTEPAD_TITLE));

		// If the window title "Program Manager" is used, the function will
		// return the size of the desktop
		assertEquals(Toolkit.getDefaultToolkit().getScreenSize().height,
				Win.getClientHeight(Win.PROGRAM_MANAGER));
	}

	@Test
	public void getClientSize() {
		// run notepad
		int pid = runNotepad();

		int[] clientSize = Win.getClientSize(NOTEPAD_TITLE);
		Assert.assertArrayEquals(new int[] { Win.getClientWidth(NOTEPAD_TITLE),
				Win.getClientHeight(NOTEPAD_TITLE) }, clientSize);

		// change notepad's height
		Win.move(NOTEPAD_TITLE, Win.getPosX(NOTEPAD_TITLE),
				Win.getPosY(NOTEPAD_TITLE), Win.getWidth(NOTEPAD_TITLE), 200);
		clientSize = Win.getClientSize(NOTEPAD_TITLE);
		Assert.assertArrayEquals(new int[] { Win.getClientWidth(NOTEPAD_TITLE),
				Win.getClientHeight(NOTEPAD_TITLE) }, clientSize);

		Win.move(NOTEPAD_TITLE, Win.getPosX(NOTEPAD_TITLE),
				Win.getPosY(NOTEPAD_TITLE), Win.getWidth(NOTEPAD_TITLE), 150);
		clientSize = Win.getClientSize(NOTEPAD_TITLE);
		Assert.assertArrayEquals(new int[] { Win.getClientWidth(NOTEPAD_TITLE),
				Win.getClientHeight(NOTEPAD_TITLE) }, clientSize);

		// hide notepad
		Assert.assertTrue(Win.hide(NOTEPAD_TITLE));
		clientSize = Win.getClientSize(NOTEPAD_TITLE);
		Assert.assertArrayEquals(new int[] { Win.getClientWidth(NOTEPAD_TITLE),
				Win.getClientHeight(NOTEPAD_TITLE) }, clientSize);

		// show notepad
		Assert.assertTrue(Win.show(NOTEPAD_TITLE));
		clientSize = Win.getClientSize(NOTEPAD_TITLE);
		Assert.assertArrayEquals(new int[] { Win.getClientWidth(NOTEPAD_TITLE),
				Win.getClientHeight(NOTEPAD_TITLE) }, clientSize);

		// minimize notepad
		Assert.assertTrue(Win.minimize(NOTEPAD_TITLE));
		Assert.assertNull(Win.getClientSize(NOTEPAD_TITLE));

		// restore notepad
		Assert.assertTrue(Win.restore(NOTEPAD_TITLE));
		clientSize = Win.getClientSize(NOTEPAD_TITLE);
		Assert.assertArrayEquals(new int[] { Win.getClientWidth(NOTEPAD_TITLE),
				Win.getClientHeight(NOTEPAD_TITLE) }, clientSize);

		// close notepad
		closeNotepad(pid);
		Assert.assertNull(Win.getClientSize(NOTEPAD_TITLE));

		// If the window title "Program Manager" is used, the function will
		// return the size of the desktop
		Assert.assertArrayEquals(new int[] {
				Toolkit.getDefaultToolkit().getScreenSize().width,
				Toolkit.getDefaultToolkit().getScreenSize().height },
				Win.getClientSize(Win.PROGRAM_MANAGER));
	}

	@Test
	public void getClientWidth() {
		// run notepad
		int pid = runNotepad();

		int width = Win.getWidth(NOTEPAD_TITLE);
		Assert.assertTrue(width > 1);
		int clientWidth = Win.getClientWidth(NOTEPAD_TITLE);
		Assert.assertTrue(clientWidth > 1);
		Assert.assertTrue(clientWidth < width);

		// change notepad's width
		Win.move(NOTEPAD_TITLE, Win.getPosX(NOTEPAD_TITLE),
				Win.getPosY(NOTEPAD_TITLE), 200, Win.getHeight(NOTEPAD_TITLE));
		assertEquals(200 + clientWidth - width,
				Win.getClientWidth(NOTEPAD_TITLE));

		Win.move(NOTEPAD_TITLE, Win.getPosX(NOTEPAD_TITLE),
				Win.getPosY(NOTEPAD_TITLE), 150, Win.getHeight(NOTEPAD_TITLE));
		assertEquals(150 + clientWidth - width,
				Win.getClientWidth(NOTEPAD_TITLE));

		// restore notepad's width
		Assert.assertTrue(Win.move(NOTEPAD_TITLE, Win.getPosX(NOTEPAD_TITLE),
				Win.getPosY(NOTEPAD_TITLE), width, Win.getHeight(NOTEPAD_TITLE)));
		assertEquals(clientWidth, Win.getClientWidth(NOTEPAD_TITLE));

		// hide notepad
		Assert.assertTrue(Win.hide(NOTEPAD_TITLE));
		assertEquals(clientWidth, Win.getClientWidth(NOTEPAD_TITLE));

		// show notepad
		Assert.assertTrue(Win.show(NOTEPAD_TITLE));
		assertEquals(clientWidth, Win.getClientWidth(NOTEPAD_TITLE));

		// minimize notepad
		Assert.assertTrue(Win.minimize(NOTEPAD_TITLE));
		Assert.assertNull(Win.getClientWidth(NOTEPAD_TITLE));

		// restore notepad
		Assert.assertTrue(Win.restore(NOTEPAD_TITLE));
		assertEquals(clientWidth, Win.getClientWidth(NOTEPAD_TITLE));

		// close notepad
		closeNotepad(pid);

		Assert.assertNull(Win.getClientWidth(NOTEPAD_TITLE));

		// If the window title "Program Manager" is used, the function will
		// return the size of the desktop
		assertEquals(Toolkit.getDefaultToolkit().getScreenSize().width,
				Win.getClientWidth(Win.PROGRAM_MANAGER));
	}

	@Test
	public void getDesktopHeight() {
		assertEquals(Toolkit.getDefaultToolkit().getScreenSize().height,
				Win.getDesktopHeight());
	}

	@Test
	public void getDesktopWidth() {
		assertEquals(Toolkit.getDefaultToolkit().getScreenSize().width,
				Win.getDesktopWidth());
	}

	@Test
	public void getHandle() {
		Assert.assertNull(Win.getHandle(NOTEPAD_TITLE));
		Assert.assertNull(Win.getHandle((HWND) null));

		// run notepad
		int pid = runNotepad();

		HWND hWnd = Win.getHandle_(NOTEPAD_TITLE);
		Assert.assertNotNull(hWnd);

		assertEquals(
				AutoItX.hwndToHandle(User32.INSTANCE.GetForegroundWindow()),
				Win.getHandle(NOTEPAD_TITLE));
		assertEquals(
				AutoItX.hwndToHandle(User32.INSTANCE.GetForegroundWindow()),
				Win.getHandle(hWnd));

		// close notepad
		closeNotepad(pid);

		Assert.assertNull(Win.getHandle(NOTEPAD_TITLE));
		Assert.assertNull(Win.getHandle(hWnd));
	}

	@Test
	public void getPosX() {
		// run notepad
		int pid = runNotepad();

		int x = Win.getPosX(NOTEPAD_TITLE);

		// change notepad's X coordinate
		Win.move(NOTEPAD_TITLE, 200, Win.getCaretPosY(),
				Win.getWidth(NOTEPAD_TITLE), Win.getHeight(NOTEPAD_TITLE));
		assertEquals(200, Win.getPosX(NOTEPAD_TITLE));

		Win.move(NOTEPAD_TITLE, 150, Win.getCaretPosY(),
				Win.getWidth(NOTEPAD_TITLE), Win.getHeight(NOTEPAD_TITLE));
		assertEquals(150, Win.getPosX(NOTEPAD_TITLE));

		Win.move(NOTEPAD_TITLE, -20, Win.getCaretPosY(),
				Win.getWidth(NOTEPAD_TITLE), Win.getHeight(NOTEPAD_TITLE));
		assertEquals(-20, Win.getPosX(NOTEPAD_TITLE));

		// restore notepad's X coordinate
		Assert.assertTrue(Win.move(NOTEPAD_TITLE, x, Win.getCaretPosY(),
				Win.getWidth(NOTEPAD_TITLE), Win.getHeight(NOTEPAD_TITLE)));
		assertEquals(x, Win.getPosX(NOTEPAD_TITLE));

		// hide notepad
		Assert.assertTrue(Win.hide(NOTEPAD_TITLE));
		assertEquals(x, Win.getPosX(NOTEPAD_TITLE));

		// show notepad
		Assert.assertTrue(Win.show(NOTEPAD_TITLE));
		assertEquals(x, Win.getPosX(NOTEPAD_TITLE));

		// minimize notepad
		Assert.assertTrue(Win.minimize(NOTEPAD_TITLE));
		Assert.assertNull(Win.getPosX(NOTEPAD_TITLE));

		// restore notepad
		Assert.assertTrue(Win.restore(NOTEPAD_TITLE));
		assertEquals(x, Win.getPosX(NOTEPAD_TITLE));

		// close notepad
		Process.close(pid);
		sleep(500);
		Assert.assertFalse(Process.exists(pid));

		Assert.assertNull(Win.getPosX(NOTEPAD_TITLE));

		// If the window title "Program Manager" is used, the function will
		// return the size of the desktop
		assertEquals(0, Win.getPosX(Win.PROGRAM_MANAGER));
	}

	@Test
	public void getPosY() {
		// run notepad
		int pid = runNotepad();

		int y = Win.getPosY(NOTEPAD_TITLE);

		// change notepad's Y coordinate
		Win.move(NOTEPAD_TITLE, Win.getPosX(NOTEPAD_TITLE), 200,
				Win.getWidth(NOTEPAD_TITLE), Win.getHeight(NOTEPAD_TITLE));
		assertEquals(200, Win.getPosY(NOTEPAD_TITLE));

		Win.move(NOTEPAD_TITLE, Win.getPosX(NOTEPAD_TITLE), 150,
				Win.getWidth(NOTEPAD_TITLE), Win.getHeight(NOTEPAD_TITLE));
		assertEquals(150, Win.getPosY(NOTEPAD_TITLE));

		Win.move(NOTEPAD_TITLE, Win.getPosX(NOTEPAD_TITLE), -20,
				Win.getWidth(NOTEPAD_TITLE), Win.getHeight(NOTEPAD_TITLE));
		assertEquals(-20, Win.getPosY(NOTEPAD_TITLE));

		// restore notepad's Y coordinate
		Assert.assertTrue(Win.move(NOTEPAD_TITLE, Win.getPosX(NOTEPAD_TITLE),
				y, Win.getWidth(NOTEPAD_TITLE), Win.getHeight(NOTEPAD_TITLE)));
		assertEquals(y, Win.getPosY(NOTEPAD_TITLE));

		// hide notepad
		Assert.assertTrue(Win.hide(NOTEPAD_TITLE));
		assertEquals(y, Win.getPosY(NOTEPAD_TITLE));

		// show notepad
		Assert.assertTrue(Win.show(NOTEPAD_TITLE));
		assertEquals(y, Win.getPosY(NOTEPAD_TITLE));

		// minimize notepad
		Assert.assertTrue(Win.minimize(NOTEPAD_TITLE));
		Assert.assertNull(Win.getPosY(NOTEPAD_TITLE));

		// restore notepad
		Assert.assertTrue(Win.restore(NOTEPAD_TITLE));
		assertEquals(y, Win.getPosY(NOTEPAD_TITLE));

		// close notepad
		Process.close(pid);
		sleep(500);
		Assert.assertFalse(Process.exists(pid));

		Assert.assertNull(Win.getPosY(NOTEPAD_TITLE));

		// If the window title "Program Manager" is used, the function will
		// return the size of the desktop
		assertEquals(0, Win.getPosY(Win.PROGRAM_MANAGER));
	}

	@Test
	public void getHeight() {
		// run notepad
		int pid = runNotepad();

		int height = Win.getHeight(NOTEPAD_TITLE);
		Assert.assertTrue(height > 1);

		// change notepad's height
		Win.move(NOTEPAD_TITLE, Win.getPosX(NOTEPAD_TITLE),
				Win.getPosY(NOTEPAD_TITLE), Win.getWidth(NOTEPAD_TITLE), 200);
		assertEquals(200, Win.getHeight(NOTEPAD_TITLE));

		Win.move(NOTEPAD_TITLE, Win.getPosX(NOTEPAD_TITLE),
				Win.getPosY(NOTEPAD_TITLE), Win.getWidth(NOTEPAD_TITLE), 150);
		assertEquals(150, Win.getHeight(NOTEPAD_TITLE));

		// restore notepad's height
		Assert.assertTrue(Win.move(NOTEPAD_TITLE, Win.getPosX(NOTEPAD_TITLE),
				Win.getPosY(NOTEPAD_TITLE), Win.getWidth(NOTEPAD_TITLE), height));
		assertEquals(height, Win.getHeight(NOTEPAD_TITLE));

		// hide notepad
		Assert.assertTrue(Win.hide(NOTEPAD_TITLE));
		assertEquals(height, Win.getHeight(NOTEPAD_TITLE));

		// show notepad
		Assert.assertTrue(Win.show(NOTEPAD_TITLE));
		assertEquals(height, Win.getHeight(NOTEPAD_TITLE));

		// minimize notepad
		Assert.assertTrue(Win.minimize(NOTEPAD_TITLE));
		Assert.assertNull(Win.getHeight(NOTEPAD_TITLE));

		// restore notepad
		Assert.assertTrue(Win.restore(NOTEPAD_TITLE));
		assertEquals(height, Win.getHeight(NOTEPAD_TITLE));

		// close notepad
		Process.close(pid);
		sleep(500);
		Assert.assertFalse(Process.exists(pid));

		Assert.assertNull(Win.getHeight(NOTEPAD_TITLE));

		// If the window title "Program Manager" is used, the function will
		// return the size of the desktop
		assertEquals(Toolkit.getDefaultToolkit().getScreenSize().height,
				Win.getHeight(Win.PROGRAM_MANAGER));
	}

	@Test
	public void getWidth() {
		// run notepad
		int pid = runNotepad();

		int width = Win.getWidth(NOTEPAD_TITLE);
		Assert.assertTrue(width > 1);

		// change notepad's width
		Win.move(NOTEPAD_TITLE, Win.getPosX(NOTEPAD_TITLE),
				Win.getPosY(NOTEPAD_TITLE), 200, Win.getHeight(NOTEPAD_TITLE));
		assertEquals(200, Win.getWidth(NOTEPAD_TITLE));

		Win.move(NOTEPAD_TITLE, Win.getPosX(NOTEPAD_TITLE),
				Win.getPosY(NOTEPAD_TITLE), 150, Win.getHeight(NOTEPAD_TITLE));
		assertEquals(150, Win.getWidth(NOTEPAD_TITLE));

		// restore notepad's width
		Assert.assertTrue(Win.move(NOTEPAD_TITLE, Win.getPosX(NOTEPAD_TITLE),
				Win.getPosY(NOTEPAD_TITLE), width, Win.getHeight(NOTEPAD_TITLE)));
		assertEquals(width, Win.getWidth(NOTEPAD_TITLE));

		// hide notepad
		Assert.assertTrue(Win.hide(NOTEPAD_TITLE));
		assertEquals(width, Win.getWidth(NOTEPAD_TITLE));

		// show notepad
		Assert.assertTrue(Win.show(NOTEPAD_TITLE));
		assertEquals(width, Win.getWidth(NOTEPAD_TITLE));

		// minimize notepad
		Assert.assertTrue(Win.minimize(NOTEPAD_TITLE));
		Assert.assertNull(Win.getWidth(NOTEPAD_TITLE));

		// restore notepad
		Assert.assertTrue(Win.restore(NOTEPAD_TITLE));
		assertEquals(width, Win.getWidth(NOTEPAD_TITLE));

		// close notepad
		Process.close(pid);
		sleep(500);
		Assert.assertFalse(Process.exists(pid));

		Assert.assertNull(Win.getWidth(NOTEPAD_TITLE));

		// If the window title "Program Manager" is used, the function will
		// return the size of the desktop
		assertEquals(Toolkit.getDefaultToolkit().getScreenSize().width,
				Win.getWidth(Win.PROGRAM_MANAGER));
	}

	@Test
	public void getProcess() {
		// run notepad
		int pid = runNotepad();

		assertEquals(pid, Win.getProcess(NOTEPAD_TITLE));
		assertEquals(pid, Win.getProcess((String) null));
		assertEquals(pid, Win.getProcess(""));

		// close notepad
		closeNotepad(pid);

		Assert.assertNull(Win.getProcess(NOTEPAD_TITLE));
	}

	@Test
	public void getState() {
		// run notepad
		int pid = runNotepad();

		assertEquals(Win.WIN_STATE_EXISTS + Win.WIN_STATE_VISIBLE
				+ Win.WIN_STATE_ENABLED + Win.WIN_STATE_ACTIVE,
				Win.getState(NOTEPAD_TITLE));

		// maximize notepad
		Assert.assertTrue(Win.maximize(NOTEPAD_TITLE));
		assertEquals(Win.WIN_STATE_EXISTS + Win.WIN_STATE_VISIBLE
				+ Win.WIN_STATE_ENABLED + Win.WIN_STATE_ACTIVE
				+ Win.WIN_STATE_MAXIMIZED, Win.getState(NOTEPAD_TITLE));

		// restore notepad
		Assert.assertTrue(Win.restore(NOTEPAD_TITLE));
		assertEquals(Win.WIN_STATE_EXISTS + Win.WIN_STATE_VISIBLE
				+ Win.WIN_STATE_ENABLED + Win.WIN_STATE_ACTIVE,
				Win.getState(NOTEPAD_TITLE));

		// minimize notepad
		Assert.assertTrue(Win.minimize(NOTEPAD_TITLE));
		assertEquals(Win.WIN_STATE_EXISTS + Win.WIN_STATE_VISIBLE
				+ Win.WIN_STATE_ENABLED + Win.WIN_STATE_MINIMIZED,
				Win.getState(NOTEPAD_TITLE));

		// restore notepad
		Assert.assertTrue(Win.restore(NOTEPAD_TITLE));
		assertEquals(Win.WIN_STATE_EXISTS + Win.WIN_STATE_VISIBLE
				+ Win.WIN_STATE_ENABLED, Win.getState(NOTEPAD_TITLE));

		// active notepad
		Win.activate(NOTEPAD_TITLE);
		assertEquals(Win.WIN_STATE_EXISTS + Win.WIN_STATE_VISIBLE
				+ Win.WIN_STATE_ENABLED + Win.WIN_STATE_ACTIVE,
				Win.getState(NOTEPAD_TITLE));

		// hide notepad
		Assert.assertTrue(Win.hide(NOTEPAD_TITLE));
		assertEquals(Win.WIN_STATE_EXISTS + Win.WIN_STATE_ENABLED
				+ Win.WIN_STATE_ACTIVE, Win.getState(NOTEPAD_TITLE));

		// close notepad
		Process.close(pid);
		sleep(500);
		Assert.assertFalse(Process.exists(pid));

		// If the window title "Program Manager" is used, the function will
		// return the size of the desktop
		Assert.assertNull(Win.getState(NOTEPAD_TITLE));
	}

	@Test
	public void getState_() {
		// run notepad
		int pid = runNotepad();

		List<WinState> winStates = Win.getState_(NOTEPAD_TITLE);
		Assert.assertTrue(winStates.contains(WinState.EXISTS));
		Assert.assertTrue(winStates.contains(WinState.VISIBLE));
		Assert.assertTrue(winStates.contains(WinState.ENABLED));
		Assert.assertTrue(winStates.contains(WinState.ACTIVE));
		Assert.assertFalse(winStates.contains(WinState.MINIMIZED));
		Assert.assertFalse(winStates.contains(WinState.MAXIMIZED));

		// maximize notepad
		Assert.assertTrue(Win.maximize(NOTEPAD_TITLE));
		winStates = Win.getState_(NOTEPAD_TITLE);
		Assert.assertTrue(winStates.contains(WinState.EXISTS));
		Assert.assertTrue(winStates.contains(WinState.VISIBLE));
		Assert.assertTrue(winStates.contains(WinState.ENABLED));
		Assert.assertTrue(winStates.contains(WinState.ACTIVE));
		Assert.assertFalse(winStates.contains(WinState.MINIMIZED));
		Assert.assertTrue(winStates.contains(WinState.MAXIMIZED));

		// restore notepad
		Assert.assertTrue(Win.restore(NOTEPAD_TITLE));
		winStates = Win.getState_(NOTEPAD_TITLE);
		Assert.assertTrue(winStates.contains(WinState.EXISTS));
		Assert.assertTrue(winStates.contains(WinState.VISIBLE));
		Assert.assertTrue(winStates.contains(WinState.ENABLED));
		Assert.assertTrue(winStates.contains(WinState.ACTIVE));
		Assert.assertFalse(winStates.contains(WinState.MINIMIZED));
		Assert.assertFalse(winStates.contains(WinState.MAXIMIZED));

		// minimize notepad
		Assert.assertTrue(Win.minimize(NOTEPAD_TITLE));
		winStates = Win.getState_(NOTEPAD_TITLE);
		Assert.assertTrue(winStates.contains(WinState.EXISTS));
		Assert.assertTrue(winStates.contains(WinState.VISIBLE));
		Assert.assertTrue(winStates.contains(WinState.ENABLED));
		Assert.assertFalse(winStates.contains(WinState.ACTIVE));
		Assert.assertTrue(winStates.contains(WinState.MINIMIZED));
		Assert.assertFalse(winStates.contains(WinState.MAXIMIZED));

		// restore notepad
		Assert.assertTrue(Win.restore(NOTEPAD_TITLE));
		winStates = Win.getState_(NOTEPAD_TITLE);
		Assert.assertTrue(winStates.contains(WinState.EXISTS));
		Assert.assertTrue(winStates.contains(WinState.VISIBLE));
		Assert.assertTrue(winStates.contains(WinState.ENABLED));
		Assert.assertFalse(winStates.contains(WinState.ACTIVE));
		Assert.assertFalse(winStates.contains(WinState.MINIMIZED));
		Assert.assertFalse(winStates.contains(WinState.MAXIMIZED));

		// active notepad
		Win.activate(NOTEPAD_TITLE);
		winStates = Win.getState_(NOTEPAD_TITLE);
		Assert.assertTrue(winStates.contains(WinState.EXISTS));
		Assert.assertTrue(winStates.contains(WinState.VISIBLE));
		Assert.assertTrue(winStates.contains(WinState.ENABLED));
		Assert.assertTrue(winStates.contains(WinState.ACTIVE));
		Assert.assertFalse(winStates.contains(WinState.MINIMIZED));
		Assert.assertFalse(winStates.contains(WinState.MAXIMIZED));

		// hide notepad
		Assert.assertTrue(Win.hide(NOTEPAD_TITLE));
		Assert.assertTrue(Win.active(NOTEPAD_TITLE));
		winStates = Win.getState_(NOTEPAD_TITLE);
		Assert.assertTrue(winStates.contains(WinState.EXISTS));
		Assert.assertFalse(winStates.contains(WinState.VISIBLE));
		Assert.assertTrue(winStates.contains(WinState.ENABLED));
		Assert.assertTrue(winStates.contains(WinState.ACTIVE));
		Assert.assertFalse(winStates.contains(WinState.MINIMIZED));
		Assert.assertFalse(winStates.contains(WinState.MAXIMIZED));

		// close notepad
		Process.close(pid);
		sleep(500);
		Assert.assertFalse(Process.exists(pid));

		winStates = Win.getState_(NOTEPAD_TITLE);
		Assert.assertNull(winStates);
	}

	@Test
	public void getText() {
		// create frame
		String title = "getText - " + System.currentTimeMillis();
		Frame frame = new Frame(title);
		String text1 = "Hello, " + System.currentTimeMillis();
		frame.add(new Button(text1));
		String text2 = "World, " + System.currentTimeMillis();
		frame.add(new Button(text2));

		try {
			// show frame
			frame.setVisible(true);
			Assert.assertTrue(Win.active(title));
			assertEquals(text1 + "\n" + text2 + "\n", Win.getText(title));
			assertEquals(text1 + "\n" + text2 + "\n",
					Win.getText((String) null));
			assertEquals(text1 + "\n" + text2 + "\n", Win.getText(""));

			// minimize frame
			Assert.assertTrue(Win.minimize(title));
			Assert.assertTrue(Win.minimized(title));
			assertEquals(text1 + "\n" + text2 + "\n", Win.getText(title));

			// hide frame
			Assert.assertTrue(Win.hide(title));
			assertEquals("", Win.getText(title));

			Opt.setWinDetectHiddenText(true);
			assertEquals(text1 + "\n" + text2 + "\n", Win.getText(title));
			Opt.setWinDetectHiddenText(false);
			assertEquals("", Win.getText(title));
			Opt.setWinDetectHiddenText(true);
			assertEquals(text1 + "\n" + text2 + "\n", Win.getText(title));
			Opt.setWinDetectHiddenText(Opt.DEFAULT_WIN_DETECT_HIDDEN_TEXT);
			assertEquals("", Win.getText(title));
		} finally {
			// destroy frame
			frame.setVisible(false);
		}
	}

	@Test
	public void getTitle() {
		// run notepad
		int pid = runNotepad();

		assertEquals(NOTEPAD_TITLE, Win.getTitle(NOTEPAD_TITLE_START));
		assertEquals(NOTEPAD_TITLE, Win.getTitle(""));
		assertEquals(NOTEPAD_TITLE, Win.getTitle((String) null));

		Assert.assertNull(Win.getTitle(NOTEPAD_TITLE + currentTimeMillis));
		Assert.assertTrue(Win.setTitle(NOTEPAD_TITLE, "0"));
		Assert.assertNull(Win.getTitle(NOTEPAD_TITLE_START));
		assertEquals("0", Win.getTitle(""));
		assertEquals("0", Win.getTitle((String) null));
		String handle = Win.getHandle("0");
		Assert.assertNotNull(handle);
		Assert.assertTrue(Win.setTitle("0", ""));
		assertEquals("", Win.getTitle(TitleBuilder.byHandle(handle)));

		// close notepad
		Process.close(pid);
		sleep(500);
		Assert.assertFalse(Process.exists(pid));

		Assert.assertNull(Win.getTitle(NOTEPAD_TITLE_START));
	}

	@Test
	public void kill() {
		// run notepad
		int pid = runNotepad();

		// kill notepad
		Assert.assertTrue(Win.kill(NOTEPAD_TITLE));
		sleep(500);
		Assert.assertFalse(Process.exists(pid));

		Assert.assertFalse(Win.kill(NOTEPAD_TITLE));
	}

	@Test
	public void menuSelectItem() {
		// run notepad
		int pid = runNotepad();

		// select menu item which could not be found
		Assert.assertFalse(Win.menuSelectItem(NOTEPAD_TITLE, null, "文件(&F)",
				"XXXXX"));
		sleep(500);
		Assert.assertTrue(Process.exists(pid));

		// exit notepad
		Assert.assertTrue(Win.menuSelectItem(NOTEPAD_TITLE, null, "文件(&F)",
				"退出(&X)"));
		sleep(500);
		Assert.assertFalse(Process.exists(pid));
	}

	@Test
	public void minimizeAll() {
		// run notepad
		int pid = runNotepad();

		Win.minimizeAll();
		sleep(500);
		Win.minimizeAllUndo();

		// close notepad
		Process.close(pid);
		sleep(500);
		Assert.assertFalse(Process.exists(pid));
	}

	@Test
	public void minimizeAllUndo() {
		minimizeAll();
	}

	@Test
	public void move() {
		// run notepad
		int pid = runNotepad();

		final int x = Win.getPosX(NOTEPAD_TITLE);
		final int y = Win.getPosY(NOTEPAD_TITLE);
		final int width = Win.getWidth(NOTEPAD_TITLE);
		final int height = Win.getHeight(NOTEPAD_TITLE);

		// move notepad
		Assert.assertTrue(Win.move(NOTEPAD_TITLE, x + 40, y + 100, -1, -1));
		assertEquals(x + 40, Win.getPosX(NOTEPAD_TITLE));
		assertEquals(y + 100, Win.getPosY(NOTEPAD_TITLE));
		assertEquals(width, Win.getWidth(NOTEPAD_TITLE));
		assertEquals(height, Win.getHeight(NOTEPAD_TITLE));

		// resize notepad
		Assert.assertTrue(Win.move(NOTEPAD_TITLE, x + 40, y + 100, width + 10,
				height + 20));
		assertEquals(x + 40, Win.getPosX(NOTEPAD_TITLE));
		assertEquals(y + 100, Win.getPosY(NOTEPAD_TITLE));
		assertEquals(width + 10, Win.getWidth(NOTEPAD_TITLE));
		assertEquals(height + 20, Win.getHeight(NOTEPAD_TITLE));

		// move and resize notepad
		Assert.assertTrue(Win.move(NOTEPAD_TITLE, x + 50, y + 110, width + 20,
				height + 30));
		assertEquals(x + 50, Win.getPosX(NOTEPAD_TITLE));
		assertEquals(y + 110, Win.getPosY(NOTEPAD_TITLE));
		assertEquals(width + 20, Win.getWidth(NOTEPAD_TITLE));
		assertEquals(height + 30, Win.getHeight(NOTEPAD_TITLE));

		// close notepad
		Process.close(pid);
		sleep(500);
		Assert.assertFalse(Process.exists(pid));

		Assert.assertFalse(Win.move(NOTEPAD_PROC_NAME, x + 40, y + 100));
	}

	@Test
	public void setOnTop() {
		// run notepad
		int pid = runNotepad();

		HWND hWnd = User32.INSTANCE.FindWindow(NOTEPAAD_CLASS_NAME,
				NOTEPAD_TITLE);
		Assert.assertNotNull(hWnd);
		assertEquals(0,
				User32.INSTANCE.GetWindowLong(hWnd, WinUser.GWL_EXSTYLE)
						& User32.WS_EX_TOPMOST);

		// set on top
		Assert.assertTrue(Win.setOnTop(NOTEPAD_TITLE));
		Assert.assertNotEquals(0,
				User32.INSTANCE.GetWindowLong(hWnd, WinUser.GWL_EXSTYLE)
						& User32.WS_EX_TOPMOST);

		// remove on top
		Assert.assertTrue(Win.setOnTop(NOTEPAD_TITLE, false));
		assertEquals(0,
				User32.INSTANCE.GetWindowLong(hWnd, WinUser.GWL_EXSTYLE)
						& User32.WS_EX_TOPMOST);

		// close notepad
		Process.close(pid);
		sleep(500);
		Assert.assertFalse(Process.exists(pid));

		Assert.assertFalse(Win.setOnTop(NOTEPAD_TITLE));
	}

	@Test
	public void show() {
		// run notepad
		int pid = runNotepad();
		Assert.assertTrue(Win.getState_(NOTEPAD_TITLE)
				.contains(WinState.ACTIVE));
		Assert.assertTrue(Win.getState_(NOTEPAD_TITLE).contains(
				WinState.VISIBLE));
		Assert.assertTrue(Win.getState_(NOTEPAD_TITLE)
				.contains(WinState.EXISTS));

		// hide notepad
		Win.hide(NOTEPAD_TITLE);
		Assert.assertTrue(Win.getState_(NOTEPAD_TITLE)
				.contains(WinState.ACTIVE));
		Assert.assertFalse(Win.getState_(NOTEPAD_TITLE).contains(
				WinState.VISIBLE));
		Assert.assertTrue(Win.getState_(NOTEPAD_TITLE)
				.contains(WinState.EXISTS));

		// show notepad
		Win.show(NOTEPAD_TITLE);
		Assert.assertTrue(Win.getState_(NOTEPAD_TITLE)
				.contains(WinState.ACTIVE));
		Assert.assertTrue(Win.getState_(NOTEPAD_TITLE).contains(
				WinState.VISIBLE));
		Assert.assertTrue(Win.getState_(NOTEPAD_TITLE)
				.contains(WinState.EXISTS));

		// close notepad
		Process.close(pid);
		sleep(500);
		Assert.assertFalse(Process.exists(pid));
		Assert.assertNull(Win.getState_(NOTEPAD_TITLE));

		Assert.assertFalse(Win.show(NOTEPAD_TITLE));
	}

	@Test
	public void hide() {
		// run notepad
		int pid = runNotepad();
		Assert.assertTrue(Win.getState_(NOTEPAD_TITLE)
				.contains(WinState.ACTIVE));
		Assert.assertTrue(Win.getState_(NOTEPAD_TITLE).contains(
				WinState.VISIBLE));
		Assert.assertTrue(Win.getState_(NOTEPAD_TITLE)
				.contains(WinState.EXISTS));

		// hide notepad
		Win.hide(NOTEPAD_TITLE);
		Assert.assertTrue(Win.getState_(NOTEPAD_TITLE)
				.contains(WinState.ACTIVE));
		Assert.assertFalse(Win.getState_(NOTEPAD_TITLE).contains(
				WinState.VISIBLE));
		Assert.assertTrue(Win.getState_(NOTEPAD_TITLE)
				.contains(WinState.EXISTS));

		// show notepad
		Win.show(NOTEPAD_TITLE);
		Assert.assertTrue(Win.getState_(NOTEPAD_TITLE)
				.contains(WinState.ACTIVE));
		Assert.assertTrue(Win.getState_(NOTEPAD_TITLE).contains(
				WinState.VISIBLE));
		Assert.assertTrue(Win.getState_(NOTEPAD_TITLE)
				.contains(WinState.EXISTS));

		// close notepad
		Process.close(pid);
		sleep(500);
		Assert.assertFalse(Process.exists(pid));
		Assert.assertNull(Win.getState_(NOTEPAD_TITLE));

		Assert.assertFalse(Win.hide(NOTEPAD_TITLE));
	}

	@Test
	public void minimize() {
		// run notepad
		int pid = runNotepad();
		Assert.assertTrue(Win.getState_(NOTEPAD_TITLE)
				.contains(WinState.ACTIVE));
		Assert.assertTrue(Win.getState_(NOTEPAD_TITLE).contains(
				WinState.VISIBLE));
		Assert.assertTrue(Win.getState_(NOTEPAD_TITLE)
				.contains(WinState.EXISTS));
		Assert.assertFalse(Win.getState_(NOTEPAD_TITLE).contains(
				WinState.MINIMIZED));

		// minimize notepad
		Win.minimize(NOTEPAD_TITLE);
		Assert.assertFalse(Win.getState_(NOTEPAD_TITLE).contains(
				WinState.ACTIVE));
		Assert.assertTrue(Win.getState_(NOTEPAD_TITLE).contains(
				WinState.VISIBLE));
		Assert.assertTrue(Win.getState_(NOTEPAD_TITLE)
				.contains(WinState.EXISTS));
		Assert.assertTrue(Win.getState_(NOTEPAD_TITLE).contains(
				WinState.MINIMIZED));

		// restore notepad
		Win.restore(NOTEPAD_TITLE);
		Assert.assertFalse(Win.getState_(NOTEPAD_TITLE).contains(
				WinState.ACTIVE));
		Assert.assertTrue(Win.getState_(NOTEPAD_TITLE).contains(
				WinState.VISIBLE));
		Assert.assertTrue(Win.getState_(NOTEPAD_TITLE)
				.contains(WinState.EXISTS));
		Assert.assertFalse(Win.getState_(NOTEPAD_TITLE).contains(
				WinState.MINIMIZED));

		// close notepad
		Process.close(pid);
		sleep(500);
		Assert.assertFalse(Process.exists(pid));
		Assert.assertNull(Win.getState_(NOTEPAD_TITLE));

		Assert.assertFalse(Win.minimize(NOTEPAD_TITLE));
	}

	@Test
	public void maximize() {
		// run notepad
		int pid = runNotepad();
		Assert.assertTrue(Win.getState_(NOTEPAD_TITLE)
				.contains(WinState.ACTIVE));
		Assert.assertTrue(Win.getState_(NOTEPAD_TITLE).contains(
				WinState.VISIBLE));
		Assert.assertTrue(Win.getState_(NOTEPAD_TITLE)
				.contains(WinState.EXISTS));
		Assert.assertFalse(Win.getState_(NOTEPAD_TITLE).contains(
				WinState.MAXIMIZED));

		// maximize notepad
		Win.maximize(NOTEPAD_TITLE);
		Assert.assertTrue(Win.getState_(NOTEPAD_TITLE)
				.contains(WinState.ACTIVE));
		Assert.assertTrue(Win.getState_(NOTEPAD_TITLE).contains(
				WinState.VISIBLE));
		Assert.assertTrue(Win.getState_(NOTEPAD_TITLE)
				.contains(WinState.EXISTS));
		Assert.assertTrue(Win.getState_(NOTEPAD_TITLE).contains(
				WinState.MAXIMIZED));

		// restore notepad
		Win.restore(NOTEPAD_TITLE);
		Assert.assertTrue(Win.getState_(NOTEPAD_TITLE)
				.contains(WinState.ACTIVE));
		Assert.assertTrue(Win.getState_(NOTEPAD_TITLE).contains(
				WinState.VISIBLE));
		Assert.assertTrue(Win.getState_(NOTEPAD_TITLE)
				.contains(WinState.EXISTS));
		Assert.assertFalse(Win.getState_(NOTEPAD_TITLE).contains(
				WinState.MAXIMIZED));

		// close notepad
		Process.close(pid);
		sleep(500);
		Assert.assertFalse(Process.exists(pid));
		Assert.assertNull(Win.getState_(NOTEPAD_TITLE));

		Assert.assertFalse(Win.maximize(NOTEPAD_TITLE));
	}

	@Test
	public void restore() {
		minimize();
		maximize();
	}

	@Test
	public void setTitle() {
		final String newNotepadTitle = "----JAutoItX winSetTitle----";

		// run notepad
		int pid = runNotepad();

		// change notepad's title
		Assert.assertTrue(Win.setTitle(NOTEPAD_TITLE, newNotepadTitle));
		Assert.assertFalse(Win.active(NOTEPAD_TITLE));
		Assert.assertTrue(Win.active(newNotepadTitle));
		Assert.assertFalse(Win.setTitle(NOTEPAD_TITLE, "hello"));

		// restore notepad's title
		Assert.assertTrue(Win.setTitle(newNotepadTitle, NOTEPAD_TITLE));
		Assert.assertTrue(Win.active(NOTEPAD_TITLE));
		Assert.assertFalse(Win.active(newNotepadTitle));
		Assert.assertFalse(Win.setTitle(newNotepadTitle, "world"));

		// close notepad
		Process.close(pid);
		sleep(500);
		Assert.assertFalse(Process.exists(pid));
	}

	@Test
	public void setTrans() {
		// run notepad
		int pid = runNotepad();

		Assert.assertTrue(Win.setTrans(NOTEPAD_TITLE, 0));
		sleep(500);
		Assert.assertTrue(Win.setTrans(NOTEPAD_TITLE, 100));
		sleep(500);
		Assert.assertTrue(Win.setTrans(NOTEPAD_TITLE, 200));
		sleep(500);
		Assert.assertTrue(Win.setTrans(NOTEPAD_TITLE, 255));
		sleep(500);
		Assert.assertTrue(Win.setTrans(NOTEPAD_TITLE, 300));
		sleep(500);
		Assert.assertTrue(Win.setTrans(NOTEPAD_TITLE, 255));
		sleep(500);
		Assert.assertTrue(Win.setTrans(NOTEPAD_TITLE, 200));
		sleep(500);
		Assert.assertTrue(Win.setTrans(NOTEPAD_TITLE, 100));
		sleep(500);
		Assert.assertTrue(Win.setTrans(NOTEPAD_TITLE, 0));
		sleep(500);

		Assert.assertFalse(Win.setTrans("NotExistWinTitle", 100));

		// close notepad
		Process.close(pid);
		sleep(500);
		Assert.assertFalse(Process.exists(pid));

		Assert.assertFalse(Win.setTrans("NOTEPAD_TITLE", 100));
	}

	@Test
	public void testWait() {
		Assert.assertFalse(Win.exists(NOTEPAD_TITLE));
		Assert.assertFalse(Win.wait(NOTEPAD_TITLE, 2));

		// Thread thread = new Thread(new Runnable() {
		// @Override
		// public void run() {
		// Win.wait(NOTEPAD_TITLE);
		// }
		// });
		// thread.start();
		// sleep(2000);
		// Assert.assertTrue(thread.isAlive());
		// thread.stop();

		Thread thread = new Thread(new Runnable() {
			public void run() {
				Win.wait(NOTEPAD_TITLE);
			}
		});
		thread.start();
		runNotepad();
		sleep(4000);
		Assert.assertFalse(thread.isAlive());

		// close notepad
		Win.close(NOTEPAD_TITLE);
		Assert.assertFalse(Win.exists(NOTEPAD_TITLE));
	}

	@SuppressWarnings("deprecation")
	@Test
	public void waitActive() {
		Assert.assertFalse(Win.exists(NOTEPAD_TITLE));
		runNotepad();
		Assert.assertTrue(Win.exists(NOTEPAD_TITLE));

		// minimize notepad
		Win.minimize(NOTEPAD_TITLE);
		Assert.assertFalse(Win.waitActive(NOTEPAD_TITLE, 2));

		Thread thread = new Thread(new Runnable() {
			public void run() {
				Win.waitActive(NOTEPAD_TITLE);
			}
		});
		thread.start();
		sleep(2000);
		Assert.assertTrue(thread.isAlive());
		thread.stop();

		thread = new Thread(new Runnable() {
			public void run() {
				Win.waitActive(NOTEPAD_TITLE);
			}
		});
		thread.start();
		Win.activate(NOTEPAD_TITLE);
		sleep(4000);
		Assert.assertFalse(thread.isAlive());

		// close notepad
		Win.close(NOTEPAD_TITLE);
		Assert.assertFalse(Win.exists(NOTEPAD_TITLE));
	}

	@SuppressWarnings("deprecation")
	@Test
	public void waitClose() {
		Assert.assertFalse(Win.exists(NOTEPAD_TITLE));
		runNotepad();
		Assert.assertTrue(Win.exists(NOTEPAD_TITLE));

		Thread thread = new Thread(new Runnable() {
			public void run() {
				Win.waitClose(NOTEPAD_TITLE);
			}
		});
		thread.start();
		sleep(2000);
		Assert.assertTrue(thread.isAlive());
		thread.stop();

		thread = new Thread(new Runnable() {
			public void run() {
				Win.waitClose(NOTEPAD_TITLE);
			}
		});
		thread.start();
		Win.close(NOTEPAD_TITLE);
		sleep(4000);
		Assert.assertFalse(thread.isAlive());
	}

	@SuppressWarnings("deprecation")
	@Test
	public void waitNotActive() {
		Assert.assertFalse(Win.exists(NOTEPAD_TITLE));
		runNotepad();
		Assert.assertTrue(Win.exists(NOTEPAD_TITLE));

		// activate notepad
		Win.activate(NOTEPAD_TITLE);
		Assert.assertFalse(Win.waitNotActive(NOTEPAD_TITLE, 2));

		Thread thread = new Thread(new Runnable() {
			public void run() {
				Win.waitNotActive(NOTEPAD_TITLE);
			}
		});
		thread.start();
		sleep(2000);
		Assert.assertTrue(thread.isAlive());
		thread.stop();

		thread = new Thread(new Runnable() {
			public void run() {
				Win.waitNotActive(NOTEPAD_TITLE);
			}
		});
		thread.start();
		Win.minimize(NOTEPAD_TITLE);
		sleep(4000);
		Assert.assertFalse(thread.isAlive());

		// close notepad
		Win.close(NOTEPAD_TITLE);
		Assert.assertFalse(Win.exists(NOTEPAD_TITLE));
	}
}
