package com.vehicalrentelsystem.vehicalrentalsystem.service;

import com.vehicalrentelsystem.vehicalrentalsystem.dto.BookingDTO;
import com.vehicalrentelsystem.vehicalrentalsystem.dto.MyBookingDetailDTO;
import com.vehicalrentelsystem.vehicalrentalsystem.model.Booking;
import com.vehicalrentelsystem.vehicalrentalsystem.model.BookingStatus;
import com.vehicalrentelsystem.vehicalrentalsystem.model.User;
import com.vehicalrentelsystem.vehicalrentalsystem.repository.BookingRepository;
import com.vehicalrentelsystem.vehicalrentalsystem.repository.UserRepository;
import com.vehicalrentelsystem.vehicalrentalsystem.repository.VehicleRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import com.vehicalrentelsystem.vehicalrentalsystem.model.Vehicle;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final VehicleRepository vehicleRepository;
    private final UserRepository userRepository;

    public BookingService(BookingRepository bookingRepository, VehicleRepository vehicleRepository, UserRepository userRepository) {
        this.bookingRepository = bookingRepository;
        this.vehicleRepository = vehicleRepository;
        this.userRepository = userRepository;
    }

    public BookingDTO createBooking(BookingDTO bookingDTO, Authentication authentication) {
        String email = authentication.getName(); // 🧠 yeh email JWT se aayega
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Vehicle vehicle = vehicleRepository.findById(bookingDTO.getVehicleId())
                .orElseThrow(() -> new RuntimeException("Vehicle not found"));

        // 🚫 Prevent self-booking
        if (vehicle.getOwnerId().equals(user.getId())) {
            throw new RuntimeException("You cannot book your own vehicle.");
        }

        List<Booking> conflicts = bookingRepository.findOverlappingBookings(
                bookingDTO.getVehicleId(), bookingDTO.getStartDate(), bookingDTO.getEndDate()
        );

        if (!conflicts.isEmpty()) {
            throw new RuntimeException("Vehicle is already booked during the selected time.");
        }

        Booking booking = new Booking();
        booking.setUser(user); // ✅ login user set ho raha
        booking.setVehicle(vehicle);
        booking.setStartDate(bookingDTO.getStartDate());
        booking.setEndDate(bookingDTO.getEndDate());
        booking.setStatus(BookingStatus.CONFIRMED);
        booking.setOwnerId(vehicle.getOwnerId());

        long days = ChronoUnit.DAYS.between(bookingDTO.getStartDate(), bookingDTO.getEndDate()) + 1;
        booking.setTotalAmount(vehicle.getPricePerDay().doubleValue() * days);

        bookingRepository.save(booking);
        return mapToDTO(booking);
    }



    public List<BookingDTO> getAllBookings() {
        return bookingRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public List<BookingDTO> getUserBookings(Long userId) {
        return bookingRepository.findByUserId(userId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public void cancelBooking(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));
        booking.setStatus(BookingStatus.CANCELED);
        bookingRepository.save(booking);
    }

    public boolean isVehicleAvailable(Long vehicleId, LocalDate startDate, LocalDate endDate) {
        return bookingRepository.findOverlappingBookings(vehicleId, startDate, endDate).isEmpty();
    }

    public Optional<Booking> findOverlappingBooking(Long vehicleId, LocalDate startDate, LocalDate endDate) {
        return bookingRepository.findFirstByVehicleIdAndDateRangeOverlap(vehicleId, startDate, endDate);
    }
        private BookingDTO mapToDTO(Booking booking) {
            return new BookingDTO(
                    booking.getId(),
                    booking.getUser().getId(),
                    booking.getVehicle().getId(),
                    booking.getStartDate(),
                    booking.getEndDate(),
                    booking.getStatus().name(),

                    // ✅ Add this
                    booking.getTotalAmount()
            );
        }


        public List<MyBookingDetailDTO> getMyDetailedBookingsByUserId(Long userId) {
        return bookingRepository.findByUserId(userId).stream().map(booking -> {
            Vehicle v = booking.getVehicle();
            User owner = userRepository.findById(v.getOwnerId()).orElse(null);

            MyBookingDetailDTO dto = new MyBookingDetailDTO();
            dto.setBookingId(booking.getId());
            dto.setStartDate(booking.getStartDate());
            dto.setEndDate(booking.getEndDate());
            dto.setStatus(booking.getStatus().name());
            dto.setTotalAmount(booking.getTotalAmount());

            dto.setVehicleId(v.getId());
            dto.setBrand(v.getBrand());
            dto.setModel(v.getModel());
            dto.setNumber(v.getNumberPlate());
            dto.setPricePerDay(v.getPricePerDay().doubleValue());

            if (owner != null) {
                dto.setOwnerCity(owner.getCity());
                dto.setOwnerPhone(owner.getPhone());
                dto.setOwnerName(owner.getName());
            }

            String[] images = new com.google.gson.Gson().fromJson(v.getPhotosJson(), String[].class);
            if (images != null && images.length > 0) {
                dto.setImage(images[0]);
            }

            return dto;
        }).collect(Collectors.toList());
    }
}