package net.azulite.Amanatsu;

import android.view.KeyEvent;
import android.view.MotionEvent;

public interface AmanatsuInput
{
  public boolean setWindowSize( float width, float height );
  public boolean setInputArea( float x, float y, float width, float height );
  public boolean update();

  public boolean touch( MotionEvent event );

  public float getX();
  public float getY();
  public int getTouchFrame();

  public int size();

  public float getX( int num );
  public float getY( int num );
  public int getTouchFrame( int num );

  public float getFingerX( int fingerid );
  public float getFingerY( int fingerid );
  public int getFingerTouchFrame( int fingerid );

  public boolean keyDown( int keycode, KeyEvent event );
  public boolean keyUp( int keycode, KeyEvent event );

  public int getKey( int keycode );
  public int getLastKey();
}
