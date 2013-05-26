package org.glob3.mobile.generated; 
public abstract class G3MCBuilder
{
  private int _sceneTimestamp;

  private final URL _serverURL;

  private String _sceneId;

  private Layer _baseLayer;

  private GL _gl;
  private boolean _glob3Created;
  private IStorage _storage;

  private LayerSet _layerSet;
  private TileRenderer createTileRenderer()
  {
    final TileTessellator tessellator = new EllipsoidalTileTessellator(true);
  
    ElevationDataProvider elevationDataProvider = null;
    final float verticalExaggeration = 1F;
    TileTexturizer texturizer = new MultiLayerTileTexturizer();
  
    final boolean renderDebug = false;
    final boolean useTilesSplitBudget = true;
    final boolean forceFirstLevelTilesRenderOnStart = true;
    final boolean incrementalTileQuality = false;
    final TilesRenderParameters parameters = new TilesRenderParameters(renderDebug, useTilesSplitBudget, forceFirstLevelTilesRenderOnStart, incrementalTileQuality);
  
    final boolean showStatistics = false;
    long texturePriority = DownloadPriority.HIGHER;
  
    return new TileRenderer(tessellator, elevationDataProvider, verticalExaggeration, texturizer, _layerSet, parameters, showStatistics, texturePriority);
  }

  private java.util.ArrayList<ICameraConstrainer> createCameraConstraints()
  {
    java.util.ArrayList<ICameraConstrainer> cameraConstraints = new java.util.ArrayList<ICameraConstrainer>();
    SimpleCameraConstrainer scc = new SimpleCameraConstrainer();
    cameraConstraints.add(scc);
  
    return cameraConstraints;
  }

  private CameraRenderer createCameraRenderer()
  {
    CameraRenderer cameraRenderer = new CameraRenderer();
    final boolean useInertia = true;
    cameraRenderer.addHandler(new CameraSingleDragHandler(useInertia));
    final boolean processRotation = true;
    final boolean processZoom = true;
    cameraRenderer.addHandler(new CameraDoubleDragHandler(processRotation, processZoom));
    cameraRenderer.addHandler(new CameraRotationHandler());
    cameraRenderer.addHandler(new CameraDoubleTapHandler());
  
    return cameraRenderer;
  }

  private Renderer createBusyRenderer()
  {
    return new BusyMeshRenderer(Color.newFromRGBA(0, 0, 0, 1));
  }

  private java.util.ArrayList<PeriodicalTask> createPeriodicalTasks()
  {
    java.util.ArrayList<PeriodicalTask> periodicalTasks = new java.util.ArrayList<PeriodicalTask>();
  
    periodicalTasks.add(new PeriodicalTask(TimeInterval.fromSeconds(15), new G3MCBuilder_PullScenePeriodicalTask(this)));
  
    return periodicalTasks;
  }

  private URL createScenesDescriptionsURL()
  {
    String serverPath = _serverURL.getPath();
  
    return new URL(serverPath + "/scenes/", false);
  }

//  LayerSet* getLayerSet();
  private void recreateLayerSet()
  {
    _layerSet.removeAllLayers(false);
    if (_baseLayer != null)
    {
      _layerSet.addLayer(_baseLayer);
    }
  }

  private IThreadUtils _threadUtils;
  private IThreadUtils getThreadUtils()
  {
    if (_threadUtils == null)
    {
      _threadUtils = createThreadUtils();
    }
    return _threadUtils;
  }

  private IDownloader _downloader;
  private IDownloader getDownloader()
  {
    if (_downloader == null)
    {
      _downloader = createDownloader();
    }
    return _downloader;
  }


  protected G3MCBuilder(URL serverURL, String sceneId)
  {
     _serverURL = serverURL;
     _sceneId = sceneId;
     _sceneTimestamp = -1;
     _gl = null;
     _glob3Created = false;
     _storage = null;
     _threadUtils = null;
     _layerSet = new LayerSet();
     _baseLayer = null;
     _downloader = null;
  
  }

  public void dispose()
  {
  }

