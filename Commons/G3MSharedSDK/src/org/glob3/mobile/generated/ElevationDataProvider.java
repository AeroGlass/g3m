package org.glob3.mobile.generated; 
public abstract class ElevationDataProvider
{

  public void dispose()
  {

  }

  public abstract boolean isReadyToRender(G3MRenderContext rc);

  public abstract void initialize(G3MContext context);

  public abstract long requestElevationData(Sector sector, Vector2I resolution, IElevationDataListener listener, boolean autodeleteListener);

  public abstract void cancelRequest(long requestId);

  public abstract java.util.ArrayList<Sector> getSectors();

  public abstract Vector2I getMinResolution();

  public abstract ElevationData createSubviewOfElevationData(ElevationData elevationData, Sector sector, Vector2I resolution);

}