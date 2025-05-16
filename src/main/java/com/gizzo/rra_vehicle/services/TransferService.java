package com.gizzo.rra_vehicle.services;

import com.gizzo.rra_vehicle.dtos.PlateDTO;
import com.gizzo.rra_vehicle.dtos.TransferDTO;
import com.gizzo.rra_vehicle.dtos.UserDTO;
import com.gizzo.rra_vehicle.dtos.VehicleDTO;
import com.gizzo.rra_vehicle.entities.*;
import com.gizzo.rra_vehicle.repositories.*;
import com.gizzo.rra_vehicle.enums.HistoryEvent;
import com.gizzo.rra_vehicle.enums.VehicleStatus;
import com.gizzo.rra_vehicle.exceptions.BadRequestException;
import com.gizzo.rra_vehicle.request.CreateTransferRequest;
import com.gizzo.rra_vehicle.services.impl.ITransferImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransferService implements ITransferImpl {

    private final TransferRepository transferRepository;
    private final UserRepository userRepository;
    private final PlateRepository plateRepository;
    private final VehicleRepository vehicleRepository;
    private final HistoryRepository historyRepository;

    @Override
    public TransferDTO createTransfer(CreateTransferRequest createTransferRequest) {

        if(!userRepository.existsById(createTransferRequest.getNewOwnerId())) {
            throw new BadRequestException(String.format("New owner with ID %s of vehicle isn't registered", createTransferRequest.getNewOwnerId()));
        }

        Vehicle vehicle = vehicleRepository.findByChassisNumber(createTransferRequest.getVehicleChassis()).orElseThrow(
                () -> new BadRequestException(String.format("Vehicle with %s chassis isn't registered", createTransferRequest.getVehicleChassis()))
        );

        User previousOwner = vehicle.getPlate().getOwner();

        List<PlateNumber> newOwnerPlates = plateRepository.findAllByOwner_Id(createTransferRequest.getNewOwnerId());
        PlateNumber inactivePlate = newOwnerPlates.stream()
                .filter(p -> p.getPlateStatus() == VehicleStatus.INACTIVE)
                .findFirst()
                .orElseThrow(() -> new BadRequestException(
                        "New owner does not have any INACTIVE plate available for transfer."
                ));

        PlateNumber currentPlate = vehicle.getPlate();
        currentPlate.setPlateStatus(VehicleStatus.INACTIVE);
        plateRepository.save(currentPlate);

        inactivePlate.setPlateStatus(VehicleStatus.ACTIVE);
        vehicle.setPlate(inactivePlate);
        plateRepository.save(inactivePlate);
        vehicleRepository.save(vehicle);

        Transfer newTransfer = Transfer.builder()
                .transferDate(createTransferRequest.getTransferDate())
                .newOwner(inactivePlate.getOwner())
                .previousOwner(previousOwner)
                .purchasePrice(createTransferRequest.getPurchasePrice())
                .vehicle(vehicle)
                .build();

        History historyRecord = History.builder()
                .event(HistoryEvent.TRANSFER_VEHICLE)
                .transfer(newTransfer)
                .build();

        transferRepository.save(newTransfer);
        historyRepository.save(historyRecord);

        return this.mapToDto(newTransfer);
    }

    @Override
    public TransferDTO getTransfer(UUID transferId) {

        Transfer transfer = transferRepository.findById(transferId).orElseThrow(
                () -> new BadRequestException(String.format("The transfer with ID %s doesn't exist", transferId))
        );

        return this.mapToDto(transfer);
    }

    @Override
    public List<TransferDTO> getVehicleTransfers(String vehicleChassis) {
        if(!vehicleRepository.existsByChassisNumber(vehicleChassis)){
            throw new BadRequestException(String.format("Vehicle with %s chassis number isn't registered", vehicleChassis));
        }

        List<Transfer> vehicleTransfers = transferRepository.findAllByVehicle_ChassisNumber(vehicleChassis);

        return vehicleTransfers.stream().map(this::mapToDto).toList();
    }

    @Override
    public List<TransferDTO> getAll() {
        List<Transfer> allTransfers = transferRepository.findAll();
        return allTransfers.stream().map(this::mapToDto).toList();
    }

    @Override
    public void deleteTransfer(UUID transferId) {
        if(!vehicleRepository.existsById(transferId)) throw new BadRequestException(String.format("The transfer with ID %s doesn't exist", transferId));
        transferRepository.deleteById(transferId);
    }

    private TransferDTO mapToDto(Transfer transfer) {
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
