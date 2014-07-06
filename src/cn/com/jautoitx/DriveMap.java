package cn.com.jautoitx;

import java.nio.CharBuffer;

import org.apache.commons.lang3.StringUtils;

import com.sun.jna.Native;

public class DriveMap extends AutoItX {
	public static int DRIVE_MAP_ADD_BUF_SIZE = 1024;

	private DriveMap() {
		// Do nothing
	}

	/**
	 * Maps a network drive.
	 * 
	 * When the function fails (returns 0) oAutoIt.error contains extended
	 * information:
	 * 
	 * 1 = Undefined / Other error
	 * 
	 * 2 = Access to the remote share was denied
	 * 
	 * 3 = The device is already assigned
	 * 
	 * 4 = Invalid device name
	 * 
	 * 5 = Invalid remote share
	 * 
	 * 6 = Invalid password
	 * 
	 * Note: When using "*" for the device parameter the drive letter selected
	 * will be returned instead of 1 or 0, e.g. "U:". If there was an error
	 * using "*" then a blank string "" will be returned.
	 * 
	 * @param share
	 *            The remote share to connect to in the form "\\server\share".
	 * @param driveMapAddFlag
	 * @return Returns null if a new mapping could not be created(see
	 *         getAddError() method). Returns "" if you pass a blank string for
	 *         device parameter(a connection will made but not mapped to a
	 *         specific drive). When using "*" for the device parameter the
	 *         drive letter selected will be returned, e.g. "U:".
	 */
	public static String add(final String share,
			final DriveMapAddFlag driveMapAddFlag) {
		return add(share, new DriveMapAddFlag[] { driveMapAddFlag });
	}

	/**
	 * Maps a network drive.
	 * 
	 * When the function fails (returns 0) oAutoIt.error contains extended
	 * information:
	 * 
	 * 1 = Undefined / Other error
	 * 
	 * 2 = Access to the remote share was denied
	 * 
	 * 3 = The device is already assigned
	 * 
	 * 4 = Invalid device name
	 * 
	 * 5 = Invalid remote share
	 * 
	 * 6 = Invalid password
	 * 
	 * Note: When using "*" for the device parameter the drive letter selected
	 * will be returned instead of 1 or 0, e.g. "U:". If there was an error
	 * using "*" then a blank string "" will be returned.
	 * 
	 * @param share
	 *            The remote share to connect to in the form "\\server\share".
	 * @param driveMapAddFlags
	 * @return Returns null if a new mapping could not be created(see
	 *         getAddError() method). Returns "" if you pass a blank string for
	 *         device parameter(a connection will made but not mapped to a
	 *         specific drive). When using "*" for the device parameter the
	 *         drive letter selected will be returned, e.g. "U:".
	 */
	public static String add(final String share,
			final DriveMapAddFlag[] driveMapAddFlags) {
		return add("*", share, driveMapAddFlags);
	}

	/**
	 * Maps a network drive.
	 * 
	 * When the function fails (returns 0) oAutoIt.error contains extended
	 * information:
	 * 
	 * 1 = Undefined / Other error
	 * 
	 * 2 = Access to the remote share was denied
	 * 
	 * 3 = The device is already assigned
	 * 
	 * 4 = Invalid device name
	 * 
	 * 5 = Invalid remote share
	 * 
	 * 6 = Invalid password
	 * 
	 * Note: When using "*" for the device parameter the drive letter selected
	 * will be returned instead of 1 or 0, e.g. "U:". If there was an error
	 * using "*" then a blank string "" will be returned.
	 * 
	 * @param device
	 *            The device to map, for example "O:" or "LPT1:". If you pass a
	 *            blank string for this parameter a connection is made but not
	 *            mapped to a specific drive. If you specify "*" an unused drive
	 *            letter will be automatically selected.
	 * @param share
	 *            The remote share to connect to in the form "\\server\share".
	 * @param driveMapAddFlag
	 * @return Returns null if a new mapping could not be created(see
	 *         getAddError() method). Returns "" if you pass a blank string for
	 *         device parameter(a connection will made but not mapped to a
	 *         specific drive). When using "*" for the device parameter the
	 *         drive letter selected will be returned, e.g. "U:".
	 */
	public static String add(final String device, final String share,
			final DriveMapAddFlag driveMapAddFlag) {
		return add(device, share, driveMapAddFlag, null);
	}

