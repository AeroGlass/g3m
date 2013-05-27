package org.glob3.mobile.generated; 
//
//  TilesRenderParameters.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 08/08/12.
//
//

//
//  TilesRenderParameters.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 08/08/12.
//
//


///#include "Sector.hpp"
///#include "Vector2I.hpp"

public class TilesRenderParameters
{
  public final boolean _renderDebug;
  public final boolean _useTilesSplitBudget;
  public final boolean _forceFirstLevelTilesRenderOnStart;
  public final boolean _incrementalTileQuality;
  public final boolean _renderIncompletePlanet;
  public final URL _incompletePlanetTexureURL = new URL();

  public TilesRenderParameters(boolean renderDebug, boolean useTilesSplitBudget, boolean forceFirstLevelTilesRenderOnStart, boolean incrementalTileQuality, boolean renderIncompletePlanet, URL incompletePlanetTexureURL)
  {
     _renderDebug = renderDebug;
     _useTilesSplitBudget = useTilesSplitBudget;
     _forceFirstLevelTilesRenderOnStart = forceFirstLevelTilesRenderOnStart;
     _incrementalTileQuality = incrementalTileQuality;
     _renderIncompletePlanet = renderIncompletePlanet;
     _incompletePlanetTexureURL = new URL(incompletePlanetTexureURL);

  }

  public void dispose()
  {
  }

}