package cn.com.jautoitx;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Choice;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.TextArea;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.SwingUtilities;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.junit.Assert;
import org.junit.Test;
import org.omg.CORBA.IntHolder;

import cn.com.jautoitx.Control.ControlClickMouseButton;
import cn.com.jautoitx.Process.RunShowFlag;
import cn.com.jautoitx.Win32.User32Ext;

public class ControlTest extends BaseTest {
	@Test
	public void click() {
		String title = "click - " + currentTimeMillis;
		Frame frame = new Frame(title);
		frame.setBounds(0, 0, 400, 300);

		// add a button to frame
		Button button = new Button("Click Me");
		frame.add(button);

		// add click listener to button
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
		Assert.assertTrue(Win.wait(title, 3));

		try {
			// left mouse click 4 times
			for (int i = 0; i < 4; i++) {
				Assert.assertTrue(Control.click(title, "Button1",
						ControlClickMouseButton.LEFT));
				sleep(200);
				assertEquals(i + 1, leftMouseClickCount.value);
				assertEquals(0, middleMouseClickCount.value);
				assertEquals(0, rightMouseClickCount.value);
			}

			// middle mouse click 4 times
			for (int i = 0; i < 4; i++) {
				Assert.assertTrue(Control.click(title, "Button1",
						ControlClickMouseButton.MIDDLE));
				sleep(200);
				assertEquals(4, leftMouseClickCount.value);
				assertEquals(i + 1, middleMouseClickCount.value);
				assertEquals(0, rightMouseClickCount.value);
			}

			// right mouse click 4 times
			for (int i = 0; i < 4; i++) {
				Assert.assertTrue(Control.click(title, "Button1",
						ControlClickMouseButton.RIGHT));
				sleep(200);
				assertEquals(4, leftMouseClickCount.value);
				assertEquals(4, middleMouseClickCount.value);
				assertEquals(i + 1, rightMouseClickCount.value);
			}

			Assert.assertTrue(Control.click(title, null, "Button1", "left", 2));
			sleep(200);
			assertEquals(6, leftMouseClickCount.value);
			assertEquals(4, middleMouseClickCount.value);
			assertEquals(4, rightMouseClickCount.value);

			Assert.assertTrue(Control
					.click(title, null, "Button1", "middle", 3));
			sleep(200);
			assertEquals(6, leftMouseClickCount.value);
			assertEquals(7, middleMouseClickCount.value);
			assertEquals(4, rightMouseClickCount.value);

			Assert.assertTrue(Control.click(title, null, "Button1", "right", 4));
			sleep(200);
			assertEquals(6, leftMouseClickCount.value);
			assertEquals(7, middleMouseClickCount.value);
			assertEquals(8, rightMouseClickCount.value);

			Assert.assertTrue(Control.click(title, "Button1"));
			sleep(200);
			assertEquals(7, leftMouseClickCount.value);
			assertEquals(7, middleMouseClickCount.value);
			assertEquals(8, rightMouseClickCount.value);

			Assert.assertTrue(Control.click(title, null, "Button1",
					(String) null));
			sleep(200);
			assertEquals(8, leftMouseClickCount.value);
			assertEquals(7, middleMouseClickCount.value);
			assertEquals(8, rightMouseClickCount.value);

			Assert.assertTrue(Control.click(title, null, "Button1", ""));
			sleep(200);
			assertEquals(9, leftMouseClickCount.value);
			assertEquals(7, middleMouseClickCount.value);
			assertEquals(8, rightMouseClickCount.value);

			// invalid mouse buttons
			Assert.assertFalse(Control.click(title, null, "Button1", " "));
			sleep(200);
			assertEquals(9, leftMouseClickCount.value);
			assertEquals(7, middleMouseClickCount.value);
			assertEquals(8, rightMouseClickCount.value);

			// invalid mouse buttons
			Assert.assertFalse(Control.click(title, null, "Button1", "xxxx"));
			sleep(200);
			assertEquals(9, leftMouseClickCount.value);
			assertEquals(7, middleMouseClickCount.value);
			assertEquals(8, rightMouseClickCount.value);

			// click on not exists button
			Assert.assertFalse(Control.click(title, null,
					"Button1" + System.currentTimeMillis(), "left"));
			sleep(200);
			assertEquals(9, leftMouseClickCount.value);
			assertEquals(7, middleMouseClickCount.value);
			assertEquals(8, rightMouseClickCount.value);
		} finally {
			// destroy frame
			frame.setVisible(false);
		}
	}

	@Test
	public void isVisible() {
		final String title = "isVisible - " + currentTimeMillis;
		Thread thread = new Thread(new Runnable() {
			public void run() {
				User32Ext.INSTANCE.MessageBox(null, "How are you?", title, 0);
			}
		});
		thread.start();
		Assert.assertTrue(Win.wait(title, 3));
		Assert.assertTrue(Control.isVisible(title, "Button1"));

		// hide button
		Assert.assertTrue(Control.hide(title, "Button1"));
		Assert.assertFalse(Control.isVisible(title, "Button1"));

		// show button
		Assert.assertTrue(Control.show(title, "Button1"));
		Assert.assertTrue(Control.isVisible(title, "Button1"));
		Assert.assertFalse(Control.isVisible(title,
				"Button1" + System.currentTimeMillis()));

		// close message box
		Assert.assertTrue(Win.close(title));

		Assert.assertFalse(Control.isVisible(title, "Button1"));
	}

	@Test
	public void isEnabled() {
		final String title = "isEnabled - " + currentTimeMillis;
		Thread thread = new Thread(new Runnable() {
			public void run() {
				User32Ext.INSTANCE.MessageBox(null, "How are you?", title, 0);
			}
		});
		thread.start();
		Assert.assertTrue(Win.wait(title, 3));
		Assert.assertTrue(Control.isEnabled(title, "Button1"));

		// disable button
		Assert.assertTrue(Control.disable(title, "Button1"));
		Assert.assertFalse(Control.isEnabled(title, "Button1"));

		// enable button
		Assert.assertTrue(Control.enable(title, "Button1"));
		Assert.assertTrue(Control.isEnabled(title, "Button1"));
		Assert.assertFalse(Control.isEnabled(title,
				"Button1" + System.currentTimeMillis()));

		// close message box
		Assert.assertTrue(Win.close(title));

		Assert.assertFalse(Control.isEnabled(title, "Button1"));
	}

	@Test
	public void showDropDown() {
		String title = "showDropDown - " + currentTimeMillis;
		Frame frame = new Frame(title);
		frame.setLayout(new FlowLayout());
		frame.setBounds(0, 0, 400, 300);
		Assert.assertFalse(Control.showDropDown(title, "ComboBox1"));

		// add a dropdown-list to frame
		Choice choice = new Choice();
		choice.add("Java");
		choice.add("Lisp");
		choice.add("Tcl");
		choice.add("Smalltalk");
		frame.add(choice);

		// show frame
		frame.setVisible(true);
		Assert.assertTrue(Win.wait(title, 3));

		try {
			// TOOD:
			// show dropdown
			// System.out.println(Win.getText(title));
			// System.out.println(Control.getText(title, "ComboBox1"));
			Assert.assertTrue(Control.showDropDown(title, "ComboBox1"));
			// System.out.println(Win.getText(title));
			// System.out.println(Control.getText(title, "ComboBox1"));
			sleep(200);

			// hide dropdown
			Assert.assertTrue(Control.hideDropDown(title, "ComboBox1"));
			sleep(200);

			// show not exists dropdown
			Assert.assertFalse(Control.showDropDown(title,
					"ComboBox1" + System.currentTimeMillis()));
		} finally {
			// destroy frame
			frame.setVisible(false);
		}
	}

	@Test
	public void hideDropDown() {
		String title = "hideDropDown - " + currentTimeMillis;
		Frame frame = new Frame(title);
		frame.setLayout(new FlowLayout());
		frame.setBounds(0, 0, 400, 300);
		Assert.assertFalse(Control.hideDropDown(title, "ComboBox1"));

		// add a dropdown-list to frame
		Choice choice = new Choice();
		choice.add("Java");
		choice.add("Lisp");
		choice.add("Tcl");
		choice.add("Smalltalk");
		frame.add(choice);

		// show frame
		frame.setVisible(true);
		Assert.assertTrue(Win.wait(title, 3));

		try {
			// TOOD:
			// show dropdown
			// System.out.println(Win.getText(title));
			// System.out.println(Control.getText(title, "ComboBox1"));
			Assert.assertTrue(Control.showDropDown(title, "ComboBox1"));
			// System.out.println(Win.getText(title));
			// System.out.println(Control.getText(title, "ComboBox1"));
			sleep(200);

			// hide dropdown
			Assert.assertTrue(Control.hideDropDown(title, "ComboBox1"));
			sleep(200);

			// show dropdown
			Assert.assertTrue(Control.showDropDown(title, "ComboBox1"));
			sleep(200);

			// hide not exists dropdown
			Assert.assertFalse(Control.hideDropDown(title,
					"ComboBox1" + System.currentTimeMillis()));
		} finally {
			// destroy frame
			frame.setVisible(false);
		}
	}

	@Test
	public void addString() {
		final String title = "addString - " + currentTimeMillis;
		Assert.assertFalse(Control.addString(title, "ComboBox1", "Perl"));

		final ObjectHolder shellHolder = new ObjectHolder();
		Thread thread = new Thread(new Runnable() {
			public void run() {
				// create frame
				Display display = new Display();
				Shell shell = new Shell(display);
				shell.setText(title);
				shell.setLocation(0, 0);
				shell.setSize(400, 300);
				shellHolder.value = shell;

				Combo combo = new Combo(shell, SWT.DROP_DOWN);
				combo.setItems(new String[] { "Java", "Lisp", "Tcl",
						"Smalltalk" });
				combo.setLocation(20, 20);
				combo.pack();

				// show frame
				shell.open();

				while (!shell.isDisposed()) {
					if (!display.readAndDispatch()) {
						display.sleep();
					}
				}

				display.dispose();
			}
		});

		// show frame
		thread.start();
		Assert.assertTrue(Win.wait(title, 3));

		try {
			Assert.assertArrayEquals(new String[] { "Java", "Lisp", "Tcl",
					"Smalltalk" }, Control.getStringList(title, "ComboBox1")
					.toArray());

			// add string to dropdown
			Assert.assertTrue(Control.addString(title, "ComboBox1", "Perl"));
			Assert.assertArrayEquals(new String[] { "Java", "Lisp", "Tcl",
					"Smalltalk", "Perl" },
					Control.getStringList(title, "ComboBox1").toArray());

			// add string to dropdown
			Assert.assertTrue(Control.addString(title, "ComboBox1", "JAVA"));
			Assert.assertArrayEquals(new String[] { "Java", "Lisp", "Tcl",
					"Smalltalk", "Perl", "JAVA" },
					Control.getStringList(title, "ComboBox1").toArray());

			// add string to dropdown
			Assert.assertTrue(Control.addString(title, "ComboBox1", "Perl"));
			Assert.assertArrayEquals(new String[] { "Java", "Lisp", "Tcl",
					"Smalltalk", "Perl", "JAVA", "Perl" }, Control
					.getStringList(title, "ComboBox1").toArray());

			// add string to not exists dropdown
			Assert.assertFalse(Control.addString(title,
					"ComboBox1" + System.currentTimeMillis(), "Python"));
			Assert.assertArrayEquals(new String[] { "Java", "Lisp", "Tcl",
					"Smalltalk", "Perl", "JAVA", "Perl" }, Control
					.getStringList(title, "ComboBox1").toArray());
		} finally {
			destroyDefaultDisplay(title);
		}
	}

	@Test
	public void delString() {
		final String title = "delString - " + currentTimeMillis;
		Assert.assertFalse(Control.delString(title, "ComboBox1", 0));

		Thread thread = new Thread(new Runnable() {
			public void run() {
				// create frame
				Display display = new Display();
				Shell shell = new Shell(display);
				shell.setText(title);
				shell.setLocation(0, 0);
				shell.setSize(400, 300);

				Combo combo = new Combo(shell, SWT.DROP_DOWN);
				combo.setItems(new String[] { "Java", "Lisp", "Tcl",
						"Smalltalk" });
				combo.setLocation(20, 20);
				combo.pack();

				// show frame
				shell.open();

				while (!shell.isDisposed()) {
					if (!display.readAndDispatch()) {
						display.sleep();
					}
				}

				display.dispose();
			}
		});

		// show frame
		thread.start();
		Assert.assertTrue(Win.wait(title, 3));

		try {
			Assert.assertArrayEquals(new String[] { "Java", "Lisp", "Tcl",
					"Smalltalk" }, Control.getStringList(title, "ComboBox1")
					.toArray());

			// delete string from dropdown
			Assert.assertTrue(Control.delString(title, "ComboBox1", 0));
			Assert.assertArrayEquals(
					new String[] { "Lisp", "Tcl", "Smalltalk" }, Control
							.getStringList(title, "ComboBox1").toArray());

			// delete string from dropdown
			Assert.assertTrue(Control.delString(title, "ComboBox1", 1));
			Assert.assertArrayEquals(new String[] { "Lisp", "Smalltalk" },
					Control.getStringList(title, "ComboBox1").toArray());

			// delete string from dropdown with invalid index
			Assert.assertTrue(Control.delString(title, "ComboBox1", 2));
			Assert.assertArrayEquals(new String[] { "Lisp", "Smalltalk" },
					Control.getStringList(title, "ComboBox1").toArray());

			// delete string from not exists dropdown
			Assert.assertFalse(Control.delString(title,
					"ComboBox1" + System.currentTimeMillis(), 0));
			Assert.assertArrayEquals(new String[] { "Lisp", "Smalltalk" },
					Control.getStringList(title, "ComboBox1").toArray());
		} finally {
			destroyDefaultDisplay(title);
		}
	}

