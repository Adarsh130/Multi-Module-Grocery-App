package com.groceryapp.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Data Transfer Object for Shopping Cart
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartDto {
    
    private String id;
    
    @NotBlank(message = "Customer ID is required")
    private String customerId;
    
    private List<CartItemDto> items;
    
    private BigDecimal totalAmount;
    
    private Integer totalItems;
    
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
}