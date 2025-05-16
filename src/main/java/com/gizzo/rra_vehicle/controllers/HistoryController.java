package com.gizzo.rra_vehicle.controllers;

import com.gizzo.rra_vehicle.dtos.HistoryDTO;
import com.gizzo.rra_vehicle.response.APIResponse;
import com.gizzo.rra_vehicle.services.HistoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@Tag(name = "History", description = "Api endpoints for history")
@RestController
@RequestMapping("/api/v1/history")
@RequiredArgsConstructor
public class HistoryController {

    private final HistoryService historyService;

    @Operation(summary = "Get history by ID", description = "Fetches history record using UUID")
    @SecurityRequirement(name = "bearerAuth")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<APIResponse<HistoryDTO>> getHistory(@PathVariable UUID id) {
      HistoryDTO history = historyService.getHistory(id);
      return new APIResponse<>("History retrieved successfully", HttpStatus.OK, history).toResponseEntity();
    };

    @Operation(summary = "Get vehicle history", description = "Fetches vehicle history using chassis number")
    @SecurityRequirement(name = "bearerAuth")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/get-by-vehicle/{chassisNumber}")
    public ResponseEntity<APIResponse<List<HistoryDTO>>> getVehicleHistory(@PathVariable String chassisNumber) {
        List<HistoryDTO> vehicleHistory = historyService.getVehicleHistory(chassisNumber);
        return new APIResponse<>("Vehicle history retrieved successfully", HttpStatus.OK, vehicleHistory).toResponseEntity();
    };

    @Operation(summary = "Get all history", description = "Fetches all history records")
    @SecurityRequirement(name = "bearerAuth")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/get-all")
    public ResponseEntity<APIResponse<List<HistoryDTO>>> getAll() {
        List<HistoryDTO> allHistory = historyService.getAll();
        return new APIResponse<>("All history retrieved successfully", HttpStatus.OK, allHistory).toResponseEntity();
    };


}
