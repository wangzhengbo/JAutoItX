package cn.com.jautoitx;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.junit.Assert;
import org.junit.Test;

import cn.com.jautoitx.TreeView.IsChecked;

public class TreeViewTest extends BaseTest {
	@Test
	public void check() {
		String title = "TreeView.check - " + System.currentTimeMillis();
		Assert.assertFalse(TreeView.check(title, "SysTreeView321", "节点2"));

		for (int i = 0; i < 2; i++) {
			boolean checkable = (i == 0);
			try {
				// show frame
				showTreeViewShell(title, checkable);

				Assert.assertFalse(TreeView
						.check(title, "SysTreeView321", null));
				Assert.assertFalse(TreeView.check(title, "SysTreeView321", ""));
				Assert.assertFalse(TreeView.check(title, "SysTreeView321", " "));
				Assert.assertFalse(TreeView.check(title, "SysTreeView321",
						"节点0"));
				Assert.assertFalse(TreeView.isChecked(title, "SysTreeView321",
						"节点1"));
				Assert.assertEquals(checkable,
						TreeView.check(title, "SysTreeView321", "节点1"));
				Assert.assertEquals(checkable,
						TreeView.check(title, "SysTreeView321", "节点1"));
				Assert.assertEquals(checkable,
						TreeView.isChecked(title, "SysTreeView321", "节点1"));

				Assert.assertFalse(TreeView.isChecked(title, "SysTreeView321",
						"节点2"));
				Assert.assertEquals(checkable, TreeView.check(
						Win.getHandle_(title),
						Control.getHandle_(title, "SysTreeView321"), "节点2"));
				Assert.assertEquals(checkable,
						TreeView.isChecked(title, "SysTreeView321", "节点2"));

				Assert.assertFalse(TreeView.isChecked(title, "SysTreeView321",
						"节点2|2A|2A1"));
				Assert.assertEquals(checkable,
						TreeView.check(title, "SysTreeView321", "节点2|2A|2A1"));
				Assert.assertEquals(checkable, TreeView.isChecked(title,
						"SysTreeView321", "节点2|2A|2A1"));

				Assert.assertFalse(TreeView.isChecked(title, "SysTreeView321",
						"节点2|2A"));
				Assert.assertEquals(checkable,
						TreeView.check(title, "SysTreeView321", "节点2|2A"));
				Assert.assertEquals(checkable,
						TreeView.isChecked(title, "SysTreeView321", "节点2|2A"));

				Assert.assertFalse(TreeView.isChecked(title, "SysTreeView321",
						"#1|#1"));
				Assert.assertEquals(checkable,
						TreeView.check(title, "SysTreeView321", "#1|#1"));
				Assert.assertEquals(checkable,
						TreeView.isChecked(title, "SysTreeView321", "#1|#1"));

				Assert.assertFalse(TreeView.isChecked(title, "SysTreeView321",
						"#2"));
				Assert.assertEquals(checkable,
						TreeView.check(title, "SysTreeView321", "#2"));
				Assert.assertEquals(checkable,
						TreeView.isChecked(title, "SysTreeView321", "#2"));

				Assert.assertFalse(TreeView.check(title, "SysTreeView321",
						"节点5"));
			} finally {
				destroyDefaultDisplay(title);
			}
		}
	}

	@Test
	public void collapse() {
		String title = "TreeView.collapse - " + System.currentTimeMillis();
		Assert.assertFalse(TreeView.collapse(title, "SysTreeView321", "节点2"));

		try {
			// show frame
			showTreeViewShell(title, true);

			Assert.assertFalse(TreeView.collapse(title, "SysTreeView321", null));
			Assert.assertFalse(TreeView.collapse(title, "SysTreeView321", ""));
			Assert.assertFalse(TreeView.collapse(title, "SysTreeView321", " "));
			Assert.assertFalse(TreeView
					.collapse(title, "SysTreeView321", "节点0"));
			Assert.assertFalse(TreeView
					.collapse(title, "SysTreeView321", "节点1"));

			Assert.assertTrue(TreeView.collapse(title, "SysTreeView321", "节点2"));
			Assert.assertTrue(TreeView.expand(title, "SysTreeView321", "节点2"));
			Assert.assertTrue(TreeView.collapse(title, "SysTreeView321", "节点2"));
			Assert.assertTrue(TreeView.collapse(title, "SysTreeView321", "节点2"));

			Assert.assertTrue(TreeView.collapse(title, "SysTreeView321",
					"节点2|2A"));
			Assert.assertTrue(TreeView
					.expand(title, "SysTreeView321", "节点2|2A"));
			Assert.assertTrue(TreeView.collapse(title, "SysTreeView321",
					"节点2|2A"));

			Assert.assertTrue(TreeView.collapse(title, "SysTreeView321",
					"#1|2A"));
			Assert.assertTrue(TreeView.expand(title, "SysTreeView321", "#1|2A"));
			Assert.assertTrue(TreeView.collapse(title, "SysTreeView321",
					"#1|2A"));
		} finally {
			destroyDefaultDisplay(title);
		}
	}

