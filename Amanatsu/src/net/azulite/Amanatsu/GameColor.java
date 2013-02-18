package net.azulite.Amanatsu;

/**
 * Amanatsuで使う色クラス。
 * 色の名前によっては既に定義してある。
 * 色名と値に関しては http://www.colordic.org/ を参考にしている。
 */
public class GameColor
{
  float[] color;

  public static final float[] BLACK                = new float[]{          0.0f,          0.0f,          0.0f, 1.0f };
  public static final float[] ALICEBLUE            = new float[]{ 240.0f/255.0f, 248.0f/255.0f,          1.0f, 1.0f };
  public static final float[] DARKCYAN             = new float[]{          0.0f, 139.0f/255.0f, 139.0f/255.0f, 1.0f };
  public static final float[] LIGHTYELLOW          = new float[]{          1.0f,          1.0f, 224.0f/255.0f, 1.0f };
  public static final float[] CORAL                = new float[]{          1.0f, 127.0f/255.0f,  80.0f/255.0f, 1.0f };
  public static final float[] DIMGRAY              = new float[]{ 105.0f/255.0f, 105.0f/255.0f, 105.0f/255.0f, 1.0f };
  public static final float[] LAVENDER             = new float[]{ 230.0f/255.0f, 230.0f/255.0f, 250.0f/255.0f, 1.0f };
  public static final float[] TEAL                 = new float[]{          0.0f, 128.0f/255.0f, 128.0f/255.0f, 1.0f };
  public static final float[] LIGHTGOLDENRODYELLOW = new float[]{ 250.0f/255.0f, 250.0f/255.0f, 210.0f/255.0f, 1.0f };
  public static final float[] TOMATO               = new float[]{          1.0f,  99.0f/255.0f,  71.0f/255.0f, 1.0f };
  public static final float[] GRAY                 = new float[]{ 128.0f/255.0f, 128.0f/255.0f, 128.0f/255.0f, 1.0f };
  public static final float[] LIGHTSTEELBLUE       = new float[]{ 176.0f/255.0f, 196.0f/255.0f, 222.0f/255.0f, 1.0f };
  public static final float[] DARKSLATEGRAY        = new float[]{  47.0f/255.0f,  79.0f/255.0f,  79.0f/255.0f, 1.0f };
  public static final float[] LEMONCHIFFON         = new float[]{          1.0f, 250.0f/255.0f, 205.0f/255.0f, 1.0f };
  public static final float[] ORANGERED            = new float[]{          1.0f,  69.0f/255.0f,          0.0f, 1.0f };
  public static final float[] DARKGRAY             = new float[]{ 169.0f/255.0f, 169.0f/255.0f, 169.0f/255.0f, 1.0f };
  public static final float[] LIGHTSLATEGRAY       = new float[]{ 119.0f/255.0f, 136.0f/255.0f, 153.0f/255.0f, 1.0f };
  public static final float[] DARKGREEN            = new float[]{          0.0f, 100.0f/255.0f,          0.0f, 1.0f };
  public static final float[] WHEAT                = new float[]{ 245.0f/255.0f, 222.0f/255.0f, 179.0f/255.0f, 1.0f };
  public static final float[] RED                  = new float[]{          1.0f,          0.0f,          0.0f, 1.0f };
  public static final float[] SILVER               = new float[]{ 192.0f/255.0f, 192.0f/255.0f, 192.0f/255.0f, 1.0f };
  public static final float[] SLATEGRAY            = new float[]{ 112.0f/255.0f, 128.0f/255.0f, 144.0f/255.0f, 1.0f };
  public static final float[] GREEN                = new float[]{          0.0f, 128.0f/255.0f,          0.0f, 1.0f };
  public static final float[] BURLYWOOD            = new float[]{ 222.0f/255.0f, 184.0f/255.0f, 135.0f/255.0f, 1.0f };
  public static final float[] CRIMSON              = new float[]{ 220.0f/255.0f,  20.0f/255.0f,  60.0f/255.0f, 1.0f };
  public static final float[] LIGHTGREY            = new float[]{ 211.0f/255.0f, 211.0f/255.0f, 211.0f/255.0f, 1.0f };
  public static final float[] STEELBLUE            = new float[]{  70.0f/255.0f, 130.0f/255.0f, 180.0f/255.0f, 1.0f };
  public static final float[] FORESTGREEN          = new float[]{  34.0f/255.0f, 139.0f/255.0f,  34.0f/255.0f, 1.0f };
  public static final float[] TAN                  = new float[]{ 210.0f/255.0f, 180.0f/255.0f, 140.0f/255.0f, 1.0f };
  public static final float[] MEDIUMVIOLETRED      = new float[]{ 199.0f/255.0f,  21.0f/255.0f, 133.0f/255.0f, 1.0f };
  public static final float[] GAINSBORO            = new float[]{ 220.0f/255.0f, 220.0f/255.0f, 220.0f/255.0f, 1.0f };
  public static final float[] ROYALBLUE            = new float[]{  65.0f/255.0f, 105.0f/255.0f, 225.0f/255.0f, 1.0f };
  public static final float[] SEAGREEN             = new float[]{  46.0f/255.0f, 139.0f/255.0f,  87.0f/255.0f, 1.0f };
  public static final float[] KHAKI                = new float[]{ 240.0f/255.0f, 230.0f/255.0f, 140.0f/255.0f, 1.0f };
  public static final float[] DEEPPINK             = new float[]{          1.0f,  20.0f/255.0f, 147.0f/255.0f, 1.0f };
  public static final float[] WHITESMOKE           = new float[]{ 245.0f/255.0f, 245.0f/255.0f, 245.0f/255.0f, 1.0f };
  public static final float[] MIDNIGHTBLUE         = new float[]{  25.0f/255.0f,  25.0f/255.0f, 112.0f/255.0f, 1.0f };
  public static final float[] MEDIUMSEAGREEN       = new float[]{  60.0f/255.0f, 179.0f/255.0f, 113.0f/255.0f, 1.0f };
  public static final float[] YELLOW               = new float[]{          1.0f,          1.0f,          0.0f, 1.0f };
  public static final float[] HOTPINK              = new float[]{          1.0f, 105.0f/255.0f, 180.0f/255.0f, 1.0f };
  public static final float[] WHITE                = new float[]{          1.0f,          1.0f,          1.0f, 1.0f };
  public static final float[] NAVY                 = new float[]{          0.0f,          0.0f, 128.0f/255.0f, 1.0f };
  public static final float[] MEDIUMAQUAMARINE     = new float[]{ 102.0f/255.0f, 205.0f/255.0f, 170.0f/255.0f, 1.0f };
  public static final float[] GOLD                 = new float[]{          1.0f, 215.0f/255.0f,          0.0f, 1.0f };
  public static final float[] PALEVIOLETRED        = new float[]{ 219.0f/255.0f, 112.0f/255.0f, 147.0f/255.0f, 1.0f };
  public static final float[] SNOW                 = new float[]{          1.0f, 250.0f/255.0f, 250.0f/255.0f, 1.0f };
  public static final float[] DARKBLUE             = new float[]{          0.0f,          0.0f, 139.0f/255.0f, 1.0f };
  public static final float[] DARKSEAGREEN         = new float[]{ 143.0f/255.0f, 188.0f/255.0f, 143.0f/255.0f, 1.0f };
  public static final float[] ORANGE               = new float[]{          1.0f, 165.0f/255.0f,          0.0f, 1.0f };
  public static final float[] PINK                 = new float[]{          1.0f, 192.0f/255.0f, 203.0f/255.0f, 1.0f };
  public static final float[] GHOSTWHITE           = new float[]{ 248.0f/255.0f, 248.0f/255.0f,          1.0f, 1.0f };
  public static final float[] MEDIUMBLUE           = new float[]{          0.0f,          0.0f, 205.0f/255.0f, 1.0f };
  public static final float[] AQUAMARINE           = new float[]{ 127.0f/255.0f,          1.0f, 212.0f/255.0f, 1.0f };
  public static final float[] SANDYBROWN           = new float[]{ 244.0f/255.0f, 164.0f/255.0f,  96.0f/255.0f, 1.0f };
  public static final float[] LIGHTPINK            = new float[]{          1.0f, 182.0f/255.0f, 193.0f/255.0f, 1.0f };
  public static final float[] FLORALWHITE          = new float[]{          1.0f, 250.0f/255.0f, 240.0f/255.0f, 1.0f };
  public static final float[] BLUE                 = new float[]{          0.0f,          0.0f,          1.0f, 1.0f };
  public static final float[] PALEGREEN            = new float[]{ 152.0f/255.0f, 251.0f/255.0f, 152.0f/255.0f, 1.0f };
  public static final float[] DARKORANGE           = new float[]{          1.0f, 140.0f/255.0f,          0.0f, 1.0f };
  public static final float[] THISTLE              = new float[]{ 216.0f/255.0f, 191.0f/255.0f, 216.0f/255.0f, 1.0f };
  public static final float[] LINEN                = new float[]{ 250.0f/255.0f, 240.0f/255.0f, 230.0f/255.0f, 1.0f };
  public static final float[] DODGERBLUE           = new float[]{  30.0f/255.0f, 144.0f/255.0f,          1.0f, 1.0f };
  public static final float[] LIGHTGREEN           = new float[]{ 144.0f/255.0f, 238.0f/255.0f, 144.0f/255.0f, 1.0f };
  public static final float[] GOLDENROD            = new float[]{ 218.0f/255.0f, 165.0f/255.0f,  32.0f/255.0f, 1.0f };
  public static final float[] MAGENTA              = new float[]{          1.0f,          0.0f,          1.0f, 1.0f };
  public static final float[] ANTIQUEWHITE         = new float[]{ 250.0f/255.0f, 235.0f/255.0f, 215.0f/255.0f, 1.0f };
  public static final float[] CORNFLOWERBLUE       = new float[]{ 100.0f/255.0f, 149.0f/255.0f, 237.0f/255.0f, 1.0f };
  public static final float[] SPRINGGREEN          = new float[]{          0.0f,          1.0f, 127.0f/255.0f, 1.0f };
  public static final float[] PERU                 = new float[]{ 205.0f/255.0f, 133.0f/255.0f,  63.0f/255.0f, 1.0f };
  public static final float[] FUCHSIA              = new float[]{          1.0f,          0.0f,          1.0f, 1.0f };
  public static final float[] PAPAYAWHIP           = new float[]{          1.0f, 239.0f/255.0f, 213.0f/255.0f, 1.0f };
  public static final float[] DEEPSKYBLUE          = new float[]{          0.0f, 191.0f/255.0f,          1.0f, 1.0f };
  public static final float[] MEDIUMSPRINGGREEN    = new float[]{          0.0f, 250.0f/255.0f, 154.0f/255.0f, 1.0f };
  public static final float[] DARKGOLDENROD        = new float[]{ 184.0f/255.0f, 134.0f/255.0f,  11.0f/255.0f, 1.0f };
  public static final float[] VIOLET               = new float[]{ 238.0f/255.0f, 130.0f/255.0f, 238.0f/255.0f, 1.0f };
  public static final float[] BLANCHEDALMOND       = new float[]{          1.0f, 235.0f/255.0f, 205.0f/255.0f, 1.0f };
  public static final float[] LIGHTSKYBLUE         = new float[]{ 135.0f/255.0f, 206.0f/255.0f, 250.0f/255.0f, 1.0f };
  public static final float[] LAWNGREEN            = new float[]{ 124.0f/255.0f, 252.0f/255.0f,          0.0f, 1.0f };
  public static final float[] CHOCOLATE            = new float[]{ 210.0f/255.0f, 105.0f/255.0f,  30.0f/255.0f, 1.0f };
  public static final float[] PLUM                 = new float[]{ 221.0f/255.0f, 160.0f/255.0f, 221.0f/255.0f, 1.0f };
  public static final float[] BISQUE               = new float[]{          1.0f, 228.0f/255.0f, 196.0f/255.0f, 1.0f };
  public static final float[] SKYBLUE              = new float[]{ 135.0f/255.0f, 206.0f/255.0f, 235.0f/255.0f, 1.0f };
  public static final float[] CHARTREUSE           = new float[]{ 127.0f/255.0f,          1.0f,          0.0f, 1.0f };
  public static final float[] SIENNA               = new float[]{ 160.0f/255.0f,  82.0f/255.0f,  45.0f/255.0f, 1.0f };
  public static final float[] ORCHID               = new float[]{ 218.0f/255.0f, 112.0f/255.0f, 214.0f/255.0f, 1.0f };
  public static final float[] MOCCASIN             = new float[]{          1.0f, 228.0f/255.0f, 181.0f/255.0f, 1.0f };
  public static final float[] LIGHTBLUE            = new float[]{ 173.0f/255.0f, 216.0f/255.0f, 230.0f/255.0f, 1.0f };
  public static final float[] GREENYELLOW          = new float[]{ 173.0f/255.0f,          1.0f,  47.0f/255.0f, 1.0f };
  public static final float[] SADDLEBROWN          = new float[]{ 139.0f/255.0f,  69.0f/255.0f,  19.0f/255.0f, 1.0f };
  public static final float[] MEDIUMORCHID         = new float[]{ 186.0f/255.0f,  85.0f/255.0f, 211.0f/255.0f, 1.0f };
  public static final float[] NAVAJOWHITE          = new float[]{          1.0f, 222.0f/255.0f, 173.0f/255.0f, 1.0f };
  public static final float[] POWDERBLUE           = new float[]{ 176.0f/255.0f, 224.0f/255.0f, 230.0f/255.0f, 1.0f };
  public static final float[] LIME                 = new float[]{          0.0f,          1.0f,          0.0f, 1.0f };
  public static final float[] MAROON               = new float[]{ 128.0f/255.0f,          0.0f,          0.0f, 1.0f };
  public static final float[] DARKORCHID           = new float[]{ 153.0f/255.0f,  50.0f/255.0f, 204.0f/255.0f, 1.0f };
  public static final float[] PEACHPUFF            = new float[]{          1.0f, 218.0f/255.0f, 185.0f/255.0f, 1.0f };
  public static final float[] PALETURQUOISE        = new float[]{ 175.0f/255.0f, 238.0f/255.0f, 238.0f/255.0f, 1.0f };
  public static final float[] LIMEGREEN            = new float[]{  50.0f/255.0f, 205.0f/255.0f,  50.0f/255.0f, 1.0f };
  public static final float[] DARKRED              = new float[]{ 139.0f/255.0f,          0.0f,          0.0f, 1.0f };
  public static final float[] DARKVIOLET           = new float[]{ 148.0f/255.0f,          0.0f, 211.0f/255.0f, 1.0f };
  public static final float[] MISTYROSE            = new float[]{          1.0f, 228.0f/255.0f, 225.0f/255.0f, 1.0f };
  public static final float[] LIGHTCYAN            = new float[]{ 224.0f/255.0f,          1.0f,          1.0f, 1.0f };
  public static final float[] YELLOWGREEN          = new float[]{ 154.0f/255.0f, 205.0f/255.0f,  50.0f/255.0f, 1.0f };
  public static final float[] BROWN                = new float[]{ 165.0f/255.0f,  42.0f/255.0f,  42.0f/255.0f, 1.0f };
  public static final float[] DARKMAGENTA          = new float[]{ 139.0f/255.0f,          0.0f, 139.0f/255.0f, 1.0f };
  public static final float[] LAVENDERBLUSH        = new float[]{          1.0f, 240.0f/255.0f, 245.0f/255.0f, 1.0f };
  public static final float[] CYAN                 = new float[]{          0.0f,          1.0f,          1.0f, 1.0f };
  public static final float[] DARKOLIVEGREEN       = new float[]{  85.0f/255.0f, 107.0f/255.0f,  47.0f/255.0f, 1.0f };
  public static final float[] FIREBRICK            = new float[]{ 178.0f/255.0f,  34.0f/255.0f,  34.0f/255.0f, 1.0f };
  public static final float[] PURPLE               = new float[]{ 128.0f/255.0f,          0.0f, 128.0f/255.0f, 1.0f };
  public static final float[] SEASHELL             = new float[]{          1.0f, 245.0f/255.0f, 238.0f/255.0f, 1.0f };
  public static final float[] AQUA                 = new float[]{          0.0f,          1.0f,          1.0f, 1.0f };
  public static final float[] OLIVEDRAB            = new float[]{ 107.0f/255.0f, 142.0f/255.0f,  35.0f/255.0f, 1.0f };
  public static final float[] INDIANRED            = new float[]{ 205.0f/255.0f,  92.0f/255.0f,  92.0f/255.0f, 1.0f };
  public static final float[] INDIGO               = new float[]{  75.0f/255.0f,          0.0f, 130.0f/255.0f, 1.0f };
  public static final float[] OLDLACE              = new float[]{ 253.0f/255.0f, 245.0f/255.0f, 230.0f/255.0f, 1.0f };
  public static final float[] TURQUOISE            = new float[]{  64.0f/255.0f, 224.0f/255.0f, 208.0f/255.0f, 1.0f };
  public static final float[] OLIVE                = new float[]{ 128.0f/255.0f, 128.0f/255.0f,          0.0f, 1.0f };
  public static final float[] ROSYBROWN            = new float[]{ 188.0f/255.0f, 143.0f/255.0f, 143.0f/255.0f, 1.0f };
  public static final float[] DARKSLATEBLUE        = new float[]{  72.0f/255.0f,  61.0f/255.0f, 139.0f/255.0f, 1.0f };
  public static final float[] IVORY                = new float[]{          1.0f,          1.0f, 240.0f/255.0f, 1.0f };
  public static final float[] MEDIUMTURQUOISE      = new float[]{  72.0f/255.0f, 209.0f/255.0f, 204.0f/255.0f, 1.0f };
  public static final float[] DARKKHAKI            = new float[]{ 189.0f/255.0f, 183.0f/255.0f, 107.0f/255.0f, 1.0f };
  public static final float[] DARKSALMON           = new float[]{ 233.0f/255.0f, 150.0f/255.0f, 122.0f/255.0f, 1.0f };
  public static final float[] BLUEVIOLET           = new float[]{ 138.0f/255.0f,  43.0f/255.0f, 226.0f/255.0f, 1.0f };
  public static final float[] HONEYDEW             = new float[]{ 240.0f/255.0f,          1.0f, 240.0f/255.0f, 1.0f };
  public static final float[] DARKTURQUOISE        = new float[]{          0.0f, 206.0f/255.0f, 209.0f/255.0f, 1.0f };
  public static final float[] PALEGOLDENROD        = new float[]{ 238.0f/255.0f, 232.0f/255.0f, 170.0f/255.0f, 1.0f };
  public static final float[] LIGHTCORAL           = new float[]{ 240.0f/255.0f, 128.0f/255.0f, 128.0f/255.0f, 1.0f };
  public static final float[] MEDIUMPURPLE         = new float[]{ 147.0f/255.0f, 112.0f/255.0f, 219.0f/255.0f, 1.0f };
  public static final float[] MINTCREAM            = new float[]{ 245.0f/255.0f,          1.0f, 250.0f/255.0f, 1.0f };
  public static final float[] LIGHTSEAGREEN        = new float[]{  32.0f/255.0f, 178.0f/255.0f, 170.0f/255.0f, 1.0f };
  public static final float[] CORNSILK             = new float[]{          1.0f, 248.0f/255.0f, 220.0f/255.0f, 1.0f };
  public static final float[] SALMON               = new float[]{ 250.0f/255.0f, 128.0f/255.0f, 114.0f/255.0f, 1.0f };
  public static final float[] SLATEBLUE            = new float[]{ 106.0f/255.0f,  90.0f/255.0f, 205.0f/255.0f, 1.0f };
  public static final float[] AZURE                = new float[]{ 240.0f/255.0f,          1.0f,          1.0f, 1.0f };
  public static final float[] CADETBLUE            = new float[]{  95.0f/255.0f, 158.0f/255.0f, 160.0f/255.0f, 1.0f };
  public static final float[] BEIGE                = new float[]{ 245.0f/255.0f, 245.0f/255.0f, 220.0f/255.0f, 1.0f };
  public static final float[] LIGHTSALMON          = new float[]{          1.0f, 160.0f/255.0f, 122.0f/255.0f, 1.0f };
  public static final float[] MEDIUMSLATEBLUE      = new float[]{ 123.0f/255.0f, 104.0f/255.0f, 238.0f/255.0f, 1.0f };

