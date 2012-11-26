package org.glob3.mobile.generated; 
//
//  GL.cpp
//  Glob3 Mobile
//
//  Created by Agustín Trujillo Pino on 02/05/11.
//  Copyright 2011 Universidad de Las Palmas. All rights reserved.
//


//
//  GL.hpp
//  Glob3 Mobile
//
//  Created by Agustín Trujillo Pino on 14/06/11.
//  Copyright 2011 Universidad de Las Palmas. All rights reserved.
//







//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class IGLProgramId;
//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class IGLUniformID;


//C++ TO JAVA CONVERTER NOTE: The following #define macro was replaced in-line:
//#define VERBOSE true

public class GL
{
  private final INativeGL _gl;

  private MutableMatrix44D _modelView = new MutableMatrix44D();

  // stack of ModelView matrices
  private java.util.LinkedList<MutableMatrix44D> _matrixStack = new java.util.LinkedList<MutableMatrix44D>();

  private final java.util.LinkedList<IGLTextureId> _texturesIdBag = new java.util.LinkedList<IGLTextureId>();
  private int _texturesIdAllocationCounter;
//  long                        _texturesIdGetCounter;
//  long                        _texturesIdTakeCounter;

  // state handling
  private boolean _enableTextures;
  private boolean _enableTexture2D;
  private boolean _enableVertexColor;
  private boolean _enableVerticesPosition;
  private boolean _enableFlatColor;
  private boolean _enableDepthTest;
  private boolean _enableBlend;

  private boolean _enableCullFace;

  private int _cullFace_face;

  private float _scaleX;
  private float _scaleY;
  private float _translationX;
  private float _translationY;

  private IFloatBuffer _vertices;
  private int _verticesTimestamp;
  private IFloatBuffer _textureCoordinates;
  private int _textureCoordinatesTimestamp;
  private IFloatBuffer _colors;
  private int _colorsTimestamp;

  private float _flatColorR;
  private float _flatColorG;
  private float _flatColorB;
  private float _flatColorA;
  private float _flatColorIntensity;

  private void loadModelView()
  {
	if (true)
		ILogger.instance().logInfo("GL::loadModelView()");
  
  ///#ifdef C_CODE
  //  float* M = _modelView.getColumnMajorFloatArray();
  ///#else
  //  float[] M = _modelView.getColumnMajorFloatArray();
  ///#endif
  
  //  _gl->uniformMatrix4fv(Uniforms.Modelview, 1, false, M);
  //  _gl->uniformMatrix4fv(Uniforms.Modelview,
  //                        false,
  //                        _modelView.getColumnMajorFloatBuffer());
  
	_gl.uniformMatrix4fv(GlobalMembersGL.Uniforms.Modelview, false, _modelView);
  }

  private IGLTextureId getGLTextureId()
  {
	if (true)
		ILogger.instance().logInfo("GL::getGLTextureId()");
  
	if (_texturesIdBag.size() == 0)
	{
	  final int bugdetSize = 256;
  
	  ILogger.instance().logInfo("= Creating %d texturesIds...", bugdetSize);
  
	  final java.util.ArrayList<IGLTextureId> ids = _gl.genTextures(bugdetSize);
  
	  for (int i = 0; i < bugdetSize; i++)
	  {
		//      _texturesIdBag.push_back(ids[i]);
		_texturesIdBag.addFirst(ids.get(i));
	  }
  
	  _texturesIdAllocationCounter += bugdetSize;
  
	  ILogger.instance().logInfo("= Created %d texturesIds (accumulated %d).", bugdetSize, _texturesIdAllocationCounter);
	}
  
  //  _texturesIdGetCounter++;
  
	final IGLTextureId result = _texturesIdBag.getLast();
	_texturesIdBag.removeLast();
  
	//  printf("   - Assigning 1 texturesId (#%d) from bag (bag size=%ld). Gets:%ld, Takes:%ld, Delta:%ld.\n",
	//         result.getGLTextureId(),
	//         _texturesIdBag.size(),
	//         _texturesIdGetCounter,
	//         _texturesIdTakeCounter,
	//         _texturesIdGetCounter - _texturesIdTakeCounter);
  
	return result;
  }

//  int _lastTextureWidth;
//  int _lastTextureHeight;
///#ifdef C_CODE
//  unsigned char* _lastImageData;
///#endif
///#ifdef JAVA_CODE
//  byte[] _lastImageData;
///#endif

