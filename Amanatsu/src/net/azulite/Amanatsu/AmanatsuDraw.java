package net.azulite.Amanatsu;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.microedition.khronos.opengles.GL10;

import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Color;
import android.opengl.GLUtils;

/**
 * @author Hiroki
 */


// Library.
// TODO:
// * screen size
// * auto change 1.1, 2.0
// * box texture
// * set funciton glEnable() GL_POINT_SMOOTH,GL_LINE_SMOOTH,GL_POLYGON_SMOOTH
public class AmanatsuDraw
{
  private Amanatsu ama;
  private GL10 gl = null;
  private int width, height;
  private float basex, basey, screenwidth, screenheight;
  private Resources resource;
  private AssetManager assets;
  private static Map<Integer, Texture> textures = new Hashtable< Integer, Texture >( 50 );
  private static Map<Integer, Paint> paints = new Hashtable< Integer, Paint >( 50 );

  // Box
  private Texture boxtex;

  // String.
  private Bitmap stringbmp;
  private Texture stringtex;
  private int stringnum;

  // tmp.
  private float[] farr4, farr6, farr8, mat;
  private Texture ttex;
  private Paint tpaint;

  public AmanatsuDraw( Amanatsu ama )
  {
    this.ama = ama;
    resource = ama.getContext().getResources();
    assets = ama.getContext().getResources().getAssets();
    farr4 = new float[ 4 ];
    farr6 = new float[ 6 ];
    farr8 = new float[ 8 ];
    mat = new float[ 16 ];
  }

  public String getGLVersion(){ return gl.glGetString( GL10.GL_VERSION ); }

  public void init( GL10 gl )
  {
    setGL( gl );

    Bitmap white = Bitmap.createBitmap( 1, 1, Config.ALPHA_8 );
    Canvas canvas = new Canvas( white );
    canvas.drawARGB( 255, 255, 255, 255 );
    createTextureFromBitmap( 0, white, false );
    boxtex = ttex;

    boxtex.col  = createFloatBuffer( new float[]{ 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f } );
    setFloatArray8( 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f );
    boxtex.uv   = createFloatBuffer( farr8 );

    stringbmp = Bitmap.createBitmap( 1024, 1024, Config.ALPHA_8 );//Bitmap.Config.ARGB_8888);
    createTextureFromBitmap( 0, stringbmp, false );
    stringtex = ttex;
    stringnum = ttex.texid[ 0 ];
    stringtex.col  = createFloatBuffer( new float[]{ 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f } );

    createFont( 0, 30 );

    setRender( Amanatsu.DRAW_TRC );
  }

  public void change( GL10 gl, int width, int height )
  {
    setGL( gl );

    gl.glMatrixMode( GL10.GL_PROJECTION );
    gl.glLoadIdentity();
    gl.glOrthof( 0.0f, width, height, 0.0f, 50.0f, -50.0f );

    if ( getWidth() <= 0 )
    {
      setWindowSize( width, height );
      setScreenSize( 0.0f, 0.0f, width, height );
      ama.input.setWindowSize( width, height );
      ama.input.setInputArea( 0.0f, 0.0f, width, height );
    } else
    {
      setWindowSize( width, height );
      ama.input.setWindowSize( width, height );
    }

    gl.glEnable( GL10.GL_BLEND );
    gl.glEnable( GL10.GL_TEXTURE_2D );
    gl.glEnableClientState( GL10.GL_TEXTURE_COORD_ARRAY );
    gl.glEnableClientState( GL10.GL_COLOR_ARRAY );
    gl.glEnableClientState( GL10.GL_VERTEX_ARRAY );//TODO

    gl.glFrontFace( GL10.GL_CW );
    gl.glEnable( GL10.GL_CULL_FACE );
    gl.glCullFace( GL10.GL_BACK );

  }

  /**
   * �X�N���[���T�C�Y�̐ݒ�B
   * �X�N���[���T�C�Y�Ƃ͉�ʑS�̂��E�B���h�E�Ƃ������A���̈ꕔ����؂��������̂��w���܂��B
   * �J�n���W�Ɨ̈�Ŏw�肷�邱�Ƃ��\�ł��B
   * @param x �J�nX���W�B
   * @param y �J�nY���W�B
   * @param width �����B
   * @param height �����B
   */
  public boolean setScreenSize( float x, float y, float width, float height )
  {
    if ( ama.input != null )
    {
      ama.input.setInputArea( x, y, width, height );
    }
    basex = x * this.width / width;
    basey = y * this.height / height;
    screenwidth = width;
    screenheight = height;
    width = (this.width * this.width) / width;
    height = (this.height * this.height) / height;

    gl.glViewport( (int)-basex, (this.height - (int)height) + (int)basey, (int)width, (int)height );

    return true;
  }

  /**
   * �X�N���[���̈ړ��B
   * @param x �ړ���X���W�B
   * @param y �ړ���Y���W�B
   */
  public boolean moveScreen( float x, float y )
  {
    basex = x;
    basey = y;
    return setScreenSize( basex, basey, screenwidth, screenheight );
  }

  public void release()
  {
    stringbmp.recycle();
  }

  public boolean setGL( GL10 gl )
  {
    this.gl = gl;
    return true;
  }

  /**
   * Return FPS.
   */
  public float getFps()
  {
    return ama.render.getFps();
  }

  /**
   * FPS�̐ݒ�A
   * @param fps FPS�̐ݒ�(Amanatsu�̃f�t�H���g��30.0f)�B
   */
  public float setFps( float fps )
  {
    return ama.render.setFps( fps );
  }

  /**
   * �`�惂�[�h�̐ݒ�B
   * �`�惂�[�h��؂�ւ��܂��B
   * Amanatsu.DRAW_ADD=���Z�����B
   * Amanatsu.DRAW_MUL=��Z�����B
   * Amanatsu.DRAW_TRC=���ߐF�L���ʏ퍇���B
   * @param type Amanatsu.DRAW_* �Őݒ肳��Ă���`�惂�[�h�B
   */
  public boolean setRender( int type )
  {
    switch ( type )
    {
    case Amanatsu.DRAW_ADD:
      gl.glBlendFunc( GL10.GL_SRC_ALPHA, GL10.GL_ONE );
      break;
/*    case Amanatsu.DRAW_SUB:TODO
      gl.glBlendEquationEXT( GL11ExtensionPack.GL_FUNC_REVERSE_SUBTRACT_EXT );
      gl.glBlendFunc( GL10.GL_SRC_ALPHA, GL10.GL_ONE );
      break;*/
    case Amanatsu.DRAW_MUL:
      gl.glBlendFunc( GL10.GL_ZERO, GL10.GL_SRC_COLOR );
      // memo:
      //dst = dst * src * alpha;
      //gl.glBlendFunc(GL10.GL_ZERO, GL10.GL_SRC_COLOR);
      //gl.glBlendFunc(GL10.GL_ZERO, GL10.GL_SRC_ALPHA);
      break;
    default:
      gl.glBlendFunc( GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA );
    }
    return true;
  }

