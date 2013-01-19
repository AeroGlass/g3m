package org.glob3.mobile.generated; 
//
//  EllipsoidalTileTessellator.cpp
//  G3MiOSSDK
//
//  Created by Agustin Trujillo Pino on 12/07/12.
//  Copyright (c) 2012 IGO Software SL. All rights reserved.
//

//
//  EllipsoidalTileTessellator.hpp
//  G3MiOSSDK
//
//  Created by Agustin Trujillo Pino on 12/07/12.
//  Copyright (c) 2012 IGO Software SL. All rights reserved.
//



///#include "MutableVector3D.hpp"
///#include "Planet.hpp"

public class EllipsoidalTileTessellator extends TileTessellator
{

  private final int _resolution;
  private final boolean _skirted;

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Mesh* createDebugMesh(const G3MRenderContext* rc, const Tile* tile) const
  public final Mesh createDebugMesh(G3MRenderContext rc, Tile tile)
  {
	final Sector sector = tile.getSector();
	final Planet planet = rc.getPlanet();
  
	final int resolutionMinus1 = _resolution - 1;
	int posS = 0;
  
	// compute offset for vertices
	final Vector3D sw = planet.toCartesian(sector.getSW());
	final Vector3D nw = planet.toCartesian(sector.getNW());
	final double offset = nw.sub(sw).length() * 1e-3;
  
	// create vectors
	FloatBufferBuilderFromGeodetic vertices = new FloatBufferBuilderFromGeodetic(CenterStrategy.givenCenter(), planet, sector.getCenter());
	// create indices
	ShortBufferBuilder indices = new ShortBufferBuilder();
  
	// west side
	for (int j = 0; j < resolutionMinus1; j++)
	{
	  final Geodetic3D g = new Geodetic3D(sector.getInnerPoint(0, (double)j/resolutionMinus1), offset);
  
	  vertices.add(g);
	  indices.add((short) posS++);
	}
  
	// south side
	for (int i = 0; i < resolutionMinus1; i++)
	{
	  final Geodetic3D g = new Geodetic3D(sector.getInnerPoint((double)i/resolutionMinus1, 1), offset);
  
	  vertices.add(g);
	  indices.add((short) posS++);
	}
  
	// east side
	for (int j = resolutionMinus1; j > 0; j--)
	{
	  final Geodetic3D g = new Geodetic3D(sector.getInnerPoint(1, (double)j/resolutionMinus1), offset);
  
	  vertices.add(g);
	  indices.add((short) posS++);
	}
  
	// north side
	for (int i = resolutionMinus1; i > 0; i--)
	{
	  final Geodetic3D g = new Geodetic3D(sector.getInnerPoint((double)i/resolutionMinus1, 0), offset);
  
	  vertices.add(g);
	  indices.add((short) posS++);
	}
  
	Color color = new Color(Color.fromRGBA((float) 1.0, (float) 0, (float) 0, (float) 1.0));
	final Vector3D center = planet.toCartesian(sector.getCenter());
  
	return new IndexedMesh(GLPrimitive.lineLoop(), true, center, vertices.create(), indices.create(), 1, 1, color);
  }

  public EllipsoidalTileTessellator(int resolution, boolean skirted)
  {
	  _resolution = resolution;
	  _skirted = skirted;
//    int __TODO_width_and_height_resolutions;
  }

