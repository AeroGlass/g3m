package org.glob3.mobile.generated; 
public class TileRenderer extends LeafRenderer implements LayerSetChangedListener
{
  private final TileTessellator _tessellator;
  private ElevationDataProvider _elevationDataProvider;
  private TileTexturizer _texturizer;
  private LayerSet _layerSet;
  private final TilesRenderParameters _parameters;
  private final boolean _showStatistics;

  private Camera     _lastCamera;
  private G3MContext _context;

  private java.util.ArrayList<Tile> _firstLevelTiles = new java.util.ArrayList<Tile>();
  private boolean _firstLevelTilesJustCreated;
  private boolean _allFirstLevelTilesAreTextureSolved;

  private ITimer _lastSplitTimer; // timer to start every time a tile get splitted into subtiles

  private void clearFirstLevelTiles()
  {
    final int firstLevelTilesCount = _firstLevelTiles.size();
    for (int i = 0; i < firstLevelTilesCount; i++)
    {
      Tile tile = _firstLevelTiles.get(i);
  
      tile.toBeDeleted(_texturizer, _elevationDataProvider);
  
      if (tile != null)
         tile.dispose();
    }
  
    _firstLevelTiles.clear();
  }
  private void createFirstLevelTiles(G3MContext context)
  {
  
    final LayerTilesRenderParameters parameters = _layerSet.getLayerTilesRenderParameters();
    if (parameters == null)
    {
      ILogger.instance().logError("LayerSet returned a NULL for LayerTilesRenderParameters, can't create first-level tiles");
      return;
    }
  
    java.util.ArrayList<Tile> topLevelTiles = new java.util.ArrayList<Tile>();
  
    final Angle fromLatitude = parameters._topSector.lower().latitude();
    final Angle fromLongitude = parameters._topSector.lower().longitude();
  
    final Angle deltaLan = parameters._topSector.getDeltaLatitude();
    final Angle deltaLon = parameters._topSector.getDeltaLongitude();
  
    final int topSectorSplitsByLatitude = parameters._topSectorSplitsByLatitude;
    final int topSectorSplitsByLongitude = parameters._topSectorSplitsByLongitude;
  
    final Angle tileHeight = deltaLan.div(topSectorSplitsByLatitude);
    final Angle tileWidth = deltaLon.div(topSectorSplitsByLongitude);
  
    for (int row = 0; row < topSectorSplitsByLatitude; row++)
    {
      final Angle tileLatFrom = tileHeight.times(row).add(fromLatitude);
      final Angle tileLatTo = tileLatFrom.add(tileHeight);
  
      for (int col = 0; col < topSectorSplitsByLongitude; col++)
      {
        final Angle tileLonFrom = tileWidth.times(col).add(fromLongitude);
        final Angle tileLonTo = tileLonFrom.add(tileWidth);
  
        final Geodetic2D tileLower = new Geodetic2D(tileLatFrom, tileLonFrom);
        final Geodetic2D tileUpper = new Geodetic2D(tileLatTo, tileLonTo);
        final Sector sector = new Sector(tileLower, tileUpper);
  
        Tile tile = new Tile(_texturizer, null, sector, 0, row, col);
        if (parameters._firstLevel == 0)
        {
          _firstLevelTiles.add(tile);
        }
        else
        {
          topLevelTiles.add(tile);
        }
      }
    }
  
    if (parameters._firstLevel > 0)
    {
      final int topLevelTilesSize = topLevelTiles.size();
      for (int i = 0; i < topLevelTilesSize; i++)
      {
        Tile tile = topLevelTiles.get(i);
        createFirstLevelTiles(_firstLevelTiles, tile, parameters._firstLevel, parameters._mercator);
      }
    }
  
    sortTiles(_firstLevelTiles);
  
    context.getLogger().logInfo("Created %d first level tiles", _firstLevelTiles.size());
  
    _firstLevelTilesJustCreated = true;
  }
  private void createFirstLevelTiles(java.util.ArrayList<Tile> firstLevelTiles, Tile tile, int firstLevel, boolean mercator)
  {
    if (tile.getLevel() == firstLevel)
    {
      firstLevelTiles.add(tile);
    }
    else
    {
      final Sector sector = tile.getSector();
      final Geodetic2D lower = sector.lower();
      final Geodetic2D upper = sector.upper();
  
      final Angle splitLongitude = Angle.midAngle(lower.longitude(), upper.longitude());
  
      final Angle splitLatitude = mercator ? MercatorUtils.calculateSplitLatitude(lower.latitude(), upper.latitude()) : Angle.midAngle(lower.latitude(), upper.latitude());
      /*                               */
      /*                               */
  
  
      java.util.ArrayList<Tile> children = tile.createSubTiles(splitLatitude, splitLongitude, false);
  
      final int childrenSize = children.size();
      for (int i = 0; i < childrenSize; i++)
      {
        Tile child = children.get(i);
        createFirstLevelTiles(firstLevelTiles, child, firstLevel, mercator);
      }
  
      children = null;
      if (tile != null)
         tile.dispose();
    }
  }

