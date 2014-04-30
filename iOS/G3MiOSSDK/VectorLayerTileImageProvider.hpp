//
//  VectorLayerTileImageProvider.hpp
//  G3MiOSSDK
//
//  Created by fpulido on 30/04/14.
//
//

#ifndef __G3MiOSSDK__VectorLayerTileImageProvider__
#define __G3MiOSSDK__VectorLayerTileImageProvider__

#include <iostream>

#include "TileImageProvider.hpp"

#include <map>
#include <string>

class TiledVectorLayer;
class IDownloader;


class VectorLayerTileImageProvider : public TileImageProvider {
private:
    const TiledVectorLayer* _layer;
    IDownloader*       _downloader;
    
    std::map<const std::string, long long> _requestsIdsPerTile;
    
protected:
    ~VectorLayerTileImageProvider();
    
public:
    
    VectorLayerTileImageProvider(const TiledVectorLayer* layer,
                                 IDownloader* downloader) :
    _layer(layer),
    _downloader(downloader)
    {
    }
    
    const TileImageContribution* contribution(const Tile* tile);
    
    void create(const Tile* tile,
                const TileImageContribution* contribution,
                const Vector2I& resolution,
                long long tileDownloadPriority,
                bool logDownloadActivity,
                TileImageListener* listener,
                bool deleteListener);
    
    void cancel(const Tile* tile);
    
    
    void requestFinish(const std::string& tileId);
    
};


#endif /* defined(__G3MiOSSDK__VectorLayerTileImageProvider__) */
