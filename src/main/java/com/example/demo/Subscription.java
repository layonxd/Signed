package com.example.demo;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "subscriptions", 
       uniqueConstraints = @UniqueConstraint(columnNames = {"subscriber_id", "creator_id"}))
public class Subscription {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "subscriber_id", nullable = false)
    private User subscriber;  // The user who is following
    
    @ManyToOne
    @JoinColumn(name = "creator_id", nullable = false)
    private User creator;     // The creator being followed
    
    @Column(nullable = false)
    private LocalDateTime subscribedAt = LocalDateTime.now();
    
    @Column(nullable = false)
    private boolean active = true;
    
    // Constructors
    public Subscription() {}
    
    public Subscription(User subscriber, User creator) {
        this.subscriber = subscriber;
        this.creator = creator;
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public User getSubscriber() { return subscriber; }
    public void setSubscriber(User subscriber) { this.subscriber = subscriber; }
    
    public User getCreator() { return creator; }
    public void setCreator(User creator) { this.creator = creator; }
    
    public LocalDateTime getSubscribedAt() { return subscribedAt; }
    public void setSubscribedAt(LocalDateTime subscribedAt) { this.subscribedAt = subscribedAt; }
    
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
}