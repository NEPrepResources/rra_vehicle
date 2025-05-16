package com.gizzo.rra_vehicle.entities;

import com.gizzo.rra_vehicle.enums.VehicleStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "plates")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class PlateNumber {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(unique = true, nullable = false)
    private String number;

    @Enumerated(EnumType.STRING)
    private VehicleStatus plateStatus;

    @ManyToOne(optional = false)
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

}
