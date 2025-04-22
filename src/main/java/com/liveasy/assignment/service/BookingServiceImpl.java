package com.liveasy.assignment.service;

import com.liveasy.assignment.entity.Booking;
import com.liveasy.assignment.entity.BookingStatus;
import com.liveasy.assignment.entity.Load;
import com.liveasy.assignment.entity.LoadStatus;
import com.liveasy.assignment.exception.BookingNotAllowedException;
import com.liveasy.assignment.exception.ResourceNotFoundException;
import com.liveasy.assignment.repository.BookingRepository;
import com.liveasy.assignment.repository.LoadRepository; // Needed to check Load status
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
// ... other imports




import java.util.List;
import java.util.UUID;





@Service
public class BookingServiceImpl implements BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private LoadRepository loadRepository; // Inject LoadRepository

    // Inject LoadService instead of LoadRepository for better separation
    // @Autowired
    // private LoadService loadService;

    @Override
    @Transactional // Involves reading Load and saving Booking + updating Load
    public Booking createBooking(Booking booking) {
        // Fetch the associated Load using loadId from the booking request DTO (passed in booking object)
        Load load = loadRepository.findById(booking.getLoad().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Load", "id", booking.getLoad().getId()));

        // Rule: A booking should not be created if the load is already CANCELLED.
        if (load.getStatus() == LoadStatus.CANCELLED) {
            throw new BookingNotAllowedException("Cannot create booking for a cancelled load (ID: " + load.getId() + ")");
        }
        // Rule: status should default to PENDING (handled by entity default)

        // Rule: When a booking is made (POST /booking), the load status should change to BOOKED.
        load.setStatus(LoadStatus.BOOKED);
        loadRepository.save(load); // Update load status

        booking.setLoad(load); // Ensure the managed Load entity is set
        booking.setStatus(BookingStatus.PENDING); // Explicitly set status
        return bookingRepository.save(booking);
    }

    @Override
    public List<Booking> getBookings(String shipperId, String transporterId) {
        // Implement filtering logic
        if (shipperId != null) {
            return bookingRepository.findByShipperId(shipperId);
        } else if (transporterId != null) {
            return bookingRepository.findByTransporterId(transporterId);
        } else {
            return bookingRepository.findAll();
        }
    }

    @Override
    public Booking getBookingById(UUID bookingId) {
        return bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking", "id", bookingId));
    }

    @Override
    @Transactional
    public Booking updateBooking(UUID bookingId, Booking bookingDetails) {
        Booking existingBooking = getBookingById(bookingId);

        // Typically, updates might only be for status (ACCEPT/REJECT) or comments/rate
        // Rule: When a booking is accepted, update the status to ACCEPTED.
        // Add logic here based on what fields are updatable in bookingDetails
        if (bookingDetails.getStatus() != null) {
             // Add validation: Can only move from PENDING to ACCEPTED/REJECTED?
             if (existingBooking.getStatus() == BookingStatus.PENDING &&
                 (bookingDetails.getStatus() == BookingStatus.ACCEPTED || bookingDetails.getStatus() == BookingStatus.REJECTED)) {
                 existingBooking.setStatus(bookingDetails.getStatus());
             } else if (existingBooking.getStatus() != bookingDetails.getStatus()) {
                 // Optional: Throw exception for invalid status transition
                  throw new BookingNotAllowedException("Invalid status transition for booking ID: " + bookingId);
             }
        }
        if (bookingDetails.getProposedRate() > 0) { // Example check
             existingBooking.setProposedRate(bookingDetails.getProposedRate());
        }
         if (bookingDetails.getComment() != null) {
            existingBooking.setComment(bookingDetails.getComment());
        }

        return bookingRepository.save(existingBooking);
    }

    @Override
    @Transactional // Involves deleting Booking and updating Load
    public void deleteBooking(UUID bookingId) {
        Booking booking = getBookingById(bookingId);
        Load load = booking.getLoad(); // Get associated load

        // Rule: If a booking is deleted, the load status should be CANCELLED.
        load.setStatus(LoadStatus.CANCELLED);
        loadRepository.save(load); // Update load status

        bookingRepository.delete(booking); // Delete the booking
    }
}