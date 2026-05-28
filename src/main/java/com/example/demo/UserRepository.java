package com.example.demo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    // Spring Data JPA automatically implements this method!
    Optional<User> findByUsername(String username);
    
    // Check if a username already exists
    boolean existsByUsername(String username);
}