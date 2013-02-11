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
  static private String VERSION = "0.0.5";
  public static final int DRAW_TRC = 0;
  public static final int DRAW_ADD = 1;
//  public static final int DRAW_SUB = 2;
  public static final int DRAW_MUL = 3;

  Context context;
  AmanatsuGLView view;

  GameView gview;

  GameGLSurfaceViewRender render;
  AmanatsuInput input;
  AmanatsuSound sound;

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

    view = new AmanatsuGLView( this );

    this.gview = gview;

    render = new GameGLSurfaceViewRender( this );
    render.SetGLLoop( new GLLoopAmanatsuOP( this, logo ) );

    view.setRenderer( render );
    view.setRenderMode( GLSurfaceView.RENDERMODE_WHEN_DIRTY );

    if ( multitouch )
    {
      this.SetInput( new MultiTouchEvent() );
    } else
    {
      this.SetInput( new TouchEvent() );      
    }

    this.SetSound( new AmanatsuSound( this ) );
  }

  /**
   * Start game.
   */
  public boolean Start()
  {
    render.Start();
    return true;
  }

  /**
   * End game.
   */
  public void Stop()
  {
    //timer.stop();
    render.Term();
  }

  // Setter.
  public GameView SetGameView( GameView view )
  {
    GameView ret = this.gview;
    this.gview = view;
    render.SetGameView( view );// TODO Lock
    return ret;
  }

  public AmanatsuInput SetInput( AmanatsuInput input )
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

  public AmanatsuSound SetSound( AmanatsuSound sound )
  {
    AmanatsuSound ret = this.sound;
    this.sound = sound;
    return ret;
  }

  // Getter.
  public String GetVersion(){ return VERSION; }
  public Context GetContext(){ return context; }

  public GLSurfaceView GetGLSurfaceView(){ return view; }

}

class AmanatsuGLView extends GLSurfaceView
{
  Amanatsu ama;
  public AmanatsuGLView( Amanatsu ama )
  {
    super( ama.GetContext() );

    this.ama = ama;

    // Enable touch.
    this.setFocusable( true );
  }

  public boolean onTouchEvent( MotionEvent event )
  {
    return this.ama.input.Touch( event );
  }

  @Override
  public boolean onKeyDown( int keyCode, KeyEvent event )
  {
    return this.ama.input.KeyDown( keyCode, event );
  }

  @Override
  public boolean onKeyUp( int keyCode, KeyEvent event )
  {
    return this.ama.input.KeyUp( keyCode, event );
  }
}

class GameGLSurfaceViewRender extends Handler implements GLSurfaceView.Renderer
{
  Amanatsu ama;
  AmanatsuGLView glview;
  GameView view = null;
  AmanatsuDraw draw;
  protected GLLoop loop;
  boolean loopflag = false;

  // FPS
  private long before, now, progress, idol;
  private int frame;
  float fps = 30.0f, nowfps;
  private float countfps;

  public GameGLSurfaceViewRender( Amanatsu ama )
  {
    this.ama = ama;
    glview = ama.view;
    this.SetGameView( ama.gview );
    draw = new AmanatsuDraw( ama );
  }

  public void SetGLLoop( GLLoop loop )
  {
    this.loop = loop;
  }

  protected GameView SetGameView( GameView view )
  {
    GameView ret;
    ret = this.view;
    this.view = view;
    return ret;
  }

  public void Term()
  {
    view.CleanUp( draw, ama.input, ama.sound );
  }

  public void Start()
  {
    loopflag = true;
    before = System.currentTimeMillis();
    sendMessageDelayed( obtainMessage( 0 ), 0 );
  }

  public void Stop()
  {
    loopflag = false;
  }

  public float GetFPS()
  {
    return nowfps;
  }

  public float SetFPS( float fps)
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

    draw.SetGL( gl );

    gl.glEnable( GL10.GL_BLEND );
    draw.SetRender( Amanatsu.DRAW_TRC );

    ama.input.Update();
    loop.Run( draw );

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
    draw.width = width;
    draw.height = height;
    gl.glViewport( 0, 0, draw.width, draw.height );
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

interface GLLoop{ public void Run( AmanatsuDraw draw ); }

class GLLoopAmanatsuOP implements GLLoop
{
  GameView view;
  Amanatsu ama;
  int counter;
  boolean logo;

  public GLLoopAmanatsuOP( Amanatsu ama, boolean logo )
  {
    this.ama = ama;
    this.view = ama.render.view;
    counter = -1;
    this.logo = logo;
  }

  @Override
  public void Run( AmanatsuDraw draw )
  {
    draw.ClearScreen();
    if ( counter < 0 )
    {
      counter = 0;
      draw.Init();
      try
      {
        URL filename = this.getClass().getResource( "/res/raw/logo.png" );
        InputStream input = filename.openStream();
        Bitmap bmp = BitmapFactory.decodeStream( input );
        draw.CreateTexture( 0, bmp );
        counter = 1;
      } catch (IOException e)
      {
      }
    } else if ( counter == 120 || logo == false )
    {
      // End.
      draw.DestroyTexture( 0 );
      ama.render.loop = new GLLoopUserInit( ama );
    } else if ( counter > 0 )
    {
      // OP
      int max = draw.width < draw.height ? draw.width : draw.height;
      max *= 0.4;
      if ( counter < 40 )
      {
        draw.SetColor( 0, counter / 40.0f );
      } else if ( counter >= 80 )
      {
        draw.SetColor( 0, (120 - counter) / 40.0f );
      } else
      {
        draw.Printf( 0, draw.width / 2.0f - 50, draw.height / 2.0f + max / 2.0f, ama.GetVersion() );
      }
      draw.DrawTextureScaring( 0, 0, 0, 256, 256, draw.width / 2 - max / 2, draw.height / 2 - max / 2, max, max );
      ++counter;
    }
  }
}

class GLLoopUserInit implements GLLoop
{
  GameView view;
  Amanatsu ama;
  int run = 0;
  public GLLoopUserInit( Amanatsu ama )
  {
    this.ama = ama;
    this.view = ama.render.view;
    this.run = 0;
  }

