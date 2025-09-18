package com.groceryapp.controller;

import com.groceryapp.common.constants.AppConstants;
import com.groceryapp.common.dto.ApiResponseDto;
import com.groceryapp.common.dto.OrderDto;
import com.groceryapp.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

/**
 * REST Controller for Order operations
 */
@Slf4j
@RestController
@RequestMapping(AppConstants.API_BASE_PATH + "/orders")
@RequiredArgsConstructor
public class OrderController {
    
    private final OrderService orderService;
    
    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<ApiResponseDto<List<OrderDto>>> getAllOrders() {
        log.info("GET request to fetch all orders");
        List<OrderDto> orders = orderService.getAllOrders();
        return ResponseEntity.ok(ApiResponseDto.success(orders));
    }
    
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER') or hasRole('CUSTOMER')")
    public ResponseEntity<ApiResponseDto<OrderDto>> getOrderById(@PathVariable String id) {
        log.info("GET request to fetch order with id: {}", id);
        OrderDto order = orderService.getOrderById(id);
        return ResponseEntity.ok(ApiResponseDto.success(order));
    }
    
    @GetMapping("/customer/{customerId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER') or hasRole('CUSTOMER')")
    public ResponseEntity<ApiResponseDto<List<OrderDto>>> getOrdersByCustomerId(@PathVariable String customerId) {
        log.info("GET request to fetch orders for customer: {}", customerId);
        List<OrderDto> orders = orderService.getOrdersByCustomerId(customerId);
        return ResponseEntity.ok(ApiResponseDto.success(orders));
    }
    
    @GetMapping("/status/{status}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<ApiResponseDto<List<OrderDto>>> getOrdersByStatus(@PathVariable String status) {
        log.info("GET request to fetch orders with status: {}", status);
        List<OrderDto> orders = orderService.getOrdersByStatus(status);
        return ResponseEntity.ok(ApiResponseDto.success(orders));
    }
    
    @PostMapping
    @PreAuthorize("hasRole('CUSTOMER') or hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<ApiResponseDto<OrderDto>> createOrder(@Valid @RequestBody OrderDto orderDto) {
        log.info("POST request to create order for customer: {}", orderDto.getCustomerId());
        OrderDto createdOrder = orderService.createOrder(orderDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponseDto.success("Order created successfully", createdOrder));
    }
    
    @PutMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<ApiResponseDto<OrderDto>> updateOrderStatus(@PathVariable String id, 
                                                                     @RequestParam String status) {
        log.info("PUT request to update order {} status to: {}", id, status);
        OrderDto updatedOrder = orderService.updateOrderStatus(id, status);
        return ResponseEntity.ok(ApiResponseDto.success("Order status updated successfully", updatedOrder));
    }
    
    @PutMapping("/{id}/payment-status")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<ApiResponseDto<OrderDto>> updatePaymentStatus(@PathVariable String id, 
                                                                       @RequestParam String paymentStatus) {
        log.info("PUT request to update order {} payment status to: {}", id, paymentStatus);
        OrderDto updatedOrder = orderService.updatePaymentStatus(id, paymentStatus);
        return ResponseEntity.ok(ApiResponseDto.success("Payment status updated successfully", updatedOrder));
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER') or hasRole('CUSTOMER')")
    public ResponseEntity<ApiResponseDto<Void>> cancelOrder(@PathVariable String id) {
        log.info("DELETE request to cancel order: {}", id);
        orderService.cancelOrder(id);
        return ResponseEntity.ok(ApiResponseDto.success("Order cancelled successfully", null));
    }
}