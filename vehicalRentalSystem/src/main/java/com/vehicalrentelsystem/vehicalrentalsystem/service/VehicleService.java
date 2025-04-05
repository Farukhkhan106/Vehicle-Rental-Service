package com.vehicalrentelsystem.vehicalrentalsystem.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vehicalrentelsystem.vehicalrentalsystem.dto.VehicleDTO;
import com.vehicalrentelsystem.vehicalrentalsystem.model.Role;
import com.vehicalrentelsystem.vehicalrentalsystem.model.User;
import com.vehicalrentelsystem.vehicalrentalsystem.model.Vehicle;
import com.vehicalrentelsystem.vehicalrentalsystem.model.VehicleStatus;
import com.vehicalrentelsystem.vehicalrentalsystem.repository.UserRepository;
import com.vehicalrentelsystem.vehicalrentalsystem.repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class VehicleService {

    private final VehicleRepository vehicleRepository;
    private final ObjectMapper objectMapper;
    @Autowired
    private final UserRepository userRepository;
    public VehicleService(VehicleRepository vehicleRepository, ObjectMapper objectMapper, UserRepository userRepository) {
        this.vehicleRepository = vehicleRepository;
        this.objectMapper = objectMapper;
        this.userRepository = userRepository;
    }

    public VehicleDTO addVehicle(VehicleDTO vehicleDTO, List<MultipartFile> files, String userEmail) throws IOException {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + userEmail));

        Vehicle vehicle = new Vehicle();
        vehicle.setOwnerId(user.getId());
        vehicle.setBrand(vehicleDTO.getBrand());
        vehicle.setModel(vehicleDTO.getModel());
        vehicle.setNumberPlate(vehicleDTO.getNumberPlate());
        vehicle.setPricePerDay(vehicleDTO.getPricePerDay());
        vehicle.setStatus(VehicleStatus.AVAILABLE);

        // ✅ Set user info into vehicle
        vehicle.setOwnerPhone(user.getPhone());
        vehicle.setOwnerCity(user.getCity());
        vehicle.setOwnerAddress(user.getAddress());

        // Handle photos
        List<String> photoUrls = new ArrayList<>();
        for (MultipartFile file : files) {
            String photoUrl = savePhoto(file);
            photoUrls.add(photoUrl);
        }

        try {
            String photosJson = objectMapper.writeValueAsString(photoUrls);
            vehicle.setPhotosJson(photosJson);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error while converting photos to JSON: " + e.getMessage());
        }

        vehicleRepository.save(vehicle);

        vehicleDTO.setId(vehicle.getId());
        vehicleDTO.setStatus(vehicle.getStatus().name());
        vehicleDTO.setPhotoUrls(photoUrls);
        vehicleDTO.setOwnerPhone(vehicle.getOwnerPhone());
        vehicleDTO.setOwnerCity(vehicle.getOwnerCity());
        vehicleDTO.setOwnerAddress(vehicle.getOwnerAddress());

        return vehicleDTO;
    }



    public List<VehicleDTO> getAllVehicles() {
        List<Vehicle> vehicles = vehicleRepository.findAll();
        System.out.println("Total vehicles fetched: " + vehicles.size());
        return vehicles.stream().map(this::mapToDTO).collect(Collectors.toList());
    }
    public List<VehicleDTO> getVehiclesByOwnerEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));

        List<Vehicle> vehicles = vehicleRepository.findByOwnerId(user.getId());

        return vehicles.stream().map(this::mapToDTO).collect(Collectors.toList());
    }
    public void updateVehicle(Long vehicleId, VehicleDTO vehicleDTO) {
        Vehicle vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new RuntimeException("Vehicle not found with ID: " + vehicleId));

        vehicle.setBrand(vehicleDTO.getBrand());
        vehicle.setModel(vehicleDTO.getModel());
        vehicle.setNumberPlate(vehicleDTO.getNumberPlate());
        vehicle.setPricePerDay(vehicleDTO.getPricePerDay());
        vehicleRepository.save(vehicle);
    }

    public void deleteVehicle(Long vehicleId, String username) {
        Vehicle vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new RuntimeException("Vehicle not found with ID: " + vehicleId));

        // Fetch user by email (username) to check their role
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new RuntimeException("User not found with username: " + username));

        // Admins can delete any vehicle, users can delete their own
        if (user.getRole() == Role.ADMIN || vehicle.getOwnerId().equals(user.getId())) {
            vehicleRepository.delete(vehicle);
            System.out.println("Vehicle deleted successfully by: " + user.getId());
        } else {
            throw new RuntimeException("Access denied! You can only delete your own vehicles.");
        }

    }

    private VehicleDTO mapToDTO(Vehicle vehicle) {
        List<String> photoUrls = new ArrayList<>();
        try {
            if (vehicle.getPhotosJson() != null) {
                photoUrls = objectMapper.readValue(vehicle.getPhotosJson(), List.class);
            }
        } catch (JsonProcessingException e) {
            System.err.println("Error while reading photos JSON: " + e.getMessage());
        }

        return VehicleDTO.builder()
                .id(vehicle.getId())
                .ownerId(vehicle.getOwnerId())
                .brand(vehicle.getBrand())
                .model(vehicle.getModel())
                .numberPlate(vehicle.getNumberPlate())
                .pricePerDay(vehicle.getPricePerDay())
                .status(vehicle.getStatus() != null ? vehicle.getStatus().name() : VehicleStatus.UNAVAILABLE.name())
                .photoUrls(photoUrls)
                .ownerPhone(vehicle.getOwnerPhone())
                .ownerCity(vehicle.getOwnerCity())
                .ownerAddress(vehicle.getOwnerAddress())
                .build();
    }

    private String savePhoto(MultipartFile file) throws IOException {
        String uploadDir = "F:/springBoot/uploads/";
        File uploadFolder = new File(uploadDir);

        if (!uploadFolder.exists() && !uploadFolder.mkdirs()) {
            throw new IOException("Failed to create directory: " + uploadDir);
        }

        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        File destFile = new File(uploadFolder, fileName);
        file.transferTo(destFile);

        return "/uploads/" + fileName;
    }
}