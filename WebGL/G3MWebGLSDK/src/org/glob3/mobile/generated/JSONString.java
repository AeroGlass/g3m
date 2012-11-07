package org.glob3.mobile.generated; 
//
//  JSONString.hpp
//  G3MiOSSDK
//
//  Created by Oliver Koehler on 03/10/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//



public class JSONString extends JSONBaseObject
{
  private final String _value;

  public void dispose()
  {
  }

  public JSONString(String value)
  {
	  _value = value;
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: const String value() const
  public final String value()
  {
	return _value;
  }

  public final JSONString asString()
  {
	return this;
  }

}