  //Get Locations warning of errors
  private boolean _errorGettingLocationOcurred;
  private int checkedGetAttribLocation(IGLProgramId program, String name)
  {
	if (true)
		ILogger.instance().logInfo("GL::checkedGetAttribLocation()");
  
	int l = _gl.getAttribLocation(program, name);
	if (l == -1)
	{
	  ILogger.instance().logError("Error fetching Attribute, Program = %d, Variable = %s", program, name);
	  _errorGettingLocationOcurred = true;
	}
	return l;
  }
  private IGLUniformID checkedGetUniformLocation(IGLProgramId program, String name)
  {
	if (true)
		ILogger.instance().logInfo("GL::checkedGetUniformLocation()");
  
	IGLUniformID uID = _gl.getUniformLocation(program, name);
	if (!uID.isValid())
	{
	  ILogger.instance().logError("Error fetching Uniform, Program = %d, Variable = %s", program, name);
	  _errorGettingLocationOcurred = true;
	}
	return uID;
  }

  private IFloatBuffer _billboardTexCoord;
  private IFloatBuffer getBillboardTexCoord()
  {
	if (true)
		ILogger.instance().logInfo("GL::getBillboardTexCoord()");
  
	if (_billboardTexCoord == null)
	{
	  FloatBufferBuilderFromCartesian2D texCoor = new FloatBufferBuilderFromCartesian2D();
	  texCoor.add(1,1);
	  texCoor.add(1,0);
	  texCoor.add(0,1);
	  texCoor.add(0,0);
	  _billboardTexCoord = texCoor.create();
	}
  
	return _billboardTexCoord;
  }

//  void info(const std::string x, ...) const {
//    va_list args;
//    va_start (args, x);
//    ILogger::instance()->logInfo(x, args);
//    va_end(args);
//  }


  public GL(INativeGL gl)
//  _enableFlatColor(false),
//  _texturesIdGetCounter(0),
//  _texturesIdTakeCounter(0),
  {
	  _gl = gl;
	  _enableTextures = false;
	  _enableTexture2D = false;
	  _enableVertexColor = false;
	  _enableVerticesPosition = false;
	  _enableBlend = false;
	  _enableDepthTest = false;
	  _enableCullFace = false;
	  _cullFace_face = GLCullFace.back();
	  _texturesIdAllocationCounter = 0;
	  _scaleX = 1F;
	  _scaleY = 1F;
	  _translationX = 0F;
	  _translationY = 0F;
	  _vertices = null;
	  _verticesTimestamp = 0;
	  _textureCoordinates = null;
	  _textureCoordinatesTimestamp = 0;
	  _colors = null;
	  _colorsTimestamp = 0;
	  _flatColorR = 0F;
	  _flatColorG = 0F;
	  _flatColorB = 0F;
	  _flatColorA = 0F;
	  _flatColorIntensity = 0F;
	  _billboardTexCoord = null;
	//Init Constants
	GLCullFace.init(gl);
	GLBufferType.init(gl);
	GLFeature.init(gl);
	GLType.init(gl);
	GLPrimitive.init(gl);
	GLBlendFactor.init(gl);
	GLTextureType.init(gl);
	GLTextureParameter.init(gl);
	GLTextureParameterValue.init(gl);
	GLAlignment.init(gl);
	GLFormat.init(gl);
	GLVariable.init(gl);
	GLError.init(gl);
  }

  public final void enableVerticesPosition()
  {
	if (true)
		ILogger.instance().logInfo("GL::enableVerticesPosition()");
  
	if (!_enableVerticesPosition)
	{
	  _gl.enableVertexAttribArray(GlobalMembersGL.Attributes.Position);
	  _enableVerticesPosition = true;
	}
  }


  // state handling
  public final void enableTextures()
  {
	if (true)
		ILogger.instance().logInfo("GL::enableTextures()");
  
	if (!_enableTextures)
	{
	  _gl.enableVertexAttribArray(GlobalMembersGL.Attributes.TextureCoord);
	  _enableTextures = true;
	}
  }

