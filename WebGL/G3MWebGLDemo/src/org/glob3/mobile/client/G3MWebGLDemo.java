

package org.glob3.mobile.client;

import java.util.ArrayList;

import org.glob3.mobile.generated.Angle;
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
import org.glob3.mobile.generated.G3MContext;
import org.glob3.mobile.generated.GInitializationTask;
import org.glob3.mobile.generated.GLPrimitive;
import org.glob3.mobile.generated.Geodetic2D;
import org.glob3.mobile.generated.Geodetic3D;
import org.glob3.mobile.generated.IBufferDownloadListener;
import org.glob3.mobile.generated.IByteBuffer;
import org.glob3.mobile.generated.ICameraActivityListener;
import org.glob3.mobile.generated.ICameraConstrainer;
import org.glob3.mobile.generated.IDownloader;
import org.glob3.mobile.generated.IImage;
import org.glob3.mobile.generated.IImageDownloadListener;
import org.glob3.mobile.generated.IImageListener;
import org.glob3.mobile.generated.IImageUtils;
import org.glob3.mobile.generated.IJSONParser;
import org.glob3.mobile.generated.ILogger;
import org.glob3.mobile.generated.IStorage;
import org.glob3.mobile.generated.IThreadUtils;
import org.glob3.mobile.generated.JSONArray;
import org.glob3.mobile.generated.JSONBaseObject;
import org.glob3.mobile.generated.JSONObject;
import org.glob3.mobile.generated.LayerSet;
import org.glob3.mobile.generated.LayerTilesRenderParameters;
import org.glob3.mobile.generated.LevelTileCondition;
import org.glob3.mobile.generated.Mark;
import org.glob3.mobile.generated.MarkTouchListener;
import org.glob3.mobile.generated.MarksRenderer;
import org.glob3.mobile.generated.Mesh;
import org.glob3.mobile.generated.MeshRenderer;
import org.glob3.mobile.generated.PeriodicalTask;
import org.glob3.mobile.generated.Planet;
import org.glob3.mobile.generated.QuadShape;
import org.glob3.mobile.generated.RectangleF;
import org.glob3.mobile.generated.SceneJSShapesParser;
import org.glob3.mobile.generated.Sector;
import org.glob3.mobile.generated.Shape;
import org.glob3.mobile.generated.ShapesRenderer;
import org.glob3.mobile.generated.SimpleCameraConstrainer;
import org.glob3.mobile.generated.TileRenderer;
import org.glob3.mobile.generated.TileRendererBuilder;
import org.glob3.mobile.generated.TimeInterval;
import org.glob3.mobile.generated.URL;
import org.glob3.mobile.generated.Vector2I;
import org.glob3.mobile.generated.Vector3D;
import org.glob3.mobile.generated.WMSLayer;
import org.glob3.mobile.generated.WMSServerVersion;
import org.glob3.mobile.generated.WidgetUserData;
import org.glob3.mobile.specific.Downloader_WebGL;
import org.glob3.mobile.specific.G3MBuilder_WebGL;
import org.glob3.mobile.specific.G3MWidget_WebGL;
import org.glob3.mobile.specific.ThreadUtils_WebGL;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.Random;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.RootPanel;


