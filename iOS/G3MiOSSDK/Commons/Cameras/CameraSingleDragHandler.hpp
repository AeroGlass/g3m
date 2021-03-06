//
//  CameraSingleDragHandler.hpp
//  G3MiOSSDK
//
//  Created by Agustin Trujillo Pino on 28/07/12.
//  Copyright (c) 2012 Universidad de Las Palmas. All rights reserved.
//

#ifndef G3MiOSSDK_CameraSingleDragHandler
#define G3MiOSSDK_CameraSingleDragHandler


#include "CameraEventHandler.hpp"
#include "Camera.hpp"
#include "Effects.hpp"
#include "MutableVector2I.hpp"



class CameraSingleDragHandler: public CameraEventHandler {
  
public:
  CameraSingleDragHandler(bool useInertia):
  _useInertia(useInertia)
  {}
  
  ~CameraSingleDragHandler() {
#ifdef JAVA_CODE
  super.dispose();
#endif

  }
  
  
  bool onTouchEvent(const G3MEventContext *eventContext,
                    const TouchEvent* touchEvent,
                    CameraContext *cameraContext);
  
  void render(const G3MRenderContext* rc,
              CameraContext *cameraContext);
  
  const bool _useInertia;
  void onDown(const G3MEventContext *eventContext,
              const TouchEvent& touchEvent,
              CameraContext *cameraContext);
  void onMove(const G3MEventContext *eventContext,
              const TouchEvent& touchEvent,
              CameraContext *cameraContext);
  void onUp(const G3MEventContext *eventContext,
            const TouchEvent& touchEvent,
            CameraContext *cameraContext);
private:
  
  MutableVector3D _cameraPosition;
  MutableVector3D _cameraCenter;
  MutableVector3D _cameraUp;
  MutableVector2I _cameraViewPort;
  MutableMatrix44D _cameraModelViewMatrix;
  MutableVector3D _finalRay;
};


#endif
