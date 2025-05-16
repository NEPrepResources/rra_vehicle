package com.gizzo.rra_vehicle.repositories;

import com.gizzo.rra_vehicle.entities.PlateNumber;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PlateRepository extends JpaRepository<PlateNumber, UUID> {

    Optional<PlateNumber> findByNumber(String number);
    List<PlateNumber> findAllByOwner_Id(UUID id);
    boolean existsByNumber(String number);

}
