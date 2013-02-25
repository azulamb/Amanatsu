package net.azulite.Amanatsu;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
 * @version 0.2.0
 */

// Library
// TODO
// * enable back to exit
// * CreateTexture
// * disable amanatsu thema
// * screen size
// * fps

/**
 * Amanatsuの総合管理クラス。
 * AmanatsuとはGithubで公開されているAndroid用のゲームライブラリです。
 * 
 * https://github.com/HirokiMiyaoka/Amanatsu
 * 
 */
public class Amanatsu
{
  private static String VERSION = new String( "0.2.0" );

  /** 透過色有効な通常合成。 */
  public static final int DRAW_TRC = 0;
  /** 加算合成。 */
  public static final int DRAW_ADD = 1;
//  public static final int DRAW_SUB = 2;
  /** 乗算合成。 */
  public static final int DRAW_MUL = 3;

  private Context context;
  protected AmanatsuGLView view;

  protected GameView gview;

  protected GameGLSurfaceViewRender render;
  protected static AmanatsuInput input;
  protected static AmanatsuSound sound;

  /**
   * Amanatsuの生成。
   * @param context Amanatsuを使用するActivity。
   * @param gview GameViewインターフェースを継承したクラス。
   */
  public Amanatsu( Context context, GameView gview )
  {
    this( context, gview, true, true );
  }
  /**
   * Amanatsuの生成。
   * @param context Amanatsuを使用するActivity。
   * @param gview GameViewインターフェースを継承したクラス。
   * @param multitouch マルチタッチの有無(true=マルチタッチ, false=シングルタッチ)。
   */
  public Amanatsu( Context context, GameView gview, boolean multitouch )
  {
    this( context, gview, multitouch, true );
  }

  /**
   * Amanatsuの生成。
   * @param context Amanatsuを使用するActivity。
   * @param gview GameViewインターフェースを継承したクラス。
   * @param multitouch マルチタッチの有無(true=マルチタッチ, false=シングルタッチ)。
   * @param logo Amanatsuのロゴの表示(true=表示する, false=表示しない)。
   */
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

    setGameView( gview );

    render.setGLLoop( new GLLoopAmanatsuOP( this, logo ) );

