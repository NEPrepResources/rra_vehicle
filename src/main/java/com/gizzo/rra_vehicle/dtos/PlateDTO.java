package com.gizzo.rra_vehicle.dtos;

import com.gizzo.rra_vehicle.enums.VehicleStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class PlateDTO {

    private UUID id;
    private String number;
    private VehicleStatus plateStatus;
    private UserDTO owner;

}
