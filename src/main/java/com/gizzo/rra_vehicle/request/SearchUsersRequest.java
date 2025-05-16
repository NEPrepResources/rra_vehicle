package com.gizzo.rra_vehicle.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SearchUsersRequest {

    private String nationalId;
    private String email;
    private String phone;

}
