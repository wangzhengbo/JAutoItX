package cn.com.jautoitx;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

public class AutoItXTest extends BaseTest {
	@Test
	public void version() {
		assertEquals("3.3.10.2", AutoItX.version());
	}

	@Ignore
	@Test
	public void tooltip() {
		final String TOOLTIP_TITLE = "[CLASS:tooltips_class32]";

		String tooltip = "Hi, this is a test tooltip at position (100, 200).";
		AutoItX.tooltip(tooltip, 100, 200);
		String hWnd = Win.getHandle(TOOLTIP_TITLE);
		assertEquals(tooltip, getTooltip(hWnd));
		assertEquals(100, Win.getPosX(TOOLTIP_TITLE));
		assertEquals(200, Win.getPosY(TOOLTIP_TITLE));
		sleep(1000);

		// clear tooltip
		Assert.assertTrue(Win.visible(String.format("[HANDLE:%s]", hWnd)));
		AutoItX.tooltip(null);
		sleep(1000);
		Assert.assertFalse(Win.visible(String.format("[HANDLE:%s]", hWnd)));

		tooltip = "Hi, this is a test tooltip\nat position (150, 450).";
		AutoItX.tooltip(tooltip, 150, 450);
		Assert.assertTrue(Win.visible(String.format("[HANDLE:%s]", hWnd)));
		assertEquals(tooltip, getTooltip(Win.getHandle(TOOLTIP_TITLE)));
		assertEquals(150, Win.getPosX(TOOLTIP_TITLE));
		assertEquals(450, Win.getPosY(TOOLTIP_TITLE));
		sleep(1000);

		tooltip = "Hi, this is a test tooltip\nat position (-20, 50).";
		AutoItX.tooltip(tooltip, -20, 50);
		assertEquals(tooltip, getTooltip(Win.getHandle(TOOLTIP_TITLE)));
		assertEquals(0, Win.getPosX(TOOLTIP_TITLE));
		assertEquals(50, Win.getPosY(TOOLTIP_TITLE));
		sleep(1000);

		// TODO: This is AutoItX's bug
		tooltip = "Hi, this is a test tooltip\nat position (-250, -80).";
		AutoItX.tooltip(tooltip, -250, -80);
		assertEquals(tooltip, getTooltip(Win.getHandle(TOOLTIP_TITLE)));
		assertEquals(0, Win.getPosX(TOOLTIP_TITLE));
		assertEquals(0, Win.getPosY(TOOLTIP_TITLE));
		sleep(1000);

		tooltip = "Hi, this is a test tooltip placed near the mouse cursor.";
		AutoItX.tooltip(tooltip);
		assertEquals(tooltip, getTooltip(Win.getHandle(TOOLTIP_TITLE)));
		assertEquals(Mouse.getPosX(), Win.getPosX(TOOLTIP_TITLE));
		assertEquals(Mouse.getPosY(), Win.getPosY(TOOLTIP_TITLE));
		sleep(1000);

		Assert.assertTrue(Win.visible(String.format("[HANDLE:%s]", hWnd)));
		AutoItX.tooltip();
		Assert.assertFalse(Win.visible(String.format("[HANDLE:%s]", hWnd)));
	}
}