	@Test
	public void findString() {
		final String title = "findString - " + currentTimeMillis;
		Assert.assertNull(Control.findString(title, "ComboBox1", "Java"));
		Assert.assertNull(Control.findString(title, "ListBox1", "Java"));

		Thread thread = new Thread(new Runnable() {
			public void run() {
				// create frame
				Display display = new Display();
				Shell shell = new Shell(display);
				shell.setText(title);
				shell.setLocation(0, 0);
				shell.setSize(400, 300);

				String[] items = new String[] { "Java", "JAVA", "Lisp", "易语言",
						"", "Visual Basic", "Smalltalk" };

				Combo combo = new Combo(shell, SWT.DROP_DOWN);
				combo.setItems(items);
				combo.setLocation(20, 20);
				combo.pack();

				org.eclipse.swt.widgets.List list = new org.eclipse.swt.widgets.List(
						shell, SWT.SINGLE);
				list.setItems(items);
				list.setLocation(150, 20);
				list.pack();

				org.eclipse.swt.widgets.Button button = new org.eclipse.swt.widgets.Button(
						shell, SWT.PUSH);
				button.setText("Button");
				button.setLocation(280, 20);
				button.pack();

				// show frame
				shell.open();

				while (!shell.isDisposed()) {
					if (!display.readAndDispatch()) {
						display.sleep();
					}
				}

				display.dispose();
			}
		});

		// show frame
		thread.start();
		Assert.assertTrue(Win.wait(title, 3));

		try {
			Assert.assertNotNull(Control.getHandle(title, "Button1"));
			Assert.assertNull(Control.findString(title, "Button1", "Java"));

			for (String control : new String[] { "ComboBox1", "ListBox1" }) {
				assertEquals(0, Control.findString(title, control, "Java"));
				assertEquals(0, Control.findString(title, control, "JAVA"));
				assertEquals(0,
						Control.findString(title, control, "Java", true));
				assertEquals(0,
						Control.findString(title, control, "JAVA", true));
				assertEquals(0,
						Control.findString(title, control, "Java", false));
				assertEquals(1,
						Control.findString(title, control, "JAVA", false));
				assertEquals(2, Control.findString(title, control, "Lisp"));
				assertEquals(3, Control.findString(title, control, "易语言"));
				assertEquals(4, Control.findString(title, control, ""));
				assertEquals(5,
						Control.findString(title, control, "Visual Basic"));
				assertEquals(6, Control.findString(title, control, "Smalltalk"));
				Assert.assertNull(Control.findString(title, control, "Lua"));
				Assert.assertNull(Control.findString(title, control, "Lua",
						true));
				Assert.assertNull(Control.findString(title, control, "Lua",
						false));

				// add string to dropdown
				Assert.assertTrue(Control.addString(title, control, "Lua"));
				assertEquals(0, Control.findString(title, control, "Java"));
				assertEquals(0, Control.findString(title, control, "JAVA"));
				assertEquals(0,
						Control.findString(title, control, "Java", true));
				assertEquals(0,
						Control.findString(title, control, "JAVA", true));
				assertEquals(0,
						Control.findString(title, control, "Java", false));
				assertEquals(1,
						Control.findString(title, control, "JAVA", false));
				assertEquals(2, Control.findString(title, control, "Lisp"));
				assertEquals(3, Control.findString(title, control, "易语言"));
				assertEquals(4, Control.findString(title, control, ""));
				assertEquals(5,
						Control.findString(title, control, "Visual Basic"));
				assertEquals(6, Control.findString(title, control, "Smalltalk"));
				assertEquals(7, Control.findString(title, control, "Lua"));
				assertEquals(7, Control.findString(title, control, "Lua", true));
				assertEquals(7,
						Control.findString(title, control, "Lua", false));
				Assert.assertNull(Control.findString(title, control, "Python"));
				Assert.assertNull(Control.findString(title, control, "Python",
						true));
				Assert.assertNull(Control.findString(title, control, "Python",
						false));

				// delete string from dropdown
				Assert.assertTrue(Control.delString(title, control, 1));
				assertEquals(0, Control.findString(title, control, "Java"));
				assertEquals(0, Control.findString(title, control, "JAVA"));
				assertEquals(0,
						Control.findString(title, control, "Java", true));
				assertEquals(0,
						Control.findString(title, control, "JAVA", true));
				assertEquals(0,
						Control.findString(title, control, "Java", false));
				Assert.assertNull(Control.findString(title, control, "JAVA",
						false));
				assertEquals(1, Control.findString(title, control, "Lisp"));
				assertEquals(2, Control.findString(title, control, "易语言"));
				assertEquals(3, Control.findString(title, control, ""));
				assertEquals(4,
						Control.findString(title, control, "Visual Basic"));
				assertEquals(5, Control.findString(title, control, "Smalltalk"));
				assertEquals(6, Control.findString(title, control, "Lua"));
				assertEquals(6, Control.findString(title, control, "Lua", true));
				assertEquals(6,
						Control.findString(title, control, "Lua", false));
				Assert.assertNull(Control.findString(title, control, "Python"));
				Assert.assertNull(Control.findString(title, control, "Python",
						true));
				Assert.assertNull(Control.findString(title, control, "Python",
						false));

				// add string to dropdown
				Assert.assertTrue(Control.addString(title, control, "JAVA"));
				assertEquals(0, Control.findString(title, control, "Java"));
				assertEquals(0, Control.findString(title, control, "JAVA"));
				assertEquals(0,
						Control.findString(title, control, "Java", true));
				assertEquals(0,
						Control.findString(title, control, "JAVA", true));
				assertEquals(0,
						Control.findString(title, control, "Java", false));
				assertEquals(7,
						Control.findString(title, control, "JAVA", false));
				assertEquals(1, Control.findString(title, control, "Lisp"));
				assertEquals(2, Control.findString(title, control, "易语言"));
				assertEquals(3, Control.findString(title, control, ""));
				assertEquals(4,
						Control.findString(title, control, "Visual Basic"));
				assertEquals(5, Control.findString(title, control, "Smalltalk"));
				assertEquals(6, Control.findString(title, control, "Lua"));
				assertEquals(6, Control.findString(title, control, "Lua", true));
				assertEquals(6,
						Control.findString(title, control, "Lua", false));
				Assert.assertNull(Control.findString(title, control, "Python"));
				Assert.assertNull(Control.findString(title, control, "Python",
						true));
				Assert.assertNull(Control.findString(title, control, "Python",
						false));

				// find string from not exists dropdown
				Assert.assertNull(Control.findString(title,
						control + System.currentTimeMillis(), "Java"));
			}
		} finally {
			destroyDefaultDisplay(title);
		}
	}

	@Test
	public void getString() {
		final String title = "getString - " + currentTimeMillis;
		Assert.assertNull(Control.getString(title, "ComboBox1", 0));
		Assert.assertNull(Control.getString(title, "ListBox1", 0));

		Thread thread = new Thread(new Runnable() {
			public void run() {
				// create frame
				Display display = new Display();
				Shell shell = new Shell(display);
				shell.setText(title);
				shell.setLocation(0, 0);
				shell.setSize(400, 300);

				String[] items = new String[] { "Java", "Lisp", "易语言", "",
						"Tcl", "Smalltalk" };

				Combo combo = new Combo(shell, SWT.DROP_DOWN);
				combo.setItems(items);
				combo.setLocation(20, 20);
				combo.pack();

				org.eclipse.swt.widgets.List list = new org.eclipse.swt.widgets.List(
						shell, SWT.SINGLE);
				list.setItems(items);
				list.setLocation(150, 20);
				list.pack();

				org.eclipse.swt.widgets.Button button = new org.eclipse.swt.widgets.Button(
						shell, SWT.PUSH);
				button.setText("Button");
				button.setLocation(280, 20);
				button.pack();

				// show frame
				shell.open();

				while (!shell.isDisposed()) {
					if (!display.readAndDispatch()) {
						display.sleep();
					}
				}

				display.dispose();
			}
		});

		// show frame
		thread.start();
		Assert.assertTrue(Win.wait(title, 3));

		try {
			Assert.assertNotNull(Control.getHandle(title, "Button1"));
			Assert.assertNull(Control.getString(title, "Button1", 0));

			for (String control : new String[] { "ComboBox1", "ListBox1" }) {
				Assert.assertNull(Control.getString(title, control, 6));
				Assert.assertNull(Control.getString(title, control, -1));
				assertEquals("Java", Control.getString(title, control, 0));
				assertEquals("Lisp", Control.getString(title, control, 1));
				assertEquals("易语言", Control.getString(title, control, 2));
				assertEquals("", Control.getString(title, control, 3));
				assertEquals("Tcl", Control.getString(title, control, 4));
				assertEquals("Smalltalk", Control.getString(title, control, 5));
				Assert.assertNull(Control.getString(title, control, 6));

				// add string to dropdown
				Assert.assertTrue(Control.addString(title, control,
						"Visual Basic"));
				Assert.assertNull(Control.getString(title, control, -1));
				assertEquals("Java", Control.getString(title, control, 0));
				assertEquals("Lisp", Control.getString(title, control, 1));
				assertEquals("易语言", Control.getString(title, control, 2));
				assertEquals("", Control.getString(title, control, 3));
				assertEquals("Tcl", Control.getString(title, control, 4));
				assertEquals("Smalltalk", Control.getString(title, control, 5));
				assertEquals("Visual Basic",
						Control.getString(title, control, 6));
				Assert.assertNull(Control.getString(title, control, 7));

				// delete string from dropdown
				Assert.assertTrue(Control.delString(title, control, 1));
				Assert.assertNull(Control.getString(title, control, -1));
				assertEquals("Java", Control.getString(title, control, 0));
				assertEquals("易语言", Control.getString(title, control, 1));
				assertEquals("", Control.getString(title, control, 2));
				assertEquals("Tcl", Control.getString(title, control, 3));
				assertEquals("Smalltalk", Control.getString(title, control, 4));
				assertEquals("Visual Basic",
						Control.getString(title, control, 5));
				Assert.assertNull(Control.getString(title, control, 6));

				// get item from not exists dropdown
				Assert.assertNull(Control.getString(title,
						control + System.currentTimeMillis(), 0));
			}
		} finally {
			destroyDefaultDisplay(title);
		}
	}

	@Test
	public void getStringList() {
		final String title = "getStringList - " + currentTimeMillis;
		Assert.assertNull(Control.getStringList(title, "ComboBox1"));
		Assert.assertNull(Control.getStringList(title, "ListBox1"));

		Thread thread = new Thread(new Runnable() {
			public void run() {
				// create frame
				Display display = new Display();

				Shell shell = new Shell(display);
				shell.setText(title);
				shell.setLocation(0, 0);
				shell.setSize(400, 300);

				String[] items = new String[] { "Java", "Lisp", "易语言", "",
						"Tcl", "Smalltalk" };

				Combo combo = new Combo(shell, SWT.DROP_DOWN);
				combo.setItems(items);
				combo.setLocation(20, 20);
				combo.pack();

				org.eclipse.swt.widgets.List list = new org.eclipse.swt.widgets.List(
						shell, SWT.SINGLE);
				list.setItems(items);
				list.setLocation(150, 20);
				list.pack();

				org.eclipse.swt.widgets.Button button = new org.eclipse.swt.widgets.Button(
						shell, SWT.PUSH);
				button.setText("Button");
				button.setLocation(280, 20);
				button.pack();

				// show frame
				shell.open();

				while (!shell.isDisposed()) {
					if (!display.readAndDispatch()) {
						display.sleep();
					}
				}

				display.dispose();
			}
		});

		// show frame
		thread.start();
		Assert.assertTrue(Win.wait(title, 3));

		try {
			Assert.assertNotNull(Control.getHandle(title, "Button1"));
			Assert.assertNull(Control.getStringList(title, "Button1"));

			for (String control : new String[] { "ComboBox1", "ListBox1" }) {
				List<String> list = Control.getStringList(title, control);
				Assert.assertNotNull(list);
				assertEquals(6, list.size());
				assertEquals("Java", list.get(0));
				assertEquals("Lisp", list.get(1));
				assertEquals("易语言", list.get(2));
				assertEquals("", list.get(3));
				assertEquals("Tcl", list.get(4));
				assertEquals("Smalltalk", list.get(5));

				// add string to dropdown
				Assert.assertTrue(Control.addString(title, control,
						"Visual Basic"));
				list = Control.getStringList(title, control);
				Assert.assertNotNull(list);
				assertEquals(7, list.size());
				assertEquals("Java", list.get(0));
				assertEquals("Lisp", list.get(1));
				assertEquals("易语言", list.get(2));
				assertEquals("", list.get(3));
				assertEquals("Tcl", list.get(4));
				assertEquals("Smalltalk", list.get(5));
				assertEquals("Visual Basic", list.get(6));

				// delete string from dropdown
				Assert.assertTrue(Control.delString(title, control, 1));
				list = Control.getStringList(title, control);
				Assert.assertNotNull(list);
				assertEquals(6, list.size());
				assertEquals("Java", list.get(0));
				assertEquals("易语言", list.get(1));
				assertEquals("", list.get(2));
				assertEquals("Tcl", list.get(3));
				assertEquals("Smalltalk", list.get(4));
				assertEquals("Visual Basic", list.get(5));

				// get item from not exists dropdown
				Assert.assertNull(Control.getStringList(title,
						control + System.currentTimeMillis()));
			}
		} finally {
			destroyDefaultDisplay(title);
		}
	}

