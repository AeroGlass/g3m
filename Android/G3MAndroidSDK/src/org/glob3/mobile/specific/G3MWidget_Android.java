

package org.glob3.mobile.specific;

import java.util.ArrayList;

import org.glob3.mobile.generated.Angle;
import org.glob3.mobile.generated.BusyMeshRenderer;
import org.glob3.mobile.generated.CachedDownloader;
import org.glob3.mobile.generated.Camera;
import org.glob3.mobile.generated.CameraDoubleDragHandler;
import org.glob3.mobile.generated.CameraDoubleTapHandler;
import org.glob3.mobile.generated.CameraRenderer;
import org.glob3.mobile.generated.CameraRotationHandler;
import org.glob3.mobile.generated.CameraSingleDragHandler;
import org.glob3.mobile.generated.Color;
import org.glob3.mobile.generated.CompositeRenderer;
import org.glob3.mobile.generated.EllipsoidalTileTessellator;
import org.glob3.mobile.generated.G3MWidget;
import org.glob3.mobile.generated.GTask;
import org.glob3.mobile.generated.Geodetic3D;
import org.glob3.mobile.generated.ICameraConstrainer;
import org.glob3.mobile.generated.IDownloader;
import org.glob3.mobile.generated.IFactory;
import org.glob3.mobile.generated.IJSONParser;
import org.glob3.mobile.generated.ILogger;
import org.glob3.mobile.generated.IMathUtils;
import org.glob3.mobile.generated.IStorage;
import org.glob3.mobile.generated.IStringBuilder;
import org.glob3.mobile.generated.IStringUtils;
import org.glob3.mobile.generated.IThreadUtils;
import org.glob3.mobile.generated.LayerSet;
import org.glob3.mobile.generated.LogLevel;
import org.glob3.mobile.generated.MultiLayerTileTexturizer;
import org.glob3.mobile.generated.PeriodicalTask;
import org.glob3.mobile.generated.Planet;
import org.glob3.mobile.generated.TileRenderer;
import org.glob3.mobile.generated.TilesRenderParameters;
import org.glob3.mobile.generated.TimeInterval;
import org.glob3.mobile.generated.Touch;
import org.glob3.mobile.generated.TouchEvent;
import org.glob3.mobile.generated.TouchEventType;
import org.glob3.mobile.generated.UserData;
import org.glob3.mobile.generated.Vector2I;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.GestureDetector.OnDoubleTapListener;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.MotionEvent.PointerCoords;


