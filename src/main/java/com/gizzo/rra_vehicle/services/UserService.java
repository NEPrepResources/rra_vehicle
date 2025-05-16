package com.gizzo.rra_vehicle.services;

import com.gizzo.rra_vehicle.dtos.UserDTO;
import com.gizzo.rra_vehicle.entities.User;
import com.gizzo.rra_vehicle.exceptions.BadRequestException;
import com.gizzo.rra_vehicle.repositories.UserRepository;
import com.gizzo.rra_vehicle.request.SearchUsersRequest;
import com.gizzo.rra_vehicle.request.UpdateUserRequest;
import com.gizzo.rra_vehicle.services.impl.IUserImpl;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService implements IUserImpl {

    private final UserRepository userRepository;

    @Override
    public UserDTO getUser(UUID userId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new BadRequestException(String.format("User with ID %s isn't registered", userId))
        );

     return this.mapToDto(user);
    }

    @Override
    public UserDTO updateUser(UUID userId, UpdateUserRequest updateUserRequest) {

        User existingUser = userRepository.findById(userId).orElseThrow(
                () -> new BadRequestException(String.format("User with ID %s isn't registered", userId))
        );

        boolean canUpdateInDb = false;

        if(updateUserRequest.getNames() != null) {
            canUpdateInDb = true;
            existingUser.setNames(updateUserRequest.getNames());
        }

        if(updateUserRequest.getNationalId() != null) {
            canUpdateInDb = true;
            existingUser.setNationalId(updateUserRequest.getNationalId());
        }

        if(updateUserRequest.getEmail() != null) {
            canUpdateInDb = true;
            existingUser.setEmail(updateUserRequest.getEmail());
        }

        if(updateUserRequest.getPhone() != null) {
            canUpdateInDb = true;
            existingUser.setPhone(updateUserRequest.getPhone());
        }

        if(updateUserRequest.getAddress() != null) {
            canUpdateInDb = true;
            existingUser.setAddress(updateUserRequest.getAddress());
        }

        if(canUpdateInDb) userRepository.save(existingUser);

        return this.mapToDto(existingUser);
    }

    @Override
    public void deleteUser(UUID userId) {

        if(!userRepository.existsById(userId)) throw new BadRequestException(String.format("User with ID %s isn't registered", userId));
        userRepository.deleteById(userId);

    }

    @Override
    public List<UserDTO> getAllUsers() {
        List<User> allUsers = userRepository.findAll();
        return allUsers.stream().map(this::mapToDto).toList();
    }

    @Override
    public List<UserDTO> searchUsers(SearchUsersRequest searchUsersRequest) {
        if (searchUsersRequest.getNationalId() == null &&
                searchUsersRequest.getEmail() == null &&
                searchUsersRequest.getPhone() == null) {
            return userRepository.findAll().stream()
                    .map(this::mapToDto)
                    .toList();
        }

        return userRepository.findAll((root, query, cb) -> {
                    Predicate predicate = cb.conjunction();

                    if (searchUsersRequest.getNationalId() != null) {
                        String nationalIdSearch = "%" + searchUsersRequest.getNationalId().trim().toLowerCase() + "%";
                        predicate = cb.and(predicate, cb.like(cb.lower(root.get("nationalId")), nationalIdSearch));
                    }

                    if (searchUsersRequest.getEmail() != null) {
                        String emailSearch = "%" + searchUsersRequest.getEmail().trim().toLowerCase() + "%";
                        predicate = cb.and(predicate, cb.like(cb.lower(root.get("email")), emailSearch));
                    }

                    if (searchUsersRequest.getPhone() != null) {
                        String phoneSearch = "%" + searchUsersRequest.getPhone().trim().toLowerCase() + "%";
                        predicate = cb.and(predicate, cb.like(cb.lower(root.get("phone")), phoneSearch));
                    }

                    return predicate;
                }).stream()
                .map(this::mapToDto)
                .toList();
    }


    private UserDTO mapToDto(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .names(user.getNames())
                .nationalId(user.getNationalId())
                .role(user.getRole())
                .email(user.getEmail())
                .phone(user.getPhone())
                .address(user.getAddress())
                .build();
    }
}
