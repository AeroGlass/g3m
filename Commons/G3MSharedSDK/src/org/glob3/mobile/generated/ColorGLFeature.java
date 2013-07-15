package org.glob3.mobile.generated; 
public class ColorGLFeature extends GLColorGroupFeature
{
  public ColorGLFeature(IFloatBuffer colors, int arrayElementSize, int index, boolean normalized, int stride, boolean blend, int sFactor, int dFactor)
  {
     super(3, blend, sFactor, dFactor);
    GPUAttributeValueVec4Float value = new GPUAttributeValueVec4Float(colors, arrayElementSize, index, stride, normalized);
    _values.addNewAttributeValue(GPUAttributeKey.COLOR, value);
  }
  public final void applyOnGlobalGLState(GLGlobalState state)
  {
    blendingOnGlobalGLState(state);
  }
}