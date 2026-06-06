
package com.example.demo;

import java.time.LocalDateTime;
import jakarta.persistence.*;

@Entity
@Table(name="images")
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String filename;
    private String uniquename;
    private long size;
    private String uploader;
    private String type;
    private LocalDateTime uploadTime;

    // Constructors
    public Image() {}

    public Image(String filename, String uniquename, int size, String uploader, String type) {
        this.filename = filename;
        this.uniquename = uniquename;
        this.size = size;
        this.uploader = uploader;
        this.type = type;
        this.uploadTime = LocalDateTime.now();
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

    public LocalDateTime getUploadTime() { return uploadTime; }
    public void setUploadTime(LocalDateTime uploadTime) { this.uploadTime = uploadTime; }
}