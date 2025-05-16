package com.gizzo.rra_vehicle.controllers;

import com.gizzo.rra_vehicle.dtos.UserDTO;
import com.gizzo.rra_vehicle.request.SearchUsersRequest;
import com.gizzo.rra_vehicle.request.UpdateUserRequest;
import com.gizzo.rra_vehicle.response.APIResponse;
import com.gizzo.rra_vehicle.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(name = "Users", description = "Endpoints for managing users")
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @Operation(summary = "Get user by ID", description = "Fetches a user using UUID")
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/{userId}")
    public ResponseEntity<APIResponse<UserDTO>> getUser(@PathVariable UUID userId) {
        UserDTO user = userService.getUser(userId);
        return new APIResponse<>("User retrieved successfully", HttpStatus.OK, user).toResponseEntity();
    }

    @Operation(summary = "Get all users", description = "Fetches all users")
    @SecurityRequirement(name = "bearerAuth")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/all")
    public ResponseEntity<APIResponse<List<UserDTO>>> getAllUser() {
        List<UserDTO> allUsers = userService.getAllUsers();
        return new APIResponse<>("All users retrieved successfully", HttpStatus.OK, allUsers).toResponseEntity();
    }

    @Operation(summary = "Update user", description = "Api for updating user by ID")
    @SecurityRequirement(name = "bearerAuth")
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/update/{userId}")
    public ResponseEntity<APIResponse<UserDTO>> updateUser(@PathVariable UUID userId, @Valid @RequestBody UpdateUserRequest updateUserRequest) {
        UserDTO updatedUser = userService.updateUser(userId, updateUserRequest);
        return new APIResponse<>("User updated successfully", HttpStatus.OK, updatedUser).toResponseEntity();
    }

    @Operation(summary = "Delete user", description = "Api for deleting user by ID")
    @SecurityRequirement(name = "bearerAuth")
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<APIResponse<String>> deleteUser(@PathVariable UUID userId) {
        userService.deleteUser(userId);
        return new APIResponse<>("User deleted successfully", HttpStatus.OK, String.format("User with ID %s is now deleted", userId)).toResponseEntity();
    }

    @Operation(summary = "Search user", description = "Api for searching user by email, phone or National ID")
    @SecurityRequirement(name = "bearerAuth")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/search")
    public ResponseEntity<APIResponse<List<UserDTO>>> searchUsers(@ModelAttribute SearchUsersRequest searchUsersRequest) {
        List<UserDTO> resultUsers = userService.searchUsers(searchUsersRequest);
        return new APIResponse<>("Results from your user search", HttpStatus.OK, resultUsers).toResponseEntity();
    }
}
