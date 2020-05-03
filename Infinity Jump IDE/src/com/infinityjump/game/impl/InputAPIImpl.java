package com.infinityjump.game.impl;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

import com.infinityjump.core.api.Input;
import com.infinityjump.core.api.Input.InputAPI;

public class InputAPIImpl implements InputAPI {

	private float aspectRatio;
	
	private int mx, my;
	private long time;
	private DragEvent dragEvent;
	
	public InputAPIImpl(long window) {
		GLFW.glfwSetWindowSizeCallback(window, (handle, width, height) -> {
			aspectRatio = (float) width / height;
			
			GL11.glViewport(0, 0, width, height);
		});
		
		int[] width = new int[1];
		int[] height = new int[1];
		
		GLFW.glfwGetWindowSize(window, width, height);
		
		aspectRatio = (float) width[0] / height[0];
		
		GLFW.glfwSetMouseButtonCallback(window, (handle, button, action, mods) -> {
			if (action == GLFW.GLFW_PRESS) {
				double[] nmx = new double[1];
				double[] nmy = new double[1];
				
				GLFW.glfwGetCursorPos(window, nmx, nmy);
				
				mx = (int)nmx[0];
				my = (int)nmy[0];
				
				time = System.nanoTime();
			} else if (action == GLFW.GLFW_RELEASE) {
				long nTime = System.nanoTime();
				double dt = (nTime - time) * 1e-9;
				
				double[] nmx = new double[1];
				double[] nmy = new double[1];
				
				GLFW.glfwGetCursorPos(window, nmx, nmy);
				
				dragEvent = new DragEvent(mx, my, (int)nmx[0], (int)nmy[0], dt);
			}
		});
		
		Input.minThresholdDrag = 100;
		Input.maxThresholdDragTime = 0.2;
		
		Input.dragToSpeed = 0.007f;
	}
	
	@Override
	public DragEvent getDragEvent() {
		DragEvent temp = dragEvent;
		dragEvent = null;
		return temp;
	}

	@Override
	public float getAspectRatio() {
		return aspectRatio;
	}
}
