package com.vehicalrentelsystem.vehicalrentalsystem.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    private String phone;      // 📞 Contact Number
    private String address;    // 🏠 Full Address
    private String city;       // 🌆 City Name
    private String state;      // 🏛️ State Name

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now(); // ⏳ Account Creation Timestamp
}
