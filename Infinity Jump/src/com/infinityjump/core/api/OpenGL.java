package com.infinityjump.core.api;

import java.nio.ByteBuffer;

public final class OpenGL {

	public static interface OpenGLAPI {
		int genVertexArray();
		void genBuffers(int n, int[] dest);
		
		void deleteVertexArray(int id);
		void deleteBuffers(int n, int[] ids);
		
		void bindVertexArray(int id);
		void bindArrayBuffer(int id);
		void bindElementArrayBuffer(int id);
		
		void arrayBufferData(int id, ByteBuffer data);
		void elementArrayBufferData(int id, ByteBuffer data);
		
		void vertexAttribPointerFloat(int index, int size);
		
		void enableVertexAttribArray(int index);
		
		void drawElements(int count);
		
		void clearColor(float r, float g, float b, float a);
		void clear();
		
		int createProgram();
		int createVertexShader();
		int createFragmentShader();
		
		void deleteProgram(int id);
		void deleteShader(int id);
		
		void shaderSource(int id, String source);
		boolean compile(int id);
		
		String getShaderInfoLog(int id);
		
		void attachShader(int program, int shader);
		void detachShader(int program, int shader);
		
		boolean linkAndValidate(int id);
		
		String getProgramInfoLog(int id);
		
		int getUniformLocation(int id, String uniform);
		
		void setUniform(int location, float v);
		void setUniform(int location, float v1, float v2);
		void setUniform(int location, float v1, float v2, float v3);
		void setUniform(int location, float v1, float v2, float v3, float v4);
		
		void setUniformMat4(int location, float[] v);
		
		void useProgram(int id);
	}
	
	private static OpenGLAPI api;
	
	public static void init(OpenGLAPI api) {
		OpenGL.api = api;
	}
	
	public static OpenGLAPI getAPI() {
		return api;
	}
}
