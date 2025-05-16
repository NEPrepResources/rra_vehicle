package com.gizzo.rra_vehicle.controllers;

import com.gizzo.rra_vehicle.dtos.TransferDTO;
import com.gizzo.rra_vehicle.request.CreateTransferRequest;
import com.gizzo.rra_vehicle.response.APIResponse;
import com.gizzo.rra_vehicle.services.TransferService;
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

@Tag(name = "Transfers", description = "Endpoints for managing vehicle transfers")
@RestController
@RequestMapping("/api/v1/transfer")
@RequiredArgsConstructor
public class TransferController {

    private final TransferService transferService;

    @Operation(summary = "Create transfer", description = "Api for creating new transfer")
    @SecurityRequirement(name = "bearerAuth")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/create")
    public ResponseEntity<APIResponse<TransferDTO>> createTransfer(@Valid @RequestBody CreateTransferRequest createTransferRequest) {
        TransferDTO transfer = transferService.createTransfer(createTransferRequest);
        return new APIResponse<>("New transfer created successfully", HttpStatus.CREATED, transfer).toResponseEntity();
    }

    @Operation(summary = "Get transfer", description = "Api to get transfer by ID")
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/{transferId}")
    public ResponseEntity<APIResponse<TransferDTO>> getTransfer(@PathVariable UUID transferId) {
        TransferDTO transfer = transferService.getTransfer(transferId);
        return new APIResponse<>("Transfer retrieved successfully", HttpStatus.OK, transfer).toResponseEntity();
    }

    @Operation(summary = "Get all transfers", description = "Api to get all transfers")
    @SecurityRequirement(name = "bearerAuth")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/get-all")
    public ResponseEntity<APIResponse<List<TransferDTO>>> getAllTransfers() {
        List<TransferDTO> allTransfers = transferService.getAll();
        return new APIResponse<>("Transfer retrieved successfully", HttpStatus.OK, allTransfers).toResponseEntity();
    }

    @Operation(summary = "Get vehicle transfers", description = "Fetches transfers made on a vehicle by chassis number")
    @SecurityRequirement(name = "bearerAuth")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/get-by-vehicle/{vehicleChassis}")
    public ResponseEntity<APIResponse<List<TransferDTO>>> getVehicleTransfers(@PathVariable String vehicleChassis) {
        List<TransferDTO> vehicleTransfers = transferService.getVehicleTransfers(vehicleChassis);
        return new APIResponse<>("Vehicle transfers retrieved successfully", HttpStatus.OK, vehicleTransfers).toResponseEntity();
    }

    @Operation(summary = "Delete transfer", description = "Api to delete a transfer by ID")
    @SecurityRequirement(name = "bearerAuth")
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete/{transferId}")
    public ResponseEntity<APIResponse<String>> deleteTransfer(@PathVariable UUID transferId) {
        transferService.deleteTransfer(transferId);
        return new APIResponse<>("Transfer retrieved successfully", HttpStatus.OK, String.format("Transfer with ID %s deletes successfully", transferId)).toResponseEntity();
    }

}
