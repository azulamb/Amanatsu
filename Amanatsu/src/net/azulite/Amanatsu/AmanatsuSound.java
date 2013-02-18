package net.azulite.Amanatsu;

import java.io.IOException;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.net.Uri;

// memo:AudioTrack
// TODO:
// * ogg loop flag

public class AmanatsuSound
{
  private Amanatsu ama;
  private AudioManager om;
  private static Map<Integer, MediaPlayer> bgm = new Hashtable<Integer, MediaPlayer>( 20 );
  private static Map<Integer, Integer> se = new Hashtable<Integer, Integer>( 20 );
  private SoundPool sp;
  private int srcvolume;
  private int bvolume;
  private float svolume;

  // tmp.

  public AmanatsuSound( Amanatsu ama )
  {
    this.ama = ama;
    om = (AudioManager) ama.getContext().getSystemService( Context.AUDIO_SERVICE );
    sp = new SoundPool( 3, AudioManager.STREAM_MUSIC, 0 );

    srcvolume = bvolume = om.getStreamVolume( AudioManager.STREAM_MUSIC );
    if ( isMannerMode() )
    {
      setVolume( 0 );
    }
    svolume = (float)bvolume / (float)om.getStreamMaxVolume( AudioManager.STREAM_MUSIC );
  }

  public void release()
  {
    restore();
  }

  /**
   * ���������Q�[���N���O�ɖ߂��B
   * @param 
   */
  public void restore()
  {
    om.setStreamVolume( AudioManager.STREAM_MUSIC, srcvolume, 0 );
  }

  // System

  /**
   * ���ʂ̐ݒ�B
   * @param volume ���ʁB�ő�l��getMaxVolume()�Ŏ擾�\�B
   */
  public boolean setVolume( int volume )
  {
    if ( volume > getMaxVolume() ){ volume = om.getStreamMaxVolume( AudioManager.STREAM_MUSIC ); }
    bvolume = volume;
    svolume = (float)bvolume / (float)om.getStreamMaxVolume( AudioManager.STREAM_MUSIC );
    om.setVibrateSetting( AudioManager.STREAM_MUSIC, bvolume );
    return true;
  }

  /**
   * ���y�Đ��f�o�C�X�̍ő剹�ʂ��擾����B
   */
  public int getMaxVolume(){ return om.getStreamMaxVolume( AudioManager.STREAM_MUSIC ); }

  /**
   * ���݂̍Đ����ʂ��擾����B
   */
  public int getVolume(){ return bvolume; }

  /**
   * �}�i�[���[�h���ǂ����擾����B
   * @return true �}�i�[���[�h�B
   * @return false �}�i�[���[�h�ł͂Ȃ��B
   */
  public boolean isMannerMode()
  {
    return ( om.getRingerMode() != AudioManager.RINGER_MODE_NORMAL );
  }

  // BGM

  /**
   * BGM��ǂݍ���(���\�[�X)�B
   * ���ԍ��̓��\�[�X�ԍ��ɂȂ�B
   * @param rnum ���\�[�X�ԍ��B
   * @param loop ���[�v�Đ��t���O(true=���[�v�Đ�, false=���[�v�Đ����Ȃ�)�B
   */
  public boolean loadBgm( int rnum, boolean loop )
  {
    return loadBgm( rnum, Uri.parse( "android.resource://" + ama.getContext().getPackageName() +"/" + rnum ), loop );
  }

  /**
   * BGM��ǂݍ���(assets)�B
   * assets�ɒu���ꂽ�t�@�C����ǂݍ��ށB
   * @param snum ���ԍ��B
   * @param file assets��root�Ƃ������̃t�@�C���p�X�B
   * @param loop ���[�v�Đ��t���O(true=���[�v�Đ�, false=���[�v�Đ����Ȃ�)�B
   */
  public boolean loadBgm( int snum, String file, boolean loop )
  {
    return loadBgm( snum, Uri.parse( "file:///android_asset/" + file ), loop );
  }

  /**
   * BGM��ǂݍ��ށB
   * @param snum ���ԍ��B
   * @param uri �t�@�C����Uri�B
   * @param loop ���[�v�Đ��t���O(true=���[�v�Đ�, false=���[�v�Đ����Ȃ�)�B
   */
  public boolean loadBgm( int snum, Uri uri, boolean loop )
  {
    MediaPlayer mp = new MediaPlayer();
    bgm.put( snum, mp );
    try
    {
      mp.setDataSource( ama.getContext(), uri );
      mp.setAudioStreamType( AudioManager.STREAM_MUSIC );
      mp.setLooping( loop );
      mp.prepare();

      return true;
    } catch (IllegalStateException e)
    {
    } catch (IOException e)
    {
    }

    bgm.remove( snum );

    return false;
  }

  /**
   * BGM���J������B
   * @param snum ���ԍ��B
   */
  public boolean unloadBgm( int snum )
  {
    if ( bgm.containsKey( snum ) )
    {
      bgm.remove( snum );
      return true;
    }
    return false;
  }

  /**
   * BGM��S�ĊJ������B
   * @param snum ���ԍ��B
   */
  public boolean unloadBgm()
  {
    Iterator< Map.Entry<Integer, MediaPlayer> > it;
    //Map.Entry<Integer, MediaPlayer> entry;

    for ( it = bgm.entrySet().iterator(); it.hasNext() ; )
    {
      it.next();
      it.remove();
    }

    return true;
  }

  /**
   * BGM���Đ�����B
   * @param snum ���ԍ��B
   */
  public boolean playBgm( int snum )
  {
    if ( bgm.containsKey( snum ) == false ){ return false; }

    MediaPlayer mp = bgm.get( snum );

    mp.seekTo( 0 );
    mp.setVolume( svolume, svolume );
    mp.start();

    return true;
  }

