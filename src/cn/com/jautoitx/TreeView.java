package cn.com.jautoitx;

import java.nio.CharBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import com.sun.jna.Memory;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.platform.win32.WinDef.HWND;
import com.sun.jna.platform.win32.WinDef.LPARAM;
import com.sun.jna.platform.win32.WinDef.UINT;
import com.sun.jna.platform.win32.WinDef.WPARAM;

public class TreeView extends AutoItX {
	/* Command used in method ControlTreeView */
	private static final String COMMAND_CHECK = "Check";
	private static final String COMMAND_COLLAPSE = "Collapse";
	private static final String COMMAND_EXISTS = "Exists";
	private static final String COMMAND_EXPAND = "Expand";
	private static final String COMMAND_GET_ITEM_COUNT = "GetItemCount";
	private static final String COMMAND_GET_SELECTED = "GetSelected";
	private static final String COMMAND_GET_TEXT = "GetText";
	private static final String COMMAND_IS_CHECKED = "IsChecked";
	private static final String COMMAND_SELECT = "Select";
	private static final String COMMAND_UNCHECK = "Uncheck";

	/* GetNext flags */
	private static final int TVGN_ROOT = 0x00000000;
	private static final int TVGN_NEXT = 0x00000001;
	// private static final int TVGN_PREVIOUS = 0x00000002;
	// private static final int TVGN_PARENT = 0x00000003;
	private static final int TVGN_CHILD = 0x00000004;

	/* Messages to send to TreeView */
	private static final int TV_FIRST = 0x1100;
	private static final int TVM_GETNEXTITEM = TV_FIRST + 10;
	private static final int TVM_GETITEMW = TV_FIRST + 62;

	/* item/itemex mask flags */
	private static final int TVIF_STATE = 0x00000008;

	/* item states */
	private static final int TVIS_EXPANDED = 0x00000020;

	/* Buffer size for method ControlTreeView */
	private static int BUF_SIZE = 8 * 1024;

	private TreeView() {
		// Do nothing
	}

	/**
	 * Checks an item (if the item supports it).
	 * 
	 * @param title
	 *            The title of the window to access.
	 * @param control
	 *            The control to interact with.
	 * @param item
	 *            The item which will be checked(if the item supports it).<br/>
	 *            The "item" parameter is a string-based parameter that is used
	 *            to reference a particular treeview item using a combination of
	 *            text and indices. Indices are 0-based. For example:<br/>
	 *            Heading1<br/>
	 *            ----> H1SubItem1<br/>
	 *            ----> H1SubItem2<br/>
	 *            ----> H1SubItem3<br/>
	 *            ----> ----> H1S1SubItem1<br/>
	 *            Heading2<br/>
	 *            Heading3<br/>
	 * 
	 *            Each "level" is separated by |. An index is preceded with #.
	 *            Examples:<br/>
	 * 
	 *            <table>
	 *            <thead>
	 *            <tr>
	 *            <td>Item</td>
	 *            <td>Item Reference</td>
	 *            </tr>
	 *            </thead>
	 *            <tr>
	 *            <td>Heading2</td>
	 *            <td>"Heading2" or "#1"</td>
	 *            </tr>
	 *            <tr>
	 *            <td>H1SubItem2</td>
	 *            <td>"Heading1|H1SubItem2" or "#0|#1"</td>
	 *            </tr>
	 *            <tr>
	 *            <td>H1S1SubItem1</td>
	 *            <td>"Heading1|H1SubItem3|H1S1SubItem1" or "#0|#2|#0"</td>
	 *            </tr>
	 *            </table>
	 * 
	 *            References can also be mixed like "Heading1|#1".
	 * @return Returns false if window/control could not be found, otherwise
	 *         returns false.
	 */
	public static boolean check(final String title, final String control,
			final String item) {
		return check(title, null, control, item);
	}

	/**
	 * Checks an item (if the item supports it).
	 * 
	 * @param title
	 *            The title of the window to access.
	 * @param text
	 *            The text of the window to access.
	 * @param control
	 *            The control to interact with.
	 * @param item
	 *            The item which will be checked(if the item supports it).<br/>
	 *            The "item" parameter is a string-based parameter that is used
	 *            to reference a particular treeview item using a combination of
	 *            text and indices. Indices are 0-based. For example:<br/>
	 *            Heading1<br/>
	 *            ----> H1SubItem1<br/>
	 *            ----> H1SubItem2<br/>
	 *            ----> H1SubItem3<br/>
	 *            ----> ----> H1S1SubItem1<br/>
	 *            Heading2<br/>
	 *            Heading3<br/>
	 * 
	 *            Each "level" is separated by |. An index is preceded with #.
	 *            Examples:<br/>
	 * 
	 *            <table>
	 *            <thead>
	 *            <tr>
	 *            <td>Item</td>
	 *            <td>Item Reference</td>
	 *            </tr>
	 *            </thead>
	 *            <tr>
	 *            <td>Heading2</td>
	 *            <td>"Heading2" or "#1"</td>
	 *            </tr>
	 *            <tr>
	 *            <td>H1SubItem2</td>
	 *            <td>"Heading1|H1SubItem2" or "#0|#1"</td>
	 *            </tr>
	 *            <tr>
	 *            <td>H1S1SubItem1</td>
	 *            <td>"Heading1|H1SubItem3|H1S1SubItem1" or "#0|#2|#0"</td>
	 *            </tr>
	 *            </table>
	 * 
	 *            References can also be mixed like "Heading1|#1".
	 * @return Returns false if window/control could not be found, otherwise
	 *         returns false.
	 */
	public static boolean check(final String title, final String text,
			final String control, final String item) {
		boolean status = true;

		if (StringUtils.isBlank(item)) {
			status = false;
		} else {
			IsChecked isChecked = TreeView.isChecked_(title, text, control,
					item);
			// if item is not checked, then check it
			if (isChecked == IsChecked.UNCHECKED) {
				// expand parent item before check item, if parent item is not
				// expanded, AutoItX will block in check item
				int index = 0;
				int index2 = item.indexOf("|", index);
				while (index2 > index) {
					if (!expand(title, text, control, item.substring(0, index2))) {
						status = false;
						break;
					}
					index = index2;
					index2 = item.indexOf("|", index + 1);
				}

				if (status) {
					controlTreeView(title, text, control, COMMAND_CHECK, item,
							null, 0);
					status = !hasError();
				}
			} else if (isChecked == IsChecked.NOT_A_CHECKBOX) {
				status = false;
			}
		}

		return status;
	}

	/**
	 * Checks an item (if the item supports it).
	 * 
	 * @param hWnd
	 *            The handle of the window to access.
	 * @param hCtrl
	 *            The handle of the control to interact with.
	 * @param item
	 *            The item which will be checked(if the item supports it).<br/>
	 *            The "item" parameter is a string-based parameter that is used
	 *            to reference a particular treeview item using a combination of
	 *            text and indices. Indices are 0-based. For example:<br/>
	 *            Heading1<br/>
	 *            ----> H1SubItem1<br/>
	 *            ----> H1SubItem2<br/>
	 *            ----> H1SubItem3<br/>
	 *            ----> ----> H1S1SubItem1<br/>
	 *            Heading2<br/>
	 *            Heading3<br/>
	 * 
	 *            Each "level" is separated by |. An index is preceded with #.
	 *            Examples:<br/>
	 * 
	 *            <table>
	 *            <thead>
	 *            <tr>
	 *            <td>Item</td>
	 *            <td>Item Reference</td>
	 *            </tr>
	 *            </thead>
	 *            <tr>
	 *            <td>Heading2</td>
	 *            <td>"Heading2" or "#1"</td>
	 *            </tr>
	 *            <tr>
	 *            <td>H1SubItem2</td>
	 *            <td>"Heading1|H1SubItem2" or "#0|#1"</td>
	 *            </tr>
	 *            <tr>
	 *            <td>H1S1SubItem1</td>
	 *            <td>"Heading1|H1SubItem3|H1S1SubItem1" or "#0|#2|#0"</td>
	 *            </tr>
	 *            </table>
	 * 
	 *            References can also be mixed like "Heading1|#1".
	 * @return Returns false if window/control could not be found, otherwise
	 *         returns false.
	 */
	public static boolean check(final HWND hWnd, final HWND hCtrl,
			final String item) {
		return ((hWnd == null) || (hCtrl == null)) ? false : check(
				buildTitle(hWnd), buildControlId(hCtrl), item);
	}

	/**
	 * Collapses an item to hide its children.
	 * 
	 * @param title
	 *            The title of the window to access.
	 * @param control
	 *            The control to interact with.
	 * @param item
	 *            The item which will be collapsed.<br/>
	 *            The item which will be checked(if the item supports it).<br/>
	 *            The "item" parameter is a string-based parameter that is used
	 *            to reference a particular treeview item using a combination of
	 *            text and indices. Indices are 0-based. For example:<br/>
	 *            Heading1<br/>
	 *            ----> H1SubItem1<br/>
	 *            ----> H1SubItem2<br/>
	 *            ----> H1SubItem3<br/>
	 *            ----> ----> H1S1SubItem1<br/>
	 *            Heading2<br/>
	 *            Heading3<br/>
	 * 
	 *            Each "level" is separated by |. An index is preceded with #.
	 *            Examples:<br/>
	 * 
	 *            <table>
	 *            <thead>
	 *            <tr>
	 *            <td>Item</td>
	 *            <td>Item Reference</td>
	 *            </tr>
	 *            </thead>
	 *            <tr>
	 *            <td>Heading2</td>
	 *            <td>"Heading2" or "#1"</td>
	 *            </tr>
	 *            <tr>
	 *            <td>H1SubItem2</td>
	 *            <td>"Heading1|H1SubItem2" or "#0|#1"</td>
	 *            </tr>
	 *            <tr>
	 *            <td>H1S1SubItem1</td>
	 *            <td>"Heading1|H1SubItem3|H1S1SubItem1" or "#0|#2|#0"</td>
	 *            </tr>
	 *            </table>
	 * 
	 *            References can also be mixed like "Heading1|#1".
	 * @return Returns false if window/control could not be found, otherwise
	 *         returns false.
	 */
	public static boolean collapse(final String title, final String control,
			final String item) {
		return collapse(title, null, control, item);
	}

	/**
	 * Collapses an item to hide its children.
	 * 
	 * @param title
	 *            The title of the window to access.
	 * @param text
	 *            The text of the window to access.
	 * @param control
	 *            The control to interact with.
	 * @param item
	 *            The item which will be collapsed.<br/>
	 *            The item which will be checked(if the item supports it).<br/>
	 *            The "item" parameter is a string-based parameter that is used
	 *            to reference a particular treeview item using a combination of
	 *            text and indices. Indices are 0-based. For example:<br/>
	 *            Heading1<br/>
	 *            ----> H1SubItem1<br/>
	 *            ----> H1SubItem2<br/>
	 *            ----> H1SubItem3<br/>
	 *            ----> ----> H1S1SubItem1<br/>
	 *            Heading2<br/>
	 *            Heading3<br/>
	 * 
	 *            Each "level" is separated by |. An index is preceded with #.
	 *            Examples:<br/>
	 * 
	 *            <table>
	 *            <thead>
	 *            <tr>
	 *            <td>Item</td>
	 *            <td>Item Reference</td>
	 *            </tr>
	 *            </thead>
	 *            <tr>
	 *            <td>Heading2</td>
	 *            <td>"Heading2" or "#1"</td>
	 *            </tr>
	 *            <tr>
	 *            <td>H1SubItem2</td>
	 *            <td>"Heading1|H1SubItem2" or "#0|#1"</td>
	 *            </tr>
	 *            <tr>
	 *            <td>H1S1SubItem1</td>
	 *            <td>"Heading1|H1SubItem3|H1S1SubItem1" or "#0|#2|#0"</td>
	 *            </tr>
	 *            </table>
	 * 
	 *            References can also be mixed like "Heading1|#1".
	 * @return Returns false if window/control could not be found, otherwise
	 *         returns false.
	 */
	public static boolean collapse(final String title, final String text,
			final String control, final String item) {
		boolean status = false;
		if (StringUtils.isNotEmpty(item)) {
			Integer itemCount = getItemCount(title, text, control, item);
			if ((itemCount != null) && (itemCount > 0)) {
				if (isExpanded(title, text, control, item)) {
					controlTreeView(title, text, control, COMMAND_COLLAPSE,
							item, null, 0);
					status = !hasError();
				} else {
					status = true;
				}
			}
		}
		return status;
	}