	@Test
	public void exists() {
		String title = "TreeView.exists - " + System.currentTimeMillis();
		Assert.assertFalse(TreeView.exists(title, "SysTreeView321", "节点2"));

		try {
			// show frame
			showTreeViewShell(title, true);

			Assert.assertTrue(TreeView.exists(title, "SysTreeView321", null));
			Assert.assertTrue(TreeView.exists(title, "SysTreeView321", ""));
			Assert.assertFalse(TreeView.exists(title, "SysTreeView321", " "));
			Assert.assertFalse(TreeView.exists(title, "SysTreeView321", "节点0"));
			Assert.assertTrue(TreeView.exists(title, "SysTreeView321", "节点1"));
			Assert.assertTrue(TreeView.exists(title, "SysTreeView321", "节点2"));
			Assert.assertTrue(TreeView
					.exists(title, "SysTreeView321", "节点2|2A"));
			Assert.assertFalse(TreeView.exists(title, "SysTreeView321", "节点3"));
			Assert.assertFalse(TreeView.exists(title, "SysTreeView321", "节|点3"));
			Assert.assertFalse(TreeView.exists(title, "SysTreeView321",
					"节\\|点3"));
			Assert.assertFalse(TreeView
					.exists(title, "SysTreeView321", "节`|点3"));
			Assert.assertTrue(TreeView.exists(title, "SysTreeView321", "节点4"));
			Assert.assertFalse(TreeView.exists(title, "SysTreeView321", "节点5"));
			Assert.assertTrue(TreeView.exists(title, "SysTreeView321", "#0"));
			Assert.assertTrue(TreeView.exists(title, "SysTreeView321", "#1"));
			Assert.assertTrue(TreeView.exists(title, "SysTreeView321", "#1|#0"));
			Assert.assertTrue(TreeView.exists(title, "SysTreeView321", "#1|#1"));
			Assert.assertFalse(TreeView
					.exists(title, "SysTreeView321", "#1|#2"));
			Assert.assertTrue(TreeView.exists(title, "SysTreeView321", "#2"));
			Assert.assertTrue(TreeView.exists(title, "SysTreeView321", "#3"));
			Assert.assertFalse(TreeView.exists(title, "SysTreeView321", "#4"));
		} finally {
			destroyDefaultDisplay(title);
		}
	}

	@Test
	public void expand() {
		String title = "TreeView.expand - " + System.currentTimeMillis();
		Assert.assertFalse(TreeView.expand(title, "SysTreeView321", "节点2"));

		try {
			// show frame
			showTreeViewShell(title, true);

			Assert.assertFalse(TreeView.expand(title, "SysTreeView321", null));
			Assert.assertFalse(TreeView.expand(title, "SysTreeView321", ""));
			Assert.assertFalse(TreeView.expand(title, "SysTreeView321", " "));
			Assert.assertFalse(TreeView.expand(title, "SysTreeView321", "节点0"));
			Assert.assertFalse(TreeView.expand(title, "SysTreeView321", "节点1"));

			Assert.assertTrue(TreeView.expand(title, "SysTreeView321", "节点2"));
			Assert.assertTrue(TreeView.expand(title, "SysTreeView321", "节点2"));
			Assert.assertTrue(TreeView.collapse(title, "SysTreeView321", "节点2"));
			Assert.assertTrue(TreeView.expand(title, "SysTreeView321", "节点2"));
			Assert.assertTrue(TreeView.collapse(title, "SysTreeView321", "节点2"));

			Assert.assertTrue(TreeView
					.expand(title, "SysTreeView321", "节点2|2A"));
			Assert.assertTrue(TreeView
					.expand(title, "SysTreeView321", "节点2|2A"));
			Assert.assertTrue(TreeView.collapse(title, "SysTreeView321",
					"节点2|2A"));
			Assert.assertTrue(TreeView
					.expand(title, "SysTreeView321", "节点2|2A"));
			Assert.assertTrue(TreeView
					.expand(title, "SysTreeView321", "节点2|2A"));
			Assert.assertTrue(TreeView.collapse(title, "SysTreeView321",
					"节点2|2A"));

			Assert.assertTrue(TreeView
					.expand(title, "SysTreeView321", "节点2|#0"));
			Assert.assertTrue(TreeView
					.expand(title, "SysTreeView321", "节点2|#0"));
			Assert.assertTrue(TreeView.collapse(title, "SysTreeView321",
					"节点2|#0"));
			Assert.assertTrue(TreeView
					.expand(title, "SysTreeView321", "节点2|#0"));
			Assert.assertTrue(TreeView.collapse(title, "SysTreeView321",
					"节点2|#0"));
		} finally {
			destroyDefaultDisplay(title);
		}
	}

