package com.gizzo.rra_vehicle.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class CreateTransferRequest {

    @NotBlank(message = "Vehicle chassis number is required")
    private String vehicleChassis;

    @NotNull(message = "Purchase price is required")
    @Positive(message = "Purchase price must be a positive number")
    private Integer purchasePrice;

    @NotNull(message = "New owner ID is required")
    private UUID newOwnerId;

    @NotNull(message = "Transfer date is required")
    @PastOrPresent(message = "Transfer date cannot be in the future")
    private LocalDateTime transferDate;

}
