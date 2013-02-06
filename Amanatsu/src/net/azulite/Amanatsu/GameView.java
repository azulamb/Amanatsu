package net.azulite.Amanatsu;

import android.view.MotionEvent;

public interface GameView
{

  public void UserInit( OpenGLDraw draw );

  public boolean MainLoop( OpenGLDraw draw );

  public void CleanUp( OpenGLDraw draw );
  
  public void Touch( MotionEvent event );
}
