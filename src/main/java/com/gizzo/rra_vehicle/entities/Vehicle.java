package com.gizzo.rra_vehicle.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "vehicles")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String model;

    @Column(nullable = false)
    private String chassisNumber;

    @Column(nullable = false)
    private String manufacturer;

    @Column(length = 5, nullable = false)
    private Integer manufacturedYear;

    @Column(nullable = false)
    private Integer price;

    @OneToOne(optional = false)
    @JoinColumn(name = "plate_id", unique = true, nullable = false)
    private PlateNumber plate;

}
