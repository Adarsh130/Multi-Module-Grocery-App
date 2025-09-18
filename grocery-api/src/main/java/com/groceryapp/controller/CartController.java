package com.groceryapp.controller;

import com.groceryapp.common.constants.AppConstants;
import com.groceryapp.common.dto.ApiResponseDto;
import com.groceryapp.common.dto.CartDto;
import com.groceryapp.common.dto.CartItemDto;
import com.groceryapp.service.CartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

/**
 * REST Controller for Shopping Cart operations
 */
@Slf4j
@RestController
@RequestMapping(AppConstants.API_BASE_PATH + "/cart")
@RequiredArgsConstructor
public class CartController {
    
    private final CartService cartService;
    
    @GetMapping("/{customerId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER') or hasRole('CUSTOMER')")
    public ResponseEntity<ApiResponseDto<CartDto>> getCart(@PathVariable String customerId) {
        log.info("GET request to fetch cart for customer: {}", customerId);
        CartDto cart = cartService.getCartByCustomerId(customerId);
        return ResponseEntity.ok(ApiResponseDto.success(cart));
    }
    
    @PostMapping("/{customerId}/items")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER') or hasRole('CUSTOMER')")
    public ResponseEntity<ApiResponseDto<CartDto>> addItemToCart(@PathVariable String customerId,
                                                                @Valid @RequestBody CartItemDto itemDto) {
        log.info("POST request to add item to cart for customer: {}", customerId);
        CartDto cart = cartService.addItemToCart(customerId, itemDto);
        return ResponseEntity.ok(ApiResponseDto.success("Item added to cart successfully", cart));
    }
    
    @PutMapping("/{customerId}/items/{productId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER') or hasRole('CUSTOMER')")
    public ResponseEntity<ApiResponseDto<CartDto>> updateItemQuantity(@PathVariable String customerId,
                                                                     @PathVariable String productId,
                                                                     @RequestParam Integer quantity) {
        log.info("PUT request to update item quantity in cart for customer: {}", customerId);
        CartDto cart = cartService.updateItemQuantity(customerId, productId, quantity);
        return ResponseEntity.ok(ApiResponseDto.success("Item quantity updated successfully", cart));
    }
    
    @DeleteMapping("/{customerId}/items/{productId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER') or hasRole('CUSTOMER')")
    public ResponseEntity<ApiResponseDto<CartDto>> removeItemFromCart(@PathVariable String customerId,
                                                                     @PathVariable String productId) {
        log.info("DELETE request to remove item from cart for customer: {}", customerId);
        CartDto cart = cartService.removeItemFromCart(customerId, productId);
        return ResponseEntity.ok(ApiResponseDto.success("Item removed from cart successfully", cart));
    }
    
    @DeleteMapping("/{customerId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER') or hasRole('CUSTOMER')")
    public ResponseEntity<ApiResponseDto<Void>> clearCart(@PathVariable String customerId) {
        log.info("DELETE request to clear cart for customer: {}", customerId);
        cartService.clearCart(customerId);
        return ResponseEntity.ok(ApiResponseDto.success("Cart cleared successfully", null));
    }
}