	@Test
	public void getStringCount() {
		final String title = "getStringCount - " + currentTimeMillis;
		Assert.assertNull(Control.getStringCount(title, "ComboBox1"));
		Assert.assertNull(Control.getStringCount(title, "ListBox1"));

		Thread thread = new Thread(new Runnable() {
			public void run() {
				// create frame
				Display display = new Display();
				Shell shell = new Shell(display);
				shell.setText(title);
				shell.setLocation(0, 0);
				shell.setSize(400, 300);

				String[] items = new String[] { "Java", "Lisp", "易语言", "",
						"Tcl", "Smalltalk" };

				Combo combo = new Combo(shell, SWT.DROP_DOWN);
				combo.setItems(items);
				combo.setLocation(20, 20);
				combo.pack();

				org.eclipse.swt.widgets.List list = new org.eclipse.swt.widgets.List(
						shell, SWT.SINGLE);
				list.setItems(items);
				list.setLocation(150, 20);
				list.pack();

				org.eclipse.swt.widgets.Button button = new org.eclipse.swt.widgets.Button(
						shell, SWT.PUSH);
				button.setText("Button");
				button.setLocation(280, 20);
				button.pack();

				// show frame
				shell.open();

				while (!shell.isDisposed()) {
					if (!display.readAndDispatch()) {
						display.sleep();
					}
				}

				display.dispose();
			}
		});

		// show frame
		thread.start();
		Assert.assertTrue(Win.wait(title, 3));

		try {
			Assert.assertNotNull(Control.getHandle(title, "Button1"));
			Assert.assertNull(Control.getStringCount(title, "Button1"));

			for (String control : new String[] { "ComboBox1", "ListBox1" }) {
				assertEquals(6, Control.getStringCount(title, control));

				// add string to dropdown
				Assert.assertTrue(Control.addString(title, control,
						"Visual Basic"));
				assertEquals(7, Control.getStringCount(title, control));

				// delete string from dropdown
				Assert.assertTrue(Control.delString(title, control, 1));
				assertEquals(6, Control.getStringCount(title, control));

				// get item from not exists dropdown
				Assert.assertNull(Control.getStringCount(title, control
						+ System.currentTimeMillis()));
			}
		} finally {
			destroyDefaultDisplay(title);
		}
	}

	@Test
	public void setCurrentSelection() {
		final String title = "setCurrentSelection - " + currentTimeMillis;
		Assert.assertFalse(Control.setCurrentSelection(title, "ComboBox1", 0));
		Assert.assertFalse(Control.setCurrentSelection(title, "ListBox1", 0));

		Thread thread = new Thread(new Runnable() {
			public void run() {
				// create frame
				Display display = new Display();
				Shell shell = new Shell(display);
				shell.setText(title);
				shell.setLocation(0, 0);
				shell.setSize(400, 300);

				String[] items = new String[] { "Java", "Lisp", "易语言", "",
						"Tcl", "Smalltalk" };

				Combo combo = new Combo(shell, SWT.DROP_DOWN);
				combo.setItems(items);
				combo.setLocation(20, 20);
				combo.pack();

				org.eclipse.swt.widgets.List list = new org.eclipse.swt.widgets.List(
						shell, SWT.SINGLE);
				list.setItems(items);
				list.setLocation(150, 20);
				list.pack();

				org.eclipse.swt.widgets.Button button = new org.eclipse.swt.widgets.Button(
						shell, SWT.PUSH);
				button.setText("Button");
				button.setLocation(280, 20);
				button.pack();

				// show frame
				shell.open();

				while (!shell.isDisposed()) {
					if (!display.readAndDispatch()) {
						display.sleep();
					}
				}

				display.dispose();
			}
		});

		// show frame
		thread.start();
		Assert.assertTrue(Win.wait(title, 3));

		try {
			Assert.assertNotNull(Control.getHandle(title, "Button1"));
			Assert.assertFalse(Control.setCurrentSelection(title, "Button1", 0));

			for (String control : new String[] { "ComboBox1", "ListBox1" }) {
				int count = Control.getStringCount(title, control);
				assertEquals(6, count);

				Assert.assertTrue(Control
						.setCurrentSelection(title, control, 0));
				assertEquals("Java",
						Control.getCurrentSelection(title, control));
				Assert.assertTrue(Control
						.setCurrentSelection(title, control, 1));
				assertEquals("Lisp",
						Control.getCurrentSelection(title, control));
				Assert.assertTrue(Control
						.setCurrentSelection(title, control, 2));
				assertEquals("易语言", Control.getCurrentSelection(title, control));
				Assert.assertTrue(Control
						.setCurrentSelection(title, control, 3));
				assertEquals("", Control.getCurrentSelection(title, control));
				Assert.assertTrue(Control
						.setCurrentSelection(title, control, 4));
				assertEquals("Tcl", Control.getCurrentSelection(title, control));
				Assert.assertTrue(Control
						.setCurrentSelection(title, control, 5));
				assertEquals("Smalltalk",
						Control.getCurrentSelection(title, control));
				Assert.assertFalse(Control.setCurrentSelection(title, control,
						6));
				assertEquals("Smalltalk",
						Control.getCurrentSelection(title, control));
				Assert.assertFalse(Control.setCurrentSelection(title, control,
						-2));
				assertEquals("Smalltalk",
						Control.getCurrentSelection(title, control));
				Assert.assertFalse(Control.setCurrentSelection(title, control,
						-1));
				assertEquals("Smalltalk",
						Control.getCurrentSelection(title, control));

				// add string to dropdown
				Assert.assertTrue(Control.addString(title, control,
						"Visual Basic"));
				assertEquals(7, Control.getStringCount(title, control));
				Assert.assertTrue(Control
						.setCurrentSelection(title, control, 0));
				assertEquals("Java",
						Control.getCurrentSelection(title, control));
				Assert.assertTrue(Control
						.setCurrentSelection(title, control, 1));
				assertEquals("Lisp",
						Control.getCurrentSelection(title, control));
				Assert.assertTrue(Control
						.setCurrentSelection(title, control, 2));
				assertEquals("易语言", Control.getCurrentSelection(title, control));
				Assert.assertTrue(Control
						.setCurrentSelection(title, control, 3));
				assertEquals("", Control.getCurrentSelection(title, control));
				Assert.assertTrue(Control
						.setCurrentSelection(title, control, 4));
				assertEquals("Tcl", Control.getCurrentSelection(title, control));
				Assert.assertTrue(Control
						.setCurrentSelection(title, control, 5));
				assertEquals("Smalltalk",
						Control.getCurrentSelection(title, control));
				Assert.assertTrue(Control
						.setCurrentSelection(title, control, 6));
				assertEquals("Visual Basic",
						Control.getCurrentSelection(title, control));
				Assert.assertFalse(Control.setCurrentSelection(title, control,
						7));
				assertEquals("Visual Basic",
						Control.getCurrentSelection(title, control));
				Assert.assertFalse(Control.setCurrentSelection(title, control,
						-2));
				assertEquals("Visual Basic",
						Control.getCurrentSelection(title, control));
				Assert.assertFalse(Control.setCurrentSelection(title, control,
						-1));
				assertEquals("Visual Basic",
						Control.getCurrentSelection(title, control));

				// delete string from dropdown
				Assert.assertTrue(Control.delString(title, control, 1));
				assertEquals(6, Control.getStringCount(title, control));
				Assert.assertTrue(Control
						.setCurrentSelection(title, control, 0));
				assertEquals("Java",
						Control.getCurrentSelection(title, control));
				Assert.assertTrue(Control
						.setCurrentSelection(title, control, 1));
				assertEquals("易语言", Control.getCurrentSelection(title, control));
				Assert.assertTrue(Control
						.setCurrentSelection(title, control, 2));
				assertEquals("", Control.getCurrentSelection(title, control));
				Assert.assertTrue(Control
						.setCurrentSelection(title, control, 3));
				assertEquals("Tcl", Control.getCurrentSelection(title, control));
				Assert.assertTrue(Control
						.setCurrentSelection(title, control, 4));
				assertEquals("Smalltalk",
						Control.getCurrentSelection(title, control));
				Assert.assertTrue(Control
						.setCurrentSelection(title, control, 5));
				assertEquals("Visual Basic",
						Control.getCurrentSelection(title, control));
				Assert.assertFalse(Control.setCurrentSelection(title, control,
						6));
				assertEquals("Visual Basic",
						Control.getCurrentSelection(title, control));
				Assert.assertFalse(Control.setCurrentSelection(title, control,
						-2));
				assertEquals("Visual Basic",
						Control.getCurrentSelection(title, control));
				Assert.assertFalse(Control.setCurrentSelection(title, control,
						-1));
				assertEquals("Visual Basic",
						Control.getCurrentSelection(title, control));

				// select item from not exists dropdown
				Assert.assertFalse(Control.setCurrentSelection(title, control
						+ System.currentTimeMillis(), 0));
			}
		} finally {
			destroyDefaultDisplay(title);
		}
	}

	@Test
	public void selectString() {
		final String title = "selectString - " + currentTimeMillis;
		Assert.assertNull(Control.selectString(title, "ComboBox1", "Java"));
		Assert.assertNull(Control.selectString(title, "ListBox1", "Java"));

		final String[] items = new String[] { "Java", "Lisp", "易语言", "", "Tcl",
				"Smalltalk" };

		Thread thread = new Thread(new Runnable() {
			public void run() {
				// create frame
				Display display = new Display();
				Shell shell = new Shell(display);
				shell.setText(title);
				shell.setLocation(0, 0);
				shell.setSize(400, 300);

				Combo combo = new Combo(shell, SWT.DROP_DOWN);
				combo.setItems(items);
				combo.setLocation(20, 20);
				combo.pack();

				org.eclipse.swt.widgets.List list = new org.eclipse.swt.widgets.List(
						shell, SWT.SINGLE);
				list.setItems(items);
				list.setLocation(150, 20);
				list.pack();

				org.eclipse.swt.widgets.Button button = new org.eclipse.swt.widgets.Button(
						shell, SWT.PUSH);
				button.setText("Button");
				button.setLocation(280, 20);
				button.pack();

				// show frame
				shell.open();

				while (!shell.isDisposed()) {
					if (!display.readAndDispatch()) {
						display.sleep();
					}
				}

				display.dispose();
			}
		});

		// show frame
		thread.start();
		Assert.assertTrue(Win.wait(title, 3));

		try {
			Assert.assertNotNull(Control.getHandle(title, "Button1"));
			Assert.assertNull(Control.selectString(title, "Button1", "Button"));

			for (String control : new String[] { "ComboBox1", "ListBox1" }) {
				int count = Control.getStringCount(title, control);
				assertEquals(6, count);

				for (int i = 0; i < count; i++) {
					assertEquals(i,
							Control.selectString(title, control, items[i]));
					assertEquals(items[i],
							Control.getCurrentSelection(title, control));
				}
				Assert.assertNull(Control.selectString(title, control, null));
				assertEquals(items[items.length - 1],
						Control.getCurrentSelection(title, control));
				Assert.assertNull(Control.selectString(title, control,
						"NotExistItem"));
				assertEquals(items[items.length - 1],
						Control.getCurrentSelection(title, control));

				// add string to dropdown
				Assert.assertTrue(Control.addString(title, control,
						"Visual Basic"));
				count = Control.getStringCount(title, control);
				assertEquals(7, count);
				for (int i = 0; i < count; i++) {
					assertEquals(i, Control.selectString(title, control,
							(i == count - 1) ? "Visual Basic" : items[i]));
					assertEquals((i == count - 1) ? "Visual Basic" : items[i],
							Control.getCurrentSelection(title, control));
				}
				Assert.assertNull(Control.selectString(title, control, null));
				assertEquals("Visual Basic",
						Control.getCurrentSelection(title, control));
				Assert.assertNull(Control.selectString(title, control,
						"NotExistItem"));
				assertEquals("Visual Basic",
						Control.getCurrentSelection(title, control));

				// delete string from dropdown
				Assert.assertTrue(Control.delString(title, control, 1));
				count = Control.getStringCount(title, control);
				assertEquals(6, count);
				for (int i = 0; i < count; i++) {
					if (i == 1) {
						Assert.assertNull(Control.selectString(title, control,
								items[i]));
						assertEquals(items[0],
								Control.getCurrentSelection(title, control));
					} else {
						assertEquals((i == 0) ? i : (i - 1),
								Control.selectString(title, control, items[i]));
						assertEquals(items[i],
								Control.getCurrentSelection(title, control));
					}
				}
				Assert.assertNull(Control.selectString(title, control, null));
				assertEquals("Smalltalk",
						Control.getCurrentSelection(title, control));
				Assert.assertNull(Control.selectString(title, control,
						"NotExistItem"));
				assertEquals("Smalltalk",
						Control.getCurrentSelection(title, control));

				// select item from not exists dropdown
				Assert.assertNull(Control.selectString(title,
						control + System.currentTimeMillis(), items[0]));
				assertEquals("Smalltalk",
						Control.getCurrentSelection(title, control));
			}
		} finally {
			destroyDefaultDisplay(title);
		}
	}

	@Test
	public void isChecked() {
		final String title = "isChecked - " + currentTimeMillis;
		Assert.assertFalse(Control.isChecked(title, "Button1"));

		Thread thread = new Thread(new Runnable() {
			public void run() {
				// create frame
				Display display = new Display();
				Shell shell = new Shell(display);
				shell.setText(title);
				shell.setLocation(0, 0);
				shell.setSize(400, 300);

				org.eclipse.swt.widgets.Button button = new org.eclipse.swt.widgets.Button(
						shell, SWT.CHECK);
				button.setText("CheckBox");
				button.setLocation(280, 20);
				button.pack();

				// show frame
				shell.open();

				while (!shell.isDisposed()) {
					if (!display.readAndDispatch()) {
						display.sleep();
					}
				}

				display.dispose();
			}
		});

		// show frame
		thread.start();
		Assert.assertTrue(Win.wait(title, 3));

		try {
			Assert.assertFalse(Control.isChecked(title, "Button1"));

			// check checkbox
			Assert.assertTrue(Control.check(title, "Button1"));
			Assert.assertTrue(Control.isChecked(title, "Button1"));

			// uncheck checkbox
			Assert.assertTrue(Control.uncheck(title, "Button1"));
			Assert.assertFalse(Control.isChecked(title, "Button1"));

			// check checkbox
			Assert.assertTrue(Control.check(title, "Button1"));
			Assert.assertTrue(Control.isChecked(title, "Button1"));

			// uncheck checkbox
			Assert.assertTrue(Control.uncheck(title, "Button1"));
			Assert.assertFalse(Control.isChecked(title, "Button1"));

			// check checkbox
			Assert.assertTrue(Control.check(title, "Button1"));
			Assert.assertTrue(Control.isChecked(title, "Button1"));

			Assert.assertFalse(Control.isChecked(title,
					"Button1" + System.currentTimeMillis()));
		} finally {
			destroyDefaultDisplay(title);
		}
	}