  @Override
  public void Run( AmanatsuDraw draw )
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
  Amanatsu ama;
  GameView view;
  public GLLoopMainLoop( Amanatsu ama )
  {
    this.ama = ama;
    this.view = ama.render.view;
  }

  @Override
  public void Run( AmanatsuDraw draw )
  {
    if ( view.MainLoop( draw, ama.input, ama.sound ) == false )
    {
      ama.render.loop = new GLLoopCleanUp( ama );
    }
  }
}

class GLLoopCleanUp implements GLLoop
{
  GameView view;
  Amanatsu ama;

  public GLLoopCleanUp( Amanatsu ama )
  {
    this.view = ama.render.view;
    this.ama = ama;
  }

  @Override
  public void Run( AmanatsuDraw draw)
  {
    view.CleanUp( draw, ama.input, ama.sound );
    ama.Stop();
    draw.Release();
    ama.sound.Release();
    ( (Activity) ama.context ).finish();
  }
  
}

class TouchEvent implements AmanatsuInput
{
  private float x, y;
  private boolean touched;
  private int frame;
  static Map<Integer, Key> keyboard;

  // tmp;
  Key key;

  public TouchEvent()
  {
    keyboard = new Hashtable<Integer, Key>();
    touched = false;
  }

  @Override
  public synchronized boolean Update()
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
  public synchronized boolean Touch( MotionEvent event )
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
  public float GetX()
  {
    return x;
  }

  @Override
  public float GetY()
  {
    return y;
  }

  @Override
  public int GetTouchFrame()
  {
    return frame;
  }

  @Override
  public int Size()
  {
    return (frame > 0) ? 1 : 0;
  }

  @Override
  public float GetX( int num )
  {
    return x;
  }

  @Override
  public float GetY( int num )
  {
    return y;
  }

  @Override
  public int GetTouchFrame( int num )
  {
    return frame;
  }

  @Override
  public float GetFingerX( int fingerid )
  {
    return x;
  }

  @Override
  public float GetFingerY( int fingerid )
  {
    return y;
  }

  @Override
  public int GetFingerTouchFrame( int fingerid )
  {
    return frame;
  }

  @Override
  public boolean KeyDown( int keycode, KeyEvent event )
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
    return true;
  }

  @Override
  public boolean KeyUp( int keycode, KeyEvent event )
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
  public int GetKey( int keycode )
  {
    if ( keyboard.containsKey( keycode ) )
    {
      return keyboard.get( keycode ).frame;
    }
    return 0;
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
  static Map<Integer, Finger> finger;
  static Map<Integer, Key> keyboard;


  // tmp;
  Iterator< Map.Entry<Integer, Finger> > itf;
  Map.Entry<Integer, Finger> entryf;
  Finger fin;
  Key key;

  boolean lock = false;

  public MultiTouchEvent()
  {
    finger = new Hashtable<Integer, Finger>();
    keyboard = new Hashtable<Integer, Key>();
    touched = false;
  }

  @Override
  public synchronized boolean Update()
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
  public synchronized boolean Touch( MotionEvent event )
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
  public float GetX()
  {
    return x;
  }

  @Override
  public float GetY()
  {
    return y;
  }

  @Override
  public int GetTouchFrame()
  {
    return frame;
  }

  @Override
  public int Size()
  {
    return len;
  }

  @Override
  public float GetX( int num )
  {
    return mx[ num ];
  }

  @Override
  public float GetY( int num )
  {
    return my[ num ];
  }

  @Override
  public int GetTouchFrame( int num )
  {
    if ( num >= fid.length || finger.containsKey( fid[ num ] ) == false ){ return -1; }
    return finger.get( fid[ num ] ).frame;
  }

  @Override
  public float GetFingerX( int fingerid )
  {
    if ( finger.containsKey( fingerid ) == false ){ return 0.0f; }
    return finger.get( fingerid ).x;
  }

  @Override
  public float GetFingerY( int fingerid )
  {
    if ( finger.containsKey( fingerid ) == false ){ return 0.0f; }
    return finger.get( fingerid ).y;
  }

  @Override
  public int GetFingerTouchFrame( int fingerid )
  {
    fin = finger.get( fingerid );
    if ( fin == null ){ return 0; }
    return fin.frame;
  }

  @Override
  public boolean KeyDown( int keycode, KeyEvent event )
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
    return true;
  }

  @Override
  public boolean KeyUp( int keycode, KeyEvent event )
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
  public int GetKey( int keycode )
  {
    if ( keyboard.containsKey( keycode ) )
    {
      return keyboard.get( keycode ).frame;
    }
    return 0;
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


