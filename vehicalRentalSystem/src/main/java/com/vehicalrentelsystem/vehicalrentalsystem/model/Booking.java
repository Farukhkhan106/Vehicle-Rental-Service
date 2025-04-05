//package com.vehicalrentelsystem.vehicalrentalsystem.model;
//
//import jakarta.persistence.*;
//import lombok.*;
//import java.time.LocalDate;
//
//@Entity
//@Table(name = "bookings")
//
//public class Booking {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @ManyToOne
//    @JoinColumn(name = "user_id", nullable = false)
//    private User user;
//
//    @ManyToOne
//    @JoinColumn(name = "vehicle_id", nullable = false)
//    private Vehicle vehicle;
//
//    private LocalDate startDate;
//    private LocalDate endDate;
//
//    @Enumerated(EnumType.STRING)
//    private BookingStatus status;
//}
