

package org.glob3.mobile.client;

import java.util.ArrayList;

import org.glob3.mobile.generated.Angle;
import org.glob3.mobile.generated.BSONGenerator;
import org.glob3.mobile.generated.BSONParser;
import org.glob3.mobile.generated.BoxShape;
import org.glob3.mobile.generated.BusyMeshRenderer;
import org.glob3.mobile.generated.CameraDoubleDragHandler;
import org.glob3.mobile.generated.CameraDoubleTapHandler;
import org.glob3.mobile.generated.CameraRenderer;
import org.glob3.mobile.generated.CameraRotationHandler;
import org.glob3.mobile.generated.CameraSingleDragHandler;
import org.glob3.mobile.generated.CenterStrategy;
import org.glob3.mobile.generated.CircleShape;
import org.glob3.mobile.generated.Color;
import org.glob3.mobile.generated.CompositeRenderer;
import org.glob3.mobile.generated.DirectMesh;
import org.glob3.mobile.generated.FloatBufferBuilderFromColor;
import org.glob3.mobile.generated.FloatBufferBuilderFromGeodetic;
import org.glob3.mobile.generated.GInitializationTask;
import org.glob3.mobile.generated.GLPrimitive;
import org.glob3.mobile.generated.Geodetic3D;
import org.glob3.mobile.generated.IBufferDownloadListener;
import org.glob3.mobile.generated.IByteBuffer;
import org.glob3.mobile.generated.ICameraConstrainer;
import org.glob3.mobile.generated.IDownloader;
import org.glob3.mobile.generated.ILogger;
import org.glob3.mobile.generated.IStorage;
import org.glob3.mobile.generated.IThreadUtils;
import org.glob3.mobile.generated.ITimer;
import org.glob3.mobile.generated.JSONBaseObject;
import org.glob3.mobile.generated.JSONGenerator;
import org.glob3.mobile.generated.LayerSet;
import org.glob3.mobile.generated.Mark;
import org.glob3.mobile.generated.MarkTouchListener;
import org.glob3.mobile.generated.MarksRenderer;
import org.glob3.mobile.generated.Mesh;
import org.glob3.mobile.generated.MeshRenderer;
import org.glob3.mobile.generated.PeriodicalTask;
import org.glob3.mobile.generated.Planet;
import org.glob3.mobile.generated.Sector;
import org.glob3.mobile.generated.Shape;
import org.glob3.mobile.generated.ShapesRenderer;
import org.glob3.mobile.generated.SimpleCameraConstrainer;
import org.glob3.mobile.generated.TileRenderer;
import org.glob3.mobile.generated.TileRendererBuilder;
import org.glob3.mobile.generated.TimeInterval;
import org.glob3.mobile.generated.URL;
import org.glob3.mobile.generated.UserData;
import org.glob3.mobile.generated.Vector3D;
import org.glob3.mobile.generated.WMSLayer;
import org.glob3.mobile.generated.WMSServerVersion;
import org.glob3.mobile.specific.Downloader_WebGL;
import org.glob3.mobile.specific.G3MBuilder_WebGL;
import org.glob3.mobile.specific.G3MWidget_WebGL;
import org.glob3.mobile.specific.ThreadUtils_WebGL;
import org.glob3.mobile.specific.Timer_WebGL;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Random;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.RootPanel;


