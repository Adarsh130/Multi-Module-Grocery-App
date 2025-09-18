package com.groceryapp.controller;

import com.groceryapp.common.constants.AppConstants;
import com.groceryapp.common.dto.ApiResponseDto;
import com.groceryapp.common.dto.CustomerDto;
import com.groceryapp.service.CustomerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

/**
 * REST Controller for Customer operations
 */
@Slf4j
@RestController
@RequestMapping(AppConstants.API_BASE_PATH + AppConstants.CUSTOMERS_PATH)
@RequiredArgsConstructor
public class CustomerController {
    
    private final CustomerService customerService;
    
    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<ApiResponseDto<List<CustomerDto>>> getAllCustomers() {
        log.info("GET request to fetch all customers");
        List<CustomerDto> customers = customerService.getAllCustomers();
        return ResponseEntity.ok(ApiResponseDto.success(customers));
    }
    
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER') or hasRole('CUSTOMER')")
    public ResponseEntity<ApiResponseDto<CustomerDto>> getCustomerById(@PathVariable String id) {
        log.info("GET request to fetch customer with id: {}", id);
        CustomerDto customer = customerService.getCustomerById(id);
        return ResponseEntity.ok(ApiResponseDto.success(customer));
    }
    
    @GetMapping("/email/{email}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<ApiResponseDto<CustomerDto>> getCustomerByEmail(@PathVariable String email) {
        log.info("GET request to fetch customer with email: {}", email);
        CustomerDto customer = customerService.getCustomerByEmail(email);
        return ResponseEntity.ok(ApiResponseDto.success(customer));
    }
    
    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public ResponseEntity<ApiResponseDto<CustomerDto>> createCustomer(@Valid @RequestBody CustomerDto customerDto) {
        log.info("POST request to create customer: {}", customerDto.getName());
        CustomerDto createdCustomer = customerService.createCustomer(customerDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponseDto.success("Customer created successfully", createdCustomer));
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER') or hasRole('CUSTOMER')")
    public ResponseEntity<ApiResponseDto<CustomerDto>> updateCustomer(@PathVariable String id, 
                                                                     @Valid @RequestBody CustomerDto customerDto) {
        log.info("PUT request to update customer with id: {}", id);
        CustomerDto updatedCustomer = customerService.updateCustomer(id, customerDto);
        return ResponseEntity.ok(ApiResponseDto.success("Customer updated successfully", updatedCustomer));
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponseDto<Void>> deleteCustomer(@PathVariable String id) {
        log.info("DELETE request to delete customer with id: {}", id);
        customerService.deleteCustomer(id);
        return ResponseEntity.ok(ApiResponseDto.success("Customer deleted successfully", null));
    }
}