	/**
	 * Maps a network drive.
	 * 
	 * When the function fails (returns 0) oAutoIt.error contains extended
	 * information:
	 * 
	 * 1 = Undefined / Other error
	 * 
	 * 2 = Access to the remote share was denied
	 * 
	 * 3 = The device is already assigned
	 * 
	 * 4 = Invalid device name
	 * 
	 * 5 = Invalid remote share
	 * 
	 * 6 = Invalid password
	 * 
	 * Note: When using "*" for the device parameter the drive letter selected
	 * will be returned instead of 1 or 0, e.g. "U:". If there was an error
	 * using "*" then a blank string "" will be returned.
	 * 
	 * @param device
	 *            The device to map, for example "O:" or "LPT1:". If you pass a
	 *            blank string for this parameter a connection is made but not
	 *            mapped to a specific drive. If you specify "*" an unused drive
	 *            letter will be automatically selected.
	 * @param share
	 *            The remote share to connect to in the form "\\server\share".
	 * @param driveMapAddFlags
	 * @return Returns null if a new mapping could not be created(see
	 *         getAddError() method). Returns "" if you pass a blank string for
	 *         device parameter(a connection will made but not mapped to a
	 *         specific drive). When using "*" for the device parameter the
	 *         drive letter selected will be returned, e.g. "U:".
	 */
	public static String add(final String device, final String share,
			final DriveMapAddFlag[] driveMapAddFlags) {
		return add(device, share, driveMapAddFlags, null);
	}

	/**
	 * Maps a network drive.
	 * 
	 * When the function fails (returns 0) oAutoIt.error contains extended
	 * information:
	 * 
	 * 1 = Undefined / Other error
	 * 
	 * 2 = Access to the remote share was denied
	 * 
	 * 3 = The device is already assigned
	 * 
	 * 4 = Invalid device name
	 * 
	 * 5 = Invalid remote share
	 * 
	 * 6 = Invalid password
	 * 
	 * Note: When using "*" for the device parameter the drive letter selected
	 * will be returned instead of 1 or 0, e.g. "U:". If there was an error
	 * using "*" then a blank string "" will be returned.
	 * 
	 * @param device
	 *            The device to map, for example "O:" or "LPT1:". If you pass a
	 *            blank string for this parameter a connection is made but not
	 *            mapped to a specific drive. If you specify "*" an unused drive
	 *            letter will be automatically selected.
	 * @param share
	 *            The remote share to connect to in the form "\\server\share".
	 * @param driveMapAddFlag
	 * @param user
	 *            The username to use to connect. In the form "username" or
	 *            "domain\Username".
	 * @return Returns null if a new mapping could not be created(see
	 *         getAddError() method). Returns "" if you pass a blank string for
	 *         device parameter(a connection will made but not mapped to a
	 *         specific drive). When using "*" for the device parameter the
	 *         drive letter selected will be returned, e.g. "U:".
	 */
	public static String add(final String device, final String share,
			final DriveMapAddFlag driveMapAddFlag, final String user) {
		return add(device, share, driveMapAddFlag, user, null);
	}

	/**
	 * Maps a network drive.
	 * 
	 * When the function fails (returns 0) oAutoIt.error contains extended
	 * information:
	 * 
	 * 1 = Undefined / Other error
	 * 
	 * 2 = Access to the remote share was denied
	 * 
	 * 3 = The device is already assigned
	 * 
	 * 4 = Invalid device name
	 * 
	 * 5 = Invalid remote share
	 * 
	 * 6 = Invalid password
	 * 
	 * Note: When using "*" for the device parameter the drive letter selected
	 * will be returned instead of 1 or 0, e.g. "U:". If there was an error
	 * using "*" then a blank string "" will be returned.
	 * 
	 * @param device
	 *            The device to map, for example "O:" or "LPT1:". If you pass a
	 *            blank string for this parameter a connection is made but not
	 *            mapped to a specific drive. If you specify "*" an unused drive
	 *            letter will be automatically selected.
	 * @param share
	 *            The remote share to connect to in the form "\\server\share".
	 * @param driveMapAddFlags
	 * @param user
	 *            The username to use to connect. In the form "username" or
	 *            "domain\Username".
	 * @return Returns null if a new mapping could not be created(see
	 *         getAddError() method). Returns "" if you pass a blank string for
	 *         device parameter(a connection will made but not mapped to a
	 *         specific drive). When using "*" for the device parameter the
	 *         drive letter selected will be returned, e.g. "U:".
	 */
	public static String add(final String device, final String share,
			final DriveMapAddFlag[] driveMapAddFlags, final String user) {
		return add(device, share, driveMapAddFlags, user, null);
	}

