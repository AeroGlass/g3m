package org.glob3.mobile.generated; 
///////////

public class GPUAttributeValueDisabled extends GPUAttributeValue
{
  public GPUAttributeValueDisabled()
  {
     super(false);
  }

  public final void setAttribute(GL gl, int id)
  {
  }

  public final boolean isEqualsTo(GPUAttributeValue v)
  {
    return (v.getEnabled() == false);
  }

  public final GPUAttributeValue shallowCopy()
  {
    return new GPUAttributeValueDisabled();
  }

  public final String description()
  {
    return "Attribute Disabled.";
  }

  public final GPUAttributeValue copyOrCreate(GPUAttributeValue oldAtt)
  {

    if (oldAtt == null)
    {
      return new GPUAttributeValueDisabled();
    }
    else
    {
      if (oldAtt.getEnabled())
      {
        if (oldAtt != null)
           oldAtt.dispose();
        return new GPUAttributeValueDisabled();
      }
      else
      {
        return oldAtt;
      }
    }

  }

}