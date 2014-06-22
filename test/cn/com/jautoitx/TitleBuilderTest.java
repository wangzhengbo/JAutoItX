package cn.com.jautoitx;

import org.junit.Assert;
import org.junit.Test;

import cn.com.jautoitx.TitleBuilder.By;

public class TitleBuilderTest extends BaseTest {
	@Test
	public void by() {
		Assert.assertEquals("[]", TitleBuilder.by());

		// by title
		Assert.assertEquals("[TITLE:]", TitleBuilder.by(By.title(null)));
		Assert.assertEquals("[TITLE:]", TitleBuilder.by(By.title("")));
		Assert.assertEquals("[TITLE:JAutoItX]",
				TitleBuilder.by(By.title("JAutoItX")));
		Assert.assertEquals("[TITLE:JAutoItX;;]",
				TitleBuilder.by(By.title("JAutoItX;")));
		Assert.assertEquals("[TITLE:]", TitleBuilder.byTitle(null));
		Assert.assertEquals("[TITLE:]", TitleBuilder.byTitle(""));
		Assert.assertEquals("[TITLE:JAutoItX]",
				TitleBuilder.byTitle("JAutoItX"));
		Assert.assertEquals("[TITLE:JAutoItX;;]",
				TitleBuilder.byTitle("JAutoItX;"));

		// by classname
		Assert.assertEquals("[CLASS:]", TitleBuilder.by(By.className(null)));
		Assert.assertEquals("[CLASS:]", TitleBuilder.by(By.className("")));
		Assert.assertEquals("[CLASS:window]",
				TitleBuilder.by(By.className("window")));
		Assert.assertEquals("[CLASS:window;;]",
				TitleBuilder.by(By.className("window;")));
		Assert.assertEquals("[CLASS:]", TitleBuilder.byClassName(null));
		Assert.assertEquals("[CLASS:]", TitleBuilder.byClassName(""));
		Assert.assertEquals("[CLASS:window]",
				TitleBuilder.byClassName("window"));
		Assert.assertEquals("[CLASS:window;;]",
				TitleBuilder.byClassName("window;"));

		// by regexp title
		Assert.assertEquals("[REGEXPTITLE:]",
				TitleBuilder.by(By.regexpTitle(null)));
		Assert.assertEquals("[REGEXPTITLE:]",
				TitleBuilder.by(By.regexpTitle("")));
		Assert.assertEquals("[REGEXPTITLE:^JAutoItX$]",
				TitleBuilder.by(By.regexpTitle("^JAutoItX$")));
		Assert.assertEquals("[REGEXPTITLE:^JAutoItX;;]",
				TitleBuilder.by(By.regexpTitle("^JAutoItX;")));
		Assert.assertEquals("[REGEXPTITLE:]", TitleBuilder.byRegexpTitle(null));
		Assert.assertEquals("[REGEXPTITLE:]", TitleBuilder.byRegexpTitle(""));
		Assert.assertEquals("[REGEXPTITLE:^JAutoItX$]",
				TitleBuilder.byRegexpTitle("^JAutoItX$"));
		Assert.assertEquals("[REGEXPTITLE:^JAutoItX;;]",
				TitleBuilder.byRegexpTitle("^JAutoItX;"));

		// by regexp classname
		Assert.assertEquals("[REGEXPCLASS:]",
				TitleBuilder.by(By.regexpClassName(null)));
		Assert.assertEquals("[REGEXPCLASS:]",
				TitleBuilder.by(By.regexpClassName("")));
		Assert.assertEquals("[REGEXPCLASS:^window$]",
				TitleBuilder.by(By.regexpClassName("^window$")));
		Assert.assertEquals("[REGEXPCLASS:^window;;]",
				TitleBuilder.by(By.regexpClassName("^window;")));
		Assert.assertEquals("[REGEXPCLASS:]",
				TitleBuilder.byRegexpClassName(null));
		Assert.assertEquals("[REGEXPCLASS:]",
				TitleBuilder.byRegexpClassName(""));
		Assert.assertEquals("[REGEXPCLASS:^window$]",
				TitleBuilder.byRegexpClassName("^window$"));
		Assert.assertEquals("[REGEXPCLASS:^window;;]",
				TitleBuilder.byRegexpClassName("^window;"));

		// by last window
		Assert.assertEquals("[LAST]", TitleBuilder.by(By.lastWindow()));
		Assert.assertEquals("[LAST]", TitleBuilder.byLastWindow());

		// by currently active window
		Assert.assertEquals("[ACTIVE]", TitleBuilder.by(By.activeWindow()));
		Assert.assertEquals("[ACTIVE]", TitleBuilder.byActiveWindow());

		// by window position
		Assert.assertEquals("[X:10\\Y:20]",
				TitleBuilder.by(By.position(10, 20)));
		Assert.assertEquals("[Y:20]", TitleBuilder.by(By.position(null, 20)));
		Assert.assertEquals("[X:10]", TitleBuilder.by(By.position(10, null)));
		Assert.assertEquals("[]", TitleBuilder.by(By.position(null, null)));
		Assert.assertEquals("[X:10\\Y:20]", TitleBuilder.byPosition(10, 20));
		Assert.assertEquals("[Y:20]", TitleBuilder.byPosition(null, 20));
		Assert.assertEquals("[X:10]", TitleBuilder.byPosition(10, null));
		Assert.assertEquals("[]", TitleBuilder.byPosition(null, null));

		// by window size
		Assert.assertEquals("[W:400\\H:300]",
				TitleBuilder.by(By.size(400, 300)));
		Assert.assertEquals("[H:300]", TitleBuilder.by(By.size(null, 300)));
		Assert.assertEquals("[W:400]", TitleBuilder.by(By.size(400, null)));
		Assert.assertEquals("[]", TitleBuilder.by(By.size(null, null)));
		Assert.assertEquals("[W:400\\H:300]", TitleBuilder.bySize(400, 300));
		Assert.assertEquals("[H:300]", TitleBuilder.bySize(null, 300));
		Assert.assertEquals("[W:400]", TitleBuilder.bySize(400, null));
		Assert.assertEquals("[]", TitleBuilder.bySize(null, null));

		// by window bounds
		Assert.assertEquals("[X:10\\Y:20\\W:400\\H:300]",
				TitleBuilder.by(By.bounds(10, 20, 400, 300)));
		Assert.assertEquals("[Y:20\\W:400\\H:300]",
				TitleBuilder.by(By.bounds(null, 20, 400, 300)));
		Assert.assertEquals("[X:10\\W:400\\H:300]",
				TitleBuilder.by(By.bounds(10, null, 400, 300)));
		Assert.assertEquals("[X:10\\Y:20\\H:300]",
				TitleBuilder.by(By.bounds(10, 20, null, 300)));
		Assert.assertEquals("[X:10\\Y:20\\W:400]",
				TitleBuilder.by(By.bounds(10, 20, 400, null)));
		Assert.assertEquals("[X:10\\H:300]",
				TitleBuilder.by(By.bounds(10, null, null, 300)));
		Assert.assertEquals("[X:10]",
				TitleBuilder.by(By.bounds(10, null, null, null)));
		Assert.assertEquals("[]",
				TitleBuilder.by(By.bounds(null, null, null, null)));
		Assert.assertEquals("[X:10\\Y:20\\W:400\\H:300]",
				TitleBuilder.byBounds(10, 20, 400, 300));
		Assert.assertEquals("[Y:20\\W:400\\H:300]",
				TitleBuilder.byBounds(null, 20, 400, 300));
		Assert.assertEquals("[X:10\\W:400\\H:300]",
				TitleBuilder.byBounds(10, null, 400, 300));
		Assert.assertEquals("[X:10\\Y:20\\H:300]",
				TitleBuilder.byBounds(10, 20, null, 300));
		Assert.assertEquals("[X:10\\Y:20\\W:400]",
				TitleBuilder.byBounds(10, 20, 400, null));
		Assert.assertEquals("[X:10\\H:300]",
				TitleBuilder.byBounds(10, null, null, 300));
		Assert.assertEquals("[X:10]",
				TitleBuilder.byBounds(10, null, null, null));
		Assert.assertEquals("[]", TitleBuilder.byBounds(null, null, null, null));

		// by 1-based instance when all given properties match
		Assert.assertEquals("[INSTANCE:-1]", TitleBuilder.by(By.instance(-1)));
		Assert.assertEquals("[INSTANCE:0]", TitleBuilder.by(By.instance(0)));
		Assert.assertEquals("[INSTANCE:1]", TitleBuilder.by(By.instance(1)));
		Assert.assertEquals("[INSTANCE:2]", TitleBuilder.by(By.instance(2)));
		Assert.assertEquals("[INSTANCE:-1]", TitleBuilder.byInstance(-1));
		Assert.assertEquals("[INSTANCE:0]", TitleBuilder.byInstance(0));
		Assert.assertEquals("[INSTANCE:1]", TitleBuilder.byInstance(1));
		Assert.assertEquals("[INSTANCE:2]", TitleBuilder.byInstance(2));

		// by handle
		Assert.assertEquals("[HANDLE:]",
				TitleBuilder.by(By.handle((String) null)));
		Assert.assertEquals("[HANDLE:]", TitleBuilder.by(By.handle("")));
		Assert.assertEquals("[HANDLE:012345]",
				TitleBuilder.by(By.handle("012345")));
		Assert.assertEquals("[HANDLE:012345;;]",
				TitleBuilder.by(By.handle("012345;")));
		Assert.assertEquals("[HANDLE:]", TitleBuilder.byHandle((String) null));
		Assert.assertEquals("[HANDLE:]", TitleBuilder.byHandle(""));
		Assert.assertEquals("[HANDLE:012345]", TitleBuilder.byHandle("012345"));
		Assert.assertEquals("[HANDLE:012345;;]",
				TitleBuilder.byHandle("012345;"));

		Assert.assertEquals(
				"[CLASS:class; TITLE:tit;;le;;; ACTIVE; LAST; HANDLE:handle]",
				TitleBuilder.by(By.className("class"), By.title("tit;le;"),
						By.activeWindow(), By.lastWindow(), By.handle("handle")));

		// run notepad
		int pid = runNotepad();
		Assert.assertTrue(Win.active(TitleBuilder.by(By.title(NOTEPAD_TITLE))));

		Assert.assertTrue(Win.active(TitleBuilder.by(By.title(NOTEPAD_TITLE),
				By.bounds(Win.getPosX(NOTEPAD_TITLE),
						Win.getPosY(NOTEPAD_TITLE),
						Win.getWidth(NOTEPAD_TITLE),
						Win.getHeight(NOTEPAD_TITLE)))));
		Assert.assertFalse(Win.active(TitleBuilder.by(By.title(NOTEPAD_TITLE),
				By.bounds(Win.getPosX(NOTEPAD_TITLE) + 1,
						Win.getPosY(NOTEPAD_TITLE),
						Win.getWidth(NOTEPAD_TITLE),
						Win.getHeight(NOTEPAD_TITLE)))));

		Assert.assertTrue(Win.active(TitleBuilder.by(By.title(NOTEPAD_TITLE),
				By.handle(Win.getHandle(NOTEPAD_TITLE)), By.bounds(
						Win.getPosX(NOTEPAD_TITLE), Win.getPosY(NOTEPAD_TITLE),
						Win.getWidth(NOTEPAD_TITLE),
						Win.getHeight(NOTEPAD_TITLE)))));
		Assert.assertFalse(Win.active(TitleBuilder.by(By.title(NOTEPAD_TITLE),
				By.handle(Win.getHandle(NOTEPAD_TITLE) + "x"), By.bounds(
						Win.getPosX(NOTEPAD_TITLE), Win.getPosY(NOTEPAD_TITLE),
						Win.getWidth(NOTEPAD_TITLE),
						Win.getHeight(NOTEPAD_TITLE)))));

		Assert.assertTrue(Win.active(TitleBuilder.by(By.title(NOTEPAD_TITLE),
				By.className(Win.getClassName(NOTEPAD_TITLE)), By.bounds(
						Win.getPosX(NOTEPAD_TITLE), Win.getPosY(NOTEPAD_TITLE),
						Win.getWidth(NOTEPAD_TITLE),
						Win.getHeight(NOTEPAD_TITLE)))));
		Assert.assertFalse(Win.active(TitleBuilder.by(By.title(NOTEPAD_TITLE),
				By.className(Win.getClassName(NOTEPAD_TITLE) + 'x'), By.bounds(
						Win.getPosX(NOTEPAD_TITLE), Win.getPosY(NOTEPAD_TITLE),
						Win.getWidth(NOTEPAD_TITLE),
						Win.getHeight(NOTEPAD_TITLE)))));

		Assert.assertTrue(Win.active(TitleBuilder.by(By.title(NOTEPAD_TITLE),
				By.className(Win.getClassName(NOTEPAD_TITLE)), By.handle(Win
						.getHandle((String) null)), By.bounds(
						Win.getPosX(NOTEPAD_TITLE), Win.getPosY(NOTEPAD_TITLE),
						Win.getWidth(NOTEPAD_TITLE),
						Win.getHeight(NOTEPAD_TITLE)))));
		Assert.assertTrue(Win.active(TitleBuilder.by(By.title(NOTEPAD_TITLE),
				By.className(Win.getClassName(NOTEPAD_TITLE)), By.handle(Win
						.getHandle(NOTEPAD_TITLE)), By.bounds(
						Win.getPosX(NOTEPAD_TITLE), Win.getPosY(NOTEPAD_TITLE),
						Win.getWidth(NOTEPAD_TITLE),
						Win.getHeight(NOTEPAD_TITLE)))));
		Assert.assertTrue(Win.active(TitleBuilder.by(
				By.title(NOTEPAD_TITLE + 'x'),
				By.className(Win.getClassName(NOTEPAD_TITLE) + 'x'),
				By.handle(Win.getHandle(NOTEPAD_TITLE)),
				By.bounds(Win.getPosX(NOTEPAD_TITLE) + 1,
						Win.getPosY(NOTEPAD_TITLE) + 1,
						Win.getWidth(NOTEPAD_TITLE) + 1,
						Win.getHeight(NOTEPAD_TITLE) + 1))));

		// close notepad
		Process.close(pid);
		sleep(500);
		Assert.assertFalse(Process.exists(pid));
	}
}
