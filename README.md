Amanatsu
========
## Android
Version 2.2 or higher.

## Compile
Use Eclipse.
Do not export "AndroidManifest.xml" and "project.properties".

## How to use
Copy Amanatsu.jar to libs/ dir.

## Sample code

<pre><code>
package XXXXX;

import android.os.Bundle;
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
  public void UserInit(  AmanatsuDraw draw, AmanatsuInput input, AmanatsuSound sound ) {
  }

  @Override
  public boolean MainLoop(  AmanatsuDraw draw, AmanatsuInput input, AmanatsuSound sound ) {
    return true; // false is Game end.
  }

  @Override
  public void CleanUp(  AmanatsuDraw draw, AmanatsuInput input, AmanatsuSound sound ) {
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

GameViewインターフェースは次のようになっている。

### UserInit
実行開始の初めの一度だけ実行されるメソッド。

### MainLoop
毎フレーム呼ばれるメソッド。

### CleanUp
終了時に呼ばれるメソッド。

## その他

### リソース
画像の場合、drawableに入れると画像がリサイズされる場合があります。
rawディレクトリを作って、その中に入れた方が無難です。

### Amanatsuについて
AmanatsuはWindowsのVC++用ゲームライブラリであるMikanライブラリのAndroid版です。
