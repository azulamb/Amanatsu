package net.azulite.Amanatsu;

import android.view.KeyEvent;
import android.view.MotionEvent;

public interface AmanatsuInput
{
  public boolean setWindowSize( float width, float height );
  /**
   * ���͂̒l������߂�B
   * ��ʃT�C�Y���犄�����v�Z���A���̓G���A���Ɏ��܂���W�n�ɕϊ�����B
   * @param x ���͊J�nX���W�B
   * @param y ���͊J�nY���W�B
   * @param width �����B
   * @param height �����B
   */
  public boolean setInputArea( float x, float y, float width, float height );

  public boolean update();

  public boolean touch( MotionEvent event );

  /**
   * �^�b�`X���W�̎擾�B
   */
  public float getX();

  /**
   * �^�b�`Y���W�̎擾�B
   */
  public float getY();

  /**
   * �^�b�`�t���[���̎擾�B
   */
  public int getTouchFrame();

  /**
   * �w�̐��B
   */
  public int fingernum();

  /**
   * �^�b�`X���W�̎擾�B
   * @param num �w�ԍ��B
   */
  public float getX( int num );

  /**
   * �^�b�`Y���W�̎擾�B
   * @param num �w�ԍ��B
   */
  public float getY( int num );

  /**
   * �^�b�`�t���[���̎擾�B
   * @param num �w�ԍ��B
   */
  public int getTouchFrame( int num );

  /**
   * �wID�̎擾�B
   * @param num �w�ԍ��B
   */
  public int getFingerId( int num );

  /**
   * �^�b�`X���W�̎擾�B
   * @param fingerid �wID�B
   */
  public float getFingerX( int fingerid );

  /**
   * �^�b�`Y���W�̎擾�B
   * @param fingerid �wID�B
   */
  public float getFingerY( int fingerid );

  /**
   * �^�b�`�t���[���̎擾�B
   * @param fingerid �wID�B
   */
  public int getFingerTouchFrame( int fingerid );

  public boolean keyDown( int keycode, KeyEvent event );
  public boolean keyUp( int keycode, KeyEvent event );

  /**
   * �L�[�̉�����Ă���t���[������Ԃ��B
   * @param keycode �L�[�ԍ��B
   * @return ���̒l ������Ă���t���[�����B
   * @return 0 ������Ă��Ȃ��B
   * @return -1 ���ꂽ���̃t���[���B
   */
  public int getKey( int keycode );

  /**
   * �Ō�ɉ����ꂽ�L�[�ԍ���Ԃ��B
   */
  public int getLastKey();
}
