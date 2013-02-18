package net.azulite.Amanatsu;

import java.nio.FloatBuffer;
/**
 * Amanatsuでのテクスチャ情報を管理するクラス。
 */

public class Texture
{
  public int rnum = -1;
  public int [] texid = null;
  public float width = 0, height = 0;
  public FloatBuffer ver, col, uv;
}
