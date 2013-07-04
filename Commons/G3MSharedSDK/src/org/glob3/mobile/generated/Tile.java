package org.glob3.mobile.generated; 
//
//  Tile.cpp
//  G3MiOSSDK
//
//  Created by Agustín Trujillo Pino on 12/06/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

//
//  Tile.hpp
//  G3MiOSSDK
//
//  Created by Agustín Trujillo Pino on 12/06/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//



//class G3MRenderContext;
//class Mesh;
//class TileTessellator;
//class TileTexturizer;
//class TilesRenderParameters;
//class ITimer;
//class TilesStatistics;
//class TileRenderContext;
//class TileKey;
//class Vector3D;
//class GLState;
//class Extent;
//class ElevationDataProvider;
//class ElevationData;
//class MeshHolder;
//class Vector2I;
//class TileElevationDataRequest;
//class Frustum;


public class Tile
{
  private TileTexturizer _texturizer;
  private Tile _parent;
  private final Sector _sector ;
  private final int _level;
  private final int _row;
  private final int _column;

  private Mesh _tessellatorMesh;

  private Mesh _debugMesh;
  private Mesh _texturizedMesh;
  private TileElevationDataRequest _elevationDataRequest;

  private boolean _textureSolved;
  private java.util.ArrayList<Tile> _subtiles;
  private boolean _justCreatedSubtiles;

  private boolean _texturizerDirty; //Texturizer needs to be called

  private float _verticalExaggeration;
  private double _minHeight;
  private double _maxHeight;

  private Mesh getTessellatorMesh(G3MRenderContext rc, TileRenderContext trc)
  {
  
    ElevationDataProvider elevationDataProvider = trc.getElevationDataProvider();
  
  //  const TileTessellator* tessellator = trc->getTessellator();
  //  const bool renderDebug = trc->getParameters()->_renderDebug;
  //  const Planet* planet = rc->getPlanet();
  //
  //  const LayerTilesRenderParameters* layerTilesRenderParameters = trc->getLayerTilesRenderParameters();
  //  const Vector2I tileMeshResolution(layerTilesRenderParameters->_tileMeshResolution);
  
    if ((_elevationData == null) && (elevationDataProvider != null))
    {
      int __ASK_JM;
      final TileTessellator tessellator = trc.getTessellator();
      final boolean renderDebug = trc.getParameters()._renderDebug;
      final Planet planet = rc.getPlanet();
  
      final LayerTilesRenderParameters layerTilesRenderParameters = trc.getLayerTilesRenderParameters();
      final Vector2I tileMeshResolution = new Vector2I(layerTilesRenderParameters._tileMeshResolution);
  
      initializeElevationData(elevationDataProvider, tessellator, tileMeshResolution, planet, renderDebug);
    }
  
    if ((_tessellatorMesh == null) || _mustActualizeMeshDueToNewElevationData)
    {
      _mustActualizeMeshDueToNewElevationData = false;
  
      final TileTessellator tessellator = trc.getTessellator();
      final boolean renderDebug = trc.getParameters()._renderDebug;
      final Planet planet = rc.getPlanet();
  
      final LayerTilesRenderParameters layerTilesRenderParameters = trc.getLayerTilesRenderParameters();
      final Vector2I tileMeshResolution = new Vector2I(layerTilesRenderParameters._tileMeshResolution);
  
      final boolean mercator = trc.getLayerTilesRenderParameters()._mercator;
  
      if (elevationDataProvider == null)
      {
        // no elevation data provider, just create a simple mesh without elevation
        _tessellatorMesh = tessellator.createTileMesh(planet, tileMeshResolution, this, null, _verticalExaggeration, mercator, renderDebug);
      }
      else
      {
        Mesh tessellatorMesh = tessellator.createTileMesh(planet, tileMeshResolution, this, _elevationData, _verticalExaggeration, mercator, renderDebug);
  
        MeshHolder meshHolder = (MeshHolder) _tessellatorMesh;
        if (meshHolder == null)
        {
          meshHolder = new MeshHolder(tessellatorMesh);
          _tessellatorMesh = meshHolder;
        }
        else
        {
          meshHolder.setMesh(tessellatorMesh);
        }
      }
    }
  
    return _tessellatorMesh;
  }

