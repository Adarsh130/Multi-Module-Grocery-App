package com.groceryapp.controller;

import com.groceryapp.common.constants.AppConstants;
import com.groceryapp.common.dto.ApiResponseDto;
import com.groceryapp.common.dto.UserDto;
import com.groceryapp.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

/**
 * REST Controller for User management operations
 */
@Slf4j
@RestController
@RequestMapping(AppConstants.API_BASE_PATH + "/users")
@RequiredArgsConstructor
public class UserController {
    
    private final UserService userService;
    
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponseDto<List<UserDto>>> getAllUsers() {
        log.info("GET request to fetch all users");
        List<UserDto> users = userService.getAllUsers();
        return ResponseEntity.ok(ApiResponseDto.success(users));
    }
    
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('CUSTOMER')")
    public ResponseEntity<ApiResponseDto<UserDto>> getUserById(@PathVariable String id) {
        log.info("GET request to fetch user with id: {}", id);
        UserDto user = userService.getUserById(id);
        return ResponseEntity.ok(ApiResponseDto.success(user));
    }
    
    @GetMapping("/username/{username}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponseDto<UserDto>> getUserByUsername(@PathVariable String username) {
        log.info("GET request to fetch user with username: {}", username);
        UserDto user = userService.getUserByUsername(username);
        return ResponseEntity.ok(ApiResponseDto.success(user));
    }
    
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponseDto<UserDto>> createUser(@Valid @RequestBody UserDto userDto) {
        log.info("POST request to create user: {}", userDto.getUsername());
        UserDto createdUser = userService.createUser(userDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponseDto.success("User created successfully", createdUser));
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('CUSTOMER')")
    public ResponseEntity<ApiResponseDto<UserDto>> updateUser(@PathVariable String id, 
                                                             @Valid @RequestBody UserDto userDto) {
        log.info("PUT request to update user with id: {}", id);
        UserDto updatedUser = userService.updateUser(id, userDto);
        return ResponseEntity.ok(ApiResponseDto.success("User updated successfully", updatedUser));
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponseDto<Void>> deleteUser(@PathVariable String id) {
        log.info("DELETE request to delete user with id: {}", id);
        userService.deleteUser(id);
        return ResponseEntity.ok(ApiResponseDto.success("User deleted successfully", null));
    }
}