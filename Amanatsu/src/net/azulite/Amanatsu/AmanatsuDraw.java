package net.azulite.Amanatsu;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Color;
import android.opengl.GLUtils;
import android.opengl.Matrix;

/**
 * @author Hiroki
 */


// Library.
// TODO:

public class AmanatsuDraw
{
  GL10 gl = null;
  int width, height;
  Resources resource;
  static Map<Integer, Texture> textures = new Hashtable< Integer, Texture >();
  static Map<Integer, Paint> paints = new Hashtable< Integer, Paint >();

  // String.
  Bitmap stringbmp;
  Texture stringtex;
  int stringnum;

  // tmp.
  private float[] farr4, farr8, mat;
  private Texture ttex;
  private Paint tpaint;

  public AmanatsuDraw( Context context )
  {
    resource = context.getResources();
    farr4 = new float[ 4 ];
    farr8 = new float[ 8 ];
    mat = new float[ 16 ];

    stringbmp = Bitmap.createBitmap( 512, 512, Config.ALPHA_8 );//Bitmap.Config.ARGB_8888);
    this.CreateFont( 0, 30, 1.0f, 1.0f, 1.0f, 1.0f );
  }

  public void Init()
  {
    this.CreateTextureFromBitmap( 0, stringbmp, false );
    stringtex = ttex;
    stringnum = ttex.texid[ 0 ];
    stringtex.col  = CreateFloatBuffer( new float[]{ 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f } );
  }

  public void Release()
  {
    stringbmp.recycle();
  }

  public boolean SetGL( GL10 gl )
  {
    this.gl = gl;
    return true;
  }

  /**
   * Clear Screen(black).
   */
  public boolean ClearScreen()
  {
    return this.ClearScreen( 0.0f, 0.0f, 0.0f );
  }

  /**
   * Clear Screen.
   * @param red Red color(0.0f-1.0f).
   * @param red Green color(0.0f-1.0f).
   * @param red Blue color(0.0f-1.0f).
   */
  public boolean ClearScreen( float red, float green, float blue)
  {
    gl.glClearColor( red, green, blue, 1.0f );
    gl.glClear( GL10.GL_COLOR_BUFFER_BIT );

    return true;
  }

  /**
   * @param x Draw x oordinate.
   * @param y Draw y cordinate.
   * @param w Width.
   * @param h Height.
   * @param color [ red, green, blue, alpha ] array(value 0.0f-1.0f).
   * */
  public boolean DrawBox( float x, float y, float w, float h, float[] color )
  {
    this.SetFloatArray8( x, y, x + w, y, x, y + h, x + w, y + h );

    gl.glVertexPointer( 2, GL10.GL_FLOAT, 0, CreateFloatBuffer( farr8 ) );
    gl.glEnableClientState( GL10.GL_VERTEX_ARRAY );

    gl.glColorPointer( 4, GL10.GL_FLOAT, 0, CreateColor( color ) );
    gl.glEnableClientState( GL10.GL_COLOR_ARRAY );

    gl.glDrawArrays( GL10.GL_TRIANGLE_STRIP, 0, 4 );

    return true;
  }

  /**
   * @param x Draw x oordinate(center).
   * @param y Draw y cordinate(center).
   * @param w Width.
   * @param h Height.
   * @param color [ red, green, blue, alpha ] array(value 0.0f-1.0f).
   * */
  public boolean DrawBoxC( float x, float y, float w, float h, float[] color )
  {
    this.SetFloatArray8( x - w / 2.0f, y - h / 2.0f, x + w / 2.0f, y - h / 2.0f, x - w / 2.0f, y + h / 2.0f, x + w / 2.0f, y + h / 2.0f );

    gl.glVertexPointer( 2, GL10.GL_FLOAT, 0, CreateFloatBuffer( farr8 ) );
    gl.glEnableClientState( GL10.GL_VERTEX_ARRAY );

    gl.glColorPointer( 4, GL10.GL_FLOAT, 0, CreateColor( color ) );
    gl.glEnableClientState( GL10.GL_COLOR_ARRAY );

    gl.glDrawArrays( GL10.GL_TRIANGLE_STRIP, 0, 4 );

    return true;
  }

  public int CreateTexture( int rnum )
  {
    return this.CreateTextureFromBitmap( rnum, BitmapFactory.decodeResource( resource, rnum ), true );
  }

  public int CreateTexture( int tnum, int rnum )
  {
    return this.CreateTextureFromBitmap( tnum, BitmapFactory.decodeResource( resource, rnum ), true );
  }