  public void dispose()
  {
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Mesh* createMesh(const G3MRenderContext* rc, const Tile* tile) const
  public final Mesh createMesh(G3MRenderContext rc, Tile tile)
  {
  
	final Sector sector = tile.getSector();
	final Planet planet = rc.getPlanet();
  
	final int resolution = _resolution;
	final int resolutionMinus1 = resolution - 1;
  
	// create vertices coordinates
	FloatBufferBuilderFromGeodetic vertices = new FloatBufferBuilderFromGeodetic(CenterStrategy.givenCenter(), planet, sector.getCenter());
	for (int j = 0; j < resolution; j++)
	{
	  for (int i = 0; i < resolution; i++)
	  {
		Geodetic2D innerPoint = sector.getInnerPoint((double) i / resolutionMinus1, (double) j / resolutionMinus1);
  
		vertices.add(innerPoint);
	  }
	}
  
	// create indices
	ShortBufferBuilder indices = new ShortBufferBuilder();
	for (int j = 0; j < resolutionMinus1; j++)
	{
	  if (j > 0)
	  {
		indices.add((short)(j *resolution));
	  }
	  for (int i = 0; i < resolution; i++)
	  {
		indices.add((short)(j *resolution + i));
		indices.add((short)(j *resolution + i + resolution));
	  }
	  indices.add((short)(j *resolution + 2 *resolution - 1));
	}
  
	// create skirts
	if (_skirted)
	{
  
	  // compute skirt height
	  final Vector3D sw = planet.toCartesian(sector.getSW());
	  final Vector3D nw = planet.toCartesian(sector.getNW());
	  final double skirtHeight = nw.sub(sw).length() * 0.05;
  
	  indices.add(0);
	  int posS = resolution * resolution;
  
	  // west side
	  for (int j = 0; j < resolutionMinus1; j++)
	  {
		final Geodetic3D g = new Geodetic3D(sector.getInnerPoint(0, (double)j/resolutionMinus1), -skirtHeight);
		vertices.add(g);
  
		indices.add((short)(j *resolution));
		indices.add((short) posS++);
	  }
  
	  // south side
	  for (int i = 0; i < resolutionMinus1; i++)
	  {
		final Geodetic3D g = new Geodetic3D(sector.getInnerPoint((double)i/resolutionMinus1, 1), -skirtHeight);
		vertices.add(g);
  
		indices.add((short)(resolutionMinus1 *resolution + i));
		indices.add((short) posS++);
	  }
  
	  // east side
	  for (int j = resolutionMinus1; j > 0; j--)
	  {
		final Geodetic3D g = new Geodetic3D(sector.getInnerPoint(1, (double)j/resolutionMinus1), -skirtHeight);
		vertices.add(g);
  
		indices.add((short)(j *resolution + resolutionMinus1));
		indices.add((short) posS++);
	  }
  
	  // north side
	  for (int i = resolutionMinus1; i > 0; i--)
	  {
		final Geodetic3D g = new Geodetic3D(sector.getInnerPoint((double)i/resolutionMinus1, 0), -skirtHeight);
		vertices.add(g);
  
		indices.add((short) i);
		indices.add((short) posS++);
	  }
  
	  // last triangle
	  indices.add(0);
	  indices.add((short)(resolution *resolution));
	}
  
	Color color = new Color(Color.fromRGBA((float) 0.1, (float) 0.1, (float) 0.1, (float) 1.0));
  
	return new IndexedMesh(GLPrimitive.triangleStrip(), true, vertices.getCenter(), vertices.create(), indices.create(), 1, 1, color);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: boolean isReady(const G3MRenderContext *rc) const
  public final boolean isReady(G3MRenderContext rc)
  {
	return true;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: IFloatBuffer* createUnitTextCoords() const
  public final IFloatBuffer createUnitTextCoords()
  {
  
	FloatBufferBuilderFromCartesian2D textCoords = new FloatBufferBuilderFromCartesian2D();
  
	final int resolution = _resolution;
	final int resolutionMinus1 = resolution - 1;
  
	float[] u = new float[resolution * resolution];
	float[] v = new float[resolution * resolution];
  
	for (int j = 0; j < resolution; j++)
	{
	  for (int i = 0; i < resolution; i++)
	  {
		final int pos = j *resolution + i;
		u[pos] = (float) i / resolutionMinus1;
		v[pos] = (float) j / resolutionMinus1;
	  }
	}
  
	for (int j = 0; j < resolution; j++)
	{
	  for (int i = 0; i < resolution; i++)
	  {
		final int pos = j *resolution + i;
		textCoords.add(u[pos], v[pos]);
	  }
	}
  
	// create skirts
	if (_skirted)
	{
	  // west side
	  for (int j = 0; j < resolutionMinus1; j++)
	  {
		final int pos = j *resolution;
		textCoords.add(u[pos], v[pos]);
	  }
  
	  // south side
	  for (int i = 0; i < resolutionMinus1; i++)
	  {
		final int pos = resolutionMinus1 * resolution + i;
		textCoords.add(u[pos], v[pos]);
	  }
  
	  // east side
	  for (int j = resolutionMinus1; j > 0; j--)
	  {
		final int pos = j *resolution + resolutionMinus1;
		textCoords.add(u[pos], v[pos]);
	  }
  
	  // north side
	  for (int i = resolutionMinus1; i > 0; i--)
	  {
		final int pos = i;
		textCoords.add(u[pos], v[pos]);
	  }
	}
  
	// free temp memory
	u = null;
	v = null;
  
	return textCoords.create();
  }

}