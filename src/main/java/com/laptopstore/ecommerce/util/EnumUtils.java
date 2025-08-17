package com.laptopstore.ecommerce.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EnumUtils {
    public static boolean isInEnum(Enum<?> value, Class<? extends Enum<?>> enumClass) {
        if (value == null || enumClass == null) {
            return false;
        }

        for (Enum<?> constant : enumClass.getEnumConstants()) {
            if (constant.equals(value)) {
                return true;
            }
        }
        return false;
    }

    public static <T extends Enum<T>> List<T> getEnumValues(List<String> values, Class<T> enumClass) {
        if (values == null || enumClass == null || values.isEmpty()) {
            return Collections.emptyList();
        }

        List<T> enumList = new ArrayList<>();

        for (String value : values) {
            for (T enumConstant : enumClass.getEnumConstants()) {
                if (enumConstant.name().equalsIgnoreCase(value.trim())) {
                    enumList.add(enumConstant);
                }
            }
        }

        return enumList;
    }
}