  private void sortTiles(java.util.ArrayList<Tile> tiles)
  {
    java.util.Collections.sort(tiles,
                               new java.util.Comparator<Tile>() {
                                 @Override
                                 public int compare(final Tile i,
                                                    final Tile j) {
                                   final int rowI = i.getRow();
                                   final int rowJ = j.getRow();
                                   if (rowI < rowJ) {
                                     return -1;
                                   }
                                   if (rowI > rowJ) {
                                     return 1;
                                   }
  
                                   final int columnI = i.getColumn();
                                   final int columnJ = j.getColumn();
                                   if (columnI < columnJ) {
                                     return -1;
                                   }
                                   if (columnI > columnJ) {
                                     return 1;
                                   }
                                   return 0;
                                 }
                               });
  }

  private boolean _firstRender;

  private void pruneFirstLevelTiles()
  {
    final int firstLevelTilesCount = _firstLevelTiles.size();
    for (int i = 0; i < firstLevelTilesCount; i++)
    {
      Tile tile = _firstLevelTiles.get(i);
      tile.prune(_texturizer, _elevationDataProvider);
    }
  }

  private Sector _lastVisibleSector;

  private java.util.ArrayList<VisibleSectorListenerEntry> _visibleSectorListeners = new java.util.ArrayList<VisibleSectorListenerEntry>();

  private long _texturePriority;

  private float _verticalExaggeration;

