package com.laptopstore.ecommerce.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class SortFields {
    public static List<String> getValidSortFields(Class<?> sortClass){
        List<String> fieldNames = new ArrayList<>();
        Field[] fields = sortClass.getDeclaredFields();

        for (Field field : fields) {
            fieldNames.add(field.getName());
        }

        return fieldNames;
    }
}
