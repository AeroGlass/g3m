package org.glob3.mobile.generated; 
public class LayerSet
{
  private java.util.ArrayList<Layer> _layers = new java.util.ArrayList<Layer>();

  private LayerSetChangedListener _listener;

  private LayerTilesRenderParameters _layerTilesRenderParameters;


  private LayerTilesRenderParameters createLayerTilesRenderParameters()
  {
    Sector topSector = null;
    int topSectorSplitsByLatitude = 0;
    int topSectorSplitsByLongitude = 0;
    int firstLevel = 0;
    int maxLevel = 0;
    int tileTextureWidth = 0;
    int tileTextureHeight = 0;
    int tileMeshWidth = 0;
    int tileMeshHeight = 0;
    boolean mercator = false;
  
    boolean first = true;
    final int layersCount = _layers.size();
    for (int i = 0; i < layersCount; i++)
    {
      Layer layer = _layers.get(i);
  
      if (layer.isEnable() && layer.isReady())
      {
        final LayerTilesRenderParameters layerParam = layer.getLayerTilesRenderParameters();
  
        if (layerParam == null)
        {
          continue;
        }
  
        if (first)
        {
          first = false;
  
          topSector = new Sector(layerParam._topSector);
          topSectorSplitsByLatitude = layerParam._topSectorSplitsByLatitude;
          topSectorSplitsByLongitude = layerParam._topSectorSplitsByLongitude;
          firstLevel = layerParam._firstLevel;
          maxLevel = layerParam._maxLevel;
          tileTextureWidth = layerParam._tileTextureResolution._x;
          tileTextureHeight = layerParam._tileTextureResolution._y;
          tileMeshWidth = layerParam._tileMeshResolution._x;
          tileMeshHeight = layerParam._tileMeshResolution._y;
          mercator = layerParam._mercator;
        }
        else
        {
          if (!topSector.isEqualsTo(layerParam._topSector))
          {
            ILogger.instance().logError("Inconsistency in Layer's Parameters: topSector");
            return null;
          }
  
          if (topSectorSplitsByLatitude != layerParam._topSectorSplitsByLatitude)
          {
            ILogger.instance().logError("Inconsistency in Layer's Parameters: topSectorSplitsByLatitude");
            return null;
          }
  
          if (topSectorSplitsByLongitude != layerParam._topSectorSplitsByLongitude)
          {
            ILogger.instance().logError("Inconsistency in Layer's Parameters: topSectorSplitsByLongitude");
            return null;
          }
  
          if (maxLevel < layerParam._maxLevel)
          {
            ILogger.instance().logWarning("Inconsistency in Layer's Parameters: maxLevel (upgrading from %d to %d)", maxLevel, layerParam._maxLevel);
            maxLevel = layerParam._maxLevel;
          }
  
          if (firstLevel < layerParam._firstLevel)
          {
            ILogger.instance().logWarning("Inconsistency in Layer's Parameters: firstLevel (upgrading from %d to %d)", firstLevel, layerParam._firstLevel);
            firstLevel = layerParam._firstLevel;
          }
  
          if ((tileTextureWidth != layerParam._tileTextureResolution._x) || (tileTextureHeight != layerParam._tileTextureResolution._y))
          {
            ILogger.instance().logError("Inconsistency in Layer's Parameters: tileTextureResolution");
            return null;
          }
  
          if ((tileMeshWidth != layerParam._tileMeshResolution._x) || (tileMeshHeight != layerParam._tileMeshResolution._y))
          {
            ILogger.instance().logError("Inconsistency in Layer's Parameters: tileMeshResolution");
            return null;
          }
  
          if (mercator != layerParam._mercator)
          {
            ILogger.instance().logError("Inconsistency in Layer's Parameters: mercator");
            return null;
          }
  
        }
      }
    }
  
    if (first)
    {
      ILogger.instance().logError("Can't create LayerSet's LayerTilesRenderParameters, not found any enabled Layer");
      return null;
    }
  
    LayerTilesRenderParameters parameters = new LayerTilesRenderParameters(topSector, topSectorSplitsByLatitude, topSectorSplitsByLongitude, firstLevel, maxLevel, new Vector2I(tileTextureWidth, tileTextureHeight), new Vector2I(tileMeshWidth, tileMeshHeight), mercator);
  
    if (topSector != null)
       topSector.dispose();
  
    return parameters;
  }
  private void layersChanged()
  {
    if (_layerTilesRenderParameters != null)
       _layerTilesRenderParameters.dispose();
    _layerTilesRenderParameters = null;
  
    if (_listener != null)
    {
      _listener.changed(this);
    }
  }

