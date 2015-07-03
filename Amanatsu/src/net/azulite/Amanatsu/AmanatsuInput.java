package net.azulite.Amanatsu;

import android.view.KeyEvent;
import android.view.MotionEvent;
/**
 * Amanatsuの入力。
 */
public interface AmanatsuInput
{
	public void term();
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
	public int fingerNum();

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

	/**
	 * 磁気センサーが利用可能かどうか調べる。
	 */
	public boolean canUseMagnetic();

	/**
	 * X軸方向の加速度を返す。
	 */
	public float getAcceleX();

	/**
	 * Y軸方向の加速度を返す。
	 */
	public float getAcceleY();

	/**
	 * Z軸方向の加速度を返す。
	 */
	public float getAcceleZ();

	/**
	 * X軸方向の磁気を返す。
	 */
	public float getMagneticX();

	/**
	 * Y軸方向の磁気を返す。
	 */
	public float getMagneticY();

	/**
	 * Z軸方向の磁気を返す。
	 */
	public float getMagneticZ();

	/**
	 * 方位角を返す。
	 * @return 北が0。東がπ/2。南がπ。西が-π/2。
	 */
	public float getAzimuth();

	/**
	 * 傾斜角を返す。
	 * @return 水平な台に端末を置いた状態を基準に、手前に立ち上げると負の値。奥に倒すと正の値。-π～π。
	 */
	public float getPitch();

	/**
	 * 回転角を返す。
	 * @return 水平な台に端末を置いた状態を基準に、右に傾けると正の値。左に傾けると負の値。-π～π。
	 */
	public float getRoll();
}