  /**
   * ��ʂ̃N���A�B
   * ��ʂ����œh��Ԃ��܂��B
   * MainLoop�J�n���Ɉ�x���s���Ă��������B
   * �܂��A��ʃN���A�O�ɕ`�悵�Ă������߂͑S�ď�����Ă��܂��̂ŋC�����ĉ������B
   */
  public boolean clearScreen()
  {
    return clearScreen( 0.0f, 0.0f, 0.0f );
  }

  /**
   * ��ʂ̃N���A�B
   * �C�ӂ̐F�ŉ�ʂ�h��Ԃ��܂�(�A���t�@�l�͖���)�B
   * @param color GameColor�̃C���X�^���X�B
   */
  public boolean clearScreen( GameColor color )
  {
    return clearScreen( color.color[ 0 ], color.color[ 1 ], color.color[ 2 ] );
  }

  /**
   * ��ʂ̃N���A�B
   * �C�ӂ̐F�ŉ�ʂ�h��Ԃ��܂�(�A���t�@�l�͖���)�B
   * @param color �F�z��( ��, ��, �� ).�e�F�̋�����0-255�B
   */
  public boolean clearScreen( byte[] color )
  {
    return clearScreen( (float)color[ 0 ] / 255.0f, (float)color[ 1 ] / 255.0f, (float)color[ 2 ] / 255.0f );
  }

  /**
   * ��ʂ̃N���A�B
   * �C�ӂ̐F�ŉ�ʂ�h��Ԃ��܂�(�A���t�@�l�͖���)�B
   * @param color �F�z��( ��, ��, �� ).�e�F�̋�����0.0f-1.0f�B
   */
  public boolean clearScreen( float[] color )
  {
    return clearScreen( color[ 0 ], color[ 1 ], color[ 2 ] );
  }

  /**
   * ��ʂ̃N���A�B
   * @param red ��(0-255).
   * @param green ��(0-255).
   * @param blue ��(0-255).
   */
  public boolean clearScreen( byte red, byte green, byte blue )
  {
    return clearScreen( (float)red / 255.0f, (float)green / 255.0f, (float)blue / 255.0f );
  }

  /**
   * ��ʂ̃N���A�B
   * @param red ��(0.0f-1.0f).
   * @param green ��(0.0f-1.0f).
   * @param blue ��(0.0f-1.0f).
   */
  public boolean clearScreen( float red, float green, float blue )
  {
    gl.glClearColor( red, green, blue, 1.0f );
    gl.glClear( GL10.GL_COLOR_BUFFER_BIT );

    return true;
  }

  /**
   * �e�N�X�`���̐���(���\�[�X����)�B
   * �e�N�X�`�������\�[�X���琶������B�e�N�X�`���ԍ��̓��\�[�X�ԍ��ƂȂ�B
   * @param rnum ���\�[�X�ԍ��B
   */
  public int createTexture( int rnum )
  {
    return createTextureFromBitmap( rnum, BitmapFactory.decodeResource( resource, rnum ), true );
  }

  /**
   * �e�N�X�`���̐���(���\�[�X����)�B
   * �e�N�X�`���ԍ��͎����Őݒ肷��B
   * @param tnum �e�N�X�`���ԍ��B
   * @param rnum ���\�[�X�ԍ��B
   */
  public int createTexture( int tnum, int rnum )
  {
    return createTextureFromBitmap( tnum, BitmapFactory.decodeResource( resource, rnum ), true );
  }

  /**
   * �e�N�X�`���̐ݒ�(assets)�B
   * assets���Ɋi�[�����t�@�C������e�N�X�`���𐶐�����B
   * �t�@�C���p�X��assets��root�ɂ����ꍇ�B
   * @param tnum �e�N�X�`���ԍ��B
   * @param path �t�@�C���p�X�B
   */
  public int createTexture( int tnum, String path )
  {
    try
    {
      InputStream is = assets.open( path );
      return createTextureFromBitmap( tnum, BitmapFactory.decodeStream( is ), true );
    } catch ( IOException e )
    {
    }
    return -1;
  }

  /**
   * �e�N�X�`���̐���(Bitmap)�B
   * Bitmap����e�N�X�`���𐶐�����B
   * @param tnum �e�N�X�`���ԍ��B
   * @param bmp Bitmap�̃C���X�^���X�B
   */
  public int createTexture( int tnum, Bitmap bmp )
  {
    return createTextureFromBitmap( tnum, bmp, true );
  }

  private int createTextureFromBitmap( int tnum, Bitmap bmp, boolean regist )
  {
    if ( bmp == null )
    {
      return -1;
    }

    if ( regist )
    {
      if ( textures.containsKey( tnum ) )
      {
        releaseTexture( tnum );
        ttex = textures.get( tnum );
      } else
      {
        ttex = new Texture();
        ttex.texid = new int [ 1 ];
        textures.put( tnum, ttex );
      }
    } else
    {
      // Unregist.
      ttex = new Texture();
      ttex.texid = new int [ 1 ];
    }

    gl.glGenTextures( 1, ttex.texid, 0 );
    gl.glBindTexture( GL10.GL_TEXTURE_2D, ttex.texid[ 0 ] );

    if ( AmanatsuDraw.isPowerOf2( bmp.getWidth() ) == false ||
         AmanatsuDraw.isPowerOf2( bmp.getHeight() ) == false ||
         bmp.getWidth() != bmp.getHeight() )
    {
      int length = AmanatsuDraw.convertPowerOf2( bmp.getWidth() >= bmp.getHeight() ? bmp.getWidth() : bmp.getHeight() );
      bmp = AmanatsuDraw.resizeBitmap( bmp, length );
    }
    GLUtils.texImage2D( GL10.GL_TEXTURE_2D, 0, bmp, 0 );

    gl.glTexParameterf( GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR );
    //gl.glTexParameterf( GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR );
    gl.glBindTexture( GL10.GL_TEXTURE_2D, 0 );

    ttex.width = bmp.getWidth();
    ttex.height = bmp.getHeight();

    if ( regist )
    {
      setColor( tnum );
      bmp.recycle();
    }

    return ttex.texid[ 0 ];
  }