	/**
	 * Collapses an item to hide its children.
	 * 
	 * @param hWnd
	 *            The handle of the window to access.
	 * @param hCtrl
	 *            The handle of the control to interact with.
	 * @param item
	 *            The item which will be collapsed.<br/>
	 *            The item which will be checked(if the item supports it).<br/>
	 *            The "item" parameter is a string-based parameter that is used
	 *            to reference a particular treeview item using a combination of
	 *            text and indices. Indices are 0-based. For example:<br/>
	 *            Heading1<br/>
	 *            ----> H1SubItem1<br/>
	 *            ----> H1SubItem2<br/>
	 *            ----> H1SubItem3<br/>
	 *            ----> ----> H1S1SubItem1<br/>
	 *            Heading2<br/>
	 *            Heading3<br/>
	 * 
	 *            Each "level" is separated by |. An index is preceded with #.
	 *            Examples:<br/>
	 * 
	 *            <table>
	 *            <thead>
	 *            <tr>
	 *            <td>Item</td>
	 *            <td>Item Reference</td>
	 *            </tr>
	 *            </thead>
	 *            <tr>
	 *            <td>Heading2</td>
	 *            <td>"Heading2" or "#1"</td>
	 *            </tr>
	 *            <tr>
	 *            <td>H1SubItem2</td>
	 *            <td>"Heading1|H1SubItem2" or "#0|#1"</td>
	 *            </tr>
	 *            <tr>
	 *            <td>H1S1SubItem1</td>
	 *            <td>"Heading1|H1SubItem3|H1S1SubItem1" or "#0|#2|#0"</td>
	 *            </tr>
	 *            </table>
	 * 
	 *            References can also be mixed like "Heading1|#1".
	 * @return Returns false if window/control could not be found, otherwise
	 *         returns false.
	 */
	public static boolean collapse(final HWND hWnd, final HWND hCtrl,
			final String item) {
		return ((hWnd == null) || (hCtrl == null)) ? false : collapse(
				buildTitle(hWnd), buildControlId(hCtrl), item);
	}

	/**
	 * Checks to see if a specified item exists.
	 * 
	 * @param title
	 *            The title of the window to access.
	 * @param control
	 *            The control to interact with.
	 * @param item
	 *            The item which will be checked(if the item supports it).<br/>
	 *            The "item" parameter is a string-based parameter that is used
	 *            to reference a particular treeview item using a combination of
	 *            text and indices. Indices are 0-based. For example:<br/>
	 *            Heading1<br/>
	 *            ----> H1SubItem1<br/>
	 *            ----> H1SubItem2<br/>
	 *            ----> H1SubItem3<br/>
	 *            ----> ----> H1S1SubItem1<br/>
	 *            Heading2<br/>
	 *            Heading3<br/>
	 * 
	 *            Each "level" is separated by |. An index is preceded with #.
	 *            Examples:<br/>
	 * 
	 *            <table>
	 *            <thead>
	 *            <tr>
	 *            <td>Item</td>
	 *            <td>Item Reference</td>
	 *            </tr>
	 *            </thead>
	 *            <tr>
	 *            <td>Heading2</td>
	 *            <td>"Heading2" or "#1"</td>
	 *            </tr>
	 *            <tr>
	 *            <td>H1SubItem2</td>
	 *            <td>"Heading1|H1SubItem2" or "#0|#1"</td>
	 *            </tr>
	 *            <tr>
	 *            <td>H1S1SubItem1</td>
	 *            <td>"Heading1|H1SubItem3|H1S1SubItem1" or "#0|#2|#0"</td>
	 *            </tr>
	 *            </table>
	 * 
	 *            References can also be mixed like "Heading1|#1".
	 * @return Returns true if an item exists, otherwise returns false.
	 */
	public static boolean exists(final String title, final String control,
			final String item) {
		return exists(title, null, control, item);
	}

	/**
	 * Checks to see if a specified item exists.
	 * 
	 * @param title
	 *            The title of the window to access.
	 * @param text
	 *            The text of the window to access.
	 * @param control
	 *            The control to interact with.
	 * @param item
	 *            The item which will be checked(if the item supports it).<br/>
	 *            The "item" parameter is a string-based parameter that is used
	 *            to reference a particular treeview item using a combination of
	 *            text and indices. Indices are 0-based. For example:<br/>
	 *            Heading1<br/>
	 *            ----> H1SubItem1<br/>
	 *            ----> H1SubItem2<br/>
	 *            ----> H1SubItem3<br/>
	 *            ----> ----> H1S1SubItem1<br/>
	 *            Heading2<br/>
	 *            Heading3<br/>
	 * 
	 *            Each "level" is separated by |. An index is preceded with #.
	 *            Examples:<br/>
	 * 
	 *            <table>
	 *            <thead>
	 *            <tr>
	 *            <td>Item</td>
	 *            <td>Item Reference</td>
	 *            </tr>
	 *            </thead>
	 *            <tr>
	 *            <td>Heading2</td>
	 *            <td>"Heading2" or "#1"</td>
	 *            </tr>
	 *            <tr>
	 *            <td>H1SubItem2</td>
	 *            <td>"Heading1|H1SubItem2" or "#0|#1"</td>
	 *            </tr>
	 *            <tr>
	 *            <td>H1S1SubItem1</td>
	 *            <td>"Heading1|H1SubItem3|H1S1SubItem1" or "#0|#2|#0"</td>
	 *            </tr>
	 *            </table>
	 * 
	 *            References can also be mixed like "Heading1|#1".
	 * @return Returns true if an item exists, otherwise returns false.
	 */
	public static boolean exists(final String title, final String text,
			final String control, final String item) {
		return "1".equals(controlTreeView(title, text, control, COMMAND_EXISTS,
				item, null, BOOLEAN_BUF_SIZE));
	}

	/**
	 * Checks to see if a specified item exists.
	 * 
	 * @param hWnd
	 *            The handle of the window to access.
	 * @param hCtrl
	 *            The handle of the control to interact with.
	 * @param item
	 *            The item which will be checked(if the item supports it).<br/>
	 *            The "item" parameter is a string-based parameter that is used
	 *            to reference a particular treeview item using a combination of
	 *            text and indices. Indices are 0-based. For example:<br/>
	 *            Heading1<br/>
	 *            ----> H1SubItem1<br/>
	 *            ----> H1SubItem2<br/>
	 *            ----> H1SubItem3<br/>
	 *            ----> ----> H1S1SubItem1<br/>
	 *            Heading2<br/>
	 *            Heading3<br/>
	 * 
	 *            Each "level" is separated by |. An index is preceded with #.
	 *            Examples:<br/>
	 * 
	 *            <table>
	 *            <thead>
	 *            <tr>
	 *            <td>Item</td>
	 *            <td>Item Reference</td>
	 *            </tr>
	 *            </thead>
	 *            <tr>
	 *            <td>Heading2</td>
	 *            <td>"Heading2" or "#1"</td>
	 *            </tr>
	 *            <tr>
	 *            <td>H1SubItem2</td>
	 *            <td>"Heading1|H1SubItem2" or "#0|#1"</td>
	 *            </tr>
	 *            <tr>
	 *            <td>H1S1SubItem1</td>
	 *            <td>"Heading1|H1SubItem3|H1S1SubItem1" or "#0|#2|#0"</td>
	 *            </tr>
	 *            </table>
	 * 
	 *            References can also be mixed like "Heading1|#1".
	 * @return Returns true if an item exists, otherwise returns false.
	 */
	public static boolean exists(final HWND hWnd, final HWND hCtrl,
			final String item) {
		return ((hWnd == null) || (hCtrl == null)) ? false : exists(
				buildTitle(hWnd), buildControlId(hCtrl), item);
	}

	/**
	 * Expands an item to show its children.
	 * 
	 * @param title
	 *            The title of the window to access.
	 * @param control
	 *            The control to interact with.
	 * @param item
	 *            The item which will be expanded.<br/>
	 *            The item which will be checked(if the item supports it).<br/>
	 *            The "item" parameter is a string-based parameter that is used
	 *            to reference a particular treeview item using a combination of
	 *            text and indices. Indices are 0-based. For example:<br/>
	 *            Heading1<br/>
	 *            ----> H1SubItem1<br/>
	 *            ----> H1SubItem2<br/>
	 *            ----> H1SubItem3<br/>
	 *            ----> ----> H1S1SubItem1<br/>
	 *            Heading2<br/>
	 *            Heading3<br/>
	 * 
	 *            Each "level" is separated by |. An index is preceded with #.
	 *            Examples:<br/>
	 * 
	 *            <table>
	 *            <thead>
	 *            <tr>
	 *            <td>Item</td>
	 *            <td>Item Reference</td>
	 *            </tr>
	 *            </thead>
	 *            <tr>
	 *            <td>Heading2</td>
	 *            <td>"Heading2" or "#1"</td>
	 *            </tr>
	 *            <tr>
	 *            <td>H1SubItem2</td>
	 *            <td>"Heading1|H1SubItem2" or "#0|#1"</td>
	 *            </tr>
	 *            <tr>
	 *            <td>H1S1SubItem1</td>
	 *            <td>"Heading1|H1SubItem3|H1S1SubItem1" or "#0|#2|#0"</td>
	 *            </tr>
	 *            </table>
	 * 
	 *            References can also be mixed like "Heading1|#1".
	 * @return Returns false if window/control could not be found, otherwise
	 *         returns false.
	 */
	public static boolean expand(final String title, final String control,
			final String item) {
		return expand(title, null, control, item);
	}

	/**
	 * Expands an item to show its children.
	 * 
	 * @param title
	 *            The title of the window to access.
	 * @param text
	 *            The text of the window to access.
	 * @param control
	 *            The control to interact with.
	 * @param item
	 *            The item which will be expanded.<br/>
	 *            The item which will be checked(if the item supports it).<br/>
	 *            The "item" parameter is a string-based parameter that is used
	 *            to reference a particular treeview item using a combination of
	 *            text and indices. Indices are 0-based. For example:<br/>
	 *            Heading1<br/>
	 *            ----> H1SubItem1<br/>
	 *            ----> H1SubItem2<br/>
	 *            ----> H1SubItem3<br/>
	 *            ----> ----> H1S1SubItem1<br/>
	 *            Heading2<br/>
	 *            Heading3<br/>
	 * 
	 *            Each "level" is separated by |. An index is preceded with #.
	 *            Examples:<br/>
	 * 
	 *            <table>
	 *            <thead>
	 *            <tr>
	 *            <td>Item</td>
	 *            <td>Item Reference</td>
	 *            </tr>
	 *            </thead>
	 *            <tr>
	 *            <td>Heading2</td>
	 *            <td>"Heading2" or "#1"</td>
	 *            </tr>
	 *            <tr>
	 *            <td>H1SubItem2</td>
	 *            <td>"Heading1|H1SubItem2" or "#0|#1"</td>
	 *            </tr>
	 *            <tr>
	 *            <td>H1S1SubItem1</td>
	 *            <td>"Heading1|H1SubItem3|H1S1SubItem1" or "#0|#2|#0"</td>
	 *            </tr>
	 *            </table>
	 * 
	 *            References can also be mixed like "Heading1|#1".
	 * @return Returns false if window/control could not be found, otherwise
	 *         returns false.
	 */
	public static boolean expand(final String title, final String text,
			final String control, final String item) {
		boolean status = false;
		if (StringUtils.isNotEmpty(item)) {
			Integer itemCount = getItemCount(title, text, control, item);
			if ((itemCount != null) && (itemCount > 0)) {
				if (isCollapsed(title, text, control, item)) {
					controlTreeView(title, text, control, COMMAND_EXPAND, item,
							null, 0);
					status = !hasError();
				} else {
					status = true;
				}
			}
		}
		return status;
	}

	/**
	 * Expands an item to show its children.
	 * 
	 * @param hWnd
	 *            The handle of the window to access.
	 * @param hCtrl
	 *            The handle of the control to interact with.
	 * @param item
	 *            The item which will be expanded.<br/>
	 *            The item which will be checked(if the item supports it).<br/>
	 *            The "item" parameter is a string-based parameter that is used
	 *            to reference a particular treeview item using a combination of
	 *            text and indices. Indices are 0-based. For example:<br/>
	 *            Heading1<br/>
	 *            ----> H1SubItem1<br/>
	 *            ----> H1SubItem2<br/>
	 *            ----> H1SubItem3<br/>
	 *            ----> ----> H1S1SubItem1<br/>
	 *            Heading2<br/>
	 *            Heading3<br/>
	 * 
	 *            Each "level" is separated by |. An index is preceded with #.
	 *            Examples:<br/>
	 * 
	 *            <table>
	 *            <thead>
	 *            <tr>
	 *            <td>Item</td>
	 *            <td>Item Reference</td>
	 *            </tr>
	 *            </thead>
	 *            <tr>
	 *            <td>Heading2</td>
	 *            <td>"Heading2" or "#1"</td>
	 *            </tr>
	 *            <tr>
	 *            <td>H1SubItem2</td>
	 *            <td>"Heading1|H1SubItem2" or "#0|#1"</td>
	 *            </tr>
	 *            <tr>
	 *            <td>H1S1SubItem1</td>
	 *            <td>"Heading1|H1SubItem3|H1S1SubItem1" or "#0|#2|#0"</td>
	 *            </tr>
	 *            </table>
	 * 
	 *            References can also be mixed like "Heading1|#1".
	 * @return Returns false if window/control could not be found, otherwise
	 *         returns false.
	 */
	public static boolean expand(final HWND hWnd, final HWND hCtrl,
			final String item) {
		return ((hWnd == null) || (hCtrl == null)) ? false : expand(
				buildTitle(hWnd), buildControlId(hCtrl), item);
	}

