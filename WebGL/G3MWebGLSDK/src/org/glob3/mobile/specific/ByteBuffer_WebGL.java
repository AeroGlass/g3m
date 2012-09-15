

package org.glob3.mobile.specific;

import org.glob3.mobile.generated.IByteBuffer;

import com.google.gwt.core.client.JavaScriptObject;


public class ByteBuffer_WebGL
         extends
            IByteBuffer {

   private final JavaScriptObject _buffer;
   private final int              _timestamp = 0;


   public ByteBuffer_WebGL(final JavaScriptObject data) {
      _buffer = jsCreateBuffer(data);
   }


   public ByteBuffer_WebGL(final int size) {
      _buffer = jsCreateBuffer(size);
   }


   ByteBuffer_WebGL(final byte[] data) {
      //      TODO needed??
      throw new RuntimeException("ByteBuffer_WebGL(final byte[] data) IS NOT IMPLEMENTED");
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
   public byte get(final int i) {
      return jsGet(i);
   }


   @Override
   public void put(final int i,
                   final byte value) {
      jsPut(i, value);
   }


   @Override
   public String description() {
      return "ByteBuffer_WebGL (size=" + size() + ")";
   }


   public JavaScriptObject getBuffer() {
      return _buffer;
   }


   private native JavaScriptObject jsCreateBuffer(final JavaScriptObject data) /*-{
		return new Uint8Array(data);
   }-*/;


   private native JavaScriptObject jsCreateBuffer(final int size) /*-{
		return new Uint8Array(size);
   }-*/;


   private native int jsSize() /*-{
		return this.@org.glob3.mobile.specific.ByteBuffer_WebGL::_buffer.length;
   }-*/;


   private native byte jsGet(int i) /*-{
		this.@org.glob3.mobile.specific.ByteBuffer_WebGL::_buffer.get(i);
   }-*/;


   private native void jsPut(int i,
                             byte value) /*-{
		var thisInstance = this;
		if (thisInstance.@org.glob3.mobile.specific.ByteBuffer_WebGL::_buffer
				.get(i) != value) {
			thisInstance.@org.glob3.mobile.specific.ByteBuffer_WebGL::_buffer
					.set(i, value);
			thisInstance.@org.glob3.mobile.specific.ByteBuffer_WebGL::_timestamp++;
		}
   }-*/;

}
