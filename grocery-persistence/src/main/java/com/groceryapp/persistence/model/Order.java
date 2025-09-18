package com.groceryapp.persistence.model;

import com.groceryapp.common.enums.OrderStatus;
import com.groceryapp.common.enums.PaymentStatus;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.Indexed;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Order entity for MongoDB
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "orders")
public class Order {
    
    @Id
    private String id;
    
    @Indexed
    private String customerId;
    
    @Indexed
    private String orderNumber;
    
    private List<OrderItem> items;
    
    // Pricing
    private BigDecimal subtotal;
    private BigDecimal tax;
    private BigDecimal shipping;
    private BigDecimal totalAmount;
    
    // Order status
    @Indexed
    private OrderStatus status = OrderStatus.PENDING;
    
    // Addresses
    private ShippingAddress shippingAddress;
    private BillingAddress billingAddress;
    
    // Payment
    private String paymentMethod; // CASH, CARD, UPI
    private PaymentStatus paymentStatus = PaymentStatus.PENDING;
    
    // Additional fields for compatibility with OrderDto
    private String customerName;
    private String deliveryAddress;
    
    // Notes
    private String notes;
    
    // Timestamps
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime orderDate;
    private LocalDateTime deliveryDate;
    private LocalDateTime shippedAt;
    private LocalDateTime deliveredAt;
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrderItem {
        private String productId;
        private String productName;
        private String productCategory;
        private Integer quantity;
        private BigDecimal unitPrice;
        private BigDecimal totalPrice;
        private String imageUrl;
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ShippingAddress {
        private String firstName;
        private String lastName;
        private String email;
        private String phone;
        private String address;
        private String city;
        private String state;
        private String zipCode;
        private String country;
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BillingAddress {
        private String firstName;
        private String lastName;
        private String address;
        private String city;
        private String state;
        private String zipCode;
        private String country;
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PaymentMethodDetails {
        private String type; // CREDIT_CARD, DEBIT_CARD, UPI, COD
        private String cardLast4;
        private String cardholderName;
        private String upiId;
    }
}