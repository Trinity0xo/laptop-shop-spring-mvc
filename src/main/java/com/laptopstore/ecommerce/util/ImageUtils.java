package com.laptopstore.ecommerce.util;

import java.util.Set;

public class ImageUtils {
    public static final int MAX_IMAGES_SIZE = 10;
    private static final Set<String> ALLOWED_IMAGE_TYPES = Set.of(
            "image/jpg", "image/jpeg", "image/png", "image/gif", "image/webp"
    );

    public static boolean isValidImageType(String value){
        if(value == null || value.trim().isEmpty()){
            return false;
        }
        for (String imageType: ALLOWED_IMAGE_TYPES){
            if(value.equalsIgnoreCase(imageType)){
                return true;
            }
        }
        return false;
    }
}
