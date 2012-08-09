package org.glob3.mobile.generated; 
//
//  SimplePlanetRenderer.cpp
//  G3MiOSSDK
//
//  Created by José Miguel S N on 12/06/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

//
//  SimplePlanetRenderer.h
//  G3MiOSSDK
//
//  Created by José Miguel S N on 12/06/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//





public class SimplePlanetRenderer extends Renderer
{

  private final String _textureFilename;
  private final int _texWidth;
  private final int _texHeight;

  private final int _latRes;
  private final int _lonRes;

  private Mesh _mesh;


  private float [] createVertices(Planet planet)
  {
	//VERTICES
	float[] vertices = new float[_latRes *_lonRes * 3];
  
	final double lonRes1 = (double)(_lonRes-1);
	final double latRes1 = (double)(_latRes-1);
	int verticesIndex = 0;
	for(double i = 0.0; i < _lonRes; i++)
	{
	  final Angle lon = Angle.fromDegrees((i * 360 / lonRes1) -180);
	  for (double j = 0.0; j < _latRes; j++)
	  {
		final Angle lat = Angle.fromDegrees((j * 180.0 / latRes1) -90.0);
		final Geodetic2D g = new Geodetic2D(lat, lon);
  
		final Vector3D v = planet.toVector3D(g);
		vertices[verticesIndex++] = (float) v.x(); //Vertices
		vertices[verticesIndex++] = (float) v.y();
		vertices[verticesIndex++] = (float) v.z();
	  }
	}
  
	return vertices;
  }
  private int[] createMeshIndex()
  {
	final int res = _lonRes;
  
	final int numIndexes = (2 * (res - 1) * (res + 1)) -1;
	int[] indexes = new int[numIndexes];
  
	int n = 0;
	for (int j = 0; j < res - 1; j++)
	{
	  if (j > 0)
		  indexes[n++] = (int)(j * res);
	  for (int i = 0; i < res; i++)
	  {
		indexes[n++] = (int)(j * res + i);
		indexes[n++] = (int)(j * res + i + res);
	  }
	  indexes[n++] = (int)(j * res + 2 * res - 1);
	}
  
	return indexes;
  }
  private float[] createTextureCoordinates()
  {
	float[] texCoords = new float[_latRes *_lonRes * 2];
  
	final double lonRes1 = (double)(_lonRes-1);
	final double latRes1 = (double)(_latRes-1);
	int p = 0;
	for(double i = 0.0; i < _lonRes; i++)
	{
	  double u = (i / lonRes1);
	  for (double j = 0.0; j < _latRes; j++)
	  {
		final double v = 1.0 - (j / latRes1);
		texCoords[p++] = (float) u;
		texCoords[p++] = (float) v;
	  }
	}
  
	return texCoords;
  }

  private boolean initializeMesh(RenderContext rc)
  {
  
  
	final Planet planet = rc.getPlanet();
  
	float ver = createVertices(planet);
	final int res = _lonRes;
	final int numIndexes = (2 * (res - 1) * (res + 1)) -1;
	int ind = createMeshIndex();
  
	//TEXTURED
	int texID = 0;
	float texC = 0F;
	if (true)
	{
	  texID = rc.getTexturesHandler().getTextureIdFromFileName(_textureFilename, _texWidth, _texHeight);
	  if (texID < 1)
	  {
		rc.getLogger().logError("Can't load file %s", _textureFilename);
		return false;
	  }
	  texC = createTextureCoordinates();
	}
  
	//COLORS PER VERTEX
	float[] colors = 0;
	if (true)
	{
	  int numVertices = res * res * 4;
	  colors = new float[numVertices];
	  for(int i = 0; i < numVertices;)
	  {
		float val = (float)(0.5 + sinf((float)(2.0 * Math.PI * ((float) i) / numVertices)) / 2.0);
  
		colors[i++] = val;
		colors[i++] = 0F;
		colors[i++] = (float)(1.0 - val);
		colors[i++] = 1F;
	  }
	}
  
	//FLAT COLOR
	Color flatColor = null;
	if (true)
	{
	  flatColor = new Color(Color.fromRGBA((float) 0.0, (float) 1.0, (float) 0.0, (float) 1.0));
	}
  
	float[] normals = 0;
	if (true)
	{
	  int numVertices = res * res * 3;
	  normals = new float[numVertices];
	  for(int i = 0; i < numVertices;)
	  {
		normals[i++] = 1.0F;
		normals[i++] = 1.0F;
		normals[i++] = 1.0F;
	  }
	}
  
	IndexedMesh im = IndexedMesh.CreateFromVector3D(true, TriangleStrip, CenterStrategy.NoCenter, new Vector3D(0,0,0), _latRes *_lonRes, ver, ind, numIndexes, flatColor, colors, 0.5, normals);
  
	TextureMapping texMap = new TextureMapping(texID, texC, rc.getTexturesHandler(), _textureFilename, _texWidth,_texHeight);
  
	_mesh = new TexturedMesh(im, true, texMap, true);
  
	return true;
  }

  public SimplePlanetRenderer(String textureFilename)
  {
	  _latRes = 30;
	  _lonRes = 30;
	  _textureFilename = textureFilename;
	  _mesh = null;
	  _texWidth = 2048;
	  _texHeight = 1024;
  }
  public void dispose()
  {
	if (_mesh != null)
		_mesh.dispose();
  }

  public final void initialize(InitializationContext ic)
  {
  
  }

  public final int render(RenderContext rc)
  {
	if (_mesh == null)
	{
	  if (!initializeMesh(rc))
	  {
		return Renderer.maxTimeToRender;
	  }
	}
  
	_mesh.render(rc);
  
	return Renderer.maxTimeToRender;
  }

  public final boolean onTouchEvent(EventContext ec, TouchEvent touchEvent)
  {
	return false;
  }

  public final void onResizeViewportEvent(EventContext ec, int width, int height)
  {

  }

  public final boolean isReadyToRender(RenderContext rc)
  {
	return true;
  }


}