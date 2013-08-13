package org.glob3.mobile.generated; 
//
//  IImage.h
//  G3MiOSSDK
//
//  Created by José Miguel S N on 01/06/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//


///#include <vector>
///#include "RectangleI.hpp"
//class RectangleI;
//class IImageListener;

public abstract class IImage extends Disposable
{
  public void dispose()
  {
    super.dispose();
  }

  public abstract int getWidth();
  public abstract int getHeight();
  public abstract Vector2I getExtent();

  public abstract String description();

  public abstract IImage shallowCopy();
}