package org.glob3.mobile.generated; 
//
//  FlatColorMesh.cpp
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 23/06/13.
//
//

//
//  FlatColorMesh.h
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 23/06/13.
//
//





public class FlatColorMesh extends Mesh
{
  private Mesh _mesh;
  private final boolean _ownedMesh;

  private final Color _flatColor;
  private final boolean _ownedColor;

  private GLState _glState = new GLState();

  private void createGLState()
  {
    GLGlobalState globalState = _glState.getGLGlobalState();
  
    if (isTransparent(null))
    {
      globalState.enableBlend();
      globalState.setBlendFactors(GLBlendFactor.srcAlpha(), GLBlendFactor.oneMinusSrcAlpha());
    }
  
    GPUProgramState progState = _glState.getGPUProgramState();
  
    progState.setUniformValue(GPUVariable.FLAT_COLOR, (double) _flatColor.getRed(), (double) _flatColor.getGreen(), (double) _flatColor.getBlue(), (double) _flatColor.getAlpha());
  
  
  }



  public FlatColorMesh(Mesh mesh, boolean ownedMesh, Color color, boolean ownedColor)
  {
     _mesh = mesh;
     _ownedMesh = ownedMesh;
     _flatColor = color;
     _ownedColor = ownedColor;
    createGLState();
  }

  public void dispose()
  {
    if (_ownedMesh)
    {
      if (_mesh != null)
         _mesh.dispose();
    }
    if (_ownedColor)
    {
      if (_flatColor != null)
         _flatColor.dispose();
    }
  }

  public final void render(G3MRenderContext rc)
  {
    _mesh.render(rc);
  }

  public final Extent getExtent()
  {
    return (_mesh == null) ? null : _mesh.getExtent();
  }

  public final int getVertexCount()
  {
    return _mesh.getVertexCount();
  }

  public final Vector3D getVertex(int i)
  {
    return _mesh.getVertex(i);
  }

  public final boolean isTransparent(G3MRenderContext rc)
  {
    return _flatColor.getAlpha() != 1.0;
  }

  public final void render(G3MRenderContext rc, GLState parentState)
  {
    _glState.setParent(parentState);
    _mesh.render(rc, _glState);
  }
}