package net.azulite.Amanatsu;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLSurfaceView;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.MotionEvent;

import net.azulite.Amanatsu.GameView;

/**
 * @author Hiroki
 * @version 0.0.3
 */

// Library
// TODO
// * enable back to exit
// * CreateTexture
// * disable amanatsu thema
// * screen size
// * fps

/**
 * Amanatsu
 * <pre>
 * Amanatsu main class.
 * </pre>
 */
public class Amanatsu
{
  private static String VERSION = "0.0.6";

  public static final int DRAW_TRC = 0;
  public static final int DRAW_ADD = 1;
//  public static final int DRAW_SUB = 2;
  public static final int DRAW_MUL = 3;

  public static final int K_BACK = 4;

  public static final int K_0 = 7;
  public static final int K_1 = 8;
  public static final int K_2 = 9;
  public static final int K_3 = 10;
  public static final int K_4 = 11;
  public static final int K_5 = 12;
  public static final int K_6 = 13;
  public static final int K_7 = 15;
  public static final int K_8 = 16;

  public static final int K_UP = 19;
  public static final int K_DOWN = 20;
  public static final int K_RIGHT = 21;
  public static final int K_LEFT = 22;

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
  public static final int K_LBRACKET = 71;//[
  public static final int K_RBRACKET = 72;//]
  public static final int K_BACKSLASH = 73;
  public static final int K_SEMICORON = 74;
  public static final int K_CORON = 75;
  public static final int K_SLASH = 76;
  public static final int K_AT = 77;
  public static final int K_MENU = 82;
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

  private Context context;
  protected AmanatsuGLView view;

  protected GameView gview;

  protected GameGLSurfaceViewRender render;
  protected AmanatsuInput input;
  protected AmanatsuSound sound;

  /**
   * Amanatsu
   * @param Content Activity instance.
   * @param GameView Class instance(implements GameView).
   */
  public Amanatsu( Context context, GameView gview )
  {
    this( context, gview, true, true );
  }

  public Amanatsu( Context context, GameView gview, boolean multitouch )
  {
    this( context, gview, multitouch, true );
  }

  public Amanatsu( Context context, GameView gview, boolean multitouch, boolean logo )
  {
    this.context = context;

    if ( multitouch )
    {
      setInput( new MultiTouchEvent() );
    } else
    {
      setInput( new TouchEvent() );      
    }

    setSound( new AmanatsuSound( this ) );

    view = new AmanatsuGLView( this );

    this.gview = gview;

    render = new GameGLSurfaceViewRender( this );
    render.setGLLoop( new GLLoopAmanatsuOP( this, logo ) );

    view.setRenderer( render );
    view.setRenderMode( GLSurfaceView.RENDERMODE_WHEN_DIRTY );
  }

  /**
   * Start game.
   */
  public boolean start()
  {
    render.start();
    return true;
  }

  /**
   * End game.
   */
  public void stop()
  {
    //timer.stop();
    render.term();
  }

  // Setter.
  public GameView setGameView( GameView view )
  {
    GameView ret = this.gview;
    this.gview = view;
    render.setGameView( view );// TODO Lock
    return ret;
  }

  public AmanatsuInput setInput( AmanatsuInput input )
  {
    AmanatsuInput ret = this.input;
    this.input = input;
    /*int n;
    Class<?>[] interfaces = input.getClass().getInterfaces();
    for ( n = 0 ; n < interfaces.length ; ++n )
    {
      if ( interfaces[ n ] == AmanatsuInput.class ){}
    }*/
    return ret;
  }

  public AmanatsuSound setSound( AmanatsuSound sound )
  {
    AmanatsuSound ret = this.sound;
    this.sound = sound;
    return ret;
  }

  // Getter.
  public static final String getVersion(){ return VERSION; }
  public final Context getContext(){ return context; }

  public final GLSurfaceView getGLSurfaceView(){ return view; }

}

class AmanatsuGLView extends GLSurfaceView
{
  private AmanatsuInput input;
  public AmanatsuGLView( Amanatsu ama )
  {
    super( ama.getContext() );

    input = ama.input;
    // Enable touch.
    setFocusable( true );
  }

  public boolean onTouchEvent( MotionEvent event )
  {
    return input.touch( event );
  }

