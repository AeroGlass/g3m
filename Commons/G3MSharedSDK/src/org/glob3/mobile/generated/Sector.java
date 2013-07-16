package org.glob3.mobile.generated; 
//
//  Sector.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 22/06/12.
//  Copyright (c) 2012 IGO Software SL. All rights reserved.
//

//
//  Sector.hpp
//  G3MiOSSDK
//
//  Created by Agustín Trujillo Pino on 12/06/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//




//class Sector_Geodetic2DCachedData;

public class Sector
{

  private final Geodetic2D _lower ;
  private final Geodetic2D _upper ;
  private final Geodetic2D _center ;

  private final Angle _deltaLatitude ;
  private final Angle _deltaLongitude ;

  // this lazy value represent the half diagonal of the sector, measured in radians
  // it's stored in double instead of Angle class to optimize performance in android
  // this value is only used in the method Sector::isBackOriented
  private double _deltaRadiusInRadians;

  //const Geodetic2D getClosestPoint(const Geodetic2D& pos) const;

  /*
  // cached values for speed up in isBackOriented()
  mutable Sector_Geodetic2DCachedData* _nwData;
  mutable Sector_Geodetic2DCachedData* _neData;
  mutable Sector_Geodetic2DCachedData* _swData;
  mutable Sector_Geodetic2DCachedData* _seData;
   */

  private Vector3D _normalizedCartesianCenter;



  /*
  class Sector_Geodetic2DCachedData {
  private:
    const Vector3D _cartesian;
    const Vector3D _geodeticSurfaceNormal;
  
  public:
  
    Sector_Geodetic2DCachedData(const Planet* planet,
                                const Geodetic2D position) :
    _cartesian( planet->toCartesian(position) ),
    _geodeticSurfaceNormal( planet->geodeticSurfaceNormal(_cartesian) )
    {
    }
  
    ~Sector_Geodetic2DCachedData() {
    }
  
    bool test(const Vector3D& eye) const {
      return _geodeticSurfaceNormal.dot( eye.sub(_cartesian) ) > 0;
    }
  };*/
  
  public void dispose()
  {
    /*
     delete _nwData;
    delete _neData;
    delete _swData;
    delete _seData;
     */
    if (_normalizedCartesianCenter != null)
       if (_normalizedCartesianCenter != null)
          _normalizedCartesianCenter.dispose();
  }

  public Sector(Geodetic2D lower, Geodetic2D upper)
  /*
   _nwData(NULL),
  _neData(NULL),
  _swData(NULL),
  _seData(NULL),
   */
  {
     _lower = new Geodetic2D(lower);
     _upper = new Geodetic2D(upper);
     _deltaLatitude = new Angle(upper.latitude().sub(lower.latitude()));
     _deltaLongitude = new Angle(upper.longitude().sub(lower.longitude()));
     _center = new Geodetic2D(Angle.midAngle(lower.latitude(), upper.latitude()), Angle.midAngle(lower.longitude(), upper.longitude()));
     _deltaRadiusInRadians = -1.0;
     _normalizedCartesianCenter = null;
  }


  public Sector(Sector sector)
  /*
   _nwData(NULL),
  _neData(NULL),
  _swData(NULL),
  _seData(NULL),
   */
  {
     _lower = new Geodetic2D(sector._lower);
     _upper = new Geodetic2D(sector._upper);
     _deltaLatitude = new Angle(sector._deltaLatitude);
     _deltaLongitude = new Angle(sector._deltaLongitude);
     _center = new Geodetic2D(sector._center);
     _deltaRadiusInRadians = sector._deltaRadiusInRadians;
    if (sector._normalizedCartesianCenter == null)
      _normalizedCartesianCenter = null;
    else
    {
      final Vector3D normalizedCartesianCenter = sector._normalizedCartesianCenter;
      _normalizedCartesianCenter = new Vector3D(normalizedCartesianCenter);
    }

  }

  public static Sector fromDegrees(double minLat, double minLon, double maxLat, double maxLon)
  {
    final Geodetic2D lower = new Geodetic2D(Angle.fromDegrees(minLat), Angle.fromDegrees(minLon));
    final Geodetic2D upper = new Geodetic2D(Angle.fromDegrees(maxLat), Angle.fromDegrees(maxLon));

    return new Sector(lower, upper);
  }

