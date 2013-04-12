package org.glob3.mobile.generated; 
//
//  Mesh.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 27/06/12.
//  Copyright (c) 2012 IGO Software SL. All rights reserved.
//

//
//  Mesh.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 27/06/12.
//  Copyright (c) 2012 IGO Software SL. All rights reserved.
//



//class Vector3D;
//class GPUProgramState;

public abstract class Mesh
{

  public void dispose()
  {
  }

  public abstract int getVertexCount();

  public abstract Vector3D getVertex(int i);

  public abstract void render(G3MRenderContext rc, GLState parentState, GPUProgramState parentProgramState);

  public abstract Extent getExtent();

  public abstract boolean isTransparent(G3MRenderContext rc);

}