	/**
	 * Returns the text of an item.
	 * 
	 * @param title
	 *            The title of the window to access.
	 * @param control
	 *            The control to interact with.
	 * @param item
	 *            The item which will be checked(if the item supports it).<br/>
	 *            The "item" parameter is a string-based parameter that is used
	 *            to reference a particular treeview item using a combination of
	 *            text and indices. Indices are 0-based. For example:<br/>
	 *            Heading1<br/>
	 *            ----> H1SubItem1<br/>
	 *            ----> H1SubItem2<br/>
	 *            ----> H1SubItem3<br/>
	 *            ----> ----> H1S1SubItem1<br/>
	 *            Heading2<br/>
	 *            Heading3<br/>
	 * 
	 *            Each "level" is separated by |. An index is preceded with #.
	 *            Examples:<br/>
	 * 
	 *            <table>
	 *            <thead>
	 *            <tr>
	 *            <td>Item</td>
	 *            <td>Item Reference</td>
	 *            </tr>
	 *            </thead>
	 *            <tr>
	 *            <td>Heading2</td>
	 *            <td>"Heading2" or "#1"</td>
	 *            </tr>
	 *            <tr>
	 *            <td>H1SubItem2</td>
	 *            <td>"Heading1|H1SubItem2" or "#0|#1"</td>
	 *            </tr>
	 *            <tr>
	 *            <td>H1S1SubItem1</td>
	 *            <td>"Heading1|H1SubItem3|H1S1SubItem1" or "#0|#2|#0"</td>
	 *            </tr>
	 *            </table>
	 * 
	 *            References can also be mixed like "Heading1|#1".
	 * @return Returns the handle of the item if success, returns null if
	 *         failed.
	 */
	public static HWND getHandle(final String title, final String control,
			final String item) {
		return getHandle(title, null, control, item);
	}

	/**
	 * Returns the text of an item.
	 * 
	 * @param title
	 *            The title of the window to access.
	 * @param text
	 *            The text of the window to access.
	 * @param control
	 *            The control to interact with.
	 * @param item
	 *            The item which will be checked(if the item supports it).<br/>
	 *            The "item" parameter is a string-based parameter that is used
	 *            to reference a particular treeview item using a combination of
	 *            text and indices. Indices are 0-based. For example:<br/>
	 *            Heading1<br/>
	 *            ----> H1SubItem1<br/>
	 *            ----> H1SubItem2<br/>
	 *            ----> H1SubItem3<br/>
	 *            ----> ----> H1S1SubItem1<br/>
	 *            Heading2<br/>
	 *            Heading3<br/>
	 * 
	 *            Each "level" is separated by |. An index is preceded with #.
	 *            Examples:<br/>
	 * 
	 *            <table>
	 *            <thead>
	 *            <tr>
	 *            <td>Item</td>
	 *            <td>Item Reference</td>
	 *            </tr>
	 *            </thead>
	 *            <tr>
	 *            <td>Heading2</td>
	 *            <td>"Heading2" or "#1"</td>
	 *            </tr>
	 *            <tr>
	 *            <td>H1SubItem2</td>
	 *            <td>"Heading1|H1SubItem2" or "#0|#1"</td>
	 *            </tr>
	 *            <tr>
	 *            <td>H1S1SubItem1</td>
	 *            <td>"Heading1|H1SubItem3|H1S1SubItem1" or "#0|#2|#0"</td>
	 *            </tr>
	 *            </table>
	 * 
	 *            References can also be mixed like "Heading1|#1".
	 * @return Returns the handle of the item if success, returns null if
	 *         failed.
	 */
	public static HWND getHandle(final String title, final String text,
			final String control, final String item) {
		HWND itemHWND = null;
		if (StringUtils.isNotEmpty(item) && exists(title, text, control, item)) {
			String[] textIndexReferences = StringUtils.split(item, '|');
			List<String> indexList = new ArrayList<String>();
			for (int i = 0; i < textIndexReferences.length; i++) {
				if (i == 0) {
					itemHWND = getFirstItemHandle(title, text, control);
				} else {
					itemHWND = getFirstChildHandle(title, text, control,
							itemHWND);
				}

				// break for loop
				if (itemHWND == null) {
					break;
				}

				int index = -1;
				if (textIndexReferences[i].startsWith("#")
						&& NumberUtils.toInt(
								textIndexReferences[i].substring(1), -1) >= 0) {
					// index reference
					index = NumberUtils.toInt(textIndexReferences[i]
							.substring(1));
				} else {
					// text reference
					String strItem = StringUtils.join(indexList.toArray(), '|');
					int itemCount = getItemCount(title, text, control, strItem);
					for (int j = 0; j < itemCount; j++) {
						if (textIndexReferences[i].equals(getText(title, text,
								control,
								strItem
										+ (StringUtils.isEmpty(strItem) ? "#"
												: "|#") + j))) {
							index = j;
							break;
						}
					}
				}
				if (index < 0) {
					itemHWND = null;
				} else {
					indexList.add("#" + index);
					while (index > 0) {
						itemHWND = getNextSiblingHandle(title, text, control,
								itemHWND);
						// break while loop
						if (itemHWND == null) {
							break;
						}
						index--;
					}
				}

				// break for loop
				if (itemHWND == null) {
					break;
				}
			}
		}
		return itemHWND;
	}

	/**
	 * Returns the text of an item.
	 * 
	 * @param hWnd
	 *            The handle of the window to access.
	 * @param hCtrl
	 *            The handle of the control to interact with.
	 * @param item
	 *            The item which will be checked(if the item supports it).<br/>
	 *            The "item" parameter is a string-based parameter that is used
	 *            to reference a particular treeview item using a combination of
	 *            text and indices. Indices are 0-based. For example:<br/>
	 *            Heading1<br/>
	 *            ----> H1SubItem1<br/>
	 *            ----> H1SubItem2<br/>
	 *            ----> H1SubItem3<br/>
	 *            ----> ----> H1S1SubItem1<br/>
	 *            Heading2<br/>
	 *            Heading3<br/>
	 * 
	 *            Each "level" is separated by |. An index is preceded with #.
	 *            Examples:<br/>
	 * 
	 *            <table>
	 *            <thead>
	 *            <tr>
	 *            <td>Item</td>
	 *            <td>Item Reference</td>
	 *            </tr>
	 *            </thead>
	 *            <tr>
	 *            <td>Heading2</td>
	 *            <td>"Heading2" or "#1"</td>
	 *            </tr>
	 *            <tr>
	 *            <td>H1SubItem2</td>
	 *            <td>"Heading1|H1SubItem2" or "#0|#1"</td>
	 *            </tr>
	 *            <tr>
	 *            <td>H1S1SubItem1</td>
	 *            <td>"Heading1|H1SubItem3|H1S1SubItem1" or "#0|#2|#0"</td>
	 *            </tr>
	 *            </table>
	 * 
	 *            References can also be mixed like "Heading1|#1".
	 * @return Returns the handle of the item if success, returns null if
	 *         failed.
	 */
	public static HWND getHandle(final HWND hWnd, final HWND hCtrl,
			final String item) {
		return ((hWnd == null) || (hCtrl == null)) ? null : getHandle(
				buildTitle(hWnd), buildControlId(hCtrl), item);
	}

	/**
	 * Returns the number of children for a selected item.
	 * 
	 * @param title
	 *            The title of the window to access.
	 * @param control
	 *            The control to interact with.
	 * @param item
	 *            The "item" parameter is a string-based parameter that is used
	 *            to reference a particular treeview item using a combination of
	 *            text and indices. Indices are 0-based. For example:<br/>
	 *            Heading1<br/>
	 *            ----> H1SubItem1<br/>
	 *            ----> H1SubItem2<br/>
	 *            ----> H1SubItem3<br/>
	 *            ----> ----> H1S1SubItem1<br/>
	 *            Heading2<br/>
	 *            Heading3<br/>
	 * 
	 *            Each "level" is separated by |. An index is preceded with #.
	 *            Examples:<br/>
	 * 
	 *            <table>
	 *            <thead>
	 *            <tr>
	 *            <td>Item</td>
	 *            <td>Item Reference</td>
	 *            </tr>
	 *            </thead>
	 *            <tr>
	 *            <td>Heading2</td>
	 *            <td>"Heading2" or "#1"</td>
	 *            </tr>
	 *            <tr>
	 *            <td>H1SubItem2</td>
	 *            <td>"Heading1|H1SubItem2" or "#0|#1"</td>
	 *            </tr>
	 *            <tr>
	 *            <td>H1S1SubItem1</td>
	 *            <td>"Heading1|H1SubItem3|H1S1SubItem1" or "#0|#2|#0"</td>
	 *            </tr>
	 *            </table>
	 * 
	 *            References can also be mixed like "Heading1|#1".
	 * @return Return null if window/control could not be found, otherwise
	 *         returns the number of children for a selected item.
	 */
	public static Integer getItemCount(final String title,
			final String control, final String item) {
		return getItemCount(title, null, control, item);
	}

	/**
	 * Returns the number of children for a selected item.
	 * 
	 * @param title
	 *            The title of the window to access.
	 * @param text
	 *            The text of the window to access.
	 * @param control
	 *            The control to interact with.
	 * @param item
	 *            The "item" parameter is a string-based parameter that is used
	 *            to reference a particular treeview item using a combination of
	 *            text and indices. Indices are 0-based. For example:<br/>
	 *            Heading1<br/>
	 *            ----> H1SubItem1<br/>
	 *            ----> H1SubItem2<br/>
	 *            ----> H1SubItem3<br/>
	 *            ----> ----> H1S1SubItem1<br/>
	 *            Heading2<br/>
	 *            Heading3<br/>
	 * 
	 *            Each "level" is separated by |. An index is preceded with #.
	 *            Examples:<br/>
	 * 
	 *            <table>
	 *            <thead>
	 *            <tr>
	 *            <td>Item</td>
	 *            <td>Item Reference</td>
	 *            </tr>
	 *            </thead>
	 *            <tr>
	 *            <td>Heading2</td>
	 *            <td>"Heading2" or "#1"</td>
	 *            </tr>
	 *            <tr>
	 *            <td>H1SubItem2</td>
	 *            <td>"Heading1|H1SubItem2" or "#0|#1"</td>
	 *            </tr>
	 *            <tr>
	 *            <td>H1S1SubItem1</td>
	 *            <td>"Heading1|H1SubItem3|H1S1SubItem1" or "#0|#2|#0"</td>
	 *            </tr>
	 *            </table>
	 * 
	 *            References can also be mixed like "Heading1|#1".
	 * @return Return null if window/control could not be found, otherwise
	 *         returns the number of children for a selected item.
	 */
	public static Integer getItemCount(final String title, final String text,
			final String control, final String item) {
		Integer itemCount = null;
		if (exists(title, text, control, item)) {
			final String strItemCount = controlTreeView(title, text, control,
					COMMAND_GET_ITEM_COUNT, item, null, INT_BUF_SIZE);
			if (!hasError()) {
				itemCount = NumberUtils.toInt(strItemCount);
			}
		}
		return itemCount;
	}

	/**
	 * Returns the number of children for a selected item.
	 * 
	 * @param hWnd
	 *            The handle of the window to access.
	 * @param hCtrl
	 *            The handle of the control to interact with.
	 * @param item
	 *            The "item" parameter is a string-based parameter that is used
	 *            to reference a particular treeview item using a combination of
	 *            text and indices. Indices are 0-based. For example:<br/>
	 *            Heading1<br/>
	 *            ----> H1SubItem1<br/>
	 *            ----> H1SubItem2<br/>
	 *            ----> H1SubItem3<br/>
	 *            ----> ----> H1S1SubItem1<br/>
	 *            Heading2<br/>
	 *            Heading3<br/>
	 * 
	 *            Each "level" is separated by |. An index is preceded with #.
	 *            Examples:<br/>
	 * 
	 *            <table>
	 *            <thead>
	 *            <tr>
	 *            <td>Item</td>
	 *            <td>Item Reference</td>
	 *            </tr>
	 *            </thead>
	 *            <tr>
	 *            <td>Heading2</td>
	 *            <td>"Heading2" or "#1"</td>
	 *            </tr>
	 *            <tr>
	 *            <td>H1SubItem2</td>
	 *            <td>"Heading1|H1SubItem2" or "#0|#1"</td>
	 *            </tr>
	 *            <tr>
	 *            <td>H1S1SubItem1</td>
	 *            <td>"Heading1|H1SubItem3|H1S1SubItem1" or "#0|#2|#0"</td>
	 *            </tr>
	 *            </table>
	 * 
	 *            References can also be mixed like "Heading1|#1".
	 * @return Return null if window/control could not be found, otherwise
	 *         returns the number of children for a selected item.
	 */
	public static Integer getItemCount(final HWND hWnd, final HWND hCtrl,
			final String item) {
		return ((hWnd == null) || (hCtrl == null)) ? null : getItemCount(
				buildTitle(hWnd), buildControlId(hCtrl), item);
	}

