package cn.com.jautoitx;

import org.apache.commons.lang3.StringUtils;

import com.sun.jna.platform.win32.WinDef.HWND;

/**
 * Build window title base on Advanced Window Descriptions.
 * 
 * @author zhengbo.wang
 */
public class TitleBuilder {
	private TitleBuilder() {
		// Do nothing
	}

	/**
	 * Build window title base on Advanced Window Descriptions.
	 * 
	 * @param bys
	 *            Selectors to build advanced window title.
	 * @return Returns advanced window title.
	 */
	public static String by(By... bys) {
		StringBuilder title = new StringBuilder();

		title.append('[');
		String separator = "";
		for (int i = 0; i < bys.length; i++) {
			title.append(separator);

			String strBy = bys[i].toAdvancedTitle();
			if (StringUtils.isNotEmpty(strBy)) {
				title.append(strBy);
				separator = "; ";
			}
		}
		title.append(']');

		return title.toString();
	}

	/**
	 * Build window title base on window title.
	 * 
	 * @param title
	 *            Window title.
	 * @return Returns advanced window title.
	 */
	public static String byTitle(String title) {
		return by(By.title(title));
	}

	/**
	 * Build window title base on the internal window classname.
	 * 
	 * @param className
	 *            The internal window classname.
	 * @return Returns advanced window title.
	 */
	public static String byClassName(String className) {
		return by(By.className(className));
	}

	/**
	 * Build window title base on window title using a regular expression.
	 * 
	 * @param regexpTitle
	 *            Window title using a regular expression.
	 * @return Returns advanced window title.
	 */
	public static String byRegexpTitle(String regexpTitle) {
		return by(By.regexpTitle(regexpTitle));
	}

	/**
	 * Build window title base on window classname using a regular expression.
	 * 
	 * @param regexpClassName
	 *            Window classname using a regular expression.
	 * @return Returns advanced window title.
	 */
	public static String byRegexpClassName(String regexpClassName) {
		return by(By.regexpClassName(regexpClassName));
	}

	/**
	 * Build window title base on window used in a previous AutoIt command.
	 * 
	 * @return Returns advanced window title.
	 */
	public static String byLastWindow() {
		return by(By.lastWindow());
	}

	/**
	 * Build window title base on currently active window.
	 * 
	 * @return Returns advanced window title.
	 */
	public static String byActiveWindow() {
		return by(By.activeWindow());
	}

	/**
	 * Build window title base on the position and size of a window. All
	 * parameters are optional.
	 * 
	 * @param x
	 *            Optional, the X coordinate of the window.
	 * @param y
	 *            Optional, the Y coordinate of the window.
	 * @param width
	 *            Optional, the width of the window.
	 * @param height
	 *            Optional, the height of the window.
	 * @return Returns advanced window title.
	 */
	public static String byBounds(Integer x, Integer y, Integer width,
			Integer height) {
		return by(By.bounds(x, y, width, height));
	}

	/**
	 * Build window title base on the position of a window. All parameters are
	 * optional.
	 * 
	 * @param x
	 *            Optional, the X coordinate of the window.
	 * @param y
	 *            Optional, the Y coordinate of the window.
	 * @return Returns advanced window title.
	 */
	public static String byPosition(Integer x, Integer y) {
		return by(By.position(x, y));
	}

	/**
	 * Build window title base on the size of a window. All parameters are
	 * optional.
	 * 
	 * @param width
	 *            Optional, the width of the window.
	 * @param height
	 *            Optional, the height of the window.
	 * @return Returns advanced window title.
	 */
	public static String bySize(Integer width, Integer height) {
		return by(By.size(width, height));
	}

	/**
	 * Build window title base on the 1-based instance when all given properties
	 * match.
	 * 
	 * @param instance
	 *            The 1-based instance when all given properties match.
	 * @return Returns advanced window title.
	 */
	public static String byInstance(int instance) {
		return by(By.instance(instance));
	}

