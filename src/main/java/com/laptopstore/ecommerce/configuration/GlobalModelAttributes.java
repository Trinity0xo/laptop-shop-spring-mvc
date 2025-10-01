package com.laptopstore.ecommerce.configuration;

import com.laptopstore.ecommerce.dto.auth.AuthenticatedInformationDto;
import com.laptopstore.ecommerce.service.FolderService;
import com.laptopstore.ecommerce.util.AuthenticationUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalModelAttributes {
    @Value("${static.resources.mapping.folder}")
    private String resourcesMappingFolder;

    private final FolderService folderService;


    public GlobalModelAttributes(FolderService folderService) {
        this.folderService = folderService;
    }

    @ModelAttribute("productImagesFolder")
    public String productImagesFolder() {
        return folderService.getProductImagesFolderName();
    }

    @ModelAttribute("categoryImagesFolder")
    public String categoryImagesFolder() {
        return folderService.getCategoryImagesFolderName();
    }

    @ModelAttribute("avatarsFolder")
    public String avatarsFolder() {
        return folderService.getAvatarsFolderName();
    }

    @ModelAttribute("resourcesMappingFolder")
    public String resourcesMappingFolder() {
        return resourcesMappingFolder;
    }

    @ModelAttribute("currentPath")
    public String currentPath(HttpServletRequest request) {
        return request.getRequestURI();
    }

    @ModelAttribute("authenticatedInformation")
    public AuthenticatedInformationDto authenticatedInformation(){
        return AuthenticationUtils.getAuthenticatedInfo();
    }
}