  /**
   * �e�N�X�`���̑��݃`�F�b�N�B
   * @param tnum �e�N�X�`���ԍ��B
   */
  public boolean existTexture( int tnum ){ return (textures.containsKey( tnum ) && textures.get( tnum ).texid != null );}

  /**
   * �e�N�X�`���̔j���B
   * @param tnum �e�N�X�`���ԍ��B
   */
  public void destroyTexture( int tnum )
  {
    if ( releaseTexture( tnum ) )
    {
      textures.remove( tnum );
    }
  }

  // TODO
  /**
   * �e�N�X�`���̉���B
   * @param tnum �e�N�X�`���ԍ��B
   */
  public boolean releaseTexture( int tnum )
  {
    if ( existTexture( tnum ) )
    {
      gl.glDeleteTextures( 1, textures.get( tnum ).texid, 0 );
      return true;
    }
    return false;
  }

  // TODO
  /**
   * �e�N�X�`����S�ĉ���B
   */
  public void releaseTextureAll()
  {
    List<Integer> keys = new ArrayList<Integer>( textures.keySet() );
    for ( Integer key : keys )
    {
      releaseTexture( key );
    }
  }

  private final void setFloatArray4( float f0, float f1, float f2, float f3 )
  {
    farr4[ 0 ] = f0; farr4[ 1 ] = f1;
    farr4[ 2 ] = f2; farr4[ 3 ] = f3;
  }

  private final void setFloatArray6( float f0, float f1, float f2, float f3, float f4, float f5 )
  {
    farr6[ 0 ] = f0; farr6[ 1 ] = f1;
    farr6[ 2 ] = f2; farr6[ 3 ] = f3;
    farr6[ 4 ] = f4; farr6[ 5 ] = f5;
  }

  private final void setFloatArray8( float f0, float f1, float f2, float f3, float f4, float f5, float f6, float f7 )
  {
    farr8[ 0 ] = f0; farr8[ 1 ] = f1;
    farr8[ 2 ] = f2; farr8[ 3 ] = f3;
    farr8[ 4 ] = f4; farr8[ 5 ] = f5;
    farr8[ 6 ] = f6; farr8[ 7 ] = f7;
  }

  private static final FloatBuffer createColor( float[] color )
  {
    if ( color.length >= 16 )
    {
      float[] col =
      {
        color[ 0 ], color[ 1 ], color[ 2 ], color[ 3 ],
        color[ 4 ], color[ 5 ], color[ 6 ], color[ 7 ],
        color[ 8 ], color[ 9 ], color[ 10 ], color[ 11 ],
        color[ 12 ], color[ 13 ], color[ 14 ], color[ 15 ],
      };
      return createFloatBuffer( col );
    } else if ( color.length >= 4 )
    {
      float[] col =
      {
        color[ 0 ], color[ 1 ], color[ 2 ], color[ 3 ],
        color[ 0 ], color[ 1 ], color[ 2 ], color[ 3 ],
        color[ 0 ], color[ 1 ], color[ 2 ], color[ 3 ],
        color[ 0 ], color[ 1 ], color[ 2 ], color[ 3 ],
      };
      return createFloatBuffer( col );
    } else if ( color.length > 0 )
    {
      float[] col =
      {
        color[ 0 ], color[ 0 ], color[ 0 ], color[ 0 ],
        color[ 0 ], color[ 0 ], color[ 0 ], color[ 0 ],
        color[ 0 ], color[ 0 ], color[ 0 ], color[ 0 ],
        color[ 0 ], color[ 0 ], color[ 0 ], color[ 0 ],
      };
      return createFloatBuffer( col );
    }
    float[] col =
    {
      1.0f, 1.0f, 1.0f, 1.0f,
      1.0f, 1.0f, 1.0f, 1.0f,
      1.0f, 1.0f, 1.0f, 1.0f,
      1.0f, 1.0f, 1.0f, 1.0f,
    };
    return createFloatBuffer( col );
  }

  /**
   * Set texture color default(white).
   * @param tnum Texture number.
   */
  public final void setColor( int tnum )
  {
    float[] color =
    {
      1.0f, 1.0f, 1.0f, 1.0f,
      1.0f, 1.0f, 1.0f, 1.0f,
      1.0f, 1.0f, 1.0f, 1.0f,
      1.0f, 1.0f, 1.0f, 1.0f,
    };
    setColor( tnum, color );
  }

  /**
   * Set texture color(red, green, blue same power).
   * @param tnum Texture number.
   * @param col Color power(0.0f-1.0f).
   */
  public final void setColor( int tnum, float col )
  {
    float[] color =
    {
      col, col, col, 1.0f,
      col, col, col, 1.0f,
      col, col, col, 1.0f,
      col, col, col, 1.0f,
    };
    setColor( tnum, color );
  }

  /**
   * Set texture color.
   * @param tnum Texture number.
   * @param color GameColor.
   */
  public final void setColor( int tnum, GameColor color )
  {
    setColor( tnum, color.color );
  }

  /**
   * Set texture color.
   * @param tnum Texture number.
   * @param color Color float array(red, green blue, alpha).
   */
  public final void setColor( int tnum, float[] color )
  {
    Texture tex;

    if ( existTexture( tnum ) == false )
    {
      return;
    }

    tex = textures.get( tnum );
    tex.col  = createFloatBuffer( color );
  }

  // TODO setAlpha

  /**
   * Set texture UV.
   * @param tex Texture/
   * @param uv UV float array( x0, y0, x1, y1, x2, y2, x3, y3 ).
   */
  public final boolean setUV( Texture tex, float[] uv )
  {
    tex.uv  = createFloatBuffer( uv );
    return true;
  }

  /**
   * Set texture draw vertex.
   * @param tex Texture.
   * @param vert Draw vertex float array( x0, y0, x1, y1, x2 y2, x3, y3 ).
   */
  public final boolean setVertex( Texture tex, float[] vert )
  {
    tex.ver = createFloatBuffer( vert );
    return true;
  }

  /**
   * Get window width.
   */
  public final int getWidth(){ return width; }

  /**
   * Get window height.
   */
  public final int getHeight(){ return height; }

  public boolean setWindowSize( int width, int height )
  {
    this.width = width;
    this.height = height;
    return true;
  }

