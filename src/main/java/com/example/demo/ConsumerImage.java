
package com.example.demo;

import java.time.LocalDateTime;
import jakarta.persistence.*;

//CLASS STUFF


@Entity
@Table(name="images")
public class ConsumerImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String filename;
    private String uniquename;
    private long size;
    private String uploader;
    private String reciever;
    private String type;

    // Constructors
    public ConsumerImage() {}

    public ConsumerImage(String filename, String uniquename, long size, String uploader, String reciever, String type) {
        this.filename = filename;
        this.uniquename = uniquename;
        this.size = size;
        this.uploader = uploader;
        this.reciever = reciever;
        this.type = type;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFilename() { return filename; }
    public void setFilename(String filename) { this.filename = filename; }

    public String getUniquename() { return uniquename; }
    public void setUniquename(String uniquename) { this.uniquename = uniquename; }

    public long getSize() { return size; }
    public void setSize(long size) { this.size = size; }

    public String getUploader() { return uploader; }
    public void setUploader(String uploader) { this.uploader = uploader; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }


    public String getReciever() { return reciever; }
    public void setReciever(String reciever) { this.reciever = reciever; }
}