package org.glob3.mobile.generated; 
//
//  IndexedMesh.cpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 22/06/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//


//
//  IndexedMesh.hpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 22/06/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//



//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class IIntBuffer;

public class IndexedMesh extends Mesh
{
  private final int _primitive;
  private final boolean _owner;
  private Vector3D _center ;
  private final MutableMatrix44D _translationMatrix;
  private IFloatBuffer _vertices;
  private IIntBuffer _indices;
  private Color _flatColor;
  private IFloatBuffer _colors;
  private final float _colorsIntensity;
  private final float _lineWidth;

  private Extent _extent;

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Extent* computeExtent() const
  private Extent computeExtent()
  {
  
	final int vertexCount = getVertexCount();
  
	if (vertexCount <= 0)
	{
	  return null;
	}
  
	double minx = 1e10;
	double miny = 1e10;
	double minz = 1e10;
	double maxx = -1e10;
	double maxy = -1e10;
	double maxz = -1e10;
  
	for (int i = 0; i < vertexCount; i++)
	{
	  final int p = i * 3;
  
	  final double x = _vertices.get(p) + _center._x;
	  final double y = _vertices.get(p+1) + _center._y;
	  final double z = _vertices.get(p+2) + _center._z;
  
	  if (x < minx)
		  minx = x;
	  if (x > maxx)
		  maxx = x;
  
	  if (y < miny)
		  miny = y;
	  if (y > maxy)
		  maxy = y;
  
	  if (z < minz)
		  minz = z;
	  if (z > maxz)
		  maxz = z;
	}
  
	return new Box(new Vector3D(minx, miny, minz), new Vector3D(maxx, maxy, maxz));
  }


  public IndexedMesh(int primitive, boolean owner, Vector3D center, IFloatBuffer vertices, IIntBuffer indices, float lineWidth, Color flatColor, IFloatBuffer colors)
  {
	  this(primitive, owner, center, vertices, indices, lineWidth, flatColor, colors, (float)0.0);
  }
  public IndexedMesh(int primitive, boolean owner, Vector3D center, IFloatBuffer vertices, IIntBuffer indices, float lineWidth, Color flatColor)
  {
	  this(primitive, owner, center, vertices, indices, lineWidth, flatColor, null, (float)0.0);
  }
  public IndexedMesh(int primitive, boolean owner, Vector3D center, IFloatBuffer vertices, IIntBuffer indices, float lineWidth)
  {
	  this(primitive, owner, center, vertices, indices, lineWidth, null, null, (float)0.0);
  }
//C++ TO JAVA CONVERTER NOTE: Java does not allow default values for parameters. Overloaded methods are inserted above.
//ORIGINAL LINE: IndexedMesh(const int primitive, boolean owner, const Vector3D& center, IFloatBuffer* vertices, IIntBuffer* indices, float lineWidth, Color* flatColor = null, IFloatBuffer* colors = null, const float colorsIntensity = (float)0.0) : _primitive(primitive), _owner(owner), _vertices(vertices), _indices(indices), _flatColor(flatColor), _colors(colors), _colorsIntensity(colorsIntensity), _extent(null), _center(center), _translationMatrix((center.isNan() || center.isZero()) ? null : new MutableMatrix44D(MutableMatrix44D::createTranslationMatrix(center))), _lineWidth(lineWidth)
  public IndexedMesh(int primitive, boolean owner, Vector3D center, IFloatBuffer vertices, IIntBuffer indices, float lineWidth, Color flatColor, IFloatBuffer colors, float colorsIntensity)
  {
	  _primitive = primitive;
	  _owner = owner;
	  _vertices = vertices;
	  _indices = indices;
	  _flatColor = flatColor;
	  _colors = colors;
	  _colorsIntensity = colorsIntensity;
	  _extent = null;
	  _center = new Vector3D(center);
	  _translationMatrix = (center.isNan() || center.isZero()) ? null : new MutableMatrix44D(MutableMatrix44D.createTranslationMatrix(center));
	  _lineWidth = lineWidth;
  }

  public void dispose()
  {
	if (_owner)
	{
	  if (_vertices != null)
		  _vertices.dispose();
	  if (_indices != null)
		  _indices.dispose();
	  if (_colors != null)
		  _colors.dispose();
	  _flatColor = null;
	}
  
	_extent = null;
	if (_translationMatrix != null)
		_translationMatrix.dispose();
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual void render(const RenderContext* rc) const
  public void render(RenderContext rc)
  {
	GL gl = rc.getGL();
  
	gl.enableVerticesPosition();
  
	if (_colors == null)
	{
	  gl.disableVertexColor();
	}
	else
	{
	  gl.enableVertexColor(_colors, _colorsIntensity);
	}
  
	boolean blend = false;
	if (_flatColor == null)
	{
	  gl.disableVertexFlatColor();
	}
	else
	{
	  if (_flatColor.isTransparent())
	  {
		gl.enableBlend();
		gl.setBlendFuncSrcAlpha();
		blend = true;
	  }
	  gl.enableVertexFlatColor(_flatColor, _colorsIntensity);
	}
  
	gl.vertexPointer(3, 0, _vertices);
  
	gl.lineWidth(_lineWidth);
  
	if (_translationMatrix != null)
	{
	  gl.pushMatrix();
	  gl.multMatrixf(_translationMatrix);
	}
  
	if (_primitive == GLPrimitive.triangles())
	{
	  gl.drawTriangles(_indices);
	}
	else if (_primitive == GLPrimitive.triangleStrip())
	{
	  gl.drawTriangleStrip(_indices);
	}
	else if (_primitive == GLPrimitive.triangleFan())
	{
	  gl.drawTriangleFan(_indices);
	}
	else if (_primitive == GLPrimitive.lines())
	{
	  gl.drawLines(_indices);
	}
	else if (_primitive == GLPrimitive.lineStrip())
	{
	  gl.drawLineStrip(_indices);
	}
	else if (_primitive == GLPrimitive.lineLoop())
	{
	  gl.drawLineLoop(_indices);
	}
	else if (_primitive == GLPrimitive.points())
	{
	  gl.drawPoints(_indices);
	}
  
	if (_translationMatrix != null)
	{
	  gl.popMatrix();
	}
  
	if (blend)
	{
	  gl.disableBlend();
	}
  
	gl.disableVerticesPosition();
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Extent* getExtent() const
  public final Extent getExtent()
  {
	if (_extent == null)
	{
	  _extent = computeExtent();
	}
	return _extent;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: int getVertexCount() const
  public final int getVertexCount()
  {
	return _vertices.size() / 3;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const Vector3D getVertex(int i) const
  public final Vector3D getVertex(int i)
  {
	final int p = i * 3;
	return new Vector3D(_vertices.get(p) + _center._x, _vertices.get(p+1) + _center._y, _vertices.get(p+2) + _center._z);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean isTransparent(const RenderContext* rc) const
  public final boolean isTransparent(RenderContext rc)
  {
	if (_flatColor == null)
	{
	  return false;
	}
	return _flatColor.isTransparent();
  }

}