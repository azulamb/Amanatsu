package net.azulite.Amanatsu;

public interface GameView
{

  public void UserInit( AmanatsuDraw draw, AmanatsuInput input, AmanatsuSound sound );

  public boolean MainLoop( AmanatsuDraw draw, AmanatsuInput input, AmanatsuSound sound );

  public void CleanUp( AmanatsuDraw draw, AmanatsuInput input, AmanatsuSound sound );
}
