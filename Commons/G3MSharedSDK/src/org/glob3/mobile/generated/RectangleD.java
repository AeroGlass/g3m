package org.glob3.mobile.generated; 
//
//  RectangleD.hpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 24/07/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//


public class RectangleD
{
  public final double _x;
  public final double _y;
  public final double _width;
  public final double _height;

  public RectangleD(double x, double y, double width, double height)
  {
	  _x = x;
	  _y = y;
	  _width = width;
	  _height = height;
  }

  public RectangleD(RectangleD that)
  {
	  _x = that._x;
	  _y = that._y;
	  _width = that._width;
	  _height = that._height;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean equalTo(const RectangleD& that) const
  public final boolean equalTo(RectangleD that)
  {
	return (_x == that._x) && (_y == that._y) && (_width == that._width) && (_height == that._height);
  }

}