	/**
	 * Build window title base on the handle address as returned by a method
	 * like Win.getHandle.
	 * 
	 * @param handle
	 *            The handle address as returned by a method like Win.getHandle.
	 * @return Returns advanced window title.
	 */
	public static String byHandle(String handle) {
		return by(By.handle(handle));
	}

	/**
	 * Build window title base on the handle address as returned by a method
	 * like Win.getHandle_.
	 * 
	 * @param hWnd
	 *            The handle address as returned by a method like
	 *            Win.getHandle_.
	 * @return Returns advanced window title.
	 */
	public static String byHandle(HWND hWnd) {
		return by(By.handle(hWnd));
	}

	/**
	 * Selector to build advanced window title.
	 * 
	 * @author zhengbo.wang
	 */
	public static abstract class By {
		private final String property;
		private final String value;

		public By(final String property) {
			this.property = property;
			this.value = null;
		}

		public By(final String property, final String value) {
			this.property = property;
			this.value = StringUtils.defaultString(value);
		}

		/**
		 * @param title
		 *            Window title.
		 * @return a By which locates window by the window title.
		 */
		public static By title(String title) {
			return new ByTitle(title);
		}

		/**
		 * @param className
		 *            The internal window classname.
		 * @return a By which locates window by the internal window classname.
		 */
		public static By className(String className) {
			return new ByClass(className);
		}

		/**
		 * @param regexpTitle
		 *            Window title using a regular expression.
		 * @return a By which locates window by the window title using a regular
		 *         expression.
		 */
		public static By regexpTitle(String regexpTitle) {
			return new ByRegexpTitle(regexpTitle);
		}

		/**
		 * @param regexpClassName
		 *            Window classname using a regular expression.
		 * @return a By which locates window by the window classname using a
		 *         regular expression.
		 */
		public static By regexpClassName(String regexpClassName) {
			return new ByRegexpClass(regexpClassName);
		}

		/**
		 * @return a By which locates window used in a previous AutoIt command.
		 */
		public static By lastWindow() {
			return new ByLast();
		}

		/**
		 * @return a By which locates currently active window.
		 */
		public static By activeWindow() {
			return new ByActive();
		}

		/**
		 * All parameters are optional.
		 * 
		 * @param x
		 *            Optional, the X coordinate of the window.
		 * @param y
		 *            Optional, the Y coordinate of the window.
		 * @param width
		 *            Optional, the width of the window.
		 * @param height
		 *            Optional, the height of the window.
		 * @return a By which locates window by the position and size of a
		 *         window.
		 */
		public static By bounds(Integer x, Integer y, Integer width,
				Integer height) {
			return new ByBounds(x, y, width, height);
		}

		/**
		 * All parameters are optional.
		 * 
		 * @param x
		 *            Optional, the X coordinate of the window.
		 * @param y
		 *            Optional, the Y coordinate of the window.
		 * @return a By which locates window by the position of a window.
		 */
		public static By position(Integer x, Integer y) {
			return bounds(x, y, null, null);
		}

		/**
		 * All parameters are optional.
		 * 
		 * @param width
		 *            Optional, the width of the window.
		 * @param height
		 *            Optional, the height of the window.
		 * @return a By which locates window by the size of a window.
		 */
		public static By size(Integer width, Integer height) {
			return bounds(null, null, width, height);
		}

		/**
		 * @param instance
		 *            The 1-based instance when all given properties match.
		 * @return a By which locates window by the instance when all given
		 *         properties match.
		 */
		public static By instance(int instance) {
			return new ByInstance(instance);
		}

		/**
		 * @param handle
		 *            The handle address as returned by a method like
		 *            Win.getHandle.
		 * @return a By which locates window by the handle address as returned
		 *         by a method like Win.getHandle.
		 */
		public static By handle(String handle) {
			return new ByHandle(handle);
		}

		/**
		 * @param hWnd
		 *            The handle address as returned by a method like
		 *            Win.getHandle_.
		 * @return a By which locates window by the handle address as returned
		 *         by a method like Win.getHandle.
		 */
		public static By handle(HWND hWnd) {
			return new ByHandle(hWnd);
		}

