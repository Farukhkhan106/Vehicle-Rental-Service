package com.vehicalrentelsystem.vehicalrentalsystem.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vehicalrentelsystem.vehicalrentalsystem.dto.UserDTO;
import com.vehicalrentelsystem.vehicalrentalsystem.dto.VehicleDTO;
import com.vehicalrentelsystem.vehicalrentalsystem.service.AdminService;
import com.vehicalrentelsystem.vehicalrentalsystem.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    @Autowired
    private AdminService adminService;
    private final VehicleService vehicleService;

    public AdminController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    // ✅ Get All Users
    @GetMapping("/users")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> users = adminService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    // ✅ Delete User by ID
    @DeleteMapping("/users/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        adminService.deleteUser(id);
        return ResponseEntity.ok("User deleted successfully!");
    }

    // ✅ Update User Role (Optional)
    @PutMapping("/users/{id}/role")
    public ResponseEntity<String> updateUserRole(@PathVariable Long id, @RequestParam String role) {
        adminService.updateUserRole(id, role);
        return ResponseEntity.ok("User role updated successfully!");
    }
    @PostMapping("/add")
    public ResponseEntity<String> addVehicle(
            @RequestParam("vehicle") String vehicleJson,
            @RequestParam(value = "files", required = false) List<MultipartFile> files) {
        try {
            adminService.addVehicle(vehicleJson, files);
            return ResponseEntity.ok("Vehicle added successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }
    }

}