	@Test
	public void check() {
		final String title = "check - " + currentTimeMillis;
		Assert.assertFalse(Control.check(title, "Button1"));

		Thread thread = new Thread(new Runnable() {
			public void run() {
				// create frame
				Display display = new Display();
				Shell shell = new Shell(display);
				shell.setText(title);
				shell.setLocation(0, 0);
				shell.setSize(400, 300);

				org.eclipse.swt.widgets.Button button = new org.eclipse.swt.widgets.Button(
						shell, SWT.CHECK);
				button.setText("CheckBox");
				button.setLocation(280, 20);
				button.pack();

				// show frame
				shell.open();

				while (!shell.isDisposed()) {
					if (!display.readAndDispatch()) {
						display.sleep();
					}
				}

				display.dispose();
			}
		});

		// show frame
		thread.start();
		Assert.assertTrue(Win.wait(title, 3));

		try {
			Assert.assertFalse(Control.isChecked(title, "Button1"));

			// check checkbox
			Assert.assertTrue(Control.check(title, "Button1"));
			Assert.assertTrue(Control.isChecked(title, "Button1"));

			// uncheck checkbox
			Assert.assertTrue(Control.uncheck(title, "Button1"));
			Assert.assertFalse(Control.isChecked(title, "Button1"));

			// check checkbox
			Assert.assertTrue(Control.check(title, "Button1"));
			Assert.assertTrue(Control.isChecked(title, "Button1"));

			// uncheck checkbox
			Assert.assertTrue(Control.uncheck(title, "Button1"));
			Assert.assertFalse(Control.isChecked(title, "Button1"));

			// check not exists checkbox
			Assert.assertFalse(Control.check(title,
					"Button1" + System.currentTimeMillis()));
			Assert.assertFalse(Control.isChecked(title, "Button1"));
		} finally {
			destroyDefaultDisplay(title);
		}
	}

	@Test
	public void uncheck() {
		final String title = "uncheck - " + currentTimeMillis;
		Assert.assertFalse(Control.uncheck(title, "Button1"));

		Thread thread = new Thread(new Runnable() {
			public void run() {
				// create frame
				Display display = new Display();
				Shell shell = new Shell(display);
				shell.setText(title);
				shell.setLocation(0, 0);
				shell.setSize(400, 300);

				org.eclipse.swt.widgets.Button button = new org.eclipse.swt.widgets.Button(
						shell, SWT.CHECK);
				button.setText("CheckBox");
				button.setLocation(280, 20);
				button.pack();

				// show frame
				shell.open();

				while (!shell.isDisposed()) {
					if (!display.readAndDispatch()) {
						display.sleep();
					}
				}

				display.dispose();
			}
		});

		// show frame
		thread.start();
		Assert.assertTrue(Win.wait(title, 3));

		try {
			Assert.assertFalse(Control.isChecked(title, "Button1"));

			// check checkbox
			Assert.assertTrue(Control.check(title, "Button1"));
			Assert.assertTrue(Control.isChecked(title, "Button1"));

			// uncheck checkbox
			Assert.assertTrue(Control.uncheck(title, "Button1"));
			Assert.assertFalse(Control.isChecked(title, "Button1"));

			// check checkbox
			Assert.assertTrue(Control.check(title, "Button1"));
			Assert.assertTrue(Control.isChecked(title, "Button1"));

			// uncheck checkbox
			Assert.assertTrue(Control.uncheck(title, "Button1"));
			Assert.assertFalse(Control.isChecked(title, "Button1"));

			// check checkbox
			Assert.assertTrue(Control.check(title, "Button1"));
			Assert.assertTrue(Control.isChecked(title, "Button1"));

			// uncheck not exists checkbox
			Assert.assertFalse(Control.uncheck(title,
					"Button1" + System.currentTimeMillis()));
			Assert.assertTrue(Control.isChecked(title, "Button1"));
		} finally {
			destroyDefaultDisplay(title);
		}
	}

	@Test
	public void getCurrentLine() {
		final String title = "getCurrentLine - " + currentTimeMillis;
		Assert.assertNull(Control.getCurrentLine(title, "Edit1"));

		Thread thread = new Thread(new Runnable() {
			public void run() {
				// create frame
				Display display = new Display();
				Shell shell = new Shell(display);
				shell.setText(title);
				shell.setLocation(0, 0);
				shell.setSize(400, 300);

				org.eclipse.swt.widgets.Text text = new org.eclipse.swt.widgets.Text(
						shell, SWT.MULTI);
				text.setLocation(20, 20);
				text.setSize(200, 100);

				// show frame
				shell.open();

				while (!shell.isDisposed()) {
					if (!display.readAndDispatch()) {
						display.sleep();
					}
				}

				display.dispose();
			}
		});

		// show frame
		thread.start();
		Assert.assertTrue(Win.wait(title, 3));
		Assert.assertTrue(Win.activate(title));

		try {
			assertEquals(1, Control.getCurrentLine(title, "Edit1"));
			Assert.assertTrue(Control.focus(title, "Edit1"));

			Keyboard.send("{ENTER}");
			assertEquals(2, Control.getCurrentLine(title, "Edit1"));

			Keyboard.send("{ENTER}");
			assertEquals(3, Control.getCurrentLine(title, "Edit1"));

			Keyboard.send("{ENTER}");
			assertEquals(4, Control.getCurrentLine(title, "Edit1"));

			Keyboard.send("{BACKSPACE}");
			assertEquals(3, Control.getCurrentLine(title, "Edit1"));

			Keyboard.send("{BACKSPACE}");
			assertEquals(2, Control.getCurrentLine(title, "Edit1"));

			Keyboard.send("{BACKSPACE}");
			assertEquals(1, Control.getCurrentLine(title, "Edit1"));

			Keyboard.send("{BACKSPACE}");
			assertEquals(1, Control.getCurrentLine(title, "Edit1"));

			// get current line for not exists edit
			Assert.assertNull(Control.getCurrentLine(title,
					"Edit1" + System.currentTimeMillis()));
		} finally {
			destroyDefaultDisplay(title);
		}
	}

	@Test
	public void getCurrentCol() {
		final String title = "getCurrentCol - " + currentTimeMillis;
		Assert.assertNull(Control.getCurrentLine(title, "Edit1"));

		Thread thread = new Thread(new Runnable() {
			public void run() {
				// create frame
				Display display = new Display();
				Shell shell = new Shell(display);
				shell.setText(title);
				shell.setLocation(0, 0);
				shell.setSize(400, 300);

				org.eclipse.swt.widgets.Text text = new org.eclipse.swt.widgets.Text(
						shell, SWT.MULTI);
				text.setLocation(20, 20);
				text.setSize(200, 100);

				// show frame
				shell.open();

				while (!shell.isDisposed()) {
					if (!display.readAndDispatch()) {
						display.sleep();
					}
				}

				display.dispose();
			}
		});

		// show frame
		thread.start();
		Assert.assertTrue(Win.wait(title, 3));
		Assert.assertTrue(Win.activate(title));

		try {
			assertEquals(1, Control.getCurrentCol(title, "Edit1"));
			Assert.assertTrue(Control.focus(title, "Edit1"));

			Keyboard.send("1");
			assertEquals(2, Control.getCurrentCol(title, "Edit1"));

			Keyboard.send("2");
			assertEquals(3, Control.getCurrentCol(title, "Edit1"));

			Keyboard.send("3");
			assertEquals(4, Control.getCurrentCol(title, "Edit1"));

			Keyboard.send("{BACKSPACE}");
			assertEquals(3, Control.getCurrentCol(title, "Edit1"));

			Keyboard.send("{BACKSPACE}");
			assertEquals(2, Control.getCurrentCol(title, "Edit1"));

			Keyboard.send("{BACKSPACE}");
			assertEquals(1, Control.getCurrentCol(title, "Edit1"));

			Keyboard.send("{BACKSPACE}");
			assertEquals(1, Control.getCurrentCol(title, "Edit1"));

			// get current line for not exists edit
			Assert.assertNull(Control.getCurrentCol(title,
					"Edit1" + System.currentTimeMillis()));
		} finally {
			destroyDefaultDisplay(title);
		}
	}

	@Test
	public void getCurrentSelection() {
		final String title = "getCurrentSelection - " + currentTimeMillis;
		Assert.assertNull(Control.getCurrentSelection(title, "ComboBox1"));
		Assert.assertNull(Control.getCurrentSelection(title, "ListBox1"));

		Thread thread = new Thread(new Runnable() {
			public void run() {
				// create frame
				Display display = new Display();

				Shell shell = new Shell(display);
				shell.setText(title);
				shell.setLocation(0, 0);
				shell.setSize(400, 300);

				String[] items = new String[] { "Java", "Lisp", "易语言", "",
						"Tcl", "Smalltalk" };

				Combo combo = new Combo(shell, SWT.DROP_DOWN);
				combo.setItems(items);
				combo.setLocation(20, 20);
				combo.pack();

				org.eclipse.swt.widgets.List list = new org.eclipse.swt.widgets.List(
						shell, SWT.SINGLE);
				list.setItems(items);
				list.setLocation(150, 20);
				list.pack();

				org.eclipse.swt.widgets.Button button = new org.eclipse.swt.widgets.Button(
						shell, SWT.PUSH);
				button.setText("Button");
				button.setLocation(280, 20);
				button.pack();

				// show frame
				shell.open();

				while (!shell.isDisposed()) {
					if (!display.readAndDispatch()) {
						display.sleep();
					}
				}

				display.dispose();
			}
		});

		// show frame
		thread.start();
		Assert.assertTrue(Win.wait(title, 3));

		try {
			Assert.assertNotNull(Control.getHandle(title, "Button1"));
			Assert.assertNull(Control.getCurrentSelection(title, "Button1"));

			for (String control : new String[] { "ComboBox1", "ListBox1" }) {
				int count = Control.getStringCount(title, control);
				assertEquals(6, count);

				Assert.assertTrue(Control
						.setCurrentSelection(title, control, 0));
				assertEquals("Java",
						Control.getCurrentSelection(title, control));
				Assert.assertTrue(Control
						.setCurrentSelection(title, control, 1));
				assertEquals("Lisp",
						Control.getCurrentSelection(title, control));
				Assert.assertTrue(Control
						.setCurrentSelection(title, control, 2));
				assertEquals("易语言", Control.getCurrentSelection(title, control));
				Assert.assertTrue(Control
						.setCurrentSelection(title, control, 3));
				assertEquals("", Control.getCurrentSelection(title, control));
				Assert.assertTrue(Control
						.setCurrentSelection(title, control, 4));
				assertEquals("Tcl", Control.getCurrentSelection(title, control));
				Assert.assertTrue(Control
						.setCurrentSelection(title, control, 5));
				assertEquals("Smalltalk",
						Control.getCurrentSelection(title, control));
				Assert.assertFalse(Control.setCurrentSelection(title, control,
						6));
				assertEquals("Smalltalk",
						Control.getCurrentSelection(title, control));
				Assert.assertFalse(Control.setCurrentSelection(title, control,
						-2));
				assertEquals("Smalltalk",
						Control.getCurrentSelection(title, control));
				Assert.assertFalse(Control.setCurrentSelection(title, control,
						-1));
				assertEquals("Smalltalk",
						Control.getCurrentSelection(title, control));

				// add string to dropdown
				Assert.assertTrue(Control.addString(title, control,
						"Visual Basic"));
				assertEquals(7, Control.getStringCount(title, control));
				Assert.assertTrue(Control
						.setCurrentSelection(title, control, 0));
				assertEquals("Java",
						Control.getCurrentSelection(title, control));
				Assert.assertTrue(Control
						.setCurrentSelection(title, control, 1));
				assertEquals("Lisp",
						Control.getCurrentSelection(title, control));
				Assert.assertTrue(Control
						.setCurrentSelection(title, control, 2));
				assertEquals("易语言", Control.getCurrentSelection(title, control));
				Assert.assertTrue(Control
						.setCurrentSelection(title, control, 3));
				assertEquals("", Control.getCurrentSelection(title, control));
				Assert.assertTrue(Control
						.setCurrentSelection(title, control, 4));
				assertEquals("Tcl", Control.getCurrentSelection(title, control));
				Assert.assertTrue(Control
						.setCurrentSelection(title, control, 5));
				assertEquals("Smalltalk",
						Control.getCurrentSelection(title, control));
				Assert.assertTrue(Control
						.setCurrentSelection(title, control, 6));
				assertEquals("Visual Basic",
						Control.getCurrentSelection(title, control));
				Assert.assertFalse(Control.setCurrentSelection(title, control,
						7));
				assertEquals("Visual Basic",
						Control.getCurrentSelection(title, control));
				Assert.assertFalse(Control.setCurrentSelection(title, control,
						-2));
				assertEquals("Visual Basic",
						Control.getCurrentSelection(title, control));
				Assert.assertFalse(Control.setCurrentSelection(title, control,
						-1));
				assertEquals("Visual Basic",
						Control.getCurrentSelection(title, control));

				// delete string from dropdown
				Assert.assertTrue(Control.delString(title, control, 1));
				assertEquals(6, Control.getStringCount(title, control));
				Assert.assertTrue(Control
						.setCurrentSelection(title, control, 0));
				assertEquals("Java",
						Control.getCurrentSelection(title, control));
				Assert.assertTrue(Control
						.setCurrentSelection(title, control, 1));
				assertEquals("易语言", Control.getCurrentSelection(title, control));
				Assert.assertTrue(Control
						.setCurrentSelection(title, control, 2));
				assertEquals("", Control.getCurrentSelection(title, control));
				Assert.assertTrue(Control
						.setCurrentSelection(title, control, 3));
				assertEquals("Tcl", Control.getCurrentSelection(title, control));
				Assert.assertTrue(Control
						.setCurrentSelection(title, control, 4));
				assertEquals("Smalltalk",
						Control.getCurrentSelection(title, control));
				Assert.assertTrue(Control
						.setCurrentSelection(title, control, 5));
				assertEquals("Visual Basic",
						Control.getCurrentSelection(title, control));
				Assert.assertFalse(Control.setCurrentSelection(title, control,
						6));
				assertEquals("Visual Basic",
						Control.getCurrentSelection(title, control));
				Assert.assertFalse(Control.setCurrentSelection(title, control,
						-2));
				assertEquals("Visual Basic",
						Control.getCurrentSelection(title, control));
				Assert.assertFalse(Control.setCurrentSelection(title, control,
						-1));
				assertEquals("Visual Basic",
						Control.getCurrentSelection(title, control));

				// get item from not exists dropdown
				Assert.assertNull(Control.getCurrentSelection(title, control
						+ System.currentTimeMillis()));
			}
		} finally {
			destroyDefaultDisplay(title);
		}
	}