public class G3MWebGLDemo
         implements
            EntryPoint {

   private final String    _g3mWidgetHolderId = "g3mWidgetHolder";


   private G3MWidget_WebGL _widget            = null;

   private boolean         _markersParsed     = false;
   private MarksRenderer   _markersRenderer;


   @Override
   public void onModuleLoad() {
      if (_widget == null) {

         // initialize a customized widget without using any builder
         //initWithoutBuilder();

         // initialize a default widget by using a builder
         initDefaultWithBuilder();

         // initialize a customized widget by using a builder
         //         initCustomizedWithBuilder();

         final Panel g3mWidgetHolder = RootPanel.get(_g3mWidgetHolderId);
         g3mWidgetHolder.add(_widget);
      }
   }


   public void initDefaultWithBuilder() {
      final G3MBuilder_WebGL builder = new G3MBuilder_WebGL();

      _markersRenderer = new MarksRenderer(true);
      _markersRenderer.setMarkTouchListener(new MarkTouchListener() {

         @Override
         public boolean touchedMark(final Mark mark) {
            Window.alert(mark.getLabel());
            return false;
         }
      }, true);

      builder.addRenderer(_markersRenderer);

      final ShapesRenderer shapesRenderer = new ShapesRenderer();
      builder.addRenderer(shapesRenderer);

      builder.setInitializationTask(createMarkersInitializationTask());

      final GInitializationTask initializationTask = new GInitializationTask() {
         @Override
         public void run(final G3MContext context) {
            //            final ICanvas canvas = context.getFactory().createCanvas();
            //
            //
            //            final String text = "Hello World!";
            //            //final GFont font = GFont.serif();
            //            //final GFont font = GFont.monospaced();
            //            final GFont font = GFont.sansSerif();
            //
            //            canvas.setFont(font);
            //
            //            final Vector2F textExtent = canvas.textExtent(text);
            //
            //            canvas.initialize(256, 256);
            //
            //            canvas.setFillColor(Color.fromRGBA(1f, 1f, 1f, 0.75f));
            //            canvas.fillRoundedRectangle(0, 0, 256, 256, 32);
            //            //canvas.fillRectangle(0, 0, 256, 256);
            //
            //            canvas.setShadow(Color.black(), 5f, 3.5f, -3.5f);
            //            canvas.setFillColor(Color.fromRGBA(1f, 0f, 0f, 0.5f));
            //            canvas.fillRectangle(32, 64, 64, 128);
            //            canvas.removeShadow();
            //
            //
            //            canvas.setStrokeColor(Color.fromRGBA(1f, 0f, 1f, 0.9f));
            //            canvas.setStrokeWidth(2.5f);
            //            final float margin = 1.25f;
            //            canvas.strokeRoundedRectangle(0 + margin, 0 + margin, 256 - (margin * 2), 256 - (margin * 2), 32);
            //            //canvas.strokeRectangle(0 + margin, 0 + margin, 256 - (margin * 2), 256 - (margin * 2));
            //
            //            canvas.setFillColor(Color.fromRGBA(1, 1, 0, 0.9f));
            //            canvas.setStrokeWidth(1.1f);
            //            canvas.setStrokeColor(Color.fromRGBA(0, 0, 0, 0.9f));
            //            canvas.fillAndStrokeRoundedRectangle(128, 16, 64, 64, 8);
            //            //canvas.fillAndStrokeRectangle(128, 16, 64, 64);
            //
            //            final int __DGD_working_at_Canvas;
            //
            //
            //            canvas.setFillColor(Color.white());
            //            canvas.setShadow(Color.black(), 5, 1, -1);
            //            canvas.fillText(text, 128 - (textExtent._x / 2), 128 - (textExtent._y / 2));
            //
            //            canvas.removeShadow();
            //            canvas.setFillColor(Color.black());
            //            canvas.fillRectangle(10, 10, 5, 5);
            //
            //            final IImageListener listener = new IImageListener() {
            //
            //               @Override
            //               public void imageCreated(final IImage image) {
            //                  final Shape quad = new QuadShape( //
            //                           new Geodetic3D(Angle.fromDegrees(37.78333333), Angle.fromDegrees(-121.5), 8000), //
            //                           image, //
            //                           50000, 50000);
            //                  shapesRenderer.addShape(quad);
            //               }
            //            };
            //            canvas.createImage(listener, true);
            //
            //            canvas.dispose();

         }


         @Override
         public boolean isDone(final G3MContext context) {
            return true;
         }
      };

      final LayerSet layerSet = new LayerSet();

      final boolean blueMarble = true;
      if (blueMarble) {
         final WMSLayer blueMarbleL = new WMSLayer( //
                  "bmng200405", //
                  new URL("http://www.nasa.network.com/wms?", false), //
                  WMSServerVersion.WMS_1_1_0, //
                  Sector.fullSphere(), //
                  "image/jpeg", //
                  "EPSG:4326", //
                  "", //
                  false, //
                  //new LevelTileCondition(0, 6),
                  null, //
                  TimeInterval.fromDays(30), //
                  true);
         layerSet.addLayer(blueMarbleL);
      }

      final boolean useOrtoAyto = true;
      if (useOrtoAyto) {

         final LayerTilesRenderParameters ltrp = new LayerTilesRenderParameters(Sector.fullSphere(), 2, 4, 0, 19, new Vector2I(
                  256, 256), LayerTilesRenderParameters.defaultTileMeshResolution(), false);

         final WMSLayer ortoAyto = new WMSLayer( //
                  "orto_refundida", //
                  new URL("http://195.57.27.86/wms_etiquetas_con_orto.mapdef?", false), //
                  WMSServerVersion.WMS_1_1_0, //
                  new Sector( //
                           new Geodetic2D(Angle.fromDegrees(39.350228), Angle.fromDegrees(-6.508713)), //
                           new Geodetic2D(Angle.fromDegrees(39.536351), Angle.fromDegrees(-6.25946))), //
                  "image/jpeg", //
                  "EPSG:4326", //
                  "", //
                  false, //
                  new LevelTileCondition(4, 19), //
                  TimeInterval.fromDays(30), //
                  true, //
                  ltrp);
         layerSet.addLayer(ortoAyto);
      }

      builder.setInitializationTask(initializationTask);

      _widget = builder.createWidget();

   }


   public void initCustomizedWithBuilder() {
      final G3MBuilder_WebGL builder = new G3MBuilder_WebGL();

      final MeshRenderer meshRenderer = new MeshRenderer();
      meshRenderer.addMesh(createPointsMesh(builder.getPlanet()));
      builder.addRenderer(meshRenderer);


      final boolean useMarkers = true;
      if (useMarkers) {
         // marks renderer
         final boolean readyWhenMarksReady = false;
         final MarksRenderer marksRenderer = new MarksRenderer(readyWhenMarksReady);

         marksRenderer.setMarkTouchListener(new MarkTouchListener() {
            @Override
            public boolean touchedMark(final Mark mark) {
               Window.alert("Touched on mark: " + mark.getLabel());
               return true;
            }
         }, true);


         final Mark m1 = new Mark( //
                  "Label", new URL("http://glob3m.glob3mobile.com/icons/markers/g3m.png", false), //
                  new Geodetic3D(Angle.fromDegrees(28.05), Angle.fromDegrees(-14.36), 0));
         //m1->addTouchListener(listener);
         marksRenderer.addMark(m1);

         final Mark m2 = new Mark( //
                  "Las Palmas", //
                  new URL("http://glob3m.glob3mobile.com/icons/markers/g3m.png", false), //
                  new Geodetic3D(Angle.fromDegrees(28.05), Angle.fromDegrees(-15.36), 0), //
                  0, //
                  false);
         //m2->addTouchListener(listener);
         marksRenderer.addMark(m2);


         final Mark m3 = new Mark( //
                  "Washington, DC", //
                  new Geodetic3D(Angle.fromDegreesMinutesSeconds(38, 53, 42.24), Angle.fromDegreesMinutesSeconds(-77, 2, 10.92),
                           100), //
                  0);
         marksRenderer.addMark(m3);


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
         builder.addRenderer(marksRenderer);
      }


      final String proxy = "";
      final Downloader_WebGL downloader = new Downloader_WebGL(8, 10, proxy);
      builder.setDownloader(downloader);


      // test bson parser and 3D model
      final ShapesRenderer shapeRenderer = new ShapesRenderer();
      builder.addRenderer(shapeRenderer);

      builder.setInitializationTask(new GInitializationTask() {

         private boolean done = false;


         @Override
         public void run(final G3MContext context) {
            context.getDownloader().requestBuffer( //
                     new URL("http://glob3m.glob3mobile.com/test/aircraft-A320/A320.bson", false), //
                     0, //
                     TimeInterval.forever(), //
                     true, //
                     new IBufferDownloadListener() {

                        @Override
                        public void onError(final URL url) {
                           ILogger.instance().logError("error downloading A320.bson");
                        }


                        @Override
                        public void onDownload(final URL url,
                                               final IByteBuffer buffer,
                                               final boolean expired) {
                           final Shape aircraft = SceneJSShapesParser.parseFromBSON(buffer,
                                    "http://glob3m.glob3mobile.com/test/aircraft-A320/textures-A320/", false);

                           if (aircraft != null) {
                              // Washington, DC
                              aircraft.setPosition(new Geodetic3D(Angle.fromDegreesMinutesSeconds(38, 53, 42.24), //
                                       Angle.fromDegreesMinutesSeconds(-77, 2, 10.92), //
                                       10000));
                              final double scale = 200;
                              aircraft.setScale(scale, scale, scale);
                              aircraft.setPitch(Angle.fromDegrees(90));
                              shapeRenderer.addShape(aircraft);
                           }
                           done = true;
                        }


                        @Override
                        public void onCanceledDownload(final URL url,
                                                       final IByteBuffer data,
                                                       final boolean expired) {
                        }


                        @Override
                        public void onCancel(final URL url) {
                        }
                     }, false);
         }


         @Override
         public boolean isDone(final G3MContext context) {
            return done;
         }
      });

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

         final boolean blueMarble = true;
         if (blueMarble) {
            final WMSLayer blueMarbleL = new WMSLayer( //
                     "bmng200405", //
                     new URL("http://www.nasa.network.com/wms?", false), //
                     WMSServerVersion.WMS_1_1_0, //
                     Sector.fullSphere(), //
                     "image/jpeg", //
                     "EPSG:4326", //
                     "", //
                     false, //
                     //new LevelTileCondition(0, 6),
                     null, //
                     TimeInterval.fromDays(30), //
                     true);
            layerSet.addLayer(blueMarbleL);
         }

         final boolean useOrtoAyto = true;
         if (useOrtoAyto) {

            final LayerTilesRenderParameters ltrp = new LayerTilesRenderParameters(Sector.fullSphere(), 2, 4, 0, 19,
                     new Vector2I(256, 256), LayerTilesRenderParameters.defaultTileMeshResolution(), false);

            final WMSLayer ortoAyto = new WMSLayer(
            //
                     "orto_refundida", //
                     new URL("http://195.57.27.86/wms_etiquetas_con_orto.mapdef?", false), //
                     WMSServerVersion.WMS_1_1_0, //
                     new Sector(new Geodetic2D(Angle.fromDegrees(39.350228), Angle.fromDegrees(-6.508713)), new Geodetic2D(
                              Angle.fromDegrees(39.536351), Angle.fromDegrees(-6.25946))), //
                     "image/jpeg", //
                     "EPSG:4326", //
                     "", //
                     false, //
                     new LevelTileCondition(4, 19), //
                     TimeInterval.fromDays(30), //
                     true, //
                     ltrp);
            layerSet.addLayer(ortoAyto);
         }


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
                     null, //
                     TimeInterval.fromDays(30), //
                     true);
            layerSet.addLayer(bing);
         }
         final boolean useOSMLatLon = false;
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
                     null, //
                     TimeInterval.fromDays(30), //
                     true);
            layerSet.addLayer(osm);
         }

         final boolean usePnoa = false;
         if (usePnoa) {
            final WMSLayer pnoa = new WMSLayer( //
                     "PNOA", //
                     new URL("http://www.idee.es/wms/PNOA/PNOA", false), //
                     WMSServerVersion.WMS_1_1_0, Sector.fromDegrees(21, -18, 45, 6), //
                     "image/png", //
                     "EPSG:4326", //
                     "", //
                     true, //
                     null, //
                     TimeInterval.fromDays(30), //
                     true);
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
                     null, //
                     TimeInterval.fromDays(30), //
                     true);
            layerSet.addLayer(ayto);
         }


         final TileRendererBuilder tlBuilder = new TileRendererBuilder();
         tlBuilder.setLayerSet(layerSet);
         tlBuilder.setRenderDebug(true);
         final TileRenderer tileRenderer = tlBuilder.create();
         mainRenderer.addRenderer(tileRenderer);


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


            final boolean testingImagesCombine = false;
            if (testingImagesCombine) {
               class DL
                        extends
                           IImageDownloadListener {

                  @Override
                  public void onDownload(final URL url,
                                         final IImage image,
                                         final boolean expired) {

                     final int w = image.getWidth();
                     final int h = image.getHeight();

                     final java.util.ArrayList<IImage> images = new ArrayList<IImage>();
                     images.add(image);
                     images.add(image);

                     final java.util.ArrayList<RectangleF> srcRs = new ArrayList<RectangleF>();
                     srcRs.add(new RectangleF(0, 0, image.getWidth(), image.getHeight()));
                     srcRs.add(new RectangleF(10, 0, image.getWidth() - 10, image.getHeight()));

                     final java.util.ArrayList<RectangleF> destRs = new ArrayList<RectangleF>();
                     destRs.add(new RectangleF(0, 0, 256, 256));
                     destRs.add(new RectangleF(50, 20, 256, 70));

                     class QuadListener
                              extends
                                 IImageListener {
                        ShapesRenderer _sr;


                        public QuadListener(final ShapesRenderer sr) {
                           _sr = sr;

                        }


                        @Override
                        public void imageCreated(final IImage image2) {
                           final Shape quadImages = new QuadShape(new Geodetic3D(Angle.fromDegrees(28.410728),
                                    Angle.fromDegrees(-16.339417), 8000), image2, 50000, 50000);

                           _sr.addShape(quadImages);
                        }
                     }


                     IImageUtils.combine(new Vector2I(256, 256), images, srcRs, destRs, new QuadListener(shapesRenderer), true);

                  }


                  @Override
                  public void onError(final URL url) {
                  }


                  @Override
                  public void onCancel(final URL url) {
                  }


                  @Override
                  public void onCanceledDownload(final URL url,
                                                 final IImage image,
                                                 final boolean expired) {
                  }

               }


               downloader.requestImage( //
                        new URL(
                                 "http://www.nasa.network.com/wms?REQUEST=GetMap&SERVICE=WMS&VERSION=1.1.1&WIDTH=256&HEIGHT=256&BBOX=-45.0,-90.0,0.0,-45.0&LAYERS=bmng200405&FORMAT=image/jpeg&SRS=EPSG:4326&STYLES=&TRANSPARENT=FALSE",
                                 false), //
                        100000, //
                        TimeInterval.fromDays(1), //
                        true, //
                        new DL(), //
                        true);

            }

         }


         final BusyMeshRenderer busyRenderer = new BusyMeshRenderer(Color.newFromRGBA(0, 0, 0, 1));

         final Color backgroundColor = Color.fromRGBA(0, (float) 0.1, (float) 0.2, 1);

         final boolean logFPS = false;

         final boolean logDownloaderStatistics = false;

         final GInitializationTask initializationTask = null;

         final boolean autoDeleteInitializationTask = true;

         final ArrayList<PeriodicalTask> periodicalTasks = new ArrayList<PeriodicalTask>();

         final WidgetUserData userData = null;


         final ICameraActivityListener cameraActivityListener = null;
         _widget.initWidget(//
                  storage, // 
                  downloader, //
                  threadUtils, //
                  cameraActivityListener, //
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


   private GInitializationTask createMarkersInitializationTask() {
      final GInitializationTask initializationTask = new GInitializationTask() {

         @Override
         public void run(final G3MContext context) {

            final IDownloader downloader = context.getDownloader();

            final IBufferDownloadListener listener = new IBufferDownloadListener() {


               @Override
               public void onDownload(final URL url,
                                      final IByteBuffer buffer,
                                      final boolean expired) {

                  final String response = buffer.getAsString();
                  final IJSONParser parser = context.getJSONParser();
                  final JSONBaseObject jsonObject = parser.parse(response);
                  final JSONObject object = jsonObject.asObject();
                  final JSONArray list = object.getAsArray("list");
                  for (int i = 0; i < list.size(); i++) {

                     final JSONObject city = list.getAsObject(i);

                     final JSONObject coords = city.getAsObject("coord");
                     final Geodetic2D position = new Geodetic2D(Angle.fromDegrees(coords.getAsNumber("lat").value()),
                              Angle.fromDegrees(coords.getAsNumber("lon").value()));
                     final JSONArray weather = city.getAsArray("weather");
                     final JSONObject weatherObject = weather.getAsObject(0);


                     String icon = "";
                     if (weatherObject.getAsString("icon", "DOUBLE").equals("DOUBLE")) {
                        icon = "" + (int) weatherObject.getAsNumber("icon").value() + "d.png";
                        if (icon.length() < 7) {
                           icon = "0" + icon;
                        }
                     }
                     else {
                        icon = weatherObject.getAsString("icon", "DOUBLE") + ".png";
                     }

                     _markersRenderer.addMark(new Mark(//
                              city.getAsString("name", ""), //
                              new URL("http://openweathermap.org/img/w/" + icon, false), //
                              new Geodetic3D(position, 0), //
                              0, //
                              true, //
                              14, //
                              Color.white(), //
                              Color.black(), //
                              2));
                  }


                  _markersParsed = true;
               }


               @Override
               public void onError(final URL url) {
                  Window.alert("Error retrieving weather data");
               }


               @Override
               public void onCancel(final URL url) {
                  //DO Nothing
               }


               @Override
               public void onCanceledDownload(final URL url,
                                              final IByteBuffer data,
                                              final boolean expired) {
                  //Do Nothing
               }
            };

            downloader.requestBuffer( //
                     new URL("http://openweathermap.org/data/2.1/find/city?bbox=-80,-180,80,180,4&cluster=yes", false), //
                     0, //
                     TimeInterval.fromHours(1.0), //
                     false, listener, //
                     false);
         }


         @Override
         public boolean isDone(final G3MContext context) {
            if (_markersParsed) {
               _widget.setAnimatedCameraPosition(new Geodetic3D(Angle.fromDegrees(45d), Angle.fromDegrees(0.d), 3000000),
                        TimeInterval.fromSeconds(3));
               return true;
            }
            return false;
         }
      };
      return initializationTask;
   }
}
