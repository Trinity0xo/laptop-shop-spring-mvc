package com.laptopstore.ecommerce.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class UploadFoldersService {
    @Value("${upload.product-pictures.folder}")
    private String productPicturesFolderName;

    @Value("${upload.category-pictures.folder}")
    private String categoryPicturesFolderName;

    @Value("${upload.avatars.folder}")
    private String avatarsFolderName;

    public String handleGetProductPicturesFolderName() {
        return productPicturesFolderName;
    }

    public String handleGetCategoryPicturesFolderName() {
        return categoryPicturesFolderName;
    }

    public String handleGetAvatarsFolderName() {
        return avatarsFolderName;
    }
}
