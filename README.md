Amanatsu
========
## Android
Version 2.2 or higher.

## Compile
Use Eclipse.
Do not export "AndroidManifest.xml" and "project.properties".

## How to use
Copy Amanatsu.jar to libs/ dir.

<pre><code>
package XXXXX;

import android.os.Bundle;
import android.view.MotionEvent;
import android.app.Activity;
import net.azulite.Amanatsu.*;

public class WitchOfGolem extends Activity
{
  Amanatsu ama;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    ama = new Amanatsu( this, new Game() );

    this.setContentView( ama.GetGLSurfaceView() );
  }

}

class Game implements GameView
{
  @Override
  public void UserInit( OpenGLDraw draw ) {
  }

  @Override
  public boolean MainLoop( OpenGLDraw draw ) {
    return true; // false is Game end.
  }

  @Override
  public void CleanUp( OpenGLDraw draw ) {
  }

  @Override
  public void Touch(MotionEvent event) {
  }
}
<code></pre>

## Android
バージョン2.2以上。

## コンパイル方法
EclipseにAmanatsuのプロジェクトを追加し、プロジェクトをエクスポートしてJARファイルとして出力する。
ただし、"AndroidManifest.xml"と"project.properties"はエクスポートしないようにチェックボックスを外すこと。

## 利用方法
使いたいプロジェクト内にlibsフォルダを作り、その中にAmanatsu.jarをコピーする。

で、後は上のような感じで適当にゲーム処理を記述するためのGameクラス(GameViewを継承)を作り、Amanatsuオブジェクト生成時に渡したり、View登録時にAmanatsuのViewを登録する。

Gameクラスは次のようになっている。

### UserInit
実行開始の初めの一度だけ実行されるメソッド。

### MainLoop
毎フレーム呼ばれるメソッド。

### CleanUp
終了時に呼ばれるメソッド。

## その他
AmanatsuはWindowsのVC++用ゲームライブラリである、MikanライブラリのAndroid版です。
