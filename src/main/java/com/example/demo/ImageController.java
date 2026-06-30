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



//CONTROLER=API ENDPOINTS

@RestController
@RequestMapping("/images")
public class ImageController {

    @Autowired 
    private ImageService imageService;

    @Autowired 
    private SubscriptionService subscriptionService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ImageRepository imageRepository;




    @Autowired
    private VisitsService visitsService;

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
            "uploadedAt", saved.getUploadTime()
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
        List<String> subscribedCreators = subscriptionService.getSubscribedCreatorUsernames(currentUser.getId());
        
       
        
        accessibleImages = allImages.stream()
            .filter(img -> subscribedCreators.contains(img.getUploader()))
            .collect(Collectors.toList());

        
            
        return ResponseEntity.ok(accessibleImages);
    }


    @GetMapping("/self")
    public ResponseEntity<?> selfImages(HttpSession session) {
        String username = (String) session.getAttribute("loggedInUser");
        if (username == null) {
            return ResponseEntity.status(401).body("Not logged in");
        }
    
        User currentUser = userRepository.findByUsername(username).orElse(null);
        if (currentUser == null) {
            return ResponseEntity.status(401).body("User not found");
        }
        
        List<Image> accessibleImages;


            
        accessibleImages = imageRepository.findByUploader(username);
        
        return ResponseEntity.ok(accessibleImages);
    }




    // CONSUMER: view/download a specific image
    @GetMapping("/{id}")
    public ResponseEntity<?> serveImage(@PathVariable Long id, HttpSession session) {
        String username = (String) session.getAttribute("loggedInUser");
        if (username == null) return ResponseEntity.status(401).body("Not logged in");

        try {
            User user = userRepository.findByUsername(username).orElse(null);
            if (user == null) return ResponseEntity.status(401).body("User not found");

            Image image = imageService.getImageById(id);
            if (image == null) {
                return ResponseEntity.status(404).body("Image not found");
                           }

            // NEW: Check if user has access
        boolean hasAccess = false;
        
        // Creator always has access to their own images
        if ("CREATOR".equals(user.getRole()) && image.getUploader().equals(username)) {
            hasAccess = true;
        } else {
            // Check if user is subscribed to the creator
            hasAccess = subscriptionService.isSubscribed(user.getId(), image.getUploader());
        }

        if (!hasAccess) {
            return ResponseEntity.status(403).body("You need to subscribe to this creator to view this image");
        }

        // Serve the original image directly (no watermark)
        Resource resource = imageService.load(id);
        String contentType = Files.probeContentType(resource.getFile().toPath());
        if (contentType == null) {
            contentType = "application/octet-stream";
        }
        
        return ResponseEntity.ok()
            .contentType(MediaType.parseMediaType(contentType))
            .header(HttpHeaders.CONTENT_DISPOSITION, "inline")
            .body(resource);

    } catch (Exception e) {
        return ResponseEntity.status(500).body("Could not serve file: " + e.getMessage());
    }
}
            

    @DeleteMapping("/{id}")
public ResponseEntity<?> deleteImage(@PathVariable Long id, HttpSession session) {
    String username = (String) session.getAttribute("loggedInUser");
    if (username == null) {
        return ResponseEntity.status(401).body("Not logged in");
    }
    
    Image image = imageService.getImageById(id);
    if (image == null) {
        return ResponseEntity.status(404).body("Image not found");
    }
    
    // Only the uploader can delete
    if (!image.getUploader().equals(username)) {
        return ResponseEntity.status(403).body("You can only delete your own images");
    }
    
    try {
        imageService.deleteImage(id);
        return ResponseEntity.ok("Image deleted successfully");
    } catch (Exception e) {
        return ResponseEntity.status(500).body("Failed to delete image");
    }
}
}