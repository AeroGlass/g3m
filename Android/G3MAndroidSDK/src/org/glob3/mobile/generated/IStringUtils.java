package org.glob3.mobile.generated; 
//
//  IStringUtils.cpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 23/08/12.
//
//

//
//  IStringUtils.hpp
//  G3MiOSSDK
//
//  Created by Diego Gomez Deck on 23/08/12.
//
//



public abstract class IStringUtils
{
  private static final IStringUtils _instance = null;

  public static void setInstance(IStringUtils instance)
  {
	if (_instance != null)
	{
	  System.out.print("Warning, IStringUtils instance set two times\n");
	}
	_instance = instance;
  }

  public static IStringUtils instance()
  {
	return _instance;
  }

  public void dispose()
  {

  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual String createString(byte data[], int length) const = 0;
  public abstract String createString(byte[] data, int length);


//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual java.util.ArrayList<String> splitLines(const String& String) const = 0;
  public abstract java.util.ArrayList<String> splitLines(String String);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual boolean beginsWith(const String& String, const String& prefix) const = 0;
  public abstract boolean beginsWith(String String, String prefix);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual int indexOf(const String& String, const String& search) const = 0;
  public abstract int indexOf(String String, String search);

  /*
   Returns a new string that is a substring of this string. The substring begins at the
   specified beginIndex and extends to the character at index endIndex - 1. Thus the length
   of the substring is endIndex-beginIndex.
   */
//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual String substring(const String& String, int beginIndex, int endIndex) const = 0;
  public abstract String substring(String String, int beginIndex, int endIndex);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual String substring(const String& String, int beginIndex) const
  public String substring(String String, int beginIndex)
  {
	return substring(String, beginIndex, String.length() + 1);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual String left(const String& String, int endIndex) const
  public String left(String String, int endIndex)
  {
	return substring(String, 0, endIndex);
  }

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual String rtrim(const String& String) const = 0;
  public abstract String rtrim(String String);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual String ltrim(const String& String) const = 0;
  public abstract String ltrim(String String);

//C++ TO JAVA CONVERTER WARNING: 'const' methods are not available in Java:
//ORIGINAL LINE: virtual String trim(const String& String) const
  public String trim(String String)
  {
	return rtrim(ltrim(String));
  }

}