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



public class TilesRenderParameters
{
  public final Sector _topSector ;
  public final int _splitsByLatitude;
  public final int _splitsByLongitude;
  public final int _topLevel;
  public final int _maxLevel;
  public final int _tileTextureHeight;
  public final int _tileTextureWidth;
  public final int _tileResolution;
  public final boolean _renderDebug;

  public final boolean _useTilesSplitBudget;

  public final boolean _forceTopLevelTilesRenderOnStart;

  public TilesRenderParameters(Sector topSector, int splitsByLatitude, int splitsByLongitude, int topLevel, int maxLevel, int tileTextureHeight, int tileTextureWidth, int tileResolution, boolean renderDebug, boolean useTilesSplitBudget, boolean forceTopLevelTilesRenderOnStart)
  {
	  _topSector = new Sector(topSector);
	  _splitsByLatitude = splitsByLatitude;
	  _splitsByLongitude = splitsByLongitude;
	  _topLevel = topLevel;
	  _maxLevel = maxLevel;
	  _tileTextureHeight = tileTextureHeight;
	  _tileTextureWidth = tileTextureWidth;
	  _tileResolution = tileResolution;
	  _renderDebug = renderDebug;
	  _useTilesSplitBudget = useTilesSplitBudget;
	  _forceTopLevelTilesRenderOnStart = forceTopLevelTilesRenderOnStart;

  }

  public static TilesRenderParameters createDefault(boolean renderDebug, boolean useTilesSplitBudget, boolean forceTopLevelTilesRenderOnStart)
  {
	final int K = 1;
	final int splitsByLatitude = 2 * K;
	final int splitsByLongitude = 4 * K;
	final int topLevel = 0;
	final int maxLevel = 17;
	final int tileTextureHeight = 256;
	final int tileTextureWidth = 256;
	final int tRes = 10;

	return new TilesRenderParameters(Sector.fullSphere(), splitsByLatitude, splitsByLongitude, topLevel, maxLevel, tileTextureHeight, tileTextureWidth, tRes, renderDebug, useTilesSplitBudget, forceTopLevelTilesRenderOnStart);
  }


  public static TilesRenderParameters createSingleSector(boolean renderDebug, boolean useTilesSplitBudget, boolean forceTopLevelTilesRenderOnStart)
  {
	final int splitsByLatitude = 1;
	final int splitsByLongitude = 1;
	final int topLevel = 0;
	final int maxLevel = 2;
	final int tileTextureHeight = 256;
	final int tileTextureWidth = 256;
	final int tRes = 10;

	//    Sector sector = Sector::fullSphere();
	//    Sector sector = Sector(Geodetic2D(Angle::fromDegrees(-90), Angle::fromDegrees(-180)),
	//                           Geodetic2D(Angle::fromDegrees(90), Angle::fromDegrees(180)));
	Sector sector = new Sector(new Geodetic2D(Angle.fromDegrees(0), Angle.fromDegrees(0)), new Geodetic2D(Angle.fromDegrees(90), Angle.fromDegrees(90)));

	return new TilesRenderParameters(sector, splitsByLatitude, splitsByLongitude, topLevel, maxLevel, tileTextureHeight, tileTextureWidth, tRes, renderDebug, useTilesSplitBudget, forceTopLevelTilesRenderOnStart);
  }
}