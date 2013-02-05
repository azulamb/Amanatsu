package net.azulite.Amanatsu;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

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
  Context context;
  AmanatsuGLView view;
  GameGLSurfaceViewRender render;
  GameView gview;
  GameTimer timer;

  public Amanatsu( Context context, GameView gview )
  {
    this.context = context;
    view = new AmanatsuGLView( this );
    render = new GameGLSurfaceViewRender( context, gview );
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

  public GameGLSurfaceViewRender( Context context, GameView view )
  {
    this.SetGameView( view );
    draw = new OpenGLDraw( context );
    loop = new GLLoopAmanatsuOP( this );
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
    loop.Run( draw );
  }

  @Override
  public void onSurfaceChanged( GL10 gl, int width, int height ) {
    // TODO Auto-generated method stub
    draw.width = width * 2;
    draw.height = height * 2;
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
  GameGLSurfaceViewRender render;
  int counter;
  public GLLoopAmanatsuOP( GameGLSurfaceViewRender render )
  {
    this.view = render.view;
    this.render = render;
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
    } else if ( counter == 120 )
    {
      // End.
      draw.DestroyTexture( 0 );
      render.loop = new GLLoopUserInit( render );
    } else if ( counter > 0 )
    {
      // OP
      int max = draw.width < draw.height ? draw.width : draw.height;
      max *= 0.8;
      draw.DrawTextureScaring( 0, draw.width / 2 - max / 2, draw.height / 2 - max / 2, 0, 0, 256, 256, max, max );
      ++counter;
    }
  }
}

class GLLoopUserInit implements GLLoop
{
  GameView view;
  GameGLSurfaceViewRender render;
  int run = 0;
  public GLLoopUserInit( GameGLSurfaceViewRender render )
  {
    this.view = render.view;
    this.render = render;
    this.run = 0;
  }

  @Override
  public void Run( OpenGLDraw draw )
  {
    if ( run == 0 )
    {
      run = 1;
      view.UserInit( draw );
      render.loop = new GLLoopMainLoop( view );
    }
  }
}

class GLLoopMainLoop implements GLLoop
{
  GameView view;
  public GLLoopMainLoop( GameView view )
  {
    this.view = view;
  }

  @Override
  public void Run( OpenGLDraw draw )
  {
    view.MainLoop( draw );
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
