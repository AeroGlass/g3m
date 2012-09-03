

package org.glob3.mobile.specific;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;

import org.glob3.mobile.generated.GLAlignment;
import org.glob3.mobile.generated.GLBlendFactor;
import org.glob3.mobile.generated.GLBufferType;
import org.glob3.mobile.generated.GLCullFace;
import org.glob3.mobile.generated.GLError;
import org.glob3.mobile.generated.GLFeature;
import org.glob3.mobile.generated.GLFormat;
import org.glob3.mobile.generated.GLPrimitive;
import org.glob3.mobile.generated.GLTextureId;
import org.glob3.mobile.generated.GLTextureParameter;
import org.glob3.mobile.generated.GLTextureParameterValue;
import org.glob3.mobile.generated.GLTextureType;
import org.glob3.mobile.generated.GLType;
import org.glob3.mobile.generated.GLVariable;
import org.glob3.mobile.generated.INativeGL;

import android.opengl.GLES20;


public class NativeGL2_Android
         extends
            INativeGL {

   final int getBitField(final GLBufferType b) {
      switch (b) {
         case ColorBuffer:
            return GLES20.GL_COLOR_BUFFER_BIT;
         case DepthBuffer:
            return GLES20.GL_DEPTH_BUFFER_BIT;
      }
      return 0;
   }


   int getEnum(final GLFeature f) {
      switch (f) {
         case PolygonOffsetFill:
            return GLES20.GL_POLYGON_OFFSET_FILL;
         case DepthTest:
            return GLES20.GL_DEPTH_TEST;
         case Blend:
            return GLES20.GL_BLEND;
         case CullFacing:
            return GLES20.GL_CULL_FACE;
      }
      return 0;
   }


   final int getEnum(final GLCullFace f) {
      switch (f) {
         case Front:
            return GLES20.GL_FRONT;
         case FrontAndBack:
            return GLES20.GL_FRONT_AND_BACK;
         case Back:
            return GLES20.GL_BACK;
      }
      return 0;
   }


   final int getEnum(final GLType t) {
      switch (t) {
         case Float:
            return GLES20.GL_FLOAT;
         case UnsignedByte:
            return GLES20.GL_UNSIGNED_BYTE;
         case UnsignedInt:
            return GLES20.GL_UNSIGNED_INT;
         case Int:
            return GLES20.GL_INT;
      }
      return 0;
   }


   final int getEnum(final GLPrimitive p) {
      switch (p) {
         case TriangleStrip:
            return GLES20.GL_TRIANGLE_STRIP;
         case Lines:
            return GLES20.GL_LINES;
         case LineLoop:
            return GLES20.GL_LINE_LOOP;
         case Points:
            return GLES20.GL_POINTS;
      }
      return 0;
   }


   final GLError getError(final int e) {
      switch (e) {
         case GLES20.GL_NO_ERROR:
            return GLError.NoError;
         case GLES20.GL_INVALID_ENUM:
            return GLError.InvalidEnum;
         case GLES20.GL_INVALID_VALUE:
            return GLError.InvalidValue;
         case GLES20.GL_INVALID_OPERATION:
            return GLError.InvalidOperation;
         case GLES20.GL_OUT_OF_MEMORY:
            return GLError.OutOfMemory;
      }
      return GLError.UnknownError;
   }


   final int getEnum(final GLBlendFactor b) {
      switch (b) {
         case SrcAlpha:
            return GLES20.GL_SRC_ALPHA;
         case OneMinusSrcAlpha:
            return GLES20.GL_ONE_MINUS_SRC_ALPHA;
      }
      return 0;
   }


   final int getEnum(final GLAlignment a) {
      switch (a) {
         case Unpack:
            return GLES20.GL_UNPACK_ALIGNMENT;
         case Pack:
            return GLES20.GL_PACK_ALIGNMENT;
      }
      return 0;
   }


   final int getEnum(final GLTextureType t) {
      switch (t) {
         case Texture2D:
            return GLES20.GL_TEXTURE_2D;
      }
      return 0;
   }


   final int getEnum(final GLTextureParameter t) {
      switch (t) {
         case MinFilter:
            return GLES20.GL_TEXTURE_MIN_FILTER;
         case MagFilter:
            return GLES20.GL_TEXTURE_MAG_FILTER;
         case WrapS:
            return GLES20.GL_TEXTURE_WRAP_S;
         case WrapT:
            return GLES20.GL_TEXTURE_WRAP_T;
      }
      return 0;
   }


   final int getValue(final GLTextureParameterValue t) {
      switch (t) {
         case Linear:
            return GLES20.GL_LINEAR;
         case ClampToEdge:
            return GLES20.GL_CLAMP_TO_EDGE;
      }
      return 0;
   }


   final int getEnum(final GLFormat f) {
      switch (f) {
         case RGBA:
            return GLES20.GL_RGBA;
      }
      return 0;
   }


   final int getEnum(final GLVariable v) {
      switch (v) {
         case Viewport:
            return GLES20.GL_VIEWPORT;
      }
      return 0;
   }


   private FloatBuffer floatArrayToFloatBuffer(final float[] fv) {
      final ByteBuffer byteBuf = ByteBuffer.allocateDirect(fv.length * 4);
      byteBuf.order(ByteOrder.nativeOrder());
      final FloatBuffer fb = byteBuf.asFloatBuffer();
      fb.put(fv); // /TOO SLOW UNTIL VERSION GINGERBEAD (BECAUSE OF THIS,
      // USE HASHMAP)
      fb.position(0);
      return fb;
   }


   /////////////////////////////

   @Override
   public void useProgram(final int program) {
      GLES20.glUseProgram(program);
   }


   @Override
   public int getAttribLocation(final int program,
                                final String name) {
      return GLES20.glGetAttribLocation(program, name);
   }


   @Override
   public int getUniformLocation(final int program,
                                 final String name) {
      return GLES20.glGetUniformLocation(program, name);
   }


   @Override
   public void uniform2f(final int loc,
                         final float x,
                         final float y) {
      GLES20.glUniform2f(loc, x, y);
   }


   @Override
   public void uniform1f(final int loc,
                         final float x) {
      GLES20.glUniform1f(loc, x);
   }


   @Override
   public void uniform1i(final int loc,
                         final int v) {
      GLES20.glUniform1i(loc, v);
   }


   @Override
   public void uniformMatrix4fv(final int location,
                                final int count,
                                final boolean transpose,
                                final float[] value) {
      final FloatBuffer fb = floatArrayToFloatBuffer(value);

      for (int i = 0; i < fb.capacity(); i++) {
         float d = fb.get(i);
         d++;
      }

      GLES20.glUniformMatrix4fv(location, count, transpose, fb);
   }


   @Override
   public void clearColor(final float red,
                          final float green,
                          final float blue,
                          final float alpha) {
      GLES20.glClearColor(red, green, blue, alpha);
   }


   @Override
   public void clear(final int nBuffer,
                     final GLBufferType[] buffers) {
      int b = 0x00000000;
      for (final GLBufferType buffer : buffers) {
         b |= getBitField(buffer);
      }
      GLES20.glClear(b);
   }


   @Override
   public void uniform4f(final int location,
                         final float v0,
                         final float v1,
                         final float v2,
                         final float v3) {
      GLES20.glUniform4f(location, v0, v1, v2, v3);
   }


   @Override
   public void enable(final GLFeature feature) {
      GLES20.glEnable(getEnum(feature));
   }


   @Override
   public void disable(final GLFeature feature) {
      GLES20.glDisable(getEnum(feature));
   }


   @Override
   public void polygonOffset(final float factor,
                             final float units) {
      GLES20.glPolygonOffset(factor, units);
   }


   @Override
   public void vertexAttribPointer(final int index,
                                   final int size,
                                   final GLType type,
                                   final boolean normalized,
                                   final int stride,
                                   final Object pointer) {
      final float[] floatArray = (float[]) pointer;
      final FloatBuffer fb = floatArrayToFloatBuffer(floatArray);
      GLES20.glVertexAttribPointer(index, size, getEnum(type), normalized, stride, fb);
   }


   @Override
   public void drawElements(final GLPrimitive mode,
                            final int count,
                            final GLType type,
                            final Object indices) {
      if ((type == GLType.Int) || (type == GLType.UnsignedInt)) {
         final int[] ind = (int[]) indices;
         final IntBuffer indexBuffer = IntBuffer.wrap(ind);
         GLES20.glDrawElements(getEnum(mode), count, getEnum(type), indexBuffer);
      }
   }


   @Override
   public void lineWidth(final float width) {
      GLES20.glLineWidth(width);
   }


   @Override
   public GLError getError() {
      return getError(GLES20.glGetError());
   }


   @Override
   public void blendFunc(final GLBlendFactor sfactor,
                         final GLBlendFactor dfactor) {
      GLES20.glBlendFunc(getEnum(sfactor), getEnum(dfactor));
   }


   @Override
   public void bindTexture(final GLTextureType target,
                           final int texture) {
      GLES20.glBindTexture(getEnum(target), texture);
   }


   @Override
   public void deleteTextures(final int n,
                              final int[] textures) {
      final IntBuffer tex = IntBuffer.wrap(textures);
      GLES20.glDeleteTextures(n, tex);
   }


   @Override
   public void enableVertexAttribArray(final int location) {
      GLES20.glEnableVertexAttribArray(location);
   }


   @Override
   public void disableVertexAttribArray(final int location) {
      GLES20.glDisableVertexAttribArray(location);
   }


   @Override
   public void pixelStorei(final GLAlignment pname,
                           final int param) {
      GLES20.glPixelStorei(getEnum(pname), param);
   }


   @Override
   public ArrayList<GLTextureId> genTextures(final int n) {
      final ArrayList<GLTextureId> ai = new ArrayList<GLTextureId>();
      final int[] tex = new int[n];
      GLES20.glGenTextures(n, tex, 0);
      for (int i = 0; i < n; i++) {
         ai.add(new GLTextureId(tex[i]));
      }
      return ai;
   }


   @Override
   public void texParameteri(final GLTextureType target,
                             final GLTextureParameter par,
                             final GLTextureParameterValue v) {
      GLES20.glTexParameteri(getEnum(target), getEnum(par), getValue(v));
   }


   @Override
   public void texImage2D(final GLTextureType target,
                          final int level,
                          final GLFormat internalFormat,
                          final int width,
                          final int height,
                          final int border,
                          final GLFormat format,
                          final GLType type,
                          final Object data) {

      if (type == GLType.UnsignedByte) {
         final byte[] array = (byte[]) data;
         final ByteBuffer pixels = ByteBuffer.wrap(array);
         GLES20.glTexImage2D(getEnum(target), level, getEnum(internalFormat), width, height, border, getEnum(format),
                  getEnum(type), pixels);
      }
   }


   @Override
   public void drawArrays(final GLPrimitive mode,
                          final int first,
                          final int count) {
      GLES20.glDrawArrays(getEnum(mode), first, count);

   }


   @Override
   public void cullFace(final GLCullFace c) {
      GLES20.glCullFace(getEnum(c));
   }


   @Override
   public void getIntegerv(final GLVariable v,
                           final int[] i) {
      GLES20.glGetIntegerv(getEnum(v), IntBuffer.wrap(i));
   }


   @Override
   public void generateMipmap(final GLTextureType target) {
      GLES20.glGenerateMipmap(getEnum(target));
   }

}