  /**
   * Get texture data.
   * @param tnum Texture number.
   */
  public final Texture getTexture( int tnum ){ return textures.get( tnum ); }

  // Support OpenGL.

  public static final Bitmap resizeBitmap( Bitmap bmp, int length )
  {
    Bitmap nbmp = Bitmap.createBitmap( length, length, Bitmap.Config.ARGB_8888);

    Canvas cv = new Canvas( nbmp );

    cv.drawBitmap( bmp, 0, 0, null );

    return nbmp;
  }

  public static final FloatBuffer createFloatBuffer( float[] arr )
  {
    ByteBuffer bb = ByteBuffer.allocateDirect( arr.length * 4 );
    bb.order( ByteOrder.nativeOrder() );
    FloatBuffer fb = bb.asFloatBuffer();
    fb.put( arr );
    fb.position( 0 );
    return fb;
  }

  public static final boolean isPowerOf2( int val ){ return val > 0 && ( val & (val - 1) ) == 0; }

  public static final int convertPowerOf2( int val )
  {
    if ( val < 0 ){ return 0; }
    int ret = 1;
    for( ; val > ret ; ret <<= 1 );
    return ret;
  }

  /**
   * Create font.
   * @param fnum Font number.
   * @param size Font size.
   */
  public boolean createFont( int fnum, int size ){ return createFont( fnum, size, false, GameColor.WHITE ); }

  /**
   * Create font.
   * @param fnum Font number.
   * @param size Font size.
   * @param antialias Antialias.
   */
  public boolean createFont( int fnum, int size, boolean antialias ){ return createFont( fnum, size, antialias, GameColor.WHITE ); }

  /**
   * Create font.
   * @param fnum Font number.
   * @param size Font size.
   * @param antialias Antialias.
   * @param color GameColor.
   */
  public boolean createFont( int fnum, int size, boolean antialias, GameColor color ){ return createFont( fnum, size, antialias, color.color ); }

  /**
   * Create font.
   * @param fnum Font number.
   * @param size Font size.
   * @param antialias Antialias.
   * @param color Color float array( red, green, blue, alpha ).
   */
  public boolean createFont( int fnum, int size, boolean antialias, float[] color )
  {
    if ( paints.containsKey( fnum ) == false )
    {
      tpaint = new Paint();
      paints.put( fnum, tpaint );
    } else
    {
      tpaint = paints.get( fnum );
    }

    tpaint.setTextSize( size );
    tpaint.setARGB( (int)(0xff * color[ 3 ]), (int)(0xff * color[ 0 ]), (int)(0xff * color[ 1 ]), (int)(0xff * color[ 2 ]) );
    tpaint.setAntiAlias( antialias );

    return true;
  }

  /**
   * Print string.
   * @param fnum Font number.
   * @param dx Draw x.
   * @param dy Draw y.
   * @param str Print string.
   */
  public boolean printf( int fnum, float dx, float dy, String str )
  {
    Canvas canvas = new Canvas( stringbmp );

    canvas.drawColor( Color.TRANSPARENT, PorterDuff.Mode.CLEAR );

    if ( paints.containsKey( fnum ) )
    {
      tpaint = paints.get( fnum );
    } else
    {
      tpaint = paints.get( 0 );
    }

    canvas.drawText( str, 0, tpaint.getTextSize(), tpaint );

    gl.glBindTexture(GL10.GL_TEXTURE_2D, stringnum );

    GLUtils.texImage2D( GL10.GL_TEXTURE_2D, 0, stringbmp, 0 );

    setFloatArray8( dx, dy, dx + stringtex.width, dy, dx, dy + stringtex.height, dx + stringtex.width, dy + stringtex.height );
    stringtex.ver = createFloatBuffer( farr8 );

    setFloatArray8( 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f );
    stringtex.uv   = createFloatBuffer( farr8 );

    return drawTexture( stringtex );
  }

  // Base draw.

  /**
   * Draw line.
   * @param sx Draw start x.
   * @param sy Draw start y.
   * @param ex Draw end x.
   * @param ey Draw end y.
   * @param color GameColor.
   */
  public boolean drawLine( float sx, float sy, float ex, float ey, GameColor color )
  {
    return drawLine( sx, sy, ex, ey, 1.0f, color.color );
  }

  /**
   * Draw line.
   * @param sx Draw start x.
   * @param sy Draw start y.
   * @param ex Draw end x.
   * @param ey Draw end y.
   * @param color Color float array( red, green, blue, alpha ).
   */
  public boolean drawLine( float sx, float sy, float ex, float ey, float[] color )
  {
    return drawLine( sx, sy, ex, ey, 1.0f, color );
  }

  /**
   * Draw line.
   * @param sx Draw start x.
   * @param sy Draw start y.
   * @param ex Draw end x.
   * @param ey Draw end y.
   * @param width Line width.
   * @param color GameColor.
   */
  public boolean drawLine( float sx, float sy, float ex, float ey, float width, GameColor color )
  {
    return drawLine( sx, sy, ex, ey, width, color.color );
  }

  /**
   * Draw line.
   * @param sx Draw start x.
   * @param sy Draw start y.
   * @param ex Draw end x.
   * @param ey Draw end y.
   * @param width Line width.
   * @param color Color float array( red, green, blue, alpha ).
   */
  public boolean drawLine( float sx, float sy, float ex, float ey, float width, float[] color )
  {
    gl.glDisable( GL10.GL_TEXTURE_2D );
    gl.glDisableClientState( GL10.GL_COLOR_ARRAY );

    gl.glColor4f( color[ 0 ], color[ 1 ], color[ 2 ], color[ 3 ] );
    //setFloatArray8( color[ 0 ], color[ 1 ], color[ 2 ], color[ 3 ], color[ 0 ], color[ 1 ], color[ 2 ], color[ 3 ] );
    gl.glColorPointer( 2, GL10.GL_FLOAT, 0, createFloatBuffer( farr8 ) );
    gl.glLineWidth( width );
    setFloatArray6( sx, sy, 1.0f, ex, ey, 1.0f );
    gl.glVertexPointer( 3, GL10.GL_FLOAT, 0, createFloatBuffer( farr6 ) );
    gl.glDrawArrays( GL10.GL_LINE_STRIP, 0, 2 );

    gl.glEnable( GL10.GL_TEXTURE_2D );
    gl.glEnableClientState( GL10.GL_COLOR_ARRAY );

    return true;
  }

