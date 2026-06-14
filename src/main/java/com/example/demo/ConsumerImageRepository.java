package com.example.demo;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;



//REPO = BD STUFF

@Repository
public interface ConsumerImageRepository extends JpaRepository<ConsumerImage, Long> {
    List<ConsumerImage> findByUploader(String username);

    List<ConsumerImage> findByReciever(String username);
}