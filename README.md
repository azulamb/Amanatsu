Amanatsu
========
## Android
Version 2.2 or higher(Enable multi touch).

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
  public void onCreate( Bundle savedInstanceState ) {
    super.onCreate( savedInstanceState );

    // Create Amanatsu object.
    ama = new Amanatsu( this, new Game() );

    // Set View.
    this.setContentView( ama.getGLSurfaceView() );

    // Start game.
    ama.start();
  }

}

class Game implements GameView
{
  @Override
  public void UserInit(  AmanatsuDraw draw, AmanatsuInput input, AmanatsuSound sound ) {
    // Prepare game.
  }

  @Override
  public boolean MainLoop(  AmanatsuDraw draw, AmanatsuInput input, AmanatsuSound sound ) {
    // Game main routine.
    return true; // false is Game end.
  }

  @Override
  public void CleanUp(  AmanatsuDraw draw, AmanatsuInput input, AmanatsuSound sound ) {
    // Cleanup game.
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

##### 画像
画像の場合、drawableに入れると画像がリサイズされる場合がある。
assetsに入れることを推奨(その場合createTextureではテクスチャ番号とファイルパスを与える形になる)。

##### 音声
res/rawもしくはassets内に置く。
前者はリソース番号、後者は音番号とファイルパスで読み込みや再生が可能。

### 入力

##### タッチ

Amanatsuでの入力は座標以外にもいくつか特徴がある。

* タッチフレームの取得(getTouchFrame, getFingerTouchFrame)
  * タッチされたフレーム数を返す
  * 0は未タッチ、-1は離れた。
  * 指番号を指定する場合-1を観測できない場合がある。
     * 指IDを使う Finger 系の命令を使うと良い。

##### キー入力

キー番号を指定して、その番号に割り当てられたキーが押されているかどうかや押しているフレーム数を調べることが出来る。
フレーム数に関してはタッチ同様の仕様。

キー番号はWindowsなどの仮想キーではなく、Androidが独自に？設定した比較的順番等に規則性があるものになっている。
また、Amanatsu.K_キー名 である程度のキー番号を得ることが出来る。
キー名は戻るボタンのBACKやキーボードのZなどの英数字、UPやSPACEなどのような名前が割り当てられている。

### Amanatsuについて
AmanatsuはWindowsのVC++用ゲームライブラリであるMikanライブラリのAndroid版だったり。

### ロゴについて
今のは適当なので、依頼予定。