  private boolean isReadyToRenderTiles(G3MRenderContext rc)
  {
    if (!_layerSet.isReady())
    {
      return false;
    }
  
    if (_elevationDataProvider != null)
    {
      if (!_elevationDataProvider.isReadyToRender(rc))
      {
        return false;
      }
    }
  
    if (_firstLevelTilesJustCreated)
    {
      _firstLevelTilesJustCreated = false;
  
      final int firstLevelTilesCount = _firstLevelTiles.size();
  
      if (_parameters._forceFirstLevelTilesRenderOnStart)
      {
        TilesStatistics statistics = new TilesStatistics();
  
        TileRenderContext trc = new TileRenderContext(_tessellator, _elevationDataProvider, _texturizer, _layerSet, _parameters, statistics, _lastSplitTimer, true, _texturePriority, _verticalExaggeration);
  
        for (int i = 0; i < firstLevelTilesCount; i++)
        {
          Tile tile = _firstLevelTiles.get(i);
          tile.prepareForFullRendering(rc, trc);
        }
      }
  
      if (_texturizer != null)
      {
        for (int i = 0; i < firstLevelTilesCount; i++)
        {
          Tile tile = _firstLevelTiles.get(i);
          _texturizer.justCreatedTopTile(rc, tile, _layerSet);
        }
      }
    }
  
    if (_parameters._forceFirstLevelTilesRenderOnStart)
    {
      if (!_allFirstLevelTilesAreTextureSolved)
      {
        final int firstLevelTilesCount = _firstLevelTiles.size();
        for (int i = 0; i < firstLevelTilesCount; i++)
        {
          Tile tile = _firstLevelTiles.get(i);
          if (!tile.isTextureSolved())
          {
            return false;
          }
        }
  
        if (_tessellator != null)
        {
          if (!_tessellator.isReady(rc))
          {
            return false;
          }
        }
  
        if (_texturizer != null)
        {
          if (!_texturizer.isReady(rc, _layerSet))
          {
            return false;
          }
        }
  
        _allFirstLevelTilesAreTextureSolved = true;
      }
    }
  
    return true;
  }
  private void renderIncompletePlanet(G3MRenderContext rc, GLState parentState)
  {
  
    if (_incompleteShape == null)
    {
      final short resolution = 16;
      final float borderWidth = 0F;
      final boolean texturedInside = false;
      final boolean mercator = false;
  
  //    Color* surfaceColor = Color::newFromRGBA(0.5f, 0.5f, 0.5f, 0.5f);
  //    Color* borderColor  = Color::newFromRGBA(1, 1, 1, 1);
  
  //    _incompleteShape = new EllipsoidShape(new Geodetic3D(Angle::zero(), Angle::zero(), 0),
  //                                          rc->getPlanet()->getRadii(),
  //                                          resolution,
  //                                          borderWidth,
  //                                          texturedInside,
  //                                          mercator,
  //                                          surfaceColor,
  //                                          borderColor);
  
      _incompleteShape = new EllipsoidShape(new Geodetic3D(Angle.zero(), Angle.zero(), 0), _parameters._incompletePlanetTexureURL, rc.getPlanet().getRadii(), resolution, borderWidth, texturedInside, mercator);
  
    }
  
    _incompleteShape.rawRender(rc, parentState, true);
  }

  private EllipsoidShape _incompleteShape;


  public TileRenderer(TileTessellator tessellator, ElevationDataProvider elevationDataProvider, float verticalExaggeration, TileTexturizer texturizer, LayerSet layerSet, TilesRenderParameters parameters, boolean showStatistics, long texturePriority)
  {
     _tessellator = tessellator;
     _elevationDataProvider = elevationDataProvider;
     _verticalExaggeration = verticalExaggeration;
     _texturizer = texturizer;
     _layerSet = layerSet;
     _parameters = parameters;
     _showStatistics = showStatistics;
     _firstLevelTilesJustCreated = false;
     _lastSplitTimer = null;
     _lastCamera = null;
     _firstRender = false;
     _context = null;
     _lastVisibleSector = null;
     _texturePriority = texturePriority;
     _allFirstLevelTilesAreTextureSolved = false;
     _incompleteShape = null;
    _layerSet.setChangeListener(this);
  }

  public void dispose()
  {
    clearFirstLevelTiles();
  
    if (_incompleteShape != null)
       _incompleteShape.dispose();
  
    if (_tessellator != null)
       _tessellator.dispose();
    if (_elevationDataProvider != null)
       _elevationDataProvider.dispose();
    if (_texturizer != null)
       _texturizer.dispose();
    if (_parameters != null)
       _parameters.dispose();
  
    if (_lastSplitTimer != null)
       _lastSplitTimer.dispose();
  
    if (_lastVisibleSector != null)
       _lastVisibleSector.dispose();
  
    final int visibleSectorListenersCount = _visibleSectorListeners.size();
    for (int i = 0; i < visibleSectorListenersCount; i++)
    {
      VisibleSectorListenerEntry entry = _visibleSectorListeners.get(i);
      if (entry != null)
         entry.dispose();
    }
  }