  public final void enableTexture2D()
  {
	if (true)
		ILogger.instance().logInfo("GL::enableTexture2D()");
  
	if (!_enableTexture2D)
	{
	  _gl.uniform1i(GlobalMembersGL.Uniforms.EnableTexture, 1);
	  _enableTexture2D = true;
	}
  }

  public final void enableVertexFlatColor(float r, float g, float b, float a, float intensity)
  {
	if (true)
		ILogger.instance().logInfo("GL::enableVertexFlatColor()");
  
	if (!_enableFlatColor)
	{
	  _gl.uniform1i(GlobalMembersGL.Uniforms.EnableFlatColor, 1);
	  _enableFlatColor = true;
	}
  
	color(r, g, b, a);
  
	//  _gl->uniform1f(Uniforms.FlatColorIntensity, intensity);
	if (_flatColorIntensity != intensity)
	{
	  _gl.uniform1f(GlobalMembersGL.Uniforms.FlatColorIntensity, intensity);
	  _flatColorIntensity = intensity;
	}
  }

  public final void disableVertexFlatColor()
  {
	if (true)
		ILogger.instance().logInfo("GL::disableVertexFlatColor()");
  
	if (_enableFlatColor)
	{
	  _gl.uniform1i(GlobalMembersGL.Uniforms.EnableFlatColor, 0);
	  _enableFlatColor = false;
	}
  }

  public final void disableTexture2D()
  {
	if (true)
		ILogger.instance().logInfo("GL::disableTexture2D()");
  
	if (_enableTexture2D)
	{
	  _gl.uniform1i(GlobalMembersGL.Uniforms.EnableTexture, 0);
	  _enableTexture2D = false;
	}
  }

  public final void disableVerticesPosition()
  {
	if (true)
		ILogger.instance().logInfo("GL::disableVerticesPosition()");
  
	if (_enableVerticesPosition)
	{
	  _gl.disableVertexAttribArray(GlobalMembersGL.Attributes.Position);
	  _enableVerticesPosition = false;
	}
  }

  public final void disableTextures()
  {
	if (true)
		ILogger.instance().logInfo("GL::disableTextures()");
  
	if (_enableTextures)
	{
	  _gl.disableVertexAttribArray(GlobalMembersGL.Attributes.TextureCoord);
	  _enableTextures = false;
	}
  }

  public final void clearScreen(float r, float g, float b, float a)
  {
	if (true)
		ILogger.instance().logInfo("GL::clearScreen()");
  
	_gl.clearColor(r, g, b, a);
	_gl.clear(GLBufferType.colorBuffer() | GLBufferType.depthBuffer());
  }

  public final void color(float r, float g, float b, float a)
  {
	if (true)
		ILogger.instance().logInfo("GL::color()");
  
	if ((_flatColorR != r) || (_flatColorG != g) || (_flatColorB != b) || (_flatColorA != a))
	{
	  _gl.uniform4f(GlobalMembersGL.Uniforms.FlatColor, r, g, b, a);
  
	  _flatColorR = r;
	  _flatColorG = g;
	  _flatColorB = b;
	  _flatColorA = a;
	}
  }

  public final void enableVertexColor(IFloatBuffer colors, float intensity)
  {
	if (true)
		ILogger.instance().logInfo("GL::enableVertexColor()");
  
	if (!_enableVertexColor)
	{
	  _gl.uniform1i(GlobalMembersGL.Uniforms.EnableColorPerVertex, 1);
	  _gl.enableVertexAttribArray(GlobalMembersGL.Attributes.Color);
	  _enableVertexColor = true;
	}
  
	if ((_colors != colors) || (_colorsTimestamp != colors.timestamp()))
	{
	  _gl.vertexAttribPointer(GlobalMembersGL.Attributes.Color, 4, false, 0, colors);
	  _colors = colors;
	  _colorsTimestamp = _colors.timestamp();
	}
  
	_gl.uniform1f(GlobalMembersGL.Uniforms.ColorPerVertexIntensity, intensity);
  }

