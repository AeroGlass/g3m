//
//  G3MWikiDownloadListener.mm
//  G3MApp
//
//  Created by Mari Luz Mateo on 22/02/13.
//  Copyright (c) 2013 Igo Software SL. All rights reserved.
//

#import "G3MWikiDownloadListener.hpp"

#import <G3MiOSSDK/G3MWidget_iOS.h>
#import <G3MiOSSDK/JSONBaseObject.hpp>
#import <G3MiOSSDK/IJSONParser.hpp>
#import <G3MiOSSDK/JSONObject.hpp>
#import <G3MiOSSDK/JSONArray.hpp>
#import <G3MiOSSDK/JSONString.hpp>
#import <G3MiOSSDK/JSONNumber.hpp>
#import <G3MiOSSDK/MarksRenderer.hpp>
#import <G3MiOSSDK/Mark.hpp>
#import "G3MAppUserData.hpp"
#import "G3MMarkerUserData.hpp"
#import "G3MAppInitializationTask.hpp"

G3MWikiDownloadListener::G3MWikiDownloadListener(GInitializationTask* initTask,
                                                 G3MWidget_iOS* widget) {
  _initTask = initTask;
  _widget = widget;
}

void G3MWikiDownloadListener::onDownload(const URL& url,
                                         IByteBuffer* buffer) {
  MarksRenderer* markerRenderer = ((G3MAppUserData*) [_widget getUserData])->getMarkerRenderer();
  JSONBaseObject* json = IJSONParser::instance()->parse(buffer->getAsString());
  const JSONArray* features = json->asObject()->getAsArray("features");
  
  for (int i = 0; i < features->size(); i++) {
    const JSONObject* item = features->getAsObject(i);
    
    const JSONObject* properties = item->asObject()->getAsObject("properties");
    std::string title = properties->getAsString("title")->value();
    std::string urlStr = properties->getAsString("url")->value();
    
    const JSONArray* coordinates = item->asObject()->getAsObject("geometry")->asObject()->getAsArray("coordinates");
    
    std::string markerIcon = "";
    if ([[UIScreen mainScreen] bounds].size.width < 768) {
      markerIcon = "file:///marker-wikipedia-48x48.png"; // iPhone
    }
    else {
      markerIcon = "file:///marker-wikipedia-72x72.png"; // iPad
    }
    
    Mark* marker = new Mark(URL(markerIcon, false),
                            Geodetic3D(Angle::fromDegrees(coordinates->getAsNumber(1)->value()),
                                       Angle::fromDegrees(coordinates->getAsNumber(0)->value()),
                                       0));
    
    MarkUserData* mud = new G3MMarkerUserData(title, URL(urlStr, false));
    marker->setUserData(mud);
    
    markerRenderer->addMark(marker);
  }
  ((G3MAppInitializationTask*) _initTask)->setWikiMarkersParsed(true);
  delete buffer;
  IJSONParser::instance()->deleteJSONData(json);
}

void G3MWikiDownloadListener::onError(const URL& url) {
  NSString* message = [NSString stringWithFormat: @"Oops!\nThere was a problem getting Wikipedia markers info"];
  
  UIAlertView *alert = [[UIAlertView alloc] initWithTitle: @"glob3 mobile"
                                                  message: message
                                                 delegate: nil
                                        cancelButtonTitle: @"OK"
                                        otherButtonTitles: nil];
  [alert show];
}
