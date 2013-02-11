package org.glob3.mobile.generated; 
public class G3MWidget
{

  public static void initSingletons(ILogger logger, IFactory factory, IStringUtils stringUtils, IStringBuilder stringBuilder, IMathUtils mathUtils, IJSONParser jsonParser, ITextUtils textUtils)
  {
    if (ILogger.instance() == null)
    {
      ILogger.setInstance(logger);
      IFactory.setInstance(factory);
      IStringUtils.setInstance(stringUtils);
      IStringBuilder.setInstance(stringBuilder);
      IMathUtils.setInstance(mathUtils);
      IJSONParser.setInstance(jsonParser);
      ITextUtils.setInstance(textUtils);
    }
    else
    {
      ILogger.instance().logWarning("Singletons already set");
    }
  }

  public static G3MWidget create(GL gl, IStorage storage, IDownloader downloader, IThreadUtils threadUtils, Planet planet, java.util.ArrayList<ICameraConstrainer> cameraConstrainers, CameraRenderer cameraRenderer, Renderer mainRenderer, Renderer busyRenderer, Color backgroundColor, boolean logFPS, boolean logDownloaderStatistics, GInitializationTask initializationTask, boolean autoDeleteInitializationTask, java.util.ArrayList<PeriodicalTask> periodicalTasks)
  {
  
    return new G3MWidget(gl, storage, downloader, threadUtils, planet, cameraConstrainers, cameraRenderer, mainRenderer, busyRenderer, backgroundColor, logFPS, logDownloaderStatistics, initializationTask, autoDeleteInitializationTask, periodicalTasks);
  }

  public void dispose()
  {
    if (_userData != null)
       _userData.dispose();
  
    if (_planet != null)
       _planet.dispose();
    if (_cameraRenderer != null)
       _cameraRenderer.dispose();
    if (_mainRenderer != null)
       _mainRenderer.dispose();
    if (_busyRenderer != null)
       _busyRenderer.dispose();
    if (_gl != null)
       _gl.dispose();
    if (_effectsScheduler != null)
       _effectsScheduler.dispose();
    if (_currentCamera != null)
       _currentCamera.dispose();
    if (_nextCamera != null)
       _nextCamera.dispose();
    if (_texturesHandler != null)
       _texturesHandler.dispose();
    if (_timer != null)
       _timer.dispose();
  
    if (_downloader != null)
    {
      _downloader.stop();
      if (_downloader != null)
         _downloader.dispose();
    }
  
    if (_storage != null)
       _storage.dispose();
    if (_threadUtils != null)
       _threadUtils.dispose();
  
    for (int n = 0; n < _cameraConstrainers.size(); n++)
    {
      if (_cameraConstrainers.get(n) != null)
         _cameraConstrainers.get(n).dispose();
    }
    if (_frameTasksExecutor != null)
       _frameTasksExecutor.dispose();
  
    for (int i = 0; i < _periodicalTasks.size(); i++)
    {
      //    _periodicalTasks[i].releaseTask();
  
      PeriodicalTask periodicalTask = _periodicalTasks.get(i);
      if (periodicalTask != null)
         periodicalTask.dispose();
    }
  
    if (_context != null)
       _context.dispose();
  
    if (_rootState != null)
       _rootState.dispose();
  }

