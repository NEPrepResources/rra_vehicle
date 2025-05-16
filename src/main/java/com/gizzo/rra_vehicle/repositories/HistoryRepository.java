package com.gizzo.rra_vehicle.repositories;

import com.gizzo.rra_vehicle.entities.History;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface HistoryRepository extends JpaRepository<History, UUID> {

    List<History> findAllByTransfer_Vehicle_ChassisNumber(String chassisNumber);

}
