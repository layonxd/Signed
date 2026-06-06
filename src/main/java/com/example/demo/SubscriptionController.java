package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;
//import java.util.Map;

@RestController
@RequestMapping("/subscriptions")
public class SubscriptionController {
    
    @Autowired
    private SubscriptionService subscriptionService;
    
    @Autowired
    private UserRepository userRepository;
    
    // Subscribe to a creator
    @PostMapping("/{creatorUsername}")
    public ResponseEntity<?> subscribe(@PathVariable String creatorUsername, 
                                       HttpSession session) {
        String subscriberUsername = (String) session.getAttribute("loggedInUser");
        if (subscriberUsername == null) {
            return ResponseEntity.status(401).body("Not logged in");
        }
        
        User subscriber = userRepository.findByUsername(subscriberUsername)
            .orElseThrow(() -> new RuntimeException("User not found"));
        
        try {
            boolean success = subscriptionService.subscribe(subscriber.getId(), creatorUsername);
            if (success) {
                return ResponseEntity.ok("Subscribed to " + creatorUsername);
            } else {
                return ResponseEntity.status(400).body("Already subscribed to " + creatorUsername);
            }
        } catch (RuntimeException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }
    
    // Unsubscribe from a creator
    @DeleteMapping("/{creatorUsername}")
    public ResponseEntity<?> unsubscribe(@PathVariable String creatorUsername, 
                                        HttpSession session) {
        String subscriberUsername = (String) session.getAttribute("loggedInUser");
        if (subscriberUsername == null) {
            return ResponseEntity.status(401).body("Not logged in");
        }
        
        User subscriber = userRepository.findByUsername(subscriberUsername)
            .orElseThrow(() -> new RuntimeException("User not found"));
        
        boolean success = subscriptionService.unsubscribe(subscriber.getId(), creatorUsername);
        
        if (success) {
            return ResponseEntity.ok("Unsubscribed from " + creatorUsername);
        } else {
            return ResponseEntity.status(400).body("Not subscribed to " + creatorUsername);
        }
    }
    
    // Get all creators I'm subscribed to
    @GetMapping("/my")
    public ResponseEntity<?> getMySubscriptions(HttpSession session) {
        String username = (String) session.getAttribute("loggedInUser");
        if (username == null) {
            return ResponseEntity.status(401).body("Not logged in");
        }
        
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("User not found"));
        
        return ResponseEntity.ok(subscriptionService.getSubscribedCreatorUsernames(user.getId()));
    }
}