	@Test
	public void getLineCount() {
		final String title = "getLineCount - " + currentTimeMillis;
		Assert.assertNull(Control.getLineCount(title, "Edit1"));

		Thread thread = new Thread(new Runnable() {
			public void run() {
				// create frame
				Display display = new Display();
				Shell shell = new Shell(display);
				shell.setText(title);
				shell.setLocation(0, 0);
				shell.setSize(400, 300);

				org.eclipse.swt.widgets.Text text = new org.eclipse.swt.widgets.Text(
						shell, SWT.MULTI);
				text.setLocation(20, 20);
				text.setSize(200, 100);

				// show frame
				shell.open();

				while (!shell.isDisposed()) {
					if (!display.readAndDispatch()) {
						display.sleep();
					}
				}

				display.dispose();
			}
		});

		// show frame
		thread.start();
		Assert.assertTrue(Win.wait(title, 3));
		Assert.assertTrue(Win.activate(title));

		try {
			assertEquals(1, Control.getLineCount(title, "Edit1"));
			Assert.assertTrue(Control.focus(title, "Edit1"));

			Keyboard.send("{ENTER}");
			assertEquals(2, Control.getLineCount(title, "Edit1"));

			Keyboard.send("{ENTER}");
			assertEquals(3, Control.getLineCount(title, "Edit1"));

			Keyboard.send("{ENTER}");
			assertEquals(4, Control.getLineCount(title, "Edit1"));

			Keyboard.send("{BACKSPACE}");
			assertEquals(3, Control.getLineCount(title, "Edit1"));

			Keyboard.send("{BACKSPACE}");
			assertEquals(2, Control.getLineCount(title, "Edit1"));

			Keyboard.send("{BACKSPACE}");
			assertEquals(1, Control.getLineCount(title, "Edit1"));

			Keyboard.send("{BACKSPACE}");
			assertEquals(1, Control.getCurrentLine(title, "Edit1"));

			Keyboard.send("{ENTER}");
			assertEquals(2, Control.getLineCount(title, "Edit1"));

			Keyboard.send("{ENTER}");
			assertEquals(3, Control.getLineCount(title, "Edit1"));

			Keyboard.send("{ENTER}");
			assertEquals(4, Control.getLineCount(title, "Edit1"));

			Keyboard.send("{LEFT}");
			assertEquals(4, Control.getLineCount(title, "Edit1"));
			assertEquals(3, Control.getCurrentLine(title, "Edit1"));

			Keyboard.send("{LEFT}");
			assertEquals(4, Control.getLineCount(title, "Edit1"));
			assertEquals(2, Control.getCurrentLine(title, "Edit1"));

			Keyboard.send("{LEFT}");
			assertEquals(4, Control.getLineCount(title, "Edit1"));
			assertEquals(1, Control.getCurrentLine(title, "Edit1"));

			// get current line for not exists edit
			Assert.assertNull(Control.getLineCount(title,
					"Edit1" + System.currentTimeMillis()));
		} finally {
			destroyDefaultDisplay(title);
		}
	}

	@Test
	public void getLine() {
		final String title = "getLine - " + currentTimeMillis;
		Assert.assertNull(Control.getLine(title, "Edit1", 1));

		Thread thread = new Thread(new Runnable() {
			public void run() {
				// create frame
				Display display = new Display();
				Shell shell = new Shell(display);
				shell.setText(title);
				shell.setLocation(0, 0);
				shell.setSize(400, 300);

				org.eclipse.swt.widgets.Text text = new org.eclipse.swt.widgets.Text(
						shell, SWT.MULTI);
				text.setLocation(20, 20);
				text.setSize(200, 100);

				// show frame
				shell.open();

				while (!shell.isDisposed()) {
					if (!display.readAndDispatch()) {
						display.sleep();
					}
				}

				display.dispose();
			}
		});

		// show frame
		thread.start();
		Assert.assertTrue(Win.wait(title, 3));
		Assert.assertTrue(Win.activate(title));

		try {
			Assert.assertTrue(Control.focus(title, "Edit1"));

			assertEquals(1, Control.getLineCount(title, "Edit1"));
			assertEquals("", Control.getLine(title, "Edit1", 1));
			Keyboard.send("12");
			assertEquals("12", Control.getLine(title, "Edit1", 1));

			Keyboard.send("{ENTER}");
			assertEquals(2, Control.getLineCount(title, "Edit1"));
			assertEquals("12", Control.getLine(title, "Edit1", 1));
			assertEquals("", Control.getLine(title, "Edit1", 2));
			Keyboard.send("34");
			assertEquals("34", Control.getLine(title, "Edit1", 2));

			Keyboard.send("{ENTER}");
			assertEquals(3, Control.getLineCount(title, "Edit1"));
			assertEquals("12", Control.getLine(title, "Edit1", 1));
			assertEquals("34", Control.getLine(title, "Edit1", 2));
			assertEquals("", Control.getLine(title, "Edit1", 3));
			Keyboard.send("56");
			assertEquals("56", Control.getLine(title, "Edit1", 3));

			Keyboard.send("{BACKSPACE}");
			assertEquals(3, Control.getLineCount(title, "Edit1"));
			assertEquals("5", Control.getLine(title, "Edit1", 3));
			Keyboard.send("{BACKSPACE}");
			assertEquals(3, Control.getLineCount(title, "Edit1"));
			assertEquals("", Control.getLine(title, "Edit1", 3));

			Keyboard.send("{BACKSPACE}");
			assertEquals(2, Control.getLineCount(title, "Edit1"));
			assertEquals("34", Control.getLine(title, "Edit1", 2));
			Keyboard.send("{BACKSPACE}");
			assertEquals(2, Control.getLineCount(title, "Edit1"));
			assertEquals("3", Control.getLine(title, "Edit1", 2));
			Keyboard.send("{BACKSPACE}");
			assertEquals(2, Control.getLineCount(title, "Edit1"));
			assertEquals("", Control.getLine(title, "Edit1", 2));

			assertEquals(null, Control.getLine(title, "Edit1", 0));
			assertEquals("12", Control.getLine(title, "Edit1", 1));
			assertEquals(null, Control.getLine(title, "Edit1", 3));

			// get line 1 for not exists edit
			assertEquals(null, Control.getLine(title,
					"Edit1" + System.currentTimeMillis(), 1));
		} finally {
			destroyDefaultDisplay(title);
		}
	}

	@Test
	public void getSelected() {
		final String title = "getSelected - " + currentTimeMillis;
		Assert.assertNull(Control.getSelected(title, "Edit1"));

		final ObjectHolder textHolder = new ObjectHolder();
		Thread thread = new Thread(new Runnable() {
			public void run() {
				// create frame
				Display display = new Display();
				Shell shell = new Shell(display);
				shell.setText(title);
				shell.setLocation(0, 0);
				shell.setSize(400, 300);

				org.eclipse.swt.widgets.Text text = new org.eclipse.swt.widgets.Text(
						shell, SWT.MULTI);
				text.setLocation(20, 20);
				text.setSize(200, 100);
				textHolder.value = text;

				// show frame
				shell.open();

				while (!shell.isDisposed()) {
					if (!display.readAndDispatch()) {
						display.sleep();
					}
				}

				display.dispose();
			}
		});

		// show frame
		thread.start();
		Assert.assertTrue(Win.wait(title, 3));

		try {
			final org.eclipse.swt.widgets.Text text = (org.eclipse.swt.widgets.Text) textHolder.value;

			assertEquals("", Control.getSelected(title, "Edit1"));
			Assert.assertTrue(Control.focus(title, "Edit1"));
			assertEquals("", Control.getSelected(title, "Edit1"));

			Keyboard.send("12345");
			assertEquals("12345", Control.getLine(title, "Edit1", 1));
			assertEquals("", Control.getSelected(title, "Edit1"));

			Keyboard.send("{ENTER}");
			assertEquals(2, Control.getLineCount(title, "Edit1"));
			Keyboard.send("67890");
			assertEquals("67890", Control.getLine(title, "Edit1", 2));
			assertEquals("", Control.getSelected(title, "Edit1"));

			Display display = Display.getDefault();
			display.syncExec(new Runnable() {
				public void run() {
					text.setSelection(0, 3);
				}
			});
			sleep(500);
			assertEquals("123", Control.getSelected(title, "Edit1"));

			display.syncExec(new Runnable() {
				public void run() {
					text.setSelection(1, 3);
				}
			});
			sleep(500);
			assertEquals("23", Control.getSelected(title, "Edit1"));

			display.syncExec(new Runnable() {
				public void run() {
					text.setSelection(-1, 3);
				}
			});
			sleep(500);
			assertEquals("", Control.getSelected(title, "Edit1"));

			display.syncExec(new Runnable() {
				public void run() {
					text.setSelection(1, 30);
				}
			});
			sleep(500);
			assertEquals("2345\r\n67890", Control.getSelected(title, "Edit1"));

			display.syncExec(new Runnable() {
				public void run() {
					text.setSelection(1, 4);
				}
			});
			sleep(500);
			assertEquals("234", Control.getSelected(title, "Edit1"));

			display.syncExec(new Runnable() {
				public void run() {
					text.setSelection(1, 5);
				}
			});
			sleep(500);
			assertEquals("2345", Control.getSelected(title, "Edit1"));

			display.syncExec(new Runnable() {
				public void run() {
					text.setSelection(1, 6);
				}
			});
			sleep(500);
			assertEquals("2345\r", Control.getSelected(title, "Edit1"));

			display.syncExec(new Runnable() {
				public void run() {
					text.setSelection(1, 7);
				}
			});
			sleep(500);
			assertEquals("2345\r\n", Control.getSelected(title, "Edit1"));

			display.syncExec(new Runnable() {
				public void run() {
					text.setSelection(1, 8);
				}
			});
			sleep(500);
			assertEquals("2345\r\n6", Control.getSelected(title, "Edit1"));

			// get selected text for not exists edit
			assertEquals(
					null,
					Control.getSelected(title,
							"Edit1" + System.currentTimeMillis()));
		} finally {
			destroyDefaultDisplay(title);
		}
	}

	@Test
	public void editPaste() {
		final String title = "editPaste - " + currentTimeMillis;
		Assert.assertFalse(Control.editPaste(title, "Edit1", "Hello"));

		Thread thread = new Thread(new Runnable() {
			public void run() {
				// create frame
				Display display = new Display();

				Shell shell = new Shell(display);
				shell.setText(title);
				shell.setLocation(0, 0);
				shell.setSize(400, 300);

				org.eclipse.swt.widgets.Text text = new org.eclipse.swt.widgets.Text(
						shell, SWT.MULTI);
				text.setLocation(20, 20);
				text.setSize(200, 100);

				// show frame
				shell.open();

				while (!shell.isDisposed()) {
					if (!display.readAndDispatch()) {
						display.sleep();
					}
				}

				display.dispose();
			}
		});

		// show frame
		thread.start();
		Assert.assertTrue(Win.wait(title, 3));
		Assert.assertTrue(Win.activate(title));

		try {
			Assert.assertTrue(Control.focus(title, "Edit1"));
			assertEquals("", Control.getSelected(title, "Edit1"));

			Keyboard.send("12345");
			assertEquals(1, Control.getLineCount(title, "Edit1"));
			assertEquals("12345", Control.getLine(title, "Edit1", 1));

			Assert.assertTrue(Control.editPaste(title, "Edit1",
					"Hello\r\nWorld"));
			assertEquals(2, Control.getLineCount(title, "Edit1"));
			assertEquals("12345Hello", Control.getLine(title, "Edit1", 1));
			assertEquals("World", Control.getLine(title, "Edit1", 2));

			Assert.assertTrue(Control.editPaste(title, "Edit1", "123"));
			assertEquals(2, Control.getLineCount(title, "Edit1"));
			assertEquals("12345Hello", Control.getLine(title, "Edit1", 1));
			assertEquals("World123", Control.getLine(title, "Edit1", 2));

			Keyboard.send("{LEFT}");
			Keyboard.send("{LEFT}");
			Keyboard.send("{LEFT}");
			Assert.assertTrue(Control.editPaste(title, "Edit1", "aBc"));
			assertEquals(2, Control.getLineCount(title, "Edit1"));
			assertEquals("12345Hello", Control.getLine(title, "Edit1", 1));
			assertEquals("WorldaBc123", Control.getLine(title, "Edit1", 2));

			Assert.assertTrue(Control.editPaste(title, "Edit1", null));
			assertEquals(2, Control.getLineCount(title, "Edit1"));
			assertEquals("12345Hello", Control.getLine(title, "Edit1", 1));
			assertEquals("WorldaBc123", Control.getLine(title, "Edit1", 2));

			Assert.assertTrue(Control.editPaste(title, "Edit1", ""));
			assertEquals(2, Control.getLineCount(title, "Edit1"));
			assertEquals("12345Hello", Control.getLine(title, "Edit1", 1));
			assertEquals("WorldaBc123", Control.getLine(title, "Edit1", 2));

			Assert.assertTrue(Control.editPaste(title, "Edit1", " "));
			assertEquals(2, Control.getLineCount(title, "Edit1"));
			assertEquals("12345Hello", Control.getLine(title, "Edit1", 1));
			assertEquals("WorldaBc 123", Control.getLine(title, "Edit1", 2));

			// past text to not exists edit
			Assert.assertFalse(Control.editPaste(title,
					"Edit1" + System.currentTimeMillis(), "xxx"));
		} finally {
			destroyDefaultDisplay(title);
		}
	}

