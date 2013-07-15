package org.glob3.mobile.generated; 
//
//  SGRotateNode.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 11/8/12.
//
//

//
//  SGRotateNode.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 11/8/12.
//
//




public class SGRotateNode extends SGNode
{
  private final double _x;
  private final double _y;
  private final double _z;
  private final double _angle;

  private MutableMatrix44D _rotationMatrix = new MutableMatrix44D();

  private GLState _glState = new GLState();

  public SGRotateNode(String id, String sId, double x, double y, double z, double angle)
  {
     super(id, sId);
     _x = x;
     _y = y;
     _z = z;
     _angle = angle;
     _rotationMatrix = new MutableMatrix44D(MutableMatrix44D.createRotationMatrix(Angle.fromDegrees(_angle), new Vector3D(_x, _y, _z)));
    //_glState.getGPUProgramState()->setUniformMatrixValue(MODELVIEW, _rotationMatrix, true);
//    _glState.setModelView(_rotationMatrix.asMatrix44D(), true);

    _glState.addGLFeatureAndRelease(new ModelTransformGLFeature(_rotationMatrix.asMatrix44D()));
  }

  public final GLState getGLState(GLState parentGLState)
  {
    _glState.setParent(parentGLState);
    return _glState;
  }

  public final String description()
  {
    return "SGRotateNode";
  }


}