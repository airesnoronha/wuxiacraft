package wuxiacraft.util;

public class MathUtil {

	/**
	 * Make sure the value is between min and max, if not, get either min or max
	 * @param value the value to be tested
	 * @param min the minimum value
	 * @param max the maximum value
	 * @return the clamped value
	 */
	public static double clamp(double value, double min, double max) {
		return Math.max(Math.min(max, value), min);
	}

	public static boolean between(double value, double min, double max) {
		return between(value, min, max, true);
	}

	public static boolean between(double value, double min, double max, boolean inclusive) {
		if(inclusive) return min <= value && value <= max;
		return min < value && value < max;
	}

	public static boolean inBounds(double x, double y, double minX, double minY, double width, double height) {
		return between(x, minX, minX+width) && between(y, minY, minY+height);
	}
}
