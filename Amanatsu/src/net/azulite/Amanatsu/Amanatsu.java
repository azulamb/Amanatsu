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

// Library
// TODO
// * enable back to exit

public class Amanatsu
{
  static private String VERSION = "0.0.1";
  Context context;
  AmanatsuGLView view;
  GameView gview;

  GameGLSurfaceViewRender render;
  AmanatsuInput input = null;
  GameTimer timer;

  public Amanatsu( Context context, GameView gview )
  {
    this.context = context;

    view = new AmanatsuGLView( this );

    this.gview = gview;

    render = new GameGLSurfaceViewRender( this );
    render.SetGLLoop( new GLLoopAmanatsuOP( this ) );

    view.setRenderer( render );

    this.SetInput( new TouchEvent() );

    timer = new GameTimer( view, 30 );
  }

  public boolean Start()
  {
    timer.start();
    return true;
  }

  public void Stop()
  {
    timer.stop();
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

  // Getter.
  static public String GetVersion(){ return VERSION; }
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

    // タッチイベントを取得可能にする。
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

class GameGLSurfaceViewRender implements GLSurfaceView.Renderer
{
  Amanatsu ama;
  GameView view = null;
  AmanatsuDraw draw;
  protected GLLoop loop;

  public GameGLSurfaceViewRender( Amanatsu ama )
  {
    this.ama = ama;
    this.SetGameView( ama.gview );
    draw = new AmanatsuDraw( ama.context );
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
    view.CleanUp( draw );
  }

  @Override
  public void onDrawFrame( GL10 gl )
  {
    draw.SetGL( gl );

    gl.glEnable( GL10.GL_BLEND );
    gl.glBlendFunc( GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA );

    ama.input.Update();
    loop.Run( draw );

    gl.glDisable( GL10.GL_BLEND );
  }

  @Override
  public void onSurfaceChanged( GL10 gl, int width, int height )
  {
    draw.width = width;
    draw.height = height;
    gl.glViewport( 0, 0, draw.width, draw.height );
    gl.glMatrixMode( GL10.GL_PROJECTION );
    gl.glLoadIdentity();
    gl.glOrthof( 0.0f, (float)width, (float)height, 0.0f, 50.0f, -50.0f );
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

  public GLLoopAmanatsuOP( Amanatsu ama )
  {
    this.ama = ama;
    this.view = ama.render.view;
    counter = -1;
  }

  @Override
  public void Run( AmanatsuDraw draw )
  {
    draw.ClearScreen();
    if ( counter < 0 )
    {
      counter = 0;
      try
      {
        URL filename = this.getClass().getResource( "/res/drawable/logo.png" );
        InputStream input = filename.openStream();
        Bitmap bmp = BitmapFactory.decodeStream( input );
        draw.CreateTexture( 0, bmp );
        counter = 1;
      } catch (IOException e)
      {
      }
    } else if ( counter == 120 )
    {
      // End.
      draw.DestroyTexture( 0 );
      ama.render.loop = new GLLoopUserInit( ama );
    } else if ( counter > 0 )
    {
      // OP
      int max = draw.width < draw.height ? draw.width : draw.height;
      max *= 0.4;
      draw.DrawTextureScaring( 0, draw.width / 2 - max / 2, draw.height / 2 - max / 2, 0, 0, 256, 256, max, max );
      draw.DrawTexture( 0, draw.width, draw.height, 0, 0, 256, 256 );
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
      view.UserInit( draw );
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
    if ( view.MainLoop( draw, ama.input ) == false )
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
    ama.Stop();
    ( (Activity) ama.context ).finish();
  }
  
}

class TouchEvent implements AmanatsuInput
{
  private float x, y;
  private float[] mx, my;
  private int[] fid;
  private int len = 0, max = 0;
  static Map<Integer, Finger> finger;
  static Map<Integer, Key> keyboard;


  // tmp;
  Finger fin;
  Key key;
  int tfid[];

  boolean lock = false;

  public TouchEvent()
  {
    finger = new Hashtable<Integer, Finger>();
    keyboard = new Hashtable<Integer, Key>();
    tfid = new int[ 1 ];
  }

  @Override
  public boolean Update()
  {
    Iterator< Map.Entry<Integer, Finger> > itf;
    Iterator< Map.Entry<Integer, Key> > itk;
    Map.Entry<Integer, Finger> entryf;
    Map.Entry<Integer, Key> entryk;
    Finger fin;
    Key key;

    int del = 0;
    len = 0;

    for ( itf = finger.entrySet().iterator(); itf.hasNext() ; )
    {
      entryf = itf.next();
      fin = entryf.getValue();
      if ( fin.touched == false )
      {
        if ( fin.frame < 0 )
        {
          if ( tfid.length < del + 1 ){ tfid = new int[ del + 1 ]; }
          tfid[ del ] = entryf.getKey();
          ++del;
          continue;
        } else
        {
          fin.frame = -1;
        }
      } else
      {
        ++fin.frame;
      }
      fin.touched = false;
      ++len;
    }

    // Delete FingerID.
    while ( --del >= 0 ){ finger.remove( tfid[ del ] ); }

    // Key Input.

    //del = 0;
    for ( itk = keyboard.entrySet().iterator(); itk.hasNext() ; )
    {
      entryk = itk.next();
      key = entryk.getValue();
      if ( key.pushed == false )
      {
        if ( key.frame < 0 )
        {
          //if ( tfid.length < del + 1 ){ tfid = new int[ del + 1 ]; }
          //tfid[ del ] = entryk.getKey();
          //++del;
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
  public boolean Touch( MotionEvent event )
  {
    int n, id;

    this.x = event.getX();
    this.y = event.getY();

    int len = event.getPointerCount();
    if ( len > max )
    {
      max = len;
      mx = new float[ max ];
      my = new float[ max ];
      fid = new int[ max ];
    }

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
      fin.x = mx[ n ] = event.getX( n );
      fin.y = my[ n ] = event.getY( n );
      // Finger id.
      fid[ n ] = id;
      // Touched.
      fin.touched = true;
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

class GameTimer extends Handler
{
  GLSurfaceView view;
  private float delayTime;
  private float frameRate;

  public GameTimer( GLSurfaceView glview, float frameRate )
  {
    this.view = glview;
    this.frameRate = frameRate;
  }

  public void start()
  {
    this.delayTime = 1000 / frameRate;

    view.invalidate();
    
    this.sendMessageDelayed( obtainMessage( 0 ), (int)delayTime );
  }

  public void stop()
  {
    delayTime = 0;
  }

  @Override
  public void handleMessage( Message msg )
  {
    view.invalidate();
    if ( delayTime == 0.0 )
    {
      return; // stop
    }
    sendMessageDelayed( obtainMessage( 0 ), (int)delayTime );
  }
}