  private Mesh getDebugMesh(G3MRenderContext rc, TileRenderContext trc)
  {
    if (_debugMesh == null)
    {
      final LayerTilesRenderParameters layerTilesRenderParameters = trc.getLayerTilesRenderParameters();
      final Vector2I tileMeshResolution = new Vector2I(layerTilesRenderParameters._tileMeshResolution);
  
      //TODO: CHECK
      _debugMesh = trc.getTessellator().createTileDebugMesh(rc.getPlanet(), tileMeshResolution, this);
    }
    return _debugMesh;
  }

  private boolean isVisible(G3MRenderContext rc, TileRenderContext trc, Planet planet, Vector3D cameraNormalizedPosition, double cameraAngle2HorizonInRadians, Frustum cameraFrustumInModelCoordinates)
  {
    // test if sector is back oriented with respect to the camera
  
    if (_sector.isBackOriented(rc, getMinHeight(), planet, cameraNormalizedPosition, cameraAngle2HorizonInRadians))
    {
      return false;
    }
  
    final Extent extent = getTessellatorMesh(rc, trc).getExtent();
    if (extent == null)
    {
      return false;
    }
  
    ////const Extent* extent = getTileExtent(rc);
    //const Extent* tileExtent = getTileExtent(rc);
    //if (!tileExtent->fullContains(extent)) {
    //  printf("break point on me\n");
    //}
  
  //  return extent->touches( rc->getCurrentCamera()->getFrustumInModelCoordinates() );
    return extent.touches(cameraFrustumInModelCoordinates);
    //return extent->touches( rc->getCurrentCamera()->getHalfFrustuminModelCoordinates() );
  }

  private boolean meetsRenderCriteria(G3MRenderContext rc, TileRenderContext trc)
  {
    //  const TilesRenderParameters* parameters = trc->getParameters();
  
    final LayerTilesRenderParameters parameters = trc.getLayerTilesRenderParameters();
  
    if (_level >= parameters._maxLevelForPoles)
    {
      if (_sector.touchesNorthPole() || _sector.touchesSouthPole())
      {
        return true;
      }
    }
  
    if (_level >= parameters._maxLevel)
    {
      return true;
    }
  
    TileTexturizer texturizer = trc.getTexturizer();
    if (texturizer != null)
    {
      if (texturizer.tileMeetsRenderCriteria(this))
      {
        return true;
      }
    }
  
    //const Extent* extent = getTessellatorMesh(rc, trc)->getExtent();
    final Extent extent = getTileExtent(rc);
    if (extent == null)
    {
      return true;
    }
  
    //  const double projectedSize = extent->squaredProjectedArea(rc);
    //  if (projectedSize <= (parameters->_tileTextureWidth * parameters->_tileTextureHeight * 2)) {
    //    return true;
    //  }
    final Vector2I ex = extent.projectedExtent(rc);
    final int t = (ex._x + ex._y);
    final double threshold = (parameters._tileTextureResolution._x + parameters._tileTextureResolution._y) * 1.75;
    if (t <= threshold)
    {
      return true;
    }
  
    if (trc.getParameters()._useTilesSplitBudget)
    {
      if (_subtiles == null) // the tile needs to create the subtiles
      {
        if (trc.getStatistics().getSplitsCountInFrame() > 1)
        {
          // there are not more splitsCount-budget to spend
          return true;
        }
  
        if (trc.getLastSplitTimer().elapsedTime().milliseconds() < 25)
        {
          // there are not more time-budget to spend
          return true;
        }
      }
    }
  
    return false;
  }

  private void rawRender(G3MRenderContext rc, TileRenderContext trc, GLState parentState)
  {
  
    Mesh tessellatorMesh = getTessellatorMesh(rc, trc);
    if (tessellatorMesh == null)
    {
      return;
    }
  
    TileTexturizer texturizer = trc.getTexturizer();
    if (texturizer == null)
    {
      tessellatorMesh.render(rc, parentState);
    }
    else
    {
      final boolean needsToCallTexturizer = (_texturizedMesh == null) || isTexturizerDirty();
  
      if (needsToCallTexturizer)
      {
        _texturizedMesh = texturizer.texturize(rc, trc, this, tessellatorMesh, _texturizedMesh);
      }
  
      if (_texturizedMesh != null)
      {
        _texturizedMesh.render(rc, parentState);
      }
      else
      {
        tessellatorMesh.render(rc, parentState);
      }
    }
  
  }

