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
import android.graphics.BitmapFactory;
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

  // tmp.
  private float[] farr4, farr8, mat;
  private Texture ttex;

  public AmanatsuDraw( Context context )
  {
    resource = context.getResources();
    farr4 = new float[ 4 ];
    farr8 = new float[ 8 ];
    mat = new float[ 16 ];

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

  public int CreateTexture( int rnum )
  {
    return this.CreateTextureFromBitmap( rnum, BitmapFactory.decodeResource( resource, rnum ) );
  }

  public int CreateTexture( int rnum, Bitmap bmp )
  {
    return this.CreateTextureFromBitmap( rnum, bmp );
  }

  private int CreateTextureFromBitmap( int rnum, Bitmap bmp )
  {
    if ( bmp == null )
    {
      return -1;
    }

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

    gl.glGenTextures( 1, ttex.texid, 0 );
    gl.glBindTexture( GL10.GL_TEXTURE_2D, ttex.texid[ 0 ] );
    GLUtils.texImage2D( GL10.GL_TEXTURE_2D, 0, bmp, 0 );
    gl.glTexParameterf( GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR );
    gl.glTexParameterf( GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR );
    gl.glBindTexture( GL10.GL_TEXTURE_2D, 0 );

    ttex.width = bmp.getWidth();
    ttex.height = bmp.getHeight();
    this.SetColor( rnum );

    bmp.recycle();

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
      rx     / ttex.width, ry + h / ttex.height,
      rx + w / ttex.width, ry + h / ttex.height );
    this.SetUV( ttex, farr8 );

    boolean ret = this.DrawTexture( ttex );

    return ret;
  }

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

  // Support OpenGL.
  public static FloatBuffer CreateFloatBuffer( float[] arr )
  {
    ByteBuffer bb = ByteBuffer.allocateDirect( arr.length * 4 );
    bb.order( ByteOrder.nativeOrder() );
    FloatBuffer fb = bb.asFloatBuffer();
    fb.put( arr );
    fb.position( 0 );
    return fb;
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
  
}

class AMatrix extends Matrix
{
  static public void IdentityMatrix( float[] mat )
  {
    mat[ 1 ] = mat[ 2 ] = mat[ 3 ] = mat[ 4 ] = mat[ 6 ] = mat[ 7 ] = mat[ 8 ] = mat[ 9 ] = mat[ 11 ] = mat[ 12 ] = mat[ 13 ] = mat[ 14 ] = 0.0f;
    mat[ 0 ] = mat[ 5 ] = mat[ 10 ] = mat[ 15 ] = 1.0f;
  }
}
