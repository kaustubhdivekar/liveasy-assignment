package com.liveasy.assignment.controller;

import com.liveasy.assignment.dto.BookingDto; // Create this DTO
import com.liveasy.assignment.entity.Booking;
import com.liveasy.assignment.entity.Load; // Needed for setting loadId
import com.liveasy.assignment.service.BookingService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/booking") // Base path for booking APIs
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @Autowired
    private ModelMapper modelMapper; // Bean configured elsewhere

    // POST /booking → Create a new booking
    @PostMapping
    public ResponseEntity<BookingDto> createBooking(@Valid @RequestBody BookingDto bookingDto) {
        // Map DTO to Entity - Handle nested Load ID
        Booking bookingRequest = modelMapper.map(bookingDto, Booking.class);
        // Manually set the Load object with just the ID for the service layer to fetch
        Load loadRef = new Load();
        loadRef.setId(bookingDto.getLoadId()); // Assuming BookingDto has loadId field
        bookingRequest.setLoad(loadRef);

        Booking createdBooking = bookingService.createBooking(bookingRequest);
        BookingDto bookingResponse = modelMapper.map(createdBooking, BookingDto.class);
        // Ensure loadId is populated in the response DTO
        bookingResponse.setLoadId(createdBooking.getLoad().getId());
        return new ResponseEntity<>(bookingResponse, HttpStatus.CREATED);
    }

    // GET /booking → Fetch bookings (filter by shipperId, transporterId)
    @GetMapping
    public ResponseEntity<List<BookingDto>> getBookings(
            @RequestParam(required = false) String shipperId,
            @RequestParam(required = false) String transporterId) {
        List<Booking> bookings = bookingService.getBookings(shipperId, transporterId);
        List<BookingDto> bookingDtos = bookings.stream()
                .map(booking -> {
                    BookingDto dto = modelMapper.map(booking, BookingDto.class);
                    dto.setLoadId(booking.getLoad().getId()); // Populate loadId
                    return dto;
                })
                .collect(Collectors.toList());
        return ResponseEntity.ok(bookingDtos);
    }

    // GET /booking/{bookingId} → Get booking details
    @GetMapping("/{bookingId}")
    public ResponseEntity<BookingDto> getBookingById(@PathVariable UUID bookingId) {
        Booking booking = bookingService.getBookingById(bookingId);
        BookingDto bookingDto = modelMapper.map(booking, BookingDto.class);
        bookingDto.setLoadId(booking.getLoad().getId()); // Populate loadId
        return ResponseEntity.ok(bookingDto);
    }

    // PUT /booking/{bookingId} → Update booking details
    @PutMapping("/{bookingId}")
    public ResponseEntity<BookingDto> updateBooking(@PathVariable UUID bookingId, @Valid @RequestBody BookingDto bookingDto) {
        // Map DTO to Entity for update data - carefully map only allowed fields to update
        Booking bookingDetails = modelMapper.map(bookingDto, Booking.class);
        // Don't allow changing the associated Load via update
        bookingDetails.setLoad(null);

        Booking updatedBooking = bookingService.updateBooking(bookingId, bookingDetails);
        BookingDto bookingResponse = modelMapper.map(updatedBooking, BookingDto.class);
        bookingResponse.setLoadId(updatedBooking.getLoad().getId()); // Populate loadId
        return ResponseEntity.ok(bookingResponse);
    }

    // DELETE /booking/{bookingId} → Delete a booking
    @DeleteMapping("/{bookingId}")
    public ResponseEntity<HttpStatus> deleteBooking(@PathVariable UUID bookingId) {
        bookingService.deleteBooking(bookingId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}