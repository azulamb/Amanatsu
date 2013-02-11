package net.azulite.Amanatsu;

public class GameColor
{
  float[] color;

  public static final float[] BLACK = new float[]{ 0.0f, 0.0f, 0.0f, 1.0f };
  public static final float[] WHITE = new float[]{ 1.0f, 1.0f, 1.0f, 1.0f };

  public GameColor( float red, float green, float blue, float alpha )
  {
    color = new float[ 4 ];
    color[ 0 ] = red;
    color[ 1 ] = green;
    color[ 2 ] = blue;
    color[ 3 ] = alpha;
  }

  public GameColor( byte red, byte green, byte blue, byte alpha )
  {
    color = new float[ 4 ];
    color[ 0 ] = (float)( red & 0xff ) / 255.0f;
    color[ 1 ] = (float)( green & 0xff ) / 255.0f;
    color[ 2 ] = (float)( blue & 0xff ) / 255.0f;
    color[ 3 ] = (float)( alpha & 0xff ) / 255.0f;
  }

  static float[] CreateColor( byte red, byte green, byte blue, byte alpha )
  {
    float[] color = new float[ 4 ];
    color[ 0 ] = (float)( red & 0xff ) / 255.0f;
    color[ 1 ] = (float)( green & 0xff ) / 255.0f;
    color[ 2 ] = (float)( blue & 0xff ) / 255.0f;
    color[ 3 ] = (float)( alpha & 0xff ) / 255.0f;
    return color;
  }

  static float[] CreateColor( float red, float green, float blue, float alpha )
  {
    float[] color = new float[ 4 ];
    color[ 0 ] = red;
    color[ 1 ] = green;
    color[ 2 ] = blue;
    color[ 3 ] = alpha;
    return color;
  }
}
