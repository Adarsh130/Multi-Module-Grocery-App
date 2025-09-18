package com.groceryapp.service;

import com.groceryapp.common.dto.OrderDto;
import com.groceryapp.common.dto.OrderItemDto;
import com.groceryapp.common.enums.OrderStatus;
import com.groceryapp.common.enums.PaymentStatus;
import com.groceryapp.common.enums.PaymentMethod;
import com.groceryapp.common.exception.BadRequestException;
import com.groceryapp.common.exception.ResourceNotFoundException;
import com.groceryapp.persistence.model.Order;
import com.groceryapp.persistence.model.Product;
import com.groceryapp.persistence.repository.OrderRepository;
import com.groceryapp.persistence.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class for Order operations
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {
    
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    
    public List<OrderDto> getAllOrders() {
        log.info("Fetching all orders");
        return orderRepository.findAll()
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    public OrderDto getOrderById(String id) {
        log.info("Fetching order with id: {}", id);
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + id));
        return convertToDto(order);
    }
    
    public List<OrderDto> getOrdersByCustomerId(String customerId) {
        log.info("Fetching orders for customer: {}", customerId);
        return orderRepository.findByCustomerId(customerId)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    public List<OrderDto> getOrdersByStatus(String status) {
        log.info("Fetching orders with status: {}", status);
        try {
            OrderStatus orderStatus = OrderStatus.valueOf(status.toUpperCase());
            return orderRepository.findByStatus(orderStatus)
                    .stream()
                    .map(this::convertToDto)
                    .collect(Collectors.toList());
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Invalid order status: " + status);
        }
    }
    
    @Transactional
    public OrderDto createOrder(OrderDto orderDto) {
        log.info("Creating new order for customer: {}", orderDto.getCustomerId());
        
        // Validate and calculate order
        validateAndCalculateOrder(orderDto);
        
        Order order = convertToEntity(orderDto);
        order.setOrderDate(LocalDateTime.now());
        order.setCreatedAt(LocalDateTime.now());
        order.setUpdatedAt(LocalDateTime.now());
        order.setStatus(OrderStatus.PENDING);
        order.setPaymentStatus(PaymentStatus.PENDING);
        
        // Update product quantities
        updateProductQuantities(order.getItems());
        
        Order savedOrder = orderRepository.save(order);
        return convertToDto(savedOrder);
    }
    
    public OrderDto updateOrderStatus(String id, String status) {
        log.info("Updating order {} status to: {}", id, status);
        
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + id));
        
        try {
            OrderStatus orderStatus = OrderStatus.valueOf(status.toUpperCase());
            order.setStatus(orderStatus);
            order.setUpdatedAt(LocalDateTime.now());
            
            Order updatedOrder = orderRepository.save(order);
            return convertToDto(updatedOrder);
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Invalid order status: " + status);
        }
    }
    
    public OrderDto updatePaymentStatus(String id, String paymentStatus) {
        log.info("Updating order {} payment status to: {}", id, paymentStatus);
        
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + id));
        
        try {
            PaymentStatus status = PaymentStatus.valueOf(paymentStatus.toUpperCase());
            order.setPaymentStatus(status);
            order.setUpdatedAt(LocalDateTime.now());
            
            Order updatedOrder = orderRepository.save(order);
            return convertToDto(updatedOrder);
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Invalid payment status: " + paymentStatus);
        }
    }
    
    public void cancelOrder(String id) {
        log.info("Cancelling order: {}", id);
        
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + id));
        
        if (OrderStatus.DELIVERED.equals(order.getStatus()) || OrderStatus.CANCELLED.equals(order.getStatus())) {
            throw new BadRequestException("Cannot cancel order with status: " + order.getStatus());
        }
        
        // Restore product quantities
        restoreProductQuantities(order.getItems());
        
        order.setStatus(OrderStatus.CANCELLED);
        order.setUpdatedAt(LocalDateTime.now());
        orderRepository.save(order);
    }
    
    private void validateAndCalculateOrder(OrderDto orderDto) {
        if (orderDto.getItems() == null || orderDto.getItems().isEmpty()) {
            throw new BadRequestException("Order must contain at least one item");
        }
        
        BigDecimal totalAmount = BigDecimal.ZERO;
        
        for (OrderItemDto item : orderDto.getItems()) {
            Product product = productRepository.findById(item.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found: " + item.getProductId()));
            
            if (product.getStockQuantity() < item.getQuantity()) {
                throw new BadRequestException("Insufficient stock for product: " + product.getName());
            }
            
            item.setProductName(product.getName());
            item.setUnitPrice(product.getPrice());
            item.setTotalPrice(product.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
            
            totalAmount = totalAmount.add(item.getTotalPrice());
        }
        
        orderDto.setTotalAmount(totalAmount);
    }
    
    private void updateProductQuantities(List<Order.OrderItem> items) {
        for (Order.OrderItem item : items) {
            Product product = productRepository.findById(item.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found: " + item.getProductId()));
            
            product.setStockQuantity(product.getStockQuantity() - item.getQuantity());
            productRepository.save(product);
        }
    }
    
    private void restoreProductQuantities(List<Order.OrderItem> items) {
        for (Order.OrderItem item : items) {
            Product product = productRepository.findById(item.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found: " + item.getProductId()));
            
            product.setStockQuantity(product.getStockQuantity() + item.getQuantity());
            productRepository.save(product);
        }
    }
    
    private OrderDto convertToDto(Order order) {
        OrderDto dto = new OrderDto();
        dto.setId(order.getId());
        dto.setCustomerId(order.getCustomerId());
        dto.setCustomerName(order.getCustomerName());
        dto.setItems(order.getItems().stream()
                .map(this::convertItemToDto)
                .collect(Collectors.toList()));
        dto.setTotalAmount(order.getTotalAmount());
        dto.setStatus(order.getStatus() != null ? order.getStatus().name() : null);
        dto.setPaymentMethod(order.getPaymentMethod());
        dto.setPaymentStatus(order.getPaymentStatus() != null ? order.getPaymentStatus().name() : null);
        dto.setDeliveryAddress(order.getDeliveryAddress());
        dto.setNotes(order.getNotes());
        dto.setOrderDate(order.getOrderDate());
        dto.setDeliveryDate(order.getDeliveryDate());
        dto.setCreatedAt(order.getCreatedAt());
        dto.setUpdatedAt(order.getUpdatedAt());
        return dto;
    }
    
    private Order convertToEntity(OrderDto orderDto) {
        Order order = new Order();
        order.setId(orderDto.getId());
        order.setCustomerId(orderDto.getCustomerId());
        order.setCustomerName(orderDto.getCustomerName());
        order.setItems(orderDto.getItems().stream()
                .map(this::convertItemToEntity)
                .collect(Collectors.toList()));
        order.setTotalAmount(orderDto.getTotalAmount());
        
        // Convert String to enum types
        if (orderDto.getStatus() != null) {
            try {
                order.setStatus(OrderStatus.valueOf(orderDto.getStatus().toUpperCase()));
            } catch (IllegalArgumentException e) {
                order.setStatus(OrderStatus.PENDING);
            }
        }
        
        order.setPaymentMethod(orderDto.getPaymentMethod());
        
        if (orderDto.getPaymentStatus() != null) {
            try {
                order.setPaymentStatus(PaymentStatus.valueOf(orderDto.getPaymentStatus().toUpperCase()));
            } catch (IllegalArgumentException e) {
                order.setPaymentStatus(PaymentStatus.PENDING);
            }
        }
        
        order.setDeliveryAddress(orderDto.getDeliveryAddress());
        order.setNotes(orderDto.getNotes());
        order.setOrderDate(orderDto.getOrderDate());
        order.setDeliveryDate(orderDto.getDeliveryDate());
        order.setCreatedAt(orderDto.getCreatedAt());
        order.setUpdatedAt(orderDto.getUpdatedAt());
        return order;
    }
    
    private OrderItemDto convertItemToDto(Order.OrderItem item) {
        return new OrderItemDto(
                item.getProductId(),
                item.getProductName(),
                item.getQuantity(),
                item.getUnitPrice(),
                item.getTotalPrice()
        );
    }
    
    private Order.OrderItem convertItemToEntity(OrderItemDto itemDto) {
        return new Order.OrderItem(
                itemDto.getProductId(),
                itemDto.getProductName(),
                null, // productCategory - not available in DTO
                itemDto.getQuantity(),
                itemDto.getUnitPrice(),
                itemDto.getTotalPrice(),
                null  // imageUrl - not available in DTO
        );
    }
}