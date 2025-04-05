//package com.vehicalrentelsystem.vehicalrentalsystem.controller;
//import com.vehicalrentelsystem.vehicalrentalsystem.dto.BookingDTO;
//import com.vehicalrentelsystem.vehicalrentalsystem.service.BookingService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/booking")
//public class BookingController {
//
//    @Autowired
//    private BookingService bookingService;
//
//    @PostMapping("/create")
//    public ResponseEntity<String> createBooking(@RequestBody BookingDTO bookingDTO) {
//        bookingService.createBooking(bookingDTO);
//        return ResponseEntity.ok("Booking created successfully!");
//    }
//
//    @GetMapping("/user/{userId}")
//    public ResponseEntity<List<BookingDTO>> getUserBookings(@PathVariable Long userId) {
//        return ResponseEntity.ok(bookingService.getUserBookings(userId));
//    }
//
//    @DeleteMapping("/cancel/{bookingId}")
//    public ResponseEntity<String> cancelBooking(@PathVariable Long bookingId) {
//        bookingService.cancelBooking(bookingId);
//        return ResponseEntity.ok("Booking cancelled successfully!");
//    }
//}
