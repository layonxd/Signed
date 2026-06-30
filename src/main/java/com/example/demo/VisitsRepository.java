package com.example.demo;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;



//REPO = BD STUFF

@Repository
public interface VisitsRepository extends JpaRepository<Visits, Long> {
    List<Visits> findByUploader(String username);

    List<Visits> findByReciever(String username);

    List<Visits> findByRecieverAndUploader(String reciever, String uploader);


}