  public final Vector2D div(Sector that)
  {
    final double scaleX = _deltaLongitude.div(that._deltaLongitude);
    final double scaleY = _deltaLatitude.div(that._deltaLatitude);
    return new Vector2D(scaleX, scaleY);
  }

//  Vector2D getTranslationFactor(const Sector& that) const;

  public final boolean fullContains(Sector s)
  {
    return contains(s.upper()) && contains(s.lower());
  }


  /*
  bool Sector::isBackOriented_v4(const G3MRenderContext *rc,
                              double minHeight) const {
    const Camera* camera = rc->getCurrentCamera();
    const Planet* planet = rc->getPlanet();
  
    // compute angle with normals in the four corners
    const Vector3D eye = camera->getCartesianPosition();
  
    if (_nwData == NULL)    { _nwData = new Sector_Geodetic2DCachedData(planet, getNW()); }
    if (_nwData->test(eye)) { return false; }
  
    if (_neData == NULL)    { _neData = new Sector_Geodetic2DCachedData(planet, getNE()); }
    if (_neData->test(eye)) { return false; }
  
    if (_swData == NULL)    { _swData = new Sector_Geodetic2DCachedData(planet, getSW()); }
    if (_swData->test(eye)) { return false; }
  
    if (_seData == NULL)    { _seData = new Sector_Geodetic2DCachedData(planet, getSE()); }
    if (_seData->test(eye)) { return false; }
  
    
    //const Vector3D cartesianNW = planet->toCartesian(getNW());
    //if (planet->geodeticSurfaceNormal(cartesianNW).dot(eye.sub(cartesianNW)) > 0) { return false; }
  
    //const Vector3D cartesianNE = planet->toCartesian(getNE());
    //if (planet->geodeticSurfaceNormal(cartesianNE).dot(eye.sub(cartesianNE)) > 0) { return false; }
  
    //const Vector3D cartesianSW = planet->toCartesian(getSW());
    //if (planet->geodeticSurfaceNormal(cartesianSW).dot(eye.sub(cartesianSW)) > 0) { return false; }
  
    //const Vector3D cartesianSE = planet->toCartesian(getSE());
    //if (planet->geodeticSurfaceNormal(cartesianSE).dot(eye.sub(cartesianSE)) > 0) { return false; }
  
    // compute angle with normal in the closest point to the camera
    const Geodetic2D center = camera->getGeodeticCenterOfView().asGeodetic2D();
  
    const Vector3D cartesianCenter = planet->toCartesian(getClosestPoint(center), minHeight);
  
    // if all the angles are higher than 90, sector is back oriented
    return (planet->geodeticSurfaceNormal(cartesianCenter).dot(eye.sub(cartesianCenter)) <= 0);
  }*/
  
  
  /*
  bool Sector::isBackOriented_v3(const G3MRenderContext *rc, double height) const {
    const Camera* camera = rc->getCurrentCamera();
    const Planet* planet = rc->getPlanet();
    
    // compute sector point nearest to camera centerPoint
    const Geodetic2D center = camera->getGeodeticCenterOfView().asGeodetic2D();
    const Vector3D point    = planet->toCartesian(Geodetic3D(getClosestPoint(center), height));
    
    // compute angle between normals
    const Vector3D eye = camera->getCartesianPosition();
    if (planet->geodeticSurfaceNormal(point).dot(eye.sub(point)) > 0)
      return false;
    
    // if sector touches north pole, also test if pole is visible
    if (touchesNorthPole()) {
      Vector3D pole = planet->toCartesian(Geodetic3D(Angle::fromDegrees(90), Angle::fromDegrees(0), 0));
      if (planet->geodeticSurfaceNormal(pole).dot(eye.sub(pole)) > 0)
        return false;
    }
    
    // if sector touches north pole, also test if pole is visible
    if (touchesSouthPole()) {
      Vector3D pole = planet->toCartesian(Geodetic3D(Angle::fromDegrees(-90), Angle::fromDegrees(0), 0));
      if (planet->geodeticSurfaceNormal(pole).dot(eye.sub(pole)) > 0)
        return false;
    }
    
    return true;
  }
  */
  /*
  bool Sector::isBackOriented_v2(const G3MRenderContext *rc, double height) const {
    const Camera* camera = rc->getCurrentCamera();
    const Planet* planet = rc->getPlanet();
    
    // compute sector point nearest to camera centerPoint
    const Geodetic2D center = camera->getGeodeticCenterOfView().asGeodetic2D();
    const Vector3D point    = planet->toCartesian(Geodetic3D(getClosestPoint(center), height));
    
    // compute angle between normals
    const Vector3D eye = camera->getCartesianPosition();
    return (planet->geodeticSurfaceNormal(point).dot(eye.sub(point)) <= 0);
  }*/
  
  
  /*
  bool Sector::isBackOriented_v1(const G3MRenderContext *rc) const {
    const Camera* camera = rc->getCurrentCamera();
    const Planet* planet = rc->getPlanet();
    
    // compute sector point nearest to centerPoint
    const Geodetic2D center = camera->getGeodeticCenterOfView().asGeodetic2D();
    const Geodetic2D point = getClosestPoint(center);
    
    // compute angle between normals
    const Vector3D normal = planet->geodeticSurfaceNormal(point);
    const Vector3D view   = camera->getViewDirection().times(-1);
    const double dot = normal.dot(view);
    
    return (dot < 0) ? true : false;
  }
  */
  
