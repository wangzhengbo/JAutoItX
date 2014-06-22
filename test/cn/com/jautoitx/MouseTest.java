package cn.com.jautoitx;

import java.awt.Cursor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

import org.junit.Assert;
import org.junit.Test;
import org.omg.CORBA.IntHolder;

import cn.com.jautoitx.Mouse.MouseButton;
import cn.com.jautoitx.Mouse.MouseCursor;
import cn.com.jautoitx.Mouse.MouseWheelDirection;

public class MouseTest extends BaseTest {

	@Test
	public void click() {
		JFrame frame = new JFrame("click - " + System.currentTimeMillis());
		JButton button = new JButton("Click Me");
		frame.getContentPane().add(button);
		frame.setBounds(0, 0, 400, 300);

		// add click listener to textArea
		final IntHolder leftMouseClickCount = new IntHolder();
		final IntHolder middleMouseClickCount = new IntHolder();
		final IntHolder rightMouseClickCount = new IntHolder();
		button.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (SwingUtilities.isLeftMouseButton(e)) {
					leftMouseClickCount.value++;
				} else if (SwingUtilities.isMiddleMouseButton(e)) {
					middleMouseClickCount.value++;
				} else if (SwingUtilities.isRightMouseButton(e)) {
					rightMouseClickCount.value++;
				}
			}
		});

		// show frame
		frame.setVisible(true);

		// move mouse to the button
		Mouse.move(200, 150);

		try {
			// left mouse click 4 times
			for (int i = 0; i < 4; i++) {
				Mouse.click(MouseButton.LEFT, 200, 150);
				sleep(200);
				Assert.assertEquals(i + 1, leftMouseClickCount.value);
				Assert.assertEquals(0, middleMouseClickCount.value);
				Assert.assertEquals(0, rightMouseClickCount.value);
			}

			// middle mouse click 4 times
			for (int i = 0; i < 4; i++) {
				Mouse.click(MouseButton.MIDDLE);
				sleep(200);
				Assert.assertEquals(4, leftMouseClickCount.value);
				Assert.assertEquals(i + 1, middleMouseClickCount.value);
				Assert.assertEquals(0, rightMouseClickCount.value);
			}

			// right mouse click 4 times
			for (int i = 0; i < 4; i++) {
				Mouse.click(MouseButton.RIGHT);
				sleep(200);
				Assert.assertEquals(4, leftMouseClickCount.value);
				Assert.assertEquals(4, middleMouseClickCount.value);
				Assert.assertEquals(i + 1, rightMouseClickCount.value);
			}

			Assert.assertTrue(Mouse.click("left", 200, 150, 2));
			sleep(200);
			Assert.assertEquals(6, leftMouseClickCount.value);
			Assert.assertEquals(4, middleMouseClickCount.value);
			Assert.assertEquals(4, rightMouseClickCount.value);

			Assert.assertTrue(Mouse.click("middle", 200, 150, 3, 20));
			sleep(200);
			Assert.assertEquals(6, leftMouseClickCount.value);
			Assert.assertEquals(7, middleMouseClickCount.value);
			Assert.assertEquals(4, rightMouseClickCount.value);

			Assert.assertTrue(Mouse.click("right", 200, 150, 4, 0));
			sleep(200);
			Assert.assertEquals(6, leftMouseClickCount.value);
			Assert.assertEquals(7, middleMouseClickCount.value);
			Assert.assertEquals(8, rightMouseClickCount.value);

			Mouse.click();
			sleep(200);
			Assert.assertEquals(7, leftMouseClickCount.value);
			Assert.assertEquals(7, middleMouseClickCount.value);
			Assert.assertEquals(8, rightMouseClickCount.value);

			Assert.assertTrue(Mouse.click((String) null));
			sleep(200);
			Assert.assertEquals(8, leftMouseClickCount.value);
			Assert.assertEquals(7, middleMouseClickCount.value);
			Assert.assertEquals(8, rightMouseClickCount.value);

			Assert.assertTrue(Mouse.click(""));
			sleep(200);
			Assert.assertEquals(9, leftMouseClickCount.value);
			Assert.assertEquals(7, middleMouseClickCount.value);
			Assert.assertEquals(8, rightMouseClickCount.value);

			Assert.assertFalse(Mouse.click(" "));
			sleep(200);
			Assert.assertEquals(9, leftMouseClickCount.value);
			Assert.assertEquals(7, middleMouseClickCount.value);
			Assert.assertEquals(8, rightMouseClickCount.value);

			Assert.assertFalse(Mouse.click("xxxx", 200, 150, 4, 0));
			sleep(200);
			Assert.assertEquals(9, leftMouseClickCount.value);
			Assert.assertEquals(7, middleMouseClickCount.value);
			Assert.assertEquals(8, rightMouseClickCount.value);
		} finally {
			// destroy frame
			frame.setVisible(false);
		}
	}

	@Test
	public void clickDrag() {
		String title = "clickDrag - " + System.currentTimeMillis();
		JFrame frame = new JFrame(title);
		frame.setSize(400, 300);

		try {
			// show frame
			frame.setVisible(true);
			Assert.assertTrue(Win.wait(title, 3));

			Assert.assertTrue(Mouse.clickDrag(50, 60, 70, 80));
			Assert.assertTrue(Mouse.clickDrag(MouseButton.LEFT, 50, 60, 70, 80));
			Assert.assertTrue(Mouse.clickDrag("left", 50, 60, 70, 80));
			Assert.assertTrue(Mouse.clickDrag((String) null, 50, 60, 70, 80));
			Assert.assertFalse(Mouse.clickDrag("xxx", 50, 60, 70, 80));
		} finally {
			// destroy frame
			destroyFrame(frame);
		}
	}

	@Test
	public void down() {
		JFrame frame = new JFrame("down - " + System.currentTimeMillis());
		JButton button = new JButton("Click Me");
		frame.getContentPane().add(button);
		frame.setBounds(0, 0, 400, 300);

		// add click listener to textArea
		final IntHolder leftMouseUpCount = new IntHolder();
		final IntHolder leftMouseDownCount = new IntHolder();
		final IntHolder middleMouseUpCount = new IntHolder();
		final IntHolder middleMouseDownCount = new IntHolder();
		final IntHolder rightMouseUpCount = new IntHolder();
		final IntHolder rightMouseDownCount = new IntHolder();
		button.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if (SwingUtilities.isLeftMouseButton(e)) {
					leftMouseDownCount.value++;
				} else if (SwingUtilities.isMiddleMouseButton(e)) {
					middleMouseDownCount.value++;
				} else if (SwingUtilities.isRightMouseButton(e)) {
					rightMouseDownCount.value++;
				}
			}

			public void mouseReleased(MouseEvent e) {
				if (SwingUtilities.isLeftMouseButton(e)) {
					leftMouseUpCount.value++;
				} else if (SwingUtilities.isMiddleMouseButton(e)) {
					middleMouseUpCount.value++;
				} else if (SwingUtilities.isRightMouseButton(e)) {
					rightMouseUpCount.value++;
				}
			}
		});

		// show frame
		frame.setVisible(true);

		// move mouse to the button
		Mouse.move(200, 150);

		try {
			// left mouse click 4 times
			for (int i = 0; i < 4; i++) {
				Mouse.down("left");
				sleep(200);
				Assert.assertEquals(i, leftMouseUpCount.value);
				Assert.assertEquals(i + 1, leftMouseDownCount.value);
				Assert.assertEquals(0, middleMouseUpCount.value);
				Assert.assertEquals(0, middleMouseDownCount.value);
				Assert.assertEquals(0, rightMouseUpCount.value);
				Assert.assertEquals(0, rightMouseDownCount.value);

				Mouse.up("left");
				sleep(200);
				Assert.assertEquals(i + 1, leftMouseUpCount.value);
				Assert.assertEquals(i + 1, leftMouseDownCount.value);
				Assert.assertEquals(0, middleMouseUpCount.value);
				Assert.assertEquals(0, middleMouseDownCount.value);
				Assert.assertEquals(0, rightMouseUpCount.value);
				Assert.assertEquals(0, rightMouseDownCount.value);
			}

			// middle mouse click 4 times
			for (int i = 0; i < 4; i++) {
				Mouse.down("middle");
				sleep(200);
				Assert.assertEquals(4, leftMouseUpCount.value);
				Assert.assertEquals(4, leftMouseDownCount.value);
				Assert.assertEquals(i, middleMouseUpCount.value);
				Assert.assertEquals(i + 1, middleMouseDownCount.value);
				Assert.assertEquals(0, rightMouseUpCount.value);
				Assert.assertEquals(0, rightMouseDownCount.value);

				Mouse.up("middle");
				sleep(200);
				Assert.assertEquals(4, leftMouseUpCount.value);
				Assert.assertEquals(4, leftMouseDownCount.value);
				Assert.assertEquals(i + 1, middleMouseUpCount.value);
				Assert.assertEquals(i + 1, middleMouseDownCount.value);
				Assert.assertEquals(0, rightMouseUpCount.value);
				Assert.assertEquals(0, rightMouseDownCount.value);
			}

			// right mouse click 4 times
			for (int i = 0; i < 4; i++) {
				Mouse.down("right");
				sleep(200);
				Assert.assertEquals(4, leftMouseUpCount.value);
				Assert.assertEquals(4, leftMouseDownCount.value);
				Assert.assertEquals(4, middleMouseUpCount.value);
				Assert.assertEquals(4, middleMouseDownCount.value);
				Assert.assertEquals(i, rightMouseUpCount.value);
				Assert.assertEquals(i + 1, rightMouseDownCount.value);

				Mouse.up("right");
				sleep(200);
				Assert.assertEquals(4, leftMouseUpCount.value);
				Assert.assertEquals(4, leftMouseDownCount.value);
				Assert.assertEquals(4, middleMouseUpCount.value);
				Assert.assertEquals(4, middleMouseDownCount.value);
				Assert.assertEquals(i + 1, rightMouseUpCount.value);
				Assert.assertEquals(i + 1, rightMouseDownCount.value);
			}
		} finally {
			// destroy frame
			frame.setVisible(false);
		}
	}

	@Test
	public void getCursor() {
		// TODO:
		JFrame frame = new JFrame("mouseGetCursor - "
				+ System.currentTimeMillis());
		JButton button = new JButton("Click Me");
		frame.getContentPane().add(button);
		frame.setBounds(0, 0, 400, 300);

		Cursor defaultCursor = button.getCursor();

		// show frame
		frame.setVisible(true);

		// move mouse to the button
		Mouse.move(200, 150);

		try {
			button.setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
			sleep(100);
			Assert.assertEquals(MouseCursor.CROSS, Mouse.getCursor());

			button.setCursor(new Cursor(Cursor.TEXT_CURSOR));
			sleep(100);
			Assert.assertEquals(MouseCursor.IBEAM, Mouse.getCursor());

			button.setCursor(new Cursor(Cursor.WAIT_CURSOR));
			sleep(100);
			Assert.assertEquals(MouseCursor.WAIT, Mouse.getCursor());

			button.setCursor(new Cursor(Cursor.SW_RESIZE_CURSOR));
			sleep(100);
			Assert.assertEquals(MouseCursor.SIZENESW, Mouse.getCursor());

			button.setCursor(new Cursor(Cursor.SE_RESIZE_CURSOR));
			sleep(100);
			Assert.assertEquals(MouseCursor.SIZENWSE, Mouse.getCursor());

			button.setCursor(new Cursor(Cursor.NW_RESIZE_CURSOR));
			sleep(100);
			Assert.assertEquals(MouseCursor.SIZENWSE, Mouse.getCursor());

			button.setCursor(new Cursor(Cursor.NE_RESIZE_CURSOR));
			sleep(100);
			Assert.assertEquals(MouseCursor.SIZENESW, Mouse.getCursor());

			button.setCursor(new Cursor(Cursor.N_RESIZE_CURSOR));
			sleep(100);
			Assert.assertEquals(MouseCursor.SIZENS, Mouse.getCursor());

			button.setCursor(new Cursor(Cursor.S_RESIZE_CURSOR));
			sleep(100);
			Assert.assertEquals(MouseCursor.SIZENS, Mouse.getCursor());

			button.setCursor(new Cursor(Cursor.W_RESIZE_CURSOR));
			sleep(100);
			Assert.assertEquals(MouseCursor.SIZEWE, Mouse.getCursor());

			button.setCursor(new Cursor(Cursor.E_RESIZE_CURSOR));
			sleep(100);
			Assert.assertEquals(MouseCursor.SIZEWE, Mouse.getCursor());

			button.setCursor(new Cursor(Cursor.HAND_CURSOR));
			sleep(100);
			Assert.assertEquals(MouseCursor.UNKNOWN, Mouse.getCursor());

			button.setCursor(new Cursor(Cursor.MOVE_CURSOR));
			sleep(100);
			Assert.assertEquals(MouseCursor.SIZEALL, Mouse.getCursor());

			button.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			sleep(100);
			Assert.assertEquals(MouseCursor.ARROW, Mouse.getCursor());

			// restore button's default cursor
			button.setCursor(defaultCursor);
			Assert.assertSame(defaultCursor, button.getCursor());
		} finally {
			// destroy frame
			frame.setVisible(false);
		}
	}

	@Test
	public void getX() {
		Mouse.move(10, 20);
		Assert.assertEquals(10, Mouse.getPosX());
		sleep(100);

		Mouse.move(100, 200, 0);
		Assert.assertEquals(100, Mouse.getPosX());
		sleep(100);

		Mouse.move(-100, -200, 50);
		Assert.assertEquals(0, Mouse.getPosX());
		sleep(100);

		Mouse.move(10000, 20000);
		Assert.assertEquals(Win.getDesktopWidth() - 1, Mouse.getPosX());
		sleep(100);

		Mouse.move(0, 0);
		Assert.assertEquals(0, Mouse.getPosX());
	}

	@Test
	public void getY() {
		Mouse.move(10, 20);
		Assert.assertEquals(20, Mouse.getPosY());
		sleep(100);

		Mouse.move(100, 200, 0);
		Assert.assertEquals(200, Mouse.getPosY());
		sleep(100);

		Mouse.move(-100, -200, 50);
		Assert.assertEquals(0, Mouse.getPosY());
		sleep(100);

		Mouse.move(10000, 20000);
		Assert.assertEquals(Win.getDesktopHeight() - 1, Mouse.getPosY());
		sleep(100);

		Mouse.move(0, 0);
		Assert.assertEquals(0, Mouse.getPosY());
	}

	@Test
	public void move() {
		Mouse.move(10, 20);
		Assert.assertEquals(10, Mouse.getPosX());
		Assert.assertEquals(20, Mouse.getPosY());
		sleep(100);

		Mouse.move(100, 200, 0);
		Assert.assertEquals(100, Mouse.getPosX());
		Assert.assertEquals(200, Mouse.getPosY());
		sleep(100);

		Mouse.move(-100, -200, 50);
		Assert.assertEquals(0, Mouse.getPosX());
		Assert.assertEquals(0, Mouse.getPosY());
		sleep(100);

		Mouse.move(10000, 20000);
		Assert.assertEquals(Win.getDesktopWidth() - 1, Mouse.getPosX());
		Assert.assertEquals(Win.getDesktopHeight() - 1, Mouse.getPosY());
		sleep(100);

		Mouse.move(0, 0);
		Assert.assertEquals(0, Mouse.getPosX());
		Assert.assertEquals(0, Mouse.getPosY());
	}

	@Test
	public void up() {
		JFrame frame = new JFrame("mouseUp - " + System.currentTimeMillis());
		JButton button = new JButton("Click Me");
		frame.getContentPane().add(button);
		frame.setBounds(0, 0, 400, 300);

		// add click listener to textArea
		final IntHolder leftMouseUpCount = new IntHolder();
		final IntHolder leftMouseDownCount = new IntHolder();
		final IntHolder middleMouseUpCount = new IntHolder();
		final IntHolder middleMouseDownCount = new IntHolder();
		final IntHolder rightMouseUpCount = new IntHolder();
		final IntHolder rightMouseDownCount = new IntHolder();
		button.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if (SwingUtilities.isLeftMouseButton(e)) {
					leftMouseDownCount.value++;
				} else if (SwingUtilities.isMiddleMouseButton(e)) {
					middleMouseDownCount.value++;
				} else if (SwingUtilities.isRightMouseButton(e)) {
					rightMouseDownCount.value++;
				}
			}

			public void mouseReleased(MouseEvent e) {
				if (SwingUtilities.isLeftMouseButton(e)) {
					leftMouseUpCount.value++;
				} else if (SwingUtilities.isMiddleMouseButton(e)) {
					middleMouseUpCount.value++;
				} else if (SwingUtilities.isRightMouseButton(e)) {
					rightMouseUpCount.value++;
				}
			}
		});

		// show frame
		frame.setVisible(true);

		// move mouse to the button
		Mouse.move(200, 150);

		try {
			// left mouse click 4 times
			for (int i = 0; i < 4; i++) {
				Mouse.down(MouseButton.LEFT);
				sleep(500);
				Assert.assertEquals(i, leftMouseUpCount.value);
				Assert.assertEquals(i + 1, leftMouseDownCount.value);
				Assert.assertEquals(0, middleMouseUpCount.value);
				Assert.assertEquals(0, middleMouseDownCount.value);
				Assert.assertEquals(0, rightMouseUpCount.value);
				Assert.assertEquals(0, rightMouseDownCount.value);

				Mouse.up(MouseButton.LEFT);
				sleep(500);
				Assert.assertEquals(i + 1, leftMouseUpCount.value);
				Assert.assertEquals(i + 1, leftMouseDownCount.value);
				Assert.assertEquals(0, middleMouseUpCount.value);
				Assert.assertEquals(0, middleMouseDownCount.value);
				Assert.assertEquals(0, rightMouseUpCount.value);
				Assert.assertEquals(0, rightMouseDownCount.value);
			}

			// middle mouse click 4 times
			for (int i = 0; i < 4; i++) {
				Mouse.down(MouseButton.MIDDLE);
				sleep(500);
				Assert.assertEquals(4, leftMouseUpCount.value);
				Assert.assertEquals(4, leftMouseDownCount.value);
				Assert.assertEquals(i, middleMouseUpCount.value);
				Assert.assertEquals(i + 1, middleMouseDownCount.value);
				Assert.assertEquals(0, rightMouseUpCount.value);
				Assert.assertEquals(0, rightMouseDownCount.value);

				Mouse.up(MouseButton.MIDDLE);
				sleep(500);
				Assert.assertEquals(4, leftMouseUpCount.value);
				Assert.assertEquals(4, leftMouseDownCount.value);
				Assert.assertEquals(i + 1, middleMouseUpCount.value);
				Assert.assertEquals(i + 1, middleMouseDownCount.value);
				Assert.assertEquals(0, rightMouseUpCount.value);
				Assert.assertEquals(0, rightMouseDownCount.value);
			}

			// right mouse click 4 times
			for (int i = 0; i < 4; i++) {
				Mouse.down(MouseButton.RIGHT);
				sleep(500);
				Assert.assertEquals(4, leftMouseUpCount.value);
				Assert.assertEquals(4, leftMouseDownCount.value);
				Assert.assertEquals(4, middleMouseUpCount.value);
				Assert.assertEquals(4, middleMouseDownCount.value);
				Assert.assertEquals(i, rightMouseUpCount.value);
				Assert.assertEquals(i + 1, rightMouseDownCount.value);

				Mouse.up(MouseButton.RIGHT);
				sleep(500);
				Assert.assertEquals(4, leftMouseUpCount.value);
				Assert.assertEquals(4, leftMouseDownCount.value);
				Assert.assertEquals(4, middleMouseUpCount.value);
				Assert.assertEquals(4, middleMouseDownCount.value);
				Assert.assertEquals(i + 1, rightMouseUpCount.value);
				Assert.assertEquals(i + 1, rightMouseDownCount.value);
			}
		} finally {
			// destroy frame
			frame.setVisible(false);
		}
	}

	@Test
	public void wheel() {
		JFrame frame = new JFrame("wheel - " + System.currentTimeMillis());
		JTextArea textArea = new JTextArea();

		// set textArea's text to a long String so it can scroll
		StringBuilder text = new StringBuilder();
		for (int i = 0; i < 50; i++) {
			text.append(i).append("\n");
		}
		textArea.setText(text.toString());

		JScrollPane scrollPane = new JScrollPane(textArea);
		frame.getContentPane().add(scrollPane);
		frame.setBounds(0, 0, 400, 300);

		// add mouse wheel listener to textArea
		final IntHolder wheelCount = new IntHolder();
		final IntHolder wheelUpCount = new IntHolder();
		final IntHolder wheelDownCount = new IntHolder();
		textArea.addMouseWheelListener(new MouseWheelListener() {
			public void mouseWheelMoved(MouseWheelEvent e) {
				wheelCount.value++;
				if (e.getWheelRotation() < 0) {
					wheelUpCount.value++;
				} else {
					wheelDownCount.value++;
				}
			}
		});

		// show frame
		frame.setVisible(true);

		// move mouse to the center of the frame
		Mouse.move(200, 150);

		// focus textArea
		Mouse.click(MouseButton.LEFT);

		try {
			// scroll down 5 times
			for (int i = 0; i < 4; i++) {
				Mouse.wheel(MouseWheelDirection.DOWN);
				sleep(1000);
				Assert.assertEquals(i + 1, wheelCount.value);
				Assert.assertEquals(0, wheelUpCount.value);
				Assert.assertEquals(i + 1, wheelDownCount.value);
			}

			// scroll up 5 times
			for (int i = 0; i < 4; i++) {
				Mouse.wheel(MouseWheelDirection.UP);
				sleep(1000);

				Assert.assertEquals(4 + (i + 1), wheelCount.value);
				Assert.assertEquals(i + 1, wheelUpCount.value);
				Assert.assertEquals(4, wheelDownCount.value);
			}

			// scroll down
			Assert.assertTrue(Mouse.wheel(Mouse.MOUSE_WHEEL_DIRECTION_DOWN));
			sleep(1000);
			Assert.assertEquals(9, wheelCount.value);
			Assert.assertEquals(4, wheelUpCount.value);
			Assert.assertEquals(5, wheelDownCount.value);

			// scroll up
			Assert.assertTrue(Mouse.wheel(Mouse.MOUSE_WHEEL_DIRECTION_UP));
			sleep(1000);
			Assert.assertEquals(10, wheelCount.value);
			Assert.assertEquals(5, wheelUpCount.value);
			Assert.assertEquals(5, wheelDownCount.value);

			// scroll with invalid direction
			Assert.assertFalse(Mouse.wheel("left"));
			sleep(1000);
			Assert.assertEquals(10, wheelCount.value);
			Assert.assertEquals(5, wheelUpCount.value);
			Assert.assertEquals(5, wheelDownCount.value);
		} finally {
			// destroy frame
			frame.setVisible(false);
		}
	}
}
