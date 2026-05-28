package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;  // NEW: To get UserRepository
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;

import java.time.LocalDateTime;  // NEW: To track when users log in
import java.util.Optional;       // NEW: Wrapper that might contain a User or be empty

@Service
public class UserService {
    
    //get/check user
    @Autowired
    private UserRepository userRepository;



     @PostConstruct
    public void createAdminAccount() {
        // Check if admin already exists
        if (!userRepository.existsByUsername("123")) {
            // Create admin account
            User admin = new User("123", "123");
            admin.setCreatedAt(LocalDateTime.now());
            userRepository.save(admin);
            System.out.println("✅ Admin account created! Username: 123, Password: 123");
        } else {
            System.out.println("ℹ️ Admin account already exists");
        }
    }



    // register new user
    public boolean register(String username, String password) {
        
        // NEW: Check if username already exists 
        if (userRepository.existsByUsername(username)) {
            return false;  
        }
        
        // make user
        User user = new User(username, password);
        
        // save to db
        userRepository.save(user);
        
        return true;
    }
    
    // auth
    public boolean authenticate(String username, String password) {
        
        //check if in db
        Optional<User> userOpt = userRepository.findByUsername(username);

        if (userOpt.isPresent()) {  // Does the Optional contain a User?
            
            // Get user
            User user = userOpt.get();
            
            // check passw
            if (user.getPassword().equals(password)) {
                
                // save last login
                user.setLastLogin(LocalDateTime.now());
                userRepository.save(user);  // Save the updated user back to database
                
                return true;
            }
        }
        
        // if no match 
        return false;
    }
}