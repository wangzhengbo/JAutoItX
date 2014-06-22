package cn.com.jautoitx;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.Frame;

import javax.swing.JFrame;
import javax.swing.JTextField;

import org.junit.Assert;
import org.junit.Test;
import org.omg.CORBA.LongHolder;

import cn.com.jautoitx.Mouse.MouseButton;
import cn.com.jautoitx.Opt.CoordMode;
import cn.com.jautoitx.Opt.WinTextMatchMode;
import cn.com.jautoitx.Opt.WinTitleMatchMode;

public class OptTest extends BaseTest {
	@Test
	public void setMouseClickDelay() {
		String title1 = "setMouseClickDelay - " + currentTimeMillis;
		JFrame frame1 = new JFrame(title1);
		frame1.setBounds(0, 0, 400, 300);
		frame1.setVisible(true);

		try {
			long start = System.currentTimeMillis();
			Mouse.click(MouseButton.LEFT, 50, 40, 1, 0);
			long end = System.currentTimeMillis();
			Assert.assertTrue((end - start) >= Opt.DEFAULT_MOUSE_CLICK_DELAY);
			Assert.assertTrue((end - start) < 500);

			Assert.assertEquals(Opt.DEFAULT_MOUSE_CLICK_DELAY,
					Opt.setMouseClickDelay(500));
			start = System.currentTimeMillis();
			Mouse.click(MouseButton.LEFT, 50, 40);
			end = System.currentTimeMillis();
			Assert.assertTrue((end - start) >= 500);
			Assert.assertTrue((end - start) < 1000);

			Assert.assertEquals(500, Opt.setMouseClickDelay(1000));
			start = System.currentTimeMillis();
			Mouse.click(MouseButton.LEFT, 50, 40);
			end = System.currentTimeMillis();
			Assert.assertTrue((end - start) >= 1000);
			Assert.assertTrue((end - start) < 1500);
		} finally {
			// destroy frame
			frame1.setVisible(false);

			// Restore default mouse click delay
			Opt.setMouseClickDelay(Opt.DEFAULT_MOUSE_CLICK_DELAY);
		}
	}

	@Test
	public void setMouseClickDownDelay() {
		String title1 = "setMouseClickDownDelay - " + currentTimeMillis;
		JFrame frame1 = new JFrame(title1);
		frame1.setBounds(0, 0, 400, 300);
		frame1.setVisible(true);

		try {
			Mouse.move(50, 40);
			long start = System.currentTimeMillis();
			Mouse.down(MouseButton.LEFT);
			long end = System.currentTimeMillis();
			Mouse.up(MouseButton.LEFT);
			Assert.assertTrue(String.format("(%d - %d) >= %d", end, start,
					Opt.DEFAULT_MOUSE_CLICK_DOWN_DELAY),
					(end - start) >= Opt.DEFAULT_MOUSE_CLICK_DOWN_DELAY);
			Assert.assertTrue(String.format("(%d - %d) < %d", end, start, 500),
					(end - start) < 500);

			Assert.assertEquals(Opt.DEFAULT_MOUSE_CLICK_DOWN_DELAY,
					Opt.setMouseClickDownDelay(500));
			start = System.currentTimeMillis();
			Mouse.down(MouseButton.LEFT);
			end = System.currentTimeMillis();
			Mouse.up(MouseButton.LEFT);
			Assert.assertTrue(
					String.format("(%d - %d) >= %d", end, start, 500),
					(end - start) >= 500);
			Assert.assertTrue(
					String.format("(%d - %d) < %d", end, start, 1000),
					(end - start) < 1000);

			Assert.assertEquals(500, Opt.setMouseClickDownDelay(1000));
			start = System.currentTimeMillis();
			Mouse.down(MouseButton.LEFT);
			end = System.currentTimeMillis();
			Mouse.up(MouseButton.LEFT);
			Assert.assertTrue(
					String.format("(%d - %d) >= %d", end, start, 500),
					(end - start) >= 1000);
			Assert.assertTrue(
					String.format("(%d - %d) < %d", end, start, 1500),
					(end - start) < 1500);
		} finally {
			// destroy frame
			frame1.setVisible(false);

			// Restore default mouse click down delay
			Opt.setMouseClickDownDelay(Opt.DEFAULT_MOUSE_CLICK_DOWN_DELAY);
		}
	}

