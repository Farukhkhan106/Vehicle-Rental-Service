package com.vehicalrentelsystem.vehicalrentalsystem.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class MyBookingDetailDTO {
    private Long bookingId;
    private LocalDate startDate;
    private LocalDate endDate;
    private String status;

    // Vehicle Info
    private Long vehicleId;
    private String brand;
    private String model;
    private String number;
    private String image;
    private double pricePerDay;

    // Owner Info
    private String ownerCity;
    private String ownerPhone;

    // ✅ Add this
    private String ownerName;

    // ✅ Add this
    private Double totalAmount;

}
