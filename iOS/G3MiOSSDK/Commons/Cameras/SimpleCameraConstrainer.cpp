//
//  SimpleCameraConstrainer.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 1/30/13.
//
//

#include "SimpleCameraConstrainer.hpp"

#include "Camera.hpp"

bool SimpleCameraConstrainer::onCameraChange(const Planet *planet,
                                             const Camera* previousCamera,
                                             Camera* nextCamera) const {
  
  long long previousCameraTimeStamp = previousCamera->getTimeStamp();
  long long nextCameraTimeStamp = nextCamera->getTimeStamp();
  if (previousCameraTimeStamp != _previousCameraTimeStamp || nextCameraTimeStamp != _nextCameraTimeStamp) {
    _previousCameraTimeStamp = previousCameraTimeStamp;
    _nextCameraTimeStamp = nextCameraTimeStamp;
    printf("Cameras TimeStamp: PreviousCam=%lld; NextCam=%lld\n", _previousCameraTimeStamp, _nextCameraTimeStamp);
  }

  const double radii = planet->getRadii().maxAxis();
  const double maxHeight = radii*9;
  const double minHeight = 10;

  const Geodetic3D cameraPosition = nextCamera->getGeodeticPosition();
  const double cameraHeight = cameraPosition._height;

  if (cameraHeight > maxHeight) {
    nextCamera->copyFromForcingMatrixCreation(*previousCamera);
    /*nextCamera->setGeodeticPosition(cameraPosition._latitude,
                                    cameraPosition._longitude,
                                    maxHeight);*/
  }
  else if (cameraHeight < minHeight) {
    nextCamera->copyFromForcingMatrixCreation(*previousCamera);
    /*nextCamera->setGeodeticPosition(cameraPosition._latitude,
                                    cameraPosition._longitude,
                                    minHeight);*/
  }

  return true;
}
