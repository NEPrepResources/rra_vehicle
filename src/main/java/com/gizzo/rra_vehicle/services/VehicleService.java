package com.gizzo.rra_vehicle.services;

import com.gizzo.rra_vehicle.dtos.PlateDTO;
import com.gizzo.rra_vehicle.dtos.UserDTO;
import com.gizzo.rra_vehicle.dtos.VehicleDTO;
import com.gizzo.rra_vehicle.entities.PlateNumber;
import com.gizzo.rra_vehicle.entities.Vehicle;
import com.gizzo.rra_vehicle.enums.VehicleStatus;
import com.gizzo.rra_vehicle.exceptions.BadRequestException;
import com.gizzo.rra_vehicle.exceptions.ResourceConflictException;
import com.gizzo.rra_vehicle.repositories.PlateRepository;
import com.gizzo.rra_vehicle.repositories.UserRepository;
import com.gizzo.rra_vehicle.repositories.VehicleRepository;
import com.gizzo.rra_vehicle.request.RegisterVehicleRequest;
import com.gizzo.rra_vehicle.request.SearchVehiclesRequest;
import com.gizzo.rra_vehicle.request.UpdateVehicleRequest;
import com.gizzo.rra_vehicle.services.impl.IVehicleImpl;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class VehicleService implements IVehicleImpl {

    private final VehicleRepository vehicleRepository;
    private final PlateRepository plateRepository;
    private final UserRepository userRepository;

    @Override
    public VehicleDTO registerVehicle(RegisterVehicleRequest registerVehicleRequest) {

        if(vehicleRepository.existsByChassisNumber(registerVehicleRequest.getChassisNumber())) {
            throw new ResourceConflictException(String.format("Vehicle with %s chassis number already exists", registerVehicleRequest.getChassisNumber()));
        }

        if(vehicleRepository.existsByPlate_Number(registerVehicleRequest.getPlateNumber())) {
            throw new BadRequestException(String.format("This plate %s has a vehicle assigned to it", registerVehicleRequest.getPlateNumber()));
        }

        PlateNumber vehiclePlate = plateRepository.findByNumber(registerVehicleRequest.getPlateNumber()).orElseThrow(
                () -> new BadRequestException(String.format("Plate with this number %s, isn't registered", registerVehicleRequest.getPlateNumber()))
        );
        vehiclePlate.setPlateStatus(VehicleStatus.ACTIVE);


        Vehicle newVehicle = Vehicle.builder()
                .model(registerVehicleRequest.getModel())
                .manufacturer(registerVehicleRequest.getManufacturer())
                .manufacturedYear(registerVehicleRequest.getManufacturedYear())
                .chassisNumber(registerVehicleRequest.getChassisNumber())
                .price(registerVehicleRequest.getPrice())
                .plate(vehiclePlate)
                .build();

        vehicleRepository.save(newVehicle);

        return this.mapToDTO(newVehicle);
    }

    @Override
    public VehicleDTO getVehicle(UUID vehicleId) {

        Vehicle vehicle = vehicleRepository.findById(vehicleId).orElseThrow(
                () -> new BadRequestException(String.format("The vehicle with ID %s isn't registered", vehicleId))
        );

        return this.mapToDTO(vehicle);
    }

    @Override
    public VehicleDTO getVehicleByChassisNumber(String chassisNumber) {

        Vehicle vehicle = vehicleRepository.findByChassisNumber(chassisNumber).orElseThrow(
                () -> new BadRequestException(String.format("Vehicle with this chassis number %s isn't registered", chassisNumber))
        );

        return this.mapToDTO(vehicle);
    }

    @Override
    public VehicleDTO updateVehicle(UUID vehicleId, UpdateVehicleRequest updateVehicleRequest) {

        Vehicle existingVehicle = vehicleRepository.findById(vehicleId).orElseThrow(
                () -> new BadRequestException(String.format("There is no vehicle with this ID %s", vehicleId))
        );

        boolean canUpdateInDb = false;

        if(updateVehicleRequest.getChassisNumber() != null) {
            canUpdateInDb = true;
            existingVehicle.setChassisNumber(updateVehicleRequest.getChassisNumber());
        }

        if(updateVehicleRequest.getManufacturer() != null) {
            canUpdateInDb = true;
            existingVehicle.setManufacturer(updateVehicleRequest.getManufacturer());
        }

        if(updateVehicleRequest.getPrice() != null) {
            canUpdateInDb = true;
            existingVehicle.setPrice(updateVehicleRequest.getPrice());
        }

        if(updateVehicleRequest.getModel() != null) {
            canUpdateInDb = true;
            existingVehicle.setModel(updateVehicleRequest.getModel());
        }

        if(updateVehicleRequest.getManufacturedYear() != null) {
            canUpdateInDb = true;
            existingVehicle.setManufacturedYear(updateVehicleRequest.getManufacturedYear());
        }

        if(canUpdateInDb) {
            vehicleRepository.save(existingVehicle);
        }

        return this.mapToDTO(existingVehicle);
    }

    @Override
    public void deleteVehicle(UUID vehicleId) {

        if(!vehicleRepository.existsById(vehicleId)) {
            throw new BadRequestException(String.format("No vehicle with this ID %s", vehicleId));
        }

        vehicleRepository.deleteById(vehicleId);
    }

    @Override
    public List<VehicleDTO> getAllVehicles() {

        List<Vehicle> allVehicles = vehicleRepository.findAll();

        return allVehicles.stream().map(this::mapToDTO).toList();
    }

    @Override
    public List<VehicleDTO> getUserVehicles(UUID userId) {

        if(!userRepository.existsById(userId)) {
            throw new BadRequestException(String.format("No user we have with this ID %s", userId));
        }

        List<Vehicle> userVehicles = vehicleRepository.findByPlate_Owner_Id(userId);

        return userVehicles.stream().map(this::mapToDTO).toList();
    }

    @Override
    public VehicleDTO getVehicleByPlateNumber(String plateNumber) {

        if(!plateRepository.existsByNumber(plateNumber)) {
            throw new BadRequestException(String.format("This plate %s isn't registered", plateNumber));
        }

        Vehicle vehicle = vehicleRepository.findByPlate_Number(plateNumber).orElseThrow(
                () -> new BadRequestException(String.format("We didn't find a vehicle assigned to this plate %s", plateNumber))
        );

        return this.mapToDTO(vehicle);
    }

    @Override
    public List<VehicleDTO> searchVehicles(SearchVehiclesRequest searchVehiclesRequest) {

        if (searchVehiclesRequest.getNationalId() == null &&
                searchVehiclesRequest.getChassisNumber() == null &&
                searchVehiclesRequest.getPlateNumber() == null) {
            return vehicleRepository.findAll().stream()
                    .map(this::mapToDTO)
                    .toList();
        }

        return vehicleRepository.findAll((root, query, cb) -> {
                    Predicate predicate = cb.conjunction();

                    if (searchVehiclesRequest.getNationalId() != null) {
                        Join<Object, Object> plateJoin = root.join("plate");
                        Join<Object, Object> ownerJoin = plateJoin.join("owner");
                        predicate = cb.and(predicate, cb.like(
                                cb.lower(ownerJoin.get("nationalId")),
                                "%" + searchVehiclesRequest.getNationalId().toLowerCase() + "%"
                        ));
                    }

                    if (searchVehiclesRequest.getPlateNumber() != null) {
                        Join<Object, Object> plateJoin = root.join("plate");
                        predicate = cb.and(predicate, cb.like(
                                cb.lower(plateJoin.get("number")),
                                "%" + searchVehiclesRequest.getPlateNumber().toLowerCase() + "%"
                        ));
                    }

                    if (searchVehiclesRequest.getChassisNumber() != null) {
                        predicate = cb.and(predicate, cb.like(
                                cb.lower(root.get("chassisNumber")),
                                "%" + searchVehiclesRequest.getChassisNumber().toLowerCase() + "%"
                        ));
                    }

                    return predicate;
                }).stream()
                .map(this::mapToDTO)
                .toList();
    }

    private VehicleDTO mapToDTO(Vehicle vehicle) {
        return VehicleDTO.builder()
                .id(vehicle.getId())
                .chassisNumber(vehicle.getChassisNumber())
                .manufacturedYear(vehicle.getManufacturedYear())
                .manufacturer(vehicle.getManufacturer())
                .model(vehicle.getManufacturer())
                .price(vehicle.getPrice())
                .plate(PlateDTO.builder()
                        .id(vehicle.getPlate().getId())
                        .number(vehicle.getPlate().getNumber())
                        .plateStatus(vehicle.getPlate().getPlateStatus())
                        .owner(UserDTO.builder()
                                .id(vehicle.getPlate().getOwner().getId())
                                .nationalId(vehicle.getPlate().getOwner().getNationalId())
                                .role(vehicle.getPlate().getOwner().getRole())
                                .names(vehicle.getPlate().getOwner().getNames())
                                .email(vehicle.getPlate().getOwner().getEmail())
                                .phone(vehicle.getPlate().getOwner().getPhone())
                                .address(vehicle.getPlate().getOwner().getAddress())
                                .build())
                        .build())
                .build();
    }
}
