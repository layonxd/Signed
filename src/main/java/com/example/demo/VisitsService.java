package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;  // NEW: To get UserRepository
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;

import java.time.LocalDateTime;  // NEW: To track when users log in
import java.util.Optional;       // NEW: Wrapper that might contain a User or be empty

//import java.util.ArrayList;
import java.util.List;
//import java.util.stream.Collectors;

@Service
public class VisitsService {
    
    @Autowired
    private VisitsRepository visitsRepository;

    public boolean recordVisit(String uploader, String reciever) {
        Visits visit = new Visits(uploader, reciever, LocalDateTime.now());
        visitsRepository.save(visit);
        System.out.println("✅ Visit recorded! Uploader: " + uploader + ", Reciever: " + reciever);
        return true;
    }



    
}