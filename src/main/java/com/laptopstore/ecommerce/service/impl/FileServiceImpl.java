package com.laptopstore.ecommerce.service.impl;

import com.laptopstore.ecommerce.service.FileService;
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
                System.out.println(">> Created new folder: " + targetPath);
            }else{
                System.out.println(">> Folder already exists, skip create");
            }

            Path filePath = targetPath.resolve(newFileName);
            Files.write(filePath, bytes);

            System.out.println(">> File uploaded to: " + filePath);

            return newFileName;
        } catch (IOException e) {
            System.out.println(">> Error when uploading file: " + e.getMessage());
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
                    System.out.println(">> File deleted: " + filePath);
                } else {
                    System.out.println(">> File not found: " + filePath);
                }
            }
        } catch (IOException e) {
            System.out.println(">> Error when deleting file: " + e.getMessage());
        }
    }
}
