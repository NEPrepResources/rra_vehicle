package com.gizzo.rra_vehicle.controllers;

import com.gizzo.rra_vehicle.dtos.PlateDTO;
import com.gizzo.rra_vehicle.request.RegisterPlateRequest;
import com.gizzo.rra_vehicle.request.UpdatePlateRequest;
import com.gizzo.rra_vehicle.response.APIResponse;
import com.gizzo.rra_vehicle.services.PlateService;
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

@Tag(name = "Plates", description = "Endpoints for plate management")
@RestController
@RequestMapping("/api/v1/plate")
@RequiredArgsConstructor
public class PlateController {

    private final PlateService plateService;

    @Operation(summary = "Register plate", description = "Registers a new plate")
    @SecurityRequirement(name = "bearerAuth")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/new")
    public ResponseEntity<APIResponse<PlateDTO>> createPlate(@Valid @RequestBody RegisterPlateRequest registerPlateRequest) {
        PlateDTO newPlate = plateService.registerPlate(registerPlateRequest);
        return new APIResponse<>("New plate created successfully", HttpStatus.CREATED, newPlate).toResponseEntity();
    }

    @Operation(summary = "Get plate by ID", description = "Fetches a plate using UUID")
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/{id}")
    public ResponseEntity<APIResponse<PlateDTO>> getPlate(@PathVariable UUID plateId) {
        PlateDTO plate = plateService.getPlate(plateId);
        return new APIResponse<>("Plate retrieved successfully", HttpStatus.OK, plate).toResponseEntity();
    }

    @Operation(summary = "Get plate by plate number", description = "Fetches a plate using plate number (Ex: RAD345U)")
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/get-by-number/{plateNumber}")
    public ResponseEntity<APIResponse<PlateDTO>> getPlateByNumber(@PathVariable String plateNumber) {
        PlateDTO plate = plateService.getPlateByNumber(plateNumber);
        return new APIResponse<>("Plate retrieved successfully", HttpStatus.OK, plate).toResponseEntity();
    }

    @Operation(summary = "Get user plates by user ID", description = "Fetches all user plates by using user ID")
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/user-plates/{userId}")
    public ResponseEntity<APIResponse<List<PlateDTO>>> getUserPlates(@PathVariable UUID userId) {
        List<PlateDTO> userPlates = plateService.getUserPlates(userId);
        return new APIResponse<>("User plates retrieved successfully", HttpStatus.OK, userPlates).toResponseEntity();
    }

    @Operation(summary = "Get all plates ", description = "Fetches all plates")
    @SecurityRequirement(name = "bearerAuth")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/get-all")
    public ResponseEntity<APIResponse<List<PlateDTO>>> getAll() {
        List<PlateDTO> allPlates = plateService.getAllPlates();
        return new APIResponse<>("All plates retrieved successfully", HttpStatus.OK, allPlates).toResponseEntity();
    }

    @Operation(summary = "Update a plate", description = "Api for updating a plate")
    @SecurityRequirement(name = "bearerAuth")
    @PatchMapping("/update/{plateId}")
    public ResponseEntity<APIResponse<PlateDTO>> updatePlate(@PathVariable UUID plateId, @Valid @RequestBody UpdatePlateRequest updatePlateRequest) {
        PlateDTO updatedPlate = plateService.updatePlate(plateId, updatePlateRequest);
        return new APIResponse<>("Plate updated successfully", HttpStatus.OK, updatedPlate).toResponseEntity();
    };

    @Operation(summary = "Delete plate by ID", description = "Api for deleting a plate by Id")
    @SecurityRequirement(name = "bearerAuth")
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete/{plateId}")
    public ResponseEntity<APIResponse<String>> deletePlate(@PathVariable UUID plateId) {
        plateService.deletePlate(plateId);
        return new APIResponse<>("Plate deleted successfully", HttpStatus.OK, String.format("Plate with %s was deleted", plateId)).toResponseEntity();
    };
}