	/**
	 * Returns the item reference of the current selection using the text
	 * reference of the item.
	 * 
	 * @param title
	 *            The title of the window to access.
	 * @param control
	 *            The control to interact with.
	 * @return Return null if window/control could not be found, otherwise
	 *         returns the item reference of the current selection.
	 */
	public static String getSelected(final String title, final String control) {
		return getSelected(title, null, control);
	}

	/**
	 * Returns the item reference of the current selection using the text
	 * reference of the item.
	 * 
	 * @param title
	 *            The title of the window to access.
	 * @param text
	 *            The text of the window to access.
	 * @param control
	 *            The control to interact with.
	 * @return Return null if window/control could not be found, otherwise
	 *         returns the item reference of the current selection.
	 */
	public static String getSelected(final String title, final String text,
			final String control) {
		return getSelected(title, text, control, null);
	}

	/**
	 * Returns the item reference of the current selection using the text
	 * reference of the item.
	 * 
	 * @param hWnd
	 *            The handle of the window to access.
	 * @param hCtrl
	 *            The handle of the control to interact with.
	 * @return Return null if window/control could not be found, otherwise
	 *         returns the item reference of the current selection.
	 */
	public static String getSelected(final HWND hWnd, final HWND hCtrl) {
		return ((hWnd == null) || (hCtrl == null)) ? null : getSelected(
				buildTitle(hWnd), buildControlId(hCtrl));
	}

	/**
	 * Returns the item reference of the current selection using the text
	 * reference of the item (or index reference if useIndex is set to true).
	 * 
	 * @param title
	 *            The title of the window to access.
	 * @param control
	 *            The control to interact with.
	 * @param useIndex
	 *            If true returns the index reference of the current selection
	 *            using the text reference of the item, otherwise returns the
	 *            item reference of the current selection using the index
	 *            reference of the item.
	 * @return Return null if window/control could not be found, otherwise
	 *         returns the item reference of the current selection.
	 */
	public static String getSelected(final String title, final String control,
			final Boolean useIndex) {
		return getSelected(title, null, control, useIndex);
	}

	/**
	 * Returns the item reference of the current selection using the text
	 * reference of the item (or index reference if useIndex is set to true).
	 * 
	 * @param title
	 *            The title of the window to access.
	 * @param text
	 *            The text of the window to access.
	 * @param control
	 *            The control to interact with.
	 * @param useIndex
	 *            If true returns the index reference of the current selection
	 *            using the text reference of the item, otherwise returns the
	 *            item reference of the current selection using the index
	 *            reference of the item.
	 * @return Return null if window/control could not be found, otherwise
	 *         returns the item reference of the current selection.
	 */
	public static String getSelected(final String title, final String text,
			final String control, final Boolean useIndex) {
		final String selected = controlTreeView(title, text, control,
				COMMAND_GET_SELECTED, (useIndex == null) ? null
						: (useIndex ? "1" : null), null, INT_BUF_SIZE);
		return hasError() ? null : selected;
	}

	/**
	 * Returns the item reference of the current selection using the text
	 * reference of the item (or index reference if useIndex is set to true).
	 * 
	 * @param hWnd
	 *            The handle of the window to access.
	 * @param hCtrl
	 *            The handle of the control to interact with.
	 * @param useIndex
	 *            If true returns the index reference of the current selection
	 *            using the text reference of the item, otherwise returns the
	 *            item reference of the current selection using the index
	 *            reference of the item.
	 * @return Return null if window/control could not be found, otherwise
	 *         returns the item reference of the current selection.
	 */
	public static String getSelected(final HWND hWnd, final HWND hCtrl,
			final Boolean useIndex) {
		return ((hWnd == null) || (hCtrl == null)) ? null : getSelected(
				buildTitle(hWnd), buildControlId(hCtrl), useIndex);
	}

	/**
	 * Returns the text of the current selected item.
	 * 
	 * @param title
	 *            The title of the window to access.
	 * @param control
	 *            The control to interact with.
	 * @return Return null if window/control could not be found, otherwise
	 *         returns the text of the current selected item.
	 */
	public static String getSelectedText(final String title,
			final String control) {
		return getSelectedText(title, null, control);
	}

	/**
	 * Returns the text of the current selected item.
	 * 
	 * @param title
	 *            The title of the window to access.
	 * @param text
	 *            The text of the window to access.
	 * @param control
	 *            The control to interact with.
	 * @return Return null if window/control could not be found, otherwise
	 *         returns the text of the current selected item.
	 */
	public static String getSelectedText(final String title, final String text,
			final String control) {
		String selectedText = null;
		String selectedItem = getSelected(title, text, control, true);
		if (selectedItem != null) {
			selectedText = getText(title, text, control, selectedItem);
		}
		return selectedText;
	}

	/**
	 * Returns the text of the current selected item.
	 * 
	 * @param hWnd
	 *            The handle of the window to access.
	 * @param hCtrl
	 *            The handle of the control to interact with.
	 * @return Return null if window/control could not be found, otherwise
	 *         returns the text of the current selected item.
	 */
	public static String getSelectedText(final HWND hWnd, final HWND hCtrl) {
		return ((hWnd == null) || (hCtrl == null)) ? null : getSelectedText(
				buildTitle(hWnd), buildControlId(hCtrl));
	}

	/**
	 * Returns the text of an item.
	 * 
	 * @param title
	 *            The title of the window to access.
	 * @param control
	 *            The control to interact with.
	 * @param item
	 *            The item to get the text.<br/>
	 *            The item which will be checked(if the item supports it).<br/>
	 *            The "item" parameter is a string-based parameter that is used
	 *            to reference a particular treeview item using a combination of
	 *            text and indices. Indices are 0-based. For example:<br/>
	 *            Heading1<br/>
	 *            ----> H1SubItem1<br/>
	 *            ----> H1SubItem2<br/>
	 *            ----> H1SubItem3<br/>
	 *            ----> ----> H1S1SubItem1<br/>
	 *            Heading2<br/>
	 *            Heading3<br/>
	 * 
	 *            Each "level" is separated by |. An index is preceded with #.
	 *            Examples:<br/>
	 * 
	 *            <table>
	 *            <thead>
	 *            <tr>
	 *            <td>Item</td>
	 *            <td>Item Reference</td>
	 *            </tr>
	 *            </thead>
	 *            <tr>
	 *            <td>Heading2</td>
	 *            <td>"Heading2" or "#1"</td>
	 *            </tr>
	 *            <tr>
	 *            <td>H1SubItem2</td>
	 *            <td>"Heading1|H1SubItem2" or "#0|#1"</td>
	 *            </tr>
	 *            <tr>
	 *            <td>H1S1SubItem1</td>
	 *            <td>"Heading1|H1SubItem3|H1S1SubItem1" or "#0|#2|#0"</td>
	 *            </tr>
	 *            </table>
	 * 
	 *            References can also be mixed like "Heading1|#1".
	 * @return Returns the text of an item.
	 */
	public static String getText(final String title, final String control,
			final String item) {
		return getText(title, null, control, item);
	}

	/**
	 * Returns the text of an item.
	 * 
	 * @param title
	 *            The title of the window to access.
	 * @param text
	 *            The text of the window to access.
	 * @param control
	 *            The control to interact with.
	 * @param item
	 *            The item to get the text.<br/>
	 *            The item which will be checked(if the item supports it).<br/>
	 *            The "item" parameter is a string-based parameter that is used
	 *            to reference a particular treeview item using a combination of
	 *            text and indices. Indices are 0-based. For example:<br/>
	 *            Heading1<br/>
	 *            ----> H1SubItem1<br/>
	 *            ----> H1SubItem2<br/>
	 *            ----> H1SubItem3<br/>
	 *            ----> ----> H1S1SubItem1<br/>
	 *            Heading2<br/>
	 *            Heading3<br/>
	 * 
	 *            Each "level" is separated by |. An index is preceded with #.
	 *            Examples:<br/>
	 * 
	 *            <table>
	 *            <thead>
	 *            <tr>
	 *            <td>Item</td>
	 *            <td>Item Reference</td>
	 *            </tr>
	 *            </thead>
	 *            <tr>
	 *            <td>Heading2</td>
	 *            <td>"Heading2" or "#1"</td>
	 *            </tr>
	 *            <tr>
	 *            <td>H1SubItem2</td>
	 *            <td>"Heading1|H1SubItem2" or "#0|#1"</td>
	 *            </tr>
	 *            <tr>
	 *            <td>H1S1SubItem1</td>
	 *            <td>"Heading1|H1SubItem3|H1S1SubItem1" or "#0|#2|#0"</td>
	 *            </tr>
	 *            </table>
	 * 
	 *            References can also be mixed like "Heading1|#1".
	 * @return Returns the text of an item.
	 */
	public static String getText(final String title, final String text,
			final String control, final String item) {
		String itemText = null;
		if (StringUtils.isNotBlank(item)) {
			itemText = controlTreeView(title, text, control, COMMAND_GET_TEXT,
					item, null, BUF_SIZE);
			if (hasError()) {
				itemText = null;
			}
		}
		return itemText;
	}

	/**
	 * Returns the text of an item.
	 * 
	 * @param hWnd
	 *            The handle of the window to access.
	 * @param hCtrl
	 *            The handle of the control to interact with.
	 * @param item
	 *            The item to get the text.<br/>
	 *            The item which will be checked(if the item supports it).<br/>
	 *            The "item" parameter is a string-based parameter that is used
	 *            to reference a particular treeview item using a combination of
	 *            text and indices. Indices are 0-based. For example:<br/>
	 *            Heading1<br/>
	 *            ----> H1SubItem1<br/>
	 *            ----> H1SubItem2<br/>
	 *            ----> H1SubItem3<br/>
	 *            ----> ----> H1S1SubItem1<br/>
	 *            Heading2<br/>
	 *            Heading3<br/>
	 * 
	 *            Each "level" is separated by |. An index is preceded with #.
	 *            Examples:<br/>
	 * 
	 *            <table>
	 *            <thead>
	 *            <tr>
	 *            <td>Item</td>
	 *            <td>Item Reference</td>
	 *            </tr>
	 *            </thead>
	 *            <tr>
	 *            <td>Heading2</td>
	 *            <td>"Heading2" or "#1"</td>
	 *            </tr>
	 *            <tr>
	 *            <td>H1SubItem2</td>
	 *            <td>"Heading1|H1SubItem2" or "#0|#1"</td>
	 *            </tr>
	 *            <tr>
	 *            <td>H1S1SubItem1</td>
	 *            <td>"Heading1|H1SubItem3|H1S1SubItem1" or "#0|#2|#0"</td>
	 *            </tr>
	 *            </table>
	 * 
	 *            References can also be mixed like "Heading1|#1".
	 * @return Returns the text of an item.
	 */
	public static String getText(final HWND hWnd, final HWND hCtrl,
			final String item) {
		return ((hWnd == null) || (hCtrl == null)) ? null : getText(
				buildTitle(hWnd), buildControlId(hCtrl), item);
	}

	/**
	 * Returns the state of an item.
	 * 
	 * @param title
	 *            The title of the window to access.
	 * @param control
	 *            The control to interact with.
	 * @param item
	 *            The "item" parameter is a string-based parameter that is used
	 *            to reference a particular treeview item using a combination of
	 *            text and indices. Indices are 0-based. For example:<br/>
	 *            Heading1<br/>
	 *            ----> H1SubItem1<br/>
	 *            ----> H1SubItem2<br/>
	 *            ----> H1SubItem3<br/>
	 *            ----> ----> H1S1SubItem1<br/>
	 *            Heading2<br/>
	 *            Heading3<br/>
	 * 
	 *            Each "level" is separated by |. An index is preceded with #.
	 *            Examples:<br/>
	 * 
	 *            <table>
	 *            <thead>
	 *            <tr>
	 *            <td>Item</td>
	 *            <td>Item Reference</td>
	 *            </tr>
	 *            </thead>
	 *            <tr>
	 *            <td>Heading2</td>
	 *            <td>"Heading2" or "#1"</td>
	 *            </tr>
	 *            <tr>
	 *            <td>H1SubItem2</td>
	 *            <td>"Heading1|H1SubItem2" or "#0|#1"</td>
	 *            </tr>
	 *            <tr>
	 *            <td>H1S1SubItem1</td>
	 *            <td>"Heading1|H1SubItem3|H1S1SubItem1" or "#0|#2|#0"</td>
	 *            </tr>
	 *            </table>
	 * 
	 *            References can also be mixed like "Heading1|#1".
	 * @return Returns true if item is checked, otherwise return false.
	 */
	public static boolean isChecked(final String title, final String control,
			final String item) {
		return isChecked(title, null, control, item);
	}