  @Override
  public boolean onKeyDown( int keyCode, KeyEvent event )
  {
    return input.keyDown( keyCode, event );
  }

  @Override
  public boolean onKeyUp( int keyCode, KeyEvent event )
  {
    return input.keyUp( keyCode, event );
  }
}

class GameGLSurfaceViewRender extends Handler implements GLSurfaceView.Renderer
{
  private Amanatsu ama;
  private AmanatsuGLView glview;
  protected GameView view = null;
  private AmanatsuDraw draw;
  protected GLLoop loop;
  private boolean loopflag = false;

  // FPS
  private long before, now, progress, idol;
  private int frame;
  float fps = 30.0f, nowfps;
  private float countfps;

  public GameGLSurfaceViewRender( Amanatsu ama )
  {
    this.ama = ama;
    glview = ama.view;
    setGameView( ama.gview );
    draw = new AmanatsuDraw( ama );
  }

  public void setGLLoop( GLLoop loop )
  {
    this.loop = loop;
  }

  protected GameView setGameView( GameView view )
  {
    GameView ret = this.view;
    this.view = view;
    return ret;
  }

  public void term()
  {
    view.CleanUp( draw, ama.input, ama.sound );
  }

  public void start()
  {
    loopflag = true;
    before = System.currentTimeMillis();
    sendMessageDelayed( obtainMessage( 0 ), 0 );
  }

  public void stop()
  {
    loopflag = false;
  }

  public final float getFps()
  {
    return nowfps;
  }

  public final float setFps( float fps)
  {
    float ret = this.fps;
    this.fps = fps;
    return ret;
  }

  @Override
  public void handleMessage( Message msg )
  {
    glview.requestRender();
  }

  @Override
  public void onDrawFrame( GL10 gl )
  {
    draw.setGL( gl );

    gl.glEnable( GL10.GL_BLEND );
    draw.setRender( Amanatsu.DRAW_TRC );

    ama.input.update();
    loop.run( draw );

    gl.glDisable( GL10.GL_BLEND );

    now = System.currentTimeMillis();
    progress = now - before;
    countfps += 1.0;
    ++frame;
    if ( progress >= 1000 )
    {
      progress = 0;
      before = now;
      frame = 0;
      nowfps = countfps;
      countfps = 0.0f;
    }
    idol = (int)((float)frame * 1000.0f / fps ) - progress;
    if ( loopflag ){ sendMessageDelayed( obtainMessage( 0 ), idol ); }
  }

  @Override
  public void onSurfaceChanged( GL10 gl, int width, int height )
  {
    draw.setWidth( width );
    draw.setHeight( height );
    gl.glViewport( 0, 0, width, height );
    gl.glMatrixMode( GL10.GL_PROJECTION );
    gl.glLoadIdentity();
    gl.glOrthof( 0.0f, width, height, 0.0f, 50.0f, -50.0f );
  }

  @Override
  public void onSurfaceCreated( GL10 gl, EGLConfig config )
  {
    // TODO Auto-generated method stub
  }

}

interface GLLoop{ public void run( AmanatsuDraw draw ); }

class GLLoopAmanatsuOP implements GLLoop
{
  private Amanatsu ama;
  private int counter;
  private boolean logo;

  public GLLoopAmanatsuOP( Amanatsu ama, boolean logo )
  {
    this.ama = ama;
    counter = -1;
    this.logo = logo;
  }

  @Override
  public void run( AmanatsuDraw draw )
  {
    draw.clearScreen();
    if ( counter < 0 )
    {
      counter = 0;
      draw.init();
      try
      {
        URL filename = getClass().getResource( "/res/raw/logo.png" );
        InputStream input = filename.openStream();
        Bitmap bmp = BitmapFactory.decodeStream( input );
        draw.createTexture( 0, bmp );
        counter = 1;
      } catch (IOException e)
      {
      }
    } else if ( counter == 120 || logo == false )
    {
      // End.
      draw.destroyTexture( 0 );
      ama.render.loop = new GLLoopUserInit( ama );
    } else if ( counter > 0 )
    {
      // OP
      int max = draw.getWidth() < draw.getHeight() ? draw.getWidth() : draw.getHeight();
      max *= 0.4;
      if ( counter < 40 )
      {
        draw.setColor( 0, counter / 40.0f );
      } else if ( counter >= 80 )
      {
        draw.setColor( 0, (120 - counter) / 40.0f );
      } else
      {
        draw.printf( 0, draw.getWidth() / 2.0f - 50, draw.getHeight() / 2.0f + max / 2.0f, Amanatsu.getVersion() );
      }
      draw.drawTextureScaring( 0, 0, 0, 256, 256, draw.getWidth() / 2 - max / 2, draw.getHeight() / 2 - max / 2, max, max );
      ++counter;
    }
  }
}

