package org.glob3.mobile.generated; 
//
//  Geodetic3D.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 31/05/12.
//  Copyright (c) 2012 IGO Software SL. All rights reserved.
//

//
//  Geodetic3D.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 31/05/12.
//  Copyright (c) 2012 IGO Software SL. All rights reserved.
//




/**
 * Class to represent a position in the globe by latitude, longitud and altitude.
 */
public class Geodetic3D
{
  private final Angle _latitude ;
  private final Angle _longitude ;
  private final double _height;


  public static Geodetic3D zero()
  {
	return new Geodetic3D(Angle.zero(), Angle.zero(), 0);
  }

  public Geodetic3D(Angle latitude, Angle longitude, double height)
  {
	  _latitude = new Angle(latitude);
	  _longitude = new Angle(longitude);
	  _height = height;
  }

  public Geodetic3D(Geodetic2D g2, double height)
  {
	  _latitude = new Angle(g2.latitude());
	  _longitude = new Angle(g2.longitude());
	  _height = height;
  }

  public Geodetic3D(Geodetic3D g)
  {
	  _latitude = new Angle(g._latitude);
	  _longitude = new Angle(g._longitude);
	  _height = g._height;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const Angle latitude() const
  public final Angle latitude()
  {
	return _latitude;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const Angle longitude() const
  public final Angle longitude()
  {
	return _longitude;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: double height() const
  public final double height()
  {
	return _height;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Geodetic2D asGeodetic2D() const
  public final Geodetic2D asGeodetic2D()
  {
	return new Geodetic2D(_latitude, _longitude);
  }

}