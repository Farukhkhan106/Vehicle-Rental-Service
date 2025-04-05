package com.vehicalrentelsystem.vehicalrentalsystem.controller;

import com.vehicalrentelsystem.vehicalrentalsystem.dto.UserDTO;
import com.vehicalrentelsystem.vehicalrentalsystem.model.User;
import com.vehicalrentelsystem.vehicalrentalsystem.security.JwtUtil;
import com.vehicalrentelsystem.vehicalrentalsystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping("/profile")
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<?> getUserProfile(@RequestHeader("Authorization") String token) {
        try {
            // JWT Token se email nikalna
            String jwt = token.substring(7); // "Bearer " hata ke
            String email = jwtUtil.extractUsername(jwt);

            // Email se user ki detail nikalna
            User user = userService.getUserByEmail(email);

            if (user == null) {
                return ResponseEntity.status(404).body("User not found.");
            }

            // 🔧 Sab fields set kar do
            UserDTO userDTO = new UserDTO();
            userDTO.setId(user.getId());
            userDTO.setName(user.getName());
            userDTO.setEmail(user.getEmail());
            userDTO.setPhone(user.getPhone());
            userDTO.setAddress(user.getAddress());
            userDTO.setCity(user.getCity());
            userDTO.setState(user.getState());
            userDTO.setRole(user.getRole().name());

            return ResponseEntity.ok(userDTO);
        } catch (Exception e) {
            return ResponseEntity.status(403).body("Error fetching profile: " + e.getMessage());
        }
    }
}
