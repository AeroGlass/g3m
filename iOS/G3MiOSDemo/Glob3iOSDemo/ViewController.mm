//
//  ViewController.m
//  Glob3iOSDemo
//
//  Created by José Miguel S N on 31/05/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#import "ViewController.h"

#include "LayerSet.hpp"
#include "WMSLayer.hpp"
#include "TileRenderer.hpp"
#include "TilesRenderParameters.hpp"
#include "MarksRenderer.hpp"
#include "CameraConstraints.hpp"
#include "GLErrorRenderer.hpp"


@implementation ViewController

@synthesize G3MWidget;

- (void)didReceiveMemoryWarning
{
  [super didReceiveMemoryWarning];
  // Release any cached data, images, etc that aren't in use.
}

#pragma mark - View lifecycle

- (void)viewDidLoad
{
  [super viewDidLoad];
	// Do any additional setup after loading the view, typically from a nib.
  //  [[self G3MWidget] initWidgetDemo];
  [self initWidgetDemo];
  
  [[self G3MWidget] startAnimation];
}

- (void) initWidgetDemo
{
  
  std::vector<Renderer*> renderers;
  
  
  //LAYERS
  LayerSet* layerSet = new LayerSet();
  
  WMSLayer* blueMarble = new WMSLayer("bmng200405",
                                      URL("http://www.nasa.network.com/wms?"),
                                      WMS_1_1_0,
                                      Sector::fullSphere(),
                                      "image/jpeg",
                                      "EPSG:4326",
                                      "",
                                      false,
                                      NULL);
  layerSet->addLayer(blueMarble);
  
  if (false) {
    WMSLayer *osm = new WMSLayer("osm",
                                 URL("http://wms.latlon.org/"),
                                 WMS_1_1_0,
                                 Sector::fromDegrees(-85.05, -180.0, 85.5, 180.0),
                                 "image/jpeg",
                                 "EPSG:4326",
                                 "",
                                 false,
                                 NULL);
    layerSet->addLayer(osm);
  }
  
  
  //  WMSLayer *pnoa = new WMSLayer("PNOA",
  //                                "http://www.idee.es/wms/PNOA/PNOA",
  //                                WMS_1_1_0,
  //                                "image/png",
  //                                Sector::fromDegrees(21, -18, 45, 6),
  //                                "EPSG:4326",
  //                                "",
  //                                true,
  //                                Angle::nan(),
  //                                Angle::nan());
  //  layerSet->addLayer(pnoa);
  
  //  WMSLayer *vias = new WMSLayer("VIAS",
  //                                "http://idecan2.grafcan.es/ServicioWMS/Callejero",
  //                                WMS_1_1_0,
  //                                "image/gif",
  //                                Sector::fromDegrees(22.5,-22.5, 33.75, -11.25),
  //                                "EPSG:4326",
  //                                "",
  //                                true,
  //                                Angle::nan(),
  //                                Angle::nan());
  //  layerSet->addLayer(vias);
  
  //  WMSLayer *oceans = new WMSLayer(//"igo:bmng200401,igo:sttOZ,igo:cntOZ",
  //                                  "bmsstcnt",
  ////                                  "OZ",
  //                                  "bmsstcnt",
  ////                                  "OZ",
  ////                                  "http://igosoftware.dyndns.org:8081/geoserver/igo/wms",
  //                                  "http://igosoftware.dyndns.org:8081/geowebcache/service/wms",
  //                                  WMS_1_1_0,
  //                                  "image/jpeg",
  //                                  Sector::fullSphere(),
  //                                  "EPSG:4326",
  //                                  "",
  //                                  false,
  //                                  Angle::nan(),
  //                                  Angle::nan());
  //
  //  oceans->addTerrainTouchEventListener(new OceanTerrainTouchEventListener(factory, downloader));
  //  layerSet->addLayer(oceans);
  
  //  WMSLayer *osm = new WMSLayer("bing",
  //                               "bing",
  //                               "http://wms.latlon.org/",
  //                               WMS_1_1_0,
  //                               "image/jpeg",
  //                               Sector::fromDegrees(-85.05, -180.0, 85.5, 180.0),
  //                               "EPSG:4326",
  //                               "",
  //                               false,
  //                               Angle::nan(),
  //                               Angle::nan());
  //  layerSet->addLayer(osm);
  
  //  WMSLayer *osm = new WMSLayer("osm",
  //                               "osm",
  //                               "http://wms.latlon.org/",
  //                               WMS_1_1_0,
  //                               "image/jpeg",
  //                               Sector::fromDegrees(-85.05, -180.0, 85.5, 180.0),
  //                               "EPSG:4326",
  //                               "",
  //                               false,
  //                               Angle::nan(),
  //                               Angle::nan());
  //  layerSet->addLayer(osm);
  
  
  //  if (false) {
  //    // dummy renderer with a simple box
  //    DummyRenderer* dum = new DummyRenderer();
  //    comp->addRenderer(dum);
  //  }
  
  //  if (false) {
  //    // simple planet renderer, with a basic world image
  //    SimplePlanetRenderer* spr = new SimplePlanetRenderer("world.jpg");
  //    comp->addRenderer(spr);
  //  }
  
  if (true) {
    // marks renderer
    MarksRenderer* marks = new MarksRenderer();
    renderers.push_back(marks);
    
    Mark* m1 = new Mark("Fuerteventura",
                        "g3m-marker.png",
                        Geodetic3D(Angle::fromDegrees(28.05), Angle::fromDegrees(-14.36), 0));
    //m1->addTouchListener(listener);
    marks->addMark(m1);
    
    
    Mark* m2 = new Mark("Las Palmas",
                        "g3m-marker.png",
                        Geodetic3D(Angle::fromDegrees(28.05), Angle::fromDegrees(-15.36), 0));
    //m2->addTouchListener(listener);
    marks->addMark(m2);
    
    if (false) {
      for (int i = 0; i < 500; i++) {
        const Angle latitude = Angle::fromDegrees( (int) (arc4random() % 180) - 90 );
        const Angle longitude = Angle::fromDegrees( (int) (arc4random() % 360) - 180 );
        //NSLog(@"lat=%f, lon=%f", latitude.degrees(), longitude.degrees());
        
        marks->addMark(new Mark("Random",
                                "g3m-marker.png",
                                Geodetic3D(latitude, longitude, 0)));
      }
    }
  }
  
  //  if (false) {
  //    LatLonMeshRenderer *renderer = new LatLonMeshRenderer();
  //    renderers.push_back(renderer);
  //  }
  
  //  if (false) {
  //    SceneGraphRenderer* sgr = new SceneGraphRenderer();
  //    SGCubeNode* cube = new SGCubeNode();
  //    // cube->setScale(Vector3D(6378137.0, 6378137.0, 6378137.0));
  //    sgr->getRootNode()->addChild(cube);
  //    renderers.push_back(sgr);
  //  }
  
  renderers.push_back(new GLErrorRenderer());
  
  std::vector <ICameraConstrainer*> cameraConstraints;
  cameraConstraints.push_back(new SimpleCameraConstrainer());
  
  UserData* userData = NULL;
  [[self G3MWidget] initWidgetWithCameraConstraints: cameraConstraints
                                           layerSet: layerSet
                                          renderers: renderers
                                           userData: userData];
  
}

- (void)viewDidUnload
{
  [super viewDidUnload];
  // Release any retained subviews of the main view.
  // e.g. self.myOutlet = nil;
}

- (void)viewWillAppear:(BOOL)animated
{
  [super viewWillAppear:animated];
}

- (void)viewDidAppear:(BOOL)animated
{
  [super viewDidAppear:animated];
}

- (void)viewWillDisappear:(BOOL)animated
{
	[super viewWillDisappear:animated];
}

- (void)viewDidDisappear:(BOOL)animated
{
	[super viewDidDisappear:animated];
}

- (BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation)interfaceOrientation
{
  // Return YES for supported orientations
  if ([[UIDevice currentDevice] userInterfaceIdiom] == UIUserInterfaceIdiomPhone) {
    return (interfaceOrientation != UIInterfaceOrientationPortraitUpsideDown);
  } else {
    return YES;
  }
}

@end
