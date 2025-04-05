package com.vehicalrentelsystem.vehicalrentalsystem.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vehicalrentelsystem.vehicalrentalsystem.dto.VehicleDTO;
import com.vehicalrentelsystem.vehicalrentalsystem.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
@RequestMapping("/vehicle")
public class VehicleController {

    private final VehicleService vehicleService;
    private final ObjectMapper objectMapper;

    @Autowired
    public VehicleController(VehicleService vehicleService, ObjectMapper objectMapper) {
        this.vehicleService = vehicleService;
        this.objectMapper = objectMapper;
    }

    // ✅ Admin or User can add a vehicle
    @PostMapping("/add")
    public ResponseEntity<?> addVehicle(@RequestParam("vehicle") String vehicleDTOStr,
                                        @RequestParam("files") List<MultipartFile> files,
                                        Authentication authentication) {
        try {
            System.out.println("Received JSON: " + vehicleDTOStr);
            System.out.println("Received Files: " + files.size());

            // Convert JSON string to DTO
            ObjectMapper objectMapper = new ObjectMapper();
            VehicleDTO vehicleDTO = objectMapper.readValue(vehicleDTOStr, VehicleDTO.class);

            String userEmail = authentication.getName();
            VehicleDTO addedVehicle = vehicleService.addVehicle(vehicleDTO, files, userEmail);
            return ResponseEntity.ok(addedVehicle);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error adding vehicle: " + e.getMessage());
        }
    }




    @GetMapping("/all")
    public ResponseEntity<?> getAllVehicles() {
        try {
            List<VehicleDTO> vehicles = vehicleService.getAllVehicles();
            if (vehicles.isEmpty()) {
                return ResponseEntity.status(404).body("No vehicles found.");
            }
            return ResponseEntity.ok(vehicles);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error fetching vehicles: " + e.getMessage());
        }
    }

    // Fetch logged-in user's vehicles
    @GetMapping("/my-vehicles")
    public ResponseEntity<?> getMyVehicles() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String userEmail = authentication.getName(); // Logged-in user's email
            List<VehicleDTO> vehicles = vehicleService.getVehiclesByOwnerEmail(userEmail);
            return ResponseEntity.ok(vehicles);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error fetching your vehicles: " + e.getMessage());
        }
    }
    // ✅ Admin can update any vehicle
    @PutMapping("/admin/vehicles/{vehicleId}")
    public ResponseEntity<String> updateVehicle(@PathVariable Long vehicleId, @RequestBody VehicleDTO vehicleDTO) {
        try {
            vehicleService.updateVehicle(vehicleId, vehicleDTO);
            return ResponseEntity.ok("Vehicle updated successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error updating vehicle: " + e.getMessage());
        }
    }

    // ✅ Admin can delete any vehicle, User can delete only their own vehicle
    // ✅ Users can delete their own vehicles, Admins can delete any vehicle
    @DeleteMapping("/delete/{vehicleId}")
    public ResponseEntity<String> deleteVehicle(@PathVariable Long vehicleId, Authentication authentication) {
        try {
            String username = authentication.getName();
            vehicleService.deleteVehicle(vehicleId, username);
            return ResponseEntity.ok("Vehicle deleted successfully!");
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error deleting vehicle: " + e.getMessage());
        }
    }


}
