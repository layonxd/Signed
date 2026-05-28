package com.example.demo;


import java.io.*;
import java.nio.file.*;
import java.util.*;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service  // Tells Spring: "This class does business logic"
public class UserService {



    @Value("${myfile}")
    private String FILE_PATH; 

    
    //private final String FILE_PATH = "users.txt";
    
    // This method reads the file and returns true if credentials match
    public boolean authenticate(String username, String password) {
        try {
            // Read all lines from the file
            List<String> lines = Files.readAllLines(Paths.get(FILE_PATH));
            
            // Loop through each line
            for (String line : lines) {
                // Split the line at the comma
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    String storedUser = parts[0];
                    String storedPass = parts[1];
                    
                    // Check if this line matches
                    if (storedUser.equals(username) && storedPass.equals(password)) {
                        return true;  // Found a match
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();  // Print error if file can't be read
        }
        
        return false;  // No match found
    }
}