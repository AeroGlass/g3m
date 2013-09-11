//
//  PlanetTileTessellator.hpp
//  G3MiOSSDK
//
//  Created by Agustin Trujillo Pino on 12/07/12.
//  Copyright (c) 2012 IGO Software SL. All rights reserved.
//

#ifndef G3MiOSSDK_PlanetTileTessellator_hpp
#define G3MiOSSDK_PlanetTileTessellator_hpp

#include "TileTessellator.hpp"
#include <map>
#include "Sector.hpp"

class IShortBuffer;
class Sector;
class FloatBufferBuilderFromGeodetic;
class ShortBufferBuilder;
#include "FloatBufferBuilderFromCartesian2D.hpp"


class PlanetTileTessellatorData {
public:
  FloatBufferBuilderFromCartesian2D* _textCoords;
  PlanetTileTessellatorData(FloatBufferBuilderFromCartesian2D* textCoords):
  _textCoords(textCoords){}

  ~PlanetTileTessellatorData(){
    delete _textCoords;
  }
};


class PlanetTileTessellator : public TileTessellator {
private:
  const bool _skirted;
  const Sector _renderedSector;

  Vector2I calculateResolution(const Vector2I& resolution,
                               const Tile* tile,
                               const Sector& renderedSector) const;

  Geodetic3D getGeodeticOnPlanetSurface(const IMathUtils* mu,
                                        const Planet* planet,
                                        const ElevationData* elevationData,
                                        float verticalExaggeration,
                                        const Geodetic2D& g) const;

  bool needsEastSkirt(const Sector& tileSector) const{
    return _renderedSector.upperLongitude().greaterThan(tileSector.upperLongitude());
  }

  bool needsNorthSkirt(const Sector& tileSector) const{
    return _renderedSector.upperLatitude().greaterThan(tileSector.upperLatitude());
  }

  bool needsWestSkirt(const Sector& tileSector) const{
    return _renderedSector.lowerLongitude().lowerThan(tileSector.lowerLongitude());
  }

  bool needsSouthSkirt(const Sector& tileSector) const{
    return _renderedSector.lowerLatitude().lowerThan(tileSector.lowerLatitude());
  }

  Sector getRenderedSectorForTile(const Tile* tile) const;

//  double getHeight(const Geodetic2D& g, const ElevationData* elevationData, double verticalExaggeration) const;

  double createSurface(const Sector& tileSector,
                       const Sector& meshSector,
                       const Vector2I& meshResolution,
                       const ElevationData* elevationData,
                       float verticalExaggeration,
                       bool mercator,
                       FloatBufferBuilderFromGeodetic& vertices,
                       ShortBufferBuilder& indices,
                       FloatBufferBuilderFromCartesian2D& textCoords) const;

  void createEastSkirt(const Planet* planet,
                       const Sector& tileSector,
                       const Sector& meshSector,
                       const Vector2I& meshResolution,
                       double skirtHeight,
                       FloatBufferBuilderFromGeodetic& vertices,
                       ShortBufferBuilder& indices,
                       FloatBufferBuilderFromCartesian2D& textCoords) const;

  void createNorthSkirt(const Planet* planet,
                        const Sector& tileSector,
                        const Sector& meshSector,
                        const Vector2I& meshResolution,
                        double skirtHeight,
                        FloatBufferBuilderFromGeodetic& vertices,
                        ShortBufferBuilder& indices,
                        FloatBufferBuilderFromCartesian2D& textCoords) const;

  void createWestSkirt(const Planet* planet,
                       const Sector& tileSector,
                       const Sector& meshSector,
                       const Vector2I& meshResolution,
                       double skirtHeight,
                       FloatBufferBuilderFromGeodetic& vertices,
                       ShortBufferBuilder& indices,
                       FloatBufferBuilderFromCartesian2D& textCoords) const;

  void createSouthSkirt(const Planet* planet,
                        const Sector& tileSector,
                        const Sector& meshSector,
                        const Vector2I& meshResolution,
                        double skirtHeight,
                        FloatBufferBuilderFromGeodetic& vertices,
                        ShortBufferBuilder& indices,
                        FloatBufferBuilderFromCartesian2D& textCoords) const;

public:

  PlanetTileTessellator(const bool skirted, const Sector& sector);

  ~PlanetTileTessellator();

  Vector2I getTileMeshResolution(const Planet* planet,
                                 const Vector2I& resolution,
                                 const Tile* tile,
                                 bool debug) const;


  Mesh* createTileMesh(const Planet* planet,
                       const Vector2I& resolution,
                       Tile* tile,
                       const ElevationData* elevationData,
                       float verticalExaggeration,
                       bool mercator,
                       bool debug) const;

  Mesh* createTileDebugMesh(const Planet* planet,
                            const Vector2I& resolution,
                            const Tile* tile) const;

  bool isReady(const G3MRenderContext *rc) const {
    return true;
  }

  IFloatBuffer* createTextCoords(const Vector2I& resolution,
                                 const Tile* tile,
                                 bool mercator) const;

  const Vector2D getTextCoord(const Tile* tile,
                              const Angle& latitude,
                              const Angle& longitude,
                              bool mercator) const;
  
};

#endif