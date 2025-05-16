package com.gizzo.rra_vehicle.services.impl;

import com.gizzo.rra_vehicle.dtos.TransferDTO;
import com.gizzo.rra_vehicle.request.CreateTransferRequest;

import java.util.List;
import java.util.UUID;

public interface ITransferImpl {

    TransferDTO createTransfer(CreateTransferRequest createTransferRequest);
    TransferDTO getTransfer(UUID transferId);
    List<TransferDTO> getVehicleTransfers(String vehicleChassis);
    List<TransferDTO> getAll();
    void deleteTransfer(UUID transferId);

}
