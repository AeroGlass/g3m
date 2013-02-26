package org.glob3.mobile.generated; 
//
//  MultiLayerTileTexturizer.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 08/08/12.
//
//

//
//  MultiLayerTileTexturizer.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 08/08/12.
//
//



//class IGLTextureId;
//class TileTextureBuilder;
//class LayerSet;
//class IDownloader;
//class LeveledTexturedMesh;
//class IFloatBuffer;


public class MultiLayerTileTexturizer extends TileTexturizer
{
  private TilesRenderParameters _parameters;

  private TexturesHandler _texturesHandler;

  private LeveledTexturedMesh getMesh(Tile tile)
  {
    TileTextureBuilderHolder tileBuilderHolder = (TileTextureBuilderHolder) tile.getTexturizerData();
    return (tileBuilderHolder == null) ? null : tileBuilderHolder.get().getMesh();
  }

  public MultiLayerTileTexturizer()
  {
     _parameters = null;
     _texturesHandler = null;
  
  }

  public void dispose()
  {
  
  }

  public final boolean isReady(G3MRenderContext rc, LayerSet layerSet)
  {
    if (layerSet != null)
    {
      return layerSet.isReady();
    }
    return true;
  }

  public final void initialize(G3MContext context, TilesRenderParameters parameters)
  {
    _parameters = parameters;
    //  _layerSet->initialize(ic);
  }

  public final Mesh texturize(G3MRenderContext rc, TileRenderContext trc, Tile tile, Mesh tessellatorMesh, Mesh previousMesh)
  {
    _texturesHandler = rc.getTexturesHandler();
  
  
    TileTextureBuilderHolder builderHolder = (TileTextureBuilderHolder) tile.getTexturizerData();
  
    if (builderHolder == null)
    {
      builderHolder = new TileTextureBuilderHolder(new TileTextureBuilder(this, rc, trc.getLayerSet(), rc.getDownloader(), tile, tessellatorMesh, trc.getTessellator(), trc.getTexturePriority()));
      tile.setTexturizerData(builderHolder);
    }
  
    TileTextureBuilder builder = builderHolder.get();
    if (trc.isForcedFullRender())
    {
      builder.start();
    }
    else
    {
      rc.getFrameTasksExecutor().addPreRenderTask(new BuilderStartTask(builder));
    }
  
    tile.setTexturizerDirty(false);
    return builder.getMesh();
  }

  public final void tileToBeDeleted(Tile tile, Mesh mesh)
  {
  
    TileTextureBuilderHolder builderHolder = (TileTextureBuilderHolder) tile.getTexturizerData();
  
    if (builderHolder != null)
    {
      TileTextureBuilder builder = builderHolder.get();
      builder.cancel();
      builder.cleanTile();
      builder.cleanMesh();
    }
    else
    {
      if (mesh != null)
      {
        ILogger.instance().logInfo("break (point) on me 4\n");
      }
    }
  }

  public final boolean tileMeetsRenderCriteria(Tile tile)
  {
    return false;
  }

  public final void justCreatedTopTile(G3MRenderContext rc, Tile tile, LayerSet layerSet)
  {
  }

  public final void ancestorTexturedSolvedChanged(Tile tile, Tile ancestorTile, boolean textureSolved)
  {
    if (!textureSolved)
    {
      return;
    }
  
    if (tile.isTextureSolved())
    {
      return;
    }
  
    LeveledTexturedMesh ancestorMesh = getMesh(ancestorTile);
    if (ancestorMesh == null)
    {
      return;
    }
  
    final IGLTextureId glTextureId = ancestorMesh.getTopLevelGLTextureId();
    if (glTextureId == null)
    {
      return;
    }
  
    LeveledTexturedMesh tileMesh = getMesh(tile);
    if (tileMesh == null)
    {
      return;
    }
  
    final int level = tile.getLevel() - ancestorTile.getLevel();
    _texturesHandler.retainGLTextureId(glTextureId);
    if (!tileMesh.setGLTextureIdForLevel(level, glTextureId))
    {
      _texturesHandler.releaseGLTextureId(glTextureId);
    }
  }

  public final IGLTextureId getTopLevelGLTextureIdForTile(Tile tile)
  {
    LeveledTexturedMesh mesh = (LeveledTexturedMesh) tile.getTexturizedMesh();
  
    return (mesh == null) ? null : mesh.getTopLevelGLTextureId();
  }

  public final void onTerrainTouchEvent(G3MEventContext ec, Geodetic3D position, Tile tile, LayerSet layerSet)
  {
    if (layerSet != null)
    {
      layerSet.onTerrainTouchEvent(ec, position, tile);
    }
  }

  public final void tileMeshToBeDeleted(Tile tile, Mesh mesh)
  {
    TileTextureBuilderHolder builderHolder = (TileTextureBuilderHolder) tile.getTexturizerData();
    if (builderHolder != null)
    {
      TileTextureBuilder builder = builderHolder.get();
      builder.cancel();
      builder.cleanMesh();
    }
    else
    {
      if (mesh != null)
      {
        ILogger.instance().logInfo("break (point) on me 5\n");
      }
    }
  }

}