  public final void render(int width, int height)
  {
      if (_paused)
      {
          return;
      }
  
      if ((_width != width || _height != height) && _mainRendererReady)
      {
          _width = width;
          _height = height;
  
          onResizeViewportEvent(_width, _height);
      }
  
    _timer.start();
    _renderCounter++;
  
    //Start periodical task
    final int periodicalTasksCount = _periodicalTasks.size();
    for (int i = 0; i < periodicalTasksCount; i++)
    {
      PeriodicalTask pt = _periodicalTasks.get(i);
      pt.executeIfNecessary(_context);
    }
  
    // give to the CameraContrainers the opportunity to change the nextCamera
    final int cameraConstrainersCount = _cameraConstrainers.size();
    for (int i = 0; i< cameraConstrainersCount; i++)
    {
      ICameraConstrainer constrainer = _cameraConstrainers.get(i);
      constrainer.onCameraChange(_planet, _currentCamera, _nextCamera);
    }
    _currentCamera.copyFrom(_nextCamera);
  
    G3MRenderContext rc = new G3MRenderContext(_frameTasksExecutor, IFactory.instance(), IStringUtils.instance(), _threadUtils, ILogger.instance(), IMathUtils.instance(), IJSONParser.instance(), _planet, _gl, _currentCamera, _nextCamera, _texturesHandler, _textureBuilder, _downloader, _effectsScheduler, IFactory.instance().createTimer(), _storage);
  
    _mainRendererReady = _mainRenderer.isReadyToRender(rc);
  
    if (_mainRendererReady)
    {
      if (_initializationTask != null)
      {
        if (!_initializationTaskWasRun)
        {
          _initializationTask.run(_context);
          _initializationTaskWasRun = true;
        }
  
        if (_initializationTask.isDone(_context))
        {
          if (_autoDeleteInitializationTask)
          {
            if (_initializationTask != null)
               _initializationTask.dispose();
          }
          _initializationTask = null;
        }
        else
        {
          _mainRendererReady = false;
        }
      }
    }
  
    _effectsScheduler.doOneCyle(rc);
  
    _frameTasksExecutor.doPreRenderCycle(rc);
  
  
    Renderer selectedRenderer = _mainRendererReady ? _mainRenderer : _busyRenderer;
    if (selectedRenderer != _selectedRenderer)
    {
      if (_selectedRenderer != null)
      {
        _selectedRenderer.stop();
      }
      _selectedRenderer = selectedRenderer;
      _selectedRenderer.start();
    }
  
    _gl.clearScreen(_backgroundColor);
  
    if (_mainRendererReady)
    {
      _cameraRenderer.render(rc, _rootState);
    }
  
    if (_selectedRenderer.isEnable())
    {
      _selectedRenderer.render(rc, _rootState);
    }
  
    java.util.ArrayList<OrderedRenderable> orderedRenderables = rc.getSortedOrderedRenderables();
    if (orderedRenderables != null)
    {
      final int orderedRenderablesCount = orderedRenderables.size();
      for (int i = 0; i < orderedRenderablesCount; i++)
      {
        OrderedRenderable orderedRenderable = orderedRenderables.get(i);
        orderedRenderable.render(rc, _rootState);
        if (orderedRenderable != null)
           orderedRenderable.dispose();
      }
    }
  
    //  _frameTasksExecutor->doPostRenderCycle(&rc);
  
    final TimeInterval elapsedTime = _timer.elapsedTime();
    if (elapsedTime.milliseconds() > 100)
    {
      ILogger.instance().logWarning("Frame took too much time: %dms", elapsedTime.milliseconds());
    }
  
    if (_logFPS)
    {
      _totalRenderTime += elapsedTime.milliseconds();
  
      if ((_renderStatisticsTimer == null) || (_renderStatisticsTimer.elapsedTime().seconds() > 2))
      {
        final double averageTimePerRender = (double) _totalRenderTime / _renderCounter;
        final double fps = 1000.0 / averageTimePerRender;
        ILogger.instance().logInfo("FPS=%f", fps);
  
        _renderCounter = 0;
        _totalRenderTime = 0;
  
        if (_renderStatisticsTimer == null)
        {
          _renderStatisticsTimer = IFactory.instance().createTimer();
        }
        else
        {
          _renderStatisticsTimer.start();
        }
      }
    }
  
    if (_logDownloaderStatistics)
    {
      String cacheStatistics = "";
  
      if (_downloader != null)
      {
        cacheStatistics = _downloader.statistics();
      }
  
      if (!_lastCacheStatistics.equals(cacheStatistics))
      {
        ILogger.instance().logInfo("%s", cacheStatistics);
        _lastCacheStatistics = cacheStatistics;
      }
    }
  
  }

  public final void onTouchEvent(TouchEvent touchEvent)
  {
    if (_mainRendererReady)
    {
      G3MEventContext ec = new G3MEventContext(IFactory.instance(), IStringUtils.instance(), _threadUtils, ILogger.instance(), IMathUtils.instance(), IJSONParser.instance(), _planet, _downloader, _effectsScheduler, _storage);
  
      boolean handled = false;
      if (_mainRenderer.isEnable())
      {
        handled = _mainRenderer.onTouchEvent(ec, touchEvent);
      }
  
      if (!handled)
      {
        _cameraRenderer.onTouchEvent(ec, touchEvent);
      }
    }
  }

