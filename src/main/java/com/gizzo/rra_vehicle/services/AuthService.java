package com.gizzo.rra_vehicle.services;

import com.gizzo.rra_vehicle.dtos.UserDTO;
import com.gizzo.rra_vehicle.entities.User;
import com.gizzo.rra_vehicle.enums.Roles;
import com.gizzo.rra_vehicle.exceptions.ResourceConflictException;
import com.gizzo.rra_vehicle.exceptions.UnAuthorizedException;
import com.gizzo.rra_vehicle.repositories.UserRepository;
import com.gizzo.rra_vehicle.request.LoginRequest;
import com.gizzo.rra_vehicle.request.RegisterRequest;
import com.gizzo.rra_vehicle.response.AuthResponse;
import com.gizzo.rra_vehicle.security.service.JwtService;
import com.gizzo.rra_vehicle.security.service.UserDetailsServiceImpl;
import com.gizzo.rra_vehicle.services.impl.IAuthImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService implements IAuthImpl {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserDetailsServiceImpl userDetailsService;
    private final JwtService jwtService;


    @Override
    public UserDTO registerUser(RegisterRequest registerRequest) {

        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new ResourceConflictException("Email already exists");
        }
        if (userRepository.existsByPhone(registerRequest.getPhone())) {
            throw new ResourceConflictException("Phone number already exists");
        }
        if (userRepository.existsByNationalId(registerRequest.getNationalId())) {
            throw new ResourceConflictException("National ID already exists");
        }

        String hashedPassword = passwordEncoder.encode(registerRequest.getPassword());

        User newUser = User.builder()
                .names(registerRequest.getNames())
                .phone(registerRequest.getPhone())
                .address(registerRequest.getAddress())
                .email(registerRequest.getEmail())
                .nationalId(registerRequest.getNationalId())
                .password(hashedPassword)
                .build();

        if (registerRequest.getRole() == null) {
            newUser.setRole(Roles.STANDARD);
        }
        newUser.setRole(registerRequest.getRole());

        userRepository.save(newUser);

        return UserDTO.builder()
                .id(newUser.getId())
                .names(newUser.getNames())
                .email(newUser.getEmail())
                .phone(newUser.getPhone())
                .nationalId(newUser.getNationalId())
                .address(newUser.getEmail())
                .role(newUser.getRole())
                .build();
    }

    @Override
    public AuthResponse login(LoginRequest loginRequest) {
        User user = userRepository.findByEmail(loginRequest.getEmail()).orElseThrow(
                () -> new UnAuthorizedException("Invalid email or password")
        );

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new UnAuthorizedException("Invalid email or password");
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.getEmail());
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities()
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        Map<String, Object> claims = new HashMap<>();
        claims.put("email", userDetails.getUsername());
        claims.put("role", userDetails.getAuthorities());
        String token = jwtService.generateToken(claims, userDetails);

        UserDTO userDTO = UserDTO.builder()
                .id(user.getId())
                .names(user.getNames())
                .email(user.getEmail())
                .address(user.getAddress())
                .role(user.getRole())
                .nationalId(user.getNationalId())
                .phone(user.getPhone())
                .build();

        return new AuthResponse(token, userDTO);
    }
}
