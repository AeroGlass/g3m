

package org.glob3.mobile.specific;

import org.glob3.mobile.generated.IBufferDownloadListener;
import org.glob3.mobile.generated.IByteBuffer;
import org.glob3.mobile.generated.IImage;
import org.glob3.mobile.generated.IImageDownloadListener;
import org.glob3.mobile.generated.ILogger;
import org.glob3.mobile.generated.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;


public class ListenerEntry {

   final static String                   TAG = "Downloader_Android_ListenerEntry";

   private boolean                       _canceled;
   private final long                    _requestId;
   private final IBufferDownloadListener _bufferListener;
   private final IImageDownloadListener  _imageListener;


   public ListenerEntry(final IBufferDownloadListener bufferListener,
                        final IImageDownloadListener imageListener,
                        final long requestId) {
      _bufferListener = bufferListener;
      _imageListener = imageListener;
      _requestId = requestId;
      _canceled = false;
   }


   public long getRequestId() {
      return _requestId;
   }


   public IBufferDownloadListener getBufferListener() {
      return _bufferListener;
   }


   public IImageDownloadListener getImageListener() {
      return _imageListener;
   }


   public void cancel() {
      if (_canceled) {
         if (ILogger.instance() != null) {
            ILogger.instance().logError(TAG + ": Listener for requestId=" + _requestId + " already canceled");
         }
         else {
            Log.e(TAG, "Listener for requestId=" + _requestId + " already canceled");
         }
      }
      _canceled = true;
   }


   public boolean isCanceled() {
      return _canceled;
   }


   void onCancel(final URL url) {
      if (_bufferListener != null) {
         _bufferListener.onCancel(url);
      }
      if (_imageListener != null) {
         _imageListener.onCancel(url);
      }
   }


   void onError(final URL url) {
      if (_bufferListener != null) {
         _bufferListener.onError(url);
      }
      if (_imageListener != null) {
         _imageListener.onError(url);
      }
   }


   void onDownload(final URL url,
                   final byte[] data) {
      if (_bufferListener != null) {
         final IByteBuffer buffer = new ByteBuffer_Android(data);
         _bufferListener.onDownload(url, buffer);
      }
      if (_imageListener != null) {
         final Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
         if (bitmap == null) {
            ILogger.instance().logError("Downloader_Android", "Can't create image from data");
            return;
         }
         final IImage image = new Image_Android(bitmap, data);
         _imageListener.onDownload(url, image);
      }
   }


   void onCanceledDownload(final URL url,
                           final byte[] data) {
      if (_bufferListener != null) {
         final IByteBuffer buffer = new ByteBuffer_Android(data);
         _bufferListener.onCanceledDownload(url, buffer);
      }
      if (_imageListener != null) {
         final Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
         if (bitmap == null) {
            ILogger.instance().logError("Downloader_Android", "Can't create image from data");
            return;
         }
         final IImage image = new Image_Android(bitmap, data);
         _imageListener.onCanceledDownload(url, image);
      }
   }

}
