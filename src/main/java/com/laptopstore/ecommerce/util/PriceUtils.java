package com.laptopstore.ecommerce.util;

import java.util.Collections;
import java.util.Map;

public class PriceUtils {
    public static double parsePrice(String priceString, double defaultValue, double defaultMin, double defaultMax) {
        String numberString = TypeParseUtils.toNumberString(priceString);
        Double price = TypeParseUtils.parseStringDoubleOrDefault(numberString, null);
        if (price == null || price < defaultMin || price > defaultMax) {
            return defaultValue;
        }
        return price;
    }

    public static Map<String, Double> getValidPriceRange(double min, double max, double defaultMin, double defaultMax){
        if (min == defaultMin && max == defaultMax || max <= min) {
            return Collections.emptyMap();
        }

        return Map.of("min", min, "max", max);
    }
}
