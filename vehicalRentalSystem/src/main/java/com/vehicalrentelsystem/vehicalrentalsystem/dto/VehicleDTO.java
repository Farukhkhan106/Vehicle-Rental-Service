package com.vehicalrentelsystem.vehicalrentalsystem.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import java.math.BigDecimal;
import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VehicleDTO {
    private Long id;
    private Long ownerId;
    private String brand;
    private String model;

    @JsonProperty("registrationNumber")
    private String numberPlate;

    private BigDecimal pricePerDay;
    private String status;
    private List<String> photoUrls;

    private String ownerPhone;
    private String ownerCity;
    private String ownerAddress;
}