  public final void initialize(G3MContext context)
  {
    _context = context;
  
    clearFirstLevelTiles();
    createFirstLevelTiles(context);
  
    if (_lastSplitTimer != null)
       _lastSplitTimer.dispose();
    _lastSplitTimer = context.getFactory().createTimer();
  
    _layerSet.initialize(context);
    _texturizer.initialize(context, _parameters);
    if (_elevationDataProvider != null)
    {
      _elevationDataProvider.initialize(context);
    }
  }

  public final void render(G3MRenderContext rc, GLState parentState)
  {
  
    if (!isReadyToRenderTiles(rc) && _parameters._renderIncompletePlanet)
    {
      renderIncompletePlanet(rc, parentState);
      return;
    }
  
  
    // Saving camera for use in onTouchEvent
    _lastCamera = rc.getCurrentCamera();
  
    TilesStatistics statistics = new TilesStatistics();
  
    TileRenderContext trc = new TileRenderContext(_tessellator, _elevationDataProvider, _texturizer, _layerSet, _parameters, statistics, _lastSplitTimer, _firstRender, _texturePriority, _verticalExaggeration); // if first render, force full render
  
    final int firstLevelTilesCount = _firstLevelTiles.size();
  
    final Planet planet = rc.getPlanet();
    final Vector3D cameraNormalizedPosition = _lastCamera.getNormalizedPosition();
    double cameraAngle2HorizonInRadians = _lastCamera.getAngle2HorizonInRadians();
    final Frustum cameraFrustumInModelCoordinates = _lastCamera.getFrustumInModelCoordinates();
  
    if (_firstRender && _parameters._forceFirstLevelTilesRenderOnStart)
    {
      // force one render pass of the firstLevelTiles tiles to make the (toplevel) textures
      // loaded as they will be used as last-chance fallback texture for any tile.
      _firstRender = false;
  
      for (int i = 0; i < firstLevelTilesCount; i++)
      {
        Tile tile = _firstLevelTiles.get(i);
        tile.render(rc, trc, parentState, null, planet, cameraNormalizedPosition, cameraAngle2HorizonInRadians, cameraFrustumInModelCoordinates);
      }
    }
    else
    {
      java.util.LinkedList<Tile> toVisit = new java.util.LinkedList<Tile>();
      for (int i = 0; i < firstLevelTilesCount; i++)
      {
        toVisit.addLast(_firstLevelTiles.get(i));
      }
  
      while (toVisit.size() > 0)
      {
        java.util.LinkedList<Tile> toVisitInNextIteration = new java.util.LinkedList<Tile>();
  
        for (java.util.Iterator<Tile> iter = toVisit.iterator(); iter.hasNext();)
        {
          Tile tile = iter.next();
  
          tile.render(rc, trc, parentState, toVisitInNextIteration, planet, cameraNormalizedPosition, cameraAngle2HorizonInRadians, cameraFrustumInModelCoordinates);
        }
  
        toVisit = toVisitInNextIteration;
      }
    }
  
    if (_showStatistics)
    {
      statistics.log(rc.getLogger());
    }
  
  
    final Sector renderedSector = statistics.getRenderedSector();
    if (renderedSector != null)
    {
      if ((_lastVisibleSector == null) || !renderedSector.isEqualsTo(_lastVisibleSector))
      {
        if (_lastVisibleSector != null)
           _lastVisibleSector.dispose();
        _lastVisibleSector = new Sector(renderedSector);
      }
    }
  
    if (_lastVisibleSector != null)
    {
      final int visibleSectorListenersCount = _visibleSectorListeners.size();
      for (int i = 0; i < visibleSectorListenersCount; i++)
      {
        VisibleSectorListenerEntry entry = _visibleSectorListeners.get(i);
  
        entry.tryToNotifyListener(_lastVisibleSector, rc);
      }
    }
  
  }

