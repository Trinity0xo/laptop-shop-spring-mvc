package com.laptopstore.ecommerce.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileService {
    String uploadFile(MultipartFile file, String targetFolder);
    void deleteFile(String fileName, String targetFolder);
}