	@Test
	public void getItemCount() {
		String title = "TreeView.getItemCount - " + System.currentTimeMillis();
		Assert.assertNull(TreeView.getItemCount(title, "SysTreeView321", "节点2"));

		try {
			// show frame
			showTreeViewShell(title, true);

			assertEquals(4,
					TreeView.getItemCount(title, "SysTreeView321", null));
			assertEquals(4, TreeView.getItemCount(title, "SysTreeView321", ""));
			Assert.assertNull(TreeView.getItemCount(title, "SysTreeView321",
					" "));
			Assert.assertNull(TreeView.getItemCount(title, "SysTreeView321",
					"节点0"));
			assertEquals(0,
					TreeView.getItemCount(title, "SysTreeView321", "节点1"));
			assertEquals(2,
					TreeView.getItemCount(title, "SysTreeView321", "节点2"));
			assertEquals(3,
					TreeView.getItemCount(title, "SysTreeView321", "节点2|2A"));
			Assert.assertNull(TreeView.getItemCount(title, "SysTreeView321",
					"节点3"));
			Assert.assertNull(TreeView.getItemCount(title, "SysTreeView321",
					"节|点3"));
			Assert.assertNull(TreeView.getItemCount(title, "SysTreeView321",
					"节\\|点3"));
			Assert.assertNull(TreeView.getItemCount(title, "SysTreeView321",
					"节`|点3"));
			assertEquals(0,
					TreeView.getItemCount(title, "SysTreeView321", "节点4"));
			Assert.assertNull(TreeView.getItemCount(title, "SysTreeView321",
					"节点5"));
			assertEquals(0,
					TreeView.getItemCount(title, "SysTreeView321", "#0"));
			assertEquals(2,
					TreeView.getItemCount(title, "SysTreeView321", "#1"));
			assertEquals(3,
					TreeView.getItemCount(title, "SysTreeView321", "#1|#0"));
			assertEquals(0,
					TreeView.getItemCount(title, "SysTreeView321", "#1|#1"));
			Assert.assertNull(TreeView.getItemCount(title, "SysTreeView321",
					"#1|#2"));
			assertEquals(2,
					TreeView.getItemCount(title, "SysTreeView321", "#2"));
			assertEquals(0,
					TreeView.getItemCount(title, "SysTreeView321", "#3"));
			Assert.assertNull(TreeView.getItemCount(title, "SysTreeView321",
					"#4"));
		} finally {
			destroyDefaultDisplay(title);
		}
	}

	@Test
	public void getSelected() {
		String title = "TreeView.getSelected - " + System.currentTimeMillis();
		Assert.assertNull(TreeView.getSelected(title, "SysTreeView321"));

		try {
			// show frame
			showTreeViewShell(title, true);

			Assert.assertEquals("节点1",
					TreeView.getSelected(title, "SysTreeView321"));

			Assert.assertTrue(TreeView.select(title, "SysTreeView321", "#1"));
			Assert.assertEquals("节点2",
					TreeView.getSelected(title, "SysTreeView321"));
			Assert.assertEquals("节点2",
					TreeView.getSelected(title, "SysTreeView321", false));
			Assert.assertEquals("#1",
					TreeView.getSelected(title, "SysTreeView321", true));

			Assert.assertFalse(TreeView.select(title, "SysTreeView321", null));
			Assert.assertEquals("节点2",
					TreeView.getSelected(title, "SysTreeView321"));
			Assert.assertFalse(TreeView.select(title, "SysTreeView321", ""));
			Assert.assertEquals("节点2",
					TreeView.getSelected(title, "SysTreeView321"));
			Assert.assertFalse(TreeView.select(title, "SysTreeView321", " "));
			Assert.assertEquals("节点2",
					TreeView.getSelected(title, "SysTreeView321"));

			Assert.assertTrue(TreeView.select(title, "SysTreeView321", "节点1"));
			Assert.assertEquals("节点1",
					TreeView.getSelected(title, "SysTreeView321"));
			Assert.assertEquals("节点1",
					TreeView.getSelected(title, "SysTreeView321", false));
			Assert.assertEquals("#0",
					TreeView.getSelected(title, "SysTreeView321", true));

			Assert.assertTrue(TreeView.select(title, "SysTreeView321", "#1"));
			Assert.assertEquals("节点2",
					TreeView.getSelected(title, "SysTreeView321"));
			Assert.assertEquals("节点2",
					TreeView.getSelected(title, "SysTreeView321", false));
			Assert.assertEquals("#1",
					TreeView.getSelected(title, "SysTreeView321", true));

			Assert.assertTrue(TreeView.select(title, "SysTreeView321", "#1|#0"));
			Assert.assertEquals("节点2|2A",
					TreeView.getSelected(title, "SysTreeView321"));
			Assert.assertEquals("节点2|2A",
					TreeView.getSelected(title, "SysTreeView321", false));
			Assert.assertEquals("#1|#0",
					TreeView.getSelected(title, "SysTreeView321", true));
			Assert.assertTrue(TreeView.select(title, "SysTreeView321", "#1|#1"));
			Assert.assertEquals("节点2|2B",
					TreeView.getSelected(title, "SysTreeView321"));
			Assert.assertEquals("节点2|2B",
					TreeView.getSelected(title, "SysTreeView321", false));
			Assert.assertEquals("#1|#1",
					TreeView.getSelected(title, "SysTreeView321", true));
			Assert.assertFalse(TreeView
					.select(title, "SysTreeView321", "#1|#2"));
			Assert.assertEquals("节点2|2B",
					TreeView.getSelected(title, "SysTreeView321"));
			Assert.assertEquals("节点2|2B",
					TreeView.getSelected(title, "SysTreeView321", false));
			Assert.assertEquals("#1|#1",
					TreeView.getSelected(title, "SysTreeView321", true));
		} finally {
			destroyDefaultDisplay(title);
		}
	}

