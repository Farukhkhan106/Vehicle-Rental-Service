package com.vehicalrentelsystem.vehicalrentalsystem.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.util.List;
@Entity
@Table(name = "vehicles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long ownerId;
    private String brand;
    private String model;
    private String numberPlate;
    private BigDecimal pricePerDay;

    private String ownerPhone;
    private String ownerCity;
    private String ownerAddress;

    @Enumerated(EnumType.STRING)
    private VehicleStatus status;

    private boolean available = true;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String photosJson;
}
