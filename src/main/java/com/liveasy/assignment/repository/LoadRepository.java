package com.liveasy.assignment.repository;

import com.liveasy.assignment.entity.Load;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;

@Repository
public interface LoadRepository extends JpaRepository<Load, UUID> {

    // Custom query methods for filtering
    List<Load> findByShipperId(String shipperId);
    List<Load> findByTruckType(String truckType);
    // Add more findBy... methods as needed for other filter combinations
    // Or use Specifications API for dynamic filtering

    // Example of combined filtering (can get complex, Specifications might be better)
    List<Load> findByShipperIdAndTruckType(String shipperId, String truckType);
}