  public int CreateTexture( int rnum, Bitmap bmp )
  {
    return this.CreateTextureFromBitmap( rnum, bmp, true );
  }

  private int CreateTextureFromBitmap( int rnum, Bitmap bmp, boolean regist )
  {
    if ( bmp == null )
    {
      return -1;
    }

    if ( regist )
    {
      if ( textures.containsKey( rnum ) )
      {
        ReleaseTexture( rnum );
        ttex = textures.get( rnum );
      } else
      {
        ttex = new Texture();
        ttex.texid = new int [ 1 ];
        textures.put( rnum, ttex );
      }
    } else
    {
      // Unregist.
      ttex = new Texture();
      ttex.texid = new int [ 1 ];
    }

    gl.glGenTextures( 1, ttex.texid, 0 );
    gl.glBindTexture( GL10.GL_TEXTURE_2D, ttex.texid[ 0 ] );

    if ( AmanatsuDraw.IsPowerOf2( bmp.getWidth() ) == false ||
         AmanatsuDraw.IsPowerOf2( bmp.getHeight() ) == false ||
         bmp.getWidth() != bmp.getHeight() )
    {
      int length = AmanatsuDraw.ConvertPowerOf2( bmp.getWidth() >= bmp.getHeight() ? bmp.getWidth() : bmp.getHeight() );
      bmp = AmanatsuDraw.ResizeBitmap( bmp, length );
    }
    GLUtils.texImage2D( GL10.GL_TEXTURE_2D, 0, bmp, 0 );

    gl.glTexParameterf( GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR );
    //gl.glTexParameterf( GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR );
    gl.glBindTexture( GL10.GL_TEXTURE_2D, 0 );

    ttex.width = bmp.getWidth();
    ttex.height = bmp.getHeight();

    if ( regist )
    {
      this.SetColor( rnum );
      bmp.recycle();
    }

    return ttex.texid[ 0 ];
  }

  public boolean ExistTexture( int rnum ){ return (textures.containsKey( rnum ) && textures.get( rnum ).texid != null );}

  public void DestroyTexture( int rnum )
  {
    if ( this.ReleaseTexture( rnum ) )
    {
      textures.remove( rnum );
    }
  }

  public boolean ReleaseTexture( int rnum )
  {
    if ( this.ExistTexture( rnum ) )
    {
      gl.glDeleteTextures( 1, textures.get( rnum ).texid, 0 );
      return true;
    }
    return false;
  }

  public void ReleaseTextureAll()
  {
    List<Integer> keys = new ArrayList<Integer>( textures.keySet() );
    for ( Integer key : keys )
    {
      this.ReleaseTexture( key );
    }
  }

  private boolean DrawTexture( Texture tex )
  {
    gl.glEnable( GL10.GL_TEXTURE_2D );
    gl.glBindTexture( GL10.GL_TEXTURE_2D, tex.texid[ 0 ] );

    gl.glVertexPointer( 2, GL10.GL_FLOAT, 0, tex.ver );
    gl.glEnableClientState( GL10.GL_VERTEX_ARRAY );

    gl.glColorPointer( 4, GL10.GL_FLOAT, 0, tex.col );
    gl.glEnableClientState( GL10.GL_COLOR_ARRAY );

    gl.glTexCoordPointer( 2, GL10.GL_FLOAT, 0, tex.uv );
    gl.glEnableClientState( GL10.GL_TEXTURE_COORD_ARRAY );

    gl.glDrawArrays( GL10.GL_TRIANGLE_STRIP, 0, 4 );

    gl.glDisableClientState( GL10.GL_TEXTURE_COORD_ARRAY );

    gl.glDisable( GL10.GL_TEXTURE_2D );

    return true;
  }

  /**
   * @param rnum Resource or Texture number.
   * @param dx Draw x coordinate.
   * @param dy Draw y coordinate.
   * */
  public boolean DrawTexture( int rnum, float dx, float dy )
  {

    if ( this.ExistTexture( rnum ) == false )
    {
      return false;
    }

    ttex = this.GetTexture( rnum );

    this.SetFloatArray8( dx, dy, dx + ttex.width, dy, dx, dy + ttex.height, dx + ttex.width, dy + ttex.height );
    ttex.ver = CreateFloatBuffer( farr8 );

    this.SetFloatArray8( 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f );
    ttex.uv   = CreateFloatBuffer( farr8 );

    return this.DrawTexture( ttex );
  }

