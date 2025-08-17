package com.laptopstore.ecommerce.util;

public class RatingUtils {
    public static double roundToNearestHalf(double value) {
        return Math.round(value * 2) / 2.0;
    }
}
