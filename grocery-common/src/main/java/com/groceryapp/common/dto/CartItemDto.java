package com.groceryapp.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

/**
 * Data Transfer Object for Cart Item
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItemDto {
    
    @NotBlank(message = "Product ID is required")
    private String productId;
    
    private String productName;
    
    private String productCategory;
    
    private String imageUrl;
    
    @NotNull(message = "Quantity is required")
    @Positive(message = "Quantity must be positive")
    private Integer quantity;
    
    @NotNull(message = "Unit price is required")
    @Positive(message = "Unit price must be positive")
    private BigDecimal unitPrice;
    
    @NotNull(message = "Total price is required")
    private BigDecimal totalPrice;
}