	/**
	 * Maps a network drive.
	 * 
	 * When the function fails (returns 0) oAutoIt.error contains extended
	 * information:
	 * 
	 * 1 = Undefined / Other error
	 * 
	 * 2 = Access to the remote share was denied
	 * 
	 * 3 = The device is already assigned
	 * 
	 * 4 = Invalid device name
	 * 
	 * 5 = Invalid remote share
	 * 
	 * 6 = Invalid password
	 * 
	 * Note: When using "*" for the device parameter the drive letter selected
	 * will be returned instead of 1 or 0, e.g. "U:". If there was an error
	 * using "*" then a blank string "" will be returned.
	 * 
	 * @param device
	 *            The device to map, for example "O:" or "LPT1:". If you pass a
	 *            blank string for this parameter a connection is made but not
	 *            mapped to a specific drive. If you specify "*" an unused drive
	 *            letter will be automatically selected.
	 * @param share
	 *            The remote share to connect to in the form "\\server\share".
	 * @param driveMapAddFlag
	 * @param user
	 *            The username to use to connect. In the form "username" or
	 *            "domain\Username".
	 * @param password
	 *            The password to use to connect.
	 * @return Returns null if a new mapping could not be created(see
	 *         getAddError() method). Returns "" if you pass a blank string for
	 *         device parameter(a connection will made but not mapped to a
	 *         specific drive). When using "*" for the device parameter the
	 *         drive letter selected will be returned, e.g. "U:".
	 */
	public static String add(final String device, final String share,
			final DriveMapAddFlag driveMapAddFlag, final String user,
			final String password) {
		return add(device, share, new DriveMapAddFlag[] { driveMapAddFlag },
				user, password);
	}

	/**
	 * Maps a network drive.
	 * 
	 * When the function fails (returns 0) oAutoIt.error contains extended
	 * information:
	 * 
	 * 1 = Undefined / Other error
	 * 
	 * 2 = Access to the remote share was denied
	 * 
	 * 3 = The device is already assigned
	 * 
	 * 4 = Invalid device name
	 * 
	 * 5 = Invalid remote share
	 * 
	 * 6 = Invalid password
	 * 
	 * Note: When using "*" for the device parameter the drive letter selected
	 * will be returned instead of 1 or 0, e.g. "U:". If there was an error
	 * using "*" then a blank string "" will be returned.
	 * 
	 * @param device
	 *            The device to map, for example "O:" or "LPT1:". If you pass a
	 *            blank string for this parameter a connection is made but not
	 *            mapped to a specific drive. If you specify "*" an unused drive
	 *            letter will be automatically selected.
	 * @param share
	 *            The remote share to connect to in the form "\\server\share".
	 * @param driveMapAddFlags
	 * @param user
	 *            The username to use to connect. In the form "username" or
	 *            "domain\Username".
	 * @param password
	 *            The password to use to connect.
	 * @return Returns null if a new mapping could not be created(see
	 *         getAddError() method). Returns "" if you pass a blank string for
	 *         device parameter(a connection will made but not mapped to a
	 *         specific drive). When using "*" for the device parameter the
	 *         drive letter selected will be returned, e.g. "U:".
	 * @see #getAddError()
	 */
	public static String add(String device, final String share,
			final DriveMapAddFlag[] driveMapAddFlags, final String user,
			final String password) {
		if ((driveMapAddFlags == null) || (driveMapAddFlags.length == 0)) {
			throw new IllegalArgumentException(
					"Parameter driveMapAddFlags is required.");
		}
		final int bufSize = DRIVE_MAP_ADD_BUF_SIZE;
		final CharBuffer result = CharBuffer.allocate(bufSize);
		int flags = 0;
		for (DriveMapAddFlag driveMapAddFlag : driveMapAddFlags) {
			if (driveMapAddFlag != null) {
				flags += driveMapAddFlag.getMode();
			}
		}

		device = StringUtils.isBlank(device) ? "" : device.trim();
		autoItX.AU3_DriveMapAdd(stringToWString(device),
				stringToWString(defaultString(share)), flags,
				stringToWString(defaultString(user)),
				stringToWString(defaultString(password)), result, bufSize);

		if (hasError()) {
			return null;
		} else if (StringUtils.isBlank(device)) {
			// If you pass a blank string for this parameter a connection is
			// made but not mapped to a specific drive.
			return "";
		} else if ("*".equals(device)) {
			// If you specify "*" an unused drive letter will be automatically
			// selected.
			return Native.toString(result.array());
		}

		return device;
	}

