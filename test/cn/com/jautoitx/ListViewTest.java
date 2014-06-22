package cn.com.jautoitx;

import java.io.File;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import cn.com.jautoitx.ListView.ControlListViewView;

public class ListViewTest extends BaseTest {
	@Test
	public void deSelect() {
		Assert.assertFalse(ListView.deSelect(HASH_MY_FILES_TITLE,
				"SysListView321", 0, 1));

		Assert.assertNull(ListView.getItemCount(HASH_MY_FILES_TITLE,
				"SysListView321"));

		// run HashMyFiles
		int pid = Process.run(HASH_MY_FILES);
		Assert.assertTrue(pid > 0);
		Assert.assertTrue(Win.waitActive(HASH_MY_FILES_TITLE, 3));

		assertEquals(0,
				ListView.getItemCount(HASH_MY_FILES_TITLE, "SysListView321"));

		// add file to ListView
		Assert.assertTrue(Win.menuSelectItem("HashMyFiles", null, "&File",
				"Add &Files"));
		Assert.assertTrue(Win.active(HASH_MY_FILES_ADD_FILES_TITLE));
		sleep(500);
		Control.setText(HASH_MY_FILES_ADD_FILES_TITLE, "Edit1", new File(
				"test/HashMyFiles.exe").getAbsolutePath());
		Keyboard.send("{ENTER}");
		sleep(1000);
		assertEquals(1,
				ListView.getItemCount(HASH_MY_FILES_TITLE, "SysListView321"));

		// add file to ListView
		Assert.assertTrue(Win.menuSelectItem("HashMyFiles", null, "&File",
				"Add &Files"));
		Assert.assertTrue(Win.active(HASH_MY_FILES_ADD_FILES_TITLE));
		sleep(500);
		Control.setText(HASH_MY_FILES_ADD_FILES_TITLE, "Edit1", new File(
				"test/iniDelete.tmp").getAbsolutePath());
		Keyboard.send("{ENTER}");
		sleep(1000);
		assertEquals(2,
				ListView.getItemCount(HASH_MY_FILES_TITLE, "SysListView321"));

		Assert.assertFalse(ListView.isSelected(HASH_MY_FILES_TITLE,
				"SysListView321", 0));
		Assert.assertFalse(ListView.isSelected(HASH_MY_FILES_TITLE,
				"SysListView321", 1));

		// select items
		Assert.assertTrue(ListView.select(HASH_MY_FILES_TITLE,
				"SysListView321", 0, 0));
		Assert.assertTrue(ListView.isSelected(HASH_MY_FILES_TITLE,
				"SysListView321", 0));
		Assert.assertFalse(ListView.isSelected(HASH_MY_FILES_TITLE,
				"SysListView321", 1));
		Assert.assertTrue(ListView.select(HASH_MY_FILES_TITLE,
				"SysListView321", 1, 1));
		Assert.assertTrue(ListView.isSelected(HASH_MY_FILES_TITLE,
				"SysListView321", 0));
		Assert.assertTrue(ListView.isSelected(HASH_MY_FILES_TITLE,
				"SysListView321", 1));

		// unselect items
		Assert.assertTrue(ListView.deSelect(HASH_MY_FILES_TITLE,
				"SysListView321", 0, 1));
		Assert.assertFalse(ListView.isSelected(HASH_MY_FILES_TITLE,
				"SysListView321", 0));
		Assert.assertFalse(ListView.isSelected(HASH_MY_FILES_TITLE,
				"SysListView321", 1));

		// select items
		Assert.assertTrue(ListView.select(HASH_MY_FILES_TITLE,
				"SysListView321", 0, 1));
		Assert.assertTrue(ListView.isSelected(HASH_MY_FILES_TITLE,
				"SysListView321", 0));
		Assert.assertTrue(ListView.isSelected(HASH_MY_FILES_TITLE,
				"SysListView321", 1));

		// unselect items
		Assert.assertTrue(ListView.deSelect(HASH_MY_FILES_TITLE,
				"SysListView321", 0, 0));
		Assert.assertFalse(ListView.isSelected(HASH_MY_FILES_TITLE,
				"SysListView321", 0));
		Assert.assertTrue(ListView.isSelected(HASH_MY_FILES_TITLE,
				"SysListView321", 1));
		Assert.assertTrue(ListView.deSelect(HASH_MY_FILES_TITLE,
				"SysListView321", 1, 1));
		Assert.assertFalse(ListView.isSelected(HASH_MY_FILES_TITLE,
				"SysListView321", 0));
		Assert.assertFalse(ListView.isSelected(HASH_MY_FILES_TITLE,
				"SysListView321", 1));

		// select items
		Assert.assertTrue(ListView.select(HASH_MY_FILES_TITLE,
				"SysListView321", 0, 1));
		Assert.assertTrue(ListView.isSelected(HASH_MY_FILES_TITLE,
				"SysListView321", 0));
		Assert.assertTrue(ListView.isSelected(HASH_MY_FILES_TITLE,
				"SysListView321", 1));

		// unselect items
		Assert.assertTrue(ListView.deSelect(HASH_MY_FILES_TITLE,
				"SysListView321", 0, 2));
		Assert.assertFalse(ListView.isSelected(HASH_MY_FILES_TITLE,
				"SysListView321", 0));
		Assert.assertFalse(ListView.isSelected(HASH_MY_FILES_TITLE,
				"SysListView321", 1));

		// select items
		Assert.assertTrue(ListView.select(HASH_MY_FILES_TITLE,
				"SysListView321", 0, 1));
		Assert.assertTrue(ListView.isSelected(HASH_MY_FILES_TITLE,
				"SysListView321", 0));
		Assert.assertTrue(ListView.isSelected(HASH_MY_FILES_TITLE,
				"SysListView321", 1));

		// unselect items
		Assert.assertTrue(ListView.deSelect(HASH_MY_FILES_TITLE,
				"SysListView321", -1, 1));
		Assert.assertFalse(ListView.isSelected(HASH_MY_FILES_TITLE,
				"SysListView321", 0));
		Assert.assertFalse(ListView.isSelected(HASH_MY_FILES_TITLE,
				"SysListView321", 1));

		// select items
		Assert.assertTrue(ListView.select(HASH_MY_FILES_TITLE,
				"SysListView321", 0, 1));
		Assert.assertTrue(ListView.isSelected(HASH_MY_FILES_TITLE,
				"SysListView321", 0));
		Assert.assertTrue(ListView.isSelected(HASH_MY_FILES_TITLE,
				"SysListView321", 1));

		// unselect items
		Assert.assertTrue(ListView.deSelect(HASH_MY_FILES_TITLE,
				"SysListView321", 0));
		Assert.assertFalse(ListView.isSelected(HASH_MY_FILES_TITLE,
				"SysListView321", 0));
		Assert.assertFalse(ListView.isSelected(HASH_MY_FILES_TITLE,
				"SysListView321", 1));

		// select items
		Assert.assertTrue(ListView.select(HASH_MY_FILES_TITLE,
				"SysListView321", 0, 1));
		Assert.assertTrue(ListView.isSelected(HASH_MY_FILES_TITLE,
				"SysListView321", 0));
		Assert.assertTrue(ListView.isSelected(HASH_MY_FILES_TITLE,
				"SysListView321", 1));

		// unselect items
		Assert.assertTrue(ListView.deSelect(HASH_MY_FILES_TITLE,
				"SysListView321", -1));
		Assert.assertFalse(ListView.isSelected(HASH_MY_FILES_TITLE,
				"SysListView321", 0));
		Assert.assertFalse(ListView.isSelected(HASH_MY_FILES_TITLE,
				"SysListView321", 1));

		// select items
		Assert.assertTrue(ListView.select(HASH_MY_FILES_TITLE,
				"SysListView321", 0, 1));
		Assert.assertTrue(ListView.isSelected(HASH_MY_FILES_TITLE,
				"SysListView321", 0));
		Assert.assertTrue(ListView.isSelected(HASH_MY_FILES_TITLE,
				"SysListView321", 1));

		// unselect items
		Assert.assertTrue(ListView.deSelect(HASH_MY_FILES_TITLE,
				"SysListView321", 1));
		Assert.assertTrue(ListView.isSelected(HASH_MY_FILES_TITLE,
				"SysListView321", 0));
		Assert.assertFalse(ListView.isSelected(HASH_MY_FILES_TITLE,
				"SysListView321", 1));

		// unselect unselected items
		Assert.assertTrue(ListView.deSelect(HASH_MY_FILES_TITLE,
				"SysListView321", -1, 1));
		Assert.assertFalse(ListView.isSelected(HASH_MY_FILES_TITLE,
				"SysListView321", 0));
		Assert.assertFalse(ListView.isSelected(HASH_MY_FILES_TITLE,
				"SysListView321", 1));

		// unselect not exists ListView
		Assert.assertFalse(ListView.deSelect(HASH_MY_FILES_TITLE,
				"SysListView321" + System.currentTimeMillis(), 0, 1));

		// close HashMyFiles
		Assert.assertTrue(Win.close(HASH_MY_FILES_TITLE));
	}

