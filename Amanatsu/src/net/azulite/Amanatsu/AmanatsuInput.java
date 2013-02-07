package net.azulite.Amanatsu;

import android.view.KeyEvent;
import android.view.MotionEvent;

public interface AmanatsuInput
{
  public boolean Touch( MotionEvent event );

  public boolean Update();

  public float GetX();
  public float GetY();

  public int Size();

  public float GetX( int num );
  public float GetY( int num );
  public int GetTouchFrame( int num );

  public float GetFingerX( int fingerid );
  public float GetFingerY( int fingerid );
  public int GetFingerTouchFrame( int fingerid );

  public boolean KeyDown( int keycode, KeyEvent event );
  public boolean KeyUp( int keycode, KeyEvent event );
}
