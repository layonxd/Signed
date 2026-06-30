package com.example.demo;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.ResponseEntity;
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


//do STUF WITH IMAGES 



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

        String contentType = file.getContentType();

        if (contentType == null) {
            System.out.println("DEBUG: File type INVALID - aborting upload");  // ← ADD THIS
            throw new IOException("Could not determine file type");
        }

        if ((!contentType.startsWith("image/"))&&(!contentType.startsWith("video/"))){
            System.out.println("DEBUG: File type INVALID - aborting upload");  // ← ADD THIS
            throw new IOException("Only image and video files are allowed");
        }

        System.out.println("DEBUG: File type VALID - continuing with upload");  // ← ADD THIS

        String storedFilename = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Files.copy(file.getInputStream(), uploadDir.resolve(storedFilename));

        Image image = new Image();
        image.setFilename(file.getOriginalFilename());
        image.setUniquename(storedFilename);
        image.setType(contentType);
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


    public void deleteImage(Long id) throws IOException {
    Image image = imageRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Image not found"));
    
    Path file = uploadDir.resolve(image.getUniquename());
    Files.deleteIfExists(file);
    imageRepository.deleteById(id);
    }


    public Image getImageById(Long id) {
        return imageRepository.findById(id).orElse(null);
    }

    public List<Image> getImageByuploader(String uploader) {
        return imageRepository.findByUploader(uploader);
    }

/* 
    @Autowired
    private SubscriptionService subscriptionService;

    public ConsumerImage Build(Image image, String reciever) {
        ConsumerImage ci = new ConsumerImage();
        ci.setFilename(image.getFilename());
        ci.setUniquename(image.getUniquename());
        ci.setSize(image.getSize());
        ci.setUploader(image.getUploader());
        ci.setType(image.getType());
        ci.setReciever(reciever);
        return ci;
    }

    public boolean Makechildren(Image image, String Uploader) {
        return true;
        
        List<String> subcribers = subscriptionService.getSubscribers(Uploader);
        for (String reciever : subcribers) {
            
            String filename=image.getUniquename();
            String receiver=reciever;
            Resource fileResource = load(image.getId());

            Image saved = store(fileResource, receiver);
            
            ConsumerImage ci = Build(image, reciever);
            fileResource.loadAsResource(filename);
            funcion watermark(fileResource, reciever);
            filresource.saveAs(filename,reciever);
            consumerImageRepository.save(ci);
            
        }
 */       
}