	@Test
	public void findItem() {
		Assert.assertNull(ListView.findItem(HASH_MY_FILES_TITLE,
				"SysListView321", "HashMyFiles"));

		// run HashMyFiles
		int pid = Process.run(HASH_MY_FILES);
		Assert.assertTrue(pid > 0);
		Assert.assertTrue(Win.waitActive(HASH_MY_FILES_TITLE, 3));

		assertEquals(0,
				ListView.getItemCount(HASH_MY_FILES_TITLE, "SysListView321"));

		// add file to ListView
		Assert.assertTrue(Win.menuSelectItem("HashMyFiles", null, "&File",
				"Add &Files"));
		Assert.assertTrue(Win.active(HASH_MY_FILES_ADD_FILES_TITLE));
		sleep(500);
		Control.setText(HASH_MY_FILES_ADD_FILES_TITLE, "Edit1", new File(
				"test/HashMyFiles.exe").getAbsolutePath());
		Keyboard.send("{ENTER}");
		sleep(1000);
		assertEquals(1,
				ListView.getItemCount(HASH_MY_FILES_TITLE, "SysListView321"));

		assertEquals(0, ListView.findItem(HASH_MY_FILES_TITLE,
				"SysListView321", "HashMyFiles.exe"));
		Assert.assertNull(ListView.findItem(HASH_MY_FILES_TITLE,
				"SysListView321", "iniDelete.tmp"));

		// add file to ListView
		Assert.assertTrue(Win.menuSelectItem("HashMyFiles", null, "&File",
				"Add &Files"));
		Assert.assertTrue(Win.active(HASH_MY_FILES_ADD_FILES_TITLE));
		sleep(500);
		Control.setText(HASH_MY_FILES_ADD_FILES_TITLE, "Edit1", new File(
				"test/你好.txt").getAbsolutePath());
		Keyboard.send("{ENTER}");
		sleep(1000);
		assertEquals(2,
				ListView.getItemCount(HASH_MY_FILES_TITLE, "SysListView321"));

		assertEquals(0, ListView.findItem(HASH_MY_FILES_TITLE,
				"SysListView321", "HashMyFiles.exe"));
		assertEquals(1, ListView.findItem(HASH_MY_FILES_TITLE,
				"SysListView321", "你好.txt"));
		Assert.assertNull(ListView.findItem(HASH_MY_FILES_TITLE,
				"SysListView321", "世界.txt"));

		assertEquals(0, ListView.findItem(HASH_MY_FILES_TITLE,
				"SysListView321", "HashMyFiles.exe", 0));
		assertEquals(1, ListView.findItem(HASH_MY_FILES_TITLE,
				"SysListView321", "你好.txt", 0));

		Assert.assertNull(ListView.findItem(HASH_MY_FILES_TITLE,
				"SysListView321", "HashMyFiles.exe", 1));
		Assert.assertNull(ListView.findItem(HASH_MY_FILES_TITLE,
				"SysListView321", "你好.txt", 1));

		Assert.assertNull(ListView.findItem(HASH_MY_FILES_TITLE,
				"SysListView321", "exe", 1));
		Assert.assertNull(ListView.findItem(HASH_MY_FILES_TITLE,
				"SysListView321", "txt", 1));

		assertEquals(0, ListView.findItem(HASH_MY_FILES_TITLE,
				"SysListView321", "exe", 14));
		assertEquals(1, ListView.findItem(HASH_MY_FILES_TITLE,
				"SysListView321", "txt", 14));

		Assert.assertNull(ListView.findItem(HASH_MY_FILES_TITLE,
				"SysListView321" + System.currentTimeMillis(), "exe", 14));
		Assert.assertNull(ListView.findItem(HASH_MY_FILES_TITLE,
				"SysListView321" + System.currentTimeMillis(), "txt", 14));

		// close HashMyFiles
		Assert.assertTrue(Win.close(HASH_MY_FILES_TITLE));
	}

	@Test
	public void getItemCount() {
		Assert.assertNull(ListView.getItemCount(HASH_MY_FILES_TITLE,
				"SysListView321"));

		// run HashMyFiles
		int pid = Process.run(HASH_MY_FILES);
		Assert.assertTrue(pid > 0);
		Assert.assertTrue(Win.waitActive(HASH_MY_FILES_TITLE, 5));

		assertEquals(0,
				ListView.getItemCount(HASH_MY_FILES_TITLE, "SysListView321"));

		// add file to ListView
		Assert.assertTrue(Win.menuSelectItem("HashMyFiles", null, "&File",
				"Add &Files"));
		Assert.assertTrue(Win.active(HASH_MY_FILES_ADD_FILES_TITLE));
		sleep(500);
		Control.setText(HASH_MY_FILES_ADD_FILES_TITLE, "Edit1", new File(
				"test/HashMyFiles.exe").getAbsolutePath());
		Keyboard.send("{ENTER}");
		sleep(1000);
		assertEquals(1,
				ListView.getItemCount(HASH_MY_FILES_TITLE, "SysListView321"));

		// add file to ListView
		Assert.assertTrue(Win.menuSelectItem("HashMyFiles", null, "&File",
				"Add &Files"));
		Assert.assertTrue(Win.active(HASH_MY_FILES_ADD_FILES_TITLE));
		sleep(500);
		Control.setText(HASH_MY_FILES_ADD_FILES_TITLE, "Edit1", new File(
				"test/iniDelete.tmp").getAbsolutePath());
		Keyboard.send("{ENTER}");
		sleep(1000);
		assertEquals(2,
				ListView.getItemCount(HASH_MY_FILES_TITLE, "SysListView321"));

		// close HashMyFiles
		Assert.assertTrue(Win.close(HASH_MY_FILES_TITLE));
	}