  /**
   * @param rnum Resource or Texture number.
   * @param dx Draw x coordinate(center).
   * @param dy Draw y coordinate(center).
   * */
  public boolean DrawTextureC( int rnum, float dx, float dy )
  {

    if ( this.ExistTexture( rnum ) == false )
    {
      return false;
    }

    ttex = this.GetTexture( rnum );

    this.SetFloatArray8(
      dx - ttex.width / 2.0f, dy - ttex.height / 2.0f,
      dx + ttex.width / 2.0f, dy - ttex.height / 2.0f,
      dx - ttex.width / 2.0f, dy + ttex.height / 2.0f,
      dx + ttex.width / 2.0f, dy + ttex.height );
    ttex.ver = CreateFloatBuffer( farr8 );

    this.SetFloatArray8( 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f );
    ttex.uv   = CreateFloatBuffer( farr8 );

    return this.DrawTexture( ttex );
  }

  /**
   * @param rnum Resource or Texture number.
   * @param rx Read x coordinate.
   * @param ry Read y coordinate.
   * @param w Width.
   * @param h Height.
   * @param dx Draw x coordinate.
   * @param dy Draw y coordinate.
   * */
  public boolean DrawTexture( int rnum,
      float rx, float ry, float w, float h,
      float dx, float dy )
  {
    if ( this.ExistTexture( rnum ) == false )
    {
      return false;
    }

    ttex = this.GetTexture( rnum );

    this.SetFloatArray8( dx, dy, dx + w, dy, dx, dy + h, dx + w, dy + h );
    this.SetVertex( ttex, farr8 );

    this.SetFloatArray8(
      rx     / ttex.width, ry     / ttex.height,
      rx + w / ttex.width, ry     / ttex.height,
      rx     / ttex.width, ry + h / ttex.height,
      rx + w / ttex.width, ry + h / ttex.height );
    this.SetUV( ttex, farr8 );

    return this.DrawTexture( ttex );
  }

