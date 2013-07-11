//
//  CameraFlatSingleDragHandler.cpp
//  G3MiOSSDK
//
//  Created by Agustín Trujillo on 10/07/13.
//
//

#include "CameraFlatSingleDragHandler.hpp"
#include "MutableVector2D.hpp"
#include "TouchEvent.hpp"
#include "CameraRenderer.hpp"
#include "GL.hpp"
#include "Plane.hpp"


bool CameraFlatSingleDragHandler::onTouchEvent(const G3MEventContext *eventContext,
                                           const TouchEvent* touchEvent,
                                           CameraContext *cameraContext)
{
  // only one finger needed
  if (touchEvent->getTouchCount()!=1) return false;
  if (touchEvent->getTapCount()>1) return false;
  
  switch (touchEvent->getType()) {
    case Down:
      onDown(eventContext, *touchEvent, cameraContext);
      break;
    case Move:
      onMove(eventContext, *touchEvent, cameraContext);
      break;
    case Up:
      onUp(eventContext, *touchEvent, cameraContext);
    default:
      break;
  }
  
  return true;
}


void CameraFlatSingleDragHandler::onDown(const G3MEventContext *eventContext,
                                     const TouchEvent& touchEvent,
                                     CameraContext *cameraContext) {
  Camera *camera = cameraContext->getNextCamera();
  _camera0.copyFrom(*camera);
  cameraContext->setCurrentGesture(Drag);
  _axis = MutableVector3D::nan();
  _lastRadians = _radiansStep = 0.0;
  
  // dragging
  const Vector2I pixel = touchEvent.getTouch(0)->getPos();
  Vector3D origin = _camera0.getCartesianPosition();
  Vector3D ray = _camera0.pixel2Ray(pixel);
  _initialPoint = Plane::intersectionXYPlaneWithRay(origin, ray).asMutableVector3D();
  
  printf ("down 1 finger. Initial point = %f %f %f\n", _initialPoint.x(), _initialPoint.y(), _initialPoint.z());
}

void CameraFlatSingleDragHandler::onMove(const G3MEventContext *eventContext,
                                     const TouchEvent& touchEvent,
                                     CameraContext *cameraContext) {
  
  if (cameraContext->getCurrentGesture()!=Drag) {
    return;
  }
  
  if (_initialPoint.isNan()) {
    return;
  }
  
  const Vector2I pixel = touchEvent.getTouch(0)->getPos();
    
  MutableVector3D finalPoint = _camera0.pixel2PlanetPoint(pixel).asMutableVector3D();
  if (finalPoint.isNan()) {
    //INVALID FINAL POINT
    //printf ("--invalid final point in drag!!\n");
    Vector3D ray = _camera0.pixel2Ray(pixel);
    Vector3D pos = _camera0.getCartesianPosition();
    finalPoint = eventContext->getPlanet()->closestPointToSphere(pos, ray).asMutableVector3D();
  }
  
  // make drag
  Camera *camera = cameraContext->getNextCamera();
  camera->copyFrom(_camera0);
  camera->dragCamera(_initialPoint.asVector3D(), finalPoint.asVector3D());
  
  
  // save drag parameters
  _axis = _initialPoint.cross(finalPoint);
  
  const double radians = - IMathUtils::instance()->asin(_axis.length()/_initialPoint.length()/finalPoint.length());
  _radiansStep = radians - _lastRadians;
  _lastRadians = radians;
}


void CameraFlatSingleDragHandler::onUp(const G3MEventContext *eventContext,
                                   const TouchEvent& touchEvent,
                                   CameraContext *cameraContext) {
  if (_useInertia) {
    // test if animation is needed
    const Touch *touch = touchEvent.getTouch(0);
    Vector2I currPixel = touch->getPos();
    Vector2I prevPixel = touch->getPrevPos();
    double desp        = currPixel.sub(prevPixel).length();
    
    if (cameraContext->getCurrentGesture()==Drag && !_axis.isNan() && desp>2) {
      // start inertial effect
      Effect *effect = new SingleDragEffect(_axis.asVector3D(), Angle::fromRadians(_radiansStep));
      
      EffectTarget* target = cameraContext->getNextCamera()->getEffectTarget();
      eventContext->getEffectsScheduler()->startEffect(effect, target);
    }
  }
  
  // update gesture
  cameraContext->setCurrentGesture(None);
  //_initialPixel = MutableVector2I::zero();
}

void CameraFlatSingleDragHandler::render(const G3MRenderContext* rc, CameraContext *cameraContext)
{
  //  // TEMP TO DRAW A POINT WHERE USER PRESS
  //  if (false) {
  //    if (cameraContext->getCurrentGesture() == Drag) {
  //      GL* gl = rc->getGL();
  //      float vertices[] = { 0,0,0};
  //      int indices[] = {0};
  //      gl->enableVerticesPosition();
  //      gl->disableTexture2D();
  //      gl->disableTextures();
  //      gl->vertexPointer(3, 0, vertices);
  //      gl->color((float) 0, (float) 1, (float) 0, 1);
  //      gl->pointSize(60);
  //      gl->pushMatrix();
  //      MutableMatrix44D T = MutableMatrix44D::createTranslationMatrix(_initialPoint.asVector3D());
  //      gl->multMatrixf(T);
  //      gl->drawPoints(1, indices);
  //      gl->popMatrix();
  //
  //      //Geodetic2D g = _planet->toGeodetic2D(_initialPoint.asVector3D());
  //      //printf ("zoom with initial point = (%f, %f)\n", g.latitude()._degrees, g.longitude()._degrees);
  //    }
  //  }
}
