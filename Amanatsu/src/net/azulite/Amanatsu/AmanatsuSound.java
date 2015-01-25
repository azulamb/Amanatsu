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

/**
 * Amanatsuの音を管理するクラス。
 */
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
   * 音声環境をゲーム起動前に戻す。
   */
  public void restore()
  {
    om.setStreamVolume( AudioManager.STREAM_MUSIC, srcvolume, 0 );
  }

  // System

  /**
   * 音量の設定。
   * @param volume 音量。最大値はgetMaxVolume()で取得可能。
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
   * 音楽再生デバイスの最大音量を取得する。
   */
  public int getMaxVolume(){ return om.getStreamMaxVolume( AudioManager.STREAM_MUSIC ); }

  /**
   * 再生音量を取得する。
   */
  public int getVolume(){ return bvolume; }
  /**
   * 現在の再生音量を調べて取得する。
   */
  public int getNowVolume()
  {
    bvolume = om.getStreamVolume( AudioManager.STREAM_MUSIC );
    svolume = (float)bvolume / (float)om.getStreamMaxVolume( AudioManager.STREAM_MUSIC );
    return bvolume;
  }

  /**
   * マナーモードかどうか取得する。
   * @return true マナーモード。
   * @return false マナーモードではない。
   */
  public boolean isMannerMode()
  {
    return ( om.getRingerMode() != AudioManager.RINGER_MODE_NORMAL );
  }

  // BGM

  /**
   * BGMを読み込む(リソース)。
   * 音番号はリソース番号になる。
   * @param rnum リソース番号。
   * @param loop ループ再生フラグ(true=ループ再生, false=ループ再生しない)。
   */
  public boolean loadBgm( int rnum, boolean loop )
  {
    return loadBgm( rnum, Uri.parse( "android.resource://" + ama.getContext().getPackageName() +"/" + rnum ), loop );
  }

  /**
   * BGMを読み込む(assets)。
   * assetsに置かれたファイルを読み込む。
   * @param snum 音番号。
   * @param file assetsをrootとした時のファイルパス。
   * @param loop ループ再生フラグ(true=ループ再生, false=ループ再生しない)。
   */
  public boolean loadBgm( int snum, String file, boolean loop )
  {
    return loadBgm( snum, Uri.parse( "file:///android_asset/" + file ), loop );
  }

  /**
   * BGMを読み込む。
   * @param snum 音番号。
   * @param uri ファイルのUri。
   * @param loop ループ再生フラグ(true=ループ再生, false=ループ再生しない)。
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
   * BGMを開放する。
   * @param snum 音番号。
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
   * BGMを全て開放する。
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
   * BGMを再生する。
   * @param snum 音番号。
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
   * BGMの再生を止める。
   * @param snum 音番号。
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
   * すべてのBGMの再生を止める。
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
   * BGMを途中から再生開始する。
   * @param snum 音番号。
   */
  public boolean resumeBgm( int snum )
  {
    if ( bgm.containsKey( snum ) == false ){ return false; }

    MediaPlayer mp = bgm.get( snum );

    mp.start();

    return true;
  }

  /**
   * BGMを一時停止する。
   * @param snum 音番号。
   */
  public boolean pauseBgm( int snum )
  {
    if ( bgm.containsKey( snum ) == false ){ return false; }

    MediaPlayer mp = bgm.get( snum );
    mp.pause();

    return true;
  }

  /**
   * すべてのBGMを一時停止する。
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
  /**
   * BGMを再生しているか調べる。
   * @param snum 音番号。
   */
  public boolean isPlayBgm( int snum )
  {
    if ( bgm.containsKey( snum ) == false ){ return false; }

    return bgm.get( snum ).isPlaying();
  }
  // SE

  /**
   * SEを読み込む。
   * @param snum 音番号。
   */
  public boolean loadSe( int snum )
  {
    int id;

    id = sp.load( ama.getContext(), snum, 1 );

    se.put( snum, id );

    return true;
  }

  /**
   * SEを開放する。
   * @param snum 音番号。
   */
  public boolean unloadSe( int snum )
  {
    if ( se.containsKey( snum ) == false ){ return false; }

    sp.unload( se.get( snum ) );
    se.remove( snum );

    return true;
  }

  /**
   * すべてのSEを開放する。
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
   * SEを再生する。
   * @param snum 音番号。
   */
  public boolean playSe( int snum ){ return playSe( snum, false, 1.0f, 1.0f, 1.0f ); }

  /**
   * SEを再生する。
   * @param snum 音番号。
   * @param loop ループ再生フラグ(true=ループ再生, false=ループ再生しない)。
   */
  public boolean playSe( int snum, boolean loop ){ return playSe( snum, loop, 1.0f, svolume, svolume ); }

  /**
   * SEを再生する。
   * @param snum 音番号。
   * @param loop ループ再生フラグ(true=ループ再生, false=ループ再生しない)。
   * @param volume 音量(0.0f-1.0f)。
   */
  public boolean playSe( int snum, boolean loop, float volume ){ return playSe( snum, loop, 1.0f, volume, volume ); }

  /**
   * SEを再生する。
   * @param snum 音番号。
   * @param loop ループ再生フラグ(true=ループ再生, false=ループ再生しない)。
   * @param pan_l 左の音量(0.0f-1.0f)。
   * @param pan_r 右の音量(0.0f-1.0f)。
   */
  public boolean playSe( int snum, boolean loop, float late, float pan_l, float pan_r )
  {
    if ( se.containsKey( snum ) == false ){ return false; }

    sp.play( se.get( snum ), pan_l, pan_r, 0, loop ? -1 : 0, late );

    return true;
  }

  /**
   * SEを停止する。
   * @param snum 音番号。
   */
  public boolean stopSe( int snum )
  {
    if ( se.containsKey( snum ) == false ){ return false;}
    sp.stop( se.get( snum ) );
    return true;
  }

  /**
   * すべてのSEを停止する。
   */
  public boolean stopSe()
  {
    return true;
  }

  /**
   * SEの再生を再開する。
   * @param snum 音番号。
   */
  public boolean resumeSe( int snum )
  {
    if ( se.containsKey( snum ) == false ){ return false;}
    sp.resume( se.get( snum ) );
    return true;
  }

  /**
   * SEの再生を一時停止する。
   * @param snum 音番号。
   */
  public boolean pauseSe( int snum )
  {
    if ( se.containsKey( snum ) == false ){ return false;}
    sp.pause( se.get( snum ) );
    return true;
  }

  /**
   * すべてのSEの再生を一時停止する。
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
