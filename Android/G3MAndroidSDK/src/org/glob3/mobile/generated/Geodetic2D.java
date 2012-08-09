package org.glob3.mobile.generated; 
//
//  Geodetic3D.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 31/05/12.
//  Copyright (c) 2012 IGO Software SL. All rights reserved.
//

//
//  Geodetic2D.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 31/05/12.
//  Copyright (c) 2012 IGO Software SL. All rights reserved.
//



/**
 * Class to represent a position in the globe by latitude, longitud and altitude.
 */
public class Geodetic2D
{
  @Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((_latitude == null) ? 0 : _latitude.hashCode());
		result = prime * result
				+ ((_longitude == null) ? 0 : _longitude.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Geodetic2D other = (Geodetic2D) obj;
		if (_latitude == null) {
			if (other._latitude != null)
				return false;
		} else if (!_latitude.equals(other._latitude))
			return false;
		if (_longitude == null) {
			if (other._longitude != null)
				return false;
		} else if (!_longitude.equals(other._longitude))
			return false;
		return true;
	}

private final Angle _latitude ;
  private final Angle _longitude ;


  public static Geodetic2D zero()
  {
	return new Geodetic2D(Angle.zero(), Angle.zero());
  }
  public static Geodetic2D fromDegrees(double lat, double lon)
  {
	return new Geodetic2D(Angle.fromDegrees(lat), Angle.fromDegrees(lon));
  }

  public Geodetic2D(Angle latitude, Angle longitude)
  {
	  _latitude = new Angle(latitude);
	  _longitude = new Angle(longitude);
  }

  public Geodetic2D(Geodetic2D g)
  {
	  _latitude = new Angle(g._latitude);
	  _longitude = new Angle(g._longitude);
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
//ORIGINAL LINE: boolean closeTo(const Geodetic2D &other) const
  public final boolean closeTo(Geodetic2D other)
  {
	if (!_latitude.closeTo(other._latitude))
	{
	  return false;
	}
  
	return _longitude.closeTo(other._longitude);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean isBetween(const Geodetic2D& min, const Geodetic2D& max) const
  public final boolean isBetween(Geodetic2D min, Geodetic2D max)
  {
	return _latitude.isBetween(min.latitude(), max.latitude()) && _longitude.isBetween(min.longitude(), max.longitude());
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean lowerThanOperator(const Geodetic2D& that) const
  public final boolean lowerThanOperator(Geodetic2D that)
  {
	if (_latitude.lowerThan(that._latitude))
	{
	  return true;
	}
	if (_latitude.greaterThan(that._latitude))
	{
	  return false;
	}
	return _longitude.lowerThan(that._longitude);
  }
}