package com.groceryapp.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

/**
 * Extended Data Transfer Object for Product with additional fields
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExtendedProductDto {
    
    private String id;
    
    @NotBlank(message = "Product name is required")
    private String name;
    
    private String description;
    
    @NotNull(message = "Quantity is required")
    @Positive(message = "Quantity must be positive")
    private Integer quantity;
    
    private Integer stockQuantity;
    
    @NotNull(message = "Price is required")
    @Positive(message = "Price must be positive")
    private BigDecimal price;
    
    @NotBlank(message = "Category is required")
    private String category;
    
    private String imageUrl;
    
    private boolean active = true;
    
    // Constructor for backward compatibility
    public ExtendedProductDto(String id, String name, Integer quantity, BigDecimal price, String category) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.stockQuantity = quantity;
        this.price = price;
        this.category = category;
    }
}