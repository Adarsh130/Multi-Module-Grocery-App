package com.groceryapp.common.constants;

/**
 * Application constants
 */
public final class AppConstants {
    
    private AppConstants() {
        // Utility class
    }
    
    // API Endpoints
    public static final String API_BASE_PATH = "/api";
    public static final String PRODUCTS_PATH = "/products";
    public static final String CUSTOMERS_PATH = "/customers";
    public static final String AUTH_PATH = "/auth";
    
    // Error Messages
    public static final String PRODUCT_NOT_FOUND = "Product not found with id: ";
    public static final String CUSTOMER_NOT_FOUND = "Customer not found with id: ";
    public static final String INVALID_CREDENTIALS = "Invalid username or password";
    public static final String ACCESS_DENIED = "Access denied";
    
    // Roles
    public static final String ROLE_ADMIN = "ADMIN";
    public static final String ROLE_USER = "USER";
    
    // JWT
    public static final String JWT_HEADER = "Authorization";
    public static final String JWT_PREFIX = "Bearer ";
}