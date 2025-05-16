package com.gizzo.rra_vehicle.services.impl;

import com.gizzo.rra_vehicle.dtos.UserDTO;
import com.gizzo.rra_vehicle.request.LoginRequest;
import com.gizzo.rra_vehicle.request.RegisterRequest;
import com.gizzo.rra_vehicle.response.AuthResponse;

public interface IAuthImpl {

    UserDTO registerUser(RegisterRequest registerRequest);
    AuthResponse login(LoginRequest loginRequest);
}
