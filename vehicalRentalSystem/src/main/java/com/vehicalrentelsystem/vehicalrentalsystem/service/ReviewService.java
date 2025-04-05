//package com.vehicalrentelsystem.vehicalrentalsystem.service;
//
//import com.vehicalrentelsystem.vehicalrentalsystem.dto.ReviewDTO;
//import com.vehicalrentelsystem.vehicalrentalsystem.model.Review;
//import com.vehicalrentelsystem.vehicalrentalsystem.model.User;
//import com.vehicalrentelsystem.vehicalrentalsystem.model.Vehicle;
//import com.vehicalrentelsystem.vehicalrentalsystem.repository.ReviewRepository;
//import com.vehicalrentelsystem.vehicalrentalsystem.repository.UserRepository;
//import com.vehicalrentelsystem.vehicalrentalsystem.repository.VehicleRepository;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Service
//public class ReviewService {
//
//    private final ReviewRepository reviewRepository;
//    private final UserRepository userRepository;
//    private final VehicleRepository vehicleRepository;
//
//    public ReviewService(ReviewRepository reviewRepository, UserRepository userRepository, VehicleRepository vehicleRepository) {
//        this.reviewRepository = reviewRepository;
//        this.userRepository = userRepository;
//        this.vehicleRepository = vehicleRepository;
//    }
//
//    public ReviewDTO addReview(ReviewDTO reviewDTO) {
//        User user = userRepository.findById(reviewDTO.getUserId())
//                .orElseThrow(() -> new RuntimeException("User not found"));
//
//        Vehicle vehicle = vehicleRepository.findById(reviewDTO.getVehicleId())
//                .orElseThrow(() -> new RuntimeException("Vehicle not found"));
//
//        Review review = new Review();
//        review.setUser(user);
//        review.setVehicle(vehicle);
//        review.setRating(reviewDTO.getRating());
//        review.setComment(reviewDTO.getComment());
//        reviewRepository.save(review);
//
//        return mapToDTO(review);
//    }
//
//    public List<ReviewDTO> getReviewsByVehicle(Long vehicleId) {
//        List<Review> reviews = reviewRepository.findByVehicleId(vehicleId);
//        if (reviews.isEmpty()) {
//            throw new RuntimeException("No reviews found for this vehicle.");
//        }
//        return reviews.stream().map(this::mapToDTO).collect(Collectors.toList());
//    }
//
//    private ReviewDTO mapToDTO(Review review) {
//        return new ReviewDTO(
//                review.getId(),
//                review.getUser().getId(),
//                review.getVehicle().getId(),
//                review.getRating(),
//                review.getComment()
//        );
//    }
//}
