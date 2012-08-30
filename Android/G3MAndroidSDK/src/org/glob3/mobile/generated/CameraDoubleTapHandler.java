package org.glob3.mobile.generated; 
//***************************************************************


public class CameraDoubleTapHandler extends CameraEventHandler
{


  public void dispose()
  {
  }


  public final boolean onTouchEvent(EventContext eventContext, TouchEvent touchEvent, CameraContext cameraContext)
  {
	// only one finger needed
	if (touchEvent.getTouchCount()!=1)
		return false;
	if (touchEvent.getTapCount()!=2)
		return false;
	if (touchEvent.getType()!=TouchEventType.Down)
		return false;
  
	onDown(eventContext, touchEvent, cameraContext);
	return true;
  }
  public final int render(RenderContext rc, CameraContext cameraContext)
  {
	return Renderer.maxTimeToRender;
  }

  public final void onDown(EventContext eventContext, TouchEvent touchEvent, CameraContext cameraContext)
  {
	// compute globe point where user tapped
	final Vector2D pixel = touchEvent.getTouch(0).getPos();
	Camera camera = cameraContext.getNextCamera();
	final Vector3D initialPoint = camera.pixel2PlanetPoint(pixel);
	if (initialPoint.isNan())
		return;
  
	// compute central point of view
	final Vector3D centerPoint = camera.getXYZCenterOfView();
  
	// compute drag parameters
	final Vector3D axis = initialPoint.cross(centerPoint);
	final Angle angle = Angle.fromRadians(- IMathUtils.instance().asin(axis.length()/initialPoint.length()/centerPoint.length()));
  
	// compute zoom factor
	final double height = eventContext.getPlanet().toGeodetic3D(camera.getCartesianPosition()).height();
	final double distance = height * 0.6;
  
	// create effect
	Effect effect = new DoubleTapEffect(TimeInterval.fromSeconds(0.75), axis, angle, distance);
  
	eventContext.getEffectsScheduler().startEffect(effect, cameraContext);
  }
  public final void onMove(EventContext eventContext, TouchEvent touchEvent, CameraContext cameraContext)
  {
  }
  public final void onUp(EventContext eventContext, TouchEvent touchEvent, CameraContext cameraContext)
  {
  }

}