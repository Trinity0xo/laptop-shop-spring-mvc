package com.laptopstore.ecommerce.service.impl;

import com.laptopstore.ecommerce.service.FolderService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class FolderServiceImpl implements FolderService {
    @Value("${upload.product-pictures.folder}")
    private String productPicturesFolderName;

    @Value("${upload.category-pictures.folder}")
    private String categoryPicturesFolderName;

    @Value("${upload.avatars.folder}")
    private String avatarsFolderName;

    @Override
    public String getProductPicturesFolderName() {
        return productPicturesFolderName.trim();
    }

    @Override
    public String getCategoryPicturesFolderName() {
        return categoryPicturesFolderName.trim();
    }

    @Override
    public String getAvatarsFolderName() {
        return avatarsFolderName.trim();
    }
}