  public final void onResizeViewportEvent(int width, int height)
  {
    if (_mainRendererReady)
    {
      G3MEventContext ec = new G3MEventContext(IFactory.instance(), IStringUtils.instance(), _threadUtils, ILogger.instance(), IMathUtils.instance(), IJSONParser.instance(), _planet, _downloader, _effectsScheduler, _storage);
  
      _nextCamera.resizeViewport(width, height);
  
         _nextCamera.resizeViewport(width, height);
  
      _cameraRenderer.onResizeViewportEvent(ec, width, height);
  
      if (_mainRenderer.isEnable())
      {
        _mainRenderer.onResizeViewportEvent(ec, width, height);
      }
    }
  }

  public final void onPause()
  {
    _paused = true;
  
    _threadUtils.onPause(_context);
  
    _effectsScheduler.onPause(_context);
  
    _mainRenderer.onPause(_context);
    _busyRenderer.onPause(_context);
  
    _downloader.onPause(_context);
    _storage.onPause(_context);
  }

  public final void onResume()
  {
    _paused = false;
  
    _storage.onResume(_context);
  
    _downloader.onResume(_context);
  
    _mainRenderer.onResume(_context);
    _busyRenderer.onResume(_context);
  
    _effectsScheduler.onResume(_context);
  
    _threadUtils.onResume(_context);
  }

  public final void onDestroy()
  {
    _threadUtils.onDestroy(_context);
  
    _effectsScheduler.onDestroy(_context);
  
    _mainRenderer.onDestroy(_context);
    _busyRenderer.onDestroy(_context);
  
    _downloader.onDestroy(_context);
    _storage.onDestroy(_context);
  }

  public final GL getGL()
  {
    return _gl;
  }

  //  const Camera* getCurrentCamera() const {
  //    return _currentCamera;
  //  }

  public final Camera getNextCamera()
  {
    return _nextCamera;
  }

  public final void setUserData(WidgetUserData userData)
  {
    if (_userData != null)
       _userData.dispose();

    _userData = userData;
    if (_userData != null)
    {
      _userData.setWidget(this);
    }
  }

  public final WidgetUserData getUserData()
  {
    return _userData;
  }

  public final void addPeriodicalTask(PeriodicalTask periodicalTask)
  {
    _periodicalTasks.add(periodicalTask);
  }

  public final void addPeriodicalTask(TimeInterval interval, GTask task)
  {
    addPeriodicalTask(new PeriodicalTask(interval, task));
  }


  public final void setCameraPosition(Geodetic3D position)
  {
    getNextCamera().setPosition(position);
  }

  public final void setCameraHeading(Angle angle)
  {
    getNextCamera().setHeading(angle);
  }

  public final void setCameraPitch(Angle angle)
  {
    getNextCamera().setPitch(angle);
  }

  public final void setAnimatedCameraPosition(Geodetic3D position)
  {
    setAnimatedCameraPosition(position, TimeInterval.fromSeconds(3));
  }

  public final void setAnimatedCameraPosition(Geodetic3D position, TimeInterval interval)
  {
  
    final Geodetic3D startPosition = _planet.toGeodetic3D(_currentCamera.getCartesianPosition());
  
    double finalLat = position.latitude()._degrees;
    double finalLon = position.longitude()._degrees;
  
    //Fixing final latitude
    while (finalLat > 90)
    {
      finalLat -= 360;
    }
    while (finalLat < -90)
    {
      finalLat += 360;
    }
  
    //Fixing final longitude
    while (finalLon > 360)
    {
      finalLon -= 360;
    }
    while (finalLon < 0)
    {
      finalLon += 360;
    }
    if (Math.abs(finalLon - startPosition.longitude()._degrees) > 180)
    {
      finalLon -= 360;
    }
  
    final Geodetic3D endPosition = Geodetic3D.fromDegrees(finalLat, finalLon, position.height());
  
    stopCameraAnimation();
  
    _effectsScheduler.startEffect(new CameraGoToPositionEffect(interval, startPosition, endPosition), _nextCamera.getEffectTarget());
  }

  public final void stopCameraAnimation()
  {
    EffectTarget target = _nextCamera.getEffectTarget();
    _effectsScheduler.cancellAllEffectsFor(target);
  }

  public final void resetCameraPosition()
  {
    getNextCamera().resetPosition();
  }

  public final CameraRenderer getCameraRenderer()
  {
    return _cameraRenderer;
  }

  public final G3MContext getG3MContext()
  {
    return _context;
  }

  private IStorage _storage;
  private IDownloader _downloader;
  private IThreadUtils _threadUtils;

