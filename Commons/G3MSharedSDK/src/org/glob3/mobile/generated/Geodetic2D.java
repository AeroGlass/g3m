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

  public final Angle latitude()
  {
    return _latitude;
  }

  public final Angle longitude()
  {
    return _longitude;
  }

  public final Geodetic2D add(Geodetic2D that)
  {
    return new Geodetic2D(_latitude.add(that._latitude), _longitude.add(that._longitude));
  }

  public final Geodetic2D sub(Geodetic2D that)
  {
    return new Geodetic2D(_latitude.sub(that._latitude), _longitude.sub(that._longitude));
  }

  public final Geodetic2D times(double magnitude)
  {
    return new Geodetic2D(_latitude.times(magnitude), _longitude.times(magnitude));
  }

  public final Geodetic2D div(double magnitude)
  {
    return new Geodetic2D(_latitude.div(magnitude), _longitude.div(magnitude));
  }

  public void dispose()
  {

  }

  public final boolean closeTo(Geodetic2D other)
  {
    if (!_latitude.closeTo(other._latitude))
    {
      return false;
    }
  
    return _longitude.closeTo(other._longitude);
  }

  public final boolean isBetween(Geodetic2D min, Geodetic2D max)
  {
    return _latitude.isBetween(min.latitude(), max.latitude()) && _longitude.isBetween(min.longitude(), max.longitude());
  }

  /**
   * Returns the (initial) bearing from this point to the supplied point
   *   see http: //williams.best.vwh.net/avform.htm#Crs
   */
  public final Angle bearingTo(Geodetic2D that)
  {
    final Angle dLon = that.longitude().sub(longitude());
    final Angle lat1 = latitude();
    final Angle lat2 = that.latitude();

    final double y = dLon.sinus() * lat2.cosinus();
    final double x = lat1.cosinus()*lat2.sinus() - lat1.sinus()*lat2.cosinus()*dLon.cosinus();
    final double radians = IMathUtils.instance().atan2(y, x);

    return Angle.fromRadians(radians);
  }


  public final boolean lowerThan(Geodetic2D that)
  {
    if (_latitude.lowerThan(that._latitude))
    {
      return true;
    }
    else if (_latitude.greaterThan(that._latitude))
    {
      return false;
    }
    return _longitude.lowerThan(that._longitude);
  }

  public final String description()
  {
    IStringBuilder isb = IStringBuilder.newStringBuilder();
    isb.addString("(lat=");
    isb.addString(_latitude.description());
    isb.addString(", lon=");
    isb.addString(_longitude.description());
    isb.addString(")");
    final String s = isb.getString();
    if (isb != null)
       isb.dispose();
    return s;
  }

  public final boolean isEqualsTo(Geodetic2D that)
  {
    return _latitude.isEqualsTo(that._latitude) && _longitude.isEqualsTo(that._longitude);
  }

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

}