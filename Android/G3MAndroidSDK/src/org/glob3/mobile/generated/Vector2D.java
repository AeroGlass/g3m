package org.glob3.mobile.generated; 
//
//  Vector2D.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 31/05/12.
//  Copyright (c) 2012 IGO Software SL. All rights reserved.
//

//
//  Vector2D.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 31/05/12.
//  Copyright (c) 2012 IGO Software SL. All rights reserved.
//




//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class MutableVector2D;

public class Vector2D
{
  private final double _x;
  private final double _y;


//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//	Vector2D operator =(Vector2D v);


  public Vector2D(double x, double y)
  {
	  _x = x;
	  _y = y;

  }

  public Vector2D(Vector2D v)
  {
	  _x = v.x();
	  _y = v.y();

  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Vector2D normalized() const;
//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//  Vector2D normalized();

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: double length() const
  public final double length()
  {
	return *IMathUtils.instance().sqrt(squaredLength());
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Angle orientation() const
  public final Angle orientation()
  {
	  return Angle.fromRadians(*IMathUtils.instance().atan2(_y, _x));
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: double squaredLength() const
  public final double squaredLength()
  {
	return _x * _x + _y * _y;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Vector2D add(const Vector2D& v) const
  public final Vector2D add(Vector2D v)
  {
	return new Vector2D(_x + v._x, _y + v._y);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Vector2D sub(const Vector2D& v) const
  public final Vector2D sub(Vector2D v)
  {
	return new Vector2D(_x - v._x, _y - v._y);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Vector2D times(const Vector2D& v) const
  public final Vector2D times(Vector2D v)
  {
	return new Vector2D(_x * v._x, _y * v._y);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Vector2D times(const double magnitude) const
  public final Vector2D times(double magnitude)
  {
	return new Vector2D(_x * magnitude, _y * magnitude);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Vector2D div(const Vector2D& v) const
  public final Vector2D div(Vector2D v)
  {
	return new Vector2D(_x / v._x, _y / v._y);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Vector2D div(const double v) const
  public final Vector2D div(double v)
  {
	return new Vector2D(_x / v, _y / v);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Angle angle() const
  public final Angle angle()
  {
	double a = *IMathUtils.instance().atan2(_y, _x);
	return Angle.fromRadians(a);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: double x() const
  public final double x()
  {
	return _x;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: double y() const
  public final double y()
  {
	return _y;
  }

  public static Vector2D nan()
  {
	return new Vector2D(*IMathUtils.instance().NanD(), *IMathUtils.instance().NanD());
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: double maxAxis() const
  public final double maxAxis()
  {
	return (_x >= _y) ? _x : _y;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: double minAxis() const
  public final double minAxis()
  {
	return (_x <= _y) ? _x : _y;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: MutableVector2D asMutableVector2D() const
  public final MutableVector2D asMutableVector2D()
  {
	return new MutableVector2D(_x, _y);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean isNan() const
  public final boolean isNan()
  {
	return *IMathUtils.instance().isNan(_x * _y);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const String description() const
  public final String description()
  {
	IStringBuilder isb = IStringBuilder.newStringBuilder();
	isb.add("(V2D ").add(_x).add(", ").add(_y).add(")");
	String s = isb.getString();
	if (isb != null)
		isb.dispose();
	return s;
  }

}