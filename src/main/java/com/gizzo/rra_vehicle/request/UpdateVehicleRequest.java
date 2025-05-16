package com.gizzo.rra_vehicle.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UpdateVehicleRequest {

    @Size(min = 1, message = "Vehicle model cannot be blank")
    private String model;

    @Size(min = 1, message = "Manufacturer cannot be blank")
    private String manufacturer;

    @Min(value = 1900, message = "Manufactured year must be valid (from 1900...)")
    @Max(value = 2025, message = "Manufactured year seems to be in the future")
    private Integer manufacturedYear;

    @Positive(message = "Price must be a positive number")
    private Integer price;

    @Size(min = 1, message = "Chassis number cannot be blank")
    private String chassisNumber;
}
