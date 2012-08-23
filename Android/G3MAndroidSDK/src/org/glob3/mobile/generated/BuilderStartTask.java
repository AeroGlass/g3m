package org.glob3.mobile.generated; 
public class BuilderStartTask extends FrameTask
{
  private TileTextureBuilder _builder;

  public BuilderStartTask(TileTextureBuilder builder)
  {
	  _builder = builder;
	_builder._retain();
  }

  public void dispose()
  {
	_builder._release();
  }

  public final void execute(RenderContext rc)
  {
	_builder.start();
  }
}