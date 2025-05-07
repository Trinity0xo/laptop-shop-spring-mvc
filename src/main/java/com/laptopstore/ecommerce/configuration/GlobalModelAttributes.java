package com.laptopstore.ecommerce.configuration;

import com.laptopstore.ecommerce.service.UploadFoldersService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalModelAttributes {
    @Value("${static.resources.mapping.folder}")
    private String resourcesMappingFolder;

    private final UploadFoldersService uploadFoldersService;

    public GlobalModelAttributes(UploadFoldersService uploadFoldersService) {
        this.uploadFoldersService = uploadFoldersService;
    }

    @ModelAttribute("productPicturesFolder")
    public String productPicturesFolder() {
        return uploadFoldersService.handleGetProductPicturesFolderName();
    }

    @ModelAttribute("categoryPicturesFolder")
    public String categoryPicturesFolder() {
        return uploadFoldersService.handleGetCategoryPicturesFolderName();
    }

    @ModelAttribute("avatarsFolder")
    public String avatarsFolder() {
        return uploadFoldersService.handleGetAvatarsFolderName();
    }

    @ModelAttribute("resourcesMappingFolder")
    public String resourcesMappingFolder() {
        return resourcesMappingFolder;
    }

    @ModelAttribute("currentPath")
    public String currentPath(HttpServletRequest request) {
        return request.getRequestURI();
    }
}
