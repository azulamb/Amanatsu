package net.azulite.Amanatsu;

/**
 * Amanatsuに登録するゲームクラス。
 */
public class GameView
{
  public static Amanatsu system;
  public static AmanatsuDraw draw;
  public static AmanatsuInput input;
  public static AmanatsuSound sound;
  
  /**
   * 端末の向きが変わった時に実行。
   * @param draw 描画サポートクラス。
   * @param input 入力サポートクラス。
   * @param sound 音サポートクラス。
   */
  public void ChangeDevice(){}
  /**
   * 初めに一度だけ実行。
   * @param draw 描画サポートクラス。
   * @param input 入力サポートクラス。
   * @param sound 音サポートクラス。
   */
  public void UserInit(){}

  /**
   * 毎フレーム実行。
   * @param draw 描画サポートクラス。
   * @param input 入力サポートクラス。
   * @param sound 音サポートクラス。
   */
  public boolean MainLoop()
  {
    draw.clearScreen();
    return true;
  }

  /**
   * 終了時に実行。
   * @param draw 描画サポートクラス。
   * @param input 入力サポートクラス。
   * @param sound 音サポートクラス。
   */
  public void CleanUp(){}
}
