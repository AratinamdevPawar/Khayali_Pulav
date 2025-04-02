package com.project.khayalipulao.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileUploadService {

    @Value("${file.upload.directory:src/main/resources/static/uploads/profiles}")
    private String uploadDir;

    public String saveProfileImage(MultipartFile file) throws IOException {
        // Create directories if they don't exist
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // Generate unique filename to prevent overwriting existing files
        String uniqueFileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        Path filePath = uploadPath.resolve(uniqueFileName);
        
        // Save the file
        Files.copy(file.getInputStream(), filePath);
        
        // Return the path that can be used in the HTML (relative to static folder)
        return "/uploads/profiles/" + uniqueFileName;
    }
    
    public boolean deleteFile(String filePath) {
        if (filePath == null || filePath.isEmpty()) {
            return false;
        }
        
        try {
            // Extract just the filename from the path
            String fileName = filePath.substring(filePath.lastIndexOf("/") + 1);
            Path path = Paths.get(uploadDir).resolve(fileName);
            return Files.deleteIfExists(path);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}