package com.gizzo.rra_vehicle.services;

import com.gizzo.rra_vehicle.dtos.*;
import com.gizzo.rra_vehicle.entities.History;
import com.gizzo.rra_vehicle.entities.Transfer;
import com.gizzo.rra_vehicle.exceptions.BadRequestException;
import com.gizzo.rra_vehicle.repositories.HistoryRepository;
import com.gizzo.rra_vehicle.repositories.VehicleRepository;
import com.gizzo.rra_vehicle.services.impl.IHistoryImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class HistoryService implements IHistoryImpl {

    private final HistoryRepository historyRepository;
    private final VehicleRepository vehicleRepository;

    @Override
    public HistoryDTO getHistory(UUID historyId) {

        History history = historyRepository.findById(historyId).orElseThrow(
                () -> new BadRequestException(String.format("History with %s ID doesn't exist", historyId))
        );

        return this.mapToDto(history);
    }

    @Override
    public List<HistoryDTO> getVehicleHistory(String vehicleChassis) {
        if(!vehicleRepository.existsByChassisNumber(vehicleChassis)) {
            throw new BadRequestException(String.format("Vehicle with chassis %s isn't registered", vehicleChassis));
        }

        List<History> vehicleHistory = historyRepository.findAllByTransfer_Vehicle_ChassisNumber(vehicleChassis);

        return vehicleHistory.stream().map(this::mapToDto).toList();
    }

    @Override
    public List<HistoryDTO> getAll() {
        List<History> allHistory = historyRepository.findAll();
        return allHistory.stream().map(this::mapToDto).toList();
    }

    private HistoryDTO mapToDto(History history) {
        return HistoryDTO.builder()
                .id(history.getId())
                .createdAt(history.getCreatedAt())
                .event(history.getEvent())
                .transfer(mapTransferToDto(history.getTransfer()))
                .build();
    }

    private TransferDTO mapTransferToDto(Transfer transfer) {
        return TransferDTO.builder()
                .id(transfer.getId())
                .newOwner(UserDTO.builder()
                        .id(transfer.getNewOwner().getId())
                        .names(transfer.getNewOwner().getNames())
                        .email(transfer.getNewOwner().getEmail())
                        .phone(transfer.getNewOwner().getPhone())
                        .address(transfer.getNewOwner().getAddress())
                        .role(transfer.getNewOwner().getRole())
                        .nationalId(transfer.getNewOwner().getNationalId())
                        .build())
                .previousOwner(UserDTO.builder()
                        .id(transfer.getPreviousOwner().getId())
                        .names(transfer.getPreviousOwner().getNames())
                        .email(transfer.getPreviousOwner().getEmail())
                        .phone(transfer.getPreviousOwner().getPhone())
                        .address(transfer.getPreviousOwner().getAddress())
                        .role(transfer.getPreviousOwner().getRole())
                        .nationalId(transfer.getPreviousOwner().getNationalId())
                        .build())
                .purchasePrice(transfer.getPurchasePrice())
                .transferDate(transfer.getTransferDate())
                .vehicle(VehicleDTO.builder()
                        .id(transfer.getVehicle().getId())
                        .price(transfer.getVehicle().getPrice())
                        .model(transfer.getVehicle().getModel())
                        .manufacturer(transfer.getVehicle().getManufacturer())
                        .manufacturedYear(transfer.getVehicle().getManufacturedYear())
                        .chassisNumber(transfer.getVehicle().getChassisNumber())
                        .plate(PlateDTO.builder()
                                .id(transfer.getVehicle().getPlate().getId())
                                .number(transfer.getVehicle().getPlate().getNumber())
                                .plateStatus(transfer.getVehicle().getPlate().getPlateStatus())
//                                .owner(UserDTO.builder()
//                                        .id(transfer.getNewOwner().getId())
//                                        .names(transfer.getNewOwner().getNames())
//                                        .email(transfer.getNewOwner().getEmail())
//                                        .phone(transfer.getNewOwner().getPhone())
//                                        .address(transfer.getNewOwner().getAddress())
//                                        .role(transfer.getNewOwner().getRole())
//                                        .nationalId(transfer.getNewOwner().getNationalId())
//                                        .build())
                                .build())
                        .build())
                .build();
    }
}
