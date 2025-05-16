package com.gizzo.rra_vehicle.request;

import com.gizzo.rra_vehicle.enums.VehicleStatus;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class UpdatePlateRequest {

    @Pattern(
            regexp = "^RA[A-Z][0-9]{3}[A-Z]$",
            message = "Plate number must be in format RAXXXXZ (e.g., RAC123U)"
    )
    private String number;

    private VehicleStatus plateStatus;

    private UUID newOwnerId;

}