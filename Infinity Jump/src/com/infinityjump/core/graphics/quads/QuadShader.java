package com.infinityjump.core.graphics.quads;

import java.io.IOException;
import java.io.InputStream;

import com.infinityjump.core.api.Logger;
import com.infinityjump.core.graphics.Shader;

public class QuadShader extends Shader {

	private static InputStream vertexSource, fragmentSource;
	
	public static void init(InputStream vertexSource, InputStream fragmentSource) {
		QuadShader.vertexSource = vertexSource;
		QuadShader.fragmentSource = fragmentSource;
	}
	
	public static void terminate() {
		try {
			vertexSource.close();
			fragmentSource.close();
		} catch (IOException e) {
			Logger.getAPI().error("Error closing resources for QuadShader");
		}
	}
	
	private int offsetLocation;
	private int scaleLocation;
	private int aspectRatioLocation;
	private int colorLocation;
	private int shiftXLocation;
	
	public QuadShader() {
		super(vertexSource, fragmentSource);
	}

	@Override
	protected void getAllUniformLocations() {
		offsetLocation = getUniformLocation("offset");
		scaleLocation = getUniformLocation("scale");
		
		aspectRatioLocation = getUniformLocation("aspectRatio");
		
		colorLocation = getUniformLocation("color");
		
		shiftXLocation = getUniformLocation("shiftX");
	}
	
	public void setOffset(float x, float y) {
		setUniformVec2(offsetLocation, x, y);
	}
	
	public void setScale(float sx, float sy) {
		setUniformVec2(scaleLocation, sx, sy);
	}
	
	public void setAspectRatio(float ratio) {
		setUniformFloat(aspectRatioLocation, ratio);
	}
	
	public void setColor(float r, float g, float b, float a) {
		setUniformVec4(colorLocation, r, g, b, a);
	}
	
	public void setShiftX(float shiftX) {
		setUniformFloat(shiftXLocation, shiftX);
	}
}