  /*
  bool Sector::isBackOriented(const G3MRenderContext *rc, double height) const {
    const Camera* camera = rc->getCurrentCamera();
    const Planet* planet = rc->getPlanet();
    const Vector3D view = camera->getViewDirection().times(-1);
  
    // if all the corners normals are back oriented, sector is back oriented
    if (planet->geodeticSurfaceNormal(getNE()).dot(view) > 0) { return false; }
    if (planet->geodeticSurfaceNormal(getNW()).dot(view) > 0) { return false; }
    if (planet->geodeticSurfaceNormal(getSE()).dot(view) > 0) { return false; }
    if (planet->geodeticSurfaceNormal(getSW()).dot(view) > 0) { return false; }
    return true;
  }*/
  
  
  /*
  bool Sector::isBackOriented(const G3MRenderContext *rc, double height) const {
    const Planet*  planet = rc->getPlanet();
    const Vector3D eye = rc->getCurrentCamera()->getCartesianPosition();
    
    // if all the corners normals are back oriented, sector is back oriented
    const Vector3D cartesianNE = planet->toCartesian(Geodetic3D(getNE(), height));
    if (planet->geodeticSurfaceNormal(cartesianNE).dot(eye.sub(cartesianNE)) > 0) { return false; }
  
    const Vector3D cartesianNW = planet->toCartesian(Geodetic3D(getNW(), height));
    if (planet->geodeticSurfaceNormal(cartesianNW).dot(eye.sub(cartesianNW)) > 0) { return false; }
  
    const Vector3D cartesianSE = planet->toCartesian(Geodetic3D(getSE(), height));
    if (planet->geodeticSurfaceNormal(cartesianSE).dot(eye.sub(cartesianSE)) > 0) { return false; }
  
    const Vector3D cartesianSW = planet->toCartesian(Geodetic3D(getSW(), height));
    if (planet->geodeticSurfaceNormal(cartesianSW).dot(eye.sub(cartesianSW)) > 0) { return false; }
    
    return true;
  }
  */
  
  public final Sector intersection(Sector that)
  {
    final Angle lowLat = Angle.max(lower().latitude(), that.lower().latitude());
    final Angle lowLon = Angle.max(lower().longitude(), that.lower().longitude());
    final Geodetic2D low = new Geodetic2D(lowLat, lowLon);
  
    final Angle upLat = Angle.min(upper().latitude(), that.upper().latitude());
    final Angle upLon = Angle.min(upper().longitude(), that.upper().longitude());
    final Geodetic2D up = new Geodetic2D(upLat, upLon);
  
    return new Sector(low, up);
  }

