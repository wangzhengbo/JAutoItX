package cn.com.jautoitx;

import org.apache.commons.lang3.StringUtils;

public class Keyboard extends AutoItX {
	private Keyboard() {
		// Do nothing
	}

	/**
	 * Sends simulated keystrokes to the active window.
	 * 
	 * See the Appendix for some tips.
	 * 
	 * The "Send" command syntax is similar to that of ScriptIt and the Visual
	 * Basic "SendKeys" command. Characters are sent as written with the
	 * exception of the following characters:
	 * 
	 * '!' This tells AutoIt to send an ALT keystroke, therefore
	 * Send("This is text!a") would send the keys "This is text" and then press
	 * "ALT+a".
	 * 
	 * N.B. Some programs are very choosy about capital letters and ALT keys,
	 * i.e. "!A" is different to "!a". The first says ALT+SHIFT+A, the second is
	 * ALT+a. If in doubt, use lowercase!
	 * 
	 * '+' This tells AutoIt to send a SHIFT keystroke, therefore Send("Hell+o")
	 * would send the text "HellO". Send("!+a") would send "ALT+SHIFT+a".
	 * 
	 * '^' This tells AutoIt to send a CONTROL keystroke, therefore Send("^!a")
	 * would send "CTRL+ALT+a".
	 * 
	 * N.B. Some programs are very choosy about capital letters and CTRL keys,
	 * i.e. "^A" is different to "^a". The first says CTRL+SHIFT+A, the second
	 * is CTRL+a. If in doubt, use lowercase!
	 * 
	 * '#' The hash now sends a Windows keystroke; therefore, Send("#r") would
	 * send Win+r which launches the Run dialog box.
	 * 
	 * You can set SendCapslockMode to make CAPS LOCK disabled at the start of a
	 * Send operation and restored upon completion. However, if a user is
	 * holding down the Shift key when a Send function begins, text may be sent
	 * in uppercase. One workaround is to Send("{SHIFTDOWN}{SHIFTUP}") before
	 * the other Send operations.
	 * 
	 * Certain special keys can be sent and should be enclosed in braces:
	 * 
	 * N.B. Windows does not allow the simulation of the "CTRL-ALT-DEL"
	 * combination!
	 * 
	 * To send the ASCII value A (same as pressing ALT+065 on the numeric
	 * keypad) Send("{ASC 65}")
	 * 
	 * Single keys can also be repeated, e.g. Send("{DEL 4}") ;Presses the DEL
	 * key 4 times Send("{S 30}") ;Sends 30 'S' characters Send("+{TAB 4})
	 * ;Presses SHIFT+TAB 4 times
	 * 
	 * To hold a key down (generally only useful for games) Send("{a down}")
	 * ;Holds the A key down Send("{a up}") ;Releases the A key
	 * 
	 * To set the state of the capslock, numlock and scrolllock keys
	 * Send("{NumLock on}") ;Turns the NumLock key on Send("{CapsLock off}")
	 * ;Turns the CapsLock key off Send("{ScrollLock toggle}") ;Toggles the
	 * state of ScrollLock
	 * 
	 * If you with to use a variable for the count, try $n = 4 Send("+{TAB " &
	 * $n & "}")
	 * 
	 * If you wish to send the ASCII value A four times, then try $x = Chr(65)
	 * Send("{" & $x & " 4}")
	 * 
	 * Most laptop computer keyboards have a special Fn key. This key cannot be
	 * simulated.
	 * 
	 * Note, by setting the flag parameter to 1 the above "special" processing
	 * will be disabled. This is useful when you want to send some text copied
	 * from a variable and you want the text sent exactly as written.
	 * 
	 * For example, open Folder Options (in the control panel) and try the
	 * following:
	 * 
	 * Use Alt-key combos to access menu items. Also, open Notepad and try the
	 * following: Send("!f") Send Alt+f, the access key for Notepad's file menu.
	 * Try other letters!
	 * 
	 * See Windows' Help--press Win+F1--for a complete list of keyboard
	 * shortcuts if you don't know the importance of Alt+F4, PrintScreen,
	 * Ctrl+C, and so on.
	 * 
	 * @param text
	 *            The sequence of keys to send.
	 */
	public static void send(final String text) {
		send(text, (Integer) null);
	}