	/**
	 * Returns the state of an item.
	 * 
	 * @param title
	 *            The title of the window to access.
	 * @param text
	 *            The text of the window to access.
	 * @param control
	 *            The control to interact with.
	 * @param item
	 *            The "item" parameter is a string-based parameter that is used
	 *            to reference a particular treeview item using a combination of
	 *            text and indices. Indices are 0-based. For example:<br/>
	 *            Heading1<br/>
	 *            ----> H1SubItem1<br/>
	 *            ----> H1SubItem2<br/>
	 *            ----> H1SubItem3<br/>
	 *            ----> ----> H1S1SubItem1<br/>
	 *            Heading2<br/>
	 *            Heading3<br/>
	 * 
	 *            Each "level" is separated by |. An index is preceded with #.
	 *            Examples:<br/>
	 * 
	 *            <table>
	 *            <thead>
	 *            <tr>
	 *            <td>Item</td>
	 *            <td>Item Reference</td>
	 *            </tr>
	 *            </thead>
	 *            <tr>
	 *            <td>Heading2</td>
	 *            <td>"Heading2" or "#1"</td>
	 *            </tr>
	 *            <tr>
	 *            <td>H1SubItem2</td>
	 *            <td>"Heading1|H1SubItem2" or "#0|#1"</td>
	 *            </tr>
	 *            <tr>
	 *            <td>H1S1SubItem1</td>
	 *            <td>"Heading1|H1SubItem3|H1S1SubItem1" or "#0|#2|#0"</td>
	 *            </tr>
	 *            </table>
	 * 
	 *            References can also be mixed like "Heading1|#1".
	 * @return Returns true if item is checked, otherwise return false.
	 */
	public static boolean isChecked(final String title, final String text,
			final String control, final String item) {
		return isChecked_(title, text, control, item) == IsChecked.CHECKED;
	}

	/**
	 * Returns the state of an item.
	 * 
	 * @param hWnd
	 *            The handle of the window to access.
	 * @param hCtrl
	 *            The handle of the control to interact with.
	 * @param item
	 *            The "item" parameter is a string-based parameter that is used
	 *            to reference a particular treeview item using a combination of
	 *            text and indices. Indices are 0-based. For example:<br/>
	 *            Heading1<br/>
	 *            ----> H1SubItem1<br/>
	 *            ----> H1SubItem2<br/>
	 *            ----> H1SubItem3<br/>
	 *            ----> ----> H1S1SubItem1<br/>
	 *            Heading2<br/>
	 *            Heading3<br/>
	 * 
	 *            Each "level" is separated by |. An index is preceded with #.
	 *            Examples:<br/>
	 * 
	 *            <table>
	 *            <thead>
	 *            <tr>
	 *            <td>Item</td>
	 *            <td>Item Reference</td>
	 *            </tr>
	 *            </thead>
	 *            <tr>
	 *            <td>Heading2</td>
	 *            <td>"Heading2" or "#1"</td>
	 *            </tr>
	 *            <tr>
	 *            <td>H1SubItem2</td>
	 *            <td>"Heading1|H1SubItem2" or "#0|#1"</td>
	 *            </tr>
	 *            <tr>
	 *            <td>H1S1SubItem1</td>
	 *            <td>"Heading1|H1SubItem3|H1S1SubItem1" or "#0|#2|#0"</td>
	 *            </tr>
	 *            </table>
	 * 
	 *            References can also be mixed like "Heading1|#1".
	 * @return Returns true if item is checked, otherwise return false.
	 */
	public static boolean isChecked(final HWND hWnd, final HWND hCtrl,
			final String item) {
		return ((hWnd == null) || (hCtrl == null)) ? false : isChecked(
				buildTitle(hWnd), buildControlId(hCtrl), item);
	}

	/**
	 * Returns the state of an item.
	 * 
	 * @param title
	 *            The title of the window to access.
	 * @param control
	 *            The control to interact with.
	 * @param item
	 *            The "item" parameter is a string-based parameter that is used
	 *            to reference a particular treeview item using a combination of
	 *            text and indices. Indices are 0-based. For example:<br/>
	 *            Heading1<br/>
	 *            ----> H1SubItem1<br/>
	 *            ----> H1SubItem2<br/>
	 *            ----> H1SubItem3<br/>
	 *            ----> ----> H1S1SubItem1<br/>
	 *            Heading2<br/>
	 *            Heading3<br/>
	 * 
	 *            Each "level" is separated by |. An index is preceded with #.
	 *            Examples:<br/>
	 * 
	 *            <table>
	 *            <thead>
	 *            <tr>
	 *            <td>Item</td>
	 *            <td>Item Reference</td>
	 *            </tr>
	 *            </thead>
	 *            <tr>
	 *            <td>Heading2</td>
	 *            <td>"Heading2" or "#1"</td>
	 *            </tr>
	 *            <tr>
	 *            <td>H1SubItem2</td>
	 *            <td>"Heading1|H1SubItem2" or "#0|#1"</td>
	 *            </tr>
	 *            <tr>
	 *            <td>H1S1SubItem1</td>
	 *            <td>"Heading1|H1SubItem3|H1S1SubItem1" or "#0|#2|#0"</td>
	 *            </tr>
	 *            </table>
	 * 
	 *            References can also be mixed like "Heading1|#1".
	 * @return Returns IsChecked.CHECKED if item is checked, returns
	 *         IsChecked.UNCHECKED if item is not checked, otherwise return
	 *         IsChecked.NOT_A_CHECKBOX.
	 */
	public static IsChecked isChecked_(final String title,
			final String control, final String item) {
		return isChecked_(title, null, control, item);
	}

	/**
	 * Returns the state of an item.
	 * 
	 * @param title
	 *            The title of the window to access.
	 * @param text
	 *            The text of the window to access.
	 * @param control
	 *            The control to interact with.
	 * @param item
	 *            The "item" parameter is a string-based parameter that is used
	 *            to reference a particular treeview item using a combination of
	 *            text and indices. Indices are 0-based. For example:<br/>
	 *            Heading1<br/>
	 *            ----> H1SubItem1<br/>
	 *            ----> H1SubItem2<br/>
	 *            ----> H1SubItem3<br/>
	 *            ----> ----> H1S1SubItem1<br/>
	 *            Heading2<br/>
	 *            Heading3<br/>
	 * 
	 *            Each "level" is separated by |. An index is preceded with #.
	 *            Examples:<br/>
	 * 
	 *            <table>
	 *            <thead>
	 *            <tr>
	 *            <td>Item</td>
	 *            <td>Item Reference</td>
	 *            </tr>
	 *            </thead>
	 *            <tr>
	 *            <td>Heading2</td>
	 *            <td>"Heading2" or "#1"</td>
	 *            </tr>
	 *            <tr>
	 *            <td>H1SubItem2</td>
	 *            <td>"Heading1|H1SubItem2" or "#0|#1"</td>
	 *            </tr>
	 *            <tr>
	 *            <td>H1S1SubItem1</td>
	 *            <td>"Heading1|H1SubItem3|H1S1SubItem1" or "#0|#2|#0"</td>
	 *            </tr>
	 *            </table>
	 * 
	 *            References can also be mixed like "Heading1|#1".
	 * @return Returns IsChecked.CHECKED if item is checked, returns
	 *         IsChecked.UNCHECKED if item is not checked, otherwise return
	 *         IsChecked.NOT_A_CHECKBOX.
	 */
	public static IsChecked isChecked_(final String title, final String text,
			final String control, final String item) {
		IsChecked isChecked = IsChecked.NOT_A_CHECKBOX;
		if (StringUtils.isNotBlank(item)) {
			String status = controlTreeView(title, text, control,
					COMMAND_IS_CHECKED, item, null, INT_BUF_SIZE);
			if (String.valueOf(IsChecked.CHECKED.getStatus()).equals(status)) {
				isChecked = IsChecked.CHECKED;
			} else if (String.valueOf(IsChecked.UNCHECKED.getStatus()).equals(
					status)) {
				// Note: AutoItX will return '0:unchecked' for not exists item,
				// so we will use getItemCount to check whether item is a
				// checkbox or not
				Integer itemCount = getItemCount(title, text, control, item);
				if (itemCount != null) {
					isChecked = IsChecked.UNCHECKED;
				}
			}
		}
		return isChecked;
	}

	/**
	 * Returns the state of an item.
	 * 
	 * @param hWnd
	 *            The handle of the window to access.
	 * @param hCtrl
	 *            The handle of the control to interact with.
	 * @param item
	 *            The "item" parameter is a string-based parameter that is used
	 *            to reference a particular treeview item using a combination of
	 *            text and indices. Indices are 0-based. For example:<br/>
	 *            Heading1<br/>
	 *            ----> H1SubItem1<br/>
	 *            ----> H1SubItem2<br/>
	 *            ----> H1SubItem3<br/>
	 *            ----> ----> H1S1SubItem1<br/>
	 *            Heading2<br/>
	 *            Heading3<br/>
	 * 
	 *            Each "level" is separated by |. An index is preceded with #.
	 *            Examples:<br/>
	 * 
	 *            <table>
	 *            <thead>
	 *            <tr>
	 *            <td>Item</td>
	 *            <td>Item Reference</td>
	 *            </tr>
	 *            </thead>
	 *            <tr>
	 *            <td>Heading2</td>
	 *            <td>"Heading2" or "#1"</td>
	 *            </tr>
	 *            <tr>
	 *            <td>H1SubItem2</td>
	 *            <td>"Heading1|H1SubItem2" or "#0|#1"</td>
	 *            </tr>
	 *            <tr>
	 *            <td>H1S1SubItem1</td>
	 *            <td>"Heading1|H1SubItem3|H1S1SubItem1" or "#0|#2|#0"</td>
	 *            </tr>
	 *            </table>
	 * 
	 *            References can also be mixed like "Heading1|#1".
	 * @return Returns IsChecked.CHECKED if item is checked, returns
	 *         IsChecked.UNCHECKED if item is not checked, otherwise return
	 *         IsChecked.NOT_A_CHECKBOX.
	 */
	public static IsChecked isChecked_(final HWND hWnd, final HWND hCtrl,
			final String item) {
		return ((hWnd == null) || (hCtrl == null)) ? IsChecked.NOT_A_CHECKBOX
				: isChecked_(buildTitle(hWnd), buildControlId(hCtrl), item);
	}

	/**
	 * Checks to see if the item collapsed.
	 * 
	 * @param title
	 *            The title of the window to access.
	 * @param control
	 *            The control to interact with.
	 * @param item
	 *            The "item" parameter is a string-based parameter that is used
	 *            to reference a particular treeview item using a combination of
	 *            text and indices. Indices are 0-based. For example:<br/>
	 *            Heading1<br/>
	 *            ----> H1SubItem1<br/>
	 *            ----> H1SubItem2<br/>
	 *            ----> H1SubItem3<br/>
	 *            ----> ----> H1S1SubItem1<br/>
	 *            Heading2<br/>
	 *            Heading3<br/>
	 * 
	 *            Each "level" is separated by |. An index is preceded with #.
	 *            Examples:<br/>
	 * 
	 *            <table>
	 *            <thead>
	 *            <tr>
	 *            <td>Item</td>
	 *            <td>Item Reference</td>
	 *            </tr>
	 *            </thead>
	 *            <tr>
	 *            <td>Heading2</td>
	 *            <td>"Heading2" or "#1"</td>
	 *            </tr>
	 *            <tr>
	 *            <td>H1SubItem2</td>
	 *            <td>"Heading1|H1SubItem2" or "#0|#1"</td>
	 *            </tr>
	 *            <tr>
	 *            <td>H1S1SubItem1</td>
	 *            <td>"Heading1|H1SubItem3|H1S1SubItem1" or "#0|#2|#0"</td>
	 *            </tr>
	 *            </table>
	 * 
	 *            References can also be mixed like "Heading1|#1".
	 * @return Returns true if item is collapsed, otherwise return false.
	 */
	public static boolean isCollapsed(final String title, final String control,
			final String item) {
		return isCollapsed(title, null, control, item);
	}

	/**
	 * Checks to see if the item collapsed.
	 * 
	 * @param title
	 *            The title of the window to access.
	 * @param text
	 *            The text of the window to access.
	 * @param control
	 *            The control to interact with.
	 * @param item
	 *            The "item" parameter is a string-based parameter that is used
	 *            to reference a particular treeview item using a combination of
	 *            text and indices. Indices are 0-based. For example:<br/>
	 *            Heading1<br/>
	 *            ----> H1SubItem1<br/>
	 *            ----> H1SubItem2<br/>
	 *            ----> H1SubItem3<br/>
	 *            ----> ----> H1S1SubItem1<br/>
	 *            Heading2<br/>
	 *            Heading3<br/>
	 * 
	 *            Each "level" is separated by |. An index is preceded with #.
	 *            Examples:<br/>
	 * 
	 *            <table>
	 *            <thead>
	 *            <tr>
	 *            <td>Item</td>
	 *            <td>Item Reference</td>
	 *            </tr>
	 *            </thead>
	 *            <tr>
	 *            <td>Heading2</td>
	 *            <td>"Heading2" or "#1"</td>
	 *            </tr>
	 *            <tr>
	 *            <td>H1SubItem2</td>
	 *            <td>"Heading1|H1SubItem2" or "#0|#1"</td>
	 *            </tr>
	 *            <tr>
	 *            <td>H1S1SubItem1</td>
	 *            <td>"Heading1|H1SubItem3|H1S1SubItem1" or "#0|#2|#0"</td>
	 *            </tr>
	 *            </table>
	 * 
	 *            References can also be mixed like "Heading1|#1".
	 * @return Returns true if item is collapsed, otherwise return false.
	 */
	public static boolean isCollapsed(final String title, final String text,
			final String control, final String item) {
		// item has sub items and item is not expanded
		return (getItemCount(title, text, control, item) > 0)
				&& !isExpanded(title, text, control, item);
	}