  public final Sector mergedWith(Sector that)
  {
    final Angle lowLat = Angle.min(lower().latitude(), that.lower().latitude());
    final Angle lowLon = Angle.min(lower().longitude(), that.lower().longitude());
    final Geodetic2D low = new Geodetic2D(lowLat, lowLon);
  
    final Angle upLat = Angle.max(upper().latitude(), that.upper().latitude());
    final Angle upLon = Angle.max(upper().longitude(), that.upper().longitude());
    final Geodetic2D up = new Geodetic2D(upLat, upLon);
  
    return new Sector(low, up);
  }

  public static Sector fullSphere()
  {
    return new Sector(new Geodetic2D(Angle.fromDegrees(-90), Angle.fromDegrees(-180)), new Geodetic2D(Angle.fromDegrees(90), Angle.fromDegrees(180)));
  }

  public final Geodetic2D lower()
  {
    return _lower;
  }

  public final Angle lowerLatitude()
  {
    return _lower.latitude();
  }

  public final Angle lowerLongitude()
  {
    return _lower.longitude();
  }

  public final Geodetic2D upper()
  {
    return _upper;
  }

  public final Angle upperLatitude()
  {
    return _upper.latitude();
  }

  public final Angle upperLongitude()
  {
    return _upper.longitude();
  }


  public final boolean contains(Angle latitude, Angle longitude)
  {
    return latitude.isBetween(_lower.latitude(), _upper.latitude()) && longitude.isBetween(_lower.longitude(), _upper.longitude());
  }

  public final boolean contains(Geodetic2D position)
  {
    return contains(position.latitude(), position.longitude());
  }

  public final boolean contains(Geodetic3D position)
  {
    return contains(position.latitude(), position.longitude());
  }

  public final boolean touchesWith(Sector that)
  {
    // from Real-Time Collision Detection - Christer Ericson
    //   page 79
  
    // Exit with no intersection if separated along an axis
    if (_upper.latitude().lowerThan(that._lower.latitude()) || _lower.latitude().greaterThan(that._upper.latitude()))
    {
      return false;
    }
    if (_upper.longitude().lowerThan(that._lower.longitude()) || _lower.longitude().greaterThan(that._upper.longitude()))
    {
      return false;
    }
  
    // Overlapping on all axes means Sectors are intersecting
    return true;
  }

  public final Angle getDeltaLatitude()
  {
    return _deltaLatitude;
  }

  public final Angle getDeltaLongitude()
  {
    return _deltaLongitude;
  }

  public final Geodetic2D getSW()
  {
    return _lower;
  }

  public final Geodetic2D getNE()
  {
    return _upper;
  }

  public final Geodetic2D getNW()
  {
    return new Geodetic2D(_upper.latitude(), _lower.longitude());
  }

  public final Geodetic2D getSE()
  {
    return new Geodetic2D(_lower.latitude(), _upper.longitude());
  }

  public final Geodetic2D getCenter()
  {
    return _center;
  }

  // (u,v) are similar to texture coordinates inside the Sector
  // (u,v)=(0,0) in NW point, and (1,1) in SE point

  // (u,v) are similar to texture coordinates inside the Sector
  // (u,v)=(0,0) in NW point, and (1,1) in SE point
  public final Geodetic2D getInnerPoint(double u, double v)
  {
    return new Geodetic2D(Angle.linearInterpolation(_lower.latitude(), _upper.latitude(), 1.0 - v), Angle.linearInterpolation(_lower.longitude(), _upper.longitude(), u));
  }

  public final Angle getInnerPointLongitude(double u)
  {
    return Angle.linearInterpolation(_lower.longitude(), _upper.longitude(), u);
  }
  public final Angle getInnerPointLatitude(double v)
  {
    return Angle.linearInterpolation(_lower.latitude(), _upper.latitude(), (float)(1.0-v));
  }


  public final Vector2D getUVCoordinates(Geodetic2D point)
  {
    return getUVCoordinates(point.latitude(), point.longitude());
  }

  public final Vector2D getUVCoordinates(Angle latitude, Angle longitude)
  {
    return new Vector2D(getUCoordinate(longitude), getVCoordinate(latitude));
  }