  /**
   * @param rnum Resource or Texture number.
   * @param rx Read x coordinate.
   * @param ry Read y coordinate.
   * @param w Width.
   * @param h Height.
   * @param dx Draw x coordinate(center).
   * @param dy Draw y coordinate(center).
   * */
  public boolean DrawTextureC( int rnum,
    float rx, float ry, float w, float h,
    float dx, float dy )
  {
    if ( this.ExistTexture( rnum ) == false )
    {
      return false;
    }

    ttex = this.GetTexture( rnum );

    this.SetFloatArray8(
      dx - w / 2.0f, dy - h / 2.0f,
      dx + w / 2.0f, dy - h / 2.0f,
      dx - w / 2.0f, dy + h / 2.0f,
      dx + w / 2.0f, dy + h / 2.0f );
    this.SetVertex( ttex, farr8 );

    this.SetFloatArray8(
      rx     / ttex.width, ry     / ttex.height,
      rx + w / ttex.width, ry     / ttex.height,
      rx     / ttex.width, ry + h / ttex.height,
      rx + w / ttex.width, ry + h / ttex.height );
    this.SetUV( ttex, farr8 );

    return this.DrawTexture( ttex );
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
   * */
  public boolean DrawTextureScaring( int rnum,
    float rx, float ry, float w, float h,
    float dx, float dy, float dw, float dh )
  {
    if ( this.ExistTexture( rnum ) == false )
    {
      return false;
    }

    ttex = this.GetTexture( rnum );

    this.SetFloatArray8( dx, dy, dx + dw, dy, dx, dy + dh, dx + dw, dy + dh );
    this.SetVertex( ttex, farr8 );

    this.SetFloatArray8(
      rx     / ttex.width, ry     / ttex.height,
      rx + w / ttex.width, ry     / ttex.height,
      rx     / ttex.width, ry + h / ttex.height,
      rx + w / ttex.width, ry + h / ttex.height );
    this.SetUV( ttex, farr8 );

    return this.DrawTexture( ttex );
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
   * */
  public boolean DrawTextureScaringC( int rnum,
    float rx, float ry, float w, float h,
    float dx, float dy, float dw, float dh )
  {
    if ( this.ExistTexture( rnum ) == false )
    {
      return false;
    }

    ttex = this.GetTexture( rnum );

    this.SetFloatArray8(
      dx - dw / 2.0f, dy - dh / 2.0f,
      dx + dw / 2.0f, dy - dh / 2.0f,
      dx - dw / 2.0f, dy + dh / 2.0f,
      dx + dw / 2.0f, dy + dh / 2.0f );
    this.SetVertex( ttex, farr8 );

    this.SetFloatArray8(
      rx     / ttex.width, ry     / ttex.height,
      rx + w / ttex.width, ry     / ttex.height,
      rx     / ttex.width, ry + h / ttex.height,
      rx + w / ttex.width, ry + h / ttex.height );
    this.SetUV( ttex, farr8 );

    return this.DrawTexture( ttex );
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
   * */
  public boolean DrawTextureScaring( int rnum,
    float rx, float ry, float w, float h,
    float dx, float dy, float scale )
  {
    if ( this.ExistTexture( rnum ) == false )
    {
      return false;
    }

    ttex = this.GetTexture( rnum );

    this.SetFloatArray8( dx, dy, dx + w * scale, dy, dx, dy + h * scale, dx + w * scale, dy + h * scale );
    this.SetVertex( ttex, farr8 );

    this.SetFloatArray8(
      rx     / ttex.width, ry     / ttex.height,
      rx + w / ttex.width, ry     / ttex.height,
      rx     / ttex.width, ry + h / ttex.height,
      rx + w / ttex.width, ry + h / ttex.height );
    this.SetUV( ttex, farr8 );

    return this.DrawTexture( ttex );
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
   * */
  public boolean DrawTextureScaringC( int rnum,
    float rx, float ry, float w, float h,
    float dx, float dy, float scale )
  {
    if ( this.ExistTexture( rnum ) == false )
    {
      return false;
    }

    ttex = this.GetTexture( rnum );

    scale /= 2.0f;
    this.SetFloatArray8(
      dx - w * scale, dy - h * scale,
      dx + w * scale, dy - h * scale,
      dx - w * scale, dy + h * scale,
      dx + w * scale, dy + h * scale );
    this.SetVertex( ttex, farr8 );

    this.SetFloatArray8(
      rx     / ttex.width, ry     / ttex.height,
      rx + w / ttex.width, ry     / ttex.height,
      rx     / ttex.width, ry + h / ttex.height,
      rx + w / ttex.width, ry + h / ttex.height );
    this.SetUV( ttex, farr8 );

    return this.DrawTexture( ttex );
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
   * */
  public boolean DrawTextureRotationC( int rnum,
    float rx, float ry, float w, float h,
    float dx, float dy, float rad )
  {
    if ( this.ExistTexture( rnum ) == false )
    {
      return false;
    }

    ttex = this.GetTexture( rnum );

    AMatrix.IdentityMatrix( mat );
    AMatrix.translateM( mat, 0, dx, dy, 0.0f );
    AMatrix.rotateM( mat, 0, rad * 180.0f / (float)Math.PI, 0.0f, 0.0f, 1.0f );

    this.SetFloatArray4( - w / 2.0f, - h / 2.0f, 0.0f, 1.0f );
    AMatrix.multiplyMV( farr4, 0, mat, 0, farr4, 0 );
    farr8[ 0 ] = farr4[ 0 ]; farr8[ 1 ] = farr4[ 1 ];

    this.SetFloatArray4( w / 2.0f, - h / 2.0f, 0.0f, 1.0f );
    AMatrix.multiplyMV( farr4, 0, mat, 0, farr4, 0 );
    farr8[ 2 ] = farr4[ 0 ]; farr8[ 3 ] = farr4[ 1 ];

    this.SetFloatArray4( - w / 2.0f, h / 2.0f, 0.0f, 1.0f );
    AMatrix.multiplyMV( farr4, 0, mat, 0, farr4, 0 );
    farr8[ 4 ] = farr4[ 0 ]; farr8[ 5 ] = farr4[ 1 ];

    this.SetFloatArray4( w / 2.0f, h / 2.0f, 0.0f, 1.0f );
    AMatrix.multiplyMV( farr4, 0, mat, 0, farr4, 0 );
    farr8[ 6 ] = farr4[ 0 ]; farr8[ 7 ] = farr4[ 1 ];

    this.SetVertex( ttex, farr8 );

    this.SetFloatArray8(
      rx     / ttex.width, ry     / ttex.height,
      rx + w / ttex.width, ry     / ttex.height,
      rx     / ttex.width, ry + h / ttex.height,
      rx + w / ttex.width, ry + h / ttex.height );
    this.SetUV( ttex, farr8 );

    boolean ret = this.DrawTexture( ttex );

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
   * @param angle Rotation angle.
   */
  public boolean DrawTextureRotationAngleC( int rnum,
    float rx, float ry, float w, float h,
    float dx, float dy, float angle )
  {
    if ( this.ExistTexture( rnum ) == false )
    {
      return false;
    }

    ttex = this.GetTexture( rnum );

    AMatrix.IdentityMatrix( mat );
    AMatrix.translateM( mat, 0, dx, dy, 0.0f );
    AMatrix.rotateM( mat, 0, angle, 0.0f, 0.0f, 1.0f );

    this.SetFloatArray4( - w / 2.0f, - h / 2.0f, 0.0f, 1.0f );
    AMatrix.multiplyMV( farr4, 0, mat, 0, farr4, 0 );
    farr8[ 0 ] = farr4[ 0 ]; farr8[ 1 ] = farr4[ 1 ];

    this.SetFloatArray4( w / 2.0f, - h / 2.0f, 0.0f, 1.0f );
    AMatrix.multiplyMV( farr4, 0, mat, 0, farr4, 0 );
    farr8[ 2 ] = farr4[ 0 ]; farr8[ 3 ] = farr4[ 1 ];

    this.SetFloatArray4( - w / 2.0f, h / 2.0f, 0.0f, 1.0f );
    AMatrix.multiplyMV( farr4, 0, mat, 0, farr4, 0 );
    farr8[ 4 ] = farr4[ 0 ]; farr8[ 5 ] = farr4[ 1 ];

    this.SetFloatArray4( w / 2.0f, h / 2.0f, 0.0f, 1.0f );
    AMatrix.multiplyMV( farr4, 0, mat, 0, farr4, 0 );
    farr8[ 6 ] = farr4[ 0 ]; farr8[ 7 ] = farr4[ 1 ];

    this.SetVertex( ttex, farr8 );

    this.SetFloatArray8(
      rx     / ttex.width, ry     / ttex.height,
      rx + w / ttex.width, ry     / ttex.height,
      rx    / ttex.width, ry + h / ttex.height,
      rx + w / ttex.width, ry + h / ttex.height );
    this.SetUV( ttex, farr8 );

    boolean ret = this.DrawTexture( ttex );

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
   * */
  public boolean DrawTextureScaleRotateC( int rnum,
    float rx, float ry, float w, float h,
    float dx, float dy, float scale, float rad )
  {
    if ( this.ExistTexture( rnum ) == false )
    {
      return false;
    }

    ttex = this.GetTexture( rnum );

    AMatrix.IdentityMatrix( mat );
    AMatrix.translateM( mat, 0, dx, dy, 0.0f );
    AMatrix.scaleM( mat, 0, scale, scale, 1.0f );
    AMatrix.rotateM( mat, 0, rad * 180.0f / (float)Math.PI, 0.0f, 0.0f, 1.0f );

    this.SetFloatArray4( - w / 2.0f, - h / 2.0f, 0.0f, 1.0f );
    AMatrix.multiplyMV( farr4, 0, mat, 0, farr4, 0 );
    farr8[ 0 ] = farr4[ 0 ]; farr8[ 1 ] = farr4[ 1 ];

    this.SetFloatArray4( w / 2.0f, - h / 2.0f, 0.0f, 1.0f );
    AMatrix.multiplyMV( farr4, 0, mat, 0, farr4, 0 );
    farr8[ 2 ] = farr4[ 0 ]; farr8[ 3 ] = farr4[ 1 ];

    this.SetFloatArray4( - w / 2.0f, h / 2.0f, 0.0f, 1.0f );
    AMatrix.multiplyMV( farr4, 0, mat, 0, farr4, 0 );
    farr8[ 4 ] = farr4[ 0 ]; farr8[ 5 ] = farr4[ 1 ];

    this.SetFloatArray4( w / 2.0f, h / 2.0f, 0.0f, 1.0f );
    AMatrix.multiplyMV( farr4, 0, mat, 0, farr4, 0 );
    farr8[ 6 ] = farr4[ 0 ]; farr8[ 7 ] = farr4[ 1 ];

    this.SetVertex( ttex, farr8 );

    this.SetFloatArray8(
      rx     / ttex.width, ry     / ttex.height,
      rx + w / ttex.width, ry     / ttex.height,
      rx     / ttex.width, ry + h / ttex.height,
      rx + w / ttex.width, ry + h / ttex.height );
    this.SetUV( ttex, farr8 );

    boolean ret = this.DrawTexture( ttex );

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
   * */
  public boolean DrawTextureScaleRotateAngleC( int rnum,
    float rx, float ry, float w, float h,
    float dx, float dy, float scale, float angle )
  {
    if ( this.ExistTexture( rnum ) == false )
    {
      return false;
    }

    ttex = this.GetTexture( rnum );

    AMatrix.IdentityMatrix( mat );
    AMatrix.translateM( mat, 0, dx, dy, 0.0f );
    AMatrix.scaleM( mat, 0, scale, scale, 1.0f );
    AMatrix.rotateM( mat, 0, angle, 0.0f, 0.0f, 1.0f );

    this.SetFloatArray4( - w / 2.0f, - h / 2.0f, 0.0f, 1.0f );
    AMatrix.multiplyMV( farr4, 0, mat, 0, farr4, 0 );
    farr8[ 0 ] = farr4[ 0 ]; farr8[ 1 ] = farr4[ 1 ];

    this.SetFloatArray4( w / 2.0f, - h / 2.0f, 0.0f, 1.0f );
    AMatrix.multiplyMV( farr4, 0, mat, 0, farr4, 0 );
    farr8[ 2 ] = farr4[ 0 ]; farr8[ 3 ] = farr4[ 1 ];

    this.SetFloatArray4( - w / 2.0f, h / 2.0f, 0.0f, 1.0f );
    AMatrix.multiplyMV( farr4, 0, mat, 0, farr4, 0 );
    farr8[ 4 ] = farr4[ 0 ]; farr8[ 5 ] = farr4[ 1 ];

    this.SetFloatArray4( w / 2.0f, h / 2.0f, 0.0f, 1.0f );
    AMatrix.multiplyMV( farr4, 0, mat, 0, farr4, 0 );
    farr8[ 6 ] = farr4[ 0 ]; farr8[ 7 ] = farr4[ 1 ];

    this.SetVertex( ttex, farr8 );

    this.SetFloatArray8(
      rx     / ttex.width, ry     / ttex.height,
      rx + w / ttex.width, ry     / ttex.height,
      rx     / ttex.width, ry + h / ttex.height,
      rx + w / ttex.width, ry + h / ttex.height );
    this.SetUV( ttex, farr8 );

    boolean ret = this.DrawTexture( ttex );

    return ret;
  }

  /**
   * @param rnum Resource or Texture number.
   * @param rx Read x coordinate.
   * @param ry Read y coordinate.
   * @param w Width.
   * @param h Height.
   * @param matrix44 4 * 4 draw matrix.
   * */
  public boolean DrawTextureMatrix( int rnum,
    float rx, float ry, float w, float h,
    float[] matrix44 )
  {
    if ( this.ExistTexture( rnum ) == false )
    {
      return false;
    }

    ttex = this.GetTexture( rnum );

    this.SetFloatArray4( 0.0f, 0.0f, 0.0f, 1.0f );
    AMatrix.multiplyMV( farr4, 0, matrix44, 0, farr4, 0 );
    farr8[ 0 ] = farr4[ 0 ]; farr8[ 1 ] = farr4[ 1 ];

    this.SetFloatArray4( w, 0.0f, 0.0f, 1.0f );
    AMatrix.multiplyMV( farr4, 0, matrix44, 0, farr4, 0 );
    farr8[ 2 ] = farr4[ 0 ]; farr8[ 3 ] = farr4[ 1 ];

    this.SetFloatArray4( 0.0f, h, 0.0f, 1.0f );
    AMatrix.multiplyMV( farr4, 0, matrix44, 0, farr4, 0 );
    farr8[ 4 ] = farr4[ 0 ]; farr8[ 5 ] = farr4[ 1 ];

    this.SetFloatArray4( w, h, 0.0f, 1.0f );
    AMatrix.multiplyMV( farr4, 0, matrix44, 0, farr4, 0 );
    farr8[ 6 ] = farr4[ 0 ]; farr8[ 7 ] = farr4[ 1 ];

    this.SetVertex( ttex, farr8 );

    this.SetFloatArray8(
      rx     / ttex.width, ry     / ttex.height,
      rx + w / ttex.width, ry     / ttex.height,
      rx     / ttex.width, ry + h / ttex.height,
      rx + w / ttex.width, ry + h / ttex.height );
    this.SetUV( ttex, farr8 );

    boolean ret = this.DrawTexture( ttex );

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
   * */
  public boolean DrawTextureMatrixC( int rnum,
    float rx, float ry, float w, float h,
    float[] matrix44 )
  {
    if ( this.ExistTexture( rnum ) == false )
    {
      return false;
    }

    ttex = this.GetTexture( rnum );

    this.SetFloatArray4( - w / 2.0f, - h / 2.0f, 0.0f, 1.0f );
    AMatrix.multiplyMV( farr4, 0, matrix44, 0, farr4, 0 );
    farr8[ 0 ] = farr4[ 0 ]; farr8[ 1 ] = farr4[ 1 ];

    this.SetFloatArray4( w / 2.0f, - h / 2.0f, 0.0f, 1.0f );
    AMatrix.multiplyMV( farr4, 0, matrix44, 0, farr4, 0 );
    farr8[ 2 ] = farr4[ 0 ]; farr8[ 3 ] = farr4[ 1 ];

    this.SetFloatArray4( - w / 2.0f, h / 2.0f, 0.0f, 1.0f );
    AMatrix.multiplyMV( farr4, 0, matrix44, 0, farr4, 0 );
    farr8[ 4 ] = farr4[ 0 ]; farr8[ 5 ] = farr4[ 1 ];

    this.SetFloatArray4( w / 2.0f, h / 2.0f, 0.0f, 1.0f );
    AMatrix.multiplyMV( farr4, 0, matrix44, 0, farr4, 0 );
    farr8[ 6 ] = farr4[ 0 ]; farr8[ 7 ] = farr4[ 1 ];

    this.SetVertex( ttex, farr8 );

    this.SetFloatArray8(
      rx     / ttex.width, ry     / ttex.height,
      rx + w / ttex.width, ry     / ttex.height,
      rx     / ttex.width, ry + h / ttex.height,
      rx + w / ttex.width, ry + h / ttex.height );
    this.SetUV( ttex, farr8 );

    boolean ret = this.DrawTexture( ttex );

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
   * */
  public boolean DrawTextureVertex( int rnum,
    float rx, float ry, float w, float h,
    float dx0, float dy0,
    float dx1, float dy1,
    float dx2, float dy2,
    float dx3, float dy3 )
  {
    if ( this.ExistTexture( rnum ) == false )
    {
      return false;
    }

    ttex = this.GetTexture( rnum );

    this.SetFloatArray8( dx0, dy0, dx1, dy1, dx2, dy2, dx3, dy3 );
    this.SetVertex( ttex, farr8 );

    this.SetFloatArray8(
      rx     / ttex.width, ry     / ttex.height,
      rx + w / ttex.width, ry     / ttex.height,
      rx     / ttex.width, ry + h / ttex.height,
      rx + w / ttex.width, ry + h / ttex.height );
    this.SetUV( ttex, farr8 );

    boolean ret = this.DrawTexture( ttex );

    return ret;
  }

  public boolean CreateFont( int fnum, int size, float red, float green, float blue, float alpha)
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
    tpaint.setARGB( (int)(0xff * alpha), (int)(0xff * red), (int)(0xff * green), (int)(0xff * blue) );

    return true;
  }

  public boolean Printf( int fnum, float dx, float dy, String str )
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

    canvas.drawText( str, 0, 30, tpaint );

    //gl.glEnable( GL10.GL_TEXTURE_2D );//
    gl.glBindTexture(GL10.GL_TEXTURE_2D, stringnum );
    //GLUtils.texSubImage2D( GL10.GL_TEXTURE_2D, 0, 0, 0, stringbmp );//
    //gl.glDisable( GL10.GL_TEXTURE_2D );//
    GLUtils.texImage2D( GL10.GL_TEXTURE_2D, 0, stringbmp, 0 );
    //gl.glGenTextures( 1, ttex.texid, 0 );

    //gl.glTexParameterf( GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR );
    //gl.glBindTexture( GL10.GL_TEXTURE_2D, 0 );

    this.SetFloatArray8( dx, dy, dx + stringtex.width, dy, dx, dy + stringtex.height, dx + stringtex.width, dy + stringtex.height );
    stringtex.ver = CreateFloatBuffer( farr8 );

    this.SetFloatArray8( 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f );
    stringtex.uv   = CreateFloatBuffer( farr8 );

    return this.DrawTexture( stringtex );
  }

  private void SetFloatArray4( float f0, float f1, float f2, float f3 )
  {
    farr4[ 0 ] = f0; farr4[ 1 ] = f1;
    farr4[ 2 ] = f2; farr4[ 3 ] = f3;
  }

  private void SetFloatArray8( float f0, float f1, float f2, float f3, float f4, float f5, float f6, float f7 )
  {
    farr8[ 0 ] = f0; farr8[ 1 ] = f1;
    farr8[ 2 ] = f2; farr8[ 3 ] = f3;
    farr8[ 4 ] = f4; farr8[ 5 ] = f5;
    farr8[ 6 ] = f6; farr8[ 7 ] = f7;
  }

  public static FloatBuffer CreateColor( float[] color )
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
      return CreateFloatBuffer( col );
    } else if ( color.length >= 4 )
    {
      float[] col =
      {
        color[ 0 ], color[ 1 ], color[ 2 ], color[ 3 ],
        color[ 0 ], color[ 1 ], color[ 2 ], color[ 3 ],
        color[ 0 ], color[ 1 ], color[ 2 ], color[ 3 ],
        color[ 0 ], color[ 1 ], color[ 2 ], color[ 3 ],
      };
      return CreateFloatBuffer( col );
    } else if ( color.length > 0 )
    {
      float[] col =
      {
        color[ 0 ], color[ 0 ], color[ 0 ], color[ 0 ],
        color[ 0 ], color[ 0 ], color[ 0 ], color[ 0 ],
        color[ 0 ], color[ 0 ], color[ 0 ], color[ 0 ],
        color[ 0 ], color[ 0 ], color[ 0 ], color[ 0 ],
      };
      return CreateFloatBuffer( col );
    }
    float[] col =
    {
      1.0f, 1.0f, 1.0f, 1.0f,
      1.0f, 1.0f, 1.0f, 1.0f,
      1.0f, 1.0f, 1.0f, 1.0f,
      1.0f, 1.0f, 1.0f, 1.0f,
    };
    return CreateFloatBuffer( col );
  }

  public boolean SetUV( Texture tex, float[] uv )
  {
    tex.uv  = CreateFloatBuffer( uv );
    return true;
  }

  public boolean SetVertex( Texture tex, float[] vert )
  {
    tex.ver = CreateFloatBuffer( vert );
    return true;
  }

  public void SetColor( int rnum, float col )
  {
    float[] color =
    {
      col, col, col, col,
      col, col, col, col,
      col, col, col, col,
      col, col, col, col,
    };
    this.SetColor( rnum, color );
  }

  public void SetColor( int rnum )
  {
    float[] color =
    {
      1.0f, 1.0f, 1.0f, 1.0f,
      1.0f, 1.0f, 1.0f, 1.0f,
      1.0f, 1.0f, 1.0f, 1.0f,
      1.0f, 1.0f, 1.0f, 1.0f,
    };
    this.SetColor( rnum, color );
  }

  public void SetColor( int rnum, float[] color )
  {
    Texture tex;

    if ( this.ExistTexture( rnum ) == false )
    {
      return;
    }

    tex = textures.get( rnum );
    tex.col  = CreateFloatBuffer( color );
  }

  public float[] Color( float red, float green, float blue, float alpha )
  {
    float[] color ={ red, blue, green, alpha, };
    return color;
  }
 
  public int GetWidth(){ return width; }
  public int GetHeight(){ return height; }
  public Texture GetTexture( int rnum ){ return textures.get( rnum ); }

  // Support OpenGL.
  public static Bitmap ResizeBitmap( Bitmap bmp, int length )
  {
    Bitmap nbmp = Bitmap.createBitmap( length, length, Bitmap.Config.ARGB_8888);

    Canvas cv = new Canvas( nbmp );

    cv.drawBitmap( bmp, 0, 0, null );

    return nbmp;
  }

  public static FloatBuffer CreateFloatBuffer( float[] arr )
  {
    ByteBuffer bb = ByteBuffer.allocateDirect( arr.length * 4 );
    bb.order( ByteOrder.nativeOrder() );
    FloatBuffer fb = bb.asFloatBuffer();
    fb.put( arr );
    fb.position( 0 );
    return fb;
  }

  public static boolean IsPowerOf2( int val ){ return val > 0 && ( val & (val - 1) ) == 0; }

  public static int ConvertPowerOf2( int val )
  {
    if ( val < 0 ){ return 0; }
    int ret = 1;
    for( ; val > ret ; ret <<= 1 );
    return ret;
  }

}

class AMatrix extends Matrix
{
  static public void IdentityMatrix( float[] mat )
  {
    mat[ 1 ] = mat[ 2 ] = mat[ 3 ] = mat[ 4 ] = mat[ 6 ] = mat[ 7 ] = mat[ 8 ] = mat[ 9 ] = mat[ 11 ] = mat[ 12 ] = mat[ 13 ] = mat[ 14 ] = 0.0f;
    mat[ 0 ] = mat[ 5 ] = mat[ 10 ] = mat[ 15 ] = 1.0f;
  }
}
