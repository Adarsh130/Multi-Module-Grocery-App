package com.groceryapp.persistence.repository;

import com.groceryapp.persistence.model.Cart;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for Cart entity
 */
@Repository
public interface CartRepository extends MongoRepository<Cart, String> {
    
    Optional<Cart> findByCustomerId(String customerId);
    
    void deleteByCustomerId(String customerId);
    
    boolean existsByCustomerId(String customerId);
}