  public final double getUCoordinate(Angle longitude)
  {
    return (longitude._radians - _lower.longitude()._radians) / _deltaLongitude._radians;
  }

  public final double getVCoordinate(Angle latitude)
  {
    return (_upper.latitude()._radians - latitude._radians) / _deltaLatitude._radians;
  }


  public final boolean isBackOriented(G3MRenderContext rc, double minHeight, Planet planet, Vector3D cameraNormalizedPosition, double cameraAngle2HorizonInRadians)
  {
  //  const Camera* camera = rc->getCurrentCamera();
  //  const Planet* planet = rc->getPlanet();
  //
  //  const double dot = camera->getNormalizedPosition().dot(getNormalizedCartesianCenter(planet));
  //  const double angleInRadians = IMathUtils::instance()->acos(dot);
  //
  //  return ( (angleInRadians - getDeltaRadiusInRadians()) > camera->getAngle2HorizonInRadians() );
  
    final double dot = cameraNormalizedPosition.dot(getNormalizedCartesianCenter(planet));
    final double angleInRadians = IMathUtils.instance().acos(dot);
  
    return ((angleInRadians - getDeltaRadiusInRadians()) > cameraAngle2HorizonInRadians);
  }

  public final Geodetic2D clamp(Geodetic2D position)
  {
    if (contains(position))
    {
      return position;
    }
  
    double latitudeInDegrees = position.latitude().degrees();
    double longitudeInDegrees = position.longitude().degrees();
  
    final double upperLatitudeInDegrees = _upper.latitude().degrees();
    if (latitudeInDegrees > upperLatitudeInDegrees)
    {
      latitudeInDegrees = upperLatitudeInDegrees;
    }
  
    final double upperLongitudeInDegrees = _upper.longitude().degrees();
    if (longitudeInDegrees > upperLongitudeInDegrees)
    {
      longitudeInDegrees = upperLongitudeInDegrees;
    }
  
    final double lowerLatitudeInDegrees = _lower.latitude().degrees();
    if (latitudeInDegrees < lowerLatitudeInDegrees)
    {
      latitudeInDegrees = lowerLatitudeInDegrees;
    }
  
    final double lowerLongitudeInDegrees = _lower.longitude().degrees();
    if (longitudeInDegrees < lowerLongitudeInDegrees)
    {
      longitudeInDegrees = lowerLongitudeInDegrees;
    }
  
    return Geodetic2D.fromDegrees(latitudeInDegrees, longitudeInDegrees);
  }


  /*
  const Geodetic2D Sector::getClosestPoint(const Geodetic2D& pos) const {
    // if pos is included, return pos
    if (contains(pos)) {
      return pos;
    }
  
    // test longitude
    Geodetic2D center = getCenter();
    double lon        = pos.longitude()._degrees;
    double centerLon  = center.longitude()._degrees;
    double oppLon1    = centerLon - 180;
    double oppLon2    = centerLon + 180;
    if (lon<oppLon1)
      lon+=360;
    if (lon>oppLon2)
      lon-=360;
    double minLon     = _lower.longitude()._degrees;
    double maxLon     = _upper.longitude()._degrees;
    //bool insideLon    = true;
    if (lon < minLon) {
      lon = minLon;
      //insideLon = false;
    }
    if (lon > maxLon) {
      lon = maxLon;
      //insideLon = false;
    }
  
    // test latitude
    double lat        = pos.latitude()._degrees;
    double minLat     = _lower.latitude()._degrees;
    double maxLat     = _upper.latitude()._degrees;
    //bool insideLat    = true;
    if (lat < minLat) {
      lat = minLat;
      //insideLat = false;
    }
    if (lat > maxLat) {
      lat = maxLat;
      //insideLat = false;
    }
  
    return Geodetic2D(Angle::fromDegrees(lat), Angle::fromDegrees(lon));
   */
  
