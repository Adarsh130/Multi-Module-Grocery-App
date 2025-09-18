package com.groceryapp.controller;

import com.groceryapp.common.constants.AppConstants;
import com.groceryapp.common.dto.ApiResponseDto;
import com.groceryapp.common.dto.AuthRequestDto;
import com.groceryapp.common.dto.AuthResponseDto;
import com.groceryapp.common.dto.UserDto;
import com.groceryapp.security.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

/**
 * REST Controller for Authentication operations
 */
@Slf4j
@RestController
@RequestMapping(AppConstants.API_BASE_PATH + AppConstants.AUTH_PATH)
@RequiredArgsConstructor
public class AuthController {
    
    private final AuthService authService;
    
    @PostMapping("/login")
    public ResponseEntity<ApiResponseDto<AuthResponseDto>> login(@Valid @RequestBody AuthRequestDto authRequest) {
        log.info("POST request to login user: {}", authRequest.getUsername());
        AuthResponseDto response = authService.login(authRequest);
        return ResponseEntity.ok(ApiResponseDto.success("Login successful", response));
    }
    
    @PostMapping("/register")
    public ResponseEntity<ApiResponseDto<AuthResponseDto>> register(@Valid @RequestBody UserDto userDto) {
        log.info("POST request to register user: {}", userDto.getUsername());
        AuthResponseDto response = authService.register(userDto);
        return ResponseEntity.ok(ApiResponseDto.success("Registration successful", response));
    }
    
    @GetMapping("/me")
    public ResponseEntity<ApiResponseDto<UserDto>> getCurrentUser() {
        log.info("GET request to fetch current user");
        UserDto user = authService.getCurrentUser();
        return ResponseEntity.ok(ApiResponseDto.success(user));
    }
}