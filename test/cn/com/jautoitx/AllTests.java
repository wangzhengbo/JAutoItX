package cn.com.jautoitx;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({ AutoItXTest.class, ClipTest.class,
		ControlIdBuilderTest.class, ControlTest.class, DriveMapTest.class,
		KeyboardTest.class, ListViewTest.class, MiscTest.class,
		MouseTest.class, OptTest.class, PixelTest.class, ProcessTest.class,
		TitleBuilderTest.class, TreeViewTest.class, Win32Test.class,
		WinTest.class })
public class AllTests {

}
