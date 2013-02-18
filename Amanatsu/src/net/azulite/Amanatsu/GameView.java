package net.azulite.Amanatsu;

/**
 * Amanatsuに登録するゲームクラス。
 */
public interface GameView
{

  /**
   * 初めに一度だけ実行。
   * @param draw 描画サポートクラス。
   * @param input 入力サポートクラス。
   * @param sound 音サポートクラス。
   */
  public void UserInit( AmanatsuDraw draw, AmanatsuInput input, AmanatsuSound sound );

  /**
   * 毎フレーム実行。
   * @param draw 描画サポートクラス。
   * @param input 入力サポートクラス。
   * @param sound 音サポートクラス。
   */
  public boolean MainLoop( AmanatsuDraw draw, AmanatsuInput input, AmanatsuSound sound );

  /**
   * 終了時に実行。
   * @param draw 描画サポートクラス。
   * @param input 入力サポートクラス。
   * @param sound 音サポートクラス。
   */
  public void CleanUp( AmanatsuDraw draw, AmanatsuInput input, AmanatsuSound sound );
}