	@Test
	public void setMouseClickDragDelay() {
		String title1 = "setMouseClickDragDelay - " + currentTimeMillis;
		JFrame frame1 = new JFrame(title1);
		frame1.setBounds(0, 0, 400, 300);
		frame1.setVisible(true);

		try {
			Mouse.move(10, 10);
			long start = System.currentTimeMillis();
			Mouse.clickDrag(MouseButton.LEFT, 40, 40, 50, 50, 0);
			long end = System.currentTimeMillis();
			Assert.assertTrue((end - start) >= Opt.DEFAULT_MOUSE_CLICK_DRAG_DELAY);
			Assert.assertTrue((end - start) < 1000);

			Assert.assertEquals(Opt.DEFAULT_MOUSE_CLICK_DRAG_DELAY,
					Opt.setMouseClickDragDelay(500));
			Mouse.move(10, 10);
			start = System.currentTimeMillis();
			Mouse.clickDrag(MouseButton.LEFT, 40, 40, 50, 50, 0);
			end = System.currentTimeMillis();
			Assert.assertTrue((end - start) >= 500);
			Assert.assertTrue((end - start) < 1500);

			Assert.assertEquals(500, Opt.setMouseClickDragDelay(1000));
			Mouse.move(10, 10);
			start = System.currentTimeMillis();
			Mouse.clickDrag(MouseButton.LEFT, 40, 40, 50, 50, 0);
			end = System.currentTimeMillis();
			Assert.assertTrue((end - start) >= 1000);
			Assert.assertTrue((end - start) < 2500);
		} finally {
			// destroy frame
			frame1.setVisible(false);

			// Restore default mouse click drag delay
			Opt.setMouseClickDragDelay(Opt.DEFAULT_MOUSE_CLICK_DRAG_DELAY);
		}
	}

	@Test
	public void setCaretCoordMode() {
		String title1 = "setCaretCoordMode - " + currentTimeMillis;
		JFrame frame1 = new JFrame(title1);
		frame1.setBounds(50, 100, 400, 300);
		JTextField textField1 = new JTextField();
		frame1.getContentPane().add(textField1, BorderLayout.NORTH);
		frame1.setVisible(true);

		try {
			// Activate frame1
			Assert.assertTrue(Win.activate(title1));
			int x1 = Win.getCaretPosX();
			int y1 = Win.getCaretPosY();
			Assert.assertTrue(x1 > 0);
			Assert.assertTrue(y1 > 0);

			Assert.assertEquals(CoordMode.ABSOLUTE_SCREEN_COORDINATES,
					Opt.setCaretCoordMode(CoordMode.RELATIVE_TO_ACTIVE_WINDOW));
			int x2 = Win.getCaretPosX();
			int y2 = Win.getCaretPosY();
			Assert.assertEquals(x2, x1 - 50);
			Assert.assertEquals(y2, y1 - 100);
		} finally {
			// destroy frame
			frame1.setVisible(false);

			// Restore caret coord mode
			Opt.setCaretCoordMode(Opt.DEFAULT_MOUSE_COORD_MODE);
		}
	}

