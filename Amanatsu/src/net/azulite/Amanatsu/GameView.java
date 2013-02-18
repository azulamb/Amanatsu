package net.azulite.Amanatsu;

/**
 * Amanatsu�ɓo�^����Q�[���N���X�B
 */
public interface GameView
{

  /**
   * ���߂Ɉ�x�������s�B
   * @param draw �`��T�|�[�g�N���X�B
   * @param input ���̓T�|�[�g�N���X�B
   * @param sound ���T�|�[�g�N���X�B
   */
  public void UserInit( AmanatsuDraw draw, AmanatsuInput input, AmanatsuSound sound );

  /**
   * ���t���[�����s�B
   * @param draw �`��T�|�[�g�N���X�B
   * @param input ���̓T�|�[�g�N���X�B
   * @param sound ���T�|�[�g�N���X�B
   */
  public boolean MainLoop( AmanatsuDraw draw, AmanatsuInput input, AmanatsuSound sound );

  /**
   * �I�����Ɏ��s�B
   * @param draw �`��T�|�[�g�N���X�B
   * @param input ���̓T�|�[�g�N���X�B
   * @param sound ���T�|�[�g�N���X�B
   */
  public void CleanUp( AmanatsuDraw draw, AmanatsuInput input, AmanatsuSound sound );
}
