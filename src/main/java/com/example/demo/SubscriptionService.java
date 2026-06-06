package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Optional;

@Service
public class SubscriptionService {
    
    @Autowired
    private SubscriptionRepository subscriptionRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    // Subscribe to a creator
    @Transactional
    public boolean subscribe(Long subscriberId, String creatorUsername) {
        User subscriber = userRepository.findById(subscriberId)
            .orElseThrow(() -> new RuntimeException("Subscriber not found"));
        
        User creator = userRepository.findByUsername(creatorUsername)
            .orElseThrow(() -> new RuntimeException("Creator not found"));
        
        // Check if creator is actually a creator
        if (!"CREATOR".equals(creator.getRole())) {
            throw new RuntimeException("Can only subscribe to CREATOR accounts");
        }
        
        // Check if already subscribed
        if (subscriptionRepository.existsBySubscriberIdAndCreatorIdAndActiveTrue(subscriberId, creator.getId())) {
            return false; // Already subscribed
        }
        
        Subscription subscription = new Subscription(subscriber, creator);
        subscriptionRepository.save(subscription);
        return true;
    }
    
    // Unsubscribe
    @Transactional
    public boolean unsubscribe(Long subscriberId, String creatorUsername) {
        User creator = userRepository.findByUsername(creatorUsername)
            .orElseThrow(() -> new RuntimeException("Creator not found"));
        
        Optional<Subscription> subscription = subscriptionRepository
            .findBySubscriberIdAndCreatorIdAndActiveTrue(subscriberId, creator.getId());
        
        if (subscription.isPresent()) {
            subscription.get().setActive(false);
            subscriptionRepository.save(subscription.get());
            return true;
        }
        return false;
    }
    
    // Get all creators a user subscribes to
    public List<String> getSubscribedCreatorUsernames(Long subscriberId) {
        return subscriptionRepository.findBySubscriberIdAndActiveTrue(subscriberId)
            .stream()
            .map(sub -> sub.getCreator().getUsername())
            .collect(Collectors.toList());
    }
    
    // Check if user is subscribed to a creator
    public boolean isSubscribed(Long subscriberId, String creatorUsername) {
        User creator = userRepository.findByUsername(creatorUsername)
            .orElse(null);
        if (creator == null) return false;
        
        return subscriptionRepository.existsBySubscriberIdAndCreatorIdAndActiveTrue(subscriberId, creator.getId());
    }
}