	@Test
	public void setMouseCoordMode() {
		String title1 = "setMouseCoordMode 1 - " + currentTimeMillis;
		JFrame frame1 = new JFrame(title1);
		frame1.setBounds(0, 0, 400, 300);
		frame1.setVisible(true);

		String title2 = "setMouseCoordMode 2 - " + currentTimeMillis;
		JFrame frame2 = new JFrame(title2);
		frame2.setBounds(400, 0, 400, 300);
		frame2.setVisible(true);

		try {
			// Activate frame1
			Assert.assertTrue(Win.activate(title1));
			Mouse.move(40, 50);
			Assert.assertEquals(40, Mouse.getPosX());
			Assert.assertEquals(50, Mouse.getPosY());
			Mouse.move(440, 60);
			Assert.assertEquals(440, Mouse.getPosX());
			Assert.assertEquals(60, Mouse.getPosY());

			// Activate frame2
			Assert.assertTrue(Win.activate(title2));
			Mouse.move(40, 50);
			Assert.assertEquals(40, Mouse.getPosX());
			Assert.assertEquals(50, Mouse.getPosY());
			Mouse.move(440, 60);
			Assert.assertEquals(440, Mouse.getPosX());
			Assert.assertEquals(60, Mouse.getPosY());

			Assert.assertEquals(CoordMode.ABSOLUTE_SCREEN_COORDINATES,
					Opt.setMouseCoordMode(CoordMode.RELATIVE_TO_ACTIVE_WINDOW));

			// Activate frame1
			Assert.assertTrue(Win.activate(title1));
			Mouse.move(40, 50);
			Assert.assertEquals(CoordMode.RELATIVE_TO_ACTIVE_WINDOW, Opt
					.setMouseCoordMode(CoordMode.ABSOLUTE_SCREEN_COORDINATES));
			Assert.assertEquals(40, Mouse.getPosX());
			Assert.assertEquals(50, Mouse.getPosY());
			Assert.assertEquals(CoordMode.ABSOLUTE_SCREEN_COORDINATES,
					Opt.setMouseCoordMode(CoordMode.RELATIVE_TO_ACTIVE_WINDOW));
			Mouse.move(440, 60);
			Assert.assertEquals(CoordMode.RELATIVE_TO_ACTIVE_WINDOW, Opt
					.setMouseCoordMode(CoordMode.ABSOLUTE_SCREEN_COORDINATES));
			Assert.assertEquals(440, Mouse.getPosX());
			Assert.assertEquals(60, Mouse.getPosY());

			Assert.assertEquals(CoordMode.ABSOLUTE_SCREEN_COORDINATES,
					Opt.setMouseCoordMode(CoordMode.RELATIVE_TO_ACTIVE_WINDOW));

			// Activate frame2
			Assert.assertTrue(Win.activate(title2));
			Mouse.move(40, 50);
			Assert.assertEquals(CoordMode.RELATIVE_TO_ACTIVE_WINDOW, Opt
					.setMouseCoordMode(CoordMode.ABSOLUTE_SCREEN_COORDINATES));
			Assert.assertEquals(440, Mouse.getPosX());
			Assert.assertEquals(50, Mouse.getPosY());
			Assert.assertEquals(CoordMode.ABSOLUTE_SCREEN_COORDINATES,
					Opt.setMouseCoordMode(CoordMode.RELATIVE_TO_ACTIVE_WINDOW));
			Mouse.move(440, 60);
			Assert.assertEquals(CoordMode.RELATIVE_TO_ACTIVE_WINDOW, Opt
					.setMouseCoordMode(CoordMode.ABSOLUTE_SCREEN_COORDINATES));
			Assert.assertEquals(840, Mouse.getPosX());
			Assert.assertEquals(60, Mouse.getPosY());
		} finally {
			// destroy frame
			frame1.setVisible(false);
			frame2.setVisible(false);

			// Restore mouse coord mode
			Opt.setMouseCoordMode(Opt.DEFAULT_MOUSE_COORD_MODE);
		}
	}

	@Test
	public void setPixelCoordMode() {
		String title1 = "setPixelCoordMode 1 - " + currentTimeMillis;
		JFrame frame1 = new JFrame(title1);
		frame1.setBounds(0, 0, 400, 300);
		frame1.getContentPane().setBackground(Color.BLACK);
		frame1.setVisible(true);

		String title2 = "setPixelCoordMode 2 - " + currentTimeMillis;
		JFrame frame2 = new JFrame(title2);
		frame2.setBounds(400, 0, 400, 300);
		frame2.getContentPane().setBackground(Color.RED);
		frame2.setVisible(true);

		try {
			// Activate frame1
			Assert.assertTrue(Win.activate(title1));
			sleep(200);
			Assert.assertEquals(Color.BLACK, Pixel.getColor_(40, 50));
			Assert.assertEquals(Color.RED, Pixel.getColor_(440, 50));

			// Activate frame2
			Assert.assertTrue(Win.activate(title2));
			Assert.assertEquals(Color.BLACK, Pixel.getColor_(40, 50));
			Assert.assertEquals(Color.RED, Pixel.getColor_(440, 50));

			Assert.assertEquals(CoordMode.ABSOLUTE_SCREEN_COORDINATES,
					Opt.setPixelCoordMode(CoordMode.RELATIVE_TO_ACTIVE_WINDOW));

			// Activate frame1
			Assert.assertTrue(Win.activate(title1));
			Assert.assertEquals(Color.BLACK, Pixel.getColor_(40, 50));
			Assert.assertEquals(Color.RED, Pixel.getColor_(440, 50));

			// Activate frame2
			Assert.assertTrue(Win.activate(title2));
			Assert.assertEquals(Color.RED, Pixel.getColor_(40, 50));
		} finally {
			// destroy frame
			frame1.setVisible(false);
			frame2.setVisible(false);

			// Restore pixel coord mode
			Opt.setPixelCoordMode(Opt.DEFAULT_PIXEL_COORD_MODE);
		}
	}

