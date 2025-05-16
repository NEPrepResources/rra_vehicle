package com.gizzo.rra_vehicle.controllers;

import com.gizzo.rra_vehicle.dtos.UserDTO;
import com.gizzo.rra_vehicle.request.LoginRequest;
import com.gizzo.rra_vehicle.request.RegisterRequest;
import com.gizzo.rra_vehicle.response.APIResponse;
import com.gizzo.rra_vehicle.response.AuthResponse;
import com.gizzo.rra_vehicle.services.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Authentication", description = "Endpoints for user authentication and registration")
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "Register new user", description = "Create new user account")
    @ApiResponse(responseCode = "200", description = "User successfully registered")
    @PostMapping("/register")
    public ResponseEntity<APIResponse<UserDTO>> registerUser(@Valid @RequestBody RegisterRequest registerRequest) {
        UserDTO user = authService.registerUser(registerRequest);
        return new APIResponse<>("User created successfully", HttpStatus.CREATED, user).toResponseEntity();
    }

    @Operation(summary = "User login", description = "Authenticate user and return JWT token")
    @ApiResponse(responseCode = "200", description = "User successfully logged in")
    @PostMapping("/login")
    public ResponseEntity<APIResponse<AuthResponse>> registerUser(@Valid @RequestBody LoginRequest loginRequest) {
        AuthResponse loggedInUser = authService.login(loginRequest);
        return new APIResponse<>("User logged in successfully", HttpStatus.OK, loggedInUser).toResponseEntity();
    }
    
}