class GLLoopUserInit implements GLLoop
{
  private GameView view;
  private Amanatsu ama;
  private int run = 0;

  public GLLoopUserInit( Amanatsu ama )
  {
    this.ama = ama;
    this.view = ama.render.view;
    this.run = 0;
  }

  @Override
  public void run( AmanatsuDraw draw )
  {
    if ( run == 0 )
    {
      run = 1;
      view.UserInit( draw, ama.input, ama.sound );
      ama.render.loop = new GLLoopMainLoop( ama );
    }
  }
}

class GLLoopMainLoop implements GLLoop
{
  private Amanatsu ama;
  private GameView view;
  private AmanatsuInput input;
  private AmanatsuSound sound;

  public GLLoopMainLoop( Amanatsu ama )
  {
    this.ama = ama;
    this.view = ama.render.view;
    this.input = ama.input;
    this.sound = ama.sound;
  }

  @Override
  public void run( AmanatsuDraw draw )
  {
    if ( view.MainLoop( draw, input, sound ) == false )
    {
      ama.render.loop = new GLLoopCleanUp( ama );
    }
  }
}

class GLLoopCleanUp implements GLLoop
{
  private GameView view;
  private Amanatsu ama;
  private int run;

  public GLLoopCleanUp( Amanatsu ama )
  {
    this.view = ama.render.view;
    this.ama = ama;
    this.run = 0;
  }

  @Override
  public void run( AmanatsuDraw draw)
  {
    if ( run == 0 )
    {
      run = 1;
      view.CleanUp( draw, ama.input, ama.sound );
      ama.stop();
      draw.release();
      ama.sound.release();
      ( (Activity) ama.getContext() ).finish();
    }
  }
  
}

class TouchEvent implements AmanatsuInput
{
  private float x, y;
  private boolean touched;
  private int frame;
  private static Map<Integer, Key> keyboard = new Hashtable<Integer, Key>();
  private int lastkey;
  // tmp;
  private Key key;

  public TouchEvent()
  {
    touched = false;
  }

  @Override
  public synchronized boolean update()
  {
    Iterator< Map.Entry<Integer, Key> > itk;
    Map.Entry<Integer, Key> entryk;
    Key key;

    if ( touched )
    {
      ++frame;
    } else if ( frame > 0 )
    {
      frame = -1;
    } else
    {
      frame = 0;
    }

    // Key Input.

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

  @Override
  public synchronized boolean touch( MotionEvent event )
  {
    this.x = event.getX();
    this.y = event.getY();

    if ( event.getAction() == MotionEvent.ACTION_UP )
    {
      touched = false;
    }else
    {
      touched = true;
    }

    return true;
  }

  @Override
  public float getX()
  {
    return x;
  }

  @Override
  public float getY()
  {
    return y;
  }

  @Override
  public int getTouchFrame()
  {
    return frame;
  }

  @Override
  public int size()
  {
    return (frame > 0) ? 1 : 0;
  }

  @Override
  public float getX( int num )
  {
    return x;
  }

  @Override
  public float getY( int num )
  {
    return y;
  }

  @Override
  public int getTouchFrame( int num )
  {
    return frame;
  }

  @Override
  public float getFingerX( int fingerid )
  {
    return x;
  }

  @Override
  public float getFingerY( int fingerid )
  {
    return y;
  }

  @Override
  public int getFingerTouchFrame( int fingerid )
  {
    return frame;
  }

  @Override
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

  @Override
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

  @Override
  public int getKey( int keycode )
  {
    if ( keyboard.containsKey( keycode ) )
    {
      return keyboard.get( keycode ).frame;
    }
    return 0;
  }

  @Override
  public int getLastKey()
  {
    return lastkey;
  }
}

class MultiTouchEvent implements AmanatsuInput
{
  private float x, y;
  private boolean touched;
  private int frame;
  private float[] mx, my;
  private int[] fid;
  private int len = 0, max = 0;
  private static Map<Integer, Finger> finger = new Hashtable<Integer, Finger>( 15 );
  private static Map<Integer, Key> keyboard = new Hashtable<Integer, Key>();
  private int lastkey;

