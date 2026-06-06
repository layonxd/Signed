package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.nio.file.Files;
//import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collectors;
//import java.util.ArrayList;
import java.util.List;
//import java.util.stream.Collectors;

@RestController
@RequestMapping("/images")
public class ImageController {

    @Autowired 
    private ImageService imageService;

    @Autowired 
    private SubscriptionService subscriptionService;

    @Autowired  // ← ADD THIS
    private UserRepository userRepository;  // ← ADD THIS

    // PRODUCER: upload (must be logged in)
    @PostMapping("/upload")
    public ResponseEntity<?> upload(
            @RequestParam("file") MultipartFile file,
            HttpSession session) {

        String uploader = (String) session.getAttribute("loggedInUser");
        if (uploader == null) return ResponseEntity.status(401).body("Not logged in");

        User user = userRepository.findByUsername(uploader).orElse(null);
    if (user == null || !"CREATOR".equals(user.getRole())) {
        return ResponseEntity.status(403).body("Only creators can upload images");
    }
        try {
            Image saved = imageService.store(file, uploader);
            return ResponseEntity.ok(Map.of(
                "id", saved.getId(),
                "filename", saved.getFilename(),
                "uploadedAt", saved.getUploadTime()  // fixed: was getUploadedAt()
            ));
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Upload failed: " + e.getMessage());
        }
    }

    @GetMapping
public ResponseEntity<?> listImages(HttpSession session) {
    String username = (String) session.getAttribute("loggedInUser");
    if (username == null) {
        return ResponseEntity.status(401).body("Not logged in");
    }
    
    User currentUser = userRepository.findByUsername(username).orElse(null);
    if (currentUser == null) {
        return ResponseEntity.status(401).body("User not found");
    }
    
    List<Image> allImages = imageService.getAll();
    List<Image> accessibleImages;
    
    if ("CREATOR".equals(currentUser.getRole())) {
        // CREATORS see ONLY their own images
        accessibleImages = allImages.stream()
            .filter(img -> img.getUploader().equals(username))
            .collect(Collectors.toList());
    } else {
        // CONSUMERS see images from creators they subscribe to
        
        // Get list of creators this consumer subscribes to
        List<String> subscribedCreators = subscriptionService.getSubscribedCreatorUsernames(currentUser.getId());
        
        // Filter images: only show from subscribed creators
        accessibleImages = allImages.stream()
            .filter(img -> subscribedCreators.contains(img.getUploader()))
            .collect(Collectors.toList());
    }
    
    return ResponseEntity.ok(accessibleImages);
}




    // CONSUMER: view/download a specific image
    @GetMapping("/{id}")
    public ResponseEntity<Resource> serveImage(@PathVariable Long id) {
        try {
            Resource resource = imageService.load(id);
            String contentType = Files.probeContentType(resource.getFile().toPath());
            return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline")
                .body(resource);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}