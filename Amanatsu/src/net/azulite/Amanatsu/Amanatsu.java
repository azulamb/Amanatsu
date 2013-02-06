package net.azulite.Amanatsu;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLSurfaceView;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import net.azulite.Amanatsu.GameView;

public class Amanatsu
{
  static private String VERSION = "0.0.1";
  Context context;
  AmanatsuGLView view;
  GameGLSurfaceViewRender render;
  GameView gview;
  GameTimer timer;

  public Amanatsu( Context context, GameView gview )
  {
    this.context = context;
    view = new AmanatsuGLView( this );
    this.gview = gview;
    render = new GameGLSurfaceViewRender( this );
    render.SetGLLoop( new GLLoopAmanatsuOP( this ) );

    view.setRenderer( render );

    timer = new GameTimer( view, 30 );

    timer.start();
  }

  public void Term()
  {
    timer.stop();
    render.Term();
  }

  // Setter.
  public GameView SetGameView( GameView view )
  {
    GameView ret;
    ret = this.gview;
    this.gview = view;
    render.SetGameView( view );// TODO Lock
    return ret;
  }

  // Getter.
  static public String GetVersion(){ return VERSION; }
  public Context GetContext(){ return context; }

  public GLSurfaceView GetGLSurfaceView(){ return view; }

}

class AmanatsuGLView extends GLSurfaceView
{
  public AmanatsuGLView( Amanatsu ama )
  {
    super( ama.GetContext() );

    // タッチイベントを取得可能にする。
    this.setFocusable( true );
  }

  public boolean onTouchEvent( MotionEvent event )
  {
    
    // タッチ座標を取得。
    //x = event.getX() * 2;
    //y = event.getY() * 2;

    return false;
  }
}

class GameGLSurfaceViewRender implements GLSurfaceView.Renderer
{
  GameView view = null;
  OpenGLDraw draw;
  protected GLLoop loop;

  public GameGLSurfaceViewRender( Amanatsu ama )
  {
    this.SetGameView( ama.gview );
    draw = new OpenGLDraw( ama.context );
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
    
    loop.Run( draw );

    gl.glDisable( GL10.GL_BLEND );
  }

  @Override
  public void onSurfaceChanged( GL10 gl, int width, int height ) {
    // TODO Auto-generated method stub
    draw.width = width;
    draw.height = height;
    gl.glViewport( 0, 0, draw.width, draw.height );
    gl.glMatrixMode( GL10.GL_PROJECTION );
    gl.glLoadIdentity();
    //gl.glOrthof( -1.0f, 1.0f, 1.0f, -1.0f, 0.5f, -0.5f );
    //gl.glOrthof( 0.0f, 1.0f, 1.0f, 0.0f, 0.5f, -0.5f );
    gl.glOrthof( 0.0f, (float)width, (float)height, 0.0f, 50.0f, -50.0f );
  }

  @Override
  public void onSurfaceCreated( GL10 gl, EGLConfig config ) {
    // TODO Auto-generated method stub
  }

}

interface GLLoop{ public void Run( OpenGLDraw draw ); }

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
  public void Run( OpenGLDraw draw )
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
    } else if ( counter == 180 )
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
  public void Run( OpenGLDraw draw )
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
  public void Run( OpenGLDraw draw )
  {
    if ( view.MainLoop( draw ) == false )
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
  public void Run(OpenGLDraw draw)
  {
    ama.Term();
    ( (Activity) ama.context ).finish();
  }
  
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
