package com.groceryapp.config;

import com.groceryapp.persistence.model.Category;
import com.groceryapp.persistence.model.Product;
import com.groceryapp.persistence.model.User;
import com.groceryapp.persistence.repository.CategoryRepository;
import com.groceryapp.persistence.repository.ProductRepository;
import com.groceryapp.persistence.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Data initializer to populate sample data on application startup
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {
    
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final PasswordEncoder passwordEncoder;
    
    @Override
    public void run(String... args) throws Exception {
        log.info("Initializing sample data...");
        
        initializeUsers();
        initializeCategories();
        initializeProducts();
        
        log.info("Sample data initialization completed!");
    }
    
    private void initializeUsers() {
        if (userRepository.count() == 0) {
            log.info("Creating sample users...");
            
            // Create admin user
            User admin = new User();
            admin.setUsername("admin");
            admin.setEmail("admin@groceryapp.com");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setFullName("System Administrator");
            admin.setPhoneNumber("1234567890");
            admin.setEnabled(true);
            admin.setCreatedAt(LocalDateTime.now());
            admin.setUpdatedAt(LocalDateTime.now());
            Set<String> adminRoles = new HashSet<>();
            adminRoles.add("ADMIN");
            admin.setRoles(adminRoles);
            userRepository.save(admin);
            
            // Create manager user
            User manager = new User();
            manager.setUsername("manager");
            manager.setEmail("manager@groceryapp.com");
            manager.setPassword(passwordEncoder.encode("manager123"));
            manager.setFullName("Store Manager");
            manager.setPhoneNumber("1234567891");
            manager.setEnabled(true);
            manager.setCreatedAt(LocalDateTime.now());
            manager.setUpdatedAt(LocalDateTime.now());
            Set<String> managerRoles = new HashSet<>();
            managerRoles.add("MANAGER");
            manager.setRoles(managerRoles);
            userRepository.save(manager);
            
            // Create customer user
            User customer = new User();
            customer.setUsername("customer");
            customer.setEmail("customer@groceryapp.com");
            customer.setPassword(passwordEncoder.encode("customer123"));
            customer.setFullName("John Doe");
            customer.setPhoneNumber("1234567892");
            customer.setEnabled(true);
            customer.setCreatedAt(LocalDateTime.now());
            customer.setUpdatedAt(LocalDateTime.now());
            Set<String> customerRoles = new HashSet<>();
            customerRoles.add("CUSTOMER");
            customer.setRoles(customerRoles);
            userRepository.save(customer);
            
            log.info("Sample users created successfully!");
        }
    }
    
    private void initializeCategories() {
        if (categoryRepository.count() == 0) {
            log.info("Creating sample categories...");
            
            String[] categoryNames = {
                "Fruits", "Vegetables", "Dairy", "Meat", "Bakery", 
                "Beverages", "Snacks", "Frozen Foods", "Grains", "Spices"
            };
            
            for (String categoryName : categoryNames) {
                Category category = new Category();
                category.setName(categoryName);
                category.setDescription("Fresh " + categoryName.toLowerCase());
                category.setActive(true);
                category.setCreatedAt(LocalDateTime.now());
                category.setUpdatedAt(LocalDateTime.now());
                categoryRepository.save(category);
            }
            
            log.info("Sample categories created successfully!");
        }
    }
    
    private void initializeProducts() {
        if (productRepository.count() == 0) {
            log.info("Creating sample products...");
            
            // Fruits
            createProduct("Apple", 50, new BigDecimal("2.50"), "Fruits");
            createProduct("Banana", 100, new BigDecimal("1.20"), "Fruits");
            createProduct("Orange", 75, new BigDecimal("3.00"), "Fruits");
            createProduct("Grapes", 30, new BigDecimal("4.50"), "Fruits");
            
            // Vegetables
            createProduct("Tomato", 80, new BigDecimal("2.00"), "Vegetables");
            createProduct("Onion", 60, new BigDecimal("1.50"), "Vegetables");
            createProduct("Potato", 120, new BigDecimal("1.80"), "Vegetables");
            createProduct("Carrot", 40, new BigDecimal("2.20"), "Vegetables");
            
            // Dairy
            createProduct("Milk", 25, new BigDecimal("3.50"), "Dairy");
            createProduct("Cheese", 15, new BigDecimal("5.00"), "Dairy");
            createProduct("Yogurt", 20, new BigDecimal("2.80"), "Dairy");
            createProduct("Butter", 10, new BigDecimal("4.20"), "Dairy");
            
            // Meat
            createProduct("Chicken Breast", 20, new BigDecimal("8.50"), "Meat");
            createProduct("Ground Beef", 15, new BigDecimal("12.00"), "Meat");
            createProduct("Salmon", 10, new BigDecimal("15.00"), "Meat");
            
            // Bakery
            createProduct("Bread", 30, new BigDecimal("2.50"), "Bakery");
            createProduct("Croissant", 20, new BigDecimal("1.80"), "Bakery");
            createProduct("Bagel", 25, new BigDecimal("1.50"), "Bakery");
            
            // Beverages
            createProduct("Orange Juice", 35, new BigDecimal("3.20"), "Beverages");
            createProduct("Coffee", 40, new BigDecimal("6.50"), "Beverages");
            createProduct("Green Tea", 50, new BigDecimal("4.80"), "Beverages");
            
            log.info("Sample products created successfully!");
        }
    }
    
    private void createProduct(String name, int quantity, BigDecimal price, String category) {
        Product product = new Product();
        product.setName(name);
        product.setStockQuantity(quantity);
        product.setPrice(price);
        product.setCategory(category);
        product.setActive(true);
        product.setCreatedAt(LocalDateTime.now());
        product.setUpdatedAt(LocalDateTime.now());
        productRepository.save(product);
    }
}