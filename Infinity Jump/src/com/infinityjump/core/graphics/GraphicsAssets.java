package com.infinityjump.core.graphics;

import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import com.infinityjump.core.api.Logger;
import com.infinityjump.core.state.StateMachine;

public class GraphicsAssets {

	public static Model quadModel;
	public static QuadShader quadShader;
	
	private static boolean assetsLoaded = false;
	
	public static void init(InputStream vertexSource, InputStream fragmentSource) {
		ByteBuffer vBuffer = ByteBuffer.allocateDirect(Float.BYTES * 2 * 4).order(ByteOrder.nativeOrder());
		vBuffer.putFloat(-1.0f);
		vBuffer.putFloat(-1.0f);
		
		vBuffer.putFloat( 1.0f);
		vBuffer.putFloat(-1.0f);
		
		vBuffer.putFloat(-1.0f);
		vBuffer.putFloat( 1.0f);
		
		vBuffer.putFloat( 1.0f);
		vBuffer.putFloat( 1.0f);
		
		vBuffer.position(0);
		
		ByteBuffer iBuffer = ByteBuffer.allocateDirect(6).order(ByteOrder.nativeOrder());
		iBuffer.put((byte)0);
		iBuffer.put((byte)1);
		iBuffer.put((byte)3);
		
		iBuffer.put((byte)0);
		iBuffer.put((byte)3);
		iBuffer.put((byte)2);
		
		iBuffer.position(0);
	
		quadModel = new Model(1);
		quadModel.setBufferData(0, 2, vBuffer);
		quadModel.setElementBufferData(iBuffer, 6);
		quadModel.unbind();
		
		QuadShader.init(vertexSource, fragmentSource);
		quadShader = new QuadShader();
		
		assetsLoaded = true;
	}
	
	public static void terminate() {
		quadShader.destroy();
		QuadShader.terminate();
		
		quadModel.destroy();
	}
	
	public static void assertLoaded() {
		if (!assetsLoaded) {
			Logger.getAPI().error("Assert failed: Graphics assets unloaded");
			StateMachine.terminate();
		}
	}
}