	/**
	 * Sends simulated keystrokes to the active window.
	 * 
	 * See the Appendix for some tips.
	 * 
	 * The "Send" command syntax is similar to that of ScriptIt and the Visual
	 * Basic "SendKeys" command. Characters are sent as written with the
	 * exception of the following characters:
	 * 
	 * '!' This tells AutoIt to send an ALT keystroke, therefore
	 * Send("This is text!a") would send the keys "This is text" and then press
	 * "ALT+a".
	 * 
	 * N.B. Some programs are very choosy about capital letters and ALT keys,
	 * i.e. "!A" is different to "!a". The first says ALT+SHIFT+A, the second is
	 * ALT+a. If in doubt, use lowercase!
	 * 
	 * '+' This tells AutoIt to send a SHIFT keystroke, therefore Send("Hell+o")
	 * would send the text "HellO". Send("!+a") would send "ALT+SHIFT+a".
	 * 
	 * '^' This tells AutoIt to send a CONTROL keystroke, therefore Send("^!a")
	 * would send "CTRL+ALT+a".
	 * 
	 * N.B. Some programs are very choosy about capital letters and CTRL keys,
	 * i.e. "^A" is different to "^a". The first says CTRL+SHIFT+A, the second
	 * is CTRL+a. If in doubt, use lowercase!
	 * 
	 * '#' The hash now sends a Windows keystroke; therefore, Send("#r") would
	 * send Win+r which launches the Run dialog box.
	 * 
	 * You can set SendCapslockMode to make CAPS LOCK disabled at the start of a
	 * Send operation and restored upon completion. However, if a user is
	 * holding down the Shift key when a Send function begins, text may be sent
	 * in uppercase. One workaround is to Send("{SHIFTDOWN}{SHIFTUP}") before
	 * the other Send operations.
	 * 
	 * Certain special keys can be sent and should be enclosed in braces:
	 * 
	 * N.B. Windows does not allow the simulation of the "CTRL-ALT-DEL"
	 * combination!
	 * 
	 * To send the ASCII value A (same as pressing ALT+065 on the numeric
	 * keypad) Send("{ASC 65}")
	 * 
	 * Single keys can also be repeated, e.g. Send("{DEL 4}") ;Presses the DEL
	 * key 4 times Send("{S 30}") ;Sends 30 'S' characters Send("+{TAB 4})
	 * ;Presses SHIFT+TAB 4 times
	 * 
	 * To hold a key down (generally only useful for games) Send("{a down}")
	 * ;Holds the A key down Send("{a up}") ;Releases the A key
	 * 
	 * To set the state of the capslock, numlock and scrolllock keys
	 * Send("{NumLock on}") ;Turns the NumLock key on Send("{CapsLock off}")
	 * ;Turns the CapsLock key off Send("{ScrollLock toggle}") ;Toggles the
	 * state of ScrollLock
	 * 
	 * If you with to use a variable for the count, try $n = 4 Send("+{TAB " &
	 * $n & "}")
	 * 
	 * If you wish to send the ASCII value A four times, then try $x = Chr(65)
	 * Send("{" & $x & " 4}")
	 * 
	 * Most laptop computer keyboards have a special Fn key. This key cannot be
	 * simulated.
	 * 
	 * Note, by setting the flag parameter to 1 the above "special" processing
	 * will be disabled. This is useful when you want to send some text copied
	 * from a variable and you want the text sent exactly as written.
	 * 
	 * For example, open Folder Options (in the control panel) and try the
	 * following:
	 * 
	 * Use Alt-key combos to access menu items. Also, open Notepad and try the
	 * following: Send("!f") Send Alt+f, the access key for Notepad's file menu.
	 * Try other letters!
	 * 
	 * See Windows' Help--press Win+F1--for a complete list of keyboard
	 * shortcuts if you don't know the importance of Alt+F4, PrintScreen,
	 * Ctrl+C, and so on.
	 * 
	 * @param text
	 *            The sequence of keys to send.
	 * @param flag
	 *            Changes how "keys" is processed.
	 */
	public static void send(final String text, final SendFlag flag) {
		send(text, (Integer) ((flag == null) ? null : flag.getSendFlag()));
	}