  private void debugRender(G3MRenderContext rc, TileRenderContext trc, GLState parentState)
  {
    Mesh debugMesh = getDebugMesh(rc, trc);
    if (debugMesh != null)
    {
      debugMesh.render(rc, parentState);
    }
  }

  private Tile createSubTile(Angle lowerLat, Angle lowerLon, Angle upperLat, Angle upperLon, int level, int row, int column, boolean setParent)
  {
    Tile parent = setParent ? this : null;
    return new Tile(_texturizer, parent, new Sector(new Geodetic2D(lowerLat, lowerLon), new Geodetic2D(upperLat, upperLon)), level, row, column);
  }


  private java.util.ArrayList<Tile> getSubTiles(Angle splitLatitude, Angle splitLongitude)
  {
    if (_subtiles == null)
    {
      _subtiles = createSubTiles(splitLatitude, splitLongitude, true);
      _justCreatedSubtiles = true;
    }
    return _subtiles;
  }

//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//  Tile(Tile that);

  private void ancestorTexturedSolvedChanged(Tile ancestor, boolean textureSolved)
  {
    if (textureSolved && isTextureSolved())
    {
      return;
    }
  
    if (_texturizer != null)
    {
      _texturizer.ancestorTexturedSolvedChanged(this, ancestor, textureSolved);
    }
  
    if (_subtiles != null)
    {
      final int subtilesSize = _subtiles.size();
      for (int i = 0; i < subtilesSize; i++)
      {
        Tile subtile = _subtiles.get(i);
        subtile.ancestorTexturedSolvedChanged(ancestor, textureSolved);
      }
    }
  }

  private boolean _isVisible;
  private void setIsVisible(boolean isVisible, TileTexturizer texturizer)
  {
    if (_isVisible != isVisible)
    {
      _isVisible = isVisible;
  
      if (!_isVisible)
      {
        deleteTexturizedMesh(texturizer);
      }
    }
  }

  private void deleteTexturizedMesh(TileTexturizer texturizer)
  {
    // check for (_parent != NULL) to avoid deleting the firstLevel tiles.
    // in this case, the mesh is always loaded (as well as its texture) to be the last option
    // falback texture for any tile
    if ((_parent != null) && (_texturizedMesh != null))
    {
  
      if (texturizer != null)
      {
        texturizer.tileMeshToBeDeleted(this, _texturizedMesh);
      }
  
      if (_texturizedMesh != null)
         _texturizedMesh.dispose();
      _texturizedMesh = null;
  
      if (_texturizerData != null)
         _texturizerData.dispose();
      _texturizerData = null;
  
      setTexturizerDirty(true);
      setTextureSolved(false);
    }
  }

  private ITexturizerData _texturizerData;

  private Extent _tileExtent;
  private Extent getTileExtent(G3MRenderContext rc)
  {
    if (_tileExtent == null)
    {
      final Planet planet = rc.getPlanet();
  
      final double minHeight = getMinHeight() * _verticalExaggeration;
      final double maxHeight = getMaxHeight() * _verticalExaggeration;
  
      final Vector3D v0 = planet.toCartesian(_sector.getCenter(), maxHeight);
      final Vector3D v1 = planet.toCartesian(_sector.getNE(), minHeight);
      final Vector3D v2 = planet.toCartesian(_sector.getNW(), minHeight);
      final Vector3D v3 = planet.toCartesian(_sector.getSE(), minHeight);
      final Vector3D v4 = planet.toCartesian(_sector.getSW(), minHeight);
  
      double lowerX = v0._x;
      if (v1._x < lowerX)
      {
         lowerX = v1._x;
      }
      if (v2._x < lowerX)
      {
         lowerX = v2._x;
      }
      if (v3._x < lowerX)
      {
         lowerX = v3._x;
      }
      if (v4._x < lowerX)
      {
         lowerX = v4._x;
      }
  
      double upperX = v0._x;
      if (v1._x > upperX)
      {
         upperX = v1._x;
      }
      if (v2._x > upperX)
      {
         upperX = v2._x;
      }
      if (v3._x > upperX)
      {
         upperX = v3._x;
      }
      if (v4._x > upperX)
      {
         upperX = v4._x;
      }
  
  
      double lowerY = v0._y;
      if (v1._y < lowerY)
      {
         lowerY = v1._y;
      }
      if (v2._y < lowerY)
      {
         lowerY = v2._y;
      }
      if (v3._y < lowerY)
      {
         lowerY = v3._y;
      }
      if (v4._y < lowerY)
      {
         lowerY = v4._y;
      }
  
      double upperY = v0._y;
      if (v1._y > upperY)
      {
         upperY = v1._y;
      }
      if (v2._y > upperY)
      {
         upperY = v2._y;
      }
      if (v3._y > upperY)
      {
         upperY = v3._y;
      }
      if (v4._y > upperY)
      {
         upperY = v4._y;
      }
  
  
      double lowerZ = v0._z;
      if (v1._z < lowerZ)
      {
         lowerZ = v1._z;
      }
      if (v2._z < lowerZ)
      {
         lowerZ = v2._z;
      }
      if (v3._z < lowerZ)
      {
         lowerZ = v3._z;
      }
      if (v4._z < lowerZ)
      {
         lowerZ = v4._z;
      }
  
      double upperZ = v0._z;
      if (v1._z > upperZ)
      {
         upperZ = v1._z;
      }
      if (v2._z > upperZ)
      {
         upperZ = v2._z;
      }
      if (v3._z > upperZ)
      {
         upperZ = v3._z;
      }
      if (v4._z > upperZ)
      {
         upperZ = v4._z;
      }
  
  
      _tileExtent = new Box(new Vector3D(lowerX, lowerY, lowerZ), new Vector3D(upperX, upperY, upperZ));
    }
    return _tileExtent;
  }