	@Test
	public void currentTab() {
		final String title = "currentTab - " + currentTimeMillis;
		Assert.assertNull(Control.currentTab(title, "SysTabControl321"));

		Thread thread = new Thread(new Runnable() {
			public void run() {
				// create frame
				Display display = new Display();
				Shell shell = new Shell(display);
				shell.setText(title);
				shell.setLocation(0, 0);
				shell.setSize(400, 300);

				org.eclipse.swt.widgets.TabFolder tabFolder = new org.eclipse.swt.widgets.TabFolder(
						shell, SWT.NULL);
				tabFolder.setLocation(20, 20);
				tabFolder.setSize(300, 200);

				// tab 1
				org.eclipse.swt.widgets.TabItem tabItem1 = new org.eclipse.swt.widgets.TabItem(
						tabFolder, SWT.NULL);
				tabItem1.setText("Tab 1");
				org.eclipse.swt.widgets.Text text1 = new org.eclipse.swt.widgets.Text(
						tabFolder, SWT.BORDER);
				text1.setText("This is tab 1.");
				tabItem1.setControl(text1);

				// tab 2
				org.eclipse.swt.widgets.TabItem tabItem2 = new org.eclipse.swt.widgets.TabItem(
						tabFolder, SWT.NULL);
				tabItem2.setText("标签 2");
				org.eclipse.swt.widgets.Text text2 = new org.eclipse.swt.widgets.Text(
						tabFolder, SWT.BORDER);
				text2.setText("This is tab 2.");
				tabItem2.setControl(text2);

				// tab 3
				org.eclipse.swt.widgets.TabItem tabItem3 = new org.eclipse.swt.widgets.TabItem(
						tabFolder, SWT.NULL);
				tabItem3.setText("");
				org.eclipse.swt.widgets.Text text3 = new org.eclipse.swt.widgets.Text(
						tabFolder, SWT.BORDER);
				text3.setText("This is tab 3.");
				tabItem3.setControl(text3);

				// tab 3
				org.eclipse.swt.widgets.TabItem tabItem4 = new org.eclipse.swt.widgets.TabItem(
						tabFolder, SWT.NULL);
				tabItem4.setText("Tab 4");
				org.eclipse.swt.widgets.Text text4 = new org.eclipse.swt.widgets.Text(
						tabFolder, SWT.BORDER);
				text4.setText("This is tab 4.");
				tabItem4.setControl(text4);

				// show frame
				shell.open();

				while (!shell.isDisposed()) {
					if (!display.readAndDispatch()) {
						display.sleep();
					}
				}

				display.dispose();
			}
		});

		// show frame
		thread.start();
		Assert.assertTrue(Win.wait(title, 3));

		try {
			assertEquals(1, Control.currentTab(title, "SysTabControl321"));

			Assert.assertTrue(Control.tabRight(title, "SysTabControl321"));
			assertEquals(2, Control.currentTab(title, "SysTabControl321"));

			Assert.assertTrue(Control.tabRight(title, "SysTabControl321"));
			assertEquals(3, Control.currentTab(title, "SysTabControl321"));

			Assert.assertTrue(Control.tabRight(title, "SysTabControl321"));
			assertEquals(4, Control.currentTab(title, "SysTabControl321"));

			Assert.assertTrue(Control.tabRight(title, "SysTabControl321"));
			assertEquals(4, Control.currentTab(title, "SysTabControl321"));

			Assert.assertTrue(Control.tabLeft(title, "SysTabControl321"));
			assertEquals(3, Control.currentTab(title, "SysTabControl321"));

			Assert.assertTrue(Control.tabLeft(title, "SysTabControl321"));
			assertEquals(2, Control.currentTab(title, "SysTabControl321"));

			Assert.assertTrue(Control.tabLeft(title, "SysTabControl321"));
			assertEquals(1, Control.currentTab(title, "SysTabControl321"));

			Assert.assertTrue(Control.tabLeft(title, "SysTabControl321"));
			assertEquals(1, Control.currentTab(title, "SysTabControl321"));

			// get current tab for not exists SysTabControl32
			Assert.assertNull(Control.currentTab(title, "SysTabControl321"
					+ System.currentTimeMillis()));
		} finally {
			destroyDefaultDisplay(title);
		}
	}

	@Test
	public void tabRight() {
		final String title = "tabRight - " + currentTimeMillis;
		Assert.assertFalse(Control.tabRight(title, "SysTabControl321"));

		Thread thread = new Thread(new Runnable() {
			public void run() {
				// create frame
				Display display = new Display();
				Shell shell = new Shell(display);
				shell.setText(title);
				shell.setLocation(0, 0);
				shell.setSize(400, 300);

				org.eclipse.swt.widgets.TabFolder tabFolder = new org.eclipse.swt.widgets.TabFolder(
						shell, SWT.NULL);
				tabFolder.setLocation(20, 20);
				tabFolder.setSize(300, 200);

				// tab 1
				org.eclipse.swt.widgets.TabItem tabItem1 = new org.eclipse.swt.widgets.TabItem(
						tabFolder, SWT.NULL);
				tabItem1.setText("Tab 1");
				org.eclipse.swt.widgets.Text text1 = new org.eclipse.swt.widgets.Text(
						tabFolder, SWT.BORDER);
				text1.setText("This is tab 1.");
				tabItem1.setControl(text1);

				// tab 2
				org.eclipse.swt.widgets.TabItem tabItem2 = new org.eclipse.swt.widgets.TabItem(
						tabFolder, SWT.NULL);
				tabItem2.setText("标签 2");
				org.eclipse.swt.widgets.Text text2 = new org.eclipse.swt.widgets.Text(
						tabFolder, SWT.BORDER);
				text2.setText("This is tab 2.");
				tabItem2.setControl(text2);

				// tab 3
				org.eclipse.swt.widgets.TabItem tabItem3 = new org.eclipse.swt.widgets.TabItem(
						tabFolder, SWT.NULL);
				tabItem3.setText("");
				org.eclipse.swt.widgets.Text text3 = new org.eclipse.swt.widgets.Text(
						tabFolder, SWT.BORDER);
				text3.setText("This is tab 3.");
				tabItem3.setControl(text3);

				// tab 3
				org.eclipse.swt.widgets.TabItem tabItem4 = new org.eclipse.swt.widgets.TabItem(
						tabFolder, SWT.NULL);
				tabItem4.setText("Tab 4");
				org.eclipse.swt.widgets.Text text4 = new org.eclipse.swt.widgets.Text(
						tabFolder, SWT.BORDER);
				text4.setText("This is tab 4.");
				tabItem4.setControl(text4);

				// show frame
				shell.open();

				while (!shell.isDisposed()) {
					if (!display.readAndDispatch()) {
						display.sleep();
					}
				}

				display.dispose();
			}
		});

		// show frame
		thread.start();
		Assert.assertTrue(Win.wait(title, 3));

		try {
			assertEquals(1, Control.currentTab(title, "SysTabControl321"));

			Assert.assertTrue(Control.tabRight(title, "SysTabControl321"));
			assertEquals(2, Control.currentTab(title, "SysTabControl321"));

			Assert.assertTrue(Control.tabRight(title, "SysTabControl321"));
			assertEquals(3, Control.currentTab(title, "SysTabControl321"));

			Assert.assertTrue(Control.tabRight(title, "SysTabControl321"));
			assertEquals(4, Control.currentTab(title, "SysTabControl321"));

			Assert.assertTrue(Control.tabRight(title, "SysTabControl321"));
			assertEquals(4, Control.currentTab(title, "SysTabControl321"));

			Assert.assertTrue(Control.tabLeft(title, "SysTabControl321"));
			assertEquals(3, Control.currentTab(title, "SysTabControl321"));

			Assert.assertTrue(Control.tabLeft(title, "SysTabControl321"));
			assertEquals(2, Control.currentTab(title, "SysTabControl321"));

			Assert.assertTrue(Control.tabLeft(title, "SysTabControl321"));
			assertEquals(1, Control.currentTab(title, "SysTabControl321"));

			Assert.assertTrue(Control.tabLeft(title, "SysTabControl321"));
			assertEquals(1, Control.currentTab(title, "SysTabControl321"));

			// Moves to the next tab to the right of a not exists
			// SysTabControl32
			Assert.assertFalse(Control.tabRight(title, "SysTabControl321"
					+ System.currentTimeMillis()));
		} finally {
			destroyDefaultDisplay(title);
		}
	}

	@Test
	public void tabLeft() {
		final String title = "tabLeft - " + currentTimeMillis;
		Assert.assertFalse(Control.tabLeft(title, "SysTabControl321"));

		Thread thread = new Thread(new Runnable() {
			public void run() {
				// create frame
				Display display = new Display();
				Shell shell = new Shell(display);
				shell.setText(title);
				shell.setLocation(0, 0);
				shell.setSize(400, 300);

				org.eclipse.swt.widgets.TabFolder tabFolder = new org.eclipse.swt.widgets.TabFolder(
						shell, SWT.NULL);
				tabFolder.setLocation(20, 20);
				tabFolder.setSize(300, 200);

				// tab 1
				org.eclipse.swt.widgets.TabItem tabItem1 = new org.eclipse.swt.widgets.TabItem(
						tabFolder, SWT.NULL);
				tabItem1.setText("Tab 1");
				org.eclipse.swt.widgets.Text text1 = new org.eclipse.swt.widgets.Text(
						tabFolder, SWT.BORDER);
				text1.setText("This is tab 1.");
				tabItem1.setControl(text1);

				// tab 2
				org.eclipse.swt.widgets.TabItem tabItem2 = new org.eclipse.swt.widgets.TabItem(
						tabFolder, SWT.NULL);
				tabItem2.setText("标签 2");
				org.eclipse.swt.widgets.Text text2 = new org.eclipse.swt.widgets.Text(
						tabFolder, SWT.BORDER);
				text2.setText("This is tab 2.");
				tabItem2.setControl(text2);

				// tab 3
				org.eclipse.swt.widgets.TabItem tabItem3 = new org.eclipse.swt.widgets.TabItem(
						tabFolder, SWT.NULL);
				tabItem3.setText("");
				org.eclipse.swt.widgets.Text text3 = new org.eclipse.swt.widgets.Text(
						tabFolder, SWT.BORDER);
				text3.setText("This is tab 3.");
				tabItem3.setControl(text3);

				// tab 3
				org.eclipse.swt.widgets.TabItem tabItem4 = new org.eclipse.swt.widgets.TabItem(
						tabFolder, SWT.NULL);
				tabItem4.setText("Tab 4");
				org.eclipse.swt.widgets.Text text4 = new org.eclipse.swt.widgets.Text(
						tabFolder, SWT.BORDER);
				text4.setText("This is tab 4.");
				tabItem4.setControl(text4);

				// show frame
				shell.open();

				while (!shell.isDisposed()) {
					if (!display.readAndDispatch()) {
						display.sleep();
					}
				}

				display.dispose();
			}
		});

		// show frame
		thread.start();
		Assert.assertTrue(Win.wait(title, 3));

		try {
			assertEquals(1, Control.currentTab(title, "SysTabControl321"));

			Assert.assertTrue(Control.tabRight(title, "SysTabControl321"));
			assertEquals(2, Control.currentTab(title, "SysTabControl321"));

			Assert.assertTrue(Control.tabRight(title, "SysTabControl321"));
			assertEquals(3, Control.currentTab(title, "SysTabControl321"));

			Assert.assertTrue(Control.tabRight(title, "SysTabControl321"));
			assertEquals(4, Control.currentTab(title, "SysTabControl321"));

			Assert.assertTrue(Control.tabRight(title, "SysTabControl321"));
			assertEquals(4, Control.currentTab(title, "SysTabControl321"));

			Assert.assertTrue(Control.tabLeft(title, "SysTabControl321"));
			assertEquals(3, Control.currentTab(title, "SysTabControl321"));

			Assert.assertTrue(Control.tabLeft(title, "SysTabControl321"));
			assertEquals(2, Control.currentTab(title, "SysTabControl321"));

			Assert.assertTrue(Control.tabLeft(title, "SysTabControl321"));
			assertEquals(1, Control.currentTab(title, "SysTabControl321"));

			Assert.assertTrue(Control.tabLeft(title, "SysTabControl321"));
			assertEquals(1, Control.currentTab(title, "SysTabControl321"));

			// Moves to the next tab to the left of a not exists SysTabControl32
			Assert.assertFalse(Control.tabLeft(title, "SysTabControl321"
					+ System.currentTimeMillis()));
		} finally {
			destroyDefaultDisplay(title);
		}
	}

	@Test
	public void disable() {
		// run notepad
		int pid = runNotepad();
		Assert.assertTrue(Control.isEnabled(NOTEPAD_TITLE, "Edit1"));

		// disable notepad
		Assert.assertTrue(Control.disable(NOTEPAD_TITLE, "Edit1"));
		Assert.assertFalse(Control.isEnabled(NOTEPAD_TITLE, "Edit1"));

		// enable notepad
		Assert.assertTrue(Control.enable(NOTEPAD_TITLE, "Edit1"));
		Assert.assertTrue(Control.isEnabled(NOTEPAD_TITLE, "Edit1"));

		// disable not exists control
		Assert.assertFalse(Control.disable(NOTEPAD_TITLE,
				"Edit1" + System.currentTimeMillis()));
		Assert.assertTrue(Control.isEnabled(NOTEPAD_TITLE, "Edit1"));

		// close process by pid
		closeNotepad(pid);

		// disable not exists control
		Assert.assertFalse(Control.disable(NOTEPAD_TITLE, "Edit1"));
	}

