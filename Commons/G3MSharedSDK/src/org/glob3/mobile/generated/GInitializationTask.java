package org.glob3.mobile.generated; 
//
//  GInitializationTask.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 12/11/12.
//
//

//
//  GInitializationTask.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 12/11/12.
//
//



public abstract class GInitializationTask extends GTask
{
///#ifdef C_CODE
//  virtual ~GInitializationTask() { }
///#endif

//C++ TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#warning vtp ask Dgd: GInitializationTask no debería ser una interfaz pura??

  //virtual void run(const G3MContext* context) = 0;

  public abstract boolean isDone(G3MContext context);
}