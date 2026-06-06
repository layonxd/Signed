package com.example.demo;

import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
    
    // Check if active subscription exists
    boolean existsBySubscriberIdAndCreatorIdAndActiveTrue(Long subscriberId, Long creatorId);
    
    // Find active subscription
    Optional<Subscription> findBySubscriberIdAndCreatorIdAndActiveTrue(Long subscriberId, Long creatorId);
    
    // Get all creators a user subscribes to
    List<Subscription> findBySubscriberIdAndActiveTrue(Long subscriberId);
    
    // Get all subscribers of a creator
    List<Subscription> findByCreatorIdAndActiveTrue(Long creatorId);
    
    // Delete all subscriptions for a user (when account is deleted)
    void deleteBySubscriberId(Long subscriberId);
    void deleteByCreatorId(Long creatorId);
}