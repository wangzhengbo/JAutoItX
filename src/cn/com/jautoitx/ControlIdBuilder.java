package cn.com.jautoitx;

import org.apache.commons.lang3.StringUtils;

import com.sun.jna.platform.win32.WinDef.HWND;

/**
 * Build control id base on Advanced Control Descriptions.
 * 
 * @author zhengbo.wang
 */
public class ControlIdBuilder {
	private ControlIdBuilder() {
		// Do nothing
	}

	/**
	 * Build control id base on Advanced Control Descriptions.
	 * 
	 * @param bys
	 *            Selectors to build advanced control id.
	 * @return Returns advanced control id.
	 */
	public static String by(By... bys) {
		StringBuilder controlId = new StringBuilder();

		controlId.append('[');
		String separator = "";
		for (int i = 0; i < bys.length; i++) {
			controlId.append(separator);

			String strBy = bys[i].toAdvancedControlId();
			if (StringUtils.isNotEmpty(strBy)) {
				controlId.append(strBy);
				separator = "; ";
			}
		}
		controlId.append(']');

		return controlId.toString();
	}

	/**
	 * Build control id base on the internal control ID.
	 * 
	 * @param id
	 *            The internal control ID. The Control ID is the internal
	 *            numeric identifier that windows gives to each control. It is
	 *            generally the best method of identifying controls. In addition
	 *            to the AutoIt Window Info Tool, other applications such as
	 *            screenreaders for the blind and Microsoft tools/APIs may allow
	 *            you to get this Control ID.
	 * @return Returns advanced control id.
	 */
	public static String byId(int id) {
		return by(By.id(id));
	}

	/**
	 * Build control id base on the text on a control.
	 * 
	 * @param text
	 *            The text on a control, for example "&Next" on a button.
	 * @return a By which locates control by the text on a control.
	 * @return Returns advanced control id.
	 */
	public static String byText(String text) {
		return by(By.text(text));
	}

	/**
	 * Build control id base on the internal control classname.
	 * 
	 * @param className
	 *            The internal control classname such as "Edit" or "Button".
	 * @return Returns advanced control id.
	 */
	public static String byClassName(String className) {
		return by(By.className(className));
	}

	/**
	 * Build control id base on the ClassnameNN.
	 * 
	 * @param classNameNN
	 *            The ClassnameNN value as used in previous versions of AutoIt,
	 *            such as "Edit1".
	 * @return Returns advanced control id.
	 */
	public static String byClassNameNN(String classNameNN) {
		return by(By.classNameNN(classNameNN));
	}

	/**
	 * Build control id base on the internal .NET Framework WinForms name.
	 * 
	 * @param name
	 *            The internal .NET Framework WinForms name (if available).
	 * @return Returns advanced control id.
	 */
	public static String byName(String name) {
		return by(By.name(name));
	}

	/**
	 * Build control id base on the control classname using a regular
	 * expression.
	 * 
	 * @param regexpClassName
	 *            Control classname using a regular expression.
	 * @return Returns advanced control id.
	 */
	public static String byRegexpClassName(String regexpClassName) {
		return by(By.regexpClassName(regexpClassName));
	}

	/**
	 * Build control id base on the the position and size of a control. All
	 * parameters are optional.
	 * 
	 * @param x
	 *            Optional, the X coordinate of the control.
	 * @param y
	 *            Optional, the Y coordinate of the control.
	 * @param width
	 *            Optional, the width of the control.
	 * @param height
	 *            Optional, the height of the control.
	 * @return Returns advanced control id.
	 */
	public static String byBounds(Integer x, Integer y, Integer width,
			Integer height) {
		return by(By.bounds(x, y, width, height));
	}

	/**
	 * Build control id base on the the position of a control. All parameters
	 * are optional.
	 * 
	 * @param x
	 *            Optional, the X coordinate of the control.
	 * @param y
	 *            Optional, the Y coordinate of the control.
	 * @return Returns advanced control id.
	 */
	public static String byPosition(Integer x, Integer y) {
		return by(By.position(x, y));
	}