	@Test
	public void getSelected() {
		Assert.assertNull(ListView.getSelected(HASH_MY_FILES_TITLE,
				"SysListView321"));
		Assert.assertNull(ListView.getSelected(HASH_MY_FILES_TITLE,
				"SysListView321", false));
		Assert.assertNull(ListView.getSelected(HASH_MY_FILES_TITLE,
				"SysListView321", false));

		// run HashMyFiles
		int pid = Process.run(HASH_MY_FILES);
		Assert.assertTrue(pid > 0);
		Assert.assertTrue(Win.waitActive(HASH_MY_FILES_TITLE, 3));

		assertEquals(0,
				ListView.getItemCount(HASH_MY_FILES_TITLE, "SysListView321"));

		// add file to ListView
		Assert.assertTrue(Win.menuSelectItem("HashMyFiles", null, "&File",
				"Add &Files"));
		Assert.assertTrue(Win.active(HASH_MY_FILES_ADD_FILES_TITLE));
		sleep(500);
		Control.setText(HASH_MY_FILES_ADD_FILES_TITLE, "Edit1", new File(
				"test/HashMyFiles.exe").getAbsolutePath());
		Keyboard.send("{ENTER}");
		sleep(1000);
		assertEquals(1,
				ListView.getItemCount(HASH_MY_FILES_TITLE, "SysListView321"));

		// add file to ListView
		Assert.assertTrue(Win.menuSelectItem("HashMyFiles", null, "&File",
				"Add &Files"));
		Assert.assertTrue(Win.active(HASH_MY_FILES_ADD_FILES_TITLE));
		sleep(500);
		Control.setText(HASH_MY_FILES_ADD_FILES_TITLE, "Edit1", new File(
				"test/iniDelete.tmp").getAbsolutePath());
		Keyboard.send("{ENTER}");
		sleep(1000);
		assertEquals(2,
				ListView.getItemCount(HASH_MY_FILES_TITLE, "SysListView321"));

		Assert.assertNull(ListView.getSelected(HASH_MY_FILES_TITLE,
				"SysListView321"));
		Assert.assertArrayEquals(new int[] {}, ListView.getSelected(
				HASH_MY_FILES_TITLE, "SysListView321", false));
		Assert.assertArrayEquals(new int[] {}, ListView.getSelected(
				HASH_MY_FILES_TITLE, "SysListView321", true));

		// select all items
		Assert.assertTrue(ListView.select(HASH_MY_FILES_TITLE,
				"SysListView321", 0));
		assertEquals(0,
				ListView.getSelected(HASH_MY_FILES_TITLE, "SysListView321"));
		Assert.assertArrayEquals(new int[] { 0 }, ListView.getSelected(
				HASH_MY_FILES_TITLE, "SysListView321", false));
		Assert.assertArrayEquals(new int[] { 0, 1 }, ListView.getSelected(
				HASH_MY_FILES_TITLE, "SysListView321", true));

		// only select the first item
		Assert.assertTrue(ListView.deSelect(HASH_MY_FILES_TITLE,
				"SysListView321", 0));
		Assert.assertTrue(ListView.select(HASH_MY_FILES_TITLE,
				"SysListView321", 0, 0));
		assertEquals(0,
				ListView.getSelected(HASH_MY_FILES_TITLE, "SysListView321"));
		Assert.assertArrayEquals(new int[] { 0 }, ListView.getSelected(
				HASH_MY_FILES_TITLE, "SysListView321", false));
		Assert.assertArrayEquals(new int[] { 0 }, ListView.getSelected(
				HASH_MY_FILES_TITLE, "SysListView321", true));

		// only select the second item
		Assert.assertTrue(ListView.deSelect(HASH_MY_FILES_TITLE,
				"SysListView321", 0));
		Assert.assertTrue(ListView.select(HASH_MY_FILES_TITLE,
				"SysListView321", 1));
		assertEquals(1,
				ListView.getSelected(HASH_MY_FILES_TITLE, "SysListView321"));
		Assert.assertArrayEquals(new int[] { 1 }, ListView.getSelected(
				HASH_MY_FILES_TITLE, "SysListView321", false));
		Assert.assertArrayEquals(new int[] { 1 }, ListView.getSelected(
				HASH_MY_FILES_TITLE, "SysListView321", true));

		// unselect all items
		Assert.assertTrue(ListView.deSelect(HASH_MY_FILES_TITLE,
				"SysListView321", 0));
		Assert.assertTrue(ListView.select(HASH_MY_FILES_TITLE,
				"SysListView321", 2));
		Assert.assertNull(ListView.getSelected(HASH_MY_FILES_TITLE,
				"SysListView321"));
		Assert.assertArrayEquals(new int[] {}, ListView.getSelected(
				HASH_MY_FILES_TITLE, "SysListView321", false));
		Assert.assertArrayEquals(new int[] {}, ListView.getSelected(
				HASH_MY_FILES_TITLE, "SysListView321", true));

		// select all items
		Assert.assertTrue(ListView.select(HASH_MY_FILES_TITLE,
				"SysListView321", 0));
		assertEquals(0,
				ListView.getSelected(HASH_MY_FILES_TITLE, "SysListView321"));
		Assert.assertArrayEquals(new int[] { 0 }, ListView.getSelected(
				HASH_MY_FILES_TITLE, "SysListView321", false));
		Assert.assertArrayEquals(new int[] { 0, 1 }, ListView.getSelected(
				HASH_MY_FILES_TITLE, "SysListView321", true));

		Assert.assertNull(ListView.getSelected(HASH_MY_FILES_TITLE,
				"SysListView321" + System.currentTimeMillis()));
		Assert.assertNull(ListView.getSelected(HASH_MY_FILES_TITLE,
				"SysListView321" + System.currentTimeMillis(), false));
		Assert.assertNull(ListView.getSelected(HASH_MY_FILES_TITLE,
				"SysListView321" + System.currentTimeMillis(), true));

		// close HashMyFiles
		Assert.assertTrue(Win.close(HASH_MY_FILES_TITLE));
	}

	@Test
	public void getSelectedCount() {
		Assert.assertNull(ListView.getSelectedCount(HASH_MY_FILES_TITLE,
				"SysListView321"));

		// run HashMyFiles
		int pid = Process.run(HASH_MY_FILES);
		Assert.assertTrue(pid > 0);
		Assert.assertTrue(Win.waitActive(HASH_MY_FILES_TITLE, 3));

		assertEquals(0,
				ListView.getItemCount(HASH_MY_FILES_TITLE, "SysListView321"));

		// add file to ListView
		Assert.assertTrue(Win.menuSelectItem("HashMyFiles", null, "&File",
				"Add &Files"));
		Assert.assertTrue(Win.active(HASH_MY_FILES_ADD_FILES_TITLE));
		sleep(500);
		Control.setText(HASH_MY_FILES_ADD_FILES_TITLE, "Edit1", new File(
				"test/HashMyFiles.exe").getAbsolutePath());
		Keyboard.send("{ENTER}");
		sleep(1000);
		assertEquals(1,
				ListView.getItemCount(HASH_MY_FILES_TITLE, "SysListView321"));

		// add file to ListView
		Assert.assertTrue(Win.menuSelectItem("HashMyFiles", null, "&File",
				"Add &Files"));
		Assert.assertTrue(Win.active(HASH_MY_FILES_ADD_FILES_TITLE));
		sleep(500);
		Control.setText(HASH_MY_FILES_ADD_FILES_TITLE, "Edit1", new File(
				"test/iniDelete.tmp").getAbsolutePath());
		Keyboard.send("{ENTER}");
		sleep(1000);
		assertEquals(2,
				ListView.getItemCount(HASH_MY_FILES_TITLE, "SysListView321"));

		assertEquals(0, ListView.getSelectedCount(HASH_MY_FILES_TITLE,
				"SysListView321"));

		// select all items
		Assert.assertTrue(ListView.select(HASH_MY_FILES_TITLE,
				"SysListView321", 0));
		assertEquals(2, ListView.getSelectedCount(HASH_MY_FILES_TITLE,
				"SysListView321"));

		// only select the first item
		Assert.assertTrue(ListView.deSelect(HASH_MY_FILES_TITLE,
				"SysListView321", 0));
		Assert.assertTrue(ListView.select(HASH_MY_FILES_TITLE,
				"SysListView321", 0, 0));
		assertEquals(1, ListView.getSelectedCount(HASH_MY_FILES_TITLE,
				"SysListView321"));

		// only select the second item
		Assert.assertTrue(ListView.deSelect(HASH_MY_FILES_TITLE,
				"SysListView321", 0));
		Assert.assertTrue(ListView.select(HASH_MY_FILES_TITLE,
				"SysListView321", 1));
		assertEquals(1, ListView.getSelectedCount(HASH_MY_FILES_TITLE,
				"SysListView321"));

		// unselect all items
		Assert.assertTrue(ListView.deSelect(HASH_MY_FILES_TITLE,
				"SysListView321", 0));
		Assert.assertTrue(ListView.select(HASH_MY_FILES_TITLE,
				"SysListView321", 2));
		assertEquals(0, ListView.getSelectedCount(HASH_MY_FILES_TITLE,
				"SysListView321"));

		// select all items
		Assert.assertTrue(ListView.select(HASH_MY_FILES_TITLE,
				"SysListView321", 0));
		assertEquals(2, ListView.getSelectedCount(HASH_MY_FILES_TITLE,
				"SysListView321"));

		Assert.assertNull(ListView.getSelectedCount(HASH_MY_FILES_TITLE,
				"SysListView321" + System.currentTimeMillis()));

		// close HashMyFiles
		Assert.assertTrue(Win.close(HASH_MY_FILES_TITLE));
	}