  private int _elevationDataLevel;
  private ElevationData _elevationData;
  private boolean _mustActualizeMeshDueToNewElevationData;
  private ElevationDataProvider _lastElevationDataProvider;
  private int _lastTileMeshResolutionX;
  private int _lastTileMeshResolutionY;

  public Tile(TileTexturizer texturizer, Tile parent, Sector sector, int level, int row, int column)
  {
     _texturizer = texturizer;
     _parent = parent;
     _sector = new Sector(sector);
     _level = level;
     _row = row;
     _column = column;
     _tessellatorMesh = null;
     _debugMesh = null;
     _texturizedMesh = null;
     _textureSolved = false;
     _texturizerDirty = true;
     _subtiles = null;
     _justCreatedSubtiles = false;
     _isVisible = false;
     _texturizerData = null;
     _tileExtent = null;
     _elevationData = null;
     _elevationDataLevel = -1;
     _elevationDataRequest = null;
     _minHeight = 0;
     _maxHeight = 0;
     _verticalExaggeration = 0F;
     _mustActualizeMeshDueToNewElevationData = false;
     _lastTileMeshResolutionX = -1;
     _lastTileMeshResolutionY = -1;
    //  int __remove_tile_print;
    //  printf("Created tile=%s\n deltaLat=%s deltaLon=%s\n",
    //         getKey().description().c_str(),
    //         _sector.getDeltaLatitude().description().c_str(),
    //         _sector.getDeltaLongitude().description().c_str()
    //         );
  }

  public void dispose()
  {
    prune(null, null);
  
    if (_debugMesh != null)
       _debugMesh.dispose();
    _debugMesh = null;
  
    if (_tessellatorMesh != null)
       _tessellatorMesh.dispose();
    _tessellatorMesh = null;
  
    if (_texturizerData != null)
       _texturizerData.dispose();
    _texturizerData = null;
  
    if (_texturizedMesh != null)
       _texturizedMesh.dispose();
    _texturizedMesh = null;
  
    if (_tileExtent != null)
       _tileExtent.dispose();
    _tileExtent = null;
  
    if (_elevationData != null)
       _elevationData.dispose();
    _elevationData = null;
  
    if (_elevationDataRequest != null)
    {
      _elevationDataRequest.cancelRequest(); //The listener will auto delete
      if (_elevationDataRequest != null)
         _elevationDataRequest.dispose();
      _elevationDataRequest = null;
    }
  }


  public final Sector getSector()
  {
    return _sector;
  }

  public final int getLevel()
  {
    return _level;
  }

  public final int getRow()
  {
    return _row;
  }

  public final int getColumn()
  {
    return _column;
  }

  public final Mesh getTexturizedMesh()
  {
    return _texturizedMesh;
  }

  public final Tile getParent()
  {
    return _parent;
  }