	@Test
	public void setSendAttachMode() {
		// TODO:

		// int pid = runNotepad();

		try {
			// Assert.assertEquals(1,
			// Control.getCurrentLine(NOTEPAD_TITLE, "Edit1"));
			// Keyboard.send("{ENTER}");
			// Assert.assertEquals(2,
			// Control.getCurrentLine(NOTEPAD_TITLE, "Edit1"));
			// Keyboard.send("{ENTER}");
			// Assert.assertEquals(3,
			// Control.getCurrentLine(NOTEPAD_TITLE, "Edit1"));
			// Keyboard.send("{ENTER}");
			// Assert.assertEquals(4,
			// Control.getCurrentLine(NOTEPAD_TITLE, "Edit1"));
			//
			// Keyboard.send("{UP}");
			// Assert.assertEquals(3,
			// Control.getCurrentLine(NOTEPAD_TITLE, "Edit1"));
			// Keyboard.send("{UP}");
			// Assert.assertEquals(2,
			// Control.getCurrentLine(NOTEPAD_TITLE, "Edit1"));
			//
			// Keyboard.send("{DOWN}");
			// Assert.assertEquals(3,
			// Control.getCurrentLine(NOTEPAD_TITLE, "Edit1"));
			// Keyboard.send("{DOWN}");
			// Assert.assertEquals(4,
			// Control.getCurrentLine(NOTEPAD_TITLE, "Edit1"));

			// Keyboard.send("^{SPACE}");

			// when you specify attach mode=true the Send("{... down/up}")
			// syntax will not work
			Assert.assertEquals(Opt.DEFAULT_SEND_ATTACH_MODE,
					Opt.setSendAttachMode(true));

			// Keyboard.send("{a down}");
			// Keyboard.send("{a up}");

			// Keyboard.send("{UP}");
			// Assert.assertEquals(4,
			// Control.getCurrentLine(NOTEPAD_TITLE, "Edit1"));
			// Keyboard.send("{UP}");
			// Assert.assertEquals(4,
			// Control.getCurrentLine(NOTEPAD_TITLE, "Edit1"));

			// closeNotepad(pid);

			Assert.assertTrue(Opt.setSendAttachMode(false));
			Assert.assertFalse(Opt.setSendAttachMode(true));
		} finally {
			// restore default send attach mode
			Opt.setSendAttachMode(Opt.DEFAULT_SEND_ATTACH_MODE);
		}
	}

	@Test
	public void setSendCapslockMode() {
		final boolean isCpaslockOn = Win32.isCapslockOn();

		try {
			Win32.setCapslockState(true);
			Assert.assertTrue(Win32.isCapslockOn());

			Assert.assertEquals(Opt.DEFAULT_SEND_CAPSLOCK_MODE,
					Opt.setSendCapslockMode(true));
			Keyboard.send("{CAPSLOCK}");
			Assert.assertTrue(Win32.isCapslockOn());

			Assert.assertTrue(Opt.setSendCapslockMode(false));
			Keyboard.send("{CAPSLOCK}");
			Assert.assertFalse(Win32.isCapslockOn());

			Assert.assertFalse(Opt.setSendCapslockMode(true));
			Keyboard.send("{CAPSLOCK}");
			Assert.assertFalse(Win32.isCapslockOn());

			Assert.assertTrue(Opt.setSendCapslockMode(false));
			Keyboard.send("{CAPSLOCK}");
			Assert.assertTrue(Win32.isCapslockOn());
		} finally {
			// restore default capslock
			Win32.setCapslockState(isCpaslockOn);

			// restore default capslock mode
			Opt.setSendCapslockMode(Opt.DEFAULT_SEND_CAPSLOCK_MODE);
		}
	}

