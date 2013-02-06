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

// Library.

public class OpenGLDraw
{
  static GL10 gl;
  int width, height;
  Resources resource;
  static Map<Integer, Texture> textures = new Hashtable< Integer, Texture >();

  public OpenGLDraw( Context context )
  {
    resource = context.getResources();
    gl = null;
  }

  public void SetGL( GL10 gl )
  {
    OpenGLDraw.gl = gl;
  }

  public void ClearScreen()
  {
    ClearScreen( 0.0f, 0.0f, 0.0f );
  }

  public void ClearScreen( float red, float green, float blue)
  {
    gl.glClearColor( red, green, blue, 1.0f );
    gl.glClear( GL10.GL_COLOR_BUFFER_BIT );
  }

  public void DrawBox( int x, int y, int w, int h, float[] color )
  {
    float[] vert =
      {
        (float)x       , (float)y,
        (float)(x + w) , (float)y,
        (float)x       , (float)(y + h),
        (float)(x + w) , (float)(y + h),
      };
    FloatBuffer pvert = CreateFloatBuffer( vert );

    FloatBuffer pcol = CreateColor( color );

    // í∏ì_èÓïÒÇìoò^ÅB
    gl.glVertexPointer( 2, GL10.GL_FLOAT, 0, pvert );
    gl.glEnableClientState( GL10.GL_VERTEX_ARRAY );

    // í∏ì_Ç…ëŒÇ∑ÇÈêFèÓïÒÇìoò^ÅB
    gl.glColorPointer( 4, GL10.GL_FLOAT, 0, pcol );
    gl.glEnableClientState( GL10.GL_COLOR_ARRAY );

    gl.glDrawArrays( GL10.GL_TRIANGLE_STRIP, 0, 4 );
  }

  public int CreateTexture( int rnum )
  {
    Bitmap bmp;

    bmp = BitmapFactory.decodeResource( resource, rnum );
    
    return this.CreateTextureFromBitmap( rnum, bmp );
  }

  public int CreateTexture( int rnum, Bitmap bmp)
  {
    return CreateTextureFromBitmap( rnum, bmp );
  }

