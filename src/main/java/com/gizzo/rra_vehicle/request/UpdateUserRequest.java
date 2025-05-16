package com.gizzo.rra_vehicle.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UpdateUserRequest {

    @Size(min = 1, message = "Name cannot be blank")
    private String names;

    @Email(message = "Invalid email")
    private String email;

    @Pattern(
            regexp = "^[0-9]{16}$",
            message = "National ID must be exactly 16 digits"
    )
    private String nationalId;

    @Pattern(
            regexp = "^[A-Za-z0-9\\s,.'-]{3,}$",
            message = "Invalid address format"
    )
    private String address;

    @Pattern(
            regexp = "^(\\+2507|2507|07)[0-9]{8}$",
            message = "Phone must start with +2507, 2507 or 07 and be followed by 8 digits"
    )
    private String phone;

}
