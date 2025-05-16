package com.gizzo.rra_vehicle.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class RegisterVehicleRequest {

    @NotBlank(message = "Vehicle model is required")
    private String model;

    @NotBlank(message = "Manufacturer is required")
    private String manufacturer;

    @NotNull(message = "Manufactured year is required")
    @Min(value = 1900, message = "Manufactured year must be valid (from 1900 ...)") // for example start from 1900 year
    @Max(value = 2025, message = "Manufactured year seems to be in the future")
    private Integer manufacturedYear;

    @NotNull(message = "Price is required")
    @Positive(message = "Price must be a positive number")
    private Integer price;

    @NotBlank(message = "Chassis number is required")
    private String chassisNumber;

    @Pattern(
            regexp = "^RA[A-Z]\\d{3}[A-Z]$",
            message = "Plate number must this format like RAC123U"
    )
    private String plateNumber;

}
