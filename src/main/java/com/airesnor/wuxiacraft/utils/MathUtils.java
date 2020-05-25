package com.airesnor.wuxiacraft.utils;

public class MathUtils {

	public static boolean between(float value, float min, float max) {
		return between(value, min, max, true);
	}

	public static boolean between(float value, float min, float max, boolean inclusive) {
		if (inclusive) return min <= value && value <= max;
		else return min < value && value < max;
	}

	public static boolean between(int value, int min, int max) {
		return between(value, min, max, true);
	}

	public static boolean between(int value, int min, int max, boolean inclusive) {
		if (inclusive) return min <= value && value <= max;
		else return min < value && value < max;
	}

	public static boolean between(double value, double min, double max) {
		return between(value, min, max, true);
	}

	public static boolean between(double value, double min, double max, boolean inclusive) {
		if (inclusive) return min <= value && value <= max;
		else return min < value && value < max;
	}

	public static float clamp(float value, float min, float max) {
		value = Math.max(min, value);
		value = Math.min(value, max);
		return value;
	}

	public static int clamp(int value, int min, int max) {
		value = Math.max(min, value);
		value = Math.min(value, max);
		return value;
	}

	public static double clamp(double value, double min, double max) {
		value = Math.max(min, value);
		value = Math.min(value, max);
		return value;
	}

	public static boolean inGroup(int value, int... group) {
		boolean found = false;
		for(int test : group) {
			if(test == value) {
				found = true;
				break;
			}
		}
		return found;
	}
}
