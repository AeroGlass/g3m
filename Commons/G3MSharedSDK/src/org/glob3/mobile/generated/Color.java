package org.glob3.mobile.generated; 
//
//  Color.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 6/3/13.
//
//

//
//  Color.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 13/06/12.
//  Copyright (c) 2012 IGO Software SL. All rights reserved.
//



public class Color
{
  private final float _red;
  private final float _green;
  private final float _blue;
  private final float _alpha;

  private Color(float red, float green, float blue, float alpha)
  {
     _red = red;
     _green = green;
     _blue = blue;
     _alpha = alpha;

  }

  public Color(Color that)
  {
     _red = that._red;
     _green = that._green;
     _blue = that._blue;
     _alpha = that._alpha;
  }

  public void dispose()
  {
  }

  public static Color parse(String str)
  {
    final IStringUtils su = IStringUtils.instance();
  
    String colorStr = su.trim(str);
  
    if (su.beginsWith(colorStr, "#"))
    {
      colorStr = su.trim(su.substring(colorStr, 1));
    }
  
    final int strSize = colorStr.length();
  
    String rs;
    String gs;
    String bs;
    String as;
    if (strSize == 3)
    {
      // RGB
      rs = su.substring(colorStr, 0, 1);
      gs = su.substring(colorStr, 1, 2);
      bs = su.substring(colorStr, 2, 3);
      as = "ff";
  
      rs = rs + rs;
      gs = gs + gs;
      bs = bs + bs;
    }
    else if (strSize == 4)
    {
      // RGBA
      rs = su.substring(colorStr, 0, 1);
      gs = su.substring(colorStr, 1, 2);
      bs = su.substring(colorStr, 2, 3);
      as = su.substring(colorStr, 3, 4);
  
      rs = rs + rs;
      gs = gs + gs;
      bs = bs + bs;
      as = as + as;
    }
    else if (strSize == 6)
    {
      // RRGGBB
      rs = su.substring(colorStr, 0, 2);
      gs = su.substring(colorStr, 2, 4);
      bs = su.substring(colorStr, 4, 6);
      as = "ff";
    }
    else if (strSize == 8)
    {
      // RRGGBBAA
      rs = su.substring(colorStr, 0, 2);
      gs = su.substring(colorStr, 2, 4);
      bs = su.substring(colorStr, 4, 6);
      as = su.substring(colorStr, 6, 8);
    }
    else
    {
      ILogger.instance().logError("Invalid color format: \"%s\"", str);
      return null;
    }
  
    final IMathUtils mu = IMathUtils.instance();
  
    final float r = mu.clamp((float) su.parseHexInt(rs) / 255.0f, 0, 1);
    final float g = mu.clamp((float) su.parseHexInt(gs) / 255.0f, 0, 1);
    final float b = mu.clamp((float) su.parseHexInt(bs) / 255.0f, 0, 1);
    final float a = mu.clamp((float) su.parseHexInt(as) / 255.0f, 0, 1);
  
    return Color.newFromRGBA(r, g, b, a);
  }

  public static Color fromRGBA(float red, float green, float blue, float alpha)
  {
    return new Color(red, green, blue, alpha);
  }

  public static Color newFromRGBA(float red, float green, float blue, float alpha)
  {
    return new Color(red, green, blue, alpha);
  }

  public static Color fromHueSaturationBrightness(double hueInRadians, float saturation, float brightness, float alpha)
  {
    final IMathUtils mu = IMathUtils.instance();
  
    final float s = mu.clamp(saturation, 0, 1);
    final float v = mu.clamp(brightness, 0, 1);
  
    //  zero saturation yields gray with the given brightness
    if (s == 0)
    {
      return fromRGBA(v, v, v, alpha);
    }
  
    final double deg60 = 60.0 / 180.0 * mu.pi();
  
    //final float hf = (float) ((hue % GMath.DEGREES_360) / GMath.DEGREES_60);
    final float hf = (float)(mu.pseudoModule(hueInRadians, mu.pi() * 2) / deg60);
  
    final int i = (int) hf; // integer part of hue
    final float f = hf - i; // fractional part of hue
  
    final float p = (1 - s) * v;
    final float q = (1 - (s * f)) * v;
    final float t = (1 - (s * (1 - f))) * v;
  
    switch (i)
    {
      case 0:
        return fromRGBA(v, t, p, alpha);
      case 1:
        return fromRGBA(q, v, p, alpha);
      case 2:
        return fromRGBA(p, v, t, alpha);
      case 3:
        return fromRGBA(p, q, v, alpha);
      case 4:
        return fromRGBA(t, p, v, alpha);
  //    case 5:
      default:
        return fromRGBA(v, p, q, alpha);
    }
  
  }

