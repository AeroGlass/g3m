package org.glob3.mobile.generated; 
//
//  QuadShape.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 10/28/12.
//
//

//
//  QuadShape.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 10/28/12.
//
//



//class IImage;
//class IGLTextureId;
//class Color;


public class QuadShape extends AbstractMeshShape
{
  private URL _textureURL = new URL();
  private final float _width;
  private final float _height;
  private final Color _color;

  private boolean _textureRequested;
  private IImage _textureImage;
  private IGLTextureId getTextureId(G3MRenderContext rc)
  {
    if (_textureImage == null)
    {
      return null;
    }
  
    final IGLTextureId texId = rc.getTexturesHandler().getGLTextureId(_textureImage, GLFormat.rgba(), _textureURL.getPath(), false);
  
    rc.getFactory().deleteImage(_textureImage);
    _textureImage = null;
  
    if (texId == null)
    {
      rc.getLogger().logError("Can't load texture %s", _textureURL.getPath());
    }
  
    return texId;
  }

  protected final Mesh createMesh(G3MRenderContext rc)
  {
    if (!_textureRequested)
    {
      _textureRequested = true;
      if (_textureURL.getPath().length() != 0)
      {
        rc.getDownloader().requestImage(_textureURL, 1000000, TimeInterval.fromDays(30), new QuadShape_IImageDownloadListener(this), true);
      }
    }
  
    final float halfWidth = _width / 2.0f;
    final float halfHeight = _height / 2.0f;
  
    final float left = -halfWidth;
    final float right = +halfWidth;
    final float bottom = -halfHeight;
    final float top = +halfHeight;
  
    FloatBufferBuilderFromCartesian3D vertices = new FloatBufferBuilderFromCartesian3D(CenterStrategy.noCenter(), Vector3D.zero());
    vertices.add(left, bottom, 0);
    vertices.add(right, bottom, 0);
    vertices.add(left, top, 0);
    vertices.add(right, top, 0);
  
  //  const Vector3D center = Vector3D::zero();
  
    Color color = (_color == null) ? null : new Color(_color);
  
    Mesh im = new DirectMesh(GLPrimitive.triangleStrip(), true, vertices.getCenter(), vertices.create(), 1, 1, color);
  
    final IGLTextureId texId = getTextureId(rc);
    if (texId == null)
    {
      return im;
    }
  
    FloatBufferBuilderFromCartesian2D texCoords = new FloatBufferBuilderFromCartesian2D();
    texCoords.add(0, 1);
    texCoords.add(1, 1);
    texCoords.add(0, 0);
    texCoords.add(1, 0);
  
    TextureMapping texMap = new SimpleTextureMapping(texId, texCoords.create(), true, true);
  
    return new TexturedMesh(im, true, texMap, true, true);
  }

  public QuadShape(Geodetic3D position, URL textureURL, float width, float height)
  {
     super(position);
     _textureURL = new URL(textureURL);
     _width = width;
     _height = height;
     _textureRequested = false;
     _textureImage = null;
     _color = null;

  }

  public QuadShape(Geodetic3D position, IImage textureImage, float width, float height)
  {
     super(position);
     _textureURL = new URL(new URL("", false));
     _width = width;
     _height = height;
     _textureRequested = true;
     _textureImage = textureImage;
     _color = null;

  }


  public QuadShape(Geodetic3D position, float width, float height, Color color)
  {
     super(position);
     _textureURL = new URL(new URL("", false));
     _width = width;
     _height = height;
     _textureRequested = false;
     _textureImage = null;
     _color = color;

  }

  public void dispose()
  {
    if (_color != null)
       _color.dispose();
  }

  public final void imageDownloaded(IImage image)
  {
    _textureImage = image;
  
    cleanMesh();
  }

}