  protected final void setGL(GL gl)
  {
    if (_gl != null)
    {
      ILogger.instance().logError("LOGIC ERROR: _gl already initialized");
      return;
    }
    if (gl == null)
    {
      ILogger.instance().logError("LOGIC ERROR: _gl cannot be NULL");
      return;
    }
    _gl = gl;
  }

  protected final GL getGL()
  {
    if (_gl == null)
    {
      ILogger.instance().logError("Logic Error: _gl not initialized");
    }
  
    return _gl;
  }

  protected final G3MWidget create()
  {
    if (_glob3Created)
    {
      ILogger.instance().logError("The G3MWidget was already created, can't create more than one");
      return null;
    }
    _glob3Created = true;
  
  
    CompositeRenderer mainRenderer = new CompositeRenderer();
  
  
    TileRenderer tileRenderer = createTileRenderer();
    mainRenderer.addRenderer(tileRenderer);
  
  
    java.util.ArrayList<ICameraConstrainer> cameraConstraints = createCameraConstraints();
  
    Color backgroundColor = Color.fromRGBA(0, 0.1f, 0.2f, 1);
  
    // GInitializationTask* initializationTask = new G3MCInitializationTask(this, createSceneDescriptionURL());
    GInitializationTask initializationTask = null;
  
    java.util.ArrayList<PeriodicalTask> periodicalTasks = createPeriodicalTasks();
  
    G3MWidget g3mWidget = G3MWidget.create(getGL(), getStorage(), getDownloader(), getThreadUtils(), createPlanet(), cameraConstraints, createCameraRenderer(), mainRenderer, createBusyRenderer(), backgroundColor, false, false, initializationTask, true, periodicalTasks); // autoDeleteInitializationTask -  logDownloaderStatistics -  logFPS
  
    //  g3mWidget->setUserData(getUserData());
  
    cameraConstraints = null;
    periodicalTasks = null;
  
    return g3mWidget;
  }

  protected final Planet createPlanet()
  {
    return Planet.createEarth();
  }

  protected final IStorage getStorage()
  {
    if (_storage == null)
    {
      _storage = createStorage();
    }
    return _storage;
  }

  protected abstract IStorage createStorage();

  protected abstract IDownloader createDownloader();

  protected abstract IThreadUtils createThreadUtils();


  /** Private to G3M, don't call it */
  public final void changeBaseLayer(Layer baseLayer)
  {
    if (_baseLayer != baseLayer)
    {
      if (_baseLayer != null)
      {
        if (_baseLayer != null)
           _baseLayer.dispose();
      }
      _baseLayer = baseLayer;
      recreateLayerSet();
    }
  }

  /** Private to G3M, don't call it */
  public final int getSceneTimestamp()
  {
    return _sceneTimestamp;
  }

  /** Private to G3M, don't call it */
  public final void setSceneTimestamp(int timestamp)
  {
    _sceneTimestamp = timestamp;
  }

  /** Private to G3M, don't call it */
  public final URL createSceneDescriptionURL()
  {
    String serverPath = _serverURL.getPath();
  
    return new URL(serverPath + "/scenes/" + _sceneId, false);
  }

  /** Private to G3M, don't call it */
  public final void rawChangeScene(String sceneId)
  {
    if (sceneId.compareTo(_sceneId) != 0)
    {
      _layerSet.removeAllLayers(false);
      if (_baseLayer != null)
      {
        if (_baseLayer != null)
           _baseLayer.dispose();
        _baseLayer = null;
      }
      _sceneTimestamp = -1;
      _sceneId = sceneId;
    }
  }

  public final void changeScene(String sceneId)
  {
    if (sceneId.compareTo(_sceneId) != 0)
    {
      getThreadUtils().invokeInRendererThread(new G3MCBuilder_ChangeSceneIdTask(this, sceneId), true);
    }
  }

  public final void requestScenesDescriptions(G3MCBuilderScenesDescriptionsListener listener)
  {
     requestScenesDescriptions(listener, true);
  }
  public final void requestScenesDescriptions(G3MCBuilderScenesDescriptionsListener listener, boolean autoDelete)
  {
    getDownloader().requestBuffer(createScenesDescriptionsURL(), DownloadPriority.HIGHEST, TimeInterval.zero(), true, new G3MCBuilder_ScenesDescriptionsBufferListener(listener, autoDelete), true);
  }

}