  public final void disableVertexColor()
  {
	if (true)
		ILogger.instance().logInfo("GL::disableVertexColor()");
  
	if (_enableVertexColor)
	{
	  _gl.disableVertexAttribArray(GlobalMembersGL.Attributes.Color);
	  _gl.uniform1i(GlobalMembersGL.Uniforms.EnableColorPerVertex, 0);
	  _enableVertexColor = false;
	}
  }

  public final void pushMatrix()
  {
	if (true)
		ILogger.instance().logInfo("GL::pushMatrix()");
  
	_matrixStack.addLast(_modelView);
  }

  public final void popMatrix()
  {
	if (true)
		ILogger.instance().logInfo("GL::popMatrix()");
  
	_modelView = _matrixStack.getLast();
	_matrixStack.removeLast();
  
	loadModelView();
  }

  public final void loadMatrixf(MutableMatrix44D modelView)
  {
	if (true)
		ILogger.instance().logInfo("GL::loadMatrixf()");
  
	_modelView = modelView;
  
	loadModelView();
  }

  public final void multMatrixf(MutableMatrix44D m)
  {
	if (true)
		ILogger.instance().logInfo("GL::multMatrixf()");
  
	_modelView = _modelView.multiply(m);
  
	loadModelView();
  }

  public final void vertexPointer(int size, int stride, IFloatBuffer vertices)
  {
	if (true)
		ILogger.instance().logInfo("GL::vertexPointer()");
  
	if ((_vertices != vertices) || (_verticesTimestamp != vertices.timestamp()))
	{
	  _gl.vertexAttribPointer(GlobalMembersGL.Attributes.Position, size, false, stride, vertices);
	  _vertices = vertices;
	  _verticesTimestamp = _vertices.timestamp();
	}
  }

  public final void drawTriangles(IIntBuffer indices)
  {
	if (true)
		ILogger.instance().logInfo("GL::drawTriangles(%s)", indices.description());
  
	_gl.drawElements(GLPrimitive.triangles(), indices.size(), indices);
  }

  public final void drawTriangleStrip(IIntBuffer indices)
  {
	if (true)
		ILogger.instance().logInfo("GL::drawTriangleStrip(%s)", indices.description());
  
	_gl.drawElements(GLPrimitive.triangleStrip(), indices.size(), indices);
  }

  public final void drawTriangleFan(IIntBuffer indices)
  {
	if (true)
		ILogger.instance().logInfo("GL::drawTriangleFan(%s)", indices.description());
  
	_gl.drawElements(GLPrimitive.triangleFan(), indices.size(), indices);
  }

  public final void drawLines(IIntBuffer indices)
  {
	if (true)
		ILogger.instance().logInfo("GL::drawLines(%s)", indices.description());
  
	_gl.drawElements(GLPrimitive.lines(), indices.size(), indices);
  }

  public final void drawLineStrip(IIntBuffer indices)
  {
	if (true)
		ILogger.instance().logInfo("GL::drawLineStrip(%s)", indices.description());
  
	_gl.drawElements(GLPrimitive.lineStrip(), indices.size(), indices);
  }

  public final void drawLineLoop(IIntBuffer indices)
  {
	if (true)
		ILogger.instance().logInfo("GL::drawLineLoop(%s)", indices.description());
  
	_gl.drawElements(GLPrimitive.lineLoop(), indices.size(), indices);
  }

  public final void drawPoints(IIntBuffer indices)
  {
	if (true)
		ILogger.instance().logInfo("GL::drawPoints(%s)", indices.description());
  
	_gl.drawElements(GLPrimitive.points(), indices.size(), indices);
  }

  public final void setProjection(MutableMatrix44D projection)
  {
	if (true)
		ILogger.instance().logInfo("GL::setProjection()");
  
  ///#ifdef C_CODE
  //  float* M = projection.getColumnMajorFloatArray();
  ///#else
  //  float[] M = projection.getColumnMajorFloatArray();
  ///#endif
  //  _gl->uniformMatrix4fv(Uniforms.Projection, 1, false, M);
  
  //  _gl->uniformMatrix4fv(Uniforms.Projection,
  //                        false,
  //                        projection.getColumnMajorFloatBuffer());
  
	_gl.uniformMatrix4fv(GlobalMembersGL.Uniforms.Projection, false, projection);
  }

