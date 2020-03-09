package com.airesnor.wuxiacraft.utils;

public class MathUtils {

    public static boolean between(float value, float min, float max) {
        return between(value, min, max, true);
    }

    public static boolean between(float value, float min, float max, boolean inclusive) {
        if(inclusive) return min <= value && value <= max;
        else return min < value && value < max;
    }

    public static boolean between(int value, int min, int max) {
        return between(value, min, max, true);
    }

    public static boolean between(int value, int min, int max, boolean inclusive) {
        if(inclusive) return min <= value && value <= max;
        else return min < value && value < max;
    }

    public static boolean between(double value, double min, double max) {
        return between(value, min, max, true);
    }

    public static boolean between(double value, double min, double max, boolean inclusive) {
        if(inclusive) return min <= value && value <= max;
        else return min < value && value < max;
    }
}
