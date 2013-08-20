//
//  GLState.h
//  G3MiOSSDK
//
//  Created by Jose Miguel SN on 17/05/13.
//
//  Created by Agustin Trujillo Pino on 27/10/12.
//  Copyright (c) 2012 Universidad de Las Palmas. All rights reserved.
//

#ifndef __G3MiOSSDK__GLState__
#define __G3MiOSSDK__GLState__

#include <iostream>

#include "GLGlobalState.hpp"
#include "GPUProgram.hpp"
#include "GPUProgramManager.hpp"

#include "GLFeatureGroup.hpp"
#include "GLFeature.hpp"
#include "GPUVariableValueSet.hpp"

class GLState{

  GLFeatureSet _features;
  mutable GLFeatureSet* _accumulatedFeatures;

  mutable int _timeStamp;
  mutable int _parentsTimeStamp;

  mutable GPUVariableValueSet* _valuesSet;
  mutable GLGlobalState*   _globalState;

  mutable GPUProgram* _lastGPUProgramUsed;

#ifdef C_CODE
  mutable const GLState* _parentGLState;
#endif
#ifdef JAVA_CODE
  private GLState _parentGLState;
#endif

  GLState(const GLState& state);

  void hasChangedStructure() const;

public:

  GLState():
  _parentGLState(NULL),
  _lastGPUProgramUsed(NULL),
  _parentsTimeStamp(0),
  _timeStamp(0),
  _valuesSet(NULL),
  _globalState(NULL),
  _accumulatedFeatures(NULL)
  {
  }

  int getTimeStamp() const { return _timeStamp;}

  GLFeatureSet* getAccumulatedFeatures() const;
//  GLFeatureSet* createAccumulatedFeatures() const;

  ~GLState();

  void setParent(const GLState* p) const;

  void applyOnGPU(GL* gl, GPUProgramManager& progManager) const;

  void addGLFeature(const GLFeature* f, bool mustRetain);

  void clearGLFeatureGroup(GLFeatureGroupName g);

  bool contains(const GLFeature* f) const{
    for (int i = 0; i < _features.size(); i++) {
      if (_features.get(i) == f){
        return true;
      }
    }
    if (_parentGLState == NULL){
      return false;
    }
    return _parentGLState->contains(f);
  }

};

#endif /* defined(__G3MiOSSDK__GLState__) */
