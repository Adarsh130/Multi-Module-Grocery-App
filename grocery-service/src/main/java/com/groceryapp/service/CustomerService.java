package com.groceryapp.service;

import com.groceryapp.common.dto.CustomerDto;
import com.groceryapp.common.exception.BadRequestException;
import com.groceryapp.common.exception.ResourceNotFoundException;
import com.groceryapp.persistence.model.Customer;
import com.groceryapp.persistence.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service class for Customer operations
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerService {
    
    private final CustomerRepository customerRepository;
    
    public List<CustomerDto> getAllCustomers() {
        log.info("Fetching all customers");
        return customerRepository.findAll()
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    public CustomerDto getCustomerById(String id) {
        log.info("Fetching customer with id: {}", id);
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + id));
        return convertToDto(customer);
    }
    
    public CustomerDto getCustomerByEmail(String email) {
        log.info("Fetching customer with email: {}", email);
        Customer customer = customerRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with email: " + email));
        return convertToDto(customer);
    }
    
    public CustomerDto createCustomer(CustomerDto customerDto) {
        log.info("Creating new customer: {}", customerDto.getName());
        
        // Check if customer already exists
        if (customerRepository.existsByEmail(customerDto.getEmail())) {
            throw new BadRequestException("Customer already exists with email: " + customerDto.getEmail());
        }
        
        if (customerRepository.existsByPhoneNumber(customerDto.getPhoneNumber())) {
            throw new BadRequestException("Customer already exists with phone number: " + customerDto.getPhoneNumber());
        }
        
        Customer customer = convertToEntity(customerDto);
        Customer savedCustomer = customerRepository.save(customer);
        return convertToDto(savedCustomer);
    }
    
    public CustomerDto updateCustomer(String id, CustomerDto customerDto) {
        log.info("Updating customer with id: {}", id);
        
        Customer existingCustomer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + id));
        
        // Check if email is being changed and if it already exists
        if (!existingCustomer.getEmail().equals(customerDto.getEmail()) && 
            customerRepository.existsByEmail(customerDto.getEmail())) {
            throw new BadRequestException("Customer already exists with email: " + customerDto.getEmail());
        }
        
        // Check if phone number is being changed and if it already exists
        if (!existingCustomer.getPhoneNumber().equals(customerDto.getPhoneNumber()) && 
            customerRepository.existsByPhoneNumber(customerDto.getPhoneNumber())) {
            throw new BadRequestException("Customer already exists with phone number: " + customerDto.getPhoneNumber());
        }
        
        existingCustomer.setName(customerDto.getName());
        existingCustomer.setEmail(customerDto.getEmail());
        existingCustomer.setPhoneNumber(customerDto.getPhoneNumber());
        
        Customer updatedCustomer = customerRepository.save(existingCustomer);
        return convertToDto(updatedCustomer);
    }
    
    public void deleteCustomer(String id) {
        log.info("Deleting customer with id: {}", id);
        if (!customerRepository.existsById(id)) {
            throw new ResourceNotFoundException("Customer not found with id: " + id);
        }
        customerRepository.deleteById(id);
    }
    
    private CustomerDto convertToDto(Customer customer) {
        return new CustomerDto(
                customer.getId(),
                customer.getName(),
                customer.getEmail(),
                customer.getPhoneNumber(),
                customer.getProductsBought()
        );
    }
    
    private Customer convertToEntity(CustomerDto customerDto) {
        return new Customer(
                customerDto.getId(),
                customerDto.getName(),
                customerDto.getEmail(),
                customerDto.getPhoneNumber(),
                customerDto.getProductsBought()
        );
    }
}