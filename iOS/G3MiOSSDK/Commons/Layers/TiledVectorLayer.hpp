//
//  TiledVectorLayer.hpp
//  G3MiOSSDK
//
//  Created by fpulido on 30/04/14.
//
//

#ifndef __G3MiOSSDK__TiledVectorLayer__
#define __G3MiOSSDK__TiledVectorLayer__

#include <iostream>
#include "VectorLayer.hpp"

class TiledVectorLayer : public VectorLayer {

protected:
    
    virtual const URL createURL(const Tile* tile) const = 0;
    
};


#endif /* defined(__G3MiOSSDK__TiledVectorLayer__) */
