package com.infinityjump.core.graphics;

import java.nio.ByteBuffer;

import com.infinityjump.core.api.OpenGL;
import com.infinityjump.core.api.OpenGL.OpenGLAPI;

public class Model {

	private int vaoID;
	private int[] bufferIDs;
	private int indexBufferID;
	
	private int vertexCount;
	
	public Model(int numBuffers) {
		OpenGLAPI api = OpenGL.getAPI();
		
		vaoID = api.genVertexArray();
		
		bufferIDs = new int[numBuffers + 1];
		
		api.genBuffers(numBuffers + 1, bufferIDs);
		
		indexBufferID = bufferIDs[numBuffers];
		
		api.bindVertexArray(vaoID);
	}
	
	public void setBufferData(int buffer, int size, ByteBuffer data) {
		OpenGLAPI api = OpenGL.getAPI();
		
		api.bindArrayBuffer(bufferIDs[buffer]);
		api.enableVertexAttribArray(buffer);
		
		api.arrayBufferData(bufferIDs[buffer], data);
		
		api.vertexAttribPointerFloat(buffer, size);
	}
	
	public void setElementBufferData(ByteBuffer data, int count) {
		this.vertexCount = count;
		
		OpenGLAPI api = OpenGL.getAPI();
		
		api.bindElementArrayBuffer(indexBufferID);
		
		api.elementArrayBufferData(indexBufferID, data);
	}
	
	public void unbind() {
		OpenGLAPI api = OpenGL.getAPI();
		
		api.bindArrayBuffer(0);
		api.bindVertexArray(0);
	}
	
	public void draw() {
		OpenGLAPI api = OpenGL.getAPI();
		
		api.bindVertexArray(vaoID);
		
		api.drawElements(vertexCount);
	}
	
	public void destroy() {
		OpenGLAPI api = OpenGL.getAPI();
		
		api.deleteVertexArray(vaoID);
		
		api.deleteBuffers(bufferIDs.length, bufferIDs);
	}
}