	/**
	 * Sends simulated keystrokes to the active window.
	 * 
	 * See the Appendix for some tips.
	 * 
	 * The "Send" command syntax is similar to that of ScriptIt and the Visual
	 * Basic "SendKeys" command. Characters are sent as written with the
	 * exception of the following characters:
	 * 
	 * '!' This tells AutoIt to send an ALT keystroke, therefore
	 * Send("This is text!a") would send the keys "This is text" and then press
	 * "ALT+a".
	 * 
	 * N.B. Some programs are very choosy about capital letters and ALT keys,
	 * i.e. "!A" is different to "!a". The first says ALT+SHIFT+A, the second is
	 * ALT+a. If in doubt, use lowercase!
	 * 
	 * '+' This tells AutoIt to send a SHIFT keystroke, therefore Send("Hell+o")
	 * would send the text "HellO". Send("!+a") would send "ALT+SHIFT+a".
	 * 
	 * '^' This tells AutoIt to send a CONTROL keystroke, therefore Send("^!a")
	 * would send "CTRL+ALT+a".
	 * 
	 * N.B. Some programs are very choosy about capital letters and CTRL keys,
	 * i.e. "^A" is different to "^a". The first says CTRL+SHIFT+A, the second
	 * is CTRL+a. If in doubt, use lowercase!
	 * 
	 * '#' The hash now sends a Windows keystroke; therefore, Send("#r") would
	 * send Win+r which launches the Run dialog box.
	 * 
	 * You can set SendCapslockMode to make CAPS LOCK disabled at the start of a
	 * Send operation and restored upon completion. However, if a user is
	 * holding down the Shift key when a Send function begins, text may be sent
	 * in uppercase. One workaround is to Send("{SHIFTDOWN}{SHIFTUP}") before
	 * the other Send operations.
	 * 
	 * Certain special keys can be sent and should be enclosed in braces:
	 * 
	 * N.B. Windows does not allow the simulation of the "CTRL-ALT-DEL"
	 * combination!
	 * 
	 * To send the ASCII value A (same as pressing ALT+065 on the numeric
	 * keypad) Send("{ASC 65}")
	 * 
	 * Single keys can also be repeated, e.g. Send("{DEL 4}") ;Presses the DEL
	 * key 4 times Send("{S 30}") ;Sends 30 'S' characters Send("+{TAB 4})
	 * ;Presses SHIFT+TAB 4 times
	 * 
	 * To hold a key down (generally only useful for games) Send("{a down}")
	 * ;Holds the A key down Send("{a up}") ;Releases the A key
	 * 
	 * To set the state of the capslock, numlock and scrolllock keys
	 * Send("{NumLock on}") ;Turns the NumLock key on Send("{CapsLock off}")
	 * ;Turns the CapsLock key off Send("{ScrollLock toggle}") ;Toggles the
	 * state of ScrollLock
	 * 
	 * If you with to use a variable for the count, try $n = 4 Send("+{TAB " &
	 * $n & "}")
	 * 
	 * If you wish to send the ASCII value A four times, then try $x = Chr(65)
	 * Send("{" & $x & " 4}")
	 * 
	 * Most laptop computer keyboards have a special Fn key. This key cannot be
	 * simulated.
	 * 
	 * Note, by setting the flag parameter to 1 the above "special" processing
	 * will be disabled. This is useful when you want to send some text copied
	 * from a variable and you want the text sent exactly as written.
	 * 
	 * For example, open Folder Options (in the control panel) and try the
	 * following:
	 * 
	 * Use Alt-key combos to access menu items. Also, open Notepad and try the
	 * following: Send("!f") Send Alt+f, the access key for Notepad's file menu.
	 * Try other letters!
	 * 
	 * See Windows' Help--press Win+F1--for a complete list of keyboard
	 * shortcuts if you don't know the importance of Alt+F4, PrintScreen,
	 * Ctrl+C, and so on.
	 * 
	 * @param text
	 *            The sequence of keys to send.
	 * @param flag
	 *            Changes how "keys" is processed:<br/>
	 *            flag = 0 (default), Text contains special characters like +
	 *            and ! to indicate SHIFT and ALT key presses.<br/>
	 * 
	 *            flag = 1, keys are sent raw.
	 */
	public static void send(final String text, final Integer flag) {
		if (StringUtils.isNotEmpty(text)) {
			autoItX.AU3_Send(stringToWString(defaultString(text)), flag);
		}
	}

	/**
	 * Special keys.
	 * 
	 * Certain special keys can be sent and should be enclosed in braces.
	 * 
	 * To send the ASCII value A (same as pressing ALT+65 on the numeric
	 * keypad): Send "{ASC 65}"
	 * 
	 * Single keys can also be repeated, e.g:
	 * 
	 * Send "{DEL 4}" Presses the DEL key 4 times
	 * 
	 * Send "{S 30}"
	 * 
	 * Sends 30 'S' characters
	 * 
	 * Send "+{TAB 4}" Presses SHIFT+TAB 4 times
	 * 
	 * @author zhengbo.wang
	 */
	public static interface Keys {
		// {!}
		// {!}
		// {#}
		// {+}
		// {^}
		// {{}
		// {}}