    /* // here we have to handle the case where sector is close to the pole,
    // and the latitude of the other point must be seen from the other side
    Geodetic2D point(Angle::fromDegrees(lat), Angle::fromDegrees(lon));
    if (touchesNorthPole()) {
      Geodetic2D pole(Angle::fromDegrees(90), Angle::fromDegrees(0));
      Angle angle1 = pos.angleTo(point);
      Angle angle2 = pos.angleTo(pole);
      if (angle1.greaterThan(angle2))
        return pole;
    }
    if (touchesSouthPole()) {
      Geodetic2D pole(Angle::fromDegrees(-90), Angle::fromDegrees(0));
      Angle angle1 = pos.angleTo(point);
      Angle angle2 = pos.angleTo(pole);
      if (angle1.greaterThan(angle2))
        return pole;
    }
    return point;*/
  
    /*
     const Angle lat = pos.latitude().nearestAngleInInterval(_lower.latitude(), _upper.latitude());
     const Angle lon = pos.longitude().nearestAngleInInterval(_lower.longitude(), _upper.longitude());
     return Geodetic2D(lat, lon);*/
  //}
  
  public final String description()
  {
    IStringBuilder isb = IStringBuilder.newStringBuilder();
    isb.addString("(Sector ");
    isb.addString(_lower.description());
    isb.addString(" - ");
    isb.addString(_upper.description());
    isb.addString(")");
    final String s = isb.getString();
    if (isb != null)
       isb.dispose();
    return s;
  }

  public final Sector shrinkedByPercentP(float percent)
  {
    final Angle deltaLatitude = _deltaLatitude.times(percent).div(2);
    final Angle deltaLongitude = _deltaLongitude.times(percent).div(2);

    final Geodetic2D delta = new Geodetic2D(deltaLatitude, deltaLongitude);

    return new Sector(_lower.add(delta), _upper.sub(delta));
  }

  public final Sector shrinkedByPercent(float percent)
  {
    final Angle deltaLatitude = _deltaLatitude.times(percent).div(2);
    final Angle deltaLongitude = _deltaLongitude.times(percent).div(2);

    final Geodetic2D delta = new Geodetic2D(deltaLatitude, deltaLongitude);

    return new Sector(_lower.add(delta), _upper.sub(delta));
  }

  public final boolean isEqualsTo(Sector that)
  {
    return _lower.isEqualsTo(that._lower) && _upper.isEqualsTo(that._upper);
  }

  public final boolean touchesNorthPole()
  {
    return (_upper.latitude()._degrees >= 89.9);
  }

  public final boolean touchesSouthPole()
  {
    return (_lower.latitude()._degrees <= -89.9);
  }

  public final boolean touchesPoles()
  {
    return ((_upper.latitude()._degrees >= 89.9) || (_lower.latitude()._degrees <= -89.9));
  }

  public final double getDeltaRadiusInRadians()
  {
    if (_deltaRadiusInRadians < 0)
      _deltaRadiusInRadians = IMathUtils.instance().sqrt(_deltaLatitude.radians() * _deltaLatitude.radians() + _deltaLongitude.radians() * _deltaLongitude.radians()) * 0.5;
    return _deltaRadiusInRadians;
  }


  //Vector2D Sector::getTranslationFactor(const Sector& that) const {
  //  const Vector2D uv = that.getUVCoordinates(_lower);
  //  const double scaleY = _deltaLatitude.div(that._deltaLatitude);
  //  return Vector2D(uv._x, uv._y - scaleY);
  //}
  
  public final Vector3D getNormalizedCartesianCenter(Planet planet)
  {
    if (_normalizedCartesianCenter == null)
    {
      _normalizedCartesianCenter = new Vector3D(planet.toCartesian(_center).normalized());
    }
    return _normalizedCartesianCenter;
  }


  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = (prime * result) + ((_lower == null) ? 0 : _lower.hashCode());
    result = (prime * result) + ((_upper == null) ? 0 : _upper.hashCode());
    return result;
  }


  @Override
  public boolean equals(final Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final Sector other = (Sector) obj;
    if (_lower == null) {
      if (other._lower != null) {
        return false;
      }
    }
    else if (!_lower.equals(other._lower)) {
      return false;
    }
    if (_upper == null) {
      if (other._upper != null) {
        return false;
      }
    }
    else if (!_upper.equals(other._upper)) {
      return false;
    }
    return true;
  }

}