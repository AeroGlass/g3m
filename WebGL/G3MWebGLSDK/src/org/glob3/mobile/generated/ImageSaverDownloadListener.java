package org.glob3.mobile.generated; 
public class ImageSaverDownloadListener implements IImageDownloadListener
{
  private CachedDownloader _downloader;
  private IImageDownloadListener _listener;
  private final boolean _deleteListener;

  public ImageSaverDownloadListener(CachedDownloader downloader, IImageDownloadListener listener, boolean deleteListener)
  {
	  _downloader = downloader;
	  _listener = listener;
	  _deleteListener = deleteListener;

  }

  public final void deleteListener()
  {
	if (_deleteListener)
	{
	  _listener = null;
	}
  }

  public final void saveImage(URL url, IImage image)
  {
	if (image != null)
	{
	  if (IStorage.instance().isAvailable())
	  {
		//if (!_cacheStorage->containsImage(url)) {
		_downloader.countSave();

		IStorage.instance().saveImage(url, image, _downloader.saveInBackground());
		//}
	  }
	  else
	  {
		ILogger.instance().logWarning("The cacheStorage is not available, skipping image save.");
	  }
	}
  }

  public final void onDownload(URL url, IImage image)
  {
	saveImage(url, image);

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
	saveImage(url, image);

	_listener.onCanceledDownload(url, image);

	// no deleteListener() call, onCanceledDownload() is always called before onCancel().
  }

  public final void onCancel(URL url)
  {
	_listener.onCancel(url);

	deleteListener();
  }

}