	@Test
	public void enable() {
		// run notepad
		int pid = runNotepad();
		Assert.assertTrue(Control.isEnabled(NOTEPAD_TITLE, "Edit1"));

		// disable notepad
		Assert.assertTrue(Control.disable(NOTEPAD_TITLE, "Edit1"));
		Assert.assertFalse(Control.isEnabled(NOTEPAD_TITLE, "Edit1"));

		// enable notepad
		Assert.assertTrue(Control.enable(NOTEPAD_TITLE, "Edit1"));
		Assert.assertTrue(Control.isEnabled(NOTEPAD_TITLE, "Edit1"));

		// disable notepad
		Assert.assertTrue(Control.disable(NOTEPAD_TITLE, "Edit1"));

		// enable not exists control
		Assert.assertFalse(Control.enable(NOTEPAD_TITLE,
				"Edit1" + System.currentTimeMillis()));
		Assert.assertFalse(Control.isEnabled(NOTEPAD_TITLE, "Edit1"));

		// close process by pid
		closeNotepad(pid);

		// enable not exists control
		Assert.assertFalse(Control.enable(NOTEPAD_TITLE, "Edit1"));
	}

	@Test
	public void focus() {
		String title = "controlFocus - " + currentTimeMillis;
		Frame frame = new Frame(title);
		frame.setLayout(new BorderLayout());
		frame.setBounds(0, 0, 400, 300);

		TextArea textArea1 = new TextArea();
		frame.add(textArea1, BorderLayout.NORTH);

		TextArea textArea2 = new TextArea();
		frame.add(textArea2, BorderLayout.CENTER);

		// show frame
		frame.setVisible(true);
		Assert.assertTrue(Win.wait(title, 3));
		Assert.assertTrue(Win.activate(title));

		try {
			Assert.assertTrue(Control.focus(title, "RichEdit20W1"));
			assertEquals("RichEdit20W1", Control.getFocus(title));

			Assert.assertTrue(Control.focus(title, "RichEdit20W2"));
			assertEquals("RichEdit20W2", Control.getFocus(title));

			Assert.assertTrue(Control.focus(title, "RichEdit20W1"));
			assertEquals("RichEdit20W1", Control.getFocus(title));

			Assert.assertFalse(Control.focus(title,
					"RichEdit20W2" + System.currentTimeMillis()));
			assertEquals("RichEdit20W1", Control.getFocus(title));
		} finally {
			// destroy frame
			destroyFrame(frame);
		}

		sleep(1000);
		Assert.assertFalse(Control.focus(title, "RichEdit20W2"));

		// run notepad
		int pid = runNotepad();

		Assert.assertTrue(Control.focus(NOTEPAD_TITLE, "Edit1"));

		// minimize notepad
		Assert.assertTrue(Win.minimize(NOTEPAD_TITLE));
		Assert.assertTrue(Win.minimized(NOTEPAD_TITLE));

		// focus notepad
		Assert.assertFalse(Control.focus(NOTEPAD_TITLE, "Edit1"));
		Assert.assertTrue(Win.minimized(NOTEPAD_TITLE));

		// restore notepad
		Assert.assertTrue(Win.restore(NOTEPAD_TITLE));
		Assert.assertFalse(Win.minimized(NOTEPAD_TITLE));

		// focus notepad
		Assert.assertFalse(Control.focus(NOTEPAD_TITLE, "Edit1"));

		// activate notepad
		Assert.assertTrue(Win.activate(NOTEPAD_TITLE));
		Assert.assertTrue(Control.focus(NOTEPAD_TITLE, "Edit1"));

		// focus not exists control
		Assert.assertFalse(Control.focus(NOTEPAD_TITLE,
				"Edit1" + System.currentTimeMillis()));

		// close process by pid
		closeNotepad(pid);

		// focus not exists control
		Assert.assertFalse(Control.focus(NOTEPAD_TITLE, "Edit1"));
	}

	@Test
	public void getFocus() {
		// run notepad
		int pid = runNotepad();

		assertEquals("Edit1", Control.getFocus(NOTEPAD_TITLE));

		// get focus control for not exists window
		Assert.assertNull(Control.getFocus(NOTEPAD_TITLE
				+ System.currentTimeMillis()));

		// close process by pid
		closeNotepad(pid);

		// get focus control for not exists window
		Assert.assertNull(Control.getFocus(NOTEPAD_TITLE));
	}

	@Test
	public void getHandle() {
		// run notepad
		int pid = runNotepad();

		// get handle
		Assert.assertTrue(StringUtils.isNotBlank(Control.getHandle(
				NOTEPAD_TITLE, "Edit1")));

		// get handle for not exists control
		Assert.assertNull(Control.getHandle(NOTEPAD_TITLE,
				"Edit1" + System.currentTimeMillis()));

		// close process by pid
		closeNotepad(pid);

		// get handle for not exists control
		Assert.assertNull(Control.getHandle(NOTEPAD_TITLE, "Edit1"));
	}

	@Test
	public void getHeight() {
		final String title = "getHeight - " + currentTimeMillis;
		Thread thread = new Thread(new Runnable() {
			public void run() {
				User32Ext.INSTANCE.MessageBox(null, "How are you?", title, 0);
			}
		});
		thread.start();
		Assert.assertTrue(Win.wait(title, 3));
		int width = Control.getWidth(title, "Button1");
		int height = Control.getHeight(title, "Button1");
		Assert.assertTrue(width > 0);
		Assert.assertTrue(height > 0);

		// move button to (10, 0)
		Assert.assertTrue(Control.move(title, "Button1", 10, 0));
		assertEquals(10, Control.getPosX(title, "Button1").intValue());
		assertEquals(0, Control.getPosY(title, "Button1").intValue());
		assertEquals(width, Control.getWidth(title, "Button1"));
		assertEquals(height, Control.getHeight(title, "Button1"));

		// move button to (20, 10)
		Assert.assertTrue(Control.move(title, "Button1", 20, 10, width + 10,
				height + 5));
		assertEquals(20, Control.getPosX(title, "Button1").intValue());
		assertEquals(10, Control.getPosY(title, "Button1").intValue());
		assertEquals(width + 10, Control.getWidth(title, "Button1"));
		assertEquals(height + 5, Control.getHeight(title, "Button1"));

		// move button to (-100, -50)
		Assert.assertTrue(Control.move(title, "Button1", -100, -50));
		assertEquals(-100, Control.getPosX(title, "Button1").intValue());
		assertEquals(-50, Control.getPosY(title, "Button1").intValue());
		assertEquals(width + 10, Control.getWidth(title, "Button1"));
		assertEquals(height + 5, Control.getHeight(title, "Button1"));

		// get not exists button's height
		Assert.assertNull(Control.getHeight(title,
				"Button1" + System.currentTimeMillis()));

		// close message box
		Assert.assertTrue(Win.close(title));

		// get not exists button's height
		Assert.assertNull(Control.getHeight(title, "Button1"));
	}

	@Test
	public void getWidth() {
		final String title = "getWidth - " + currentTimeMillis;
		Thread thread = new Thread(new Runnable() {
			public void run() {
				User32Ext.INSTANCE.MessageBox(null, "How are you?", title, 0);
			}
		});
		thread.start();
		Assert.assertTrue(Win.wait(title, 3));
		int width = Control.getWidth(title, "Button1");
		int height = Control.getHeight(title, "Button1");
		Assert.assertTrue(width > 0);
		Assert.assertTrue(height > 0);

		// move button to (10, 0)
		Assert.assertTrue(Control.move(title, "Button1", 10, 0));
		assertEquals(10, Control.getPosX(title, "Button1").intValue());
		assertEquals(0, Control.getPosY(title, "Button1").intValue());
		assertEquals(width, Control.getWidth(title, "Button1"));
		assertEquals(height, Control.getHeight(title, "Button1"));

		// move button to (20, 10)
		Assert.assertTrue(Control.move(title, "Button1", 20, 10, width + 10,
				height + 5));
		assertEquals(20, Control.getPosX(title, "Button1").intValue());
		assertEquals(10, Control.getPosY(title, "Button1").intValue());
		assertEquals(width + 10, Control.getWidth(title, "Button1"));
		assertEquals(height + 5, Control.getHeight(title, "Button1"));

		// move button to (-100, -50)
		Assert.assertTrue(Control.move(title, "Button1", -100, -50));
		assertEquals(-100, Control.getPosX(title, "Button1").intValue());
		assertEquals(-50, Control.getPosY(title, "Button1").intValue());
		assertEquals(width + 10, Control.getWidth(title, "Button1"));
		assertEquals(height + 5, Control.getHeight(title, "Button1"));

		// get not exists button's width
		Assert.assertNull(Control.getWidth(title,
				"Button1" + System.currentTimeMillis()));

		// close message box
		Assert.assertTrue(Win.close(title));

		// get not exists button's width
		Assert.assertNull(Control.getWidth(title, "Button1"));
	}

	@Test
	public void getPosX() {
		final String title = "getPosX - " + currentTimeMillis;
		Thread thread = new Thread(new Runnable() {
			public void run() {
				User32Ext.INSTANCE.MessageBox(null, "How are you?", title, 0);
			}
		});
		thread.start();
		Assert.assertTrue(Win.wait(title, 3));
		int width = Control.getWidth(title, "Button1");
		int height = Control.getHeight(title, "Button1");
		Assert.assertTrue(width > 0);
		Assert.assertTrue(height > 0);

		// move button to (10, 0)
		Assert.assertTrue(Control.move(title, "Button1", 10, 0));
		assertEquals(10, Control.getPosX(title, "Button1").intValue());
		assertEquals(0, Control.getPosY(title, "Button1").intValue());
		assertEquals(width, Control.getWidth(title, "Button1"));
		assertEquals(height, Control.getHeight(title, "Button1"));

		// move button to (20, 10)
		Assert.assertTrue(Control.move(title, "Button1", 20, 10));
		assertEquals(20, Control.getPosX(title, "Button1").intValue());
		assertEquals(10, Control.getPosY(title, "Button1").intValue());
		assertEquals(width, Control.getWidth(title, "Button1"));
		assertEquals(height, Control.getHeight(title, "Button1"));

		// move button to (-100, -50)
		Assert.assertTrue(Control.move(title, "Button1", -100, -50));
		assertEquals(-100, Control.getPosX(title, "Button1").intValue());
		assertEquals(-50, Control.getPosY(title, "Button1").intValue());
		assertEquals(width, Control.getWidth(title, "Button1"));
		assertEquals(height, Control.getHeight(title, "Button1"));

		// get not exists button's position
		Assert.assertNull(Control.getPosX(title,
				"Button1" + System.currentTimeMillis()));

		// close message box
		Assert.assertTrue(Win.close(title));

		// get not exists button's position
		Assert.assertNull(Control.getPosX(title, "Button1"));
	}

	@Test
	public void getPosY() {
		final String title = "getPosY - " + currentTimeMillis;
		Thread thread = new Thread(new Runnable() {
			public void run() {
				User32Ext.INSTANCE.MessageBox(null, "How are you?", title, 0);
			}
		});
		thread.start();
		Assert.assertTrue(Win.wait(title, 3));
		int width = Control.getWidth(title, "Button1");
		int height = Control.getHeight(title, "Button1");
		Assert.assertTrue(width > 0);
		Assert.assertTrue(height > 0);

		// move button to (10, 0)
		Assert.assertTrue(Control.move(title, "Button1", 10, 0));
		assertEquals(10, Control.getPosX(title, "Button1").intValue());
		assertEquals(0, Control.getPosY(title, "Button1").intValue());
		assertEquals(width, Control.getWidth(title, "Button1"));
		assertEquals(height, Control.getHeight(title, "Button1"));

		// move button to (20, 10)
		Assert.assertTrue(Control.move(title, "Button1", 20, 10));
		assertEquals(20, Control.getPosX(title, "Button1").intValue());
		assertEquals(10, Control.getPosY(title, "Button1").intValue());
		assertEquals(width, Control.getWidth(title, "Button1"));
		assertEquals(height, Control.getHeight(title, "Button1"));

		// move button to (-100, -50)
		Assert.assertTrue(Control.move(title, "Button1", -100, -50));
		assertEquals(-100, Control.getPosX(title, "Button1").intValue());
		assertEquals(-50, Control.getPosY(title, "Button1").intValue());
		assertEquals(width, Control.getWidth(title, "Button1"));
		assertEquals(height, Control.getHeight(title, "Button1"));

		// get not exists button's position
		Assert.assertNull(Control.getPosY(title,
				"Button1" + System.currentTimeMillis()));

		// close message box
		Assert.assertTrue(Win.close(title));

		// get not exists button's position
		Assert.assertNull(Control.getPosY(title, "Button1"));
	}

	@Test
	public void getText() {
		// run notepad
		int pid = runNotepad();
		assertEquals("", Control.getText(NOTEPAD_TITLE, "", "Edit1"));

		Assert.assertTrue(Control.setText(NOTEPAD_TITLE, "Edit1", "Hello"));
		assertEquals("Hello", Control.getText(NOTEPAD_TITLE, "Edit1"));

		Assert.assertTrue(Control.setText(NOTEPAD_TITLE, "Edit1", "World"));
		assertEquals("World", Control.getText(NOTEPAD_TITLE, "Edit1"));

		Assert.assertTrue(Control.setText(NOTEPAD_TITLE, "Edit1", ""));
		assertEquals("", Control.getText(NOTEPAD_TITLE, "Edit1"));

		Assert.assertNull(Control.getText(NOTEPAD_TITLE,
				"Edit1" + System.currentTimeMillis()));

		// close process by pid
		closeNotepad(pid);
	}