	@Test
	public void setSendKeyDelay() {
		int pid = runNotepad();

		try {
			long start = System.currentTimeMillis();
			Keyboard.send("1");
			long end = System.currentTimeMillis();
			Assert.assertTrue((end - start) >= 0);
			Assert.assertTrue((end - start) < 500);
			Assert.assertEquals("1", Control.getText(NOTEPAD_TITLE, "Edit1"));

			Assert.assertEquals(Opt.DEFAULT_SEND_KEY_DELAY,
					Opt.setSendKeyDelay(500));
			start = System.currentTimeMillis();
			Keyboard.send("2");
			end = System.currentTimeMillis();
			Assert.assertTrue((end - start) >= 500);
			Assert.assertTrue((end - start) < 2000);
			Assert.assertEquals("12", Control.getText(NOTEPAD_TITLE, "Edit1"));

			Assert.assertEquals(500, Opt.setSendKeyDelay(1000));
			start = System.currentTimeMillis();
			Keyboard.send("3");
			end = System.currentTimeMillis();
			Assert.assertTrue((end - start) >= 1000);
			Assert.assertTrue((end - start) < 4000);
			Assert.assertEquals("123", Control.getText(NOTEPAD_TITLE, "Edit1"));

			Assert.assertEquals(1000, Opt.setSendKeyDelay(0));
			Assert.assertEquals(1, Opt.setSendKeyDelay(20));
			Assert.assertEquals(20, Opt.setSendKeyDelay(-1));
			Assert.assertEquals(1, Opt.setSendKeyDelay(50));
		} finally {
			closeNotepad(pid);

			// restore default send key delay
			Opt.setSendKeyDelay(Opt.DEFAULT_SEND_KEY_DELAY);
		}
	}

	@Test
	public void setSendKeyDownDelay() {
		int pid = runNotepad();

		try {
			long start = System.currentTimeMillis();
			Keyboard.send("{1 down}");
			long end = System.currentTimeMillis();
			Keyboard.send("{1 up}");
			Assert.assertTrue(String.format("(%d - %d) >= 0", end, start),
					(end - start) >= 0);
			Assert.assertTrue(String.format("(%d - %d) < 500", end, start),
					(end - start) < 500);
			Assert.assertEquals("1", Control.getText(NOTEPAD_TITLE, "Edit1"));

			Assert.assertEquals(Opt.DEFAULT_SEND_KEY_DOWN_DELAY,
					Opt.setSendKeyDownDelay(500));
			start = System.currentTimeMillis();
			Keyboard.send("{2 down}");
			end = System.currentTimeMillis();
			Keyboard.send("{2 up}");
			Assert.assertTrue(String.format("(%d - %d) >= 500", end, start),
					(end - start) >= 500);
			Assert.assertTrue(String.format("(%d - %d) < 1000", end, start),
					(end - start) < 1000);
			Assert.assertEquals("12", Control.getText(NOTEPAD_TITLE, "Edit1"));

			Assert.assertEquals(500, Opt.setSendKeyDownDelay(1000));
			start = System.currentTimeMillis();
			Keyboard.send("{3 down}");
			end = System.currentTimeMillis();
			Keyboard.send("{3 up}");
			Assert.assertTrue(String.format("(%d - %d) >= 1000", end, start),
					(end - start) >= 1000);
			Assert.assertTrue(String.format("(%d - %d) < 1500", end, start),
					(end - start) < 1500);
			Assert.assertEquals("123", Control.getText(NOTEPAD_TITLE, "Edit1"));

			Assert.assertEquals(1000, Opt.setSendKeyDownDelay(0));
			Assert.assertEquals(0, Opt.setSendKeyDownDelay(20));
			Assert.assertEquals(20, Opt.setSendKeyDownDelay(-1));
			Assert.assertEquals(-1, Opt.setSendKeyDownDelay(50));

			closeNotepad(pid);
		} finally {
			// restore default send key down delay
			Opt.setSendKeyDownDelay(Opt.DEFAULT_SEND_KEY_DOWN_DELAY);
		}
	}

