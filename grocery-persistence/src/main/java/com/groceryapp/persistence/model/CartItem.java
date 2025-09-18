package com.groceryapp.persistence.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Cart Item embedded document
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItem {
    
    private String productId;
    
    private String productName;
    
    private String productCategory;
    
    private Integer quantity;
    
    private BigDecimal unitPrice;
    
    private BigDecimal totalPrice;
}