	@Test
	public void hide() {
		final String title = "Control.hide - " + currentTimeMillis;
		Thread thread = new Thread(new Runnable() {
			public void run() {
				User32Ext.INSTANCE.MessageBox(null, "How are you?", title, 0);
			}
		});
		thread.start();
		Assert.assertTrue(Win.wait(title, 3));
		assertEquals(MESSAGE_BOX_OK_BUTTON_TEXT + "\nHow are you?\n",
				Win.getText(title));

		// hide button
		Assert.assertTrue(Control.hide(title, "Button1"));
		assertEquals("How are you?\n", Win.getText(title));

		// hide button
		Assert.assertTrue(Control.hide(title, "Button1"));
		assertEquals("How are you?\n", Win.getText(title));

		// show button
		Assert.assertTrue(Control.show(title, "Button1"));
		assertEquals(MESSAGE_BOX_OK_BUTTON_TEXT + "\nHow are you?\n",
				Win.getText(title));

		// hide not exists button
		Assert.assertFalse(Control.hide(title,
				"Button1" + System.currentTimeMillis()));
		assertEquals(MESSAGE_BOX_OK_BUTTON_TEXT + "\nHow are you?\n",
				Win.getText(title));

		// close message box
		Assert.assertTrue(Win.close(title));

		// hide not exists button
		Assert.assertFalse(Control.hide(title, "Button1"));
	}

	@Test
	public void move() {
		final String title = "move - " + currentTimeMillis;
		Thread thread = new Thread(new Runnable() {
			public void run() {
				User32Ext.INSTANCE.MessageBox(null, "How are you?", title, 0);
			}
		});
		thread.start();
		Assert.assertTrue(Win.wait(title, 3));
		int width = Control.getWidth(title, "Button1");
		int height = Control.getHeight(title, "Button1");
		Assert.assertTrue(width > 0);
		Assert.assertTrue(height > 0);

		// move button to (10, 0)
		Assert.assertTrue(Control.move(title, "Button1", 10, 0));
		assertEquals(10, Control.getPosX(title, "Button1").intValue());
		assertEquals(0, Control.getPosY(title, "Button1").intValue());
		assertEquals(width, Control.getWidth(title, "Button1"));
		assertEquals(height, Control.getHeight(title, "Button1"));

		// move button to (20, 10)
		Assert.assertTrue(Control.move(title, "Button1", 20, 10));
		assertEquals(20, Control.getPosX(title, "Button1").intValue());
		assertEquals(10, Control.getPosY(title, "Button1").intValue());
		assertEquals(width, Control.getWidth(title, "Button1"));
		assertEquals(height, Control.getHeight(title, "Button1"));

		// move button to (30, 15)
		Assert.assertTrue(Control.move(title, "Button1", 30, 15));
		assertEquals(30, Control.getPosX(title, "Button1").intValue());
		assertEquals(15, Control.getPosY(title, "Button1").intValue());
		assertEquals(width, Control.getWidth(title, "Button1"));
		assertEquals(height, Control.getHeight(title, "Button1"));

		// move not exists button
		Assert.assertFalse(Control.move(title,
				"Button1" + System.currentTimeMillis(), 30, 15));
		assertEquals(30, Control.getPosX(title, "Button1").intValue());
		assertEquals(15, Control.getPosY(title, "Button1").intValue());
		assertEquals(width, Control.getWidth(title, "Button1"));
		assertEquals(height, Control.getHeight(title, "Button1"));

		// move button to (-100, -50)
		Assert.assertTrue(Control.move(title, "Button1", -100, -50));
		assertEquals(-100, Control.getPosX(title, "Button1").intValue());
		assertEquals(-50, Control.getPosY(title, "Button1").intValue());
		assertEquals(width, Control.getWidth(title, "Button1"));
		assertEquals(height, Control.getHeight(title, "Button1"));

		// move and change button's width
		Assert.assertTrue(Control.move(title, "Button1", 10, 5, width + 20,
				null));
		assertEquals(10, Control.getPosX(title, "Button1").intValue());
		assertEquals(5, Control.getPosY(title, "Button1").intValue());
		assertEquals(width + 20, Control.getWidth(title, "Button1"));
		assertEquals(height, Control.getHeight(title, "Button1"));

		// move and change button's height
		Assert.assertTrue(Control.move(title, "Button1", 20, 10, null,
				height + 10));
		assertEquals(20, Control.getPosX(title, "Button1").intValue());
		assertEquals(10, Control.getPosY(title, "Button1").intValue());
		assertEquals(width + 20, Control.getWidth(title, "Button1"));
		assertEquals(height + 10, Control.getHeight(title, "Button1"));

		// close message box
		Assert.assertTrue(Win.close(title));

		// move not exists button
		Assert.assertFalse(Control.move(title, "Button1", 30, 15));
	}

	@Test
	public void send() {
		// run notepad
		int pid = runNotepad();
		assertEquals("", Control.getText(NOTEPAD_TITLE, "", "Edit1"));

		// send text
		Assert.assertTrue(Control.send(NOTEPAD_TITLE, "Edit1", "Hello"));
		assertEquals("Hello", Control.getText(NOTEPAD_TITLE, "Edit1"));

		Assert.assertTrue(Control.send(NOTEPAD_TITLE, "Edit1", "World"));
		assertEquals("HelloWorld", Control.getText(NOTEPAD_TITLE, "Edit1"));

		// send empty text
		Assert.assertTrue(Control.send(NOTEPAD_TITLE, "Edit1", ""));
		assertEquals("HelloWorld", Control.getText(NOTEPAD_TITLE, "Edit1"));

		Assert.assertTrue(Control.send(NOTEPAD_TITLE, "Edit1", "\n+hello"));
		assertEquals("HelloWorld\r\nHello",
				Control.getText(NOTEPAD_TITLE, "Edit1"));

		Assert.assertTrue(Control.send(NOTEPAD_TITLE, "Edit1", "{LEFT}{LEFT}"));
		assertEquals("HelloWorld\r\nHello",
				Control.getText(NOTEPAD_TITLE, "Edit1"));

		Assert.assertTrue(Control.send(NOTEPAD_TITLE, "Edit1", "world"));
		assertEquals("HelloWorld\r\nHelworldlo",
				Control.getText(NOTEPAD_TITLE, "Edit1"));

		// send text to not exists control
		Assert.assertFalse(Control.send(NOTEPAD_TITLE,
				"Edit1" + System.currentTimeMillis(), "world"));
		assertEquals("HelloWorld\r\nHelworldlo",
				Control.getText(NOTEPAD_TITLE, "Edit1"));

		// clear notepad text
		Assert.assertTrue(Control.setText(NOTEPAD_TITLE, "Edit1", ""));
		assertEquals("", Control.getText(NOTEPAD_TITLE, "Edit1"));

		// send text
		Assert.assertTrue(Control.send(NOTEPAD_TITLE, "Edit1", "Hello", true));
		assertEquals("Hello", Control.getText(NOTEPAD_TITLE, "Edit1"));

		Assert.assertTrue(Control.send(NOTEPAD_TITLE, "Edit1", "World", true));
		assertEquals("HelloWorld", Control.getText(NOTEPAD_TITLE, "Edit1"));

		// send empty text
		Assert.assertTrue(Control.send(NOTEPAD_TITLE, "Edit1", "", true));
		assertEquals("HelloWorld", Control.getText(NOTEPAD_TITLE, "Edit1"));

		Assert.assertTrue(Control
				.send(NOTEPAD_TITLE, "Edit1", "\n+hello", true));
		assertEquals("HelloWorld\r\n+hello",
				Control.getText(NOTEPAD_TITLE, "Edit1"));

		Assert.assertTrue(Control.send(NOTEPAD_TITLE, "Edit1", "{LEFT}{LEFT}",
				true));
		assertEquals("HelloWorld\r\n+hello{LEFT}{LEFT}",
				Control.getText(NOTEPAD_TITLE, "Edit1"));

		Assert.assertTrue(Control.send(NOTEPAD_TITLE, "Edit1", "world", true));
		assertEquals("HelloWorld\r\n+hello{LEFT}{LEFT}world",
				Control.getText(NOTEPAD_TITLE, "Edit1"));

		// send text to not exists control
		Assert.assertFalse(Control.send(NOTEPAD_TITLE,
				"Edit1" + System.currentTimeMillis(), "world", true));
		assertEquals("HelloWorld\r\n+hello{LEFT}{LEFT}world",
				Control.getText(NOTEPAD_TITLE, "Edit1"));

		// close process by pid
		closeNotepad(pid);

		// send text to not exists control
		Assert.assertFalse(Control.send(NOTEPAD_TITLE, "Edit1", "world", true));
	}

	@Test
	public void setText() {
		final String title = "controlSetText - " + currentTimeMillis;
		Thread thread = new Thread(new Runnable() {
			public void run() {
				User32Ext.INSTANCE.MessageBox(null, "How are you?", title, 0);
			}
		});
		thread.start();
		Assert.assertTrue(Win.wait(title, 3));

		// change button text
		Assert.assertTrue(Control.setText(title, "[CLASS:Button; INSTANCE:1]",
				"Fine, thank you."));
		assertEquals("Fine, thank you.", Control.getText(title, "Button1"));

		Assert.assertTrue(Control.setText(title, "[CLASS:Button; INSTANCE:1]",
				""));
		assertEquals("", Control.getText(title, "Button1"));

		Assert.assertTrue(Control.setText(title, "[CLASS:Button; INSTANCE:1]",
				"Fine, thank you."));
		assertEquals("Fine, thank you.", Control.getText(title, "Button1"));

		Assert.assertTrue(Control.setText(title, "[CLASS:Button; INSTANCE:1]",
				null));
		assertEquals("", Control.getText(title, "Button1"));

		Assert.assertFalse(Control.setText(title,
				"[CLASS:Buttonxxx; INSTANCE:1]", "Fine, thank you."));
		assertEquals("", Control.getText(title, "Button1"));

		// close message box
		Assert.assertTrue(Win.close(title));

		Assert.assertFalse(Control.setText(title, "[CLASS:Button; INSTANCE:1]",
				"Fine, thank you."));
	}

	@Test
	public void show() {
		final String title = "controlShow - " + currentTimeMillis;
		Thread thread = new Thread(new Runnable() {
			public void run() {
				User32Ext.INSTANCE.MessageBox(null, "How are you?", title, 0);
			}
		});
		thread.start();
		Assert.assertTrue(Win.wait(title, 3));
		assertEquals(MESSAGE_BOX_OK_BUTTON_TEXT + "\nHow are you?\n",
				Win.getText(title));

		// hide button
		Assert.assertTrue(Control.hide(title, "Button1"));
		assertEquals("How are you?\n", Win.getText(title));

		// show not exists button
		Assert.assertFalse(Control.show(title,
				"Button1" + System.currentTimeMillis()));
		assertEquals("How are you?\n", Win.getText(title));

		// show button
		Assert.assertTrue(Control.show(title, "Button1"));
		assertEquals(MESSAGE_BOX_OK_BUTTON_TEXT + "\nHow are you?\n",
				Win.getText(title));

		// show button
		Assert.assertTrue(Control.show(title, "Button1"));
		assertEquals(MESSAGE_BOX_OK_BUTTON_TEXT + "\nHow are you?\n",
				Win.getText(title));

		// close message box
		Assert.assertTrue(Win.close(title));

		// show not exists button
		Assert.assertFalse(Control.show(title, "Button1"));
	}

	@Test
	public void statusbarGetText() {
		// run task manager
		int pid = Process.run(TASK_MANAGER, "", RunShowFlag.MAXIMIZE);
		Assert.assertTrue(pid > 0);
		sleep(1000);
		Assert.assertTrue(Win.active(TASK_MANAGER_TITLE));

		String processes = Control.statusbarGetText(TASK_MANAGER_TITLE);
		Assert.assertTrue(processes.startsWith(STATUS_BAR_TEXT_PROCESSES));
		assertEquals(processes, Control.statusbarGetText(TASK_MANAGER_TITLE, 1));

		String cpuUsage = Control.statusbarGetText(TASK_MANAGER_TITLE, 2);
		Assert.assertTrue(String.format(
				"Expected start with %s, but actual is %s.",
				STATUS_BAR_TEXT_CPU_USAGE, cpuUsage), cpuUsage
				.startsWith(STATUS_BAR_TEXT_CPU_USAGE));

		String commitCharge = Control.statusbarGetText(TASK_MANAGER_TITLE,
				null, 3);
		Assert.assertTrue(commitCharge
				.startsWith(STATUS_BAR_TEXT_COMMIT_CHARGE) || commitCharge
				.startsWith(STATUS_BAR_TEXT_PHYSICAL_MEMORY));

		// minimize task manager
		Assert.assertTrue(Win.minimize(TASK_MANAGER_TITLE));
		assertEquals(processes, Control.statusbarGetText(TASK_MANAGER_TITLE, 1));
		Assert.assertTrue(cpuUsage.startsWith(STATUS_BAR_TEXT_CPU_USAGE));
		commitCharge = Control.statusbarGetText(TASK_MANAGER_TITLE, null, 3);
		Assert.assertTrue(commitCharge
				.startsWith(STATUS_BAR_TEXT_COMMIT_CHARGE) || commitCharge
				.startsWith(STATUS_BAR_TEXT_PHYSICAL_MEMORY));

		// hide task manager
		Assert.assertTrue(Win.minimize(TASK_MANAGER_TITLE));
		assertEquals(processes, Control.statusbarGetText(TASK_MANAGER_TITLE, 1));
		Assert.assertTrue(cpuUsage.startsWith(STATUS_BAR_TEXT_CPU_USAGE));
		commitCharge = Control.statusbarGetText(TASK_MANAGER_TITLE, null, 3);
		Assert.assertTrue(commitCharge
				.startsWith(STATUS_BAR_TEXT_COMMIT_CHARGE) || commitCharge
				.startsWith(STATUS_BAR_TEXT_PHYSICAL_MEMORY));

		// close task manager
		Process.close(pid);
		sleep(500);
		Assert.assertFalse(Process.exists(pid));

		Assert.assertNull(Control.statusbarGetText(TASK_MANAGER_TITLE));
		Assert.assertNull(Control.statusbarGetText(TASK_MANAGER_TITLE, 1));
		Assert.assertNull(Control.statusbarGetText(TASK_MANAGER_TITLE, 2));
		Assert.assertNull(Control.statusbarGetText(TASK_MANAGER_TITLE, 3));
	}
}