  /**
   * @param x Draw x.
   * @param y Draw y.
   * @param w Width.
   * @param h Height.
   * @param color GameColor.
   */
  public boolean drawBox( float x, float y, float w, float h, GameColor color )
  {
    return drawBox( x, y, w, h, color.color );
  }

  /**
   * @param x Draw x.
   * @param y Draw y.
   * @param w Width.
   * @param h Height.
   * @param color Color float array(red, green, blue, alpha).
   */
  public boolean drawBox( float x, float y, float w, float h, float[] color )
  {
    setFloatArray8( x, y, x + w, y, x, y + h, x + w, y + h );
    boxtex.ver = createFloatBuffer( farr8 );
    boxtex.col = createColor( color );
    return drawTexture( boxtex );
  }

  /**
   * @param x Draw x(center).
   * @param y Draw y(center).
   * @param w Width.
   * @param h Height.
   * @param color GameColor.
   */
  public boolean drawBoxC( float x, float y, float w, float h, GameColor color )
  {
    return drawBoxC( x, y, w, h, color.color );
  }

  /**
   * @param x Draw x(center).
   * @param y Draw y(center).
   * @param w Width.
   * @param h Height.
   * @param color Color float array(red, green, blue, alpha).
   */
  public boolean drawBoxC( float x, float y, float w, float h, float[] color )
  {
    setFloatArray8( x - w / 2.0f, y - h / 2.0f, x + w / 2.0f, y - h / 2.0f, x - w / 2.0f, y + h / 2.0f, x + w / 2.0f, y + h / 2.0f );
    boxtex.ver = createFloatBuffer( farr8 );
    boxtex.col = createColor( color );
    return drawTexture( boxtex );
  }
  
  // TODO drawCircle

  // Draw texture.
  private boolean drawTexture( Texture tex )//TODO
  {
    gl.glBindTexture( GL10.GL_TEXTURE_2D, tex.texid[ 0 ] );

    gl.glVertexPointer( 2, GL10.GL_FLOAT, 0, tex.ver );
    gl.glColorPointer( 4, GL10.GL_FLOAT, 0, tex.col );
    gl.glTexCoordPointer( 2, GL10.GL_FLOAT, 0, tex.uv );

    gl.glDrawArrays( GL10.GL_TRIANGLE_STRIP, 0, 4 );

    return true;
  }

  /**
   * @param rnum Resource or Texture number.
   * @param dx Draw x coordinate.
   * @param dy Draw y coordinate.
   */
  public boolean drawTexture( int rnum, float dx, float dy )
  {

    if ( existTexture( rnum ) == false )
    {
      return false;
    }

    ttex = getTexture( rnum );

    setFloatArray8( dx, dy, dx + ttex.width, dy, dx, dy + ttex.height, dx + ttex.width, dy + ttex.height );
    ttex.ver = createFloatBuffer( farr8 );

    setFloatArray8( 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f );
    ttex.uv   = createFloatBuffer( farr8 );

    return drawTexture( ttex );
  }

  /**
   * @param rnum Resource or Texture number.
   * @param dx Draw x coordinate(center).
   * @param dy Draw y coordinate(center).
   */
  public boolean drawTextureC( int rnum, float dx, float dy )
  {

    if ( existTexture( rnum ) == false )
    {
      return false;
    }

    ttex = getTexture( rnum );

    setFloatArray8(
      dx - ttex.width / 2.0f, dy - ttex.height / 2.0f,
      dx + ttex.width / 2.0f, dy - ttex.height / 2.0f,
      dx - ttex.width / 2.0f, dy + ttex.height / 2.0f,
      dx + ttex.width / 2.0f, dy + ttex.height );
    ttex.ver = createFloatBuffer( farr8 );

    setFloatArray8( 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f );
    ttex.uv   = createFloatBuffer( farr8 );

    return drawTexture( ttex );
  }

  /**
   * @param rnum Resource or Texture number.
   * @param rx Read x coordinate.
   * @param ry Read y coordinate.
   * @param w Width.
   * @param h Height.
   * @param dx Draw x coordinate.
   * @param dy Draw y coordinate.
   */
  public boolean drawTexture( int rnum,
      float rx, float ry, float w, float h,
      float dx, float dy )
  {
    if ( existTexture( rnum ) == false )
    {
      return false;
    }

    ttex = getTexture( rnum );

    setFloatArray8( dx, dy, dx + w, dy, dx, dy + h, dx + w, dy + h );
    setVertex( ttex, farr8 );

    setFloatArray8(
      rx     / ttex.width, ry     / ttex.height,
      rx + w / ttex.width, ry     / ttex.height,
      rx     / ttex.width, ry + h / ttex.height,
      rx + w / ttex.width, ry + h / ttex.height );
    setUV( ttex, farr8 );

    return drawTexture( ttex );
  }

  /**
   * @param rnum Resource or Texture number.
   * @param rx Read x coordinate.
   * @param ry Read y coordinate.
   * @param w Width.
   * @param h Height.
   * @param dx Draw x coordinate(center).
   * @param dy Draw y coordinate(center).
   */
  public boolean drawTextureC( int rnum,
    float rx, float ry, float w, float h,
    float dx, float dy )
  {
    if ( existTexture( rnum ) == false )
    {
      return false;
    }

    ttex = getTexture( rnum );

    setFloatArray8(
      dx - w / 2.0f, dy - h / 2.0f,
      dx + w / 2.0f, dy - h / 2.0f,
      dx - w / 2.0f, dy + h / 2.0f,
      dx + w / 2.0f, dy + h / 2.0f );
    setVertex( ttex, farr8 );

    setFloatArray8(
      rx     / ttex.width, ry     / ttex.height,
      rx + w / ttex.width, ry     / ttex.height,
      rx     / ttex.width, ry + h / ttex.height,
      rx + w / ttex.width, ry + h / ttex.height );
    setUV( ttex, farr8 );

    return drawTexture( ttex );
  }

  /**
   * @param rnum Resource or Texture number.
   * @param rx Read x coordinate.
   * @param ry Read y coordinate.
   * @param w Width.
   * @param h Height.
   * @param dx Draw x coordinate.
   * @param dy Draw y coordinate.
   * @param dw Draw width.
   * @param dh Draw height.
   */
  public boolean drawTextureScaring( int rnum,
    float rx, float ry, float w, float h,
    float dx, float dy, float dw, float dh )
  {
    if ( existTexture( rnum ) == false )
    {
      return false;
    }

    ttex = getTexture( rnum );

    setFloatArray8( dx, dy, dx + dw, dy, dx, dy + dh, dx + dw, dy + dh );
    setVertex( ttex, farr8 );

    setFloatArray8(
      rx     / ttex.width, ry     / ttex.height,
      rx + w / ttex.width, ry     / ttex.height,
      rx     / ttex.width, ry + h / ttex.height,
      rx + w / ttex.width, ry + h / ttex.height );
    setUV( ttex, farr8 );

    return drawTexture( ttex );
  }

