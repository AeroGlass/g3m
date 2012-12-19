package org.glob3.mobile.generated; 
//
//  IG3MJSONBuilder.hpp
//  G3MiOSSDK
//
//  Created by Eduardo de la Montaña on 29/10/12.
//  Copyright (c) 2012 __MyCompanyName__. All rights reserved.
//




public abstract class IG3MJSONBuilder
{

	protected String _jsonSource;


	public IG3MJSONBuilder(String jsonSource)
	{
		_jsonSource = jsonSource;
	}
	public abstract void create(LayerSet layerSet, GInitializationTask initializationTask, MarkTouchListener markTouchListener, MarkTouchListener panoTouchListener);
	public void dispose()
	{
	}
}