  /**
   * BGM�̍Đ����~�߂�B
   * @param snum ���ԍ��B
   */
  public boolean stopBgm( int snum )
  {
    boolean ret = pauseBgm( snum );
    if ( ret )
    {
      bgm.get( snum ).seekTo( 0 );
    }
    return ret;
  }

  /**
   * ���ׂĂ�BGM�̍Đ����~�߂�B
   */
  public boolean stopBgm()
  {
    Iterator< Map.Entry<Integer, MediaPlayer> > it;
    Map.Entry<Integer, MediaPlayer> entry;

    for ( it = bgm.entrySet().iterator(); it.hasNext() ; )
    {
      entry = it.next();
      entry.getValue().pause();
      entry.getValue().seekTo( 0 );
    }
    return true;
  }

  /**
   * BGM��r������Đ��J�n����B
   * @param snum ���ԍ��B
   */
  public boolean resumeBgm( int rnum )
  {
    if ( bgm.containsKey( rnum ) == false ){ return false; }

    MediaPlayer mp = bgm.get( rnum );

    mp.start();

    return true;
  }

  /**
   * BGM���ꎞ��~����B
   * @param snum ���ԍ��B
   */
  public boolean pauseBgm( int rnum )
  {
    if ( bgm.containsKey( rnum ) == false ){ return false; }

    MediaPlayer mp = bgm.get( rnum );
    mp.pause();

    return true;
  }

  /**
   * ���ׂĂ�BGM���ꎞ��~����B
   */
  public boolean pauseBgm()
  {
    Iterator< Map.Entry<Integer, MediaPlayer> > it;
    Map.Entry<Integer, MediaPlayer> entry;

    for ( it = bgm.entrySet().iterator(); it.hasNext() ; )
    {
      entry = it.next();
      entry.getValue().pause();
    }
    return true;
  }

  // SE

  /**
   * SE��ǂݍ��ށB
   * @param snum ���ԍ��B
   */
  public boolean loadSe( int snum )
  {
    int id;

    id = sp.load( ama.getContext(), snum, 1 );

    se.put( snum, id );

    return true;
  }

  /**
   * SE���J������B
   * @param snum ���ԍ��B
   */
  public boolean unloadSe( int snum )
  {
    if ( se.containsKey( snum ) == false ){ return false; }

    sp.unload( se.get( snum ) );
    se.remove( snum );

    return true;
  }

  /**
   * ���ׂĂ�SE���J������B
   * @param snum ���ԍ��B
   */
  public boolean unloadSe()
  {
    Iterator< Map.Entry<Integer, Integer> > it;
    Map.Entry<Integer, Integer> entry;

    for ( it = se.entrySet().iterator(); it.hasNext() ; )
    {
      entry = it.next();
      sp.unload( entry.getValue() );
      it.remove();
    }
    return true;
  }

  /**
   * SE���Đ�����B
   * @param snum ���ԍ��B
   */
  public boolean playSe( int snum ){ return playSe( snum, false, 1.0f, 1.0f, 1.0f ); }

  /**
   * SE���Đ�����B
   * @param snum ���ԍ��B
   * @param loop ���[�v�Đ��t���O(true=���[�v�Đ�, false=���[�v�Đ����Ȃ�)�B
   */
  public boolean playSe( int snum, boolean loop ){ return playSe( snum, loop, 1.0f, svolume, svolume ); }

  /**
   * SE���Đ�����B
   * @param snum ���ԍ��B
   * @param loop ���[�v�Đ��t���O(true=���[�v�Đ�, false=���[�v�Đ����Ȃ�)�B
   * @param volume ����(0.0f-1.0f)�B
   */
  public boolean playSe( int snum, boolean loop, float volume ){ return playSe( snum, loop, 1.0f, volume, volume ); }

  /**
   * SE���Đ�����B
   * @param snum ���ԍ��B
   * @param loop ���[�v�Đ��t���O(true=���[�v�Đ�, false=���[�v�Đ����Ȃ�)�B
   * @param pan_l ���̉���(0.0f-1.0f)�B
   * @param pan_r �E�̉���(0.0f-1.0f)�B
   */
  public boolean playSe( int snum, boolean loop, float late, float pan_l, float pan_r )
  {
    if ( se.containsKey( snum ) == false ){ return false; }

    sp.play( se.get( snum ), pan_l, pan_r, 0, loop ? -1 : 0, late );

    return true;
  }

  /**
   * SE���~����B
   * @param snum ���ԍ��B
   */
  public boolean stopSe( int snum )
  {
    if ( se.containsKey( snum ) == false ){ return false;}
    sp.stop( se.get( snum ) );
    return true;
  }

  /**
   * ���ׂĂ�SE���~����B
   */
  public boolean stopSe()
  {
    return true;
  }

  /**
   * SE�̍Đ����ĊJ����B
   * @param snum ���ԍ��B
   */
  public boolean resumeSe( int snum )
  {
    if ( se.containsKey( snum ) == false ){ return false;}
    sp.resume( se.get( snum ) );
    return true;
  }

  /**
   * SE�̍Đ����ꎞ��~����B
   * @param snum ���ԍ��B
   */
  public boolean pauseSe( int snum )
  {
    if ( se.containsKey( snum ) == false ){ return false;}
    sp.pause( se.get( snum ) );
    return true;
  }

  /**
   * ���ׂĂ�SE�̍Đ����ꎞ��~����B
   */
  public boolean pauseSe()
  {
    Iterator< Map.Entry<Integer, Integer> > it;
    Map.Entry<Integer, Integer> entry;

    for ( it = se.entrySet().iterator(); it.hasNext() ; )
    {
      entry = it.next();
      sp.pause( entry.getValue() );
    }
    return true;
  }

}
