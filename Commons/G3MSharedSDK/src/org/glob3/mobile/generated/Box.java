package org.glob3.mobile.generated; 
//
//  Box.cpp
//  G3MiOSSDK
//
//  Created by Agustín Trujillo Pino on 17/07/12.
//  Copyright (c) 2012 Universidad de Las Palmas. All rights reserved.
//

//
//  Box.h
//  G3MiOSSDK
//
//  Created by Agustín Trujillo Pino on 16/07/12.
//  Copyright (c) 2012 Universidad de Las Palmas. All rights reserved.
//




//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class Vector2D;


public class Box extends Extent
{

  public Box(Vector3D lower, Vector3D upper)
  {
	  _lower = new Vector3D(lower);
	  _upper = new Vector3D(upper);
	  _mesh = null;
  }

  public void dispose()
  {
	if (_mesh != null)
		_mesh.dispose();
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean touches(const Frustum* frustum) const
  public final boolean touches(Frustum frustum)
  {
	return frustum.touchesWithBox(this);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Vector3D getLower() const
  public final Vector3D getLower()
  {
	  return _lower;
  }
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Vector3D getUpper() const
  public final Vector3D getUpper()
  {
	  return _upper;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const java.util.ArrayList<Vector3D> getCorners() const
  public final java.util.ArrayList<Vector3D> getCorners()
  {
	if (_corners == null) {
  	_corners = new java.util.ArrayList<Vector3D>(8);
  
  	_corners.add(_lower);
  	_corners.add(new Vector3D(_lower._x, _lower._y, _upper._z));
  	_corners.add(new Vector3D(_lower._x, _upper._y, _lower._z));
  	_corners.add(new Vector3D(_lower._x, _upper._y, _upper._z));
  	_corners.add(new Vector3D(_upper._x, _lower._y, _lower._z));
  	_corners.add(new Vector3D(_upper._x, _lower._y, _upper._z));
  	_corners.add(new Vector3D(_upper._x, _upper._y, _lower._z));
  	_corners.add(_upper);
	}
	return _corners;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: double squaredProjectedArea(const G3MRenderContext* rc) const
  public final double squaredProjectedArea(G3MRenderContext rc)
  {
	final Vector2I extent = projectedExtent(rc);
	return extent._x * extent._y;
  }
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Vector2I projectedExtent(const G3MRenderContext *rc) const
  public final Vector2I projectedExtent(G3MRenderContext rc)
  {
	final java.util.ArrayList<Vector3D> corners = getCorners();
  
	final Camera currentCamera = rc.getCurrentCamera();
  
	final Vector2I pixel0 = currentCamera.point2Pixel(corners.get(0));
  
	int lowerX = pixel0._x;
	int upperX = pixel0._x;
	int lowerY = pixel0._y;
	int upperY = pixel0._y;
  
	final int cornersSize = corners.size();
	for (int i = 1; i < cornersSize; i++)
	{
	  final Vector2I pixel = currentCamera.point2Pixel(corners.get(i));
  
	  final int x = pixel._x;
	  final int y = pixel._y;
  
	  if (x < lowerX)
	  {
		  lowerX = x;
	  }
	  if (y < lowerY)
	  {
		  lowerY = y;
	  }
  
	  if (x > upperX)
	  {
		  upperX = x;
	  }
	  if (y > upperY)
	  {
		  upperY = y;
	  }
	}
  
	final int width = upperX - lowerX;
	final int height = upperY - lowerY;
  
	return new Vector2I(width, height);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean contains(const Vector3D& p) const
  public final boolean contains(Vector3D p)
  {
	final double margin = 1e-3;
	if (p._x < _lower._x - margin)
		return false;
	if (p._x > _upper._x + margin)
		return false;
  
	if (p._y < _lower._y - margin)
		return false;
	if (p._y > _upper._y + margin)
		return false;
  
	if (p._z < _lower._z - margin)
		return false;
	if (p._z > _upper._z + margin)
		return false;
  
	return true;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Vector3D intersectionWithRay(const Vector3D& origin, const Vector3D& direction) const
  public final Vector3D intersectionWithRay(Vector3D origin, Vector3D direction)
  {
	//MIN X
	{
	  Plane p = new Plane(new Vector3D(1.0, 0.0, 0.0), _lower._x);
	  Vector3D inter = p.intersectionWithRay(origin, direction);
	  if (!inter.isNan() && contains(inter))
		  return inter;
	}
  
	//MAX X
	{
	  Plane p = new Plane(new Vector3D(1.0, 0.0, 0.0), _upper._x);
	  Vector3D inter = p.intersectionWithRay(origin, direction);
	  if (!inter.isNan() && contains(inter))
		  return inter;
	}
  
	//MIN Y
	{
	  Plane p = new Plane(new Vector3D(0.0, 1.0, 0.0), _lower._y);
	  Vector3D inter = p.intersectionWithRay(origin, direction);
	  if (!inter.isNan() && contains(inter))
		  return inter;
	}
  
	//MAX Y
	{
	  Plane p = new Plane(new Vector3D(0.0, 1.0, 0.0), _upper._y);
	  Vector3D inter = p.intersectionWithRay(origin, direction);
	  if (!inter.isNan() && contains(inter))
		  return inter;
	}
  
	//MIN Z
	{
	  Plane p = new Plane(new Vector3D(0.0, 0.0, 1.0), _lower._z);
	  Vector3D inter = p.intersectionWithRay(origin, direction);
	  if (!inter.isNan() && contains(inter))
		  return inter;
	}
  
	//MAX Z
	{
	  Plane p = new Plane(new Vector3D(0.0, 0.0, 1.0), _upper._z);
	  Vector3D inter = p.intersectionWithRay(origin, direction);
	  if (!inter.isNan() && contains(inter))
		  return inter;
	}
  
	return Vector3D.nan();
  }

  public final void render(G3MRenderContext rc, GLState parentState)
  {
	if (_mesh == null)
	{
	  createMesh(Color.newFromRGBA((float)1.0, (float)1.0, (float)0.0, (float)1.0));
	}
	_mesh.render(rc, parentState);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean touchesBox(const Box* box) const
  public final boolean touchesBox(Box box)
  {
	if (_lower._x > box._upper._x)
		return false;
	if (_upper._x < box._lower._x)
		return false;
	if (_lower._y > box._upper._y)
		return false;
	if (_upper._y < box._lower._y)
		return false;
	if (_lower._z > box._upper._z)
		return false;
	if (_upper._z < box._lower._z)
		return false;
	return true;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Extent* mergedWith(const Extent* that) const
  public final Extent mergedWith(Extent that)
  {
	if (that == null)
	{
	  return null;
	}
	return that.mergedWithBox(this);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Extent* mergedWithBox(const Box* that) const
  public final Extent mergedWithBox(Box that)
  {
	final double lowerX = IMathUtils.instance().min(_lower._x, that._lower._x);
	final double lowerY = IMathUtils.instance().min(_lower._y, that._lower._y);
	final double lowerZ = IMathUtils.instance().min(_lower._z, that._lower._z);
  
	final double upperX = IMathUtils.instance().max(_upper._x, that._upper._x);
	final double upperY = IMathUtils.instance().max(_upper._y, that._upper._y);
	final double upperZ = IMathUtils.instance().max(_upper._z, that._upper._z);
  
	return new Box(new Vector3D(lowerX, lowerY, lowerZ), new Vector3D(upperX, upperY, upperZ));
  }


  private final Vector3D _lower ;
  private final Vector3D _upper ;

  private java.util.ArrayList<Vector3D> _corners = null; // cache for getCorners() method

  private Mesh _mesh;
  private void createMesh(Color color)
  {
  
	float[] v = { (float) _lower._x, (float) _lower._y, (float) _lower._z, (float) _lower._x, (float) _upper._y, (float) _lower._z, (float) _lower._x, (float) _upper._y, (float) _upper._z, (float) _lower._x, (float) _lower._y, (float) _upper._z, (float) _upper._x, (float) _lower._y, (float) _lower._z, (float) _upper._x, (float) _upper._y, (float) _lower._z, (float) _upper._x, (float) _upper._y, (float) _upper._z, (float) _upper._x, (float) _lower._y, (float) _upper._z };
  
	int[] i = { 0, 1, 1, 2, 2, 3, 3, 0, 1, 5, 5, 6, 6, 2, 2, 1, 5, 4, 4, 7, 7, 6, 6, 5, 4, 0, 0, 3, 3, 7, 7, 4, 3, 2, 2, 6, 6, 7, 7, 3, 0, 1, 1, 5, 5, 4, 4, 0 };
  
	FloatBufferBuilderFromCartesian3D vertices = new FloatBufferBuilderFromCartesian3D(CenterStrategy.noCenter(), Vector3D.zero());
	IntBufferBuilder indices = new IntBufferBuilder();
  
	final int numVertices = 8;
	for (int n = 0; n<numVertices; n++)
	{
	  vertices.add(v[n *3], v[n *3+1], v[n *3+2]);
	}
  
	final int numIndices = 48;
	for (int n = 0; n<numIndices; n++)
	{
	  indices.add(i[n]);
	}
  
	_mesh = new IndexedMesh(GLPrimitive.lines(), true, vertices.getCenter(), vertices.create(), indices.create(), 1, color);
  }

}