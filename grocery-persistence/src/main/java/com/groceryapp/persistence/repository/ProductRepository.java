package com.groceryapp.persistence.repository;

import com.groceryapp.persistence.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Product entity
 */
@Repository
public interface ProductRepository extends MongoRepository<Product, String> {
    
    Optional<Product> findBySlug(String slug);
    
    List<Product> findByCategory(String category);
    
    List<Product> findByActiveTrue();
    
    List<Product> findByFeaturedTrue();
    
    Page<Product> findByActiveTrue(Pageable pageable);
    
    Page<Product> findByCategoryAndActiveTrue(String category, Pageable pageable);
    
    @Query("{'name': {$regex: ?0, $options: 'i'}, 'active': true}")
    Page<Product> findByNameContainingIgnoreCaseAndActiveTrue(String name, Pageable pageable);
    
    @Query("{'$and': [{'active': true}, {'$or': [{'name': {$regex: ?0, $options: 'i'}}, {'description': {$regex: ?0, $options: 'i'}}]}]}")
    Page<Product> searchByNameOrDescription(String searchTerm, Pageable pageable);
    
    List<Product> findByPriceBetweenAndActiveTrue(BigDecimal minPrice, BigDecimal maxPrice);
    
    List<Product> findByStockQuantityLessThan(Integer threshold);
    
    boolean existsByName(String name);
    
    // Additional methods needed by ProductService
    Optional<Product> findByIdAndActiveTrue(String id);
    
    List<Product> findByCategoryAndActiveTrue(String category);
    
    @Query("{'$and': [{'active': true}, {'$or': [{'name': {$regex: ?0, $options: 'i'}}, {'description': {$regex: ?0, $options: 'i'}}, {'category': {$regex: ?0, $options: 'i'}}]}]}")
    List<Product> searchProducts(String searchTerm);
    
    @Query("{'stockQuantity': {$lt: ?0}, 'active': true}")
    List<Product> findLowStockProducts(Integer threshold);
}