

package org.glob3.mobile.specific;

import org.glob3.mobile.generated.BasicShadersGL2;
import org.glob3.mobile.generated.IDownloader;
import org.glob3.mobile.generated.IG3MBuilder;
import org.glob3.mobile.generated.IStorage;
import org.glob3.mobile.generated.IThreadUtils;


public class G3MBuilder_WebGL
   extends
      IG3MBuilder {

   private final G3MWidget_WebGL _nativeWidget;


   public G3MBuilder_WebGL() {
      this(new G3MWidget_WebGL());
   }


   public G3MBuilder_WebGL(final G3MWidget_WebGL widget) {
      _nativeWidget = widget;
   }


   public G3MWidget_WebGL createWidget() {
      if (_nativeWidget.isWebGLSupported()) {
         final BasicShadersGL2 shaders = new BasicShadersGL2();
         for (int i = 0; i < shaders.size(); i++) {
            addGPUProgramSources(shaders.get(i));
         }

         setGL(_nativeWidget.getGL());

         _nativeWidget.setG3MWidget(create());
         _nativeWidget.startWidget();
      }

      return _nativeWidget;
   }


   @Override
   protected IDownloader createDefaultDownloader() {
      final int maxConcurrentOperationCount = 8;
      final int delayMillis = 10;
      return new Downloader_WebGL(maxConcurrentOperationCount, delayMillis, "", true);
   }


   @Override
   protected IThreadUtils createDefaultThreadUtils() {
      final int delayMillis = 10;
      return new ThreadUtils_WebGL(delayMillis);
   }


   @Override
   protected IStorage createDefaultStorage() {
      // TODO To be implemented when Storage_WebGL is implemented.
      return null;
   }

}
