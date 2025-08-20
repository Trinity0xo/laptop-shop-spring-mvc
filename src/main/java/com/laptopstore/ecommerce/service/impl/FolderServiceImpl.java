package com.laptopstore.ecommerce.service.impl;

import com.laptopstore.ecommerce.service.FolderService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class FolderServiceImpl implements FolderService {
    @Value("${upload.product-images.folder}")
    private String productImagesFolderName;

    @Value("${upload.category-images.folder}")
    private String categoryImagesFolderName;

    @Value("${upload.avatars.folder}")
    private String avatarsFolderName;

    @Override
    public String getProductImagesFolderName() {
        return productImagesFolderName.trim();
    }

    @Override
    public String getCategoryImagesFolderName() {
        return categoryImagesFolderName.trim();
    }

    @Override
    public String getAvatarsFolderName() {
        return avatarsFolderName.trim();
    }
}
