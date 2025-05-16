package com.gizzo.rra_vehicle.dtos;

import com.gizzo.rra_vehicle.enums.HistoryEvent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class HistoryDTO {

    private UUID id;
    private HistoryEvent event;
    private TransferDTO transfer;
    private LocalDateTime createdAt;

}
