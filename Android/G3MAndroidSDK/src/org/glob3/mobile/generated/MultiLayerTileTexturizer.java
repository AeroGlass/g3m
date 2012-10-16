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



//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class IGLTextureId;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class TileTextureBuilder;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class LayerSet;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class IDownloader;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class LeveledTexturedMesh;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class IFloatBuffer;


public class MultiLayerTileTexturizer extends TileTexturizer
{
//  LayerSet* _layerSet;

  private TilesRenderParameters _parameters;

  private IFloatBuffer _texCoordsCache;

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: IFloatBuffer* getTextureCoordinates(const TileRenderContext* trc) const
  private IFloatBuffer getTextureCoordinates(TileRenderContext trc)
  {
	if (_texCoordsCache == null)
	{
	  _texCoordsCache = trc.getTessellator().createUnitTextCoords();
	}
	return _texCoordsCache;
  }

  private int _pendingTopTileRequests;

  private TexturesHandler _texturesHandler;

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: LeveledTexturedMesh* getMesh(Tile* tile) const
  private LeveledTexturedMesh getMesh(Tile tile)
  {
	TileTextureBuilderHolder tileBuilderHolder = (TileTextureBuilderHolder) tile.getTexturizerData();
	return (tileBuilderHolder == null) ? null : tileBuilderHolder.get().getMesh();
  }

  public MultiLayerTileTexturizer()
  {
	  _parameters = null;
	  _texCoordsCache = null;
	  _pendingTopTileRequests = 0;
	  _texturesHandler = null;
  
  }

  public final void countTopTileRequest()
  {
	_pendingTopTileRequests--;
  }

  public void dispose()
  {
	if (_texCoordsCache != null)
	{
	  if (_texCoordsCache != null)
		  _texCoordsCache.dispose();
	  _texCoordsCache = null;
	}
  }

  public final boolean isReady(RenderContext rc, LayerSet layerSet)
  {
	if (_pendingTopTileRequests > 0)
	{
	  return false;
	}
	if (layerSet != null)
	{
	  return layerSet.isReady();
	}
	return true;
	//  return (_pendingTopTileRequests <= 0) && _layerSet->isReady();
  }

  public final void initialize(InitializationContext ic, TilesRenderParameters parameters)
  {
	_parameters = parameters;
  //  _layerSet->initialize(ic);
  }

  public final Mesh texturize(RenderContext rc, TileRenderContext trc, Tile tile, Mesh tessellatorMesh, Mesh previousMesh)
  {
	_texturesHandler = rc.getTexturesHandler();
  
  
	TileTextureBuilderHolder builderHolder = (TileTextureBuilderHolder) tile.getTexturizerData();
  
	if (builderHolder == null)
	{
	  builderHolder = new TileTextureBuilderHolder(new TileTextureBuilder(this, rc, trc.getLayerSet(), _parameters, rc.getDownloader(), tile, tessellatorMesh, getTextureCoordinates(trc)));
	  tile.setTexturizerData(builderHolder);
	}
  
	if (trc.isForcedFullRender())
	{
	  builderHolder.get().start();
	}
	else
	{
//C++ TO JAVA CONVERTER TODO TASK: Java does not allow declaring types within methods:
//	  class BuilderStartTask : public FrameTask
//	  {
//	  private:
//		TileTextureBuilder* _builder;
//  
//	  public:
//		BuilderStartTask(TileTextureBuilder* builder) : _builder(builder)
//		{
//		  _builder->_retain();
//		}
//  
//		virtual ~BuilderStartTask()
//		{
//		  _builder->_release();
//		}
//  
//		void execute(const RenderContext* rc)
//		{
//		  _builder->start();
//		}
//  
//		boolean isCanceled(const RenderContext *rc)
//		{
//		  return _builder->isCanceled();
//		}
//	  };
	  rc.getFrameTasksExecutor().addPreRenderTask(new BuilderStartTask(builderHolder.get()));
	}
  
	tile.setTexturizerDirty(false);
	return builderHolder.get().getMesh();
  }

  public final void tileToBeDeleted(Tile tile, Mesh mesh)
  {
  
	TileTextureBuilderHolder builderHolder = (TileTextureBuilderHolder) tile.getTexturizerData();
  
	if (builderHolder != null)
	{
	  builderHolder.get().cancel();
	  builderHolder.get().cleanTile();
	  builderHolder.get().cleanMesh();
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

  public final void justCreatedTopTile(RenderContext rc, Tile tile, LayerSet layerSet)
  {
	java.util.ArrayList<Petition> petitions = layerSet.createTileMapPetitions(rc, tile, _parameters._tileTextureWidth, _parameters._tileTextureHeight);
  
	_pendingTopTileRequests += petitions.size();
  
	final int priority = 1000000000; // very big priority for toplevel tiles
	for (int i = 0; i < petitions.size(); i++)
	{
	  final Petition petition = petitions.get(i);
	  rc.getDownloader().requestImage(new URL(petition.getURL()), priority, new TopTileDownloadListener(this), true);
  
	  if (petition != null)
		  petition.dispose();
	}
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
  
	final int level = tile.getLevel() - ancestorTile.getLevel() - _parameters._topLevel;
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

  public final void onTerrainTouchEvent(EventContext ec, Geodetic3D position, Tile tile, LayerSet layerSet)
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
	  builderHolder.get().cancel();
	  builderHolder.get().cleanMesh();
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