  public final boolean useProgram(IGLProgramId program)
  {
	if (true)
		ILogger.instance().logInfo("GL::useProgram()");
  
	// set shaders
	_gl.useProgram(program);
  
	//Methods checkedGetAttribLocation and checkedGetUniformLocation
	//will turn _errorGettingLocationOcurred to true is that happens
	_errorGettingLocationOcurred = false;
  
	// Extract the handles to attributes
	GlobalMembersGL.Attributes.Position = checkedGetAttribLocation(program, "Position");
	GlobalMembersGL.Attributes.TextureCoord = checkedGetAttribLocation(program, "TextureCoord");
	GlobalMembersGL.Attributes.Color = checkedGetAttribLocation(program, "Color");
  
	GlobalMembersGL.Uniforms.deleteUniformsIDs(); //DELETING
  
	// Extract the handles to uniforms
	GlobalMembersGL.Uniforms.Projection = checkedGetUniformLocation(program, "Projection");
	GlobalMembersGL.Uniforms.Modelview = checkedGetUniformLocation(program, "Modelview");
	GlobalMembersGL.Uniforms.Sampler = checkedGetUniformLocation(program, "Sampler");
	GlobalMembersGL.Uniforms.EnableTexture = checkedGetUniformLocation(program, "EnableTexture");
	GlobalMembersGL.Uniforms.FlatColor = checkedGetUniformLocation(program, "FlatColor");
	GlobalMembersGL.Uniforms.TranslationTexCoord = checkedGetUniformLocation(program, "TranslationTexCoord");
	GlobalMembersGL.Uniforms.ScaleTexCoord = checkedGetUniformLocation(program, "ScaleTexCoord");
	GlobalMembersGL.Uniforms.PointSize = checkedGetUniformLocation(program, "PointSize");
  
	// default values
	_gl.uniform2f(GlobalMembersGL.Uniforms.ScaleTexCoord, _scaleX, _scaleY);
	_gl.uniform2f(GlobalMembersGL.Uniforms.TranslationTexCoord, _translationX, _translationY);
	_gl.uniform1f(GlobalMembersGL.Uniforms.PointSize, 1);
  
	//BILLBOARDS
	GlobalMembersGL.Uniforms.BillBoard = checkedGetUniformLocation(program, "BillBoard");
	GlobalMembersGL.Uniforms.ViewPortRatio = checkedGetUniformLocation(program, "ViewPortRatio");
	_gl.uniform1i(GlobalMembersGL.Uniforms.BillBoard, 0); //NOT DRAWING BILLBOARD
  
	//FOR FLAT COLOR MIXING
	GlobalMembersGL.Uniforms.FlatColorIntensity = checkedGetUniformLocation(program, "FlatColorIntensity");
	GlobalMembersGL.Uniforms.ColorPerVertexIntensity = checkedGetUniformLocation(program, "ColorPerVertexIntensity");
	GlobalMembersGL.Uniforms.EnableColorPerVertex = checkedGetUniformLocation(program, "EnableColorPerVertex");
	GlobalMembersGL.Uniforms.EnableFlatColor = checkedGetUniformLocation(program, "EnableFlatColor");
  
	//Return
	return !_errorGettingLocationOcurred;
  }

  public final void enablePolygonOffset(float factor, float units)
  {
	if (true)
		ILogger.instance().logInfo("GL::enablePolygonOffset()");
  
	_gl.enable(GLFeature.polygonOffsetFill());
	_gl.polygonOffset(factor, units);
  }

  public final void disablePolygonOffset()
  {
	if (true)
		ILogger.instance().logInfo("GL::disablePolygonOffset()");
  
	_gl.disable(GLFeature.polygonOffsetFill());
  }

  public final void lineWidth(float width)
  {
	if (true)
		ILogger.instance().logInfo("GL::lineWidth()");
  
	_gl.lineWidth(width);
  }

  public final void pointSize(float size)
  {
	if (true)
		ILogger.instance().logInfo("GL::pointSize()");
  
	_gl.uniform1f(GlobalMembersGL.Uniforms.PointSize, size);
  }

  public final int getError()
  {
	if (true)
		ILogger.instance().logInfo("GL::getError()()");
  
	return _gl.getError();
  }

