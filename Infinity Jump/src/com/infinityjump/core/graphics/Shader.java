package com.infinityjump.core.graphics;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.infinityjump.core.api.Logger;
import com.infinityjump.core.api.OpenGL;
import com.infinityjump.core.api.OpenGL.OpenGLAPI;

public abstract class Shader {

	private int programID;
	private int vertexID, fragmentID;
	
	public Shader(InputStream vertexSource, InputStream fragmentSource) {
		OpenGLAPI api = OpenGL.getAPI();
		
		programID = api.createProgram();
		
		vertexID = api.createVertexShader();
		fragmentID = api.createFragmentShader();
		
		StringBuilder vSource = new StringBuilder();
		StringBuilder fSource = new StringBuilder();
		
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(vertexSource))) {
			String line;
			
			while ((line = reader.readLine()) != null) {
				vSource.append(line).append('\n');
			}
		} catch (IOException e) {
			Logger.getAPI().error("Error loading vertex shader. Using default shader instead");
			
			vSource = new StringBuilder();
			vSource.append(
					  "#version 400 core\n\n"
					+ "void main() {}");
		}
		
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(fragmentSource))) {
			String line;
			
			while ((line = reader.readLine()) != null) {
				fSource.append(line).append('\n');
			}
		} catch (IOException e) {
			Logger.getAPI().error("Error loading fragment shader. Using default shader instead");
			
			fSource = new StringBuilder();
			fSource.append(
					  "#version 400 core\n\n"
					+ "void main() {}");
		}
		
		api.shaderSource(vertexID, vSource.toString());
		api.shaderSource(fragmentID, fSource.toString());
		
		if (!api.compile(vertexID)) {
			Logger.getAPI().error("Error compiling vertex shader:\n");
			Logger.getAPI().error(api.getShaderInfoLog(vertexID));
		}
		
		if (!api.compile(fragmentID)) {
			Logger.getAPI().error("Error compiling fragment shader:\n");
			Logger.getAPI().error(api.getShaderInfoLog(fragmentID));
		}
		
		api.attachShader(programID, vertexID);
		api.attachShader(programID, fragmentID);
		
		if (!api.linkAndValidate(programID)) {
			Logger.getAPI().error("Error linking or validating shader program:\n");
			Logger.getAPI().error(api.getProgramInfoLog(programID));
		}
		
		getAllUniformLocations();
		
		api.detachShader(programID, vertexID);
		api.detachShader(programID, fragmentID);
	}
	
	protected abstract void getAllUniformLocations();
	
	protected int getUniformLocation(String name) {
		return OpenGL.getAPI().getUniformLocation(programID, name);
	}
	
	protected void setUniformFloat(int location, float v) {
		OpenGL.getAPI().setUniform(location, v);
	}
	
	protected void setUniformVec2(int location, float v1, float v2) {
		OpenGL.getAPI().setUniform(location, v1, v2);
	}
	
	protected void setUniformVec3(int location, float v1, float v2, float v3) {
		OpenGL.getAPI().setUniform(location, v1, v2, v3);
	}
	
	protected void setUniformVec4(int location, float v1, float v2, float v3, float v4) {
		OpenGL.getAPI().setUniform(location, v1, v2, v3, v4);
	}
	
	protected void setUniformMat4(int location, float[] v) {
		OpenGL.getAPI().setUniformMat4(location, v);
	}
	
	public void bind() {
		OpenGL.getAPI().useProgram(programID);
	}
	
	public void destroy() {
		OpenGLAPI api = OpenGL.getAPI();
		
		api.deleteShader(vertexID);
		api.deleteShader(programID);
		
		api.deleteProgram(programID);
	}
}
