package com.liveasy.assignment.repository;

import com.liveasy.assignment.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;

@Repository
public interface BookingRepository extends JpaRepository<Booking, UUID> {

    List<Booking> findByTransporterId(String transporterId);

    // Find bookings by shipper ID (requires joining with Load)
    @Query("SELECT b FROM Booking b JOIN b.load l WHERE l.shipperId = :shipperId")
    List<Booking> findByShipperId(String shipperId);

    // Find bookings by Load ID
    List<Booking> findByLoadId(UUID loadId);
}