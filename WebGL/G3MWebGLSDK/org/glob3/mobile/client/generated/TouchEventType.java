package org.glob3.mobile.client.generated; 
public enum TouchEventType
{
  Down,
  Up,
  Move,
  LongPress;

	public int getValue()
	{
		return this.ordinal();
	}

	public static TouchEventType forValue(int value)
	{
		return values()[value];
	}
}