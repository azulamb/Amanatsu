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

  public void restore()
  {
    om.setStreamVolume( AudioManager.STREAM_MUSIC, srcvolume, 0 );
  }

  // System

  public boolean setVolume( int volume )
  {
    if ( volume > om.getStreamMaxVolume( AudioManager.STREAM_MUSIC ) ){ volume = om.getStreamMaxVolume( AudioManager.STREAM_MUSIC ); }
    bvolume = volume;
    svolume = (float)bvolume / (float)om.getStreamMaxVolume( AudioManager.STREAM_MUSIC );
    om.setVibrateSetting( AudioManager.STREAM_MUSIC, bvolume );
    return true;
  }

  public int GetVolume(){ return bvolume; }

  public boolean isMannerMode()
  {
    return ( om.getRingerMode() != AudioManager.RINGER_MODE_NORMAL );
  }
  // BGM

  public boolean loadBgm( int rnum, boolean loop )
  {
    return loadBgm( rnum, Uri.parse("android.resource://" + ama.getContext().getPackageName() +"/" + rnum ), loop );
  }

  public boolean loadBgm( int snum, String file, boolean loop )
  {
    return loadBgm( snum, Uri.parse( "file:///android_asset/" + file ), loop );
  }

  public boolean loadBgm( int rnum, Uri uri, boolean loop )
  {
    MediaPlayer mp = new MediaPlayer();
    bgm.put( rnum, mp );
    try
    {
      mp.setDataSource( ama.getContext(), Uri.parse("android.resource://" + ama.getContext().getPackageName() +"/" + rnum ) );
      mp.setAudioStreamType( AudioManager.STREAM_MUSIC );//STREAM_NOTIFICATION
      mp.setLooping( loop );
      mp.prepare();

      return true;
    } catch (IllegalStateException e)
    {
    } catch (IOException e)
    {
    }

    bgm.remove( rnum );

    return false;
  }

  public boolean unloadBgm( int rnum )
  {
    if ( bgm.containsKey( rnum ) )
    {
      bgm.remove( rnum );
      return true;
    }
    return false;
  }

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

  public boolean playBgm( int rnum )
  {
    if ( bgm.containsKey( rnum ) == false ){ return false; }

    MediaPlayer mp = bgm.get( rnum );

    mp.seekTo( 0 );
    mp.setVolume( svolume, svolume );
    mp.start();

    return true;
  }

  public boolean stopBgm( int rnum )
  {
    boolean ret = pauseBgm( rnum );
    if ( ret )
    {
      bgm.get( rnum ).seekTo( 0 );
    }
    return ret;
  }

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

  public boolean resumeBgm( int rnum )
  {
    if ( bgm.containsKey( rnum ) == false ){ return false; }

    MediaPlayer mp = bgm.get( rnum );

    mp.start();

    return true;
  }

  public boolean pauseBgm( int rnum )
  {
    if ( bgm.containsKey( rnum ) == false ){ return false; }

    MediaPlayer mp = bgm.get( rnum );
    mp.pause();

    return true;
  }

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

  public boolean loadSe( int rnum )
  {
    int id;

    id = sp.load( ama.getContext(), rnum, 1 );

    se.put( rnum, id );

    return true;
  }

  public boolean unloadSe( int rnum )
  {
    if ( se.containsKey( rnum ) == false ){ return false; }

    sp.unload( se.get( rnum ) );
    se.remove( rnum );

    return true;
  }

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

  public boolean playSe( int rnum ){ return playSe( rnum, false, 1.0f, 1.0f, 1.0f ); }

  public boolean playSe( int rnum, boolean loop ){ return playSe( rnum, loop, 1.0f, svolume, svolume ); }

  public boolean playSe( int rnum, boolean loop, float volume ){ return playSe( rnum, loop, 1.0f, volume, volume ); }

  public boolean playSe( int rnum, boolean loop, float late, float pan_l, float pan_r )
  {
    if ( se.containsKey( rnum ) == false ){ return false; }

    sp.play( se.get( rnum ), pan_l, pan_r, 0, loop ? -1 : 0, late );

    return true;
  }

  public boolean stopSe( int rnum )
  {
    if ( se.containsKey( rnum ) == false ){ return false;}
    sp.stop( se.get( rnum ) );
    return true;
  }

  public boolean stopSe()
  {
    return true;
  }

  public boolean resumeSe( int rnum )
  {
    if ( se.containsKey( rnum ) == false ){ return false;}
    sp.resume( se.get( rnum ) );
    return true;
  }

  public boolean pauseSe( int rnum )
  {
    if ( se.containsKey( rnum ) == false ){ return false;}
    sp.pause( se.get( rnum ) );
    return true;
  }
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
