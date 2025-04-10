package com.vehicalrentelsystem.vehicalrentalsystem.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Table(name = "bookings")
@Data
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "vehicle_id", nullable = false)
    private Vehicle vehicle;

    private LocalDate startDate;
    private LocalDate endDate;

    private Long ownerId;

    @Enumerated(EnumType.STRING)
    private BookingStatus status;

    // ✅ Add this for amount calculation
    private Double totalAmount;
}
