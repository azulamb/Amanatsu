package net.azulite.Amanatsu;

import android.view.KeyEvent;
import android.view.MotionEvent;

public interface AmanatsuInput
{
  public boolean setWindowSize( float width, float height );
  /**
   * 入力の値域を決める。
   * 画面サイズから割合を計算し、入力エリア内に収まる座標系に変換する。
   * @param x 入力開始X座標。
   * @param y 入力開始Y座標。
   * @param width 横幅。
   * @param height 高さ。
   */
  public boolean setInputArea( float x, float y, float width, float height );

  public boolean update();

  public boolean touch( MotionEvent event );

  /**
   * タッチX座標の取得。
   */
  public float getX();

  /**
   * タッチY座標の取得。
   */
  public float getY();

  /**
   * タッチフレームの取得。
   */
  public int getTouchFrame();

  /**
   * 指の数。
   */
  public int fingernum();

  /**
   * タッチX座標の取得。
   * @param num 指番号。
   */
  public float getX( int num );

  /**
   * タッチY座標の取得。
   * @param num 指番号。
   */
  public float getY( int num );

  /**
   * タッチフレームの取得。
   * @param num 指番号。
   */
  public int getTouchFrame( int num );

  /**
   * 指IDの取得。
   * @param num 指番号。
   */
  public int getFingerId( int num );

  /**
   * タッチX座標の取得。
   * @param fingerid 指ID。
   */
  public float getFingerX( int fingerid );

  /**
   * タッチY座標の取得。
   * @param fingerid 指ID。
   */
  public float getFingerY( int fingerid );

  /**
   * タッチフレームの取得。
   * @param fingerid 指ID。
   */
  public int getFingerTouchFrame( int fingerid );

  public boolean keyDown( int keycode, KeyEvent event );
  public boolean keyUp( int keycode, KeyEvent event );

  /**
   * キーの押されているフレーム数を返す。
   * @param keycode キー番号。
   * @return 正の値 押されているフレーム数。
   * @return 0 押されていない。
   * @return -1 離れた次のフレーム。
   */
  public int getKey( int keycode );

  /**
   * 最後に押されたキー番号を返す。
   */
  public int getLastKey();
}
