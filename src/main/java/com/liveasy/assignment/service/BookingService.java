package com.liveasy.assignment.service;

import com.liveasy.assignment.entity.Booking;
import java.util.List;
import java.util.UUID;
// ... other imports

public interface BookingService {
    Booking createBooking(Booking booking);
    List<Booking> getBookings(String shipperId, String transporterId);
    Booking getBookingById(UUID bookingId);
    Booking updateBooking(UUID bookingId, Booking bookingDetails); // Primarily for status updates
    void deleteBooking(UUID bookingId);
}