  /**
   * @param rnum Resource or Texture number.
   * @param rx Read x coordinate.
   * @param ry Read y coordinate.
   * @param w Width.
   * @param h Height.
   * @param dx Draw x coordinate(center).
   * @param dy Draw y coordinate(center).
   * @param dw Draw width.
   * @param dh Draw height.
   */
  public boolean drawTextureScaringC( int rnum,
    float rx, float ry, float w, float h,
    float dx, float dy, float dw, float dh )
  {
    if ( existTexture( rnum ) == false )
    {
      return false;
    }

    ttex = getTexture( rnum );

    setFloatArray8(
      dx - dw / 2.0f, dy - dh / 2.0f,
      dx + dw / 2.0f, dy - dh / 2.0f,
      dx - dw / 2.0f, dy + dh / 2.0f,
      dx + dw / 2.0f, dy + dh / 2.0f );
    setVertex( ttex, farr8 );

    setFloatArray8(
      rx     / ttex.width, ry     / ttex.height,
      rx + w / ttex.width, ry     / ttex.height,
      rx     / ttex.width, ry + h / ttex.height,
      rx + w / ttex.width, ry + h / ttex.height );
    setUV( ttex, farr8 );

    return drawTexture( ttex );
  }

  /**
   * @param rnum Resource or Texture number.
   * @param rx Read x coordinate.
   * @param ry Read y coordinate.
   * @param w Width.
   * @param h Height.
   * @param dx Draw x coordinate.
   * @param dy Draw y coordinate.
   * @param scale Draw scale.
   */
  public boolean drawTextureScaring( int rnum,
    float rx, float ry, float w, float h,
    float dx, float dy, float scale )
  {
    if ( existTexture( rnum ) == false )
    {
      return false;
    }

    ttex = getTexture( rnum );

    setFloatArray8( dx, dy, dx + w * scale, dy, dx, dy + h * scale, dx + w * scale, dy + h * scale );
    setVertex( ttex, farr8 );

    setFloatArray8(
      rx     / ttex.width, ry     / ttex.height,
      rx + w / ttex.width, ry     / ttex.height,
      rx     / ttex.width, ry + h / ttex.height,
      rx + w / ttex.width, ry + h / ttex.height );
    setUV( ttex, farr8 );

    return drawTexture( ttex );
  }

  /**
   * @param rnum Resource or Texture number.
   * @param rx Read x coordinate.
   * @param ry Read y coordinate.
   * @param w Width.
   * @param h Height.
   * @param dx Draw x coordinate(center).
   * @param dy Draw y coordinate(center).
   * @param scale Draw scale.
   */
  public boolean drawTextureScaringC( int rnum,
    float rx, float ry, float w, float h,
    float dx, float dy, float scale )
  {
    if ( existTexture( rnum ) == false )
    {
      return false;
    }

    ttex = getTexture( rnum );

    scale /= 2.0f;
    setFloatArray8(
      dx - w * scale, dy - h * scale,
      dx + w * scale, dy - h * scale,
      dx - w * scale, dy + h * scale,
      dx + w * scale, dy + h * scale );
    setVertex( ttex, farr8 );

    setFloatArray8(
      rx     / ttex.width, ry     / ttex.height,
      rx + w / ttex.width, ry     / ttex.height,
      rx     / ttex.width, ry + h / ttex.height,
      rx + w / ttex.width, ry + h / ttex.height );
    setUV( ttex, farr8 );

    return drawTexture( ttex );
  }

  /**
   * @param rnum Resource or Texture number.
   * @param rx Read x coordinate.
   * @param ry Read y coordinate.
   * @param w Width.
   * @param h Height.
   * @param dx Draw x coordinate(center).
   * @param dy Draw y coordinate(center).
   * @param rad Rotation radian.
   */
  public boolean drawTextureRotationC( int rnum,
    float rx, float ry, float w, float h,
    float dx, float dy, float rad )
  {
    if ( existTexture( rnum ) == false )
    {
      return false;
    }

    ttex = getTexture( rnum );

    AMatrix.identityMatrix( mat );
    AMatrix.translateM( mat, 0, dx, dy, 0.0f );
    AMatrix.rotateM( mat, 0, rad * 180.0f / (float)Math.PI, 0.0f, 0.0f, 1.0f );

    setFloatArray4( - w / 2.0f, - h / 2.0f, 0.0f, 1.0f );
    AMatrix.multiplyMV( farr4, 0, mat, 0, farr4, 0 );
    farr8[ 0 ] = farr4[ 0 ]; farr8[ 1 ] = farr4[ 1 ];

    setFloatArray4( w / 2.0f, - h / 2.0f, 0.0f, 1.0f );
    AMatrix.multiplyMV( farr4, 0, mat, 0, farr4, 0 );
    farr8[ 2 ] = farr4[ 0 ]; farr8[ 3 ] = farr4[ 1 ];

    setFloatArray4( - w / 2.0f, h / 2.0f, 0.0f, 1.0f );
    AMatrix.multiplyMV( farr4, 0, mat, 0, farr4, 0 );
    farr8[ 4 ] = farr4[ 0 ]; farr8[ 5 ] = farr4[ 1 ];

    setFloatArray4( w / 2.0f, h / 2.0f, 0.0f, 1.0f );
    AMatrix.multiplyMV( farr4, 0, mat, 0, farr4, 0 );
    farr8[ 6 ] = farr4[ 0 ]; farr8[ 7 ] = farr4[ 1 ];

    setVertex( ttex, farr8 );

    setFloatArray8(
      rx     / ttex.width, ry     / ttex.height,
      rx + w / ttex.width, ry     / ttex.height,
      rx     / ttex.width, ry + h / ttex.height,
      rx + w / ttex.width, ry + h / ttex.height );
    setUV( ttex, farr8 );

    return drawTexture( ttex );
  }

