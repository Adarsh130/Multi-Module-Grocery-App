package com.groceryapp.persistence.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.Indexed;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Product entity for MongoDB
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "products")
public class Product {
    
    @Id
    private String id;
    
    @Indexed
    private String name;
    
    private String description;
    
    @Indexed
    private String category;
    
    private BigDecimal price;
    private BigDecimal originalPrice;
    
    @Indexed
    private Integer stockQuantity;
    
    private String imageUrl;
    private String brand;
    private String weight;
    private String dimensions;
    
    // Product status
    private boolean active = true;
    private boolean featured = false;
    
    // Timestamps
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // SEO fields
    private String slug;
    private String metaTitle;
    private String metaDescription;
}