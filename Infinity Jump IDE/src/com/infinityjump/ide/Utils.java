package com.infinityjump.ide;

import com.infinityjump.core.game.base.Boundary;
import com.infinityjump.core.game.base.quad.QuadShape;

public class Utils {

	public static double getX(double position, double width) {
		return (position + 1.0d) * 0.5d * width;
	}
	
	public static double getY(double position, double height) {
		return (1.0d - position) * 0.5d * height;
	}
	
	public static float clamp(float value, float low, float high) {
		return value < low? low : value > high? high : value;
	}
	
	public static double clamp(double value, double low, double high) {
		return value < low? low : value > high? high : value;
	}
	
	public static boolean pressed(QuadShape quad, double mouseX, double mouseY) {
		return quad.getLeft().doubleValue() <= mouseX
				&& quad.getRight().doubleValue() >= mouseX
				&& quad.getBottom().doubleValue() <= mouseY
				&& quad.getTop().doubleValue() >= mouseY;
	}
	
	public static boolean boundaryPressed(Boundary boundary, double mouseX, double mouseY) {
		return boundary.getLeft().doubleValue() > mouseX
				|| boundary.getRight().doubleValue() < mouseX
				|| boundary.getBottom().doubleValue() > mouseY
				|| boundary.getTop().doubleValue() < mouseY;
	}
}
