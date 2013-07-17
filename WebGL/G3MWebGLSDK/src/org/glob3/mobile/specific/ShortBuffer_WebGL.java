

package org.glob3.mobile.specific;

import org.glob3.mobile.generated.IShortBuffer;

import com.google.gwt.core.client.JavaScriptObject;


public class ShortBuffer_WebGL
         extends
            IShortBuffer {

   private final JavaScriptObject _buffer;
   private int                    _timestamp   = 0;

   private JavaScriptObject       _webGLBuffer = null;
   private JavaScriptObject       _gl          = null;


   public JavaScriptObject getWebGLBuffer(final JavaScriptObject gl) {
      if (_webGLBuffer == null) {
         _gl = gl;
         _webGLBuffer = jsCreateWebGLBuffer();
      }
      return _webGLBuffer;
   }


   @Override
   public void dispose() {
      if (_webGLBuffer != null) {
         jsDeleteWebGLBuffer();
         _webGLBuffer = null;
         _gl = null;
      }
      super.dispose();
   }


   private native JavaScriptObject jsCreateWebGLBuffer() /*-{
		return this.@org.glob3.mobile.specific.ShortBuffer_WebGL::_gl
				.createBuffer();
   }-*/;


   private native JavaScriptObject jsDeleteWebGLBuffer() /*-{
		return this.@org.glob3.mobile.specific.ShortBuffer_WebGL::_gl
				.deleteBuffer(this.@org.glob3.mobile.specific.ShortBuffer_WebGL::_webGLBuffer);
   }-*/;


   public ShortBuffer_WebGL(final JavaScriptObject data) {
      _buffer = jsCreateBuffer(data);
   }


   public ShortBuffer_WebGL(final int size) {
      _buffer = jsCreateBuffer(size);
   }


   public ShortBuffer_WebGL(final short[] array) {
      _buffer = jsCreateBuffer(array.length);
      for (int i = 0; i < array.length; i++) {
         rawPut(i, array[i]);
      }
   }


   @Override
   public int size() {
      return jsSize();
   }


   @Override
   public int timestamp() {
      return _timestamp;
   }


   @Override
   public short get(final int i) {
      return jsGet(i);
   }


   @Override
   public void put(final int i,
                   final short value) {
      jsPut(i, value);
   }


   private native void jsPut(int i,
                             short value) /*-{

		if (value < 0 || value > 65535) {
			alert("EXCEDING SHORT RANGE IN UINT16 JAVASCRIPT BUFFER");
		}

		var thisInstance = this;
		if (thisInstance.@org.glob3.mobile.specific.ShortBuffer_WebGL::_buffer[i] != value) {
			thisInstance.@org.glob3.mobile.specific.ShortBuffer_WebGL::_buffer[i] = value;
			thisInstance.@org.glob3.mobile.specific.ShortBuffer_WebGL::incTimestamp()();
		}
   }-*/;


   @Override
   public native void rawPut(final int i,
                             final short value) /*-{

		if (value < 0 || value > 65535) {
			alert("EXCEDING SHORT RANGE IN UINT16 JAVASCRIPT BUFFER");
		}

		this.@org.glob3.mobile.specific.ShortBuffer_WebGL::_buffer[i] = value;
   }-*/;


   public JavaScriptObject getBuffer() {
      return _buffer;
   }


   private void incTimestamp() {
      _timestamp++;
   }


   private native JavaScriptObject jsCreateBuffer(final JavaScriptObject data) /*-{
		return new Uint16Array(data);
   }-*/;


   private native JavaScriptObject jsCreateBuffer(final int size) /*-{
		return new Uint16Array(size);
   }-*/;


   private native int jsSize() /*-{
		return this.@org.glob3.mobile.specific.ShortBuffer_WebGL::_buffer.length;
   }-*/;


   private native short jsGet(int i) /*-{
		return this.@org.glob3.mobile.specific.ShortBuffer_WebGL::_buffer[i];
   }-*/;


   @Override
   public String description() {
      return "ShortBuffer_WebGL(timestamp=" + _timestamp + ", buffer=" + _buffer + ")";
   }

}