  private final G3MContext _context;

  public LayerSet()
  {
     _listener = null;
     _layerTilesRenderParameters = null;
     _context = null;

  }

  public void dispose()
  {
    if (_layerTilesRenderParameters != null)
       _layerTilesRenderParameters.dispose();
    for (int i = 0; i < _layers.size(); i++)
    {
      if (_layers.get(i) != null)
         _layers.get(i).dispose();
    }
  }

  public final void removeAllLayers(boolean deleteLayers)
  {
    final int layersSize = _layers.size();
    if (layersSize > 0)
    {
      for (int i = 0; i < layersSize; i++)
      {
        Layer layer = _layers.get(i);
        layer.removeLayerSet(this);
        if (deleteLayers)
        {
          if (layer != null)
             layer.dispose();
        }
      }
      _layers.clear();
  
      layersChanged();
    }
  }

  public final void addLayer(Layer layer)
  {
    layer.setLayerSet(this);
    _layers.add(layer);
  
    if (_context != null)
    {
      layer.initialize(_context);
    }
  
    layersChanged();
  }

  public final java.util.ArrayList<Petition> createTileMapPetitions(G3MRenderContext rc, Tile tile)
  {
    java.util.ArrayList<Petition> petitions = new java.util.ArrayList<Petition>();
  
    final int layersSize = _layers.size();
    for (int i = 0; i < layersSize; i++)
    {
      Layer layer = _layers.get(i);
      if (layer.isAvailable(rc, tile))
      {
  
        Tile petitionTile = tile;
        final int maxLevel = layer.getLayerTilesRenderParameters()._maxLevel;
        while ((petitionTile.getLevel() > maxLevel) && (petitionTile != null))
        {
          petitionTile = petitionTile.getParent();
        }
  
        if (petitionTile == null)
        {
          ILogger.instance().logError("Can't find a valid tile for petitions");
        }
  
        java.util.ArrayList<Petition> tilePetitions = layer.createTileMapPetitions(rc, petitionTile);
  
        final int tilePetitionsSize = tilePetitions.size();
        for (int j = 0; j < tilePetitionsSize; j++)
        {
          petitions.add(tilePetitions.get(j));
        }
      }
    }
  
    if (petitions.isEmpty())
    {
      rc.getLogger().logWarning("Can't create map petitions for tile %s", tile.getKey().description());
    }
  
    return petitions;
  }

  public final boolean onTerrainTouchEvent(G3MEventContext ec, Geodetic3D position, Tile tile)
  {
  
  
  
    for (int i = _layers.size()-1; i >= 0; i--)
    {
      Layer layer = _layers.get(i);
      if (layer.isAvailable(ec, tile))
      {
        TerrainTouchEvent tte = new TerrainTouchEvent(position, tile.getSector(), layer);
  
        if (layer.onTerrainTouchEventListener(ec, tte))
        {
          return true;
        }
      }
    }
  
    return false;
  }

  public final boolean isReady()
  {
    final int layersCount = _layers.size();
    if (layersCount < 1)
    {
      return false;
    }
  
    for (int i = 0; i < layersCount; i++)
    {
      if (!(_layers.get(i).isReady()))
      {
        return false;
      }
    }
    return true;
  }

  public final void initialize(G3MContext context)
  {
    final int layersCount = _layers.size();
  
    for (int i = 0; i < layersCount; i++)
    {
      _layers.get(i).initialize(context);
    }
  
    _context = context;
  }

  public final int size()
  {
    return _layers.size();
  }

  public final void layerChanged(Layer layer)
  {
    layersChanged();
  }

  public final void setChangeListener(LayerSetChangedListener listener)
  {
    if (_listener != null)
    {
      ILogger.instance().logError("Listener already set");
    }
    _listener = listener;
  }

  public final Layer get(int index)
  {
    if (index < _layers.size())
    {
      return _layers.get(index);
    }
  
    return null;
  }

  public final Layer getLayer(String name)
  {
    final int layersCount = _layers.size();
    for (int i = 0; i < layersCount; i++)
    {
      if (name.equals(_layers.get(i).getName()))
      {
        return _layers.get(i);
      }
    }
  
    return null;
  }

  public final LayerTilesRenderParameters getLayerTilesRenderParameters()
  {
    if (_layerTilesRenderParameters == null)
    {
      _layerTilesRenderParameters = createLayerTilesRenderParameters();
    }
    return _layerTilesRenderParameters;
  }

  //  const Angle calculateSplitLatitude(const Tile* tile) const;

}