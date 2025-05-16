package com.gizzo.rra_vehicle.services;

import com.gizzo.rra_vehicle.dtos.PlateDTO;
import com.gizzo.rra_vehicle.dtos.UserDTO;
import com.gizzo.rra_vehicle.entities.PlateNumber;
import com.gizzo.rra_vehicle.entities.User;
import com.gizzo.rra_vehicle.enums.VehicleStatus;
import com.gizzo.rra_vehicle.exceptions.BadRequestException;
import com.gizzo.rra_vehicle.exceptions.ResourceConflictException;
import com.gizzo.rra_vehicle.repositories.PlateRepository;
import com.gizzo.rra_vehicle.repositories.UserRepository;
import com.gizzo.rra_vehicle.request.RegisterPlateRequest;
import com.gizzo.rra_vehicle.request.UpdatePlateRequest;
import com.gizzo.rra_vehicle.services.impl.IPlateImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class PlateService implements IPlateImpl {

    private final PlateRepository plateRepository;
    private final UserRepository userRepository;


    @Override
    public PlateDTO registerPlate(RegisterPlateRequest registerPlateRequest) {

        if(plateRepository.existsByNumber(registerPlateRequest.getNumber())) {
            throw new ResourceConflictException("Plate already exists");
        }

        User plateOwner = userRepository.findById(registerPlateRequest.getOwnerId()).orElseThrow(
                () -> new BadRequestException("The specified owner doesn't exist")
        );

        PlateNumber newPlate = PlateNumber.builder()
                .number(registerPlateRequest.getNumber())
                .owner(plateOwner)
                .plateStatus(VehicleStatus.INACTIVE)
                .build();


        plateRepository.save(newPlate);

        return PlateDTO.builder()
                .id(newPlate.getId())
                .number(newPlate.getNumber())
                .plateStatus(newPlate.getPlateStatus())
                .owner(UserDTO.builder()
                        .names(plateOwner.getNames())
                        .email(plateOwner.getEmail())
                        .role(plateOwner.getRole())
                        .phone(plateOwner.getPhone())
                        .address(plateOwner.getAddress())
                        .nationalId(plateOwner.getNationalId())
                        .id(plateOwner.getId())
                        .build())
                .build();
    }

    @Override
    public void deletePlate(UUID plateId) {
        if(!plateRepository.existsById(plateId)) throw new BadRequestException("The plate with this ID doesn't exist");
        plateRepository.deleteById(plateId);
    }

    @Override
    public PlateDTO updatePlate(UUID plateId, UpdatePlateRequest updatePlateRequest) {

        PlateNumber existingPlate = plateRepository.findById(plateId).orElseThrow(
                () -> new BadRequestException("The plate with this ID doesn't exist")
        );

        boolean canUpdateInDb = false;

        if(updatePlateRequest.getPlateStatus() != null) {
            canUpdateInDb = true;
            existingPlate.setPlateStatus(updatePlateRequest.getPlateStatus());
        }
        if(updatePlateRequest.getNumber() != null) {
            canUpdateInDb = true;
            existingPlate.setNumber(updatePlateRequest.getNumber());
        }
        if(updatePlateRequest.getNewOwnerId() != null) {
            canUpdateInDb = true;
            User newOwner = userRepository.findById(updatePlateRequest.getNewOwnerId()).orElseThrow(
                    () -> new BadRequestException("This new owner doesn't exist")
            );
            existingPlate.setOwner(newOwner);
        }

        if(canUpdateInDb) plateRepository.save(existingPlate);

        return PlateDTO.builder()
                .id(plateId)
                .owner(UserDTO.builder()
                        .names(existingPlate.getOwner().getNames())
                        .email(existingPlate.getOwner().getEmail())
                        .nationalId(existingPlate.getOwner().getNationalId())
                        .role(existingPlate.getOwner().getRole())
                        .phone(existingPlate.getOwner().getPhone())
                        .address(existingPlate.getOwner().getAddress())
                        .id(existingPlate.getId())
                        .build())
                .build();
    }

    @Override
    public PlateDTO getPlate(UUID plateId) {

        PlateNumber plateNumber = plateRepository.findById(plateId).orElseThrow(
                () -> new BadRequestException("The plate with this ID doesn't exist")
        );

        return PlateDTO.builder()
                .id(plateNumber.getId())
                .owner(UserDTO.builder()
                        .id(plateNumber.getOwner().getId())
                        .names(plateNumber.getOwner().getNames())
                        .email(plateNumber.getOwner().getEmail())
                        .phone(plateNumber.getOwner().getPhone())
                        .nationalId(plateNumber.getOwner().getNationalId())
                        .address(plateNumber.getOwner().getAddress())
                        .role(plateNumber.getOwner().getRole())
                        .build())
                .plateStatus(plateNumber.getPlateStatus())
                .number(plateNumber.getNumber())
                .build();
    }

    @Override
    public PlateDTO getPlateByNumber(String number) {
        PlateNumber plateNumber = plateRepository.findByNumber(number).orElseThrow(
                () -> new BadRequestException("The plate with this Number doesn't exist")
        );

        return PlateDTO.builder()
                .id(plateNumber.getId())
                .owner(UserDTO.builder()
                        .id(plateNumber.getOwner().getId())
                        .names(plateNumber.getOwner().getNames())
                        .email(plateNumber.getOwner().getEmail())
                        .phone(plateNumber.getOwner().getPhone())
                        .nationalId(plateNumber.getOwner().getNationalId())
                        .address(plateNumber.getOwner().getAddress())
                        .role(plateNumber.getOwner().getRole())
                        .build())
                .plateStatus(plateNumber.getPlateStatus())
                .number(plateNumber.getNumber())
                .build();
    }

    @Override
    public List<PlateDTO> getAllPlates() {
        List<PlateNumber> allPlates = plateRepository.findAll();
        return allPlates.stream().map(this::mapToDTO).toList();
    }

    @Override
    public List<PlateDTO> getUserPlates(UUID userId) {
        List<PlateNumber> userPlates = plateRepository.findAllByOwner_Id(userId);
        return userPlates.stream().map(this::mapToDTO).toList();
    }

    private PlateDTO mapToDTO(PlateNumber plate) {
        return PlateDTO.builder()
                .id(plate.getId())
                .number(plate.getNumber())
                .plateStatus(plate.getPlateStatus())
                .owner(UserDTO.builder()
                        .id(plate.getOwner().getId())
                        .names(plate.getOwner().getNames())
                        .email(plate.getOwner().getEmail())
                        .phone(plate.getOwner().getPhone())
                        .address(plate.getOwner().getAddress())
                        .role(plate.getOwner().getRole())
                        .nationalId(plate.getOwner().getNationalId())
                        .build())
                .build();
    }
}