	@Test
	public void getSubItemCount() {
		Assert.assertNull(ListView.getSubItemCount(HASH_MY_FILES_TITLE,
				"SysListView321"));

		// run HashMyFiles
		int pid = Process.run(HASH_MY_FILES);
		Assert.assertTrue(pid > 0);
		Assert.assertTrue(Win.waitActive(HASH_MY_FILES_TITLE, 3));

		assertEquals(16,
				ListView.getSubItemCount(HASH_MY_FILES_TITLE, "SysListView321"));

		// add file to ListView
		Assert.assertTrue(Win.menuSelectItem("HashMyFiles", null, "&File",
				"Add &Files"));
		Assert.assertTrue(Win.active(HASH_MY_FILES_ADD_FILES_TITLE));
		sleep(500);
		Control.setText(HASH_MY_FILES_ADD_FILES_TITLE, "Edit1", new File(
				"test/HashMyFiles.exe").getAbsolutePath());
		Keyboard.send("{ENTER}");
		sleep(1000);
		assertEquals(1,
				ListView.getItemCount(HASH_MY_FILES_TITLE, "SysListView321"));

		assertEquals(16,
				ListView.getSubItemCount(HASH_MY_FILES_TITLE, "SysListView321"));

		// // hide Filename column
		// Assert.assertTrue(Win.menuSelectItem(HASH_MY_FILES_TITLE,
		// null, "&View", "Choose Colum&ns"));
		// Assert.assertTrue(Win.waitActive("Column Settings", 3));
		// int x = AutoItX.winGetPosX("Column Settings");
		// int y = AutoItX.winGetPosY("Column Settings");
		// AutoItX.mouseClick(MouseButton.LEFT, x + 26, y + 85);
		// Assert.assertTrue(AutoItX.controlClick("Column Settings",
		// "Button6"));
		//
		// sleep(1000);
		// assertEquals(15, ControlListView.getSubItemCount(
		// HASH_MY_FILES_TITLE, "SysListView321"));
		//
		// // show Filename column
		// Assert.assertTrue(Win.menuSelectItem(HASH_MY_FILES_TITLE,
		// null, "&View", "Choose Colum&ns"));
		// Assert.assertTrue(Win.waitActive("Column Settings", 3));
		// x = AutoItX.winGetPosX("Column Settings");
		// y = AutoItX.winGetPosY("Column Settings");
		// AutoItX.mouseClick(MouseButton.LEFT, x + 26, y + 85);
		// Assert.assertTrue(AutoItX.controlClick("Column Settings",
		// "Button6"));
		//
		// sleep(200);
		// assertEquals(16, ControlListView.getSubItemCount(
		// HASH_MY_FILES_TITLE, "SysListView321"));

		Assert.assertNull(ListView.getSubItemCount(HASH_MY_FILES_TITLE,
				"SysListView321" + System.currentTimeMillis()));

		// close HashMyFiles
		Assert.assertTrue(Win.close(HASH_MY_FILES_TITLE));
	}

	@Test
	public void getText() {
		Assert.assertNull(ListView.getText(HASH_MY_FILES_TITLE,
				"SysListView321", 0));
		Assert.assertNull(ListView.getText(HASH_MY_FILES_TITLE,
				"SysListView321", 0, 0));

		// run HashMyFiles
		int pid = Process.run(HASH_MY_FILES);
		Assert.assertTrue(pid > 0);
		Assert.assertTrue(Win.waitActive(HASH_MY_FILES_TITLE, 3));

		Assert.assertNull(ListView.getText(HASH_MY_FILES_TITLE,
				"SysListView321", 0));
		Assert.assertNull(ListView.getText(HASH_MY_FILES_TITLE,
				"SysListView321", 0, 0));

		// add file to ListView
		Assert.assertTrue(Win.menuSelectItem("HashMyFiles", null, "&File",
				"Add &Files"));
		Assert.assertTrue(Win.active(HASH_MY_FILES_ADD_FILES_TITLE));
		sleep(500);
		Control.setText(HASH_MY_FILES_ADD_FILES_TITLE, "Edit1", new File(
				"test/HashMyFiles.exe").getAbsolutePath());
		Keyboard.send("{ENTER}");
		sleep(1000);
		String[] texts = ListView.getText(HASH_MY_FILES_TITLE,
				"SysListView321", 0);
		Assert.assertNotNull(texts);
		Assert.assertEquals("HashMyFiles.exe", texts[0]);
		Assert.assertEquals("exe", texts[14]);
		Assert.assertEquals("HashMyFiles.exe",
				ListView.getText(HASH_MY_FILES_TITLE, "SysListView321", 0, 0));
		Assert.assertNull(ListView.getText(HASH_MY_FILES_TITLE,
				"SysListView321", 1));
		Assert.assertNull(ListView.getText(HASH_MY_FILES_TITLE,
				"SysListView321", 1, 0));
		Assert.assertNull(ListView.getText(HASH_MY_FILES_TITLE,
				"SysListView321", 1, 14));

		// add file to ListView
		Assert.assertTrue(Win.menuSelectItem("HashMyFiles", null, "&File",
				"Add &Files"));
		Assert.assertTrue(Win.active(HASH_MY_FILES_ADD_FILES_TITLE));
		sleep(500);
		Control.setText(HASH_MY_FILES_ADD_FILES_TITLE, "Edit1", new File(
				"test/你好.txt").getAbsolutePath());
		Keyboard.send("{ENTER}");
		sleep(1000);
		texts = ListView.getText(HASH_MY_FILES_TITLE, "SysListView321", 0);
		Assert.assertNotNull(texts);
		Assert.assertEquals("HashMyFiles.exe", texts[0]);
		Assert.assertEquals("exe", texts[14]);
		Assert.assertEquals("HashMyFiles.exe",
				ListView.getText(HASH_MY_FILES_TITLE, "SysListView321", 0, 0));
		Assert.assertEquals("exe",
				ListView.getText(HASH_MY_FILES_TITLE, "SysListView321", 0, 14));
		texts = ListView.getText(HASH_MY_FILES_TITLE, "SysListView321", 1);
		Assert.assertNotNull(texts);
		Assert.assertEquals("你好.txt", texts[0]);
		Assert.assertEquals("txt", texts[14]);
		Assert.assertEquals("你好.txt",
				ListView.getText(HASH_MY_FILES_TITLE, "SysListView321", 1, 0));
		Assert.assertEquals("txt",
				ListView.getText(HASH_MY_FILES_TITLE, "SysListView321", 1, 14));
		Assert.assertNull(ListView.getText(HASH_MY_FILES_TITLE,
				"SysListView321", 2));
		Assert.assertNull(ListView.getText(HASH_MY_FILES_TITLE,
				"SysListView321", 2, 0));
		Assert.assertNull(ListView.getText(HASH_MY_FILES_TITLE,
				"SysListView321", 2, 14));

		Assert.assertNull(ListView.getText(HASH_MY_FILES_TITLE,
				"SysListView321" + System.currentTimeMillis(), 0));
		Assert.assertNull(ListView.getText(HASH_MY_FILES_TITLE,
				"SysListView321" + System.currentTimeMillis(), 0, 0));
		Assert.assertNull(ListView.getText(HASH_MY_FILES_TITLE,
				"SysListView321" + System.currentTimeMillis(), 0, 14));

		// close HashMyFiles
		Assert.assertTrue(Win.close(HASH_MY_FILES_TITLE));
	}