	@Test
	public void setWinDetectHiddenText() {
		final Frame frame1 = createTestFrame("setWinDetectHiddenText - detect",
				"Hello", true);
		frame1.setVisible(true);
		final Frame frame2 = createTestFrame(
				"setWinDetectHiddenText - no detect", "World", false);
		frame2.setVisible(true);

		try {
			Assert.assertTrue(Win.exists("setWinDetectHiddenText - detect", ""));
			Assert.assertFalse(Win.exists("setWinDetectHiddenText - detect",
					"Hello"));
			Assert.assertTrue(Win.exists("setWinDetectHiddenText - no detect",
					""));
			Assert.assertTrue(Win.exists("setWinDetectHiddenText - no detect",
					"World"));

			Assert.assertEquals(Opt.DEFAULT_WIN_DETECT_HIDDEN_TEXT,
					Opt.setWinDetectHiddenText(true));
			Assert.assertTrue(Win.exists("setWinDetectHiddenText - detect", ""));
			Assert.assertTrue(Win.exists("setWinDetectHiddenText - detect",
					"Hello"));
			Assert.assertTrue(Win.exists("setWinDetectHiddenText - no detect",
					""));
			Assert.assertTrue(Win.exists("setWinDetectHiddenText - no detect",
					"World"));

			Assert.assertTrue(Opt.setWinDetectHiddenText(false));
			Assert.assertTrue(Win.exists("setWinDetectHiddenText - detect", ""));
			Assert.assertFalse(Win.exists("setWinDetectHiddenText - detect",
					"Hello"));
			Assert.assertTrue(Win.exists("setWinDetectHiddenText - no detect",
					""));
			Assert.assertTrue(Win.exists("setWinDetectHiddenText - no detect",
					"World"));

			Assert.assertFalse(Opt.setWinDetectHiddenText(true));
			Assert.assertTrue(Win.exists("setWinDetectHiddenText - detect", ""));
			Assert.assertTrue(Win.exists("setWinDetectHiddenText - detect",
					"Hello"));
			Assert.assertTrue(Win.exists("setWinDetectHiddenText - no detect",
					""));
			Assert.assertTrue(Win.exists("setWinDetectHiddenText - no detect",
					"World"));
		} finally {
			// destroy frame
			frame1.setVisible(false);
			frame2.setVisible(false);

			// restore default win detect hidden text
			Opt.setWinDetectHiddenText(Opt.DEFAULT_WIN_DETECT_HIDDEN_TEXT);
		}
	}

	/**
	 * Create frame to test Opt.setWinDetectHiddenText() method.
	 * 
	 * @param title
	 *            frame title
	 * @param buttonText
	 *            button text
	 * @param hideButton
	 *            whether or not hidden the button
	 * @return
	 */
	private Frame createTestFrame(final String title, final String buttonText,
			final boolean hideButton) {
		Frame frame = new Frame(title);

		final Button button = new Button(buttonText);
		frame.add(button, BorderLayout.CENTER);
		if (hideButton) {
			button.setVisible(false);
		}
		frame.setSize(400, 300);

		return frame;
	}

	@Test
	public void setWinSearchChildren() {
		try {
			Assert.assertEquals(Opt.DEFAULT_WIN_SEARCH_CHILDREN,
					Opt.setWinSearchChildren(true));
			Assert.assertTrue(Opt.setWinSearchChildren(false));
			Assert.assertFalse(Opt.setWinSearchChildren(true));
			Assert.assertTrue(Opt.setWinSearchChildren(false));
			Assert.assertFalse(Opt.setWinSearchChildren(true));
		} finally {
			// restore default win search childrenOpt
			Opt.setWinSearchChildren(Opt.DEFAULT_WIN_SEARCH_CHILDREN);
		}
	}

	@Test
	public void setWinTextMatchMode() {
		try {
			Assert.assertEquals(Opt.DEFAULT_WIN_TEXT_MATCH_MODE,
					Opt.setWinTextMatchMode(WinTextMatchMode.SLOW));
			Assert.assertEquals(WinTextMatchMode.SLOW,
					Opt.setWinTextMatchMode(WinTextMatchMode.QUICK));
			Assert.assertEquals(WinTextMatchMode.QUICK,
					Opt.setWinTextMatchMode(WinTextMatchMode.SLOW));
			Assert.assertEquals(WinTextMatchMode.SLOW,
					Opt.setWinTextMatchMode(WinTextMatchMode.QUICK));
			Assert.assertEquals(WinTextMatchMode.QUICK,
					Opt.setWinTextMatchMode(WinTextMatchMode.SLOW));
		} finally {
			// restore default win text match mode
			Opt.setWinTextMatchMode(Opt.DEFAULT_WIN_TEXT_MATCH_MODE);
		}
	}

