package org.glob3.mobile.generated; 
//
//  FloatBufferBuilderFromGeodetic.hpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 06/09/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//




public class FloatBufferBuilderFromGeodetic extends FloatBufferBuilder {

  private final int _centerStrategy;
  private float _cx;
  private float _cy;
  private float _cz;

  private void setCenter(Vector3D center) {
    _cx = (float) center._x;
    _cy = (float) center._y;
    _cz = (float) center._z;
  }

  private Planet _planet; // REMOVED FINAL WORD BY CONVERSOR RULE


  public FloatBufferBuilderFromGeodetic(int centerStrategy, Planet planet, Vector3D center) {
     _planet = planet;
     _centerStrategy = centerStrategy;
    setCenter(center);
  }

  public FloatBufferBuilderFromGeodetic(int centerStrategy, Planet planet, Geodetic2D center) {
     _planet = planet;
     _centerStrategy = centerStrategy;
    setCenter(_planet.toCartesian(center));
  }

  public FloatBufferBuilderFromGeodetic(int centerStrategy, Planet planet, Geodetic3D center) {
     _planet = planet;
     _centerStrategy = centerStrategy;
    setCenter(_planet.toCartesian(center));
  }

  public final void add(Geodetic3D position) {
    final Vector3D vector = _planet.toCartesian(position);

    if (_centerStrategy == CenterStrategy.firstVertex() && _values.size() == 0) {
      setCenter(vector);
    }

    float x = (float) vector._x;
    float y = (float) vector._y;
    float z = (float) vector._z;
    if (_centerStrategy != CenterStrategy.noCenter()) {
      x -= _cx;
      y -= _cy;
      z -= _cz;
    }

    _values.add(x);
    _values.add(y);
    _values.add(z);
  }

  public final void add(Geodetic2D position) {
    final Vector3D vector = _planet.toCartesian(position);

    if (_centerStrategy == CenterStrategy.firstVertex() && _values.size() == 0) {
      setCenter(vector);
    }

    float x = (float) vector._x;
    float y = (float) vector._y;
    float z = (float) vector._z;
    if (_centerStrategy != CenterStrategy.noCenter()) {
      x -= _cx;
      y -= _cy;
      z -= _cz;
    }

    _values.add(x);
    _values.add(y);
    _values.add(z);
  }

  public final Vector3D getCenter() {
    return new Vector3D(_cx, _cy, _cz);
  }
}