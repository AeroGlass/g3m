package org.glob3.mobile.generated; 
public class TextureUploader extends IImageListener
{
  private TileTextureBuilder _builder;

  private final java.util.ArrayList<RectangleI> _srcRects;
  private final java.util.ArrayList<RectangleI> _dstRects;

  private final String _textureId;

  public TextureUploader(TileTextureBuilder builder, java.util.ArrayList<RectangleF> srcRects, java.util.ArrayList<RectangleF> dstRects, String textureId)
  {
     _builder = builder;
     _srcRects = srcRects;
     _dstRects = dstRects;
     _textureId = textureId;

  }

  public final void imageCreated(IImage image)
  {
    _builder.imageCreated(image, _srcRects, _dstRects, _textureId);
  }
}