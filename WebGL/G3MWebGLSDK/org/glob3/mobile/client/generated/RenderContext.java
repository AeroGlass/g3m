package org.glob3.mobile.generated; 
//************************************************************


//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class FrameTasksExecutor;


public class RenderContext extends Context
{
  private FrameTasksExecutor _frameTasksExecutor;
  private GL _gl;
  private final Camera _currentCamera;
  private Camera _nextCamera;
  private TexturesHandler _texturesHandler;
  private ITimer _frameStartTimer;

  public RenderContext(FrameTasksExecutor frameTasksExecutor, IFactory factory, IStringUtils stringUtils, IThreadUtils threadUtils, ILogger logger, Planet planet, GL gl, Camera currentCamera, Camera nextCamera, TexturesHandler texturesHandler, IDownloader downloader, EffectsScheduler scheduler, ITimer frameStartTimer)
  {
	  super(factory, stringUtils, threadUtils, logger, planet, downloader, scheduler);
	  _frameTasksExecutor = frameTasksExecutor;
	  _gl = gl;
	  _currentCamera = currentCamera;
	  _nextCamera = nextCamera;
	  _texturesHandler = texturesHandler;
	  _frameStartTimer = frameStartTimer;

  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: GL* getGL() const
  public final GL getGL()
  {
	return _gl;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const Camera* getCurrentCamera() const
  public final Camera getCurrentCamera()
  {
	return _currentCamera;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: Camera* getNextCamera() const
  public final Camera getNextCamera()
  {
	return _nextCamera;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: TexturesHandler* getTexturesHandler() const
  public final TexturesHandler getTexturesHandler()
  {
	return _texturesHandler;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const ITimer* getFrameStartTimer() const
  public final ITimer getFrameStartTimer()
  {
	return _frameStartTimer;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: FrameTasksExecutor* getFrameTasksExecutor() const
  public final FrameTasksExecutor getFrameTasksExecutor()
  {
	return _frameTasksExecutor;
  }

  public void dispose()
  {
	if (_frameStartTimer != null)
		_frameStartTimer.dispose();
  }

}