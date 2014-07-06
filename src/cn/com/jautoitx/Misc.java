package cn.com.jautoitx;

public class Misc extends AutoItX {
	private Misc() {
		// Do nothing
	}

	/**
	 * Checks if the current user has administrator privileges.
	 * 
	 * @return Return 1 if the current user has administrator privileges, return
	 *         0 if user lacks admin privileges.
	 */
	public static boolean isAdmin() {
		return autoItX.AU3_IsAdmin() == SUCCESS_RETURN_VALUE;
	}

	/**
	 * Pause script execution.
	 * 
	 * Maximum sleep time is 2147483647 milliseconds (24 days).
	 * 
	 * @param milliSeconds
	 *            Amount of time to pause (in milliseconds).
	 */
	public static void sleep(final int milliSeconds) {
		autoItX.AU3_Sleep(milliSeconds);
	}
}
