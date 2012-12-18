/**
 * 
 */


package org.glob3.mobile.demo;

import org.glob3.mobile.generated.LayerSet;
import org.glob3.mobile.generated.Sector;
import org.glob3.mobile.generated.URL;
import org.glob3.mobile.generated.WMSLayer;
import org.glob3.mobile.generated.WMSServerVersion;
import org.glob3.mobile.specific.G3MBuilder_Android;
import org.glob3.mobile.specific.G3MWidget_Android;

import android.app.Activity;
import android.os.Bundle;
import android.widget.FrameLayout;


/**
 * @author mdelacalle
 * 
 */
public class G3MDoubleGlob3Activity
         extends
            Activity {

   private G3MWidget_Android _widgetAndroidUp   = null;
   private G3MWidget_Android _widgetAndroidDown = null;


   @Override
   public void onCreate(final Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);

      //Don't forget to assign the layout and inflate it
      setContentView(R.layout.double_glob3_activity);

      final G3MBuilder_Android g3mBuilderUp = new G3MBuilder_Android(this);

      _widgetAndroidUp = g3mBuilderUp.createWidget();
      final FrameLayout layoutUp = (FrameLayout) findViewById(R.id.glob3up);
      layoutUp.addView(_widgetAndroidUp);


      final G3MBuilder_Android g3mBuilderDown = new G3MBuilder_Android(this);
      final LayerSet layerSet = new LayerSet();
      final WMSLayer osm = new WMSLayer( //
               "osm_auto:all", //
               new URL("http://129.206.228.72/cached/osm", false), //
               WMSServerVersion.WMS_1_1_0, //
               Sector.fromDegrees(-85.05, -180.0, 85.05, 180.0), //
               "image/jpeg", //
               "EPSG:4326", //
               "", //
               false, //
               null);
      layerSet.addLayer(osm);

      g3mBuilderDown.setLayerSet(layerSet);
      _widgetAndroidDown = g3mBuilderDown.createWidget();

      final FrameLayout layoutDown = (FrameLayout) findViewById(R.id.glob3down);
      layoutDown.addView(_widgetAndroidDown);


      //      final G3MBuilder glob3Builder = new G3MBuilder();

      //      _widgetAndroidUp = glob3Builder.getSimpleBingGlob3(getApplicationContext());
      //      final FrameLayout layoutUp = (FrameLayout) findViewById(R.id.glob3up);
      //      layoutUp.addView(_widgetAndroidUp);
      //
      //      _widgetAndroidDown = glob3Builder.getSimpleOSMGlob3(getApplicationContext());
      //      final FrameLayout layoutDown = (FrameLayout) findViewById(R.id.glob3down);
      //      layoutDown.addView(_widgetAndroidDown);


   }


   @Override
   protected void onResume() {
      super.onResume();
      _widgetAndroidUp.onResume();
      _widgetAndroidDown.onResume();
   }


   @Override
   protected void onPause() {
      if (isFinishing()) {
         _widgetAndroidUp.onDestroy();
         _widgetAndroidDown.onDestroy();
      }
      else {
         _widgetAndroidUp.onPause();
         _widgetAndroidDown.onPause();
      }
      super.onPause();
   }

}