  public final IGLTextureId uploadTexture(IImage image, int format, boolean generateMipmap)
  {
	if (true)
		ILogger.instance().logInfo("GL::uploadTexture()");
  
	final IGLTextureId texId = getGLTextureId();
	if (texId != null)
	{
  
	  _gl.blendFunc(GLBlendFactor.srcAlpha(), GLBlendFactor.oneMinusSrcAlpha());
	  _gl.pixelStorei(GLAlignment.unpack(), 1);
  
	  _gl.bindTexture(GLTextureType.texture2D(), texId);
	  _gl.texParameteri(GLTextureType.texture2D(), GLTextureParameter.minFilter(), GLTextureParameterValue.linear());
	  _gl.texParameteri(GLTextureType.texture2D(), GLTextureParameter.magFilter(), GLTextureParameterValue.linear());
	  _gl.texParameteri(GLTextureType.texture2D(), GLTextureParameter.wrapS(), GLTextureParameterValue.clampToEdge());
	  _gl.texParameteri(GLTextureType.texture2D(), GLTextureParameter.wrapT(), GLTextureParameterValue.clampToEdge());
	  _gl.texImage2D(image, format);
  
	  if (generateMipmap)
	  {
		_gl.generateMipmap(GLTextureType.texture2D());
	  }
	}
	else
	{
	  ILogger.instance().logError("can't get a valid texture id\n");
	  return null;
	}
  
	return texId;
  }

  //  const const GLTextureId*uploadTexture(const IImage* image,
  //                                  int textureWidth, int textureHeight,
  //                                  bool generateMipmap);

  public final void setTextureCoordinates(int size, int stride, IFloatBuffer textureCoordinates)
  {
	if (true)
		ILogger.instance().logInfo("GL::setTextureCoordinates()");
  
	if ((_textureCoordinates != textureCoordinates) || (_textureCoordinatesTimestamp != textureCoordinates.timestamp()))
	{
	  _gl.vertexAttribPointer(GlobalMembersGL.Attributes.TextureCoord, size, false, stride, textureCoordinates);
	  _textureCoordinates = textureCoordinates;
	  _textureCoordinatesTimestamp = _textureCoordinates.timestamp();
	}
  }

  public final void bindTexture(IGLTextureId textureId)
  {
	if (true)
		ILogger.instance().logInfo("GL::bindTexture()");
  
	_gl.bindTexture(GLTextureType.texture2D(), textureId);
  }

  public final void enableDepthTest()
  {
	if (true)
		ILogger.instance().logInfo("GL::enableDepthTest()");
  
	if (!_enableDepthTest)
	{
	  _gl.enable(GLFeature.depthTest());
	  _enableDepthTest = true;
	}
  }
  public final void disableDepthTest()
  {
	if (true)
		ILogger.instance().logInfo("GL::disableDepthTest()");
  
	if (_enableDepthTest)
	{
	  _gl.disable(GLFeature.depthTest());
	  _enableDepthTest = false;
	}
  }

  public final void enableBlend()
  {
	if (true)
		ILogger.instance().logInfo("GL::enableBlend()");
  
	if (!_enableBlend)
	{
	  _gl.enable(GLFeature.blend());
	  _enableBlend = true;
	}
  }
  public final void disableBlend()
  {
	if (true)
		ILogger.instance().logInfo("GL::disableBlend()");
  
	if (_enableBlend)
	{
	  _gl.disable(GLFeature.blend());
	  _enableBlend = false;
	}
  
  }

  public final void drawBillBoard(IGLTextureId textureId, IFloatBuffer vertices, float viewPortRatio)
  {
	if (true)
		ILogger.instance().logInfo("GL::drawBillBoard()");
  
	int TODO_refactor_billboard;
  
	_gl.uniform1i(GlobalMembersGL.Uniforms.BillBoard, 1);
  
	_gl.uniform1f(GlobalMembersGL.Uniforms.ViewPortRatio, viewPortRatio);
  
	disableDepthTest();
  
	enableTexture2D();
	color(1, 1, 1, 1);
  
	bindTexture(textureId);
  
	vertexPointer(3, 0, vertices);
	setTextureCoordinates(2, 0, getBillboardTexCoord());
  
	_gl.drawArrays(GLPrimitive.triangleStrip(), 0, vertices.size() / 3);
  
	enableDepthTest();
  
	_gl.uniform1i(GlobalMembersGL.Uniforms.BillBoard, 0);
  }

