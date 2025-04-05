package com.vehicalrentelsystem.vehicalrentalsystem.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private Long id;
    private String name;
    private String email;
    private String password;
    private String role;
    private String phone;
    private String address;
    private String city;
    private String state;
}
