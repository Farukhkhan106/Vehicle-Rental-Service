//package com.vehicalrentelsystem.vehicalrentalsystem.service;
//
//import com.vehicalrentelsystem.vehicalrentalsystem.dto.BookingDTO;
//import com.vehicalrentelsystem.vehicalrentalsystem.model.Booking;
//import com.vehicalrentelsystem.vehicalrentalsystem.model.BookingStatus;
//import com.vehicalrentelsystem.vehicalrentalsystem.model.User;
//import com.vehicalrentelsystem.vehicalrentalsystem.repository.BookingRepository;
//import com.vehicalrentelsystem.vehicalrentalsystem.repository.UserRepository;
//import com.vehicalrentelsystem.vehicalrentalsystem.repository.VehicleRepository;
//import org.springframework.stereotype.Service;
//import com.vehicalrentelsystem.vehicalrentalsystem.model.Vehicle;
//
//import java.util.List;
//import java.util.stream.Collectors;
//
//
//@Service
//public class BookingService {
//
//    private final BookingRepository bookingRepository;
//    private final VehicleRepository vehicleRepository;
//    private final UserRepository userRepository;
//
//    public BookingService(BookingRepository bookingRepository, VehicleRepository vehicleRepository, UserRepository userRepository) {
//        this.bookingRepository = bookingRepository;
//        this.vehicleRepository = vehicleRepository;
//        this.userRepository = userRepository;
//    }
//
//    public BookingDTO createBooking(BookingDTO bookingDTO) {
//        User user = userRepository.findById(bookingDTO.getUserId())
//                .orElseThrow(() -> new RuntimeException("User not found"));
//
//        Vehicle vehicle = vehicleRepository.findById(bookingDTO.getVehicleId())
//                .orElseThrow(() -> new RuntimeException("Vehicle not found"));
//
//        Booking booking = new Booking();
//        booking.setUser(user);
//        booking.setVehicle(vehicle);
//        booking.setStartDate(bookingDTO.getStartDate());
//        booking.setEndDate(bookingDTO.getEndDate());
//        booking.setStatus(BookingStatus.CONFIRMED);
//
//        bookingRepository.save(booking);
//        return mapToDTO(booking);
//    }
//
//    public List<BookingDTO> getAllBookings() {
//        return bookingRepository.findAll().stream()
//                .map(this::mapToDTO)
//                .collect(Collectors.toList());
//    }
//    public List<BookingDTO> getUserBookings(Long userId) {
//        List<Booking> bookings = bookingRepository.findByUserId(userId);
//        return bookings.stream().map(this::mapToDTO).collect(Collectors.toList());
//    }
//
//    public void cancelBooking(Long bookingId) {
//        Booking booking = bookingRepository.findById(bookingId)
//                .orElseThrow(() -> new RuntimeException("Booking not found"));
//        booking.setStatus(BookingStatus.CANCELED);
//        bookingRepository.save(booking);
//    }
//
//
//    private BookingDTO mapToDTO(Booking booking) {
//        return new BookingDTO(
//                booking.getId(),
//                booking.getUser().getId(),
//                booking.getVehicle().getId(),
//                booking.getStartDate(),
//                booking.getEndDate(),
//                booking.getStatus().name()
//        );
//    }
//}