	/**
	 * Checks to see if the item collapsed.
	 * 
	 * @param hWnd
	 *            The handle of the window to access.
	 * @param hCtrl
	 *            The handle of the control to interact with.
	 * @param item
	 *            The "item" parameter is a string-based parameter that is used
	 *            to reference a particular treeview item using a combination of
	 *            text and indices. Indices are 0-based. For example:<br/>
	 *            Heading1<br/>
	 *            ----> H1SubItem1<br/>
	 *            ----> H1SubItem2<br/>
	 *            ----> H1SubItem3<br/>
	 *            ----> ----> H1S1SubItem1<br/>
	 *            Heading2<br/>
	 *            Heading3<br/>
	 * 
	 *            Each "level" is separated by |. An index is preceded with #.
	 *            Examples:<br/>
	 * 
	 *            <table>
	 *            <thead>
	 *            <tr>
	 *            <td>Item</td>
	 *            <td>Item Reference</td>
	 *            </tr>
	 *            </thead>
	 *            <tr>
	 *            <td>Heading2</td>
	 *            <td>"Heading2" or "#1"</td>
	 *            </tr>
	 *            <tr>
	 *            <td>H1SubItem2</td>
	 *            <td>"Heading1|H1SubItem2" or "#0|#1"</td>
	 *            </tr>
	 *            <tr>
	 *            <td>H1S1SubItem1</td>
	 *            <td>"Heading1|H1SubItem3|H1S1SubItem1" or "#0|#2|#0"</td>
	 *            </tr>
	 *            </table>
	 * 
	 *            References can also be mixed like "Heading1|#1".
	 * @return Returns true if item is collapsed, otherwise return false.
	 */
	public static boolean isCollapsed(final HWND hWnd, final HWND hCtrl,
			final String item) {
		return ((hWnd == null) || (hCtrl == null)) ? false : isCollapsed(
				buildTitle(hWnd), buildControlId(hCtrl), item);
	}

	/**
	 * Checks to see if the item expanded.
	 * 
	 * @param title
	 *            The title of the window to access.
	 * @param control
	 *            The control to interact with.
	 * @param item
	 *            The "item" parameter is a string-based parameter that is used
	 *            to reference a particular treeview item using a combination of
	 *            text and indices. Indices are 0-based. For example:<br/>
	 *            Heading1<br/>
	 *            ----> H1SubItem1<br/>
	 *            ----> H1SubItem2<br/>
	 *            ----> H1SubItem3<br/>
	 *            ----> ----> H1S1SubItem1<br/>
	 *            Heading2<br/>
	 *            Heading3<br/>
	 * 
	 *            Each "level" is separated by |. An index is preceded with #.
	 *            Examples:<br/>
	 * 
	 *            <table>
	 *            <thead>
	 *            <tr>
	 *            <td>Item</td>
	 *            <td>Item Reference</td>
	 *            </tr>
	 *            </thead>
	 *            <tr>
	 *            <td>Heading2</td>
	 *            <td>"Heading2" or "#1"</td>
	 *            </tr>
	 *            <tr>
	 *            <td>H1SubItem2</td>
	 *            <td>"Heading1|H1SubItem2" or "#0|#1"</td>
	 *            </tr>
	 *            <tr>
	 *            <td>H1S1SubItem1</td>
	 *            <td>"Heading1|H1SubItem3|H1S1SubItem1" or "#0|#2|#0"</td>
	 *            </tr>
	 *            </table>
	 * 
	 *            References can also be mixed like "Heading1|#1".
	 * @return Returns true if item is expanded, otherwise return false.
	 */
	public static boolean isExpanded(final String title, final String control,
			final String item) {
		return isExpanded(title, null, control, item);
	}

	/**
	 * Checks to see if the item expanded.
	 * 
	 * @param title
	 *            The title of the window to access.
	 * @param text
	 *            The text of the window to access.
	 * @param control
	 *            The control to interact with.
	 * @param item
	 *            The "item" parameter is a string-based parameter that is used
	 *            to reference a particular treeview item using a combination of
	 *            text and indices. Indices are 0-based. For example:<br/>
	 *            Heading1<br/>
	 *            ----> H1SubItem1<br/>
	 *            ----> H1SubItem2<br/>
	 *            ----> H1SubItem3<br/>
	 *            ----> ----> H1S1SubItem1<br/>
	 *            Heading2<br/>
	 *            Heading3<br/>
	 * 
	 *            Each "level" is separated by |. An index is preceded with #.
	 *            Examples:<br/>
	 * 
	 *            <table>
	 *            <thead>
	 *            <tr>
	 *            <td>Item</td>
	 *            <td>Item Reference</td>
	 *            </tr>
	 *            </thead>
	 *            <tr>
	 *            <td>Heading2</td>
	 *            <td>"Heading2" or "#1"</td>
	 *            </tr>
	 *            <tr>
	 *            <td>H1SubItem2</td>
	 *            <td>"Heading1|H1SubItem2" or "#0|#1"</td>
	 *            </tr>
	 *            <tr>
	 *            <td>H1S1SubItem1</td>
	 *            <td>"Heading1|H1SubItem3|H1S1SubItem1" or "#0|#2|#0"</td>
	 *            </tr>
	 *            </table>
	 * 
	 *            References can also be mixed like "Heading1|#1".
	 * @return Returns true if item is expanded, otherwise return false.
	 */
	public static boolean isExpanded(final String title, final String text,
			final String control, final String item) {
		boolean expanded = false;
		HWND hWnd = Control.getHandle_(title, text, control);
		if (hWnd != null) {
			HWND itemHWND = getHandle(title, text, control, item);
			if (itemHWND != null) {
				TVITEM treeViewItem = new TVITEM();
				treeViewItem.mask = new UINT(TVIF_STATE);
				treeViewItem.hItem = (int) Pointer.nativeValue(itemHWND
						.getPointer());
				Win32.user32.SendMessage(hWnd, TVM_GETITEMW, new WPARAM(0),
						treeViewItem);
				expanded = ((treeViewItem.state.intValue() & TVIS_EXPANDED) != 0);
			}
		}
		return expanded;
	}

	/**
	 * Checks to see if the item expanded.
	 * 
	 * @param hWnd
	 *            The handle of the window to access.
	 * @param hCtrl
	 *            The handle of the control to interact with.
	 * @param item
	 *            The "item" parameter is a string-based parameter that is used
	 *            to reference a particular treeview item using a combination of
	 *            text and indices. Indices are 0-based. For example:<br/>
	 *            Heading1<br/>
	 *            ----> H1SubItem1<br/>
	 *            ----> H1SubItem2<br/>
	 *            ----> H1SubItem3<br/>
	 *            ----> ----> H1S1SubItem1<br/>
	 *            Heading2<br/>
	 *            Heading3<br/>
	 * 
	 *            Each "level" is separated by |. An index is preceded with #.
	 *            Examples:<br/>
	 * 
	 *            <table>
	 *            <thead>
	 *            <tr>
	 *            <td>Item</td>
	 *            <td>Item Reference</td>
	 *            </tr>
	 *            </thead>
	 *            <tr>
	 *            <td>Heading2</td>
	 *            <td>"Heading2" or "#1"</td>
	 *            </tr>
	 *            <tr>
	 *            <td>H1SubItem2</td>
	 *            <td>"Heading1|H1SubItem2" or "#0|#1"</td>
	 *            </tr>
	 *            <tr>
	 *            <td>H1S1SubItem1</td>
	 *            <td>"Heading1|H1SubItem3|H1S1SubItem1" or "#0|#2|#0"</td>
	 *            </tr>
	 *            </table>
	 * 
	 *            References can also be mixed like "Heading1|#1".
	 * @return Returns true if item is expanded, otherwise return false.
	 */
	public static boolean isExpanded(final HWND hWnd, final HWND hCtrl,
			final String item) {
		return ((hWnd == null) || (hCtrl == null)) ? false : isExpanded(
				buildTitle(hWnd), buildControlId(hCtrl), item);
	}

	/**
	 * Checks to see if a specified item is currently selected.
	 * 
	 * @param title
	 *            The title of the window to access.
	 * @param control
	 *            The control to interact with.
	 * @param item
	 *            The "item" parameter is a string-based parameter that is used
	 *            to reference a particular treeview item using a combination of
	 *            text and indices. Indices are 0-based. For example:<br/>
	 *            Heading1<br/>
	 *            ----> H1SubItem1<br/>
	 *            ----> H1SubItem2<br/>
	 *            ----> H1SubItem3<br/>
	 *            ----> ----> H1S1SubItem1<br/>
	 *            Heading2<br/>
	 *            Heading3<br/>
	 * 
	 *            Each "level" is separated by |. An index is preceded with #.
	 *            Examples:<br/>
	 * 
	 *            <table>
	 *            <thead>
	 *            <tr>
	 *            <td>Item</td>
	 *            <td>Item Reference</td>
	 *            </tr>
	 *            </thead>
	 *            <tr>
	 *            <td>Heading2</td>
	 *            <td>"Heading2" or "#1"</td>
	 *            </tr>
	 *            <tr>
	 *            <td>H1SubItem2</td>
	 *            <td>"Heading1|H1SubItem2" or "#0|#1"</td>
	 *            </tr>
	 *            <tr>
	 *            <td>H1S1SubItem1</td>
	 *            <td>"Heading1|H1SubItem3|H1S1SubItem1" or "#0|#2|#0"</td>
	 *            </tr>
	 *            </table>
	 * 
	 *            References can also be mixed like "Heading1|#1".
	 * @return Returns true if item is selected, otherwise return false.
	 */
	public static boolean isSelected(final String title, final String control,
			final String item) {
		return isSelected(title, null, control, item);
	}

	/**
	 * Checks to see if a specified item is currently selected.
	 * 
	 * @param title
	 *            The title of the window to access.
	 * @param text
	 *            The text of the window to access.
	 * @param control
	 *            The control to interact with.
	 * @param item
	 *            The "item" parameter is a string-based parameter that is used
	 *            to reference a particular treeview item using a combination of
	 *            text and indices. Indices are 0-based. For example:<br/>
	 *            Heading1<br/>
	 *            ----> H1SubItem1<br/>
	 *            ----> H1SubItem2<br/>
	 *            ----> H1SubItem3<br/>
	 *            ----> ----> H1S1SubItem1<br/>
	 *            Heading2<br/>
	 *            Heading3<br/>
	 * 
	 *            Each "level" is separated by |. An index is preceded with #.
	 *            Examples:<br/>
	 * 
	 *            <table>
	 *            <thead>
	 *            <tr>
	 *            <td>Item</td>
	 *            <td>Item Reference</td>
	 *            </tr>
	 *            </thead>
	 *            <tr>
	 *            <td>Heading2</td>
	 *            <td>"Heading2" or "#1"</td>
	 *            </tr>
	 *            <tr>
	 *            <td>H1SubItem2</td>
	 *            <td>"Heading1|H1SubItem2" or "#0|#1"</td>
	 *            </tr>
	 *            <tr>
	 *            <td>H1S1SubItem1</td>
	 *            <td>"Heading1|H1SubItem3|H1S1SubItem1" or "#0|#2|#0"</td>
	 *            </tr>
	 *            </table>
	 * 
	 *            References can also be mixed like "Heading1|#1".
	 * @return Returns true if item is selected, otherwise return false.
	 */
	public static boolean isSelected(final String title, final String text,
			final String control, final String item) {
		boolean selected = false;
		if (StringUtils.isNotEmpty(item)) {
			String selectedIndexReference = getSelected(title, text, control,
					true);
			if (selectedIndexReference != null) {
				String[] indexReferences = StringUtils.split(
						selectedIndexReference, '|');
				String[] textIndexReferences = StringUtils.split(item, '|');
				// all index or text are equal
				if (indexReferences.length == textIndexReferences.length) {
					boolean allEquals = true;
					for (int i = 0; i < indexReferences.length; i++) {
						if (!textIndexReferences[i].equals(indexReferences[i])
								&& !textIndexReferences[i].equals(getText(
										title, text, control, StringUtils.join(
												ArrayUtils.subarray(
														indexReferences, 0,
														i + 1), '|')))) {
							allEquals = false;
							break;
						}
					}
					if (allEquals) {
						selected = true;
					}
				}
			}
		}
		return selected;
	}

