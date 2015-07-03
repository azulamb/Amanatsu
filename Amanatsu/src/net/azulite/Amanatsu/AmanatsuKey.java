package net.azulite.Amanatsu;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

import android.view.KeyEvent;

public class AmanatsuKey
{
	// Keyboard.
	/** 戻る */
	public static final int K_BACK			= 4;
	/** メニュー */
	public static final int K_MENU			= 82;

	/** 上(キーボード/ゲームパッド共用) */
	public static final int K_UP				= 19;
	/** 下(キーボード/ゲームパッド共用) */
	public static final int K_DOWN			= 20;
	/** 右(キーボード/ゲームパッド共用) */
	public static final int K_RIGHT		 = 21;
	/** 左(キーボード/ゲームパッド共用) */
	public static final int K_LEFT			= 22;
	/** 上(キーボード/ゲームパッド共用) */
	public static final int PAD_UP			= 19;
	/** 下(キーボード/ゲームパッド共用) */
	public static final int PAD_DOWN		= 20;
	/** 右(キーボード/ゲームパッド共用) */
	public static final int PAD_RIGHT	 = 21;
	/** 左(キーボード/ゲームパッド共用) */
	public static final int PAD_LEFT		= 22;

	/** 0 */
	public static final int K_0				 = 7;
	/** 1 */
	public static final int K_1				 = 8;
	/** 2 */
	public static final int K_2				 = 9;
	/** 3 */
	public static final int K_3				 = 10;
	/** 4 */
	public static final int K_4				 = 11;
	/** 5 */
	public static final int K_5				 = 12;
	/** 6 */
	public static final int K_6				 = 13;
	/** 7 */
	public static final int K_7				 = 15;
	/** 8 */
	public static final int K_8				 = 16;
	/** 9 */
	public static final int K_9				 = 17;

	/** A */
	public static final int K_A				 = 29;
	/** B */
	public static final int K_B				 = 30;
	/** C */
	public static final int K_C				 = 31;
	/** D */
	public static final int K_D				 = 32;
	/** E */
	public static final int K_E				 = 33;
	/** F */
	public static final int K_F				 = 34;
	/** G */
	public static final int K_G				 = 35;
	/** H */
	public static final int K_H				 = 36;
	/** I */
	public static final int K_I				 = 37;
	/** J */
	public static final int K_J				 = 38;
	/** K */
	public static final int K_K				 = 39;
	/** L */
	public static final int K_L				 = 40;
	/** M */
	public static final int K_M				 = 41;
	/** N */
	public static final int K_N				 = 42;
	/** O */
	public static final int K_O				 = 43;
	/** P */
	public static final int K_P				 = 44;
	/** Q */
	public static final int K_Q				 = 45;
	/** R */
	public static final int K_R				 = 46;
	/** S */
	public static final int K_S				 = 47;
	/** T */
	public static final int K_T				 = 48;
	/** U */
	public static final int K_U				 = 49;
	/** V */
	public static final int K_V				 = 50;
	/** W */
	public static final int K_W				 = 51;
	/** X */
	public static final int K_X				 = 52;
	/** Y */
	public static final int K_Y				 = 53;
	/** Z */
	public static final int K_Z				 = 54;

	/** , */
	public static final int K_COMMA		 = 55;
	/** . */
	public static final int K_PERIOD		= 56;
	/** 左ALT */
	public static final int K_LALT			= 57;
	/** 右ALT */
	public static final int K_RALT			= 58;
	/** 左Shift */
	public static final int K_LSHIFT		= 59;
	/** 右Shift */
	public static final int K_RSHIFT		= 60;
	/** タブ */
	public static final int K_TAB			 = 61;
	/** スペース */
	public static final int K_SPACE		 = 62;
	/** エンター */
	public static final int K_ENTER		 = 66;
	/** バックスペース */
	public static final int K_BACKSPACE = 67;
	/** 全角半角 */
	public static final int K_ZENKAKU	 = 68;
	/** - */
	public static final int K_MINUS		 = 69;
	/** - */
	public static final int K_HYPHEN		= 69;
	/** ^ */
	public static final int K_HAT			 = 70;
	/** [ */
	public static final int K_LBRACKET	= 71;
	/** ] */
	public static final int K_RBRACKET	= 72;
	/** \ */
	public static final int K_BACKSLASH = 73;
	/** ; */
	public static final int K_SEMICOLON = 74;
	/** : */
	public static final int K_COLON		 = 75;
	/** / */
	public static final int K_SLASH		 = 76;
	/** {@literal @} */
	public static final int K_AT				= 77;

	/** 検索 */
	public static final int K_SEARCH		= 84;

