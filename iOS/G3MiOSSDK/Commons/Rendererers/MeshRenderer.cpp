//
//  MeshRenderer.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 12/22/12.
//
//

#include "MeshRenderer.hpp"

#include "Mesh.hpp"
#include "Camera.hpp"

void MeshRenderer::clearMeshes() {
  const int meshesCount = _meshes.size();
  for (int i = 0; i < meshesCount; i++) {
    Mesh* mesh = _meshes[i];
    delete mesh;
  }
  _meshes.clear();
}

MeshRenderer::~MeshRenderer() {
  const int meshesCount = _meshes.size();
  for (int i = 0; i < meshesCount; i++) {
    Mesh* mesh = _meshes[i];
    delete mesh;
  }
}

void MeshRenderer::render(const G3MRenderContext* rc) {
  const Frustum* frustum = rc->getCurrentCamera()->getFrustumInModelCoordinates();

  _glState.getGPUProgramState()->setUniformMatrixValue("uModelview", rc->getCurrentCamera()->getModelViewMatrix(), false);

  const int meshesCount = _meshes.size();
  for (int i = 0; i < meshesCount; i++) {
    Mesh* mesh = _meshes[i];
    const Extent* extent = mesh->getExtent();

    if ( extent->touches(frustum) ) {
      mesh->render(rc, &_glState);
    }
  }
}

void MeshRenderer::createGLState() const{
  
  _glState.getGLGlobalState()->enableDepthTest();
  
//  GPUProgramState& progState = *_glState.getGPUProgramState();
//  progState.setUniformValue("EnableTexture", false);
//  progState.setUniformValue("uPointSize", (float)1.0);
//  progState.setUniformValue("uScaleTexCoord", Vector2D(1.0,1.0));
//  progState.setUniformValue("uTranslationTexCoord", Vector2D(0.0,0.0));
}