	@Test
	public void getSelectedText() {
		String title = "TreeView.getSelectedText - "
				+ System.currentTimeMillis();
		Assert.assertNull(TreeView.getSelectedText(title, "SysTreeView321"));

		try {
			// show frame
			showTreeViewShell(title, true);

			Assert.assertEquals("节点1",
					TreeView.getSelected(title, "SysTreeView321"));

			Assert.assertTrue(TreeView.select(title, "SysTreeView321", "#1"));
			Assert.assertEquals("节点2",
					TreeView.getSelectedText(title, "SysTreeView321"));

			Assert.assertFalse(TreeView.select(title, "SysTreeView321", null));
			Assert.assertEquals("节点2",
					TreeView.getSelectedText(title, "SysTreeView321"));
			Assert.assertFalse(TreeView.select(title, "SysTreeView321", ""));
			Assert.assertEquals("节点2",
					TreeView.getSelectedText(title, "SysTreeView321"));
			Assert.assertFalse(TreeView.select(title, "SysTreeView321", " "));
			Assert.assertEquals("节点2",
					TreeView.getSelectedText(title, "SysTreeView321"));

			Assert.assertTrue(TreeView.select(title, "SysTreeView321", "节点1"));
			Assert.assertEquals("节点1",
					TreeView.getSelectedText(title, "SysTreeView321"));

			Assert.assertTrue(TreeView.select(title, "SysTreeView321", "#1"));
			Assert.assertEquals("节点2",
					TreeView.getSelectedText(title, "SysTreeView321"));

			Assert.assertTrue(TreeView.select(title, "SysTreeView321", "#2"));
			Assert.assertEquals("节|点3",
					TreeView.getSelectedText(title, "SysTreeView321"));

			Assert.assertTrue(TreeView.select(title, "SysTreeView321", "#1|#0"));
			Assert.assertEquals("2A",
					TreeView.getSelectedText(title, "SysTreeView321"));
			Assert.assertTrue(TreeView.select(title, "SysTreeView321", "#1|#1"));
			Assert.assertEquals("2B",
					TreeView.getSelectedText(title, "SysTreeView321"));
			Assert.assertFalse(TreeView
					.select(title, "SysTreeView321", "#1|#2"));
			Assert.assertEquals("2B",
					TreeView.getSelectedText(title, "SysTreeView321"));
		} finally {
			destroyDefaultDisplay(title);
		}
	}

	@Test
	public void getText() {
		String title = "TreeView.getText - " + System.currentTimeMillis();
		Assert.assertNull(TreeView.getText(title, "SysTreeView321", "节点2"));

		try {
			// show frame
			showTreeViewShell(title, true);

			Assert.assertNull(TreeView.getText(title, "SysTreeView321", null));
			Assert.assertNull(TreeView.getText(title, "SysTreeView321", ""));
			Assert.assertNull(TreeView.getText(title, "SysTreeView321", " "));
			Assert.assertNull(TreeView.getText(title, "SysTreeView321", "节点0"));
			Assert.assertEquals("节点1",
					TreeView.getText(title, "SysTreeView321", "节点1"));
			Assert.assertEquals("节点2",
					TreeView.getText(title, "SysTreeView321", "节点2"));
			Assert.assertEquals("2A",
					TreeView.getText(title, "SysTreeView321", "节点2|2A"));
			Assert.assertNull(TreeView.getText(title, "SysTreeView321", "节点3"));
			Assert.assertNull(TreeView.getText(title, "SysTreeView321", "节|点3"));
			Assert.assertNull(TreeView.getText(title, "SysTreeView321",
					"节\\|点3"));
			Assert.assertNull(TreeView
					.getText(title, "SysTreeView321", "节`|点3"));
			Assert.assertEquals("节点4",
					TreeView.getText(title, "SysTreeView321", "节点4"));
			Assert.assertNull(TreeView.getText(title, "SysTreeView321", "节点5"));
			Assert.assertEquals("节点1",
					TreeView.getText(title, "SysTreeView321", "#0"));
			Assert.assertEquals("节点2",
					TreeView.getText(title, "SysTreeView321", "#1"));
			Assert.assertEquals("2A",
					TreeView.getText(title, "SysTreeView321", "#1|#0"));
			Assert.assertEquals("2B",
					TreeView.getText(title, "SysTreeView321", "#1|#1"));
			Assert.assertNull(TreeView
					.getText(title, "SysTreeView321", "#1|#2"));
			Assert.assertEquals("节|点3",
					TreeView.getText(title, "SysTreeView321", "#2"));
			Assert.assertEquals("节点4",
					TreeView.getText(title, "SysTreeView321", "#3"));
			Assert.assertNull(TreeView.getText(title, "SysTreeView321", "#4"));
		} finally {
			destroyDefaultDisplay(title);
		}
	}