  public final void prepareForFullRendering(G3MRenderContext rc, TileRenderContext trc)
  {
    Mesh tessellatorMesh = getTessellatorMesh(rc, trc);
    if (tessellatorMesh == null)
    {
      return;
    }
  
    TileTexturizer texturizer = trc.getTexturizer();
    if (texturizer != null)
    {
      final boolean needsToCallTexturizer = (_texturizedMesh == null) || isTexturizerDirty();
  
      if (needsToCallTexturizer)
      {
        _texturizedMesh = texturizer.texturize(rc, trc, this, tessellatorMesh, _texturizedMesh);
      }
    }
  }

  public final void render(G3MRenderContext rc, TileRenderContext trc, GLState parentState, java.util.LinkedList<Tile> toVisitInNextIteration, Planet planet, Vector3D cameraNormalizedPosition, double cameraAngle2HorizonInRadians, Frustum cameraFrustumInModelCoordinates)
  {
  
    final float verticalExaggeration = trc.getVerticalExaggeration();
    if (verticalExaggeration != _verticalExaggeration)
    {
      // TODO: verticalExaggeration changed, invalidate tileExtent, Mesh, etc.
  
      _verticalExaggeration = trc.getVerticalExaggeration();
    }
  
    TilesStatistics statistics = trc.getStatistics();
    statistics.computeTileProcessed(this);
  
    if (isVisible(rc, trc, planet, cameraNormalizedPosition, cameraAngle2HorizonInRadians, cameraFrustumInModelCoordinates))
    {
      setIsVisible(true, trc.getTexturizer());
  
      statistics.computeVisibleTile(this);
  
      final boolean isRawRender = ((toVisitInNextIteration == null) || meetsRenderCriteria(rc, trc) || (trc.getParameters()._incrementalTileQuality && !_textureSolved));
  
      if (isRawRender)
      {
        rawRender(rc, trc, parentState);
        if (trc.getParameters()._renderDebug)
        {
          debugRender(rc, trc, parentState);
        }
  
        statistics.computeTileRendered(this);
  
        prune(trc.getTexturizer(), trc.getElevationDataProvider());
        //TODO: AVISAR CAMBIO DE TERRENO
      }
      else
      {
        final Geodetic2D lower = _sector.lower();
        final Geodetic2D upper = _sector.upper();
  
        final Angle splitLongitude = Angle.midAngle(lower.longitude(), upper.longitude());
  
        final Angle splitLatitude = trc.getLayerTilesRenderParameters()._mercator ? MercatorUtils.calculateSplitLatitude(lower.latitude(), upper.latitude()) : Angle.midAngle(lower.latitude(), upper.latitude());
        /*                               */
        /*                               */
  
        java.util.ArrayList<Tile> subTiles = getSubTiles(splitLatitude, splitLongitude);
        if (_justCreatedSubtiles)
        {
          trc.getLastSplitTimer().start();
          statistics.computeSplitInFrame();
          _justCreatedSubtiles = false;
        }
  
        final int subTilesSize = subTiles.size();
        for (int i = 0; i < subTilesSize; i++)
        {
          Tile subTile = subTiles.get(i);
          toVisitInNextIteration.addLast(subTile);
        }
      }
    }
    else
    {
      setIsVisible(false, trc.getTexturizer());
  
      prune(trc.getTexturizer(), trc.getElevationDataProvider());
      //TODO: AVISAR CAMBIO DE TERRENO
    }
  }

  public final TileKey getKey()
  {
    return new TileKey(_level, _row, _column);
  }

  public final void setTextureSolved(boolean textureSolved)
  {
    if (textureSolved != _textureSolved)
    {
      _textureSolved = textureSolved;
  
      if (_subtiles != null)
      {
        final int subtilesSize = _subtiles.size();
        for (int i = 0; i < subtilesSize; i++)
        {
          Tile subtile = _subtiles.get(i);
          subtile.ancestorTexturedSolvedChanged(this, _textureSolved);
        }
      }
    }
  }

  public final boolean isTextureSolved()
  {
    return _textureSolved;
  }

  public final void setTexturizerDirty(boolean texturizerDirty)
  {
    _texturizerDirty = texturizerDirty;
  }

  public final boolean isTexturizerDirty()
  {
    return _texturizerDirty;
  }

  public final boolean hasTexturizerData()
  {
    return (_texturizerData != null);
  }