	/**
	 * Checks to see if a specified item is currently selected.
	 * 
	 * @param hWnd
	 *            The handle of the window to access.
	 * @param hCtrl
	 *            The handle of the control to interact with.
	 * @param item
	 *            The "item" parameter is a string-based parameter that is used
	 *            to reference a particular treeview item using a combination of
	 *            text and indices. Indices are 0-based. For example:<br/>
	 *            Heading1<br/>
	 *            ----> H1SubItem1<br/>
	 *            ----> H1SubItem2<br/>
	 *            ----> H1SubItem3<br/>
	 *            ----> ----> H1S1SubItem1<br/>
	 *            Heading2<br/>
	 *            Heading3<br/>
	 * 
	 *            Each "level" is separated by |. An index is preceded with #.
	 *            Examples:<br/>
	 * 
	 *            <table>
	 *            <thead>
	 *            <tr>
	 *            <td>Item</td>
	 *            <td>Item Reference</td>
	 *            </tr>
	 *            </thead>
	 *            <tr>
	 *            <td>Heading2</td>
	 *            <td>"Heading2" or "#1"</td>
	 *            </tr>
	 *            <tr>
	 *            <td>H1SubItem2</td>
	 *            <td>"Heading1|H1SubItem2" or "#0|#1"</td>
	 *            </tr>
	 *            <tr>
	 *            <td>H1S1SubItem1</td>
	 *            <td>"Heading1|H1SubItem3|H1S1SubItem1" or "#0|#2|#0"</td>
	 *            </tr>
	 *            </table>
	 * 
	 *            References can also be mixed like "Heading1|#1".
	 * @return Returns true if item is selected, otherwise return false.
	 */
	public static boolean isSelected(final HWND hWnd, final HWND hCtrl,
			final String item) {
		return ((hWnd == null) || (hCtrl == null)) ? false : isSelected(
				buildTitle(hWnd), buildControlId(hCtrl), item);
	}

	/**
	 * Checks to see if a specified item is a leaf node.
	 * 
	 * @param title
	 *            The title of the window to access.
	 * @param control
	 *            The control to interact with.
	 * @param item
	 *            The "item" parameter is a string-based parameter that is used
	 *            to reference a particular treeview item using a combination of
	 *            text and indices. Indices are 0-based. For example:<br/>
	 *            Heading1<br/>
	 *            ----> H1SubItem1<br/>
	 *            ----> H1SubItem2<br/>
	 *            ----> H1SubItem3<br/>
	 *            ----> ----> H1S1SubItem1<br/>
	 *            Heading2<br/>
	 *            Heading3<br/>
	 * 
	 *            Each "level" is separated by |. An index is preceded with #.
	 *            Examples:<br/>
	 * 
	 *            <table>
	 *            <thead>
	 *            <tr>
	 *            <td>Item</td>
	 *            <td>Item Reference</td>
	 *            </tr>
	 *            </thead>
	 *            <tr>
	 *            <td>Heading2</td>
	 *            <td>"Heading2" or "#1"</td>
	 *            </tr>
	 *            <tr>
	 *            <td>H1SubItem2</td>
	 *            <td>"Heading1|H1SubItem2" or "#0|#1"</td>
	 *            </tr>
	 *            <tr>
	 *            <td>H1S1SubItem1</td>
	 *            <td>"Heading1|H1SubItem3|H1S1SubItem1" or "#0|#2|#0"</td>
	 *            </tr>
	 *            </table>
	 * 
	 *            References can also be mixed like "Heading1|#1".
	 * @return Returns true if item is a leaf node, otherwise return false.
	 */
	public static boolean isLeaf(final String title, final String control,
			final String item) {
		return isLeaf(title, null, control, item);
	}

	/**
	 * Checks to see if a specified item is a leaf node.
	 * 
	 * @param title
	 *            The title of the window to access.
	 * @param text
	 *            The text of the window to access.
	 * @param control
	 *            The control to interact with.
	 * @param item
	 *            The "item" parameter is a string-based parameter that is used
	 *            to reference a particular treeview item using a combination of
	 *            text and indices. Indices are 0-based. For example:<br/>
	 *            Heading1<br/>
	 *            ----> H1SubItem1<br/>
	 *            ----> H1SubItem2<br/>
	 *            ----> H1SubItem3<br/>
	 *            ----> ----> H1S1SubItem1<br/>
	 *            Heading2<br/>
	 *            Heading3<br/>
	 * 
	 *            Each "level" is separated by |. An index is preceded with #.
	 *            Examples:<br/>
	 * 
	 *            <table>
	 *            <thead>
	 *            <tr>
	 *            <td>Item</td>
	 *            <td>Item Reference</td>
	 *            </tr>
	 *            </thead>
	 *            <tr>
	 *            <td>Heading2</td>
	 *            <td>"Heading2" or "#1"</td>
	 *            </tr>
	 *            <tr>
	 *            <td>H1SubItem2</td>
	 *            <td>"Heading1|H1SubItem2" or "#0|#1"</td>
	 *            </tr>
	 *            <tr>
	 *            <td>H1S1SubItem1</td>
	 *            <td>"Heading1|H1SubItem3|H1S1SubItem1" or "#0|#2|#0"</td>
	 *            </tr>
	 *            </table>
	 * 
	 *            References can also be mixed like "Heading1|#1".
	 * @return Returns true if item is a leaf node, otherwise return false.
	 */
	public static boolean isLeaf(final String title, final String text,
			final String control, final String item) {
		Integer itemCount = getItemCount(title, text, control, item);
		return (itemCount != null) && (itemCount == 0);
	}

	/**
	 * Checks to see if a specified item is a leaf node.
	 * 
	 * @param hWnd
	 *            The handle of the window to access.
	 * @param hCtrl
	 *            The handle of the control to interact with.
	 * @param item
	 *            The "item" parameter is a string-based parameter that is used
	 *            to reference a particular treeview item using a combination of
	 *            text and indices. Indices are 0-based. For example:<br/>
	 *            Heading1<br/>
	 *            ----> H1SubItem1<br/>
	 *            ----> H1SubItem2<br/>
	 *            ----> H1SubItem3<br/>
	 *            ----> ----> H1S1SubItem1<br/>
	 *            Heading2<br/>
	 *            Heading3<br/>
	 * 
	 *            Each "level" is separated by |. An index is preceded with #.
	 *            Examples:<br/>
	 * 
	 *            <table>
	 *            <thead>
	 *            <tr>
	 *            <td>Item</td>
	 *            <td>Item Reference</td>
	 *            </tr>
	 *            </thead>
	 *            <tr>
	 *            <td>Heading2</td>
	 *            <td>"Heading2" or "#1"</td>
	 *            </tr>
	 *            <tr>
	 *            <td>H1SubItem2</td>
	 *            <td>"Heading1|H1SubItem2" or "#0|#1"</td>
	 *            </tr>
	 *            <tr>
	 *            <td>H1S1SubItem1</td>
	 *            <td>"Heading1|H1SubItem3|H1S1SubItem1" or "#0|#2|#0"</td>
	 *            </tr>
	 *            </table>
	 * 
	 *            References can also be mixed like "Heading1|#1".
	 * @return Returns true if item is a leaf node, otherwise return false.
	 */
	public static boolean isLeaf(final HWND hWnd, final HWND hCtrl,
			final String item) {
		return ((hWnd == null) || (hCtrl == null)) ? false : isLeaf(
				buildTitle(hWnd), buildControlId(hCtrl), item);
	}

	/**
	 * Selects an item.
	 * 
	 * @param title
	 *            The title of the window to access.
	 * @param control
	 *            The control to interact with.
	 * @param item
	 *            The item which will be expanded.<br/>
	 *            The item which will be checked(if the item supports it).<br/>
	 *            The "item" parameter is a string-based parameter that is used
	 *            to reference a particular treeview item using a combination of
	 *            text and indices. Indices are 0-based. For example:<br/>
	 *            Heading1<br/>
	 *            ----> H1SubItem1<br/>
	 *            ----> H1SubItem2<br/>
	 *            ----> H1SubItem3<br/>
	 *            ----> ----> H1S1SubItem1<br/>
	 *            Heading2<br/>
	 *            Heading3<br/>
	 * 
	 *            Each "level" is separated by |. An index is preceded with #.
	 *            Examples:<br/>
	 * 
	 *            <table>
	 *            <thead>
	 *            <tr>
	 *            <td>Item</td>
	 *            <td>Item Reference</td>
	 *            </tr>
	 *            </thead>
	 *            <tr>
	 *            <td>Heading2</td>
	 *            <td>"Heading2" or "#1"</td>
	 *            </tr>
	 *            <tr>
	 *            <td>H1SubItem2</td>
	 *            <td>"Heading1|H1SubItem2" or "#0|#1"</td>
	 *            </tr>
	 *            <tr>
	 *            <td>H1S1SubItem1</td>
	 *            <td>"Heading1|H1SubItem3|H1S1SubItem1" or "#0|#2|#0"</td>
	 *            </tr>
	 *            </table>
	 * 
	 *            References can also be mixed like "Heading1|#1".
	 * @return Return false if window/control could not be found, otherwise
	 *         return true.
	 */
	public static boolean select(final String title, final String control,
			final String item) {
		return select(title, null, control, item);
	}

	/**
	 * Selects an item.
	 * 
	 * @param title
	 *            The title of the window to access.
	 * @param text
	 *            The text of the window to access.
	 * @param control
	 *            The control to interact with.
	 * @param item
	 *            The item which will be expanded.<br/>
	 *            The item which will be checked(if the item supports it).<br/>
	 *            The "item" parameter is a string-based parameter that is used
	 *            to reference a particular treeview item using a combination of
	 *            text and indices. Indices are 0-based. For example:<br/>
	 *            Heading1<br/>
	 *            ----> H1SubItem1<br/>
	 *            ----> H1SubItem2<br/>
	 *            ----> H1SubItem3<br/>
	 *            ----> ----> H1S1SubItem1<br/>
	 *            Heading2<br/>
	 *            Heading3<br/>
	 * 
	 *            Each "level" is separated by |. An index is preceded with #.
	 *            Examples:<br/>
	 * 
	 *            <table>
	 *            <thead>
	 *            <tr>
	 *            <td>Item</td>
	 *            <td>Item Reference</td>
	 *            </tr>
	 *            </thead>
	 *            <tr>
	 *            <td>Heading2</td>
	 *            <td>"Heading2" or "#1"</td>
	 *            </tr>
	 *            <tr>
	 *            <td>H1SubItem2</td>
	 *            <td>"Heading1|H1SubItem2" or "#0|#1"</td>
	 *            </tr>
	 *            <tr>
	 *            <td>H1S1SubItem1</td>
	 *            <td>"Heading1|H1SubItem3|H1S1SubItem1" or "#0|#2|#0"</td>
	 *            </tr>
	 *            </table>
	 * 
	 *            References can also be mixed like "Heading1|#1".
	 * @return Return false if window/control could not be found, otherwise
	 *         return true.
	 */
	public static boolean select(final String title, final String text,
			final String control, final String item) {
		boolean status = false;
		if (StringUtils.isNotEmpty(item)) {
			controlTreeView(title, text, control, COMMAND_SELECT, item, null, 0);
			status = !hasError();
		}
		return status;
	}

	/**
	 * Selects an item.
	 * 
	 * @param hWnd
	 *            The handle of the window to access.
	 * @param hCtrl
	 *            The handle of the control to interact with.
	 * @param item
	 *            The item which will be expanded.<br/>
	 *            The item which will be checked(if the item supports it).<br/>
	 *            The "item" parameter is a string-based parameter that is used
	 *            to reference a particular treeview item using a combination of
	 *            text and indices. Indices are 0-based. For example:<br/>
	 *            Heading1<br/>
	 *            ----> H1SubItem1<br/>
	 *            ----> H1SubItem2<br/>
	 *            ----> H1SubItem3<br/>
	 *            ----> ----> H1S1SubItem1<br/>
	 *            Heading2<br/>
	 *            Heading3<br/>
	 * 
	 *            Each "level" is separated by |. An index is preceded with #.
	 *            Examples:<br/>
	 * 
	 *            <table>
	 *            <thead>
	 *            <tr>
	 *            <td>Item</td>
	 *            <td>Item Reference</td>
	 *            </tr>
	 *            </thead>
	 *            <tr>
	 *            <td>Heading2</td>
	 *            <td>"Heading2" or "#1"</td>
	 *            </tr>
	 *            <tr>
	 *            <td>H1SubItem2</td>
	 *            <td>"Heading1|H1SubItem2" or "#0|#1"</td>
	 *            </tr>
	 *            <tr>
	 *            <td>H1S1SubItem1</td>
	 *            <td>"Heading1|H1SubItem3|H1S1SubItem1" or "#0|#2|#0"</td>
	 *            </tr>
	 *            </table>
	 * 
	 *            References can also be mixed like "Heading1|#1".
	 * @return Return false if window/control could not be found, otherwise
	 *         return true.
	 */
	public static boolean select(final HWND hWnd, final HWND hCtrl,
			final String item) {
		return ((hWnd == null) || (hCtrl == null)) ? false : select(
				buildTitle(hWnd), buildControlId(hCtrl), item);
	}