public class G3MWebGLDemo
         implements
            EntryPoint {

   private final static String FILE_SERVER        = "http://glob3m.glob3mobile.com/test/";

   private final String        _g3mWidgetHolderId = "g3mWidgetHolder";


   private G3MWidget_WebGL     _widget            = null;
   private ITimer              _timer;


   @Override
   public void onModuleLoad() {
      if (_widget == null) {

         // initialize a customized widget without using any builder
         //         initWithoutBuilder();

         // initialize a default widget by using a builder
         initDefaultWithBuilder();

         // initialize a customized widget by using a builder
         //         initCustomizedWithBuilder();
         //
         final Panel g3mWidgetHolder = RootPanel.get(_g3mWidgetHolderId);
         g3mWidgetHolder.add(_widget);

         _timer = new Timer_WebGL();
         _timer.start();

         final Button bson2jsonBtn = new Button("BSON -> JSON");
         bson2jsonBtn.getElement().getStyle().setProperty("position", "absolute");
         bson2jsonBtn.getElement().getStyle().setProperty("top", "0");
         bson2jsonBtn.getElement().getStyle().setProperty("left", "0");

         bson2jsonBtn.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(final ClickEvent event) {
               //               transformBSON2JSON();
               transformBSON2JSON();

            }
         });

         g3mWidgetHolder.add(bson2jsonBtn);


         final Button json2bsonBtn = new Button("JSON -> BSON");
         json2bsonBtn.getElement().getStyle().setProperty("position", "absolute");
         json2bsonBtn.getElement().getStyle().setProperty("top", "30");
         json2bsonBtn.getElement().getStyle().setProperty("left", "0");

         json2bsonBtn.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(final ClickEvent event) {
               //               transformBSON2JSON();
               transformJSON2BSON();

            }
         });

         g3mWidgetHolder.add(json2bsonBtn);
      }
   }


   /******************* BEGIN BSON *************************************/


   private void transformBSON2JSON() {

      //      final String fileName = "ACCESS-A.2011020104.nc7.slice10.bson";
      final String fileName = "ACCESS-test.bson";

      _widget.getG3MContext().getDownloader().requestBuffer(//
               new URL(FILE_SERVER + fileName, false), //
               100000000L, //
               TimeInterval.fromDays(30), //
               new IBufferDownloadListener() {

                  @Override
                  public void onError(final URL url) {
                     ILogger.instance().logError("error downloading");
                  }


                  @Override
                  public void onDownload(final URL url,
                                         final IByteBuffer buffer) {
                     final JSONBaseObject jsonBO = parseBSON(buffer);
                     generateJSON(jsonBO);
                  }


                  @Override
                  public void onCanceledDownload(final URL url,
                                                 final IByteBuffer data) {

                  }


                  @Override
                  public void onCancel(final URL url) {

                  }
               }, //
               true);
   }


   private void transformJSON2BSON() {

      //    final String fileName = "seymour-plane.json";
      //      final String fileName = "boundary_lines_land.geojson";
      final String fileName = "ACCESS-test.json";

      _widget.getG3MContext().getDownloader().requestBuffer(//
               new URL(FILE_SERVER + fileName, false), //
               100000000L, //
               TimeInterval.fromDays(30), //
               new IBufferDownloadListener() {

                  @Override
                  public void onError(final URL url) {
                     ILogger.instance().logError("error downloading");
                  }


                  @Override
                  public void onDownload(final URL url,
                                         final IByteBuffer buffer) {
                     final JSONBaseObject jsonBO = parseJSON(buffer);
                     final IByteBuffer bufBSON = generateBSON(jsonBO);
                  }


                  @Override
                  public void onCanceledDownload(final URL url,
                                                 final IByteBuffer data) {

                  }


                  @Override
                  public void onCancel(final URL url) {

                  }
               }, //
               true);
   }


   public JSONBaseObject parseBSON(final IByteBuffer buffer) {
      ILogger.instance().logInfo("BEGIN BSONParser.parse(buffer)");
      final TimeInterval beginTime = _timer.now();
      final JSONBaseObject bsonObj = BSONParser.parse(buffer);
      final long elapsedTime = _timer.now().milliseconds() - beginTime.milliseconds();
      ILogger.instance().logInfo("END BSONParser.parse(buffer):" + elapsedTime + "ms");

      return bsonObj;
   }


   public JSONBaseObject parseJSON(final IByteBuffer buffer) {
      ILogger.instance().logInfo("BEGIN _jsonParser.parse(buffer)");
      final TimeInterval beginTime = _timer.now();
      final JSONBaseObject jsonObj = _widget.getG3MContext().getJSONParser().parse(buffer);
      final long elapsedTime = _timer.now().milliseconds() - beginTime.milliseconds();
      ILogger.instance().logInfo("END _jsonParser.parse(buffer) " + elapsedTime + "ms");

      return jsonObj;
   }


   public IByteBuffer generateBSON(final JSONBaseObject jsonObj) {
      ILogger.instance().logInfo("BEGIN BSONGenerator.generate(jsonObj)");
      final TimeInterval beginTime = _timer.now();
      final IByteBuffer buffer = BSONGenerator.generate(jsonObj);
      final long elapsedTime = _timer.now().milliseconds() - beginTime.milliseconds();
      ILogger.instance().logInfo("END BSONGenerator.generate(jsonObj): " + elapsedTime + "ms");

      return buffer;
   }


   public String generateJSON(final JSONBaseObject jsonObj) {
      ILogger.instance().logInfo("BEGIN JSONGenerator.generate(jsonObj)");
      final TimeInterval beginTime = _timer.now();
      final String jsonString = JSONGenerator.generate(jsonObj);
      final long elapsedTime = _timer.now().milliseconds() - beginTime.milliseconds();
      ILogger.instance().logInfo("END JSONGenerator.generate(jsonObj): " + elapsedTime + "ms");

      return jsonString;
   }


   /******************* BEGIN BSON *************************************/


   public void initDefaultWithBuilder() {
      final G3MBuilder_WebGL builder = new G3MBuilder_WebGL();

      _widget = builder.createWidget();
   }


   public void initCustomizedWithBuilder() {
      final G3MBuilder_WebGL builder = new G3MBuilder_WebGL();

      final MeshRenderer meshRenderer = new MeshRenderer();
      meshRenderer.addMesh(createPointsMesh(builder.getPlanet()));
      builder.addRenderer(meshRenderer);

      final String proxy = "";
      final Downloader_WebGL downloader = new Downloader_WebGL(8, 10, proxy);
      builder.setDownloader(downloader);

      _widget = builder.createWidget();
   }


   private Mesh createPointsMesh(final Planet planet) {
      final FloatBufferBuilderFromGeodetic vertices = new FloatBufferBuilderFromGeodetic(CenterStrategy.firstVertex(), planet,
               Geodetic3D.zero());
      final FloatBufferBuilderFromColor colors = new FloatBufferBuilderFromColor();

      final Angle centerLat = Angle.fromDegreesMinutesSeconds(38, 53, 42);
      final Angle centerLon = Angle.fromDegreesMinutesSeconds(-77, 02, 11);

      final Angle deltaLat = Angle.fromDegrees(1).div(16);
      final Angle deltaLon = Angle.fromDegrees(1).div(16);

      final int steps = 128;
      final int halfSteps = steps / 2;
      for (int i = -halfSteps; i < halfSteps; i++) {
         final Angle lat = centerLat.add(deltaLat.times(i));
         for (int j = -halfSteps; j < halfSteps; j++) {
            final Angle lon = centerLon.add(deltaLon.times(j));

            vertices.add(new Geodetic3D(lat, lon, 100000));

            final float red = (float) (i + halfSteps + 1) / steps;
            final float green = (float) (j + halfSteps + 1) / steps;
            colors.add(Color.fromRGBA(red, green, 0, 1));
         }
      }

      final float lineWidth = 1;
      final float pointSize = 2;
      final Color flatColor = null;
      return new DirectMesh(GLPrimitive.points(), true, vertices.getCenter(), vertices.create(), lineWidth, pointSize, flatColor,
               colors.create());
   }


   public void initWithoutBuilder() {

      final Panel g3mWidgetHolder = RootPanel.get(_g3mWidgetHolderId);

      g3mWidgetHolder.setWidth("640px");
      g3mWidgetHolder.setHeight("480px");

      _widget = new G3MWidget_WebGL();

      if (_widget.isSupported()) {

         final IStorage storage = null;

         final int delayMillis = 10;
         final String proxy = "";
         final IDownloader downloader = new Downloader_WebGL(8, delayMillis, proxy);

         final IThreadUtils threadUtils = new ThreadUtils_WebGL(delayMillis);

         final Planet planet = Planet.createEarth();

         final ArrayList<ICameraConstrainer> cameraConstraints = new ArrayList<ICameraConstrainer>();
         cameraConstraints.add(new SimpleCameraConstrainer());

         final CameraRenderer cameraRenderer = new CameraRenderer();
         final boolean useInertia = true;
         cameraRenderer.addHandler(new CameraSingleDragHandler(useInertia));
         final boolean processRotation = true;
         final boolean processZoom = true;
         cameraRenderer.addHandler(new CameraDoubleDragHandler(processRotation, processZoom));
         cameraRenderer.addHandler(new CameraRotationHandler());
         cameraRenderer.addHandler(new CameraDoubleTapHandler());

         final CompositeRenderer mainRenderer = new CompositeRenderer();

         final LayerSet layerSet = new LayerSet();
         final boolean useBing = false;
         if (useBing) {
            final WMSLayer bing = new WMSLayer( //
                     "ve", //
                     new URL("http://worldwind27.arc.nasa.gov/wms/virtualearth?", false), //
                     WMSServerVersion.WMS_1_1_0, //
                     Sector.fullSphere(), //
                     "image/png", //
                     "EPSG:4326", //
                     "", //
                     false, //
                     null);
            layerSet.addLayer(bing);
         }
         final boolean useOSMLatLon = true;
         if (useOSMLatLon) {
            //         final WMSLayer osm = new WMSLayer( //
            //                  "osm", //
            //                  new URL("http://wms.latlon.org/", false), //
            //                  WMSServerVersion.WMS_1_1_0, //
            //                  Sector.fromDegrees(-85.05, -180.0, 85.5, 180.0), //
            //                  "image/jpeg", //
            //                  "EPSG:4326", //
            //                  "", //
            //                  false, //
            //                  null);
            //         layerSet.addLayer(osm);

            final WMSLayer osm = new WMSLayer( //
                     "osm_auto:all", //
                     new URL("http://129.206.228.72/cached/osm", false), //
                     WMSServerVersion.WMS_1_1_0, //
                     // Sector.fromDegrees(-85.05, -180.0, 85.05, 180.0), //
                     Sector.fullSphere(), //
                     "image/jpeg", //
                     "EPSG:4326", //
                     "", //
                     false, //
                     // new LevelTileCondition(3, 100));
                     null);
            layerSet.addLayer(osm);
         }

         final boolean usePnoa = true;
         if (usePnoa) {
            final WMSLayer pnoa = new WMSLayer( //
                     "PNOA", //
                     new URL("http://www.idee.es/wms/PNOA/PNOA", false), //
                     WMSServerVersion.WMS_1_1_0, Sector.fromDegrees(21, -18, 45, 6), //
                     "image/png", //
                     "EPSG:4326", //
                     "", //
                     true, //
                     null);
            layerSet.addLayer(pnoa);
         }

         final boolean testURLescape = false;
         if (testURLescape) {
            final WMSLayer ayto = new WMSLayer(URL.escape("Ejes de via"), //
                     new URL("http://sig.caceres.es/wms_callejero.mapdef?", false), //
                     WMSServerVersion.WMS_1_1_0,//  
                     Sector.fullSphere(), //
                     "image/png", //
                     "EPSG:4326", //
                     "", //
                     true, //
                     null);
            layerSet.addLayer(ayto);
         }


         final TileRendererBuilder tlBuilder = new TileRendererBuilder();
         tlBuilder.setLayerSet(layerSet);
         final TileRenderer tileRenderer = tlBuilder.create();
         mainRenderer.addRenderer(tileRenderer);

         final boolean useMarkers = true;
         if (useMarkers) {
            // marks renderer
            final boolean readyWhenMarksReady = false;
            final MarksRenderer marksRenderer = new MarksRenderer(readyWhenMarksReady);

            marksRenderer.setMarkTouchListener(new MarkTouchListener() {
               @Override
               public boolean touchedMark(final Mark mark) {
                  Window.alert("Touched on mark: " + mark.getName());
                  return true;
               }
            }, true);


            final Mark m1 = new Mark(//
                     "Fuerteventura", //
                     new URL("http://glob3m.glob3mobile.com/icons/markers/g3m.png", false), //
                     new Geodetic3D(Angle.fromDegrees(28.05), Angle.fromDegrees(-14.36), 0));
            //m1->addTouchListener(listener);
            marksRenderer.addMark(m1);

            final Mark m2 = new Mark( //
                     "Las Palmas", //
                     new URL("http://glob3m.glob3mobile.com/icons/markers/g3m.png", false), //
                     new Geodetic3D(Angle.fromDegrees(28.05), Angle.fromDegrees(-15.36), 0));
            //m2->addTouchListener(listener);
            marksRenderer.addMark(m2);

            final boolean randomMarkers = false;
            if (randomMarkers) {
               for (int i = 0; i < 500; i++) {
                  final Angle latitude = Angle.fromDegrees((Random.nextInt() % 180) - 90);
                  final Angle longitude = Angle.fromDegrees((Random.nextInt() % 360) - 180);
                  //NSLog(@"lat=%f, lon=%f", latitude.degrees(), longitude.degrees());

                  marksRenderer.addMark(new Mark( //
                           "Random", //
                           new URL("http://glob3m.glob3mobile.com/icons/markers/g3m.png", false), //
                           new Geodetic3D(latitude, longitude, 0)));
               }
            }
            mainRenderer.addRenderer(marksRenderer);
         }

         final boolean useQuadShapes = true;
         if (useQuadShapes) {
            final ShapesRenderer shapesRenderer = new ShapesRenderer();

            //         final String textureFileName = "g3m-marker.png";
            //         final IImage textureImage = IFactory.instance().createImageFromFileName(textureFileName);
            //
            //         final QuadShape quad = new QuadShape( //
            //                  new Geodetic3D(Angle.fromDegrees(37.78333333), //
            //                           Angle.fromDegrees(-122.41666666666667), //
            //                           10000), //
            //                  textureImage, //
            //                  true, //
            //                  textureFileName, //
            //                  500000, //
            //                  500000);
            //         quad.setHeading(Angle.fromDegrees(0));
            //         quad.setPitch(Angle.fromDegrees(0));
            //         shapesRenderer.addShape(quad);

            final Geodetic3D circlePosition = new Geodetic3D( //
                     Angle.fromDegrees(37.78333333), //
                     Angle.fromDegrees(-122.41666666666667), //
                     8000);

            //circle.setHeading(Angle.fromDegrees(45));
            //circle.setPitch(Angle.fromDegrees(45));
            //circle.setScale(2.0, 0.5, 1);
            //circle.setRadius(circleRadius);

            final Color circleColor = Color.newFromRGBA(1, 1, 0, 0.5f);
            final Shape circle = new CircleShape(circlePosition, 50000, circleColor);
            shapesRenderer.addShape(circle);

            final Geodetic3D boxPosition = new Geodetic3D(Angle.fromDegrees(37.78333333), //
                     Angle.fromDegrees(-122.41666666666667), //
                     45000);
            final Vector3D size = new Vector3D(20000, 30000, 50000);
            final Color boxColor = Color.newFromRGBA(0, 1, 0, 0.5f);
            final Color edgeColor = Color.newFromRGBA(0.75f, 0, 0, 0.75f);
            final Shape box = new BoxShape(boxPosition, size, 2, boxColor, edgeColor);
            shapesRenderer.addShape(box);

            mainRenderer.addRenderer(shapesRenderer);
         }


         final BusyMeshRenderer busyRenderer = new BusyMeshRenderer();

         final Color backgroundColor = Color.fromRGBA(0, (float) 0.1, (float) 0.2, 1);

         final boolean logFPS = false;

         final boolean logDownloaderStatistics = false;

         final GInitializationTask initializationTask = null;

         final boolean autoDeleteInitializationTask = true;

         final ArrayList<PeriodicalTask> periodicalTasks = new ArrayList<PeriodicalTask>();

         final UserData userData = null;


         _widget.initWidget(//
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
      }
   }
}