	@Test
	public void isChecked() {
		String title = "TreeView.isChecked - " + System.currentTimeMillis();
		Assert.assertFalse(TreeView.isChecked(title, "SysTreeView321", "节点2"));
		Assert.assertEquals(IsChecked.NOT_A_CHECKBOX,
				TreeView.isChecked_(title, "SysTreeView321", "节点2"));

		try {
			// show frame
			showTreeViewShell(title, true);

			Assert.assertFalse(TreeView
					.isChecked(title, "SysTreeView321", null));
			Assert.assertEquals(IsChecked.NOT_A_CHECKBOX,
					TreeView.isChecked_(title, "SysTreeView321", null));

			Assert.assertFalse(TreeView.isChecked(title, "SysTreeView321", ""));
			Assert.assertEquals(IsChecked.NOT_A_CHECKBOX,
					TreeView.isChecked_(title, "SysTreeView321", ""));

			Assert.assertFalse(TreeView.isChecked(title, "SysTreeView321", " "));
			Assert.assertEquals(IsChecked.NOT_A_CHECKBOX,
					TreeView.isChecked_(title, "SysTreeView321", " "));

			Assert.assertFalse(TreeView.isChecked(title, "SysTreeView321",
					"节点0"));
			Assert.assertEquals(IsChecked.NOT_A_CHECKBOX,
					TreeView.isChecked_(title, "SysTreeView321", "节点0"));

			Assert.assertFalse(TreeView.isChecked(title, "SysTreeView321",
					"节点1"));
			Assert.assertEquals(IsChecked.UNCHECKED,
					TreeView.isChecked_(title, "SysTreeView321", "节点1"));
			Assert.assertTrue(TreeView.check(title, "SysTreeView321", "节点1"));
			Assert.assertTrue(TreeView
					.isChecked(title, "SysTreeView321", "节点1"));
			Assert.assertEquals(IsChecked.CHECKED,
					TreeView.isChecked_(title, "SysTreeView321", "节点1"));

			Assert.assertFalse(TreeView.isChecked(title, "SysTreeView321",
					"节点2"));
			Assert.assertTrue(TreeView.check(title, "SysTreeView321", "节点2"));
			Assert.assertTrue(TreeView
					.isChecked(title, "SysTreeView321", "节点2"));
			Assert.assertEquals(IsChecked.CHECKED,
					TreeView.isChecked_(title, "SysTreeView321", "节点2"));

			Assert.assertFalse(TreeView.isChecked(title, "SysTreeView321",
					"节点2|2A|2A1"));
			Assert.assertTrue(TreeView.check(title, "SysTreeView321",
					"节点2|2A|2A1"));
			Assert.assertTrue(TreeView.isChecked(title, "SysTreeView321",
					"节点2|2A|2A1"));
			Assert.assertEquals(IsChecked.CHECKED,
					TreeView.isChecked_(title, "SysTreeView321", "节点2|2A|2A1"));

			Assert.assertFalse(TreeView.isChecked(title, "SysTreeView321",
					"节点2|2A"));
			Assert.assertTrue(TreeView.check(title, "SysTreeView321", "节点2|2A"));
			Assert.assertTrue(TreeView.isChecked(title, "SysTreeView321",
					"节点2|2A"));
			Assert.assertEquals(IsChecked.CHECKED,
					TreeView.isChecked_(title, "SysTreeView321", "节点2|2A"));

			Assert.assertFalse(TreeView.isChecked(title, "SysTreeView321",
					"#1|#1"));
			Assert.assertTrue(TreeView.check(title, "SysTreeView321", "#1|#1"));
			Assert.assertTrue(TreeView.isChecked(title, "SysTreeView321",
					"#1|#1"));
			Assert.assertEquals(IsChecked.CHECKED,
					TreeView.isChecked_(title, "SysTreeView321", "#1|#1"));

			Assert.assertFalse(TreeView
					.isChecked(title, "SysTreeView321", "#2"));
			Assert.assertTrue(TreeView.check(title, "SysTreeView321", "#2"));
			Assert.assertTrue(TreeView.isChecked(title, "SysTreeView321", "#2"));
			Assert.assertEquals(IsChecked.CHECKED,
					TreeView.isChecked_(title, "SysTreeView321", "#2"));

			Assert.assertFalse(TreeView.isChecked(title, "SysTreeView321",
					"节点5"));
			Assert.assertEquals(IsChecked.NOT_A_CHECKBOX,
					TreeView.isChecked_(title, "SysTreeView321", "节点5"));
		} finally {
			destroyDefaultDisplay(title);
		}
	}

