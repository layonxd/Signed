package com.example.demo;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jakarta.annotation.PostConstruct;

import java.io.IOException;
//import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;


@Service
public class ImageService {

    

    @Autowired
    private ImageRepository imageRepository;

    private final Path uploadDir = Paths.get("uploads");

    @PostConstruct
    public void init() throws IOException {
        Files.createDirectories(uploadDir);
    }

    public Image store(MultipartFile file, String uploaderUsername) throws IOException {
        String storedFilename = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Files.copy(file.getInputStream(), uploadDir.resolve(storedFilename));

        Image image = new Image();
        image.setFilename(file.getOriginalFilename());
        image.setUniquename(storedFilename);
        image.setType(file.getContentType());
        image.setSize(file.getSize());
        image.setUploader(uploaderUsername);
        image.setUploadTime(LocalDateTime.now());

        return imageRepository.save(image);
    }

    public Resource load(Long imageId) throws IOException {
        Image image = imageRepository.findById(imageId)
            .orElseThrow(() -> new RuntimeException("Image not found"));

        Path file = uploadDir.resolve(image.getUniquename());
        Resource resource = new UrlResource(file.toUri());

        if (resource.exists() && resource.isReadable()) return resource;
        throw new RuntimeException("Could not read file");
    }

    public List<Image> getAll() {
        return imageRepository.findAll();
    }
}