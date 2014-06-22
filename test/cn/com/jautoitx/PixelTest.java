package cn.com.jautoitx;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Frame;
import java.awt.Robot;

import org.junit.Assert;
import org.junit.Test;

public class PixelTest extends BaseTest {
	@Test
	public void checksum() {
		// create frame
		String title = "checksum - " + System.currentTimeMillis();
		Frame frame = new Frame(title);
		frame.setSize(400, 300);
		frame.setLocation(0, 0);

		try {
			// show frame
			frame.setVisible(true);
			sleep(500);
			Assert.assertTrue(Win.active(title));

			Color background = frame.getBackground();
			int checksum = Pixel.checksum(0, 0, 300, 200);
			Assert.assertEquals(checksum, Pixel.checksum(0, 0, 300, 200));
			int checksum2 = Pixel.checksum(-300, -200, 300, 200);
			Assert.assertEquals(checksum2, Pixel.checksum(-300, -200, 300, 200));
			Assert.assertFalse(AutoItX.hasError());

			// change frame's background color
			frame.setBackground(Color.RED);
			sleep(2000);
			int checksum3 = Pixel.checksum(0, 0, 300, 200);
			Assert.assertNotEquals(checksum, checksum3);
			Assert.assertEquals(checksum3, Pixel.checksum(0, 0, 300, 200));
			int checksum4 = Pixel.checksum(-300, -200, 300, 200);
			Assert.assertNotEquals(checksum2, checksum4);
			Assert.assertEquals(checksum4, Pixel.checksum(-300, -200, 300, 200));
			Assert.assertFalse(AutoItX.hasError());

			// restore frame's background color
			frame.setBackground(background);
			sleep(1000);
			Assert.assertEquals(checksum, Pixel.checksum(0, 0, 300, 200));
			Assert.assertEquals(checksum2, Pixel.checksum(-300, -200, 300, 200));
			Assert.assertFalse(AutoItX.hasError());
		} finally {
			// destroy frame
			frame.setVisible(false);
		}
	}

	@Test
	public void getColor() throws AWTException {
		final Robot robot = new Robot();

		Color color = robot.getPixelColor(10, 13);
		String r = Integer.toHexString(color.getRed());
		String g = Integer.toHexString(color.getGreen());
		String b = Integer.toHexString(color.getBlue());
		assertEquals(Integer.parseInt(r + g + b, 16), Pixel.getColor(10, 13));

		color = robot.getPixelColor(653, 13);
		r = Integer.toHexString(color.getRed());
		g = Integer.toHexString(color.getGreen());
		b = Integer.toHexString(color.getBlue());
		assertEquals(Integer.parseInt(r + g + b, 16), Pixel.getColor(653, 13));
	}

	@Test
	public void getColorAsAwtColor() throws AWTException {
		final Robot robot = new Robot();

		Assert.assertEquals(
				robot.getPixelColor(Integer.MIN_VALUE, Integer.MAX_VALUE),
				Pixel.getColor_(Integer.MIN_VALUE, Integer.MAX_VALUE));
		Assert.assertEquals(robot.getPixelColor(10, 13),
				Pixel.getColor_(10, 13));
		Assert.assertEquals(robot.getPixelColor(653, 13),
				Pixel.getColor_(653, 13));
	}

	@Test
	public void search() {
		// create frame
		String title = "pixelSearch - " + System.currentTimeMillis();
		Frame frame = new Frame(title);
		frame.setBounds(0, 0, 400, 300);
		frame.setVisible(true);
		sleep(500);

		try {
			// search white color
			int[] point = Pixel.search(50, 60, 100, 100, 0xFFFFFF);
			Assert.assertNotNull(point);
			Assert.assertEquals(50, point[0]);
			Assert.assertEquals(60, point[1]);

			// search orange color
			point = Pixel.search(50, 60, 100, 100, 0xFFC800);
			Assert.assertNull(point);

			// set background color to orange
			frame.setBackground(Color.ORANGE);
			sleep(200);

			// search white color
			point = Pixel.search(50, 60, 100, 100, 0xFFFFFF);
			Assert.assertNull(point);

			// search orange color
			point = Pixel.search(50, 60, 100, 100, 0xFFC800);
			Assert.assertNotNull(point);
			Assert.assertEquals(50, point[0]);
			Assert.assertEquals(60, point[1]);
		} finally {
			frame.setVisible(false);
		}
	}
}
