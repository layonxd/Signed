package com.example.demo;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.servlet.http.HttpSession;
//import java.util.ArrayList;
import java.util.List;
//import java.util.stream.Collectors;
import java.util.Map;
//import java.util.HashMap;  // Also add this if you need to create Maps

@RestController  // Tells Spring: "This class handles HTTP requests"
@RequestMapping("/auth")  // All endpoints here start with /auth
public class UserController {
    // dependencies ?
    @Autowired
    private HttpSession session;

    @Autowired  
    private UserService userService;
    
    @Autowired
    private UserRepository userRepository;

    // endpoints

    //login
    @PostMapping("/login")  
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        
        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();
        
        // Check creds 
        boolean isValid = userService.authenticate(username, password);
        


        if (isValid) {

            session.setAttribute("loggedInUser", username);
            session.setAttribute("isLoggedIn", true);
            // Return success
            return ResponseEntity.ok()
                .body("Login successful! Welcome " + username);
        } else {
            // Return failure
            return ResponseEntity.status(401)
                .body("Invalid username or password");
        }
    }
    @GetMapping("/check")
    public ResponseEntity<?> checkSession(HttpSession session) {
        String loggedInUser = (String) session.getAttribute("loggedInUser");
        Boolean isLoggedIn = (Boolean) session.getAttribute("isLoggedIn");
        
        if (loggedInUser != null && isLoggedIn != null && isLoggedIn) {
            // Get the user from database to fetch their role
            User user = userRepository.findByUsername(loggedInUser).orElse(null);
            String role = (user != null) ? user.getRole() : "CONSUMER";
            
            // Return JSON object with both username and role
            Map<String, String> userInfo = Map.of(
                "username", loggedInUser,
                "role", role
            );
            return ResponseEntity.ok(userInfo);
        } else {
            return ResponseEntity.status(401).body("Not logged in");
        }
    }
    
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok("Logged out successfully");
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody LoginRequest loginRequest) {
        boolean success = userService.register(
            loginRequest.getUsername(), 
            loginRequest.getPassword(),
            loginRequest.getRole()
        );
        
        if (success) {
            return ResponseEntity.ok("User registered successfully!");
        } else {
            return ResponseEntity.status(409)  // 409 Conflict
                .body("Username already exists");
        }
    }
    @GetMapping("/creators")
    public ResponseEntity<?> listCreators() {

        String currentUsername = (String) session.getAttribute("loggedInUser");
        if (currentUsername == null) {
            return ResponseEntity.status(401).body("Please login to view creators");
        }


        List<User> creators = userService.getAllCreators();
        // Return only safe information (exclude passwords)
        List<Map<String, String>> creatorInfo = creators.stream()
            .filter(user -> !user.getUsername().equals(currentUsername))
            .map(user -> Map.of(
                "username", user.getUsername(),
                "role", user.getRole()
            ))
            .collect(Collectors.toList());
        return ResponseEntity.ok(creatorInfo);
    }
    
    @GetMapping("/user/{username}")
public ResponseEntity<?> getUserByUsername(@PathVariable String username, HttpSession session) {
    String loggedInUser = (String) session.getAttribute("loggedInUser");
    
    // Optional: Only allow access if logged in
    if (loggedInUser == null) {
        return ResponseEntity.status(401).body("Not logged in");
    }
    
    User user = userRepository.findByUsername(username).orElse(null);
    if (user == null) {
        return ResponseEntity.status(404).body("User not found");
    }
    
    // Return safe user info (no password)
    Map<String, String> userInfo = Map.of(
        "username", user.getUsername(),
        "role", user.getRole()
    );
    
    return ResponseEntity.ok(userInfo);
}
}

