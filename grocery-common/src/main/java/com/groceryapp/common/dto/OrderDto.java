package com.groceryapp.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Data Transfer Object for Order
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDto {
    
    private String id;
    
    @NotBlank(message = "Customer ID is required")
    private String customerId;
    
    private String customerName;
    
    @NotEmpty(message = "Order items cannot be empty")
    private List<OrderItemDto> items;
    
    @NotNull(message = "Total amount is required")
    private BigDecimal totalAmount;
    
    private String status; // PENDING, CONFIRMED, SHIPPED, DELIVERED, CANCELLED
    
    private String paymentMethod; // CASH, CARD, UPI
    
    private String paymentStatus; // PENDING, PAID, FAILED
    
    private String deliveryAddress;
    
    private String notes;
    
    private LocalDateTime orderDate;
    
    private LocalDateTime deliveryDate;
    
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
}