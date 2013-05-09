package org.glob3.mobile.generated; 
//#include "G3MError.hpp"
//#include "G3MError.hpp"

//#define TEXTURES_DOWNLOAD_PRIORITY 1000000


public class SGLayerNode_ImageDownloadListener extends IImageDownloadListener
{
  private SGLayerNode _layerNode;

  public SGLayerNode_ImageDownloadListener(SGLayerNode layerNode)
  {
     _layerNode = layerNode;

  }

  public final void onDownload(URL url, IImage image, boolean expired)
  {
    _layerNode.onImageDownload(image);
  }

  public final void onError(URL url)
  {
    ILogger.instance().logWarning("Can't download texture \"%s\"", url.getPath());
  }

  public final void onCancel(URL url)
  {

  }

  public final void onCanceledDownload(URL url, IImage image, boolean expired)
  {

  }
}