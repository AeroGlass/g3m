//
//  LeafRenderer.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 10/16/12.
//
//

#ifndef __G3MiOSSDK__LeafRenderer__
#define __G3MiOSSDK__LeafRenderer__

#include "Renderer.hpp"

class GPUProgramState;

class LeafRenderer : public Renderer {
private:
  bool _enable;
  
public:
  LeafRenderer() :
  _enable(true)
  {
    
  }
  
  LeafRenderer(bool enable) :
  _enable(enable)
  {
    
  }
  
  ~LeafRenderer() {
    
  }
  
  bool isEnable() const {
    return _enable;
  }

#ifdef C_CODE
  void setEnable(bool enable) {
    _enable = enable;
  }
#endif
#ifdef JAVA_CODE
  public void setEnable(final boolean enable) {
    _enable = enable;
  }
#endif
  
  virtual void onResume(const G3MContext* context) = 0;
  
  virtual void onPause(const G3MContext* context) = 0;

  virtual void onDestroy(const G3MContext* context) = 0;

  virtual void initialize(const G3MContext* context) = 0;
  
  virtual bool isReadyToRender(const G3MRenderContext* rc) = 0;
  
  virtual void render(const G3MRenderContext* rc,
                      const GLState& parentState) = 0;
  
  virtual bool onTouchEvent(const G3MEventContext* ec,
                            const TouchEvent* touchEvent) = 0;
  
  virtual void onResizeViewportEvent(const G3MEventContext* ec,
                                     int width, int height) = 0;
  
  virtual void start(const G3MRenderContext* rc) = 0;
  
  virtual void stop(const G3MRenderContext* rc) = 0;

};

#endif