  /**
   * @param rnum Resource or Texture number.
   * @param rx Read x coordinate.
   * @param ry Read y coordinate.
   * @param w Width.
   * @param h Height.
   * @param dx Draw x coordinate(center).
   * @param dy Draw y coordinate(center).
   * @param angle Rotation angle.
   */
  public boolean drawTextureRotationAngleC( int rnum,
    float rx, float ry, float w, float h,
    float dx, float dy, float angle )
  {
    if ( existTexture( rnum ) == false )
    {
      return false;
    }

    ttex = getTexture( rnum );

    AMatrix.identityMatrix( mat );
    AMatrix.translateM( mat, 0, dx, dy, 0.0f );
    AMatrix.rotateM( mat, 0, angle, 0.0f, 0.0f, 1.0f );

    setFloatArray4( - w / 2.0f, - h / 2.0f, 0.0f, 1.0f );
    AMatrix.multiplyMV( farr4, 0, mat, 0, farr4, 0 );
    farr8[ 0 ] = farr4[ 0 ]; farr8[ 1 ] = farr4[ 1 ];

    setFloatArray4( w / 2.0f, - h / 2.0f, 0.0f, 1.0f );
    AMatrix.multiplyMV( farr4, 0, mat, 0, farr4, 0 );
    farr8[ 2 ] = farr4[ 0 ]; farr8[ 3 ] = farr4[ 1 ];

    setFloatArray4( - w / 2.0f, h / 2.0f, 0.0f, 1.0f );
    AMatrix.multiplyMV( farr4, 0, mat, 0, farr4, 0 );
    farr8[ 4 ] = farr4[ 0 ]; farr8[ 5 ] = farr4[ 1 ];

    setFloatArray4( w / 2.0f, h / 2.0f, 0.0f, 1.0f );
    AMatrix.multiplyMV( farr4, 0, mat, 0, farr4, 0 );
    farr8[ 6 ] = farr4[ 0 ]; farr8[ 7 ] = farr4[ 1 ];

    setVertex( ttex, farr8 );

    setFloatArray8(
      rx     / ttex.width, ry     / ttex.height,
      rx + w / ttex.width, ry     / ttex.height,
      rx    / ttex.width, ry + h / ttex.height,
      rx + w / ttex.width, ry + h / ttex.height );
    setUV( ttex, farr8 );

    boolean ret = drawTexture( ttex );

    return ret;
  }

  /**
   * @param rnum Resource or Texture number.
   * @param rx Read x coordinate.
   * @param ry Read y coordinate.
   * @param w Width.
   * @param h Height.
   * @param dx Draw x coordinate(center).
   * @param dy Draw y coordinate(center).
   * @param scale Draw scale.
   * @param rad Rotation radian.
   */
  public boolean drawTextureScaleRotateC( int rnum,
    float rx, float ry, float w, float h,
    float dx, float dy, float scale, float rad )
  {
    if ( existTexture( rnum ) == false )
    {
      return false;
    }

    ttex = getTexture( rnum );

    AMatrix.identityMatrix( mat );
    AMatrix.translateM( mat, 0, dx, dy, 0.0f );
    AMatrix.scaleM( mat, 0, scale, scale, 1.0f );
    AMatrix.rotateM( mat, 0, rad * 180.0f / (float)Math.PI, 0.0f, 0.0f, 1.0f );

    setFloatArray4( - w / 2.0f, - h / 2.0f, 0.0f, 1.0f );
    AMatrix.multiplyMV( farr4, 0, mat, 0, farr4, 0 );
    farr8[ 0 ] = farr4[ 0 ]; farr8[ 1 ] = farr4[ 1 ];

    setFloatArray4( w / 2.0f, - h / 2.0f, 0.0f, 1.0f );
    AMatrix.multiplyMV( farr4, 0, mat, 0, farr4, 0 );
    farr8[ 2 ] = farr4[ 0 ]; farr8[ 3 ] = farr4[ 1 ];

    setFloatArray4( - w / 2.0f, h / 2.0f, 0.0f, 1.0f );
    AMatrix.multiplyMV( farr4, 0, mat, 0, farr4, 0 );
    farr8[ 4 ] = farr4[ 0 ]; farr8[ 5 ] = farr4[ 1 ];

    setFloatArray4( w / 2.0f, h / 2.0f, 0.0f, 1.0f );
    AMatrix.multiplyMV( farr4, 0, mat, 0, farr4, 0 );
    farr8[ 6 ] = farr4[ 0 ]; farr8[ 7 ] = farr4[ 1 ];

    setVertex( ttex, farr8 );

    setFloatArray8(
      rx     / ttex.width, ry     / ttex.height,
      rx + w / ttex.width, ry     / ttex.height,
      rx     / ttex.width, ry + h / ttex.height,
      rx + w / ttex.width, ry + h / ttex.height );
    setUV( ttex, farr8 );

    boolean ret = drawTexture( ttex );

    return ret;
  }

  /**
   * @param rnum Resource or Texture number.
   * @param rx Read x coordinate.
   * @param ry Read y coordinate.
   * @param w Width.
   * @param h Height.
   * @param dx Draw x coordinate(center).
   * @param dy Draw y coordinate(center).
   * @param scale Draw scale.
   * @param angle Rotation angle.
   */
  public boolean drawTextureScaleRotateAngleC( int rnum,
    float rx, float ry, float w, float h,
    float dx, float dy, float scale, float angle )
  {
    if ( existTexture( rnum ) == false )
    {
      return false;
    }

    ttex = getTexture( rnum );

    AMatrix.identityMatrix( mat );
    AMatrix.translateM( mat, 0, dx, dy, 0.0f );
    AMatrix.scaleM( mat, 0, scale, scale, 1.0f );
    AMatrix.rotateM( mat, 0, angle, 0.0f, 0.0f, 1.0f );

    setFloatArray4( - w / 2.0f, - h / 2.0f, 0.0f, 1.0f );
    AMatrix.multiplyMV( farr4, 0, mat, 0, farr4, 0 );
    farr8[ 0 ] = farr4[ 0 ]; farr8[ 1 ] = farr4[ 1 ];

    setFloatArray4( w / 2.0f, - h / 2.0f, 0.0f, 1.0f );
    AMatrix.multiplyMV( farr4, 0, mat, 0, farr4, 0 );
    farr8[ 2 ] = farr4[ 0 ]; farr8[ 3 ] = farr4[ 1 ];

    setFloatArray4( - w / 2.0f, h / 2.0f, 0.0f, 1.0f );
    AMatrix.multiplyMV( farr4, 0, mat, 0, farr4, 0 );
    farr8[ 4 ] = farr4[ 0 ]; farr8[ 5 ] = farr4[ 1 ];

    setFloatArray4( w / 2.0f, h / 2.0f, 0.0f, 1.0f );
    AMatrix.multiplyMV( farr4, 0, mat, 0, farr4, 0 );
    farr8[ 6 ] = farr4[ 0 ]; farr8[ 7 ] = farr4[ 1 ];

    setVertex( ttex, farr8 );

    setFloatArray8(
      rx     / ttex.width, ry     / ttex.height,
      rx + w / ttex.width, ry     / ttex.height,
      rx     / ttex.width, ry + h / ttex.height,
      rx + w / ttex.width, ry + h / ttex.height );
    setUV( ttex, farr8 );

    boolean ret = drawTexture( ttex );

    return ret;
  }

