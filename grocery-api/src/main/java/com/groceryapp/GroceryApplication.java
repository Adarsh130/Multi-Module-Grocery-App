package com.groceryapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main application class for the Grocery App
 * 
 * This is a multi-module Spring Boot application with the following modules:
 * - grocery-common: Shared DTOs, constants, and utilities
 * - grocery-persistence: MongoDB models and repositories
 * - grocery-service: Business logic layer
 * - grocery-security: JWT authentication and security
 * - grocery-api: REST controllers and main application (this module)
 */
@SpringBootApplication(scanBasePackages = "com.groceryapp")
public class GroceryApplication {

	public static void main(String[] args) {

		SpringApplication.run(GroceryApplication.class, args);
	}

}