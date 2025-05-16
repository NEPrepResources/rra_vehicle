package com.gizzo.rra_vehicle.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SearchVehiclesRequest {

    private String chassisNumber;
    private String nationalId;
    private String plateNumber;

}
