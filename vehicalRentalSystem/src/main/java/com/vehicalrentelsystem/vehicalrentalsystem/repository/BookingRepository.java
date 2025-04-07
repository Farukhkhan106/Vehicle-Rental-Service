package com.vehicalrentelsystem.vehicalrentalsystem.repository;

import com.vehicalrentelsystem.vehicalrentalsystem.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByUserId(Long userId);

    List<Booking> findByVehicleId(Long vehicleId);

    @Query("SELECT b FROM Booking b WHERE b.vehicle.id = :vehicleId " +
            "AND b.status = 'CONFIRMED' " +
            "AND b.startDate <= :endDate " +
            "AND b.endDate >= :startDate")
    List<Booking> findOverlappingBookings(
            @Param("vehicleId") Long vehicleId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );
    @Query("SELECT b FROM Booking b WHERE b.vehicle.id = :vehicleId AND " +
            "(:startDate <= b.endDate AND :endDate >= b.startDate)")
    Optional<Booking> findFirstByVehicleIdAndDateRangeOverlap(
            @Param("vehicleId") Long vehicleId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);

}