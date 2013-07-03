package org.glob3.mobile.generated; 
//
//  AbstractGeometryMesh.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 23/06/13.
//
//

//
//  AbstractGeometryMesh.h
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 23/06/13.
//
//




//class MutableMatrix44D;
//class IFloatBuffer;
//class Color;

public abstract class AbstractGeometryMesh extends Mesh
{

  protected final int _primitive;
  protected final boolean _owner;
  protected Vector3D _center ;
  protected final MutableMatrix44D _translationMatrix;
  protected IFloatBuffer _vertices;
  protected final float _lineWidth;
  protected final float _pointSize;
  protected final boolean _depthTest;

  protected Extent _extent;
  protected final Extent computeExtent()
  {
    final int vertexCount = getVertexCount();
  
    if (vertexCount <= 0)
    {
      return null;
    }
  
    double minX = 1e12;
    double minY = 1e12;
    double minZ = 1e12;
  
    double maxX = -1e12;
    double maxY = -1e12;
    double maxZ = -1e12;
  
    for (int i = 0; i < vertexCount; i++)
    {
      final int i3 = i * 3;
  
      final double x = _vertices.get(i3) + _center._x;
      final double y = _vertices.get(i3 + 1) + _center._y;
      final double z = _vertices.get(i3 + 2) + _center._z;
  
      if (x < minX)
         minX = x;
      if (x > maxX)
         maxX = x;
  
      if (y < minY)
         minY = y;
      if (y > maxY)
         maxY = y;
  
      if (z < minZ)
         minZ = z;
      if (z > maxZ)
         maxZ = z;
    }
  
    return new Box(new Vector3D(minX, minY, minZ), new Vector3D(maxX, maxY, maxZ));
  }

  protected AbstractGeometryMesh(int primitive, boolean owner, Vector3D center, IFloatBuffer vertices, float lineWidth, float pointSize, boolean depthTest)
  {
     _primitive = primitive;
     _owner = owner;
     _vertices = vertices;
     _extent = null;
     _center = new Vector3D(center);
     _translationMatrix = (center.isNan() || center.isZero()) ? null : new MutableMatrix44D(MutableMatrix44D.createTranslationMatrix(center));
     _lineWidth = lineWidth;
     _pointSize = pointSize;
     _depthTest = depthTest;
    createGLState();
  }

  protected GLState _glState = new GLState();

  protected final void createGLState()
  {
  
    GLGlobalState globalState = _glState.getGLGlobalState();
  
    globalState.setLineWidth(_lineWidth);
    if (_depthTest)
    {
      globalState.enableDepthTest();
    }
    else
    {
      globalState.disableDepthTest();
    }
  
    GPUProgramState progState = _glState.getGPUProgramState();
  
    progState.setAttributeValue(GPUAttributeKey.POSITION, _vertices, 4, 3, 0, false, 0); //Stride 0 - Not normalized - Index 0 - Our buffer contains elements of 3 - The attribute is a float vector of 4 elements
  
    if (_translationMatrix != null)
    {
      //progState.setUniformMatrixValue(MODELVIEW, *_translationMatrix, true);
      _glState.setModelView(_translationMatrix.asMatrix44D(), true);
    }
  
  
    progState.setUniformValue(GPUUniformKey.POINT_SIZE, _pointSize);
  }

  protected abstract void rawRender(G3MRenderContext rc);

  public void dispose()
  {
    if (_owner)
    {
      if (_vertices != null)
         _vertices.dispose();
    }
  
    if (_extent != null)
       _extent.dispose();
    if (_translationMatrix != null)
       _translationMatrix.dispose();
  }

  public final void render(G3MRenderContext rc)
  {
  }

  public final Extent getExtent()
  {
    if (_extent == null)
    {
      _extent = computeExtent();
    }
    return _extent;
  }

  public final int getVertexCount()
  {
    return _vertices.size() / 3;
  }

  public final Vector3D getVertex(int i)
  {
    final int p = i * 3;
    return new Vector3D(_vertices.get(p) + _center._x, _vertices.get(p+1) + _center._y, _vertices.get(p+2) + _center._z);
  }

  public final boolean isTransparent(G3MRenderContext rc)
  {
    return false; //TODO: CHECK
  }

  public final void render(G3MRenderContext rc, GLState parentGLState)
  {
    _glState.setParent(parentGLState);
    rawRender(rc);
  }

}