	/**
	 * Build control id base on the the size of a control. All parameters are
	 * optional.
	 * 
	 * @param width
	 *            Optional, the width of the control.
	 * @param height
	 *            Optional, the height of the control.
	 * @return Returns advanced control id.
	 */
	public static String bySize(Integer width, Integer height) {
		return by(By.size(width, height));
	}

	/**
	 * Build control id base on the 1-based instance when all given properties
	 * match.
	 * 
	 * @param instance
	 *            The 1-based instance when all given properties match.
	 * @return Returns advanced control id.
	 */
	public static String byInstance(int instance) {
		return by(By.instance(instance));
	}

	/**
	 * Build control id base on the handle address as returned by a method like
	 * Control.getHandle.
	 * 
	 * @param handle
	 *            The handle address as returned by a method like
	 *            Control.getHandle.
	 * @return Returns advanced control id.
	 */
	public static String byHandle(String handle) {
		return by(By.handle(handle));
	}

	/**
	 * Build control id base on the handle address as returned by a method like
	 * Control.getHandle_.
	 * 
	 * @param hCtrl
	 *            The handle address as returned by a method like
	 *            Control.getHandle_.
	 * @return Returns advanced control id.
	 */
	public static String byHandle(HWND hCtrl) {
		return by(By.handle(hCtrl));
	}

	/**
	 * Selector to build advanced control id.
	 * 
	 * @author zhengbo.wang
	 */
	public static abstract class By {
		protected final String property;
		protected final String value;

		public By(final String property) {
			this.property = property;
			this.value = null;
		}

		public By(final String property, final String value) {
			this.property = property;
			this.value = StringUtils.defaultString(value);
		}

		/**
		 * @param id
		 *            The internal control ID. The Control ID is the internal
		 *            numeric identifier that windows gives to each control. It
		 *            is generally the best method of identifying controls. In
		 *            addition to the AutoIt Window Info Tool, other
		 *            applications such as screenreaders for the blind and
		 *            Microsoft tools/APIs may allow you to get this Control ID.
		 * @return a By which locates control by the internal control ID.
		 */
		public static By id(int id) {
			return new ById(id);
		}

		/**
		 * @param text
		 *            The text on a control, for example "&Next" on a button.
		 * @return a By which locates control by the text on a control.
		 */
		public static By text(String text) {
			return new ByText(text);
		}

		/**
		 * @param className
		 *            The internal control classname such as "Edit" or "Button".
		 * @return a By which locates control by the internal control classname.
		 */
		public static By className(String className) {
			return new ByClass(className);
		}

		/**
		 * @param classNameNN
		 *            The ClassnameNN value as used in previous versions of
		 *            AutoIt, such as "Edit1".
		 * @return a By which locates control by the internal control
		 *         ClassnameNN.
		 */
		public static By classNameNN(String classNameNN) {
			return new ByClassNN(classNameNN);
		}

		/**
		 * @param name
		 *            The internal .NET Framework WinForms name (if available).
		 * @return a By which locates control by the internal .NET Framework
		 *         WinForms name.
		 */
		public static By name(String name) {
			return new ByName(name);
		}

		/**
		 * @param regexpClassName
		 *            Control classname using a regular expression.
		 * @return a By which locates control by the control classname using a
		 *         regular expression.
		 */
		public static By regexpClassName(String regexpClassName) {
			return new ByRegexpClass(regexpClassName);
		}

		/**
		 * All parameters are optional.
		 * 
		 * @param x
		 *            Optional, the X coordinate of the control.
		 * @param y
		 *            Optional, the Y coordinate of the control.
		 * @param width
		 *            Optional, the width of the control.
		 * @param height
		 *            Optional, the height of the control.
		 * @return a By which locates control by the position and size of a
		 *         control.
		 */
		public static By bounds(Integer x, Integer y, Integer width,
				Integer height) {
			return new ByBounds(x, y, width, height);
		}