public final class G3MWidget_Android
         extends
            GLSurfaceView
         implements
            OnGestureListener {

   private G3MWidget                                      _g3mWidget;
   private ES2Renderer                                    _es2renderer;
   //   private SQLiteStorage_Android                          _storage              = null;

   private final MotionEventProcessor                     _motionEventProcessor = new MotionEventProcessor();
   private final OnDoubleTapListener                      _doubleTapListener;
   private final GestureDetector                          _gestureDetector;

   private ArrayList<ICameraConstrainer>                  _cameraConstraints;
   private LayerSet                                       _layerSet;
   private ArrayList<org.glob3.mobile.generated.Renderer> _renderers;
   private UserData                                       _userData;

   //   private IDownloader                                    _downloader;
   private GTask                                          _initializationTask;
   private ArrayList<PeriodicalTask>                      _periodicalTasks;
   private boolean                                        _incrementalTileQuality;


   //   private boolean                                        _isPaused             = false;
   //   private final LinkedList<Runnable>                     _pausedRunnableQueue  = new LinkedList<Runnable>();
   //   private final Object                                   _pausedMutex          = new Object();


   public G3MWidget_Android(final Context context) {
      this(context, null);
   }


   // Needed to create widget from XML layout
   public G3MWidget_Android(final Context context,
                            final AttributeSet attrs) {
      super(context, attrs);

      initSingletons();

      setEGLContextClientVersion(2); // OPENGL ES VERSION MUST BE SPECIFED
      setEGLConfigChooser(true); // IT GIVES US A RGB DEPTH OF 8 BITS PER
      // CHANNEL, HAVING TO FORCE PROPER BUFFER
      // ALLOCATION

      // Detect Long-Press events
      setLongClickable(true);

      // Debug flags
      if (false) {
         setDebugFlags(DEBUG_CHECK_GL_ERROR | DEBUG_LOG_GL_CALLS);
      }

      if (!isInEditMode()) { // needed to avoid visual edition of this widget
         //Double Tap Listener
         _gestureDetector = new GestureDetector(this);
         _doubleTapListener = new OnDoubleTapListener() {

            @Override
            public boolean onSingleTapConfirmed(final MotionEvent e) {
               // TODO Auto-generated method stub
               return false;
            }


            @Override
            public boolean onDoubleTapEvent(final MotionEvent event) {
               return true;
            }


            @Override
            public boolean onDoubleTap(final MotionEvent event) {

               final TouchEvent te = _motionEventProcessor.processDoubleTapEvent(event);

               queueEvent(new Runnable() {
                  @Override
                  public void run() {
                     _g3mWidget.onTouchEvent(te);
                  }
               });

               return true;
            }
         };
         _gestureDetector.setOnDoubleTapListener(_doubleTapListener);
      }
      else {
         _gestureDetector = null;
         _doubleTapListener = null;
      }
   }


   private void initSingletons() {
      final ILogger logger = new Logger_Android(LogLevel.ErrorLevel);
      final IFactory factory = new Factory_Android(getContext());
      final IStringUtils stringUtils = new StringUtils_Android();
      final IThreadUtils threadUtils = new ThreadUtils_Android(this);
      final IStringBuilder stringBuilder = new StringBuilder_Android();
      final IMathUtils mathUtils = new MathUtils_Android();
      final IJSONParser jsonParser = new JSONParser_Android();
      final IStorage storage = new SQLiteStorage_Android("g3m.cache", this.getContext());
      final int connectTimeout = 60000;
      final int readTimeout = 60000;
      final boolean saveInBackground = true;
      final IDownloader downloader = new CachedDownloader(new Downloader_Android(8, connectTimeout, readTimeout),
               saveInBackground);

      G3MWidget.initSingletons(logger, factory, stringUtils, threadUtils, stringBuilder, mathUtils, jsonParser, storage,
               downloader);
   }


   @Override
   protected void onSizeChanged(final int w,
                                final int h,
                                final int oldw,
                                final int oldh) {
      super.onSizeChanged(w, h, oldw, oldh);

      if (_es2renderer == null) {
         _es2renderer = new ES2Renderer(this.getContext(), this);
         setRenderer(_es2renderer);
      }
   }


   @Override
   public boolean onTouchEvent(final MotionEvent event) {

      //Notifing gestureDetector for DoubleTap recognition
      _gestureDetector.onTouchEvent(event);

      final TouchEvent te = _motionEventProcessor.processEvent(event);
      if (te == null) {
         return false;
      }

      queueEvent(new Runnable() {
         @Override
         public void run() {
            _g3mWidget.onTouchEvent(te);
         }
      });
      return true;
   }


   @Override
   public boolean onDown(final MotionEvent arg0) {
      return false;
   }


   @Override
   public boolean onFling(final MotionEvent e1,
                          final MotionEvent e2,
                          final float velocityX,
                          final float velocityY) {
      return false;
   }


   @Override
   public void onLongPress(final MotionEvent e) {
      final PointerCoords pc = new PointerCoords();
      e.getPointerCoords(0, pc);
      final Touch t = new Touch(new Vector2I((int) pc.x, (int) pc.y), new Vector2I(0, 0));
      final TouchEvent te = TouchEvent.create(TouchEventType.LongPress, t);

      queueEvent(new Runnable() {
         @Override
         public void run() {
            _g3mWidget.onTouchEvent(te);
         }
      });
   }


   @Override
   public boolean onScroll(final MotionEvent e1,
                           final MotionEvent e2,
                           final float distanceX,
                           final float distanceY) {
      return false;
   }


   @Override
   public void onShowPress(final MotionEvent e) {

   }


   @Override
   public boolean onSingleTapUp(final MotionEvent e) {
      return false;
   }


   public G3MWidget getG3MWidget() {
      if (_g3mWidget == null) {
         initWidget();
      }
      return _g3mWidget;
   }


   public void initWidget(final ArrayList<ICameraConstrainer> cameraConstraints,
                          final LayerSet layerSet,
                          final ArrayList<org.glob3.mobile.generated.Renderer> renderers,
                          final UserData userData,
                          final GTask initializationTask,
                          final ArrayList<PeriodicalTask> periodicalTasks,
                          final boolean incrementalTileQuality) {
      _cameraConstraints = cameraConstraints;
      _layerSet = layerSet;
      _renderers = renderers;
      _userData = userData;
      _initializationTask = initializationTask;
      _periodicalTasks = periodicalTasks;
      _incrementalTileQuality = incrementalTileQuality;
   }


   private void initWidget() {
      // creates default camera-renderer and camera-handlers
      final CameraRenderer cameraRenderer = new CameraRenderer();

      final boolean useInertia = true;
      cameraRenderer.addHandler(new CameraSingleDragHandler(useInertia));

      final boolean processRotation = true;
      final boolean processZoom = true;
      cameraRenderer.addHandler(new CameraDoubleDragHandler(processRotation, processZoom));
      cameraRenderer.addHandler(new CameraRotationHandler());
      cameraRenderer.addHandler(new CameraDoubleTapHandler());

      final boolean renderDebug = false;
      final boolean useTilesSplitBudget = true;
      final boolean forceTopLevelTilesRenderOnStart = true;

      final TilesRenderParameters parameters = TilesRenderParameters.createDefault(renderDebug, useTilesSplitBudget,
               forceTopLevelTilesRenderOnStart, _incrementalTileQuality);

      initWidget(cameraRenderer, parameters, _initializationTask);
   }


   private void initWidget(final CameraRenderer cameraRenderer,
                           final TilesRenderParameters parameters,
                           final GTask initializationTask) {

      // create GLOB3M WIDGET
      final int width = getWidth();
      final int height = getHeight();

      final NativeGL2_Android nativeGL = new NativeGL2_Android();

      final CompositeRenderer mainRenderer = new CompositeRenderer();

      // composite.addRenderer(cameraRenderer);

      if (_layerSet != null) {
         final boolean showStatistics = false;

         final TileRenderer tr = new TileRenderer( //
                  new EllipsoidalTileTessellator(parameters._tileResolution, true), //
                  new MultiLayerTileTexturizer(), //
                  _layerSet, //
                  parameters, //
                  showStatistics);

         mainRenderer.addRenderer(tr);
      }

      for (final org.glob3.mobile.generated.Renderer renderer : _renderers) {
         mainRenderer.addRenderer(renderer);
      }


      final Planet planet = Planet.createEarth();

      final org.glob3.mobile.generated.Renderer busyRenderer = new BusyMeshRenderer();


      _g3mWidget = G3MWidget.create( //
               nativeGL, //
               planet, //
               _cameraConstraints, //
               cameraRenderer, //
               mainRenderer, //
               busyRenderer, //
               width, //
               height, //
               Color.fromRGBA(0, (float) 0.1, (float) 0.2, 1), //
               true, // 
               false, // 
               initializationTask, //
               true, //
               _periodicalTasks);

      _g3mWidget.setUserData(_userData);

      //      //Testing Periodical Tasks
      //      if (true) {
      //         class PeriodicTask
      //                  extends
      //                     GTask {
      //            private long      _lastExec;
      //            private final int _number;
      //
      //
      //            public PeriodicTask(final int n) {
      //               _number = n;
      //            }
      //
      //
      //            @Override
      //            public void run() {
      //               final ITimer t = IFactory.instance().createTimer();
      //               final long now = t.now().milliseconds();
      //               ILogger.instance().logInfo("Running periodical Task " + _number + " - " + (now - _lastExec) + " ms.\n");
      //               _lastExec = now;
      //               IFactory.instance().deleteTimer(t);
      //            }
      //         }
      //
      //         _g3mWidget.addPeriodicalTask(TimeInterval.fromMilliseconds(4000), new PeriodicTask(1));
      //         _g3mWidget.addPeriodicalTask(TimeInterval.fromMilliseconds(6000), new PeriodicTask(2));
      //         _g3mWidget.addPeriodicalTask(TimeInterval.fromMilliseconds(500), new PeriodicTask(3));
      //      }
   }


   @Override
   public void onPause() {
      //      synchronized (_pausedMutex) {
      //         _isPaused = true;
      //      }

      final int __TODO_check_onpause;
      if (_es2renderer != null) {
         _g3mWidget.onPause();
      }

      /*
      if (_g3mWidget == null) {
         System.err.println("break (point) on me");
      }
       */
      super.onPause();
   }


   @Override
   public void onResume() {
      if (_es2renderer != null) {
         super.onResume();
         _g3mWidget.onResume();
      }

      //      synchronized (_pausedMutex) {
      //         _isPaused = false;
      //
      //         // drain queue
      //         for (final Runnable runnable : _pausedRunnableQueue) {
      //            super.queueEvent(runnable);
      //         }
      //         _pausedRunnableQueue.clear();
      //      }
   }


   @Override
   public void queueEvent(final Runnable runnable) {
      //      synchronized (_pausedMutex) {
      //         if (_isPaused) {
      //            _pausedRunnableQueue.add(runnable);
      //         }
      //         else {
      super.queueEvent(runnable);
      //         }
      //      }
   }


   public void closeStorage() {
      if (IDownloader.instance() != null) {
         IDownloader.instance().stop();
      }
      if (IStorage.instance() != null) {
         // _storage.onPause(null);
         ((SQLiteStorage_Android) IStorage.instance()).close();
      }
   }


   public Camera getNextCamera() {
      return getG3MWidget().getNextCamera();
   }


   public UserData getUserData() {
      return getG3MWidget().getUserData();
   }


   public void setAnimatedCameraPosition(final Geodetic3D position,
                                         final TimeInterval interval) {
      getG3MWidget().setAnimatedCameraPosition(position, interval);
   }


   public void setAnimatedCameraPosition(final Geodetic3D position) {
      getG3MWidget().setAnimatedCameraPosition(position);
   }


   public void setCameraPosition(final Geodetic3D position) {
      getG3MWidget().setCameraPosition(position);
   }


   public CameraRenderer getCameraRenderer() {
      return getG3MWidget().getCameraRenderer();
   }


   public void setCameraHeading(final Angle angle) {
      getG3MWidget().setCameraHeading(angle);
   }


   public void resetCameraPosition() {
      getG3MWidget().resetCameraPosition();
   }


   public void setCameraPitch(final Angle angle) {
      getG3MWidget().setCameraPitch(angle);
   }
}
