//package com.vehicalrentelsystem.vehicalrentalsystem.model;
//
//import jakarta.persistence.*;
//import lombok.*;
//@Entity
//@Table(name = "vehicle_photos")
//@Getter
//@Setter
//@NoArgsConstructor
//@AllArgsConstructor
//public class VehiclePhoto {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @ManyToOne
//    @JoinColumn(name = "vehicle_id", nullable = false)
//    private Vehicle vehicle;
//
//    private String photoUrl; // URL or path to the photo
//}
