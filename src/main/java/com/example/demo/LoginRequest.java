package com.example.demo;

public class LoginRequest {
    private String username;
    private String password;
    private String role;  // ← NEW - "CREATOR" or "CONSUMER"
    
    // Getters (Spring needs these)
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() { 
        return role; 
    }  // ← NEW


    public void setRole(String role) {
         this.role = role; 
    }  // ← NEW
}