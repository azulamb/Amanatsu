package net.azulite.Amanatsu;

public interface GameView
{

  public void UserInit( AmanatsuDraw draw );

  public boolean MainLoop( AmanatsuDraw draw, AmanatsuInput input );

  public void CleanUp( AmanatsuDraw draw );
}
