package org.glob3.mobile.generated; 
//
//  MapQuestLayer.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 3/8/13.
//
//

//
//  MapQuestLayer.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 3/8/13.
//
//



/*
 http: //developer.mapquest.com/web/products/open/map
 */

public class MapQuestLayer extends MercatorTiledLayer
{

  private static java.util.ArrayList<String> getSubdomains()
  {
    java.util.ArrayList<String> result = new java.util.ArrayList<String>();
    result.add("otile1.");
    result.add("otile2.");
    result.add("otile3.");
    result.add("otile4.");
    return result;
  }

  private MapQuestLayer(String name, String domain, java.util.ArrayList<String> subdomains, int initialLevel, int maxLevel, TimeInterval timeToCache, LayerCondition condition)
  {
     super(name, "http://", domain, subdomains, "jpg", timeToCache, Sector.fullSphere(), initialLevel, maxLevel, condition);

  }



  public static MapQuestLayer newOSM(TimeInterval timeToCache, int initialLevel)
  {
     return newOSM(timeToCache, initialLevel, null);
  }
  public static MapQuestLayer newOSM(TimeInterval timeToCache)
  {
     return newOSM(timeToCache, 3, null);
  }
  public static MapQuestLayer newOSM(TimeInterval timeToCache, int initialLevel, LayerCondition condition)
  {
    return new MapQuestLayer("MapQuest-OSM", "mqcdn.com/tiles/1.0.0/map", getSubdomains(), initialLevel, 19, timeToCache, condition);
  }


  public static MapQuestLayer newOpenAerial(TimeInterval timeToCache, int initialLevel)
  {
     return newOpenAerial(timeToCache, initialLevel, null);
  }
  public static MapQuestLayer newOpenAerial(TimeInterval timeToCache)
  {
     return newOpenAerial(timeToCache, 3, null);
  }
  public static MapQuestLayer newOpenAerial(TimeInterval timeToCache, int initialLevel, LayerCondition condition)
  {
    return new MapQuestLayer("MapQuest-OpenAerial", "mqcdn.com/tiles/1.0.0/sat", getSubdomains(), initialLevel, 11, timeToCache, condition);
  }

}