	@Test
	public void isSelected() {
		String title = "TreeView.isSelected - " + System.currentTimeMillis();
		Assert.assertFalse(TreeView.isSelected(title, "SysTreeView321", "节点2"));

		try {
			// show frame
			showTreeViewShell(title, true);

			Assert.assertTrue(TreeView.isSelected(title, "SysTreeView321",
					"节点1"));
			Assert.assertTrue(TreeView
					.isSelected(title, "SysTreeView321", "#0"));

			Assert.assertTrue(TreeView.select(title, "SysTreeView321", "#1"));
			Assert.assertTrue(TreeView.isSelected(title, "SysTreeView321",
					"节点2"));
			Assert.assertTrue(TreeView
					.isSelected(title, "SysTreeView321", "#1"));

			Assert.assertFalse(TreeView.select(title, "SysTreeView321", null));
			Assert.assertTrue(TreeView.isSelected(title, "SysTreeView321",
					"节点2"));
			Assert.assertFalse(TreeView.select(title, "SysTreeView321", ""));
			Assert.assertTrue(TreeView.isSelected(title, "SysTreeView321",
					"节点2"));
			Assert.assertFalse(TreeView.select(title, "SysTreeView321", " "));
			Assert.assertTrue(TreeView.isSelected(title, "SysTreeView321",
					"节点2"));

			Assert.assertTrue(TreeView.select(title, "SysTreeView321", "节点1"));
			Assert.assertFalse(TreeView.isSelected(title, "SysTreeView321",
					"节点2"));
			Assert.assertFalse(TreeView.isSelected(title, "SysTreeView321",
					"#1"));
			Assert.assertTrue(TreeView.isSelected(title, "SysTreeView321",
					"节点1"));
			Assert.assertTrue(TreeView
					.isSelected(title, "SysTreeView321", "#0"));

			Assert.assertTrue(TreeView.select(title, "SysTreeView321", "#1"));
			Assert.assertFalse(TreeView.isSelected(title, "SysTreeView321",
					"节点1"));
			Assert.assertFalse(TreeView.isSelected(title, "SysTreeView321",
					"#0"));
			Assert.assertTrue(TreeView.isSelected(title, "SysTreeView321",
					"节点2"));
			Assert.assertTrue(TreeView
					.isSelected(title, "SysTreeView321", "#1"));

			Assert.assertTrue(TreeView.select(title, "SysTreeView321", "#1|#0"));
			Assert.assertFalse(TreeView.isSelected(title, "SysTreeView321",
					"节点2"));
			Assert.assertFalse(TreeView.isSelected(title, "SysTreeView321",
					"#1"));
			Assert.assertTrue(TreeView.isSelected(title, "SysTreeView321",
					"节点2|2A"));
			Assert.assertTrue(TreeView.isSelected(title, "SysTreeView321",
					"#1|#0"));

			Assert.assertTrue(TreeView.select(title, "SysTreeView321", "#1|#1"));
			Assert.assertFalse(TreeView.isSelected(title, "SysTreeView321",
					"节点2|2A"));
			Assert.assertFalse(TreeView.isSelected(title, "SysTreeView321",
					"#1|#0"));
			Assert.assertTrue(TreeView.isSelected(title, "SysTreeView321",
					"节点2|#1"));
			Assert.assertTrue(TreeView.isSelected(title, "SysTreeView321",
					"#1|2B"));

			Assert.assertFalse(TreeView
					.select(title, "SysTreeView321", "#1|#2"));
			Assert.assertTrue(TreeView.isSelected(title, "SysTreeView321",
					"节点2|#1"));
			Assert.assertTrue(TreeView.isSelected(title, "SysTreeView321",
					"#1|2B"));
		} finally {
			destroyDefaultDisplay(title);
		}
	}