  public final void deleteTexture(IGLTextureId texture)
  {
	if (true)
		ILogger.instance().logInfo("GL::deleteTexture()");
  
	if (texture != null)
	{
	  if (_gl.deleteTexture(texture))
	  {
		_texturesIdBag.addLast(texture);
	  }
  
  //    _texturesIdTakeCounter++;
	}
  }

  public final void enableCullFace(int face)
  {
	if (true)
		ILogger.instance().logInfo("GL::enableCullFace()");
  
	if (!_enableCullFace)
	{
	  _gl.enable(GLFeature.cullFace());
	  _enableCullFace = true;
	}
  
	if (_cullFace_face != face)
	{
	  _gl.cullFace(face);
	  _cullFace_face = face;
	}
  }
  public final void disableCullFace()
  {
	if (true)
		ILogger.instance().logInfo("GL::disableCullFace()");
  
	if (_enableCullFace)
	{
	  _gl.disable(GLFeature.cullFace());
	  _enableCullFace = false;
	}
  }

  public final void transformTexCoords(float scaleX, float scaleY, float translationX, float translationY)
  {
	if (true)
		ILogger.instance().logInfo("GL::transformTexCoords()");
  
	if ((_scaleX != scaleX) || (_scaleY != scaleY))
	{
	  _gl.uniform2f(GlobalMembersGL.Uniforms.ScaleTexCoord, scaleX, scaleY);
	  _scaleX = scaleX;
	  _scaleY = scaleY;
	}
  
	if ((_translationX != translationX) || (_translationY != translationY))
	{
	  _gl.uniform2f(GlobalMembersGL.Uniforms.TranslationTexCoord, translationX, translationY);
	  _translationX = translationX;
	  _translationY = translationY;
	}
  }

  public final void transformTexCoords(double scaleX, double scaleY, double translationX, double translationY)
  {
	if (true)
		ILogger.instance().logInfo("GL::transformTexCoords()");

	transformTexCoords((float) scaleX, (float) scaleY, (float) translationX, (float) translationY);
  }

  public final void transformTexCoords(Vector2D scale, Vector2D translation)
  {
	if (true)
		ILogger.instance().logInfo("GL::transformTexCoords()");

	transformTexCoords((float) scale._x, (float) scale._y, (float) translation._x, (float) translation._y);
  }

  public final void transformTexCoords(MutableVector2D scale, MutableVector2D translation)
  {
	if (true)
		ILogger.instance().logInfo("GL::transformTexCoords()");

	transformTexCoords((float) scale.x(), (float) scale.y(), (float) translation.x(), (float) translation.y());
  }


  public final void color(Color col)
  {
	if (true)
		ILogger.instance().logInfo("GL::color()");

	color(col.getRed(), col.getGreen(), col.getBlue(), col.getAlpha());
  }

  public final void clearScreen(Color col)
  {
	if (true)
		ILogger.instance().logInfo("GL::clearScreen()");

	clearScreen(col.getRed(), col.getGreen(), col.getBlue(), col.getAlpha());
  }

  public final void enableVertexFlatColor(Color c, float intensity)
  {
	if (true)
		ILogger.instance().logInfo("GL::enableVertexFlatColor()");

	enableVertexFlatColor(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha(), intensity);
  }

  public final void setBlendFuncSrcAlpha()
  {
	if (true)
		ILogger.instance().logInfo("GL::setBlendFuncSrcAlpha()");
  
	_gl.blendFunc(GLBlendFactor.srcAlpha(), GLBlendFactor.oneMinusSrcAlpha());
  }

  public final void getViewport(int[] v)
  {
	if (true)
		ILogger.instance().logInfo("GL::getViewport()");

	_gl.getIntegerv(GLVariable.viewport(), v);
  }

  public void dispose()
  {

//    if (_lastImageData != NULL) {
//      delete [] _lastImageData;
//      _lastImageData = NULL;
//    }

	if (_vertices != null)
		_vertices.dispose();
	if (_textureCoordinates != null)
		_textureCoordinates.dispose();
	if (_colors != null)
		_colors.dispose();

  }

}