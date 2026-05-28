package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.servlet.http.HttpSession;

@RestController  // Tells Spring: "This class handles HTTP requests"
@RequestMapping("/auth")  // All endpoints here start with /auth
public class UserController {
    // dependencies ?
    @Autowired
    private HttpSession session;

    @Autowired  
    private UserService userService;
    


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
        
        if (loggedInUser != null && (boolean) session.getAttribute("isLoggedIn")) {
            return ResponseEntity.ok(loggedInUser);
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
            loginRequest.getPassword()
        );
        
        if (success) {
            return ResponseEntity.ok("User registered successfully!");
        } else {
            return ResponseEntity.status(409)  // 409 Conflict
                .body("Username already exists");
        }
    }
}