  // tmp;
  private Iterator< Map.Entry<Integer, Finger> > itf;
  private Map.Entry<Integer, Finger> entryf;
  private Finger fin;
  private Key key;

  boolean lock = false;

  public MultiTouchEvent()
  {
    touched = false;
  }

  @Override
  public synchronized boolean update()
  {
    Iterator< Map.Entry<Integer, Key> > itk;
    Map.Entry<Integer, Key> entryk;
    Finger fin;
    Key key;

    len = 0;

    if ( touched )
    {
      ++frame;
    } else if ( frame > 0 )
    {
      frame = -1;
    } else
    {
      frame = 0;
    }

    for ( itf = finger.entrySet().iterator(); itf.hasNext() ; )
    {
      // java.util.concurrentmodificationexception
      entryf = itf.next();
      fin = entryf.getValue();
      if ( fin.touched == false )
      {
        if ( fin.frame < 0 )
        {
          itf.remove();
          continue;
        } else
        {
          fin.frame = -1;
        }
      } else
      {
        ++fin.frame;
      }
      //fin.touched = false;
      ++len;
    }

    // Key Input.

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

  @Override
  public synchronized boolean touch( MotionEvent event )
  {
    int n, id;

    this.x = event.getX();
    this.y = event.getY();
    if ( event.getAction() == MotionEvent.ACTION_UP )
    {
      touched = false;
    }else
    {
      touched = true;
    }

    int len = event.getPointerCount();
    if ( len > max )
    {
      max = len;
      mx = new float[ max ];
      my = new float[ max ];
      fid = new int[ max ];
    }

    for ( itf = finger.entrySet().iterator(); itf.hasNext() ; )
    {
      entryf = itf.next();
      entryf.getValue().touched = false;
    }

    if ( event.getAction() != MotionEvent.ACTION_UP )
    {
      for ( n = 0 ; n < len ; ++n )
      {
        id = event.getPointerId( n );
        if ( finger.get( id ) == null )
        {
          fin = new Finger();
          finger.put( id, fin );
          fin.frame = 0;
        } else
        {
          fin = finger.get( id );
        }

        // Finger position.
        // NullPo?
        fin.x = event.getX( n );
        fin.y = event.getY( n );

        // Array
        // Finger id.
        fid[ n ] = id;
        mx[ n ] = fin.x;
        my[ n ] = fin.y;

        // Touched.
        fin.touched = true;
      }
    }

    return true;
  }

  @Override
  public float getX()
  {
    return x;
  }

  @Override
  public float getY()
  {
    return y;
  }

  @Override
  public int getTouchFrame()
  {
    return frame;
  }

  @Override
  public int size()
  {
    return len;
  }

  @Override
  public float getX( int num )
  {
    return mx[ num ];
  }

  @Override
  public float getY( int num )
  {
    return my[ num ];
  }

  @Override
  public int getTouchFrame( int num )
  {
    if ( num >= fid.length || finger.containsKey( fid[ num ] ) == false ){ return -1; }
    return finger.get( fid[ num ] ).frame;
  }

  @Override
  public float getFingerX( int fingerid )
  {
    if ( finger.containsKey( fingerid ) == false ){ return 0.0f; }
    return finger.get( fingerid ).x;
  }

  @Override
  public float getFingerY( int fingerid )
  {
    if ( finger.containsKey( fingerid ) == false ){ return 0.0f; }
    return finger.get( fingerid ).y;
  }

  @Override
  public int getFingerTouchFrame( int fingerid )
  {
    fin = finger.get( fingerid );
    if ( fin == null ){ return 0; }
    return fin.frame;
  }

  @Override
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

  @Override
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

  @Override
  public int getKey( int keycode )
  {
    if ( keyboard.containsKey( keycode ) )
    {
      return keyboard.get( keycode ).frame;
    }
    return 0;
  }

  @Override
  public int getLastKey()
  {
    return lastkey;
  }
}

class Finger
{
  float x, y;
  int id;
  int frame;
  boolean touched;
}

class Key
{
  int code;
  int frame;
  boolean pushed;
}