  public final ITexturizerData getTexturizerData()
  {
    return _texturizerData;
  }

  public final void setTexturizerData(ITexturizerData texturizerData)
  {
    if (texturizerData != _texturizerData)
    {
      if (_texturizerData != null)
         _texturizerData.dispose();
      _texturizerData = texturizerData;
    }
  }

  public final Tile getDeepestTileContaining(Geodetic3D position)
  {
    if (_sector.contains(position))
    {
      if (_subtiles == null)
      {
        return this;
      }
  
      for (int i = 0; i < _subtiles.size(); i++)
      {
        final Tile subtile = _subtiles.get(i);
        final Tile subtileResult = subtile.getDeepestTileContaining(position);
        if (subtileResult != null)
        {
          return subtileResult;
        }
      }
    }
  
    return null;
  }

  public final void prune(TileTexturizer texturizer, ElevationDataProvider elevationDataProvider)
  {
    if (_subtiles != null)
    {
  
      //    printf("= pruned tile %s\n", getKey().description().c_str());
  
      //    TileTexturizer* texturizer = (trc == NULL) ? NULL : trc->getTexturizer();
  
      final int subtilesSize = _subtiles.size();
      for (int i = 0; i < subtilesSize; i++)
      {
        Tile subtile = _subtiles.get(i);
  
        subtile.setIsVisible(false, texturizer);
  
        subtile.prune(texturizer, elevationDataProvider);
        if (texturizer != null)
        {
          texturizer.tileToBeDeleted(subtile, subtile._texturizedMesh);
        }
  
        //      if (elevationDataProvider != NULL) {
        //        //subtile->cancelElevationDataRequest(elevationDataProvider);
        //      }
  
        if (subtile != null)
           subtile.dispose();
      }
  
      _subtiles = null;
      _subtiles = null;
  
    }
  }

  public final void toBeDeleted(TileTexturizer texturizer, ElevationDataProvider elevationDataProvider)
  {
    if (texturizer != null)
    {
      texturizer.tileToBeDeleted(this, _texturizedMesh);
    }
  
    if (elevationDataProvider != null)
    {
      //cancelElevationDataRequest(elevationDataProvider);
      if (_elevationDataRequest != null)
      {
        _elevationDataRequest.cancelRequest();
      }
    }
  }


  public final double getMinHeight()
  {
    return _minHeight;
  }
  public final double getMaxHeight()
  {
    return _maxHeight;
  }

  public final String description()
  {
    IStringBuilder isb = IStringBuilder.newStringBuilder();
    isb.addString("(Tile");
    isb.addString(" level=");
    isb.addInt(_level);
    isb.addString(", row=");
    isb.addInt(_row);
    isb.addString(", column=");
    isb.addInt(_column);
    isb.addString(", sector=");
    isb.addString(_sector.description());
    isb.addString(")");
    final String s = isb.getString();
    if (isb != null)
       isb.dispose();
    return s;
  }

  public final java.util.ArrayList<Tile> createSubTiles(Angle splitLatitude, Angle splitLongitude, boolean setParent)
  {
    final Geodetic2D lower = _sector.lower();
    final Geodetic2D upper = _sector.upper();
  
    final int nextLevel = _level + 1;
  
    final int row2 = 2 * _row;
    final int column2 = 2 * _column;
  
    java.util.ArrayList<Tile> subTiles = new java.util.ArrayList<Tile>();
  
    subTiles.add(createSubTile(lower.latitude(), lower.longitude(), splitLatitude, splitLongitude, nextLevel, row2, column2, setParent));
  
    subTiles.add(createSubTile(lower.latitude(), splitLongitude, splitLatitude, upper.longitude(), nextLevel, row2, column2 + 1, setParent));
  
    subTiles.add(createSubTile(splitLatitude, lower.longitude(), upper.latitude(), splitLongitude, nextLevel, row2 + 1, column2, setParent));
  
    subTiles.add(createSubTile(splitLatitude, splitLongitude, upper.latitude(), upper.longitude(), nextLevel, row2 + 1, column2 + 1, setParent));
  
    return subTiles;
  }

  public final boolean isElevationDataSolved()
  {
    return (_elevationDataLevel == _level);
  }

  public final ElevationData getElevationData()
  {
    return _elevationData;
  }