	@Test
	public void isSelected() {
		Assert.assertFalse(ListView.isSelected(HASH_MY_FILES_TITLE,
				"SysListView321", 0));

		// run HashMyFiles
		int pid = Process.run(HASH_MY_FILES);
		Assert.assertTrue(pid > 0);
		Assert.assertTrue(Win.waitActive(HASH_MY_FILES_TITLE, 3));

		assertEquals(0,
				ListView.getItemCount(HASH_MY_FILES_TITLE, "SysListView321"));

		// add file to ListView
		Assert.assertTrue(Win.menuSelectItem("HashMyFiles", null, "&File",
				"Add &Files"));
		Assert.assertTrue(Win.active(HASH_MY_FILES_ADD_FILES_TITLE));
		sleep(500);
		Control.setText(HASH_MY_FILES_ADD_FILES_TITLE, "Edit1", new File(
				"test/HashMyFiles.exe").getAbsolutePath());
		Keyboard.send("{ENTER}");
		sleep(1000);
		assertEquals(1,
				ListView.getItemCount(HASH_MY_FILES_TITLE, "SysListView321"));

		// add file to ListView
		Assert.assertTrue(Win.menuSelectItem("HashMyFiles", null, "&File",
				"Add &Files"));
		Assert.assertTrue(Win.active(HASH_MY_FILES_ADD_FILES_TITLE));
		sleep(500);
		Control.setText(HASH_MY_FILES_ADD_FILES_TITLE, "Edit1", new File(
				"test/iniDelete.tmp").getAbsolutePath());
		Keyboard.send("{ENTER}");
		sleep(1000);
		assertEquals(2,
				ListView.getItemCount(HASH_MY_FILES_TITLE, "SysListView321"));

		Assert.assertFalse(ListView.isSelected(HASH_MY_FILES_TITLE,
				"SysListView321", -1));
		Assert.assertFalse(ListView.isSelected(HASH_MY_FILES_TITLE,
				"SysListView321", 0));
		Assert.assertFalse(ListView.isSelected(HASH_MY_FILES_TITLE,
				"SysListView321", 1));
		Assert.assertFalse(ListView.isSelected(HASH_MY_FILES_TITLE,
				"SysListView321", 2));

		// select all items
		Assert.assertTrue(ListView.select(HASH_MY_FILES_TITLE,
				"SysListView321", 0));
		Assert.assertFalse(ListView.isSelected(HASH_MY_FILES_TITLE,
				"SysListView321", -1));
		Assert.assertTrue(ListView.isSelected(HASH_MY_FILES_TITLE,
				"SysListView321", 0));
		Assert.assertTrue(ListView.isSelected(HASH_MY_FILES_TITLE,
				"SysListView321", 1));
		Assert.assertFalse(ListView.isSelected(HASH_MY_FILES_TITLE,
				"SysListView321", 2));

		// only select the first item
		Assert.assertTrue(ListView.deSelect(HASH_MY_FILES_TITLE,
				"SysListView321", 0));
		Assert.assertTrue(ListView.select(HASH_MY_FILES_TITLE,
				"SysListView321", 0, 0));
		Assert.assertFalse(ListView.isSelected(HASH_MY_FILES_TITLE,
				"SysListView321", -1));
		Assert.assertTrue(ListView.isSelected(HASH_MY_FILES_TITLE,
				"SysListView321", 0));
		Assert.assertFalse(ListView.isSelected(HASH_MY_FILES_TITLE,
				"SysListView321", 1));
		Assert.assertFalse(ListView.isSelected(HASH_MY_FILES_TITLE,
				"SysListView321", 2));

		// only select the second item
		Assert.assertTrue(ListView.deSelect(HASH_MY_FILES_TITLE,
				"SysListView321", 0));
		Assert.assertTrue(ListView.select(HASH_MY_FILES_TITLE,
				"SysListView321", 1));
		Assert.assertFalse(ListView.isSelected(HASH_MY_FILES_TITLE,
				"SysListView321", -1));
		Assert.assertFalse(ListView.isSelected(HASH_MY_FILES_TITLE,
				"SysListView321", 0));
		Assert.assertTrue(ListView.isSelected(HASH_MY_FILES_TITLE,
				"SysListView321", 1));
		Assert.assertFalse(ListView.isSelected(HASH_MY_FILES_TITLE,
				"SysListView321", 2));

		// unselect all items
		Assert.assertTrue(ListView.deSelect(HASH_MY_FILES_TITLE,
				"SysListView321", 0));
		Assert.assertTrue(ListView.select(HASH_MY_FILES_TITLE,
				"SysListView321", 2));
		Assert.assertFalse(ListView.isSelected(HASH_MY_FILES_TITLE,
				"SysListView321", -1));
		Assert.assertFalse(ListView.isSelected(HASH_MY_FILES_TITLE,
				"SysListView321", 0));
		Assert.assertFalse(ListView.isSelected(HASH_MY_FILES_TITLE,
				"SysListView321", 1));
		Assert.assertFalse(ListView.isSelected(HASH_MY_FILES_TITLE,
				"SysListView321", 2));

		// select all items
		Assert.assertTrue(ListView.select(HASH_MY_FILES_TITLE,
				"SysListView321", 0));
		Assert.assertFalse(ListView.isSelected(HASH_MY_FILES_TITLE,
				"SysListView321", -1));
		Assert.assertTrue(ListView.isSelected(HASH_MY_FILES_TITLE,
				"SysListView321", 0));
		Assert.assertTrue(ListView.isSelected(HASH_MY_FILES_TITLE,
				"SysListView321", 1));
		Assert.assertFalse(ListView.isSelected(HASH_MY_FILES_TITLE,
				"SysListView321", 2));

		Assert.assertFalse(ListView.isSelected(HASH_MY_FILES_TITLE,
				"SysListView321" + System.currentTimeMillis(), -1));
		Assert.assertFalse(ListView.isSelected(HASH_MY_FILES_TITLE,
				"SysListView321" + System.currentTimeMillis(), 0));
		Assert.assertFalse(ListView.isSelected(HASH_MY_FILES_TITLE,
				"SysListView321" + System.currentTimeMillis(), 1));
		Assert.assertFalse(ListView.isSelected(HASH_MY_FILES_TITLE,
				"SysListView321" + System.currentTimeMillis(), 2));

		// close HashMyFiles
		Assert.assertTrue(Win.close(HASH_MY_FILES_TITLE));
	}

