package com.example.demo;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;



//REPO = BD STUFF

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {
    List<Image> findByUploader(String username);
}