  public final void setElevationData(ElevationData ed, int level)
  {
    if (_elevationDataLevel < level)
    {
  
      if (_elevationData != null)
      {
        if (_elevationData != null)
           _elevationData.dispose();
      }
  
      _elevationData = ed;
      _elevationDataLevel = level;
      _mustActualizeMeshDueToNewElevationData = true;
  
      //If the elevation belongs to tile's level, we notify the sub-tree
      if (isElevationDataSolved())
      {
        if (_subtiles != null)
        {
          final int subtilesSize = _subtiles.size();
          for (int i = 0; i < subtilesSize; i++)
          {
            Tile subtile = _subtiles.get(i);
            subtile.ancestorChangedElevationData(this);
          }
        }
      }
  
    }
  }

  public final void getElevationDataFromAncestor(Vector2I extent)
  {
    if (_elevationData == null)
    {
      Tile ancestor = getParent();
      while ((ancestor != null) && !ancestor.isElevationDataSolved())
      {
        ancestor = ancestor.getParent();
      }
  
      if (ancestor != null)
      {
        ElevationData subView = createElevationDataSubviewFromAncestor(ancestor);
        setElevationData(subView, ancestor.getLevel());
      }
    }
    else
    {
      System.out.print("break point on me\n");
    }
  }

  public final void initializeElevationData(ElevationDataProvider elevationDataProvider, TileTessellator tessellator, Vector2I tileMeshResolution, Planet planet, boolean renderDebug)
  {
    //Storing for subviewing
    _lastElevationDataProvider = elevationDataProvider;
    _lastTileMeshResolutionX = tileMeshResolution._x;
    _lastTileMeshResolutionY = tileMeshResolution._y;
    if (_elevationDataRequest == null)
    {
  //    const Sector caceresSector = Sector::fromDegrees(39.4642996294239623,
  //                                                     -6.3829977122432933,
  //                                                     39.4829891936013553,
  //                                                     -6.3645288909498845);
  //
  //    if (caceresSector.touchesWith(_sector)) {
  //      printf("break point on me\n");
  //    }
  
      final Vector2I res = tessellator.getTileMeshResolution(planet, tileMeshResolution, this, renderDebug);
      _elevationDataRequest = new TileElevationDataRequest(this, res, elevationDataProvider);
      _elevationDataRequest.sendRequest();
    }
  
    //If after petition we still have no data we request from ancestor
    if (_elevationData == null)
    {
      getElevationDataFromAncestor(tileMeshResolution);
    }
  
  }

  public final void ancestorChangedElevationData(Tile ancestor)
  {
  
    if (ancestor.getLevel() > _elevationDataLevel)
    {
      ElevationData subView = createElevationDataSubviewFromAncestor(ancestor);
      if (subView != null)
      {
        setElevationData(subView, ancestor.getLevel());
      }
    }
  
    if (_subtiles != null)
    {
      final int subtilesSize = _subtiles.size();
      for (int i = 0; i < subtilesSize; i++)
      {
        Tile subtile = _subtiles.get(i);
        subtile.ancestorChangedElevationData(this);
      }
    }
  }

  public final ElevationData createElevationDataSubviewFromAncestor(Tile ancestor)
  {
    ElevationData ed = ancestor.getElevationData();
  
    if (ed == null)
    {
      ILogger.instance().logError("Ancestor can't have undefined Elevation Data.");
      return null;
    }
  
    if (ed.getExtentWidth() < 1 || ed.getExtentHeight() < 1)
    {
      ILogger.instance().logWarning("Tile too small for ancestor elevation data.");
      return null;
    }
  
    if ((_lastElevationDataProvider != null) && (_lastTileMeshResolutionX > 0) && (_lastTileMeshResolutionY > 0))
    {
  //    ElevationData* subView = _lastElevationDataProvider->createSubviewOfElevationData(ed,
  //                                                                                      getSector(),
  //                                                                                      Vector2I(_lastTileMeshResolutionX, _lastTileMeshResolutionY));
  //    return subView;
  
      return new SubviewElevationData(ed, getSector(), new Vector2I(_lastTileMeshResolutionX, _lastTileMeshResolutionY), true);
                                      //bool ownsElevationData,
    }
  
    ILogger.instance().logError("Can't create subview of elevation data from ancestor");
    return null;
  
  }

}
//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#pragma mark ElevationData methods