	@Test
	public void setWinTitleMatchMode() {
		try {
			Assert.assertEquals(Opt.DEFAULT_WIN_TITLE_MATCH_MODE,
					Opt.setWinTitleMatchMode(WinTitleMatchMode.EXACT));
			Assert.assertEquals(WinTitleMatchMode.EXACT,
					Opt.setWinTitleMatchMode(WinTitleMatchMode.ADVANCED));
			Assert.assertEquals(WinTitleMatchMode.ADVANCED,
					Opt.setWinTitleMatchMode(WinTitleMatchMode.ANY));
			Assert.assertEquals(WinTitleMatchMode.ANY,
					Opt.setWinTitleMatchMode(WinTitleMatchMode.EXACT));
			Assert.assertEquals(WinTitleMatchMode.EXACT,
					Opt.setWinTitleMatchMode(WinTitleMatchMode.ADVANCED));

			int pid = runNotepad();
			Assert.assertTrue(Win.active(TitleBuilder.byTitle(NOTEPAD_TITLE)));
			Assert.assertTrue(Win.active(TitleBuilder
					.byClassName(NOTEPAAD_CLASS_NAME)));
			Assert.assertTrue(Win.active(NOTEPAD_TITLE));
			Assert.assertTrue(Win.active(NOTEPAD_TITLE_START));
			Assert.assertFalse(Win.active(NOTEPAD_TITLE_ANY));
			Assert.assertFalse(Win.active(NOTEPAD_TITLE_END));

			Assert.assertEquals(WinTitleMatchMode.ADVANCED,
					Opt.setWinTitleMatchMode(WinTitleMatchMode.START));
			Assert.assertTrue(Win.active(NOTEPAD_TITLE));
			Assert.assertTrue(Win.active(NOTEPAD_TITLE_START));
			Assert.assertFalse(Win.active(NOTEPAD_TITLE_ANY));
			Assert.assertFalse(Win.active(NOTEPAD_TITLE_END));

			Assert.assertEquals(WinTitleMatchMode.START,
					Opt.setWinTitleMatchMode(WinTitleMatchMode.ANY));
			Assert.assertTrue(Win.active(NOTEPAD_TITLE));
			Assert.assertTrue(Win.active(NOTEPAD_TITLE_START));
			Assert.assertTrue(Win.active(NOTEPAD_TITLE_ANY));
			Assert.assertTrue(Win.active(NOTEPAD_TITLE_END));

			Assert.assertEquals(WinTitleMatchMode.ANY,
					Opt.setWinTitleMatchMode(WinTitleMatchMode.EXACT));
			Assert.assertTrue(Win.active(NOTEPAD_TITLE));
			Assert.assertFalse(Win.active(NOTEPAD_TITLE_START));
			Assert.assertFalse(Win.active(NOTEPAD_TITLE_ANY));
			Assert.assertFalse(Win.active(NOTEPAD_TITLE_END));

			closeNotepad(pid);
		} finally {
			// restore default win title match mode
			Opt.setWinTitleMatchMode(Opt.DEFAULT_WIN_TITLE_MATCH_MODE);
		}
	}

	@Test
	public void setWinWaitDelay() {
		try {
			// delay 5 seconds
			Assert.assertEquals(Opt.DEFAULT_WIN_WAIT_DELAY,
					Opt.setWinWaitDelay(3000));
			final LongHolder time = new LongHolder(0);
			Thread thread = new Thread(new Runnable() {
				public void run() {
					long start = System.currentTimeMillis();
					Win.wait(NOTEPAD_TITLE);
					long end = System.currentTimeMillis();
					time.value = end - start;
				}
			});
			thread.start();
			runNotepad();
			sleep(4000);
			Assert.assertTrue(String.format("%d >= 3000", time.value),
					time.value >= 3000);
			Assert.assertTrue(String.format("%d <= 4000", time.value),
					time.value <= 4000);
			Assert.assertFalse(thread.isAlive());
			Win.close(NOTEPAD_TITLE);

			// delay 2 seconds
			Assert.assertEquals(3000, Opt.setWinWaitDelay(2000));
			time.value = 0;
			thread = new Thread(new Runnable() {
				public void run() {
					long start = System.currentTimeMillis();
					Win.wait(NOTEPAD_TITLE);
					long end = System.currentTimeMillis();
					time.value = end - start;
				}
			});
			thread.start();
			runNotepad();
			while(thread.isAlive()) {
				sleep(100);
			}
			Assert.assertTrue(String.format("%d >= 2000", time.value),
					time.value >= 2000);
			Assert.assertTrue(String.format("%d <= 3000", time.value),
					time.value <= 3000);
			Assert.assertFalse(thread.isAlive());
			Win.close(NOTEPAD_TITLE);
		} finally {
			// restore default win delay
			Opt.setWinWaitDelay(Opt.DEFAULT_WIN_WAIT_DELAY);
		}
	}
}
