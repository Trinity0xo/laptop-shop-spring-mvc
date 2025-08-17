package com.laptopstore.ecommerce.util;

import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

import static com.laptopstore.ecommerce.util.DateTimeUtils.ZONE_ID;

public class TypeParseUtils {
    public static String toNumberString(String value){
        if(value == null){
            return "";
        }
        return value.trim().replaceAll("\\D", "");
    }

    public static Integer parseStringIntegerOrDefault(String value, Integer defaultValue) {
        if (value == null || value.trim().isEmpty()) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    public static Long parseStringLongOrDefault(String value, Long defaultValue) {
        if (value == null || value.trim().isEmpty()) {
            return defaultValue;
        }
        try {
            return Long.parseLong(value.trim());
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    public static Double parseStringDoubleOrDefault(String value, Double defaultValue) {
        if (value == null || value.trim().isEmpty()) {
            return defaultValue;
        }
        try {
            return Double.parseDouble(value.trim());
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    public static Boolean parseStringBooleanOrDefault(String value, Boolean defaultValue) {
        if (value == null || value.trim().isEmpty()) {
            return defaultValue;
        }
        return Boolean.parseBoolean(value.trim());
    }

    public static <T extends Enum<T>> T parseStringEnumOrDefault(Class<T> enumClass, String value, T defaultValue) {
        if (value == null || enumClass == null || value.trim().isEmpty()) {
            return defaultValue;
        }
        for (T enumConstant : enumClass.getEnumConstants()) {
            if (enumConstant.name().equalsIgnoreCase(value.trim())) {
                return enumConstant;
            }
        }
        return defaultValue;
    }

    public static List<Integer> parseStringIntegerListOrEmpty(List<String> values){
        List<Integer> integerList = new ArrayList<>();

        if(values == null || values.isEmpty()){
            return integerList;
        }

        for (String value : values){
            Integer integer = parseStringIntegerOrDefault(value, null);
            if(integer != null){
                integerList.add(integer);
            }
        }

        return integerList;
    }

    public static Instant parseStringInstantOrNull(String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }

        try {
            LocalDate date = LocalDate.parse(value.trim(), DateTimeFormatter.ISO_LOCAL_DATE);
            return date.atStartOfDay(ZONE_ID).toInstant();
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    public static LocalDate parseStringLocalDateOrNull(String value) {
        try {
            if (value == null || value.trim().isEmpty()) {
                return null;
            }
            return LocalDate.parse(value.trim(), DateTimeFormatter.ISO_LOCAL_DATE);
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    public static LocalDate parseInstantLocalDateOrNull(Instant value) {
        if (value == null){
            return null;
        }
        try{
            return value.atZone(ZONE_ID).toLocalDate();
        }catch (DateTimeParseException e){
            return null;
        }
    }
}

