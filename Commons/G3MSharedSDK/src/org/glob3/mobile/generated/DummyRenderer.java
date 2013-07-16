package org.glob3.mobile.generated; 
//  DummyRenderer.cpp
//  Glob3 Mobile
//
//  Created by Agustín Trujillo Pino on 02/05/11.
//  Copyright 2011 Universidad de Las Palmas. All rights reserved.


//
//  DummyRenderer.hpp
//  Glob3 Mobile
//
//  Created by Agustín Trujillo Pino on 02/05/11.
//  Copyright 2011 Universidad de Las Palmas. All rights reserved.
//



//class IFloatBuffer;
//class IShortBuffer;
//class GL;
//class Color;
//class Angle;
//class Vector3D;
//class GPUProgramManager;
//class GLState;


public abstract class DummyRenderer extends LeafRenderer
{

  private double _halfSize;

  private IShortBuffer _indices;
  private IFloatBuffer _vertices;

  private void drawFace(GL gl, GLState parentState, Color color, Vector3D translation, Angle a, Vector3D rotationAxis, GPUProgramManager manager)
  {
  
    GLState glState = new GLState();
    glState.setParent(parentState);
  
  //  GPUProgramState& progState = *glState.getGPUProgramState();
  //  progState.setUniformValue(FLAT_COLOR, color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
  
    glState.addGLFeature(new FlatColorGLFeature(color, color.isTransparent(), GLBlendFactor.srcAlpha(), GLBlendFactor.oneMinusSrcAlpha()), false);
  
    MutableMatrix44D T = MutableMatrix44D.createTranslationMatrix(translation);
    MutableMatrix44D R = MutableMatrix44D.createRotationMatrix(a, rotationAxis);
  
    MutableMatrix44D TR = T.multiply(R);
  
  //  glState.setModelView(TR.asMatrix44D(), true);
  
    glState.clearGLFeatureGroup(GLFeatureGroupName.CAMERA_GROUP);
    glState.addGLFeature(new ModelTransformGLFeature(TR.asMatrix44D()), false);
  
    gl.drawElements(GLPrimitive.triangleStrip(), _indices, glState, manager);
  }


  ///#include "GPUProgramState.hpp"
  
  public void dispose()
  {
    if (_indices != null)
       _indices.dispose();
    if (_vertices != null)
       _vertices.dispose();
  }

  public final void initialize(G3MContext context)
  {
    int res = 12;
  
    FloatBufferBuilderFromCartesian3D vertices = new FloatBufferBuilderFromCartesian3D(CenterStrategy.noCenter(), Vector3D.zero());
    ShortBufferBuilder index = new ShortBufferBuilder();
  
    // create vertices
  
    if (context != null && context.getPlanet() != null)
    {
      _halfSize = context.getPlanet().getRadii()._x / 2.0;
    }
    else
    {
      _halfSize = 7e6;
    }
  
    for (int j = 0; j < res; j++)
    {
      for (int i = 0; i < res; i++)
      {
  
        vertices.add((float)0, (float)(-_halfSize + i / (float)(res - 1) * 2 *_halfSize), (float)(+_halfSize - j / (float)(res - 1) * 2 *_halfSize));
      }
    }
  
    for (int j = 0; j < res - 1; j++)
    {
      if (j > 0)
      {
        index.add((short)(j * res));
      }
      for (int i = 0; i < res; i++)
      {
        index.add((short)(j * res + i));
        index.add((short)(j * res + i + res));
      }
      index.add((short)(j * res + 2 * res - 1));
    }
  
    _indices = index.create();
    _vertices = vertices.create();
  }

  public final void render(G3MRenderContext rc, GLGlobalState parentState)
  {
  
    //TODO: IMPLEMENT
    GLState glState = new GLState();
  //  GPUProgramState& progState = *glState.getGPUProgramState();
  
    glState.addGLFeature(new GeometryGLFeature(_vertices, 3, 0, false, 0, true, false, 0, false, (float)0.0, (float)0.0, (float)1.0, false, (float)1.0), false); //Depth test - Stride 0 - Not normalized - Index 0 - Our buffer contains elements of 3 - The attribute is a float vector of 4 elements
  
  //  progState.setAttributeValue(POSITION,
  //                              _vertices, 4, //The attribute is a float vector of 4 elements
  //                              3,            //Our buffer contains elements of 3
  //                              0,            //Index 0
  //                              false,        //Not normalized
  //                              0);           //Stride 0
  //  glState.setModelView(rc->getCurrentCamera()->getModelViewMatrix().asMatrix44D(), false);
  
    rc.getCurrentCamera().addProjectionAndModelGLFeatures(glState);
  
  //  GLGlobalState state(parentState);
  
    GL gl = rc.getGL();
    GPUProgramManager manager = rc.getGPUProgramManager();
    drawFace(gl, glState, Color.fromRGBA((float) 1, (float) 0, (float) 0, (float) 1), new Vector3D(_halfSize,0,0), Angle.fromDegrees(0), new Vector3D(0,0,1), manager);
  
    drawFace(gl, glState, Color.fromRGBA((float) 0, (float) 1, (float) 0, (float) 1), new Vector3D(0,_halfSize,0), Angle.fromDegrees(90), new Vector3D(0,0,1), manager);
  
    drawFace(gl, glState, Color.fromRGBA((float) 0, (float) 0, (float) 1, (float) 1), new Vector3D(0,-_halfSize,0), Angle.fromDegrees(-90), new Vector3D(0,0,1), manager);
  
    drawFace(gl, glState, Color.fromRGBA((float) 1, (float) 0, (float) 1, (float) 1), new Vector3D(0,0,-_halfSize), Angle.fromDegrees(90), new Vector3D(0,1,0), manager);
  
    drawFace(gl, glState, Color.fromRGBA((float) 0, (float) 1, (float) 1, (float) 1), new Vector3D(0,0,_halfSize), Angle.fromDegrees(-90), new Vector3D(0,1,0), manager);
  
    drawFace(gl, glState, Color.fromRGBA((float) 0.5, (float) 0.5, (float) 0.5, (float) 1), new Vector3D(-_halfSize,0,0), Angle.fromDegrees(180), new Vector3D(0,0,1), manager);
  }

  public final boolean onTouchEvent(G3MEventContext ec, TouchEvent touchEvent)
  {
    return false;
  }

  public final void onResizeViewportEvent(G3MEventContext ec, int width, int height)
  {

  }

  public final boolean isReadyToRender(G3MRenderContext rc)
  {
    return true;
  }

  public final void start(G3MRenderContext rc)
  {

  }

  public final void stop(G3MRenderContext rc)
  {

  }

  public final void onResume(G3MContext context)
  {

  }

  public final void onPause(G3MContext context)
  {

  }

  public final void onDestroy(G3MContext context)
  {

  }

}