  private int CreateTextureFromBitmap( int rnum, Bitmap bmp )
  {
    Texture tex;

    if ( bmp == null )
    {
      return -1;
    }

    if ( textures.containsKey( rnum ) )
    {
      ReleaseTexture( rnum );
      tex = textures.get( rnum );
    } else
    {
      tex = new Texture();
      tex.texid = new int [ 1 ];
      textures.put( rnum, tex );
    }

    gl.glGenTextures( 1, tex.texid, 0 );
    gl.glBindTexture( GL10.GL_TEXTURE_2D, tex.texid[ 0 ] );
    GLUtils.texImage2D( GL10.GL_TEXTURE_2D, 0, bmp, 0 );
    gl.glTexParameterf( GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR );
    gl.glTexParameterf( GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR );
    gl.glBindTexture( GL10.GL_TEXTURE_2D, 0 );
    
    tex.width = bmp.getWidth();
    tex.height = bmp.getHeight();

    bmp.recycle();

    return tex.texid[ 0 ];
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
      //textures.get( rnum ).Release();
      //int [] texid = new int[ 1 ];
      //texid[ 0 ] = textures.get( rnum );
      gl.glDeleteTextures( 1, textures.get( rnum ).texid, 0 );
      //textures.remove( rnum );
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

  public final void DrawTexture( int rnum, int dx, int dy, int sx, int sy, int w, int h )
  {
    Texture tex;

    if ( this.ExistTexture( rnum ) == false )
    {
      return;
    }

    tex = textures.get( rnum );

    float[] vert =
    {
      (float)dx       , (float)dy,
      (float)(dx + w) , (float)dy,
      (float)dx       , (float)(dy + h),
      (float)(dx + w) , (float)(dy + h),
    };
    
    //int tnum = textures.get( rnum );
    
    float[] col =
    {
      1.0f, 1.0f, 1.0f, 1.0f,
      1.0f, 1.0f, 1.0f, 1.0f,
      1.0f, 1.0f, 1.0f, 1.0f,
      1.0f, 1.0f, 1.0f, 1.0f,
    };

    float[] tsize = new float[ 2 ];
    tsize[ 0 ] = tex.width;//texwidth.get( rnum );
    tsize[ 1 ] = tex.height;//textures.get( rnum ).GetHeight();//texheight.get( rnum );

    float[] uv =
    {
      (float)( sx     ) / tsize[ 0 ], (float)( sy     ) / tsize[ 1 ],
      (float)( sx + w ) / tsize[ 0 ], (float)( sy     ) / tsize[ 1 ],
      (float)( sx     ) / tsize[ 0 ], (float)( sy + h ) / tsize[ 1 ],
      (float)( sx + w ) / tsize[ 0 ], (float)( sy + h ) / tsize[ 1 ],
    };
    
    FloatBuffer fb_vert = CreateFloatBuffer( vert );
    FloatBuffer fb_col  = CreateFloatBuffer( col );
    FloatBuffer fb_uv   = CreateFloatBuffer( uv );
    
    gl.glEnable( GL10.GL_TEXTURE_2D );
    
    //gl.glBindTexture( GL10.GL_TEXTURE_2D, tnum );
    gl.glBindTexture( GL10.GL_TEXTURE_2D, tex.texid[ 0 ] );
    
    gl.glVertexPointer( 2, GL10.GL_FLOAT, 0, fb_vert );
    gl.glEnableClientState( GL10.GL_VERTEX_ARRAY );
    
    gl.glColorPointer( 4, GL10.GL_FLOAT, 0, fb_col );
    gl.glEnableClientState( GL10.GL_COLOR_ARRAY );
    
    gl.glTexCoordPointer( 2, GL10.GL_FLOAT, 0, fb_uv );
    gl.glEnableClientState( GL10.GL_TEXTURE_COORD_ARRAY );
    
    gl.glDrawArrays( GL10.GL_TRIANGLE_STRIP, 0, 4 );
    
    gl.glDisableClientState( GL10.GL_TEXTURE_COORD_ARRAY );
    
    gl.glDisable( GL10.GL_TEXTURE_2D );

  }
  
  public final void DrawTextureScaring( int rnum, int dx, int dy, int sx, int sy, int w, int h, int dw, int dh )
  {
    Texture tex;

    if ( this.ExistTexture( rnum ) == false )
    {
      return;
    }

    tex = textures.get( rnum );

    float[] vert =
    {
      (float)dx        , (float)dy,
      (float)(dx + dw) , (float)dy,
      (float)dx        , (float)(dy + dh),
      (float)(dx + dw) , (float)(dy + dh),
    };

    //int tnum = textures.get( rnum );

    float[] col =
    {
      1.0f, 1.0f, 1.0f, 1.0f,
      1.0f, 1.0f, 1.0f, 1.0f,
      1.0f, 1.0f, 1.0f, 1.0f,
      1.0f, 1.0f, 1.0f, 1.0f,
    };

    float[] tsize = new float[ 2 ];
    tsize[ 0 ] = tex.width;//textures.get( rnum ).GetWidth();//texwidth.get( rnum );
    tsize[ 1 ] = tex.height;//textures.get( rnum ).GetHeight();//texheight.get( rnum );

    float[] uv =
    {
      (float)( sx     ) / tsize[ 0 ], (float)( sy     ) / tsize[ 1 ],
      (float)( sx + w ) / tsize[ 0 ], (float)( sy     ) / tsize[ 1 ],
      (float)( sx     ) / tsize[ 0 ], (float)( sy + h ) / tsize[ 1 ],
      (float)( sx + w ) / tsize[ 0 ], (float)( sy + h ) / tsize[ 1 ],
    };

    FloatBuffer fb_vert = CreateFloatBuffer( vert );
    FloatBuffer fb_col  = CreateFloatBuffer( col );
    FloatBuffer fb_uv   = CreateFloatBuffer( uv );

    gl.glEnable( GL10.GL_TEXTURE_2D );

    gl.glBindTexture( GL10.GL_TEXTURE_2D, tex.texid[ 0 ] );//textures.get( rnum ).GetTextureNum() );//tnum );

    gl.glVertexPointer( 2, GL10.GL_FLOAT, 0, fb_vert );
    gl.glEnableClientState( GL10.GL_VERTEX_ARRAY );

    gl.glColorPointer( 4, GL10.GL_FLOAT, 0, fb_col );
    gl.glEnableClientState( GL10.GL_COLOR_ARRAY );

    gl.glTexCoordPointer( 2, GL10.GL_FLOAT, 0, fb_uv );
    gl.glEnableClientState( GL10.GL_TEXTURE_COORD_ARRAY );
    
    gl.glDrawArrays( GL10.GL_TRIANGLE_STRIP, 0, 4 );
    
    gl.glDisableClientState( GL10.GL_TEXTURE_COORD_ARRAY );
    
    gl.glDisable( GL10.GL_TEXTURE_2D );

  }

  // Support OpenGL.
  public static final FloatBuffer CreateFloatBuffer( float[] arr )
  {
    ByteBuffer bb = ByteBuffer.allocateDirect( arr.length * 4 );
    bb.order( ByteOrder.nativeOrder() );
    FloatBuffer fb = bb.asFloatBuffer();
    fb.put( arr );
    fb.position( 0 );
    return fb;
  }
  public static FloatBuffer CreateColor( float[] color )
  {
    if ( color.length / 4 >= 4 )
    {
      float[] col =
      {
        color[ 0 ], color[ 1 ], color[ 2 ], color[ 3 ],
        color[ 4 ], color[ 5 ], color[ 6 ], color[ 7 ],
        color[ 8 ], color[ 9 ], color[ 10 ], color[ 11 ],
        color[ 12 ], color[ 13 ], color[ 14 ], color[ 15 ],
      };
      return CreateFloatBuffer( col );
    }
    float[] col =
    {
      color[ 0 ], color[ 1 ], color[ 2 ], color[ 3 ],
      color[ 0 ], color[ 1 ], color[ 2 ], color[ 3 ],
      color[ 0 ], color[ 1 ], color[ 2 ], color[ 3 ],
      color[ 0 ], color[ 1 ], color[ 2 ], color[ 3 ],
    };
    return CreateFloatBuffer( col );
  }
  public float[] Color( float red, float green, float blue, float alpha )
  {
    float[] color ={ red, blue, green, alpha, };
   
    return color;
  }
 
  public int GetWidth(){ return width; }
  public int GetHeight(){ return height; }
  public Texture GetTexture( int rnum ){ return textures.get( rnum ); }
  public float GetTextureWidth( int rnum ){ return textures.get( rnum ) != null ? textures.get( rnum ).width : 0.0f; }


  class Texture
  {
    int rnum = -1, width = 0, height = 0;
    int [] texid = null;
  }
}



