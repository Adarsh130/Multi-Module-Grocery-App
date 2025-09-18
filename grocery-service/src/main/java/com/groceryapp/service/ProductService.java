package com.groceryapp.service;

import com.groceryapp.common.dto.ProductDto;
import com.groceryapp.common.dto.ExtendedProductDto;
import com.groceryapp.persistence.model.Product;
import com.groceryapp.persistence.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service class for Product operations
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {
    
    private final ProductRepository productRepository;
    
    public List<ExtendedProductDto> getAllProducts() {
        log.info("Fetching all products");
        return productRepository.findByActiveTrue()
                .stream()
                .map(this::convertToExtendedDto)
                .collect(Collectors.toList());
    }
    
    public Optional<ExtendedProductDto> getProductById(String id) {
        log.info("Fetching product with id: {}", id);
        return productRepository.findByIdAndActiveTrue(id)
                .map(this::convertToExtendedDto);
    }
    
    public ExtendedProductDto createProduct(ExtendedProductDto productDto) {
        log.info("Creating new product: {}", productDto.getName());
        Product product = convertToEntity(productDto);
        product.setCreatedAt(LocalDateTime.now());
        product.setUpdatedAt(LocalDateTime.now());
        product.setActive(true);
        Product savedProduct = productRepository.save(product);
        return convertToExtendedDto(savedProduct);
    }
    
    public Optional<ExtendedProductDto> updateProduct(String id, ExtendedProductDto productDto) {
        log.info("Updating product with id: {}", id);
        return productRepository.findById(id)
                .map(existingProduct -> {
                    // Update all fields from DTO
                    existingProduct.setName(productDto.getName());
                    existingProduct.setDescription(productDto.getDescription());
                    existingProduct.setPrice(productDto.getPrice());
                    existingProduct.setCategory(productDto.getCategory());
                    existingProduct.setImageUrl(productDto.getImageUrl());
                    
                    // Handle stock quantity properly
                    Integer stockQty = productDto.getStockQuantity() != null ? 
                                      productDto.getStockQuantity() : productDto.getQuantity();
                    log.debug("Updating product {} stock quantity from {} to {}", 
                             existingProduct.getName(), existingProduct.getStockQuantity(), stockQty);
                    existingProduct.setStockQuantity(stockQty);
                    // Note: Product entity only has stockQuantity field
                    
                    existingProduct.setUpdatedAt(LocalDateTime.now());
                    Product savedProduct = productRepository.save(existingProduct);
                    return convertToExtendedDto(savedProduct);
                });
    }
    
    public boolean deleteProduct(String id) {
        log.info("Deleting product with id: {}", id);
        return productRepository.findById(id)
                .map(product -> {
                    // Soft delete - mark as inactive
                    product.setActive(false);
                    product.setUpdatedAt(LocalDateTime.now());
                    productRepository.save(product);
                    return true;
                })
                .orElse(false);
    }
    
    public List<ExtendedProductDto> getProductsByCategory(String category) {
        log.info("Fetching products by category: {}", category);
        return productRepository.findByCategoryAndActiveTrue(category)
                .stream()
                .map(this::convertToExtendedDto)
                .collect(Collectors.toList());
    }
    
    public List<ExtendedProductDto> searchProducts(String searchTerm) {
        log.info("Searching products with term: {}", searchTerm);
        return productRepository.searchProducts(searchTerm)
                .stream()
                .map(this::convertToExtendedDto)
                .collect(Collectors.toList());
    }
    
    public List<ExtendedProductDto> getLowStockProducts(Integer threshold) {
        log.info("Fetching low stock products with threshold: {}", threshold);
        return productRepository.findLowStockProducts(threshold)
                .stream()
                .map(this::convertToExtendedDto)
                .collect(Collectors.toList());
    }
    
    // Backward compatibility methods
    public List<ProductDto> getAllProductsBasic() {
        return getAllProducts().stream()
                .map(this::convertToBasicDto)
                .collect(Collectors.toList());
    }
    
    public Optional<ProductDto> getProductByIdBasic(String id) {
        return getProductById(id).map(this::convertToBasicDto);
    }
    
    private ExtendedProductDto convertToExtendedDto(Product product) {
        ExtendedProductDto dto = new ExtendedProductDto();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setDescription(product.getDescription());
        dto.setQuantity(product.getStockQuantity());
        dto.setStockQuantity(product.getStockQuantity());
        dto.setPrice(product.getPrice());
        dto.setCategory(product.getCategory());
        dto.setImageUrl(product.getImageUrl());
        dto.setActive(product.isActive());
        return dto;
    }
    
    private ProductDto convertToBasicDto(ExtendedProductDto extendedDto) {
        return new ProductDto(
                extendedDto.getId(),
                extendedDto.getName(),
                extendedDto.getQuantity(),
                extendedDto.getPrice(),
                extendedDto.getCategory()
        );
    }
    
    private Product convertToEntity(ExtendedProductDto productDto) {
        Product product = new Product();
        product.setId(productDto.getId());
        product.setName(productDto.getName());
        product.setDescription(productDto.getDescription());
        product.setPrice(productDto.getPrice());
        product.setCategory(productDto.getCategory());
        product.setImageUrl(productDto.getImageUrl());
        
        // Handle stock quantity properly
        Integer stockQty = productDto.getStockQuantity() != null ? 
                          productDto.getStockQuantity() : productDto.getQuantity();
        product.setStockQuantity(stockQty);
        // Note: Product entity only has stockQuantity field
        
        product.setActive(productDto.isActive());
        return product;
    }
}