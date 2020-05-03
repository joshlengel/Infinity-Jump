package com.infinityjump.core.api;

public final class Input {
	
	public static int minThresholdDrag;
	public static double maxThresholdDragTime;
	
	public static float dragToSpeed;

	public static interface InputAPI {
		
		public static class DragEvent {
			int sx, sy, ex, ey;
			int dx, dy;
			double time;
			
			public DragEvent(int sx, int sy, int ex, int ey, double time) {
				this.sx = sx;
				this.sy = sy;
				this.ex = ex;
				this.ey = ey;
				this.time = time;
				
				this.dx = ex - sx;
				this.dy = ey - sy;
			}
			
			public int getDX() {
				return dx;
			}
			
			public int getDY() {
				return dy;
			}
			
			public double getTime() {
				return time;
			}
		}
		
		DragEvent getDragEvent();
		
		float getAspectRatio();
	}
	
	private static InputAPI api;
	
	public static void init(InputAPI api) {
		Input.api = api;
	}
	
	public static InputAPI getAPI() {
		return api;
	}
}
