package com.laptopstore.ecommerce.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class SortFieldsUtils {
    public static List<String> getValidSortFields(Class<?> sortClass){
        List<String> fieldNames = new ArrayList<>();
        Field[] fields = sortClass.getDeclaredFields();

        for (Field field : fields) {
            fieldNames.add(field.getName());
        }

        return fieldNames;
    }

    public static String getValidSortField(Class<?> sortClass, String sortName){
        Field[] fields = sortClass.getDeclaredFields();

        if (sortName == null || sortName.trim().isEmpty()) {
            return fields[0].getName();
        }

        for (Field field : fields) {
            if(sortName.trim().equalsIgnoreCase(field.getName())){
                return field.getName();
            }
        }

        return fields[0].getName();
    }
}
