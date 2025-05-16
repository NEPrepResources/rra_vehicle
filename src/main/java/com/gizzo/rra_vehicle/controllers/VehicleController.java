package com.gizzo.rra_vehicle.controllers;

import com.gizzo.rra_vehicle.dtos.VehicleDTO;
import com.gizzo.rra_vehicle.request.RegisterVehicleRequest;
import com.gizzo.rra_vehicle.request.SearchVehiclesRequest;
import com.gizzo.rra_vehicle.request.UpdateVehicleRequest;
import com.gizzo.rra_vehicle.response.APIResponse;
import com.gizzo.rra_vehicle.services.VehicleService;
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

@Tag(name = "Vehicles", description = "Endpoints for managing vehicles")
@RestController
@RequestMapping("/api/v1/vehicle")
@RequiredArgsConstructor
public class VehicleController {

    private final VehicleService vehicleService;

    @Operation(summary = "Register vehicle", description = "Registers a new vehicle")
    @SecurityRequirement(name = "bearerAuth")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/register")
    public ResponseEntity<APIResponse<VehicleDTO>> registerVehicle(@Valid @RequestBody RegisterVehicleRequest registerVehicleRequest) {
        VehicleDTO newVehicle = vehicleService.registerVehicle(registerVehicleRequest);
        return new APIResponse<>("Vehicle created successfully", HttpStatus.CREATED, newVehicle).toResponseEntity();
    }

    @Operation(summary = "Get vehicle by ID", description = "Fetches a vehicle using UUID")
    @SecurityRequirement(name = "bearerAuth")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{vehicleId}")
    public ResponseEntity<APIResponse<VehicleDTO>> getVehicle(@PathVariable UUID vehicleId) {
        VehicleDTO vehicle = vehicleService.getVehicle(vehicleId);
        return new APIResponse<>("Vehicle retrieved successfully", HttpStatus.OK, vehicle).toResponseEntity();
    }

    @Operation(summary = "Get vehicle by plate", description = "Fetches a vehicle using Plate number (Ex: RAC345U)")
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/get-by-plate/{plateNumber}")
    public ResponseEntity<APIResponse<VehicleDTO>> getVehicleByPlateNumber(@PathVariable String plateNumber) {
        VehicleDTO vehicle = vehicleService.getVehicleByPlateNumber(plateNumber);
        return new APIResponse<>("Vehicle retrieved successfully", HttpStatus.OK, vehicle).toResponseEntity();
    }

    @Operation(summary = "Get all vehicles", description = "Fetches all vehicles")
    @SecurityRequirement(name = "bearerAuth")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/get-all")
    public ResponseEntity<APIResponse<List<VehicleDTO>>> getAll() {
        List<VehicleDTO> allVehicles = vehicleService.getAllVehicles();
        return new APIResponse<>("All vehicles retrieved successfully", HttpStatus.OK, allVehicles).toResponseEntity();
    }

    @Operation(summary = "Get vehicles by User", description = "Fetches all vehicles owned by a user")
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/get-by-user/{userId}")
    public ResponseEntity<APIResponse<List<VehicleDTO>>> getVehicleByPlateNumber(@PathVariable UUID userId) {
        List<VehicleDTO> userVehicles = vehicleService.getUserVehicles(userId);
        return new APIResponse<>("User vehicles retrieved successfully", HttpStatus.OK, userVehicles).toResponseEntity();
    }

    @Operation(summary = "Get vehicle by chassis number", description = "Fetches a vehicle using chassis number")
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/get-by-chassis/{chassisNumber}")
    public ResponseEntity<APIResponse<VehicleDTO>> getVehicleByChassisNumber(@PathVariable String chassisNumber) {
        VehicleDTO vehicle = vehicleService.getVehicleByChassisNumber(chassisNumber);
        return new APIResponse<>("Vehicle retrieved successfully", HttpStatus.OK, vehicle).toResponseEntity();
    }

    @Operation(summary = "Update vehicle", description = "Api for updating vehicle")
    @SecurityRequirement(name = "bearerAuth")
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/update/{vehicleId}")
    public ResponseEntity<APIResponse<VehicleDTO>> updateVehicle(@PathVariable UUID vehicleId, @Valid @RequestBody UpdateVehicleRequest updateVehicleRequest) {
        VehicleDTO updatedVehicle = vehicleService.updateVehicle(vehicleId, updateVehicleRequest);
        return new APIResponse<>("Vehicle updated successfully", HttpStatus.OK, updatedVehicle).toResponseEntity();
    }

    @Operation(summary = "Delete vehicle", description = "Api for deleting vehicle by UUID")
    @SecurityRequirement(name = "bearerAuth")
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete/{vehicleId}")
    public ResponseEntity<APIResponse<String>> deleteVehicle(@PathVariable UUID vehicleId) {
        vehicleService.deleteVehicle(vehicleId);
        return new APIResponse<>("Vehicle deleted successfully", HttpStatus.OK, String.format("Vehicle of %s ID was deleted", vehicleId)).toResponseEntity();
    }

    @Operation(summary = "Search vehicles", description = "Api for searching vehicle using plate, chassis number or owner's National ID")
    @SecurityRequirement(name = "bearerAuth")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/search")
    public ResponseEntity<APIResponse<List<VehicleDTO>>> searchVehicles(@ModelAttribute SearchVehiclesRequest searchVehiclesRequest) {
        List<VehicleDTO> resultVehicles = vehicleService.searchVehicles(searchVehiclesRequest);
        return new APIResponse<>("Results form vehicle search", HttpStatus.OK, resultVehicles).toResponseEntity();
    }

}
