package org.glob3.mobile.generated; 
//
//  Frustum.cpp
//  G3MiOSSDK
//
//  Created by Agustín Trujillo Pino on 15/07/12.
//  Copyright (c) 2012 Universidad de Las Palmas. All rights reserved.
//

//
//  Frustum.h
//  G3MiOSSDK
//
//  Created by Agustín Trujillo Pino on 15/07/12.
//  Copyright (c) 2012 Universidad de Las Palmas. All rights reserved.
//



//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class Box;

public class FrustumData
{
  public double _left;
  public double _right;
  public double _bottom;
  public double _top;
  public double _znear;
  public double _zfar;

  public FrustumData(double left, double right, double bottom, double top, double znear, double zfar)
  {
	  _left = left;
	  _right = right;
	  _bottom = bottom;
	  _top = top;
	  _znear = znear;
	  _zfar = zfar;

  }

  public FrustumData()
  {
	  _left = -1;
	  _right = 1;
	  _bottom = -1;
	  _top = 1;
	  _znear = 1;
	  _zfar = 10;
  }
}