	@Test
	public void select() {
		Assert.assertFalse(ListView.select(HASH_MY_FILES_TITLE,
				"SysListView321", 0));

		// run HashMyFiles
		int pid = Process.run(HASH_MY_FILES);
		Assert.assertTrue(pid > 0);
		Assert.assertTrue(Win.waitActive(HASH_MY_FILES_TITLE, 3));

		assertEquals(0,
				ListView.getItemCount(HASH_MY_FILES_TITLE, "SysListView321"));

		// add file to ListView
		Assert.assertTrue(Win.menuSelectItem("HashMyFiles", null, "&File",
				"Add &Files"));
		Assert.assertTrue(Win.active(HASH_MY_FILES_ADD_FILES_TITLE));
		sleep(500);
		Control.setText(HASH_MY_FILES_ADD_FILES_TITLE, "Edit1", new File(
				"test/HashMyFiles.exe").getAbsolutePath());
		Keyboard.send("{ENTER}");
		sleep(1000);
		assertEquals(1,
				ListView.getItemCount(HASH_MY_FILES_TITLE, "SysListView321"));

		// add file to ListView
		Assert.assertTrue(Win.menuSelectItem("HashMyFiles", null, "&File",
				"Add &Files"));
		Assert.assertTrue(Win.active(HASH_MY_FILES_ADD_FILES_TITLE));
		sleep(500);
		Control.setText(HASH_MY_FILES_ADD_FILES_TITLE, "Edit1", new File(
				"test/iniDelete.tmp").getAbsolutePath());
		Keyboard.send("{ENTER}");
		sleep(1000);
		assertEquals(2,
				ListView.getItemCount(HASH_MY_FILES_TITLE, "SysListView321"));

		Assert.assertFalse(ListView.isSelected(HASH_MY_FILES_TITLE,
				"SysListView321", -1));
		Assert.assertFalse(ListView.isSelected(HASH_MY_FILES_TITLE,
				"SysListView321", 0));
		Assert.assertFalse(ListView.isSelected(HASH_MY_FILES_TITLE,
				"SysListView321", 1));
		Assert.assertFalse(ListView.isSelected(HASH_MY_FILES_TITLE,
				"SysListView321", 2));

		// select all items
		Assert.assertTrue(ListView.select(HASH_MY_FILES_TITLE,
				"SysListView321", 0));
		Assert.assertFalse(ListView.isSelected(HASH_MY_FILES_TITLE,
				"SysListView321", -1));
		Assert.assertTrue(ListView.isSelected(HASH_MY_FILES_TITLE,
				"SysListView321", 0));
		Assert.assertTrue(ListView.isSelected(HASH_MY_FILES_TITLE,
				"SysListView321", 1));
		Assert.assertFalse(ListView.isSelected(HASH_MY_FILES_TITLE,
				"SysListView321", 2));

		// deselect all items, then select all items
		Assert.assertTrue(ListView.deSelect(HASH_MY_FILES_TITLE,
				"SysListView321", 0));
		assertEquals(0, ListView.getSelectedCount(HASH_MY_FILES_TITLE,
				"SysListView321"));
		Assert.assertTrue(ListView.select(HASH_MY_FILES_TITLE,
				"SysListView321", -1));
		Assert.assertFalse(ListView.isSelected(HASH_MY_FILES_TITLE,
				"SysListView321", -1));
		Assert.assertTrue(ListView.isSelected(HASH_MY_FILES_TITLE,
				"SysListView321", 0));
		Assert.assertTrue(ListView.isSelected(HASH_MY_FILES_TITLE,
				"SysListView321", 1));
		Assert.assertFalse(ListView.isSelected(HASH_MY_FILES_TITLE,
				"SysListView321", 2));

		// deselect all items, then select all items
		Assert.assertTrue(ListView.deSelect(HASH_MY_FILES_TITLE,
				"SysListView321", 0));
		assertEquals(0, ListView.getSelectedCount(HASH_MY_FILES_TITLE,
				"SysListView321"));
		Assert.assertTrue(ListView.select(HASH_MY_FILES_TITLE,
				"SysListView321", 0, 1));
		Assert.assertFalse(ListView.isSelected(HASH_MY_FILES_TITLE,
				"SysListView321", -1));
		Assert.assertTrue(ListView.isSelected(HASH_MY_FILES_TITLE,
				"SysListView321", 0));
		Assert.assertTrue(ListView.isSelected(HASH_MY_FILES_TITLE,
				"SysListView321", 1));
		Assert.assertFalse(ListView.isSelected(HASH_MY_FILES_TITLE,
				"SysListView321", 2));

		// deselect all items, then select all items
		Assert.assertTrue(ListView.deSelect(HASH_MY_FILES_TITLE,
				"SysListView321", 0));
		assertEquals(0, ListView.getSelectedCount(HASH_MY_FILES_TITLE,
				"SysListView321"));
		Assert.assertTrue(ListView.select(HASH_MY_FILES_TITLE,
				"SysListView321", 0, 2));
		Assert.assertFalse(ListView.isSelected(HASH_MY_FILES_TITLE,
				"SysListView321", -1));
		Assert.assertTrue(ListView.isSelected(HASH_MY_FILES_TITLE,
				"SysListView321", 0));
		Assert.assertTrue(ListView.isSelected(HASH_MY_FILES_TITLE,
				"SysListView321", 1));
		Assert.assertFalse(ListView.isSelected(HASH_MY_FILES_TITLE,
				"SysListView321", 2));

		// deselect all items, then select all items
		Assert.assertTrue(ListView.deSelect(HASH_MY_FILES_TITLE,
				"SysListView321", 0));
		assertEquals(0, ListView.getSelectedCount(HASH_MY_FILES_TITLE,
				"SysListView321"));
		Assert.assertTrue(ListView.select(HASH_MY_FILES_TITLE,
				"SysListView321", -1, 1));
		Assert.assertFalse(ListView.isSelected(HASH_MY_FILES_TITLE,
				"SysListView321", -1));
		Assert.assertTrue(ListView.isSelected(HASH_MY_FILES_TITLE,
				"SysListView321", 0));
		Assert.assertTrue(ListView.isSelected(HASH_MY_FILES_TITLE,
				"SysListView321", 1));
		Assert.assertFalse(ListView.isSelected(HASH_MY_FILES_TITLE,
				"SysListView321", 2));

		// deselect all items, then select all items
		Assert.assertTrue(ListView.deSelect(HASH_MY_FILES_TITLE,
				"SysListView321", 0));
		assertEquals(0, ListView.getSelectedCount(HASH_MY_FILES_TITLE,
				"SysListView321"));
		Assert.assertTrue(ListView.select(HASH_MY_FILES_TITLE,
				"SysListView321", -1, 2));
		Assert.assertFalse(ListView.isSelected(HASH_MY_FILES_TITLE,
				"SysListView321", -1));
		Assert.assertTrue(ListView.isSelected(HASH_MY_FILES_TITLE,
				"SysListView321", 0));
		Assert.assertTrue(ListView.isSelected(HASH_MY_FILES_TITLE,
				"SysListView321", 1));
		Assert.assertFalse(ListView.isSelected(HASH_MY_FILES_TITLE,
				"SysListView321", 2));

		// only select the first item
		Assert.assertTrue(ListView.deSelect(HASH_MY_FILES_TITLE,
				"SysListView321", 0));
		Assert.assertTrue(ListView.select(HASH_MY_FILES_TITLE,
				"SysListView321", 0, 0));
		Assert.assertFalse(ListView.isSelected(HASH_MY_FILES_TITLE,
				"SysListView321", -1));
		Assert.assertTrue(ListView.isSelected(HASH_MY_FILES_TITLE,
				"SysListView321", 0));
		Assert.assertFalse(ListView.isSelected(HASH_MY_FILES_TITLE,
				"SysListView321", 1));
		Assert.assertFalse(ListView.isSelected(HASH_MY_FILES_TITLE,
				"SysListView321", 2));

		// only select the second item
		Assert.assertTrue(ListView.deSelect(HASH_MY_FILES_TITLE,
				"SysListView321", 0));
		Assert.assertTrue(ListView.select(HASH_MY_FILES_TITLE,
				"SysListView321", 1));
		Assert.assertFalse(ListView.isSelected(HASH_MY_FILES_TITLE,
				"SysListView321", -1));
		Assert.assertFalse(ListView.isSelected(HASH_MY_FILES_TITLE,
				"SysListView321", 0));
		Assert.assertTrue(ListView.isSelected(HASH_MY_FILES_TITLE,
				"SysListView321", 1));
		Assert.assertFalse(ListView.isSelected(HASH_MY_FILES_TITLE,
				"SysListView321", 2));

		// unselect all items
		Assert.assertTrue(ListView.deSelect(HASH_MY_FILES_TITLE,
				"SysListView321", 0));
		Assert.assertTrue(ListView.select(HASH_MY_FILES_TITLE,
				"SysListView321", 2));
		Assert.assertFalse(ListView.isSelected(HASH_MY_FILES_TITLE,
				"SysListView321", -1));
		Assert.assertFalse(ListView.isSelected(HASH_MY_FILES_TITLE,
				"SysListView321", 0));
		Assert.assertFalse(ListView.isSelected(HASH_MY_FILES_TITLE,
				"SysListView321", 1));
		Assert.assertFalse(ListView.isSelected(HASH_MY_FILES_TITLE,
				"SysListView321", 2));

		Assert.assertFalse(ListView.select(HASH_MY_FILES_TITLE,
				"SysListView321" + System.currentTimeMillis(), -1));
		Assert.assertFalse(ListView.select(HASH_MY_FILES_TITLE,
				"SysListView321" + System.currentTimeMillis(), 0));
		Assert.assertFalse(ListView.select(HASH_MY_FILES_TITLE,
				"SysListView321" + System.currentTimeMillis(), 1));
		Assert.assertFalse(ListView.select(HASH_MY_FILES_TITLE,
				"SysListView321" + System.currentTimeMillis(), 2));

		// close HashMyFiles
		Assert.assertTrue(Win.close(HASH_MY_FILES_TITLE));
	}

