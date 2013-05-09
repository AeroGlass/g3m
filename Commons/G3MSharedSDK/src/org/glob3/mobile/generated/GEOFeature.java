package org.glob3.mobile.generated; 
//
//  GEOFeature.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 11/29/12.
//
//

//
//  GEOFeature.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 11/29/12.
//
//




//class GEOGeometry;
//class JSONBaseObject;
//class JSONObject;
//class GPUProgramState;

public class GEOFeature extends GEOObject
{
  private final JSONBaseObject _id;
  private GEOGeometry _geometry;
  private final JSONObject _properties;


  public GEOFeature(JSONBaseObject id, GEOGeometry geometry, JSONObject properties)
  {
     _id = id;
     _geometry = geometry;
     _properties = properties;
    if (_geometry != null)
    {
      _geometry.setFeature(this);
    }
  }

  public void dispose()
  {
    if (_id != null)
       _id.dispose();
    if (_geometry != null)
       _geometry.dispose();
    if (_properties != null)
       _properties.dispose();
  }

<<<<<<< HEAD
  public final void render(G3MRenderContext rc, GLState parentState, GPUProgramState parentProgramState, GEOSymbolizer symbolizer)
  {
    _geometry.render(rc, parentState, parentProgramState, symbolizer);
=======
  public final JSONObject getProperties()
  {
    return _properties;
>>>>>>> webgl-port
  }

  public final void symbolize(G3MRenderContext rc, GEOSymbolizationContext sc)
  {
    if (_geometry != null)
    {
      _geometry.symbolize(rc, sc);
    }
  }

}