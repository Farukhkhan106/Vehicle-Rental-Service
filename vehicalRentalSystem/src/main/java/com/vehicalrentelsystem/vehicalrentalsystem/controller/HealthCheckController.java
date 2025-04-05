package com.vehicalrentelsystem.vehicalrentalsystem.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/health")
public class HealthCheckController {

    @GetMapping("/check")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("Application is running!");
    }
    @GetMapping("/hello")
    public ResponseEntity<String> hello() {
      return ResponseEntity.ok("Hello World!");
    }
}
