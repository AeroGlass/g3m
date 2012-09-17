package org.glob3.mobile.generated; 
//
//  BingLayer.cpp
//  G3MiOSSDK
//
//  Created by Oliver Koehler on 30/08/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//

//
//  BingLayer.h
//  G3MiOSSDK
//
//  Created by Oliver Koehler on 30/08/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//



public enum MapType
{
  Road,
  Aerial,
  Hybrid;

	public int getValue()
	{
		return this.ordinal();
	}

	public static MapType forValue(int value)
	{
		return values()[value];
	}
}