  /**
   * @param rnum Resource or Texture number.
   * @param rx Read x coordinate.
   * @param ry Read y coordinate.
   * @param w Width.
   * @param h Height.
   * @param matrix44 4 * 4 draw matrix.
   */
  public boolean drawTextureMatrix( int rnum,
    float rx, float ry, float w, float h,
    float[] matrix44 )
  {
    if ( existTexture( rnum ) == false )
    {
      return false;
    }

    ttex = getTexture( rnum );

    setFloatArray4( 0.0f, 0.0f, 0.0f, 1.0f );
    AMatrix.multiplyMV( farr4, 0, matrix44, 0, farr4, 0 );
    farr8[ 0 ] = farr4[ 0 ]; farr8[ 1 ] = farr4[ 1 ];

    setFloatArray4( w, 0.0f, 0.0f, 1.0f );
    AMatrix.multiplyMV( farr4, 0, matrix44, 0, farr4, 0 );
    farr8[ 2 ] = farr4[ 0 ]; farr8[ 3 ] = farr4[ 1 ];

    setFloatArray4( 0.0f, h, 0.0f, 1.0f );
    AMatrix.multiplyMV( farr4, 0, matrix44, 0, farr4, 0 );
    farr8[ 4 ] = farr4[ 0 ]; farr8[ 5 ] = farr4[ 1 ];

    setFloatArray4( w, h, 0.0f, 1.0f );
    AMatrix.multiplyMV( farr4, 0, matrix44, 0, farr4, 0 );
    farr8[ 6 ] = farr4[ 0 ]; farr8[ 7 ] = farr4[ 1 ];

    setVertex( ttex, farr8 );

    setFloatArray8(
      rx     / ttex.width, ry     / ttex.height,
      rx + w / ttex.width, ry     / ttex.height,
      rx     / ttex.width, ry + h / ttex.height,
      rx + w / ttex.width, ry + h / ttex.height );
    setUV( ttex, farr8 );

    boolean ret = drawTexture( ttex );

    return ret;
  }

  /**
   * @param rnum Resource or Texture number.
   * @param rx Read x coordinate.
   * @param ry Read y coordinate.
   * @param w Width.
   * @param h Height.
   * @param dx Draw x coordinate(center).
   * @param dy Draw y coordinate(center).
   * @param matrix44 4 * 4 draw matrix.
   */
  public boolean drawTextureMatrixC( int rnum,
    float rx, float ry, float w, float h,
    float[] matrix44 )
  {
    if ( existTexture( rnum ) == false )
    {
      return false;
    }

    ttex = getTexture( rnum );

    setFloatArray4( - w / 2.0f, - h / 2.0f, 0.0f, 1.0f );
    AMatrix.multiplyMV( farr4, 0, matrix44, 0, farr4, 0 );
    farr8[ 0 ] = farr4[ 0 ]; farr8[ 1 ] = farr4[ 1 ];

    setFloatArray4( w / 2.0f, - h / 2.0f, 0.0f, 1.0f );
    AMatrix.multiplyMV( farr4, 0, matrix44, 0, farr4, 0 );
    farr8[ 2 ] = farr4[ 0 ]; farr8[ 3 ] = farr4[ 1 ];

    setFloatArray4( - w / 2.0f, h / 2.0f, 0.0f, 1.0f );
    AMatrix.multiplyMV( farr4, 0, matrix44, 0, farr4, 0 );
    farr8[ 4 ] = farr4[ 0 ]; farr8[ 5 ] = farr4[ 1 ];

    setFloatArray4( w / 2.0f, h / 2.0f, 0.0f, 1.0f );
    AMatrix.multiplyMV( farr4, 0, matrix44, 0, farr4, 0 );
    farr8[ 6 ] = farr4[ 0 ]; farr8[ 7 ] = farr4[ 1 ];

    setVertex( ttex, farr8 );

    setFloatArray8(
      rx     / ttex.width, ry     / ttex.height,
      rx + w / ttex.width, ry     / ttex.height,
      rx     / ttex.width, ry + h / ttex.height,
      rx + w / ttex.width, ry + h / ttex.height );
    setUV( ttex, farr8 );

    boolean ret = drawTexture( ttex );

    return ret;
  }

  /**
   * @param rnum Resource or Texture number.
   * @param rx Read x coordinate.
   * @param ry Read y coordinate.
   * @param w Width.
   * @param h Height.
   * @param dx0 Draw x coordinate(left up).
   * @param dy0 Draw y coordinate(left up).
   * @param dx1 Draw x coordinate(right up).
   * @param dy1 Draw y coordinate(right up).
   * @param dx2 Draw x coordinate(left down).
   * @param dy2 Draw y coordinate(left down).
   * @param dx3 Draw x coordinate.
   * @param dy3 Draw y coordinate.
   */
  public boolean drawTextureVertex( int rnum,
    float rx, float ry, float w, float h,
    float dx0, float dy0,
    float dx1, float dy1,
    float dx2, float dy2,
    float dx3, float dy3 )
  {
    if ( existTexture( rnum ) == false )
    {
      return false;
    }

    ttex = getTexture( rnum );

    setFloatArray8( dx0, dy0, dx1, dy1, dx2, dy2, dx3, dy3 );
    setVertex( ttex, farr8 );

    setFloatArray8(
      rx     / ttex.width, ry     / ttex.height,
      rx + w / ttex.width, ry     / ttex.height,
      rx     / ttex.width, ry + h / ttex.height,
      rx + w / ttex.width, ry + h / ttex.height );
    setUV( ttex, farr8 );

    boolean ret = drawTexture( ttex );

    return ret;
  }

}