		/**
		 * All parameters are optional.
		 * 
		 * @param x
		 *            Optional, the X coordinate of the control.
		 * @param y
		 *            Optional, the Y coordinate of the control.
		 * @return a By which locates control by the position of a control.
		 */
		public static By position(Integer x, Integer y) {
			return bounds(x, y, null, null);
		}

		/**
		 * All parameters are optional.
		 * 
		 * @param width
		 *            Optional, the width of the control.
		 * @param height
		 *            Optional, the height of the control.
		 * @return a By which locates control by the size of a control.
		 */
		public static By size(Integer width, Integer height) {
			return bounds(null, null, width, height);
		}

		/**
		 * @param instance
		 *            The 1-based instance when all given properties match.
		 * @return a By which locates control by the 1-based instance when all
		 *         given properties match.
		 */
		public static By instance(int instance) {
			return new ByInstance(instance);
		}

		/**
		 * @param handle
		 *            The handle address as returned by a method like
		 *            Control.getHandle.
		 * @return a By which locates control by the handle address as returned
		 *         by a method like Control.getHandle.
		 */
		public static By handle(String handle) {
			return new ByHandle(handle);
		}

		/**
		 * @param hCtrl
		 *            The handle address as returned by a method like
		 *            Control.getHandle_.
		 * @return a By which locates control by the handle address as returned
		 *         by a method like Control.getHandle.
		 */
		public static By handle(HWND hCtrl) {
			return new ByHandle(hCtrl);
		}

		public String toAdvancedControlId() {
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
	 * The internal control ID. The Control ID is the internal numeric
	 * identifier that windows gives to each control. It is generally the best
	 * method of identifying controls. In addition to the AutoIt Window Info
	 * Tool, other applications such as screenreaders for the blind and
	 * Microsoft tools/APIs may allow you to get this Control ID.
	 * 
	 * @author zhengbo.wang
	 */
	public static class ById extends By {
		public ById(int id) {
			super("ID", String.valueOf(id));
		}
	}

	/**
	 * The text on a control, for example "&Next" on a button.
	 * 
	 * @author zhengbo.wang
	 */
	public static class ByText extends By {
		public ByText(String text) {
			super("TEXT", text);
		}
	}

	/**
	 * The internal control classname such as "Edit" or "Button".
	 * 
	 * @author zhengbo.wang
	 */
	public static class ByClass extends By {
		public ByClass(String clazz) {
			super("CLASS", clazz);
		}
	}

	/**
	 * The ClassnameNN value as used in previous versions of AutoIt, such as
	 * "Edit1".
	 * 
	 * @author zhengbo.wang
	 */
	public static class ByClassNN extends By {
		public ByClassNN(String classNN) {
			super("CLASSNN", classNN);
		}
	}

	/**
	 * The internal .NET Framework WinForms name (if available).
	 * 
	 * @author zhengbo.wang
	 */
	public static class ByName extends By {
		public ByName(String name) {
			super("NAME", name);
		}
	}

	/**
	 * Control classname using a regular expression.
	 * 
	 * @author zhengbo.wang
	 */
	public static class ByRegexpClass extends By {
		public ByRegexpClass(String regexpClass) {
			super("REGEXPCLASS", regexpClass);
		}
	}

	/**
	 * The position and size of a control.
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
		public String toAdvancedControlId() {
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
	 * The handle address as returned by a method like Control.getHandle or
	 * Control.getHandle_.
	 * 
	 * @author zhengbo.wang
	 */
	public static class ByHandle extends By {
		public ByHandle(String handle) {
			super("HANDLE", handle);
		}

		public ByHandle(HWND hCtrl) {
			this(AutoItX.hwndToHandle(hCtrl));
		}

		@Override
		public String toAdvancedControlId() {
			return new ById(Win32.getControlId(AutoItX.handleToHwnd(value)))
					.toAdvancedControlId();
		}
	}
}
