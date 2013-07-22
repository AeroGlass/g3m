package org.glob3.mobile.generated; 
//
//  GEOTileRasterizer.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 7/10/13.
//
//

//
//  GEOTileRasterizer.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 7/10/13.
//
//



//class GEORasterSymbol;


public class GEOTileRasterizer extends CanvasTileRasterizer
{
  private QuadTree _quadTree = new QuadTree();

  public final String getId()
  {
    return "GEOTileRasterizer";
  }

  public final void rasterize(IImage image, Tile tile, boolean mercator, IImageListener listener, boolean autodelete)
  {
  
    final int width = image.getWidth();
    final int height = image.getHeight();
  
    GEORasterProjection projection = new GEORasterProjection(tile.getSector(), mercator, width, height);
  
    ICanvas canvas = getCanvas(width, height);
  
    canvas.drawImage(image, 0, 0);
  
  //  canvas->setFillColor(Color::yellow());
  
  //  canvas->setLineColor(Color::white());
  //  canvas->setLineWidth(1);
  //  canvas->strokeRectangle(0, 0, width, height);
  
  
    _quadTree.acceptVisitor(tile.getSector(), new GEOTileRasterizer_QuadTreeVisitor(canvas, projection));
  
    canvas.createImage(listener, autodelete);
  
    if (image != null)
       image.dispose();
  
    if (projection != null)
       projection.dispose();
  }

  public final void addSymbol(GEORasterSymbol symbol)
  {
    final boolean added = _quadTree.add(symbol.getSector(), symbol);
    if (!added)
    {
      if (symbol != null)
         symbol.dispose();
    }
  }

}