  public static Color transparent()
  {
    return Color.fromRGBA(0, 0, 0, 0);
  }

  public static Color black()
  {
    return Color.fromRGBA(0, 0, 0, 1);
  }

  public static Color white()
  {
    return Color.fromRGBA(1, 1, 1, 1);
  }

  public static Color yellow()
  {
    return Color.fromRGBA(1, 1, 0, 1);
  }

  public static Color cyan()
  {
    return Color.fromRGBA(0, 1, 1, 1);
  }

  public static Color magenta()
  {
    return Color.fromRGBA(1, 0, 1, 1);
  }

  public static Color red()
  {
    return Color.fromRGBA(1, 0, 0, 1);
  }

  public static Color green()
  {
    return Color.fromRGBA(0, 1, 0, 1);
  }

  public static Color blue()
  {
    return Color.fromRGBA(0, 0, 1, 1);
  }

  public final float getRed()
  {
    return _red;
  }

  public final float getGreen()
  {
    return _green;
  }

  public final float getBlue()
  {
    return _blue;
  }

  public final float getAlpha()
  {
    return _alpha;
  }

  public final Color mixedWith(Color that, float factor)
  {
    float frac1 = factor;
    if (frac1 < 0)
       frac1 = 0F;
    if (frac1 > 1)
       frac1 = 1F;

    final float frac2 = 1 - frac1;

    final float newRed = (getRed() * frac2) + (that.getRed() * frac1);
    final float newGreen = (getGreen() * frac2) + (that.getGreen() * frac1);
    final float newBlue = (getBlue() * frac2) + (that.getBlue() * frac1);
    final float newAlpha = (getAlpha() * frac2) + (that.getAlpha() * frac1);

    return Color.fromRGBA(newRed, newGreen, newBlue, newAlpha);
  }

  public final boolean isTransparent()
  {
    return (_alpha < 1);
  }

  public final boolean isFullTransparent()
  {
    return (_alpha < 0.01);
  }

  public final boolean isEqualsTo(Color that)
  {
    return ((_red == that._red) && (_green == that._green) && (_blue == that._blue) && (_alpha == that._alpha));
  }

  public final Color wheelStep(int wheelSize, int step)
  {
    final double stepInRadians = (IMathUtils.instance().pi() * 2) / wheelSize;
  
    final double hueInRadians = getHueInRadians() + (stepInRadians * step);
  
    return Color.fromHueSaturationBrightness(hueInRadians, getSaturation(), getBrightness(), _alpha);
  }

  public final float getSaturation()
  {
    final IMathUtils mu = IMathUtils.instance();
  
  //  const float r = _red;
  //  const float g = _green;
  //  const float b = _blue;
  
    final float max = mu.max(_red, _green, _blue);
    final float min = mu.min(_red, _green, _blue);
  
    if (max == 0)
    {
      return 0;
    }
  
    return (max - min) / max;
  }

  public final float getBrightness()
  {
    return IMathUtils.instance().max(_red, _green, _blue);
  }

  public final double getHueInRadians()
  {
    final IMathUtils mu = IMathUtils.instance();
  
    final float r = getRed();
    final float g = getGreen();
    final float b = getBlue();
  
    final float max = mu.max(r, g, b);
    final float min = mu.min(r, g, b);
  
    final float span = (max - min);
  
    if (span == 0)
    {
      return 0;
    }
  
    final double deg60 = 60.0 / 180.0 * mu.pi();
  
    double h;
    if (r == max)
    {
      h = ((g - b) / span) * deg60;
    }
    else if (g == max)
    {
      h = (deg60 * 2) + (((b - r) / span) * deg60);
    }
    else
    {
      h = (deg60 * 4) + (((r - g) / span) * deg60);
    }
  
    if (h < 0)
    {
      return (mu.pi() * 2) + h;
    }
  
    return h;
  
  }

  public final Angle getHue()
  {
    return Angle.fromRadians(getHueInRadians());
  }

}