//package com.vehicalrentelsystem.vehicalrentalsystem.controller;
//
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequestMapping("/protected-endpoint")
//public class ProtectedController {
//
//    @PreAuthorize("hasRole('USER')")
//    @GetMapping
//    public ResponseEntity<String> getProtectedResource() {
//        return ResponseEntity.ok("Access granted to protected resource.");
//    }
//}