  public final boolean onTouchEvent(G3MEventContext ec, TouchEvent touchEvent)
  {
    if (_lastCamera == null)
    {
      return false;
    }
  
    if (touchEvent.getType() == TouchEventType.LongPress)
    {
      final Vector2I pixel = touchEvent.getTouch(0).getPos();
      final Vector3D ray = _lastCamera.pixel2Ray(pixel);
      final Vector3D origin = _lastCamera.getCartesianPosition();
  
      final Planet planet = ec.getPlanet();
  
      final Vector3D positionCartesian = planet.closestIntersection(origin, ray);
      if (positionCartesian.isNan())
      {
        return false;
      }
  
      final Geodetic3D position = planet.toGeodetic3D(positionCartesian);
  
      final int firstLevelTilesCount = _firstLevelTiles.size();
      for (int i = 0; i < firstLevelTilesCount; i++)
      {
        final Tile tile = _firstLevelTiles.get(i).getDeepestTileContaining(position);
        if (tile != null)
        {
          ILogger.instance().logInfo("Touched on %s", tile.description());
          if (_texturizer.onTerrainTouchEvent(ec, position, tile, _layerSet))
          {
            return true;
          }
        }
      }
  
    }
  
    return false;
  }

  public final void onResizeViewportEvent(G3MEventContext ec, int width, int height)
  {

  }

  public final boolean isReadyToRender(G3MRenderContext rc)
  {
    return isReadyToRenderTiles(rc) || _parameters._renderIncompletePlanet;
  }


  public final void start(G3MRenderContext rc)
  {
    _firstRender = true;
  }

  public final void stop(G3MRenderContext rc)
  {
    _firstRender = false;
  }

  public final void onResume(G3MContext context)
  {

  }

  public final void onPause(G3MContext context)
  {
    recreateTiles();
  }

  public final void onDestroy(G3MContext context)
  {

  }

  public final void setEnable(boolean enable)
  {
    super.setEnable(enable);

    if (!enable)
    {
      pruneFirstLevelTiles();
    }
  }

  public final void changed(LayerSet layerSet)
  {
    // recreateTiles();
  
    // recreateTiles() delete tiles, then meshes, and delete textures from the GPU so it has to be executed in the OpenGL thread
    _context.getThreadUtils().invokeInRendererThread(new RecreateTilesTask(this), true);
  }

  public final void recreateTiles()
  {
    pruneFirstLevelTiles();
    clearFirstLevelTiles();
    _firstRender = true;
    _allFirstLevelTilesAreTextureSolved = false;
    createFirstLevelTiles(_context);
  }

  /**
   Answer the visible-sector, it can be null if globe was not yet rendered.
   */
  public final Sector getVisibleSector()
  {
    return _lastVisibleSector;
  }

  /**
   Add a listener for notification of visible-sector changes.

   @param stabilizationInterval How many time the visible-sector has to be settled (without changes) before triggering the event.  Useful for avoid process while the camera is being moved (as in animations).  If stabilizationInterval is zero, the event is triggered inmediatly.
   */
  public final void addVisibleSectorListener(VisibleSectorListener listener, TimeInterval stabilizationInterval)
  {
    _visibleSectorListeners.add(new VisibleSectorListenerEntry(listener, stabilizationInterval));
  }

  /**
   Add a listener for notification of visible-sector changes.

   The event is triggered immediately without waiting for the visible-sector get settled.
   */
  public final void addVisibleSectorListener(VisibleSectorListener listener)
  {
    addVisibleSectorListener(listener, TimeInterval.zero());
  }

  /**
   * Set the download-priority used by Tiles (for downloading textures).
   *
   * @param texturePriority: new value for download priority of textures
   */
  public final void setTexturePriority(long texturePriority)
  {
    _texturePriority = texturePriority;
  }

  /**
   * Return the current value for the download priority of textures
   *
   * @return _texturePriority: long
   */
  public final long getTexturePriority()
  {
    return _texturePriority;
  }

  /**
   * @see Renderer#isTileRenderer()
   */
  public final boolean isTileRenderer()
  {
    return true;
  }

}