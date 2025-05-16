package com.gizzo.rra_vehicle.entities;

import com.gizzo.rra_vehicle.enums.HistoryEvent;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "history")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class History {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private HistoryEvent event;

    @OneToOne(optional = false)
    @JoinColumn(name = "transfer_id", nullable = false)
    private Transfer transfer;

}