	/**
	 * Get extended error information for add method.
	 * 
	 * @return Returns extended error information for add method.
	 */
	public static DriveMapAddError getAddError() {
		DriveMapAddError driveMapAddError = null;

		if (hasError()) {
			int errorCode = error();

			for (DriveMapAddError error : DriveMapAddError.values()) {
				if (error.getCode() == errorCode) {
					driveMapAddError = error;
					break;
				}
			}

			// Unknown error type
			if (driveMapAddError == null) {
				driveMapAddError = DriveMapAddError.UNDEFINED;
				logger.fine(String
						.format("Unknown error type %d for DriveMapAdd, return %s.%s instead.",
								errorCode,
								DriveMapAddError.class.getSimpleName(),
								DriveMapAddError.UNDEFINED.name()));
			}
		}

		return driveMapAddError;
	}

	/**
	 * Disconnects a network drive.
	 * 
	 * If a connection has no drive letter mapped you may use the connection
	 * name to disconnect, e.g. \\server\share
	 * 
	 * @param device
	 *            The device to disconnect, e.g. "O:" or "LPT1:".
	 * @return Return true if success, return false if the disconnection was
	 *         unsuccessful.
	 */
	public static boolean del(final String device) {
		return autoItX.AU3_DriveMapDel(stringToWString(defaultString(device))) == SUCCESS_RETURN_VALUE;
	}

	/**
	 * Retrieves the details of a mapped drive.
	 * 
	 * @param device
	 *            The device (drive or printer) letter to query. Eg. "O:" or
	 *            "LPT1:"
	 * @return Returns details of the mapping, e.g. \\server\share if success,
	 *         returns null if failed..
	 */
	public static String get(final String device) {
		return get(device, -1);
	}

	/**
	 * Retrieves the details of a mapped drive.
	 * 
	 * @param device
	 *            The device (drive or printer) letter to query. Eg. "O:" or
	 *            "LPT1:"
	 * @param bufSize
	 *            Buffer size to the mapping.
	 * @return Returns details of the mapping, e.g. \\server\share if success,
	 *         returns null if failed.
	 */
	public static String get(final String device, int bufSize) {
		if (bufSize <= 0) {
			bufSize = DRIVE_MAP_ADD_BUF_SIZE;
		}
		final CharBuffer mapping = CharBuffer.allocate(bufSize);
		autoItX.AU3_DriveMapGet(stringToWString(defaultString(device)),
				mapping, bufSize);

		return hasError() ? null : Native.toString(mapping.array());
	}

	public static enum DriveMapAddFlag {
		DEFAULT(0),

		/* Persistant mapping */
		PERSISTANT_MAPPING(1),

		/* Show authentication dialog if required */
		SHOW_AUTHENTICATION_DIALOG_IF_REQUIRED(8);

		private int flag;

		private DriveMapAddFlag(final int flag) {
			this.flag = flag;
		}

		public int getMode() {
			return flag;
		}

		@Override
		public String toString() {
			return String.valueOf(flag);
		}
	}

	/**
	 * Extended error information for add method.
	 * 
	 * @author zhengbo.wang
	 */
	public static enum DriveMapAddError {
		/* Undefined / Other error */
		UNDEFINED(1, "Undefined / Other error"),

		/* Access to the remote share was denied */
		ACCESS_REMOTE_SHARE_DENIED(2, "Access to the remote share was denied"),

		/* The device is already assigned */
		DEVICE_ALREADY_ASSIGNED(3, "The device is already assigned"),

		/* Invalid device name */
		INVALID_DEVICE_NAME(4, "Invalid device name"),

		/* Invalid remote share */
		INVALID_REMOVE_SHARE(5, "Invalid remote share"),

		/* Invalid password */
		INVALID_PASSWORD(6, "Invalid password");

		private final int code;

		private final String description;

		private DriveMapAddError(final int code, final String description) {
			this.code = code;
			this.description = description;
		}

		public int getCode() {
			return code;
		}

		public String getDescription() {
			return description;
		}

		@Override
		public String toString() {
			return code + ": " + description;
		}
	}
}