	@Test
	public void isLeaf() {
		String title = "TreeView.isLeaf - " + System.currentTimeMillis();
		Assert.assertFalse(TreeView.isLeaf(title, "SysTreeView321", "节点2"));

		try {
			// show frame
			showTreeViewShell(title, true);

			Assert.assertFalse(TreeView.isLeaf(title, "SysTreeView321", null));
			Assert.assertFalse(TreeView.isLeaf(title, "SysTreeView321", ""));
			Assert.assertFalse(TreeView.isLeaf(title, "SysTreeView321", " "));
			Assert.assertFalse(TreeView.isLeaf(title, "SysTreeView321", "节点0"));
			Assert.assertTrue(TreeView.isLeaf(title, "SysTreeView321", "节点1"));
			Assert.assertFalse(TreeView.isLeaf(title, "SysTreeView321", "节点2"));
			Assert.assertFalse(TreeView.isLeaf(title, "SysTreeView321",
					"节点2|2A"));
			Assert.assertFalse(TreeView.isLeaf(title, "SysTreeView321", "节点3"));
			Assert.assertFalse(TreeView.isLeaf(title, "SysTreeView321", "节|点3"));
			Assert.assertFalse(TreeView.isLeaf(title, "SysTreeView321",
					"节\\|点3"));
			Assert.assertFalse(TreeView
					.isLeaf(title, "SysTreeView321", "节`|点3"));
			Assert.assertTrue(TreeView.isLeaf(title, "SysTreeView321", "节点4"));
			Assert.assertFalse(TreeView.isLeaf(title, "SysTreeView321", "节点5"));
			Assert.assertTrue(TreeView.isLeaf(title, "SysTreeView321", "#0"));
			Assert.assertFalse(TreeView.isLeaf(title, "SysTreeView321", "#1"));
			Assert.assertFalse(TreeView
					.isLeaf(title, "SysTreeView321", "#1|#0"));
			Assert.assertTrue(TreeView.isLeaf(title, "SysTreeView321", "#1|#1"));
			Assert.assertFalse(TreeView
					.isLeaf(title, "SysTreeView321", "#1|#2"));
			Assert.assertFalse(TreeView.isLeaf(title, "SysTreeView321", "#2"));
			Assert.assertTrue(TreeView.isLeaf(title, "SysTreeView321", "#3"));
			Assert.assertFalse(TreeView.isLeaf(title, "SysTreeView321", "#4"));
		} finally {
			destroyDefaultDisplay(title);
		}
	}

	@Test
	public void select() {
		String title = "TreeView.select - " + System.currentTimeMillis();
		Assert.assertFalse(TreeView.select(title, "SysTreeView321", "节点2"));

		try {
			// show frame
			showTreeViewShell(title, true);

			Assert.assertEquals("节点1",
					TreeView.getSelected(title, "SysTreeView321"));

			Assert.assertTrue(TreeView.select(title, "SysTreeView321", "#1"));
			Assert.assertTrue(TreeView.select(title, "SysTreeView321", "#1"));
			Assert.assertEquals("节点2",
					TreeView.getSelected(title, "SysTreeView321"));

			Assert.assertFalse(TreeView.select(title, "SysTreeView321", null));
			Assert.assertEquals("节点2",
					TreeView.getSelected(title, "SysTreeView321"));
			Assert.assertFalse(TreeView.select(title, "SysTreeView321", ""));
			Assert.assertEquals("节点2",
					TreeView.getSelected(title, "SysTreeView321"));
			Assert.assertFalse(TreeView.select(title, "SysTreeView321", " "));
			Assert.assertEquals("节点2",
					TreeView.getSelected(title, "SysTreeView321"));

			Assert.assertTrue(TreeView.select(title, "SysTreeView321", "节点1"));
			Assert.assertEquals("节点1",
					TreeView.getSelected(title, "SysTreeView321"));

			Assert.assertTrue(TreeView.select(title, "SysTreeView321", "#1"));
			Assert.assertEquals("节点2",
					TreeView.getSelected(title, "SysTreeView321"));

			Assert.assertTrue(TreeView.select(title, "SysTreeView321", "#1|#0"));
			Assert.assertEquals("节点2|2A",
					TreeView.getSelected(title, "SysTreeView321"));
			Assert.assertTrue(TreeView.select(title, "SysTreeView321", "#1|#1"));
			Assert.assertEquals("节点2|2B",
					TreeView.getSelected(title, "SysTreeView321"));
			Assert.assertFalse(TreeView
					.select(title, "SysTreeView321", "#1|#2"));
			Assert.assertEquals("节点2|2B",
					TreeView.getSelected(title, "SysTreeView321"));
		} finally {
			destroyDefaultDisplay(title);
		}
	}