	/**
	 * Unchecks an item (if the item supports it).
	 * 
	 * @param title
	 *            The title of the window to access.
	 * @param control
	 *            The control to interact with.
	 * @param item
	 *            The item which will be unchecked(if the item supports it).<br/>
	 *            The "item" parameter is a string-based parameter that is used
	 *            to reference a particular treeview item using a combination of
	 *            text and indices. Indices are 0-based. For example:<br/>
	 *            Heading1<br/>
	 *            ----> H1SubItem1<br/>
	 *            ----> H1SubItem2<br/>
	 *            ----> H1SubItem3<br/>
	 *            ----> ----> H1S1SubItem1<br/>
	 *            Heading2<br/>
	 *            Heading3<br/>
	 * 
	 *            Each "level" is separated by |. An index is preceded with #.
	 *            Examples:<br/>
	 * 
	 *            <table>
	 *            <thead>
	 *            <tr>
	 *            <td>Item</td>
	 *            <td>Item Reference</td>
	 *            </tr>
	 *            </thead>
	 *            <tr>
	 *            <td>Heading2</td>
	 *            <td>"Heading2" or "#1"</td>
	 *            </tr>
	 *            <tr>
	 *            <td>H1SubItem2</td>
	 *            <td>"Heading1|H1SubItem2" or "#0|#1"</td>
	 *            </tr>
	 *            <tr>
	 *            <td>H1S1SubItem1</td>
	 *            <td>"Heading1|H1SubItem3|H1S1SubItem1" or "#0|#2|#0"</td>
	 *            </tr>
	 *            </table>
	 * 
	 *            References can also be mixed like "Heading1|#1".
	 * @return Return false if window/control could not be found, otherwise
	 *         return true.
	 */
	public static boolean uncheck(final String title, final String control,
			final String item) {
		return uncheck(title, null, control, item);
	}

	/**
	 * Unchecks an item (if the item supports it).
	 * 
	 * @param title
	 *            The title of the window to access.
	 * @param text
	 *            The text of the window to access.
	 * @param control
	 *            The control to interact with.
	 * @param item
	 *            The item which will be unchecked(if the item supports it).<br/>
	 *            The "item" parameter is a string-based parameter that is used
	 *            to reference a particular treeview item using a combination of
	 *            text and indices. Indices are 0-based. For example:<br/>
	 *            Heading1<br/>
	 *            ----> H1SubItem1<br/>
	 *            ----> H1SubItem2<br/>
	 *            ----> H1SubItem3<br/>
	 *            ----> ----> H1S1SubItem1<br/>
	 *            Heading2<br/>
	 *            Heading3<br/>
	 * 
	 *            Each "level" is separated by |. An index is preceded with #.
	 *            Examples:<br/>
	 * 
	 *            <table>
	 *            <thead>
	 *            <tr>
	 *            <td>Item</td>
	 *            <td>Item Reference</td>
	 *            </tr>
	 *            </thead>
	 *            <tr>
	 *            <td>Heading2</td>
	 *            <td>"Heading2" or "#1"</td>
	 *            </tr>
	 *            <tr>
	 *            <td>H1SubItem2</td>
	 *            <td>"Heading1|H1SubItem2" or "#0|#1"</td>
	 *            </tr>
	 *            <tr>
	 *            <td>H1S1SubItem1</td>
	 *            <td>"Heading1|H1SubItem3|H1S1SubItem1" or "#0|#2|#0"</td>
	 *            </tr>
	 *            </table>
	 * 
	 *            References can also be mixed like "Heading1|#1".
	 * @return Return false if window/control could not be found, otherwise
	 *         return true.
	 */
	public static boolean uncheck(final String title, final String text,
			final String control, final String item) {
		boolean status = true;

		if (StringUtils.isBlank(item)) {
			status = false;
		} else {
			IsChecked isChecked = TreeView.isChecked_(title, text, control,
					item);
			// if item is checked, then uncheck it
			if (isChecked == IsChecked.CHECKED) {
				// expand parent item before uncheck item, if parent item is not
				// expanded, AutoItX will block in uncheck item
				int index = 0;
				int index2 = item.indexOf("|", index);
				while (index2 > index) {
					if (!expand(title, text, control, item.substring(0, index2))) {
						status = false;
						break;
					}
					index = index2;
					index2 = item.indexOf("|", index + 1);
				}

				if (status) {
					controlTreeView(title, text, control, COMMAND_UNCHECK,
							item, null, 0);
					status = !hasError();
				}
			} else if (isChecked == IsChecked.NOT_A_CHECKBOX) {
				status = false;
			}
		}

		return status;
	}

	/**
	 * Unchecks an item (if the item supports it).
	 * 
	 * @param hWnd
	 *            The handle of the window to access.
	 * @param hCtrl
	 *            The handle of the control to interact with.
	 * @param item
	 *            The item which will be unchecked(if the item supports it).<br/>
	 *            The "item" parameter is a string-based parameter that is used
	 *            to reference a particular treeview item using a combination of
	 *            text and indices. Indices are 0-based. For example:<br/>
	 *            Heading1<br/>
	 *            ----> H1SubItem1<br/>
	 *            ----> H1SubItem2<br/>
	 *            ----> H1SubItem3<br/>
	 *            ----> ----> H1S1SubItem1<br/>
	 *            Heading2<br/>
	 *            Heading3<br/>
	 * 
	 *            Each "level" is separated by |. An index is preceded with #.
	 *            Examples:<br/>
	 * 
	 *            <table>
	 *            <thead>
	 *            <tr>
	 *            <td>Item</td>
	 *            <td>Item Reference</td>
	 *            </tr>
	 *            </thead>
	 *            <tr>
	 *            <td>Heading2</td>
	 *            <td>"Heading2" or "#1"</td>
	 *            </tr>
	 *            <tr>
	 *            <td>H1SubItem2</td>
	 *            <td>"Heading1|H1SubItem2" or "#0|#1"</td>
	 *            </tr>
	 *            <tr>
	 *            <td>H1S1SubItem1</td>
	 *            <td>"Heading1|H1SubItem3|H1S1SubItem1" or "#0|#2|#0"</td>
	 *            </tr>
	 *            </table>
	 * 
	 *            References can also be mixed like "Heading1|#1".
	 * @return Return false if window/control could not be found, otherwise
	 *         return true.
	 */
	public static boolean uncheck(final HWND hWnd, final HWND hCtrl,
			final String item) {
		return ((hWnd == null) || (hCtrl == null)) ? false : uncheck(
				buildTitle(hWnd), buildControlId(hCtrl), item);
	}

	/**
	 * Sends a command to a TreeView32 control.
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
	 * @param bufSize
	 * @return Depends on command as table below shows. In case of an error
	 *         (such as an invalid command or window/control could not be found)
	 *         then @error is set to 1.<br>
	 * <br>
	 * <br>
	 *         <table border="1" width="100%" cellspacing="0" cellpadding="3" bordercolor="#C0C0C0">
	 *         <tr>
	 *         <td width="40%"><b>Command, Option1, Option2</b></td>
	 *         <td width="60%"><b>Operation</b></td>
	 *         </tr>
	 *         <tr>
	 *         <td>"Check", "item"</td>
	 *         <td>Checks an item (if the item supports it).</td>
	 *         </tr>
	 *         <tr>
	 *         <td>"Collapse", "item"</td>
	 *         <td>Collapses an item to hide its children.</td>
	 *         </tr>
	 *         <tr>
	 *         <td>"Exists", "item"</td>
	 *         <td>Returns 1 if an item exists, otherwise 0.</td>
	 *         </tr>
	 *         <tr>
	 *         <td>"Expand", "item"</td>
	 *         <td>Expands an item to show its children.</td>
	 *         </tr>
	 *         <tr>
	 *         <td>"GetItemCount", "item"</td>
	 *         <td>Returns the number of children for a selected item.</td>
	 *         </tr>
	 *         <tr>
	 *         <td>"GetSelected" [, UseIndex]</td>
	 *         <td>Returns the item reference of the current selection using the
	 *         text reference of the item (or index reference if UseIndex is set
	 *         to 1).</td>
	 *         </tr>
	 *         <tr>
	 *         <td>"GetText", "item"</td>
	 *         <td>Returns the text of an item.</td>
	 *         </tr>
	 *         <tr>
	 *         <td>"IsChecked"</td>
	 *         <td>Returns the state of an item. 1:checked, 0:unchecked, -1:not
	 *         a checkbox.</td>
	 *         </tr>
	 *         <tr>
	 *         <td>"Select", "item"</td>
	 *         <td>Selects an item.</td>
	 *         </tr>
	 *         <tr>
	 *         <td>"Uncheck", "item"</td>
	 *         <td>Unchecks an item (if the item supports it).</td>
	 *         </tr>
	 *         </table>
	 */
	private static String controlTreeView(final String title,
			final String text, final String control, final String command,
			final String extra1, final String extra2, int bufSize) {
		// if bufSize is 0, there will be 'invalid memory access' error
		if (bufSize <= 0) {
			bufSize = 1;
		}
		final CharBuffer result = CharBuffer.allocate(bufSize);
		autoItX.AU3_ControlTreeView(stringToWString(defaultString(title)),
				stringToWString(text), stringToWString(defaultString(control)),
				stringToWString(defaultString(command)),
				stringToWString(defaultString(extra1)),
				stringToWString(defaultString(extra2)), result, bufSize);
		return hasError() ? "" : Native.toString(result.array());
	}

	private static HWND getFirstChildHandle(final String title,
			final String text, final String control, final HWND itemHWND) {
		HWND firstChildHWND = null;
		HWND hWnd = Control.getHandle_(title, text, control);
		if (hWnd != null) {
			firstChildHWND = Win32.user32.SendMessage(hWnd, TVM_GETNEXTITEM,
					new WPARAM(TVGN_CHILD), itemHWND);
		}
		return firstChildHWND;
	}

	// private static HWND getFirstChildHandle(final String title,
	// final String text, final String control, final String item) {
	// return getFirstChildHandle(title, text, control,
	// getHandle(title, text, control, item));
	// }

	private static HWND getFirstItemHandle(final String title,
			final String text, final String control) {
		HWND firstItemHWND = null;
		HWND hWnd = Control.getHandle_(title, text, control);
		if (hWnd != null) {
			firstItemHWND = Win32.user32.SendMessage(hWnd, TVM_GETNEXTITEM,
					new WPARAM(TVGN_ROOT), new LPARAM(0));
		}
		return firstItemHWND;
	}

	private static HWND getNextSiblingHandle(final String title,
			final String text, final String control, final HWND itemHWND) {
		HWND nextSiblingHWND = null;
		if (itemHWND != null) {
			HWND hWnd = Control.getHandle_(title, text, control);
			if (hWnd != null) {
				nextSiblingHWND = Win32.user32.SendMessage(hWnd,
						TVM_GETNEXTITEM, new WPARAM(TVGN_NEXT), itemHWND);
			}
		}
		return nextSiblingHWND;
	}

	// private static HWND getParentHandle(final String title, final String
	// text,
	// final String control, final HWND itemHWND) {
	// HWND parentHWND = null;
	// if (itemHWND != null) {
	// HWND hWnd = Control.getHandle_(title, text, control);
	// if (hWnd != null) {
	// parentHWND = Win32Utils.user32.SendMessage(hWnd,
	// TVM_GETNEXTITEM, new WPARAM(TVGN_PARENT), itemHWND);
	// }
	// }
	// return parentHWND;
	// }
	//
	// private static HWND getPreviousSiblingHandle(final String title,
	// final String text, final String control, final HWND itemHWND) {
	// HWND nextSiblingHWND = null;
	// if (itemHWND != null) {
	// HWND hWnd = Control.getHandle_(title, text, control);
	// if (hWnd != null) {
	// nextSiblingHWND = Win32Utils.user32.SendMessage(hWnd,
	// TVM_GETNEXTITEM, new WPARAM(TVGN_PREVIOUS), itemHWND);
	// }
	// }
	// return nextSiblingHWND;
	// }

	/**
	 * The state of an item.
	 * 
	 * @author zhengbo.wang
	 */
	public static enum IsChecked {
		/* checked */
		CHECKED(1),

		/* unchecked */
		UNCHECKED(0),

		/* not a checkbox */
		NOT_A_CHECKBOX(-1);

		private int status = 0;

		private IsChecked(final int status) {
			this.status = status;
		}

		public int getStatus() {
			return status;
		}

		@Override
		public String toString() {
			String result = null;
			switch (this) {
			case CHECKED:
				result = "checked";
				break;
			case UNCHECKED:
				result = "unchecked";
				break;
			default:
				result = "not a checkbox";
			}
			return result;
		}
	}

	/**
	 * This structure is used to specifies or receives attributes of a tree-view
	 * item.
	 * 
	 * @author zhengbo.wang
	 */
	public static class TVITEM extends Structure {
		private static final int TEXT_BUF_SIZE = 1024;
		public UINT mask;
		public int hItem;
		public UINT state = new UINT(0);
		public UINT stateMask;
		public Pointer pszText;
		public int cchTextMax;
		public int iImage;
		public int iSelectedImage;
		public int cChildren;
		public LPARAM lParam;

		public TVITEM() {
			this(TEXT_BUF_SIZE);
		}

		public TVITEM(int textBufSize) {
			cchTextMax = (textBufSize <= 0) ? TEXT_BUF_SIZE : textBufSize;
			pszText = new Memory(cchTextMax);
		}

		protected List<String> getFieldOrder() {
			return Arrays.asList(new String[] { "mask", "hItem", "state",
					"stateMask", "pszText", "cchTextMax", "iImage",
					"iSelectedImage", "cChildren", "lParam" });
		}
	}
}
