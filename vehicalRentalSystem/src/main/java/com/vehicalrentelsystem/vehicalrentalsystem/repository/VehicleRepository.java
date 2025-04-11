package com.vehicalrentelsystem.vehicalrentalsystem.repository;
import com.vehicalrentelsystem.vehicalrentalsystem.model.Vehicle;
import com.vehicalrentelsystem.vehicalrentalsystem.model.VehicleStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
    List<Vehicle> findByOwnerId(Long ownerId);
    List<Vehicle> findByStatus(VehicleStatus status);
    // VehicleRepository.java
    Optional<Vehicle> findById(Long id);


}