		public String toAdvancedTitle() {
			StringBuilder advancedTitle = new StringBuilder();
			advancedTitle.append(property);
			if (value != null) {
				advancedTitle.append(':');
				for (int i = 0; i < value.length(); i++) {
					char ch = value.charAt(i);
					advancedTitle.append(ch);
					// Note: if a Value must contain a ";" it must be doubled.
					if (ch == ';') {
						advancedTitle.append(';');
					}
				}
			}
			return advancedTitle.toString();
		}

		@Override
		public String toString() {
			return "By." + property + ": " + value;
		}
	}

	/**
	 * Window title.
	 * 
	 * @author zhengbo.wang
	 */
	public static class ByTitle extends By {
		public ByTitle(String title) {
			super("TITLE", title);
		}
	}

	/**
	 * The internal window classname.
	 * 
	 * @author zhengbo.wang
	 */
	public static class ByClass extends By {
		public ByClass(String clazz) {
			super("CLASS", clazz);
		}
	}

	/**
	 * Window title using a regular expression.
	 * 
	 * @author zhengbo.wang
	 */
	public static class ByRegexpTitle extends By {
		public ByRegexpTitle(String clazz) {
			super("REGEXPTITLE", clazz);
		}
	}

	/**
	 * Window classname using a regular expression.
	 * 
	 * @author zhengbo.wang
	 */
	public static class ByRegexpClass extends By {
		public ByRegexpClass(String regexpClass) {
			super("REGEXPCLASS", regexpClass);
		}
	}

	/**
	 * Last window used in a previous AutoIt command.
	 * 
	 * @author zhengbo.wang
	 */
	public static class ByLast extends By {
		public ByLast() {
			super("LAST");
		}
	}

	/**
	 * Currently active window.
	 * 
	 * @author zhengbo.wang
	 */
	public static class ByActive extends By {
		public ByActive() {
			super("ACTIVE");
		}
	}

	/**
	 * The position and size of a window.
	 * 
	 * @author zhengbo.wang
	 */
	public static class ByBounds extends By {
		private final Integer x;
		private final Integer y;
		private final Integer width;
		private final Integer height;

		public ByBounds(Integer x, Integer y, Integer width, Integer height) {
			super("POSITION AND SIZE", String.format("%s \\ %s \\ %s \\ %s",
					String.valueOf(x), String.valueOf(y),
					String.valueOf(width), String.valueOf(height)));
			this.x = x;
			this.y = y;
			this.width = width;
			this.height = height;
		}

		@Override
		public String toAdvancedTitle() {
			// see http://www.autoitscript.com/forum/topic/90848-x-y-w-h/
			StringBuilder advancedTitle = new StringBuilder();
			if (x != null) {
				advancedTitle.append("X:").append(x);
			}
			if (y != null) {
				if (StringUtils.isNotEmpty(advancedTitle.toString())) {
					advancedTitle.append("\\");
				}
				advancedTitle.append("Y:").append(y);
			}
			if (width != null) {
				if (StringUtils.isNotEmpty(advancedTitle.toString())) {
					advancedTitle.append("\\");
				}
				advancedTitle.append("W:").append(width);
			}
			if (height != null) {
				if (StringUtils.isNotEmpty(advancedTitle.toString())) {
					advancedTitle.append("\\");
				}
				advancedTitle.append("H:").append(height);
			}
			return advancedTitle.toString();
		}
	}

	/**
	 * The 1-based instance when all given properties match.
	 * 
	 * @author zhengbo.wang
	 */
	public static class ByInstance extends By {
		public ByInstance(int instance) {
			super("INSTANCE", String.valueOf(instance));
		}
	}

	/**
	 * The handle address as returned by a method like Win.getHandle or
	 * Win.getHandle_.
	 * 
	 * @author zhengbo.wang
	 */
	public static class ByHandle extends By {
		public ByHandle(String handle) {
			super("HANDLE", handle);
		}

		public ByHandle(HWND hWnd) {
			this(AutoItX.hwndToHandle(hWnd));
		}
	}
}
