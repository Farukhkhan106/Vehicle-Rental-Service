package com.vehicalrentelsystem.vehicalrentalsystem.controller;

import com.vehicalrentelsystem.vehicalrentalsystem.dto.BookingDTO;
import com.vehicalrentelsystem.vehicalrentalsystem.dto.MyBookingDetailDTO;
import com.vehicalrentelsystem.vehicalrentalsystem.model.Booking;
import com.vehicalrentelsystem.vehicalrentalsystem.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.*;
@RestController
@RequestMapping("/booking")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @PostMapping("/create")
    public ResponseEntity<String> createBooking(@RequestBody BookingDTO bookingDTO, Authentication authentication) {
        bookingService.createBooking(bookingDTO, authentication);
        return ResponseEntity.ok("Booking created successfully!");
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<BookingDTO>> getUserBookings(@PathVariable Long userId) {
        return ResponseEntity.ok(bookingService.getUserBookings(userId));
    }

    @DeleteMapping("/cancel/{bookingId}")
    public ResponseEntity<String> cancelBooking(@PathVariable Long bookingId) {
        bookingService.cancelBooking(bookingId);
        return ResponseEntity.ok("Booking cancelled successfully!");
    }

    @GetMapping("/check-availability")
    public ResponseEntity<Map<String, Object>> checkAvailability(
            @RequestParam Long vehicleId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        Optional<Booking> overlappingBooking = bookingService.findOverlappingBooking(vehicleId, startDate, endDate);
        Map<String, Object> response = new HashMap<>();

        if (overlappingBooking.isPresent()) {
            Booking booking = overlappingBooking.get();
            response.put("available", false);
            response.put("unavailableFrom", booking.getStartDate().toString());
            response.put("unavailableTo", booking.getEndDate().toString());
        } else {
            response.put("available", true);
        }

        return ResponseEntity.ok(response);
    }

    @GetMapping("/my-detailed-bookings-by-user/{userId}")
    public ResponseEntity<List<MyBookingDetailDTO>> getMyDetailedBookingsByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(bookingService.getMyDetailedBookingsByUserId(userId));
    }
}
