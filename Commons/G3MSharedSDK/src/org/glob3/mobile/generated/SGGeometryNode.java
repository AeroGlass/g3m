package org.glob3.mobile.generated; 
//
//  SGGeometryNode.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 11/8/12.
//
//

//
//  SGGeometryNode.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 11/8/12.
//
//



//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class IFloatBuffer;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class IIntBuffer;

public class SGGeometryNode extends SGNode
{
  private final int _primitive;
  private IFloatBuffer _vertices;
  private IFloatBuffer _colors;
  private IFloatBuffer _uv;
  private IFloatBuffer _normals;
  private IIntBuffer _indices;


  public SGGeometryNode(String id, String sId, int primitive, IFloatBuffer vertices, IFloatBuffer colors, IFloatBuffer uv, IFloatBuffer normals, IIntBuffer indices)
  {
	  super(id, sId);
	  _primitive = primitive;
	  _vertices = vertices;
	  _colors = colors;
	  _uv = uv;
	  _normals = normals;
	  _indices = indices;

  }

  public void dispose()
  {
	if (_vertices != null)
		_vertices.dispose();
	if (_colors != null)
		_colors.dispose();
	if (_uv != null)
		_uv.dispose();
	if (_normals != null)
		_normals.dispose();
	if (_indices != null)
		_indices.dispose();
  }

  public final void rawRender(G3MRenderContext rc, GLState parentState)
  {
	GL gl = rc.getGL();
  
	GLState state = new GLState(parentState);
  
	state.enableVerticesPosition();
  
	if (_colors == null)
	{
	  state.disableVertexColor();
	}
	else
	{
	  final float colorsIntensity = 1F;
	  state.enableVertexColor(_colors, colorsIntensity);
	}
  
	if (_uv != null)
	{
	  gl.transformTexCoords(1.0f, 1.0f, 0.0f, 0.0f);
  
	  gl.setTextureCoordinates(2, 0, _uv);
	}
  
	gl.setState(state);
  
	gl.vertexPointer(3, 0, _vertices);
  
	gl.drawElements(_primitive, _indices);
  }

  public final GLState createState(G3MRenderContext rc, GLState parentState)
  {
	return null;
  }

}