		public static final String SPACE = "{SPACE}";
		public static final String ENTER = "{ENTER}";
		public static final String ALT = "{ALT}";
		public static final String BACKSPACE = "{BACKSPACE}";
		public static final String BS = "{BS}";
		public static final String DELETE = "{DELETE}";
		public static final String DEL = "{DEL}";
		/* Cursor up */
		public static final String UP = "{UP}";
		/* Cursor down */
		public static final String DOWN = "{DOWN}";
		/* Cursor left */
		public static final String LEFT = "{LEFT}";
		/* Cursor right */
		public static final String RIGHT = "{RIGHT}";
		public static final String HOME = "{HOME}";
		public static final String END = "{END}";
		public static final String ESCAPE = "{ESCAPE}";
		public static final String ESC = "{ESC}";
		public static final String INSERT = "{INSERT}";
		public static final String INS = "{INS}";
		public static final String PGUP = "{PGUP}";
		public static final String PGDN = "{PGDN}";
		/* Function keys */
		public static final String F1 = "{F1}";
		public static final String F2 = "{F2}";
		public static final String F3 = "{F3}";
		public static final String F4 = "{F4}";
		public static final String F5 = "{F5}";
		public static final String F6 = "{F6}";
		public static final String F7 = "{F7}";
		public static final String F8 = "{F8}";
		public static final String F9 = "{F9}";
		public static final String F10 = "{F10}";
		public static final String F11 = "{F11}";
		public static final String F12 = "{F12}";
		public static final String TAB = "{TAB}";
		public static final String PRINTSCREEN = "{PRINTSCREEN}";
		/* Left Windows key */
		public static final String LWIN = "{LWIN}";
		/* Right Windows key */
		public static final String RWIN = "{RWIN}";
		public static final String NUMLOCK = "{NUMLOCK}";
		/* Ctrl+break */
		public static final String CTRLBREAK = "{CTRLBREAK}";
		public static final String PAUSE = "{PAUSE}";
		public static final String CAPSLOCK = "{CAPSLOCK}";
		/* Numpad digits */
		public static final String NUMPAD_0 = "{NUMPAD0}";
		public static final String NUMPAD_1 = "{NUMPAD1}";
		public static final String NUMPAD_2 = "{NUMPAD2}";
		public static final String NUMPAD_3 = "{NUMPAD3}";
		public static final String NUMPAD_4 = "{NUMPAD4}";
		public static final String NUMPAD_5 = "{NUMPAD5}";
		public static final String NUMPAD_6 = "{NUMPAD6}";
		public static final String NUMPAD_7 = "{NUMPAD7}";
		public static final String NUMPAD_8 = "{NUMPAD8}";
		public static final String NUMPAD_9 = "{NUMPAD9}";
		/* Numpad Multiply */
		public static final String NUMPAD_MULT = "{NUMPADMULT}";
		/* Numpad Add */
		public static final String NUMPAD_ADD = "{NUMPADADD}";
		/* Numpad Subtract */
		public static final String NUMPAD_SUB = "{NUMPADSUB}";
		/* Numpad Divide */
		public static final String NUMPAD_DIV = "{NUMPADDIV}";
		/* Numpad period */
		public static final String NUMPAD_DOT = "{NUMPADDOT}";
		/* Windows App key */
		public static final String APPSKEY = "{APPSKEY}";
		/* Holds the ALT key down until {ALTUP} is sent */
		public static final String ALTDOWN = "{ALTDOWN}";
		/* Holds the SHIFT key down until {SHIFTUP} is sent */
		public static final String SHIFTDOWN = "{SHIFTDOWN}";
		/* Holds the CTRL key down until {CTRLUP} is sent */
		public static final String CTRLDOWN = "{CTRLDOWN}";
		/* Holds the left Windows key down until {LWINUP} is sent */
		public static final String LWINDOWN = "{LWINDOWN}";
		/* Holds the right Windows key down until {RWINUP} is sent */
		public static final String RWINDOWN = "{RWINDOWN}";
		/* Send the ALT+nnnn key combination */
		public static final String ASCII_0 = "{ASC 0}";
		public static final String ASCII_1 = "{ASC 1}";
		public static final String ASCII_2 = "{ASC 2}";
		public static final String ASCII_3 = "{ASC 3}";
		public static final String ASCII_4 = "{ASC 4}";
		public static final String ASCII_5 = "{ASC 5}";
		public static final String ASCII_6 = "{ASC 6}";
		public static final String ASCII_7 = "{ASC 7}";
		public static final String ASCII_8 = "{ASC 8}";
		public static final String ASCII_9 = "{ASC 9}";
		public static final String ASCII_10 = "{ASC 10}";
		public static final String ASCII_11 = "{ASC 11}";
		public static final String ASCII_12 = "{ASC 12}";
		public static final String ASCII_13 = "{ASC 13}";
		public static final String ASCII_14 = "{ASC 14}";
		public static final String ASCII_15 = "{ASC 15}";
		public static final String ASCII_16 = "{ASC 16}";
		public static final String ASCII_17 = "{ASC 17}";
		public static final String ASCII_18 = "{ASC 18}";
		public static final String ASCII_19 = "{ASC 19}";
		public static final String ASCII_20 = "{ASC 20}";
		public static final String ASCII_21 = "{ASC 21}";
		public static final String ASCII_22 = "{ASC 22}";
		public static final String ASCII_23 = "{ASC 23}";
		public static final String ASCII_24 = "{ASC 24}";
		public static final String ASCII_25 = "{ASC 25}";
		public static final String ASCII_26 = "{ASC 26}";
		public static final String ASCII_27 = "{ASC 27}";
		public static final String ASCII_28 = "{ASC 28}";
		public static final String ASCII_29 = "{ASC 29}";
		public static final String ASCII_30 = "{ASC 30}";
		public static final String ASCII_31 = "{ASC 31}";
		public static final String ASCII_32 = "{ASC 32}";
		public static final String ASCII_33 = "{ASC 33}";
		public static final String ASCII_34 = "{ASC 34}";
		public static final String ASCII_35 = "{ASC 35}";
		public static final String ASCII_36 = "{ASC 36}";
		public static final String ASCII_37 = "{ASC 37}";
		public static final String ASCII_38 = "{ASC 38}";
		public static final String ASCII_39 = "{ASC 39}";
		public static final String ASCII_40 = "{ASC 40}";
		public static final String ASCII_41 = "{ASC 41}";
		public static final String ASCII_42 = "{ASC 42}";
		public static final String ASCII_43 = "{ASC 43}";
		public static final String ASCII_44 = "{ASC 44}";
		public static final String ASCII_45 = "{ASC 45}";
		public static final String ASCII_46 = "{ASC 46}";
		public static final String ASCII_47 = "{ASC 47}";
		public static final String ASCII_48 = "{ASC 48}";
		public static final String ASCII_49 = "{ASC 49}";
		public static final String ASCII_50 = "{ASC 50}";
		public static final String ASCII_51 = "{ASC 51}";
		public static final String ASCII_52 = "{ASC 52}";
		public static final String ASCII_53 = "{ASC 53}";
		public static final String ASCII_54 = "{ASC 54}";
		public static final String ASCII_55 = "{ASC 55}";
		public static final String ASCII_56 = "{ASC 56}";
		public static final String ASCII_57 = "{ASC 57}";
		public static final String ASCII_58 = "{ASC 58}";
		public static final String ASCII_59 = "{ASC 59}";
		public static final String ASCII_60 = "{ASC 60}";
		public static final String ASCII_61 = "{ASC 61}";
		public static final String ASCII_62 = "{ASC 62}";
		public static final String ASCII_63 = "{ASC 63}";
		public static final String ASCII_64 = "{ASC 64}";
		public static final String ASCII_65 = "{ASC 65}";
		public static final String ASCII_66 = "{ASC 66}";
		public static final String ASCII_67 = "{ASC 67}";
		public static final String ASCII_68 = "{ASC 68}";
		public static final String ASCII_69 = "{ASC 69}";
		public static final String ASCII_70 = "{ASC 70}";
		public static final String ASCII_71 = "{ASC 71}";
		public static final String ASCII_72 = "{ASC 72}";
		public static final String ASCII_73 = "{ASC 73}";
		public static final String ASCII_74 = "{ASC 74}";
		public static final String ASCII_75 = "{ASC 75}";
		public static final String ASCII_76 = "{ASC 76}";
		public static final String ASCII_77 = "{ASC 77}";
		public static final String ASCII_78 = "{ASC 78}";
		public static final String ASCII_79 = "{ASC 79}";
		public static final String ASCII_80 = "{ASC 80}";
		public static final String ASCII_81 = "{ASC 81}";
		public static final String ASCII_82 = "{ASC 82}";
		public static final String ASCII_83 = "{ASC 83}";
		public static final String ASCII_84 = "{ASC 84}";
		public static final String ASCII_85 = "{ASC 85}";
		public static final String ASCII_86 = "{ASC 86}";
		public static final String ASCII_87 = "{ASC 87}";
		public static final String ASCII_88 = "{ASC 88}";
		public static final String ASCII_89 = "{ASC 89}";
		public static final String ASCII_90 = "{ASC 90}";
		public static final String ASCII_91 = "{ASC 91}";
		public static final String ASCII_92 = "{ASC 92}";
		public static final String ASCII_93 = "{ASC 93}";
		public static final String ASCII_94 = "{ASC 94}";
		public static final String ASCII_95 = "{ASC 95}";
		public static final String ASCII_96 = "{ASC 96}";
		public static final String ASCII_97 = "{ASC 97}";
		public static final String ASCII_98 = "{ASC 98}";
		public static final String ASCII_99 = "{ASC 99}";
		public static final String ASCII_100 = "{ASC 100}";
		public static final String ASCII_101 = "{ASC 101}";
		public static final String ASCII_102 = "{ASC 102}";
		public static final String ASCII_103 = "{ASC 103}";
		public static final String ASCII_104 = "{ASC 104}";
		public static final String ASCII_105 = "{ASC 105}";
		public static final String ASCII_106 = "{ASC 106}";
		public static final String ASCII_107 = "{ASC 107}";
		public static final String ASCII_108 = "{ASC 108}";
		public static final String ASCII_109 = "{ASC 109}";
		public static final String ASCII_110 = "{ASC 110}";
		public static final String ASCII_111 = "{ASC 111}";
		public static final String ASCII_112 = "{ASC 112}";
		public static final String ASCII_113 = "{ASC 113}";
		public static final String ASCII_114 = "{ASC 114}";
		public static final String ASCII_115 = "{ASC 115}";
		public static final String ASCII_116 = "{ASC 116}";
		public static final String ASCII_117 = "{ASC 117}";
		public static final String ASCII_118 = "{ASC 118}";
		public static final String ASCII_119 = "{ASC 119}";
		public static final String ASCII_120 = "{ASC 120}";
		public static final String ASCII_121 = "{ASC 121}";
		public static final String ASCII_122 = "{ASC 122}";
		public static final String ASCII_123 = "{ASC 123}";
		public static final String ASCII_124 = "{ASC 124}";
		public static final String ASCII_125 = "{ASC 125}";
		public static final String ASCII_126 = "{ASC 126}";
		public static final String ASCII_127 = "{ASC 127}";
		public static final String ASCII_128 = "{ASC 128}";
		public static final String ASCII_129 = "{ASC 129}";
		public static final String ASCII_130 = "{ASC 130}";
		public static final String ASCII_131 = "{ASC 131}";
		public static final String ASCII_132 = "{ASC 132}";
		public static final String ASCII_133 = "{ASC 133}";
		public static final String ASCII_134 = "{ASC 134}";
		public static final String ASCII_135 = "{ASC 135}";
		public static final String ASCII_136 = "{ASC 136}";
		public static final String ASCII_137 = "{ASC 137}";
		public static final String ASCII_138 = "{ASC 138}";
		public static final String ASCII_139 = "{ASC 139}";
		public static final String ASCII_140 = "{ASC 140}";
		public static final String ASCII_141 = "{ASC 141}";
		public static final String ASCII_142 = "{ASC 142}";
		public static final String ASCII_143 = "{ASC 143}";
		public static final String ASCII_144 = "{ASC 144}";
		public static final String ASCII_145 = "{ASC 145}";
		public static final String ASCII_146 = "{ASC 146}";
		public static final String ASCII_147 = "{ASC 147}";
		public static final String ASCII_148 = "{ASC 148}";
		public static final String ASCII_149 = "{ASC 149}";
		public static final String ASCII_150 = "{ASC 150}";
		public static final String ASCII_151 = "{ASC 151}";
		public static final String ASCII_152 = "{ASC 152}";
		public static final String ASCII_153 = "{ASC 153}";
		public static final String ASCII_154 = "{ASC 154}";
		public static final String ASCII_155 = "{ASC 155}";
		public static final String ASCII_156 = "{ASC 156}";
		public static final String ASCII_157 = "{ASC 157}";
		public static final String ASCII_158 = "{ASC 158}";
		public static final String ASCII_159 = "{ASC 159}";
		public static final String ASCII_160 = "{ASC 160}";
		public static final String ASCII_161 = "{ASC 161}";
		public static final String ASCII_162 = "{ASC 162}";
		public static final String ASCII_163 = "{ASC 163}";
		public static final String ASCII_164 = "{ASC 164}";
		public static final String ASCII_165 = "{ASC 165}";
		public static final String ASCII_166 = "{ASC 166}";
		public static final String ASCII_167 = "{ASC 167}";
		public static final String ASCII_168 = "{ASC 168}";
		public static final String ASCII_169 = "{ASC 169}";
		public static final String ASCII_170 = "{ASC 170}";
		public static final String ASCII_171 = "{ASC 171}";
		public static final String ASCII_172 = "{ASC 172}";
		public static final String ASCII_173 = "{ASC 173}";
		public static final String ASCII_174 = "{ASC 174}";
		public static final String ASCII_175 = "{ASC 175}";
		public static final String ASCII_176 = "{ASC 176}";
		public static final String ASCII_177 = "{ASC 177}";
		public static final String ASCII_178 = "{ASC 178}";
		public static final String ASCII_179 = "{ASC 179}";
		public static final String ASCII_180 = "{ASC 180}";
		public static final String ASCII_181 = "{ASC 181}";
		public static final String ASCII_182 = "{ASC 182}";
		public static final String ASCII_183 = "{ASC 183}";
		public static final String ASCII_184 = "{ASC 184}";
		public static final String ASCII_185 = "{ASC 185}";
		public static final String ASCII_186 = "{ASC 186}";
		public static final String ASCII_187 = "{ASC 187}";
		public static final String ASCII_188 = "{ASC 188}";
		public static final String ASCII_189 = "{ASC 189}";
		public static final String ASCII_190 = "{ASC 190}";
		public static final String ASCII_191 = "{ASC 191}";
		public static final String ASCII_192 = "{ASC 192}";
		public static final String ASCII_193 = "{ASC 193}";
		public static final String ASCII_194 = "{ASC 194}";
		public static final String ASCII_195 = "{ASC 195}";
		public static final String ASCII_196 = "{ASC 196}";
		public static final String ASCII_197 = "{ASC 197}";
		public static final String ASCII_198 = "{ASC 198}";
		public static final String ASCII_199 = "{ASC 199}";
		public static final String ASCII_200 = "{ASC 200}";
		public static final String ASCII_201 = "{ASC 201}";
		public static final String ASCII_202 = "{ASC 202}";
		public static final String ASCII_203 = "{ASC 203}";
		public static final String ASCII_204 = "{ASC 204}";
		public static final String ASCII_205 = "{ASC 205}";
		public static final String ASCII_206 = "{ASC 206}";
		public static final String ASCII_207 = "{ASC 207}";
		public static final String ASCII_208 = "{ASC 208}";
		public static final String ASCII_209 = "{ASC 209}";
		public static final String ASCII_210 = "{ASC 210}";
		public static final String ASCII_211 = "{ASC 211}";
		public static final String ASCII_212 = "{ASC 212}";
		public static final String ASCII_213 = "{ASC 213}";
		public static final String ASCII_214 = "{ASC 214}";
		public static final String ASCII_215 = "{ASC 215}";
		public static final String ASCII_216 = "{ASC 216}";
		public static final String ASCII_217 = "{ASC 217}";
		public static final String ASCII_218 = "{ASC 218}";
		public static final String ASCII_219 = "{ASC 219}";
		public static final String ASCII_220 = "{ASC 220}";
		public static final String ASCII_221 = "{ASC 221}";
		public static final String ASCII_222 = "{ASC 222}";
		public static final String ASCII_223 = "{ASC 223}";
		public static final String ASCII_224 = "{ASC 224}";
		public static final String ASCII_225 = "{ASC 225}";
		public static final String ASCII_226 = "{ASC 226}";
		public static final String ASCII_227 = "{ASC 227}";
		public static final String ASCII_228 = "{ASC 228}";
		public static final String ASCII_229 = "{ASC 229}";
		public static final String ASCII_230 = "{ASC 230}";
		public static final String ASCII_231 = "{ASC 231}";
		public static final String ASCII_232 = "{ASC 232}";
		public static final String ASCII_233 = "{ASC 233}";
		public static final String ASCII_234 = "{ASC 234}";
		public static final String ASCII_235 = "{ASC 235}";
		public static final String ASCII_236 = "{ASC 236}";
		public static final String ASCII_237 = "{ASC 237}";
		public static final String ASCII_238 = "{ASC 238}";
		public static final String ASCII_239 = "{ASC 239}";
		public static final String ASCII_240 = "{ASC 240}";
		public static final String ASCII_241 = "{ASC 241}";
		public static final String ASCII_242 = "{ASC 242}";
		public static final String ASCII_243 = "{ASC 243}";
		public static final String ASCII_244 = "{ASC 244}";
		public static final String ASCII_245 = "{ASC 245}";
		public static final String ASCII_246 = "{ASC 246}";
		public static final String ASCII_247 = "{ASC 247}";
		public static final String ASCII_248 = "{ASC 248}";
		public static final String ASCII_249 = "{ASC 249}";
		public static final String ASCII_250 = "{ASC 250}";
		public static final String ASCII_251 = "{ASC 251}";
		public static final String ASCII_252 = "{ASC 252}";
		public static final String ASCII_253 = "{ASC 253}";
		public static final String ASCII_254 = "{ASC 254}";
		public static final String ASCII_255 = "{ASC 255}";
		public static final String[] ASCII = new String[] { ASCII_0, ASCII_1,
				ASCII_2, ASCII_3, ASCII_4, ASCII_5, ASCII_6, ASCII_7, ASCII_8,
				ASCII_9, ASCII_10, ASCII_11, ASCII_12, ASCII_13, ASCII_14,
				ASCII_15, ASCII_16, ASCII_17, ASCII_18, ASCII_19, ASCII_20,
				ASCII_21, ASCII_22, ASCII_23, ASCII_24, ASCII_25, ASCII_26,
				ASCII_27, ASCII_28, ASCII_29, ASCII_30, ASCII_31, ASCII_32,
				ASCII_33, ASCII_34, ASCII_35, ASCII_36, ASCII_37, ASCII_38,
				ASCII_39, ASCII_40, ASCII_41, ASCII_42, ASCII_43, ASCII_44,
				ASCII_45, ASCII_46, ASCII_47, ASCII_48, ASCII_49, ASCII_50,
				ASCII_51, ASCII_52, ASCII_53, ASCII_54, ASCII_55, ASCII_56,
				ASCII_57, ASCII_58, ASCII_59, ASCII_60, ASCII_61, ASCII_62,
				ASCII_63, ASCII_64, ASCII_65, ASCII_66, ASCII_67, ASCII_68,
				ASCII_69, ASCII_70, ASCII_71, ASCII_72, ASCII_73, ASCII_74,
				ASCII_75, ASCII_76, ASCII_77, ASCII_78, ASCII_79, ASCII_80,
				ASCII_81, ASCII_82, ASCII_83, ASCII_84, ASCII_85, ASCII_86,
				ASCII_87, ASCII_88, ASCII_89, ASCII_90, ASCII_91, ASCII_92,
				ASCII_93, ASCII_94, ASCII_95, ASCII_96, ASCII_97, ASCII_98,
				ASCII_99, ASCII_100, ASCII_101, ASCII_102, ASCII_103,
				ASCII_104, ASCII_105, ASCII_106, ASCII_107, ASCII_108,
				ASCII_109, ASCII_110, ASCII_111, ASCII_112, ASCII_113,
				ASCII_114, ASCII_115, ASCII_116, ASCII_117, ASCII_118,
				ASCII_119, ASCII_120, ASCII_121, ASCII_122, ASCII_123,
				ASCII_124, ASCII_125, ASCII_126, ASCII_127, ASCII_128,
				ASCII_129, ASCII_130, ASCII_131, ASCII_132, ASCII_133,
				ASCII_134, ASCII_135, ASCII_136, ASCII_137, ASCII_138,
				ASCII_139, ASCII_140, ASCII_141, ASCII_142, ASCII_143,
				ASCII_144, ASCII_145, ASCII_146, ASCII_147, ASCII_148,
				ASCII_149, ASCII_150, ASCII_151, ASCII_152, ASCII_153,
				ASCII_154, ASCII_155, ASCII_156, ASCII_157, ASCII_158,
				ASCII_159, ASCII_160, ASCII_161, ASCII_162, ASCII_163,
				ASCII_164, ASCII_165, ASCII_166, ASCII_167, ASCII_168,
				ASCII_169, ASCII_170, ASCII_171, ASCII_172, ASCII_173,
				ASCII_174, ASCII_175, ASCII_176, ASCII_177, ASCII_178,
				ASCII_179, ASCII_180, ASCII_181, ASCII_182, ASCII_183,
				ASCII_184, ASCII_185, ASCII_186, ASCII_187, ASCII_188,
				ASCII_189, ASCII_190, ASCII_191, ASCII_192, ASCII_193,
				ASCII_194, ASCII_195, ASCII_196, ASCII_197, ASCII_198,
				ASCII_199, ASCII_200, ASCII_201, ASCII_202, ASCII_203,
				ASCII_204, ASCII_205, ASCII_206, ASCII_207, ASCII_208,
				ASCII_209, ASCII_210, ASCII_211, ASCII_212, ASCII_213,
				ASCII_214, ASCII_215, ASCII_216, ASCII_217, ASCII_218,
				ASCII_219, ASCII_220, ASCII_221, ASCII_222, ASCII_223,
				ASCII_224, ASCII_225, ASCII_226, ASCII_227, ASCII_228,
				ASCII_229, ASCII_230, ASCII_231, ASCII_232, ASCII_233,
				ASCII_234, ASCII_235, ASCII_236, ASCII_237, ASCII_238,
				ASCII_239, ASCII_240, ASCII_241, ASCII_242, ASCII_243,
				ASCII_244, ASCII_245, ASCII_246, ASCII_247, ASCII_248,
				ASCII_249, ASCII_250, ASCII_251, ASCII_252, ASCII_253,
				ASCII_254, ASCII_255 };
	}

	/**
	 * Changes how "keys" is processed.
	 * 
	 * @author zhengbo.wang
	 */
	public static enum SendFlag {
		/*
		 * (default), Text contains special characters like + and ! to indicate
		 * SHIFT and ALT key presses
		 */
		SEND_SPECIAL_KEYS(0),

		/* keys are sent raw */
		SEND_RAW_KEYS(1);

		private int sendFlag;

		private SendFlag(final int sendFlag) {
			this.sendFlag = sendFlag;
		}

		public int getSendFlag() {
			return sendFlag;
		}

		@Override
		public String toString() {
			return String.valueOf(sendFlag);
		}
	}
}
