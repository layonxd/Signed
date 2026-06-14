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
public class ConsumerImageService {

    

    @Autowired
    private ConsumerImageRepository consumerImageRepository;


    
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
}   