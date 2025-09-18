package com.groceryapp.persistence.repository;

import com.groceryapp.persistence.model.Category;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Category entity
 */
@Repository
public interface CategoryRepository extends MongoRepository<Category, String> {
    
    Optional<Category> findByName(String name);
    
    Optional<Category> findBySlug(String slug);
    
    List<Category> findByActiveTrue();
    
    List<Category> findByParentIdAndActiveTrue(String parentId);
    
    List<Category> findByParentIdIsNullAndActiveTrue();
    
    boolean existsByName(String name);
    
    boolean existsBySlug(String slug);
}