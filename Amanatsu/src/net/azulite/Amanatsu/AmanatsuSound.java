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

public class AmanatsuSound
{
  Amanatsu ama;
  AudioManager om;
  static Map<Integer, MediaPlayer> bgm;
  static Map<Integer, Integer> se;
  SoundPool sp;
  int srcvolume;
  float volume;

  // tmp.

  public AmanatsuSound( Amanatsu ama )
  {
    this.ama = ama;
    om = (AudioManager) ama.context.getSystemService( Context.AUDIO_SERVICE );
    bgm = new Hashtable<Integer, MediaPlayer>();
    se = new Hashtable<Integer, Integer>();
    sp = new SoundPool( 3, AudioManager.STREAM_MUSIC, 0 );

    srcvolume = om.getStreamVolume( AudioManager.STREAM_MUSIC );
    volume = (float)om.getStreamVolume( AudioManager.STREAM_MUSIC ) / (float)om.getStreamMaxVolume( AudioManager.STREAM_MUSIC );
  }

  public void Release()
  {
    this.Restore();
  }

  public void Restore()
  {
    om.setStreamVolume( AudioManager.STREAM_MUSIC, srcvolume, 0 );
  }

  // System

  public boolean IsMannerMode()
  {
    if ( om.getRingerMode() == AudioManager.RINGER_MODE_NORMAL ){ return false; }
    return true;
  }
  // BGM

  public boolean LoadBGM( int rnum, boolean loop )
  {
    return this.LoadBGM( rnum, Uri.parse("android.resource://" + ama.context.getPackageName() +"/" + rnum ), loop );
  }

  public boolean LoadBGM( int rnum, Uri uri, boolean loop )
  {
    MediaPlayer mp = new MediaPlayer();
    bgm.put( rnum, mp );
    try
    {
      mp.setDataSource( ama.context, Uri.parse("android.resource://" + ama.context.getPackageName() +"/" + rnum ) );
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

  public boolean UnloadBGM( int rnum )
  {
    if ( bgm.containsKey( rnum ) )
    {
      bgm.remove( rnum );
      return true;
    }
    return false;
  }

  public boolean UnloadBGM()
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

  public boolean PlayBGM( int rnum )
  {
    if ( bgm.containsKey( rnum ) == false ){ return false; }

    MediaPlayer mp = bgm.get( rnum );

    mp.seekTo( 0 );
    mp.start();

    return true;
  }

  public boolean StopBGM( int rnum )
  {
    boolean ret = this.PauseBGM( rnum );
    if ( ret )
    {
      bgm.get( rnum ).seekTo( 0 );
    }
    return ret;
  }

  public boolean StopBGM()
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

  public boolean ResumeBGM( int rnum )
  {
    if ( bgm.containsKey( rnum ) == false ){ return false; }

    MediaPlayer mp = bgm.get( rnum );

    mp.start();

    return true;
  }

  public boolean PauseBGM( int rnum )
  {
    if ( bgm.containsKey( rnum ) == false ){ return false; }

    MediaPlayer mp = bgm.get( rnum );
    mp.pause();

    return true;
  }

  public boolean PauseBGM()
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

  public boolean LoadSE( int rnum )
  {
    int id;

    id = sp.load( this.ama.context, rnum, 1 );

    se.put( rnum, id );

    return true;
  }

  public boolean UnloadSE( int rnum )
  {
    if ( se.containsKey( rnum ) == false ){ return false; }

    sp.unload( se.get( rnum ) );
    se.remove( rnum );

    return true;
  }

  public boolean UnloadSE()
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

  public boolean PlaySE( int rnum ){ return this.PlaySE( rnum, false, 1.0f, 1.0f, 1.0f ); }

  public boolean PlaySE( int rnum, boolean loop ){ return this.PlaySE( rnum, loop, 1.0f, volume, volume ); }

  public boolean PlaySE( int rnum, boolean loop, float volume ){ return this.PlaySE( rnum, loop, 1.0f, volume, volume ); }

  public boolean PlaySE( int rnum, boolean loop, float late, float pan_l, float pan_r )
  {
    if ( se.containsKey( rnum ) == false ){ return false; }

    sp.play( se.get( rnum ), pan_l, pan_r, 0, loop ? -1 : 0, late );

    return true;
  }

  public boolean StopSE( int rnum )
  {
    if ( se.containsKey( rnum ) == false ){ return false;}
    sp.stop( se.get( rnum ) );
    return true;
  }

  public boolean StopSE()
  {
    return true;
  }

  public boolean ResumeSE( int rnum )
  {
    if ( se.containsKey( rnum ) == false ){ return false;}
    sp.resume( se.get( rnum ) );
    return true;
  }

  public boolean PauseSE( int rnum )
  {
    if ( se.containsKey( rnum ) == false ){ return false;}
    sp.pause( se.get( rnum ) );
    return true;
  }
  public boolean PauseSE()
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
