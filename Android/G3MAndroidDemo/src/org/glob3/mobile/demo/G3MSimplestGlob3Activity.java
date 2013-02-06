

package org.glob3.mobile.demo;

import java.util.ArrayList;

import org.glob3.mobile.generated.Angle;
import org.glob3.mobile.generated.BusyMeshRenderer;
import org.glob3.mobile.generated.CPUTextureBuilder;
import org.glob3.mobile.generated.CachedDownloader;
import org.glob3.mobile.generated.CameraDoubleDragHandler;
import org.glob3.mobile.generated.CameraDoubleTapHandler;
import org.glob3.mobile.generated.CameraRenderer;
import org.glob3.mobile.generated.CameraRotationHandler;
import org.glob3.mobile.generated.CameraSingleDragHandler;
import org.glob3.mobile.generated.Color;
import org.glob3.mobile.generated.CompositeRenderer;
import org.glob3.mobile.generated.FrameTasksExecutor;
import org.glob3.mobile.generated.G3MContext;
import org.glob3.mobile.generated.G3MRenderContext;
import org.glob3.mobile.generated.GInitializationTask;
import org.glob3.mobile.generated.Geodetic2D;
import org.glob3.mobile.generated.Geodetic3D;
import org.glob3.mobile.generated.ICameraConstrainer;
import org.glob3.mobile.generated.IDownloader;
import org.glob3.mobile.generated.IFactory;
import org.glob3.mobile.generated.IJSONParser;
import org.glob3.mobile.generated.ILogger;
import org.glob3.mobile.generated.IMathUtils;
import org.glob3.mobile.generated.IStorage;
import org.glob3.mobile.generated.IStringUtils;
import org.glob3.mobile.generated.IThreadUtils;
import org.glob3.mobile.generated.LayerSet;
import org.glob3.mobile.generated.Mark;
import org.glob3.mobile.generated.MarksRenderer;
import org.glob3.mobile.generated.PeriodicalTask;
import org.glob3.mobile.generated.Planet;
import org.glob3.mobile.generated.Sector;
import org.glob3.mobile.generated.SimpleCameraConstrainer;
import org.glob3.mobile.generated.TexturesHandler;
import org.glob3.mobile.generated.TileRenderer;
import org.glob3.mobile.generated.TileRendererBuilder;
import org.glob3.mobile.generated.TimeInterval;
import org.glob3.mobile.generated.URL;
import org.glob3.mobile.generated.WMSLayer;
import org.glob3.mobile.generated.WMSServerVersion;
import org.glob3.mobile.generated.WidgetUserData;
import org.glob3.mobile.specific.Downloader_Android;
import org.glob3.mobile.specific.G3MBaseActivity;
import org.glob3.mobile.specific.G3MWidget_Android;
import org.glob3.mobile.specific.SQLiteStorage_Android;
import org.glob3.mobile.specific.ThreadUtils_Android;
import org.glob3.mobile.specific.TileVisitorCache_Android;

import android.os.Bundle;


