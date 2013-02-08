package org.glob3.mobile.generated; 
//
//  Petition.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 20/08/12.
//
//

//
//  Petition.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 20/08/12.
//
//





//class Tile;
//class Rectangle;
//class Sector;
//class IFactory;
//class MutableVector2D;

public class Petition
{
  private final Sector _sector;
  private IImage _image;

  final private URL _url; //Conversor creates class "Url"

  private final long _timeToCacheInMS;

//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//  Petition(Petition that);

//C++ TO JAVA CONVERTER TODO TASK: The implementation of the following method could not be found:
//  void operator =(Petition that);


  public Petition(Sector sector, URL url, TimeInterval timeToCache)
  {
     _sector = new Sector(sector);
     _url = url;
     _timeToCacheInMS = timeToCache.milliseconds();
     _image = null;
  }

  public void dispose()
  {
    if (_sector != null)
       _sector.dispose();
    releaseImage();
  }

  public final void releaseImage()
  {
    IFactory.instance().deleteImage(_image);
    _image = null;
  }

  public final boolean hasImage()
  {
    return (_image != null);
  }

  public final URL getURL()
  {
    return _url;
  }

  public final Sector getSector()
  {
    return _sector;
  }

  public final void setImage(IImage image)
  {
    releaseImage();
    _image = image;
  }

  public final IImage getImage()
  {
    return _image;
  }

  public final TimeInterval getTimeToCache()
  {
    return TimeInterval.fromMilliseconds(_timeToCacheInMS);
  }

  public final String description()
  {
    IStringBuilder isb = IStringBuilder.newStringBuilder();
    isb.addString("Petition(url=");
    isb.addString(_url.description());
    isb.addString(", sector=");
    isb.addString(_sector.description());
    isb.addString(", buffer=");
    if (_image == null)
    {
      isb.addString("NULL");
    }
    else
    {
      isb.addString(_image.description());
    }
  
    final String s = isb.getString();
    if (isb != null)
       isb.dispose();
    return s;
  }

}