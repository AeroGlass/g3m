package org.glob3.mobile.generated; 
public class GeometryGLFeature extends GLFeature
{
  //Position + cull + depth + polygonoffset + linewidth
  private GPUAttributeValueVec4Float _position;

  private final boolean _depthTestEnabled;
  private final boolean _cullFace;
  private final int _culledFace;
  private final boolean _polygonOffsetFill;
  private final float _polygonOffsetFactor;
  private final float _polygonOffsetUnits;
  private final float _lineWidth;


  public GeometryGLFeature(IFloatBuffer buffer, int arrayElementSize, int index, boolean normalized, int stride, boolean depthTestEnabled, boolean cullFace, int culledFace, boolean polygonOffsetFill, float polygonOffsetFactor, float polygonOffsetUnits, float lineWidth, boolean needsPointSize, float pointSize)
  {
     super(GLFeatureGroupName.NO_GROUP);
     _depthTestEnabled = depthTestEnabled;
     _cullFace = cullFace;
     _culledFace = culledFace;
     _polygonOffsetFill = polygonOffsetFill;
     _polygonOffsetFactor = polygonOffsetFactor;
     _polygonOffsetUnits = polygonOffsetUnits;
     _lineWidth = lineWidth;
  
    _position = new GPUAttributeValueVec4Float(buffer, arrayElementSize, index, stride, normalized);
    _values.addAttributeValue(GPUAttributeKey.POSITION, _position);
  
  //  _globalState = new GLGlobalState();
  //  if (depthTestEnabled){
  //    _globalState->enableDepthTest();
  //  } else{
  //    _globalState->disableDepthTest();
  //  }
  //
  //  if (cullFace){
  //    _globalState->enableCullFace(culledFace);
  //  } else{
  //    _globalState->disableCullFace();
  //  }
  //
  //  if (polygonOffsetFill){
  //    _globalState->enablePolygonOffsetFill(polygonOffsetFactor, polygonOffsetFill);
  //  } else{
  //    _globalState->disPolygonOffsetFill();
  //  }
  //
  //  _globalState->setLineWidth(lineWidth);
  
    if (needsPointSize)
    {
      _values.addNewUniformValue(GPUUniformKey.POINT_SIZE, new GPUUniformValueFloat(pointSize));
    }
  }

  public void dispose()
  {
    _position._release();
  }

  public final void applyOnGlobalGLState(GLGlobalState state)
  {
    if (_depthTestEnabled)
    {
      state.enableDepthTest();
    }
    else
    {
      state.disableDepthTest();
    }
  
    if (_cullFace)
    {
      state.enableCullFace(_culledFace);
    }
    else
    {
      state.disableCullFace();
    }
  
    if (_polygonOffsetFill)
    {
      state.enablePolygonOffsetFill(_polygonOffsetFactor, _polygonOffsetUnits);
    }
    else
    {
      state.disPolygonOffsetFill();
    }
  
    state.setLineWidth(_lineWidth);
  }

}