package com.infinityjump.game.impl;

import java.nio.ByteBuffer;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import com.infinityjump.core.api.OpenGL.OpenGLAPI;

public class OpenGLAPIImpl implements OpenGLAPI {

	@Override
	public int genVertexArray() {
		return GL30.glGenVertexArrays();
	}

	@Override
	public void genBuffers(int n, int[] dest) {
		GL15.glGenBuffers(dest);
	}

	@Override
	public void deleteVertexArray(int id) {
		GL30.glDeleteVertexArrays(id);
	}

	@Override
	public void deleteBuffers(int n, int[] ids) {
		GL15.glDeleteBuffers(ids);
	}

	@Override
	public void bindVertexArray(int id) {
		GL30.glBindVertexArray(id);
	}

	@Override
	public void bindArrayBuffer(int id) {
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, id);
	}

	@Override
	public void bindElementArrayBuffer(int id) {
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, id);
	}

	@Override
	public void arrayBufferData(int id, ByteBuffer data) {
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, data, GL15.GL_STATIC_DRAW);
	}

	@Override
	public void elementArrayBufferData(int id, ByteBuffer data) {
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, data, GL15.GL_STATIC_DRAW);
	}

	@Override
	public void vertexAttribPointerFloat(int index, int size) {
		GL20.glVertexAttribPointer(index, size, GL11.GL_FLOAT, false, 0, 0l);
	}

	@Override
	public void enableVertexAttribArray(int index) {
		GL20.glEnableVertexAttribArray(index);
	}

	@Override
	public void drawElements(int count) {
		GL11.glDrawElements(GL11.GL_TRIANGLES, count, GL11.GL_UNSIGNED_BYTE, 0l);
	}

	@Override
	public void clearColor(float r, float g, float b, float a) {
		GL11.glClearColor(r, g, b, a);
	}
	
	@Override
	public void clear() {
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
	}
	
	@Override
	public int createProgram() {
		return GL20.glCreateProgram();
	}

	@Override
	public int createVertexShader() {
		return GL20.glCreateShader(GL20.GL_VERTEX_SHADER);
	}

	@Override
	public int createFragmentShader() {
		return GL20.glCreateShader(GL20.GL_FRAGMENT_SHADER);
	}

	@Override
	public void deleteProgram(int id) {
		GL20.glDeleteProgram(id);
	}

	@Override
	public void deleteShader(int id) {
		GL20.glDeleteShader(id);
	}

	@Override
	public void shaderSource(int id, String source) {
		GL20.glShaderSource(id, source);
	}

	@Override
	public boolean compile(int id) {
		GL20.glCompileShader(id);
		
		return GL20.glGetShaderi(id, GL20.GL_COMPILE_STATUS) == GL20.GL_TRUE;
	}

	@Override
	public String getShaderInfoLog(int id) {
		return GL20.glGetShaderInfoLog(id);
	}

	@Override
	public void attachShader(int program, int shader) {
		GL20.glAttachShader(program, shader);
	}

	@Override
	public void detachShader(int program, int shader) {
		GL20.glDetachShader(program, shader);
	}

	@Override
	public boolean linkAndValidate(int id) {
		GL20.glLinkProgram(id);
		GL20.glValidateProgram(id);
		
		return GL20.glGetProgrami(id, GL20.GL_LINK_STATUS) == GL20.GL_TRUE && GL20.glGetProgrami(id, GL20.GL_VALIDATE_STATUS) == GL20.GL_TRUE;
	}

	@Override
	public String getProgramInfoLog(int id) {
		return GL20.glGetProgramInfoLog(id);
	}

	@Override
	public int getUniformLocation(int id, String uniform) {
		return GL20.glGetUniformLocation(id, uniform);
	}

	@Override
	public void setUniform(int location, float v) {
		GL20.glUniform1f(location, v);
	}

	@Override
	public void setUniform(int location, float v1, float v2) {
		GL20.glUniform2f(location, v1, v2);
	}

	@Override
	public void setUniform(int location, float v1, float v2, float v3) {
		GL20.glUniform3f(location, v1, v2, v3);
	}

	@Override
	public void setUniform(int location, float v1, float v2, float v3, float v4) {
		GL20.glUniform4f(location, v1, v2, v3, v4);
	}

	@Override
	public void setUniformMat4(int location, float[] v) {
		GL20.glUniformMatrix4fv(location, false, v);
	}

	@Override
	public void useProgram(int id) {
		GL20.glUseProgram(id);
	}
}
