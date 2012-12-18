//
//  G3MJSONBuilder.mm
//  G3MiOSSDK
//
//  Created by Eduardo de la Montaña on 30/10/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

#include <iostream>

#include "G3MJSONBuilder_iOS.hpp"
#include "SceneParser.hpp"
#include "SQLiteStorage_iOS.hpp"
#include "CachedDownloader.hpp"
#include "Downloader_iOS.hpp"
#include "MarksRenderer.hpp"
#include "GEOJSONDownloadListener.hpp"
#include "PanoDownloadListener.hpp"
#include "IStringBuilder.hpp"

#include "ThreadUtils_iOS.hpp"
#include "CompositeRenderer.hpp"

#include "IStorage.hpp"
#include "IDownloader.hpp"
#include "GInitializationTask.hpp"
#include "G3MBuilder_iOS.hpp"

G3MJSONBuilder_iOS::G3MJSONBuilder_iOS(std::string jsonSource, G3MWidget_iOS* g3mWidget): IG3MJSONBuilder(jsonSource), _g3mWidget(g3mWidget) {
};


void  G3MJSONBuilder_iOS::initWidgetWithCameraConstraints (std::vector<ICameraConstrainer*> cameraConstraints,  LayerSet* layerSet, bool incrementalTileQuality, std::vector<Renderer*> renderers, UserData* userData, GInitializationTask* initializationTask, std::vector<PeriodicalTask*> periodicalTasks, MarkTouchListener* markTouchListener, MarkTouchListener* panoTouchListener){
    
    const bool readyWhenMarksReady = false;
    MarksRenderer* marksRenderer = new MarksRenderer(readyWhenMarksReady);
    
    if (markTouchListener != NULL) {
        marksRenderer->setMarkTouchListener(markTouchListener, true);
    }
    renderers.push_back(marksRenderer);
    
    SceneParser::instance()->parse(layerSet, _jsonSource);
    
    class G3MJSONBuilderInitializationTask : public GInitializationTask {
    private:
        GInitializationTask* _customInitializationTask;
        MarksRenderer* _marksRenderer;
        std::map<std::string, std::string> _mapGeoJSONSources;
        std::vector<std::string> _panoSources;
        MarkTouchListener* _panoTouchListener;
        G3MWidget_iOS* _g3mWidget;
    public:
        G3MJSONBuilderInitializationTask(GInitializationTask* customInitializationTask, MarksRenderer* marksRenderer, G3MWidget_iOS* g3mWidget, MarkTouchListener* panoTouchListener){
            _customInitializationTask = customInitializationTask;
            _marksRenderer = marksRenderer;
            _mapGeoJSONSources = SceneParser::instance()->getMapGeoJSONSources();
            _panoSources = SceneParser::instance()->getPanoSources();
            _panoTouchListener = panoTouchListener;
            _g3mWidget = g3mWidget;
        }
        
        void run(const G3MContext* context) {            
            if(!_mapGeoJSONSources.empty()){
                for (std::map<std::string, std::string>::iterator it=_mapGeoJSONSources.begin(); it!=_mapGeoJSONSources.end(); it++){
                    [_g3mWidget getG3MContext]->getDownloader()->requestBuffer(URL(it->first, false), 100000000L,                                           TimeInterval::fromDays(30),new GEOJSONDownloadListener(_marksRenderer, it->second), true);
                }            
            }
            if(!_panoSources.empty()){
                for (std::vector<std::string>::iterator it=_panoSources.begin(); it!=_panoSources.end(); it++) {
                    IStringBuilder *url = IStringBuilder::newStringBuilder();
                    url->addString(*it);
                    url->addString("/metadata.json");
                    [_g3mWidget getG3MContext]->getDownloader()->requestBuffer(URL(url->getString(), false), 100000000L,TimeInterval::fromDays(30), new PanoDownloadListener(_marksRenderer,_panoTouchListener), true);
                    delete url;
                }
            }
            if (_customInitializationTask!=NULL){
                _customInitializationTask->run(context);
            }
        }
        
        bool isDone(const G3MContext* context){
            return true;
        }

    };

    
//    [_g3mWidget initWidgetWithCameraConstraints: cameraConstraints
//                                       layerSet: layerSet
//                         incrementalTileQuality: incrementalTileQuality
//                                      renderers: renderers
//                                       userData: userData
//                             initializationTask: new G3MJSONBuilderInitializationTask(initializationTask, marksRenderer, _g3mWidget, panoTouchListener)
//                                periodicalTasks: periodicalTasks];
    
}

void G3MJSONBuilder_iOS::create(LayerSet* layerSet, GInitializationTask* initializationTask, MarkTouchListener* markTouchListener, MarkTouchListener* panoTouchListener){
    
    SceneParser::instance()->parse(layerSet, _jsonSource);
    
    class G3MJSONBuilderInitializationTask : public GInitializationTask {
    private:
        GInitializationTask* _customInitializationTask;
        MarksRenderer* _marksRenderer;
        std::map<std::string, std::string> _mapGeoJSONSources;
        std::vector<std::string> _panoSources;
        MarkTouchListener* _panoTouchListener;
        G3MWidget_iOS* _g3mWidget;
    public:
        G3MJSONBuilderInitializationTask(GInitializationTask* customInitializationTask, MarksRenderer* marksRenderer, G3MWidget_iOS* g3mWidget, MarkTouchListener* panoTouchListener){
            _customInitializationTask = customInitializationTask;
            _marksRenderer = marksRenderer;
            _mapGeoJSONSources = SceneParser::instance()->getMapGeoJSONSources();
            _panoSources = SceneParser::instance()->getPanoSources();
            _panoTouchListener = panoTouchListener;
            _g3mWidget = g3mWidget;
        }
        
        void run(const G3MContext* context) {            
            if(!_mapGeoJSONSources.empty()){
                for (std::map<std::string, std::string>::iterator it=_mapGeoJSONSources.begin(); it!=_mapGeoJSONSources.end(); it++){
                    [_g3mWidget getG3MContext]->getDownloader()->requestBuffer(URL(it->first, false), 100000000L,                                           TimeInterval::fromDays(30),new GEOJSONDownloadListener(_marksRenderer, it->second), true);
                }            
            }
            if(!_panoSources.empty()){
                for (std::vector<std::string>::iterator it=_panoSources.begin(); it!=_panoSources.end(); it++) {
                    IStringBuilder *url = IStringBuilder::newStringBuilder();
                    url->addString(*it);
                    url->addString("/metadata.json");
                    [_g3mWidget getG3MContext]->getDownloader()->requestBuffer(URL(url->getString(), false), 100000000L,TimeInterval::fromDays(30), new PanoDownloadListener(_marksRenderer,_panoTouchListener), true);
                    delete url;
                }
            }
            if (_customInitializationTask!=NULL){
                _customInitializationTask->run(context);
            }
        }
        
        bool isDone(const G3MContext* context){
            return true;
        }
        
    };
    
    G3MBuilder_iOS* builder = new G3MBuilder_iOS(_g3mWidget);
    builder->setLayerSet(layerSet);

    const bool readyWhenMarksReady = false;
    MarksRenderer* marksRenderer = new MarksRenderer(readyWhenMarksReady);
    if (markTouchListener != NULL) {
        marksRenderer->setMarkTouchListener(markTouchListener, true);
    }
    builder->setInitializationTask(new G3MJSONBuilderInitializationTask(initializationTask, marksRenderer, _g3mWidget, panoTouchListener));
    builder->addRenderer(marksRenderer);
    builder->initializeWidget();
    
}