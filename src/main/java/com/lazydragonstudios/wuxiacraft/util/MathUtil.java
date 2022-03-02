package com.lazydragonstudios.wuxiacraft.util;

import java.math.BigDecimal;

public class MathUtil {

	/**
	 * Make sure the value is between min and max, if not, get either min or max
	 *
	 * @param value the value to be tested
	 * @param min   the minimum value
	 * @param max   the maximum value
	 * @return the clamped value
	 */
	public static double clamp(double value, double min, double max) {
		return Math.max(Math.min(max, value), min);
	}

	public static boolean between(double value, double min, double max) {
		return between(value, min, max, true);
	}

	public static boolean between(double value, double min, double max, boolean inclusive) {
		if (inclusive) return min <= value && value <= max;
		return min < value && value < max;
	}

	public static boolean inBounds(double x, double y, double minX, double minY, double width, double height) {
		return between(x, minX, minX + width) && between(y, minY, minY + height);
	}

	/**
	 * Finds the common logarithm (base 10) fot the passed value
	 *
	 * @param value value to be applied logarithm to
	 * @return the logarithm in the base 10 for value
	 */
	public static BigDecimal log10(BigDecimal value) {
		if (value.compareTo(BigDecimal.ZERO) <= 0)
			return BigDecimal.ZERO;//should've been undefined, but let's make it like this to avoid exceptions
		//if the value can successfully be converted to double
		if (value.compareTo(new BigDecimal(Double.MAX_VALUE)) < 0) {
			//we use the java Math log10
			return BigDecimal.valueOf(Math.log10(value.doubleValue()));
		}
		//TODO make it return a more precise number
		//this just basically mean to get a very imprecise power of ten, since that for very large numbers, it might not matter the imprecise
		BigDecimal engineering = new BigDecimal(value.toEngineeringString());
		return new BigDecimal(engineering.scale() * -1 + engineering.precision() - 1);
	}
}
