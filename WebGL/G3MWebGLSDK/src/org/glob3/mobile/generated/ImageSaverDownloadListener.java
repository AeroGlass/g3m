package org.glob3.mobile.generated; 
public class ImageSaverDownloadListener implements IImageDownloadListener
{
  private CachedDownloader _downloader;
  private IImageDownloadListener _listener;
  private final boolean _deleteListener;
  private IStorage _cacheStorage;

  public ImageSaverDownloadListener(CachedDownloader downloader, IStorage cacheStorage, IImageDownloadListener listener, boolean deleteListener)
  {
	  _downloader = downloader;
	  _cacheStorage = cacheStorage;
	  _listener = listener;
	  _deleteListener = deleteListener;

  }

  public final void deleteListener()
  {
	if (_deleteListener && (_listener != null))
	{
	  _listener = null;
	}
  }

  public final void saveResponse(URL url, IImage image)
  {
	if (!_cacheStorage.containsImage(url))
	{
	  _downloader.countSave();

	  _cacheStorage.saveImage(url, image);
	}
  }

  public final void onDownload(URL url, IImage image)
  {
	saveResponse(url, image);

	_listener.onDownload(url, image);

	deleteListener();
  }

  public final void onError(URL url)
  {
	_listener.onError(url);

	deleteListener();
  }

  public final void onCanceledDownload(URL url, IImage image)
  {
	saveResponse(url, image);

	_listener.onCanceledDownload(url, image);

	// no deleteListener() call, onCanceledDownload() is always called before onCancel().
  }

  public final void onCancel(URL url)
  {
	_listener.onCancel(url);

	deleteListener();
  }

}