package com.groceryapp.controller;

import com.groceryapp.common.constants.AppConstants;
import com.groceryapp.common.dto.ProductDto;
import com.groceryapp.common.dto.ExtendedProductDto;
import com.groceryapp.service.ProductService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.math.BigDecimal;
import java.util.List;

/**
 * REST Controller for Product operations
 */
@Slf4j
@RestController
@RequestMapping(AppConstants.API_BASE_PATH + AppConstants.PRODUCTS_PATH)
@RequiredArgsConstructor
public class ProductController {
    
    private final ProductService productService;
    private final ObjectMapper objectMapper;
    
    @GetMapping
    public ResponseEntity<List<ExtendedProductDto>> getAllProducts() {
        log.info("GET request to fetch all products");
        List<ExtendedProductDto> products = productService.getAllProducts();
        return ResponseEntity.ok(products);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ExtendedProductDto> getProductById(@PathVariable String id) {
        log.info("GET request to fetch product with id: {}", id);
        return productService.getProductById(id)
                .map(product -> ResponseEntity.ok(product))
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public ResponseEntity<ExtendedProductDto> createProduct(@RequestBody JsonNode productJson) {
        log.info("POST request to create product");
        try {
            ExtendedProductDto productDto = parseExtendedProductDto(productJson);
            ExtendedProductDto createdProduct = productService.createProduct(productDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdProduct);
        } catch (Exception e) {
            log.error("Error creating product", e);
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<ExtendedProductDto> updateProduct(@PathVariable String id, 
                                                           @RequestBody JsonNode productJson) {
        log.info("PUT request to update product with id: {}", id);
        try {
            ExtendedProductDto productDto = parseExtendedProductDto(productJson);
            productDto.setId(id); // Ensure the ID is set
            
            return productService.updateProduct(id, productDto)
                    .map(product -> ResponseEntity.ok(product))
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            log.error("Error updating product with id: {}", id, e);
            return ResponseEntity.badRequest().build();
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable String id) {
        log.info("DELETE request to delete product with id: {}", id);
        if (productService.deleteProduct(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
    
    @GetMapping("/category/{category}")
    public ResponseEntity<List<ExtendedProductDto>> getProductsByCategory(@PathVariable String category) {
        log.info("GET request to fetch products by category: {}", category);
        List<ExtendedProductDto> products = productService.getProductsByCategory(category);
        return ResponseEntity.ok(products);
    }
    
    @GetMapping("/search")
    public ResponseEntity<List<ExtendedProductDto>> searchProducts(@RequestParam String q) {
        log.info("GET request to search products with query: {}", q);
        List<ExtendedProductDto> products = productService.searchProducts(q);
        return ResponseEntity.ok(products);
    }
    
    @GetMapping("/low-stock")
    public ResponseEntity<List<ExtendedProductDto>> getLowStockProducts(@RequestParam(defaultValue = "10") Integer threshold) {
        log.info("GET request to fetch low stock products with threshold: {}", threshold);
        List<ExtendedProductDto> products = productService.getLowStockProducts(threshold);
        return ResponseEntity.ok(products);
    }
    
    // Backward compatibility endpoints
    @GetMapping("/basic")
    public ResponseEntity<List<ProductDto>> getAllProductsBasic() {
        log.info("GET request to fetch all products (basic)");
        List<ProductDto> products = productService.getAllProductsBasic();
        return ResponseEntity.ok(products);
    }
    
    @GetMapping("/{id}/basic")
    public ResponseEntity<ProductDto> getProductByIdBasic(@PathVariable String id) {
        log.info("GET request to fetch product with id: {} (basic)", id);
        return productService.getProductByIdBasic(id)
                .map(product -> ResponseEntity.ok(product))
                .orElse(ResponseEntity.notFound().build());
    }
    
    private ExtendedProductDto parseExtendedProductDto(JsonNode json) {
        ExtendedProductDto dto = new ExtendedProductDto();
        
        // Required fields
        if (json.has("name")) {
            dto.setName(json.get("name").asText());
        }
        if (json.has("price")) {
            dto.setPrice(new BigDecimal(json.get("price").asText()));
        }
        if (json.has("category")) {
            dto.setCategory(json.get("category").asText());
        }
        
        // Handle quantity fields - prioritize stockQuantity
        if (json.has("stockQuantity")) {
            Integer stockQuantity = json.get("stockQuantity").asInt();
            dto.setStockQuantity(stockQuantity);
            dto.setQuantity(stockQuantity); // Keep both in sync
        } else if (json.has("quantity")) {
            Integer quantity = json.get("quantity").asInt();
            dto.setQuantity(quantity);
            dto.setStockQuantity(quantity); // Keep both in sync
        }
        
        // Optional fields
        if (json.has("description")) {
            dto.setDescription(json.get("description").asText());
        }
        if (json.has("imageUrl")) {
            dto.setImageUrl(json.get("imageUrl").asText());
        }
        if (json.has("id")) {
            dto.setId(json.get("id").asText());
        }
        if (json.has("active")) {
            dto.setActive(json.get("active").asBoolean());
        }
        
        return dto;
    }
}