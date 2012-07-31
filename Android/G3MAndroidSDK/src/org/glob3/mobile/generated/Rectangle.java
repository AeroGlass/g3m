package org.glob3.mobile.generated; 
//
//  Rectangle.hpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 24/07/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//


public class Rectangle
{
  public final double _x;
  public final double _y;
  public final double _width;
  public final double _height;

  public Rectangle(double x, double y, double width, double height)
  {
	  _x = x;
	  _y = y;
	  _width = width;
	  _height = height;
  }

  public Rectangle(Rectangle rec)
  {
	  _x = rec._x;
	  _y = rec._y;
	  _width = rec._width;
	  _height = rec._height;
  }



}