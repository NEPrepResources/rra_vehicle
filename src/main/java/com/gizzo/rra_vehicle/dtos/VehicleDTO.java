package com.gizzo.rra_vehicle.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class VehicleDTO {

    private UUID id;
    private String chassisNumber;
    private String model;
    private String manufacturer;
    private Integer manufacturedYear;
    private Integer price;
    private PlateDTO plate;

}