	@Test
	public void selectAll() {
		Assert.assertFalse(ListView.selectAll(HASH_MY_FILES_TITLE,
				"SysListView321"));

		// run HashMyFiles
		int pid = Process.run(HASH_MY_FILES);
		Assert.assertTrue(pid > 0);
		Assert.assertTrue(Win.waitActive(HASH_MY_FILES_TITLE, 3));

		assertEquals(0,
				ListView.getItemCount(HASH_MY_FILES_TITLE, "SysListView321"));

		// add file to ListView
		Assert.assertTrue(Win.menuSelectItem("HashMyFiles", null, "&File",
				"Add &Files"));
		Assert.assertTrue(Win.active(HASH_MY_FILES_ADD_FILES_TITLE));
		sleep(500);
		Control.setText(HASH_MY_FILES_ADD_FILES_TITLE, "Edit1", new File(
				"test/HashMyFiles.exe").getAbsolutePath());
		Keyboard.send("{ENTER}");
		sleep(1000);
		assertEquals(1,
				ListView.getItemCount(HASH_MY_FILES_TITLE, "SysListView321"));

		// add file to ListView
		Assert.assertTrue(Win.menuSelectItem("HashMyFiles", null, "&File",
				"Add &Files"));
		Assert.assertTrue(Win.active(HASH_MY_FILES_ADD_FILES_TITLE));
		sleep(500);
		Control.setText(HASH_MY_FILES_ADD_FILES_TITLE, "Edit1", new File(
				"test/iniDelete.tmp").getAbsolutePath());
		Keyboard.send("{ENTER}");
		sleep(1000);
		assertEquals(2,
				ListView.getItemCount(HASH_MY_FILES_TITLE, "SysListView321"));

		Assert.assertFalse(ListView.isSelected(HASH_MY_FILES_TITLE,
				"SysListView321", -1));
		Assert.assertFalse(ListView.isSelected(HASH_MY_FILES_TITLE,
				"SysListView321", 0));
		Assert.assertFalse(ListView.isSelected(HASH_MY_FILES_TITLE,
				"SysListView321", 1));
		Assert.assertFalse(ListView.isSelected(HASH_MY_FILES_TITLE,
				"SysListView321", 2));

		// select all items
		Assert.assertTrue(ListView.selectAll(HASH_MY_FILES_TITLE,
				"SysListView321"));
		Assert.assertTrue(ListView.isSelected(HASH_MY_FILES_TITLE,
				"SysListView321", 0));
		Assert.assertTrue(ListView.isSelected(HASH_MY_FILES_TITLE,
				"SysListView321", 1));

		// deselect all items, then select all items
		Assert.assertTrue(ListView.deSelect(HASH_MY_FILES_TITLE,
				"SysListView321", 0));
		assertEquals(0, ListView.getSelectedCount(HASH_MY_FILES_TITLE,
				"SysListView321"));
		Assert.assertTrue(ListView.selectAll(HASH_MY_FILES_TITLE,
				"SysListView321"));
		Assert.assertTrue(ListView.isSelected(HASH_MY_FILES_TITLE,
				"SysListView321", 0));
		Assert.assertTrue(ListView.isSelected(HASH_MY_FILES_TITLE,
				"SysListView321", 1));

		// unselect all items
		Assert.assertTrue(ListView.deSelect(HASH_MY_FILES_TITLE,
				"SysListView321", 0));
		Assert.assertFalse(ListView.isSelected(HASH_MY_FILES_TITLE,
				"SysListView321", 0));
		Assert.assertFalse(ListView.isSelected(HASH_MY_FILES_TITLE,
				"SysListView321", 1));

		Assert.assertFalse(ListView.selectAll(HASH_MY_FILES_TITLE,
				"SysListView321" + System.currentTimeMillis()));
		Assert.assertFalse(ListView.isSelected(HASH_MY_FILES_TITLE,
				"SysListView321", 0));
		Assert.assertFalse(ListView.isSelected(HASH_MY_FILES_TITLE,
				"SysListView321", 1));

		// close HashMyFiles
		Assert.assertTrue(Win.close(HASH_MY_FILES_TITLE));
	}

	@Test
	public void selectClear() {
		Assert.assertFalse(ListView.selectClear(HASH_MY_FILES_TITLE,
				"SysListView321"));

		// run HashMyFiles
		int pid = Process.run(HASH_MY_FILES);
		Assert.assertTrue(pid > 0);
		Assert.assertTrue(Win.waitActive(HASH_MY_FILES_TITLE, 3));

		assertEquals(0,
				ListView.getItemCount(HASH_MY_FILES_TITLE, "SysListView321"));

		// add file to ListView
		Assert.assertTrue(Win.menuSelectItem("HashMyFiles", null, "&File",
				"Add &Files"));
		Assert.assertTrue(Win.active(HASH_MY_FILES_ADD_FILES_TITLE));
		sleep(500);
		Control.setText(HASH_MY_FILES_ADD_FILES_TITLE, "Edit1", new File(
				"test/HashMyFiles.exe").getAbsolutePath());
		Keyboard.send("{ENTER}");
		sleep(1000);
		assertEquals(1,
				ListView.getItemCount(HASH_MY_FILES_TITLE, "SysListView321"));

		// add file to ListView
		Assert.assertTrue(Win.menuSelectItem("HashMyFiles", null, "&File",
				"Add &Files"));
		Assert.assertTrue(Win.active(HASH_MY_FILES_ADD_FILES_TITLE));
		sleep(500);
		Control.setText(HASH_MY_FILES_ADD_FILES_TITLE, "Edit1", new File(
				"test/iniDelete.tmp").getAbsolutePath());
		Keyboard.send("{ENTER}");
		sleep(1000);
		assertEquals(2,
				ListView.getItemCount(HASH_MY_FILES_TITLE, "SysListView321"));

		Assert.assertFalse(ListView.isSelected(HASH_MY_FILES_TITLE,
				"SysListView321", -1));
		Assert.assertFalse(ListView.isSelected(HASH_MY_FILES_TITLE,
				"SysListView321", 0));
		Assert.assertFalse(ListView.isSelected(HASH_MY_FILES_TITLE,
				"SysListView321", 1));
		Assert.assertFalse(ListView.isSelected(HASH_MY_FILES_TITLE,
				"SysListView321", 2));

		// select all items
		Assert.assertTrue(ListView.select(HASH_MY_FILES_TITLE,
				"SysListView321", 0));
		Assert.assertTrue(ListView.isSelected(HASH_MY_FILES_TITLE,
				"SysListView321", 0));
		Assert.assertTrue(ListView.isSelected(HASH_MY_FILES_TITLE,
				"SysListView321", 1));

		Assert.assertTrue(ListView.selectClear(HASH_MY_FILES_TITLE,
				"SysListView321"));
		Assert.assertFalse(ListView.isSelected(HASH_MY_FILES_TITLE,
				"SysListView321", 0));
		Assert.assertFalse(ListView.isSelected(HASH_MY_FILES_TITLE,
				"SysListView321", 1));

		// only select the first item
		Assert.assertTrue(ListView.deSelect(HASH_MY_FILES_TITLE,
				"SysListView321", 0));
		Assert.assertTrue(ListView.select(HASH_MY_FILES_TITLE,
				"SysListView321", 0, 0));
		Assert.assertTrue(ListView.isSelected(HASH_MY_FILES_TITLE,
				"SysListView321", 0));
		Assert.assertFalse(ListView.isSelected(HASH_MY_FILES_TITLE,
				"SysListView321", 1));

		Assert.assertTrue(ListView.selectClear(HASH_MY_FILES_TITLE,
				"SysListView321"));
		Assert.assertFalse(ListView.isSelected(HASH_MY_FILES_TITLE,
				"SysListView321", 0));
		Assert.assertFalse(ListView.isSelected(HASH_MY_FILES_TITLE,
				"SysListView321", 1));

		// only select the second item
		Assert.assertTrue(ListView.deSelect(HASH_MY_FILES_TITLE,
				"SysListView321", 0));
		Assert.assertTrue(ListView.select(HASH_MY_FILES_TITLE,
				"SysListView321", 1));
		Assert.assertFalse(ListView.isSelected(HASH_MY_FILES_TITLE,
				"SysListView321", 0));
		Assert.assertTrue(ListView.isSelected(HASH_MY_FILES_TITLE,
				"SysListView321", 1));

		Assert.assertTrue(ListView.selectClear(HASH_MY_FILES_TITLE,
				"SysListView321"));
		Assert.assertFalse(ListView.isSelected(HASH_MY_FILES_TITLE,
				"SysListView321", 0));
		Assert.assertFalse(ListView.isSelected(HASH_MY_FILES_TITLE,
				"SysListView321", 1));

		Assert.assertFalse(ListView.selectClear(HASH_MY_FILES_TITLE,
				"SysListView321" + System.currentTimeMillis()));

		// close HashMyFiles
		Assert.assertTrue(Win.close(HASH_MY_FILES_TITLE));
	}

