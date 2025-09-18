package com.groceryapp.service;

import com.groceryapp.persistence.model.Product;
import com.groceryapp.persistence.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

/**
 * Service to initialize sample data with realistic products and images
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DataInitializationService implements CommandLineRunner {
    
    private final ProductRepository productRepository;
    
    @Override
    public void run(String... args) throws Exception {
        if (productRepository.count() == 0) {
            log.info("Initializing sample product data with images...");
            initializeSampleProducts();
        }
    }
    
    private void initializeSampleProducts() {
        List<Product> sampleProducts = Arrays.asList(
            // Fresh Produce
            createProduct("Fresh Bananas", "Fresh organic bananas - rich in potassium and vitamins", 
                         new BigDecimal("2.99"), "fresh", 150, 
                         "https://images.unsplash.com/photo-1571771894821-ce9b6c11b08e?w=400"),
            
            createProduct("Red Apples", "Crispy red apples - perfect for snacking", 
                         new BigDecimal("4.99"), "fresh", 120,
                         "https://images.unsplash.com/photo-1560806887-1e4cd0b6cbd6?w=400"),
            
            createProduct("Fresh Oranges", "Juicy oranges packed with Vitamin C", 
                         new BigDecimal("3.49"), "fresh", 100,
                         "https://images.unsplash.com/photo-1547514701-42782101795e?w=400"),
            
            createProduct("Green Spinach", "Fresh organic spinach leaves", 
                         new BigDecimal("2.49"), "fresh", 80,
                         "https://images.unsplash.com/photo-1576045057995-568f588f82fb?w=400"),
            
            createProduct("Fresh Tomatoes", "Ripe red tomatoes - perfect for cooking", 
                         new BigDecimal("3.99"), "fresh", 90,
                         "https://images.unsplash.com/photo-1546470427-e26264be0b0d?w=400"),
            
            createProduct("Fresh Carrots", "Organic carrots - great source of beta-carotene", 
                         new BigDecimal("2.79"), "fresh", 110,
                         "https://images.unsplash.com/photo-1598170845058-32b9d6a5da37?w=400"),
            
            // Dairy Products
            createProduct("Whole Milk", "Fresh whole milk - 1 gallon", 
                         new BigDecimal("3.49"), "dairy", 60,
                         "https://images.unsplash.com/photo-1550583724-b2692b85b150?w=400"),
            
            createProduct("Greek Yogurt", "Creamy Greek yogurt - high in protein", 
                         new BigDecimal("5.99"), "dairy", 45,
                         "https://images.unsplash.com/photo-1488477181946-6428a0291777?w=400"),
            
            createProduct("Cheddar Cheese", "Sharp cheddar cheese block", 
                         new BigDecimal("6.99"), "dairy", 35,
                         "https://images.unsplash.com/photo-1486297678162-eb2a19b0a32d?w=400"),
            
            createProduct("Fresh Eggs", "Farm fresh eggs - dozen", 
                         new BigDecimal("4.49"), "dairy", 70,
                         "https://images.unsplash.com/photo-1582722872445-44dc5f7e3c8f?w=400"),
            
            createProduct("Butter", "Unsalted butter - premium quality", 
                         new BigDecimal("4.99"), "dairy", 50,
                         "https://images.unsplash.com/photo-1589985270826-4b7bb135bc9d?w=400"),
            
            // Meat & Seafood
            createProduct("Chicken Breast", "Boneless skinless chicken breast - 1 lb", 
                         new BigDecimal("8.99"), "meat", 40,
                         "https://images.unsplash.com/photo-1604503468506-a8da13d82791?w=400"),
            
            createProduct("Ground Beef", "Fresh ground beef - 80/20 lean", 
                         new BigDecimal("7.99"), "meat", 30,
                         "https://images.unsplash.com/photo-1588347818133-6b2e6c0b5e8e?w=400"),
            
            createProduct("Salmon Fillet", "Fresh Atlantic salmon fillet", 
                         new BigDecimal("12.99"), "meat", 25,
                         "https://images.unsplash.com/photo-1519708227418-c8fd9a32b7a2?w=400"),
            
            createProduct("Pork Chops", "Bone-in pork chops - 4 pack", 
                         new BigDecimal("9.99"), "meat", 20,
                         "https://images.unsplash.com/photo-1602470520998-f4a52199a3d6?w=400"),
            
            // Bakery
            createProduct("Sourdough Bread", "Artisan sourdough bread - freshly baked", 
                         new BigDecimal("4.99"), "bakery", 25,
                         "https://images.unsplash.com/photo-1509440159596-0249088772ff?w=400"),
            
            createProduct("Croissants", "Buttery French croissants - 6 pack", 
                         new BigDecimal("6.99"), "bakery", 20,
                         "https://images.unsplash.com/photo-1555507036-ab794f4afe5a?w=400"),
            
            createProduct("Bagels", "Fresh bagels - variety pack", 
                         new BigDecimal("5.49"), "bakery", 30,
                         "https://images.unsplash.com/photo-1551024506-0bccd828d307?w=400"),
            
            createProduct("Chocolate Muffins", "Double chocolate muffins - 4 pack", 
                         new BigDecimal("7.99"), "bakery", 15,
                         "https://images.unsplash.com/photo-1607958996333-41aef7caefaa?w=400"),
            
            // Pantry Items
            createProduct("Basmati Rice", "Premium basmati rice - 5 lb bag", 
                         new BigDecimal("8.99"), "pantry", 60,
                         "https://images.unsplash.com/photo-1586201375761-83865001e31c?w=400"),
            
            createProduct("Pasta Sauce", "Organic marinara pasta sauce", 
                         new BigDecimal("3.49"), "pantry", 80,
                         "https://images.unsplash.com/photo-1551782450-17144efb9c50?w=400"),
            
            createProduct("Olive Oil", "Extra virgin olive oil - 500ml", 
                         new BigDecimal("9.99"), "pantry", 40,
                         "https://images.unsplash.com/photo-1474979266404-7eaacbcd87c5?w=400"),
            
            createProduct("Spaghetti Pasta", "Whole wheat spaghetti pasta", 
                         new BigDecimal("2.99"), "pantry", 70,
                         "https://images.unsplash.com/photo-1551892374-ecf8754cf8b0?w=400"),
            
            createProduct("Black Beans", "Organic black beans - canned", 
                         new BigDecimal("1.99"), "pantry", 90,
                         "https://images.unsplash.com/photo-1583258292688-d0213dc5a3a8?w=400"),
            
            createProduct("Quinoa", "Organic quinoa - superfood grain", 
                         new BigDecimal("7.99"), "pantry", 35,
                         "https://images.unsplash.com/photo-1586444248902-2f64eddc13df?w=400"),
            
            // Beverages
            createProduct("Orange Juice", "Fresh squeezed orange juice - 64 oz", 
                         new BigDecimal("4.99"), "beverages", 50,
                         "https://images.unsplash.com/photo-1621506289937-a8e4df240d0b?w=400"),
            
            createProduct("Green Tea", "Premium green tea bags - 20 count", 
                         new BigDecimal("6.99"), "beverages", 40,
                         "https://images.unsplash.com/photo-1556679343-c7306c1976bc?w=400"),
            
            createProduct("Coffee Beans", "Premium arabica coffee beans - 1 lb", 
                         new BigDecimal("12.99"), "beverages", 30,
                         "https://images.unsplash.com/photo-1559056199-641a0ac8b55e?w=400"),
            
            createProduct("Sparkling Water", "Natural sparkling water - 12 pack", 
                         new BigDecimal("5.99"), "beverages", 60,
                         "https://images.unsplash.com/photo-1523362628745-0c100150b504?w=400"),
            
            createProduct("Coconut Water", "Pure coconut water - 6 pack", 
                         new BigDecimal("8.99"), "beverages", 25,
                         "https://images.unsplash.com/photo-1481671703460-040cb8a2d909?w=400"),
            
            // Snacks
            createProduct("Mixed Nuts", "Premium mixed nuts - roasted & salted", 
                         new BigDecimal("9.99"), "snacks", 45,
                         "https://images.unsplash.com/photo-1599599810769-bcde5a160d32?w=400"),
            
            createProduct("Potato Chips", "Kettle cooked potato chips - sea salt", 
                         new BigDecimal("3.99"), "snacks", 80,
                         "https://images.unsplash.com/photo-1566478989037-eec170784d0b?w=400"),
            
            createProduct("Dark Chocolate", "70% dark chocolate bar - organic", 
                         new BigDecimal("4.99"), "snacks", 35,
                         "https://images.unsplash.com/photo-1511381939415-e44015466834?w=400"),
            
            createProduct("Granola Bars", "Oats & honey granola bars - 6 pack", 
                         new BigDecimal("6.99"), "snacks", 50,
                         "https://images.unsplash.com/photo-1571019613454-1cb2f99b2d8b?w=400"),
            
            createProduct("Popcorn", "Organic popcorn kernels - 2 lb bag", 
                         new BigDecimal("4.49"), "snacks", 40,
                         "https://images.unsplash.com/photo-1578849278619-e73505e9610f?w=400"),
            
            // Frozen Foods
            createProduct("Frozen Pizza", "Margherita frozen pizza - wood fired", 
                         new BigDecimal("8.99"), "frozen", 20,
                         "https://images.unsplash.com/photo-1513104890138-7c749659a591?w=400"),
            
            createProduct("Ice Cream", "Vanilla bean ice cream - premium", 
                         new BigDecimal("6.99"), "frozen", 30,
                         "https://images.unsplash.com/photo-1563805042-7684c019e1cb?w=400"),
            
            createProduct("Frozen Berries", "Mixed berry blend - organic frozen", 
                         new BigDecimal("5.99"), "frozen", 40,
                         "https://images.unsplash.com/photo-1506905925346-21bda4d32df4?w=400"),
            
            createProduct("Frozen Vegetables", "Mixed vegetables - steam in bag", 
                         new BigDecimal("3.99"), "frozen", 60,
                         "https://images.unsplash.com/photo-1590779033100-9f60a05a013d?w=400"),
            
            // Health & Beauty
            createProduct("Organic Honey", "Raw organic honey - 16 oz jar", 
                         new BigDecimal("11.99"), "health", 25,
                         "https://images.unsplash.com/photo-1587049352846-4a222e784d38?w=400"),
            
            createProduct("Coconut Oil", "Virgin coconut oil - multipurpose", 
                         new BigDecimal("9.99"), "health", 30,
                         "https://images.unsplash.com/photo-1474979266404-7eaacbcd87c5?w=400"),
            
            createProduct("Vitamin C", "Vitamin C supplements - 60 tablets", 
                         new BigDecimal("12.99"), "health", 40,
                         "https://images.unsplash.com/photo-1584308666744-24d5c474f2ae?w=400"),
            
            createProduct("Protein Powder", "Whey protein powder - vanilla", 
                         new BigDecimal("29.99"), "health", 20,
                         "https://images.unsplash.com/photo-1593095948071-474c5cc2989d?w=400"),
            
            // Indian Grocery Items
            createProduct("Turmeric Powder", "Pure turmeric powder - 200g", 
                         new BigDecimal("3.99"), "spices", 50,
                         "https://images.unsplash.com/photo-1615485500704-8e990f9900f7?w=400"),
            
            createProduct("Cumin Seeds", "Whole cumin seeds - aromatic spice", 
                         new BigDecimal("4.99"), "spices", 45,
                         "https://images.unsplash.com/photo-1596040033229-a9821ebd058d?w=400"),
            
            createProduct("Basmati Rice", "Aged basmati rice - 10 lb bag", 
                         new BigDecimal("15.99"), "grains", 30,
                         "https://images.unsplash.com/photo-1586201375761-83865001e31c?w=400"),
            
            createProduct("Lentils (Dal)", "Red lentils - protein rich", 
                         new BigDecimal("5.99"), "grains", 60,
                         "https://images.unsplash.com/photo-1559181567-c3190ca9959b?w=400"),
            
            createProduct("Ghee", "Pure cow ghee - clarified butter", 
                         new BigDecimal("12.99"), "dairy", 25,
                         "https://images.unsplash.com/photo-1609501676725-7186f734b2b0?w=400"),
            
            createProduct("Chapati Flour", "Whole wheat flour for chapati", 
                         new BigDecimal("6.99"), "grains", 40,
                         "https://images.unsplash.com/photo-1574323347407-f5e1ad6d020b?w=400"),
            
            // Household Items
            createProduct("Dish Soap", "Eco-friendly dish soap - lemon scent", 
                         new BigDecimal("3.99"), "household", 70,
                         "https://images.unsplash.com/photo-1563453392212-326f5e854473?w=400"),
            
            createProduct("Paper Towels", "Ultra absorbent paper towels - 6 rolls", 
                         new BigDecimal("8.99"), "household", 50,
                         "https://images.unsplash.com/photo-1584464491033-06628f3a6b7b?w=400"),
            
            createProduct("Laundry Detergent", "Concentrated laundry detergent", 
                         new BigDecimal("11.99"), "household", 35,
                         "https://images.unsplash.com/photo-1558618666-fcd25c85cd64?w=400"),
            
            createProduct("Toilet Paper", "Soft toilet paper - 12 rolls", 
                         new BigDecimal("9.99"), "household", 60,
                         "https://images.unsplash.com/photo-1584464491033-06628f3a6b7b?w=400")
        );
        
        productRepository.saveAll(sampleProducts);
        log.info("Sample product data initialized successfully. Created {} products with images.", sampleProducts.size());
    }
    
    private Product createProduct(String name, String description, BigDecimal price, 
                                 String category, Integer stockQuantity, String imageUrl) {
        Product product = new Product();
        product.setName(name);
        product.setDescription(description);
        product.setPrice(price);
        product.setCategory(category);
        // Note: Product entity only has stockQuantity field
        product.setStockQuantity(stockQuantity);
        product.setImageUrl(imageUrl);
        product.setActive(true);
        product.setCreatedAt(LocalDateTime.now());
        product.setUpdatedAt(LocalDateTime.now());
        
        log.debug("Created product: {} with stock quantity: {} and image: {}", name, stockQuantity, imageUrl);
        return product;
    }
}