public class G3MSimplestGlob3Activity
    extends
      G3MBaseActivity {

  private G3MWidget_Android _widgetAndroid = null;


  @Override
  public void onCreate(final Bundle savedInstanceState) {
    final long mill = TimeInterval.fromDays(30).milliseconds();
    System.out.println("TimeInterval.fromDays(30): " + mill);

    final long expiration = System.currentTimeMillis() + mill;
    System.out.println("Expiration: " + expiration);


    super.onCreate(savedInstanceState);

    // initialize a default widget by using a builder
    // final G3MBuilder_Android g3mBuilder = new G3MBuilder_Android(this);
    //
    // _widgetAndroid = g3mBuilder.createWidget();
    // setContentView(_widgetAndroid);


    // initialize a customized widget without using any builder
    // initialize a customized widget without using any builder
    _widgetAndroid = new G3MWidget_Android(this);

    final IStorage storage = new SQLiteStorage_Android("g3m.cache", this);

    final TimeInterval connectTimeout = TimeInterval.fromSeconds(20);
    final TimeInterval readTimeout = TimeInterval.fromSeconds(30);
    final boolean saveInBackground = true;
    final IDownloader downloader = new CachedDownloader( //
        new Downloader_Android(8, connectTimeout, readTimeout, this), //
        storage, //
        saveInBackground);

    final IThreadUtils threadUtils = new ThreadUtils_Android(_widgetAndroid);

    final Planet planet = Planet.createEarth();

    final ArrayList<ICameraConstrainer> cameraConstraints = new ArrayList<ICameraConstrainer>();
    cameraConstraints.add(new SimpleCameraConstrainer());

    final CameraRenderer cameraRenderer = new CameraRenderer();
    final boolean useInertia = true;
    cameraRenderer.addHandler(new CameraSingleDragHandler(useInertia));
    final boolean processRotation = true;
    final boolean processZoom = true;
    cameraRenderer.addHandler(new CameraDoubleDragHandler(processRotation,
        processZoom));
    cameraRenderer.addHandler(new CameraRotationHandler());
    cameraRenderer.addHandler(new CameraDoubleTapHandler());

    final CompositeRenderer mainRenderer = new CompositeRenderer();
    final LayerSet layerSet = new LayerSet();
    final WMSLayer osm = new WMSLayer( //
        "osm_auto:all", //
        new URL("http://129.206.228.72/cached/osm", false), //
        WMSServerVersion.WMS_1_1_0, //
        Sector.fromDegrees(-85.05, -180.0, 85.05, 180.0), //
        "image/jpeg", //
        "EPSG:4326", //
        "", //
        false, //
        null, //
        TimeInterval.fromDays(30));
    layerSet.addLayer(osm);
    final TileRendererBuilder tlBuilder = new TileRendererBuilder();
    tlBuilder.setLayerSet(layerSet);
    final TileRenderer tileRenderer = tlBuilder.create();
    mainRenderer.addRenderer(tileRenderer);


    final MarksRenderer marksRenderer = new MarksRenderer(false);
    final Mark m1 = new Mark(
        "Fuerteventura", //
        new URL("http://glob3m.glob3mobile.com/icons/markers/g3m.png", false), //
        new Geodetic3D(Angle.fromDegrees(28.05), Angle.fromDegrees(-14.36), 0), //
        false);
    marksRenderer.addMark(m1);

    final Mark m3 = new Mark("Washington, DC", //
        new Geodetic3D(Angle.fromDegreesMinutesSeconds(38, 53, 42.24),
            Angle.fromDegreesMinutesSeconds(-77, 2, 10.92), 100), //
        0);
    marksRenderer.addMark(m3);
    mainRenderer.addRenderer(marksRenderer);


    final org.glob3.mobile.generated.Renderer busyRenderer = new BusyMeshRenderer();

    final Color backgroundColor = Color.fromRGBA(0, (float) 0.1, (float) 0.2,
        1);

    final boolean logFPS = true;

    final boolean logDownloaderStatistics = false;


    final GInitializationTask initializationTask = new GInitializationTask() {
      @Override
      public void run(final G3MContext ctx) {
        tileRenderer.acceptTileVisitor(new TileVisitorCache_Android(ctx,
            layerSet, 256, 256));

        tileRenderer.visitTilesTouchesWith(
            new Sector(new Geodetic2D(Angle.fromDegrees(39.31),
                Angle.fromDegrees(-6.72)), new Geodetic2D(
                Angle.fromDegrees(39.38), Angle.fromDegrees(-6.64))), 0, 14);

        ctx.getLogger().logInfo(
            "SE HA TERMINADO DE CACHEAR LOS SECTORES ESPECIFICADOS");
      }


      @Override
      public boolean isDone(final G3MContext context1) {
        return true;
      }
    };

    final boolean autoDeleteInitializationTask = true;

    final ArrayList<PeriodicalTask> periodicalTasks = new ArrayList<PeriodicalTask>();

    final WidgetUserData userData = null;

    _widgetAndroid.initWidget(//
        storage, //
        downloader, //
        threadUtils, //
        planet, //
        cameraConstraints, //
        cameraRenderer, //
        mainRenderer, //
        busyRenderer, //
        backgroundColor, //
        logFPS, //
        logDownloaderStatistics, //
        initializationTask, //
        autoDeleteInitializationTask, //
        periodicalTasks, //
        userData);

    setContentView(_widgetAndroid);

    final G3MRenderContext rc = new G3MRenderContext(new FrameTasksExecutor(),
        IFactory.instance(), IStringUtils.instance(),
        _widgetAndroid.getG3MWidget().getG3MContext().getThreadUtils(),
        ILogger.instance(), IMathUtils.instance(), IJSONParser.instance(),
        planet, _widgetAndroid.getG3MWidget().getGL(),
        _widgetAndroid.getG3MWidget().getNextCamera(),
        _widgetAndroid.getG3MWidget().getNextCamera(), new TexturesHandler(
            _widgetAndroid.getG3MWidget().getGL(), false),
        new CPUTextureBuilder(),
        _widgetAndroid.getG3MWidget().getG3MContext().getDownloader(),
        _widgetAndroid.getG3MWidget().getG3MContext().getEffectsScheduler(),
        IFactory.instance().createTimer(),
        _widgetAndroid.getG3MWidget().getG3MContext().getStorage());

    // final G3MBuilder glob3Builder = new G3MBuilder();
    // _widgetAndroid =
    // glob3Builder.getSimpleBingGlob3(getApplicationContext());
    // setContentView(_widgetAndroid);
  }


  @Override
  protected G3MWidget_Android getWidgetAndroid() {
    return _widgetAndroid;
  }
}
