package net.azulite.Amanatsu;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

import android.view.KeyEvent;

public class AmanatsuKey
{
  // Keyboard.
  public static final int K_BACK = 4;
  public static final int K_MENU = 82;

  public static final int K_UP = 19;
  public static final int K_DOWN = 20;
  public static final int K_RIGHT = 21;
  public static final int K_LEFT = 22;

  public static final int K_0 = 7;
  public static final int K_1 = 8;
  public static final int K_2 = 9;
  public static final int K_3 = 10;
  public static final int K_4 = 11;
  public static final int K_5 = 12;
  public static final int K_6 = 13;
  public static final int K_7 = 15;
  public static final int K_8 = 16;

  public static final int K_A = 29;
  public static final int K_B = 30;
  public static final int K_C = 31;
  public static final int K_D = 32;
  public static final int K_E = 33;
  public static final int K_F = 34;
  public static final int K_G = 35;
  public static final int K_H = 36;
  public static final int K_I = 37;
  public static final int K_J = 38;
  public static final int K_K = 39;
  public static final int K_L = 40;
  public static final int K_M = 41;
  public static final int K_N = 42;
  public static final int K_O = 43;
  public static final int K_P = 44;
  public static final int K_Q = 45;
  public static final int K_R = 46;
  public static final int K_S = 47;
  public static final int K_T = 48;
  public static final int K_U = 49;
  public static final int K_V = 50;
  public static final int K_W = 51;
  public static final int K_X = 52;
  public static final int K_Y = 53;
  public static final int K_Z = 54;

  public static final int K_COMMA = 55;
  public static final int K_PERIOD = 56;
  public static final int K_LALT = 57;
  public static final int K_RALT = 58;

  public static final int K_LSHIFT = 59;
  public static final int K_RSHIFT = 60;
  public static final int K_TAB = 61;
  public static final int K_SPACE = 62;
  public static final int K_ENTER = 66;
  public static final int K_BACKSPACE = 67;
  public static final int K_ZENKAKU = 68;
  public static final int K_MINUS = 69;
  public static final int K_HAT = 70;
  public static final int K_LBRACKET = 71;//[ TODO
  public static final int K_RBRACKET = 72;//] TODO
  public static final int K_BACKSLASH = 73;
  public static final int K_SEMICORON = 74;
  public static final int K_CORON = 75;
  public static final int K_SLASH = 76;
  public static final int K_AT = 77;

  public static final int K_SEARCH = 84;

  public static final int K_ESC = 111;
  public static final int K_LCTRL = 113;
  public static final int K_RCTRL = 114;
  public static final int K_LWINDOWS = 117;
  public static final int K_RWINDOWS = 118;

  public static final int K_F1 = 131;
  public static final int K_F2 = 132;
  public static final int K_F3 = 133;
  public static final int K_F4 = 134;
  public static final int K_F5 = 135;
  public static final int K_F6 = 136;
  public static final int K_F7 = 137;
  public static final int K_F8 = 138;
  public static final int K_F9 = 139;
  public static final int K_F10 = 140;
  public static final int K_F11 = 141;
  public static final int K_F12 = 142;

  public static final int K_NUMLOCK = 143;
  public static final int K_NUMPAD0 = 144;
  public static final int K_NUMPAD1 = 145;
  public static final int K_NUMPAD2 = 146;
  public static final int K_NUMPAD3 = 147;
  public static final int K_NUMPAD4 = 148;
  public static final int K_NUMPAD5 = 149;
  public static final int K_NUMPAD6 = 150;
  public static final int K_NUMPAD7 = 151;
  public static final int K_NUMPAD8 = 152;
  public static final int K_NUMPAD9 = 153;
  public static final int K_DIV = 154;
  public static final int K_MUL = 155;
  public static final int K_SUB = 156;
  public static final int K_ADD = 157;
  public static final int K_DECIMAL = 168;

  public static final int K_CAPSLOCK = 212;
  public static final int K_MUHENKAN = 213;
  public static final int K_HENKAN = 214;
  public static final int K_KANA = 215;
  public static final int K_YEN = 216;

  private static Map<Integer, Key> keyboard = new Hashtable<Integer, Key>();
  private int lastkey;
  private Key key;

  public synchronized boolean updateKey()
  {
    Iterator< Map.Entry<Integer, Key> > itk;
    Map.Entry<Integer, Key> entryk;
    Key key;

    for ( itk = keyboard.entrySet().iterator(); itk.hasNext() ; )
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
