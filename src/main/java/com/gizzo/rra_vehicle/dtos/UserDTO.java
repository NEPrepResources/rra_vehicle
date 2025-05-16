package com.gizzo.rra_vehicle.dtos;

import com.gizzo.rra_vehicle.enums.Roles;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class UserDTO {

    private UUID id;
    private String names;
    private String email;
    private String phone;
    private String nationalId;
    private String address;
    private Roles role;

}