    view.setRenderer( render );
    view.setRenderMode( GLSurfaceView.RENDERMODE_WHEN_DIRTY );

  }

  /**
   * GLSurfaceViewの登録と同時にゲームの実行を開始する。
   * @param context Activityのインスタンス。
   * @param gview GameViewを継承したクラスのインスタンス。
   */
  public static GLSurfaceView autoRunAmanatsu( Context context, GameView gview )
  {
    Amanatsu ama = new Amanatsu( context, gview );
    ama.start();
    return ama.getGLSurfaceView();
  }

  /**
   * ゲームの処理を開始する。
   */
  public boolean start()
  {
    render.start();
    return true;
  }

  /**
   * ゲームの処理を止める。
   */
  public void stop()
  {
    //timer.stop();
    render.term();
  }

  /**
   * GameViewをセットする。
   * 前に設定したGameViewがある場合、前のGameViewのCleanUp()と新たに設定するGameViewのUserInit()が実行される。
   */
  public GameView setGameView( GameView view )
  {
    GameView ret = this.gview;
    this.gview = view;
    GameView.system = this;
    render.setGameView( view );// TODO Lock
    return ret;
  }

  public AmanatsuInput setInput( AmanatsuInput input )
  {
    AmanatsuInput ret = Amanatsu.input;
    Amanatsu.input = input;
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
    AmanatsuSound ret = Amanatsu.sound;
    Amanatsu.sound = sound;
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

    input = Amanatsu.input;
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
  protected GameView view = null, nextview;
  private AmanatsuDraw draw;
  protected GLLoop loop;
  private boolean loopflag = false;
  private String gltype = "", glver = "";

  // FPS
  private long before, now, progress;
  private int frame;
  float fps = 30.0f, nowfps;
  private float countfps;

  public GameGLSurfaceViewRender( Amanatsu ama )
  {
    this.ama = ama;
    glview = ama.view;
    //setGameView( ama.gview );
  }

  public void setGLLoop( GLLoop loop )
  {
    this.loop = loop;
  }

  protected GameView setGameView( GameView view )
  {
    GameView ret = this.view;
    if ( ret != null )
    {
      ret.CleanUp();
      nextview = view;
    } else
    {
      this.view = view;
      nextview = view;
    }
    if ( ret != null )
    {
      view.UserInit();
    }
    return ret;
  }

  public void term()
  {
    view.CleanUp();
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

    Amanatsu.input.update();

    loop.run( draw );
    gl.glFlush();

    if ( view != nextview )
    {
      view = nextview;
      loop.setGameView( view );
    }

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
    if ( loopflag ){ sendMessageDelayed( obtainMessage( 0 ), (int)((float)frame * 1000.0f / fps ) - progress ); }
  }

  @Override
  public void onSurfaceChanged( GL10 gl, int width, int height )
  {
    draw.change( gl, width, height );
  }

  @Override
  public void onSurfaceCreated( GL10 gl, EGLConfig config )
  {
    // TODO:select version
    Pattern pattern = Pattern.compile( "OpenGL ES(-*[CML]*) ([0-9.]+)" );
    Matcher matcher = pattern.matcher( gl.glGetString( GL10.GL_VERSION ) );
    if ( matcher.find() )
    {
      gltype = matcher.group( 1 );
      glver = matcher.group( 2 );
    }
    draw = new AmanatsuDraw( ama, gltype, glver );
    draw.init( gl );
    if ( view != null )
    {
      GameView.draw = draw;
      GameView.sound = Amanatsu.sound;
      GameView.input = Amanatsu.input;
    }
  }

}

class GLLoop
{
  protected GameView view;
  public GameView setGameView( GameView view )
  {
    GameView ret = this.view;
    this.view = view;
    return ret;
  }
  public void run( AmanatsuDraw draw ){}
}

class GLLoopAmanatsuOP extends GLLoop
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
    if ( counter == 120 || logo == false )
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
      draw.drawTextureScaling( 0, 0, 0, 256, 256, draw.getWidth() / 2 - max / 2, draw.getHeight() / 2 - max / 2, max, max );
      ++counter;
    } else if ( counter < 0 )
    {
      counter = 0;
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
    }

  }
}

class GLLoopUserInit extends GLLoop
{
  private Amanatsu ama;
  private int run = 0;

  public GLLoopUserInit( Amanatsu ama )
  {
    this.ama = ama;
    setGameView( ama.render.view );
    this.run = 0;
  }

  @Override
  public void run( AmanatsuDraw draw )
  {
    if ( run == 0 )
    {
      run = 1;
      view.UserInit();
      ama.render.loop = new GLLoopMainLoop( ama );
    }
  }
}

class GLLoopMainLoop extends GLLoop
{
  private Amanatsu ama;

  public GLLoopMainLoop( Amanatsu ama )
  {
    this.ama = ama;
    setGameView( ama.render.view );
  }

  @Override
  public void run( AmanatsuDraw draw )
  {
    if ( view.MainLoop() == false )
    {
      ama.render.loop = new GLLoopCleanUp( ama );
    }
  }
}

class GLLoopCleanUp extends GLLoop
{
  private Amanatsu ama;
  private int run;

  public GLLoopCleanUp( Amanatsu ama )
  {
    setGameView( ama.render.view );
    this.ama = ama;
    this.run = 0;
  }

  @Override
  public void run( AmanatsuDraw draw)
  {
    if ( run == 0 )
    {
      run = 1;
      view.CleanUp();
      ama.stop();
      draw.release();
      Amanatsu.sound.release();
      ( (Activity) ama.getContext() ).finish();
    }
  }
  
}

