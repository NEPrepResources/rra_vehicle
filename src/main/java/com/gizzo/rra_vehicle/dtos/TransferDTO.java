package com.gizzo.rra_vehicle.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class TransferDTO {

    private UUID id;
    private UserDTO previousOwner;
    private UserDTO newOwner;
    private Integer purchasePrice;
    private VehicleDTO vehicle;
    private LocalDateTime transferDate;

}
