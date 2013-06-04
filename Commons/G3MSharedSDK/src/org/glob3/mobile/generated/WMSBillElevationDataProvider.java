package org.glob3.mobile.generated; 
//
//  WMSBillElevationDataProvider.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 2/17/13.
//
//

//
//  WMSBillElevationDataProvider.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 2/17/13.
//
//




//class IDownloader;

public class WMSBillElevationDataProvider extends ElevationDataProvider
{
  private IDownloader _downloader;
  private URL _url = new URL();
  private Sector _sector ;

  public WMSBillElevationDataProvider(URL url, Sector sector)
  {
     _url = new URL(url);
     _sector = new Sector(sector);
     _downloader = null;

  }

  public final boolean isReadyToRender(G3MRenderContext rc)
  {
    return true;
  }

  public final void initialize(G3MContext context)
  {
    _downloader = context.getDownloader();
  }

  public final long requestElevationData(Sector sector, Vector2I extent, IElevationDataListener listener, boolean autodeleteListener)
  {
    if (_downloader == null)
    {
      ILogger.instance().logError("WMSBillElevationDataProvider was not initialized.");
      return -1;
    }
  
    IStringBuilder isb = IStringBuilder.newStringBuilder();
  
    /*
     // http://data.worldwind.arc.nasa.gov/elev?REQUEST=GetMap&SERVICE=WMS&VERSION=1.3.0&LAYERS=srtm30&STYLES=&FORMAT=image/bil&CRS=EPSG:4326&BBOX=-180.0,-90.0,180.0,90.0&WIDTH=10&HEIGHT=10
  
  
     isb->addString("http://data.worldwind.arc.nasa.gov/elev?");
     isb->addString("REQUEST=GetMap");
     isb->addString("&SERVICE=WMS");
     isb->addString("&VERSION=1.3.0");
     isb->addString("&LAYERS=srtm30");
     isb->addString("&STYLES=");
     isb->addString("&FORMAT=image/bil");
     isb->addString("&CRS=EPSG:4326");
     */
  
    isb.addString(_url.getPath());
  
    isb.addString("&BBOX=");
    isb.addDouble(sector.lower().latitude()._degrees);
    isb.addString(",");
    isb.addDouble(sector.lower().longitude()._degrees);
    isb.addString(",");
    isb.addDouble(sector.upper().latitude()._degrees);
    isb.addString(",");
    isb.addDouble(sector.upper().longitude()._degrees);
  
    isb.addString("&WIDTH=");
    isb.addInt(extent._x);
    isb.addString("&HEIGHT=");
    isb.addInt(extent._y);
  
    final String path = isb.getString();
    if (isb != null)
       isb.dispose();
  
  
    return _downloader.requestBuffer(new URL(path, false), 2000000000, TimeInterval.fromDays(30), true, new WMSBillElevationDataProvider_BufferDownloadListener(sector, extent, listener, autodeleteListener), true);
  }

  public final void cancelRequest(long requestId)
  {
    _downloader.cancelRequest(requestId);
  }

  public final java.util.ArrayList<Sector> getSectors()
  {
    final java.util.ArrayList<Sector> sectors = new java.util.ArrayList<Sector>();
    sectors.add(_sector);
    return sectors;
  }

  public final Vector2I getMinResolution()
  {
    int WORKING_JM;
    return Vector2I.zero();
  }

//  ElevationData* createSubviewOfElevationData(ElevationData* elevationData,
//                                              const Sector& sector,
//                                              const Vector2I& extent) const;

}