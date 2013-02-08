Amanatsu
========
## Android
Version 2.2 or higher.

## How to use
Iy you only want to use Amanatsu, Download "Amanatsu.jar".
And copy "Amanatsu.jar" to "libs/" dir in your project.

## Compile
Use Eclipse.
Do not export "AndroidManifest.xml" and "project.properties".

## Sample code

<pre><code>
package XXXXX;

import android.os.Bundle;
import android.app.Activity;
import net.azulite.Amanatsu.*;

public class XXXXX extends Activity
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

## 利用方法
使いたいプロジェクト内にlibsフォルダを作り、その中にAmanatsu.jarをコピーする。
ソースとか興味なくてただ単に使いたい場合は、ルートディレクトリにある"Amanatsu.jar"をダウンロード(ファイル名をクリック->Raw or View Rawをクリック)して、プロジェクト内に用意した libs/ ディレクトリに入れてください。

で、後は上のような感じで適当にゲーム処理を記述するためのGameクラス(GameViewを継承)を作り、Amanatsuオブジェクト生成時に渡したり、View登録時にAmanatsuのViewを登録する。

GameViewインターフェースは次のようになっている。

### UserInit
実行開始の初めの一度だけ実行されるメソッド。

### MainLoop
毎フレーム呼ばれるメソッド。

### CleanUp
終了時に呼ばれるメソッド。

## コンパイル方法
EclipseにAmanatsuのプロジェクトを追加し、プロジェクトをエクスポートしてJARファイルとして出力する。
ただし、"AndroidManifest.xml"と"project.properties"はエクスポートしないようにチェックボックスを外すこと。

## その他

### リソース
画像の場合、drawableに入れると画像がリサイズされる場合がある。
rawディレクトリを作って、その中に入れた方が無難。

### Amanatsuについて
AmanatsuはWindowsのVC++用ゲームライブラリであるMikanライブラリのAndroid版だったり。

### ロゴについて
今のは適当なので、依頼予定。
