package com.laptopstore.ecommerce.service.impl;

import com.laptopstore.ecommerce.service.FileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

@Service
public class FileServiceImpl implements FileService {
    private static final Logger log = LoggerFactory.getLogger(FileServiceImpl.class);

    @Value("${files.path}")
    private String filesPath;

    @Value("${static.resources.mapping.folder}")
    private String resourcesMappingFolder;

    @Override
    public String uploadFile(MultipartFile file, String targetFolder) {
        try {
            byte[] bytes = file.getBytes();

            String fileType = Objects.requireNonNull(file.getContentType()).split("/")[1];
            String newFileName = targetFolder + "-" + System.currentTimeMillis() + "." + fileType;

            Path basePath = Paths.get(filesPath).resolve(resourcesMappingFolder);
            Path targetPath = basePath.resolve(targetFolder);

            if (Files.notExists(targetPath)) {
                Files.createDirectories(targetPath);
                log.info("Created new folder: {}", targetPath);
            }else{
                log.debug("Folder already exists, skipping create: {}", targetPath);
            }

            Path filePath = targetPath.resolve(newFileName);
            Files.write(filePath, bytes);

            log.info("File uploaded to: {}", filePath);

            return newFileName;
        } catch (IOException e) {
            log.error("Error uploading file {} to folder {}", file.getOriginalFilename(), targetFolder, e);
            return null;
        }
    }

    @Override
    public void deleteFile(String fileName, String targetFolder) {
        try {
            if(fileName != null && !fileName.isEmpty()){
                Path basePath = Paths.get(filesPath).resolve(resourcesMappingFolder);
                Path targetPath = basePath.resolve(targetFolder);
                Path filePath = targetPath.resolve(fileName);

                if (Files.exists(filePath)) {
                    Files.delete(filePath);
                    log.info("File deleted: {}", filePath);
                } else {
                    log.warn("File not found for deletion: {}", filePath);
                }
            }
        } catch (IOException e) {
            log.error("Error deleting file {} in folder {}", fileName, targetFolder, e);
        }
    }
}
