package net.azulite.Amanatsu;

import android.opengl.Matrix;


public class AMatrix extends Matrix
{
  public static void identityMatrix( float[] mat )
  {
    mat[ 1 ] = mat[ 2 ] = mat[ 3 ] = mat[ 4 ] = mat[ 6 ] = mat[ 7 ] = mat[ 8 ] = mat[ 9 ] = mat[ 11 ] = mat[ 12 ] = mat[ 13 ] = mat[ 14 ] = 0.0f;
    mat[ 0 ] = mat[ 5 ] = mat[ 10 ] = mat[ 15 ] = 1.0f;
  }
}
