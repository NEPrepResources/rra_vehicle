package com.gizzo.rra_vehicle.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class RegisterPlateRequest {

    @Pattern(
            regexp = "^RA[A-Z][0-9]{3}[A-Z]$",
            message = "Plate number must be in format RAXXXXZ (e.g., RAC123U)"
    )
    @NotNull(message = "Plate number is required")
    private String number;

    @NotNull(message = "Id of owner is required")
    private UUID ownerId;

}