class TouchEvent extends AmanatsuKey implements AmanatsuInput
{
  protected float basex, basey, width, height, W, H;
  protected float x, y;
  protected boolean touched;
  protected int frame;


  public TouchEvent()
  {
    touched = false;
  }

  @Override
  public boolean setWindowSize( float width, float height )
  {
    W = width;
    H = height;
    return true;
  }

  @Override
  public boolean setInputArea(float x, float y, float width, float height)
  {
    basex = x;
    basey = y;
    this.width = width;
    this.height = height;
    return true;
  }

  @Override
  public synchronized boolean update()
  {

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
    updateKey();

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
  public float getX() { return basex + x / W * width; }

  @Override
  public float getY() { return basey + y / H * height; }

  @Override
  public int getTouchFrame() { return frame; }

  @Override
  public int fingerNum() { return (frame > 0) ? 1 : 0; }

  @Override
  public float getX( int num ) { return basex + x / W * width; }

  @Override
  public float getY( int num ) { return basey + y / H * height; }

  @Override
  public int getTouchFrame( int num ) { return frame; }

  @Override
  public int getFingerId(int num) { return 0; }

  @Override
  public float getFingerX( int fingerid ) { return basex + x / W * width; }

  @Override
  public float getFingerY( int fingerid ) { return basey + y / H * height; }

  @Override
  public int getFingerTouchFrame( int fingerid ) { return frame; }

}

class MultiTouchEvent extends TouchEvent implements AmanatsuInput
{
  private float[] mx, my;
  private int[] fid;
  private int len = 0, max = 0;
  private static Map<Integer, Finger> finger = new Hashtable<Integer, Finger>( 16 );

  // tmp;
  private Iterator< Map.Entry<Integer, Finger> > itf;
  private Map.Entry<Integer, Finger> entryf;
  private Finger fin;

  boolean lock = false;

  public MultiTouchEvent()
  {
    touched = false;
  }

  @Override
  public synchronized boolean update()
  {
    Finger fin;

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

    for ( itf = finger.entrySet().iterator() ; itf.hasNext() ; )
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
    updateKey();

    return true;
  }

  @Override
  public synchronized boolean touch( MotionEvent event )
  {
    int n, id;

    super.touch( event );

    int len = event.getPointerCount();
    if ( len > max )
    {
      max = len;
      mx = new float[ max ];
      my = new float[ max ];
      fid = new int[ max ];
    }

    for ( itf = finger.entrySet().iterator() ; itf.hasNext() ; )
    {
      entryf = itf.next();
      entryf.getValue().touched = false;
    }

    if ( event.getAction() != MotionEvent.ACTION_UP ) // TODO
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
  public int fingerNum() { return len; }

  @Override
  public float getX( int num )
  {
    return basex + mx[ num ] / W * width;
  }

  @Override
  public float getY( int num )
  {
    return basey + my[ num ] / H * height;
  }

  @Override
  public int getTouchFrame( int num )
  {
    if ( num >= fid.length || finger.containsKey( fid[ num ] ) == false ){ return -1; }
    return finger.get( fid[ num ] ).frame;
  }

  @Override
  public int getFingerId( int num )
  {
    if ( num >= fid.length || finger.containsKey( fid[ num ] ) == false ){ return -1; }
    return fid[ num ];
  }

  @Override
  public float getFingerX( int fingerid )
  {
    if ( finger.containsKey( fingerid ) == false ){ return 0.0f; }
    return basex + finger.get( fingerid ).x / W * width;
  }

  @Override
  public float getFingerY( int fingerid )
  {
    if ( finger.containsKey( fingerid ) == false ){ return 0.0f; }
    return basey + finger.get( fingerid ).y / H * height;
  }

  @Override
  public int getFingerTouchFrame( int fingerid )
  {
    fin = finger.get( fingerid );
    if ( fin == null ){ return 0; }
    return fin.frame;
  }

}

class Finger
{
  float x, y;
  int id;
  int frame;
  boolean touched;
}


