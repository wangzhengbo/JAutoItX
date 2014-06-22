package cn.com.jautoitx;

import java.awt.Color;

import org.apache.commons.lang3.StringUtils;

import com.sun.jna.platform.win32.WinDef.POINT;
import com.sun.jna.platform.win32.WinDef.RECT;

public class Pixel extends AutoItX {
	public static final int INVALID_COLOR = -1;
	public static final int DEFAULT_STEP = 1;
	
	private Pixel() {
		// Do nothing
	}

	/**
	 * Generates a checksum for a region of pixels.
	 * 
	 * Performing a checksum of a region is very time consuming, so use the
	 * smallest region you are able to reduce CPU load. On some machines a
	 * checksum of the whole screen could take many seconds!
	 * 
	 * A checksum only allows you to see if "something" has changed in a region
	 * - it does not tell you exactly what has changed.
	 * 
	 * When using a step value greater than 1 you must bear in mind that the
	 * checksumming becomes less reliable for small changes as not every pixel
	 * is checked.
	 * 
	 * @param left
	 *            left coordinate of rectangle.
	 * @param top
	 *            top coordinate of rectangle.
	 * @param right
	 *            right coordinate of rectangle.
	 * @param bottom
	 *            bottom coordinate of rectangle.
	 * @return Returns the checksum value of the region.
	 */
	public static int checksum(final int left, final int top, final int right,
			final int bottom) {
		return checksum(left, top, right, bottom, null);
	}

	/**
	 * Generates a checksum for a region of pixels.
	 * 
	 * Performing a checksum of a region is very time consuming, so use the
	 * smallest region you are able to reduce CPU load. On some machines a
	 * checksum of the whole screen could take many seconds!
	 * 
	 * A checksum only allows you to see if "something" has changed in a region
	 * - it does not tell you exactly what has changed.
	 * 
	 * When using a step value greater than 1 you must bear in mind that the
	 * checksumming becomes less reliable for small changes as not every pixel
	 * is checked.
	 * 
	 * @param left
	 *            left coordinate of rectangle.
	 * @param top
	 *            top coordinate of rectangle.
	 * @param right
	 *            right coordinate of rectangle.
	 * @param bottom
	 *            bottom coordinate of rectangle.
	 * @param step
	 *            Instead of checksumming each pixel use a value larger than 1
	 *            to skip pixels (for speed). E.g. A value of 2 will only check
	 *            every other pixel. Default is 1.
	 * @return Returns the checksum value of the region.
	 */
	public static int checksum(final int left, final int top, final int right,
			final int bottom, Integer step) {
		if ((step == null) || (step <= 0)) {
			step = DEFAULT_STEP;
		}
		RECT rect = new RECT();
		rect.left = left;
		rect.top = top;
		rect.right = right;
		rect.bottom = bottom;
		return autoItX.AU3_PixelChecksum(rect, step);
	}

	/**
	 * Returns a pixel color according to x,y pixel coordinates.
	 * 
	 * @param x
	 *            x coordinate of pixel.
	 * @param y
	 *            y coordinate of pixel.
	 * @return Return decimal value of pixel's color if success, return null if
	 *         invalid coordinates.
	 */
	public static Integer getColor(int x, int y) {
		Integer color = autoItX.AU3_PixelGetColor(x, y);
		if (color == -1) {
			color = null;
		}
		return color;
	}

	/**
	 * Returns a java.awt.Color object according to x,y pixel coordinates.
	 * 
	 * @param x
	 *            x coordinate of pixel.
	 * @param y
	 *            y coordinate of pixel.
	 * @return Return java.awt.Color object for pixel's color if success, return
	 *         null if invalid coordinates.
	 */
	public static Color getColor_(int x, int y) {
		final int color = getColor(x, y);
		if (color == INVALID_COLOR) {
			return null;
		}

		final String strColor = StringUtils.leftPad(Integer.toHexString(color),
				6, '0');
		return new Color(Integer.parseInt(strColor.substring(0, 2), 16),
				Integer.parseInt(strColor.substring(2, 4), 16),
				Integer.parseInt(strColor.substring(4, 6), 16));
	}

	/**
	 * Searches a rectangle of pixels for the pixel color provided.
	 * 
	 * @param left
	 *            left coordinate of rectangle.
	 * @param top
	 *            top coordinate of rectangle.
	 * @param right
	 *            right coordinate of rectangle.
	 * @param bottom
	 *            bottom coordinate of rectangle.
	 * @param color
	 *            Colour value of pixel to find (in decimal or hex).
	 * @return Return a 2 element array containing the pixel's coordinates if
	 *         success, return null if color is not found.
	 */
	public static int[] search(final int left, final int top, final int right,
			final int bottom, final int color) {
		return search(left, top, right, bottom, color, null);
	}

	/**
	 * Searches a rectangle of pixels for the pixel color provided.
	 * 
	 * @param left
	 *            left coordinate of rectangle.
	 * @param top
	 *            top coordinate of rectangle.
	 * @param right
	 *            right coordinate of rectangle.
	 * @param bottom
	 *            bottom coordinate of rectangle.
	 * @param color
	 *            Colour value of pixel to find (in decimal or hex).
	 * @param shadeVariation
	 *            A number between 0 and 255 to indicate the allowed number of
	 *            shades of variation of the red, green, and blue components of
	 *            the colour. Default is 0 (exact match).
	 * @return Return a 2 element array containing the pixel's coordinates if
	 *         success, return null if color is not found.
	 */
	public static int[] search(final int left, final int top, final int right,
			final int bottom, final int color, final Integer shadeVariation) {
		return search(left, top, right, bottom, color, shadeVariation, null);
	}

	/**
	 * Searches a rectangle of pixels for the pixel color provided.
	 * 
	 * @param left
	 *            left coordinate of rectangle.
	 * @param top
	 *            top coordinate of rectangle.
	 * @param right
	 *            right coordinate of rectangle.
	 * @param bottom
	 *            bottom coordinate of rectangle.
	 * @param color
	 *            Colour value of pixel to find (in decimal or hex).
	 * @param shadeVariation
	 *            A number between 0 and 255 to indicate the allowed number of
	 *            shades of variation of the red, green, and blue components of
	 *            the colour. Default is 0 (exact match).
	 * @param step
	 *            Instead of searching each pixel use a value larger than 1 to
	 *            skip pixels (for speed). E.g. A value of 2 will only check
	 *            every other pixel. Default is 1.
	 * @return Return a 2 element array containing the pixel's coordinates if
	 *         success, return null if color is not found.
	 */
	public static int[] search(final int left, final int top, final int right,
			final int bottom, final int color, Integer shadeVariation,
			Integer step) {
		final POINT point = new POINT();

		if ((shadeVariation == null) || (shadeVariation < 0)
				|| (shadeVariation > 255)) {
			shadeVariation = 0;
		}
		if ((step == null) || (step <= 0)) {
			step = DEFAULT_STEP;
		}

		RECT rect = new RECT();
		rect.left = left;
		rect.top = top;
		rect.right = right;
		rect.bottom = bottom;
		autoItX.AU3_PixelSearch(rect, color, shadeVariation, step, point);

		return hasError() ? null : new int[] { point.x, point.y };
	}
}
