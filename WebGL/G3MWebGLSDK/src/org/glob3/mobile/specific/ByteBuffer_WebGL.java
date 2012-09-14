

package org.glob3.mobile.specific;

import java.nio.ByteBuffer;

import org.glob3.mobile.generated.IByteBuffer;


public class ByteBuffer_WebGL
         extends
            IByteBuffer {

   private final ByteBuffer _buffer;
   private int              _timestamp = 0;


   ByteBuffer_WebGL(final byte[] data) {
      _buffer = ByteBuffer.wrap(data);

      //_buffer = ByteBuffer.allocateDirect(data.length);
      //_buffer.put(data);
      //_buffer.rewind();
   }


   public ByteBuffer_WebGL(final int size) {
      //_buffer = ByteBuffer.allocate(size);
      _buffer = ByteBuffer.wrap(new byte[size]);
   }


   @Override
   public int size() {
      return _buffer.capacity();
   }


   @Override
   public int timestamp() {
      return _timestamp;
   }


   @Override
   public byte get(final int i) {
      return _buffer.get(i);
   }


   @Override
   public void put(final int i,
                   final byte value) {
      if (_buffer.get(i) != value) {
         _buffer.put(i, value);
         _timestamp++;
      }
   }


   public ByteBuffer getBuffer() {
      return _buffer;
   }


   @Override
   public String description() {
      return "ByteBuffer_iOS (size=" + _buffer.capacity() + ")";
   }

}
