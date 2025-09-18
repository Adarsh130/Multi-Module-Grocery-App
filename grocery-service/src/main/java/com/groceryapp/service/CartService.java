package com.groceryapp.service;

import com.groceryapp.common.dto.CartDto;
import com.groceryapp.common.dto.CartItemDto;
import com.groceryapp.common.exception.ResourceNotFoundException;
import com.groceryapp.persistence.model.Cart;
import com.groceryapp.persistence.model.Product;
import com.groceryapp.persistence.repository.CartRepository;
import com.groceryapp.persistence.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Service class for Cart operations
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CartService {
    
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    
    public CartDto getCartByCustomerId(String customerId) {
        log.info("Getting cart for customer: {}", customerId);
        
        Optional<Cart> cartOpt = cartRepository.findByCustomerId(customerId);
        if (cartOpt.isEmpty()) {
            // Create empty cart if doesn't exist
            Cart cart = new Cart();
            cart.setCustomerId(customerId);
            cart.setItems(new ArrayList<>());
            cart.setTotalAmount(BigDecimal.ZERO);
            cart.setTotalItems(0);
            cart.setCreatedAt(LocalDateTime.now());
            cart.setUpdatedAt(LocalDateTime.now());
            cart = cartRepository.save(cart);
            return convertToDto(cart);
        }
        
        return convertToDto(cartOpt.get());
    }
    
    public CartDto addItemToCart(String customerId, CartItemDto itemDto) {
        log.info("Adding item to cart for customer: {}", customerId);
        
        // Validate product exists
        Product product = productRepository.findById(itemDto.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
        
        // Check stock availability
        if (product.getStockQuantity() < itemDto.getQuantity()) {
            throw new IllegalArgumentException("Insufficient stock available");
        }
        
        Cart cart = cartRepository.findByCustomerId(customerId)
                .orElse(createNewCart(customerId));
        
        // Check if item already exists in cart
        Optional<Cart.CartItem> existingItem = cart.getItems().stream()
                .filter(item -> item.getProductId().equals(itemDto.getProductId()))
                .findFirst();
        
        if (existingItem.isPresent()) {
            // Update quantity
            Cart.CartItem item = existingItem.get();
            int newQuantity = item.getQuantity() + itemDto.getQuantity();
            
            // Check stock for new quantity
            if (product.getStockQuantity() < newQuantity) {
                throw new IllegalArgumentException("Insufficient stock available");
            }
            
            item.setQuantity(newQuantity);
            item.setTotalPrice(product.getPrice().multiply(BigDecimal.valueOf(newQuantity)));
        } else {
            // Add new item
            Cart.CartItem newItem = new Cart.CartItem();
            newItem.setProductId(product.getId());
            newItem.setProductName(product.getName());
            newItem.setProductCategory(product.getCategory());
            newItem.setQuantity(itemDto.getQuantity());
            newItem.setUnitPrice(product.getPrice());
            newItem.setTotalPrice(product.getPrice().multiply(BigDecimal.valueOf(itemDto.getQuantity())));
            newItem.setImageUrl(product.getImageUrl());
            
            cart.getItems().add(newItem);
        }
        
        updateCartTotals(cart);
        cart.setUpdatedAt(LocalDateTime.now());
        cart = cartRepository.save(cart);
        
        return convertToDto(cart);
    }
    
    public CartDto updateItemQuantity(String customerId, String productId, Integer quantity) {
        log.info("Updating item quantity in cart for customer: {}", customerId);
        
        Cart cart = cartRepository.findByCustomerId(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found"));
        
        Cart.CartItem item = cart.getItems().stream()
                .filter(cartItem -> cartItem.getProductId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Item not found in cart"));
        
        if (quantity <= 0) {
            cart.getItems().remove(item);
        } else {
            // Validate stock
            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
            
            if (product.getStockQuantity() < quantity) {
                throw new IllegalArgumentException("Insufficient stock available");
            }
            
            item.setQuantity(quantity);
            item.setTotalPrice(item.getUnitPrice().multiply(BigDecimal.valueOf(quantity)));
        }
        
        updateCartTotals(cart);
        cart.setUpdatedAt(LocalDateTime.now());
        cart = cartRepository.save(cart);
        
        return convertToDto(cart);
    }
    
    public CartDto removeItemFromCart(String customerId, String productId) {
        log.info("Removing item from cart for customer: {}", customerId);
        
        Cart cart = cartRepository.findByCustomerId(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found"));
        
        cart.getItems().removeIf(item -> item.getProductId().equals(productId));
        
        updateCartTotals(cart);
        cart.setUpdatedAt(LocalDateTime.now());
        cart = cartRepository.save(cart);
        
        return convertToDto(cart);
    }
    
    public void clearCart(String customerId) {
        log.info("Clearing cart for customer: {}", customerId);
        cartRepository.deleteByCustomerId(customerId);
    }
    
    private Cart createNewCart(String customerId) {
        Cart cart = new Cart();
        cart.setCustomerId(customerId);
        cart.setItems(new ArrayList<>());
        cart.setTotalAmount(BigDecimal.ZERO);
        cart.setTotalItems(0);
        cart.setCreatedAt(LocalDateTime.now());
        cart.setUpdatedAt(LocalDateTime.now());
        return cart;
    }
    
    private void updateCartTotals(Cart cart) {
        BigDecimal totalAmount = cart.getItems().stream()
                .map(Cart.CartItem::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        int totalItems = cart.getItems().stream()
                .mapToInt(Cart.CartItem::getQuantity)
                .sum();
        
        cart.setTotalAmount(totalAmount);
        cart.setTotalItems(totalItems);
    }
    
    private CartDto convertToDto(Cart cart) {
        CartDto dto = new CartDto();
        dto.setId(cart.getId());
        dto.setCustomerId(cart.getCustomerId());
        dto.setTotalAmount(cart.getTotalAmount());
        dto.setTotalItems(cart.getTotalItems());
        dto.setCreatedAt(cart.getCreatedAt());
        dto.setUpdatedAt(cart.getUpdatedAt());
        
        List<CartItemDto> itemDtos = cart.getItems().stream()
                .map(this::convertItemToDto)
                .toList();
        dto.setItems(itemDtos);
        
        return dto;
    }
    
    private CartItemDto convertItemToDto(Cart.CartItem item) {
        CartItemDto dto = new CartItemDto();
        dto.setProductId(item.getProductId());
        dto.setProductName(item.getProductName());
        dto.setProductCategory(item.getProductCategory());
        dto.setQuantity(item.getQuantity());
        dto.setUnitPrice(item.getUnitPrice());
        dto.setTotalPrice(item.getTotalPrice());
        dto.setImageUrl(item.getImageUrl());
        return dto;
    }
}