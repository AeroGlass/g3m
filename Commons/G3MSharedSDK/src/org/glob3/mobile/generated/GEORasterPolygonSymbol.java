package org.glob3.mobile.generated; 
//
//  GEORasterPolygonSymbol.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 7/23/13.
//
//

//
//  GEORasterPolygonSymbol.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 7/23/13.
//
//




//class GEO2DPolygonData;


public class GEORasterPolygonSymbol extends GEORasterSymbol
{
  private java.util.ArrayList<Geodetic2D> _coordinates;
  private final GEO2DLineRasterStyle      _lineStyle;

  private final java.util.ArrayList<java.util.ArrayList<Geodetic2D>> _holesCoordinatesArray;
  private final GEO2DSurfaceRasterStyle _surfaceStyle = new GEO2DSurfaceRasterStyle();

  public final void rasterize(ICanvas canvas, GEORasterProjection projection)
  {
    final boolean rasterSurface = _surfaceStyle.apply(canvas);
    final boolean rasterBoundary = _lineStyle.apply(canvas);
  
    rasterPolygon(_coordinates, _holesCoordinatesArray, rasterSurface, rasterBoundary, canvas, projection);
  }

  public GEORasterPolygonSymbol(GEO2DPolygonData polygonData, GEO2DLineRasterStyle lineStyle, GEO2DSurfaceRasterStyle surfaceStyle)
  {
     super(calculateSectorFromCoordinates(polygonData.getCoordinates()));
     _coordinates = copyCoordinates(polygonData.getCoordinates());
     _holesCoordinatesArray = copyCoordinatesArray(polygonData.getHolesCoordinatesArray());
     _lineStyle = lineStyle;
     _surfaceStyle = new GEO2DSurfaceRasterStyle(surfaceStyle);
  
  }

}