	/** ゲームパッドの1ボタン */
	public static final int PAD_A			 = 96;
	/** ゲームパッドの2ボタン */
	public static final int PAD_B			 = 97;
	/** ゲームパッドの3ボタン */
	public static final int PAD_C			 = 98;
	/** ゲームパッドの4ボタン */
	public static final int PAD_D			 = 99;
	/** ゲームパッドの5ボタン */
	public static final int PAD_E			 = 100;
	/** ゲームパッドの6ボタン */
	public static final int PAD_F			 = 101;
	/** ゲームパッドの7ボタン */
	public static final int PAD_G			 = 102;
	/** ゲームパッドの8ボタン */
	public static final int PAD_H			 = 103;
	/** ゲームパッドの9ボタン */
	public static final int PAD_I			 = 104;
	/** ゲームパッドの10ボタン */
	public static final int PAD_J			 = 105;

	//public static final int PAD_???		 = 110;

	/** ESC */
	public static final int K_ESC			 = 111;
	/** 左Control */
	public static final int K_LCTRL		 = 113;
	/** 右Control */
	public static final int K_RCTRL		 = 114;
	/** 左Windows */
	public static final int K_LWINDOWS	= 117;
	/** 右Windows */
	public static final int K_RWINDOWS	= 118;

	/** F1 */
	public static final int K_F1				= 131;
	/** F2 */
	public static final int K_F2				= 132;
	/** F3 */
	public static final int K_F3				= 133;
	/** F4 */
	public static final int K_F4				= 134;
	/** F5 */
	public static final int K_F5				= 135;
	/** F6 */
	public static final int K_F6				= 136;
	/** F7 */
	public static final int K_F7				= 137;
	/** F8 */
	public static final int K_F8				= 138;
	/** F9 */
	public static final int K_F9				= 139;
	/** F10 */
	public static final int K_F10			 = 140;
	/** F11 */
	public static final int K_F11			 = 141;
	/** F12 */
	public static final int K_F12			 = 142;

	/** NumLock */
	public static final int K_NUMLOCK	 = 143;
	/** テンキーの0 */
	public static final int K_NUMPAD0	 = 144;
	/** テンキーの1 */
	public static final int K_NUMPAD1	 = 145;
	/** テンキーの2 */
	public static final int K_NUMPAD2	 = 146;
	/** テンキーの3 */
	public static final int K_NUMPAD3	 = 147;
	/** テンキーの4 */
	public static final int K_NUMPAD4	 = 148;
	/** テンキーの5 */
	public static final int K_NUMPAD5	 = 149;
	/** テンキーの6 */
	public static final int K_NUMPAD6	 = 150;
	/** テンキーの7 */
	public static final int K_NUMPAD7	 = 151;
	/** テンキーの8 */
	public static final int K_NUMPAD8	 = 152;
	/** テンキーの9 */
	public static final int K_NUMPAD9	 = 153;
	/** テンキーの/ */
	public static final int K_DIV			 = 154;
	/** テンキーの{@literal *} */
	public static final int K_MUL			 = 155;
	/** テンキーの- */
	public static final int K_SUB			 = 156;
	/** テンキーの+ */
	public static final int K_ADD			 = 157;

	/** テンキーの. */ //TODO
	public static final int K_DECIMAL	 = 168;

	/** CapsLock */
	public static final int K_CAPSLOCK	= 212;
	/** 無変換 */
	public static final int K_MUHENKAN	= 213;
	/** 変換 */
	public static final int K_HENKAN		= 214;
	/** かな */
	public static final int K_KANA			= 215;
	/** ￥？ */ //TODO
	public static final int K_YEN			 = 216;

	private static Map<Integer, Key> keyboard = new Hashtable<Integer, Key>();
	private int lastkey;
	private Key key;

	public synchronized boolean updateKey()
	{
		Iterator< Map.Entry<Integer, Key> > itk;
		Map.Entry<Integer, Key> entryk;
		Key key;

		for ( itk = keyboard.entrySet().iterator() ; itk.hasNext() ; )
		{
			entryk = itk.next();
			key = entryk.getValue();
			if ( key.pushed == false )
			{
				if ( key.frame < 0 )
				{
					key.frame = 0;
					continue;
				} else
				{
					key.frame = -1;
				}
			} else
			{
				++key.frame;
			}
			key.pushed = false;
		}
		return true;
	}

	public boolean keyDown( int keycode, KeyEvent event )
	{
		if ( keyboard.containsKey( keycode ) )
		{
			key = keyboard.get( keycode );
		} else
		{
			key = new Key();
			keyboard.put( keycode, key );
		}
		key.pushed = true;
		lastkey = keycode;
		return true;
	}

	public boolean keyUp( int keycode, KeyEvent event )
	{
		if ( keyboard.containsKey( keycode ) )
		{
			key = keyboard.get( keycode );
		} else
		{
			key = new Key();
			keyboard.put( keycode, key );
		}
		key.pushed = false;
		return true;
	}

	public int getKey( int keycode )
	{
		if ( keyboard.containsKey( keycode ) )
		{
			return keyboard.get( keycode ).frame;
		}
		return 0;
	}

	public int getLastKey()
	{
		return lastkey;
	}
}

class Key
{
	int code;
	int frame;
	boolean pushed;
}