	@Test
	public void selectInvert() {
		Assert.assertFalse(ListView.selectInvert(HASH_MY_FILES_TITLE,
				"SysListView321"));

		// run HashMyFiles
		int pid = Process.run(HASH_MY_FILES);
		Assert.assertTrue(pid > 0);
		Assert.assertTrue(Win.waitActive(HASH_MY_FILES_TITLE, 3));

		assertEquals(0,
				ListView.getItemCount(HASH_MY_FILES_TITLE, "SysListView321"));

		// add file to ListView
		Assert.assertTrue(Win.menuSelectItem("HashMyFiles", null, "&File",
				"Add &Files"));
		Assert.assertTrue(Win.active(HASH_MY_FILES_ADD_FILES_TITLE));
		sleep(500);
		Control.setText(HASH_MY_FILES_ADD_FILES_TITLE, "Edit1", new File(
				"test/HashMyFiles.exe").getAbsolutePath());
		Keyboard.send("{ENTER}");
		sleep(1000);
		assertEquals(1,
				ListView.getItemCount(HASH_MY_FILES_TITLE, "SysListView321"));

		// add file to ListView
		Assert.assertTrue(Win.menuSelectItem("HashMyFiles", null, "&File",
				"Add &Files"));
		Assert.assertTrue(Win.active(HASH_MY_FILES_ADD_FILES_TITLE));
		sleep(500);
		Control.setText(HASH_MY_FILES_ADD_FILES_TITLE, "Edit1", new File(
				"test/iniDelete.tmp").getAbsolutePath());
		Keyboard.send("{ENTER}");
		sleep(1000);
		assertEquals(2,
				ListView.getItemCount(HASH_MY_FILES_TITLE, "SysListView321"));

		Assert.assertFalse(ListView.isSelected(HASH_MY_FILES_TITLE,
				"SysListView321", 0));
		Assert.assertFalse(ListView.isSelected(HASH_MY_FILES_TITLE,
				"SysListView321", 1));

		// select all items
		Assert.assertTrue(ListView.select(HASH_MY_FILES_TITLE,
				"SysListView321", 0));
		Assert.assertTrue(ListView.isSelected(HASH_MY_FILES_TITLE,
				"SysListView321", 0));
		Assert.assertTrue(ListView.isSelected(HASH_MY_FILES_TITLE,
				"SysListView321", 1));

		Assert.assertTrue(ListView.selectInvert(HASH_MY_FILES_TITLE,
				"SysListView321"));
		Assert.assertFalse(ListView.isSelected(HASH_MY_FILES_TITLE,
				"SysListView321", 0));
		Assert.assertFalse(ListView.isSelected(HASH_MY_FILES_TITLE,
				"SysListView321", 1));

		Assert.assertTrue(ListView.selectInvert(HASH_MY_FILES_TITLE,
				"SysListView321"));
		Assert.assertTrue(ListView.isSelected(HASH_MY_FILES_TITLE,
				"SysListView321", 0));
		Assert.assertTrue(ListView.isSelected(HASH_MY_FILES_TITLE,
				"SysListView321", 1));

		// only select the first item
		Assert.assertTrue(ListView.deSelect(HASH_MY_FILES_TITLE,
				"SysListView321", 0));
		Assert.assertTrue(ListView.select(HASH_MY_FILES_TITLE,
				"SysListView321", 0, 0));
		Assert.assertTrue(ListView.isSelected(HASH_MY_FILES_TITLE,
				"SysListView321", 0));
		Assert.assertFalse(ListView.isSelected(HASH_MY_FILES_TITLE,
				"SysListView321", 1));

		Assert.assertTrue(ListView.selectInvert(HASH_MY_FILES_TITLE,
				"SysListView321"));
		Assert.assertFalse(ListView.isSelected(HASH_MY_FILES_TITLE,
				"SysListView321", 0));
		Assert.assertTrue(ListView.isSelected(HASH_MY_FILES_TITLE,
				"SysListView321", 1));

		Assert.assertTrue(ListView.selectInvert(HASH_MY_FILES_TITLE,
				"SysListView321"));
		Assert.assertTrue(ListView.isSelected(HASH_MY_FILES_TITLE,
				"SysListView321", 0));
		Assert.assertFalse(ListView.isSelected(HASH_MY_FILES_TITLE,
				"SysListView321", 1));

		// only select the second item
		Assert.assertTrue(ListView.deSelect(HASH_MY_FILES_TITLE,
				"SysListView321", 0));
		Assert.assertTrue(ListView.select(HASH_MY_FILES_TITLE,
				"SysListView321", 1));
		Assert.assertFalse(ListView.isSelected(HASH_MY_FILES_TITLE,
				"SysListView321", 0));
		Assert.assertTrue(ListView.isSelected(HASH_MY_FILES_TITLE,
				"SysListView321", 1));

		Assert.assertTrue(ListView.selectInvert(HASH_MY_FILES_TITLE,
				"SysListView321"));
		Assert.assertTrue(ListView.isSelected(HASH_MY_FILES_TITLE,
				"SysListView321", 0));
		Assert.assertFalse(ListView.isSelected(HASH_MY_FILES_TITLE,
				"SysListView321", 1));

		Assert.assertTrue(ListView.selectInvert(HASH_MY_FILES_TITLE,
				"SysListView321"));
		Assert.assertFalse(ListView.isSelected(HASH_MY_FILES_TITLE,
				"SysListView321", 0));
		Assert.assertTrue(ListView.isSelected(HASH_MY_FILES_TITLE,
				"SysListView321", 1));

		Assert.assertFalse(ListView.selectInvert(HASH_MY_FILES_TITLE,
				"SysListView321" + System.currentTimeMillis()));
		Assert.assertFalse(ListView.isSelected(HASH_MY_FILES_TITLE,
				"SysListView321", 0));
		Assert.assertTrue(ListView.isSelected(HASH_MY_FILES_TITLE,
				"SysListView321", 1));

		// close HashMyFiles
		Assert.assertTrue(Win.close(HASH_MY_FILES_TITLE));
	}

	@Test
	public void viewChange() {
		Assert.assertFalse(ListView.viewChange(HASH_MY_FILES_TITLE,
				"SysListView321", ControlListViewView.LIST));

		// run HashMyFiles
		int pid = Process.run(HASH_MY_FILES);
		Assert.assertTrue(pid > 0);
		Assert.assertTrue(Win.waitActive(HASH_MY_FILES_TITLE, 3));

		assertEquals(0,
				ListView.getItemCount(HASH_MY_FILES_TITLE, "SysListView321"));

		// add file to ListView
		Assert.assertTrue(Win.menuSelectItem("HashMyFiles", null, "&File",
				"Add &Files"));
		Assert.assertTrue(Win.active(HASH_MY_FILES_ADD_FILES_TITLE));
		sleep(500);
		Control.setText(HASH_MY_FILES_ADD_FILES_TITLE, "Edit1", new File(
				"test/HashMyFiles.exe").getAbsolutePath());
		Keyboard.send("{ENTER}");
		sleep(1000);
		assertEquals(1,
				ListView.getItemCount(HASH_MY_FILES_TITLE, "SysListView321"));

		// add file to ListView
		Assert.assertTrue(Win.menuSelectItem("HashMyFiles", null, "&File",
				"Add &Files"));
		Assert.assertTrue(Win.active(HASH_MY_FILES_ADD_FILES_TITLE));
		sleep(500);
		Control.setText(HASH_MY_FILES_ADD_FILES_TITLE, "Edit1", new File(
				"test/iniDelete.tmp").getAbsolutePath());
		Keyboard.send("{ENTER}");
		sleep(1000);
		assertEquals(2,
				ListView.getItemCount(HASH_MY_FILES_TITLE, "SysListView321"));

		for (ControlListViewView view : ControlListViewView.values()) {
			Assert.assertTrue(ListView.viewChange(HASH_MY_FILES_TITLE,
					"SysListView321", view));
			sleep(1000);
		}

		for (ControlListViewView view : ControlListViewView.values()) {
			Assert.assertFalse(ListView.viewChange(HASH_MY_FILES_TITLE,
					"SysListView321", "InvalidView"));
			Assert.assertFalse(ListView.viewChange(HASH_MY_FILES_TITLE,
					"SysListView321" + System.currentTimeMillis(), view));
		}

		// close HashMyFiles
		Assert.assertTrue(Win.close(HASH_MY_FILES_TITLE));
	}

	@After
	public void tearDown() {
		File file = new File("test/HashMyFiles.cfg");
		if (file.exists()) {
			file.delete();
		}
	}
}
