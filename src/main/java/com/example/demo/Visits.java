package com.example.demo;


import java.time.LocalDateTime;

import jakarta.persistence.*;

//CLASS STUFF
    

@Entity
@Table(name="visits")
public class Visits {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String uploader;
    private String reciever;
    private LocalDateTime visitTime;

    // Constructors
    public Visits() {}

    public Visits(String uploader, String reciever, LocalDateTime visitTime) {
        this.uploader = uploader;
        this.reciever = reciever;
        this.visitTime = visitTime;
    }



    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUploader() { return uploader; }
    public void setUploader(String uploader) { this.uploader = uploader; }

    public String getReciever() { return reciever; }
    public void setReciever(String reciever) { this.reciever = reciever; }

    public LocalDateTime getVisitTime() { return visitTime; }
    public void setVisitTime(LocalDateTime visitTime) { this.visitTime = visitTime; }

}