	@Test
	public void uncheck() {
		String title = "TreeView.uncheck - " + System.currentTimeMillis();
		Assert.assertFalse(TreeView.uncheck(title, "SysTreeView321", "节点2"));

		for (int i = 0; i < 2; i++) {
			boolean checkable = (i == 0);
			try {
				// show frame
				showTreeViewShell(title, checkable);

				Assert.assertFalse(TreeView.uncheck(title, "SysTreeView321",
						null));
				Assert.assertFalse(TreeView
						.uncheck(title, "SysTreeView321", ""));
				Assert.assertFalse(TreeView.uncheck(title, "SysTreeView321",
						" "));
				Assert.assertFalse(TreeView.uncheck(title, "SysTreeView321",
						"节点0"));
				Assert.assertFalse(TreeView.isChecked(title, "SysTreeView321",
						"节点1"));
				Assert.assertEquals(checkable,
						TreeView.check(title, "SysTreeView321", "节点1"));
				Assert.assertEquals(checkable,
						TreeView.isChecked(title, "SysTreeView321", "节点1"));
				Assert.assertEquals(checkable,
						TreeView.uncheck(title, "SysTreeView321", "节点1"));
				Assert.assertEquals(checkable,
						TreeView.uncheck(title, "SysTreeView321", "节点1"));
				Assert.assertFalse(TreeView.isChecked(title, "SysTreeView321",
						"节点1"));

				Assert.assertFalse(TreeView.isChecked(title, "SysTreeView321",
						"节点2"));
				Assert.assertEquals(checkable,
						TreeView.check(title, "SysTreeView321", "节点2"));
				Assert.assertEquals(checkable,
						TreeView.isChecked(title, "SysTreeView321", "节点2"));
				Assert.assertEquals(checkable,
						TreeView.uncheck(title, "SysTreeView321", "节点2"));
				Assert.assertFalse(TreeView.isChecked(title, "SysTreeView321",
						"节点2"));

				Assert.assertFalse(TreeView.isChecked(title, "SysTreeView321",
						"节点2|2A|2A1"));
				Assert.assertEquals(checkable,
						TreeView.check(title, "SysTreeView321", "节点2|2A|2A1"));
				Assert.assertEquals(checkable, TreeView.isChecked(title,
						"SysTreeView321", "节点2|2A|2A1"));
				Assert.assertEquals(checkable,
						TreeView.uncheck(title, "SysTreeView321", "节点2|2A|2A1"));
				Assert.assertFalse(TreeView.isChecked(title, "SysTreeView321",
						"节点2|2A|2A1"));

				Assert.assertFalse(TreeView.isChecked(title, "SysTreeView321",
						"节点2|2A"));
				Assert.assertEquals(checkable,
						TreeView.check(title, "SysTreeView321", "节点2|2A"));
				Assert.assertEquals(checkable,
						TreeView.isChecked(title, "SysTreeView321", "节点2|2A"));
				Assert.assertEquals(checkable,
						TreeView.uncheck(title, "SysTreeView321", "节点2|2A"));
				Assert.assertFalse(TreeView.isChecked(title, "SysTreeView321",
						"节点2|2A"));

				Assert.assertFalse(TreeView.isChecked(title, "SysTreeView321",
						"#1|#1"));
				Assert.assertEquals(checkable,
						TreeView.check(title, "SysTreeView321", "#1|#1"));
				Assert.assertEquals(checkable,
						TreeView.isChecked(title, "SysTreeView321", "#1|#1"));
				Assert.assertEquals(checkable,
						TreeView.uncheck(title, "SysTreeView321", "#1|#1"));
				Assert.assertFalse(TreeView.isChecked(title, "SysTreeView321",
						"#1|#1"));

				Assert.assertFalse(TreeView.isChecked(title, "SysTreeView321",
						"#2"));
				Assert.assertEquals(checkable,
						TreeView.check(title, "SysTreeView321", "#2"));
				Assert.assertEquals(checkable,
						TreeView.isChecked(title, "SysTreeView321", "#2"));
				Assert.assertEquals(checkable,
						TreeView.uncheck(title, "SysTreeView321", "#2"));
				Assert.assertFalse(TreeView.isChecked(title, "SysTreeView321",
						"#2"));

				Assert.assertFalse(TreeView.uncheck(title, "SysTreeView321",
						"节点5"));
			} finally {
				destroyDefaultDisplay(title);
			}
		}
	}

	private void showTreeViewShell(final String title, final boolean checkable) {
		Thread thread = new Thread(new Runnable() {
			public void run() {
				Display display = new Display();
				Shell shell = new Shell(display);
				shell.setText(title);
				shell.setSize(250, 200);
				shell.setLayout(new FillLayout(SWT.HORIZONTAL));

				// Add tree to shell
				int style = SWT.SINGLE | SWT.BORDER;
				if (checkable) {
					style = style | SWT.CHECK;
				}
				final Tree tree = new Tree(shell, style);

				final TreeItem child1 = new TreeItem(tree, SWT.NONE, 0);
				child1.setText("节点1");

				final TreeItem child2 = new TreeItem(tree, SWT.NONE, 1);
				child2.setText("节点2");
				final TreeItem child2a = new TreeItem(child2, SWT.NONE, 0);
				child2a.setText("2A");
				final TreeItem child2a1 = new TreeItem(child2a, SWT.NONE, 0);
				child2a1.setText("2A1");
				final TreeItem child2a2 = new TreeItem(child2a, SWT.NONE, 1);
				child2a2.setText("2A2");
				final TreeItem child2a3 = new TreeItem(child2a, SWT.NONE, 2);
				child2a3.setText("2A3");
				final TreeItem child2b = new TreeItem(child2, SWT.NONE, 1);
				child2b.setText("2B");

				final TreeItem child3 = new TreeItem(tree, SWT.NONE, 2);
				child3.setText("节|点3");
				final TreeItem child3a = new TreeItem(child3, SWT.NONE, 0);
				child3a.setText("3A");
				final TreeItem child3b = new TreeItem(child3, SWT.NONE, 1);
				child3b.setText("3B");

				final TreeItem child4 = new TreeItem(tree, SWT.NONE, 3);
				child4.setText("节点4");

				// display shell
				shell.open();

				while (!shell.isDisposed()) {
					if (!display.readAndDispatch())
						display.sleep();
				}
				display.dispose();
			}
		});
		thread.start();
		Assert.assertTrue(Win.wait(title, 3));
	}
}