  /**
   * GameColorインスタンスの作成。
   * @param red 赤(0-255)。
   * @param green 緑(0-255)。
   * @param blue 青(0-255)。
   * @param alpha 不透明度(0-255)。
   */
  public GameColor( byte red, byte green, byte blue, byte alpha )
  {
    color = new float[ 4 ];
    color[ 0 ] = (float)( red & 0xff ) / 255.0f;
    color[ 1 ] = (float)( green & 0xff ) / 255.0f;
    color[ 2 ] = (float)( blue & 0xff ) / 255.0f;
    color[ 3 ] = (float)( alpha & 0xff ) / 255.0f;
  }

  /**
   * GameColorインスタンスの作成。
   * @param red 赤の色の強さ(0.0f-1.0f)。
   * @param green 緑の強さ(0.0f-1.0f)。
   * @param blue 青の強さ(0.0f-1.0f)。
   * @param alpha 不透明度(0.0f-1.0f)。
   */
  public GameColor( float red, float green, float blue, float alpha )
  {
    color = new float[ 4 ];
    color[ 0 ] = red;
    color[ 1 ] = green;
    color[ 2 ] = blue;
    color[ 3 ] = alpha;
  }

  /**
   * 色配列の作成。
   * @param red 赤(0-255)。
   * @param green 緑(0-255)。
   * @param blue 青(0-255)。
   * @param alpha 不透明度(0-255)。
   */
  static final float[] createColor( byte red, byte green, byte blue, byte alpha )
  {
    float[] color = new float[ 4 ];
    color[ 0 ] = (float)( red & 0xff ) / 255.0f;
    color[ 1 ] = (float)( green & 0xff ) / 255.0f;
    color[ 2 ] = (float)( blue & 0xff ) / 255.0f;
    color[ 3 ] = (float)( alpha & 0xff ) / 255.0f;
    return color;
  }

  /**
   * 色配列の作成。
   * @param red 赤の色の強さ(0.0f-1.0f)。
   * @param green 緑の強さ(0.0f-1.0f)。
   * @param blue 青の強さ(0.0f-1.0f)。
   * @param alpha 不透明度(0.0f-1.0f)。
   */
  static final float[] createColor( float red, float green, float blue, float alpha )
  {
    float[] color = new float[ 4 ];
    color[ 0 ] = red;
    color[ 1 ] = green;
    color[ 2 ] = blue;
    color[ 3 ] = alpha;
    return color;
  }
}
