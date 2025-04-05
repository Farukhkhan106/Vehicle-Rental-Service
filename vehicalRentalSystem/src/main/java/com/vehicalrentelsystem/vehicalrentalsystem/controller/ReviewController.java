//package com.vehicalrentelsystem.vehicalrentalsystem.controller;
//
//import com.vehicalrentelsystem.vehicalrentalsystem.dto.ReviewDTO;
//import com.vehicalrentelsystem.vehicalrentalsystem.service.ReviewService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/review")
//public class ReviewController {
//
//    @Autowired
//    private ReviewService reviewService;
//
//    @PostMapping("/add")
//    public ResponseEntity<String> addReview(@RequestBody ReviewDTO reviewDTO) {
//        try {
//            reviewService.addReview(reviewDTO);
//            return ResponseEntity.ok("Review added successfully!");
//        } catch (Exception e) {
//            return ResponseEntity.badRequest().body(e.getMessage());
//        }
//    }
//
//    @GetMapping("/vehicle/{vehicleId}")
//    public ResponseEntity<?> getVehicleReviews(@PathVariable Long vehicleId) {
//        try {
//            List<ReviewDTO> reviews = reviewService.getReviewsByVehicle(vehicleId);
//            return ResponseEntity.ok(reviews);
//        } catch (Exception e) {
//            return ResponseEntity.status(404).body(e.getMessage());
//        }
//    }
//}
