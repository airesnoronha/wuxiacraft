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
}