  private FrameTasksExecutor _frameTasksExecutor;
  private GL _gl;
  private Planet _planet; // REMOVED FINAL WORD BY CONVERSOR RULE

  private CameraRenderer _cameraRenderer;
  private Renderer _mainRenderer;
  private Renderer _busyRenderer;
  private boolean _mainRendererReady;
  private Renderer _selectedRenderer;

  private EffectsScheduler _effectsScheduler;

  private java.util.ArrayList<ICameraConstrainer> _cameraConstrainers = new java.util.ArrayList<ICameraConstrainer>();

  private Camera _currentCamera;
  private Camera _nextCamera;
  private TexturesHandler _texturesHandler;
  private TextureBuilder _textureBuilder;
  private final Color _backgroundColor ;

  private ITimer _timer;
  private int _renderCounter;
  private int _totalRenderTime;
  private final boolean _logFPS;
  private final boolean _logDownloaderStatistics;
  private String _lastCacheStatistics;

  private ITimer _renderStatisticsTimer;

  private WidgetUserData _userData;

  private GInitializationTask _initializationTask;
  private boolean _autoDeleteInitializationTask;

  private java.util.ArrayList<PeriodicalTask> _periodicalTasks = new java.util.ArrayList<PeriodicalTask>();

  private int _width;
  private int _height;

  private void initializeGL()
  {
    //_gl->enableDepthTest();
  
    //_gl->enableCullFace(GLCullFace::back());
  }

  private final G3MContext _context;

  private boolean _paused;

  private final GLState _rootState;

  private boolean _initializationTaskWasRun;

  private G3MWidget(GL gl, IStorage storage, IDownloader downloader, IThreadUtils threadUtils, Planet planet, java.util.ArrayList<ICameraConstrainer> cameraConstrainers, CameraRenderer cameraRenderer, Renderer mainRenderer, Renderer busyRenderer, Color backgroundColor, boolean logFPS, boolean logDownloaderStatistics, GInitializationTask initializationTask, boolean autoDeleteInitializationTask, java.util.ArrayList<PeriodicalTask> periodicalTasks)
  /*
   =======
  _gl( new GL(nativeGL, false) ),
  >>>>>>> origin/webgl-port
   */
  {
     _rootState = GLState.newDefault();
     _frameTasksExecutor = new FrameTasksExecutor();
     _effectsScheduler = new EffectsScheduler();
     _gl = gl;
     _downloader = downloader;
     _storage = storage;
     _threadUtils = threadUtils;
     _texturesHandler = new TexturesHandler(_gl, false);
     _textureBuilder = new CPUTextureBuilder();
     _planet = planet;
     _cameraConstrainers = cameraConstrainers;
     _cameraRenderer = cameraRenderer;
     _mainRenderer = mainRenderer;
     _busyRenderer = busyRenderer;
     _width = 1;
     _height = 1;
     _currentCamera = new Camera(_width, _height);
     _nextCamera = new Camera(_width, _height);
     _backgroundColor = new Color(backgroundColor);
     _timer = IFactory.instance().createTimer();
     _renderCounter = 0;
     _totalRenderTime = 0;
     _logFPS = logFPS;
     _mainRendererReady = false;
     _selectedRenderer = null;
     _renderStatisticsTimer = null;
     _logDownloaderStatistics = logDownloaderStatistics;
     _userData = null;
     _initializationTask = initializationTask;
     _autoDeleteInitializationTask = autoDeleteInitializationTask;
     _context = new G3MContext(IFactory.instance(), IStringUtils.instance(), threadUtils, ILogger.instance(), IMathUtils.instance(), IJSONParser.instance(), _planet, downloader, _effectsScheduler, storage);
     _paused = false;
     _initializationTaskWasRun = false;
    initializeGL();
  
    _effectsScheduler.initialize(_context);
    _cameraRenderer.initialize(_context);
    _mainRenderer.initialize(_context);
    _busyRenderer.initialize(_context);
    _currentCamera.initialize(_context);
    _nextCamera.initialize(_context);
  
    if (_threadUtils != null)
    {
      _threadUtils.initialize(_context);
    }
  
    if (_storage != null)
    {
      _storage.initialize(_context);
    }
  
    if (_downloader != null)
    {
      _downloader.initialize(_context, _frameTasksExecutor);
      _downloader.start();
    }
  
    for (int i = 0; i < periodicalTasks.size(); i++)
    {
      addPeriodicalTask(periodicalTasks.get(i));
    }
  }

}